package com.twfhclife.eservice_batch.dao;

import java.util.List;

import com.twfhclife.eservice_batch.model.TransStatusHistoryVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransMapper;
import com.twfhclife.eservice_batch.model.TransVo;

public class TransDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransDao.class);
	
	public TransVo findById(TransVo transVo) {
		TransVo trans = null;
		try {
			TransMapper transMapper = this.getSqlSession().getMapper(TransMapper.class);
			trans = transMapper.findById(transVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return trans;
		
	}
	
	public List<TransVo> getTransList(TransVo transVo) {
		List<TransVo> transList = null;
		try {
			TransMapper transMapper = this.getSqlSession().getMapper(TransMapper.class);
			transList = transMapper.getTransList(transVo);
		} catch (Exception e) {
			logger.error("getTransList error:", e);
		} finally {
			this.release();
		}
		return transList;
	}
	
	public int updateTrans(TransVo transVo) {
		int result = 0;
		try {
			TransMapper transMapper = this.getSqlSession().getMapper(TransMapper.class);
			result = transVo.getTransNum().indexOf("M") == -1 ? transMapper.updateTrans(transVo) : transMapper.updateTransMerge(transVo);
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("updateTrans error:", e);
		} finally {
			this.release();
		}
		return result;
	}

	public int addTransStatusHistory(TransStatusHistoryVo transStatusHistoryVo) {
		int result = 0;
		try {
			TransMapper transMapper = this.getSqlSession().getMapper(TransMapper.class);
			result = transMapper.addTransStatusHistory(transStatusHistoryVo);
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("addTransStatusHistory error:", e);
		} finally {
			this.release();
		}
		return result;
	}

	public String getTransNumsByMergeNum(String transNum) {
		try {
			TransMapper transMapper = this.getSqlSession().getMapper(TransMapper.class);
			return transMapper.getTransNumsByMergeNum(transNum);
		} catch (Exception e) {
			logger.error("getTransNumsByMergeNum error:", e);
		} finally {
			this.release();
		}
		return "";
	}
}
