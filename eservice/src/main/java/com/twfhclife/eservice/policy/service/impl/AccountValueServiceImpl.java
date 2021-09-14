package com.twfhclife.eservice.policy.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.twfhclife.eservice.policy.dao.FundPcoiDao;
import com.twfhclife.eservice.policy.dao.FundPrdtDao;
import com.twfhclife.eservice.policy.dao.FundTransactionDao;
import com.twfhclife.eservice.policy.dao.PolicyAccountValueDao;
import com.twfhclife.eservice.policy.dao.PolicyExtraDao;
import com.twfhclife.eservice.policy.dao.PolicyListDao;
import com.twfhclife.eservice.policy.model.AcctValuePdfVo;
import com.twfhclife.eservice.policy.model.AcctValueVo;
import com.twfhclife.eservice.policy.model.FundPcoiVo;
import com.twfhclife.eservice.policy.model.FundPrdtVo;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.model.PolicyAccountValueVo;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IAccountValueService;
import com.twfhclife.eservice.user.dao.LilipmDao;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.generic.util.PdfUtil;

/**
 * 帳戶價值服務.
 * 
 * @author alan
 */
@Service
public class AccountValueServiceImpl implements IAccountValueService {

	private static final Logger logger = LogManager.getLogger(AccountValueServiceImpl.class);
	
	@Autowired
	public PolicyListDao policyListDao;
	
	@Autowired
	public PolicyExtraDao policyExtraDao;

	@Autowired
	private LilipmDao lilipmDao;
	
	@Autowired
	private FundPrdtDao fundPrdtDao;
	
	@Autowired
	private FundPcoiDao fundPcoiDao;
	
	@Autowired
	private FundTransactionDao fundTransactionDao;
	
	@Autowired
	private PolicyAccountValueDao policyAccountValueDao;

	/**
	 * 取得帳戶價值通知書清單.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳該保單號碼下的所有帳戶價值通知書清單.
	 */
	@Override
	public List<AcctValueVo> getAccountValueList(String policyNo) {
		List<AcctValueVo> acctValueList = new ArrayList<>();
		
		// 日期條件
		String rocDate = DateUtil.getRocDate(new Date());
		String rocYear = rocDate.substring(0, 3);
		String yyyy = Integer.parseInt(rocYear) + 1911 + "";
		String nextYyyy = Integer.parseInt(yyyy) + 1 + "";
		
		// 預設顯示當年4個季度資料
		String[] quaterCodeArray = new String[] { rocYear + "Q1", rocYear + "Q2", rocYear + "Q3", rocYear + "Q4" };
		
		// 下一季期初餘額資料有才能下載
		List<FundTransactionVo> invtList = fundTransactionDao.getInvtListByPolicyNo(policyNo);
		if (invtList != null && invtList.size() > 0) {
			for (String quaterCode : quaterCodeArray) {
				String yyy = quaterCode.substring(0, 3);
				String quaterNumber = quaterCode.substring(4, 5);
				
				String startDate = "";
				if ("1".equals(quaterNumber)) {
					startDate = yyyy + "-04-01";
				}
				if ("2".equals(quaterNumber)) {
					startDate = yyyy + "-07-01";
				}
				if ("3".equals(quaterNumber)) {
					startDate = yyyy + "-10-01";
				}
				// 當年Q4，要用下一年Q1的條件
				if ("4".equals(quaterNumber)) {
					startDate = nextYyyy + "-01-01";
				}
				
				// 是否有下一季的期初餘額
				boolean haseNextQuaterCodeData = false;
				for (int i = 0; i < invtList.size(); i++) {
					FundTransactionVo fundTransactionVo = invtList.get(i);
					FundTransactionVo openingBalanceData = fundTransactionDao.getOpeningBalance(startDate, policyNo,
							fundTransactionVo.getInvtNo());
					if (openingBalanceData != null) {
						haseNextQuaterCodeData = true;
						break;
					}
				}
				
				if (haseNextQuaterCodeData) {
					logger.info("Find {} openingBalance for {}", quaterCode, policyNo);
					AcctValueVo vo = new AcctValueVo();
					vo.setPolicyNo(policyNo);
					vo.setQuaterCode(quaterCode);
					vo.setQuaterName(String.format("民國%s年第0%s季", yyy, quaterNumber));
					acctValueList.add(vo);
				} else {
					logger.info("Unable ot find {} openingBalance for {}", quaterCode, policyNo);
				}
			}
		}
		
		return acctValueList;
	}

