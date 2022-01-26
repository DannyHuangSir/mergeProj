package com.twfhclife.adm.controller.auth.base;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.JobTitleVo;
import com.twfhclife.adm.service.IJobTitleService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;

/**
 * 基本資料管理-職位管理.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class JobTitleController extends BaseController {

	private static final Logger logger = LogManager.getLogger(JobTitleController.class);

	@Autowired
	private IJobTitleService jobTitleService;
	
	/**
	 * 職位管理頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "301",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/jobTitleMgnt")
	public String loginRecord() {
		return "backstage/auth/base/jobTitleMgnt";
	}

	/**
	 * 取得職位管理查詢結果.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.JOBTITLEMGT_001, 
			sqlId = EventCodeConstants.JOBTITLEMGT_001_SQL_ID,
			daoVoParamKey = "jobTitleVo",
			systemEventParams = {}
			))
	@PostMapping("/jobTitleMgnt/getJobTitleDetail")
	public ResponseEntity<PageResponseObj> getJobTitleDetail(@RequestBody JobTitleVo vo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: JobTitleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(jobTitleService.getJobTitlePageList(vo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(jobTitleService.getJobTitlePageTotal(vo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(vo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getJobTitleDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 新增職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.JOBTITLEMGT_002, 
			sqlId = EventCodeConstants.JOBTITLEMGT_002_SQL_ID,
			daoVoParamKey = "jobTitleVo",
			systemEventParams = {}
			))
	@PostMapping("/jobTitleMgnt/insertJobTitle")
	public ResponseEntity<ResponseObj> insertJobTitle(@RequestBody JobTitleVo jobTitleVo) {
		try {
			jobTitleVo.setCreateUser(getUserId());
			jobTitleVo.setModifyUser(getUserId());
			
			if (jobTitleService.isJobTitleNameExist(jobTitleVo)) {
				processError("職位名稱重覆");
			} else {
				int result = jobTitleService.insertJobTitle(jobTitleVo);
				if (result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			}
			processSuccessMsg("新增成功");
		} catch (Exception e) {
			logger.error("Unable to insertJobTitle: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 更新職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.JOBTITLEMGT_003, 
			sqlId = EventCodeConstants.JOBTITLEMGT_003_SQL_ID,
			daoVoParamKey = "jobTitleVo",
			systemEventParams = {}
			))
	@PostMapping("/jobTitleMgnt/updateJobTitle")
	public ResponseEntity<ResponseObj> updateJobTitle(@RequestBody JobTitleVo jobTitleVo) {
		try {
			jobTitleVo.setModifyUser(getUserId());
			int result = jobTitleService.updateJobTitle(jobTitleVo);
			if (result > 0) {
				processSuccessMsg("更新成功");
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateJobTitle: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 刪除職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.JOBTITLEMGT_004, 
			sqlId = EventCodeConstants.JOBTITLEMGT_004_SQL_ID,
			daoVoParamKey = "jobTitleVo",
			systemEventParams = {}
			))
	@PostMapping("/jobTitleMgnt/deleteJobTitle")
	public ResponseEntity<ResponseObj> deleteJobTitle(@RequestBody JobTitleVo jobTitleVo) {
		try {
			int result = jobTitleService.deleteJobTitle(jobTitleVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteJobTitle: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
