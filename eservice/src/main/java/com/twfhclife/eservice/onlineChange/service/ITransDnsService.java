package com.twfhclife.eservice.onlineChange.service;

import java.util.Map;

import com.twfhclife.eservice.onlineChange.model.TransDnsVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import org.apache.ibatis.annotations.Param;

public interface ITransDnsService {
	
	/**
	 * 
	 * @param transDnsVo
	 * @param hisVo
	 * @return Map<String,String>
	 * @throws Exception
	 */
	public Map<String,String> insertTransDns(TransDnsVo transDnsVo, TransStatusHistoryVo hisVo) throws Exception;

	/**
	 * 查询死亡除户的Detail信息
	 * @param transNum
	 * @return
	 * @throws Exception
	 */
	TransDnsVo getTransServiceDnsDetail(String transNum)throws Exception;
}
