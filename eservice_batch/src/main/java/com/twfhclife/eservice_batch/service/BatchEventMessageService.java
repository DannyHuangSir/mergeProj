package com.twfhclife.eservice_batch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.BusinessEventDao;
import com.twfhclife.eservice_batch.dao.UserDao;
import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.MessageRecordVo;
import com.twfhclife.eservice_batch.model.UserVo;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.SmsService;

public class BatchEventMessageService {

	private static final Logger logger = LogManager.getLogger(BatchEventMessageService.class);

	private List<BusinessEventMsgTmpVo> unhandledEventList;

	// 主流程
	public void process() {
		logger.debug("### Batch BatchEventMessageService start...");

		this.getUnhandledEvent();

		if (unhandledEventList != null && unhandledEventList.size() > 0) {
			logger.info(unhandledEventList.size() + " unhandled events found.");
			// send message
			this.sendMessage();

		} else {
			logger.info("No event found.");
		}
		logger.info("End of BatchEventMessageService.");
	}

	// 查詢事件記錄
	private void getUnhandledEvent() {
		try {
			BusinessEventDao businessEventDao = new BusinessEventDao();
			unhandledEventList = businessEventDao.getUnhandledEvent();
		} catch (Exception e) {
			logger.error("getUnhandledEvent error - " + e.getMessage(), e);
		}
	}

	// 處理發送通知
	private void sendMessage() {
		if (unhandledEventList != null) {
			BusinessEventDao businessEventDao = new BusinessEventDao();
			for (BusinessEventMsgTmpVo vo : unhandledEventList) {
				try {
					logger.debug("EventName=" + vo.getEventName() + ", MessagingTemplateName=" + vo.getMessagingTemplateName());
					if (MyStringUtil.isNotNullOrEmpty(vo.getMessagingTemplateCode())) {
						// 有建立通信模板，依照通信模板設定進行通知
						String sendType = vo.getSendType();// sms,email
						String contents = vo.getMessagingContent();
						
						MessageRecordVo messageRecordVo = new MessageRecordVo();
						messageRecordVo.setSendType(sendType);
						messageRecordVo.setSystemId(vo.getTargetSystemId());
						messageRecordVo.setSendSubject(vo.getMessagingSubject());
						messageRecordVo.setCreateUser("eservice_batch");

						// 處理模板參數
						if (vo.getParams() != null && vo.getParams().split("\\,").length > 0) {
							// TODO
							String[] paramsArray = vo.getParams().split("\\,");
							for (String param : paramsArray) {
								if (param.equals("CustomerName")) {
									contents = contents.replace("${CustomerName}", "保戶");
								}
							}
						}
						messageRecordVo.setSendContent(contents);

						// 處理收件者資訊
						UserVo userVo = null;
						List<String> receiverList = null;
						if ("dynamic".equals(vo.getReceiverMode())) {
							// 動態指定收件者(寄送給Event的createUser)
							String username = vo.getCreateUser();
							// 以username撈出user的email或mobile
							UserDao userdao = new UserDao();
							userVo = userdao.getContactByUsername("elife", username);
							receiverList = new ArrayList<>();
							if (sendType.equals("email")) {
								receiverList.add(userVo.getEmail());
								messageRecordVo.setEmail(userVo.getEmail());
							} else if (sendType.equals("sms")) {
								receiverList.add(userVo.getMobile());
								messageRecordVo.setMobile(userVo.getMobile());
							}
						} else {
							// 撈設定的收件者
							String receivers = vo.getReceivers();
							receiverList = Arrays.asList(receivers.split("\\,"));
						}

						// 發送訊息
						if (sendType.equals("email")) {
							// 確認user有登錄email才送email
							if (MyStringUtil.isNotNullOrEmpty(userVo.getEmail())) {
								MailService MailService = new MailService();
								MailService.sendMail(contents, vo.getMessagingSubject(), receiverList,
										null, null);
								// 儲存郵件簡訊發送紀錄
								BatchApiService apiService = new BatchApiService();
								for (String addr : receiverList) {
									apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, contents));
								}
							}

						} else if (sendType.equals("sms")) {
							// 確認user有登錄mobile才送sms
							if (MyStringUtil.isNotNullOrEmpty(userVo.getMobile())) {
								SmsService SmsService = new SmsService();
								SmsService.sendSms(receiverList.get(0), contents);
								// 儲存郵件簡訊發送紀錄
								BatchApiService apiService = new BatchApiService();
								apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "sms", receiverList.get(0), contents));
							}
						}
						businessEventDao.updateEventNoticeStatus(vo.getBusinessEventId(), 1);

						// add MESSAGING_RECORDS
						messageRecordVo.setSendDate(new Date().toString());
						businessEventDao.insertMessageRecord(messageRecordVo);
					} else {
						// 未建立通信模板，不發送通知，直接註記NoticeStatus=1
						businessEventDao.updateEventNoticeStatus(vo.getBusinessEventId(), 1);
					}
				} catch (Exception e) {
					logger.error("Handle business-event error (BusinessEventId="+ vo.getBusinessEventId()+"), please check log for reason.", e);
					businessEventDao.updateEventNoticeStatus(vo.getBusinessEventId(), 2);
				}
			}
		}

	}
	
	
	// 查詢事件記錄
		private void insertMessageRecord(MessageRecordVo messageRecordVo) {
			try {
				BusinessEventDao businessEventDao = new BusinessEventDao();
				businessEventDao.insertMessageRecord(messageRecordVo);
			} catch (Exception e) {
				logger.error("getUnhandledEvent error - " + e.getMessage(), e);
			}
		}

	public static void main(String[] args) {

	}
}
