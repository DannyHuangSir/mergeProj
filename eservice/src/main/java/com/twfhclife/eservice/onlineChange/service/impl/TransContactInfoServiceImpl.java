package com.twfhclife.eservice.onlineChange.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.controller.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransContactInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransContactInfoDtlDao;
import com.twfhclife.eservice.onlineChange.dao.TransContactInfoOldDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.util.ApConstants;

/**
 * 變更保單聯絡資料服務.
 * 
 * @author all
 */
@Service
public class TransContactInfoServiceImpl implements ITransContactInfoService {

	private static final Logger logger = LogManager.getLogger(TransContactInfoServiceImpl.class);
	
	@Autowired
	private ILilipmService lilipmService;

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransContactInfoDao transContactInfoDao;

	@Autowired
	private TransContactInfoDtlDao transContactInfoDtlDao;

	@Autowired
	private TransContactInfoOldDao transContactInfoOldDao;
	
	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	private ParameterDao parameterDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					}
				}
			}
		}
	}
	
	/**
	 * 查看现行网络资料
	 * @param policyNo 保單清單No
	 */
	@Override
	public HashMap getUserCurrentNetworkData(String policyNo) {
		return transDao.getUserCurrentNetworkData(policyNo);
	}
	
	/**
	 * 變更保單聯絡資料主檔-新增.
	 * 
	 * @param transContactInfoVo TransContactInfoVo
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public Map<String,Object> insertTransContactInfo(TransContactInfoVo transContactInfoVo,
			TransContactInfoDtlVo transContactInfoDtlVo, TransStatusHistoryVo hisVo) {
			String userId = transContactInfoDtlVo.getUserId();
			String fromCompanyId = transContactInfoDtlVo.getFormCompany();
			/** 線上申請-申請中狀態-已接受 */
			//	String status = OnlineChangeUtil.TRANS_STATUS_RECEIVED;
			/** 線上申請-申請中狀態-處理中 */
			//	String status = OnlineChangeUtil.TRANS_STATUS_PROCESSING;
				/** 線上申請-申請中狀態-已審核 */
				String status = OnlineChangeUtil.TRANS_STATUS_AUDITED;
			if(fromCompanyId != null && !OnlineChangeUtil.FROM_COMPANY_L01.equals(fromCompanyId)) {
				//判斷異常件
				if(OnlineChangeUtil.TRANS_STATUS_ABNORMAL.equals(transContactInfoDtlVo.getStatus())) {
					status = transContactInfoDtlVo.getStatus();
				}
			}
			int result = 0;
			//BigDecimal contactInfoBatchId = transContactInfoDao.getNextTransContactInfoBatchId();
			List<String> transNumList = new ArrayList<String>();
			try {
				// 新增保單號碼
				for (String policyNo : transContactInfoDtlVo.getPolicyNoList()) {

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("transNum", null);
					transDao.getTransNum(params);
					String transNum = params.get("transNum").toString();
					transNumList.add(transNum);
					// 新增線上申請主檔
					TransVo transVo = new TransVo();
					transVo.setTransNum(transNum);
					transVo.setTransDate(new Date());
					transVo.setTransType(TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
					transVo.setStatus(status);
					transVo.setUserId(userId);
					transVo.setCreateUser(userId);
					transVo.setCreateDate(new Date());
					transDao.insertTrans(transVo);

					//寫入狀態歷程
					hisVo.setTransNum(transNum);
					hisVo.setStatus(status);
					onlineChangeDao.addTransStatusHistory(hisVo);

					TransPolicyVo transPolicyVo = new TransPolicyVo();
					transPolicyVo.setTransNum(transNum);
					transPolicyVo.setPolicyNo(policyNo);
					transPolicyDao.insertTransPolicy(transPolicyVo);
	//			}

				// 新增變更保單聯絡資料主檔
				BigDecimal contactInfoId = transContactInfoDao.getNextTransContactInfoId();
				transContactInfoVo.setId(contactInfoId);
				transContactInfoVo.setTransNum(transNum);
				logger.info("transContactInfoVo:{}",transContactInfoVo);
				transContactInfoDao.insertTransContactInfo(transContactInfoVo);
					/***存储到ESERVICE.dbo.TRANS_CONTACT_INFO_FILEDATAS表中
					 以便往後ＵＩ上有顯示聯盟變更保單資料縮圖
					 			聯盟檔案要傳核心時亦要使用
					 */
				//INSERT INTO   ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDATAS
			    List<ContactInfoFileDataVo> fileDatas = transContactInfoVo.getFileDatas();
			    if (fileDatas!=null){
					if(fileDatas.size()>0) {
			    logger.info("-----------------START INSERT INTO  ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDATA:{}"+fileDatas.size()+"-----------------");
						for (ContactInfoFileDataVo fileData : fileDatas) {
							fileData.setContactSeqId(contactInfoId);
							logger.info("INSERT INTO  ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDATA  添加參數 :{}",fileData.toString() );
							Float aFloat = transContactInfoDao.selectTransContactInfoFileDatasId();
							logger.info("---------------selectTransContactInfoFileDatasId---------------     :{}",aFloat );
							fileData.setFdId(aFloat);
							String fileBase64 = fileData.getFileBase64();
							fileData.setFileBase64("");
							int i = transContactInfoDao.addTransContactInfoFileDatas(fileData);
							logger.info("---------------INSERT INTO  ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDAT---------------     響應行數:{}",i );
							fileData.setFileBase64(fileBase64);
							if (i>0) {
								int j = transContactInfoDao.updateTransContactInfoFileDatasFileBase64(fileData);
								logger.info("UPDATE   ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDATA  響應行數 :{}",j );
							}
						}
			    logger.info("-----------------END  INSERT INTO  ESERVICE.DBO.TRANS_CONTACT_INFO_FILEDATA:{}"+fileDatas.size()+"-----------------");
					}
			    }
					// 新增變更後保單聯絡資料
				transContactInfoDtlVo.setTransContactId(contactInfoId);
				transContactInfoDtlDao.insertTransContactInfoDtl(transContactInfoDtlVo);

				// 新增變更前保單聯絡資料
	//			for (String policyNo : transContactInfoDtlVo.getPolicyNoList()) {
					TransContactInfoOldVo transContactInfoOldVo = new TransContactInfoOldVo();
					transContactInfoOldVo.setTransContactId(contactInfoId);
					transContactInfoOldVo.setPolicyNo(policyNo);

					// 找出保單變更前的聯絡資料
					LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
					if (lilipmVo != null) {
						transContactInfoOldVo.setTelHome(lilipmVo.getNoHiddenLipmTelH());
						transContactInfoOldVo.setTelOffice(lilipmVo.getNoHiddenLipmTelO());
						transContactInfoOldVo.setAddress(lilipmVo.getLipmAddr());
						transContactInfoOldVo.setAddressFull(lilipmVo.getLipmAddrNoHidden());//modify 2021/10/27
						transContactInfoOldVo.setAddressCharge(lilipmVo.getLipmCharAddr());
						transContactInfoOldVo.setAddressFullCharge(lilipmVo.getLipmCharAddrNoHidden());//modify 2021/10/27
					}
					
					//transContactInfoOldVo取用LIPMDA_ES的MOBLIE,EMAIL
					String mobileOld = transContactInfoOldDao.getPmdaMobile(policyNo);
					String emailOld = transContactInfoOldDao.getPmdaEmail(policyNo);
					logger.debug("Old mobile,email : LIPMDA_ES mobileOld={}, emailOld={}", mobileOld, emailOld);
					
					if (mobileOld != null) {
						transContactInfoOldVo.setMobile(mobileOld);
					}
					if (emailOld != null) {
						transContactInfoOldVo.setEmail(emailOld);
					}
					
					/**
					UsersVo usersVo = hisVo.getUsersVo();
					String email = usersVo.getEmail();
					String mobile1 = usersVo.getMobile();
					logger.debug("獲取當前保戶email{},mobile1{}",email,mobile1);
					
					 if (email!=null) {
					 	transContactInfoOldVo.setEmail(email);
					 }
					 if (mobile1!=null) {
					 	transContactInfoOldVo.setMobile(mobile1);
					 }
					 **/
					
					 transContactInfoOldDao.insertTransContactInfoOld(transContactInfoOldVo);
					 logger.info("after insertTransContactInfoOld(),transContactInfoOldVo="+transContactInfoOldVo);
				}

				result = 1;
			} catch (Exception e) {
				result = 0;
				logger.error("Unable to init from insertTransContactInfo: {}", ExceptionUtils.getStackTrace(e));
				throw e;
			}
			Map<String,Object> rMap = new HashMap<String,Object>();
			rMap.put("result", result);
			rMap.put("status", status);
			rMap.put("transNums",transNumList.toString());
			//返回单号
			rMap.put("policyNo",transContactInfoDtlVo.getPolicyNoList().toString());
			return rMap;
	}
	
	/**
	 * 取得變更後保單聯絡資料申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public BigDecimal getTransContactInfoId(String transNum) {
		TransContactInfoVo transContactInfoVo = new TransContactInfoVo();
		transContactInfoVo.setTransNum(transNum);
		List<TransContactInfoVo> dataList = transContactInfoDao.getTransContactInfo(transContactInfoVo);
		
		BigDecimal transContactInfoId = null;
		if (!CollectionUtils.isEmpty(dataList)) {
			transContactInfoId = dataList.get(0).getId();
		}
		return transContactInfoId;
	}
	
	/**
	 * 變更後保單聯絡資料-明細查詢.
	 * 
	 * @param contactInfoId 變更保單聯絡資料主檔id
	 * @return 回傳查詢結果
	 */
	@Override
	public TransContactInfoDtlVo getTransContactInfoDtlDetail(BigDecimal contactInfoId) {
		TransContactInfoDtlVo qryVo = new TransContactInfoDtlVo();
		qryVo.setTransContactId(contactInfoId);
		List<TransContactInfoDtlVo> detailList = transContactInfoDtlDao.getTransContactInfoDtl(qryVo);
		
		TransContactInfoDtlVo detailVo = new TransContactInfoDtlVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
	
	/**
	 * 變更前保單聯絡資料-明細查詢.
	 * 
	 * @param contactInfoId 變更保單聯絡資料主檔id
	 * @return 回傳查詢結果
	 */
	@Override
	public TransContactInfoOldVo getTransContactInfoOldDetail(BigDecimal contactInfoId) {
		TransContactInfoOldVo qryVo = new TransContactInfoOldVo();
		qryVo.setTransContactId(contactInfoId);
		List<TransContactInfoOldVo> detailList = transContactInfoOldDao.getTransContactInfoOld(qryVo);
		
		TransContactInfoOldVo detailVo = new TransContactInfoOldVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}

	@Override
	public int getContactInfoCompleted(String rocId) {
		return onlineChangeDao.getContactInfoCompleted(rocId);
	}
	
	public Map<String,Object> getSendMailInfo(String status) {
		String statusName = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, status);
		String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.CONTACT_INFO_TRANS_REMARK);
		String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM, "security_alliance_twfhclife_adm");
		String[] mails = mailTo.split(";");
		Map<String,Object> rMap = new HashMap<String,Object>();
		List<String> receivers = new ArrayList<String>();
		if(mails.length > 0) {
			for (String mail : mails) {
				receivers.add(mail);
				logger.info("Mail Address : " + mail);
			}
		}
		
		rMap.put("statusName", statusName);
		rMap.put("transRemark", transRemark);
		rMap.put("receivers", receivers);
		return rMap;
	}
	public Map<String,Object> getSendMailInfo() {
		String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.CONTACT_INFO_TRANS_REMARK);
		String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM, "security_alliance_twfhclife_adm");
		String[] mails = mailTo.split(";");
		Map<String,Object> rMap = new HashMap<String,Object>();
		List<String> receivers = new ArrayList<String>();
		if(mails.length > 0) {
			for (String mail : mails) {
				receivers.add(mail);
				logger.info("Mail Address : " + mail);
			}
		}
		rMap.put("transRemark", transRemark);
		rMap.put("receivers", receivers);
		return rMap;
	}

	@Override
	public Map<String, Object> getCIOUserDetailInfoOld(String roc_id) {
		return transContactInfoDao.getCIOUserDetailInfoOld(roc_id);
	}

	@Override
	public Map<String, Object> getCIOUserDetailInfoNew(String roc_id) {
		return transContactInfoDao.getCIOUserDetailInfoNew(roc_id);
	}

	@Override
	public  List<TransContactInfoVo> getTransContactInfoTransNum(String transNum) {
		return transContactInfoDao.getTransContactInfoTransNum(transNum);
	}

	@Override
	public String getTransContactInfoTransNumCheck(String transNum) {

		if(transNum != null  && !"".equals(transNum)) {
			int i = transContactInfoDao.getTransContactInfoTransNumCheck(transNum);
			if (i==1) {
				return ReturnHeader.SUCCESS_CODE;
			}
		}
		return ReturnHeader.FAIL_CODE;
	}

	@Override
	public int updateFileBase64ByFileId(TransContactInfoFileDataVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getFileId()!=null && !StringUtils.isEmpty(vo.getFileBase64())) {
			rtn = transContactInfoDao.updateFileBase64ByFileId(vo);
		}
		return rtn;
	}

	@Override
	public int updateTransContactInfoCaseId(String caseId, String transNum) {
		return transContactInfoDao.updateTransContactInfoCaseId(caseId,transNum);
	}

	@Override
	public List<String> getTransContactInfoTransNumByBatchId(Float batchId) {
		return transContactInfoDao.getTransContactInfoTransNumByBatchId(batchId);
	}

	@Override
	public String getHistoryPolicyNo(String transNum) {
		List<String> transPolicyNoList = transPolicyDao.getTransPolicyNoList(transNum);
		String s="";
		if (transPolicyNoList!=null && transPolicyNoList.size()>0) {
			 s = transPolicyNoList.get(0);
		}
		return s;
	}


}