package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransFundNotificationDtlMapper;
import com.twfhclife.eservice_batch.model.TransFundNotificationDtlVo;

public class TransFundNotificationDtlDao extends BaseDao {
	
	private static final Logger logger = LogManager.getLogger(TransFundNotificationDao.class);
	
	public List<TransFundNotificationDtlVo> queryTransFundNotificationDtl(TransFundNotificationDtlVo transFundNotificationDtlVo){
		List<TransFundNotificationDtlVo> rtnList = null;
		try {
			TransFundNotificationDtlMapper mapper = this.getSqlSession().getMapper(TransFundNotificationDtlMapper.class);
			rtnList = mapper.queryTransFundNotificationDtl(transFundNotificationDtlVo);
		} catch(Exception e) {
			logger.error("queryTransFundNotificationDtl error: {}", e);
		} finally {
			this.release();
		}
		return rtnList;
	}

}
