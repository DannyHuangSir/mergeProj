package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransChangeAccountMapper;
import com.twfhclife.eservice_batch.model.TransChangeAccountVo;

public class TransChangeAccountDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransChangeAccountDao.class);
	
	public TransChangeAccountVo findById(TransChangeAccountVo transChangeAccountVo) {
		TransChangeAccountVo transChangeAccount = null;
		try {
			TransChangeAccountMapper transChangeAccountMapper = this.getSqlSession().getMapper(TransChangeAccountMapper.class);
			transChangeAccount = transChangeAccountMapper.findById(transChangeAccountVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transChangeAccount;
		
	}
	
	public List<TransChangeAccountVo> getTransChangeAccountList(TransChangeAccountVo transChangeAccountVo) {
		List<TransChangeAccountVo> transChangeAccountList = null;
		try {
			TransChangeAccountMapper transChangeAccountMapper = this.getSqlSession().getMapper(TransChangeAccountMapper.class);
			transChangeAccountList = transChangeAccountMapper.getTransChangeAccountList(transChangeAccountVo);
		} catch (Exception e) {
			logger.error("getTransChangeAccountList error:", e);
		} finally {
			this.release();
		}
		return transChangeAccountList;
	}
	
	public TransChangeAccountVo getTransChangeAccount(TransChangeAccountVo transChangeAccountVo) {
		List<TransChangeAccountVo> transChangeAccountList = this.getTransChangeAccountList(transChangeAccountVo);
		return transChangeAccountList != null && transChangeAccountList.size() > 0 ? transChangeAccountList.get(0) : null;
	}
	
	public String findRocId(String changeType, String policyNo) {
		String rocId = "";
		try {
			TransChangeAccountMapper transChangeAccountMapper = this.getSqlSession().getMapper(TransChangeAccountMapper.class);
			rocId = transChangeAccountMapper.findRocId(changeType, policyNo);
		} catch (Exception e) {
			logger.error("findRocId error:", e);
		} finally {
			this.release();
		}
		return rocId;
	}
}
