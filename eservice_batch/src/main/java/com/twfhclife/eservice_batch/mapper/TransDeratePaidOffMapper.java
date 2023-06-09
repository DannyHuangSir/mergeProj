package com.twfhclife.eservice_batch.mapper;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransDeratePaidOffVo;

public interface TransDeratePaidOffMapper {
	public TransDeratePaidOffVo findByTransNum(@Param("transDeratePaidOffVo") TransDeratePaidOffVo transDeratePaidOffVo);
}
