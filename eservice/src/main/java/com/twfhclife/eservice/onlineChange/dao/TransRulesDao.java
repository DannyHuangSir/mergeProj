package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransRulesVo;

public interface TransRulesDao {	
	
	TransRulesVo getTransRulesByTransType(@Param("transType") String transType , @Param("policyType") String policyType );
	
}
