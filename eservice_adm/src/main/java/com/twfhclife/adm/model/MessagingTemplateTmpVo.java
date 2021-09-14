package com.twfhclife.adm.model;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessagingTemplateTmpVo {
	
	/** ID */
	private BigDecimal messagingTemplateId;
	/** 系統別 */
	private String systemId;
	/** 通信模版代碼 */
	private String messagingTemplateCode;
	/** 通信模版名稱 */
	private String messagingTemplateName;
	/** 通信模版狀態 */
	private String status;
	/** 通信模版狀態-觸發類型 */
	private String triggerType;
	/** 通信模版狀態-事件類型 */
	private String eventType;
	/** 通信模版狀態-事件類型-寄送類型 */
	private String sendType;
	/** 通信模版狀態-事件類型-寄送時間 */
	private String sendTime;
	/** 通信模版狀態-事件類型-週期類型 */
	private String circleType;
	/** 通信模版狀態-事件類型-週期值 */
	private String circleValue;
	/** 通信模版狀態-事件類型-收件者類型 */
	private String receiverMode;
	/** 通信模版狀態-事件類型-通信主旨 */
	private String messagingSubject;
	/** 通信模版狀態-事件類型-通信內容 */
	private String messagingContent;
	/** 通信模版狀態-事件類型-創建日期 */
	private Date createDate;
	/** 通信模版狀態-事件類型-創建人員 */
	private String createUser;
	/** 通信模版狀態-事件類型-修改日期 */
	private Date updateDate;
	/** 通信模版狀態-事件類型-修改人員 */
	private String updateUser;

	// 自定義
	/** 通信模版狀態名稱 */
	private String statusName;
	/** 系統別名稱 */
	private String sysName;
	/** 模版作業類型 新增 修改 停用 */
	private String type;
	/** 事件名稱 */
	private String eventTypeName;
	/** 通信模版狀態-引用變數(可多筆) */
	private String[] parameters;
	private List<MessagingParameterTmpVo> messagingParameterTmpList;
	/** 通信模版狀態-參數(收件人資訊)*/
	private String[] messagingReceivers;
	private List<MessagingReceiverTmpVo> messagingReceiverTmpList;
	
	public BigDecimal getMessagingTemplateId() {
		return this.messagingTemplateId;
	}
	
	public void setMessagingTemplateId(BigDecimal messagingTemplateId) {
		this.messagingTemplateId = messagingTemplateId;
	}
	
	public String getSystemId() {
		return this.systemId;
	}
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public String getMessagingTemplateCode() {
		return this.messagingTemplateCode;
	}
	
	public void setMessagingTemplateCode(String messagingTemplateCode) {
		this.messagingTemplateCode = messagingTemplateCode;
	}
	
	public String getMessagingTemplateName() {
		return this.messagingTemplateName;
	}
	
	public void setMessagingTemplateName(String messagingTemplateName) {
		this.messagingTemplateName = messagingTemplateName;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTriggerType() {
		return this.triggerType;
	}
	
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	
	public String getEventType() {
		return this.eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getSendType() {
		return this.sendType;
	}
	
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public String getSendTime() {
		return this.sendTime;
	}
	
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getCircleType() {
		return this.circleType;
	}
	
	public void setCircleType(String circleType) {
		this.circleType = circleType;
	}
	
	public String getCircleValue() {
		return this.circleValue;
	}
	
	public void setCircleValue(String circleValue) {
		this.circleValue = circleValue;
	}
	
	public String getReceiverMode() {
		return this.receiverMode;
	}
	
	public void setReceiverMode(String receiverMode) {
		this.receiverMode = receiverMode;
	}
	
	public String getMessagingSubject() {
		return this.messagingSubject;
	}
	
	public void setMessagingSubject(String messagingSubject) {
		this.messagingSubject = messagingSubject;
	}
	
	public String getMessagingContent() {
		return this.messagingContent;
	}
	
	public void setMessagingContent(String messagingContent) {
		this.messagingContent = messagingContent;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getUpdateUser() {
		return this.updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	public String[] getMessagingReceivers() {
		return messagingReceivers;
	}

	public void setMessagingReceivers(String[] messagingReceivers) {
		this.messagingReceivers = messagingReceivers;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	
	public List<MessagingParameterTmpVo> getMessagingParameterTmpList() {
		return messagingParameterTmpList;
	}

	public void setMessagingParameterTmpList(List<MessagingParameterTmpVo> messagingParameterTmpList) {
		this.messagingParameterTmpList = messagingParameterTmpList;
	}

	public List<MessagingReceiverTmpVo> getMessagingReceiverTmpList() {
		return messagingReceiverTmpList;
	}

	public void setMessagingReceiverTmpList(List<MessagingReceiverTmpVo> messagingReceiverTmpList) {
		this.messagingReceiverTmpList = messagingReceiverTmpList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

