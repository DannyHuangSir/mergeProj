package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.ETLMapper;
import com.twfhclife.eservice_batch.model.ETLJobLogVo;

public class ETLDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(ETLDao.class);

	public void callETLProcess() {
		try {
			ETLMapper etlMapper = this.getSqlSession().getMapper(ETLMapper.class);
			etlMapper.callETLProcess();
		} catch (Exception e) {
			logger.error("getSqlSessionFactory error:", e);
		} finally {
			this.release();
		}
	}
	
	public List<ETLJobLogVo> getETLResult() {
		List<ETLJobLogVo> list = null;
		try {
			ETLMapper etlMapper = this.getSqlSession().getMapper(ETLMapper.class);
			list = etlMapper.getETLResult();
		} catch (Exception e) {
			logger.error("getETLResult error:", e);
		} finally {
			this.release();
		}
		return list;
	}
}