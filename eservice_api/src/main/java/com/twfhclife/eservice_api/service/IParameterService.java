package com.twfhclife.eservice_api.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.api.adm.domain.ParamAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;

/**
 * 參數維護服務.
 * 
 * @author all
 */
public interface IParameterService {
	
	/**
	 * 參數維護-分頁查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳參數維護-分頁查詢結果
	 */
	public List<Map<String, String>> getParameterPageList(ParamRequestVo parameterVo);

	/**
	 * 參數維護-查詢結果總筆數.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳總筆數
	 */
	public int getParameterPageTotal(ParamRequestVo parameterVo);
	
	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳查詢結果
	 */
	public List<ParameterVo> getParameter(ParamRequestVo parameterVo);
	
	/**
	 * 用參數代碼取出參數.
	 * 
	 * @param systemId 系統別
	 * @param parameterCode 參數代碼
	 * @return 回傳查詢結果
	 */
	public String getParameterValueByCode(String systemId, String parameterCode);
	
	/**
	 * 用參數類別代碼取出所有以下的參數.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數類別代碼
	 * @return 回傳查詢結果
	 */
	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode);
	
	/**
	 * 參數維護-新增.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 * @throws Exception 
	 */
	public int insertParameter(ParamAddRequestVo parameterVo) throws Exception;
	
	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 * @throws Exception 
	 */
	public int updateParameter(ParamUpdateRequestVo parameterVo) throws Exception;

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	public int deleteParameter(ParamDelRequestVo parameterVo);
	
	/**
	 * 取得參數的下拉選單資料.
	 * 
	 * @param systemId 系統代號
	 * @param categoryCode 參數種類類型代碼
	 * @return 回傳下拉選單資料.
	 */
	public List<ParameterVo> getOptionList(String systemId, String categoryCode);
	
	
	public List<String> getRegionOption();
	
}
