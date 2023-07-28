package com.twfhclife.adm.service.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.ParameterDao;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 參數維護服務.
 * 
 * @author all
 */
@Service
public class ParameterServiceImpl implements IParameterService {

	@Autowired
	private ParameterDao parameterDao;
	
	private Map<String, Map<String, Map<String, ParameterVo>>> sysParamCacheMap = new HashMap<>();
	/**
	 * 參數維護-分頁查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳參數維護-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getParameterPageList(ParameterVo parameterVo) {
		return parameterDao.getParameterPageList(parameterVo);
	}

	/**
	 * 參數維護-查詢結果總筆數.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getParameterPageTotal(ParameterVo parameterVo) {
		return parameterDao.getParameterPageTotal(parameterVo);
	}
	
	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<ParameterVo> getParameter(ParameterVo parameterVo) {
		return parameterDao.getParameter(parameterVo);
	}
	
	/**
	 * 用參數代碼取出參數.
	 * 
	 * @param systemId 系統別
	 * @param parameterCode 參數代碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public String getParameterValueByCode(String systemId, String parameterCode) {
		return parameterDao.getParameterValueByCode(systemId, parameterCode);
	}
	
	/**
	 * 用參數類別代碼取出所有以下的參數.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數類別代碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode) {
		return parameterDao.getParameterByCategoryCode(systemId, Arrays.asList(categoryCode));
	}
	
	/**
	 * 參數維護-新增.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int insertParameter(ParameterVo parameterVo) {
		return parameterDao.insertParameter(parameterVo);
	}
	
	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int updateParameter(ParameterVo parameterVo) {
		return parameterDao.updateParameter(parameterVo);
	}

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int deleteParameter(ParameterVo parameterVo) {
		return parameterDao.deleteParameter(parameterVo);
	}
	
	/**
	 * 取得參數的下拉選單資料.
	 * 
	 * @param systemId 系統代號
	 * @param categoryCode 參數種類類型代碼
	 * @return 回傳下拉選單資料.
	 */
	@RequestLog
	@Override
	public List<ParameterVo> getOptionList(String systemId, String categoryCode) {
		return parameterDao.getOptionList(systemId, categoryCode);
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
			List<ParameterVo> parameterList = parameterDao.getParameterByCategoryCode(systemId, Arrays.asList(categoryCode));
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
	/**
	 * 查詢醫療異常描述id值
	 * @param systemId
	 * @param medicalAbnormalReasonMsg
	 * @return
	 */
	@Override
	public String getParameterValueByCategoryCodeAndSystemId(String systemId, String medicalAbnormalReasonMsg,String rejectReason) {
		return parameterDao.getParameterValueByCategoryCodeAndSystemId(systemId,medicalAbnormalReasonMsg,rejectReason);
	}

	public List<String> getUserAccountStatusList() {
		return parameterDao.getUserAccountStatusList();
	}
}
