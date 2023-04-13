package com.twfhclife.generic.api_model;

public class TransCtcLiprpaVo {
	private String prpaRcpBatch;
	/** 實繳金額=退費金額 */
	private String prpaActAmt;
	/** 繳型 (H=信用卡繳費；3=劃撥件；K=匯款件；A=轉帳) */
	private String prpkRcpTypeCode;
	/** 商品幣別 != ‘NTD’ 即為外幣保單 */
	private String prodCurrency;
	
	public String getPrpaRcpBatch() {
		return prpaRcpBatch;
	}
	public void setPrpaRcpBatch(String prpaRcpBatch) {
		this.prpaRcpBatch = prpaRcpBatch;
	}
	public String getPrpaActAmt() {
		return prpaActAmt;
	}
	public void setPrpaActAmt(String prpaActAmt) {
		this.prpaActAmt = prpaActAmt;
	}
	public String getPrpkRcpTypeCode() {
		return prpkRcpTypeCode;
	}
	public void setPrpkRcpTypeCode(String prpkRcpTypeCode) {
		this.prpkRcpTypeCode = prpkRcpTypeCode;
	}
	public String getProdCurrency() {
		return prodCurrency;
	}
	public void setProdCurrency(String prodCurrency) {
		this.prodCurrency = prodCurrency;
	}
	
}
