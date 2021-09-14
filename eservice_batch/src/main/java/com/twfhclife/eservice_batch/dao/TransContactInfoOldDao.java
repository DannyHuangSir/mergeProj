package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransContactInfoOldMapper;
import com.twfhclife.eservice_batch.model.TransContactInfoOldVo;

public class TransContactInfoOldDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransContactInfoOldDao.class);	
	
	public List<TransContactInfoOldVo> getTransContactInfoOldList(TransContactInfoOldVo transContactInfoOldVo) {
		List<TransContactInfoOldVo> transContactInfoOldList = null;
		try {
			TransContactInfoOldMapper transContactInfoOldMapper = this.getSqlSession().getMapper(TransContactInfoOldMapper.class);
			transContactInfoOldList = transContactInfoOldMapper.getTransContactInfoOldList(transContactInfoOldVo);
		} catch (Exception e) {
			logger.error("getTransContactInfoOldList error:", e);
		} finally {
			this.release();
		}
		return transContactInfoOldList;
	}
	
	public TransContactInfoOldVo getTransContactInfoOld(TransContactInfoOldVo transContactInfoOldVo) {
		List<TransContactInfoOldVo> transContactInfoOldList = this.getTransContactInfoOldList(transContactInfoOldVo);
		return transContactInfoOldList != null && transContactInfoOldList.size() > 0 ? transContactInfoOldList.get(0) : null;
	}
}
