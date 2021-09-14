package com.twfhclife.eservice.api.elife.service;

import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;

/**
 * 送出線上申請服務.
 * 
 * @author all
 */
public interface ITransAddService {

	/**
	 * 送出線上申請資料.
	 * 
	 * @param apiReq TransAddRequest
	 * @return 回傳線上申請結果
	 */
	public TransAddResponse addTransRequest(TransAddRequest apiReq);
}