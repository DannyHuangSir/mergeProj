package com.twfhclife.eservice.onlineChange.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
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
import com.twfhclife.eservice.onlineChange.dao.TransExtendAttrDao;
import com.twfhclife.eservice.onlineChange.dao.TransLoanDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.BankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransBankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.model.TransLoanVo;
import com.twfhclife.eservice.onlineChange.service.ITransLoanService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.dao.PolicyExtraDao;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.util.RptUtils;

/**
 * 申請保單貸款服務.
 * 
 * @author all
 */
@Service
public class TransLoanServiceImpl implements ITransLoanService {

	private static final Logger logger = LogManager.getLogger(TransLoanServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransLoanDao transLoanDao;

	@Autowired
	private TransBankInfoDao transBankInfoDao;
	
	@Autowired
	private BankInfoDao bankInfoDao;

	@Autowired
	private PolicyExtraDao policyExtraDao;

	@Autowired
	private TransExtendAttrDao transExtendAttrDao;

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
					boolean lockFlag = false;
					PolicyExtraVo policyExtraVo = vo.getPolicyExtraVo();
					if (policyExtraVo == null) {
						lockFlag = true;
					} else {
						if (policyExtraVo.getRemainLoanValue() != null
								&& policyExtraVo.getRemainLoanValue().intValue() < 1000) {
							lockFlag = true;
						}
					}
					
					if (lockFlag) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_LOAN_MSG);
						continue;
					}
					
					// 要被保人是否為同一人 1:同一人, 0:不同人
