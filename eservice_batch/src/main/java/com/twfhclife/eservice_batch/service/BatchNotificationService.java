package com.twfhclife.eservice_batch.service;

import com.twfhclife.eservice_batch.dao.IMessagingTemplateServiceImpl;
import com.twfhclife.eservice_batch.dao.RoiNotificationDao;
import com.twfhclife.eservice_batch.dao.TransFundNotificationDao;
import com.twfhclife.eservice_batch.dao.TransFundNotificationDtlDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.*;
import com.twfhclife.eservice_batch.util.CallApiMailCode;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MailTemplateUtil;
import com.twfhclife.eservice_batch.util.MathUtil;
import com.twfhclife.eservice_batch.util.RoiRateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class BatchNotificationService {

	private static final Logger logger = LogManager.getLogger(BatchNotificationService.class);
	
	private static final String INVT_FD = "FD";
	private static final String INVT_RT = "RT";
	
	private RoiNotificationDao dao;
	/**
	 * 開始執行停損停利通知
	 */
	public void excute() {
		/**
		 * 1. Query all notification data (TRANS_NOTIFICATION_JOB)
		 * 2. Mapping need to notification target (FD/RT)
		 * 3. Compute dividend 
		 * 4. Send Notification email
		 */
		Map<String, List<RoiNotificationVo>> mapSendMail = new HashMap<String, List<RoiNotificationVo>>();
		List<RoiNotificationVo> listNotification = new ArrayList<RoiNotificationVo>();
		//觀察中的投資標
		dao = new RoiNotificationDao();
		logger.info("============================================================================");
		logger.info("0. Start running send notification message.");
		logger.info("============================================================================");
		logger.info(" 前置動作: 更新 ROI_NOTIFICATION_JOB. ");
		if(!this.preBatchStartProcess()) {
			logger.info(" 更新 ROI_NOTIFICATION_JOB 失敗. ");
		}
		logger.info("End.");
		logger.info("============================================================================");
		logger.info("============================================================================");
		listNotification.addAll(this.queryAllNotificationData());
		if(listNotification.isEmpty()) {
			logger.info("目前沒有設置任何停損停利通知");
		} else {
			// 取出投資標的資訊
			logger.info("============================================================================");
			logger.info("1. Get invtment latest infomation by roi setting fund code start.");
			this.mappingNotificationData(listNotification);
			logger.info("End.");
			logger.info("============================================================================");
			// 計算報酬率
			logger.info("============================================================================");
			logger.info("2. Compute ROI every data start.");
			this.computeDividendRate(listNotification);
			logger.info("End.");
			logger.info("============================================================================");
			// 找出需要通知的 保單-標的 
			logger.info("============================================================================");
			logger.info("3. Find notification data and email content start.");
			mapSendMail.putAll(this.preSendMail(listNotification));
			logger.info("End.");
			logger.info("============================================================================");
			// 發送 Email 
			logger.info("============================================================================");
			logger.info("4. Send email start.");
			if(mapSendMail.values().size() > 0) {
				this.sendMail(mapSendMail);
			} else {
				logger.info("BatchNotificationService.excute() 本次沒有通知.");
			}
			logger.info("End.");
			logger.info("============================================================================");
		}
	}
	
	private Boolean preBatchStartProcess() {
		TransFundNotificationDtlDao transFundNotificationDtlDao = new TransFundNotificationDtlDao();
		TransFundNotificationDao transFundNotificationDao = new TransFundNotificationDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		// 處理舊通知設定
		if(!dao.updateAllDisable()) {
			logger.info(" 前置動作: 解除原始投報率警示失敗. ");
			return false;
		}
		logger.info(" 前置動作: 設置當天投保率警示 start. ");
		// 取得新通知設定
		logger.info(" 前置動作: 取得當天投保率警示設定 start. ");
		TransFundNotificationDtlVo transFundNotificationDtlVo = new TransFundNotificationDtlVo(); 
		List<TransFundNotificationDtlVo> listNewNoti = transFundNotificationDtlDao.queryTransFundNotificationDtl(transFundNotificationDtlVo);

		// insert ROI_NOTIFICATION_JOB
		logger.info(" 前置動作: 插入警示設定 start. ");
		for(TransFundNotificationDtlVo vo: listNewNoti) {
				RoiNotificationVo roiNotificationVo = new RoiNotificationVo();
			roiNotificationVo.setPolicyNo(vo.getPolicyNo());
				roiNotificationVo.setEnabled("Y");
				roiNotificationVo.setFundCode(vo.getFundCode());
				if (StringUtils.equals(vo.getType(), "1")) {
				Integer percentageUp = vo.getPercentageUp()==null? null: vo.getPercentageUp().intValue();
				roiNotificationVo.setPercentageUp(percentageUp);
				Integer percentageDown = vo.getPercentageDown()==null? null: vo.getPercentageDown().intValue();
				roiNotificationVo.setPercentageDown(percentageDown);
				} else {
				roiNotificationVo.setUpValue(vo.getUpValue());
				roiNotificationVo.setDownValue(vo.getDownValue());
				}
				if(dao.insertRoiNotificationJob(roiNotificationVo)) {
				logger.info("PolicyNo=" + vo.getPolicyNo() + ", FundCode=" + vo.getFundCode() + " 設定成功");
				} else {
				logger.info("PolicyNo=" + vo.getPolicyNo() + ", FundCode=" + vo.getFundCode() + " 設定失敗");
			}
		}
		logger.info(" 前置動作: 插入警示設定 end. ");
		logger.info(" 前置動作: 設置當天投保率警示 end. ");
		return true;
	}
	
	/**
	 * 取出所有設定停損停利通知資料
	 * @return 
	 */
	private List<RoiNotificationVo> queryAllNotificationData() {
		List<RoiNotificationVo> listNotification = new ArrayList<>();
		try {
			listNotification.addAll(dao.findEnableAll());
		} catch(Exception e) {
			logger.error("queryAllNotificationData error:", e);
		}
		return listNotification;
	}
	
	/**
	 * 依照標的代碼取出當前資訊
	 */
	private void mappingNotificationData(List<RoiNotificationVo> list) {
		/**
		 * 1. 取出標的最新淨值資訊
		 * 2. mapping 標的資訊
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		for(RoiNotificationVo vo: list) {
			InvestmentVo investmentVo = new InvestmentVo();
			investmentVo.setInsuNo(vo.getPolicyNo());
			investmentVo.setInvtNo(vo.getFundCode());
				vo.setPortfolioVo(dao.findPortfolioByInvestNo(investmentVo));
			InvestmentVo newInvestmentVo = dao.findFundByInvestNo(investmentVo);
			if (newInvestmentVo != null) {
				if (StringUtils.equals(newInvestmentVo.getInvtCurr(), "NTD")) {
					newInvestmentVo.setExchRate(BigDecimal.valueOf(1));
				}
				vo.setInvestmentVo(newInvestmentVo);
			}
		}
	}
	
	/**
	 * 計算投報率
	 */
	private void computeDividendRate(List<RoiNotificationVo> list) {
		/**
		 * FD: {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]-1}%
		 * RT: {[(帳戶金額*匯率)/平均台幣買價]-1}%
		 */
		List<RoiNotificationVo> tmpList = new ArrayList<RoiNotificationVo>();
		for(RoiNotificationVo vo: list) {
			if (vo.getPortfolioVo() != null) {
			MyPortfolioVo portfolioVo = vo.getPortfolioVo();
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
					values = this.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
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
			vo.setPortfolioVo(portfolioVo);
			}
			tmpList.add(vo);
		}
		list.clear();
		list.addAll(tmpList);
	}
	
	/**
	 * 發出 email 前準備動作
	 */
	private Map<String, List<RoiNotificationVo>> preSendMail(List<RoiNotificationVo> list) {
		Map<String, List<RoiNotificationVo>> mailMap = 
				new HashMap<String, List<RoiNotificationVo>>(); // Key: 保單號碼_標的類型(TEST000001_FD); Value: RoiNotificationVo
		List<RoiNotificationVo> listRoiNotification = null; // 達到通知標準的 FundCode

		for (RoiNotificationVo item : list) {
			boolean flag = false;
			String policyNo = StringUtils.trimToEmpty(item.getPolicyNo());
			String fundCode = StringUtils.trimToEmpty(item.getFundCode());
			String percentageUp = "";
			if(item.getPercentageUp() != null) {
				percentageUp = StringUtils.trimToEmpty(item.getPercentageUp().toString());
			}
			String percentageDown = "";
			if(item.getPercentageDown() != null) {
				percentageDown = StringUtils.trimToEmpty(item.getPercentageDown().toString());
			}
			
			// 如果保戶IDNO為空值, 離開
			if (policyNo.length() == 0 || fundCode.length() == 0) {
				continue;
			}

			boolean isNew;

			// 如果停利、停損皆為空值, 離開
			if (item.getDownValue() != null && item.getUpValue() != null) {
				isNew = true;
			} else if ((percentageUp.length() > 0 && percentageDown.length() > 0) && (!"0".equals(percentageUp) && !"0".equals(percentageDown))) {
				isNew = false;
			} else {
				continue;
			}

			// 開始比對
			try {
				if (isNew) {// 比對淨值
					flag = checkAssetValue(item);
				} else {
					if(checkROI(item)) {// 比對報酬率
						flag = true;
					}
				}
				// 若比對為 true, 表示有條件到滿足點
				if (flag) {
					if (mailMap.containsKey(policyNo)) {
						listRoiNotification = mailMap.get(policyNo);
					} else {
						listRoiNotification = new ArrayList<RoiNotificationVo>();
					}
					listRoiNotification.add(item);
					mailMap.put(policyNo, listRoiNotification);
				}
			} catch (Exception e) {
				logger.error("preSendMail error: ", e);
			} // try_catch_end
		}

		return mailMap;
	}

	private boolean checkAssetValue(RoiNotificationVo vo) {
		BigDecimal netValue = vo.getInvestmentVo().getNetValue();
		logger.info(vo.getPolicyNo() + "標的 " + vo.getFundCode()+ " 淨值: " + netValue);
		BigDecimal upValue = new BigDecimal(vo.getUpValue()==null? "99999": vo.getUpValue().toString());
		BigDecimal downValue = new BigDecimal(vo.getDownValue()==null? "99999": vo.getDownValue().toString());
		return this.doCompare(upValue, downValue.multiply(new BigDecimal(1)), netValue);
	}

	/**
	 * 發出 email
	 */
	private void sendMail(Map<String, List<RoiNotificationVo>> mapMail) {
		for(String policyNo: mapMail.keySet()) {
			// 取出該保戶EMail
			String email = StringUtils.trimToEmpty(dao.findEmailByPolicyNo(policyNo));
			List<String> listMail = new ArrayList<String>();
			listMail.add(email);
			if (email.length() == 0) {
				continue;
			}
			String content = this.getDynamicContent(mapMail.get(policyNo));
			// 寄發EMail
			try {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("FUND_NOTIFICATION_DATA", content);
				logger.info("paramMap:"+paramMap.toString());
				MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
				vo.setMessagingTemplateCode(CallApiMailCode.TRANSFER_MAIL_FUND_NOTIFICATION);
				vo.setSendType("email");
				vo.setMessagingReceivers(listMail);
				vo.setParameters(paramMap);
				vo.setSystemId("eservice_batch");
				logger.info("MessageTriggerRequestVo:"+vo.toString());
				IMessagingTemplateServiceImpl iMessagingTemplateService = new IMessagingTemplateServiceImpl();
				String resultMsg = iMessagingTemplateService.triggerMessageTemplate(vo);
				logger.info("send fund notification result: {}", resultMsg);
			} catch(Exception e) {
				logger.error("sendMail error: ", e);
				break;
			}
		}
	}
	
	/** 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}% */
	private BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
		BigDecimal[] values = new BigDecimal[3];
		if (netUnits != null && netValue != null && exchRate != null && ntdVal != null && accumAmt != null) {
			values = RoiRateUtil.formula1(netUnits, netValue, exchRate, ntdVal, accumAmt);
		} else {
			values = getZero();
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
	
	/** 取0 */
	private BigDecimal[] getZero() {
		BigDecimal zero = new BigDecimal("0");
		BigDecimal[] values = { zero, zero, zero };
		return values;
	}
	
	/** 比對報酬率 */
	private boolean checkROI(RoiNotificationVo vo) {
		// 計算該保戶+該投資標的之報酬率
		BigDecimal roiRate = vo.getPortfolioVo().getRoiRate();
		logger.info(vo.getPolicyNo() + "標的 " + vo.getFundCode()+ " 報酬率: " + roiRate);
		BigDecimal percentageUp = new BigDecimal(vo.getPercentageUp()==null? "99999": vo.getPercentageUp().toString());
		BigDecimal percentageDown = new BigDecimal(vo.getPercentageDown()==null? "99999": vo.getPercentageDown().toString());
		return this.doCompare(percentageUp, percentageDown.multiply(new BigDecimal(-1)), roiRate);
	}

	/** 進行比較: FD/RT */
	private boolean doCompare(BigDecimal percentageUp, BigDecimal percentageDown, BigDecimal value) {
		boolean flag = false;
		// 當滿足點發生, 即回傳true
		if (value.compareTo(percentageUp) >= 0 || value.compareTo(percentageDown) <= 0) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 組出動態停損停利table
	 * 
	 * @param list
	 */
	private String getDynamicContent(List<RoiNotificationVo> list) {

		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (RoiNotificationVo vo : list) {
			// 表頭
			if (vo.getPercentageDown() != null || vo.getPercentageUp() != null) {
				MyPortfolioVo portfolio = vo.getPortfolioVo();
				sb.append("<br/>☆已持有投資標的<br/>");
				sb.append("￭保單號碼：" + vo.getPolicyNo() + "<br/>");
				sb.append("￭類型：已持有 <br/>");
				sb.append("￭投資標的：" + portfolio.getInvtNo() + "<br/>");
				sb.append("￭幣別：" + StringUtils.trimToEmpty(portfolio.getInvtExchCurr()) + "<br/>");
				sb.append("￭投資收益等級：" + portfolio.getInvtRiskBeneLevel() + "<br/>");
				sb.append("￭幣別參考價值：" + portfolio.getAcctValue() + "<br/>");
				sb.append("￭參考報酬率百分比：" + portfolio.getRoiRate() + "<br/>");
				sb.append("￭現行的停利點：" + vo.getPercentageUp() + "<br/>");
				sb.append("￭現行的停損點：" + vo.getPercentageDown() + "<br/>");
				sb.append("￭通知日期：" + sdf.format(new Date()) + "<br/>");
			} else if (vo.getDownValue() != null || vo.getUpValue() != null) {
				InvestmentVo investmentVo = vo.getInvestmentVo();
				sb.append("<br/>☆觀察中投資標的<br/>");
				sb.append("￭保單號碼：" + vo.getPolicyNo() + "<br/>");
				sb.append("￭類型：觀察中<br/>");
				sb.append("￭投資標的：" + investmentVo.getInvtNo() + "<br/>");
				sb.append("￭淨值日：" + sdf.format(investmentVo.getInNetValueDate()) + "<br/>");
				sb.append("￭單位淨值：" + investmentVo.getNetValue() + "<br/>");
				sb.append("￭現行淨值上限：" + vo.getUpValue() + "<br/>");
				sb.append("￭現行淨值上限：" + vo.getDownValue() + "<br/>");
				sb.append("￭通知日期：" + sdf.format(new Date()) + "<br/>");
				}
			}
		return sb.toString();
	}
	
	/** 取得最新淨值、日期 */
	private Map<String, Object> getNetValue(String invtNo) {
		Map<String, Object> map = dao.findNetValueLatest(invtNo);
		return map;
	}
	
	/** 取得最新匯率、日期 */
	private Map<String, Object> getExchRate(String exchCode) {
		Map<String, Object> map = dao.findExchRateLatest(exchCode);
		return map;
	}
	
	/** 取得累計投資收益 */
	private Map<String, Object> getDividend(String insuNo, String invtNo) {
		Map<String, Object> map = dao.findAccumulation(insuNo, invtNo);
		return map;
	}
}
