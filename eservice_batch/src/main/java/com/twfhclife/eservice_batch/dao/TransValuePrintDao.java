package com.twfhclife.eservice_batch.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransValuePrintMapper;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransValuePrintVo;

public class TransValuePrintDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransValuePrintDao.class);
	
	public TransValuePrintVo findById(TransValuePrintVo transValuePrintVo) {
		TransValuePrintVo transValuePrint = null;
		try {
			TransValuePrintMapper transValuePrintMapper = this.getSqlSession().getMapper(TransValuePrintMapper.class);
			transValuePrint = transValuePrintMapper.findById(transValuePrintVo);
		} catch (Exception e) {
			logger.error("findById error:", e);
		} finally {
			this.release();
		}
		return transValuePrint;
		
	}
	
	public List<TransValuePrintVo> getTransValuePrintList(TransValuePrintVo transValuePrintVo) {
		List<TransValuePrintVo> transValuePrintList = null;
		try {
			TransValuePrintMapper transValuePrintMapper = this.getSqlSession().getMapper(TransValuePrintMapper.class);
			transValuePrintList = transValuePrintMapper.getTransValuePrintList(transValuePrintVo);
		} catch (Exception e) {
			logger.error("getTransValuePrintList error:", e);
		} finally {
			this.release();
		}
		return transValuePrintList;
	}
	
	public TransValuePrintVo getTransValuePrint(TransValuePrintVo transValuePrintVo) {
		List<TransValuePrintVo> transValuePrintList = this.getTransValuePrintList(transValuePrintVo);
		return transValuePrintList != null && transValuePrintList.size() > 0 ? transValuePrintList.get(0) : null;
	}
	
	public Map<String, Object> getTransValuePrintInfoData(TransPolicyVo transPolicyVo){
		Map<String, Object> infoMap = null;
		try {
			TransValuePrintMapper transValuePrintMapper = this.getSqlSession().getMapper(TransValuePrintMapper.class);
			infoMap = transValuePrintMapper.getTransValuePrintInfoData(transPolicyVo);
		} catch(Exception e) {
			logger.error("getTransValuePrintInfoData error:", e);
		} finally {
			this.release();
		}
		return infoMap;
	}
	
	public String getProductNameByCode(String productCode) {
		String productName = null;
		try {
			TransValuePrintMapper transValuePrintMapper = this.getSqlSession().getMapper(TransValuePrintMapper.class);
			productName = transValuePrintMapper.getProductNameByCode(productCode);
		} catch(Exception e) {
			logger.error("getProductNameByCode error: ", e);
		} finally {
			this.release();
		}
		return productName;
	}
}
