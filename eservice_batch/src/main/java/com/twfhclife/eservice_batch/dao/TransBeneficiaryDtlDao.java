package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransBeneficiaryDtlMapper;
import com.twfhclife.eservice_batch.model.TransBeneficiaryDtlVo;

public class TransBeneficiaryDtlDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransBeneficiaryDtlDao.class);
	
	public TransBeneficiaryDtlVo findById(TransBeneficiaryDtlVo transBeneficiaryDtlVo) {
		TransBeneficiaryDtlVo transBeneficiaryDtl = null;
		try {
			TransBeneficiaryDtlMapper transBeneficiaryDtlMapper = this.getSqlSession().getMapper(TransBeneficiaryDtlMapper.class);
			transBeneficiaryDtl = transBeneficiaryDtlMapper.findById(transBeneficiaryDtlVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transBeneficiaryDtl;
		
	}
	
	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlList(TransBeneficiaryDtlVo transBeneficiaryDtlVo) {
		List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = null;
		try {
			TransBeneficiaryDtlMapper transBeneficiaryDtlMapper = this.getSqlSession().getMapper(TransBeneficiaryDtlMapper.class);
			transBeneficiaryDtlList = transBeneficiaryDtlMapper.getTransBeneficiaryDtlList(transBeneficiaryDtlVo);
		} catch (Exception e) {
			logger.error("getTransBeneficiaryDtlList error:", e);
		} finally {
			this.release();
		}
		return transBeneficiaryDtlList;
	}
}
