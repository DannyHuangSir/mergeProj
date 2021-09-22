package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransChangePremiumVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface TransChangePremiumDao {
	
	Date getActiveDate(@Param("policyNo") String policyNo);

	int insert(TransChangePremiumVo record);

    List<TransChangePremiumVo> getTransChangePremium(TransChangePremiumVo qryVo);
}