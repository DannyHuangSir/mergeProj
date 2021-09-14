package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransRewardMapper;
import com.twfhclife.eservice_batch.model.TransRewardVo;

public class TransRewardDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransRewardDao.class);
	
	public TransRewardVo findById(TransRewardVo transRewardVo) {
		TransRewardVo transReward = null;
		try {
			TransRewardMapper transRewardMapper = this.getSqlSession().getMapper(TransRewardMapper.class);
			transReward = transRewardMapper.findById(transRewardVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transReward;
		
	}
	
	public List<TransRewardVo> getTransRewardList(TransRewardVo transRewardVo) {
		List<TransRewardVo> transRewardList = null;
		try {
			TransRewardMapper transRewardMapper = this.getSqlSession().getMapper(TransRewardMapper.class);
			transRewardList = transRewardMapper.getTransRewardList(transRewardVo);
		} catch (Exception e) {
			logger.error("getTransRewardList error:", e);
		} finally {
			this.release();
		}
		return transRewardList;
	}
	
	public TransRewardVo getTransReward(TransRewardVo transRewardVo) {
		List<TransRewardVo> transRewardList = this.getTransRewardList(transRewardVo);
		return transRewardList != null && transRewardList.size() > 0 ? transRewardList.get(0) : null;
	}
}
