package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import com.twfhclife.eservice_batch.model.ETLJobLogVo;

public interface ETLMapper {
	public void callETLProcess();
	public List<ETLJobLogVo> getETLResult();
}