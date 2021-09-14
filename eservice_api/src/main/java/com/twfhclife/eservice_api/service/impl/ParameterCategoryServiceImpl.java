package com.twfhclife.eservice_api.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.domain.ParamCategoryAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterCategoryVo;
import com.twfhclife.eservice_api.service.IParameterCategoryService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.dao.adm.ParameterCategoryDao;

/**
 * 參數類型維護服務.
 * 
 * @author all
 */
@Service
public class ParameterCategoryServiceImpl implements IParameterCategoryService {

	private static final Logger logger = LogManager.getLogger(ParameterCategoryServiceImpl.class);

	@Autowired
	private ParameterCategoryDao parameterCategoryDao;
	
	/**
	 * 參數類型維護-分頁查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳參數類型維護-分頁查詢結果
	 */
	@Override
	public List<Map<String, String>> getParameterCategoryPageList(ParamCategoryRequestVo parameterCategoryVo) {
		return parameterCategoryDao.getParameterCategoryPageList(parameterCategoryVo);
	}

	/**
	 * 參數類型維護-查詢結果總筆數.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳總筆數
	 */
	@Override
	public int getParameterCategoryPageTotal(ParamCategoryRequestVo parameterCategoryVo) {
		return parameterCategoryDao.getParameterCategoryPageTotal(parameterCategoryVo);
	}
	
	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<ParameterCategoryVo> getParameterCategory(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.getParameterCategory(parameterCategoryVo);
	}

	
	/**
	 * 參數類型維護-新增.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int insertParameterCategory(ParamCategoryAddRequestVo parameterCategoryVo) {
		return parameterCategoryDao.insertParameterCategory(parameterCategoryVo);
	}
	
	/**
	 * 參數類型維護-更新.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int updateParameterCategory(ParamCategoryUpdateRequestVo parameterCategoryVo) {
		return parameterCategoryDao.updateParameterCategory(parameterCategoryVo);
	}

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteParameterCategory(ParamCategoryDelRequestVo parameterCategoryVo) {
		return parameterCategoryDao.deleteParameterCategory(parameterCategoryVo);
	}
}
