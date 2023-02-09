package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.AgentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 招攬人 Dao.
 * 
 * @author all
 */
public interface AgentDao {
	
	/**
	 * 代理人-查詢.
	 * 
	 * @param agentVo AgentVo
	 * @return 回傳查詢結果
	 */
	List<AgentVo> getAgentList(@Param("agentVo") AgentVo agentVo);
	
	/**
	 * 招攬人-查詢.
	 * 
	 * @param agentCode 代理人代碼
	 * @return 回傳查詢結果
	 */
	AgentVo getAgent(@Param("agentCode") String agentCode);
	
	/**
	 * 取得合作對象.
	 * 
	 * @param agentCode 代理人代碼
	 * @return 回傳合作對象
	 */
	List<AgentVo> getAgentOptionList(@Param("agentCode") String agentCode);
}