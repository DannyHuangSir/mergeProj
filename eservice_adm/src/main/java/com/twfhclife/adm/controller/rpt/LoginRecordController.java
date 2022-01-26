package com.twfhclife.adm.controller.rpt;

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
import com.twfhclife.adm.model.LoginLogVo;
import com.twfhclife.adm.service.ILoginRecordService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;

/**
 * 報表查詢-登入記錄查詢.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class LoginRecordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(LoginRecordController.class);

	@Autowired
	private ILoginRecordService loginRecordService;
	
	/**
	 * 登入記錄查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "189",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/loginRecord")
	public String loginRecord() {
		return "backstage/rpt/loginRecord";
	}
	
	/**
	 * 登入記錄查詢頁面-結果程式進入點.
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/loginRecordDetail")
	public String loginRecordDetail() {
		return "backstage/rpt/loginRecordDetail";
	}

	/**
	 * 取得登入記錄查詢結果.
	 * 
	 * @param auditLog AuditLogVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.LOGIN_RECORD, 
			sqlId = EventCodeConstants.LOGIN_RECORD_SQL_ID,
			daoVoParamKey = "loginLogVo",
			systemEventParams = {}
			))
	@PostMapping("/loginRecord/getLoginRecordDetail")
	public ResponseEntity<PageResponseObj> getLoginRecordDetail(@RequestBody LoginLogVo loginLogVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: LoginLogVo 需要繼承Pagination，接收 jquery datatable 的start 跟 length 屬性
			// 設定jquery datatable 資料查詢集合
			pageResp.setAaData(loginRecordService.getLoginRecordDetail(loginLogVo));
			// 設定jquery datatable 需要的資料總筆數
			pageResp.setiTotalDisplayRecords(loginRecordService.getLoginRecordDetailTotal(loginLogVo));
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getLoginRecordDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
}
