package com.twfhclife.eservice.api.adm.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.adm.domain.ParamCategoryAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamCategoryUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterCategoryVo;
import com.twfhclife.eservice_api.service.IParameterCategoryService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.PageResponseObj;
import com.twfhclife.generic.domain.ResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyStringUtil;

/**
 * 參數管理-參數類型維護.
 * 
 * @author all
 */
@RestController
// @RequestMapping("/eservice_adm/parameterType")
public class ParameterCategoryController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ParameterCategoryController.class);

	@Autowired
	private IParameterCategoryService parameterCategoryService;

	/**
	 * 參數類型維護-查詢.
	 * 
	 * @param vo
	 *            ParameterCategoryVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param-category/searches", produces = { "application/json" })
	public ResponseEntity<?> getParameterCategoryPageList(@Valid @RequestBody ParamCategoryRequestVo vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<PageResponseObj<Map<String, String>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PageResponseObj<Map<String, String>> pageResp = new PageResponseObj<>();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param-category/searches:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUserId());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}

			// 查詢當頁資料集合
			List<Map<String, String>> resultList = parameterCategoryService.getParameterCategoryPageList(vo);
			pageResp.setPageData(resultList);

			// 資料總筆數
			int totalRecords = parameterCategoryService.getParameterCategoryPageTotal(vo);
			pageResp.setTotalRecords(totalRecords);

			if (vo.getRows() != null && vo.getPage() != null) {
				// 分頁查詢
				// 目前頁數
				pageResp.setPage(vo.getPage());
				// 總頁數
				pageResp.setTotalPage((pageResp.getTotalRecords() + vo.getRows() - 1) / vo.getRows());
			} else {
				// 查詢全部
				// 目前頁數
				pageResp.setPage(1);
				// 總頁數
				pageResp.setTotalPage(1);
			}
			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
			apiResponseObj.setResult(pageResp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getParameterPageList: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 參數類型維護-新增.
	 * 
	 * @param parameterCategoryVo
	 *            ParameterCategoryVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param-category/create", produces = { "application/json" })
	public ResponseEntity<?> insertParameterCategory(@Valid @RequestBody ParamCategoryAddRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterCategoryVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param-category/create:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getCreateUser());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}

			int result = parameterCategoryService.insertParameterCategory(vo);
			if (result > 0) {
				// processSuccessMsg("新增成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "新增成功", "", "");
			} else {
				// processError("新增失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "新增失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to insertParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			String errmsg = e.getMessage();
			if(e.getMessage().contains("ORA-00001")) {
				errmsg = "違反唯一值限制(系統別+參數類型代碼必需唯一)";
				Map<String, Object> error = Collections.singletonMap("error", errmsg);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
			}
			Map<String, Object> error = Collections.singletonMap("error", errmsg);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 取得參數類型資料.
	 * 
	 * @param vo
	 *            ParameterCategoryVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/getParameterCategory", produces = { "application/json" })
	public ResponseEntity<ResponseObj> getParameterCategory(@RequestBody ParameterCategoryVo vo) {
		try {
			processSuccess(parameterCategoryService.getParameterCategory(vo));
		} catch (Exception e) {
			logger.error("Unable to getParameterCategory: {}", ExceptionUtils.getStackTrace(e));
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
	@ApiRequest
	@PostMapping(value = "/getParameterCategoryById", produces = { "application/json" })
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
	@ApiRequest
	@PostMapping(value = "/param-category/update", produces = { "application/json" })
	public ResponseEntity<?> updateParameterCategory(@Valid @RequestBody ParamCategoryUpdateRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterCategoryVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param-category/update:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUpdateUser());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			
			int result = parameterCategoryService.updateParameterCategory(vo);
			if (result > 0) {
				// processSuccessMsg("更新成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "更新成功", "", "");
			} else {
				// processError("更新失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "更新失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to updateParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 參數類型維護-刪除.
	 * 
	 * @param parameterCategoryVo
	 *            ParameterCategoryVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param-category/delete", produces = { "application/json" })
	public ResponseEntity<?> deleteParameterCategory(@Valid @RequestBody ParamCategoryDelRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterCategoryVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param-category/delete:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUserId());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			
			int result = parameterCategoryService.deleteParameterCategory(vo);
			if (result > 0) {
				// processSuccessMsg("刪除成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "刪除成功", "", "");
			} else {
				// processError("刪除失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "刪除失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteParameterCategory: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}
