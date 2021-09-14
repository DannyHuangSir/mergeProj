package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.ParameterCategoryDao;
import com.twfhclife.adm.model.ParameterCategoryVo;
import com.twfhclife.adm.service.IParameterCategoryService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 參數類型維護服務.
 * 
 * @author all
 */
@Service
public class ParameterCategoryServiceImpl implements IParameterCategoryService {

	@Autowired
	private ParameterCategoryDao parameterCategoryDao;
	
	/**
	 * 參數類型維護-分頁查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳參數類型維護-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getParameterCategoryPageList(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.getParameterCategoryPageList(parameterCategoryVo);
	}

	/**
	 * 參數類型維護-查詢結果總筆數.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getParameterCategoryPageTotal(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.getParameterCategoryPageTotal(parameterCategoryVo);
	}
	
	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
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
	@RequestLog
	@Override
	public int insertParameterCategory(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.insertParameterCategory(parameterCategoryVo);
	}
	
	/**
	 * 參數類型維護-更新.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int updateParameterCategory(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.updateParameterCategory(parameterCategoryVo);
	}

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int deleteParameterCategory(ParameterCategoryVo parameterCategoryVo) {
		return parameterCategoryDao.deleteParameterCategory(parameterCategoryVo);
	}
}
