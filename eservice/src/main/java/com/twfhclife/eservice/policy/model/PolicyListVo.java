package com.twfhclife.eservice.policy.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.eservice.onlineChange.model.LitracEsVo;

public class PolicyListVo extends PolicyVo {

	private static final long serialVersionUID = 1L;

	/** 要保人名稱 */
	private String customerName;
	
	/** 約定扣款日 */
	private String drawDay;

	/** 每期保費 */
	private BigDecimal paidAmount;
	
	/** 平均參考報酬率(%) */
	private BigDecimal avgRoiRate;
	
	/** 保單價值 */
	private BigDecimal policyAcctValue;

	/** 主約被保人 */
	private String mainInsuredName;

	/** 保單商品名稱 */
	private String productName;

	/** 保單超過失效日期 */
	private String expiredFlag;

	/** 保單鎖定註記 Flag */
	private String applyLockedFlag;
	
	private String isLoginerFlag;
	
	/** 保單鎖定訊息 */
	private String applyLockedMsg;
	
	/** 滿期生存匯款資訊 */
	private List<LitracEsVo> litracEsList;
	
	/** 付款資訊 */
	private PolicyPayerVo policyPayerVo;
	
	/** 貸款資訊 */
	private PolicyExtraVo policyExtraVo;
	
	/** 受益類型 */
	private String beneficiaryName;
	
	/** svg檔 base64 string **/
	private String mainInsuredNameBase64;
	private String customerNameBase64;

	/** 要被保人同一人 1:同一人, 0:不同人 **/
	private int sameIdCount;
	
	private String canRenew = "N";
	
	private String canReduce = "N";
	
	private String prodIsFatca;
	
	private String prodType;

	private String lipiRocId;

	/** 20211220 by 203990 保單是否擁有電子保單 */
	private String pmdaEpoMk;
	
	private String pmdaEInfoNStatus;

	private String policyListType ;
	
	/** 20230712 by 203990 保單NTD價值 */
	private BigDecimal policyAcctValueNtd;

	public String getPmdaEInfoNStatus() {
		return pmdaEInfoNStatus;
	}

	public void setPmdaEInfoNStatus(String pmdaEInfoNStatus) {
		this.pmdaEInfoNStatus = pmdaEInfoNStatus;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd hh:mm:ss")
	private Date beginDate;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}


	public String getPmdaEpoMk() {
		return pmdaEpoMk;
	}

	public void setPmdaEpoMk(String pmdaEpoMk) {
		this.pmdaEpoMk = pmdaEpoMk;
	}

	public BigDecimal getPolicyAcctValueNtd() {
		return policyAcctValueNtd;
	}

	public void setPolicyAcctValueNtd(BigDecimal policyAcctValueNtd) {
		this.policyAcctValueNtd = policyAcctValueNtd;
	}

	private String lipmFlexRcpMk = "N";

	public String getCustomerNameBase64() {
		return customerNameBase64;
	}

	public void setCustomerNameBase64(String customerNameBase64) {
		this.customerNameBase64 = customerNameBase64;
	}

	private List<CoverageVo> coverageList = new ArrayList<>();
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDrawDay() {
		return drawDay;
	}

	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getAvgRoiRate() {
		return avgRoiRate;
	}

	public void setAvgRoiRate(BigDecimal avgRoiRate) {
		this.avgRoiRate = avgRoiRate;
	}

	public BigDecimal getPolicyAcctValue() {
		return policyAcctValue;
	}

	public void setPolicyAcctValue(BigDecimal policyAcctValue) {
		this.policyAcctValue = policyAcctValue;
	}

	public List<CoverageVo> getCoverageList() {
		return coverageList;
	}

	public void setCoverageList(List<CoverageVo> coverageList) {
		this.coverageList = coverageList;
	}

	public String getMainInsuredName() {
		return mainInsuredName;
	}

	public void setMainInsuredName(String mainInsuredName) {
		this.mainInsuredName = mainInsuredName;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getExpiredFlag() {
		return expiredFlag;
	}

	public void setExpiredFlag(String expiredFlag) {
		this.expiredFlag = expiredFlag;
	}

	public String getApplyLockedFlag() {
		return applyLockedFlag;
	}

	public void setApplyLockedFlag(String applyLockedFlag) {
		this.applyLockedFlag = applyLockedFlag;
	}

	public String getApplyLockedMsg() {
		return applyLockedMsg;
	}

	public void setApplyLockedMsg(String applyLockedMsg) {
		this.applyLockedMsg = applyLockedMsg;
	}

	public List<LitracEsVo> getLitracEsList() {
		return litracEsList;
	}

	public void setLitracEsList(List<LitracEsVo> litracEsList) {
		this.litracEsList = litracEsList;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getMainInsuredNameBase64() {
		return mainInsuredNameBase64;
	}

	public void setMainInsuredNameBase64(String mainInsuredNameBase64) {
		this.mainInsuredNameBase64 = mainInsuredNameBase64;
	}
	
	public int getSameIdCount() {
		return sameIdCount;
	}

	public void setSameIdCount(int sameIdCount) {
		this.sameIdCount = sameIdCount;
	}

	public PolicyExtraVo getPolicyExtraVo() {
		return policyExtraVo;
	}

	public void setPolicyExtraVo(PolicyExtraVo policyExtraVo) {
		this.policyExtraVo = policyExtraVo;
	}
	
	public PolicyPayerVo getPolicyPayerVo() {
		return policyPayerVo;
	}

	public void setPolicyPayerVo(PolicyPayerVo policyPayerVo) {
		this.policyPayerVo = policyPayerVo;
	}

	public String getCanRenew() {
		return canRenew;
	}

	public void setCanRenew(String canRenew) {
		this.canRenew = canRenew;
	}

	public String getCanReduce() {
		return canReduce;
	}

	public void setCanReduce(String canReduce) {
		this.canReduce = canReduce;
	}

	public String getProdIsFatca() {
		return prodIsFatca;
	}

	public void setProdIsFatca(String prodIsFatca) {
		this.prodIsFatca = prodIsFatca;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getIsLoginerFlag() {
		return isLoginerFlag;
	}

	public void setIsLoginerFlag(String isLoginerFlag) {
		this.isLoginerFlag = isLoginerFlag;
	}

	public String getLipiRocId() {
		return lipiRocId;
	}

	public void setLipiRocId(String lipiRocId) {
		this.lipiRocId = lipiRocId;
	}

	public String getLipmFlexRcpMk() {
		return lipmFlexRcpMk;
	}

	public void setLipmFlexRcpMk(String lipmFlexRcpMk) {
		this.lipmFlexRcpMk = lipmFlexRcpMk;
	}
	
	public String getPolicyListType() {
		return policyListType;
	}

	public void setPolicyListType(String policyListType) {
		this.policyListType = policyListType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}