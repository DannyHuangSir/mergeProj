package com.twfhclife.eservice_batch.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.BusinessEventDao;
import com.twfhclife.eservice_batch.dao.BusinessEventJobDao;
import com.twfhclife.eservice_batch.dao.UserDao;
import com.twfhclife.eservice_batch.model.BusinessEventJobVo;
import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.MessageRecordVo;
import com.twfhclife.eservice_batch.model.UserVo;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.SmsService;

/**
 * Event Job 
 * 事件通知
 * @author Ken Wei
 *
 */
public class BatchBusinessEventJobService {
	
	private static final Logger logger = LogManager.getLogger(BatchBusinessEventJobService.class);
	
	private List<BusinessEventJobVo> bussinessEvenJobtList ;
	
	private Integer eventJobId;
	
	private BusinessEventJobDao businessEventJobDao;
	
	public BatchBusinessEventJobService() {
		this.businessEventJobDao = new BusinessEventJobDao();
		this.bussinessEvenJobtList = new ArrayList<>();
	}

	/** 
	 * main process 
	 * <p>
	 * <ol>
	 * <li>取得 EVENT JOB</li> 
	 * <li>取出 EVENT 發生但尚未處理資料</li>
	 * <li>使用通信模板建立內容送出</li>
	 * <li>更新事件狀態 NOTICE_STATUS</li>
	 * </p>
	 * @param inputJobId
	 */
	public void execute(String inputJobId) {
		if(inputJobId.matches("[0-9]+")) {
			this.eventJobId = Integer.parseInt(inputJobId);
			try {
				// 1. 取得 EVENT JOB
				this.queryEventJob();
				
				// 2. 取出 EVENT 發生但尚未處理資料
				this.bussinessEvenJobtList.stream().parallel().forEach(vo -> {
					List<BusinessEventMsgTmpVo> listEventData = this.queryUnProcessEventData(vo);
					listEventData.stream().forEach(data -> {
				// 3. 使用通信模板建立內容送出
						if(this.send(data)) {
				// 4. 更新事件狀態  1: SUCC;2: FAIL
							this.updateNoticeStatus(data.getBusinessEventId(), 1);
						} else {
							this.updateNoticeStatus(data.getBusinessEventId(), 2);
						}
					});
				});
			} catch(Exception e) {
				logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
			} finally {
				logger.info("BatchBusinessEventJobService.execute() End.");
			}
		} else {
			logger.info("輸入 Event_Job_Id 有誤，應為全數字");
		}
	}
	
	/**
	 * 取出當前 event 
	 * @return
	 * @throws RuntimeException
	 */
	private void queryEventJob() throws RuntimeException{
		BusinessEventJobVo vo = new BusinessEventJobVo();
		vo.setEventJobId(this.eventJobId);
		Optional<List<BusinessEventJobVo>> optional = businessEventJobDao.queryBusinessEvenJobtList(vo);
		if(optional.isPresent()) {
			this.bussinessEvenJobtList.addAll(optional.get());
		} else {
			throw new RuntimeException("query BusinessEventJob error, result is null");
		}
	}
	
	/**
	 * 取出當前 event 需要處理的事件
	 * @param vo
	 * @return
	 * @throws RuntimeException
	 */
	private List<BusinessEventMsgTmpVo> queryUnProcessEventData(BusinessEventJobVo vo) throws RuntimeException{
		Optional<List<BusinessEventMsgTmpVo>> optional = businessEventJobDao.queryUnTriggerData(vo);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			throw new RuntimeException("query UnProcessEvent error, result is null");
		}
	}
	
	/**
	 * 更新 BUSINESS_EVENT.NOTICE_STATUS
	 * @param businessEventId
	 * @throws RuntimeException
	 */
	private void updateNoticeStatus(Integer businessEventId, Integer status) throws RuntimeException{
		boolean result = businessEventJobDao.updateNoticeStatus(businessEventId, status);
		if(!result) {
			throw new RuntimeException("update NoticeStatus error, update fail");
		}
	}
	
	/**
	 * 發出 message: sms/email
	 * @param userVo
	 * @param eventMsgVo
	 * @return
	 * @throws RuntimeException
	 */
	private boolean send(BusinessEventMsgTmpVo eventMsgVo){
		try {
			MessageRecordVo messageRecordVo = new MessageRecordVo();
			messageRecordVo.setSendType(eventMsgVo.getSendType());
			messageRecordVo.setSystemId(eventMsgVo.getTargetSystemId());
			messageRecordVo.setSendSubject(eventMsgVo.getMessagingSubject());
			messageRecordVo.setCreateUser("eservice_batch");
			messageRecordVo.setSendContent(eventMsgVo.getMessagingContent());
			// 處理收件者資訊
			List<String> receiverList = null;
			UserVo userVo = null;
			if ("dynamic".equals(eventMsgVo.getReceiverMode())) {
				// 動態指定收件者(寄送給Event的createUser)
				String username = eventMsgVo.getCreateUser();
				// 以username撈出user的email或mobile
				UserDao userdao = new UserDao();
				userVo = userdao.getContactByUsername("elife", username);
				receiverList = new ArrayList<>();
				if ("email".equals(eventMsgVo.getSendType())) {
					receiverList.add(userVo.getEmail());
					messageRecordVo.setEmail(userVo.getEmail());
				} else if ("sms".equals(eventMsgVo.getSendType())) {
					receiverList.add(userVo.getMobile());
					messageRecordVo.setMobile(userVo.getMobile());
				}
			} else {
				// 撈設定的收件者
				String receivers = eventMsgVo.getReceivers();
				receiverList = Arrays.asList(receivers.split("\\,"));
			}
			if("email".equals(eventMsgVo.getSendType())){
				// 確認user有登錄email才送email
				if (MyStringUtil.isNotNullOrEmpty(userVo.getEmail())) {
					MailService MailService = new MailService();
					MailService.sendMail(eventMsgVo.getMessagingContent(), eventMsgVo.getMessagingSubject(), receiverList,
							null, null);
					// 儲存郵件簡訊發送紀錄
					BatchApiService apiService = new BatchApiService();
					for (String addr : receiverList) {
						apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, eventMsgVo.getMessagingContent()));
					}
				}
			} else if("sms".equals(eventMsgVo.getSendType())){
				// 確認user有登錄mobile才送sms
				if (MyStringUtil.isNotNullOrEmpty(userVo.getMobile())) {
					SmsService SmsService = new SmsService();
					SmsService.sendSms(receiverList.get(0), eventMsgVo.getMessagingContent());
					// 儲存郵件簡訊發送紀錄
					BatchApiService apiService = new BatchApiService();
					apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "sms", receiverList.get(0), eventMsgVo.getMessagingContent()));
				}
			}
			messageRecordVo.setSendDate(LocalDateTime.now().toString());
			BusinessEventDao businessEventDao = new BusinessEventDao();
			businessEventDao.insertMessageRecord(messageRecordVo);
			
			return true;
		} catch(Exception e) {
			logger.error("send message error: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
}