	/**
	 * 取得帳戶價值通知書PDF資料.
	 * 
	 * @param policyNo 保單號碼
	 * @param quaterCode 年份季別(例:107Q1)
	 * @return 回傳帳戶價值通知書PDF資料.
	 */
	public byte[] getAccountValueData(String policyNo, String quaterCode) {
		byte[] pdfByte = null;
		try {
			String yyy = quaterCode.substring(0, 3);
			String yyyy = Integer.parseInt(yyy) + 1911 + "";
			String startDate = "";
			String endDate = "";
			String twStartDate = "";
			String twEndDate = "";
			String quaterNumber = quaterCode.substring(4, 5);
			
			// 設定季度的日期條件
			if ("1".equals(quaterNumber)) {
				startDate = yyyy + "-01-01";
				endDate = yyyy + "-03-31";
				twStartDate = String.format("%s年01月01日", yyy);
				twEndDate = String.format("%s年03月31日", yyy);
			}
			if ("2".equals(quaterNumber)) {
				startDate = yyyy + "-04-01";
				endDate = yyyy + "-06-30"; 
				twStartDate = String.format("%s年04月01日", yyy);
				twEndDate = String.format("%s年06月30日", yyy);
			}
			if ("3".equals(quaterNumber)) {
				startDate = yyyy + "-07-01";
				endDate = yyyy + "-09-30"; 
				twStartDate = String.format("%s年07月01日", yyy);
				twEndDate = String.format("%s年09月30日", yyy);
			}
			if ("4".equals(quaterNumber)) {
				startDate = yyyy + "-10-01";
				endDate = yyyy + "-12-31"; 
				twStartDate = String.format("%s年10月01日", yyy);
				twEndDate = String.format("%s年12月31日", yyy);
			}
			PdfUtil pdfUtil = new PdfUtil();
			
			// 取得取得保單帳戶價值
			List<PolicyAccountValueVo> quaterDataList = policyAccountValueDao.getQuaterPolicyAccountValueList(policyNo,
					startDate, endDate);

			// 產生帳戶價值通知書的表頭用戶資訊
			genAccountValueHeader(pdfUtil, policyNo, quaterDataList);

			// 產生帳戶價值通知書page1
			genAccountValuePage1(pdfUtil, twEndDate, quaterDataList);
			
			// 產生帳戶價值通知書page2
			genAccountValuePage2(pdfUtil, policyNo, twStartDate, twEndDate, startDate, endDate);
			
			// 產生帳戶價值通知書page
			genAccountValuePage3(pdfUtil, policyNo, twStartDate, twEndDate, startDate, endDate);
			pdfByte = pdfUtil.closeDoc().pageHF();
		} catch (Exception e) {
			logger.error("Unable to getAccountValueData: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}
	
	/**
	 * 產生帳戶價值通知書的表頭用戶資訊.
	 * 
	 * @param pdfUtil PdfUtil
	 * @param policyNo 保單號碼
	 * @param quaterDataList 保單帳戶價值資料
	 * @throws DocumentException
	 */
	private void genAccountValueHeader(PdfUtil pdfUtil, String policyNo, List<PolicyAccountValueVo> quaterDataList)
			throws DocumentException {
		DecimalFormat df1 = new DecimalFormat("#,##0");
		PolicyListVo qryVo = new PolicyListVo();
		qryVo.setPolicyNo(policyNo);
		List<PolicyListVo> policyList = policyListDao.getPolicyList(qryVo);
		
		if (policyList != null && policyList.size() == 1) {
			PolicyListVo policyListVo = policyListDao.getPolicyList(qryVo).get(0);
			
			// 要保人
			String customerName = "";
			LilipmVo lilipmVo = lilipmDao.findByPolicyNo(policyNo);
			if (lilipmVo != null) {
				customerName = lilipmVo.getLipmName1();
			}
			
			// 貸款金額
			BigDecimal loanAmount = BigDecimal.ZERO;
			PolicyExtraVo policyExtraVo = policyExtraDao.findByPolicyNo(policyNo);
			if (policyExtraVo != null) {
				loanAmount = policyExtraVo.getLoanAmount();
			}
			
			AcctValuePdfVo vo = new AcctValuePdfVo();
			vo.setPolicyNo(policyNo);
			vo.setProductName(StringUtils.trimToEmpty(policyListVo.getProductName()));
			vo.setEffectivedate(policyListVo.getEffectiveDate());
			vo.setCustomerName(StringUtils.trimToEmpty(customerName));
			vo.setInsuredName(StringUtils.trimToEmpty(policyListVo.getMainInsuredName()));
			vo.setPolicyType("");
			vo.setMainAmount(policyListVo.getMainAmount());
			vo.setLoanAmount(loanAmount);
			vo.setIntAmount(BigDecimal.ZERO);
			vo.setDeathInsuranceAmount(BigDecimal.ZERO);
			
			// 表頭
			pdfUtil.txtC(String.format("%s帳戶價值通知單", vo.getProductName()));
			pdfUtil.newLine();
			// 保單資訊
			String[] effectivedate = DateUtil.getRocDate(vo.getEffectivedate()).split("/");
			
			pdfUtil.tableLR(
					String.format("保單號碼：%s", policyNo),
					String.format("投保始期：%s", String.format("%s年%s月%s日", effectivedate[0], effectivedate[1], effectivedate[2])));
			pdfUtil.tableLR(
					String.format("要 保 人：%s", vo.getCustomerName()),
					String.format("被 保 人：%s", vo.getInsuredName()));
			pdfUtil.tableLR(
					String.format("保險型態：%s", vo.getPolicyType()),
					String.format("保險金額：%s", df1.format(vo.getMainAmount()) + "元"));
			try {
				//修正疑似因資料問題格式化失敗
				pdfUtil.tableLR(
						String.format("保單借款本金：%s", StringUtils.leftPad(df1.format(vo.getLoanAmount()) + "元", 11, " ")),
						String.format("保單借款利息累計：%s", StringUtils.leftPad(df1.format(vo.getIntAmount()) + "元", 11, " ")));
			} catch (Exception e) {
				if (vo != null) {
					logger.info("format error: vo.getLoanAmount(): {}", vo.getLoanAmount());
					logger.info("format error: vo.getIntAmount(): {}", vo.getIntAmount());
				}
				pdfUtil.tableLR(
						String.format("保單借款本金：%s", StringUtils.leftPad("元", 11, " ")),
						String.format("保單借款利息累計：%s", StringUtils.leftPad("元", 11, " ")));
			}
			
			// 身故保險金額 = 保險金額 + 帳戶價值
			BigDecimal deathInsuranceAmount = vo.getMainAmount();
			for (PolicyAccountValueVo rowVo : quaterDataList) {
				double acctValue = getDoubleValue(rowVo.getPoevSafpUnits()) * 
						getDoubleValue(rowVo.getPoevInprRate()) * getDoubleValue(rowVo.getPoevStateExchRate());
				deathInsuranceAmount = deathInsuranceAmount.add(BigDecimal.valueOf(acctValue));
			}
			
			pdfUtil.tableLR(
					String.format("身故保險金額：%s", StringUtils.leftPad(df1.format(deathInsuranceAmount) + "元", 11, " ")), "");
			pdfUtil.newLine();
		}
	}
	
	/**
	 * 產生帳戶價值通知書page1.
	 * 
	 * @param pdfUtil PdfUtil
	 * @param twEndDate 該季民國年的結束日期
	 * @param quaterDataList 保單帳戶價值資料
	 * @throws DocumentException
	 */
	private void genAccountValuePage1(PdfUtil pdfUtil, String twEndDate, List<PolicyAccountValueVo> quaterDataList)
			throws DocumentException {
		DecimalFormat df2 = new DecimalFormat("#,##0.00");
		
		// 帳戶價值明細
		pdfUtil.txt(String.format("＊至 %s 止保單帳戶價值明細：", twEndDate));
		pdfUtil.newLine();
		
		pdfUtil.txt("投資標的                               平均成本     單位數/金額     參考單位淨值      幣別        參考匯率");
		pdfUtil.txt("                                           A              B               C                           D");
		pdfUtil.txt("                                                                    參考價戶價值    未實現損益   參考報酬率(%)");
		pdfUtil.txt("                                                                      E=B*C*D         F=E-A         G=F/A");
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		
		double sumPoevAvgPntdval = 0;
		double sumAcctValue = 0;
		double sumCost = 0;
		
		for (PolicyAccountValueVo rowVo : quaterDataList) {
			String fundName = rowVo.getFundName();
			String fundNameLine1 = "";
			String fundNameLine2 = "";
			
			int fundNameSize = MyStringUtil.countDobuleSize(fundName);
			if (fundNameSize > 34) {
				String[] fundNameArray = splitFundName(fundName, 30);
				fundNameLine1 = fundNameArray[0];
				fundNameLine2 = fundNameArray[1];
			} else {
				fundNameLine1 = fundName;
				fundNameLine2 = "";
			}
			
			// 平均成本
			double doevAvgPntdval = getDoubleValue(rowVo.getPoevAvgPntdval());
			sumPoevAvgPntdval += doevAvgPntdval;
			
			// 帳戶價值
			double acctValue = getDoubleValue(rowVo.getPoevSafpUnits()) * 
					getDoubleValue(rowVo.getPoevInprRate()) * getDoubleValue(rowVo.getPoevStateExchRate());
			sumAcctValue += acctValue;
			
			// 未實現損益
			double cost = getDoubleValue(rowVo.getCost());
			sumCost += cost;
			
			// 參考報酬率(G=F/A)
			double gValue = 0.0;
			if (rowVo.getPoevAvgPntdval() != null && rowVo.getPoevAvgPntdval().intValue() != 0) {
				double division = rowVo.getPoevAvgPntdval().doubleValue();
				gValue = cost / division;
			}
			
			// 第一行
			pdfUtil.txt(MyStringUtil.rpad(fundNameLine1, 35, " ") 
					+ MyStringUtil.lpad(formatNumber(rowVo.getPoevAvgPntdval(), df2), 12, " ")
					+ MyStringUtil.lpad(formatNumber(rowVo.getPoevSafpUnits(), df2), 16, " ")
					+ MyStringUtil.lpad(formatNumber(rowVo.getPoevInprRate(), df2), 16, " ")
					+ MyStringUtil.lpad(rowVo.getCurrency(), 9, " ")
					+ MyStringUtil.lpad(formatNumber(rowVo.getPoevStateExchRate(), df2), 17, " "));
			
			// 第二行
			pdfUtil.txt(MyStringUtil.rpad(fundNameLine2, 35, " ") 
					+ MyStringUtil.lpad("", 12, " ")
					+ MyStringUtil.lpad("", 16, " ")
					+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(acctValue), df2), 16, " ")
					+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(cost), df2), 15, " ")
					+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(gValue), df2), 13, " ") + "%");
		}
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		
		double sumGValue = 0;
		if (sumPoevAvgPntdval != 0) {
			sumGValue = (sumCost / sumPoevAvgPntdval);
		}
		logger.info("sumCost: {}", sumCost);
		logger.info("sumPoevAvgPntdval: {}", sumPoevAvgPntdval);
		logger.info("sumCost / sumPoevAvgPntdval: {}", sumGValue);
		
		pdfUtil.txt(MyStringUtil.rpad("總計", 35, " ") 
				+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sumPoevAvgPntdval), df2), 12, " ")
				+ MyStringUtil.lpad("", 16, " ")
				+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sumAcctValue), df2), 16, " ")
				+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sumCost), df2), 15, " ")
				+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sumGValue), df2), 13, " ") + "%");
		
		// 注意事項
		pdfUtil.newLine();
		pdfUtil.txt("※注意事項");
		pdfUtil.txt("1.本對帳單之金額、單位數、帳戶價值、未實現損益為各基準日當時之金額，若其後有異動則以實際金額為準。");
		pdfUtil.txt("2.期末淨現金解約價值等於期末保單帳戶價值加計按日數比例計算已扣除未到期保險成本。");
		pdfUtil.txt("3.本險投資標的年度財務報表請至本公司網站http://www.twfhclife.com.tw/documents/f_reports.htm查詢。 ");
		pdfUtil.txt("4.本公司「金采人生變額萬能壽險」連結之元大寶來系列基金自103年11月3日起依照新版基金風險報酬等級修正調整，調整情"); 
		pdfUtil.txt("  形如下（括號表示異動後之風險等級）：「元大寶來經貿基金」RR5（RR4）、「元大寶來新主流基金」RR5（RR4）、「元大"); 
		pdfUtil.txt("  寶來2001基金」RR5（RR4）、「元大寶來台灣加權股價指數基金」RR5（RR4）。"); 
		pdfUtil.txt("5.本公司「金采人生變額萬能壽險」連結之投資標的「摩根國際債券及貨幣基金」自103年12月31日起撤銷在台募集與銷售。"); 
		pdfUtil.txt("6.本公司「金采人生變額萬能壽險」連結之保德信系列基金自103年11月1日起依照新版基金風險報酬等級修正調整，調整情形");
		pdfUtil.txt("  如下（括號表示異動後之風險等級）：「保德信亞太基金」RR4（RR5）、「保德信大中華基金」RR4（RR5）。"); 
		pdfUtil.txt("7.本公司「金采人生變額萬能壽險」連結之施羅德系列基金自104年1月1日起依照新版基金風險報酬等級修正調整，調整情形如");
		pdfUtil.txt("  下（括號表示異動後之風險等級）：「施羅德環球基金系列新興亞洲A1累積」RR4（RR5）、「施羅德環球基金系列環球能源");
		pdfUtil.txt("  基金A1累積」RR5（RR4）。");  
		pdfUtil.txt("8.如有疑問，請洽詢免付費電話：0800-011-966保戶服務中心。");
	}
	
	/**
	 * 產生帳戶價值通知書page2.
	 * 
	 * @param pdfUtil PdfUtil
	 * @param policyNo 保單號碼
	 * @param twStartDate 該季民國年的開始日期
	 * @param twEndDate 該季民國年的結束日期
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @throws DocumentException
	 */
	private void genAccountValuePage2(PdfUtil pdfUtil, String policyNo, String twStartDate, String twEndDate,
			String startDate, String endDate) throws DocumentException {
		DecimalFormat df1 = new DecimalFormat("#,##0");
		// page2
		pdfUtil.newPage();
		pdfUtil.txt(String.format("＊ %s至 %s 止單位異動情形：", twStartDate, twEndDate));
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		pdfUtil.newLine();
		pdfUtil.txt("投資標的/幣別");
		pdfUtil.txt("交易日期     交易項目          金額(NT)      單位淨值   淨值日期       匯率      匯率日期       單位數/金額");
		pdfUtil.txt("                                K=H*I*J          H                       I                           J");
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		
		// 取得保單的投資標的
		List<FundTransactionVo> invtList = fundTransactionDao.getInvtListByPolicyNo(policyNo);
		if (invtList != null && invtList.size() > 0) {
			for (int i = 0; i < invtList.size(); i++) {
				FundTransactionVo fundTransactionVo = invtList.get(i);
				pdfUtil.txt(fundTransactionVo.getInvtName());
				
				// 期初餘額
				FundTransactionVo openingBalanceData = fundTransactionDao.getOpeningBalance(startDate, policyNo, fundTransactionVo.getInvtNo());
				if (openingBalanceData != null) {
					String trDate = DateUtil.getRocDate(openingBalanceData.getSadpTrDate()).replaceAll("/", "");
					String tripDate = DateUtil.getRocDate(openingBalanceData.getSadpTripDate()).replaceAll("/", "");
					String exchDate = DateUtil.getRocDate(openingBalanceData.getSadpExchDate()).replaceAll("/", "");
					
					double sadpApplyAmt = getDoubleValue(openingBalanceData.getSadpApplyAmt());
					double fundVlaue = getDoubleValue(openingBalanceData.getFundValue());
					double exchRate = getDoubleValue(openingBalanceData.getExchangeRate());
					double lefbf = getDoubleValue(openingBalanceData.getSadpSacntLefbf());
					
					pdfUtil.txt(MyStringUtil.rpad(trDate, 8, " ") 
							+ MyStringUtil.rpad("期初餘額", 19, " ")
							+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sadpApplyAmt), df1), 12, " ")
							+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(fundVlaue), df1), 14, " ")
							+ MyStringUtil.lpad(tripDate, 12, " ")
							+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(exchRate), df1), 11, " ")
							+ MyStringUtil.lpad(exchDate, 13, " ")
							+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(lefbf), df1), 18, " "));
				} else {
					logger.info("Unable ot find {} openingBalance for {}", fundTransactionVo.getInvtNo(), policyNo);
				}
				
				// 續期
				List<FundTransactionVo> interimList = fundTransactionDao.getInterimBalance(startDate, endDate, policyNo, fundTransactionVo.getInvtNo());
				if (interimList != null && interimList.size() > 0) {
					for (FundTransactionVo interimBalanceData :interimList) {
						String trDate = DateUtil.getRocDate(interimBalanceData.getSadpTrDate()).replaceAll("/", "");
						String trName = interimBalanceData.getSadpTrName();
						String trCode = interimBalanceData.getSadpTrCode();
						String tripDate = DateUtil.getRocDate(interimBalanceData.getSadpTripDate()).replaceAll("/", "");
						String exchDate = DateUtil.getRocDate(interimBalanceData.getSadpExchDate()).replaceAll("/", "");
						
						double sadpApplyAmt = getDoubleValue(interimBalanceData.getSadpApplyAmt());
						double fundVlaue = getDoubleValue(interimBalanceData.getFundValue());
						double exchRate = getDoubleValue(interimBalanceData.getExchangeRate());
						double lefbf = getDoubleValue(interimBalanceData.getSadpSacntLefbf());
						
						if ("INV201".equals(trCode)) {
							FundPcoiVo qryFundPcoiVo = new FundPcoiVo();
							qryFundPcoiVo.setPcoiInsuNo(policyNo);
							qryFundPcoiVo.setPcoiBookDate(interimBalanceData.getSadpTrDate());
							
							List<FundPcoiVo> fundPcoiList = fundPcoiDao.getFundPcoi(qryFundPcoiVo);
							if (fundPcoiList != null && fundPcoiList.size() > 0) {
								for (FundPcoiVo fundPcoiVo : fundPcoiList) {
									pdfUtil.txt(MyStringUtil.rpad(trDate, 8, " ") 
											+ MyStringUtil.rpad("扣收保險成本", 19, " ")
											+ MyStringUtil.lpad(formatNumber(fundPcoiVo.getPcoiInsuCost(), df1), 12, " ")
											+ MyStringUtil.lpad("", 14, " ")
											+ MyStringUtil.lpad("", 12, " ")
											+ MyStringUtil.lpad("", 11, " ")
											+ MyStringUtil.lpad("", 13, " ")
											+ MyStringUtil.lpad("", 18, " "));
									pdfUtil.txt(MyStringUtil.rpad("", 8, " ") 
											+ MyStringUtil.rpad("扣收管理費用", 19, " ")
											+ MyStringUtil.lpad(formatNumber(fundPcoiVo.getPcoiMonthCharge(), df1), 12, " ")
											+ MyStringUtil.lpad("", 14, " ")
											+ MyStringUtil.lpad("", 12, " ")
											+ MyStringUtil.lpad("", 11, " ")
											+ MyStringUtil.lpad("", 13, " ")
											+ MyStringUtil.lpad("", 18, " "));
									pdfUtil.txt(MyStringUtil.rpad("", 8, " ") 
											+ MyStringUtil.rpad("總計", 19, " ")
											+ MyStringUtil.lpad(formatNumber(fundPcoiVo.getPcoiAmount(), df1), 12, " ")
											+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(fundVlaue), df1), 14, " ")
											+ MyStringUtil.lpad(tripDate, 12, " ")
											+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(exchRate), df1), 11, " ")
											+ MyStringUtil.lpad(exchDate, 13, " ")
											+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(lefbf), df1), 18, " "));
								}
							}
						} else {
							pdfUtil.txt(MyStringUtil.rpad(trDate, 8, " ") 
									+ MyStringUtil.rpad(trName, 19, " ")
									+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(sadpApplyAmt), df1), 12, " ")
									+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(fundVlaue), df1), 14, " ")
									+ MyStringUtil.lpad(tripDate, 12, " ")
									+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(exchRate), df1), 11, " ")
									+ MyStringUtil.lpad(exchDate, 13, " ")
									+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(lefbf), df1), 18, " "));
						}
					}
				} else {
					logger.info("Unable ot find {} interimBalance for {}", fundTransactionVo.getInvtNo(), policyNo);
				}
				
				// 期末餘額
				FundTransactionVo endingBalanceData = fundTransactionDao.getEndingBalance(endDate, policyNo, fundTransactionVo.getInvtNo());
				if (endingBalanceData != null) {
					String trDate = DateUtil.getRocDate(endingBalanceData.getSadpTrDate()).replaceAll("/", "");
					double lefbf = getDoubleValue(endingBalanceData.getSadpSacntLefbf());
					pdfUtil.txt(MyStringUtil.rpad(trDate, 8, " ") 
							+ MyStringUtil.rpad("期末餘額", 19, " ")
							+ MyStringUtil.lpad("", 12, " ")
							+ MyStringUtil.lpad("", 14, " ")
							+ MyStringUtil.lpad("", 12, " ")
							+ MyStringUtil.lpad("", 11, " ")
							+ MyStringUtil.lpad("", 13, " ")
							+ MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(lefbf), df1), 18, " "));
				} else {
					logger.info("Unable ot find {} endingBalance for {}", fundTransactionVo.getInvtNo(), policyNo);
				}
				
				if (i != (invtList.size() - 1)) {
					pdfUtil.txt("-------------------------------------------------------------------------------------------------------------");
				}
			}
			pdfUtil.txt("_____________________________________________________________________________________________________________");
		}
	}
	
	/**
	 * 產生帳戶價值通知書page3.
	 * 
	 * @param pdfUtil PdfUtil
	 * @param policyNo 保單號碼
	 * @param twStartDate 該季民國年的開始日期
	 * @param twEndDate 該季民國年的結束日期
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @throws DocumentException
	 */
	private void genAccountValuePage3(PdfUtil pdfUtil, String policyNo, String twStartDate, String twEndDate,
			String startDate, String endDate) throws DocumentException {
		DecimalFormat df1 = new DecimalFormat("#,##0");
		// page3
		pdfUtil.newPage();
		pdfUtil.txt(String.format("＊ %s至 %s 止交易項目明細：", twStartDate, twEndDate));
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		pdfUtil.txt("  日期      交易項目        交易明細              交易金額            明細金額");
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		
		FundPrdtVo qryFundPrdtVo = new FundPrdtVo();
		qryFundPrdtVo.setPrdtInsuNo(policyNo);
		qryFundPrdtVo.setStartDate(startDate);
		qryFundPrdtVo.setEndDate(endDate);
		
		List<FundPrdtVo> fundPrdtList = fundPrdtDao.getFundPrdt(qryFundPrdtVo);
		if (fundPrdtList != null && fundPrdtList.size() > 0) {
			for (int i = 0; i < fundPrdtList.size(); i++) {
				FundPrdtVo fundPrdtVo = fundPrdtList.get(i);
				String rocBookDate = DateUtil.getRocDate(fundPrdtVo.getPrdtBookDate()).replaceAll("/", "");
				
				if (fundPrdtVo.getPrdtRcpAmt() != null) {
					double prdtRcpAmt = getDoubleValue(fundPrdtVo.getPrdtRcpAmt());
					pdfUtil.txt(rocBookDate + "     續期保費" + MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(prdtRcpAmt), df1), 38, " "));
				}
				if (fundPrdtVo.getPrdtIncreLoading() != null) {
					double prdtIncreLoading = 0 - getDoubleValue(fundPrdtVo.getPrdtIncreLoading());
					pdfUtil.txt("                            增額保險費費用" + MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(prdtIncreLoading), df1), 36, " "));
				}
				if (fundPrdtVo.getPrdtInvestAmt() != null) {
					double prdtInvestAmt = getDoubleValue(fundPrdtVo.getPrdtInvestAmt());
					pdfUtil.txt("                            投入金額" + MyStringUtil.lpad(formatNumber(BigDecimal.valueOf(prdtInvestAmt), df1), 42, " "));
				}
			
				if (i != (fundPrdtList.size() - 1)) {
					pdfUtil.txt("-------------------------------------------------------------------------------------------------------------");
				}
			}
		} else {
			pdfUtil.txt(" ");
		}
		pdfUtil.txt("_____________________________________________________________________________________________________________");
		pdfUtil.txt("                          （此為最末頁）");
	}

	/**
	 * 投資標的名稱過長時，切刻2行顯示.
	 * 
	 * @param foundName 投資標的名稱
	 * @param splitNumber 切割長度
	 * @return 回傳切刻過的字元陣列
	 */
	private static String[] splitFundName(String foundName, int splitNumber) {
		int cnt = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < foundName.length(); i++) {
			String strTmp = foundName.substring(i, i + 1);
			sb.append(strTmp);
			
			if (strTmp.matches("[^\\x00-\\xff]")) {
				cnt++;
			}
			cnt++;
			
			if ((cnt % splitNumber) == 0) {
				sb.append("\n");
			}
		}
		return sb.toString().split("\\n");
	}

	/**
	 * 取得 double.
	 * 
	 * @param value BigDecimal
	 * @return 若為null，回傳0
	 */
	private double getDoubleValue(BigDecimal value) {
		if (value == null) {
			return 0;
		}
		return value.doubleValue();
	}

	/**
	 * 格式化金額.
	 * 
	 * @param amount 金額
	 * @param df DecimalFormat
	 * @return 回傳格式化過得金額
	 */
	private String formatNumber(BigDecimal amount, DecimalFormat df) {
		if (amount == null) {
			return "";
		}
		return df.format(amount);
	}
}
