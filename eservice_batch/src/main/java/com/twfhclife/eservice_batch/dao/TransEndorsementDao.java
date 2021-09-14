package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransEndorsementMapper;
import com.twfhclife.eservice_batch.model.EZAcquireVo;
import com.twfhclife.eservice_batch.model.TransEndorsementVo;

public class TransEndorsementDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransEndorsementDao.class);
	
	public int insertTransEndorsement(List<TransEndorsementVo> list) {
		int result = 0;
		try {
			TransEndorsementMapper mapper = this.getSqlSession().getMapper(TransEndorsementMapper.class);
			for (TransEndorsementVo vo : list) {
				try {
					result += mapper.insertTransEndorsement(vo);
				} catch (Exception e) {
					
				}
			}
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("insertTransEndorsement error:", e);
		} finally {
			this.release();
		}
		return result;
	}
	
	public String getPolicyHolderName(String transNum) {
		String name = "";
		try {
			TransEndorsementMapper mapper = this.getSqlSession().getMapper(TransEndorsementMapper.class);
			name = mapper.getPolicyHolderName(transNum.trim());
		} catch (Exception e) {
			logger.error("getPolicyHolderName error:", e);
		} finally {
			this.release();
		}
		return name;
	}
	
	public int insertTransEZ(EZAcquireVo vo) {
		int result = 0;
		try {
			TransEndorsementMapper mapper = this.getSqlSession().getMapper(TransEndorsementMapper.class);
			result = mapper.insertTransEZ(vo);
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("insertTransEZ error:", e);
		} finally {
			this.release();
		}
		return result;
	}
}
