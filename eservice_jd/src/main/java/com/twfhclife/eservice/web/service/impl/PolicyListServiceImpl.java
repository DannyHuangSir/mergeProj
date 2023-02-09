package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.shouxian.dao.PolicyExtraDao;
import com.twfhclife.eservice.shouxian.dao.PolicyListDao;
import com.twfhclife.eservice.util.FormulaUtil;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.IPolicyListService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保單服務.
 * 
 * @author alan
 */
@Service
public class PolicyListServiceImpl  implements IPolicyListService {
	
	private static final Logger logger = LogManager.getLogger(PolicyListServiceImpl.class);
	
	@Autowired
	public PolicyListDao policyListDao;
	@Autowired
	public PolicyExtraDao policyExtraDao;

	@Autowired
	public IUnicodeService unicodeService;
	
	/**
	 * 取得使用者的所有保單清單.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	@Override
	public List<PolicyListVo> getUserPolicyList(String rocId) {
		List<PolicyListVo> list = policyListDao.getUserPolicyList(rocId);
		for (PolicyListVo policy : list) {
			if (StringUtils.isEmpty(policy.getMainInsuredName())) {
				policy.setMainInsuredNameBase64("");
			} else {
				policy.setMainInsuredNameBase64(unicodeService.convertString2Unicode(policy.getMainInsuredName()));
			}

			//customerName
			if (StringUtils.isEmpty(policy.getCustomerName())) {
				policy.setCustomerNameBase64("");
			} else {
				policy.setCustomerNameBase64(unicodeService.convertString2Unicode(policy.getCustomerName()));
			}
		}
		return list;
	}
	
	/**
	 * 取得使用者的所有保單清單(保單理賠).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	@Override
	public List<PolicyListVo> getUserPolicyListByRocId(String rocId) {
		List<PolicyListVo> list = policyListDao.getUserPolicyListByRocId(rocId);
		for (PolicyListVo policy : list) {
			if (StringUtils.isEmpty(policy.getMainInsuredName())) {
				policy.setMainInsuredNameBase64("");
			} else {
				policy.setMainInsuredNameBase64(unicodeService.convertString2Unicode(policy.getMainInsuredName()));
			}

			//customerName
			if (StringUtils.isEmpty(policy.getCustomerName())) {
				policy.setCustomerNameBase64("");
			} else {
				policy.setCustomerNameBase64(unicodeService.convertString2Unicode(policy.getCustomerName()));
			}
		}
		return list;
	}

	/**
	 * 取得使用者的所有保單清單(投資型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	@Override
	public List<PolicyListVo> getInvtPolicyList(String rocId) {
		List<PolicyListVo> list = policyListDao.getInvtPolicyList(rocId);
		for (PolicyListVo policy : list) {
			if (StringUtils.isEmpty(policy.getMainInsuredName())) {
				policy.setMainInsuredNameBase64("");
			} else {
				policy.setMainInsuredNameBase64(unicodeService.convertString2Unicode(policy.getMainInsuredName()));
			}
			//customerName
			if (StringUtils.isEmpty(policy.getCustomerName())) {
				policy.setCustomerNameBase64("");
			} else {
				policy.setCustomerNameBase64(unicodeService.convertString2Unicode(policy.getCustomerName()));
			}
			logger.info("==========投資型============getMainInsuredName={}============getCustomerNameBase64={}",policy.getCustomerName(),policy.getCustomerNameBase64());
			logger.info("===========投資型===========getCustomerName={}============getMainInsuredNameBase64={}",policy.getMainInsuredName(),policy.getMainInsuredNameBase64());
			PolicyExtraVo extra = policyExtraDao.findByPolicyNo(policy.getPolicyNo());
			if (extra != null) {
				policy.setPolicyExtraVo(extra);
			}
		}
		return list;
	}

	/**
	 * 取得使用者的所有保單清單(保障型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳該使用者下的所有保單號碼清單.
	 */
	@Override
	public List<PolicyListVo> getBenefitPolicyList(String rocId) {
		List<PolicyListVo> list = policyListDao.getBenefitPolicyList(rocId);
		for (PolicyListVo policy : list) {
			if (StringUtils.isEmpty(policy.getMainInsuredName())) {
				policy.setMainInsuredNameBase64("");
			} else {
				policy.setMainInsuredNameBase64(unicodeService.convertString2Unicode(policy.getMainInsuredName()));
			}

			if (StringUtils.isEmpty(policy.getCustomerName())) {
				policy.setCustomerNameBase64("");
			} else {//customerName
				policy.setCustomerNameBase64(unicodeService.convertString2Unicode(policy.getCustomerName()));
			}
			logger.info("==============保障型========getMainInsuredName={}============getCustomerNameBase64={}",policy.getCustomerName(),policy.getCustomerNameBase64());
			logger.info("==============保障型========getCustomerName={}============getMainInsuredNameBase64={}",policy.getMainInsuredName(),policy.getMainInsuredNameBase64());


			PolicyExtraVo extra = policyExtraDao.findByPolicyNo(policy.getPolicyNo());
			if (extra != null) {
				policy.setPolicyExtraVo(extra);
			}
		}
		return list;
	}
	
