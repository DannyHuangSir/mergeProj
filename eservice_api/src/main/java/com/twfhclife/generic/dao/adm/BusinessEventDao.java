package com.twfhclife.generic.dao.adm;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.EventConditionVo;
import com.twfhclife.eservice.api.adm.model.EventParameterVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;

public interface BusinessEventDao {

	public List<BusinessEventVo> getEventRecordTable(@Param("businessEventVo") BusinessEventVo vo,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	public BusinessEventVo getBusinessEventById(@Param("businessEventId") String businessEventId);

	public List<EventConditionVo> getEventCondition(@Param("businessEventId") String businessEventId);

	public List<EventParameterVo> getEventParameter(@Param("businessEventId") String businessEventId);

	public List<SystemEventVo> getSystemEventById(@Param("businessEventId") String businessEventId);
	
	List<BusinessEventVo> query(@Param("businessEventVo") BusinessEventVo businessEventVo);
	
	int count(@Param("businessEventVo") BusinessEventVo businessEventVo);
	
	int getNextId();
	
	int add(@Param("businessEventVo") BusinessEventVo businessEventVo);
	
	void update(@Param("businessEventVo") BusinessEventVo businessEventVo);
	
	void delete(@Param("businessEventVo") BusinessEventVo businessEventVo);

}
