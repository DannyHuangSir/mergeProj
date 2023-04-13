package com.twfhclife.eservice.onlineChange.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.ContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.TransContractRevocationFileDataVo;
import com.twfhclife.eservice.onlineChange.service.ITransContractRevocationService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.ISendAuthenticationService;
import com.twfhclife.generic.util.ApConstants;

@Controller
public class TransContractRevocationController extends BaseUserDataController {
	
	private static final Logger logger = LogManager.getLogger(TransContractRevocationController.class);
	
	@Autowired
	private ISendAuthenticationService sendAuthenticationService;
	
	@Autowired
	private ITransContractRevocationService transContractRevocationService;
	
	@Autowired
	private ITransService transService;
	
	private static String IMAGE_HEAD = "data:image/png;base64,";
	
	/**
	 * 取得減額繳清保險畫面資料
	 * @return
	 */
	@RequestLog
	@GetMapping("/contractRevocation1")
	public String getContractRevocation(RedirectAttributes redirectAttributes) {
		try {
			/**
			 * 1.確認功能是否可以使用
			 */
			if (!checkCanUseOnlineChange()) {
				/* addSystemError("目前無法使用此功能，請臨櫃申請線上服務。"); */
				String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
				addSystemError(message);
				redirectAttributes.addFlashAttribute("errorMessage", message);
				return "redirect:apply1";
			}
			/**
			 * 2.呼叫CTC查詢資料
			 */
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<ContractRevocationVo> contractRevocationList =  transContractRevocationService.getPolicyDetail(userRocId ,userId , TransTypeUtil.CONTRACT_REVOCATION);
			contractRevocationList = contractRevocationList.stream().sorted(Comparator.comparing(ContractRevocationVo::getLipmInsuNo))
					.collect(Collectors.toList());
			contractRevocationList = contractRevocationList.stream().sorted(Comparator.comparing(ContractRevocationVo::getApplyLockedFlag))
					.collect(Collectors.toList());
			addAttribute("policyList", contractRevocationList);

		} catch (Exception e) {
			logger.error("Unable to init from contractRevocation1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/contractRevocation/contractRevocation1";
	}
	
	/**
	 * 取得減額繳清保險畫面資料
	 * @return
	 */
	@RequestLog
	@PostMapping("/contractRevocation2")
	public String getContractRevocation2(ContractRevocationVo contractRevocationVo) {
		ContractRevocationVo contractRevocationVos = new ContractRevocationVo();
		try {
			/**
			 * 1.取得選取的所有資料
			 */
			contractRevocationVos = transContractRevocationService
					.getPolicy(contractRevocationVo.getLipmInsuNo());
			/**
			 * 2.取得畫面資料
			 */
			transContractRevocationService.getRevokeByLiprpaForInsuSeqNo(contractRevocationVos);
			// 若為信用卡繳費，無需顯示原繳款銀戶，只顯示此行: 【信用卡繳費件】退回原繳費卡號
			if (StringUtils.isNotEmpty(contractRevocationVos.getPrpkRcpTypeCode())) {
				contractRevocationVos.setRcpTypeCodeFlag(contractRevocationVos.getPrpkRcpTypeCode());
				if (contractRevocationVos.getPrpkRcpTypeCode().equals("A")) {
					transContractRevocationService.getRevokeByLineacForNeacInsuNo(contractRevocationVos);
				}
			}

			addAttribute("contractRevocationVos", contractRevocationVos);

		} catch (Exception e) {
			logger.error("Unable to init from contractRevocation2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		if(StringUtils.isNotEmpty(contractRevocationVos.getRcpTypeCodeFlag())) {
			if(contractRevocationVos.getRcpTypeCodeFlag().equals("H")) {
				return "frontstage/onlineChange/contractRevocation/contractRevocation2";
			}else if(contractRevocationVos.getRcpTypeCodeFlag().equals("A")){
				if(StringUtils.isNotEmpty(contractRevocationVos.getOldBankCode())) {
					return "frontstage/onlineChange/contractRevocation/contractRevocation2";
				}else {
					contractRevocationVos.setOldNeacName(contractRevocationVos.getLipmName());
					return "frontstage/onlineChange/contractRevocation/contractRevocation2-1";
				}
			}else {
				contractRevocationVos.setOldNeacName(contractRevocationVos.getLipmName());
				return "frontstage/onlineChange/contractRevocation/contractRevocation2-1";
			}
		}else {
			contractRevocationVos.setOldNeacName(contractRevocationVos.getLipmName());
			return "frontstage/onlineChange/contractRevocation/contractRevocation2-1";
		}
	}
	
	@RequestLog
	@PostMapping("/contractRevocation3")
	public String getContractRevocation3(ContractRevocationVo contractRevocationVo , BindingResult bindingResult) {
		try {
			/** 1.整理畫面資料*/			
			StringBuffer  flagMsg = new StringBuffer();
			if(contractRevocationVo.getNeedsFlag().equals("1")) {
				flagMsg.append("保單規劃不符需求、");				
			}
			if(contractRevocationVo.getEconomyFlag().equals("1")) {
				flagMsg.append("經濟因素、");
			}
			if(contractRevocationVo.getFamilyFlag().equals("1")) {
				flagMsg.append("家人反對、");
			}
			if(contractRevocationVo.getCognitionFlag().equals("1")) {
				flagMsg.append("對商品認知有誤、");
			}
			if(contractRevocationVo.getOtherFlag().equals("1")) {
				flagMsg.append("其他、");
			}
			
			String msg = flagMsg.substring(0,flagMsg.length()-1);		
			contractRevocationVo.setStatusFlag(msg);
			
			if(StringUtils.isNotEmpty(contractRevocationVo.getImage1())) {
				contractRevocationVo.setImage1Tobase64(IMAGE_HEAD + contractRevocationVo.getImage1());
			}
			if(StringUtils.isNotEmpty(contractRevocationVo.getImage2())) {
				contractRevocationVo.setImage2Tobase64(IMAGE_HEAD + contractRevocationVo.getImage2());
			}
			
			addAttribute("contractRevocationVo", contractRevocationVo);
			addAttribute("timeSet", 300);
			sendAuthCode("contractRevocation");
		} catch (Exception e) {
			logger.error("Unable to init from contractRevocation3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/contractRevocation/contractRevocation3";
	}
	
	/**
	 * 上傳申請應備文件
	 * 
	 * @param files
	 * @return
	 */
	@RequestLog
	@RequestMapping(value = "/upLoadContractRevocationFiles")
	@ResponseBody
	public TransContractRevocationFileDataVo upLoadFiles(@RequestParam("files") MultipartFile[] files) {
		TransContractRevocationFileDataVo fileUpload = transContractRevocationService.upLoadFiles(files);
		return fileUpload;
	}
	
	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * @return
	 */
	@RequestLog
	@PostMapping("/contractRevocationSuccess")
	public String contractRevocationSuccess(ContractRevocationVo contractRevocationVo, BindingResult bindingResult) {
		try {
			boolean isTransApplyed = false;
			boolean applyed = false;
			/**
			 * 1.判斷是否有申請過的的單子
			 */
			String lipmInsuNo = contractRevocationVo.getLipmInsuNo();
			applyed = transService.isTransApplyed(lipmInsuNo, TransTypeUtil.CONTRACT_REVOCATION,
					OnlineChangeUtil.TRANS_STATUS_RECEIVED);
			if (applyed) {
				isTransApplyed = true;
			}

			/**
			 * 2. 沒有申請過的單子才開始進行新增動作
			 */
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = contractRevocationVo.getAuthenticationNum();
				String msg = checkAuthCode("contractRevocation", authNum);
				if (!StringUtils.isEmpty(msg)) {
					contractRevocationVo.setAuthenticationNum(null);
					addAttribute("contractRevocationVo", contractRevocationVo);
					addSystemError(msg);
					return "forward:contractRevocation3";
				}
				Map<String, ParameterVo> parameterList =getParameterMap(OnlineChangeUtil.BUSINESS_SECURITY_COMPANY);
				String transNum = transService.getTransNum();
				String userRocId = getUserRocId();
				contractRevocationVo.setLipmId(userRocId);
				int result = transContractRevocationService.insertContractRevocation(transNum, contractRevocationVo,
						getUserDetail() , parameterList);
				if (result <= 0) {
					addAttribute("contractRevocationVo", contractRevocationVo);
					addDefaultSystemError();
					return "forward:contractRevocation3";
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from contractRevocation: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:contractRevocation3";
		}
		return "frontstage/onlineChange/contractRevocation/contractRevocation-success";
	}
	
	protected void sendAuthCode(String authenticationType) {
		try {
			setUserForSendAuth(authenticationType);
			//TODO 檢核需開啟
			// 20210628 輸入驗證碼連續錯誤達幾次，該驗證碼即失效; 有到後台新增 系統常數(SYSTEM_CONSTANTS)
			// AUTH_CHECK_COUNTS 值設為 5
			if (getSession(authenticationType + "Authentication") == null) {
				String email = "";
				String mobile = "";
				UsersVo usersVo = (UsersVo) getSession(UserDataInfo.USER_DETAIL);
				if ("1".equals(usersVo.getMailFlag()) && getSession(authenticationType + "_email") != null) {
					email = getSession(authenticationType + "_email").toString();
				}
				if ("1".equals(usersVo.getSmsFlag()) && getSession(authenticationType + "_mobile") != null) {
					mobile = getSession(authenticationType + "_mobile").toString();
				}
				// String authentication = "0000";//POC後還原
//				sendAuthenticationService.sendAuthentication(email, mobile);
				String authentication = sendAuthenticationService.sendAuthentication(email, mobile);
				logger.info("sendAuthentication authentication(otp code)=" + authentication);

				// 紀錄驗證碼與時間
				addSession(authenticationType + "Authentication", authentication);
				addSession(authenticationType + "AuthenticationTime", new Date());
				addSession(authenticationType + "AuthenticationCheckCounts",
				getParameterValue(ApConstants.SYSTEM_CONSTANTS, "AUTH_CHECK_COUNTS"));
			} else {
				addSession(authenticationType + "AuthenticationTime", new Date());
			}

			setAuthenticationShow(authenticationType);
		} catch (Exception e) {
			logger.error("sendAuthCode error: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	 /**
     * 取得申請明細資料.
     *
     * @param transNum 申請序號
     * @return
     */
    @RequestLog
    @PostMapping("/getTransContractRevocationDetail")
    public String getTransContractRevocationDetail(@RequestParam("transNum") String transNum) {
        try {
        	ContractRevocationVo contractRevocationVo = transContractRevocationService.getTransContractRevocationDetail(transNum);
			if(StringUtils.isNotEmpty(contractRevocationVo.getImage1())) {
				contractRevocationVo.setImage1Tobase64(IMAGE_HEAD + contractRevocationVo.getImage1());
			}
			if(StringUtils.isNotEmpty(contractRevocationVo.getImage2())) {
				contractRevocationVo.setImage2Tobase64(IMAGE_HEAD + contractRevocationVo.getImage2());
			}
            addAttribute("transContractRevocationDetail", contractRevocationVo);
        } catch (Exception e) {
            logger.error("Unable to getTransContractRevocationDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
		return "frontstage/onlineChange/contractRevocation/contractRevocation-detail";
    }
}
