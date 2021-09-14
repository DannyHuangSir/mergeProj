package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransPolicyMapper;
import com.twfhclife.eservice_batch.model.TransPolicyVo;

public class TransPolicyDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransPolicyDao.class);
	
	public TransPolicyVo findById(TransPolicyVo transPolicyVo) {
		TransPolicyVo transPolicy = null;
		try {
			TransPolicyMapper transPolicyMapper = this.getSqlSession().getMapper(TransPolicyMapper.class);
			transPolicy = transPolicyMapper.findById(transPolicyVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transPolicy;
		
	}
	
	public List<TransPolicyVo> getTransPolicyList(TransPolicyVo transPolicyVo) {
		List<TransPolicyVo> transPolicyList = null;
		try {
			TransPolicyMapper transPolicyMapper = this.getSqlSession().getMapper(TransPolicyMapper.class);
			transPolicyList = transPolicyMapper.getTransPolicyList(transPolicyVo);
		} catch (Exception e) {
			logger.error("getTransPolicyList error:", e);
		} finally {
			this.release();
		}
		return transPolicyList;
	}
}
