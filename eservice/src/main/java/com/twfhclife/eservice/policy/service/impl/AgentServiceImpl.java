package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.AgentDao;
import com.twfhclife.eservice.policy.model.AgentVo;
import com.twfhclife.eservice.policy.service.IAgentService;

/**
 * 招攬人服務.
 * 
 * @author all
 */
@Service
public class AgentServiceImpl implements IAgentService {

	@Autowired
	private AgentDao agentDao;
	
	/**
	 * 招攬人-查詢.
	 * 
	 * @param agentVo AgentVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<AgentVo> getAgentList(AgentVo agentVo) {
		return agentDao.getAgentList(agentVo);
	}
	
	/**
	 * 招攬人-查詢.
	 * 
	 * @param agentCode 招攬人代碼
	 * @return 回傳查詢結果
	 */
	@Override
	public AgentVo getAgent(String agentCode) {
		return agentDao.getAgent(agentCode);
	}
}