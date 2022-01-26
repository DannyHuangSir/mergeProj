package com.twfhclife.adm.controller.rpt;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.CommLogVo;
import com.twfhclife.adm.service.ICommLogService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_model.CommLogRequestVo;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.keycloak.model.KeycloakUser;

/**
 * 
 * 報表查詢-通知記錄查詢
 *
 */
@Controller
@EnableAutoConfiguration
public class CommLogController extends BaseController{

	private static final Logger logger = LogManager.getLogger(CommLogController.class);
	
	@Autowired
	private ICommLogService commLogService;

	/**
	 * 通知記錄查詢-頁面程式進入點.
	 * 
	 * @return String
	 */
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "501",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/commLog")
	public String commLog() {
		return "backstage/rpt/commLog";
	}
	
	@RequestLog
	@PostMapping("/commLog/getAllCommType")
	public ResponseEntity<ResponseObj> getAllCommType() {
		try {
			processSuccess(commLogService.getAllCommType());
		} catch (Exception e) {
			logger.error("Unable to getAllCommType: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@PostMapping("/commLog/getCommCntById")
	public ResponseEntity<ResponseObj> getCommCntById(
			@RequestParam(value = "id") String id,
			HttpServletRequest request) {
		try {
			String strId = null;
			if(id!=null) {
				strId = id;
			}
			
			List<String> list = commLogService.getCommCntById(strId);
			if(list!=null && !list.isEmpty()) {
				processSuccess(list.get(0));
			}
			
		} catch (Exception e) {
			logger.error("Unable to getCommCntById: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 通知記錄查詢-結果程式進入點.
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/commLogDetail")
	public String commLogDetail() {
		return "backstage/rpt/commLogDetail";
	}
	
	/**
	 * 
	 * @param commLogVo
	 * @return ResponseEntity
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.COMMLOG_001, 
			sqlId = EventCodeConstants.COMMLOG_001_SQL_ID,
			daoVoParamKey = "commLogVo",
			systemEventParams = {}
			))
	@PostMapping("/commLog/getCommLogDetail")
	public ResponseEntity<PageResponseObj> getCommLogDetail(
			@RequestBody CommLogVo vo,
			HttpServletRequest request) {
		com.twfhclife.generic.api_model.PageResponseObj<Map<String, Object>> apiResp = 
				new com.twfhclife.generic.api_model.PageResponseObj<>();
		PageResponseObj pageResp = new PageResponseObj();
		try {
			CommLogRequestVo requestBody = new CommLogRequestVo();
			
			BeanUtils.copyProperties(vo, requestBody);
			
			if(this.isApiMode()) {
				requestBody.setId(vo.getId());
				requestBody.setSystemId(vo.getSystemId());
				requestBody.setUserName(vo.getUserId());
				requestBody.setCommType(vo.getCommType());
				requestBody.setCommTarget(vo.getCommTarget());
				//requestBody.setCommDate(vo.getCommDate().toString());
				requestBody.setStartDate(vo.getStartDate());
				requestBody.setEndDate(vo.getEndDate());
				requestBody.setStart(vo.getStart());//分頁
				
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(apiResp.getPageData());
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(apiResp.getTotalRecords());
			
			}
			
			// Note: CommLogVo 需要繼承Pagination，接收 jquery datatable 的start 跟 length 屬性
			if(vo.getLength()<=0) {
				vo.setLength(10);
			}
			List<Map<String, Object>> listResult = commLogService.getCommLogDetail(vo);
			this.logger.info("after commLogService.getCommLogDetail");
			
			if(listResult==null || listResult.isEmpty()) {
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(null);
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(0);
				
				pageResp.setAaData(null);
				pageResp.setiTotalDisplayRecords(0);
			}else {
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(listResult);
				// 設定jqGrid 資料查詢總筆數
				int pageTotal = commLogService.getCommLogDetailPageTotal(vo);
				pageResp.setRecords(pageTotal);
				
				pageResp.setAaData(listResult);//設定jquery datatable 資料查詢集合
				pageResp.setiTotalDisplayRecords(pageTotal);//設定jquery datatable 需要的資料總筆數
			}
			
			// 設定jqGrid 目前頁數
			pageResp.setPage(vo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getCommLogDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
}
