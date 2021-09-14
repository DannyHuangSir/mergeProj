package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransBeneficiaryMapper;
import com.twfhclife.eservice_batch.model.TransBeneficiaryVo;

public class TransBeneficiaryDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransBeneficiaryDao.class);
	
	public TransBeneficiaryVo findById(TransBeneficiaryVo transBeneficiaryVo) {
		TransBeneficiaryVo transBeneficiary = null;
		try {
			TransBeneficiaryMapper transBeneficiaryMapper = this.getSqlSession().getMapper(TransBeneficiaryMapper.class);
			transBeneficiary = transBeneficiaryMapper.findById(transBeneficiaryVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transBeneficiary;
		
	}
	
	public List<TransBeneficiaryVo> getTransBeneficiaryList(TransBeneficiaryVo transBeneficiaryVo) {
		List<TransBeneficiaryVo> transBeneficiaryList = null;
		try {
			TransBeneficiaryMapper transBeneficiaryMapper = this.getSqlSession().getMapper(TransBeneficiaryMapper.class);
			transBeneficiaryList = transBeneficiaryMapper.getTransBeneficiaryList(transBeneficiaryVo);
		} catch (Exception e) {
			logger.error("getTransBeneficiaryList error:", e);
		} finally {
			this.release();
		}
		return transBeneficiaryList;
	}

	public TransBeneficiaryVo getTransBeneficiary(TransBeneficiaryVo transBeneficiaryVo) {
		List<TransBeneficiaryVo> transBeneficiaryList = this.getTransBeneficiaryList(transBeneficiaryVo);
		return transBeneficiaryList != null && transBeneficiaryList.size() > 0 ? transBeneficiaryList.get(0) : null;
	}
}
