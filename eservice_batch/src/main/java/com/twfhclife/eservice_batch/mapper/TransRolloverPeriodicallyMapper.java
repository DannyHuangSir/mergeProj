package com.twfhclife.eservice_batch.mapper;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransRolloverPeriodicallyVo;

public interface TransRolloverPeriodicallyMapper {
	public TransRolloverPeriodicallyVo findByTransNum(@Param("transRolloverPeriodicallyVo") TransRolloverPeriodicallyVo transRolloverPeriodicallyVo);
}
