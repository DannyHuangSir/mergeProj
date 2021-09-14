package com.twfhclife.adm.controller.msg;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.MessagingTemplateTmpVo;
import com.twfhclife.adm.model.MessagingTemplateVo;
import com.twfhclife.adm.service.IMessagingTemplateService;
import com.twfhclife.adm.service.IMessagingTemplateTmpService;
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
public class MessagingTemplateController extends BaseController {

	private static final Logger logger = LogManager.getLogger(MessagingTemplateController.class);

	@Autowired
	private IMessagingTemplateService messagingTemplateService;

	@Autowired
	private IMessagingTemplateTmpService messagingTemplateTmpService;
	
	/**
	 * 通信管理-查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "204",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/communicationTemplateSearch")
	public String communicationTemplateSearch() {
		return "backstage/msg/communicationTemplateSearch";
	}
	
	/**
	 * 通信管理-查詢明細頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/communicationTemplateDetail")
	public String communicationTemplateDetail() {
		return "backstage/msg/communicationTemplateDetail";
	}
	
	/**
	 * 通信管理-更新頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/communicationTemplateUpdate")
	public String communicationTemplateUpdate() {
		return "backstage/msg/communicationTemplateUpdate";
	}
	
	/**
	 * 通信管理-新增頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "205",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/communicationTemplateAdd")
	public String communicationTemplateAdd() {
		return "backstage/msg/communicationTemplateAdd";
	}
	
	/**
	 * 通信管理-審核頁面程式進入點.
	 * 
	 * @return
	 */
//	@RequestLog
//	@GetMapping("/communicationTempReview")
//	public String communctionTempReview() {
//		return "backstage/msg/communicationTempReview";
//	}
	
