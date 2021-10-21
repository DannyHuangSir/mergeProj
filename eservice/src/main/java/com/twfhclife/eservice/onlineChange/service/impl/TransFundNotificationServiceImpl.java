package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.common.base.Splitter;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransFundNotificationDao;
import com.twfhclife.eservice.onlineChange.dao.TransFundNotificationDtlDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationVo;
import com.twfhclife.eservice.onlineChange.service.ITransFundNotificationService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.*;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.util.FormulaUtil;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 停損停利通知服務.
 * 
 * @author all
 */
@Service
public class TransFundNotificationServiceImpl implements ITransFundNotificationService {

	private static final Logger logger = LogManager.getLogger(TransFundNotificationServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransFundNotificationDao transFundNotificationDao;

	@Autowired
	private TransFundNotificationDtlDao transFundNotificationDtlDao;
	
	@Autowired
	private IPortfolioService portfolioService;

	@Autowired
	private ITransRiskLevelService riskLevelService;

	@Autowired
	private IParameterService parameterService;

	/**
	 * 停損停利通知-查詢.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransFundNotificationVo> getTransFundNotification(TransFundNotificationVo transFundNotificationVo) {
		return transFundNotificationDao.getTransFundNotification(transFundNotificationVo);
	}

	/**
	 * 停損停利通知-新增.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransFundNotification(TransFundNotificationVo transFundNotificationVo) {
		String transNum = transFundNotificationVo.getTransNum();
		String userId = transFundNotificationVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_COMPLETE);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transFundNotificationVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增停損停利通知
			BigDecimal transFundNotificationId = transFundNotificationDao.getNextTransFundNotificationId();
			transFundNotificationVo.setId(transFundNotificationId);
			transFundNotificationDao.insertTransFundNotification(transFundNotificationVo);
			
			// 新增停損停利通知-明細
			List<TransFundNotificationDtlVo> transFundNotificationDtlList = transFundNotificationVo.getTransFundNotificationDtlList();
			for (TransFundNotificationDtlVo transFundNotificationDtlVo : transFundNotificationDtlList) {
				if (transFundNotificationDtlVo.getPercentageDown() != null || transFundNotificationDtlVo.getPercentageUp() != null) {
					transFundNotificationDtlVo.setTransFundNotificationId(transFundNotificationId);
					transFundNotificationDtlDao.insertTransFundNotificationDtl(transFundNotificationDtlVo);
				} else if (transFundNotificationDtlVo.getDownValue() != null || transFundNotificationDtlVo.getUpValue() != null) {
					transFundNotificationDtlVo.setTransFundNotificationId(transFundNotificationId);
					transFundNotificationDtlDao.insertTransFundNotificationDtl(transFundNotificationDtlVo);
				}
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransFundNotification: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 取得停損停利通知申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public BigDecimal getTransFundNotificationId(String transNum) {
		TransFundNotificationVo qryVo = new TransFundNotificationVo();
		qryVo.setTransNum(transNum);
		List<TransFundNotificationVo> dataList = transFundNotificationDao.getTransFundNotification(qryVo);
		
		BigDecimal transFundNotificationId = null;
		if (!CollectionUtils.isEmpty(dataList)) {
			transFundNotificationId = dataList.get(0).getId();
		}
		return transFundNotificationId;
	}
	
	/**
	 * 停損停利通知-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @param transFundNotificationId 停損停利通知申請id
	 * @return 回傳查詢結果
	 */
	@Override
	public List<TransFundNotificationDtlVo> getTransFundNotificationDtlDetail(String transNum, BigDecimal transFundNotificationId) {
		TransFundNotificationDtlVo qryVo = new TransFundNotificationDtlVo();
		qryVo.setTransFundNotificationId(transFundNotificationId);
		List<TransFundNotificationDtlVo> detailList = transFundNotificationDtlDao.getTransFundNotificationDtl(qryVo);
		
		// 找policyNo
		String policyNo = "";
		List<String> policyNoList = transPolicyDao.getTransPolicyNoList(transNum);
		if (!CollectionUtils.isEmpty(policyNoList)) {
			policyNo = policyNoList.get(0);
		}
		
		if (!StringUtils.isEmpty(policyNo)) {
			for (TransFundNotificationDtlVo dtlVo : detailList) {
				if (StringUtils.equals(dtlVo.getType(), "1")) {
					List<PortfolioVo> portfolioList = portfolioService.getPortfolioList(policyNo, Arrays.asList(dtlVo.getFundCode()));
					if (portfolioList != null && portfolioList.size() > 0) {
						PortfolioVo portfolioVo = portfolioList.get(0);
						dtlVo.setInvtName(portfolioVo.getInvtName());
						dtlVo.setCurrency(portfolioVo.getCurrency());
						dtlVo.setRiskBeneLevel(portfolioVo.getInvtRiskBeneLevel());
						dtlVo.setAcctValue(String.valueOf(portfolioVo.getAcctValue()));
						if (portfolioVo.getAcctValue() != null && portfolioVo.getAvgPval() != null && portfolioVo.getAvgPval().intValue() != 0) {
								dtlVo.setRoiRate(String.valueOf(portfolioVo.getAcctValue().subtract(portfolioVo.getAvgPval()).
										divide(portfolioVo.getAvgPval(), 4, BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100)).doubleValue()));
						} else {
							dtlVo.setRoiRate("0");
						}
						dtlVo.setInCurr(portfolioVo.getInvtExchCurr());
					}
				} else {
					dtlVo.setInvtName(transFundNotificationDao.findByInvtNo(policyNo, dtlVo.getFundCode()));
				}
			}
		}
		return detailList;
	}

	@Override
	public List<NotificationFundVo> getSearchPortfolio(String policyNo, List<String> invtNos, String userRocId) {
		String riskLevel = riskLevelService.getUserRiskAttr(userRocId);
		String listRR = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_LEVEL_TO_RR_" + riskLevel);
		List<String> rrs = null;
		if (StringUtils.isNotBlank(listRR)) {
			rrs = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(listRR);
		}
		return transFundNotificationDao.getSearchFunds(policyNo, rrs, invtNos);
	}

	@Override
	public List<NotificationPortfolioVo> getOwnNotifications(String policyNo) {
		List<NotificationPortfolioVo> portfolioList = transFundNotificationDao.getNotificationPortfolioList(policyNo);
		for (NotificationPortfolioVo portfolioVo : portfolioList) {
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
					values = FormulaUtil.formula2(netAmt, exchRate, ntdVal);
					portfolioVo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
				} else {
					// {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
					//System.out.println("invtNo=" + portfolioVo.getInvtNo());
					values = FormulaUtil.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
					if (values[2] != null && values[1] != null) {
						values[0] = values[1].subtract(values[2]).divide(values[2], 4, BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100));
					}
				}

				// 從Array指定變數
				roiRate = values[0];
				acctValue = values[1];
				avgPval = values[2];

			} catch (Exception ex) {
				logger.error("policyNo:" + portfolioVo.getPolicyNo() + "invtNo :" + portfolioVo.getInvtNo());
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
}