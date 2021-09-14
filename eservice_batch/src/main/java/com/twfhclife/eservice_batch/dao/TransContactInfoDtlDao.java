package com.twfhclife.eservice_batch.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransContactInfoDtlMapper;
import com.twfhclife.eservice_batch.model.TransContactInfoDtlVo;

public class TransContactInfoDtlDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransContactInfoDtlDao.class);

	public  List<Map<String, Object>> getTransContactInfoDetailList( String transContactId){

		List<Map<String, Object>> transContactInfoDetailList = null;
		try {
			TransContactInfoDtlMapper transContactInfoDtlMapper = this.getSqlSession().getMapper(TransContactInfoDtlMapper.class);
			transContactInfoDetailList = transContactInfoDtlMapper.getTransContactInfoDetailList(transContactId);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transContactInfoDetailList;

	};


	public TransContactInfoDtlVo findById(TransContactInfoDtlVo transContactInfoDtlVo) {
		TransContactInfoDtlVo transContactInfoDtl = null;
		try {
			TransContactInfoDtlMapper transContactInfoDtlMapper = this.getSqlSession().getMapper(TransContactInfoDtlMapper.class);
			transContactInfoDtl = transContactInfoDtlMapper.findById(transContactInfoDtlVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transContactInfoDtl;
		
	}
	
	public List<TransContactInfoDtlVo> getTransContactInfoDtlList(TransContactInfoDtlVo transContactInfoDtlVo) {
		List<TransContactInfoDtlVo> transContactInfoDtlList = null;
		try {
			TransContactInfoDtlMapper transContactInfoDtlMapper = this.getSqlSession().getMapper(TransContactInfoDtlMapper.class);
			transContactInfoDtlList = transContactInfoDtlMapper.getTransContactInfoDtlList(transContactInfoDtlVo);
		} catch (Exception e) {
			logger.error("getTransContactInfoDtlList error:", e);
		} finally {
			this.release();
		}
		return transContactInfoDtlList;
	}
	
	public TransContactInfoDtlVo getTransContactInfoDtl(TransContactInfoDtlVo transContactInfoDtlVo) {
		List<TransContactInfoDtlVo> transContactInfoDtlList = this.getTransContactInfoDtlList(transContactInfoDtlVo);
		return transContactInfoDtlList != null && transContactInfoDtlList.size() > 0 ? transContactInfoDtlList.get(0) : null;
	}
}
