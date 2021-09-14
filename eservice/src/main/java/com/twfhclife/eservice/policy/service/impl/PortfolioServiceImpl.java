package com.twfhclife.eservice.policy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.PortfolioDao;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.generic.util.RoiRateUtil;

@Service
public class PortfolioServiceImpl implements IPortfolioService {

	private static final Logger log = LogManager.getLogger(PortfolioServiceImpl.class);

	@Autowired
	private PortfolioDao portfolioDao;
	
	/**
	 * 取得投資損益及投報率清單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @param invtNo 投資標的方式(從投資停損表反查用)
	 * @return 回傳投資損益及投報率清單資料
	 */
	@Override
	public List<PortfolioVo> getPortfolioList(String policyNo, List<String> invtNo) {
		List<PortfolioVo> portfolioList = portfolioDao.getPortfolioList(policyNo, invtNo);
		for (PortfolioVo portfolioVo : portfolioList) {
			BigDecimal netAmt = portfolioVo.getSafpNetAmt(); // 目前金額
			BigDecimal netUnits = portfolioVo.getSafpNetUnits(); // 目前單位數
			BigDecimal ntdVal = portfolioVo.getSafpAvgPntdval(); // 平均台幣買價
			BigDecimal netValue = portfolioVo.getNetValueSell(); // 淨值
			BigDecimal exchRate = portfolioVo.getExchRateBuy(); // 匯率
			BigDecimal expeNtd = portfolioVo.getClupExpeNtdSum(); // 累計投資收益(不一定是台幣)
			
			BigDecimal[] values = new BigDecimal[3];
			BigDecimal roiRate; // 參考報酬率(%)
			BigDecimal acctValue; // 帳戶價值
			BigDecimal avgPval; // 平均成本
			
			// 台幣時匯率為1
			if ("NTD".equals(portfolioVo.getInvtExchCurr())) {
				exchRate = BigDecimal.valueOf(1);
			}
			
			try {
				if (portfolioVo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
					values = this.formula2(netAmt, exchRate, ntdVal);
					portfolioVo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
				} else {
					// {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
					//System.out.println("invtNo=" + portfolioVo.getInvtNo());
					values = this.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
				}
				
//				if (portfolioVo.getInvtNo().startsWith("FD")) { // FD：
//													// {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
//					values = this.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
//				} else if (portfolioVo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
//					values = this.formula2(netAmt, exchRate, ntdVal);
//					portfolioVo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
//				} else {
//					values = this.getZero();
//				}

				// 從Array指定變數
				roiRate = values[0];
				acctValue = values[1];
				avgPval = values[2];

			} catch (Exception ex) {
				log.error("policyNo:" + portfolioVo.getPolicyNo() + "invtNo :" + portfolioVo.getInvtNo());
				roiRate = new BigDecimal(0);
				acctValue = new BigDecimal(0);
				avgPval = new BigDecimal(0);
			}

			// 放入vo
			portfolioVo.setRoiRate(roiRate);
			portfolioVo.setAcctValue(acctValue);
			portfolioVo.setAvgPval(avgPval);
		}
		return portfolioList;
	}
	
	/**
	 * 設定平均報酬率及帳戶價值資料.
	 * 
	 * @param invtPolicyList 投資型保單料
	 */
	@Override
	public void setPortfolioData(List<PolicyListVo> invtPolicyList) {
		for (PolicyListVo vo : invtPolicyList) {
			BigDecimal policyAcctValue = BigDecimal.ZERO; // 帳戶價值
			BigDecimal avgPval = BigDecimal.ZERO; // 投資收益
			BigDecimal policyRoiRate = BigDecimal.ZERO; // 報酬率
			BigDecimal fdCount = BigDecimal.ZERO; // 投資標的FD開頭的數量，RD不需要列入次數
			
			List<PortfolioVo> portfolioList = getPortfolioList(vo.getPolicyNo(), null);
			for (PortfolioVo portfolioVo : portfolioList) {
				if ("FD".equals(portfolioVo.getInvtNo().substring(0, 2))) {
					fdCount = fdCount.add(BigDecimal.ONE);
				}
				policyRoiRate = policyRoiRate.add(portfolioVo.getRoiRate().multiply(new BigDecimal("100")));
				policyAcctValue = policyAcctValue.add(portfolioVo.getAcctValue());
				avgPval = avgPval.add(portfolioVo.getAvgPval());
			}
			
			if (!fdCount.equals(BigDecimal.ZERO)) {
//				vo.setAvgRoiRate(policyRoiRate.divide(fdCount));
				if (avgPval.equals(BigDecimal.ZERO)) {
					vo.setAvgRoiRate(BigDecimal.ZERO);
				} else {
					vo.setAvgRoiRate(policyAcctValue.subtract(avgPval).divide(avgPval,2, BigDecimal.ROUND_DOWN));
				}
				
			} else {
				vo.setAvgRoiRate(BigDecimal.ZERO);
			}
			vo.setPolicyAcctValue(policyAcctValue);
		}
	}
	
	/**
	 * 取得投資風險屬性.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資風險屬性
	 */
	@Override
	public String getRiskLevelName(String policyNo) {
		String riskLevelName = portfolioDao.getRiskLevelName(policyNo);
		return (StringUtils.isEmpty(riskLevelName) ? "" : riskLevelName);
	}
	
	/**
	 * 取得基金下拉選單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳基金下拉選單資料
	 */
	@Override
	public List<PortfolioVo> getInvtOptionList(String policyNo) {
		return portfolioDao.getInvtOptionList(policyNo);
	}

	/** 取0 */
	private BigDecimal[] getZero() {
		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal[] values = { zero, zero, zero };
		return values;
	}

	/** 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}% */
	private BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
		BigDecimal[] values = new BigDecimal[3];
		if (netUnits != null && netValue != null && exchRate != null && ntdVal != null && accumAmt != null) {
			values = RoiRateUtil.formula1(netUnits, netValue, exchRate, ntdVal, accumAmt);
		} else {
			values = this.getZero();
		}
		return values;
	}

	/** 公式2: RT 貨幣帳戶: {[(帳戶金額*匯率)/平均台幣買價]-1}% */
	private BigDecimal[] formula2(BigDecimal netAmt, BigDecimal exchRate, BigDecimal ntdVal) {
		BigDecimal[] values = new BigDecimal[3];
		if (netAmt != null && exchRate != null && ntdVal != null) {
			values = RoiRateUtil.formula2(netAmt, exchRate, ntdVal);
		} else {
			values = this.getZero();
		}
		return values;
	}
}
