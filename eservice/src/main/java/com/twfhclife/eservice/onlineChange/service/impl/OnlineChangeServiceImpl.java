package com.twfhclife.eservice.onlineChange.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONArray;
import org.json.JSONObject;

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

	@Autowired
	private  TransMedicalTreatmentClaimDao transMedicalTreatmentClaimDao;
	
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
			//進行判斷是否為醫起通
			if (TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE.equals(transType)) {
				rfeId = transMedicalTreatmentClaimDao.getRefId(transNum);
				transMedicalTreatmentClaimDao.updateSatusByRefId(rfeId);
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
						fileData.setType("#");
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


				// 判斷是否線上申請-保單醫起通
				if (TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE.equals(transType)) {
					// 通過transNum獲取對象
					List<TransMedicalTreatmentClaimVo> transMedicalClaimVoList =  transMedicalTreatmentClaimDao.getTransInsuranceClaimByTransNum(transNum);
					if (transMedicalClaimVoList != null && transMedicalClaimVoList.size() > 0) {
						TransMedicalTreatmentClaimVo medicalClaimVo = transMedicalClaimVoList.get(0);
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
						TransMedicalTreatmentClaimFileDataVo fileData = new TransMedicalTreatmentClaimFileDataVo();
						fileData.setClaimSeqId(medicalClaimVo.getClaimSeqId());//獲取ClaimSeqId
						fileData.setFilePath(filepath);
						fileData.setFileName(fileName);
						fileData.setType("C");
						fileData.setRfeId(rfeId);
						fileData.setFileBase64(this.converFileToBase64Str(filepath+'/'+fileName));

						//获取文件的id编号
						Float aFloat = transMedicalTreatmentClaimDao.selectInsuranceClaimFileDataId();
						fileData.setFdId(aFloat);
						String fileBase64 = fileData.getFileBase64();
						logger.info("========保單醫起通:補件==========獲取檔案的ID編號=================={}", aFloat);
						fileData.setFileBase64("");
						int i = transMedicalTreatmentClaimDao.addInsuranceClaimFileData(fileData);
						logger.info("========保單醫起通:補件==========INSERT  InsuranceClaimFileData==================   響應行數----{}", i);
						fileData.setFileBase64(fileBase64);
						if (i>0) {
							int J = transMedicalTreatmentClaimDao.updateInsuranceClaimFileDataFileBase64(fileData);
							logger.info("========保單醫起通:補件==========UPDATE   InsuranceClaimFileData   FileBase64==================   響應行數----{}", J);
						}

					}
				}


				if (fileBytes.length > 0) {
					if (!TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE.equals(transType)) {
					onlineChangeDao.addTransFile(transNum, fileName, fileBytes, userId);
					}

					if (!TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE.equals(transType)) {
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
				rptUtils.txt(vo1.getTextContent(), 12, 1, 105, (float)(defaultX - (offset * (i + 1))));
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
		long limitSize = Long.parseLong(limitSizeStr) * 1000000;
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
	public void cancelMedicalTreatmentApplyTrans(String transNum, TransStatusHistoryVo hisVo) {
		onlineChangeDao.updateTransStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
		onlineChangeDao.addTransStatusHistory(hisVo);
	}

	@Override
	public List<TransStatusHistoryVo> getTransStatusHistoryList(TransStatusHistoryVo vo) {
		return onlineChangeDao.getTransStatusHistoryList(vo);
	}

	@Override
	public void cancelApplyTransInvestment(String transNum, TransStatusHistoryVo hisVo) {
		onlineChangeDao.updateTransStatus(transNum, OnlineChangeUtil.TRANS_STATUS_CANCEL);
		onlineChangeDao.addTransStatusHistory(hisVo);
	}

    @Override
    public int addTransStatusHistory(TransStatusHistoryVo hisVo) {
        return onlineChangeDao.addTransStatusHistory(hisVo);
    }

    @Override
	public List<TransRFEVo> getTransRFEList(TransRFEVo vo) {
		List<TransRFEVo> transRFEVos = onlineChangeDao.getTransRFEList(vo);
		for (int i = 0; i < transRFEVos.size(); i++) {
			TransRFEVo tVo = transRFEVos.get(i);

			List<TransInsuranceClaimFileDataVo> fileDataVoList = onlineChangeDao.getTransInsCliamFileData(tVo);
			for (TransInsuranceClaimFileDataVo fileDataVo : fileDataVoList) {
				//String filePath = fileDataVo.getFilePath()+"/"+fileDataVo.getFileName();
				String filePath = fileDataVo.getFilePath()+File.separator+fileDataVo.getFileName();
				fileDataVo.setFileBase64(this.fileDataToBase64Str(filePath));
			}
			tVo.setFileDatas(fileDataVoList);
		}
		return transRFEVos;
	}

	@Override
	public Map<String, String> checkMedicalBackList(BlackListVo blackListVo) {
		Map<String, String> rMap = new HashMap<String, String>();
		String detailInfo = onlineChangeDao.getMedicalBlackList(blackListVo);
		String errMsg = parameterDao.getParameterValueByCode( ApConstants.SYSTEM_ID, ApConstants.MEDICAL_BLACKLIST_ALERT01);
		rMap.put("detailInfo", detailInfo);
		rMap.put("errMsg", errMsg);
		return   rMap;
	}

	@Override
	public String getTransMedicalTreatmentClaimBySendAlliance(String transNum) {
		return onlineChangeDao.getTransMedicalTreatmentClaimBySendAlliance(transNum);
	}


	public String fileDataToBase64Str(String filePath) {
		String encodedString = null;
		try {
			if(filePath!=null) {
				logger.info("--------------------------------------------------input filePath="+filePath);
				File file = new File(filePath);
				long length = file.length();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				//pdf获取缩略图
				if(".pdf".equals(substring) || ".PDF".equals(substring)){
					logger.info("--------------------------------------------------input filePath pdf=>Image="+filePath);
					PDDocument doc = PDDocument.load(file);
					encodedString =this.imgBase64(doc,baos);
					if(encodedString!=null) {
						//do not print base64 string
						logger.info("Thumbnails  PDF=>img Base64  is not null.");
					}else {
						logger.info("Thumbnails  PDF=>img Base64   is null.");
					}
					doc.close();
				}else {
					//<=50KB
					if (length<=51200) {
						logger.info("--------------------------------------------------input filePath length<=51200{}"+filePath);
						encodedString =this.imgBase64(file);
						if(encodedString!=null) {
							//do not print base64 string
							logger.info("Thumbnails  Base64 length<=51200   encodedString is not null.");
						}else {
							logger.info("Thumbnails  Base64 length<=51200   encodedString  is null.");
						}
					}else{
						logger.info("--------------------------------------------------input filePath length>51200{}"+filePath);
						//进行抓取缩略图
						encodedString =this.imgBase64(file,baos);
						if(encodedString!=null) {
							//do not print base64 string
							logger.info("Thumbnails  Base64 length>51200   encodedString is not null.");
						}else {
							logger.info("Thumbnails  Base64 length>1200   encodedString  is null.");
						}
					}
				}
				logger.error("--------------------------------------------------Thumbnails  Base64  END");
			}
		}catch(Exception e) {
			logger.error("input filePath is null.");
			logger.error(e);
		}

		return encodedString;
	}
	/**
	 * 抓取PDF第一张转换为缩略图
	 * @param path   pdf 地址
	 * @return  缩略图
	 */
	private BufferedImage pdfBufferedImage(PDDocument doc ) {
		//File file = null;
		//PDDocument doc = null;
		PDFRenderer renderer = null;
		BufferedImage bufferedImage = null;
		try {
			//file = new File(path);
			//String imgPDFPath = file.getParent();
			//int dot = file.getName().lastIndexOf('.');
			//String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
			//doc = PDDocument.load(file);
			renderer = new PDFRenderer(doc);
			//int pageCount = doc.getNumberOfPages();
			List<BufferedImage> piclist = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				/* dpi越大转换后144越清晰，相对转换速度越慢 */
				BufferedImage image = renderer.renderImageWithDPI(i, 144);
				//ImageIO.write(image, "png", new File("));
				piclist.add(image);
			}
			bufferedImage = listBufferedImage(piclist);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
	/**
	 * 生产图片
	 * @param piclist
	 * @return
	 */
	private  BufferedImage listBufferedImage(List<BufferedImage> picList) {
		// 纵向处理图片
		if (picList == null || picList.size() <= 0) {
			logger.error("input filePath is null.图片数组为空!");
			return null;
		}
		try {
			int height = 0,
					width = 0,
					newHeight = 0,
					newLastHeight = 0,
					picNumber = picList.size();
			int[] heightArray = new int[picNumber];
			BufferedImage newImage = null;
			List<int[]> imgRGB = new ArrayList<int[]>();
			// 保存一张图片中的RGB数据
			int[] newImgRGB;
			for (int i = 0; i < picNumber; i++) {
				newImage = picList.get(i);
				heightArray[i] = newHeight = newImage.getHeight();
				if (i == 0) {
					width = newImage.getWidth();
				}
				height += newHeight;
				newImgRGB = new int[width * newHeight];
				newImgRGB = newImage.getRGB(0, 0, width, newHeight, newImgRGB, 0, width);
				imgRGB.add(newImgRGB);
			}
			newHeight = 0;
			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < picNumber; i++) {
				newLastHeight = heightArray[i];
				// 计算偏移高度
				if (i != 0) newHeight += newLastHeight;
				// 写入流中
				imageResult.setRGB(0, newHeight, width, newLastHeight, imgRGB.get(i), 0, width);
			}
			return imageResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * PPT转换图片50*50的base64数据
	 * @param filePath  PPT转换图片50*50地址
	 * @param baos
	 * @return  base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(PDDocument doc,ByteArrayOutputStream baos) throws IOException {
		BufferedImage bufferedImage = pdfBufferedImage(doc);
		Thumbnails.of(bufferedImage). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64= Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}
	/**
	 * 获取图片50*50的的base64数据
	 * @param file  获取图片50*50地址
	 * @param baos
	 * @return   base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(File file,ByteArrayOutputStream baos) throws IOException {
		Thumbnails.of(file). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64= Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}

	/**
	 * 获取小于51200的图片的base64数据
	 * @param file  小于51200的图片地址
	 * @return  base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String base64= Base64.getEncoder().encodeToString(fileContent);
		return  base64;
	}

	// 20211118 add by 203990	
	@Override
	public byte[] getEINPDF(String policyNo, String rocId) {
		String ENI_API_API008_URL = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "ENI_API_API008_URL");
				
		String ENI_API_API008_ACCESSTOKEN = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "ENI_API_API008_ACCESSTOKEN");
				
		String ENI_API_API009_URL = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "ENI_API_API009_URL");
				
		String ENI_API_API009_ACCESSTOKEN = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "ENI_API_API009_ACCESSTOKEN");

		byte[] pdfByte = null;
		try {
			/*
			javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
					  new javax.net.ssl.HostnameVerifier(){

					      public boolean verify(String hostname,
					             javax.net.ssl.SSLSession sslSession) {
					          return hostname.equals("localhost"); // or return true
					      }
					  });*/
			
			//ENI API008 取得資料
	        HttpsURLConnection.setDefaultHostnameVerifier(new OnlineChangeServiceImpl().new NullHostNameVerifier());
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			java.net.URL netUrl = new java.net.URL(ENI_API_API008_URL);
			java.net.HttpURLConnection httpConn=(java.net.HttpURLConnection)netUrl.openConnection();
		   //設定引數
		   httpConn.setDoOutput(true);//需要輸出
		   httpConn.setDoInput(true);//需要輸入
		   httpConn.setUseCaches(false);//不允許快取
		   httpConn.setRequestMethod("POST");//設定POST方式連線
		   //設定請求屬性
		   httpConn.setRequestProperty("Content-Type", "application/json");
		   //httpConn.setRequestProperty("Connection", "Keep-Alive");// 維持長連線
		   httpConn.setRequestProperty("Charset", "UTF-8");
		   httpConn.setRequestProperty("Access-Token", ENI_API_API008_ACCESSTOKEN);
		   //httpConn.setRequestProperty("call_user",unParams.get("call_user") );
		   //連線
		   httpConn.connect();
		   //建立輸入流，向指向的URL傳入引數
		   java.io.DataOutputStream dos = new java.io.DataOutputStream(httpConn.getOutputStream());

		   //test
		   //rocId = "S202340416";
		   //policyNo = "GQ10400020";
		   String raw_josn = "{\"askIdno\":\"" + rocId + "\",\"insNo\":\"" + policyNo + "\"}";
		   
		   dos.write(raw_josn.getBytes("UTF-8"));
		   dos.flush();
		   dos.close();
		   //獲得響應狀態
		   BufferedReader br = null;
		   String api008Result = "";
		   if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 399) {
		       br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		       api008Result = br.lines().collect(Collectors.joining());
		   } else {
		       br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
		       logger.error("Unable to getEINPDF: ENI_API_API008 http error ", br.toString());
		   }
		   
		   // 解析 api008Result , 取得 fileId
		   /*
			{
			   "msg":"SUCCESS",
			   "code":"0",
			   "data":[
			      {
			         "insNo":"20201120_02-01_0001",
			         "insSource":"01",
			         "insCode":"000001",
			         "step":"9",
			         "askIdno":"B123000001",
			         "askName":"我是要保人姓名",
			         "fileId":"202111271639-XXXXX"
			      },
			      {
			         "insNo":"20201120_02-01_0001",
			         "insSource":"02",
			         "insCode":"000001",
			         "step":"9",
			         "askIdno":"B123000001",
			         "askName":"我是要保人姓名",
			         "fileId":"202111271639-YYYYY"
			      }
			   ]
			}		    
		    */
	        JSONObject obj = new JSONObject(api008Result);
	        String api008Result_code = obj.getString("code");
	        if (! "0".equals(api008Result_code)) {
			    logger.error("Unable to getEINPDF: ENI_API_API008 response error code ", api008Result_code);
	        	return pdfByte;
	        }

	        JSONArray arr = obj.getJSONArray("data");
	        String fileId = arr.getJSONObject(0).getString("fileId");
	        for (int i = 0; i < arr.length(); i++) {
				if ("01".equals(arr.getJSONObject(i).getString("insSource"))) {
					fileId = arr.getJSONObject(i).getString("fileId");
					break;
				}
	        }
		   

	        //ENI API009 取得資料
	        netUrl = new java.net.URL(ENI_API_API009_URL);
		    httpConn=(java.net.HttpURLConnection)netUrl.openConnection();
		   //設定引數
		   httpConn.setDoOutput(true);//需要輸出
		   httpConn.setDoInput(true);//需要輸入
		   httpConn.setUseCaches(false);//不允許快取
		   httpConn.setRequestMethod("POST");//設定POST方式連線
		   //設定請求屬性
		   httpConn.setRequestProperty("Content-Type", "application/json");
		   //httpConn.setRequestProperty("Connection", "Keep-Alive");// 維持長連線
		   httpConn.setRequestProperty("Charset", "UTF-8");
		   httpConn.setRequestProperty("Access-Token", ENI_API_API009_ACCESSTOKEN);
		   //httpConn.setRequestProperty("call_user",unParams.get("call_user") );
		   //連線
		   httpConn.connect();
		   //建立輸入流，向指向的URL傳入引數
		   dos = new java.io.DataOutputStream(httpConn.getOutputStream());

		   raw_josn = "{\"fileId\":\"" + fileId + "\"}";
		   dos.write(raw_josn.getBytes("UTF-8"));
		   dos.flush();
		   dos.close();
		   //獲得響應狀態
		   br = null;
		   String api009Result = "";
		   if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 399) {
		       br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		       api009Result = br.lines().collect(Collectors.joining());
		   } else {
		       br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
		       logger.error("Unable to getEINPDF: ENI_API_API009 http error ", br.toString());
		       return pdfByte;
		   }
		   
		   // 解析 api009Result , 取得 b64Pdf
		   /*
			{
			  "code": "0",
			  "msg": "success",
			  "data": {
			    "b64Pdf": "JFGJTVGL:OJUYG651Dcce7fgeUGLklm424reger...",
			    "fileName": "no_sign.pdf"
			  }
			}
		    */
	        obj = new JSONObject(api009Result);
	        String api009Result_code = obj.getString("code");
	        if (! "0".equals(api009Result_code)) {
			    logger.error("Unable to getEINPDF: ENI_API_API009 response error code ", api009Result_code);
	        	return pdfByte;
	        }

	        String b64Pdf = obj.getJSONObject("data").getString("b64Pdf");
		   
			pdfByte = Base64.getDecoder().decode(b64Pdf);
		} catch (Exception e) {
			logger.error("Unable to getEINPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}

	// 20211118 add by 203990
	@Override
	public String getUserIdByPolicyNo(String policyNo) {
		return onlineChangeDao.getUserIdByPolicyNo(policyNo);
	}

	// 20211118 add by 203990
    static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    } };

	// 20211118 add by 203990
    public class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         * 
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
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
