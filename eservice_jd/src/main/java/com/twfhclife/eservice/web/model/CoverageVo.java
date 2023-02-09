package com.twfhclife.eservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CoverageVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private String coverageId;
	/**  */
	private String policyNo;
	/**  */
	private String productId;
	/**  */
	private String individualId;
	/**  */
	private String insuredName;
	/**  */
	private String status;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date effectiveDate;
	/**  */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date expireDate;
	/**  */
	private BigDecimal mainAmount;
	/**  */
	private BigDecimal premiumAmount;

	private ProductVo productVo;

	private PolicyVo policyVo;

	private IndividualVo individualVo;

	private List<BenefitVo> benefitList;

	private String insuredNameBase64;

	private String ltStatus;

	private String isWholeLife = "N";

	public String getInsuredNameBase64() {
		return insuredNameBase64;
	}

	public void setInsuredNameBase64(String insuredNameBase64) {
		this.insuredNameBase64 = insuredNameBase64;
	}

	public String getCoverageId() {
		return this.coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}

	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getIndividualId() {
		return this.individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getInsuredName() {
		return this.insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getMainAmount() {
		return this.mainAmount;
	}

	public void setMainAmount(BigDecimal mainAmount) {
		this.mainAmount = mainAmount;
	}

	public BigDecimal getPremiumAmount() {
		return this.premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public ProductVo getProductVo() {
		return productVo;
	}

	public void setProductVo(ProductVo productVo) {
		this.productVo = productVo;
	}

	public PolicyVo getPolicyVo() {
		return policyVo;
	}

	public void setPolicyVo(PolicyVo policyVo) {
		this.policyVo = policyVo;
	}

	public IndividualVo getIndividualVo() {
		return individualVo;
	}

	public void setIndividualVo(IndividualVo individualVo) {
		this.individualVo = individualVo;
	}

	public List<BenefitVo> getBenefitList() {
		return benefitList;
	}

	public void setBenefitList(List<BenefitVo> benefitList) {
		this.benefitList = benefitList;
	}

	public String getLtStatus() {
		return ltStatus;
	}

	public void setLtStatus(String ltStatus) {
		this.ltStatus = ltStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getIsWholeLife() {
		return isWholeLife;
	}

	public void setIsWholeLife(String isWholeLife) {
		this.isWholeLife = isWholeLife;
	}
}