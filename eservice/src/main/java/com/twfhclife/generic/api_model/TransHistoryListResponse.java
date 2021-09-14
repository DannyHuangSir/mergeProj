package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransVo;

public class TransHistoryListResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<TransVo> transHistoryList;

	public List<TransVo> getTransHistoryList() {
		return transHistoryList;
	}

	public void setTransHistoryList(List<TransVo> transHistoryList) {
		this.transHistoryList = transHistoryList;
	}
}