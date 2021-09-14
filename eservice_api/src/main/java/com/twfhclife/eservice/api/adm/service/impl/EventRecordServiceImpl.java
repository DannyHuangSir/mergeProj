package com.twfhclife.eservice.api.adm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.EventConditionVo;
import com.twfhclife.eservice.api.adm.model.EventParameterVo;
import com.twfhclife.eservice.api.adm.model.EventRecordVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.IEventRecordService;
import com.twfhclife.generic.dao.adm.BusinessEventDao;

@Service
public class EventRecordServiceImpl implements IEventRecordService {

	private static final Logger logger = LogManager.getLogger(EventRecordServiceImpl.class);

	@Autowired
	private BusinessEventDao businessEventDao;

	@Override
	public EventRecordVo getSelectOption() {
		EventRecordVo result = null;

		return result;
	}

	@Override
	public List<BusinessEventVo> getEventRecordTable(BusinessEventVo vo) {
		List<BusinessEventVo> results = new ArrayList<BusinessEventVo>();

		SimpleDateFormat params = new SimpleDateFormat("yyyy-MM-dd");
		// 進行轉換
		Date start_Date = null;
		Date end_Date = null;
		try {
			if (vo.getStartDate() != null && !vo.getStartDate().equals("") 
					&& vo.getEndDate() != null && !vo.getEndDate().equals("")) {
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
	public BusinessEventVo getBusinessEventDetail(String businessEventId) {
		BusinessEventVo result = businessEventDao.getBusinessEventById(businessEventId);
		List<EventConditionVo> eventConditions = businessEventDao.getEventCondition(businessEventId);
		List<EventParameterVo> eventParameters = businessEventDao.getEventParameter(businessEventId);
		result.setEventConditions(eventConditions);
		result.setEventParameters(eventParameters);
		return result;
	}

	@Override
	public List<SystemEventVo> getSystemEventDetail(String businessEventId) {
		List<SystemEventVo> results = businessEventDao.getSystemEventById(businessEventId);
		return results;
	}

}
