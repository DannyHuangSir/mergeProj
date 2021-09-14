package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransPaymodeMapper;
import com.twfhclife.eservice_batch.model.TransPaymodeVo;

public class TransPaymodeDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransPaymodeDao.class);
	
	public TransPaymodeVo findById(TransPaymodeVo transPaymodeVo) {
		TransPaymodeVo transPaymode = null;
		try {
			TransPaymodeMapper transPaymodeMapper = this.getSqlSession().getMapper(TransPaymodeMapper.class);
			transPaymode = transPaymodeMapper.findById(transPaymodeVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transPaymode;
		
	}
	
	public List<TransPaymodeVo> getTransPaymodeList(TransPaymodeVo transPaymodeVo) {
		List<TransPaymodeVo> transPaymodeList = null;
		try {
			TransPaymodeMapper transPaymodeMapper = this.getSqlSession().getMapper(TransPaymodeMapper.class);
			transPaymodeList = transPaymodeMapper.getTransPaymodeList(transPaymodeVo);
		} catch (Exception e) {
			logger.error("getTransPaymodeList error:", e);
		} finally {
			this.release();
		}
		return transPaymodeList;
	}
	
	public TransPaymodeVo getTransPaymode(TransPaymodeVo transPaymodeVo) {
		List<TransPaymodeVo> transPaymodeList = this.getTransPaymodeList(transPaymodeVo);
		return transPaymodeList != null && transPaymodeList.size() > 0 ? transPaymodeList.get(0) : null;
	}
	
	public String getActiveDate(String policyNo) {
		String activeDate = "";
		try {
			TransPaymodeMapper transPaymodeMapper = this.getSqlSession().getMapper(TransPaymodeMapper.class);
			TransPaymodeVo vo = transPaymodeMapper.getActiveDate(policyNo);
			if (vo != null) {
				activeDate = vo.getActiveDate();
			}
		} catch (Exception e) {
			logger.error("getTransPaymodeList error:", e);
		} finally {
			this.release();
		}
		return activeDate;
	}
}
