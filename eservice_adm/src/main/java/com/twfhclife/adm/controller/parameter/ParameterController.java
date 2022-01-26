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
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.ParameterClient;
import com.twfhclife.generic.api_model.ParamRequestVo;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.keycloak.model.KeycloakUser;

/**
 * 參數管理-參數維護.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class ParameterController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ParameterController.class);

	@Autowired
	private IParameterService parameterService;

	@Autowired
	ParameterClient parameterClient;
	
	/**
	 * 參數維護查詢頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "202",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/parameterSearch")
	public String parameterSearch() {
		return "backstage/parameter/parameterSearch";
	}

	/**
	 * 參數維護新增頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "203",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/parameterAdd")
	public String parameterAdd() {
		return "backstage/parameter/parameterAdd";
	}

	/**
	 * 參數維護更新頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "",
			systemId = ApConstants.SYSTEM_ID
			))
	@PostMapping("/parameterUpdate")
	public String parameterUpdate() {
		return "backstage/parameter/parameterUpdate";
	}

	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo ParameterVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_001, 
			sqlId = EventCodeConstants.PARAMMGT_001_SQL_ID,
			daoVoParamKey = "parameterVo",
			systemEventParams = {}
			))
	@PostMapping("/parameter/getParameterPageList")
	public ResponseEntity<PageResponseObj> getParameterPageList(@RequestBody ParameterVo vo,
			HttpServletRequest request) {
		com.twfhclife.generic.api_model.PageResponseObj<Map<String, Object>> apiResp = new com.twfhclife.generic.api_model.PageResponseObj<>();
		PageResponseObj pageResp = new PageResponseObj();
		try {
			ParamRequestVo requestBody = new ParamRequestVo();
			BeanUtils.copyProperties(vo, requestBody);
			Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
			if (KeycloakUserObj != null) {
				KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
				requestBody.setUserId(keycloakUser.getUsername());
			}
			// Note: ParameterVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			if(this.isApiMode()) {
				requestBody.setSystemId(vo.getSystemId());
				requestBody.setParameterCategoryId(vo.getParameterCategoryId());
				requestBody.setParameterCode(vo.getParameterCode());
				requestBody.setParameterName(vo.getParameterName());
				requestBody.setStatus(vo.getStatus());
				apiResp = parameterClient.searchParameter(requestBody);//TODO 打開即使用API取資料
				// 設定jqGrid 資料查詢集合
				pageResp.setRows(apiResp.getPageData());
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(apiResp.getTotalRecords());
			} else {
				List<Map<String, Object>>  paramList = parameterService.getParameterPageList(vo);
				// 設定jqGrid 資料查詢集合			
				pageResp.setRows(paramList);
				// 設定jqGrid 資料查詢總筆數
				pageResp.setRecords(parameterService.getParameterPageTotal(vo));
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
	 * 參數維護-新增.
	 * 
	 * @param parameterVo ParameterVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_002, 
			sqlId = EventCodeConstants.PARAMMGT_002_SQL_ID,
			daoVoParamKey = "parameterVo",
			systemEventParams = {}
			))
	@PostMapping("/parameter/insertParameter")
	public ResponseEntity<ResponseObj> insertParameter(@RequestBody ParameterVo parameterVo) {
		try {
			parameterVo.setCreateUser(getUserId());
			parameterVo.setUpdateUser(getUserId());
			
			if("Y".equals(parameterVo.getEncryptType())) {
				parameterVo.setParameterValue(EncryptionUtil.Encrypt(parameterVo.getParameterValue()));
			}
			int result = parameterService.insertParameter(parameterVo);
			if (result > 0) {
				processSuccessMsg("新增成功");
			} else {
				processError("新增失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to insertParameter: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 取得當筆參數資料.
	 * 
	 * @param vo ParameterVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/parameter/getParameterById")
	public ResponseEntity<ResponseObj> getParameterById(@RequestBody ParameterVo vo) {
		try {
			ParameterVo getParam = parameterService.getParameter(vo).get(0);
			if(getParam.getEncryptType()!=null && getParam.getEncryptType().equals("Y")) {
				//decrypt param value
				getParam.setParameterValue(EncryptionUtil.Decrypt(getParam.getParameterValue()));
			}
			processSuccess(getParam);
		} catch (Exception e) {
			logger.error("Unable to getParameterById: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo ParameterVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_003, 
			sqlId = EventCodeConstants.PARAMMGT_003_SQL_ID,
			daoVoParamKey = "parameterVo",
			systemEventParams = {}
			))
	@PostMapping("/parameter/updateParameter")
	public ResponseEntity<ResponseObj> updateParameter(@RequestBody ParameterVo parameterVo) {
		try {
			if(parameterVo.getEncryptType()!=null && parameterVo.getEncryptType().equals("Y")) {
				//encrypt param value
				parameterVo.setParameterValue(EncryptionUtil.Encrypt(parameterVo.getParameterValue()));
			}
			parameterVo.setUpdateUser(getUserId());
			int result = parameterService.updateParameter(parameterVo);
			if (result > 0) {
				processSuccessMsg("更新成功");
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateParameter: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo ParameterVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_004, 
			sqlId = EventCodeConstants.PARAMMGT_004_SQL_ID,
			daoVoParamKey = "parameterVo",
			systemEventParams = {}
			))
	@PostMapping("/parameter/deleteParameter")
	public ResponseEntity<ResponseObj> deleteParameter(@RequestBody ParameterVo parameterVo) {
		try {
			int result = parameterService.deleteParameter(parameterVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteParameter: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下載參數類型資料csv檔
	 * @param startDate
	 * @param endDate
	 * @param systemId
	 * @param categoryCode
	 * @param categoryName
	 * @param status
	 * @param response
	 * @throws IOException
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARAMMGT_005, 
			sqlId = EventCodeConstants.PARAMMGT_005_SQL_ID,
			daoVoParamKey = "parameterVo",
			systemEventParams = {}
			))
	@RequestMapping(value = "/parameter/downloadParameterCsv")
	public void downloadParameterCsv(@RequestParam(value = "stD", required = false) String startDate, @RequestParam(value = "enD", required = false) String endDate,
			@RequestParam("sy") String systemId, @RequestParam("ci") Integer categoryId,
			@RequestParam("pc") String parameterCode, @RequestParam("pn") String parameterName,
			@RequestParam("st") Integer status, HttpServletResponse response) throws IOException {
		ParameterVo vo = new ParameterVo();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if (!StringUtils.isEmpty(systemId)) {
			vo.setSystemId(systemId);
		}
		if (categoryId != null) {
			vo.setParameterCategoryId(categoryId);
		}
		if (!StringUtils.isEmpty(parameterCode)) {
			vo.setParameterCode(parameterCode);
		}
		if (!StringUtils.isEmpty(parameterName)) {
			vo.setParameterName(parameterName);
		}
		if (status != null) {
			vo.setStatus(status);
		}
		List<ParameterVo> result = parameterService.getParameter(vo);

		// 開始準備產生csv
		String DATE_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String timeStr = sdf.format(c1.getTime());
		String csvFileName = "exportParameter_" + timeStr + ".csv"; // 預設的檔名

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setCharacterEncoding("utf-8");
		response.setHeader(headerKey, headerValue);
		CSVPrinter csvFilePrinter = null;

		// 定義標題列
		Object[] headers = { "系統別", "參數類型名稱", "參數代碼", "參數名稱", "是否啟用" };
		try {
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			response.getWriter().print('\ufeff');
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			//寫入header
			csvFilePrinter.printRecord(headers);

			// 寫入List每一行資料
			for (ParameterVo data : result) {
				csvFilePrinter.printRecord(data.getSysName(), data.getCategoryName(), data.getParameterCode(),
						data.getParameterName(), data.getStatusName());
			}
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error("Unable to downloadParameterCsv: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
