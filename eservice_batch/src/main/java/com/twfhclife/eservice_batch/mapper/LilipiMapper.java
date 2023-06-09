package com.twfhclife.eservice_batch.mapper;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.LilipiVo;

public interface LilipiMapper {
	LilipiVo findByLipiInsuNo(@Param("lipiInsuNo") String lipiInsuNo);
}
