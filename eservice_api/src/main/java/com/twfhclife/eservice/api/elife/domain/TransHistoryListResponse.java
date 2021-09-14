package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransVo;

public class TransHistoryListResponse {

	private List<TransVo> transHistoryList;

	public List<TransVo> getTransHistoryList() {
		return transHistoryList;
	}

	public void setTransHistoryList(List<TransVo> transHistoryList) {
		this.transHistoryList = transHistoryList;
	}
}