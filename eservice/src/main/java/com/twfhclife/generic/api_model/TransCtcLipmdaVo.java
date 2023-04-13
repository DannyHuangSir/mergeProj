package com.twfhclife.generic.api_model;

import java.io.Serializable;

public class TransCtcLipmdaVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String lipmInsuNo;
	private String pmdaEInfoN;
	private String pmdaApplicantEmail;
	private String pmdaApplicantCellphone;
	
	public String getLipmInsuNo() {
		return lipmInsuNo;
	}
	public void setLipmInsuNo(String lipmInsuNo) {
		this.lipmInsuNo = lipmInsuNo;
	}
	public String getPmdaEInfoN() {
		return pmdaEInfoN;
	}
	public void setPmdaEInfoN(String pmdaEInfoN) {
		this.pmdaEInfoN = pmdaEInfoN;
	}
	public String getPmdaApplicantEmail() {
		return pmdaApplicantEmail;
	}
	public void setPmdaApplicantEmail(String pmdaApplicantEmail) {
		this.pmdaApplicantEmail = pmdaApplicantEmail;
	}
	public String getPmdaApplicantCellphone() {
		return pmdaApplicantCellphone;
	}
	public void setPmdaApplicantCellphone(String pmdaApplicantCellphone) {
		this.pmdaApplicantCellphone = pmdaApplicantCellphone;
	}

}
