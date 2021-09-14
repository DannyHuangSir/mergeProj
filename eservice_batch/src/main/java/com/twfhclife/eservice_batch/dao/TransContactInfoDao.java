package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransContactInfoMapper;
import com.twfhclife.eservice_batch.model.TransContactInfoVo;

public class TransContactInfoDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransContactInfoDao.class);
	
	public TransContactInfoVo findById(TransContactInfoVo transContactInfoVo) {
		TransContactInfoVo transContactInfo = null;
		try {
			TransContactInfoMapper transContactInfoMapper = this.getSqlSession().getMapper(TransContactInfoMapper.class);
			transContactInfo = transContactInfoMapper.findById(transContactInfoVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transContactInfo;
		
	}
	
	public List<TransContactInfoVo> getTransContactInfoList(TransContactInfoVo transContactInfoVo) {
		List<TransContactInfoVo> transContactInfoList = null;
		try {
			TransContactInfoMapper transContactInfoMapper = this.getSqlSession().getMapper(TransContactInfoMapper.class);
			transContactInfoList = transContactInfoMapper.getTransContactInfoList(transContactInfoVo);
		} catch (Exception e) {
			logger.error("getTransContactInfoList error:", e);
		} finally {
			this.release();
		}
		return transContactInfoList;
	}
	
	public TransContactInfoVo getTransContactInfo(TransContactInfoVo transContactInfoVo) {
		List<TransContactInfoVo> transContactInfoList = this.getTransContactInfoList(transContactInfoVo);
		return transContactInfoList != null && transContactInfoList.size() > 0 ? transContactInfoList.get(0) : null;
	}
}
