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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.BankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransBankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransChangeAccountDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.BankInfoVo;
import com.twfhclife.eservice.onlineChange.model.LitracEsVo;
import com.twfhclife.eservice.onlineChange.model.TransBankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransChangeAccountService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyBonusVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyBonusService;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 匯款帳號變更服務.
 * 
 * @author all
 */
@Service
public class TransChangeAccountServiceImpl implements ITransChangeAccountService {

	private static final Logger logger = LogManager.getLogger(TransChangeAccountServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransChangeAccountDao transChangeAccountDao;

	@Autowired
	private TransBankInfoDao transBankInfoDao;
	
	@Autowired
	private BankInfoDao bankInfoDao;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;
	
	@Autowired
	private IPolicyBonusService policyBonusService;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 * @return 回傳保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					boolean lockFlag = false;
					String canChangeType1 = "N";
					String canChangeType2 = "N";
					String canChangeType3 = "N";
					// 取消從 LitracEs 判斷, 改用受益人+紅利判斷 
					List<BeneficiaryVo> beneficiaryVoList = beneficiaryService.getBeneficiaryByPolicyNo(vo.getPolicyNo());
					for(BeneficiaryVo beneficiaryVo: beneficiaryVoList) {
						// 戶名、證號有一空白則不能申請
						boolean userDataComplete = true;
//						if(StringUtils.isBlank(beneficiaryVo.getBeneficiaryName())
//								|| StringUtils.isBlank(beneficiaryVo.getBeneficiaryRocid())) {
//							userDataComplete = false;
//						}
						if(userDataComplete && "01".equals(String.format("%02d", beneficiaryVo.getBeneficiaryType()))) {
							canChangeType1 = "Y";
							continue;
						}
						if(userDataComplete && "05".equals(String.format("%02d", beneficiaryVo.getBeneficiaryType()))) {
							canChangeType2 = "Y";
							continue;
						}
					}
					List<PolicyBonusVo> policyBonusVoList = policyBonusService.findByPolicyNo(vo.getPolicyNo());
					for(PolicyBonusVo policyBonusVo: policyBonusVoList) {
						// 戶名、證號有一空白則不能申請
						boolean userDataComplete = true;
//						if(StringUtils.isBlank(policyBonusVo.getIndividualVo().getName())
//								|| StringUtils.isBlank(policyBonusVo.getIndividualVo().getRocId())) {
//							userDataComplete = false;
//						}
						if(userDataComplete && "1".equals(policyBonusVo.getTakeCode())) {
							canChangeType3 = "Y";
							continue;
						}
					}
					if("N".equals(canChangeType1) && "N".equals(canChangeType2) && "N".equals(canChangeType3)) {
						lockFlag = true;
					}
					
					if (lockFlag) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.CHANGE_ACCOUNT_INFO_NOT_ALLOW_MSG);
					}
				}
			}
		}
	}
	
	/**
	 * 匯款帳號變更-查詢.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransChangeAccountVo> getTransChangeAccount(TransChangeAccountVo transChangeAccountVo) {
		return transChangeAccountDao.getTransChangeAccount(transChangeAccountVo);
	}

	/**
	 * 匯款帳號變更-新增.
	 * 
	 * @param transChangeAccountVo TransChangeAccountVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransChangeAccount(TransChangeAccountVo transChangeAccountVo) {
		String transNum = transChangeAccountVo.getTransNum();
		String userId = transChangeAccountVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transChangeAccountVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增匯款帳號變更
			transChangeAccountDao.insertTransChangeAccount(transChangeAccountVo);
			
			// 新增銀行帳戶資訊
			TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
			transBankInfoVo.setTransNum(transNum);
			transBankInfoVo.setAccountName(transChangeAccountVo.getAccountName());
			transBankInfoVo.setBankId(transChangeAccountVo.getBankId());
			transBankInfoVo.setBranchId(transChangeAccountVo.getBranchId());
			transBankInfoVo.setAccountNo(transChangeAccountVo.getAccountNo());
			transBankInfoDao.insertTransBankInfo(transBankInfoVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransChangeAccount: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	/**
	 * 匯款帳號變更-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransChangeAccountVo getTransChangeAccountDetail(String transNum) {
		TransChangeAccountVo qryVo = new TransChangeAccountVo();
		qryVo.setTransNum(transNum);
		List<TransChangeAccountVo> detailList = transChangeAccountDao.getTransChangeAccount(qryVo);
		
		TransChangeAccountVo detailVo = new TransChangeAccountVo();
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