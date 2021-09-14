package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransBounsMapper;
import com.twfhclife.eservice_batch.model.TransBounsVo;

public class TransBounsDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransBounsDao.class);
	
	public TransBounsVo findById(TransBounsVo transBounsVo) {
		TransBounsVo transBouns = null;
		try {
			TransBounsMapper transBounsMapper = this.getSqlSession().getMapper(TransBounsMapper.class);
			transBouns = transBounsMapper.findById(transBounsVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transBouns;
		
	}
	
	public List<TransBounsVo> getTransBounsList(TransBounsVo transBounsVo) {
		List<TransBounsVo> transBounsList = null;
		try {
			TransBounsMapper transBounsMapper = this.getSqlSession().getMapper(TransBounsMapper.class);
			transBounsList = transBounsMapper.getTransBounsList(transBounsVo);
		} catch (Exception e) {
			logger.error("getTransBounsList error:", e);
		} finally {
			this.release();
		}
		return transBounsList;
	}
	
	public TransBounsVo getTransBouns(TransBounsVo transBounsVo) {
		List<TransBounsVo> transBounsList = this.getTransBounsList(transBounsVo);
		return transBounsList != null && transBounsList.size() > 0 ? transBounsList.get(0) : null;
	}
}
