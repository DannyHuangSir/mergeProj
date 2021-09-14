package com.twfhclife.eservice_batch.service;

import com.twfhclife.eservice_batch.dao.RoiNotificationDao;
import com.twfhclife.eservice_batch.dao.TransFundNotificationDao;
import com.twfhclife.eservice_batch.dao.TransFundNotificationDtlDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.*;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MailTemplateUtil;
import com.twfhclife.eservice_batch.util.MathUtil;
import com.twfhclife.eservice_batch.util.RoiRateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
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
		Map<String, List<TransFundNotificationDtlVo>> mapNotice = new HashMap<>();
		for(TransFundNotificationDtlVo vo: listNewNoti) {
			List<TransFundNotificationDtlVo> tmpList;
			Integer transNotificationId = vo.getTransFundNotificationId();
			if(mapNotice.get(transNotificationId.toString()) != null) {
				tmpList = mapNotice.get(transNotificationId.toString());
			} else {
				tmpList = new ArrayList<>();
			}
			tmpList.add(vo);
			mapNotice.put(transNotificationId.toString(), tmpList);
		}
		logger.info(" 前置動作: 取得當天投保率警示設定 end. ");
		
		// 取得通知設定保單
		logger.info(" 前置動作: 取得當天投保率警示設定對應保單 start. ");
		Map<String, List<TransFundNotificationDtlVo>> mapPolicyNotice = new HashMap<>();
		for(String notificationId: mapNotice.keySet()) {
			TransFundNotificationVo transFundNotificationVo = new TransFundNotificationVo();
			transFundNotificationVo.setId(Integer.parseInt(notificationId));
			List<TransFundNotificationVo> tmpList = transFundNotificationDao.queryTransFundNotification(transFundNotificationVo);
			if(tmpList != null && !tmpList.isEmpty()) {
				String transNum = tmpList.get(0).getTransNum();
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				List<TransPolicyVo> listResult = transPolicyDao.getTransPolicyList(transPolicyVo);
				if(listResult != null) {
					String policyNo = listResult.get(0).getPolicyNo();
					mapPolicyNotice.put(policyNo, mapNotice.get(notificationId));
				}
			}
		}
		logger.info(" 前置動作: 取得當天投保率警示設定對應保單 end. ");
		
		// insert ROI_NOTIFICATION_JOB
		logger.info(" 前置動作: 插入警示設定 start. ");
		for(String policyNo: mapPolicyNotice.keySet()) {
			List<TransFundNotificationDtlVo> list = mapPolicyNotice.get(policyNo);
			for(TransFundNotificationDtlVo vo: list) {
				RoiNotificationVo roiNotificationVo = new RoiNotificationVo();
				roiNotificationVo.setPolicyNo(policyNo);
				roiNotificationVo.setEnabled("Y");
				roiNotificationVo.setFundCode(vo.getFundCode());
				Integer percentageUp = vo.getPercentageUp()==null? null: vo.getPercentageUp().intValue();
				roiNotificationVo.setPercentageUp(percentageUp);
				Integer percentageDown = vo.getPercentageDown()==null? null: vo.getPercentageDown().intValue();
				roiNotificationVo.setPercentageDown(percentageDown);
				roiNotificationVo.setUpValue(vo.getUpValue());
				roiNotificationVo.setDownValue(vo.getDownValue());
				if(dao.insertRoiNotificationJob(roiNotificationVo)) {
					logger.info("PolicyNo=" + policyNo + ", FundCode=" + vo.getFundCode() + " 設定成功");
				} else {
					logger.info("PolicyNo=" + policyNo + ", FundCode=" + vo.getFundCode() + " 設定失敗");
				}
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
			investmentVo = dao.findFundByInvestNo(investmentVo, calendar.getTime());
			if (investmentVo != null) {
				vo.setPortfolioVo(dao.findPortfolioByInvestNo(investmentVo));
				if (StringUtils.equals(investmentVo.getInvtCurr(), "NTD")) {
					investmentVo.setExchRate(BigDecimal.valueOf(1));
				}
			}
			vo.setInvestmentVo(investmentVo);
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
			InvestmentVo investmentVo = vo.getInvestmentVo();
			if(investmentVo == null) {
				continue;
			}
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
				if (isNew) {// 比對净值
					flag = checkAssetValue(item);
				} else if(item.getFundCode().startsWith(INVT_FD)) {
					if(checkROI(item)) {// 比對報酬率
						flag = true;
					}
				} else if(item.getFundCode().startsWith(INVT_RT)){
					if(checkExchRate(item)) {// 比對匯率
						flag = true;
					}
				} else {
					// 都不滿足
					// do nothing
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
		logger.info(vo.getPolicyNo() + "標的 " + vo.getFundCode()+ " 净值: " + netValue);
		BigDecimal upValue = new BigDecimal(vo.getPercentageUp()==null? "99999": vo.getPercentageUp().toString());
		BigDecimal downValue = new BigDecimal(vo.getPercentageDown()==null? "99999": vo.getPercentageDown().toString());
		return this.doCompare(upValue, downValue.multiply(new BigDecimal(-1)), netValue);
	}

	/**
	 * 發出 email
	 */
	private void sendMail(Map<String, List<RoiNotificationVo>> mapMail) {
		MailService mailService = new MailService();
		Map<String, String> mapDynimicContent = new HashMap<String, String>();
		String templateContent = null;
		try {
			templateContent = MailTemplateUtil.getMailTempleteContent("notice_winloss_template.html");
		} catch(IOException e) {
			logger.info("");
		}
		for(String policyNo: mapMail.keySet()) {
			// 取出該保戶EMail
			String email = StringUtils.trimToEmpty(dao.findEmailByPolicyNo(policyNo));
			List<String> listMail = new ArrayList<String>();
			listMail.add(email);
			if (email.length() == 0) {
				continue;
			}
			String subject = "投資輔助-停利停損通知";
			mapDynimicContent = this.getDynamicContent(mapMail.get(policyNo));
			String content = templateContent;
			for(String replaceKey: mapDynimicContent.keySet()) {
				content = content.replaceAll(replaceKey, mapDynimicContent.get(replaceKey));
			}
			// 寄發EMail
			try {
				mailService.sendMail(content, subject, listMail, null, null);
				// 儲存郵件簡訊發送紀錄
				BatchApiService apiService = new BatchApiService();
				for (String addr : listMail) {
					apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, content));
				}
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
		roiRate = MathUtil.mul(roiRate, new BigDecimal(100)); // 投資報酬率需*100
		logger.info(vo.getPolicyNo() + "標的 " + vo.getFundCode()+ " 報酬率: " + roiRate);
		BigDecimal percentageUp = new BigDecimal(vo.getPercentageUp()==null? "99999": vo.getPercentageUp().toString());
		BigDecimal percentageDown = new BigDecimal(vo.getPercentageDown()==null? "99999": vo.getPercentageDown().toString());
		return this.doCompare(percentageUp, percentageDown.multiply(new BigDecimal(-1)), roiRate);
	}
	
	/** 檢查匯率 */
	private boolean checkExchRate(RoiNotificationVo vo) {
		boolean flag = false;
		InvestmentVo investmentVo = vo.getInvestmentVo();
		MyPortfolioVo portfolioVo = vo.getPortfolioVo();
		BigDecimal exchRate = portfolioVo.getExchRateBuy(); // 匯率
		BigDecimal maxBigdecimal = null;
		BigDecimal minBigdecimal = null;

		if(vo.getPercentageUp() != null) {
			String max = StringUtils.trimToEmpty(vo.getPercentageUp().toString()); // 保戶設定值
			maxBigdecimal = new BigDecimal(max);
		}
		if(vo.getPercentageDown() != null) {
			String min = StringUtils.trimToEmpty(vo.getPercentageDown().toString()); // 保戶設定值
			minBigdecimal = new BigDecimal(min);
		}
		exchRate = investmentVo.getExchRate(); // 買價
		flag = this.doCompare(maxBigdecimal, minBigdecimal, exchRate);
		return flag;
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
	private Map<String, String> getDynamicContent(List<RoiNotificationVo> list) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		MyPortfolioVo portfolio = null;
		InvestmentVo investmentVo = null;

		String lastInsuNo = "";
		boolean firstInvt = false;

		for (RoiNotificationVo vo : list) {
			portfolio = vo.getPortfolioVo();
			investmentVo = vo.getInvestmentVo();
			
			String insuCurr = StringUtils.trimToEmpty(investmentVo.getInvtCurr());
			String insuCurrName = StringUtils.trimToEmpty(investmentVo.getInvtCurr());
			String prodName = StringUtils.trimToEmpty(investmentVo.getName());
			
			if (!lastInsuNo.equals(vo.getPolicyNo())) {
				sb.append("<tr>");
				sb.append("<td colspan=\"8\" align=\"left\" bgcolor=\"#FFFFFF\" class=\"txt12\">");
				sb.append("<b>保單號碼</b>：" + vo.getPolicyNo() + "&nbsp;&nbsp;<b>保單幣別</b>：" + insuCurrName + "&nbsp;&nbsp;<b>主約險種名稱</b>：" + prodName + "</td>");
				sb.append("</tr>");
				firstInvt = true;
			}
			
			// 表頭
			if (firstInvt) {
				sb.append("<tr align=\"center\" class=\"table_th\">");
				sb.append("<th rowspan=\"2\">投資標的代碼</th>");
				sb.append("<th rowspan=\"2\">投資標的名稱</th>");
				sb.append("<th rowspan=\"2\">投資標的<br>幣別</th>");
				sb.append("<th rowspan=\"2\">投資收益<br>等級</th>");
				sb.append("<th rowspan=\"2\">參考保單<br>幣別價值</th>");
				sb.append("<th rowspan=\"2\">參考<br>報酬率(%)</th>");
				sb.append("<th colspan=\"2\">投資報酬率</th>");
				sb.append("</tr>");
				sb.append("<tr align=\"center\" class=\"table_th2\">");
				sb.append("<th width=\"8%\">停利點</th>");
				sb.append("<th width=\"8%\">停損點</th>");
				sb.append("</tr>");
			}
			sb.append("<tr align=\"center\">");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + investmentVo.getInvestNo() + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\" align=\"left\">" + investmentVo.getName() + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + investmentVo.getInvtCurr() + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + (StringUtils.trimToEmpty(investmentVo.getRiskBeneLevel()).length() == 0 ? "--" : investmentVo.getRiskBeneLevel()) + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + ("NTD".equals(investmentVo.getInvtCurr()) ? investmentVo.getNetAmt().setScale(0, BigDecimal.ROUND_HALF_UP) : investmentVo.getNetAmt().setScale(2, BigDecimal.ROUND_HALF_UP)) + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + MathUtil.mul(portfolio.getRoiRate(), new BigDecimal(100)) + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + (StringUtils.trimToEmpty(vo.getPercentageUp().toString()).length() == 0 ? "--" : " +(正)&nbsp;" + vo.getPercentageUp() + "%") + "</td>");
			sb.append("<td bgcolor=\"#ffffff\" class=\"txt12\">" + (StringUtils.trimToEmpty(vo.getPercentageDown().toString()).length() == 0 ? "--" : "-(負)&nbsp;" + vo.getPercentageDown() + "%") + "</td>");
			sb.append("</tr>");

			firstInvt = false; // 設定非每個商品的第一個投資標的
			lastInsuNo = vo.getPolicyNo(); // 設定這次的商品代碼
		}

		map.put("[@LIST@]", sb.toString());
		return map;
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
