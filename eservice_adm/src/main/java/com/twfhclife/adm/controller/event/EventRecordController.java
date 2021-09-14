package com.twfhclife.adm.controller.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.BusinessEventVo;
import com.twfhclife.adm.model.EventRecordVo;
import com.twfhclife.adm.model.SystemEventVo;
import com.twfhclife.adm.service.IEventRecordService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 紀錄管理.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class EventRecordController extends BaseController {

	private static final Logger logger = LogManager.getLogger(EventRecordController.class);

	@Autowired
	private IEventRecordService eventRecordService;

	@GetMapping("/businessEventDetail")
	public String businessEventDetail() {
		logger.info("open demo/businessEventDetail.html");
		return "backstage/businessEventDetail";
	}

	@GetMapping("/systemEventDetail")
	public String systemEventDetail() {
		logger.info("open demo/systemEventDetail.html");
		return "backstage/systemEventDetail";
	}

	@LoginCheck(value=@FuncUsageParam(
			funcId = "194",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/eventRecord")
	public String eventRecord() {
		logger.info("open demo/eventRecord.html");
		return "backstage/eventRecord";
	}

	@RequestLog
	@PostMapping("/eventRecord/initSelect")
	public ResponseEntity<ResponseObj> initSelect() {
		EventRecordVo result = eventRecordService.getSelectOption();
		this.setResponseObj(ResponseObj.SUCCESS, "", result);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@PostMapping("/eventRecord/getEventRecordTable")
	public ResponseEntity<ResponseObj> getEventRecordTable(@RequestBody BusinessEventVo vo) {
		logger.info("getEventRecordTable. sysId = "+vo.getTargetSystemId() +" userId = "+vo.getUserId() + 
				" eventName = "+vo.getEventName());
		List<BusinessEventVo> results = eventRecordService.getEventRecordTable(vo);
		this.setResponseObj(ResponseObj.SUCCESS, "", results);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@PostMapping("/eventRecord/getBusinessEventDetail")
	public ResponseEntity<ResponseObj> getBusinessEventDetail(@RequestBody String businessEventId) {
		BusinessEventVo result = eventRecordService.getBusinessEventDetail(businessEventId);
		this.setResponseObj(ResponseObj.SUCCESS, "", result);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@PostMapping("/eventRecord/getSystemEventDetail")
	public ResponseEntity<ResponseObj> getSystemEventDetail(@RequestBody String businessEventId) {
		List<SystemEventVo> results = eventRecordService.getSystemEventDetail(businessEventId);
		this.setResponseObj(ResponseObj.SUCCESS, "", results);
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}
	
	@RequestLog
	@RequestMapping("/eventRecord/downloadEventRecord")
	public void downloadEventRecord(@RequestParam("userId") String userId, @RequestParam("targetSystemId") String targetSystemId, 
			@RequestParam("eventName") String eventName, @RequestParam("eventCode") String eventCode,
			@RequestParam("eventStatus") String eventStatus, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, HttpServletResponse response) {
		/** 取得查詢資料*/
		BusinessEventVo vo = new BusinessEventVo();
		vo.setUserId(userId);
		vo.setTargetSystemId(targetSystemId);
		vo.setEventName(eventName);
		vo.setEventCode(eventCode);
		vo.setEventStatus(eventStatus);
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);

		List<BusinessEventVo> results = eventRecordService.getEventRecordTable(vo);

		// 開始準備產生csv
		String DATE_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String timeStr = sdf.format(c1.getTime());
		String csvFileName = "exportEventRecord_"+timeStr+".csv";// 預設的檔名

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setCharacterEncoding("utf-8");
		response.setHeader(headerKey, headerValue);
		CSVPrinter csvFilePrinter = null;

		// 定義標題列 SYS_ID, EVENT_DATE, ROLE_NAME, EVENT_CODE, EVENT_NAME, USER_ID
		Object[] headers = { "系統別", "紀錄時間", "事件代碼", "事件名稱", "帳號" };
		try {
			//Create the CSVFormat object with "\n" as a record delimiter

			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			//寫入header
			csvFilePrinter.printRecord(headers);

			// 寫入List每一行資料
			for (BusinessEventVo eventVo : results) {
				csvFilePrinter.printRecord(eventVo.getTargetSystemId(), eventVo.getEventDate(), 
						eventVo.getEventCode(), eventVo.getEventName(), eventVo.getUserId());
			}
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
