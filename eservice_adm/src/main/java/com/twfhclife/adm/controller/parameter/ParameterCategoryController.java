package com.twfhclife.adm.controller.parameter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.ParameterCategoryVo;
import com.twfhclife.adm.service.IParameterCategoryService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.ParameterClient;
import com.twfhclife.generic.api_model.ParamCategoryRequestVo;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.keycloak.model.KeycloakUser;

/**
 * 參數管理-參數類型維護.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class ParameterCategoryController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ParameterCategoryController.class);

	@Autowired
	private IParameterCategoryService parameterCategoryService;

	@Autowired
	ParameterClient parameterClient;

	/**
	 * 參數類型維護查詢頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "200",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/parameterTypeSearch")
	public String parameterTypeSearch() {
		return "backstage/parameter/parameterTypeSearch";
	}

	/**
	 * 參數類型維護新增頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "201",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/parameterTypeAdd")
	public String parameterTypeAdd() {
		return "backstage/parameter/parameterTypeAdd";
	}

	/**
	 * 參數類型維護更新頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "",
			systemId = ApConstants.SYSTEM_ID
			))
	@PostMapping("/parameterTypeUpdate")
	public String parameterTypeUpdate() {
		return "backstage/parameter/parameterTypeUpdate";
	}

	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param vo
	 *            ParameterCategoryVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_006, 
			sqlId = EventCodeConstants.PARAMMGT_006_SQL_ID, 
			daoVoParamKey = "parameterCategoryVo",
			systemEventParams = {}
			))
	@PostMapping("/parameterType/getParameterCategoryPageList")
	public ResponseEntity<PageResponseObj> getParameterCategoryPageList(@RequestBody ParameterCategoryVo vo,
			HttpServletRequest request) {
		com.twfhclife.generic.api_model.PageResponseObj<Map<String, Object>> apiResp = new com.twfhclife.generic.api_model.PageResponseObj<>();
		PageResponseObj pageResp = new PageResponseObj();

		try {
			ParamCategoryRequestVo requestBody = new ParamCategoryRequestVo();
			BeanUtils.copyProperties(vo, requestBody);
			Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
			if (KeycloakUserObj != null) {
				KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
				requestBody.setUserId(keycloakUser.getUsername());
			}

			// Note: ParameterCategoryVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			if(this.isApiMode()) {
				apiResp = parameterClient.searchParamCategory(requestBody);// Call API取得資料
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(apiResp.getPageData());
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(apiResp.getTotalRecords());
			} else {
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(parameterCategoryService.getParameterCategoryPageList(vo));
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(parameterCategoryService.getParameterCategoryPageTotal(vo));
			}

			// 設定jqGrid 目前頁數
			pageResp.setPage(vo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getParameterPageList: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pageResp);
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 參數類型維護-新增.
	 * 
	 * @param parameterCategoryVo
	 *            ParameterCategoryVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_007, 
			sqlId = EventCodeConstants.PARAMMGT_007_SQL_ID, 
			daoVoParamKey = "parameterCategoryVo",
			systemEventParams = {}
			))
	@PostMapping("/parameterType/insertParameterCategory")
	public ResponseEntity<ResponseObj> insertParameterCategory(@RequestBody ParameterCategoryVo parameterCategoryVo) {
		try {
			parameterCategoryVo.setCreateUser(getUserId());
			parameterCategoryVo.setUpdateUser(getUserId());

			int result = parameterCategoryService.insertParameterCategory(parameterCategoryVo);
			if (result > 0) {
				processSuccessMsg("新增成功");
			} else {
				processError("新增失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to insertParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得當筆參數類型資料.
	 * 
	 * @param vo
	 *            ParameterCategoryVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/parameterType/getParameterCategoryById")
	public ResponseEntity<ResponseObj> getParameterCategoryById(@RequestBody ParameterCategoryVo vo) {
		try {
			processSuccess(parameterCategoryService.getParameterCategory(vo).get(0));
		} catch (Exception e) {
			logger.error("Unable to getParameterCategoryById: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 參數類型維護-更新.
	 * 
	 * @param parameterCategoryVo
	 *            ParameterCategoryVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_008, 
			sqlId = EventCodeConstants.PARAMMGT_008_SQL_ID, 
			daoVoParamKey = "parameterCategoryVo",
			systemEventParams = {}
			))
	@PostMapping("/parameterType/updateParameterCategory")
	public ResponseEntity<ResponseObj> updateParameterCategory(@RequestBody ParameterCategoryVo parameterCategoryVo) {
		try {
			parameterCategoryVo.setUpdateUser(getUserId());
			int result = parameterCategoryService.updateParameterCategory(parameterCategoryVo);
			if (result > 0) {
				processSuccessMsg("更新成功");
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo
	 *            ParameterCategoryVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_009, 
			sqlId = EventCodeConstants.PARAMMGT_009_SQL_ID, 
			daoVoParamKey = "parameterCategoryVo",
			systemEventParams = {}
			))
	@PostMapping("/parameterType/deleteParameterCategory")
	public ResponseEntity<ResponseObj> deleteParameterCategory(@RequestBody ParameterCategoryVo parameterCategoryVo) {
		try {
			int result = parameterCategoryService.deleteParameterCategory(parameterCategoryVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下載參數類型資料csv檔.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param systemId
	 * @param parameterCategoryId
	 * @param status
	 * @param response
	 * @throws IOException
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_010, 
			sqlId = EventCodeConstants.PARAMMGT_010_SQL_ID, 
			daoVoParamKey = "parameterCategoryVo",
			systemEventParams = {}
			))
	@RequestMapping(value = "/parameterType/downloadParameterCategoryCsv")
	public void downloadParameterCsv(@RequestParam(value = "stD", required = false) String startDate, @RequestParam(value = "enD", required = false) String endDate,
			@RequestParam("sy") String systemId, @RequestParam("co") Integer parameterCategoryId,
			@RequestParam("st") Integer status, HttpServletResponse response) throws IOException {
		ParameterCategoryVo vo = new ParameterCategoryVo();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if (!StringUtils.isEmpty(systemId)) {
			vo.setSystemId(systemId);
		}
		if (parameterCategoryId != null) {
			vo.setParameterCategoryId(parameterCategoryId);
		}
		if (status != null) {
			vo.setStatus(status);
		}
		List<ParameterCategoryVo> result = parameterCategoryService.getParameterCategory(vo);

		// 開始準備產生csv
		String DATE_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String timeStr = sdf.format(c1.getTime());
		String csvFileName = "exportParameterCategory_" + timeStr + ".csv"; // 預設的檔名

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setCharacterEncoding("utf-8");
		response.setHeader(headerKey, headerValue);
		CSVPrinter csvFilePrinter = null;

		// 定義標題列
		Object[] headers = { "系統別", "參數類型名稱", "參數類型代碼", "是否啟用" };
		try {
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			response.getWriter().print('\ufeff');
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			// 寫入header
			csvFilePrinter.printRecord(headers);

			// 寫入List每一行資料
			for (ParameterCategoryVo data : result) {
				csvFilePrinter.printRecord(data.getSysName(), data.getCategoryName(), data.getCategoryCode(),
						data.getStatusName());
			}
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error("Unable to downloadParameterCsv: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
