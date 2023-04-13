package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLineacVo;

public interface TransCtcLineacDao {

	
	List<TransCtcLineacVo> getRevokeByLineacForNeacInsuNo(@Param("insuNo") String insuNo);
}
