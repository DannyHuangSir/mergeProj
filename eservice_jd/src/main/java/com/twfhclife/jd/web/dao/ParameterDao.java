package com.twfhclife.jd.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.jd.web.model.ParameterVo;

public interface ParameterDao {

	/**
	 * 取得參數代碼清單.
	 * 
	 * @param systemId 系統別
	 * @param categoryCode 參數分類類型代碼
	 * @return
	 */
	public List<Map<String, Object>> getParameterList(@Param("systemId") String systemId,
			@Param("categoryCode") String categoryCode);
	
	/**
	 * 取得系統使用的參數類型代碼.
	 * 
	 * @param systemId 系統別
	 * @return
	 */
	public List<String> getSystemCategoryCode(@Param("systemId") String systemId);
	
	/**
	 * 由Code取Value
	 * @param parameterCode
	 * @return
	 */
	String getParameterValueByCode(@Param("systemId") String systemId, @Param("parameterCode") String parameterCode);
	
	/**
	 * 取得模板類型代碼下的所有parameter
	 * @param categoryCode
	 * @param parameterValue
	 * @return
	 */
	public ParameterVo getParameterByParameterValue(@Param("systemId") String systemId, @Param("categoryCode") String categoryCode, @Param("parameterValue") String parameterValue);
	
	/**
	 * 取得模板類型代碼下的所有parameter
	 * @param categoryCode
	 * @return
	 */
	public List<ParameterVo> getParameterByCategoryCode(@Param("systemId") String systemId, @Param("categoryCode") String categoryCode);
	
	/**
	 * 階層式取參數-從第一階參數取第二階層parameter
	 * @param categoryCode
	 * @return
	 */
	public List<ParameterVo> getSecondFloorParameter(@Param("sysId") String systemId, @Param("categoryCode") String categoryCode, @Param("parentCategoryCode") String parentCategoryCode, @Param("parameterCode") String parameterCode);

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
	public List<ParameterVo> getSendSYSMailInfo(@Param("pubstates") List<String> pubstates);

	/**
	 * 查詢名稱
	 * @param systemIdAdm
	 * @param medicalAbnormalReasonMsg
	 * @param reject_reason
	 * @return
	 */
    String getParameterByCategoryCodeParameterValue(@Param("systemId") String systemIdAdm, @Param("medicalAbnormal") String medicalAbnormalReasonMsg,@Param("reject")  String reject_reason);

	ParameterVo getParameterByCode(@Param("sysId") String systemId, @Param("parameterCode") String parameterCode);
}
