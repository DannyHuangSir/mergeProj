package com.twfhclife.eservice.onlineChange.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransEndorsementDao;
import com.twfhclife.eservice.onlineChange.dao.TransInsuranceClaimDao;
import com.twfhclife.eservice.onlineChange.model.BlackListVo;
import com.twfhclife.eservice.onlineChange.model.TableGetVo;
import com.twfhclife.eservice.onlineChange.model.TransEndorsementVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransPolicyVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.eservice.onlineChange.service.ITravelPolicyService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.generic.api_model.CommLogRequest;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.service.impl.BaseServiceImpl;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.HttpUtil;
import com.twfhclife.generic.util.RptUtils;

@Service
public class OnlineChangeServiceImpl extends BaseServiceImpl implements IOnlineChangeService {
	
	private static final Logger logger = LogManager.getLogger(OnlineChangeServiceImpl.class);

	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	public TransDao transDao;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private TransEndorsementDao transEndorsementDao;
	
	@Autowired
	private TransInsuranceClaimDao transInsuranceClaimDao;
	
	/** 各功能service */
	
	//旅平險
	@Autowired
	private ITravelPolicyService travelPolicyService;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Value("${upload.file.save.path}")
	private String FILE_SAVE_PATH;
	
	/**
	 * 我的申請
	 */
	@Override
	public List<TransVo> getTransByUserId(String userId, String status, String transType, 
			String policyNo, String startDate, String endDate, int pageNum, int pageSize){
		List<TransVo> trans = onlineChangeDao.getTransByUserId(userId, status, transType, policyNo, startDate, endDate, pageNum, pageSize);
		for(TransVo tran : trans){
			List<TransPolicyVo> policys = onlineChangeDao.getTransPolicy(tran.getTransNum());
			tran.setPolicys(policys);
		}
		return trans;
	}
	
	/**
	 * 取得詳細畫面資料
	 * @param transType
	 * @return
	 */
	@Override
	public TableGetVo getTransDetail(String transType, String transNum, String userId) {
		TableGetVo tableGet = new TableGetVo();
		//旅平險
		if(transType.equals(TransTypeUtil.TRAVEL_POLICY_PARAMETER_CODE)){
			tableGet = travelPolicyService.getTransDetail(transType, transNum);
		}else{
			logger.info("線上申請類別錯誤! transType = "+transType);
		}
		return tableGet;
	}

	@Transactional
	@Override
	public void cancelApplyTrans(String transNum, TransStatusHistoryVo hisVo){
		onlineChangeDao.updateTransStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
		onlineChangeDao.updateInsClaimStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
		onlineChangeDao.addTransStatusHistory(hisVo);
	}
	
	@Override
	public void cancelInsClaim(String transNum){
		onlineChangeDao.updateInsClaimStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
	}
	
	@Override
	public void beAddedTrans(String transNum){
		onlineChangeDao.updateTransStatus(transNum, OnlineChangeUtil.TRANS_STATUS_PROCESSING);
	}
	
