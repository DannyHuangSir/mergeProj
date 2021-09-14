package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransReducePolicyMapper;
import com.twfhclife.eservice_batch.model.TransReducePolicyVo;

public class TransReducePolicyDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransReducePolicyDao.class);
	
	public TransReducePolicyVo findById(TransReducePolicyVo transReducePolicyVo) {
		TransReducePolicyVo transReducePolicy = null;
		try {
			TransReducePolicyMapper transReducePolicyMapper = this.getSqlSession().getMapper(TransReducePolicyMapper.class);
			transReducePolicy = transReducePolicyMapper.findById(transReducePolicyVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transReducePolicy;
		
	}
	
	public List<TransReducePolicyVo> getTransReducePolicyList(TransReducePolicyVo transReducePolicyVo) {
		List<TransReducePolicyVo> transReducePolicyList = null;
		try {
			TransReducePolicyMapper transReducePolicyMapper = this.getSqlSession().getMapper(TransReducePolicyMapper.class);
			transReducePolicyList = transReducePolicyMapper.getTransReducePolicyList(transReducePolicyVo);
		} catch (Exception e) {
			logger.error("getTransReducePolicyList error:", e);
		} finally {
			this.release();
		}
		return transReducePolicyList;
	}
}
