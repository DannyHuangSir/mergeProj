package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TableGetVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 申請項目
	private String transType;

	// 保單清單
	private List<TransPolicyVo> policys;

	// 顯示多筆資料
	private List<Map<String, Object>> transDoubleObject;

	// 顯示多筆新的資料(有新舊時)
	private List<Map<String, Object>> transDoubleObjectNew;

	// 顯示多筆舊的資料(有新舊時)
	private List<Map<String, Object>> transDoubleObjectOld;

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public List<TransPolicyVo> getPolicys() {
		return policys;
	}

	public void setPolicys(List<TransPolicyVo> policys) {
		this.policys = policys;
	}

	public List<Map<String, Object>> getTransDoubleObject() {
		return transDoubleObject;
	}

	public void setTransDoubleObject(List<Map<String, Object>> transDoubleObject) {
		this.transDoubleObject = transDoubleObject;
	}

	public List<Map<String, Object>> getTransDoubleObjectNew() {
		return transDoubleObjectNew;
	}

	public void setTransDoubleObjectNew(List<Map<String, Object>> transDoubleObjectNew) {
		this.transDoubleObjectNew = transDoubleObjectNew;
	}

	public List<Map<String, Object>> getTransDoubleObjectOld() {
		return transDoubleObjectOld;
	}

	public void setTransDoubleObjectOld(List<Map<String, Object>> transDoubleObjectOld) {
		this.transDoubleObjectOld = transDoubleObjectOld;
	}

}
