package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransChangePremiumMapper;
import com.twfhclife.eservice_batch.model.TransChangePremiumVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class TransChangePremiumDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransChangePremiumDao.class);

	public List<TransChangePremiumVo> getTransChangePremium(TransChangePremiumVo vo) {
		List<TransChangePremiumVo> transChangePremiumVos = null;
		try {
			TransChangePremiumMapper transChangePremiumMapper = this.getSqlSession().getMapper(TransChangePremiumMapper.class);
			transChangePremiumVos = transChangePremiumMapper.getTransChangePremiums(vo.getTransNum());
		} catch (Exception e) {
			logger.error("getTransChangePremium error:", e);
		} finally {
			this.release();
		}
		return transChangePremiumVos;
	}
}