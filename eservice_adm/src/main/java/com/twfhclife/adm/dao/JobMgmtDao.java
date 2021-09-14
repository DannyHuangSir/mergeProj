package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.BusinessEventJobVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.model.ReportJobConditionVo;
import com.twfhclife.adm.model.ReportJobHistoryVo;
import com.twfhclife.adm.model.ReportJobScheduleVo;

/**
 * 工作管理
 * @author Ken Wei
 *
 */
public interface JobMgmtDao {

	public int getReportJobSeq();
	
	/**
	 * 新增報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int insertReportJob(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 新增報表排程參數
	 * @param reportJobScheduleVo
	 * @param reportJobConditionVo
	 * @return
	 */
	public int insertReportJobCondition(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo, @Param("reportJobConditionVo") ReportJobConditionVo reportJobConditionVo);
	
	/**
	 * 取得報表排程結果(分頁)
	 * @param reportJobScheduleVo
	 * @return
	 */
	public List<Map<String, Object>> getReportJobList(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 取得報表排程分頁總筆數
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int getReportJobPageTotal(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 取得報表參數 BY reportJobId
	 * @param reportJobId
	 * @return
	 */
	public List<ReportJobConditionVo> getReportJobConditionByReportJobId(@Param("reportJobId") Integer reportJobId);
	
	/**
	 * 刪除報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int deleteReportJobByReportJobId(@Param("reportJobId") Integer reportJobId);
	
	/**
	 * 刪除報表排程參數
	 * @param reportJobId
	 * @return
	 */
	public int deleteReportJobConditionByReportJobId(@Param("reportJobId") Integer reportJobId);
	
	/**
	 * 取出報表排程設定
	 * @param reportJobId
	 * @return
	 */
	public List<ReportJobScheduleVo> getReportJobScheduleList(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 更新報表排程設定
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int updateReportJob(@Param("reportJobScheduleVo") ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 更新報表排成參數
	 * @param reportJobConditionVo
	 * @return
	 */
	public int updateReportJobCondition(@Param("reportJobConditionVo") ReportJobConditionVo reportJobConditionVo);
	
	/**
	 * 查詢報表已經設定參數
	 * @param reprotJobId
	 * @return
	 */
	public List<String> querySettingConditionKeyByReportJobId(@Param("reportJobId") Integer reprotJobId);
	
	/**
	 * 查詢報表歷史紀錄
	 * @param reportJobId
	 * @return
	 */
	public List<Map<String, Object>> getReportJobHistory(@Param("reportJobHistoryVo") ReportJobHistoryVo reportJobHistoryVo);
	
	/**
	 * 查詢報表歷史紀錄總筆數
	 * @param reportJobHistoryVo
	 * @return
	 */
	public int getReportJobHistoryPageTotal(@Param("reportJobHistoryVo") ReportJobHistoryVo reportJobHistoryVo);
	
	/**
	 * 取出所有排程命令
	 * @return
	 */
	public List<String> queryAllSettingReportJobCmd();
	
	/**
	 * 取出報表路徑
	 * @param reportId
	 * @return
	 */
	public String queryDownloadUrlById(@Param("reportId") Integer reportId);
	
	/**
	 * 刪除不被使用的 Condition 
	 * @param reportJobConditionVo
	 * @return
	 */
	public int deleteCondition(@Param("reportJobConditionVo") ReportJobConditionVo reportJobConditionVo);
	
	/**
	 * 取出被設定的事件類型
	 * @param systemId
	 * @return 
	 */
	public List<ParameterVo> getEvenJobSelect(@Param("systemId") String systemId);
	
	/**
	 * 取得事件通知排程結果(分頁)
	 * @param businessEventJobVo
	 * @return
	 */
	public List<Map<String, Object>> getBusinessEventJobList(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	
	/**
	 * 取得事件通知分頁總筆數
	 * @param businessEventJobVo
	 * @return
	 */
	public int getBusinessEventJobPageTotal(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	
	/**
	 * 取出 eventJobId seq
	 * @return
	 */
	public int getEventJobSeq();
	
	public BusinessEventJobVo getBusinessEventJobData(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	/**
	 * 新增事件通知
	 * @param businessEventJobVo
	 * @return
	 */
	public int insertBusinessEventJob(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	/**
	 * 更新事件通知
	 * @param businessEventJobVo
	 * @return
	 */
	public int updBusinessEventJob(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	/**
	 * 刪除事件通知
	 * @param businessEventJobVo
	 * @return
	 */
	public int delBusinessEventJob(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	
	public List<String> queryAllSettingBusinessEventJobCmd();
}
