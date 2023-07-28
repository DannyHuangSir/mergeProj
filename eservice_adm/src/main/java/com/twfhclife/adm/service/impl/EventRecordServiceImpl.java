package com.twfhclife.adm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.BusinessEventDao;
import com.twfhclife.adm.dao.ParameterDao;
import com.twfhclife.adm.dao.SystemsDao;
import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.EventConditionVo;
import com.twfhclife.adm.model.EventParameterVo;
import com.twfhclife.adm.model.EventRecordVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.model.SystemEventVo;
import com.twfhclife.adm.model.SystemsVo;
import com.twfhclife.adm.service.IEventRecordService;

@Service
public class EventRecordServiceImpl implements IEventRecordService{
	
	private static final Logger logger = LogManager.getLogger(EventRecordServiceImpl.class);
	
	@Autowired
	private BusinessEventDao businessEventDao;
	
	@Autowired
	private SystemsDao systemsDao;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Override
	public EventRecordVo getSelectOption(){
		EventRecordVo result = null;
		try {
			List<SystemsVo> systems = systemsDao.findAll();

			//List<BusinessEventVo> businessEvents = businessEventDao.getEventCodeName();
			List<ParameterVo> eventTypes = parameterDao.getParameterByCategoryCode(null, Arrays.asList("EVENT_TYPE_JD","EVENT_TYPE"));
			
			//如果之後用代碼的話--
			List<ParameterVo> eventStatus = parameterDao.getParameterByCategoryCode(null, Arrays.asList("EVENT_STATUS"));
			
			//未用代碼
//			List<ParameterVo> eventStatus = new ArrayList<ParameterVo>();
//			ParameterVo parameter = new ParameterVo();
//			parameter.setParameterName("1");
//			parameter.setParameterValue("成功");
//			eventStatus.add(parameter);
//			parameter = new ParameterVo();
//			parameter.setParameterName("0");
//			parameter.setParameterValue("失敗");
//			eventStatus.add(parameter);
			
			result = new EventRecordVo();
			result.setSystems(systems);
			result.setEventTypes(eventTypes);
			result.setEventStatus(eventStatus);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	@Override
	public List<BusinessEventVo> getEventRecordTable(BusinessEventVo vo){
		List<BusinessEventVo> results = new ArrayList<BusinessEventVo>();
		
		SimpleDateFormat params = new SimpleDateFormat("yyyy-MM-dd");
		//進行轉換
		Date start_Date = null;
		Date end_Date = null;
		try {
			if(!vo.getStartDate().equals("") && !vo.getEndDate().equals("")){
				start_Date = params.parse(vo.getStartDate());
				end_Date = params.parse(vo.getEndDate());
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(end_Date);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.SECOND, -1);
				end_Date = cal.getTime();
			}
			results = businessEventDao.getEventRecordTable(vo, start_Date, end_Date);
		} catch (ParseException e) {
			logger.error("Unable to getEventRecordTable: {}", ExceptionUtils.getStackTrace(e));
		}
		return results;
	}
	
	@Override
	public BusinessEventVo getBusinessEventDetail(String businessEventId){
		BusinessEventVo result = businessEventDao.getBusinessEventById(businessEventId);
		List<EventConditionVo> eventConditions = businessEventDao.getEventCondition(businessEventId);
		List<EventParameterVo> eventParameters = businessEventDao.getEventParameter(businessEventId);
		result.setEventConditions(eventConditions);
		result.setEventParameters(eventParameters);
		return result;
	}
	
	@Override
	public List<SystemEventVo> getSystemEventDetail(String businessEventId){
		List<SystemEventVo> results = businessEventDao.getSystemEventById(businessEventId);
		return results;
	}

}
