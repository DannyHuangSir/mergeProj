package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransBeneficiaryOldMapper;
import com.twfhclife.eservice_batch.model.TransBeneficiaryOldVo;

/**
 * 變更受益人修改前 Dao.
 * 
 * @author all
 */
public class TransBeneficiaryOldDao extends BaseDao {
	
	private static final Logger logger = LogManager.getLogger(TransBeneficiaryOldDao.class);
	
	/**
	 * 變更受益人修改前-查詢.
	 * 
	 * @param transBeneficiaryOldVo TransBeneficiaryOldVo
	 * @return 回傳查詢結果
	 */
	
	public List<TransBeneficiaryOldVo> getTransBeneficiaryOldList(TransBeneficiaryOldVo transBeneficiaryOldVo) {
		List<TransBeneficiaryOldVo> transBeneficiaryOldList = null;
		try {
			TransBeneficiaryOldMapper transBeneficiaryOldMapper = this.getSqlSession().getMapper(TransBeneficiaryOldMapper.class);
			transBeneficiaryOldList = transBeneficiaryOldMapper.getTransBeneficiaryOldList(transBeneficiaryOldVo);
		} catch (Exception e) {
			logger.error("getTransBeneficiaryOldList error:", e);
		} finally {
			this.release();
		}
		return transBeneficiaryOldList;
	}
}