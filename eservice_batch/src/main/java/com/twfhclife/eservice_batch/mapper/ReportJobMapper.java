package com.twfhclife.eservice_batch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.ReportJobConditionVo;
import com.twfhclife.eservice_batch.model.ReportJobHistoryVo;
import com.twfhclife.eservice_batch.model.ReportJobVo;

public interface ReportJobMapper {

	/**
	 * 查詢輸入排程 ID 資料
	 * @param reportJobId
	 * @return
	 */
	public ReportJobVo getReportJob(@Param("reportJobId") Integer reportJobId);
	
	/**
	 * 查詢排程其他條件
	 * @param reportJobVo
	 * @return
	 */
	public List<ReportJobConditionVo> getReportCondition(@Param("reportJobVo") ReportJobVo reportJobVo);
	
	/**
	 * 紀錄排程執行結果
	 * @param reportJobHistoryVo
	 * @return
	 */
	public int insertReportHitory(@Param("reportJobHistoryVo") ReportJobHistoryVo reportJobHistoryVo);
	
	/**
	 * Report01 - 帳號登入統計報表查詢
	 * @param realmId
	 * @param dateRange
	 * @param username
	 * @param roleId
	 * @param deptId
	 * @param titleId
	 * @return
	 */
	public List<Map<String, Object>> queryReport01(@Param("realmId") String realmId, @Param("dateRange") Integer dateRange, @Param("userName") String username, @Param("roleId") String roleId, @Param("deptId") String deptId, @Param("titleId") String titleId);
	
	/**
	 * Report02 - 使用帳號清單報表查詢
	 * @param realmId
	 * @param username
	 * @param roleId
	 * @param deptId
	 * @param titleId
	 * @return
	 */
	public List<Map<String, Object>> queryReport02(@Param("realmId") String realmId, @Param("userName") String username, @Param("roleId") String roleId, @Param("deptId") String deptId, @Param("titleId") String titleId);
	
	/**
	 * Report03 - 各項作業使用統計報表查詢
	 * @param systemId
	 * @param dateRange
	 * @return
	 */
	public List<Map<String, Object>> queryReport03(@Param("systemId") String systemId, @Param("dateRange") Integer dateRange);
}
