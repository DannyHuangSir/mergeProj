package com.twfhclife.eservice_api.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.api.adm.domain.ParamCategoryAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterCategoryVo;

/**
 * 參數類型維護服務.
 * 
 * @author all
 */
public interface IParameterCategoryService {
	
	/**
	 * 參數類型維護-分頁查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳參數類型維護-分頁查詢結果
	 */
	public List<Map<String, String>> getParameterCategoryPageList(ParamCategoryRequestVo parameterCategoryVo);

	/**
	 * 參數類型維護-查詢結果總筆數.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳總筆數
	 */
	public int getParameterCategoryPageTotal(ParamCategoryRequestVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳查詢結果
	 */
	public List<ParameterCategoryVo> getParameterCategory(ParameterCategoryVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-新增.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	public int insertParameterCategory(ParamCategoryAddRequestVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-更新.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	public int updateParameterCategory(ParamCategoryUpdateRequestVo parameterCategoryVo);

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	public int deleteParameterCategory(ParamCategoryDelRequestVo parameterCategoryVo);
	
}
