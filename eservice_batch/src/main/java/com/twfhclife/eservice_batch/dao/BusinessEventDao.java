package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.BusinessEventMapper;
import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;
import com.twfhclife.eservice_batch.model.MessageRecordVo;

public class BusinessEventDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(BusinessEventDao.class);

	public List<BusinessEventMsgTmpVo> getUnhandledEvent() {
		List<BusinessEventMsgTmpVo> eventList = null;
		try {
			BusinessEventMapper businessEventMapper = this.getSqlSession().getMapper(BusinessEventMapper.class);
			eventList = businessEventMapper.findUnhandledEvent();
			logger.debug("eventList=", eventList);
		} catch (Exception e) {
			logger.error("BusinessEventDao getUnhandledEvent error:", e);
		} finally {
			this.release();
		}
		return eventList;
	}
	
	public void updateEventNoticeStatus(int eventId, int noticeStatus) {
		int result = 0;
		try {
			BusinessEventMapper businessEventMapper = this.getSqlSession().getMapper(BusinessEventMapper.class);
			result = businessEventMapper.updateEventNoticeStatus(eventId, noticeStatus);
			logger.debug("update result=", result);
			this.commit();
		} catch (Exception e) {
			logger.error("BusinessEventDao updateEventNoticeStatus error:", e);
			this.rollback();
		} finally {
			this.release();
		}
	}
	
	public void insertMessageRecord(MessageRecordVo messageRecordVo) {
		int result = 0;
		try {
			BusinessEventMapper businessEventMapper = this.getSqlSession().getMapper(BusinessEventMapper.class);
			result = businessEventMapper.insertMessageRecord(messageRecordVo);
			logger.debug("insert result=", result);
			this.commit();
		} catch (Exception e) {
			logger.error("BusinessEventDao insertMessageRecord error:", e);
			this.rollback();
		} finally {
			this.release();
		}
	}
	
}
