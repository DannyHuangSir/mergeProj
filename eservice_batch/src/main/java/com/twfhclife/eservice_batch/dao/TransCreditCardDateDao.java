package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransCreditCardDateMapper;
import com.twfhclife.eservice_batch.model.TransCreditCardDateVo;

public class TransCreditCardDateDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransCreditCardDateDao.class);
	
	public TransCreditCardDateVo findById(TransCreditCardDateVo transCreditCardDateVo) {
		TransCreditCardDateVo transCreditCardDate = null;
		try {
			TransCreditCardDateMapper transCreditCardDateMapper = this.getSqlSession().getMapper(TransCreditCardDateMapper.class);
			transCreditCardDate = transCreditCardDateMapper.findById(transCreditCardDateVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transCreditCardDate;
		
	}
	
	public List<TransCreditCardDateVo> getTransCreditCardDateList(TransCreditCardDateVo transCreditCardDateVo) {
		List<TransCreditCardDateVo> transCreditCardDateList = null;
		try {
			TransCreditCardDateMapper transCreditCardDateMapper = this.getSqlSession().getMapper(TransCreditCardDateMapper.class);
			transCreditCardDateList = transCreditCardDateMapper.getTransCreditCardDateList(transCreditCardDateVo);
		} catch (Exception e) {
			logger.error("getTransCreditCardDateList error:", e);
		} finally {
			this.release();
		}
		return transCreditCardDateList;
	}
	
	public TransCreditCardDateVo getTransCreditCardDate(TransCreditCardDateVo transCreditCardDateVo) {
		List<TransCreditCardDateVo> transCreditCardDateList = this.getTransCreditCardDateList(transCreditCardDateVo);
		return transCreditCardDateList != null && transCreditCardDateList.size() > 0 ? transCreditCardDateList.get(0) : null;
	}
}
