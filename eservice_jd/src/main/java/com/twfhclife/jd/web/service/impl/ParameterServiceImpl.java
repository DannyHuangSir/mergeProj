package com.twfhclife.jd.web.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.jd.web.dao.ParameterDao;
import com.twfhclife.jd.web.model.ParameterVo;
import com.twfhclife.jd.web.service.IParameterService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterServiceImpl implements IParameterService {

	@Autowired
	private ParameterDao parameterDao;
	
	private Map<String, Map<String, Map<String, ParameterVo>>> sysParamCacheMap = new HashMap<>();
	
	/**
	 * 取得參數代碼清單.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數分類類型代碼
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getParameterList(String systemId, String categoryCode) {
		return parameterDao.getParameterList(systemId, categoryCode);
	}
	
	/**
	 * 取得系統所有使用的參數代碼.
	 * 
	 * Map<參數類別Code, Map<參數Code, 參數物件>>
	 * 
	 * @param systemId 系統別
	 * @return
	 */
	public Map<String, Map<String, ParameterVo>> getSystemParameter(String systemId) {
//		Map<String, Map<String, ParameterVo>> sysParamMap = sysParamCacheMap.get(systemId);
//		if (sysParamMap != null) {
//			return sysParamMap;
//		}
//		
		Map<String, Map<String, ParameterVo>> sysParamMap = new LinkedHashMap<>();
		List<String> categoryCodeList = parameterDao.getSystemCategoryCode(systemId);
		for (String categoryCode : categoryCodeList) {
			List<ParameterVo> parameterList = parameterDao.getParameterByCategoryCode(systemId, categoryCode);
			if (!CollectionUtils.isEmpty(parameterList)) {
				Map<String, ParameterVo> paramMap = new LinkedHashMap<>();
				for (ParameterVo parameterVo : parameterList) {
					paramMap.put(parameterVo.getParameterCode(), parameterVo);
				}
				sysParamMap.put(categoryCode, paramMap);
			}
		}
		sysParamCacheMap.put(systemId, sysParamMap);
		return sysParamMap;
	}
	
	@Override
	public String getParameterValueByCode(String sysId, String parameterCode){
		return parameterDao.getParameterValueByCode(sysId, parameterCode);
	}
	
	@Override
	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode){
		return parameterDao.getParameterByCategoryCode(systemId, categoryCode);
	}
	
	@Override
	public List<ParameterVo> getSecondFloorParameter(String systemId, 
			String categoryCode, String parentCategoryCode, String parameterCode){
		return parameterDao.getSecondFloorParameter(systemId, categoryCode, parentCategoryCode, parameterCode);
	}

	@Override
	public String getParameterByCategoryCodeParameterValue(String systemIdAdm, String medicalAbnormalReasonMsg, String reject_reason) {
		return parameterDao.getParameterByCategoryCodeParameterValue(systemIdAdm,medicalAbnormalReasonMsg, reject_reason);
	}

    @Override
    public ParameterVo getParameterByCode(String systemId, String parameterCode) {
		return parameterDao.getParameterByCode(systemId, parameterCode);
    }

    @Override
	public ParameterVo getParameterByParameterValue(String systemId, String categoryCode, String parameterValue) {
		// TODO Auto-generated method stub
		return parameterDao.getParameterByParameterValue(systemId,categoryCode, parameterValue);
	}
	
}
