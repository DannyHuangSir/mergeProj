package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransBankInfoMapper;
import com.twfhclife.eservice_batch.model.TransBankInfoVo;

public class TransBankInfoDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransBankInfoDao.class);
	
	public TransBankInfoVo findById(TransBankInfoVo transBankInfoVo) {
		TransBankInfoVo transBankInfo = null;
		try {
			TransBankInfoMapper transBankInfoMapper = this.getSqlSession().getMapper(TransBankInfoMapper.class);
			transBankInfo = transBankInfoMapper.findById(transBankInfoVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transBankInfo;
		
	}
	
	public List<TransBankInfoVo> getTransBankInfoList(TransBankInfoVo transBankInfoVo) {
		List<TransBankInfoVo> transBankInfoList = null;
		try {
			TransBankInfoMapper transBankInfoMapper = this.getSqlSession().getMapper(TransBankInfoMapper.class);
			transBankInfoList = transBankInfoMapper.getTransBankInfoList(transBankInfoVo);
		} catch (Exception e) {
			logger.error("getTransBankInfoList error:", e);
		} finally {
			this.release();
		}
		return transBankInfoList;
	}
	
	public TransBankInfoVo getTransBankInfo(TransBankInfoVo transBankInfoVo) {
		List<TransBankInfoVo> transBankInfoList = this.getTransBankInfoList(transBankInfoVo);
		return transBankInfoList != null && transBankInfoList.size() > 0 ? transBankInfoList.get(0) : null;
	}
}
