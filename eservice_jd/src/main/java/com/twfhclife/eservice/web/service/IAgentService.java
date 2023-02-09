package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.AgentVo;

import java.util.List;

/**
 * 招攬人服務.
 * 
 * @author all
 */
public interface IAgentService {
	
	/**
	 * 招攬人-查詢.
	 * 
	 * @param agentVo AgentVo
	 * @return 回傳查詢結果
	 */
	public List<AgentVo> getAgentList(AgentVo agentVo);
	
	/**
	 * 招攬人-查詢.
	 * 
	 * @param agentCode 招攬人代碼
	 * @return 回傳查詢結果
	 */
	public AgentVo getAgent(String agentCode);
}