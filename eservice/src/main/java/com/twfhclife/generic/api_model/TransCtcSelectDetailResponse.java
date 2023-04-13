package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

public class TransCtcSelectDetailResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<TransCtcSelectDetailVo> selectDetailList;

	public List<TransCtcSelectDetailVo> getSelectDetailList() {
		return selectDetailList;
	}

	public void setSelectDetailList(List<TransCtcSelectDetailVo> selectDetailList) {
		this.selectDetailList = selectDetailList;
	}
	
}
