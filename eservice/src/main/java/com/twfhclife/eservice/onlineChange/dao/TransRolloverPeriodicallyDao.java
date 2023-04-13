package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransRolloverPeriodicallyVo;

public interface TransRolloverPeriodicallyDao {

	int insertTransRolloverPeriodically(@Param("transRolloverPeriodicallyVo") TransRolloverPeriodicallyVo transRolloverPeriodicallyVo);
	
	public TransRolloverPeriodicallyVo getTransRolloverPeriodicallyDetail(@Param("transNum") String transNum);
}
