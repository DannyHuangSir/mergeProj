package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransResendPolicyMapper;
import com.twfhclife.eservice_batch.model.TransResendPolicyVo;

public class TransResendPolicyDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransResendPolicyDao.class);
	
	public TransResendPolicyVo findById(TransResendPolicyVo transResendPolicyVo) {
		TransResendPolicyVo transResendPolicy = null;
		try {
			TransResendPolicyMapper transResendPolicyMapper = this.getSqlSession().getMapper(TransResendPolicyMapper.class);
			transResendPolicy = transResendPolicyMapper.findById(transResendPolicyVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transResendPolicy;
		
	}
	
	public List<TransResendPolicyVo> getTransResendPolicyList(TransResendPolicyVo transResendPolicyVo) {
		List<TransResendPolicyVo> transResendPolicyList = null;
		try {
			TransResendPolicyMapper transResendPolicyMapper = this.getSqlSession().getMapper(TransResendPolicyMapper.class);
			transResendPolicyList = transResendPolicyMapper.getTransResendPolicyList(transResendPolicyVo);
		} catch (Exception e) {
			logger.error("getTransResendPolicyList error:", e);
		} finally {
			this.release();
		}
		return transResendPolicyList;
	}
	
	public TransResendPolicyVo getTransResendPolicy(TransResendPolicyVo transResendPolicyVo) {
		List<TransResendPolicyVo> transResendPolicyList = this.getTransResendPolicyList(transResendPolicyVo);
		return transResendPolicyList != null && transResendPolicyList.size() > 0 ? transResendPolicyList.get(0) : null;
	}
}
