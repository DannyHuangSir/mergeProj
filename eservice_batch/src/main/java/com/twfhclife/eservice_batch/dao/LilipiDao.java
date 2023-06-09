package com.twfhclife.eservice_batch.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.LilipiMapper;
import com.twfhclife.eservice_batch.model.LilipiVo;


public class LilipiDao extends BaseDao{

    private static final Logger logger = LogManager.getLogger(LilipiDao.class);
	
	public LilipiVo findByLipiInsuNo(String  lipiInsuNo) {
		LilipiVo lilipiVo = null;
		try {
			LilipiMapper lilipiMapper = this.getSqlSession().getMapper(LilipiMapper.class);
			lilipiVo = lilipiMapper.findByLipiInsuNo(lipiInsuNo);
		} catch (Exception e) {
			logger.error("findByLipiInsuNo error:", e);
		} finally {
			this.release();
		}
		return lilipiVo;
		
	}
}
