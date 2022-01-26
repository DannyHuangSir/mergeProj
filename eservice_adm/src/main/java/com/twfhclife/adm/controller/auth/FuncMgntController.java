package com.twfhclife.adm.controller.auth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.SystemsVo;
import com.twfhclife.adm.service.IFuncMgntService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.api_client.FuncMgmtClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.generic.util.MenuUtil;

/**
 * 權限管理-功能維護.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class FuncMgntController extends BaseController {

	private static final Logger logger = LogManager.getLogger(FuncMgntController.class);

	@Autowired
	private IFuncMgntService funcMgntService;
	
	@Autowired
	FuncMgmtClient funcMgmtClient;

	/**
	 * 功能維護查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "191",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/funcMgnt")
	public String funcMgnt() {
		return "backstage/auth/funcMgnt";
	}
	
	@RequestLog
	@PostMapping("/funcMgnt/insertSystem")
	public ResponseEntity<ResponseObj> insertSystem(@RequestBody SystemsVo systemsVo) {
		try {
			systemsVo.setCreateUser(getUserId());
			int result = funcMgntService.addSystem(systemsVo);
				if (result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			
		} catch (Exception e) {
			if(e.getMessage().indexOf("ORA-00001") > 0 ) {
				//違反唯一值
				processError("系統代號不可重複");
			} else {
				logger.error("Unable to insertFunctions: {}", ExceptionUtils.getStackTrace(e));
				processSystemError();
			}
		}
		return processResponseEntity();
	}

	/**
	 * 取得系統功能.
	 * 
	 * @param functionVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.FUNCMGT_001, 
			sqlId = EventCodeConstants.FUNCMGT_001_SQL_ID,
			sqlParams = {
					@SqlParam(requestParamkey = "sysId", sqlParamkey = "sysId") 
			},
			systemEventParams = {}
			))
	@PostMapping("/funcMgnt/getFunctions")
	public ResponseEntity<ResponseObj> getFunctions(@RequestParam("sysId") String sysId) {
		try {
			// 取得該系統別下的所有功能
			List<FunctionItemVo> sysFunList = null;
			sysFunList = funcMgntService.getAllFuncBySysId(sysId);
			
			if (!CollectionUtils.isEmpty(sysFunList)) {
				processSuccess(new MenuUtil().convertAceTree(sysFunList, sysId));
			} else {
				logger.info("Unable to get function list by sysId: {}", sysId);
				processSuccessMsg("請先新增根目錄");
			}
		} catch (Exception e) {
			logger.error("Unable to getFunctions: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 新增節點
	 * 
	 * @param functionVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.FUNCMGT_002, 
			sqlId = EventCodeConstants.FUNCMGT_002_SQL_ID,
			daoVoParamKey = "functionVo",
			systemEventParams = {}
			))
	@PostMapping("/funcMgnt/insertFunctions")
	public ResponseEntity<ResponseObj> insertFunctions(@RequestBody FunctionItemVo functionVo) {
		try {
			functionVo.setCreateUser(getUserId());
			functionVo.setUpdateUser(getUserId());
			
			ReturnHeader header = funcMgmtClient.addFunction(functionVo);
			if(ReturnHeader.SUCCESS_CODE.equals(header.getReturnCode())) {
				processSuccessMsg("新增成功");
			} else {
				processError(header.getReturnMesg());
			}
			
//			if (funcMgntService.isFunctionNameExist(functionVo)) {
//				processError("功能名稱重覆");
//			} else {
//				int result = funcMgntService.addFunctionItem(functionVo);
//				if (result > 0) {
//					processSuccessMsg("新增成功");
//				} else {
//					processError("新增失敗");
//				}
//			}
		} catch (Exception e) {
			logger.error("Unable to insertFunctions: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 更新節點
	 * 
	 * @param functionVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.FUNCMGT_003, 
			sqlId = EventCodeConstants.FUNCMGT_003_SQL_ID,
			daoVoParamKey = "functionVo",
			systemEventParams = {}
			))
	@PostMapping("/funcMgnt/updateFunctions")
	public ResponseEntity<ResponseObj> updateFunctions(@RequestBody FunctionItemVo functionVo) {
		try {
			functionVo.setUpdateUser(getUserId());
			
			if (funcMgntService.isFunctionNameExist(functionVo)) {
				processError("功能名稱重覆");
			} else {
				int result = funcMgntService.updateFunctionItem(functionVo);
				if (result > 0) {
					processSuccessMsg("更新成功");
				} else {
					processError("更新失敗");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to updateFunctions: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 刪除節點
	 * 
	 * @param functionVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.FUNCMGT_004, 
			sqlId = EventCodeConstants.FUNCMGT_004_SQL_ID,
			sqlParams = {
					@SqlParam(requestParamkey = "functionId", sqlParamkey = "functionId") 
			},
			systemEventParams = {}
			))
	@PostMapping("/funcMgnt/deleteFunctions")
	public ResponseEntity<ResponseObj> deleteFunctions(@RequestBody FunctionItemVo functionVo) {
		try {
			int result = funcMgntService.deleteFunctionItem(functionVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteFunctions: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 功能匯出.
	 * 
	 * @param param
	 * @param response
	 * @throws IOException
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.FUNCMGT_005, 
			sqlId = EventCodeConstants.FUNCMGT_005_SQL_ID,
			sqlParams = {
					@SqlParam(requestParamkey = "param", sqlParamkey = "sysId") 
			},
			systemEventParams = {}
			))
	@RequestMapping(value = "/funcMgnt/downloadFunctionsCsv")
	public void downloadFunctionsCsv(@RequestParam("param") String param, HttpServletResponse response)
			throws IOException {
		Map<String, String> functionItmeMap = new HashMap<String, String>();
		Map<String, String> systemMap = new HashMap<String, String>();
		List<FunctionItemVo> listFuncs = funcMgntService.getAllFuncBySysId(param);

		// 開始準備產生csv
		String DATE_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String timeStr = sdf.format(c1.getTime());
		String csvFileName = "exportSystemFunctions_"+timeStr+".csv";// 預設的檔名

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setCharacterEncoding("utf-8");
		response.setHeader(headerKey, headerValue);
		CSVPrinter csvFilePrinter = null;

		// 定義標題列 FUNCTION_ID, FUNCTION_NAME, FUNCTION_TYPE, FUNCTION_URL, SORT, PARENT_FUNC_ID, SYS_ID, ACTIVE
		Object[] headers = { "功能名稱", "功能項目類型", "功能執行URL", "排序", "上層功能名稱", "系統代碼", "是否啟用" };
		try {
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
			response.getWriter().print('\ufeff'); //write BOM
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			//寫入header
			csvFilePrinter.printRecord(headers);

			// 寫入List每一行資料
			for (FunctionItemVo func : listFuncs) {
				csvFilePrinter.printRecord(func.getFunctionName(), "FG".equals(func.getFunctionType()) ? "功能群組" : "功能",
						func.getFunctionUrl(), func.getSort(), functionItmeMap.get(func.getParentFuncId()),
						systemMap.get(func.getSysId()), "Y".equals(func.getActive()) ? "是" : "否");
			}
			
			csvFilePrinter.flush();
			csvFilePrinter.close();
		} catch (Exception e) {
			logger.error("Unable to downloadFunctionsCsv: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
