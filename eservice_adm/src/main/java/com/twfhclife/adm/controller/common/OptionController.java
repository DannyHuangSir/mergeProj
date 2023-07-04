package com.twfhclife.adm.controller.common;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.*;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.keycloak.model.KeycloakUser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 共用選單資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class OptionController extends BaseController {

	private static final Logger logger = LogManager.getLogger(OptionController.class);

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private IParameterCategoryService parameterCategoryService;
	
	@Autowired
	private ISystemService systemService;
	
	@Autowired
	private IDeptMgntService deptMgntService;

	@Autowired
	private IJobTitleService jobTitleService;
	
	@Autowired
	private IRoleService roleService;

	@Autowired
	private IMessagingTemplateService messagingTemplateService;

	@Autowired
	private IJdDeptMgntService jdDeptMgntService;

	@Autowired
	private IJdRoleService jdRoleService;

	/**
	 * 下拉選單資料-醫療資料介接案件狀態
	 *
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/optionMedicalStatusList")
	public ResponseEntity<ResponseObj> optionMedicalStatusList(@RequestParam("categoryCode") String categoryCode) {
		try {
			processSuccess(parameterService.getOptionList(ApConstants.SYSTEM_API_ID, categoryCode));
		} catch (Exception e) {
			logger.error("Unable to optionList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	/**
	 * 下拉選單資料-醫療申請狀態
	 *
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/optionMedicalApplyForStatusList")
	public ResponseEntity<ResponseObj> optionMedicalApplyForStatusList(@RequestParam("categoryCode") String categoryCode) {
		try {
			//獲取申請狀態數據信息
			List<ParameterVo> applyForOptionStatusList = parameterService.getOptionList(ApConstants.SYSTEM_ID_ESERVICE,categoryCode);
			/*0	處理中 1	已審核  5	已上傳 6	失敗*/
			String [] str = {"0","1","5","6"};
			List<ParameterVo> collect = applyForOptionStatusList.stream().filter((x) -> {
				for (String s : str) {
					if (s.equals(x.getParameterCode())) {
						return false;
					}
				}
				return true;
			}).collect(Collectors.toList());
			processSuccess(applyForOptionStatusList);
		} catch (Exception e) {
			logger.error("Unable to optionList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}


	/**
	 * 下拉選單資料-根據參數類別代碼取得.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/optionList")
	public ResponseEntity<ResponseObj> optionList(@RequestParam("categoryCode") String categoryCode) {
		try {
			// TODO 需要使用eservice_adm, 等sys_id 資料設定成 eservice_adm 在設定
			String systemId = null;
			processSuccess(parameterService.getOptionList(systemId, categoryCode));
		} catch (Exception e) {
			logger.error("Unable to optionList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-全部的參數類別代碼.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/parameterCategoryList")
	public ResponseEntity<ResponseObj> parameterCategoryList() {
		try {
			processSuccess(parameterCategoryService.getParameterCategory(new ParameterCategoryVo()));
		} catch (Exception e) {
			logger.error("Unable to parameterCategoryList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-全部的參數代碼.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/parameterList")
	public ResponseEntity<ResponseObj> parameterList() {
		try {
			processSuccess(parameterService.getParameter(new ParameterVo()));
		} catch (Exception e) {
			logger.error("Unable to parameterList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 下拉選單資料-通信管理的參數代碼.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/msg/parameterList")
	public ResponseEntity<ResponseObj> msgParameterList() {
		try {
			ParameterVo vo = new ParameterVo();
			vo.setSystemId("eservice_adm");
			vo.setCategoryCode("MessagingParameter");
			processSuccess(parameterService.getParameter(vo));
		} catch (Exception e) {
			logger.error("Unable to parameterList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-系統別.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/systemList")
	public ResponseEntity<ResponseObj> systemList() {
		try {
			processSuccess(systemService.getAll());
		} catch (Exception e) {
			logger.error("Unable to systemList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-部門.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/deptList")
	public ResponseEntity<ResponseObj> deptList() {
		try {
			KeycloakUser kuser = getLoginUser();
			String username = kuser.getUsername();
			String keyCloakUserId = kuser.getId();//此處查詢應使用id
			processSuccess(deptMgntService.getDeptList(keyCloakUserId, username));
		} catch (Exception e) {
			logger.error("Unable to deptList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-職位.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/jobTitleList")
	public ResponseEntity<ResponseObj> jobTitleList() {
		try {
			processSuccess(jobTitleService.getJobTitle(new JobTitleVo()));
		} catch (Exception e) {
			logger.error("Unable to jobTitleList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-角色.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/roleList")
	public ResponseEntity<ResponseObj> roleList() {
		try {
			String userName = getUserId();
			String keyCloakUserId = getLoginUser().getId();
			processSuccess(roleService.getRoleByAuth(userName, keyCloakUserId));
		} catch (Exception e) {
			logger.error("Unable to roleList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-通信模板代碼.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/msgTmpCodeList")
	public ResponseEntity<ResponseObj> msgTmpCodeList() {
		try {
			List<MessagingTemplateVo> dataList = messagingTemplateService
					.getMessagingTemplate(new MessagingTemplateVo());
			List<String> msgTmpCodes = new ArrayList<>();
			List<MessagingTemplateVo> optionList = new LinkedList<>();
			for (MessagingTemplateVo vo : dataList) {
				if (!msgTmpCodes.contains(vo.getMessagingTemplateCode())) {
					msgTmpCodes.add(vo.getMessagingTemplateCode());
					optionList.add(vo);
				}
			}
			processSuccess(optionList);
		} catch (Exception e) {
			logger.error("Unable to msgTmpCodeList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-通信模板名稱.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/msgTmpNameList")
	public ResponseEntity<ResponseObj> msgTmpNameList() {
		try {
			List<MessagingTemplateVo> dataList = messagingTemplateService
					.getMessagingTemplate(new MessagingTemplateVo());
			List<String> msgTmpNames = new ArrayList<>();
			List<MessagingTemplateVo> optionList = new LinkedList<>();
			for (MessagingTemplateVo vo : dataList) {
				if (!msgTmpNames.contains(vo.getMessagingTemplateCode())) {
					msgTmpNames.add(vo.getMessagingTemplateName());
					optionList.add(vo);
				}
			}
			processSuccess(optionList);
		} catch (Exception e) {
			logger.error("Unable to msgTmpNameList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 下拉選單資料-关系名称.
	 *
	 * @return
	 */
	@RequestLog
	@PostMapping("/common/relationList")
	public ResponseEntity<ResponseObj> relationList() {
		try {
			List<ParameterVo> optionList = parameterService.getParameterByCategoryCode("eservice", "MEMBER_RESULT_OPTION");
			processSuccess(optionList);
		} catch (Exception e) {
			logger.error("Unable to msgTmpNameList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/deptList")
	public ResponseEntity<ResponseObj> jdDeptList(@RequestBody DepartmentVo vo) {
		try {
			com.twfhclife.keycloak.model.KeycloakUser kuser = getLoginUser();
			String username = kuser.getUsername();
			String keyCloakUserId = kuser.getId();//此處查詢應使用id
			processSuccess(jdDeptMgntService.getDeptList(keyCloakUserId, username,vo));
		} catch (Exception e) {
			logger.error("Unable to deptList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/roleList")
	public ResponseEntity<ResponseObj> jdRoleList() {
		try {
			String userName = getUserId();
			String keyCloakUserId = getLoginUser().getId();
			processSuccess(jdRoleService.getRoleByAuth(userName, keyCloakUserId));
		} catch (Exception e) {
			logger.error("Unable to roleList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/dept/parentList")
	public ResponseEntity<ResponseObj> jdDeptParentList() {
		try {
			com.twfhclife.keycloak.model.KeycloakUser kuser = getLoginUser();
			String username = kuser.getUsername();
			String keyCloakUserId = kuser.getId();//此處查詢應使用id
			processSuccess(jdDeptMgntService.getDeptParentList(keyCloakUserId, username));
		} catch (Exception e) {
			logger.error("Unable to jdDeptParentList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/userAccountStatusList")
	public ResponseEntity<ResponseObj> userDetail() {
		try {
			processSuccess(parameterService.getUserAccountStatusList());
		} catch (Exception e) {
			logger.error("Unable to userAccountStatusList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/branchList")
	public ResponseEntity<ResponseObj> jdBranchList(@RequestBody DepartmentVo vo) {
		try {
			com.twfhclife.keycloak.model.KeycloakUser kuser = getLoginUser();
			String username = kuser.getUsername();
			String keyCloakUserId = kuser.getId();//此處查詢應使用id
			processSuccess(jdDeptMgntService.getBranchList(keyCloakUserId, username,vo));
		} catch (Exception e) {
			logger.error("Unable to branchList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/depRoleList")
	public ResponseEntity<ResponseObj> jdDepRoleList(@RequestBody DepartmentVo vo) {
		try {
			String userName = getUserId();
			String keyCloakUserId = getLoginUser().getId();
			processSuccess(jdRoleService.getDepRole(userName, keyCloakUserId,vo.getDepId()));
		} catch (Exception e) {
			logger.error("Unable to jdDepRoleList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/jd/common/branchByList")
	public ResponseEntity<ResponseObj> jdBranchByList(@RequestBody DepartmentVo vo) {
		try {
			com.twfhclife.keycloak.model.KeycloakUser kuser = getLoginUser();
			String username = kuser.getUsername();
			String keyCloakUserId = kuser.getId();//此處查詢應使用id
			processSuccess(jdDeptMgntService.getBranchByList(keyCloakUserId, username,vo));
		} catch (Exception e) {
			logger.error("Unable to jdBranchByList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
