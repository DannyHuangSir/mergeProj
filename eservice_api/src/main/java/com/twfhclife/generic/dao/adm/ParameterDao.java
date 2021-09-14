package com.twfhclife.generic.dao.adm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.twfhclife.eservice.api.adm.domain.ParamAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;


/**
 * 參數維護 Dao.
 * 
 * @author all
 */
@Repository("apiParameterDao")
public interface ParameterDao {
	
	/**
	 * 參數維護-分頁查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳參數維護-分頁查詢結果
	 */
	List<Map<String, String>> getParameterPageList(@Param("parameterVo") ParamRequestVo parameterVo);

	/**
	 * 參數維護-查詢結果總筆數.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳總筆數
	 */
	int getParameterPageTotal(@Param("parameterVo") ParamRequestVo parameterVo);
	
	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳查詢結果
	 */
	List<ParameterVo> getParameter(@Param("parameterVo") ParamRequestVo parameterVo);
	
	/**
	 * 用參數代碼取出參數.
	 * 
	 * @param systemId 系統別
	 * @param parameterCode 參數代碼
	 * @return 回傳查詢結果
	 */
	String getParameterValueByCode(@Param("systemId") String systemId, @Param("parameterCode") String parameterCode);
	
	/**
	 * 用參數類別代碼取出所有以下的參數.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數類別代碼
	 * @return 回傳查詢結果
	 */
	List<ParameterVo> getParameterByCategoryCode(@Param("systemId") String systemId, @Param("categoryCode") String categoryCode);
	
	/**
	 * 參數維護-新增.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	int insertParameter(@Param("parameterVo") ParamAddRequestVo parameterVo);
	
	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	int updateParameter(@Param("parameterVo") ParamUpdateRequestVo parameterVo);

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo ParameterVo
	 * @return 回傳影響筆數
	 */
	int deleteParameter(@Param("parameterVo") ParamDelRequestVo parameterVo);
	
	/**
	 * 取得參數的下拉選單資料.
	 * 
	 * @param systemId 系統代號
	 * @param categoryCode 參數種類類型代碼
	 * @return 回傳下拉選單資料.
	 */
	List<ParameterVo> getOptionList(@Param("systemId") String systemId, @Param("categoryCode") String categoryCode);
	
	ParameterVo getParameterVoByCode(@Param("systemId") String systemId,@Param("parameterCode") String parameterCode,@Param("categoryCode") String categoryCode);
	
	/**
	  *  獲取流程狀態
	 * @param status
	 * @return String
	 */
	public String getStatusName(@Param("parentCategoryCode") String parentCategoryCode, @Param("status") String status);

	/**
	 * 獲取系統收件人信息
	 * @param pubstates
	 * @return List<ParameterVo>
	 */
	List<ParameterVo> getSendSYSMailInfo(@Param("pubstates") List<String> pubstates);

	public List<String> getRegionOption();
}
