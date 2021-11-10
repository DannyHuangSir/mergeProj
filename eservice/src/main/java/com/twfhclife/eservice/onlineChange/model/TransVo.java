package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.eservice.web.model.BankVo;
import com.twfhclife.eservice.web.model.PageInfoVo;

public class TransVo extends PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 保單號碼-List */
	private List<TransPolicyVo> policys;

	/** 保單名稱 */
	private String productName;

	/** 申請時間 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date transDate;

	/** 申請序號 */
	private String transNum;

	/** 申請項目類別代碼 */
	private String transType;

	/** 申請項目類別名稱 */
	private String transTypeStr;

	/** user ID */
	private String userId;

	/** user 姓名 */
	private String userName;

	/** 申請狀態代碼 */
	private String status;

	/** 申請狀態名稱 */
	private String statusStr;

	/** 各申請項目id */
	private Integer id;

	/** 預借金額 */
	private BigDecimal debitAmount;

	/** 基本資料變更-變更後手機號碼 */
	private String mobile;

	/** 基本資料變更-變更後電子郵件 */
	private String email;

	/** 基本資料變更-綁定FB ID */
//	private String fbId;

	/** 基本資料變更-綁定自然人憑證ID */
//	private String cardSn;
	
	/** 基本資料變更-變更後簡訊寄送 */
	private String smsFlag;

	/** 基本資料變更-變更後郵件寄送 */
	private String mailFlag;

	/** 基本資料變更-原手機號碼 */
	private String mobileOld;

	/** 基本資料變更-原電子郵件 */
	private String emailOld;

	/** 基本資料變更-原簡訊寄送 */
	private String smsFlagOld;

	/** 基本資料變更-原郵件寄送 */
	private String mailFlagOld;
	//保单类型
	private String fromCompanyId;


	/** 銀行資訊 */
	private BankVo bank;

	/** 旅遊平安險 */
	private TravelPolicyVo travelPolicy;
	
	private int endorsementStatus;
	//是否開始已經推送至聯盟
	private String sendAlliance;

	public String getSendAlliance() {
		return sendAlliance;
	}

	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}

	@Override
	public String toString() {
		return "TransVo{" +
				"policys=" + policys +
				", productName='" + productName + '\'' +
				", transDate=" + transDate +
				", transNum='" + transNum + '\'' +
				", transType='" + transType + '\'' +
				", transTypeStr='" + transTypeStr + '\'' +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", status='" + status + '\'' +
				", statusStr='" + statusStr + '\'' +
				", id=" + id +
				", debitAmount=" + debitAmount +
				", mobile='" + mobile + '\'' +
				", email='" + email + '\'' +
				", smsFlag='" + smsFlag + '\'' +
				", mailFlag='" + mailFlag + '\'' +
				", mobileOld='" + mobileOld + '\'' +
				", emailOld='" + emailOld + '\'' +
				", smsFlagOld='" + smsFlagOld + '\'' +
				", mailFlagOld='" + mailFlagOld + '\'' +
				", fromCompanyId='" + fromCompanyId + '\'' +
				", bank=" + bank +
				", travelPolicy=" + travelPolicy +
				", endorsementStatus=" + endorsementStatus +
				'}';
	}

	public String getFromCompanyId() {
		return fromCompanyId;
	}

	public void setFromCompanyId(String fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}

	public List<TransPolicyVo> getPolicys() {
		return policys;
	}

	public void setPolicys(List<TransPolicyVo> policys) {
		this.policys = policys;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTypeStr() {
		return transTypeStr;
	}

	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}

	public String getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(String mailFlag) {
		this.mailFlag = mailFlag;
	}

	public String getMobileOld() {
		return mobileOld;
	}

	public void setMobileOld(String mobileOld) {
		this.mobileOld = mobileOld;
	}

	public String getEmailOld() {
		return emailOld;
	}

	public void setEmailOld(String emailOld) {
		this.emailOld = emailOld;
	}

	public String getSmsFlagOld() {
		return smsFlagOld;
	}

	public void setSmsFlagOld(String smsFlagOld) {
		this.smsFlagOld = smsFlagOld;
	}

	public String getMailFlagOld() {
		return mailFlagOld;
	}

	public void setMailFlagOld(String mailFlagOld) {
		this.mailFlagOld = mailFlagOld;
	}

	public BankVo getBank() {
		return bank;
	}

	public void setBank(BankVo bank) {
		this.bank = bank;
	}

	public TravelPolicyVo getTravelPolicy() {
		return travelPolicy;
	}

	public void setTravelPolicy(TravelPolicyVo travelPolicy) {
		this.travelPolicy = travelPolicy;
	}

	public int getEndorsementStatus() {
		return endorsementStatus;
	}

	public void setEndorsementStatus(int endorsementStatus) {
		this.endorsementStatus = endorsementStatus;
	}
}
