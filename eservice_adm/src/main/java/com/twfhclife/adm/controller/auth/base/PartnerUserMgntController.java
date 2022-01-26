package com.twfhclife.adm.controller.auth.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.PartnerUserEntityVo;
import com.twfhclife.adm.model.RoleDivAuthVo;
import com.twfhclife.adm.model.UsersVo;
import com.twfhclife.adm.service.IPartnerUserMgntService;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.generic.util.ValidateUtils;

/**
 * 外部人員管理.
 * 
 * @author all
 */
@Controller
public class PartnerUserMgntController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PartnerUserMgntController.class);

	@Autowired
	private IPartnerUserMgntService partnerUserMgntService;
	
	/**
	 * 人員管理頁面程式進入點.
	 * 
	 * @return
	 */
	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "342",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/partnerUserMgnt")
	public String loginRecord(Model model) {
		
		//2020/12-控制USER所屬的ROLE是否可使用deletePartnerBtn-start
		boolean hideDelBtn = false;
		String userId = (String)getSession(ApConstants.LOGIN_KEYCLOAK_USER_ID);
		List<RoleDivAuthVo> listRoleDivAuth = partnerUserMgntService.getRoleDivAuthByUserId(userId);
		if(listRoleDivAuth!=null) {
			int div22 = 22;//FUNCTION_DIV.DIV_ID=22,DIV_NAME=deletePartnerBtn
			for(RoleDivAuthVo vo:listRoleDivAuth) {
				if(vo!=null && vo.getDivId()!=null) {
					if(div22 == vo.getDivId().intValue()) {
						hideDelBtn = true;
						break;
					}
				}
			}
		}
		addSession("HIDE_DEL_BTN", hideDelBtn);
		//2020/12-控制USER所屬的ROLE是否可使用deletePartnerBtn-end
				
		model = this.setDivAuth(model, "partnerUserMgnt");
//		Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>> divAuthMap = (Map<String, List<com.twfhclife.generic.api_model.FunctionDivVo>>) getSession(ApConstants.DIV_AUTH_MAP);
//		List<com.twfhclife.generic.api_model.FunctionDivVo> divAuth = divAuthMap.get("partnerUserMgnt");
//		if(divAuth != null) {
//			for(com.twfhclife.generic.api_model.FunctionDivVo vo : divAuth) {
//				model.addAttribute(vo.getDivName(), vo.getDivName());
//			}
//		}

		return "backstage/auth/base/partnerUserMgnt";
	}

	/**
	 * 取得外部人員管理查詢結果.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARTNER_USER_MGNT_GET_PARTNERUSER_ENTITYPAGELIST_001, 
			sqlId = EventCodeConstants.PARTNER_USER_MGNT_GET_PARTNERUSER_ENTITYPAGELIST_SQL_ID_001, 
			daoVoParamKey = "userEntityVo",
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.adm.service.impl.PartnerUserMngtServiceImpl.partnerUserEntityDao",
					execMethod = "外部人員管理-分頁查詢",
					sqlVoType  = "com.twfhclife.adm.model.PartnerUserEntityVo",
					sqlVoKey   = "userEntityVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "realmId", sqlParamkey = "REALM_ID")
					}
				)
			}
			))
	@PostMapping("/partnerUserMgnt/getPartnerUserEntityPageList")
	public ResponseEntity<PageResponseObj> getPartnerUserEntityPageList(@RequestBody PartnerUserEntityVo userEntityVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: PartnerUserEntityVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			pageResp.setRows(partnerUserMgntService.getPartnerUserEntityPageList(userEntityVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(partnerUserMgntService.getPartnerUserEntityPageTotal(userEntityVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(userEntityVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + userEntityVo.getRows() - 1) / userEntityVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getPartnerUserEntityPageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 取得外部人員使用者名稱.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/partnerUserMgnt/getNextPartnerUserName")
	public ResponseEntity<ResponseObj> getNextPartnerUserName(@RequestBody PartnerUserEntityVo userEntityVo) {
		try {
			processSuccess(partnerUserMgntService.getNextPartnerUserName(userEntityVo));
		} catch (Exception e) {
			logger.error("Unable to getUserName: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 外部人員管理-新增.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARTNER_USER_MGNT_INSERT_002, 
			sqlId = EventCodeConstants.PARTNER_USER_MGNT_INSERT_002_SQL_ID, 
			daoVoParamKey = "userEntityVo",
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.adm.dao.UsersDao.insertUsers",
					execMethod = "外部人員-新增",
					sqlVoType  = "com.twfhclife.adm.model.UsersVo",
					sqlVoKey   = "usersVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "rocId", sqlParamkey = "ROC_ID")
					}
				)
			}
			))
	@PostMapping("/partnerUserMgnt/insertPartnerUser")
	public ResponseEntity<ResponseObj> insertPartnerUser(@RequestBody PartnerUserEntityVo userEntityVo) {
		try {
			int userNameCnt = partnerUserMgntService.checkUserNameExist(userEntityVo);
			if (userNameCnt > 0) {
				processError("帳號已經存在");
			} else {
				int result = partnerUserMgntService.insertPartnerUser(userEntityVo);
				if (result > 0) {
					processSuccessMsg("新增成功");
				} else {
					processError("新增失敗");
				}
			}
		} catch (Exception e) {
			logger.error("Unable to insertPartnerUser: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 檢查帳號跟密碼.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/partnerUserMgnt/checkUserNameAndEmail")
	public ResponseEntity<ResponseObj> checkUserNameAndEmail(@RequestBody PartnerUserEntityVo userEntityVo) {
		try {
			String errMsg = "";
			int userNameCnt = partnerUserMgntService.checkUserNameExist(userEntityVo);
			if (userNameCnt > 0) {
				errMsg = "帳號已經存在";
			}
			
			if (StringUtils.isEmpty(errMsg)) {
				boolean isEmail = ValidateUtils.isEmail(userEntityVo.getEmail());
				if (!isEmail) {
					errMsg = "EMAIL格式不正確";
				}
			}
			
			if (StringUtils.isEmpty(errMsg)) {
				boolean isMobilePhoneNumber = ValidateUtils.isMobilePhoneNumber(userEntityVo.getMobile());
				if (!isMobilePhoneNumber) {
					errMsg = "手機號碼格式不正確";
				}
			}
			
			processSuccessMsg(errMsg);
			
		} catch (Exception e) {
			logger.error("Unable to checkUserNameAndEmail: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 外部人員管理-刪除.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARTNER_USER_MGNT_DELETE_003, 
			sqlId = EventCodeConstants.PARTNER_USER_MGNT_DELETE_003_SQL_ID, 
			daoVoParamKey = "userEntityVo",
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.adm.dao.UsersDao.deleteUsers",
					execMethod = "外部人員-刪除",
					sqlVoType  = "com.twfhclife.adm.model.UsersVo",
					sqlVoKey   = "usersVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "rocId", sqlParamkey = "ROC_ID")
					}
				)
			}
			))
	@PostMapping("/partnerUserMgnt/deletePartnerUser")
	public ResponseEntity<ResponseObj> deletePartnerUser(@RequestBody PartnerUserEntityVo userEntityVo) {
		try {
			int result = partnerUserMgntService.deletePartnerUser(userEntityVo);
			if (result > 0) {
				processSuccessMsg("刪除成功");
			} else {
				processError("刪除失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to deletePartnerUser: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 外部人員管理-解鎖.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARTNER_USER_MGNT_UNLOCKUSER_004, 
			sqlId = EventCodeConstants.PARTNER_USER_MGNT_UNLOCKUSER_004_SQL_ID, 
			daoVoParamKey = "userEntityVo",
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.adm.dao.UsersDao.updateUsers",
					execMethod = "外部人員管理-解鎖",
					sqlVoType  = "com.twfhclife.adm.model.UsersVo",
					sqlVoKey   = "usersVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "rocId", sqlParamkey = "ROC_ID")
					}
				)
			}
			))
	@PostMapping("/partnerUserMgnt/unlockUser")
	public ResponseEntity<ResponseObj> unlockUser(@RequestBody PartnerUserEntityVo userEntityVo) {
		try {
			int result = partnerUserMgntService.unlockUser(userEntityVo);
			if (result > 0) {
				processSuccessMsg("解鎖成功");
			} else {
				processError("解鎖失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to unlockUser: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 外部人員管理-更新線上申請使用
	 * @param usersVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.PARTNER_USER_MGNT_UPDATEONLINECHANGE_005, 
			sqlId = EventCodeConstants.PARTNER_USER_MGNT_UPDATEONLINECHANGE_005_SQL_ID, 
			daoVoParamKey = "usersVo",
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.adm.dao.UsersDao.updateOnlineChangeUse",
					execMethod = "外部人員管理-解鎖",
					sqlVoType  = "com.twfhclife.adm.model.UsersVo",
					sqlVoKey   = "usersVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "userId", sqlParamkey = "USER_ID")
					}
				)
			}
			))
	@PostMapping("/partnerUserMgnt/updateOnlineChangeUse")
	public ResponseEntity<ResponseObj> updateOnlineChangeUse(@RequestBody UsersVo usersVo){
		try {
			if(usersVo == null) {
				// 解析失敗/傳空物件直接回 fail 
				processSystemError();
			} else {
				if(StringUtils.isNotBlank(usersVo.getUserId())) {
					int result = partnerUserMgntService.updateOnlineChangeUse(usersVo);
					if(result > 0) {
						processSuccessMsg("更新線上申請功能成功");
					} else {
						processError("更新線上申請功能失敗");
					}
				} else {
					// 沒有指定 UserId 可能造成多筆更動，故回 fail 
					processError("沒有指定異動使用者，請重新操作");
				}
			}
		} catch(Exception e) {
			logger.error("Unable to updateOnlineChangeFlag: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}