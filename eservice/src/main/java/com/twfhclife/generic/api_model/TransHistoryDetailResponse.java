package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransDetailVo;

public class TransHistoryDetailResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<TransDetailVo> transHistoryDetailList;

	public List<TransDetailVo> getTransHistoryDetailList() {
		return transHistoryDetailList;
	}

	public void setTransHistoryDetailList(List<TransDetailVo> transHistoryDetailList) {
		this.transHistoryDetailList = transHistoryDetailList;
	}
}