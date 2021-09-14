package com.twfhclife.eservice.api.adm.service;

import java.util.List;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.EventRecordVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;

public interface IEventRecordService {

	public EventRecordVo getSelectOption();

	public List<BusinessEventVo> getEventRecordTable(BusinessEventVo vo);

	public BusinessEventVo getBusinessEventDetail(String businessEventId);

	public List<SystemEventVo> getSystemEventDetail(String businessEventId);

}
