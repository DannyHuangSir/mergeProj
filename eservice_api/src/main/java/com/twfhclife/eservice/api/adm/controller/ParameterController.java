package com.twfhclife.eservice.api.adm.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.api.adm.domain.ParamAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice_api.service.IParameterService;
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
 * 參數管理-參數維護.
 * 
 * @author all
 */
@RestController
// @RequestMapping("/eservice_adm/parameter")
public class ParameterController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ParameterController.class);

	@Autowired
	@Qualifier("apiParameterService")
	private IParameterService parameterService;
	
//	@Value("${eservice_api.secret.required}")
//	protected Boolean SECRET_REQUIRED;

	/**
	 * 參數維護-查詢.
	 * parameterName為模糊搜尋
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param/searches", produces = { "application/json" })
	public ResponseEntity<?> searchParamCategory(@Valid @RequestBody ParamRequestVo vo,
			@RequestHeader(value = "secret", required = false) String secret) {
		ApiResponseObj<PageResponseObj<Map<String, String>>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		PageResponseObj<Map<String, String>> pageResp = new PageResponseObj<>();
		try {
			if (SECRET_REQUIRE && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param/searches:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUserId());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}

			// 查詢當頁資料集合
			List<Map<String, String>> resultList = parameterService.getParameterPageList(vo);
			if(resultList != null && resultList.size() > 0) {
				for(Map<String, String> param : resultList) {
					String encryptType = param.get("encryptType");
					if("Y".equals(encryptType)) {
						param.put("parameterValue", EncryptionUtil.Decrypt(param.get("parameterValue")));
					}
				}
			}
			
			pageResp.setPageData(resultList);

			// 資料總筆數
			int totalRecords = parameterService.getParameterPageTotal(vo);
			pageResp.setTotalRecords(totalRecords);

			if (vo.getRows() != null && vo.getRows() != 0 && vo.getPage() != null && vo.getPage() != 0) {
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
			//Map<String, Object> error = Collections.singletonMap("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 參數維護-新增.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param/create", produces = { "application/json" })
	public ResponseEntity<?> insertParameter(@Valid @RequestBody ParamAddRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		String userId = vo.getCreateUser();
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param/create:" + ApConstants.INVALID_SECRET + ", userId=" + userId);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}

			int result = parameterService.insertParameter(vo);
			if (result > 0) {
//				processSuccessMsg("新增成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "新增成功", "", "");
			} else {
//				processError("新增失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "新增失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to insertParameter: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
//			processSystemError();
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 取得參數資料.
	 * 
	 * @param vo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/getParameter", produces = { "application/json" })
	public ResponseEntity<ResponseObj> getParameter(@RequestBody ParamRequestVo vo) {
		try {
			processSuccess(parameterService.getParameter(vo));
		} catch (Exception e) {
			logger.error("Unable to getParameter: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得當筆參數資料.
	 * 
	 * @param vo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/getParameterById", produces = { "application/json" })
	public ResponseEntity<ResponseObj> getParameterById(@RequestBody ParamRequestVo vo) {
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
	 * @param parameterVo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param/update", produces = { "application/json" })
	public ResponseEntity<?> updateParameter(@Valid @RequestBody ParamUpdateRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		String userId = vo.getUpdateUser();
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param/update:" + ApConstants.INVALID_SECRET + ", userId=" + userId);
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			int result = parameterService.updateParameter(vo);
			if (result > 0) {
//				processSuccessMsg("更新成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "更新成功", "", "");
			} else {
//				processError("更新失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "更新失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to updateParameter: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
//			processSystemError();
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return
	 */
	@ApiRequest
	@PostMapping(value = "/param/delete", produces = { "application/json" })
	public ResponseEntity<?> deleteParameter(@Valid @RequestBody ParamDelRequestVo vo,
			@RequestHeader(value = "secret", required = true) String secret) {
		ApiResponseObj<PageResponseObj<ParameterVo>> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		
		try {
			if (MyStringUtil.isNotNullOrEmpty(secret) && !this.validateSecret(secret)) {
				this.setErrorMessages(ApConstants.INVALID_SECRET);
				logger.error("/param/delete:" + ApConstants.INVALID_SECRET + ", userId=" + vo.getUserId());
				Map<String, Object> error = Collections.singletonMap("error", this.errorMessages);
				return ResponseEntity.badRequest().body(error);
			}
			int result = parameterService.deleteParameter(vo);
			if (result > 0) {
//				processSuccessMsg("刪除成功");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "刪除成功", "", "");
			} else {
//				processError("刪除失敗");
				returnHeader.setReturnHeader(ReturnHeader.FAIL_CODE, "刪除失敗", "", "");
			}
		} catch (Exception e) {
			logger.error("Unable to deleteParameter: {}", ExceptionUtils.getStackTrace(e));
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
//			processSystemError();
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
}
