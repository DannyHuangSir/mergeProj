package com.twfhclife.eservice_batch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.twfhclife.eservice_batch.dao.OnlineChangeInfoDao;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransAnnuityMethodDao;
import com.twfhclife.eservice_batch.dao.TransBankInfoDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDtlDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryOldDao;
import com.twfhclife.eservice_batch.dao.TransBounsDao;
import com.twfhclife.eservice_batch.dao.TransCertPrintDao;
import com.twfhclife.eservice_batch.dao.TransChangeAccountDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDtlDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoOldDao;
import com.twfhclife.eservice_batch.dao.TransCreditCardDateDao;
import com.twfhclife.eservice_batch.dao.TransCushionDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransLoanDao;
import com.twfhclife.eservice_batch.dao.TransPaymodeDao;
import com.twfhclife.eservice_batch.dao.TransPolicyHolderProfileDao;
import com.twfhclife.eservice_batch.dao.TransResendPolicyDao;
import com.twfhclife.eservice_batch.dao.TransRewardDao;
import com.twfhclife.eservice_batch.dao.TransValuePrintDao;
import com.twfhclife.eservice_batch.model.OnlineChangeInfoVo;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.ReportPrintListVo;
import com.twfhclife.eservice_batch.model.TransAnnuityMethodVo;
import com.twfhclife.eservice_batch.model.TransBankInfoVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryVo;
import com.twfhclife.eservice_batch.model.TransBounsVo;
import com.twfhclife.eservice_batch.model.TransCertPrintVo;
import com.twfhclife.eservice_batch.model.TransChangeAccountVo;
import com.twfhclife.eservice_batch.model.TransContactInfoDtlVo;
import com.twfhclife.eservice_batch.model.TransContactInfoOldVo;
import com.twfhclife.eservice_batch.model.TransContactInfoVo;
import com.twfhclife.eservice_batch.model.TransCreditCardDateVo;
import com.twfhclife.eservice_batch.model.TransCushionVo;
import com.twfhclife.eservice_batch.model.TransLoanVo;
import com.twfhclife.eservice_batch.model.TransPaymodeVo;
import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice_batch.model.TransResendPolicyVo;
import com.twfhclife.eservice_batch.model.TransRewardVo;
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
	private static Map<String, Map<String, String>> paramMap;
	
	private static final String SYSTEM_ID = "eservice";
	
	public BatchUploadOnlineChangeSheetService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		SAVE_PATH = rb.getString("local.file.download.path");
		SAVE_PATH_BACKUP = rb.getString("local.file.backup.file.path");
		FILE_FORMAT = rb.getString("report.upload.file.format");
		FILE_SOURCE_NAME = rb.getString("report.file.src.name");
		paramMap = new HashMap<>();
	}
	
	/**
	 * 主流程
	 * @param inputList
	 */
	public void process(List<TransVo> inputList) {
		
		logger.info("BatchUploadOnlineChangeSheetService start.");
		try {
			if(inputList == null || inputList.size() == 0) {
				logger.info("No upload file pass.");
				return ;
			} else {
				initParamMap();
			}
			/**
			 * 1. Loop transVo list 
			 * 1.1 Get transVo policy info  
			 * 1.2 Get transVo online change info 
			 * 1.3 Prepare upload PDF file 
			 * 1.3.1 Load PDF template 
			 * 1.3.2 Draw data to template 
			 * 1.3.3 Use trans type decide change info detail to template
			 * 1.3.4 Filename used onlinechange_info_{trans_no}.pdf
			 * 1.4 Call Image Manager System API to upload 
			 * 2. Backup upload's file to backup folder 
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String logMessage = "%d. TransNum: %s, %s";
			OnlineChangeInfoDao dao = new OnlineChangeInfoDao();
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String enviorment = rb.getString("running.enviorment");
			for(TransVo transVo: inputList) {
				int process = 0;
				logger.info("============================================================================");
				logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始取得申請資料"));				
				OnlineChangeInfoVo infoVo = dao.getOnlineChangeInfoByTransNum(transVo.getTransNum());
				if (infoVo == null) {
					logger.info("(error #10311)transNum:{}, getOnlineChangeInfoByTransNum is null, can't create onlinechange_info_" + transVo.getTransNum() + ".pdf.", transVo.getTransNum());
					continue;
				}
				StringBuilder beforeChange = new StringBuilder();
				StringBuilder afterChange = new StringBuilder();
				
				logger.info("============================================================================");
				logger.info(String.format(logMessage, process++, transVo.getTransNum(), "開始建立差異內容"));
				this.appendDetailData(transVo.getTransType(), beforeChange, afterChange, transVo.getTransNum());
				logger.info("============================================================================");
				
				try {
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
					listPrintVo.add(new ReportPrintListVo(MyStringUtil.nullToString(infoVo.getMainAmount()), textOrder++));
					// 要保人 LIPM
					listPrintVo.add(new ReportPrintListVo(infoVo.getPolicyHolderName(), textOrder++));
					// 被保人 LIPI
					listPrintVo.add(new ReportPrintListVo(infoVo.getInsuredName(), textOrder++));
					// 幣別 XX
					listPrintVo.add(new ReportPrintListVo(infoVo.getCurrency(), textOrder++));
					// 保單狀態 policy status
					listPrintVo.add(new ReportPrintListVo(paramMap.get("POLICY_STATUS").get(infoVo.getStatus()), textOrder++));
					// 投保始期 policy effective_date
					listPrintVo.add(new ReportPrintListVo(infoVo.getEffectiveDate() == null ? null : sdf.format(infoVo.getEffectiveDate()), textOrder++));
					// 繳費年期 policy premYear
					listPrintVo.add(new ReportPrintListVo(MyStringUtil.objToStr(infoVo.getPremYear()), textOrder++));
					// 繳別 policy paymentMode
					listPrintVo.add(new ReportPrintListVo(paramMap.get("PAYMODE_TYPE").get(infoVo.getPaymentMode()), textOrder++));
					
					for(ReportPrintListVo vo : listPrintVo.stream()
							.sorted( (v1, v2) -> { return v1.getTextOrder().compareTo(v2.getTextOrder());})
							.collect(Collectors.toList())) {
						reportUtil.txt(vo.getTextContent(), fontSize, 1, leftPosition, topPosition);
						topPosition -= range;
					}
					
					// 變更明細資料
					// 變更時間 
					reportUtil.txt(sdf.format(transVo.getCreateDate()), fontSize, 1, 110f, 498f);
					// 變更編號
					reportUtil.txt(transVo.getTransNum(), fontSize, 1, 210f, 498f);
					// 變更種類 trans type to CHT 
					reportUtil.txtAlignCenter(paramMap.get("ONLINE_CHANGE").get(transVo.getTransType()), fontSize, 1, 395f, 498f);
					// 變更前
					// reportUtil.txt(beforeChange.toString(), fontSize, 1, 100f, 440f);
					// 因為沒有辦法直接印成多列，所以修改成每一個換行獨立處理
					float beforeTop = 440f;
					float high = 15f;
					for(String beforeLine: beforeChange.toString().split("\\r\\n")) {
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
					for(String afterLine: afterChange.toString().split("\\r\\n")) {
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
						//上傳申請單到影像系統
						BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
						String token = batchUploadEZService.getEZToken();
						boolean isSucc = batchUploadEZService.uploadFile("U" + transVo.getTransNum(), "PDF", token, reportFile, infoVo);
						
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
					
					if(FileUtil.moveFile(SAVE_PATH + "/" + filename, SAVE_PATH_BACKUP + todayPath + "/" + filename)) {
						logger.info("備份完成");
					} else {
						logger.info("備份失敗");
					}

				} catch(Exception e) {
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
			paramMap.put("ONLINE_CHANGE"
					, paramDao.getParameterByCategoryCode(SYSTEM_ID, "ONLINE_CHANGE").stream()
						.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterName)));
			// setting 繳別
			paramMap.put("PAYMODE_TYPE"
					, paramDao.getParameterByCategoryCode(SYSTEM_ID, "PAYMODE_TYPE").stream()
						.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			// setting 保單狀態
			paramMap.put("POLICY_STATUS"
					, paramDao.getParameterByCategoryCode(SYSTEM_ID, "POLICY_STATUS").stream()
						.collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			
		} catch(Exception e) {
			logger.error("initParamMap error: ", e);
		}
	}
	
	/**
	 * 建立異動內容
	 * @param transType
	 * @param before
	 * @param after
	 * @param transDetailList
	 */
	private void appendDetailData(String transType, StringBuilder before, StringBuilder after, String transNum) {
		TransVo transVo = null;
		switch(transType) {
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
				List<TransBeneficiaryDtlVo> dtlList = new TransBeneficiaryDtlDao().getTransBeneficiaryDtlList(transBeneficiaryDtlVo);
				transBeneficiaryMaster.setDtlList(dtlList);
				
				TransBeneficiaryOldVo transBeneficiaryOldVo = new TransBeneficiaryOldVo();
				transBeneficiaryOldVo.setTransBeneficiaryId(transBeneficiaryMaster.getId());
				List<TransBeneficiaryOldVo> oldList = new TransBeneficiaryOldDao().getTransBeneficiaryOldList(transBeneficiaryOldVo);
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
				logger.debug("==================================================", new Gson().toJson(transContactInfoDtlVo));
				logger.debug("transContactInfoDtlVo:{}", new Gson().toJson(transContactInfoDtlVo));
				
				TransContactInfoOldVo transContactInfoOldVo = new TransContactInfoOldVo();
				transContactInfoOldVo.setTransContactId(transContactInfoMaster.getId());
				transContactInfoOldVo = new TransContactInfoOldDao().getTransContactInfoOld(transContactInfoOldVo);
				transContactInfoMaster.setTransContactInfoOldVo(transContactInfoOldVo);
				logger.debug("==================================================", new Gson().toJson(transContactInfoDtlVo));
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
			List<TransPolicyHolderProfileVo> transPolicyHolderProfileList = new TransPolicyHolderProfileDao().getTransPolicyHolderProfileList(newVo);
			if (transPolicyHolderProfileList != null && transPolicyHolderProfileList.size() > 0) {
				newVo = transPolicyHolderProfileList.get(0);
			}

			new TransPolicyHolderProfileUtil().getChangeContent(before, after, newVo, oldVo);
			break;
		default:
			logger.debug("Undefined trans type, please check it: " + transType);
			break;
		}
	}
	

	/**
	 * test main method 
	 * @param args
	 */
	public static void main(String[] args) {
//		String transNum = "201810180005";
//		TransVo transVo = new TransVo();
//		transVo.setTransNum(transNum);
//		transVo = new TransDao().findById(transVo);
	}
}
