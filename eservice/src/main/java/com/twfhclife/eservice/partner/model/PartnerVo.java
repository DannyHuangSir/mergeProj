package com.twfhclife.eservice.partner.model;

import java.io.Serializable;
import java.util.Date;

import com.twfhclife.eservice.web.model.PageInfoVo;

public class PartnerVo extends PageInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 要保人 */
	private String proposer;
	
	/** 身分證字號 */
	private String rocId;

	/** 保單號碼 */
	private String policyNo;
	
	/** 保單類型(1:投資, 2:保障) */
	private String policyListType;
	
	/** 被保人姓名 */
	private String insuredName;
	
	/** 被保人姓名 */
	private String insuredNameBase64;

	/** 生效日 */
	private Date effectiveDate;

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyListType() {
		return policyListType;
	}

	public void setPolicyListType(String policyListType) {
		this.policyListType = policyListType;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredNameBase64() {
		return insuredNameBase64;
	}

	public void setInsuredNameBase64(String insuredNameBase64) {
		this.insuredNameBase64 = insuredNameBase64;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}