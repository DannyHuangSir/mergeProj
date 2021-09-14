package com.twfhclife.eservice.policy.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProductVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String productId;
	/**  */
	private String productCode;
	/**  */
	private String productType;
	/** 1:主約, 2:附約 */
	private String productTypeName;
	/**  */
	private String productName;
	/**  */
	private String prodCate;
	
	private String desiVersion;
	
	private Date desiSaleDate;
	
	private Date desiChanVarDate;
	
	public String getProductId() {
		return this.productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getProductType() {
		return this.productType;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getProductName() {
		return this.productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProdCate() {
		return this.prodCate;
	}
	
	public void setProdCate(String prodCate) {
		this.prodCate = prodCate;
	}

	public String getDesiVersion() {
		return desiVersion;
	}

	public void setDesiVersion(String desiVersion) {
		this.desiVersion = desiVersion;
	}

	public Date getDesiSaleDate() {
		return desiSaleDate;
	}

	public void setDesiSaleDate(Date desiSaleDate) {
		this.desiSaleDate = desiSaleDate;
	}

	public Date getDesiChanVarDate() {
		return desiChanVarDate;
	}

	public void setDesiChanVarDate(Date desiChanVarDate) {
		this.desiChanVarDate = desiChanVarDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

