package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransCertPrintMapper;
import com.twfhclife.eservice_batch.model.TransCertPrintVo;

public class TransCertPrintDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransCertPrintDao.class);
	
	public TransCertPrintVo findById(TransCertPrintVo transCertPrintVo) {
		TransCertPrintVo transCertPrint = null;
		try {
			TransCertPrintMapper transCertPrintMapper = this.getSqlSession().getMapper(TransCertPrintMapper.class);
			transCertPrint = transCertPrintMapper.findById(transCertPrintVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transCertPrint;
		
	}
	
	public List<TransCertPrintVo> getTransCertPrintList(TransCertPrintVo transCertPrintVo) {
		List<TransCertPrintVo> transCertPrintList = null;
		try {
			TransCertPrintMapper transCertPrintMapper = this.getSqlSession().getMapper(TransCertPrintMapper.class);
			transCertPrintList = transCertPrintMapper.getTransCertPrintList(transCertPrintVo);
		} catch (Exception e) {
			logger.error("getTransCertPrintList error:", e);
		} finally {
			this.release();
		}
		return transCertPrintList;
	}
	
	public TransCertPrintVo getTransCertPrint(TransCertPrintVo transCertPrintVo) {
		List<TransCertPrintVo> transCertPrintList = this.getTransCertPrintList(transCertPrintVo);
		return transCertPrintList != null && transCertPrintList.size() > 0 ? transCertPrintList.get(0) : null;
	}
}