//					if (!lockFlag) {
//						if (vo.getSameIdCount() == 0) {
//							vo.setApplyLockedFlag("Y");
//							vo.setApplyLockedMsg(OnlineChangMsgUtil.INSURED_NOT_SAME_MSG);
//						}
//					}
				}
			}
		}
	}
	
	/**
	 * 申請保單貸款-查詢.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransLoanVo> getTransLoan(TransLoanVo transLoanVo) {
		return transLoanDao.getTransLoan(transLoanVo);
	}

	/**
	 * 申請保單貸款-新增.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransLoan(TransLoanVo transLoanVo) {
		String transNum = transLoanVo.getTransNum();
		String userId = transLoanVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.LOAN_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_BEADDED);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			TransPolicyVo transPolicyVo = new TransPolicyVo();
			transPolicyVo.setTransNum(transNum);
			transPolicyVo.setPolicyNo(transLoanVo.getPolicyNo());
			transPolicyDao.insertTransPolicy(transPolicyVo);
			
			// 新增申請保單貸款
			transLoanDao.insertTransLoan(transLoanVo);
			
			// 新增銀行帳戶資訊
			String accountName = transLoanVo.getCustomerName();
			if (!StringUtils.isEmpty(accountName) && accountName.length() > 3) {
				accountName = accountName.substring(0, 3);
			}
			
			TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
			transBankInfoVo.setTransNum(transNum);
			transBankInfoVo.setAccountName(accountName);
			transBankInfoVo.setBankId(transLoanVo.getBankId());
			transBankInfoVo.setBranchId(transLoanVo.getBranchId());
			transBankInfoVo.setAccountNo(transLoanVo.getAccountNumber());
			transBankInfoDao.insertTransBankInfo(transBankInfoVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransLoan: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 申請保單貸款-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransLoanVo getTransLoanDetail(String transNum) {
		TransLoanVo qryVo = new TransLoanVo();
		qryVo.setTransNum(transNum);
		List<TransLoanVo> detailList = transLoanDao.getTransLoan(qryVo);
		
		PolicyExtraVo policyExtraVo = null;
		List<String> policyNos = transPolicyDao.getTransPolicyNoList(transNum);
		if (policyNos != null && policyNos.size() > 0) {
			String policyNo = policyNos.get(0);
			policyExtraVo = policyExtraDao.findByPolicyNo(policyNo);
		}
		
		TransLoanVo detailVo = new TransLoanVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
			
			// 可借、已借金額
			if (policyExtraVo != null) {
				detailVo.setRemainLoanValue(policyExtraVo.getRemainLoanValue());
				detailVo.setLoanedAmount(policyExtraVo.getLoanAmount());
			}
			
			// 取得銀行帳號變更申請資訊
			TransBankInfoVo qryTransBankInfoVo = new TransBankInfoVo();
			qryTransBankInfoVo.setTransNum(transNum);
			List<TransBankInfoVo> bankList = transBankInfoDao.getTransBankInfo(qryTransBankInfoVo);
			if (bankList != null && bankList.size() > 0) {
				TransBankInfoVo transBankInfoVo = bankList.get(0);
				detailVo.setAccountName(transBankInfoVo.getAccountName());
				detailVo.setBankId(transBankInfoVo.getBankId());
				detailVo.setBranchId(transBankInfoVo.getBranchId());
				detailVo.setAccountNumber(transBankInfoVo.getAccountNo());
				
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
			
			List<TransExtendAttrVo> transExtendAttrList = transExtendAttrDao.getTransExtendsByTransNum(transNum);
			if (transExtendAttrList != null) {
				for (TransExtendAttrVo vo : transExtendAttrList) {
					try {
						for (Field f : detailVo.getClass().getDeclaredFields()) {
							if (f.getName().equals(vo.getAttrKey())) {
								f.setAccessible(true);
								f.set(detailVo, vo.getAttrValue());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return detailVo;
	}

	/**
	 * 取得申請保單貸款報表 byte[].
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF(TransLoanVo transLoanVo) {
		byte[] pdfByte = null;
		try {
			RptUtils rptUtils = new RptUtils("loan.pdf");

			// 保單號碼
			String policyNo = transLoanVo.getPolicyNo();
			rptUtils.txt(policyNo, 11, 1, 98, 743);

			// 要保人
			rptUtils.txt(transLoanVo.getCustomerName(), 11, 1, 300, 743);
			
			// 被保險人
			rptUtils.txt(transLoanVo.getInsuredName(), 11, 1, 480, 743);
			
			// 申借金額
			BigDecimal loanAmount = transLoanVo.getLoanAmount();
			String twLoanAmount = "";
			if (loanAmount != null && loanAmount.toString().endsWith("000")) {
				twLoanAmount = loanAmount.toString();
				twLoanAmount = twLoanAmount.replace("000", ""); // 千元為單位
				twLoanAmount = twLoanAmount.replace("1", "壹");
				twLoanAmount = twLoanAmount.replace("2", "貳");
				twLoanAmount = twLoanAmount.replace("3", "參");
				twLoanAmount = twLoanAmount.replace("4", "肆");
				twLoanAmount = twLoanAmount.replace("5", "伍");
				twLoanAmount = twLoanAmount.replace("6", "陸");
				twLoanAmount = twLoanAmount.replace("7", "柒");
				twLoanAmount = twLoanAmount.replace("8", "捌");
				twLoanAmount = twLoanAmount.replace("9", "玖");
				twLoanAmount = twLoanAmount.replace("0", "零");
			}
			
			if (twLoanAmount.length() == 1) {
				rptUtils.txt(twLoanAmount, 12, 1, 375, 718);
			}
			if (twLoanAmount.length() == 2) {
				rptUtils.txt(twLoanAmount.substring(0, 1), 12, 1, 320, 718);
				rptUtils.txt(twLoanAmount.substring(1, 2), 12, 1, 375, 718);
			}
			if (twLoanAmount.length() == 3) {
				rptUtils.txt(twLoanAmount.substring(0, 1), 12, 1, 260, 718);
				rptUtils.txt(twLoanAmount.substring(1, 2), 12, 1, 320, 718);
				rptUtils.txt(twLoanAmount.substring(2, 3), 12, 1, 375, 718);
			}
			if (twLoanAmount.length() == 4) {
				rptUtils.txt(twLoanAmount.substring(0, 1), 12, 1, 205, 718);
				rptUtils.txt(twLoanAmount.substring(1, 2), 12, 1, 260, 718);
				rptUtils.txt(twLoanAmount.substring(2, 3), 12, 1, 320, 718);
				rptUtils.txt(twLoanAmount.substring(3, 4), 12, 1, 375, 718);
			}
			if (twLoanAmount.length() == 5) {
				rptUtils.txt(twLoanAmount.substring(0, 1), 12, 1, 150, 718);
				rptUtils.txt(twLoanAmount.substring(1, 2), 12, 1, 205, 718);
				rptUtils.txt(twLoanAmount.substring(2, 3), 12, 1, 260, 718);
				rptUtils.txt(twLoanAmount.substring(3, 4), 12, 1, 320, 718);
				rptUtils.txt(twLoanAmount.substring(4, 5), 12, 1, 375, 718);
			}
			
			// 電匯
			rptUtils.txt("■", 12, 1, 42.5f, 625);
			
			// 金融機構名稱
			rptUtils.txt(transLoanVo.getBankName(), 11, 1, 138, 611);
			
			// 分行(支庫、支局)
			rptUtils.txt(transLoanVo.getBranchName(), 11, 1, 340, 611);
			
			// 帳號
			rptUtils.txt(transLoanVo.getAccountNumber(), 11, 1, 485, 611);

			pdfByte = rptUtils.toPdf();
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}

	@Override
	public List<PolicyListVo> filterPolicyLoanAcct(List<PolicyListVo> policyList) {
		List<PolicyListVo> list = new ArrayList<PolicyListVo>();
		for (PolicyListVo vo : policyList) {
			if (vo.getPolicyExtraVo() != null && vo.getPolicyExtraVo().getInmsAccountNo() != null) {
				list.add(SerializationUtils.clone(vo));
			}
		}
		return list;
	}

	/**
	 * 申請保單貸款(已有指定帳號)-新增.
	 * 
	 * @param transLoanVo TransLoanVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransLoanNew(TransLoanVo transLoanVo, List<TransExtendAttrVo> transExtendAttrList) {
		String transNum = transLoanVo.getTransNum();
		String userId = transLoanVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.LOAN_NEW_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			TransPolicyVo transPolicyVo = new TransPolicyVo();
			transPolicyVo.setTransNum(transNum);
			transPolicyVo.setPolicyNo(transLoanVo.getPolicyNo());
			transPolicyDao.insertTransPolicy(transPolicyVo);
			
			// 新增申請保單貸款
			transLoanDao.insertTransLoan(transLoanVo);
			
			// 新增銀行帳戶資訊
			String accountName = transLoanVo.getCustomerName();
			if (!StringUtils.isEmpty(accountName) && accountName.length() > 3) {
				accountName = accountName.substring(0, 3);
			}
			
			TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
			transBankInfoVo.setTransNum(transNum);
			transBankInfoVo.setAccountName(accountName);
			transBankInfoVo.setBankId(transLoanVo.getBankId());
			transBankInfoVo.setBranchId(transLoanVo.getBranchId());
			transBankInfoVo.setAccountNo(transLoanVo.getAccountNumber());
			transBankInfoDao.insertTransBankInfo(transBankInfoVo);
			
			// 新增額外欄位資料
			if (transExtendAttrList != null) {
				for (TransExtendAttrVo transExtendAttrVo : transExtendAttrList) {
					transExtendAttrVo.setTransNum(transLoanVo.getTransNum());
					transExtendAttrDao.insertTransExtendAttr(transExtendAttrVo);
				}
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransLoanNew: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
}