package com.twfhclife.eservice_batch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.twfhclife.eservice_batch.dao.LilipiDao;
import com.twfhclife.eservice_batch.dao.OnlineChangeInfoDao;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransAnnuityMethodDao;
import com.twfhclife.eservice_batch.dao.TransBankInfoDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDtlDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryOldDao;
import com.twfhclife.eservice_batch.dao.TransBounsDao;
import com.twfhclife.eservice_batch.dao.TransCashPaymentDao;
import com.twfhclife.eservice_batch.dao.TransCertPrintDao;
import com.twfhclife.eservice_batch.dao.TransChangeAccountDao;
import com.twfhclife.eservice_batch.dao.TransChangePremiumDao;
import com.twfhclife.eservice_batch.dao.TransChooseLevelDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDtlDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoOldDao;
import com.twfhclife.eservice_batch.dao.TransContractRevocationDao;
import com.twfhclife.eservice_batch.dao.TransCreditCardDateDao;
import com.twfhclife.eservice_batch.dao.TransCushionDao;
import com.twfhclife.eservice_batch.dao.TransDepositDao;
import com.twfhclife.eservice_batch.dao.TransDeratePaidOffDao;
import com.twfhclife.eservice_batch.dao.TransElectronicFormDao;
import com.twfhclife.eservice_batch.dao.TransFundConversionDao;
import com.twfhclife.eservice_batch.dao.TransInvestmentDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransLoanDao;
import com.twfhclife.eservice_batch.dao.TransPaymodeDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransPolicyHolderProfileDao;
import com.twfhclife.eservice_batch.dao.TransResendPolicyDao;
import com.twfhclife.eservice_batch.dao.TransRewardDao;
import com.twfhclife.eservice_batch.dao.TransRiskLevelDao;
import com.twfhclife.eservice_batch.dao.TransRolloverPeriodicallyDao;
import com.twfhclife.eservice_batch.dao.TransValuePrintDao;
import com.twfhclife.eservice_batch.model.IndividualChooseVo;
import com.twfhclife.eservice_batch.model.LilipiVo;
import com.twfhclife.eservice_batch.model.OnlineChangeInfoVo;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.ReportPrintListVo;
import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransAnnuityMethodVo;
import com.twfhclife.eservice_batch.model.TransBankInfoVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryVo;
import com.twfhclife.eservice_batch.model.TransBounsVo;
import com.twfhclife.eservice_batch.model.TransCashPaymentVo;
import com.twfhclife.eservice_batch.model.TransCertPrintVo;
import com.twfhclife.eservice_batch.model.TransChangeAccountVo;
import com.twfhclife.eservice_batch.model.TransChangePremiumVo;
import com.twfhclife.eservice_batch.model.TransChooseLevelVo;
import com.twfhclife.eservice_batch.model.TransContactInfoDtlVo;
import com.twfhclife.eservice_batch.model.TransContactInfoOldVo;
import com.twfhclife.eservice_batch.model.TransContactInfoVo;
import com.twfhclife.eservice_batch.model.TransContractRevocationVo;
import com.twfhclife.eservice_batch.model.TransCreditCardDateVo;
import com.twfhclife.eservice_batch.model.TransCushionVo;
import com.twfhclife.eservice_batch.model.TransDepositVo;
import com.twfhclife.eservice_batch.model.TransDeratePaidOffVo;
import com.twfhclife.eservice_batch.model.TransElectronicFormVo;
import com.twfhclife.eservice_batch.model.TransFundConversionVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransLoanVo;
import com.twfhclife.eservice_batch.model.TransPaymodeVo;
import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransResendPolicyVo;
import com.twfhclife.eservice_batch.model.TransRewardVo;
import com.twfhclife.eservice_batch.model.TransRiskLevelVo;
import com.twfhclife.eservice_batch.model.TransRolloverPeriodicallyVo;
import com.twfhclife.eservice_batch.model.TransValuePrintVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.service.onlineChange.TransAnnuityMethodUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransBeneficiaryUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransBounsUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransCertPrintUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransChangeAccountUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransContactInfoUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransCreditCardDateUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransCushionUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransLoanUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransPaymodeUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransPolicyHolderProfileUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransReduceUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransRenewUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransResendPolicyUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransRewardUtil;
import com.twfhclife.eservice_batch.service.onlineChange.TransValuePrintUtil;
import com.twfhclife.eservice_batch.util.FileUtil;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.ReportExportUtil;
import com.twfhclife.eservice_batch.util.StringUtil;
import com.twfhclife.eservice_batch.util.TransTypeUtil;

public class BatchUploadOnlineChangeSheetService {

	private static final Logger logger = LogManager.getLogger(BatchUploadOnlineChangeSheetService.class);

	private static String SAVE_PATH;
	private static String SAVE_PATH_BACKUP;
	private static String FILE_FORMAT;
	private static String FILE_SOURCE_NAME;
	private static String FILE_SOURCE_NAME2;
	private static Map<String, Map<String, String>> paramMap;

	private static final String SYSTEM_ID = "eservice";

