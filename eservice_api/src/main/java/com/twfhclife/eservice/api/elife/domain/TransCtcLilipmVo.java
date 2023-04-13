package com.twfhclife.eservice.api.elife.domain;

import java.util.Date;

public class TransCtcLilipmVo {

	/** 主約險種 */
	private String lipmInsuTyp;
	/** 保單編號 */
	private String lipmInsuNo;
	/** 契約狀況 */
	private String lipmSt;
	/** 有無借款 */
	private String lipmLoMk;
	/** 附約險種1 */
	private String lipaAddCode;
	/** 附約險種2 */
	private String lipfAddCode;
	/** ID */
	private String lipmId;
	/** 投保日期, */
	private Date lipmInsuBegDate;
	/** 最近繳費日 */
	private Date lipmTredRcpDate;
	/** 繳別 */
	private String lipmRcpCode;
	/** 最近已繳費期次 */
	private Date lipmTredPaabDate;
	/** 主契約保額 */
	private String lipiMainAmt;
	/** 下期應繳期次 */
	private Date nextLipmTredPaabDate;
	/** 下下期應繳期次*/
	private Date nextLipmTredPaabDate2;
	
	private String lipmAccUnit    ;
	private String lipmAgenCode   ;
	private String aginRecCode    ;
	private String aginAgenCode   ;
	private String aginInveArea   ;
	private String aginSeq        ;
	
	private String lipmStDate;
	private String prodNo;
	private String prodType;
	private String prodKind3;
	private String assnArriDate;
	
	public String getLipmAgenCode() {
		return lipmAgenCode;
	}
	public void setLipmAgenCode(String lipmAgenCode) {
		this.lipmAgenCode = lipmAgenCode;
	}
	public String getAginRecCode() {
		return aginRecCode;
	}
	public void setAginRecCode(String aginRecCode) {
		this.aginRecCode = aginRecCode;
	}
	public String getAginAgenCode() {
		return aginAgenCode;
	}
	public void setAginAgenCode(String aginAgenCode) {
		this.aginAgenCode = aginAgenCode;
	}
	public String getAginInveArea() {
		return aginInveArea;
	}
	public void setAginInveArea(String aginInveArea) {
		this.aginInveArea = aginInveArea;
	}
	public String getAginSeq() {
		return aginSeq;
	}
	public void setAginSeq(String aginSeq) {
		this.aginSeq = aginSeq;
	}
	public String getLipmAccUnit() {
		return lipmAccUnit;
	}
	public void setLipmAccUnit(String lipmAccUnit) {
		this.lipmAccUnit = lipmAccUnit;
	}
	public String getLipmInsuTyp() {
		return lipmInsuTyp;
	}
	public void setLipmInsuTyp(String lipmInsuTyp) {
		this.lipmInsuTyp = lipmInsuTyp;
	}
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}
	public String getLipmSt() {
		return lipmSt;
	}
	public void setLipmSt(String lipmSt) {
		this.lipmSt = lipmSt;
	}
	public String getLipmLoMk() {
		return lipmLoMk;
	}
	public void setLipmLoMk(String lipmLoMk) {
		this.lipmLoMk = lipmLoMk;
	}
	public String getLipaAddCode() {
		return lipaAddCode;
	}
	public void setLipaAddCode(String lipaAddCode) {
		this.lipaAddCode = lipaAddCode;
	}
	public String getLipfAddCode() {
		return lipfAddCode;
	}
	public void setLipfAddCode(String lipfAddCode) {
		this.lipfAddCode = lipfAddCode;
	}
	public String getLipmId() {
		return lipmId;
	}
	public void setLipmId(String lipmId) {
		this.lipmId = lipmId;
	}
	public Date getLipmInsuBegDate() {
		return lipmInsuBegDate;
	}
	public void setLipmInsuBegDate(Date lipmInsuBegDate) {
		this.lipmInsuBegDate = lipmInsuBegDate;
	}
	public Date getLipmTredRcpDate() {
		return lipmTredRcpDate;
	}
	public void setLipmTredRcpDate(Date lipmTredRcpDate) {
		this.lipmTredRcpDate = lipmTredRcpDate;
	}
	public String getLipmRcpCode() {
		return lipmRcpCode;
	}
	public void setLipmRcpCode(String lipmRcpCode) {
		this.lipmRcpCode = lipmRcpCode;
	}
	public Date getLipmTredPaabDate() {
		return lipmTredPaabDate;
	}
	public void setLipmTredPaabDate(Date lipmTredPaabDate) {
		this.lipmTredPaabDate = lipmTredPaabDate;
	}
	public String getLipiMainAmt() {
		return lipiMainAmt;
	}
	public void setLipiMainAmt(String lipiMainAmt) {
		this.lipiMainAmt = lipiMainAmt;
	}
	public Date getNextLipmTredPaabDate() {
		return nextLipmTredPaabDate;
	}
	public void setNextLipmTredPaabDate(Date nextLipmTredPaabDate) {
		this.nextLipmTredPaabDate = nextLipmTredPaabDate;
	}
	public Date getNextLipmTredPaabDate2() {
		return nextLipmTredPaabDate2;
	}
	public void setNextLipmTredPaabDate2(Date nextLipmTredPaabDate2) {
		this.nextLipmTredPaabDate2 = nextLipmTredPaabDate2;
	}
	public String getLipmStDate() {
		return lipmStDate;
	}
	public void setLipmStDate(String lipmStDate) {
		this.lipmStDate = lipmStDate;
	}
	public String getProdNo() {
		return prodNo;
	}
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getProdKind3() {
		return prodKind3;
	}
	public void setProdKind3(String prodKind3) {
		this.prodKind3 = prodKind3;
	}
	public String getAssnArriDate() {
		return assnArriDate;
	}
	public void setAssnArriDate(String assnArriDate) {
		this.assnArriDate = assnArriDate;
	}
	
	
	
}
