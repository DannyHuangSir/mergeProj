package com.twfhclife.eservice.onlineChange.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransCustInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.service.ITransCustInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;

/**
 * 線上申請-變更基本資料
 */
@Controller
public class TransCustInfoController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TransCustInfoController.class);
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private ITransCustInfoService transCustInfoService;

	@Autowired
	private KeycloakService keycloakService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;

	/**
	 * 變更資料.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/changeInfo1")
	public String changeInfo1() {
		try {
			KeycloakUser keycloakUser = getLoginUser();
			UsersVo user = getUserDetail();
			
			if (keycloakUser != null && user != null) {
				TransCustInfoVo transCustInfoVo = new TransCustInfoVo();
				transCustInfoVo.setEmailOld(keycloakUser.getEmail());
				transCustInfoVo.setMobileOld(keycloakUser.getMobile());
				transCustInfoVo.setMailFlagOld(user.getMailFlag());
				transCustInfoVo.setSmsFlagOld(user.getSmsFlag());
				
				addAttribute("transCustInfoVo", transCustInfoVo);
			} else {
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to init from changeInfo1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "491");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/changeInfo/change-info1";
	}
	
	/**
	 * 確認資料.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeInfo2")
	public String changeInfo2(TransCustInfoVo transCustInfoVo) {
		try {
			addAttribute("transCustInfoVo", transCustInfoVo);
		} catch (Exception e) {
			logger.error("Unable to init from changeInfo2: {}", ExceptionUtils.getStackTrace(e));
		}
		return "frontstage/onlineChange/changeInfo/change-info2";
	}
	
	/**
	 * 申請完成.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/changeInfoSuccess")
	public String changeInfoSuccess(TransCustInfoVo transCustInfoVo) {
		try {
			// 設定交易序號跟使用者
			String transNum = transService.getTransNum();
			transCustInfoVo.setTransNum(transNum);
			transCustInfoVo.setUserId(getUserId());
			
			int result = transCustInfoService.insertTransCustInfo(transCustInfoVo);
			if (result <= 0) {
				addDefaultSystemError();
				return "forward:changeInfo2";
			} else {
				// 成功才更新Keycloak
				KeycloakUser keycloakUser = getLoginUser();
				keycloakUser.setMobile(transCustInfoVo.getMobile());
				keycloakUser.setEmail(transCustInfoVo.getEmail());
				keycloakService.updateUser(keycloakUser, null);
				
				// 更新session
				UsersVo userDetail = getUserDetail();
				userDetail.setMobile(transCustInfoVo.getMobile());
				userDetail.setEmail(transCustInfoVo.getEmail());
				userDetail.setSmsFlag(transCustInfoVo.getSmsFlag());
				userDetail.setMailFlag(transCustInfoVo.getMailFlag());
				addSession(UserDataInfo.USER_DETAIL, userDetail);
			}
		} catch (Exception e) {
			logger.error("Unable to init from changeInfoSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:changeInfo2";
		}
		return "frontstage/onlineChange/changeInfo/change-info-success";
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransCustInfoDetail")
	public String getTransCustInfoDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransCustInfoVo transCustInfoVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transCustInfoVo = transHistoryDetailList.get(0).getTransCustInfoVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransCustInfoDetail data", userId);
				transCustInfoVo = transCustInfoService.getTransCustInfoDetail(transNum);
			}
			
			addAttribute("transCustInfoVo", transCustInfoVo);
		} catch (Exception e) {
			logger.error("Unable to getTransCustInfoDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/changeInfo/change-info-detail";
	}
}