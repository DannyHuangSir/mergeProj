package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 線上申請主檔服務.
 * 
 * @author all
 */
@Service
public class TransServiceImpl implements ITransService {
	
	private static final Logger logger = LogManager.getLogger(TransServiceImpl.class);
	
	@Autowired
	public TransDao transDao;
	
	@Autowired
	public TransPolicyDao transPolicyDao;
	
	/**
	 * 線上申請-新增.
	 * 
	 * @param transVo TransVo
	 * @return 回傳影響筆數
	 */
	public int insertTrans(TransVo transVo) {
		int rtn = -1;
		if(transVo!=null) {
			rtn = transDao.insertTrans(transVo);
		}
		return rtn;
	}

	/**
	 * 取線上申請序號.
	 */
	@Override
	public synchronized String getTransNum() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("transNum", null);
		transDao.getTransNum(params);
		return params.get("transNum").toString();
	}

	/**
	 * 線上申請保單驗證.
	 * 
	 * @param policyNo 保單號碼
	 * @param transType 申請類型
	 * @return 回傳驗證結果
	 */
	@Override
	public VerifyPolicyResult verifyPolicyRule(String policyNo, String transType) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("policyNo", policyNo);
		params.put("transType", transType);
		params.put("verifyResult", null);
		params.put("verifyMsg", null);
		params.put("optionList", null);//年金給付方式

		VerifyPolicyResult verifyPolicyResult = new VerifyPolicyResult();
		try {
			transDao.verifyPolicyRule(params);
			if (params.get("verifyResult") != null) {
				verifyPolicyResult.setVerifyResult(params.get("verifyResult").toString());
			}
			if (params.get("verifyMsg") != null) {
				verifyPolicyResult.setVerifyMsg(params.get("verifyMsg").toString());
			}
			if (params.get("optionList") != null) {
				String[] options = params.get("optionList").toString().split(",");
				List<String> optiionList = new ArrayList<>();
				for (String option : options) {
					optiionList.add(option);
				}
				verifyPolicyResult.setOptiionList(optiionList);
			}
			
			if(params!=null && !params.isEmpty()) {//for log.
				logger.debug(com.twfhclife.generic.util.MyJacksonUtil.object2Json(params));
			}
			
		} catch (Exception e) {
			logger.error("invoke usp_verifyPolicyResult error : {}", ExceptionUtils.getStackTrace(e));
			verifyPolicyResult.setVerifyResult("N");
		}		
		
		return verifyPolicyResult;
	}

	/**
	 * 檢查是否已經申請.
	 * 
	 * @param policyNo 保單號碼
	 * @param transType 申請類型
	 * @param transStatus 申請狀態
	 * @return 回傳是否已經申請
	 */
	@Override
	public boolean isTransApplyed(String policyNo, String transType, String transStatus) {
		int cnt = transDao.isTransApplyed(policyNo, transType, transStatus);
		return (cnt > 0);
	}
	
	/**
	 * 處理保單過投保終期狀態鎖定.
	 * 
	 * @param policyList 保單清單資料
	 * @return 回傳處理過的保單清單資料
	 */
	@Override
	public List<PolicyListVo> handlePolicyStatusExpiredLocked(List<PolicyListVo> policyList) {
		List<PolicyListVo> handledPolicyList = new ArrayList<>();
		
		// 先根據保單到期時間設定鎖定註記
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				// policyList 有可能會從session傳來，需要複製操作，避免影響來源
				PolicyListVo newVo =  SerializationUtils.clone(vo);
				if ("Y".equals(newVo.getExpiredFlag())) {
					newVo.setApplyLockedFlag("Y");
					// 此張保單已過投保終期
					newVo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
				} else {
					newVo.setApplyLockedFlag("N");
					newVo.setApplyLockedMsg("");
				}
				handledPolicyList.add(newVo);
			}
		}
		return handledPolicyList;
	}
	
	/**
	 * 處理全域通用型的狀態鎖定，限定保單狀態不能申請(個別申請類型的限制，請寫在各自的服務內).
	 * 
	 * @param policyList 保單清單資料
	 * @param userId 使用者ID
	 * @param transType 申請類型
	 * @return 回傳處理過的保單清單資料
	 */
	@Override
	public List<PolicyListVo> handleGlobalPolicyStatusLocked(List<PolicyListVo> policyList, String userId, String transType) {
		List<PolicyListVo> handledPolicyList = new ArrayList<>();
		
		// 先根據保單到期時間設定鎖定註記
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				// policyList 有可能會從session傳來，需要複製操作，避免影響來源
				PolicyListVo newVo =  SerializationUtils.clone(vo);
				
				if ("Y".equals(newVo.getExpiredFlag())) {
					newVo.setApplyLockedFlag("Y");
					// 此張保單已過投保終期
//					newVo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
					appendMsg(newVo,OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
				} 
//				else {
//					newVo.setApplyLockedFlag("N");
//					newVo.setApplyLockedMsg("");
//				}
				handledPolicyList.add(newVo);
				
				if(newVo!=null) {//only for log
					System.out.println(com.twfhclife.generic.util.MyJacksonUtil.object2Json(newVo));
				}
			}
		}
		
		// 判斷是否有前一次申請
		if (!CollectionUtils.isEmpty(handledPolicyList) && !StringUtils.isEmpty(userId)) {
			// 取得目前申請中的保單號碼
			List<String> applyPolicyNos = transPolicyDao.getProgressPolicyNoList(userId, transType);
			if (!CollectionUtils.isEmpty(applyPolicyNos)) {
				for (PolicyListVo vo : handledPolicyList) {
					// 保單未到期但已經有在申請中
//					if ("N".equals(vo.getExpiredFlag())) {
						if (applyPolicyNos.contains(vo.getPolicyNo())) {
							vo.setApplyLockedFlag("Y");
							// 前次申請尚在處理中，暫無法進行此項申請
//							vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
							appendMsg(vo,OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
					}
//					}
				}
			}
		}
		
		return handledPolicyList;
	}
	
	private void appendMsg(PolicyListVo policyListVo, String msg) {
		// TODO Auto-generated method stub
		if(policyListVo.getApplyLockedMsg() != null) {
			String eStr = policyListVo.getApplyLockedMsg();
			if(!eStr.contains(msg)) {
				policyListVo.setApplyLockedMsg(eStr + "|" + msg);
			}
		}else {
			policyListVo.setApplyLockedMsg(msg);
		}
		
	}
	
	/**
	 * 呼叫 SP verifyPolicyRule 驗證是否可以申請.
	 * 
	 * @param policyList 保單清單資料
	 * @param transType 申請類型
	 * @return 回傳處理過的保單清單資料
	 */
	@Override
	public List<PolicyListVo> handleVerifyPolicyRuleStatusLocked(List<PolicyListVo> policyList, String transType) {
		// 若保單狀態是可以申請，在呼叫一次 verifyPolicyRule 驗證是否可以申請
		for (PolicyListVo vo : policyList) {
			String policyNo = vo.getPolicyNo();
			if (!"Y".equals(vo.getApplyLockedFlag())) {
				VerifyPolicyResult verifyPolicyResult = verifyPolicyRule(policyNo, transType);
				
//				if(verifyPolicyResult!=null) {//for log.
//					logger.debug("verifyPolicyResult="+com.twfhclife.generic.util.MyJacksonUtil.object2Json(vo));
//				}
				
				if ("N".equals(verifyPolicyResult.getVerifyResult())) {
					vo.setApplyLockedFlag("Y");
					vo.setApplyLockedMsg(verifyPolicyResult.getVerifyMsg());
				}
				
				if (TransTypeUtil.RENEW_PARAMETER_CODE.equals(transType)) {
					//展期定期保險
					if ("Y".equals(verifyPolicyResult.getVerifyResult())) {
						vo.setCanRenew("Y");
					}
				} else if (TransTypeUtil.REDUCE_PARAMETER_CODE.equals(transType)) {
					//減額繳清保險
					if ("Y".equals(verifyPolicyResult.getVerifyResult())) {
						vo.setCanReduce("Y");
					}
				} else if(TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE.equals(transType)) {
					if("Y".equals(verifyPolicyResult.getVerifyResult())
							&& !verifyPolicyResult.getOptiionList().isEmpty()) {
						String[] arrayOption = verifyPolicyResult.getOptiionList().get(0).split("=");
						vo.setBeneficiaryName(arrayOption[0]);
						PolicyPayerVo policyPayerVo= new PolicyPayerVo();
						policyPayerVo.setPayerName(arrayOption[1]);
						vo.setPolicyPayerVo(policyPayerVo);
					}
				}
			}
			
			if(vo!=null) {//for log.
				logger.debug("handleVerifyPolicyRuleStatusLocked="+com.twfhclife.generic.util.MyJacksonUtil.object2Json(vo));
			}
		}
		return policyList;
	}

	@Override
	public Float getBatchIdSequence() {
		// TODO Auto-generated method stub
		return transDao.getBatchIdSequence();
	}

    @Override
    public Date getLastCompleteTime(String transType, String userId) {
        return transDao.getLastCompleteTime(transType, userId);
    }

    @Override
    public int updateTransStatus(String transNum, String status) {
        return transDao.updateTransStatus(transNum, status);
    }

    @Override
    public int checkTransInSignProcess(String transNum) {
        return transDao.countInSignTrans(transNum);
    }
}