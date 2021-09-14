package com.twfhclife.adm.service;

import java.util.List;

import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.EventRecordVo;
import com.twfhclife.adm.model.SystemEventVo;

public interface IEventRecordService {
	
	public EventRecordVo getSelectOption();

	public List<BusinessEventVo> getEventRecordTable(BusinessEventVo vo);
	
	public BusinessEventVo getBusinessEventDetail(String businessEventId);
	
	public List<SystemEventVo> getSystemEventDetail(String businessEventId);
	
}
