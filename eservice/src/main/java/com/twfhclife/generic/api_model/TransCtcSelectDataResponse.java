package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

public class TransCtcSelectDataResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<TransCtcSelectDataVo> selectDataList;
	public List<TransCtcSelectDataVo> getSelectDataList() {
		return selectDataList;
	}
	public void setSelectDataList(List<TransCtcSelectDataVo> selectDataList) {
		this.selectDataList = selectDataList;
	}
}
