package com.twfhclife.eservice_batch.dao;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.BusinessEventJobMapper;
import com.twfhclife.eservice_batch.model.BusinessEventJobVo;
import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;

public class BusinessEventJobDao extends BaseDao{
	
	private static final Logger logger = LogManager.getLogger(BusinessEventJobDao.class);

	public Optional<List<BusinessEventJobVo>> queryBusinessEvenJobtList(BusinessEventJobVo businessEventJobVo){
		List<BusinessEventJobVo> listResult = null;
		try {
			BusinessEventJobMapper mapper = this.getSqlSession().getMapper(BusinessEventJobMapper.class);
			listResult = mapper.queryBusinessEvenJobtList(businessEventJobVo);
		} catch(Exception e) {
			logger.error("BusinessEventJobDao queryHandleEventList error: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			this.release();
		}
		return Optional.ofNullable(listResult);
	}
	
	public Optional<List<BusinessEventMsgTmpVo>> queryUnTriggerData(BusinessEventJobVo businessEventJobVo){
		List<BusinessEventMsgTmpVo> listResult = null;
		try {
			BusinessEventJobMapper mapper = this.getSqlSession().getMapper(BusinessEventJobMapper.class);
			listResult = mapper.queryUnTriggerData(businessEventJobVo);
		} catch(Exception e) {
			logger.error("BusinessEventJobDao queryUnTriggerData error: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			this.release();
		}
		return Optional.ofNullable(listResult);
	}
	
	public boolean updateNoticeStatus(Integer businessEventId, Integer status) {
		boolean result = false;
		try {
			BusinessEventJobMapper mapper = this.getSqlSession().getMapper(BusinessEventJobMapper.class);
			result = mapper.updateNoticeStatus(status, businessEventId) == 1;
			this.commit();
		} catch(Exception e) {
			logger.error("BusinessEventJobDao updateNoticeStatus error: {}", ExceptionUtils.getStackTrace(e));
			this.rollback();
		} finally {
			this.release();
		}
		return result;
	}
}
