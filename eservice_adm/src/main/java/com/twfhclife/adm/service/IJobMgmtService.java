package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.BusinessEventJobVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.model.ReportJobConditionVo;
import com.twfhclife.adm.model.ReportJobHistoryVo;
import com.twfhclife.adm.model.ReportJobScheduleVo;

/**
 * 職位管理服務.
 * 
 * @author all
 */
public interface IJobMgmtService {

	public Map<String, String> getRealtimeLoginStat() throws Exception;
	
	/**
	 * 工作管理-報表排程查詢
	 * <p>單一指定報表或者全部查詢
	 * @param reportJobVo
	 * @return
	 */
	public List<Map<String, Object>> getReportJobList(ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 工作管理-報表排程查詢總筆數
	 * @param reportJobVo
	 * @return
	 */
	public int getReportJobPageTotal(ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 工作管理-新增報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int insertReportJobSchedule(ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 工作管理-更新報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int updateReportJobSchedule(ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 掛上 crontab 排程
	 * <ol>
	 * <li>更新異動執行完成後一定要執行做排程更新
	 * <li>此一服務會更新所有 crontab
	 * </ol>
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int processCrontab();
	
	/**
	 * 工作管理-刪除報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public int delReportJob(ReportJobScheduleVo reportJobScheduleVo);
	
	/**
	 * 工作管理-查詢報表排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	public ReportJobScheduleVo queryReportJobDetailByReportJobId(ReportJobScheduleVo reportJobScheduleVo);

	/**
	 * 工作管理-查詢報表排程執行紀錄
	 * @param reportJobScheduleVo
	 * @return
	 */
	public List<Map<String, Object>> queryReportJobHistory(ReportJobHistoryVo reportJobHistoryVo);
	
	/**
	 * 工作管理-查詢報表排程執行紀錄總筆數
	 * @param reportJobHistoryVo
	 * @return
	 */
	public int getReportJobHistoryPageTotal(ReportJobHistoryVo reportJobHistoryVo);
	
	/**
	 * 工作管理-查詢報表執行結果檔案路徑
	 * @param reportId
	 * @return
	 */
	public String queryDownloadUrlById(Integer reportId);
	
	/**
	 * 工作管理-事件通知
	 * 取出事件類型
	 * @param systemId
	 * @return
	 */
	public List<ParameterVo> getEventJobSelect(String systemId);
	
	/**
	 * 工作管理-事件通知查詢
	 * <p>單一指定報表或者全部查詢
	 * @param businessEventJobVo
	 * @return
	 */
	public List<Map<String, Object>> getBusinessEventJobList(BusinessEventJobVo businessEventJobVo);
	
	public BusinessEventJobVo getBusinessEventJobData(BusinessEventJobVo businessEventJobVo);
	/**
	 * 工作管理-事件通知查詢總筆數
	 * @param businessEventJobVo
	 * @return
	 */
	public int getBusinessEventJobPageTotal(BusinessEventJobVo businessEventJobVo);
	
	/**
	 * 工作管理-事件通知新增
	 * @param businessEventJobVo
	 * @return
	 */
	public boolean addBusinessEventJob(BusinessEventJobVo businessEventJobVo);
	/**
	 * 工作管理-事件通知刪除
	 * @param businessEventJobVo
	 * @return
	 */
	public boolean delBusinessEventJob(BusinessEventJobVo businessEventJobVo);
	/**
	 * 工作管理-事件通知更新
	 * @param businessEventJobVo
	 * @return
	 */
	public boolean updBusinessEventJob(BusinessEventJobVo businessEventJobVo);
	
}