package com.twfhclife.adm.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransMedicalTreatmentClaimMedicalInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 申請資料序號Stirng(2)
	 */
	private String seNo;
	
	/**
	 * 授權開始時間
	 * String(8),採西元年YYYYMMDD,例如：19801115
	 */
	private String hsTime;
	
	/**
	 * 授權結束時間
	 * String(8),採西元年YYYYMMDD,例如：19801115
	 */
	private String heTime;
	
	/**
	 * 就診類型代碼,門/急/住,String(50)
	 */
	private String otype;

	/**
	 * 就診類型名稱
	 */
	private String otypeName;

	/**
	 * 科別名稱
	 */
	private String depName;

	/**
	 * 子科別名稱
	 */
	private String subDepName;

	/**
	 * 科別代碼,String(50)
	 */
	private String depid;


	private List<Map<String, String>> dtypeList = Lists.newArrayList();

	/**
	 * 文件類型,Stirng(200),請參考(醫療資料檔案類型代碼表)
	 * 可使用MedicalTreatmentClaimFileTypeEnum
	 */
	private List<String> dtypes;

	public String getSeNo() {
		return seNo;
	}

	public void setSeNo(String seNo) {
		this.seNo = seNo;
	}

	public String getHsTime() {
		return hsTime;
	}

	public void setHsTime(String hsTime) {
		this.hsTime = hsTime;
	}

	public String getHeTime() {
		return heTime;
	}

	public void setHeTime(String heTime) {
		this.heTime = heTime;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public List<String> getDtypes() {
		return dtypes;
	}

	public void setDtypes(List<String> dtypes) {
		this.dtypes = dtypes;
	}

	public String getOtypeName() {
		return otypeName;
	}

	public void setOtypeName(String otypeName) {
		this.otypeName = otypeName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getSubDepName() {
		return subDepName;
	}

	public void setSubDepName(String subDepName) {
		this.subDepName = subDepName;
	}

	public List<Map<String, String>> getDtypeList() {
		return dtypeList;
	}

	public void setDtypeList(List<Map<String, String>> dtypeList) {
		this.dtypeList = dtypeList;
	}
}