	/**
	 * 通信管理-審核頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/communicationTempReview")
	public ResponseEntity<String> communctionTempReview(@RequestParam(value = "messagingTemplateId") String messagingTemplateId,
			@RequestParam(value = "status") String status, @RequestParam(value = "type") String type,
			@RequestParam(value = "dateTime") String dateTime) {
		String res = null;
		try {
			MessagingTemplateVo vo = new MessagingTemplateVo();
			vo.setMessagingTemplateId(new BigDecimal(messagingTemplateId));
			vo.setStatus(status);
			vo.setType(type);
			vo.setStartDate(dateTime);
			res = messagingTemplateService.updateMessagingTemplateStatus(vo);

		} catch (Exception e) {
			logger.error("Unable to reviewMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("系統錯誤，請聯絡系統管理人員。(Exception:" + e.getMessage() + ")");
		}
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	/**
	 * 取得通信管理查詢結果.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/getMessagingTemplatePageList")
	public ResponseEntity<PageResponseObj> getMessagingTemplatePageList(@RequestBody MessagingTemplateVo messagingTemplateVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: MessagingTemplateVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(messagingTemplateService.getMessagingTemplatePageList(messagingTemplateVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(messagingTemplateService.getMessagingTemplatePageTotal(messagingTemplateVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(messagingTemplateVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + messagingTemplateVo.getRows() - 1) / messagingTemplateVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getMessagingTemplatePageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
	
	/**
	 * 取得當筆通信資料.
	 * 
	 * @param vo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/getMessagingTemplateById")
	public ResponseEntity<ResponseObj> getMessagingTemplateById(@RequestBody MessagingTemplateVo vo) {
		try {
			processSuccess(messagingTemplateService.getMessagingTemplate(vo).get(0));
		} catch (Exception e) {
			logger.error("Unable to getMessagingTemplateById: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 取得當筆通信TMP資料.
	 * 
	 * @param vo MessagingTemplateTmpVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/getMessagingTemplateTmpById")
	public ResponseEntity<ResponseObj> getMessagingTemplateTmpById(@RequestBody MessagingTemplateTmpVo vo) {
		try {
			processSuccess(messagingTemplateTmpService.getMessagingTemplateTmp(vo).get(0));
		} catch (Exception e) {
			logger.error("Unable to getMessagingTemplateTmpById: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 通信模版-停用.
	 * 
	 * @param vo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/stopMessagingTemplate")
	public ResponseEntity<ResponseObj> stopMessagingTemplate(@RequestBody MessagingTemplateVo vo) {
		try {
			vo.setUpdateUser(getUserId());
			int result = messagingTemplateService.stopMessagingTemplate(vo);
			if (result > 0) {
				processSuccessMsg("停用成功");
			} else {
				processError("停用失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to stopMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 通信模版-新增.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/insertMessagingTemplate")
	public ResponseEntity<ResponseObj> insertMessagingTemplate(@RequestBody MessagingTemplateVo messagingTemplateVo) {
		try {
			messagingTemplateVo.setCreateUser(getUserId());
			messagingTemplateVo.setUpdateUser(getUserId());
			
			boolean checkCode = messagingTemplateService.checkMessagingCode(messagingTemplateVo);
			if (checkCode) {
				processError("模版代碼已存在!");
			} else {
				int result = messagingTemplateService.insertMessagingTemplate(messagingTemplateVo);
				if (result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to insertMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 通信模版-更新.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/updateMessagingTemplate")
	public ResponseEntity<ResponseObj> updateMessagingTemplate(@RequestBody MessagingTemplateVo messagingTemplateVo) {
		try {
			messagingTemplateVo.setUpdateUser(getUserId());
			int result = messagingTemplateService.updateMessagingTemplate(messagingTemplateVo);
			if (result > 0) {
				processSuccessMsg("更新成功");
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 覆核作業.
	 * 
	 * @param vo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/communctionTemp/reviewMessagingTemplate")
	public ResponseEntity<ResponseObj> reviewMessagingTemplate(@RequestBody MessagingTemplateVo vo) {
		try {
			processSuccess(messagingTemplateService.updateMessagingTemplateStatus(vo));
		} catch (Exception e) {
			logger.error("Unable to reviewMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * CSV檔案下載.
	 * 
	 * @param systemId
	 * @param messagingTemplateCode
	 * @param messagingTemplateName
	 * @param messagingSubject
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param response
	 */
	@RequestLog
	@GetMapping("/communctionTemp/downloadMessagingTemplate")
	public void downloadMessagingTemplate(@RequestParam("systemId") String systemId,
			@RequestParam("messagingTemplateCode") String messagingTemplateCode, 
			@RequestParam("messagingTemplateName") String messagingTemplateName,
			@RequestParam("messagingSubject") String messagingSubject,
			@RequestParam("status") String status, @RequestParam("startDate") String startDate, 
			@RequestParam("endDate") String endDate, HttpServletResponse response) {
		CSVPrinter csvFilePrinter = null;
		Object[] headers = { "系統別", "通信模版代碼", "通信模版名稱", "狀態"};
		try {
			MessagingTemplateVo vo = new MessagingTemplateVo();
			vo.setSystemId(systemId);
			vo.setMessagingTemplateCode(messagingTemplateCode);
			vo.setMessagingTemplateName(messagingTemplateName);
			vo.setMessagingSubject(messagingSubject);
			vo.setStatus(status);
			vo.setStartDate(startDate);
			vo.setEndDate(endDate);
			
			// 開始準備產生csv
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar c1 = Calendar.getInstance(); // today
			
			String timeStr = sdf.format(c1.getTime());
			String csvFileName = "exportMessagingTemplate_" + timeStr + ".csv";// 預設的檔名
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			
			response.setContentType("text/csv");
			response.setCharacterEncoding("utf-8");
			response.setHeader(headerKey, headerValue);
			
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			csvFilePrinter.printRecord(headers);
			
			// 寫入List每一行資料
			List<MessagingTemplateVo> dataList = messagingTemplateService.getMessagingTemplate(vo);
			for (MessagingTemplateVo msvo : dataList) {
				csvFilePrinter.printRecord(msvo.getSystemId(), msvo.getMessagingTemplateCode(),
						msvo.getMessagingTemplateName(), msvo.getStatusName());
			}
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error("Unable to downloadMessagingTemplate: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
