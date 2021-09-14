package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransAnnuityMethodMapper;
import com.twfhclife.eservice_batch.model.TransAnnuityMethodVo;

public class TransAnnuityMethodDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransAnnuityMethodDao.class);
	
	public TransAnnuityMethodVo findById(TransAnnuityMethodVo transAnnuityMethodVo) {
		TransAnnuityMethodVo transAnnuityMethod = null;
		try {
			TransAnnuityMethodMapper transAnnuityMethodMapper = this.getSqlSession().getMapper(TransAnnuityMethodMapper.class);
			transAnnuityMethod = transAnnuityMethodMapper.findById(transAnnuityMethodVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transAnnuityMethod;
		
	}
	
	public List<TransAnnuityMethodVo> getTransAnnuityMethodList(TransAnnuityMethodVo transAnnuityMethodVo) {
		List<TransAnnuityMethodVo> transAnnuityMethodList = null;
		try {
			TransAnnuityMethodMapper transAnnuityMethodMapper = this.getSqlSession().getMapper(TransAnnuityMethodMapper.class);
			transAnnuityMethodList = transAnnuityMethodMapper.getTransAnnuityMethodList(transAnnuityMethodVo);
		} catch (Exception e) {
			logger.error("getTransAnnuityMethodList error:", e);
		} finally {
			this.release();
		}
		return transAnnuityMethodList;
	}
	
	public TransAnnuityMethodVo getTransAnnuityMethod(TransAnnuityMethodVo transAnnuityMethodVo) {
		List<TransAnnuityMethodVo> transAnnuityMethodList = this.getTransAnnuityMethodList(transAnnuityMethodVo);
		return transAnnuityMethodList != null && transAnnuityMethodList.size() > 0 ? transAnnuityMethodList.get(0) : null;
	}
}
