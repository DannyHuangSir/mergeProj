package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransCushionMapper;
import com.twfhclife.eservice_batch.model.TransCushionVo;

public class TransCushionDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransCushionDao.class);
	
	public TransCushionVo findById(TransCushionVo transCushionVo) {
		TransCushionVo transCushion = null;
		try {
			TransCushionMapper transCushionMapper = this.getSqlSession().getMapper(TransCushionMapper.class);
			transCushion = transCushionMapper.findById(transCushionVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transCushion;
		
	}
	
	public List<TransCushionVo> getTransCushionList(TransCushionVo transCushionVo) {
		List<TransCushionVo> transCushionList = null;
		try {
			TransCushionMapper transCushionMapper = this.getSqlSession().getMapper(TransCushionMapper.class);
			transCushionList = transCushionMapper.getTransCushionList(transCushionVo);
		} catch (Exception e) {
			logger.error("getTransCushionList error:", e);
		} finally {
			this.release();
		}
		return transCushionList;
	}
	
	public TransCushionVo getTransCushion(TransCushionVo transCushionVo) {
		List<TransCushionVo> transCushionList = this.getTransCushionList(transCushionVo);
		return transCushionList != null && transCushionList.size() > 0 ? transCushionList.get(0) : null;
	}
}
