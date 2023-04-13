package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

public class TransCtcSelectUtilResponse  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<TransCtcSelectUtilVo> selectUtilList;

	public List<TransCtcSelectUtilVo> getSelectUtilList() {
		return selectUtilList;
	}
	public void setSelectUtilList(List<TransCtcSelectUtilVo> selectUtilList) {
		this.selectUtilList = selectUtilList;
	}
}
