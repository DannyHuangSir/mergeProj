package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.BankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransBankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.dao.TransPaymethodDao;
import com.twfhclife.eservice.onlineChange.model.BankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransBankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;
import com.twfhclife.eservice.onlineChange.service.ITransPaymethodService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.dao.PolicyListDao;
import com.twfhclife.eservice.policy.dao.PolicyPayerDao;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyPayerVo;
import com.twfhclife.eservice.policy.model.PolicyVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 變更收費管道服務.
 * 
 * @author all
 */
@Service
public class TransPaymethodServiceImpl implements ITransPaymethodService {

	private static final Logger logger = LogManager.getLogger(TransPaymethodServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransPaymethodDao transPaymethodDao;

	@Autowired
	private TransBankInfoDao transBankInfoDao;
	
	@Autowired
	private BankInfoDao bankInfoDao;
	
	@Autowired
	private PolicyPayerDao policyPayerDao;
	
	@Autowired
	private PolicyListDao policyListDao;

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
	 * 變更收費管道-查詢.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransPaymethodVo> getTransPaymethod(TransPaymethodVo transPaymethodVo) {
		return transPaymethodDao.getTransPaymethod(transPaymethodVo);
	}

	/**
	 * 變更收費管道-新增.
	 * 
	 * @param transPaymethodVo TransPaymethodVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransPaymethod(TransPaymethodVo transPaymethodVo) {
		String transNum = transPaymethodVo.getTransNum();
		String userId = transPaymethodVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.PAYMETHOD_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transPaymethodVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更收費管道
			for (String policyNo : transPaymethodVo.getPolicyNoList()) {
				// 若為信用卡，找出舊的信用卡資料
				if ("9".equals(transPaymethodVo.getCardType())) {
					PolicyPayerVo policyPayerVo = policyPayerDao.getPolicyPayer(policyNo);
					if (policyPayerVo != null) {
						String expireDate = policyPayerVo.getExpireDate();
						if (!StringUtils.isEmpty(expireDate)) {
							String validYyOld = null;
							String validMmOld = null;
							if (expireDate.indexOf("/") != -1) {
								String[] date = expireDate.split("/");
								validYyOld = date[0];
								validMmOld = date[1];
							} else if (expireDate.length() >= 4) {
								validYyOld = expireDate.substring(0, 2);
								validMmOld = expireDate.substring(2, 4);
							}
							
							transPaymethodVo.setValidYyOld(validYyOld);
							transPaymethodVo.setValidMmOld(validMmOld);
						}
						
						transPaymethodVo.setCardNoOld(policyPayerVo.getAcctNumber());
					} else {
						transPaymethodVo.setCardNoOld(null);
						transPaymethodVo.setValidYyOld(null);
						transPaymethodVo.setValidMmOld(null);
					}
				}
				
				// 找出舊的支付工具
				PolicyVo policyVo = policyListDao.findById(policyNo);
				if (policyVo != null) {
					transPaymethodVo.setPaymethodOld(policyVo.getPaymentMethod());
				}
				transPaymethodVo.setPolicyNo(policyNo);
				
				transPaymethodDao.insertTransPaymethod(transPaymethodVo);
			}
			
			// 新增銀行帳戶資訊
			if ("6".equals(transPaymethodVo.getCardType())) {
				TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
				transBankInfoVo.setTransNum(transNum);
				transBankInfoVo.setAccountName(transPaymethodVo.getAccountName());
				transBankInfoVo.setBankId(transPaymethodVo.getBankId());
				transBankInfoVo.setBranchId(transPaymethodVo.getBranchId());
				transBankInfoVo.setAccountNo(transPaymethodVo.getAccountNo());
				transBankInfoDao.insertTransBankInfo(transBankInfoVo);
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransPaymethod: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更收費管道-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransPaymethodVo getTransPaymethodDetail(String transNum) {
		TransPaymethodVo qryVo = new TransPaymethodVo();
		qryVo.setTransNum(transNum);
		List<TransPaymethodVo> detailList = transPaymethodDao.getTransPaymethod(qryVo);
		
		TransPaymethodVo detailVo = new TransPaymethodVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
			
			// 取得銀行帳號變更申請資訊
			TransBankInfoVo qryTransBankInfoVo = new TransBankInfoVo();
			qryTransBankInfoVo.setTransNum(transNum);
			List<TransBankInfoVo> bankList = transBankInfoDao.getTransBankInfo(qryTransBankInfoVo);
			if (bankList != null && bankList.size() > 0) {
				TransBankInfoVo transBankInfoVo = bankList.get(0);
				detailVo.setAccountName(transBankInfoVo.getAccountName());
				detailVo.setBankId(transBankInfoVo.getBankId());
				detailVo.setBranchId(transBankInfoVo.getBranchId());
				detailVo.setAccountNo(transBankInfoVo.getAccountNo());
				
				// 取得銀行名稱
				BankInfoVo qryBankInfoVo = new BankInfoVo();
				qryBankInfoVo.setBankId(transBankInfoVo.getBankId());
				qryBankInfoVo.setBranchId(transBankInfoVo.getBranchId());
				List<BankInfoVo> bankInfoList = bankInfoDao.getBankInfo(qryBankInfoVo);
				if (bankInfoList != null && bankInfoList.size() > 0) {
					BankInfoVo bankInfoVo = bankInfoList.get(0);
					detailVo.setBankName(bankInfoVo.getBankName());
					detailVo.setBranchName(bankInfoVo.getBranchName());
				}
			}
		}
		return detailVo;
	}
}