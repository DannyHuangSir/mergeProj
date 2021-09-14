package com.twfhclife.eservice_batch.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.OnlineChangeInfoMapper;
import com.twfhclife.eservice_batch.model.OnlineChangeInfoVo;

/**
 * onlinechange image file Dao.
 * 
 * @author all
 */
public class OnlineChangeInfoDao extends BaseDao {	
	
	private static final Logger logger = LogManager.getLogger(OnlineChangeInfoDao.class);
	
	public OnlineChangeInfoVo getOnlineChangeInfo(String rocId, String policyNo) {
		OnlineChangeInfoVo onlineChangeInfoVo = null;
		try {
			OnlineChangeInfoMapper onlineChangeInfoMapper = this.getSqlSession().getMapper(OnlineChangeInfoMapper.class);
			onlineChangeInfoVo = onlineChangeInfoMapper.getOnlineChangeInfo(rocId, policyNo);
		} catch (Exception e) {
			logger.error("getOnlineChangeInfo error:", e);
		} finally {
			this.release();
		}
		return onlineChangeInfoVo;
	}
	
	public OnlineChangeInfoVo getOnlineChangeInfoByTransNum(String transNum) {
		OnlineChangeInfoVo onlineChangeInfoVo = null;
		try {
			OnlineChangeInfoMapper onlineChangeInfoMapper = this.getSqlSession().getMapper(OnlineChangeInfoMapper.class);
			onlineChangeInfoVo = onlineChangeInfoMapper.getOnlineChangeInfoByTransNum(transNum);
		} catch (Exception e) {
			logger.error("getOnlineChangeInfo error:", e);
		} finally {
			this.release();
		}
		return onlineChangeInfoVo;
	}
	
}
