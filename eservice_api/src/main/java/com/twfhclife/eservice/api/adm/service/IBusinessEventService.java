package com.twfhclife.eservice.api.adm.service;

import java.util.List;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;

public interface IBusinessEventService {
	
	List<BusinessEventVo> query(BusinessEventVo businessEventVo);
	
	int count(BusinessEventVo businessEventVo);
	
	int getNextId();
	
	int add(BusinessEventVo businessEventVo);
	
	void update(BusinessEventVo businessEventVo);
	
	void delete(BusinessEventVo businessEventVo);
}