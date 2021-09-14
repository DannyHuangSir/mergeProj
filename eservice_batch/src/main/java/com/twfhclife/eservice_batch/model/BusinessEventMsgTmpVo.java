package com.twfhclife.eservice_batch.model;

import java.util.Date;
import java.util.List;

public class BusinessEventMsgTmpVo {

	private Integer businessEventId;
	private String userId;
	private Date eventDate;
	private String sourceIp;
	private String targetIp;
	private String targetSystemId;
	private String eventName;
	private String eventCode;
	private String eventStatus;
	private String eventMsg;
	private Date createDate;
	private String createUser;
	private Integer noticeStatus;

	private Integer messagingTemplateId;
	private String messagingTemplateCode;
	private String messagingTemplateName;
	/**
	 * api,event
	 */	
	private String triggerType;// api,event
	
	private String sendType;// sms,email
	private String sendTime;// 排程時間
	private String circleType;
	private String circleValue;
	/**
	 * customize,dynamic
	 */
	private String receiverMode;// 收件者指定方式(customize,dynamic)
	private String messagingSubject;// 主旨
	private String messagingContent;// 內容
	private String params;//模板參數(逗點分隔)
	private String receivers;//收件者(email/mobile逗點分隔)

	private List<EventConditionVo> eventConditions;
	private List<EventParameterVo> eventParameters;

	// 開始日期
	private String startDate;
	// 結束日期
	private String endDate;

	public Integer getBusinessEventId() {
		return businessEventId;
	}

	public void setBusinessEventId(Integer businessEventId) {
		this.businessEventId = businessEventId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getTargetSystemId() {
		return targetSystemId;
	}

	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventMsg() {
		return eventMsg;
	}

	public void setEventMsg(String eventMsg) {
		this.eventMsg = eventMsg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<EventConditionVo> getEventConditions() {
		return eventConditions;
	}

	public void setEventConditions(List<EventConditionVo> eventConditions) {
		this.eventConditions = eventConditions;
	}

	public List<EventParameterVo> getEventParameters() {
		return eventParameters;
	}

	public void setEventParameters(List<EventParameterVo> eventParameters) {
		this.eventParameters = eventParameters;
	}

	public Integer getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(Integer noticeStatus) {
		this.noticeStatus = noticeStatus;
	}

	public Integer getMessagingTemplateId() {
		return messagingTemplateId;
	}

	public void setMessagingTemplateId(Integer messagingTemplateId) {
		this.messagingTemplateId = messagingTemplateId;
	}

	public String getMessagingTemplateCode() {
		return messagingTemplateCode;
	}

	public void setMessagingTemplateCode(String messagingTemplateCode) {
		this.messagingTemplateCode = messagingTemplateCode;
	}

	public String getMessagingTemplateName() {
		return messagingTemplateName;
	}

	public void setMessagingTemplateName(String messagingTemplateName) {
		this.messagingTemplateName = messagingTemplateName;
	}

	/**
	 * api,event
	 * @return
	 */
	public String getTriggerType() {
		return triggerType;
	}
	/**
	 * api,event
	 * @param triggerType
	 */
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	/**
	 * sms,email
	 * @return
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * sms,email
	 * @param sendType
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getCircleType() {
		return circleType;
	}

	public void setCircleType(String circleType) {
		this.circleType = circleType;
	}

	public String getCircleValue() {
		return circleValue;
	}

	public void setCircleValue(String circleValue) {
		this.circleValue = circleValue;
	}

	/**
	 * customize,dynamic
	 * @return
	 */
	public String getReceiverMode() {
		return receiverMode;
	}
	/**
	 * customize,dynamic
	 * @param receiverMode
	 */
	public void setReceiverMode(String receiverMode) {
		this.receiverMode = receiverMode;
	}

	public String getMessagingSubject() {
		return messagingSubject;
	}

	public void setMessagingSubject(String messagingSubject) {
		this.messagingSubject = messagingSubject;
	}

	public String getMessagingContent() {
		return messagingContent;
	}

	public void setMessagingContent(String messagingContent) {
		this.messagingContent = messagingContent;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

}
