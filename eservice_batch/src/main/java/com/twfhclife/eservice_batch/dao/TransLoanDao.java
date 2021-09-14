package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransLoanMapper;
import com.twfhclife.eservice_batch.model.TransLoanVo;

public class TransLoanDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransLoanDao.class);
	
	public TransLoanVo findById(TransLoanVo transLoanVo) {
		TransLoanVo transLoan = null;
		try {
			TransLoanMapper transLoanMapper = this.getSqlSession().getMapper(TransLoanMapper.class);
			transLoan = transLoanMapper.findById(transLoanVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transLoan;
		
	}
	
	public List<TransLoanVo> getTransLoanList(TransLoanVo transLoanVo) {
		List<TransLoanVo> transLoanList = null;
		try {
			TransLoanMapper transLoanMapper = this.getSqlSession().getMapper(TransLoanMapper.class);
			transLoanList = transLoanMapper.getTransLoanList(transLoanVo);
		} catch (Exception e) {
			logger.error("getTransLoanList error:", e);
		} finally {
			this.release();
		}
		return transLoanList;
	}
}