	public BatchUploadOnlineChangeSheetService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		SAVE_PATH = rb.getString("local.file.download.path");
		SAVE_PATH_BACKUP = rb.getString("local.file.backup.file.path");
		FILE_FORMAT = rb.getString("report.upload.file.format");
		FILE_SOURCE_NAME = rb.getString("report.file.src.name");
		FILE_SOURCE_NAME2 = rb.getString("report.file.src.risk.name");
		paramMap = new HashMap<>();
	}

	/**
	 * 主流程
	 * 
	 * @param inputList
	 */
	public void process(List<TransVo> inputList) {

		logger.info("BatchUploadOnlineChangeSheetService start.");
		try {
			if (inputList == null || inputList.size() == 0) {
				logger.info("No upload file pass.");
				return;
			} else {
				initParamMap();
			}
			/**
			 * 1. Loop transVo list 1.1 Get transVo policy info 1.2 Get transVo online
			 * change info 1.3 Prepare upload PDF file 1.3.1 Load PDF template 1.3.2 Draw
			 * data to template 1.3.3 Use trans type decide change info detail to template
			 * 1.3.4 Filename used onlinechange_info_{trans_no}.pdf 1.4 Call Image Manager
			 * System API to upload 2. Backup upload's file to backup folder
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String logMessage = "%d. TransNum: %s, %s";
			OnlineChangeInfoDao dao = new OnlineChangeInfoDao();
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String enviorment = rb.getString("running.enviorment");
			for (TransVo transVo : inputList) {
				int process = 0;
				logger.info("============================================================================");
				logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始取得申請資料"));
				OnlineChangeInfoVo infoVo = dao.getOnlineChangeInfoByTransNum(transVo.getTransNum());
				if (infoVo == null) {
					logger.info(
							"(error #10311)transNum:{}, getOnlineChangeInfoByTransNum is null, can't create onlinechange_info_"
									+ transVo.getTransNum() + ".pdf.",
							transVo.getTransNum());
					continue;
				}

				/// 20220930 by 203990
				/// Begin: 變更風險屬性 TRANS_POLICY表 沒有記錄 policy_no 要另外取得取得一筆保單號碼及要保人ID供影像系統記錄使用
				if ("RISK_LEVEL".equals(infoVo.getTransType())) {
					TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
					transRiskLevelVo.setTransNum(transVo.getTransNum());
					TransRiskLevelDao transRiskLevelDao = new TransRiskLevelDao();
					List<TransRiskLevelVo> list = transRiskLevelDao.getTransLevel(transRiskLevelVo);
					if (list != null && list.size() > 0) {
						infoVo.setLipmId(list.get(0).getRocId());
						infoVo.setPolicyNo(transRiskLevelDao.getTop1PolicyNo(list.get(0).getRocId()));
					}
				}
				if ("CHOOSE_LEVEL".equals(infoVo.getTransType())) {
					TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
					transChooseLevelVo.setTransNum(transVo.getTransNum());
					TransChooseLevelDao transChooseLevelDao = new TransChooseLevelDao();
					List<TransChooseLevelVo> transChooseLevelList = transChooseLevelDao
							.getTransChooseLevel(transChooseLevelVo);
					if (transChooseLevelList != null && transChooseLevelList.size() > 0) {
						infoVo.setLipmId(transChooseLevelList.get(0).getRocId());
						infoVo.setPolicyNo(transChooseLevelList.get(0).getRocId());
					}
				}
				/// End

				StringBuilder beforeChange = new StringBuilder();
				StringBuilder afterChange = new StringBuilder();

				logger.info("============================================================================");
				logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始建立差異內容"));
				this.appendDetailData(transVo.getTransType(), beforeChange, afterChange, transVo.getTransNum());
				logger.info("============================================================================");
				// 2023/03/25 新增 變更風險屬性 呼叫另外一章底板
				try {
					if ("RISK_LEVEL".equals(infoVo.getTransType()) || "CHOOSE_LEVEL".equals(infoVo.getTransType())) {
						
						logger.info("============================================================================");
						logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始畫出 pdf"));
						//取得基本資料
						String rocId ="";
						TransChooseLevelDao transChooseLevelDao = new TransChooseLevelDao();
						TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
						TransRiskLevelDao transRiskLevelDao = new TransRiskLevelDao();
						TransRiskLevelVo transRiskLevelVo= new TransRiskLevelVo();
						transChooseLevelVo = transChooseLevelDao.getEffectTransChooseLevel(transVo.getTransNum());
						if(transChooseLevelVo == null) {
							transRiskLevelVo = transRiskLevelDao.getEffectUserTransRiskLevel(transVo.getTransNum());
							rocId = transRiskLevelVo.getRocId();
						}else {
							rocId = transChooseLevelVo.getRocId();
						}

						IndividualChooseVo individualChooseVo = transChooseLevelDao.getIndividualChooseByRocId(rocId);
						String source ="";
						if(StringUtils.isNotEmpty(individualChooseVo.getEmployeeId())) {
							source ="獨立填寫頁";
						}else if(StringUtils.isNotEmpty(transVo.getUserId())) {
							source ="保單網路服務";
						}else {
							source ="官網";
						}

//						if(individualChooseVo  != null) {
							//開始製作PDF
							ReportExportUtil reportUtil = new ReportExportUtil(FILE_SOURCE_NAME2);
							Integer fontSize = 11;
							// 保單基本資料
							float leftPosition = 197f;
							float topPosition = 1450f;
							float range = 19.5f;
							List<ReportPrintListVo> listPrintVo = new ArrayList<>();
							Integer textOrder = 1;
							// 保單號碼
							listPrintVo.add(new ReportPrintListVo(StringUtils.isNotEmpty(infoVo.getPolicyNo()) ? infoVo.getPolicyNo() : infoVo.getLipmId() , textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getRocId(), textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getName() , textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getMobile() , textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getBirthDate() , textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getEducationLevel() , textOrder++));
							listPrintVo.add(new ReportPrintListVo("" , textOrder++));
							listPrintVo.add(new ReportPrintListVo(individualChooseVo.getDisability() , textOrder++));
							listPrintVo.add(new ReportPrintListVo(source , textOrder++));
							for(ReportPrintListVo vo : listPrintVo.stream()
									.sorted( (v1, v2) -> { return v1.getTextOrder().compareTo(v2.getTextOrder());})
									.collect(Collectors.toList())) {
								reportUtil.txt(vo.getTextContent(), fontSize, 1, leftPosition, topPosition);
								topPosition -= range;
							}
							// 變更明細資料
							// 變更時間 
							reportUtil.txt(sdf.format(transVo.getCreateDate()), fontSize, 1, 112f, 1230f);
							// 變更編號
							reportUtil.txt(transVo.getTransNum(), fontSize, 1, 220f, 1230f);
							// 變更種類 trans type to CHT 
							reportUtil.txtAlignCenter(paramMap.get("ONLINE_CHANGE").get(transVo.getTransType()), fontSize, 1, 425f, 1230f);
							// 變更前
							float beforeTop = 1170f;
							float high = 15f;
							for(String beforeLine: beforeChange.toString().split("\\r\\n")) {
								if (StringUtil.countDobuleSize(beforeLine) >= 80) {
									reportUtil.txt(beforeLine.substring(0, 40), 10, 1, 100f, beforeTop);
									beforeTop -= high;
									reportUtil.txt(beforeLine.substring(40), 10, 1, 100f, beforeTop);
									beforeTop -= high;
								} else {
									reportUtil.txt(beforeLine, 10, 1, 100f, beforeTop);
									beforeTop -= high;
								}							
							}
							// 變更後
							float afterTop = 1062f;
							for(String afterLine: afterChange.toString().split("\\r\\n")) {
								if (StringUtil.countDobuleSize(afterLine) >= 80) {
									reportUtil.txt(afterLine.substring(0, 45), 10, 1, 100f, afterTop);
									afterTop -= high;
									reportUtil.txt(afterLine.substring(45), 10, 1, 100f, afterTop);
									afterTop -= high;
								} else {
									reportUtil.txt(afterLine, 10, 1, 100f, afterTop);
									afterTop -= high;					
								}
							}
							reportUtil.toPdf();
							String filename = String.format(FILE_FORMAT, transVo.getTransNum());
							File saveFolder = new File(SAVE_PATH);
							if(!saveFolder.exists()) {
								saveFolder.mkdirs();
							}
							File reportFile = new File(SAVE_PATH + "/" + filename);
							if(!reportFile.exists()) {
								reportFile.createNewFile();
							}
							FileOutputStream fos = new FileOutputStream(reportFile);
							IOUtils.write(reportUtil.getPdfBytes(), fos);
							IOUtils.closeQuietly(fos);
							
							if (!"dev".equalsIgnoreCase(enviorment)) {
								logger.info("============================================================================");
								logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始上傳申請單影像..."));
								// 上傳申請單到影像系統
								BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
								String token = batchUploadEZService.getEZToken();
								boolean isSucc = batchUploadEZService.uploadFile("U" + transVo.getTransNum(), "PDF", token,
										reportFile, infoVo);

								if (isSucc) {
									logger.info("上傳申請單到影像系統完成");
								} else {
									logger.info("上傳申請單到影像系統失敗, transNum: " + transVo.getTransNum());
								}
							}

							// backup report file to backup path
							logger.info("============================================================================");
							logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始備份申請單影像..."));

							String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
							String todayPath = "/" + today;
							File file = new File(SAVE_PATH_BACKUP + todayPath);
							if (!file.exists()) {
								file.mkdir();
							}

							if (FileUtil.moveFile(SAVE_PATH + "/" + filename,
									SAVE_PATH_BACKUP + todayPath + "/" + filename)) {
								logger.info("備份完成");
							} else {
								logger.info("備份失敗");
							}
//						}						
					} else {
						// 開始畫出 pdf
						logger.info("============================================================================");
						logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始畫出 pdf"));
						ReportExportUtil reportUtil = new ReportExportUtil(FILE_SOURCE_NAME);
						Integer fontSize = 11;
						// 保單基本資料
						float leftPosition = 180f;
						float topPosition = 733f;
						float range = 18f;
						List<ReportPrintListVo> listPrintVo = new ArrayList<>();
						Integer textOrder = 1;
						// 保單號碼
						listPrintVo.add(new ReportPrintListVo(infoVo.getPolicyNo(), textOrder++));
						// 商品名稱 policy product_name
						listPrintVo.add(new ReportPrintListVo(infoVo.getProductName(), textOrder++));
						// 主約保額
						listPrintVo.add(
								new ReportPrintListVo(MyStringUtil.nullToString(infoVo.getMainAmount()), textOrder++));
						// 要保人 LIPM
						listPrintVo.add(new ReportPrintListVo(infoVo.getPolicyHolderName(), textOrder++));
						// 被保人 LIPI
						listPrintVo.add(new ReportPrintListVo(infoVo.getInsuredName(), textOrder++));
						// 幣別 XX
						listPrintVo.add(new ReportPrintListVo(infoVo.getCurrency(), textOrder++));
						// 保單狀態 policy status
						listPrintVo.add(new ReportPrintListVo(paramMap.get("POLICY_STATUS").get(infoVo.getStatus()),
								textOrder++));
						// 投保始期 policy effective_date
						listPrintVo.add(new ReportPrintListVo(
								infoVo.getEffectiveDate() == null ? null : sdf.format(infoVo.getEffectiveDate()),
								textOrder++));
						// 繳費年期 policy premYear
						listPrintVo
								.add(new ReportPrintListVo(MyStringUtil.objToStr(infoVo.getPremYear()), textOrder++));
						// 繳別 policy paymentMode
						listPrintVo.add(new ReportPrintListVo(paramMap.get("PAYMODE_TYPE").get(infoVo.getPaymentMode()),
								textOrder++));

						for (ReportPrintListVo vo : listPrintVo.stream().sorted((v1, v2) -> {
							return v1.getTextOrder().compareTo(v2.getTextOrder());
						}).collect(Collectors.toList())) {
							reportUtil.txt(vo.getTextContent(), fontSize, 1, leftPosition, topPosition);
							topPosition -= range;
						}

						// 變更明細資料
						// 變更時間
						reportUtil.txt(sdf.format(transVo.getCreateDate()), fontSize, 1, 110f, 498f);
						// 變更編號
						reportUtil.txt(transVo.getTransNum(), fontSize, 1, 210f, 498f);
						// 變更種類 trans type to CHT
						reportUtil.txtAlignCenter(paramMap.get("ONLINE_CHANGE").get(transVo.getTransType()), fontSize,
								1, 395f, 498f);
						// 變更前
						// reportUtil.txt(beforeChange.toString(), fontSize, 1, 100f, 440f);
						// 因為沒有辦法直接印成多列，所以修改成每一個換行獨立處理
						float beforeTop = 440f;
						float high = 15f;
						for (String beforeLine : beforeChange.toString().split("\\r\\n")) {
							if (StringUtil.countDobuleSize(beforeLine) >= 72) {
								reportUtil.txt(beforeLine.substring(0, 36), fontSize, 1, 100f, beforeTop);
								beforeTop -= high;
								reportUtil.txt(beforeLine.substring(36), fontSize, 1, 100f, beforeTop);
								beforeTop -= high;
							} else {
								reportUtil.txt(beforeLine, fontSize, 1, 100f, beforeTop);
								beforeTop -= high;
							}

						}
						// 變更後
						// reportUtil.txt(afterChange.toString(), fontSize, 1, 100f, 255f);
						float afterTop = 255f;
						for (String afterLine : afterChange.toString().split("\\r\\n")) {
							if (StringUtil.countDobuleSize(afterLine) >= 72) {
								reportUtil.txt(afterLine.substring(0, 36), fontSize, 1, 100f, afterTop);
								afterTop -= high;
								reportUtil.txt(afterLine.substring(36), fontSize, 1, 100f, afterTop);
								afterTop -= high;
							} else {
								reportUtil.txt(afterLine, fontSize, 1, 100f, afterTop);
								afterTop -= high;
							}
						}

						reportUtil.toPdf();

						String filename = String.format(FILE_FORMAT, transVo.getTransNum());
						File saveFolder = new File(SAVE_PATH);
						if (!saveFolder.exists()) {
							saveFolder.mkdirs();
						}
						File reportFile = new File(SAVE_PATH + "/" + filename);
						if (!reportFile.exists()) {
							reportFile.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(reportFile);
						IOUtils.write(reportUtil.getPdfBytes(), fos);
						IOUtils.closeQuietly(fos);

						if (!"dev".equalsIgnoreCase(enviorment)) {
							logger.info("============================================================================");
							logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始上傳申請單影像..."));
							// 上傳申請單到影像系統
							BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
							String token = batchUploadEZService.getEZToken();
							boolean isSucc = batchUploadEZService.uploadFile("U" + transVo.getTransNum(), "PDF", token,
									reportFile, infoVo);

							if (isSucc) {
								logger.info("上傳申請單到影像系統完成");
							} else {
								logger.info("上傳申請單到影像系統失敗, transNum: " + transVo.getTransNum());
							}
						}

						// backup report file to backup path
						logger.info("============================================================================");
						logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始備份申請單影像..."));

						String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
						String todayPath = "/" + today;
						File file = new File(SAVE_PATH_BACKUP + todayPath);
						if (!file.exists()) {
							file.mkdir();
						}

						if (FileUtil.moveFile(SAVE_PATH + "/" + filename,
								SAVE_PATH_BACKUP + todayPath + "/" + filename)) {
							logger.info("備份完成");
						} else {
							logger.info("備份失敗");
						}
					}
				} catch (Exception e) {
					logger.error("Create report error: {}", ExceptionUtils.getStackTrace(e));
				}
			}
		} finally {
			logger.info("BatchUploadOnlineChangeSheetService end.");
		}
	}

	/**
	 * 設定共通參數
	 */
	private void initParamMap() {
		ParameterDao paramDao = new ParameterDao();
		try {
			// setting Online Change 項目
			paramMap.put("ONLINE_CHANGE", paramDao.getParameterByCategoryCode(SYSTEM_ID, "ONLINE_CHANGE").stream()
					.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterName)));
			// setting 繳別
			paramMap.put("PAYMODE_TYPE", paramDao.getParameterByCategoryCode(SYSTEM_ID, "PAYMODE_TYPE").stream()
					.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			// setting 保單狀態
			paramMap.put("POLICY_STATUS", paramDao.getParameterByCategoryCode(SYSTEM_ID, "POLICY_STATUS").stream()
					.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
		} catch (Exception e) {
			logger.error("initParamMap error: ", e);
		}
	}

	/**
	 * 建立異動內容
	 * 
	 * @param transType
	 * @param before
	 * @param after
	 * @param transDetailList
	 */
	private void appendDetailData(String transType, StringBuilder before, StringBuilder after, String transNum) {
		TransVo transVo = null;
		switch (transType) {
		// use change_data create text content
		case TransTypeUtil.PAYMODE_PARAMETER_CODE:
			// 繳別
			TransPaymodeVo transPaymodeVo = new TransPaymodeVo();
			transPaymodeVo.setTransNum(transNum);
			transPaymodeVo = new TransPaymodeDao().getTransPaymode(transPaymodeVo);
			new TransPaymodeUtil().getChangeContent(before, after, transPaymodeVo);
			break;
		case TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE:
			// 年金給付方式
			TransAnnuityMethodVo transAnnuityMethodVo = new TransAnnuityMethodVo();
			transAnnuityMethodVo.setTransNum(transNum);
			transAnnuityMethodVo = new TransAnnuityMethodDao().getTransAnnuityMethod(transAnnuityMethodVo);
			new TransAnnuityMethodUtil().getChangeContent(before, after, transAnnuityMethodVo);
			break;
		case TransTypeUtil.BONUS_PARAMETER_CODE:
			// 紅利選擇權
			TransBounsVo transBounsVo = new TransBounsVo();
			transBounsVo.setTransNum(transNum);
			transBounsVo = new TransBounsDao().getTransBouns(transBounsVo);
			new TransBounsUtil().getChangeContent(before, after, transBounsVo);
			break;
		case TransTypeUtil.REWARD_PARAMETER_CODE:
			// 增值回饋金領取方式
			TransRewardVo transRewardVo = new TransRewardVo();
			transRewardVo.setTransNum(transNum);
			transRewardVo = new TransRewardDao().getTransReward(transRewardVo);
			new TransRewardUtil().getChangeContent(before, after, transRewardVo);
			break;
		case TransTypeUtil.CUSHION_PARAMETER_CODE:
			// 是否自動墊繳
			TransCushionVo transCushionVo = new TransCushionVo();
			transCushionVo.setTransNum(transNum);
			transCushionVo = new TransCushionDao().getTransCushion(transCushionVo);
			new TransCushionUtil().getChangeContent(before, after, transCushionVo);
			break;
		case TransTypeUtil.BENEFICIARY_PARAMETER_CODE:
			// 受益人
			TransBeneficiaryVo transBeneficiaryMaster = new TransBeneficiaryVo();
			transBeneficiaryMaster.setTransNum(transNum);
			transBeneficiaryMaster = new TransBeneficiaryDao().getTransBeneficiary(transBeneficiaryMaster);
			if (transBeneficiaryMaster != null) {
				TransBeneficiaryDtlVo transBeneficiaryDtlVo = new TransBeneficiaryDtlVo();
				transBeneficiaryDtlVo.setTransBeneficiaryId(transBeneficiaryMaster.getId());
				List<TransBeneficiaryDtlVo> dtlList = new TransBeneficiaryDtlDao()
						.getTransBeneficiaryDtlList(transBeneficiaryDtlVo);
				transBeneficiaryMaster.setDtlList(dtlList);

				TransBeneficiaryOldVo transBeneficiaryOldVo = new TransBeneficiaryOldVo();
				transBeneficiaryOldVo.setTransBeneficiaryId(transBeneficiaryMaster.getId());
				List<TransBeneficiaryOldVo> oldList = new TransBeneficiaryOldDao()
						.getTransBeneficiaryOldList(transBeneficiaryOldVo);
				transBeneficiaryMaster.setOldList(oldList);

				new TransBeneficiaryUtil().getChangeContent(before, after, transBeneficiaryMaster);
			}
			break;
		case TransTypeUtil.RENEW_PARAMETER_CODE:
			// RENEW/REDUCE 畫面為同一個
			transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo = new TransDao().findById(transVo);
			new TransRenewUtil().getChangeContent(before, after, transVo);
			break;
		case TransTypeUtil.REDUCE_PARAMETER_CODE:
			transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo = new TransDao().findById(transVo);
			new TransReduceUtil().getChangeContent(before, after, transVo);
			break;
		case TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE:
			// pass
			break;
		case TransTypeUtil.CONTACT_INFO_PARAMETER_CODE:
			// 聯絡資料變更
			TransContactInfoVo transContactInfoMaster = new TransContactInfoVo();
			transContactInfoMaster.setTransNum(transNum);
			transContactInfoMaster = new TransContactInfoDao().getTransContactInfo(transContactInfoMaster);
			logger.debug("transContactInfoMaster:{}", new Gson().toJson(transContactInfoMaster));
			if (transContactInfoMaster != null) {
				TransContactInfoDtlVo transContactInfoDtlVo = new TransContactInfoDtlVo();
				transContactInfoDtlVo.setTransContactId(transContactInfoMaster.getId());
				transContactInfoDtlVo = new TransContactInfoDtlDao().getTransContactInfoDtl(transContactInfoDtlVo);
				transContactInfoMaster.setTransContactInfoDtlVo(transContactInfoDtlVo);
				logger.debug("==================================================",
						new Gson().toJson(transContactInfoDtlVo));
				logger.debug("transContactInfoDtlVo:{}", new Gson().toJson(transContactInfoDtlVo));

				TransContactInfoOldVo transContactInfoOldVo = new TransContactInfoOldVo();
				transContactInfoOldVo.setTransContactId(transContactInfoMaster.getId());
				transContactInfoOldVo = new TransContactInfoOldDao().getTransContactInfoOld(transContactInfoOldVo);
				transContactInfoMaster.setTransContactInfoOldVo(transContactInfoOldVo);
				logger.debug("==================================================",
						new Gson().toJson(transContactInfoDtlVo));
				logger.debug("transContactInfoOldVo:{}", new Gson().toJson(transContactInfoOldVo));
				new TransContactInfoUtil().getChangeContent(before, after, transContactInfoMaster);
			}

			break;
		case TransTypeUtil.VALUE_PRINT_PARAMETER_CODE:
			// 保單價值列印
			TransValuePrintVo transValuePrintVo = new TransValuePrintVo();
			transValuePrintVo.setTransNum(transNum);
			transValuePrintVo = new TransValuePrintDao().getTransValuePrint(transValuePrintVo);
			new TransValuePrintUtil().getChangeContent(before, after, transValuePrintVo);
			break;
		case TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE:
			// 信用卡效期變更
			TransCreditCardDateVo transCreditCardDateVo = new TransCreditCardDateVo();
			transCreditCardDateVo.setTransNum(transNum);
			transCreditCardDateVo = new TransCreditCardDateDao().getTransCreditCardDate(transCreditCardDateVo);
			new TransCreditCardDateUtil().getChangeContent(before, after, transCreditCardDateVo);
			break;
		case TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE:
			// 匯款帳號變更
			TransChangeAccountVo transChangeAccountVo = new TransChangeAccountVo();
			transChangeAccountVo.setTransNum(transNum);
			transChangeAccountVo = new TransChangeAccountDao().getTransChangeAccount(transChangeAccountVo);

			TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
			transBankInfoVo.setTransNum(transNum);
			transBankInfoVo = new TransBankInfoDao().getTransBankInfo(transBankInfoVo);
			transChangeAccountVo.setTransBankInfoVo(transBankInfoVo);

			new TransChangeAccountUtil().getChangeContent(before, after, transChangeAccountVo);
			break;
		case TransTypeUtil.CANCEL_AUTH_ACCOUNT_PARAMETER_CODE:
			// pass not detail html
			break;
		case TransTypeUtil.CERT_PRINT_PARAMETER_CODE:
			// 投保證明列印
			TransCertPrintVo transCertPrintVo = new TransCertPrintVo();
			transCertPrintVo.setTransNum(transNum);
			transCertPrintVo = new TransCertPrintDao().getTransCertPrint(transCertPrintVo);
			new TransCertPrintUtil().getChangeContent(before, after, transCertPrintVo);
			break;
		case TransTypeUtil.POLICY_RESEND_PARAMETER_CODE:
			// 補發保單
			TransResendPolicyVo transResendPolicyVo = new TransResendPolicyVo();
			transResendPolicyVo.setTransNum(transNum);
			transResendPolicyVo = new TransResendPolicyDao().getTransResendPolicy(transResendPolicyVo);
			new TransResendPolicyUtil().getChangeContent(before, after, transResendPolicyVo);
			break;
		case TransTypeUtil.LOAN_NEW_PARAMETER_CODE:
			// 保單借款(線上)
			TransLoanVo transLoanVo = new TransLoanVo();
			transLoanVo.setTransNum(transNum);
			List<TransLoanVo> transLoanList = new TransLoanDao().getTransLoanList(transLoanVo);
			if (transLoanList != null && transLoanList.size() > 0) {
				transLoanVo = transLoanList.get(0);

				TransBankInfoVo bankInfoVo = new TransBankInfoVo();
				bankInfoVo.setTransNum(transNum);
				bankInfoVo = new TransBankInfoDao().getTransBankInfo(bankInfoVo);
				if (bankInfoVo != null) {
					transLoanVo.setTransBankInfoVo(bankInfoVo);
				}
			}
			new TransLoanUtil().getChangeContent(before, after, transLoanVo);
			break;
		case TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE:
			// 保戶基本資料更新
			TransPolicyHolderProfileVo oldVo = new TransPolicyHolderProfileDao().getOldJobByTransNum(transNum);
			if (oldVo == null) {
				logger.info("查無保戶基本資料!!!");
			} else {
				logger.info("保戶基本資料={}", new Gson().toJson(oldVo));
			}

			TransPolicyHolderProfileVo newVo = new TransPolicyHolderProfileVo();
			newVo.setTransNum(transNum);
			List<TransPolicyHolderProfileVo> transPolicyHolderProfileList = new TransPolicyHolderProfileDao()
					.getTransPolicyHolderProfileList(newVo);
			if (transPolicyHolderProfileList != null && transPolicyHolderProfileList.size() > 0) {
				newVo = transPolicyHolderProfileList.get(0);
			}

			new TransPolicyHolderProfileUtil().getChangeContent(before, after, newVo, oldVo);
			break;

		/// 2022/09/26~09/30 by 203990
		/// Begin: 優投功能變更明細介接影像系統
		case TransTypeUtil.RISK_LEVEL_PARAMETER_CODE:
			// 變更風險屬性
			try {
				// 取得資料
				TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
				transRiskLevelVo.setTransNum(transNum);
				TransRiskLevelDao transRiskLevelDao = new TransRiskLevelDao();
				List<TransRiskLevelVo> list = transRiskLevelDao.getTransLevel(transRiskLevelVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "";
					for (TransRiskLevelVo vo : list) {
						/*
						 * String contentFormat = "AAA：%s \r\nBBB：%s";
						 * before.append(String.format(contentFormat ,
						 * StringUtils.trimToEmpty(transRiskLevelVo.getRiskLevelOld()) ));
						 * after.append(String.format(contentFormat ,
						 * StringUtils.trimToEmpty(transRiskLevelVo.getRiskLevelNew()) ));
						 */
						contentSubFormat = "風險等級: %s ";
						before.append(String.format(contentSubFormat, vo.getRiskLevelOld()));
						if(vo.getRuleStatus().equals("3")) {
							contentSubFormat = " 風險等級: %s \r\n 風險等級問題與答案 : \r\n %s ";						
						}else {
							contentSubFormat = " 風險等級: %s \r\n %s 同意「一、風險屬性評估」之評估結果 \r\n 風險等級問題與答案 : \r\n %s ";
						}
						StringBuilder sb = new StringBuilder();
						String choose = vo.getChoose();
						if (StringUtils.isNotEmpty(choose)) {
							String[] chooseList = choose.split("\n");
							for (String st : chooseList) {
								int stindex = st.indexOf("<br>");
								if(stindex > 0) {
									String  str = st.substring(0 , stindex);
									sb.append(str);
									sb.append("\r\n");
									String str1 = st.substring(stindex+4);
									int stindex2 = str1.indexOf("<br>");
									if(stindex2 > 0) {
										String str2 = str1.substring(0 , stindex2 );
										sb.append(str2);
										sb.append("\r\n");
										sb.append(str1.substring(stindex2 + 4));
										sb.append("\r\n");
									}else {
										sb.append(st.substring(stindex+4));
										sb.append("\r\n");
									}
								}else {					
									sb.append(st);
									sb.append("\r\n");
								}
							}
							sb.toString();
						}
						if(vo.getRuleStatus().equals("3")) {
							after.append(String.format(contentSubFormat, vo.getRiskLevelNew(), sb.toString()));						
						}else {
							String ruleStatus = vo.getRuleStatus().equals("2") ? "有勾選" : "未勾選";	
							after.append(String.format(contentSubFormat, vo.getRiskLevelNew(), ruleStatus ,sb.toString()));	
						}
					}
				}
				logger.debug("變更風險屬性 before:{}", before);
				logger.debug("變更風險屬性 after:{}", after);
			} catch (Exception e) {
				logger.error("getEffectUserTransRiskLevel error: " + e);
			}
			break;
		case TransTypeUtil.CHANGE_PREMIUM_PARAMETER_CODE:
			// 定期超額保費
			try {
				// 取得資料
				TransChangePremiumVo transChangePremiumVo = new TransChangePremiumVo();
				transChangePremiumVo.setTransNum(transNum);
				TransChangePremiumDao transChangePremiumDao = new TransChangePremiumDao();
				List<TransChangePremiumVo> list = transChangePremiumDao.getTransChangePremium(transChangePremiumVo);
				if (list != null && list.size() > 0) {
					for (TransChangePremiumVo vo : list) {
						before.append(vo.getOldAmount()).append("\r\n");
						after.append(vo.getAmount()).append("\r\n");
					}
				}
				logger.debug("定期超額保費 before:{}", before);
				logger.debug("定期超額保費 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransChangePremium error: " + e);
			}
			break;
		case TransTypeUtil.DEPOSIT_PARAMETER_CODE:
			// 提領(贖回)
			try {
				// 取得資料
				TransDepositVo transDepositVo = new TransDepositVo();
				transDepositVo.setTransNum(transNum);
				TransDepositDao transDepositDao = new TransDepositDao();
				List<TransDepositVo> list = transDepositDao.getTransDeposits(transDepositVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "投資標的：%s \r\n提領比例：%s \r\n提領金額：%s \r\n";
					for (TransDepositVo vo : list) {
						after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(vo.getInvtNo()),
								StringUtils.trimToEmpty(vo.getRatio().toString()),
								StringUtils.trimToEmpty(vo.getAmount().toString() + " " + vo.getCurrency())));
					}
					TransDepositVo firstVo = list.get(0);
					String contentFormat = "Swift Code：%s \r\n英文戶名：%s \r\n帳戶名稱：%s \r\n銀行代碼：%s \r\n銀行名稱：%s \r\n分行代碼：%s \r\n分行名稱：%s \r\n帳號：%s";
					after.append(String.format(contentFormat, StringUtils.trimToEmpty(firstVo.getSwiftCode()),
							StringUtils.trimToEmpty(firstVo.getEnglishName()),
							StringUtils.trimToEmpty(firstVo.getAccountName()),
							StringUtils.trimToEmpty(firstVo.getBankCode()),
							StringUtils.trimToEmpty(firstVo.getBankName()),
							StringUtils.trimToEmpty(firstVo.getBranchCode()),
							StringUtils.trimToEmpty(firstVo.getBranchName()),
							StringUtils.trimToEmpty(firstVo.getBankAccount())));
				}
				logger.debug("提領(贖回) before:{}", before);
				logger.debug("提領(贖回) after:{}", after);
			} catch (Exception e) {
				logger.error("getTransDeposits error: " + e);
			}
			break;
		case TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE:
			// 收益分配或撥回資產分配方式
			try {
				// 取得資料
				TransCashPaymentVo transCashPaymentVo = new TransCashPaymentVo();
				transCashPaymentVo.setTransNum(transNum);
				TransCashPaymentDao transCashPaymentDao = new TransCashPaymentDao();
				List<TransCashPaymentVo> list = transCashPaymentDao.getTransDeposits(transCashPaymentVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "(變更前)分配方式：%s \r\n";
					String PreAllocation = "增加投資標的單位數";
					for (TransCashPaymentVo vo : list) {
						if ("1".equals(vo.getPreAllocation())) {
							PreAllocation = "現金給付";
						}
						before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));

						PreAllocation = "增加投資標的單位數";
						if ("1".equals(vo.getAllocation())) {
							PreAllocation = "現金給付";
							contentSubFormat = "(變更後)分配方式：%s \r\nSwift Code：%s \r\n英文戶名：%s \r\n帳戶名稱：%s \r\n銀行代碼：%s \r\n銀行名稱：%s \r\n分行代碼：%s \r\n分行名稱：%s \r\n帳號：%s";
							after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation),
									StringUtils.trimToEmpty(vo.getSwiftCode()),
									StringUtils.trimToEmpty(vo.getEnglishName()),
									StringUtils.trimToEmpty(vo.getAccountName()),
									StringUtils.trimToEmpty(vo.getBankCode()),
									StringUtils.trimToEmpty(vo.getBankName()),
									StringUtils.trimToEmpty(vo.getBranchCode()),
									StringUtils.trimToEmpty(vo.getBranchName()),
									StringUtils.trimToEmpty(vo.getBankAccount())));
						} else {
							contentSubFormat = "(變更後)分配方式：%s";
							after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
						}
					}
				}
				logger.debug("收益分配或撥回資產分配方式 before:{}", before);
				logger.debug("收益分配或撥回資產分配方式 after:{}", after);
			} catch (Exception e) {
				logger.error("transCashPaymentDao.getTransDeposits error: " + e);
			}
			break;
		case TransTypeUtil.INVESTMENT_PARAMETER_CODE:
			// 變更投資標的分配比例
			try {
				// 取得資料
				TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
				transInvestmentVo.setTransNum(transNum);
				TransInvestmentDao transInvestmentDao = new TransInvestmentDao();
				List<TransInvestmentVo> list = transInvestmentDao.getTransInvestments(transInvestmentVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "(變更前)投資標的：%s \r\n(變更前)分配比例：%s \r\n";
					for (TransInvestmentVo vo : list) {
						before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(vo.getInvtNo()),
								StringUtils.trimToEmpty(vo.getPreDistributionRatio().toString())));
					}

					contentSubFormat = "(變更後)投資標的：%s \r\n(變更後)分配比例：%s \r\n";
					for (TransInvestmentVo vo : list) {
						after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(vo.getInvtNo()),
								StringUtils.trimToEmpty(vo.getDistributionRatio().toString())));
					}

					TransInvestmentVo firstVo = list.get(0);
					TransAccountVo accountVo = transInvestmentDao.findAccount(transNum, firstVo.getInvtNo());
					if (!"".equals(accountVo.getBankCode())) {
						String contentFormat = "Swift Code：%s \r\n英文戶名：%s \r\n帳戶名稱：%s \r\n銀行代碼：%s \r\n銀行名稱：%s \r\n分行代碼：%s \r\n分行名稱：%s \r\n帳號：%s";
						after.append(String.format(contentFormat, StringUtils.trimToEmpty(accountVo.getSwiftCode()),
								StringUtils.trimToEmpty(accountVo.getEnglishName()),
								StringUtils.trimToEmpty(accountVo.getAccountName()),
								StringUtils.trimToEmpty(accountVo.getBankCode()),
								StringUtils.trimToEmpty(accountVo.getBankName()),
								StringUtils.trimToEmpty(accountVo.getBranchCode()),
								StringUtils.trimToEmpty(accountVo.getBranchName()),
								StringUtils.trimToEmpty(accountVo.getBankAccount())));
					}
				}
				logger.debug("變更投資標的分配比例 before:{}", before);
				logger.debug("變更投資標的分配比例 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransInvestments error: " + e);
			}
			break;
		case TransTypeUtil.CONVERSION_PARAMETER_CODE:
			// 已持有投資標的轉換
			try {
				// 取得資料
				TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
				transFundConversionVo.setTransNum(transNum);
				TransFundConversionDao transFundConversionDao = new TransFundConversionDao();
				List<TransFundConversionVo> list = transFundConversionDao
						.getTransFundConversions(transFundConversionVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "(轉出)投資標的：%s \r\n(轉出)單位數：%s \r\n";
					String invtNo = "";
					for (TransFundConversionVo vo : list) {
						if (!invtNo.contains(vo.getInvtNo())) {
							before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(vo.getInvtNo()),
									StringUtils.trimToEmpty(vo.getValue().toString())));
							invtNo += vo.getInvtNo();
						}
					}

					contentSubFormat = "(轉入)投資標的：%s \r\n(轉入)百分比：%s \r\n";
					invtNo = "";
					for (TransFundConversionVo vo : list) {
						if (!invtNo.contains(vo.getInInvtNo())) {
							after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(vo.getInInvtNo()),
									StringUtils.trimToEmpty(vo.getRatio().toString())));
							invtNo += vo.getInInvtNo();
						}
					}

					TransFundConversionVo firstVo = list.get(0);
					TransAccountVo accountVo = transFundConversionDao.findAccount(transNum, firstVo.getInInvtNo());
					if (!"".equals(accountVo.getBankCode())) {
						String contentFormat = "Swift Code：%s \r\n英文戶名：%s \r\n帳戶名稱：%s \r\n銀行代碼：%s \r\n銀行名稱：%s \r\n分行代碼：%s \r\n分行名稱：%s \r\n帳號：%s";
						after.append(String.format(contentFormat, StringUtils.trimToEmpty(accountVo.getSwiftCode()),
								StringUtils.trimToEmpty(accountVo.getEnglishName()),
								StringUtils.trimToEmpty(accountVo.getAccountName()),
								StringUtils.trimToEmpty(accountVo.getBankCode()),
								StringUtils.trimToEmpty(accountVo.getBankName()),
								StringUtils.trimToEmpty(accountVo.getBranchCode()),
								StringUtils.trimToEmpty(accountVo.getBranchName()),
								StringUtils.trimToEmpty(accountVo.getBankAccount())));
					}
				}
				logger.debug("已持有投資標的轉換 before:{}", before);
				logger.debug("已持有投資標的轉換 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransFundConversions error: " + e);
			}
			break;

		case TransTypeUtil.ELECTRONIC_FORM_A_CODE:
			try {
				// 取得資料
				TransElectronicFormVo transElectronicFormVo = new TransElectronicFormVo();
				transElectronicFormVo.setTransNum(transNum);
				TransElectronicFormDao transElectronicFormDao = new TransElectronicFormDao();
				List<TransElectronicFormVo> list = transElectronicFormDao.getTransElectronicForm(transElectronicFormVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "";
					String PreAllocation = "";
					for (TransElectronicFormVo vo : list) {
						before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
						PreAllocation = "申請";
						contentSubFormat = "(變更後)：%s";
						after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
					}
				}
				logger.debug("申請電子表單開通服務-申請 before:{}", before);
				logger.debug("申請電子表單開通服務-申請 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransElectronicForm error: " + e);
			}
			break;
		case TransTypeUtil.ELECTRONIC_FORM_C_CODE:
			try {
				// 取得資料
				TransElectronicFormVo transElectronicFormVo = new TransElectronicFormVo();
				transElectronicFormVo.setTransNum(transNum);
				TransElectronicFormDao transElectronicFormDao = new TransElectronicFormDao();
				List<TransElectronicFormVo> list = transElectronicFormDao.getTransElectronicForm(transElectronicFormVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "";
					String PreAllocation = "";
					for (TransElectronicFormVo vo : list) {
						PreAllocation = "已申請";
						before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
						PreAllocation = "取消";
						contentSubFormat = "(變更後)：%s";
						after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
					}
				}
				logger.debug("申請電子表單開通服務-取消 before:{}", before);
				logger.debug("申請電子表單開通服務-取消 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransElectronicForm error: " + e);
			}
			break;
		case TransTypeUtil.CONTRACT_REVOCATION_CODE:
			try {
				// 取得資料
				TransContractRevocationVo transContractRevocationVo = new TransContractRevocationVo();
				transContractRevocationVo.setTransNum(transNum);
				TransContractRevocationDao transContractRevocationDao = new TransContractRevocationDao();
				List<TransContractRevocationVo> list = transContractRevocationDao
						.getTransContractRevocation(transContractRevocationVo);
				if (list != null && list.size() > 0) {
					String contentSubFormat = "";
					String PreAllocation = "";
					for (TransContractRevocationVo vo : list) {
						// 契撤原因
						StringBuilder flagMsg = new StringBuilder();
						if ("1".equals(vo.getNeedsFlag())) {
							flagMsg.append("保單規劃不符需求、");
						}
						if ("1".equals(vo.getEconomyFlag())) {
							flagMsg.append("經濟因素、");
						}
						if ("1".equals(vo.getFamilyFlag())) {
							flagMsg.append("家人反對、");
						}
						if ("1".equals(vo.getCognitionFlag())) {
							flagMsg.append("對商品認知有誤、");
						}
						if ("1".equals(vo.getOtherFlag())) {
							flagMsg.append("其他、");
						}
						String msg = flagMsg.substring(0, flagMsg.length() - 1);
						// 是否為信用卡
						String rcpTypeCode = vo.getRcpTypeCodeFlag();
						if (StringUtils.isNotEmpty(rcpTypeCode)) {
							if ("A".equals(rcpTypeCode) && StringUtils.isNotEmpty(vo.getOldDetails())) {
								String oldDetails = vo.getOldDetails();
								String[] oldDetail = oldDetails.split(";");
								String swiftCode = oldDetail[0];
								String englishName = oldDetail[1];
								String neacName = oldDetail[2];
								String bankName = oldDetail[3];
								String branchName = oldDetail[4];
								String account = oldDetail[5];

								contentSubFormat = "(變更前)： \r\n  SwiftCode : %s \r\n  英文戶名 : %s \r\n  帳戶名稱 : %s \r\n  銀行名稱 : %s \r\n  分行名稱 : %s \r\n  帳號 : %s \r\n  ";
								before.append(String.format(contentSubFormat,
										StringUtils.trimToEmpty(StringUtils.isNotEmpty(swiftCode) ? swiftCode : ""),
										StringUtils.trimToEmpty(StringUtils.isNotEmpty(englishName) ? englishName : ""),
										StringUtils.trimToEmpty(neacName), StringUtils.trimToEmpty(bankName),
										StringUtils.trimToEmpty(branchName), StringUtils.trimToEmpty(account)));
								contentSubFormat = "(變更後)： \r\n  SwiftCode : %s \r\n  英文戶名 : %s \r\n  帳戶名稱 : %s \r\n  銀行名稱 : %s \r\n  分行名稱 : %s \r\n  帳號 : %s \r\n  契撤原因：%s  \r\n";
								after.append(String.format(contentSubFormat,
										StringUtils.trimToEmpty(
												StringUtils.isNotEmpty(vo.getSwiftCode()) ? vo.getSwiftCode() : ""),
										StringUtils.trimToEmpty(
												StringUtils.isNotEmpty(vo.getEnglishName()) ? vo.getEnglishName() : ""),
										StringUtils.trimToEmpty(vo.getNeacName()),
										StringUtils.trimToEmpty(vo.getBankName()),
										StringUtils.trimToEmpty(vo.getBranchName()),
										StringUtils.trimToEmpty(vo.getAccount()), StringUtils.trimToEmpty(msg)));

							} else if ("H".equals(rcpTypeCode)) { // 信用卡退款不需要變更前資料
								before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
								contentSubFormat = "(變更後)： \r\n 契撤原因：%s  \r\n";
								after.append(String.format(contentSubFormat, StringUtils.trimToEmpty(msg)));
							} else { // 非信用卡 需要判別 是否有變更退款帳戶
								PreAllocation = ""; // 變更前
								before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
								contentSubFormat = "(變更後)： \r\n  SwiftCode : %s \r\n  英文戶名 : %s \r\n  帳戶名稱 : %s \r\n  銀行名稱 : %s \r\n  分行名稱 : %s \r\n  帳號 : %s \r\n  契撤原因：%s  \r\n";
								after.append(String.format(contentSubFormat,
										StringUtils.trimToEmpty(
												StringUtils.isNotEmpty(vo.getSwiftCode()) ? vo.getSwiftCode() : ""),
										StringUtils.trimToEmpty(
												StringUtils.isNotEmpty(vo.getEnglishName()) ? vo.getEnglishName() : ""),
										StringUtils.trimToEmpty(vo.getNeacName()),
										StringUtils.trimToEmpty(vo.getBankName()),
										StringUtils.trimToEmpty(vo.getBranchName()),
										StringUtils.trimToEmpty(vo.getAccount()), StringUtils.trimToEmpty(msg)));
							}
						} else {
							PreAllocation = ""; // 變更前
							before.append(String.format(contentSubFormat, StringUtils.trimToEmpty(PreAllocation)));
							contentSubFormat = "(變更後)： \r\n  SwiftCode : %s \r\n  英文戶名 : %s \r\n  帳戶名稱 : %s \r\n  銀行名稱 : %s \r\n  分行名稱 : %s \r\n  帳號 : %s \r\n  契撤原因：%s  \r\n";
							after.append(String.format(contentSubFormat,
									StringUtils.trimToEmpty(
											StringUtils.isNotEmpty(vo.getSwiftCode()) ? vo.getSwiftCode() : ""),
									StringUtils.trimToEmpty(
											StringUtils.isNotEmpty(vo.getEnglishName()) ? vo.getEnglishName() : ""),
									StringUtils.trimToEmpty(vo.getNeacName()),
									StringUtils.trimToEmpty(vo.getBankName()),
									StringUtils.trimToEmpty(vo.getBranchName()),
									StringUtils.trimToEmpty(vo.getAccount()), StringUtils.trimToEmpty(msg)));
						}
					}
				}
				logger.debug("契約撤銷 before:{}", before);
				logger.debug("契約撤銷 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransContractRevocation error: " + e);
			}
			break;
		case TransTypeUtil.DERATE_PAID_OFF_CODE:
			try {
				TransDeratePaidOffVo vo = new TransDeratePaidOffVo();
				vo.setTransNum(transNum);
				TransDeratePaidOffDao dao = new TransDeratePaidOffDao();
				vo = dao.getTransDeratePaidOff(vo);
				TransPolicyVo policyVo = new TransPolicyVo();
				policyVo.setTransNum(transNum);
				TransPolicyDao policyDao = new TransPolicyDao();
				List<TransPolicyVo> policyVoList = policyDao.getTransPolicyList(policyVo);
				LilipiDao lilipiDao = new LilipiDao();
				LilipiVo lilipiVo = lilipiDao.findByLipiInsuNo(policyVoList.get(0).getPolicyNo());
				String contentSubFormat = "";
				DecimalFormat df = new DecimalFormat("#,##0");
				contentSubFormat = "(變更前)： \r\n  保額 : %s 元";
				before.append(String.format(contentSubFormat, df.format(lilipiVo.getLipiMainAmt())));
				contentSubFormat = "(變更後)： \r\n  保額 : %s 元";
				after.append(
						String.format(contentSubFormat,
								StringUtils.trimToEmpty(StringUtils.isNotEmpty(vo.getDerateAmt())
										? df.format(new BigDecimal(vo.getDerateAmt()))
										: "0")));
				logger.debug("減額繳清 before:{}", before);
				logger.debug("減額繳清 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransDeratePaidOff error: " + e);
			}
			break;
		case TransTypeUtil.ROLLOVER_PERIODICALLY_CODE:
			try {
				TransRolloverPeriodicallyVo vo = new TransRolloverPeriodicallyVo();
				vo.setTransNum(transNum);
				TransRolloverPeriodicallyDao dao = new TransRolloverPeriodicallyDao();
				vo = dao.getTransRolloverPeriodically(vo);
				TransPolicyVo policyVo = new TransPolicyVo();
				policyVo.setTransNum(transNum);
				TransPolicyDao policyDao = new TransPolicyDao();
				List<TransPolicyVo> policyVoList = policyDao.getTransPolicyList(policyVo);
				LilipiDao lilipiDao = new LilipiDao();
				LilipiVo lilipiVo = lilipiDao.findByLipiInsuNo(policyVoList.get(0).getPolicyNo());
				DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
				String endDate = dateformat.format(lilipiVo.getLipiInsuEndDate());
				String contentSubFormat = "";
				DecimalFormat df = new DecimalFormat("#,##0");
				contentSubFormat = "(變更前)： \r\n 投保終期 : %s \r\n 保額 : %s 元";
				before.append(
						String.format(contentSubFormat, westToTwDate(endDate), df.format(lilipiVo.getLipiMainAmt())));
				contentSubFormat = "(變更後)： \r\n 投保終期 : %s \r\n 保額 : %s 元";
				after.append(String.format(contentSubFormat,
						StringUtils
								.trimToEmpty(StringUtils.isNotEmpty(vo.getRolloverDate()) ? vo.getRolloverDate() : ""),
						StringUtils.trimToEmpty(StringUtils.isNotEmpty(vo.getRolloverAmt())
								? df.format(new BigDecimal(vo.getRolloverAmt()))
								: "0")));
				logger.debug("展期延期 before:{}", before);
				logger.debug("展期延期 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransRolloverPeriodically error: " + e);
			}

			break;
		case TransTypeUtil.CHOOSE_LEVEL_CODE:
			try {
				TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
				transChooseLevelVo.setTransNum(transNum);
				TransChooseLevelDao transChooseLevelDao = new TransChooseLevelDao();
				List<TransChooseLevelVo> transChooseLevelList = transChooseLevelDao
						.getTransChooseLevel(transChooseLevelVo);
				String contentSubFormat = "";
				for (TransChooseLevelVo vo : transChooseLevelList) {
					contentSubFormat = " 風險等級: %s ";
					before.append(String.format(contentSubFormat, vo.getChooseLevelOld()));
					if(vo.getRuleStatus().equals("3")) {
						contentSubFormat = " 風險等級: %s \r\n 風險等級問題與答案 : \r\n %s ";						
					}else {
						contentSubFormat = " 風險等級: %s \r\n %s 同意「一、風險屬性評估」之評估結果 \r\n 風險等級問題與答案 : \r\n %s ";
					}

					StringBuilder sb = new StringBuilder();
					String choose = vo.getChoose();
					if (StringUtils.isNotEmpty(choose)) {
						String[] chooseList = choose.split("\n");
						for (String st : chooseList) {
							int stindex = st.indexOf("<br>");
							if(stindex > 0) {
								String  str = st.substring(0 , stindex);
								sb.append(str);
								sb.append("\r\n");
								String str1 = st.substring(stindex+4);
								int stindex2 = str1.indexOf("<br>");
								if(stindex2 > 0) {
									String str2 = str1.substring(0 , stindex2 );
									sb.append(str2);
									sb.append("\r\n");
									sb.append(str1.substring(stindex2 + 4));
									sb.append("\r\n");
								}else {
									sb.append(st.substring(stindex+4));
									sb.append("\r\n");
								}
							}else {					
								sb.append(st);
								sb.append("\r\n");
							}
						}
						sb.toString();
					}
					if(vo.getRuleStatus().equals("3")) {
						after.append(String.format(contentSubFormat, vo.getChooseLevelNew(), sb.toString()));						
					}else {
						String ruleStatus = vo.getRuleStatus().equals("2") ? "有勾選" : "未勾選";	
						after.append(String.format(contentSubFormat, vo.getChooseLevelNew(), ruleStatus ,sb.toString()));	
					}
				}
				logger.debug("官網-風險等級變更 before:{}", before);
				logger.debug("官網-風險等級變更 after:{}", after);
			} catch (Exception e) {
				logger.error("getTransChooseLevel error: " + e);
			}

			break;
		/// End: 優投功能變更明細介接影像系統
		default:
			logger.debug("Undefined trans type, please check it: " + transType);
			break;
		}
	}

	/**
	 * 西元年轉民國年
	 * 
	 * @param westDate YYYY/MM/DD
	 * @return 民國年 格式: YYY/MM/DD
	 */
	private static String westToTwDate(String westDate) {
		String twYear = Integer.parseInt(westDate.substring(0, 4)) - 1911 + "";
		return StringUtils.leftPad(twYear, 3, "0") + westDate.substring(4);
	}

	/**
	 * test main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String transNum = "201810180005";
//		TransVo transVo = new TransVo();
//		transVo.setTransNum(transNum);
//		transVo = new TransDao().findById(transVo);
	}

}
