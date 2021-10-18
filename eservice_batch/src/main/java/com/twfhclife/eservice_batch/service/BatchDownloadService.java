package com.twfhclife.eservice_batch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import com.twfhclife.eservice_batch.dao.*;
import com.twfhclife.eservice_batch.model.*;
import com.twfhclife.eservice_batch.service.onlineChange.TransRiskLevelUtil;
import com.twfhclife.eservice_batch.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class BatchDownloadService {

	private static final Logger logger = LogManager.getLogger(BatchDownloadService.class);
	
	public final static String SYSTEM_ID = "eservice_batch";
	
	private ResourceBundle rb = ResourceBundle.getBundle("config");
	
	public void downloadOnlineChangeFile() {
		// 下載回應檔
		logger.info("============================================================================");
		logger.info("0. Start running download CHANGE_RSP file from remote ftp...");
		String downloadFileName = downloadFileFromFTP("CHANGE_RSP");
		logger.info("0. End running download CHANGE_RSP file from remote ftp...");
		
		if (!"".equals(downloadFileName)) {
			logger.info("============================================================================");
			logger.info("1. Start running update trans status from CHANGE_RSP file...");
			updateApplyResult(downloadFileName);
			logger.info("1. End running update trans status from CHANGE_RSP file...");
			logger.info("============================================================================");
		} else {
			logger.info("1. There's no file retrieved CHANGE_RSP today !!! end job");
		}
	}
	
	public void downloadTransEndormentFile() {
		// 下載批註檔
		logger.info("============================================================================");
		logger.info("2. Start running download ENDORSEMENT_RSP file from remote ftp...");
		String pichuFileName = downloadFileFromFTP("ENDORSEMENT_RSP");//"C:/tmp/eservice/download/ENDORSEMENT_RSP.2019011103268000";
		logger.info("2. End running download ENDORSEMENT_RSP file from remote ftp...");
		
		if (!"".equals(pichuFileName)) {
			logger.info("============================================================================");
			logger.info("3. Start running write trans_endorsement from ENDORSEMENT_RSP file...");
			Map<String, List<TransEndorsementVo>> endorsementMap = this.saveTransEndorsement(pichuFileName);
			List<String> fileNameList = this.genTransEndorsementPDF(endorsementMap);

			String sourcePath = rb.getString("local.file.download.path");
			String targetPath = rb.getString("local.file.backup.file.path");
			
			//移至備份資料夾
			File file = new  File(targetPath);
			if (!file.exists()) {
				file.mkdir();
			}
			
			String today = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			String todayPath = "/" + today;
			for (String fileName : fileNameList) {
				FileUtil.moveFile(sourcePath + fileName, targetPath + todayPath + fileName);
			}
			
			//移除n天前的備份資料夾
			for(File bakFile : file.listFiles()) {
				if (Integer.parseInt(bakFile.getName()) <= Integer.parseInt(today) - 7) {
					FileUtil.forceDeleteDir(bakFile.getPath());
				}
			}
			
			logger.info("3. End running write trans_endorsement from ENDORSEMENT_RSP file...");
			logger.info("============================================================================");
		} else {
			logger.info("3. There's no file retrieved ENDORSEMENT_RSP today !!! end job");
		}
	}
	
	/**
	 * 下載檔案.
	 */
	@SuppressWarnings("unchecked")
	private String downloadFileFromFTP(String prefixName) {
		String downloadFileName = "";
		Session session = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String remoteDownloadPath = rb.getString("sftp.download.path");
			String localDownloadPath = rb.getString("local.file.download.path");
			
			File localDownloadDir = new File(localDownloadPath);
			if (!localDownloadDir.exists()) {
				FileUtils.forceMkdir(localDownloadDir);
			}

			// 取得SFTP連線資訊
			String username = rb.getString("sftp.username");
			String password = rb.getString("sftp.password");
			String host = rb.getString("sftp.host");
			String port = rb.getString("sftp.port");
			logger.info("connect info => username:{}, password:{}, host:{}, port:{}", username, password, host, port);

			if ("21".equals(port)) {
				FTPUtil ftpUtil = new FTPUtil(username, password, host, port);
				ftpUtil.connect();
				List<String> fileList = ftpUtil.listFile(remoteDownloadPath);
				for (String fileName : fileList) {
					// TODO: 壽險核心→保戶專區 ToELIFE CHANGE_RSP.xxxxxxxx
					if (fileName.startsWith(prefixName)) {
						String remoteFilePath = (remoteDownloadPath + "/" + fileName);
						String localFilePath = (localDownloadPath + "/" + fileName);
						ftpUtil.downloadFile(remoteDownloadPath + "/", fileName, new File(localFilePath));
						
						logger.info("remoteDownloadPath: {}", remoteDownloadPath);
						logger.info("localDownloadPath: {}", localDownloadPath);
						logger.info("fileName: {}", fileName);
						logger.info("remoteFilePath: {}", remoteFilePath);
						logger.info("localFilePath: {}", localFilePath);
						
						downloadFileName = localFilePath;
						logger.info("Download {} to {}...", remoteFilePath, localFilePath);
						ftpUtil.deleteFile(remoteDownloadPath, fileName);
						logger.info("delete remoteFile: {}", remoteFilePath);
						break;
					}		
				}
				ftpUtil.disConnect();
				
			} else {
				JSch jsch = new JSch();
				session = jsch.getSession(username, host, Integer.parseInt(port));
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				session.connect();

				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftpChannel = (ChannelSftp) channel;

				// 取得準備下載的的檔案清單
				Vector<ChannelSftp.LsEntry> fileList = sftpChannel.ls(remoteDownloadPath);
				if (fileList.isEmpty()) {
					logger.info("No file exist in the specified sftp folder location.");
				} else {
					
					for (ChannelSftp.LsEntry entry : fileList) {
						try {
							String fileName = entry.getFilename();
							String prefix = prefixName;
							// TODO: 壽險核心→保戶專區 ToELIFE CHANGE_RSP.xxxxxxxx
							if (fileName.startsWith(prefix)) {
								String remoteFilePath = (remoteDownloadPath + "/" + fileName);
								String localFilePath = (localDownloadPath + "/" + fileName);
								logger.info("remoteDownloadPath: {}", remoteDownloadPath);
								logger.info("localDownloadPath: {}", localDownloadPath);
								logger.info("fileName: {}", fileName);
								logger.info("remoteFilePath: {}", remoteFilePath);
								logger.info("localFilePath: {}", localFilePath);
								
								sftpChannel.get(remoteFilePath, localFilePath);
								downloadFileName = localFilePath;
								logger.info("Download {} to {}...", remoteFilePath, localFilePath);
								sftpChannel.rm(remoteFilePath);
								logger.info("rm remoteFile: {}", remoteFilePath);
								break;
							}		

						} catch (SftpException sftpException) {
							logger.info("Failed to download the file the sftp folder location: {}", sftpException);
						}
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("downloadFileFromFTP error:", e);
		} finally {
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		
		logger.info("Download file from FTP : {}", downloadFileName);
		return downloadFileName;
	}
	
	private void updateApplyResult(String downloadFileName) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle("config");
			Map<String, String> transTypeMap = this.getTransTypeMapping();
			File file = new File(downloadFileName);
			List<String> applyLines = FileUtils.readLines(file);
			ParameterDao parameterDao = new ParameterDao();
			
			logger.info("\n");
			int i = 1;
			
			String transOkCode = parameterDao.getParameterValueByCode("eservice", "TRANS_OK_CODE");
			Map<String, String> okMap = new HashMap<String, String>();
			if (transOkCode != null) { 
				for (String str : transOkCode.split("&")) {
					//001:1,2&
					String[] s = str.split(":");
					okMap.put(s[0], s[1]);
				}
			}
			
			for (String line : applyLines) {
				String transCode = line.substring(0, 3);
				String transNum = line.substring(3, 15);
				//获取保單號碼(10)
				String insuranceNum = line.substring(15, 25);

				logger.info("======================================================");
				// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),處理結果(1),備註(100)
				String applyResult = line.substring(32, 33);
				String transType = transTypeMap.get(transCode) == null? transCode: transTypeMap.get(transCode);
				logger.info("line[{}]: {}", i, line);
				logger.info("transCode: {}", transCode);
				logger.info("transNum: {}", transNum);
				logger.info("applyResult: {}", applyResult);
				logger.info("transType: {}", transType);
				
				if (!okMap.containsKey(transCode)) {
					// 未設定成功代碼不執行直接跳出
					continue;
				}
				
				String successCode = okMap.get(transCode);
				//通过保单获取保户邮件地址与单当前保单是否不同意轉送聯盟鏈与聯盟轉收件
				List<Map<String, Object>> transContactInfoDetailList =null;
				if(transNum!=null) {
						TransContactInfoDtlDao transContactInfoDtlDao = new TransContactInfoDtlDao();
						transContactInfoDetailList = transContactInfoDtlDao.getTransContactInfoDetailList(transNum);
				}
				if(successCode.indexOf(applyResult) != -1) {
					updateTransStatus(transNum, transType, "2"); // 0:已完成
					logger.info("updateTransStatus: {}={}", "2","已完成");

					/**
					 * 追加是保全的才進行發生,狀態歷程"已完成"的時間記錄;
					 */
					if("010".equals(transCode)) {
						addTransStatusHistory(transNum, "2");
						logger.info("addTransStatusHistory: {}={}:Z執行的響應行數:{}", "2", "已完成");
					}

					/**
					 * 发送邮件
					 */
					if(transNum!=null) {
						if (transContactInfoDetailList!=null  && transContactInfoDetailList.size()>0) {
							Map<String, Object> stringObjectMap = transContactInfoDetailList.get(0);
							String contact_info = (String) stringObjectMap.get("TRANS_TYPE");
							String from_company_id = (String) stringObjectMap.get("FROM_COMPANY_ID");
							if(CallApiMailCode.CONTACT_INFO.equals(contact_info)) {
								/**
								 * 发送邮件给管理员
								 */
								String s = resultManageSendMail(insuranceNum, from_company_id, "2");
								logger.info("從壽險核心接收回案件狀態,發送郵件給管理員-----{}", s);

								/**
								 * 发送邮件给保戶
								 */
								logger.info("***start to send mail to insurance member***");
								String send_alliance = (String) stringObjectMap.get("SEND_ALLIANCE");
								String email = (String) stringObjectMap.get("EMAIL");
								logger.info("send_alliance="+send_alliance);
								logger.info("email="+email);
								logger.info("from_company_id="+from_company_id);
								String oldEmail = (String) stringObjectMap.get("OEMAIL");
								logger.info("oldEmail="+oldEmail);
								logger.info("*** dtl mail:{} to message***",email);
								
								List<String> listMail = new ArrayList<String>();
								if(email!=null && StringUtils.isNotEmpty(email.trim())) {
									listMail.add(email);
								}
								if(oldEmail!=null && StringUtils.isNotEmpty(oldEmail)) {
									listMail.add(oldEmail);
								}
								
								if(listMail!=null && listMail.size()>0){
									String[] mails = (String[])listMail.toArray(new String[0]);
									if (!"L01".equals(from_company_id)) {
										/*
										 *  @ policyType    [首家不同意轉送聯盟鏈/聯盟轉收件]
										 * @ policyState   線上/保全聯盟鏈轉送
										 * @ messagingTemplateCode  郵件模板
										 * @ insuredMail  保戶郵件
										 * */
										  resultInsuredSendMail("聯盟轉收件", "保全聯盟鏈轉送", CallApiMailCode.TRANSFER_MAIL_021, mails);
									} else {
										if (!"N".equals(send_alliance)) {
											resultInsuredSendMail("首家同意轉送聯盟鏈", null, CallApiMailCode.TRANSFER_MAIL_022, mails);
										} else {
											resultInsuredSendMail("首家不同意轉送聯盟鏈", "線上", CallApiMailCode.TRANSFER_MAIL_021, mails);
										}

									}
								 }

								logger.info("***end to send mail to insurance member***");
							}
						}
					}


					if("012".equals(transCode)) {
						// 保單價值列印還有後續處理
						String policyNum = line.substring(15, 25);
						String twDate = line.substring(25, 32);
						String rtnData = line.substring(33, line.length());
						try {
							this.processValuePrint(transNum, policyNum, rtnData, twDate);
						} catch(Exception e) {
							logger.error("Create Value_Print pdf error: ", e);
						}
					}

					if (StringUtils.equals("031", transCode)) {
						TransRiskLevelUtil.updateIndividual(transNum);
					}

				} else {
					updateTransStatus(transNum, transType, "6"); // 6:失敗
					logger.info("updateTransStatus: {}={}", "6","失敗");

					/**
					 * 追加是保全的才進行發生,狀態歷程"失敗"的時間記錄;
					 */
					if("010".equals(transCode)) {
						addTransStatusHistory(transNum, "6");
						logger.info("addTransStatusHistory: {}={}:Z執行的響應行數:{}", "6", "失敗");
					}
				}
				
				if("027".equals(transCode)) {
					// 保戶基本資料更新不通知
					continue;
				}
				
				try {
					String enviorment = rb.getString("running.enviorment");
					if ("dev".equalsIgnoreCase(enviorment)) {
						continue;
					}
					// 變更完成後，以該保單號碼的手機及電子郵件信箱寄出變更完成通知訊息
					List<ParameterVo> paramList = parameterDao.getParameterByCategoryCode("eservice", "trans_" + transCode + "_mapping");
					String remark = "";
					String policyNum = line.substring(15, 25);
					if ("X".equals(applyResult)) {
						remark = line.substring(34, 133); //100
					} else {
						for (ParameterVo vo : paramList) {
							if (vo.getParameterCode().equalsIgnoreCase(applyResult)) {
								remark = vo.getParameterValue();
								break;
							}
						}
					}
					
					// send mail or sms
					BatchApiService api = new BatchApiService();
					MessageTriggerRequestVo req = new MessageTriggerRequestVo();
					req.setMessagingTemplateCode("ELIFE_MAIL-004");
					
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("TransNum", transNum);
					paramMap.put("TransStatus", successCode.indexOf(applyResult) != -1 ? "成功" : "失敗");
					paramMap.put("TransRemark", remark);
					req.setParameters(paramMap);
					
					UserDao userDao = new UserDao();
					UserVo userVo = userDao.getMailPhoneByPolicyNo(policyNum);
					if (StringUtils.isNotEmpty(userVo.getEmail())) {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getEmail());
						req.setMessagingReceivers(receivers);
						req.setSendType(MessageTriggerRequestVo.SEND_TYPE_MAIL);
						api.postMessageTemplateTrigger(req);
					}
					if (StringUtils.isNotEmpty(userVo.getMobile())) {
						List<String> receivers = new ArrayList<String>();
						receivers.add(userVo.getMobile());
						req.setMessagingReceivers(receivers);
						req.setSendType(MessageTriggerRequestVo.SEND_TYPE_SMS);
						api.postMessageTemplateTrigger(req);
					}
				} catch (Exception e) {
					logger.error("寄出變更完成通知訊息 error:", e);
				}
				
				i ++;
				logger.info("======================================================\n\n");
			}
		} catch (IOException e) {
			logger.error("update apply result error:", e);
		}
	}

	/***
	 * 從壽險核心接收回案件狀    回寫狀態
	 * @param transNum  單號
	 * @param s  狀態
	 */
	public void addTransStatusHistory(String transNum, String s) {
		TransDao transDao = new TransDao();
		try {
			logger.info(" update   TransStatusHistory[{}] status[{}] ", transNum, s);
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

	/**
	 * 從壽險核心接收回案件狀  發送郵件給保戶
	 * @param policyType    [首家不同意轉送聯盟鏈/聯盟轉收件]
	 * @param policyState   線上/保全聯盟鏈轉送
	 * @param messagingTemplateCode  郵件模板
	 * @param insuredMail  保戶郵件
	 * @return
	 */
	public String  resultInsuredSendMail(String policyType, String policyState
			, String messagingTemplateCode, String[] insuredMails){
		logger.info("**In resultInsuredSendMail**");
		String resultMsg = null;
		try {
			IMessagingTemplateServiceImpl transContactInfoService = new IMessagingTemplateServiceImpl();
			Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			//paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			//paramMap.put("POLICY_TYPE", policyType);
			if(policyState!=null) {
				paramMap.put("POLICY_STATE", policyState);
			}
			logger.info("paramMap:"+paramMap.toString());
			
			//發送保戶
			List<String> receivers = new ArrayList<String>();
			if(insuredMails!=null && insuredMails.length>0) {
				for(String str : insuredMails) {
					receivers.add(str);
				}
			}
			logger.info("receivers:"+receivers.toString());

			MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
			vo.setMessagingTemplateCode(messagingTemplateCode);
			vo.setSendType("email");
			vo.setMessagingReceivers(receivers);
			vo.setParameters(paramMap);
			vo.setSystemId("eservice_batch");
			logger.info("MessageTriggerRequestVo:"+vo.toString());
			
			//进行发送通信
			IMessagingTemplateServiceImpl iMessagingTemplateService = new IMessagingTemplateServiceImpl();
			resultMsg = iMessagingTemplateService.triggerMessageTemplate(vo);
		}catch(Exception e) {
			logger.info(e.toString());
		}
		
		logger.info("**Out resultInsuredSendMail**");
		return  resultMsg;
	}


	/**
	 * 從壽險核心接收回案件狀  發送郵件給管理員-
	 * @param transNum    单号
	 * @param   from_company_id 类型
	 * @param status  状态
	 * @return
	 */
	private  String  resultManageSendMail(String  transNum, String  from_company_id, String status){
			IMessagingTemplateServiceImpl transContactInfoService = new IMessagingTemplateServiceImpl();
			Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			//paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			if("L01".equals(from_company_id)){
				paramMap.put("POLICY_TYPE", "首家件");
			}else{
			    paramMap.put("POLICY_TYPE", "轉收件");
			}
			paramMap.put("POLICY_NUMBER", transNum);
			
//			ParameterDao paramDao = new ParameterDao();
//			List<ParameterVo> listParam = new ArrayList<>();
//			listParam.addAll(paramDao.getParameterByCategoryCode("eservice", "ONLINE_CHANGE_STATUS"));
			if("2".equals(status)){
				paramMap.put("POLICY_STATUS", "成功");
			}else if("6".equals(status)){
				paramMap.put("POLICY_STATUS", "失敗");
			}else {
				//理論上，核心只會回傳2 OR 6
				paramMap.put("POLICY_STATUS", status);
			}
			//發送系統管理員
			List<String> receivers = new ArrayList<String>();
			receivers = (List)mailInfo.get("receivers");

			MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
			vo.setMessagingTemplateCode(CallApiMailCode.TRANSFER_MAIL_020);
			vo.setSendType("email");
			vo.setMessagingReceivers(receivers);
			vo.setParameters(paramMap);
			vo.setSystemId(CallApiMailCode.ESERVICE_BATCH);
			//进行发送通信
			IMessagingTemplateServiceImpl iMessagingTemplateService = new IMessagingTemplateServiceImpl();
			String resultMsg = iMessagingTemplateService.triggerMessageTemplate(vo);
			return  resultMsg;
	}
	
	private void updateTransStatus(String transNum, String transType, String status) {
		TransDao transDao = new TransDao();
		try {
			logger.info("Update trans[{}] status[{}] for {}", transNum, status, transType);

			TransVo updVo = new TransVo();
			updVo.setTransNum(transNum);
			updVo.setStatus(status);
			updVo.setUpdateDate(new Date());
			updVo.setUpdateUser(SYSTEM_ID);
			
			int result = transDao.updateTrans(updVo);
			if (result > 0) {
				logger.info("Update trans[{}] success", transNum);
			} else {
				logger.info("Update trans[{}] fail", transNum);
			}
		} catch (Exception e) {
			logger.error("updateTransStatus error:", e);
		}
	}
	
	/**
	 * 保單價值列印後續處理: 發送 pdf
	 * @throws Exception 
	 */
	private void processValuePrint(String transNum, String policyNo, String inputData, String twDate) throws Exception {
		String templateSource = rb.getString("local.file.upload.value-print");
		ReportExportUtil pdfUtil = new ReportExportUtil(templateSource);
		// 系統日
		pdfUtil.txt(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()), 12, 1, 450f, 676f);
		// 處理表格內容()
		List<String> listData = new ArrayList<String>();
		TransDao transDao = new TransDao();
		String[] arrayInputData = inputData.split("(?<=\\G.{12})");
		TransValuePrintDao transValuePrintDao = new TransValuePrintDao();
		TransPolicyVo transPolicyVo = new TransPolicyVo();
		transPolicyVo.setPolicyNo(policyNo);
		transPolicyVo.setTransNum(transNum);
		Map<String, Object> infoMap = transValuePrintDao.getTransValuePrintInfoData(transPolicyVo);
		if(infoMap == null || infoMap.isEmpty()) {
			logger.error("取得要保人或者保單資料異常");
			return ;
		}
		TransVo transVo = new TransVo();
		transVo.setTransNum(transNum);
		transVo = transDao.findById(transVo);
		
		// get from table LILIPM.LIPM_NAME_1
		listData.add(String.format("要保人 %s  君", MyStringUtil.nullToString(infoMap.get("LIPM_NAME_1"))));
		// get from table LILIPM.LIPM_INSU_BEG_DATE
		LocalDate begDate = ((Date)infoMap.get("LIPM_INSU_BEG_DATE")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String currency = MyStringUtil.nullToString(infoMap.get("CURRENCY"));
		String currencyCht = "NTD".equals(currency)? "新台幣": (("USD".equals(currency))?"美金": "");
		String moneyMain = arrayInputData[0].substring(0, 10).replaceAll(" ", "").trim();
		String productCodeMain = arrayInputData[0].substring(10, 12);
		String productNameMain = MyStringUtil.nullToString(transValuePrintDao.getProductNameByCode(productCodeMain));
		logger.debug("currency:{},currencyCht:{},moneyMain:{},productCodeMain:{},productNameMain:{}", currency, currencyCht, moneyMain, productCodeMain, productNameMain);
		String twYear = twDate.substring(0, 3);
		String twMonth = twDate.substring(3, 5);
		String twDay = twDate.substring(5, 7);
		listData.add(String.format("於%3d年%2d月%2d日投保本公司︰「%s」"
				, begDate.getYear() - 1911 // y-1911 顯示民國年
				, begDate.getMonthValue()
				, begDate.getDayOfMonth()
				, productNameMain));
		listData.add(String.format("（保單號碼%s），至%3d年%2d月%2d日止，保單價值準備金"
				, policyNo
				, Integer.parseInt(twYear)
				, Integer.parseInt(twMonth)
				, Integer.parseInt(twDay)));
		listData.add(String.format("為%s  %d元。"
				, currencyCht
				, Integer.parseInt(moneyMain)));
		
		String valuePrintFormatSub = "%s，保單價值準備金為 %s %d 元";
		for(int i = 1; i < arrayInputData.length; i++) {
			String moneySub = arrayInputData[i].substring(0, 10).replaceAll(" ", "").trim();
			String prodCodeSub = arrayInputData[i].substring(10, 12).replaceAll(" ", "").trim();
			logger.debug("moneySub:{},prodCodeSub:{}", moneySub, prodCodeSub);
			if (moneySub.length() > 0 && prodCodeSub.length() == 0) {
				//已抓到單行結尾
				break;
			}
			if (!"".equals(prodCodeSub) && prodCodeSub.length() > 0) {
				// 附約名稱: 使用後2碼對出來
				String prodNameSub = transValuePrintDao.getProductNameByCode(prodCodeSub);
				logger.debug("prodNameSub:{}", prodNameSub);
				
				listData.add(String.format(valuePrintFormatSub
						, prodNameSub.trim()
						, currencyCht
						, Integer.parseInt(moneySub)));
			}
		}
		pdfUtil.addToOneColumnTable(listData, 12, 100f, 655f, 15f);
		pdfUtil.closeDoc();
		pdfUtil.toPdf();
		
		TransValuePrintVo transValuePrintVo = new TransValuePrintVo();
		transValuePrintVo.setTransNum(transNum);
		List<TransValuePrintVo> transValuePrintList = transValuePrintDao.getTransValuePrintList(transValuePrintVo);
		if(transValuePrintList != null && transValuePrintList.size() > 0) {
			transValuePrintVo = transValuePrintList.get(0);
		}
		if(MyStringUtil.isNotNullOrEmpty(transValuePrintVo.getBeliverMail())) {
			ParameterDao parameterDao = new ParameterDao();
			// send email 
			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html lang=\"zh-Hant\"><head><meta charset=\"utf-8\"/></head><body>CONTENT</body></html>");
			String content = sb.toString();
			content = content.replace("CONTENT", MyStringUtil.nullToString(parameterDao.getParameterValueByCode(SYSTEM_ID, "VALUE_PRINT_MAIL_CONTENT")));
			content = content.replace("TRANS_NUM", transNum);
			String subject = parameterDao.getParameterValueByCode(SYSTEM_ID, "VALUE_PRINT_MAIL_SUBJECT");
	    	String mailTo = transValuePrintVo.getBeliverMail();
	    	subject = subject.replace("TRANS_NUM", transNum);
	    	String filename = "/temp/保單價值列印_" + transNum + ".pdf";
	    	logger.debug("filename:" + filename);
	    	logger.debug("subject:" + subject);
	    	logger.debug("content:" + content);
	    	logger.debug("mailTo:" + mailTo);
	    	java.io.File sendFile = new java.io.File(filename);
	    	FileUtils.writeByteArrayToFile(sendFile, pdfUtil.getPdfBytes());
	    	List<java.io.File> listFile = new ArrayList<>();
	    	listFile.add(sendFile);
	    	MailService mailService = new MailService();
	    	logger.info("send value_print pdf mail start...");
	    	mailService.sendMail(content, subject, Arrays.asList(mailTo), null, listFile);

	    	// 儲存郵件簡訊發送紀錄
			BatchApiService apiService = new BatchApiService();
			for (String addr : Arrays.asList(mailTo)) {
				apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, content));
			}
			
	    	logger.info("send value_print pdf mail end...");
	    	if (sendFile.delete()) {
	    		logger.info("delete file success");
	    	}
		}
	}
	
	/**
	 * 設定 transCode, transType 對應
	 * @return Map
	 */
	private Map<String, String> getTransTypeMapping(){
		Map<String, String> transTypeMap = new HashMap<>();
		// 繳別
		transTypeMap.put("001", "PAYMODE");
		// 年金給付方式
		transTypeMap.put("002", "ANNUITY_METHOD");
		// 紅利選擇權 
		transTypeMap.put("003", "BONUS");
		// 增值回饋金領取方式
		transTypeMap.put("004", "REWARD");
		// 自動墊繳選擇權
		transTypeMap.put("005", "CUSHION");
		// 受益人 (DS05)
		transTypeMap.put("006", "BENEFICIARY");
		// 展期定期保險
		transTypeMap.put("007", "RENEW");
		// 減額繳清保險
		transTypeMap.put("008", "REDUCE");
		// 減少保險金額 (主約：DS01, 附約：DS04)
		transTypeMap.put("009", "REDUCE_POLICY");
		// 聯絡資料變更 (DS01)
		transTypeMap.put("010", "CONTACT_INFO");
		// 保單價值列印 (DS48)
		transTypeMap.put("012", "VALUE_PRINT");
		// 投保證明列印 (DS51)
		transTypeMap.put("013", "CERTIFICATE_PRINT");
		// 補發保單 (DS01)
		transTypeMap.put("014", "POLICY_RESEND");
		// 信用卡有效年月 (CS17)
		transTypeMap.put("017", "CREDIT_CARD_DATE");
		// 終止授權 (CS17)
		transTypeMap.put("018", "CANCEL_AUTH");
		// 匯款帳號變更 (CS17)
		transTypeMap.put("023", "CHANGE_PAY_ACCOUNT");

		transTypeMap.put("031", "RISK_LEVEL");
		transTypeMap.put("033", "CHANGE_PREMIUM");
		transTypeMap.put("034", "PAYMODE");
		transTypeMap.put("032", "CASH_PAYMENT");
		transTypeMap.put("030", "CONVERSION");
		transTypeMap.put("029", "INVESTMENT");
		transTypeMap.put("028", "DEPOSIT");
		return transTypeMap;
	}
	
	private List<TransEndorsementVo> convertText(String transNum, String text, int order) {
		//20200103 不折行
//		String[] str = null;
//		if (text.length() > 30) {
//			str = new String[2];
//			str[0] = text.substring(0, 30);
//			str[1] = text.substring(30);
//		} else {
//			str = new String[]{text};
//		}
		String[] str = new String[]{text};
		List<TransEndorsementVo> list = new ArrayList<TransEndorsementVo>();
		for (String s : str) {
			TransEndorsementVo vo = new TransEndorsementVo();
			vo.setTextContent(s);
			vo.setTextOrder(order + "");
			vo.setTransNum(transNum);
			list.add(vo);
			order ++;
		}
		return list;
	}
	
	/**
	 * 儲存批註單內容
	 * @param pichuFileName
	 * @return
	 */
	private Map<String, List<TransEndorsementVo>> saveTransEndorsement(String pichuFileName) {
		Map<String, List<TransEndorsementVo>> endorsementMap = null;
		try {
			
			endorsementMap = new HashMap<String, List<TransEndorsementVo>>();
			File file = new File(pichuFileName);
			List<String> applyLines = FileUtils.readLines(file, "BIG5");
			List<TransEndorsementVo> textList = null;

			logger.info("\n");
			String transNum = "";
			logger.info("======================================================");
			int order = 1;
			int total = 1;
			for (String line : applyLines) {
				line = line.trim();
				logger.info("line: {}", line);
				if ("".equals(line)) {
					total ++;
					continue;
				}

				if (line.startsWith("##") && line.endsWith("##")) {
					if (textList != null) {
						endorsementMap.put(transNum, textList);
					}
					transNum = line.replaceAll("##", "").trim();
					textList = new ArrayList<TransEndorsementVo>();
					order = 1;
				} else {
					List<TransEndorsementVo> list = this.convertText(transNum, line, order);
					textList.addAll(list);
					order += list.size();
				}
				total ++;
				
				if (total == applyLines.size()) {
					endorsementMap.put(transNum, textList);
				}
			}
			logger.info("======================================================\n\n");
			
			TransEndorsementDao dao = new TransEndorsementDao();
			for (String key : endorsementMap.keySet()) {
				List<TransEndorsementVo> list = endorsementMap.get(key);
				//儲存批註單內容
				dao.insertTransEndorsement(list);
			}
		} catch (Exception e) {
			logger.error("saveTransEndorsement error:", e);
		}
		return endorsementMap;
	}
	
	/**
	 * 產生批註單實體檔案
	 * @param endorsementMap
	 */
	private List<String> genTransEndorsementPDF(Map<String, List<TransEndorsementVo>> endorsementMap) {
		List<String> fileNameList = new ArrayList<String>();
		try {
			OnlineChangeInfoDao dao = new OnlineChangeInfoDao();
			String sourcePath = rb.getString("local.file.download.path");
			String enviorment = rb.getString("running.enviorment");
			for (String key : endorsementMap.keySet()) {
				List<TransEndorsementVo> list = endorsementMap.get(key);
				
				Collections.sort(list, new Comparator<TransEndorsementVo>() {
				    @Override
				    public int compare(TransEndorsementVo v1, TransEndorsementVo v2) {
				    	return String.format("%02d", Integer.parseInt(v1.getTextOrder())).compareTo(String.format("%02d", Integer.parseInt(v2.getTextOrder())));
				    }
				});
				
				ReportExportUtil pdf = new ReportExportUtil("endorsement.pdf");
				int offset = 15;
				int defaultX = 500;//list.size() < 8 ? 380 : 340;
				for (int i=0; i<list.size(); i++) {
					TransEndorsementVo vo1 = list.get(i);
					pdf.txt(vo1.getTextContent(), 12, 1, 105, defaultX - (offset * (i + 1)));
				}
				
				String policyHolderName = new TransEndorsementDao().getPolicyHolderName(key);
				pdf.txt(policyHolderName, 12, 1, 123, 696);
				
				pdf.toPdf();
				
				File file = new File(sourcePath + "/endorsement_" + key + ".pdf");
				fileNameList.add("/endorsement_" + key + ".pdf");
				FileOutputStream fos = new FileOutputStream(file);
				IOUtils.write(pdf.getPdfBytes(), fos);
				IOUtils.closeQuietly(fos);
				
				if (!"dev".equalsIgnoreCase(enviorment)) {
					logger.info("============================================================================");
					//上傳批註單到影像系統
					OnlineChangeInfoVo infoVo = dao.getOnlineChangeInfoByTransNum(key);
					BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
					String token = batchUploadEZService.getEZToken();
					boolean isSucc = batchUploadEZService.uploadFile("D" + key, "PDF", token, file, infoVo);
					
					if (isSucc) {
						logger.info("上傳批註單到影像系統完成");
					} else {
						logger.info("上傳批註單到影像系統失敗, transNum: " + key);
					}
				}
				
			}
		} catch (Exception e) {
			logger.error("genTransEndorsementPDF error:", e);
		}
		return fileNameList;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		try {
			
			String line = "010201902120004TE100000621080212T0000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999aaaaaaaaaa";
			String transCode = line.substring(0, 3);
			String transNum = line.substring(3, 15);
			String applyResult = line.substring(32, 33);
			String remark = line.substring(34, 133);
			System.out.println(remark);
//			BatchDownloadService service = new BatchDownloadService();
//			service.processValuePrint("201811150005", "CA12003335", "  88455000EE       2941R                                    成功                                                                                                                                                                                                                                                                        ", "1080212");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
