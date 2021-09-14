package com.twfhclife.adm.controller.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.DownloadUserAuthCSVResponse;
import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.model.UserEntityVo;
import com.twfhclife.adm.service.IAuthorizationService;
import com.twfhclife.adm.service.IUserMgntService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;

/**
 * 權限查詢與報表.
 * 
 * @author all
 */
@Controller
public class AuthorizationController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private IAuthorizationService authorizationService;

	@Autowired
	private IUserMgntService userMgntService;

	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "198",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/permissionsSearch")
	public String permissionsSearch() {
		return "backstage/auth/permissionsSearch";
	}

	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "199",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/downloadUserAuthCSV")
	public String downloadUserAuthCSV() {
		return "backstage/auth/downloadUserAuthCSV";
	}

	/**
	 * 取得使用者權限查詢結果.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/autho/getUserAuthDetail")
	public ResponseEntity<PageResponseObj> getUserAuthDetail(@RequestBody UserEntityVo userEntityVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: UserEntityVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			userEntityVo.setId(getLoginUser().getId());
			List<Map<String, Object>> userAuthList = userMgntService.getUserEntityPageList(getUserId(), userEntityVo);
			
			// 設定使用者的系統跟角色權限清單
			authorizationService.setUserSystemRoleAuth(userAuthList);
			
			pageResp.setRows(userAuthList);
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(userMgntService.getUserEntityPageTotal(getUserId(), userEntityVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userEntityVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userEntityVo.getRows() - 1) / userEntityVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getUserEntityPageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * Download user-authorization CSV by system.
	 * 
	 * @param sysName
	 * @param response
	 */
	@RequestLog
	@RequestMapping(value = "/autho/downloadUserAuthCSV")
	public void downloadUserAuthCSV(@RequestParam("sysId") String sysId, HttpServletResponse response) {
		List<DownloadUserAuthCSVResponse> results = null;
		try {
			// query data
			results = authorizationService.downloadUserAuthCSV(sysId);

			// 準備產生csv
			String fileName = "帳號功能權限報表.csv";// 預設的檔名
			String csvFileName = new String(fileName.getBytes(), "ISO8859-1"); 
//			String headerKey = "Content-Disposition";
//			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

			response.setContentType("text/csv");
			response.setCharacterEncoding("utf-8");
//			response.setHeader(headerKey, headerValue);
			response.setHeader("Content-Disposition", "attachment;filename=" + csvFileName);
			// 定義標題列
			Object[] headers = { "使用者帳號", "使用者姓名", "功能名稱", "建立日期"};

			// Create the CSVFormat object with "\n" as a record delimiter
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			
			PrintWriter pw = response.getWriter();
			try {
				pw.print('\ufeff');// 加入BOM避免Excel亂碼問題
				CSVPrinter csvFilePrinter = new CSVPrinter(pw, csvFileFormat);

				// 寫入標題
				csvFilePrinter.printRecord(headers);

				// 寫入資料
				for (DownloadUserAuthCSVResponse result : results) {
					csvFilePrinter.printRecord(result.getUserAcct(), result.getFirstName(), result.getFunctionName(),
							MyStringUtil.getFormateStringFromDate(result.getCreateDate(), "yyyy/MM/dd"));
				}

				csvFilePrinter.flush();
				csvFilePrinter.close();
			} finally {
				pw.flush();
			    pw.close();
			}
			
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}