package com.twfhclife.generic.dao.adm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.adm.model.SystemEventVo;

public interface SystemEventDao {
	
	List<SystemEventVo> query(@Param("systemEventVo") SystemEventVo systemEventVo);
	
	int count(@Param("systemEventVo") SystemEventVo systemEventVo);
	
	void add(@Param("systemEventVo") SystemEventVo systemEventVo);
	
	void update(@Param("systemEventVo") SystemEventVo systemEventVo);
	
	void delete(@Param("systemEventVo") SystemEventVo systemEventVo);
}

