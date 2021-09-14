package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.ParameterCategoryVo;

/**
 * 參數類型維護 Dao.
 * 
 * @author all
 */
public interface ParameterCategoryDao {
	
	/**
	 * 參數類型維護-分頁查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳參數類型維護-分頁查詢結果
	 */
	List<Map<String, Object>> getParameterCategoryPageList(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);

	/**
	 * 參數類型維護-查詢結果總筆數.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳總筆數
	 */
	int getParameterCategoryPageTotal(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳查詢結果
	 */
	List<ParameterCategoryVo> getParameterCategory(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-新增.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	int insertParameterCategory(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);
	
	/**
	 * 參數類型維護-更新.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	int updateParameterCategory(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo ParameterCategoryVo
	 * @return 回傳影響筆數
	 */
	int deleteParameterCategory(@Param("parameterCategoryVo") ParameterCategoryVo parameterCategoryVo);
}
