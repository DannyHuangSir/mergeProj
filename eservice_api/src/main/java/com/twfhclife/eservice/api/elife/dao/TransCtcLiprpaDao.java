package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLiprpaVo;

public interface TransCtcLiprpaDao {
	
	List<TransCtcLiprpaVo> getRevokeByLiprpaForInsuSeqNo(@Param("prodNo") String prodNo , @Param("prpaInsuSeqNo") String prpaInsuSeqNo);
}

