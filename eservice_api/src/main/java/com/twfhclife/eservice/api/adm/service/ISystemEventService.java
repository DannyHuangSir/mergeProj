package com.twfhclife.eservice.api.adm.service;

import java.util.List;

import com.twfhclife.eservice.api.adm.model.SystemEventVo;

public interface ISystemEventService {
	
	List<SystemEventVo> query(SystemEventVo systemEventVo);
	
	int count(SystemEventVo systemEventVo);
	
	void add(SystemEventVo systemEventVo);
	
	void update(SystemEventVo systemEventVo);
	
	void delete(SystemEventVo systemEventVo);
}