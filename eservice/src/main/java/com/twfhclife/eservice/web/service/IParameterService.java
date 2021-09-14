package com.twfhclife.eservice.web.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.web.model.ParameterVo;

public interface IParameterService {
	
	/**
	 * 取得參數代碼清單.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數分類類型代碼
	 * @return
	 */
	public List<Map<String, Object>> getParameterList(String systemId, String categoryCode);
	
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
	 * 取得參數代碼的值
	 * @param systemId
	 * @param parameterCode
	 * @return
	 */
	public String getParameterValueByCode(String systemId, String parameterCode);
	
	/**
	 * 取得參數類別下的所有參數
	 * @param systemId
	 * @param categoryCode
	 * @return
	 */
	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode);
	
	/**
	 * 取得參數類別下的所有參數名
	 * @param systemId
	 * @param categoryCode
	 * @param parameterValue
	 * @return
	 */
	public ParameterVo getParameterByParameterValue(String systemId, String categoryCode, String parameterValue);
	
	/**
	 * 取得階層式參數值
	 *  
	 * ex-地址舉例:
	 * 
	 * 參數類別 = 縣市 = A;
	 * A類別下的參數 = 台北市 = B;
	 * B參數所屬類別 = 地區 = C;
	 * C類別下參數 = 內湖區 = D;
	 * 
	 * @param systemId	系統Id (可以為空字串);
	 * @param categoryCode	該參數所屬的參數類別Code(ex:A)
	 * @param parentCategoryCode	該參數所屬上層參數類別的Code(ex:C)
	 * @param parameterCode		該參數所屬上層參數的Code(ex:B)
	 * @return List<ParameterVo>	所有第二層參數下的參數值(ex:List<D>)
	 */
	public List<ParameterVo> getSecondFloorParameter(String systemId, String categoryCode, String parentCategoryCode, String parameterCode);
			
}
