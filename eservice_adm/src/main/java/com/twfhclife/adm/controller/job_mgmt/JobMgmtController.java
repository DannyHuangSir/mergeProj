package com.twfhclife.adm.controller.job_mgmt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.BusinessEventJobVo;
import com.twfhclife.adm.model.ReportJobHistoryVo;
import com.twfhclife.adm.model.ReportJobScheduleVo;
import com.twfhclife.adm.service.IJobMgmtService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 通信管理.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class JobMgmtController extends BaseController {

	private static final Logger logger = LogManager.getLogger(JobMgmtController.class);

	@Autowired
	private IJobMgmtService jobMgmtService;
	
	
	/**
	 * 工作管理-查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "404",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/realtimeLoginStat")
	public String realtimeLoginStat() {
		return "backstage/job_mgmt/realtimeLoginStat";
	}


	/**
	 * 取得即時登入資訊
	 * @return
	 */
	@RequestLog
	@PostMapping("/job/getRealtimeLoginStat")
	public ResponseEntity<ResponseObj> getRealtimeLoginStat() {
		Map<String, String> statsMap = new HashMap<>();
		try {
			statsMap = jobMgmtService.getRealtimeLoginStat();
			processSuccess(statsMap);
		} catch (Exception e) {
			logger.error("Unable to getRealtimeLoginStat: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-報表排程查詢頁面程式進入點.
	 * @return
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "402",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/reportJobMgmt")
	public String reportJobMgmtEntry() {
		return "backstage/job_mgmt/reportJobMgmt";
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>查詢已設定排程清單
	 * @param reportJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/getReportJobList")
	public ResponseEntity<PageResponseObj> getReportJobList(@RequestBody ReportJobScheduleVo reportJobScheduleVo){
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserDeputyVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(jobMgmtService.getReportJobList(reportJobScheduleVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(jobMgmtService.getReportJobPageTotal(reportJobScheduleVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(reportJobScheduleVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + reportJobScheduleVo.getRows() - 1) / reportJobScheduleVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch(Exception e) {
			logger.error("Unable to getReportJobList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>新增報表排程
	 * @param reportJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/addReportJobSchedule")
	public ResponseEntity<ResponseObj> addReportJobSchedule(@RequestBody ReportJobScheduleVo reportJobScheduleVo){
		try {
			reportJobScheduleVo.setCreateUser(getUserId());
			int result = jobMgmtService.insertReportJobSchedule(reportJobScheduleVo);
			if(result > 0) {
				jobMgmtService.processCrontab();
				processSuccess(null);
			} else {
				processError("排程建立失敗，請重新確認");
			}
		} catch(Exception e) {
			logger.error("Unable to addReportJobSchedule: {}" + ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>更新報表排程
	 * @param reportJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/updateReportJobSchedule")
	public ResponseEntity<ResponseObj> updateReportJobSchedule(@RequestBody ReportJobScheduleVo reportJobScheduleVo){
		try {
			reportJobScheduleVo.setModifyUser(getUserId());
			int result = jobMgmtService.updateReportJobSchedule(reportJobScheduleVo);
			if(result > 0) {
				jobMgmtService.processCrontab();
				processSuccessMsg(null);
			} else {
				processError("更新排程失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to updateReportJobSchedule: {}" + ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>歷史查詢
	 * @param reportJobScheduleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/getReportJobHistory")
	public ResponseEntity<PageResponseObj> getReportJobHistory(@RequestBody ReportJobHistoryVo reportJobHistoryVo){
		PageResponseObj pageResponseObj = new PageResponseObj();
		try {
			// 排程紀錄查詢
			// Note: UserDeputyVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResponseObj.setRows(jobMgmtService.queryReportJobHistory(reportJobHistoryVo));
			// 設定jqGrid 資料查詢總筆數
			pageResponseObj.setRecords(jobMgmtService.getReportJobHistoryPageTotal(reportJobHistoryVo));
			// 設定jqGrid 目前頁數
			pageResponseObj.setPage(reportJobHistoryVo.getPage());
			// 設定jqGrid 總頁數
			pageResponseObj.setTotal((pageResponseObj.getRecords() + reportJobHistoryVo.getRows() - 1) / reportJobHistoryVo.getRows());
			pageResponseObj.setResult(PageResponseObj.SUCCESS);
		} catch(Exception e) {
			logger.error("Unable to getReportJobHistory: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResponseObj);
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>刪除排程
	 * @param reportJobScheduleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/deleteReportJob")
	public ResponseEntity<ResponseObj> deleteReportJob(@RequestBody ReportJobScheduleVo reportJobScheduleVo){
		try {
			// 刪除排程
			int delResult = jobMgmtService.delReportJob(reportJobScheduleVo);
			if(delResult > 0) {
				jobMgmtService.processCrontab();
				processSuccess(null);
			} else {
				processError("刪除排程失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to deleteReportJob: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-報表排程
	 * <li>取出排程設定
	 * @param reportJobScheduleVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/reportJobMgmt/getReportJobSetting")
	public ResponseEntity<ResponseObj> getReportJobSetting(@RequestBody ReportJobScheduleVo reportJobScheduleVo){
		try {
			ReportJobScheduleVo outReportJobScheduleVo = jobMgmtService.queryReportJobDetailByReportJobId(reportJobScheduleVo);
			if(outReportJobScheduleVo != null) {
				processSuccess(outReportJobScheduleVo);
			} else {
				processError(null);
			}
		} catch(Exception e) {
			logger.error("Unable to getReportJboSetting: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@RequestMapping(path = "/reportJobMgmt/reportDownload", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadReport(@RequestParam("reportId") Integer reportId) {
		File report = null;
		ByteArrayResource resource = null;
		HttpHeaders headers = new HttpHeaders();
	    try {
	        String reportPath = jobMgmtService.queryDownloadUrlById(reportId);
	        report = new File(reportPath);
	        String outFilename = reportPath.substring(reportPath.lastIndexOf("\\") + 1, reportPath.length());
	        java.nio.file.Path path = Paths.get(report.getAbsolutePath());
	        resource = new ByteArrayResource(Files.readAllBytes(path));
	        headers.add(HttpHeaders.CONTENT_DISPOSITION
	        		, String.format("attachment; filename=%s", outFilename));
	    } catch (Exception e) {
	        logger.error("Unable to downloadReport: {}", ExceptionUtils.getStackTrace(e));
	        return ResponseEntity.badRequest().build();
	    }
	    return ResponseEntity.ok().headers(headers).contentLength(report.length())
	    		.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}
	
	/**
	 * 工作管理-事件通知程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "401",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/eventJob")
	public String eventJob() {
		return "backstage/job_mgmt/eventJob";
	}
	
	/**
	 * 工作管理-事件通知
	 * 事件類型查詢
	 * @param businussEventJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/getEventJobSelect")
	public ResponseEntity<ResponseObj> getEventJobSelect(@RequestBody BusinessEventJobVo businussEventJobVo){
		try {
			processSuccess(jobMgmtService.getEventJobSelect(businussEventJobVo.getSystemId()));
		} catch(Exception e) {
			logger.error("Unable to getEventJobSelect: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-事件通知
	 * <li>查詢已設定排程清單
	 * @param reportJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/getBusinessEventJobList")
	public ResponseEntity<PageResponseObj> getBusinessEventJobList(@RequestBody BusinessEventJobVo businessEventJobVo){
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserDeputyVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(jobMgmtService.getBusinessEventJobList(businessEventJobVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(jobMgmtService.getBusinessEventJobPageTotal(businessEventJobVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(businessEventJobVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + businessEventJobVo.getRows() - 1) / businessEventJobVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch(Exception e) {
			logger.error("Unable to getReportJobList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
	
	/**
	 * 工作管理-事件通知更新
	 * @param businessEventJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/updateEventJob")
	public ResponseEntity<ResponseObj> updateBusinessEventJob(@RequestBody BusinessEventJobVo businessEventJobVo){
		try {
			businessEventJobVo.setModifyUser(getUserId());
			boolean updateResult = jobMgmtService.updBusinessEventJob(businessEventJobVo);
			if(updateResult) {
				jobMgmtService.processCrontab();
				processSuccess(null);
			} else {
				processError("更新失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to updateBusinessEventJob: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-事件通知新增
	 * @param businessEventJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/addEventJob")
	public ResponseEntity<ResponseObj> addBusinessEventJob(@RequestBody BusinessEventJobVo businessEventJobVo){
		try {
			businessEventJobVo.setCreateUser(getUserId());
			boolean addResult = jobMgmtService.addBusinessEventJob(businessEventJobVo);
			if(addResult) {
				jobMgmtService.processCrontab();
				processSuccess(null);
			} else {
				processError("新增失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to addBusinessEventJob: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		} 
		return processResponseEntity();
	}
	
	/**
	 * 工作管理-事件通知刪除
	 * @param businessEventJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/delEventJob")
	public ResponseEntity<ResponseObj> delBusinessEventJob(@RequestBody BusinessEventJobVo businessEventJobVo){
		try {
			boolean delResult = jobMgmtService.delBusinessEventJob(businessEventJobVo);
			if(delResult) {
				jobMgmtService.processCrontab();
				processSuccess(null);
			} else {
				processError("刪除失敗");
			}
		} catch(Exception e) {
			logger.error("Unable to delBusinessEventJob: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 取得想要修改的事件通知
	 * @param businessEventJobVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/eventJob/getBusinessEventJob")
	public ResponseEntity<ResponseObj> getBusinessEventJobData(@RequestBody BusinessEventJobVo businessEventJobVo){
		try {
			processSuccess(jobMgmtService.getBusinessEventJobData(businessEventJobVo));
		} catch(Exception e) {
			logger.error("Unable to getBusinessEventJobData: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