	@Override
	public void addTransFile(String transNum, List<MultipartFile> uploadFiles, String userId) throws Exception {
		try {
			Float rfeId = null;
			String transType = transInsuranceClaimDao.getTransTypeByTransNum(transNum);
			if (TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE.equals(transType)) {
				rfeId = transInsuranceClaimDao.getRefId(transNum);
				transInsuranceClaimDao.updateSatusByRefId(rfeId);
			}
			List<File> mailFiles = new ArrayList<>();
			for (MultipartFile transFile : uploadFiles) {
				String fileName = transFile.getOriginalFilename();
				byte[] fileBytes = transFile.getBytes();
				// 判斷是否線上申請-保單理賠
				if (TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE.equals(transType)) {
					// 通過transNum獲取TransInsuranceClaim對象
					List<TransInsuranceClaimVo> transInsuranceClaimVoList = transInsuranceClaimDao.getTransInsuranceClaimByTransNum(transNum);
					if (transInsuranceClaimVoList != null && transInsuranceClaimVoList.size() > 0) {
						TransInsuranceClaimVo transInsuranceClaimVo = transInsuranceClaimVoList.get(0);
						// 上传文件
						String filepath = FILE_SAVE_PATH;
			        	File localFile = new File(filepath);
			            if (!localFile.exists()) {
			                localFile.mkdirs();
			            }
		            	File server_file = new File(filepath + fileName);
		            	if (server_file.exists()) {
		                    SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		            		fileName = fileName.split("\\.")[0]+fmdate.format(new Date())+"."+fileName.split("\\.")[1];
		            		server_file = new File(filepath + fileName);
		            	}
		            	transFile.transferTo(server_file);
		            	TransInsuranceClaimFileDataVo fileData = new TransInsuranceClaimFileDataVo();
		            	fileData.setClaimSeqId(transInsuranceClaimVo.getClaimSeqId());//獲取ClaimSeqId
						fileData.setFilePath(filepath);
						fileData.setFileName(fileName);
						fileData.setType("C");
						fileData.setRfeId(rfeId);
						fileData.setFileBase64(this.converFileToBase64Str(filepath+'/'+fileName));

						//获取文件的id编号
						Float aFloat = transInsuranceClaimDao.selectInsuranceClaimFileDataId();
						fileData.setFdId(aFloat);
						String fileBase64 = fileData.getFileBase64();
						logger.info("========保單理賠:補件==========獲取檔案的ID編號=================={}", aFloat);
						fileData.setFileBase64("");
						int i = transInsuranceClaimDao.addInsuranceClaimFileData(fileData);
						logger.info("========保單理賠:補件==========INSERT  InsuranceClaimFileData==================   響應行數----{}", i);
						fileData.setFileBase64(fileBase64);
						if (i>0) {
							int J = transInsuranceClaimDao.updateInsuranceClaimFileDataFileBase64(fileData);
							logger.info("========保單理賠:補件==========UPDATE   InsuranceClaimFileData   FileBase64==================   響應行數----{}", J);
						}

					}
				}
				
				if (fileBytes.length > 0) {
					if (!TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE.equals(transType)) {
					onlineChangeDao.addTransFile(transNum, fileName, fileBytes, userId);
					}
					File sendFile = new File(fileName);
					FileUtils.writeByteArrayToFile(sendFile, fileBytes);
					mailFiles.add(sendFile);
				}
			}
			
			String content = "";
			String subject = "";
			String email = "";
			
			// 補件 email 附件通知
			// 線上登打申請書，系統產生為PDF檔提供客戶下載，簽名後連同身分證及存簿上傳系統，電子郵件(申請帳號)通知契服life108@twfhclife.com.tw
			com.twfhclife.eservice.web.model.TransVo transVo = transDao.findByTransNum(transNum);
			if (transVo != null && TransTypeUtil.MATURITY_PARAMETER_CODE.equals(transVo.getTransType())) {
				content = parameterDao.getParameterValueByCode("eservice", "MATURITY_MAIL_CONTENT")
						.replace("TRANS_NUM", transVo.getTransNum());
				subject = parameterDao.getParameterValueByCode("eservice", "MATURITY_MAIL_SUBJECT")
						.replace("TRANS_NUM", transVo.getTransNum());
				email = parameterDao.getParameterValueByCode("eservice", "MATURITY_MAIL_ADDR");
			} else if (transVo != null && TransTypeUtil.LOAN_PARAMETER_CODE.equals(transVo.getTransType())) {
				content = parameterDao.getParameterValueByCode("eservice", "LOAN_MAIL_CONTENT")
						.replace("TRANS_NUM", transVo.getTransNum());
				subject = parameterDao.getParameterValueByCode("eservice", "LOAN_MAIL_SUBJECT")
						.replace("TRANS_NUM", transVo.getTransNum());
				email = parameterDao.getParameterValueByCode("eservice", "LOAN_MAIL_ADDR");
			} else {
				return;
			}
			logger.debug("addTransFile:email=" + email + ", subject=" + subject + ", content=" + content);
			mailService.sendMail(content, subject, email, "", mailFiles);
			try {
				String url = parameterDao.getParameterValueByCode("eservice_batch", "COMM_LOG_ADD_URL");
				String accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
				new HttpUtil().postCommLogAdd(url, accessKey, new CommLogRequest(ApConstants.SYSTEM_ID, "email", email, content));
			} catch (Exception e) {
				logger.error("Unable to postCommLogAdd(email) in OnlineChangeServiceImpl: {}", ExceptionUtils.getStackTrace(e));
			}
			
			for (File sendFile : mailFiles) {
				FileUtils.forceDelete(sendFile);
			}
		} catch (Exception e) {
			logger.error("Unable to addTransFile: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
	}
	
	@Override
	public byte[] getEndorsementPDF(String transNum, String rocId) {
		byte[] pdfByte = null;
		try {
			//get lilipm
			LilipmVo lipm = lilipmService.findByRocId(rocId);
			
			//prepare pdf data
			RptUtils rptUtils = new RptUtils("endorsement.pdf");

			// 要保人
			rptUtils.txt(lipm == null ? "" : lipm.getLipmName1(), 12, 1, 125, 696);
			
			//批註書內容
			List<TransEndorsementVo> list = transEndorsementDao.getTransEndorsementByTransNum(transNum);
			Collections.sort(list, new Comparator<TransEndorsementVo>() {
			    @Override
			    public int compare(TransEndorsementVo v1, TransEndorsementVo v2) {
			    	return String.format("%02d", Integer.parseInt(v1.getTextOrder())).compareTo(String.format("%02d", Integer.parseInt(v2.getTextOrder())));
			    }
			});
			
			int offset = 15;
			int defaultX = 500;
			for (int i=0; i<list.size(); i++) {
				TransEndorsementVo vo1 = list.get(i);
				rptUtils.txt(vo1.getTextContent(), 12, 1, 105, defaultX - (offset * (i + 1)));
			}
			
			pdfByte = rptUtils.toPdf();
		} catch (Exception e) {
			logger.error("Unable to getEndorsementPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}

	@Override
	public String getUserIdByTransNum(String transNum, String transType) {
		return onlineChangeDao.getUserIdByTransNum(transNum, transType);
	}

	@Override
	public boolean checkFileSize(String transNum, List<MultipartFile> uploadFiles, String limitSizeStr) {
		long totalSize = 0;
		long limitSize = Integer.parseInt(limitSizeStr) * 1000000;
		for (MultipartFile file : uploadFiles) {
			if (file.getSize() > limitSize) {
				return false;
			}
			totalSize += file.getSize();
		}
		boolean isAllow = totalSize < limitSize;
		if (!isAllow) {
			logger.debug("upload files over limit:transNum={}, total size={}, limit size={}", transNum, totalSize, limitSize);
		}
		return isAllow;
	}

	@Override
	public Map<String, String> checkBackList(BlackListVo blackListVo) {
		Map<String, String> rMap = new HashMap<String, String>();
		String detailInfo = onlineChangeDao.getBlackList(blackListVo);
		String errMsg = parameterDao.getParameterValueByCode( ApConstants.SYSTEM_ID, ApConstants.INSURANCEC_CLAIM_BLACKLIST_ALERT01);
		rMap.put("detailInfo", detailInfo);
		rMap.put("errMsg", errMsg);
		return rMap;
	}

	@Override
	public void cancelApplyTransConversion(String transNum, TransStatusHistoryVo hisVo) {
		onlineChangeDao.updateTransStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
		onlineChangeDao.addTransStatusHistory(hisVo);
	}

	public String converFileToBase64Str(String filePath) {
		String encodedString = null;
		try {
			if(filePath!=null) {
				logger.info("input filePath="+filePath);
				
				byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			}else {
				logger.error("input filePath is null.");
			}
		}catch(Exception e) {
			logger.error(e);
		}
		
		return encodedString;
	}
}
