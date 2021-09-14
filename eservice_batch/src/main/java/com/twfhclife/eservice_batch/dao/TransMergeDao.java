package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransMergeMapper;
import com.twfhclife.eservice_batch.model.TransMergeVo;

public class TransMergeDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransMergeDao.class);
	
	public TransMergeVo findById(TransMergeVo transMergeVo) {
		TransMergeVo transMerge = null;
		try {
			TransMergeMapper transMergeMapper = this.getSqlSession().getMapper(TransMergeMapper.class);
			transMerge = transMergeMapper.findById(transMergeVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transMerge;
		
	}
	
	public String getTodayNextTransMergeNum() {
		String transMergeNum = null;
		try {
			TransMergeMapper transMergeMapper = this.getSqlSession().getMapper(TransMergeMapper.class);
			transMergeNum = transMergeMapper.getTodayNextTransMergeNum();
		} catch (Exception e) {
			logger.error("getTodayNextTransMergeNum error:", e);
		} finally {
			this.release();
		}
		return transMergeNum;
	}
	
	public List<TransMergeVo> getTransMergeList(TransMergeVo transMergeVo) {
		List<TransMergeVo> transMergeList = null;
		try {
			TransMergeMapper transMergeMapper = this.getSqlSession().getMapper(TransMergeMapper.class);
			transMergeList = transMergeMapper.getTransMergeList(transMergeVo);
		} catch (Exception e) {
			logger.error("getTransMergeList error:", e);
		} finally {
			this.release();
		}
		return transMergeList;
	}
	
	public int insertTransMerge(TransMergeVo transMergeVo) {
		int result = 0;
		try {
			TransMergeMapper transMergeMapper = this.getSqlSession().getMapper(TransMergeMapper.class);
			result = transMergeMapper.insertTransMerge(transMergeVo);
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("insertTransMerge error:", e);
		} finally {
			this.release();
		}
		return result;
	}
}