package com.twfhclife.eservice.api.adm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.api.adm.model.FunctionItemVo;
import com.twfhclife.eservice.api.adm.service.IFuncMgntService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyStringUtil;

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

	/**
	 * 功能維護查詢頁面程式進入點.
	 * 
	 * @return
	 */
	@ApiRequest
	@GetMapping("/funcMgnt")
	public String funcMgnt() {
		return "backstage/auth/funcMgnt";
	}

	/**
	 * 取得系統功能.
	 * AA-003
	 * 
	 * @param functionVo
	 * @return
	 */
	@ApiRequest
	@GetMapping(value={"/{sysId}/function-items", "/funcMgnt/{sysId}/getFunctions"})
	public ResponseEntity<ApiResponseObj<?>> getFunctions(@PathVariable("sysId") String sysId) {
		ApiResponseObj<List<FunctionItemVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		List<FunctionItemVo> sysFunList = new ArrayList<>();
		try {
			// 取得該系統別下的所有功能
			sysFunList = funcMgntService.getAllFuncBySysId(sysId);
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setResult(sysFunList);
//			if (!CollectionUtils.isEmpty(sysFunList)) {
//				processSuccess(new MenuUtil().convertAceTree(sysFunList, sysId));
//			} else {
//				logger.info("Unable to get function list by sysId: {}", sysId);
//				processSuccessMsg("請先新增根目錄");
//			}
		} catch (Exception e) {
			logger.error("Unable to getFunctions: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 新增節點
	 * AA-007
	 * 
	 * @param functionVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value={"/function-item/create", "/funcMgnt/insertFunctions"})
	public ResponseEntity<?> insertFunctions(@Valid @RequestBody FunctionItemVo functionVo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/function-items/create:" + ApConstants.INVALID_SECRET + ", userId=" + functionVo.getCreateUser());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			if(MyStringUtil.isNullOrEmpty(functionVo.getCreateUser())) {
				Map<String, Object> error = Collections.singletonMap("error", "createUser cannot empty!");
				return ResponseEntity.badRequest().body(error);
			}
			
			if (funcMgntService.isFunctionNameExist(functionVo)) {
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "功能名稱重覆", "", "");
			} else {
				int result = funcMgntService.addFunctionItem(functionVo);
				if (result > 0) {
					returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				} else {
					returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "新增失敗", "", "");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to insertFunctions: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 更新節點
	 * AA-009
	 * 
	 * @param functionVo
	 * @return
	 */
	@ApiRequest
	@PutMapping(value={"/function-item/update", "/funcMgnt/updateFunctions"})
	public ResponseEntity<?> updateFunctions(@Valid @RequestBody FunctionItemVo functionVo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/function-items/create:" + ApConstants.INVALID_SECRET + ", userId=" + functionVo.getCreateUser());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			if(MyStringUtil.isNullOrEmpty(functionVo.getUpdateUser())) {
				Map<String, Object> error = Collections.singletonMap("error", "updateUser cannot empty!");
				return ResponseEntity.badRequest().body(error);
			}
			
			if (funcMgntService.isFunctionNameExist(functionVo)) {
				//processError("功能名稱重覆");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "功能名稱重覆", "", "");
			} else {
				int result = funcMgntService.updateFunctionItem(functionVo);
				if (result > 0) {
					//processSuccessMsg("更新成功");
					returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				} else {
					//processError("更新失敗");
					returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "更新失敗", "", "");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to updateFunctions: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * AA-010
	 * 刪除節點
	 * 
	 * @param functionVo
	 * @return
	 */
	@ApiRequest
	@DeleteMapping(value={"/function-item/{functionId}", "/funcMgnt/deleteFunctions/{functionId}"})
	public ResponseEntity<?> deleteFunctions(@PathVariable("functionId") String functionId, @RequestBody String updateUser,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj apiResponseObj = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/function-items/create:" + ApConstants.INVALID_SECRET + ", updateUser=" + updateUser);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			if(MyStringUtil.isNullOrEmpty(updateUser)) {
				Map<String, Object> error = Collections.singletonMap("error", "updateUser cannot empty!");
				return ResponseEntity.badRequest().body(error);
			}
			FunctionItemVo functionVo = new FunctionItemVo();
			functionVo.setFunctionId(functionId);
			logger.info("function-item delete: updateUser=" + updateUser);
			int result = funcMgntService.deleteFunctionItem(functionVo);
			if (result > 0) {
				//processSuccessMsg("刪除成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			} else {
				//processError("刪除失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "刪除失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteFunctions: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 功能匯出(請用getFunctions取得匯出資料).
	 * 
	 * @param param
	 * @param response
	 * @throws IOException
	 */
//	@RequestMapping(value = "/funcMgnt/downloadFunctionsCsv")
//	public void downloadFunctionsCsv(@RequestParam("param") String param, HttpServletResponse response)
//			throws IOException {
//		Map<String, String> functionItmeMap = new HashMap<String, String>();
//		Map<String, String> systemMap = new HashMap<String, String>();
//		List<FunctionItemVo> listFuncs = funcMgntService.getAllFuncBySysId(param);
//
//		// 開始準備產生csv
//		String DATE_FORMAT = "yyyyMMddHHmmss";
//		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//		Calendar c1 = Calendar.getInstance(); // today
//		String timeStr = sdf.format(c1.getTime());
//		String csvFileName = "exportSystemFunctions_"+timeStr+".csv";// 預設的檔名
//
//		response.setContentType("text/csv");
//		String headerKey = "Content-Disposition";
//		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
//		response.setCharacterEncoding("utf-8");
//		response.setHeader(headerKey, headerValue);
//		CSVPrinter csvFilePrinter = null;
//
//		// 定義標題列 FUNCTION_ID, FUNCTION_NAME, FUNCTION_TYPE, FUNCTION_URL, SORT, PARENT_FUNC_ID, SYS_ID, ACTIVE
//		Object[] headers = { "功能名稱", "功能項目類型", "功能執行URL", "排序", "上層功能名稱", "系統代碼", "是否啟用" };
//		try {
//			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ApConstants.NEW_LINE_SEPARATOR);
//			response.getWriter().print('\ufeff'); //write BOM
//			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
//			//寫入header
//			csvFilePrinter.printRecord(headers);
//
//			// 寫入List每一行資料
//			for (FunctionItemVo func : listFuncs) {
//				csvFilePrinter.printRecord(func.getFunctionName(), "FG".equals(func.getFunctionType()) ? "功能群組" : "功能",
//						func.getFunctionUrl(), func.getSort(), functionItmeMap.get(func.getParentFuncId()),
//						systemMap.get(func.getSysId()), "Y".equals(func.getActive()) ? "是" : "否");
//			}
//			
//			csvFilePrinter.flush();
//			csvFilePrinter.close();
//		} catch (Exception e) {
//			logger.error("Unable to downloadFunctionsCsv: {}", ExceptionUtils.getStackTrace(e));
//		}
//	}
	
	
}
