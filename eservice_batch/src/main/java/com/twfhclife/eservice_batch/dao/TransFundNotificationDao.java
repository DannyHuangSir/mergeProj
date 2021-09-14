package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransFundNotificationMapper;
import com.twfhclife.eservice_batch.model.TransFundNotificationVo;

public class TransFundNotificationDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransFundNotificationDao.class);
	
	public List<TransFundNotificationVo> queryTransFundNotification(TransFundNotificationVo transFundNotificationVo){
		List<TransFundNotificationVo> list = null;
		try {
			TransFundNotificationMapper mapper = this.getSqlSession().getMapper(TransFundNotificationMapper.class);
			list = mapper.queryTransFundNotification(transFundNotificationVo);
		} catch(Exception e) {
			logger.error("queryTransFundNotification error: {}", e);
		} finally {
			this.release();
		}
		return list;
	}
}
