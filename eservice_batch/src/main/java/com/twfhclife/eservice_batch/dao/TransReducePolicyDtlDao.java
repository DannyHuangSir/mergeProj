package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransReducePolicyDtlMapper;
import com.twfhclife.eservice_batch.model.TransReducePolicyDtlVo;

public class TransReducePolicyDtlDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransReducePolicyDtlDao.class);
	
	public TransReducePolicyDtlVo findById(TransReducePolicyDtlVo transReducePolicyDtlVo) {
		TransReducePolicyDtlVo transReducePolicyDtl = null;
		try {
			TransReducePolicyDtlMapper transReducePolicyDtlMapper = this.getSqlSession().getMapper(TransReducePolicyDtlMapper.class);
			transReducePolicyDtl = transReducePolicyDtlMapper.findById(transReducePolicyDtlVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transReducePolicyDtl;
		
	}
	
	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlList(TransReducePolicyDtlVo transReducePolicyDtlVo) {
		List<TransReducePolicyDtlVo> transReducePolicyDtlList = null;
		try {
			TransReducePolicyDtlMapper transReducePolicyDtlMapper = this.getSqlSession().getMapper(TransReducePolicyDtlMapper.class);
			transReducePolicyDtlList = transReducePolicyDtlMapper.getTransReducePolicyDtlList(transReducePolicyDtlVo);
		} catch (Exception e) {
			logger.error("getTransReducePolicyDtlList error:", e);
		} finally {
			this.release();
		}
		return transReducePolicyDtlList;
	}
}
