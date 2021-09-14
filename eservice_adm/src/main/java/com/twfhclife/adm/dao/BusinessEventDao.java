package com.twfhclife.adm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.EventConditionVo;
import com.twfhclife.adm.model.EventParameterVo;
import com.twfhclife.adm.model.SystemEventVo;

public interface BusinessEventDao {
	
	public List<BusinessEventVo> getEventRecordTable(@Param("businessEventVo") BusinessEventVo vo, 
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	public BusinessEventVo getBusinessEventById(@Param("businessEventId") String businessEventId);
	
	public List<EventConditionVo> getEventCondition(@Param("businessEventId") String businessEventId);
	
	public List<EventParameterVo> getEventParameter(@Param("businessEventId") String businessEventId);
	
	public List<SystemEventVo> getSystemEventById(@Param("businessEventId") String businessEventId);

}
