package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransElectronicFormMapper;
import com.twfhclife.eservice_batch.mapper.TransRolloverPeriodicallyMapper;
import com.twfhclife.eservice_batch.model.TransElectronicFormVo;
import com.twfhclife.eservice_batch.model.TransRolloverPeriodicallyVo;

public class TransRolloverPeriodicallyDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransRolloverPeriodicallyDao.class);

	public TransRolloverPeriodicallyVo getTransRolloverPeriodically(TransRolloverPeriodicallyVo transRolloverPeriodicallyVo) {
		try {
			TransRolloverPeriodicallyMapper transRolloverPeriodicallyMapper = this.getSqlSession()
					.getMapper(TransRolloverPeriodicallyMapper.class);
			transRolloverPeriodicallyVo = transRolloverPeriodicallyMapper
					.findByTransNum(transRolloverPeriodicallyVo);
		} catch (Exception e) {
			logger.error("getTransRolloverPeriodicallyList error:", e);
		} finally {
			this.release();
		}
		return transRolloverPeriodicallyVo;
	}

}