	/**
	 * 取得我的保單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@Override
	public PolicyVo findById(String policyNo) {
		return policyListDao.findById(policyNo);
	}

	@Override
	public ProductVo findProductByPolicyNo(String policyNo) {
		return policyListDao.findProductByPolicyNo(policyNo);
	}


	/***
	 * 获取投资型保单
	 * @param rocId
	 * @return
	 */
	@Override
	public List<PolicyListVo> getInvestmentPolicyList(String rocId) {
		List<PolicyListVo> list = policyListDao.getInvestmentPolicyList(rocId);
		for (PolicyListVo policy : list) {
			if (StringUtils.isEmpty(policy.getMainInsuredName())) {
				policy.setMainInsuredNameBase64("");
			} else {
				policy.setMainInsuredNameBase64(unicodeService.convertString2Unicode(policy.getMainInsuredName()));
			}

			//customerName
			if (StringUtils.isEmpty(policy.getCustomerName())) {
				policy.setCustomerNameBase64("");
			} else {
				policy.setCustomerNameBase64(unicodeService.convertString2Unicode(policy.getCustomerName()));
			}
		}
		return list;
	}

    @Override
	public List<PolicyListVo> getDepositList(String userRocId, String policyNo) {
		List<DepositPolicyListVo> depositList = policyListDao.getDepositList(userRocId, policyNo);
		List<PolicyListVo> result = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(depositList)) {
			for (DepositPolicyListVo depositVo : depositList) {
				if (!CollectionUtils.isEmpty(depositVo.getPortfolioVoList())) {
					BigDecimal amount = BigDecimal.valueOf(0);
					for (PortfolioVo portfolioVo : depositVo.getPortfolioVoList()) {
						BigDecimal netAmt = portfolioVo.getSafpNetAmt(); // 目前金額
						BigDecimal netUnits = portfolioVo.getSafpNetUnits(); // 目前單位數
						BigDecimal ntdVal = portfolioVo.getSafpAvgPntdval(); // 平均台幣買價
						BigDecimal netValue = portfolioVo.getNetValueSell(); // 淨值
						BigDecimal exchRate = portfolioVo.getExchRateBuy(); // 匯率
						BigDecimal expeNtd = portfolioVo.getClupExpeNtdSum(); // 累計投資收益(不一定是台幣)
						BigDecimal[] values = new BigDecimal[3];
						BigDecimal acctValue; // 帳戶價值

						// 台幣時匯率為1
						if ("NTD".equals(portfolioVo.getInvtExchCurr())) {
							exchRate = BigDecimal.valueOf(1);
						}
						try {
							if (portfolioVo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
								values = FormulaUtil.formula2(netAmt, exchRate, ntdVal);
								portfolioVo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
							} else {
								// {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
								values = FormulaUtil.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
							}
							// 從Array指定變數
							acctValue = values[1];
							portfolioVo.setAcctValue(acctValue);
						} catch (Exception ex) {
							logger.error("policyNo:" + portfolioVo.getPolicyNo() + "invtNo :" + portfolioVo.getInvtNo());
							acctValue = new BigDecimal(0);
						}
						amount = amount.add(acctValue);
					}
					depositVo.setPolicyAcctValue(amount);
				}
				result.add(depositVo);
			}
		}
		return result;
    }
}