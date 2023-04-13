package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransDeratePaidOffVo;


public interface TransDeratePaidOffDao {
	int insertTransDeratePaidOff(@Param("transDeratePaidOffVo") TransDeratePaidOffVo transDeratePaidOffVo);
	
	public TransDeratePaidOffVo getTransDeratePaidOffDetail(@Param("transNum") String transNum);
}
