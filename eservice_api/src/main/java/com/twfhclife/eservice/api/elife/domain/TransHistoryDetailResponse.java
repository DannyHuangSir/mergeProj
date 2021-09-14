package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

public class TransHistoryDetailResponse {

	private List<TransDetailVo> transHistoryDetailList;

	public List<TransDetailVo> getTransHistoryDetailList() {
		return transHistoryDetailList;
	}

	public void setTransHistoryDetailList(List<TransDetailVo> transHistoryDetailList) {
		this.transHistoryDetailList = transHistoryDetailList;
	}
}