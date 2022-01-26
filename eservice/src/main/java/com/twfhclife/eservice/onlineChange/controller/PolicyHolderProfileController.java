package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.service.IOnlineChangeService;
import com.twfhclife.eservice.onlineChange.service.IPolicyHolderProfileService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.api_client.TransAddClient;
import com.twfhclife.generic.api_client.TransHistoryDetailClient;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.api_model.TransAddRequest;
import com.twfhclife.generic.api_model.TransAddResponse;
import com.twfhclife.generic.api_model.TransHistoryDetailResponse;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;

/**
 * 線上申請-保戶基本資料更新
 */
@Controller
public class PolicyHolderProfileController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(PolicyHolderProfileController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private TransHistoryDetailClient transHistoryDetailClient;
	
	@Autowired
	private TransAddClient transAddClient;
	
	@Autowired
	private IPolicyHolderProfileService policyHolderProfileService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	private void prepaidPage() {
		Map<String, ParameterVo> parameterList = getParameterMap("PAGE_WORDING");
		Map<String, String> map = new HashMap<String, String>();
		for(String key : parameterList.keySet()){ 
			if(key.substring(0, 9).equals(OnlineChangeUtil.ONLINE_CHANGE_HOME_PARAMETER_CATEGORY_CODE)){
				map.put(key, parameterList.get(key).getParameterValue());
			}
		}
		addAttribute("onlineChangeHome", map);
		addAttribute("canUseFlag", getUserDetail().getOnlineFlag());
		addAttribute("onlinechangeEnableEntry", onlinechangeEnableEntry);
	}
	
	/**
	 * 保戶基本資料更新頁面.
	 * 
	 * @return
	 */
	@RequestLog
	@GetMapping("/policyHolderProfile1")
	public String policyHolderProfile1() {
		try {
			String userId = getUserId();
			List<TransVo> transHistoryList = onlineChangeService.getTransByUserId(userId, null,
							TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE, null, null, null, 1, 1);
			for (TransVo vo : transHistoryList) {
				if (vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_PROCESSING)
						|| vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_AUDITED)
						|| vo.getStatus().equals(OnlineChangeUtil.TRANS_STATUS_UPLOADED)) {
					addAttribute("errorMessage", OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
					prepaidPage();
					return "frontstage/onlineChange/apply1";
				}
			}
			
			LilipmVo qry = new LilipmVo();
			qry.setLipmId(this.getUserRocId());
			List<LilipmVo> lipmList = lilipmService.getAliveLilipm(qry);
			if (lipmList == null || lipmList.size() == 0) {
				addAttribute("errorMessage", "查無可供變更的有效保單");
				prepaidPage();
				return "frontstage/onlineChange/apply1";
			}
			addAttribute("lipmList", lipmList);
			
			TransPolicyHolderProfileVo transPolicyHolderProfileVo = new TransPolicyHolderProfileVo();
			transPolicyHolderProfileVo.setName(lipmList != null && lipmList.size() > 0 ? lipmList.get(0).getLipmName1() : "");
			addAttribute("transPolicyHolderProfileVo", transPolicyHolderProfileVo);
			
			List<String> addCodes = policyHolderProfileService.getAddCodeByLipmId(this.getUserRocId());
			if (addCodes.contains("1R")) {
				addAttribute("MSG_1R", "Y");
			}
			if (addCodes.contains("1D")) {
				addAttribute("MSG_1D", "Y");
			}
			
			
		} catch (Exception e) {
			logger.error("Unable to init from policyHolderProfile1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyHolderProfile/policy-holder-pofile1";
	}

	/**
	 * 步驟2(確認資料及發送驗證碼).
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyHolderProfile2")
	public String policyHolderProfile2(TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		try {
			addAttribute("transPolicyHolderProfileVo", transPolicyHolderProfileVo);
			// 發送驗證碼
			sendAuthCode("policyHolderProfile");
		} catch (Exception e) {
			logger.error("Unable to init from policyHolderProfile2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyHolderProfile/policy-holder-pofile2";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * 
	 * @param TranspolicyHolderProfileVo transpolicyHolderProfileVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/policyHolderProfileSuccess")
	public String policyHolderProfileSuccess(TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		try {
			// 驗證驗證碼
			String authNum = transPolicyHolderProfileVo.getAuthenticationNum();
			String msg = checkAuthCode("policyHolderProfile", authNum);
			if (!StringUtils.isEmpty(msg)) {
				addSystemError(msg);
				return "forward:policyHolderProfile2";
			}
			
			// 設定使用者
			String userId = getUserId();
			transPolicyHolderProfileVo.setUserId(userId);
			
			// 設定保單
			LilipmVo qry = new LilipmVo();
			qry.setLipmId(this.getUserRocId());
			List<LilipmVo> lipmList = lilipmService.getAliveLilipm(qry);
			transPolicyHolderProfileVo.setPolicyNoList(this.convertPol(lipmList));
			
			// Call api 送出線上申請資料
			logger.info("Send user[{}] trans data to eservice_api[addTransRequest]", userId);
			
			String transAddResult = "";
			TransAddRequest apiReq = new TransAddRequest();
			apiReq.setSysId(ApConstants.SYSTEM_ID);
			apiReq.setTransType(TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE);
			apiReq.setTransPolicyHolderProfileVo(transPolicyHolderProfileVo);
			apiReq.setUserId(userId);
			
			TransAddResponse transAddResponse = transAddClient.addTransRequest(apiReq);
			if (transAddResponse != null) {
				logger.info("Get user[{}] transAddResponse from eservice_api[addTransRequest]: {}", userId,
						MyJacksonUtil.object2Json(transAddResponse));
				transAddResult = transAddResponse.getTransResult();
			} else {
				// 若無資料，嘗試由內部服務取得資料
				logger.info("Call internal service to get user[{}] insertPolicyHolderProfile data", userId);
				
				int result = policyHolderProfileService.insertPolicyHolderProfile(transPolicyHolderProfileVo);
				if (result <= 0) {
					transAddResult = ReturnHeader.FAIL_CODE;
				} else {
					transAddResult = ReturnHeader.SUCCESS_CODE;
				}
			}
			
			if (!StringUtils.equals(transAddResult, ReturnHeader.SUCCESS_CODE)) {
				addDefaultSystemError();
				return "forward:policyHolderProfile2";
			}
		} catch (Exception e) {
			logger.error("Unable to init from policyHolderProfileSuccess: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:policyHolderProfile2";
		}
		return "frontstage/onlineChange/policyHolderProfile/policy-holder-pofile-success";
	}
	
	private List<String> convertPol(List<LilipmVo> lipmList) {
		List<String> policyNos = new ArrayList<String>();
		for (LilipmVo vo : lipmList) {
			policyNos.add(vo.getLipmInsuNo());
		}
		return policyNos;
	}

	/**
	 * 取得申請明細資料.
	 * 
	 * @param transNum 申請序號
	 * @return
	 */
	@RequestLog
	@PostMapping("/getPolicyHolderProfileDetail")
	public String getPolicyHolderProfileDetail(@RequestParam("transNum") String transNum) {
		try {
			String userId = getUserId();
			TransPolicyHolderProfileVo transPolicyHolderProfileVo = null;
			
			// Call api 取得資料
			TransHistoryDetailResponse transHistoryDetailResponse = transHistoryDetailClient
					.getTransHistoryDetail(userId, Arrays.asList(transNum));
			// 若無資料，嘗試由內部服務取得資料
			if (transHistoryDetailResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getTransHistoryDetail]", userId);
				List<TransDetailVo> transHistoryDetailList = transHistoryDetailResponse.getTransHistoryDetailList();
				if (transHistoryDetailList != null && transHistoryDetailList.size() > 0) {
					transPolicyHolderProfileVo = transHistoryDetailList.get(0).getTransPolicyHolderProfileVo();
				}
			} else {
				logger.info("Call internal service to get user[{}] getTransPolicyHolderProfileDetail data", userId);
				transPolicyHolderProfileVo = policyHolderProfileService.getPolicyHolderProfileDetail(transNum);
			}
			
			addAttribute("transPolicyHolderProfileVo", transPolicyHolderProfileVo);
		} catch (Exception e) {
			logger.error("Unable to getPolicyHolderProfileDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/policyHolderProfile/policy-holder-pofile-detail";
	}
}