package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LitracEsVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 帳號型別 */
	private String tracRecoCode;
	/** 保單號碼 */
	private String tracInsuNo;
	/** 序號 */
	private String tracSeq;
	/** 銀行別 */
	private String tracBankCode;
	/** 分行別 */
	private String tracBranchCode;
	/** 收費帳號 */
	private String tracCharAt;
	/** 收費帳號身分證號 */
	private String tracCharId;
	/** 戶名 */
	private String tracName;
	/** 轉帳金額 */
	private String tracTrAmt;
	/** 卡號 */
	private String tracCreditCardNo;
	/** 到期日 */
	private String tracCdDueDate;
	
	public String getTracRecoCode() {
		return this.tracRecoCode;
	}
	
	public void setTracRecoCode(String tracRecoCode) {
		this.tracRecoCode = tracRecoCode;
	}
	
	public String getTracInsuNo() {
		return this.tracInsuNo;
	}
	
	public void setTracInsuNo(String tracInsuNo) {
		this.tracInsuNo = tracInsuNo;
	}
	
	public String getTracSeq() {
		return this.tracSeq;
	}
	
	public void setTracSeq(String tracSeq) {
		this.tracSeq = tracSeq;
	}
	
	public String getTracBankCode() {
		return this.tracBankCode;
	}
	
	public void setTracBankCode(String tracBankCode) {
		this.tracBankCode = tracBankCode;
	}
	
	public String getTracBranchCode() {
		return this.tracBranchCode;
	}
	
	public void setTracBranchCode(String tracBranchCode) {
		this.tracBranchCode = tracBranchCode;
	}
	
	public String getTracCharAt() {
		return this.tracCharAt;
	}
	
	public void setTracCharAt(String tracCharAt) {
		this.tracCharAt = tracCharAt;
	}
	
	public String getTracCharId() {
		return this.tracCharId;
	}
	
	public void setTracCharId(String tracCharId) {
		this.tracCharId = tracCharId;
	}
	
	public String getTracName() {
		return this.tracName;
	}
	
	public void setTracName(String tracName) {
		this.tracName = tracName;
	}
	
	public String getTracTrAmt() {
		return this.tracTrAmt;
	}
	
	public void setTracTrAmt(String tracTrAmt) {
		this.tracTrAmt = tracTrAmt;
	}
	
	public String getTracCreditCardNo() {
		return this.tracCreditCardNo;
	}
	
	public void setTracCreditCardNo(String tracCreditCardNo) {
		this.tracCreditCardNo = tracCreditCardNo;
	}
	
	public String getTracCdDueDate() {
		return this.tracCdDueDate;
	}
	
	public void setTracCdDueDate(String tracCdDueDate) {
		this.tracCdDueDate = tracCdDueDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}