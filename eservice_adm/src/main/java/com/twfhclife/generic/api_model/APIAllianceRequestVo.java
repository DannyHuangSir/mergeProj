package com.twfhclife.generic.api_model;

import java.util.Map;

/**
 * 調用API的通信模板
 */
public class APIAllianceRequestVo {

	//API URL 地址
	private  String  url;
	//傳遞聯盟參數
	private  Map<String, String> params;
	//聯盟鏈歷程參數
	private  Map<String, String> unParams ;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getUnParams() {
		return unParams;
	}

	public void setUnParams(Map<String, String> unParams) {
		this.unParams = unParams;
	}
}
