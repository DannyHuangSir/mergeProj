package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;
import com.twfhclife.eservice_batch.model.BusinessEventVo;
import com.twfhclife.eservice_batch.model.MessageRecordVo;

public interface BusinessEventMapper {
	public List<BusinessEventMsgTmpVo> findUnhandledEvent();
	
	public BusinessEventVo getBusinessEventById(String id);
	
	int updateEventNoticeStatus(int eventId, int noticeStatus);
	
	int insertMessageRecord(MessageRecordVo messageRecordVo);
	
}