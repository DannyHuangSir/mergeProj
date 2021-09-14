package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransPolicyHolderProfileMapper;
import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;

public class TransPolicyHolderProfileDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransPolicyHolderProfileDao.class);
	
	public TransPolicyHolderProfileVo findById(TransPolicyHolderProfileVo vo) {
		TransPolicyHolderProfileVo transPolicyHolderProfileVo = null;
		try {
			TransPolicyHolderProfileMapper mapper = this.getSqlSession().getMapper(TransPolicyHolderProfileMapper.class);
			transPolicyHolderProfileVo = mapper.findById(vo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transPolicyHolderProfileVo;
		
	}
	
	public List<TransPolicyHolderProfileVo> getTransPolicyHolderProfileList(TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		List<TransPolicyHolderProfileVo> transPolicyHolderProfileList = null;
		try {
			TransPolicyHolderProfileMapper mapper = this.getSqlSession().getMapper(TransPolicyHolderProfileMapper.class);
			transPolicyHolderProfileList = mapper.getPolicyHolderProfileList(transPolicyHolderProfileVo);
		} catch (Exception e) {
			logger.error("getTransPolicyHolderProfileList error:", e);
		} finally {
			this.release();
		}
		return transPolicyHolderProfileList;
	}
	
	public TransPolicyHolderProfileVo getOldJobByTransNum(String transNum) {
		TransPolicyHolderProfileVo transPolicyHolderProfileVo = null;
		try {
			TransPolicyHolderProfileMapper mapper = this.getSqlSession().getMapper(TransPolicyHolderProfileMapper.class);
			transPolicyHolderProfileVo = mapper.getOldJobByTransNum(transNum);
		} catch (Exception e) {
			logger.error("getOldJobByTransNum error:", e);
		} finally {
			this.release();
		}
		return transPolicyHolderProfileVo;
		
	}
}
