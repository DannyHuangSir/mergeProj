package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice_batch.mapper.ParameterMapper;
import com.twfhclife.eservice_batch.model.ParameterVo;

public class ParameterDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(ParameterDao.class);


	public String getStatusName(String var1, String var2) {
		String   atatusName = null;
		try {
			ParameterMapper transMapper = this.getSqlSession().getMapper(ParameterMapper.class);
			atatusName = transMapper.getStatusName(var1, var2);
		} catch (Exception e) {
			logger.error("getStatusName error:", e);
		} finally {
			this.release();
		}
		return atatusName;
	}


	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode) {
		List<ParameterVo> parameterList = null;
		try {
			ParameterMapper transMapper = this.getSqlSession().getMapper(ParameterMapper.class);
			parameterList = transMapper.getParameterByCategoryCode(systemId, categoryCode);
		} catch (Exception e) {
			logger.error("getParameterByCategoryCode error:", e);
		} finally {
			this.release();
		}
		return parameterList;
	}
	
	public String getParameterValueByCode(String systemId, String parameterCode) {
		String parameterValue = "";
		try {
			ParameterMapper mapper = this.getSqlSession().getMapper(ParameterMapper.class);
			ParameterVo parameter = mapper.getParameterValueByCode(systemId, parameterCode);
			if(parameter != null) {
//				if("Y".equals(parameter.getEncryptType())) {
//					parameterValue = EncryptionUtil.Decrypt(parameter.getParameterValue());
//				} else {
					parameterValue = parameter.getParameterValue();
//				}
			}
			logger.debug("getParameterValueByCode systemId:{}, parameterCode:{}, parameterValue:{}", systemId, parameterCode, parameterValue);
		} catch (Exception e) {
			logger.error("getParameterByCode error:", e);
		} finally {
			this.release();
		}
		return parameterValue;
	}
}
