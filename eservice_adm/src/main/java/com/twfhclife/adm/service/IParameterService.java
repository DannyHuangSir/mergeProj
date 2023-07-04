package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.ParameterVo;

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
	public List<Map<String, Object>> getParameterPageList(ParameterVo parameterVo);

	/**
	 * 參數維護-查詢結果總筆數.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳總筆數
	 */
	public int getParameterPageTotal(ParameterVo parameterVo);
	
	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳查詢結果
	 */
	public List<ParameterVo> getParameter(ParameterVo parameterVo);
	
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
	 */
	public int insertParameter(ParameterVo parameterVo);
	
	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	public int updateParameter(ParameterVo parameterVo);

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	public int deleteParameter(ParameterVo parameterVo);
	
	/**
	 * 取得參數的下拉選單資料.
	 * 
	 * @param systemId 系統代號
	 * @param categoryCode 參數種類類型代碼
	 * @return 回傳下拉選單資料.
	 */
	public List<ParameterVo> getOptionList(String systemId, String categoryCode);
	
	/**
	 * 取得系統所有使用的參數代碼.
	 * 
	 * Map<參數類別Code, Map<參數Code, 參數物件>>
	 * 
	 * @param systemId 系統別
	 * @return
	 */
	public Map<String, Map<String, ParameterVo>> getSystemParameter(String systemId);

	/**
	 * 查詢醫療異常描述id值
	 * @param systemId
	 * @param medicalAbnormalReasonMsg
	 * @return
	 */
    String getParameterValueByCategoryCodeAndSystemId(String systemId, String medicalAbnormalReasonMsg, String  rejectReason);

	List<String> getUserAccountStatusList();
}
