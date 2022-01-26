package com.twfhclife.eservice.dashboard.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.dashboard.service.IDashboardService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.BenefitDao;
import com.twfhclife.eservice.policy.dao.CoverageDao;
import com.twfhclife.eservice.policy.dao.ExchangeRateDao;
import com.twfhclife.eservice.policy.model.BenefitVo;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 保障總覽服務.
 * 
 * @author alan
 */
@Service
public class DashboardServiceImpl implements IDashboardService {
	
	@Autowired
	private CoverageDao coverageDao;

	@Autowired
	private BenefitDao benefitDao;
	
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	
	/**
	 * 取得被保人的商品類型清單.
	 * 
	 * @param benefitPolicyList 保障型保單資料
	 * @return 回傳被保人的商品資料
	 */
	@RequestLog
	@Override
	public Map<String, List<String>> getInsuredProductData(List<PolicyListVo> benefitPolicyList) {
		// 設定每個保單的保項資料
		setPolicyCoverageData(benefitPolicyList);
		
		// 取得保障型保單資料的被保人分類清單
		List<String> insuredNameList = getInsuredNameList(benefitPolicyList);

		// 用被保人群組分類保項資料
		Map<String, List<String>> InsuredProductMap = new HashMap<>();
		for (String insuredName : insuredNameList) {
			if (insuredName != null) {
				List<String> insuredProductList = new LinkedList<>();
				for (PolicyListVo policyListVo : benefitPolicyList) {
					for (CoverageVo vo : policyListVo.getCoverageList()) {
						// 找相同被保人
						if (vo != null && insuredName.equals(vo.getInsuredName())) {
							if (vo.getProductVo() != null && vo.getProductVo().getProductCode() != null) {
								// 設定被保人有的商品代號
								String productCode = vo.getProductVo().getProductCode();
								if (!insuredProductList.contains(productCode)) {
									insuredProductList.add(productCode);
								}
							}
						}
					}
				}
				InsuredProductMap.put(insuredName, insuredProductList);
			}
		}
		
		return InsuredProductMap;
	}
	
	/**
	 * 取得保障型的的被保人保項資料.
	 * 
	 * @param benefitPolicyList 保障型保單資料
	 * @return 回傳被保人保項資料
	 */
	@RequestLog
	@Override
	public Map<String, List<CoverageVo>> getBenefitInsuredData(List<PolicyListVo> benefitPolicyList) {
		// 設定每個保單的保項資料
		setPolicyCoverageData(benefitPolicyList);
		
		// 設定每個保項的保障明細資料
		for (PolicyListVo policyListVo : benefitPolicyList) {
			setCoverageBenefitData(policyListVo.getCoverageList());
		}
		
		// 取得保障型保單資料的被保人分類清單
		List<String> insuredNameList = getInsuredNameList(benefitPolicyList);

		// 用被保人群組分類保項資料
		Map<String, List<CoverageVo>> insuredCoverageMap = new HashMap<>();
		for (String insuredName : insuredNameList) {
			if (insuredName != null) {
				List<CoverageVo> insuredCoverageList = new LinkedList<>();
				for (PolicyListVo policyListVo : benefitPolicyList) {
					for (CoverageVo vo : policyListVo.getCoverageList()) {
						if (insuredName.equals(vo.getInsuredName())) {
							insuredCoverageList.add(vo);
						}
					}
				}
				insuredCoverageMap.put(insuredName, insuredCoverageList);
			}
		}
		
		return insuredCoverageMap;
	}
	
	/**
	 * 設定每個保單的保項資料.
	 * 
	 * @param benefitPolicyList 保障型保單資料
	 */
	private void setPolicyCoverageData(List<PolicyListVo> benefitPolicyList) {
		for (PolicyListVo policyListVo : benefitPolicyList) {
			CoverageVo coverageVo = new CoverageVo();
			coverageVo.setPolicyNo(policyListVo.getPolicyNo());
			
			List<CoverageVo> coverageList = coverageDao.getCoverageList(coverageVo);
			if (coverageList != null) {
				policyListVo.setCoverageList(coverageList);
			}
		}
	}
	
	/**
	 * 設定每個保項的保障明細資料.
	 * 
	 * @param coverageVo CoverageVo
	 */
	private void setCoverageBenefitData(List<CoverageVo> coverageList) {
		if (coverageList != null) {
			for (CoverageVo vo : coverageList) {
				if (vo.getProductVo() != null) {
					BenefitVo benefitVo = new BenefitVo();
					benefitVo.setProductCode(vo.getProductVo().getProductCode());
					vo.setBenefitList(benefitDao.getBenefitList(benefitVo, vo.getPolicyNo()));
				}
			}
		}
	}
	
	/**
	 * 取得保障型保單資料的被保人分類清單.
	 * 
	 * @param benefitPolicyList
	 * @return
	 */
	private List<String> getInsuredNameList(List<PolicyListVo> benefitPolicyList) {
		List<String> insuredNameList = new ArrayList<>();
		for (PolicyListVo policyListVo : benefitPolicyList) {
			for (CoverageVo vo : policyListVo.getCoverageList()) {
				if (!insuredNameList.contains(vo.getInsuredName())) {
					insuredNameList.add(vo.getInsuredName());
				}
			}
		}
		return insuredNameList;
	}

	@Override
	public BigDecimal getPolicyAcctValueTotal(List<PolicyListVo> invtPolicyList) {
		BigDecimal policyAcctValueTotal = BigDecimal.ZERO;
		
		HashMap<String,ExchangeRateVo> mapER = new HashMap<String,ExchangeRateVo>();
		if(invtPolicyList!=null && invtPolicyList.size()>0) {
			
			List<ExchangeRateVo> listER = null;
			for (PolicyListVo vo : invtPolicyList) {
				
				//非台幣時需加計幣值匯率
				if(vo.getPolicyAcctValue()!=null && vo.getPolicyAcctValue().compareTo(BigDecimal.ZERO)>0) {
					if(!"NTD".equals(vo.getCurrency())){
						//非台幣
						listER = null;
						if(!mapER.containsKey(vo.getCurrency())) {//同幣值只取一次
							listER = exchangeRateDao.getNewestExchangeRateByExchangeCode(vo.getCurrency());
							if(listER!=null && listER.size()>0) {
								ExchangeRateVo erVo = (ExchangeRateVo)listER.get(0);
								mapER.put(erVo.getExchangeCode(), erVo);
							}
						}
						
						ExchangeRateVo erVo = (ExchangeRateVo)mapER.get(vo.getCurrency());
						policyAcctValueTotal = policyAcctValueTotal.add(vo.getPolicyAcctValue().multiply(erVo.getSellPrice()));
						
					}else {
						//台幣
						policyAcctValueTotal = policyAcctValueTotal.add(vo.getPolicyAcctValue());
					}
				}else {
					//do nothing.
				}

			}
		}
		
		return policyAcctValueTotal;
	}
	
	
}