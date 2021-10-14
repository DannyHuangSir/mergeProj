package com.twfhclife.eservice_batch.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.twfhclife.eservice_batch.service.onlineChange.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransStatusHistoryVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.service.onlineChange.*;
import com.twfhclife.eservice_batch.util.FTPUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BatchUploadService {

	private static final Logger logger = LogManager.getLogger(BatchUploadService.class);
	
	public final static String SYSTEM_ID = "eservice_batch";
	
	private List<TransVo> processList;
	
	public BatchUploadService() {
		processList = new ArrayList<>();
	}
	
	public void uploadOnlineChangeFile() {
		logger.info("============================================================================");
		logger.info("0. Start running upload apply file to ftp...");
		logger.info("============================================================================");
		
		StringBuilder txtSb = new StringBuilder();
		String systemTwDate = getTwDate();
		String todayApplyFilepPath = getApplyFilePath();
		
		// 自動審核
		logger.info("============================================================================");
		logger.info("1. Start running autoReview...");
		autoReview();
		logger.info("1. End running autoReview...");
		logger.info("============================================================================");
		
		// 產生申請檔
		logger.info("============================================================================");
		logger.info("2. Start generate apply file...");
		String applyItemText = "";
		try {
			// 2019.01.10 修改 by Ken: 為了上傳明細表紀錄取出的 trans object
			processList.addAll(new TransPaymodeUtil().appendApplyItems(txtSb, systemTwDate));        // 繳別:001 | 035
			processList.addAll(new TransAnnuityMethodUtil().appendApplyItems(txtSb, systemTwDate));  // 年金給付方式:002
			processList.addAll(new TransBounsUtil().appendApplyItems(txtSb, systemTwDate));          // 紅利選擇權:003
			processList.addAll(new TransRewardUtil().appendApplyItems(txtSb, systemTwDate));         // 增值回饋金領取方式:004
			processList.addAll(new TransCushionUtil().appendApplyItems(txtSb, systemTwDate));        // 自動墊繳選擇權:005
			processList.addAll(new TransBeneficiaryUtil().appendApplyItems(txtSb, systemTwDate));    // 受益人 (DS05):006
			processList.addAll(new TransRenewUtil().appendApplyItems(txtSb, systemTwDate));          // 展期定期保險:007
			processList.addAll(new TransReduceUtil().appendApplyItems(txtSb, systemTwDate));         // 減額繳清保險:008
			processList.addAll(new TransReducePolicyUtil().appendApplyItems(txtSb, systemTwDate));   // 減少保險金額(主約：DS01, 附約：DS04):009
			processList.addAll(new TransContactInfoUtil().appendApplyItems(txtSb, systemTwDate));    // 聯絡資料變更 (DS01):010
			// 設定停損停利通知? 待確認暨有程式
			processList.addAll(new TransValuePrintUtil().appendApplyItems(txtSb, systemTwDate));     // 保單價值列印 (DS48):012
			processList.addAll(new TransCertPrintUtil().appendApplyItems(txtSb, systemTwDate));      // 投保證明列印 (DS51):013
			processList.addAll(new TransResendPolicyUtil().appendApplyItems(txtSb, systemTwDate));   // 補發保單 (DS01):014
			processList.addAll(new TransCreditCardDateUtil().appendApplyItems(txtSb, systemTwDate)); // 信用卡有效年月 (CS17):017
			processList.addAll(new TransCancelAuthUtil().appendApplyItems(txtSb, systemTwDate));     // 終止授權 (CS17):018
			processList.addAll(new TransChangeAccountUtil().appendApplyItems(txtSb, systemTwDate));  // 匯款帳號變更 (CS17):023
			processList.addAll(new TransLoanUtil().appendApplyItems(txtSb, systemTwDate));  		 // 保單借款(線上):026
			processList.addAll(new TransPolicyHolderProfileUtil().appendApplyItems(txtSb, systemTwDate));// 保戶基本資料更新:027
			processList.addAll(new TransDepositUtil().appendApplyItems(txtSb, systemTwDate));// 申請保單提領(贖回):028
			processList.addAll(new TransInvestmentUtil().appendApplyItems(txtSb, systemTwDate));// 未來保費投資標的與分配比例:029
			processList.addAll(new TransConversionUtil().appendApplyItems(txtSb, systemTwDate));// 變更投資標的與分配比例:030
			processList.addAll(new TransCashPaymentUtil().appendApplyItems(txtSb, systemTwDate));// 收益分配或撥回資產分配方式:032
			processList.addAll(new TransChangePremiumUtil().appendApplyItems(txtSb, systemTwDate));// 定期定額保費變更:034
			processList.addAll(new TransRiskLevelUtil().appendApplyItems(txtSb, systemTwDate));// 變更風險屬性:031

			applyItemText = txtSb.toString();
			if (StringUtils.isEmpty(applyItemText)) {
				// 產生空檔
				FileUtils.writeStringToFile(new File(todayApplyFilepPath), "", "big5");
			} else {
				// 合併變更(一):101
				String newApplyItemText = "";
				MergeChangeUtil mcUtil = new MergeChangeUtil();
				List<String> mergePolicyNoList = mcUtil.getMergePolicyNoList(applyItemText);
				if (mergePolicyNoList != null && mergePolicyNoList.size() > 0) {
					newApplyItemText = mcUtil.mergeApplyItem(applyItemText, mergePolicyNoList, systemTwDate);
					writeNoMergeChangeFile(applyItemText, todayApplyFilepPath);
				} else {
					newApplyItemText = applyItemText;
				}
				FileUtils.writeStringToFile(new File(todayApplyFilepPath), newApplyItemText, "big5");
			}
		} catch (Exception e) {
			logger.error("create apply file error:", e);
		}
		logger.info("2. End generate apply file...");
		logger.info("============================================================================");
		
		// 上傳申請檔
		logger.info("============================================================================");
		logger.info("3. Start running upload apply file to FTP...");
		boolean uploadResult = uploadFileToFTP(todayApplyFilepPath);
		logger.info("3. End running upload apply file to FTP...");
		logger.info("============================================================================");
		
		// 更新已上傳
		logger.info("============================================================================");
		logger.info("4. Start running update trans status to uploaded:5...");
		if (uploadResult) {
			updateUpload(applyItemText);
		}
		logger.info("4. End running update trans status to uploaded:5...");
		logger.info("============================================================================");
	}

	/**
	 * 自動審核，更新狀態為1
	 */
	private void autoReview() {
		ParameterDao parameterDao = new ParameterDao();
		TransDao transDao = new TransDao();

		// 讀取自動審核的申請類型
		List<ParameterVo> autoReviewTypeList = parameterDao.getParameterByCategoryCode(SYSTEM_ID,
				"AUTO_REVIEW_TRNAS_TYPE");
		for (ParameterVo parameterVo : autoReviewTypeList) {
			String autoReviewType = parameterVo.getParameterValue();
			
			TransVo qryVo = new TransVo();
			qryVo.setStatus("0"); // 0:處理中
			qryVo.setTransType(autoReviewType);
			List<TransVo> transList = transDao.getTransList(qryVo);
			for (TransVo transVo : transList) {
				String transNum = transVo.getTransNum();
				logger.info("Auto review transType[{}] for transNum: {}", autoReviewType, transNum);
				
				transVo.setStatus("1");
				transVo.setUpdateDate(new Date());
				transVo.setUpdateUser(SYSTEM_ID);
				
				int result = transDao.updateTrans(transVo);
				if (result > 0) {
					logger.info("Auto review transType[{}] for transNum success: {}", transNum);
				} else {
					logger.info("Auto review transType[{}] for transNum fail: {}", transNum);
				}
			}
		}
	}
	
	/**
	 * 更新狀態為已上傳.
	 * 
	 * @param applyItemText 沒有合併的項目
	 */
	private void updateUpload(String applyItemText) {
		TransDao transDao = new TransDao();
		try {
			String[] applyLines = applyItemText.split("\r\n");
			List<String> doneList = new ArrayList<String>();
			for (String line : applyLines) {
				if (!"".equals(StringUtils.trimToEmpty(line)) && line.length() >= 15) {
					String transNum = line.substring(3, 15);
					if (doneList.contains(transNum)) {
						continue;
					}
					doneList.add(transNum);
					logger.info("Update trans status to uploaded for transNum : {}", transNum);
					
					TransVo updVo = new TransVo();
					updVo.setTransNum(transNum);
					updVo.setStatus("5"); // 5:已上傳
					updVo.setUpdateDate(new Date());
					updVo.setUpdateUser(SYSTEM_ID);
					
					int result = transDao.updateTrans(updVo);
					if (result > 0) {
						logger.info("Update trans status to uploaded for transNum success: {}", transNum);

						/**
						 * 追加是保全的才進行發生,狀態歷程"已上傳"的時間記錄;
						 */
						if (line.startsWith("010")) {//聯絡資料變更 (DS01):010
							addTransStatusHistory(transNum, "5");
							logger.info("addTransStatusHistory: {}={}:Z執行的響應行數:{}", "5", "已上傳");
						}
					} else {
						logger.info("Update trans status to uploaded for transNum fail: {}", transNum);
					}
				}
			}
		} catch (Exception e) {
			logger.error("update trans status to uploaded error:", e);
		}
	}
	
	/**
	 * 上傳檔案.
	 * 
	 * @param todayApplyFilepPath 申請檔路徑
	 */
	private boolean uploadFileToFTP(String todayApplyFilepPath) {
		boolean uploadResult = false;
		Session session = null;
		try {
			if (!new File(todayApplyFilepPath).exists()) {
				return false;
			}
			
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String remoteUploadPath = rb.getString("sftp.upload.path");
			String localUploadPath = rb.getString("local.file.upload.path");
			
			File localUploadDir = new File(localUploadPath);
			if (!localUploadDir.exists()) {
				FileUtils.forceMkdir(localUploadDir);
			}

			// 取得SFTP連線資訊
			String username = rb.getString("sftp.username");
			String password = rb.getString("sftp.password");
			String host = rb.getString("sftp.host");
			String port = rb.getString("sftp.port");
			
			//if ftp
			if ("21".equals(port)) {
				// 取得準備上傳至SFTP(ToCTC) 的檔案清單
				String remoteApplyFilePath = remoteUploadPath + "/CHANGE_REQ.txt";
				logger.info("localUploadPath(ftp): {}", localUploadPath);
				logger.info("remoteUploadPath(ftp): {}", remoteUploadPath);
				logger.info("remoteApplyFilePath(ftp): {}", remoteApplyFilePath);

				FTPUtil ftpUtil = new FTPUtil(username, password, host, port);
				ftpUtil.connect();
				boolean result = ftpUtil.storeFile(todayApplyFilepPath, remoteApplyFilePath);
				if (!result) {
					return false;
				}
				ftpUtil.disConnect();
			} else {
				JSch jsch = new JSch();
				session = jsch.getSession(username, host, Integer.parseInt(port));
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				session.connect(60000);

				Channel channel = session.openChannel("sftp");
				channel.connect();

				ChannelSftp sftpChannel = (ChannelSftp) channel;

				// 取得準備上傳至SFTP(ToCTC) 的檔案清單
//				String remoteApplyFilePath = todayApplyFilepPath.replace(localUploadPath, remoteUploadPath);
				String remoteApplyFilePath = remoteUploadPath + "/CHANGE_REQ.txt";
				logger.info("localUploadPath(sftp): {}", localUploadPath);
				logger.info("remoteUploadPath(sftp): {}", remoteUploadPath);
				logger.info("remoteApplyFilePath(sftp): {}", remoteApplyFilePath);
				
				sftpChannel.put(todayApplyFilepPath, remoteApplyFilePath);
				
			}
			
			uploadResult = true;
		} catch (Exception e) {
			logger.error("uploadFileToFTP error:", e);
			uploadResult = false;
		} finally {
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		return uploadResult;
	}
	
	/**
	 * 取得系統民國年
	 * 
	 * @return
	 */
	private String getTwDate() {
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String twYear = Integer.parseInt(today.substring(0, 4)) - 1911 + "";
		return StringUtils.rightPad(twYear, 3, "0") + today.substring(4, 8);
	}
	
	/**
	 * 取得系統日申請檔路徑.
	 * 
	 * @return
	 */
	private String getApplyFilePath() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String localUploadPath = rb.getString("local.file.upload.path");
		String dateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		try {
			File localUploadDir = new File(localUploadPath);
			if (!localUploadDir.exists()) {
				FileUtils.forceMkdir(localUploadDir);
			}
		} catch (IOException e) {
			logger.error("Unable to forceMkdir error: ", e);
		}
		
		return String.format("%s/%sUPLOAD.txt", localUploadPath, dateFormat);
	}
	
	/**
	 * 產生沒有合併的前的申請檔.
	 * 
	 * @return
	 */
	private void writeNoMergeChangeFile(String applyItemText, String todayApplyFilepPath) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String writeNoMergeChangeFile = rb.getString("writeNoMergeChangeFile");
		try {
			if ("Y".equals(writeNoMergeChangeFile)) {
				String noMergeChangeFilePath = todayApplyFilepPath.replace("UPLOAD", "NOMERGE");
				FileUtils.writeStringToFile(new File(noMergeChangeFilePath), applyItemText, "big5");
			}
		} catch (IOException e) {
			logger.error("Unable to writeNoMergeChangeFile error: ", e);
		}
	}

	/**
	 * 取出處理的 TRANS data
	 * @return
	 */
	public List<TransVo> getProcessList() {
		return processList == null? Collections.emptyList(): processList;
	}
	
	/***
	 * 從壽險核心接收回案件狀    回寫狀態
	 * @param transNum  單號
	 * @param s  狀態
	 */
	public void addTransStatusHistory(String transNum, String s) {
		TransDao transDao = new TransDao();
		try {
			logger.info(" update TransStatusHistory[{}] status[{}] ", transNum, s);
			TransStatusHistoryVo transStatusHistoryVo = new TransStatusHistoryVo();
			transStatusHistoryVo.setStatus(s);
			transStatusHistoryVo.setTransNum(transNum);
			transStatusHistoryVo.setCustomerName("系統日程");
			transStatusHistoryVo.setUserIdentity("系統日程");
			transStatusHistoryVo.setRequestDate(new Date());
			if (transNum != null) {
				int in = transDao.addTransStatusHistory(transStatusHistoryVo);
				if (in > 0) {
					logger.info("=========================== add   TransStatusHistory success===========================");
				} else {
					logger.info("=========================== add   TransStatusHistory fail===========================");
				}
			} else {
				logger.info("=======update   TransStatusHistory   is  args  transNum  is null =================");
			}
		} catch (Exception ex) {
			logger.error(" add   TransStatusHistory Exception:{}", ex);
		}
	}
	
}
