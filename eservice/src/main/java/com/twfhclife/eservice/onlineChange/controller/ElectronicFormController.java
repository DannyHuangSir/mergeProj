package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.ElectronicFormVo;
import com.twfhclife.eservice.onlineChange.service.ITransElectronicFormService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeNoteUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_client.TransCtcLipmdaClient;
import com.twfhclife.generic.api_model.TransCtcLipmdaRequest;
import com.twfhclife.generic.api_model.TransCtcLipmdaResponse;
import com.twfhclife.generic.api_model.TransCtcLipmdaVo;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.service.ISendAuthenticationService;
import com.twfhclife.generic.util.ApConstants;

@Controller
public class ElectronicFormController extends BaseUserDataController {

	private static final Logger logger = LogManager.getLogger(ElectronicFormController.class);

	@Autowired
	private ITransService transService;

	@Autowired
	private ISendAuthenticationService sendAuthenticationService;

	@Autowired
	private ITransElectronicFormService transElectronicFormService;

	@Autowired
	protected HttpSession session;

	@Autowired
	private TransCtcLipmdaClient transCtcLipmdaClient ;

	/**
	 * 取得申請/取消按鈕狀態
	 * @return
	 */
	@RequestLog
	@GetMapping("/electronicForm1")
	public String electronicForm1(RedirectAttributes redirectAttributes) {		
		ElectronicFormVo electronicFormVo = new ElectronicFormVo();
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
			
			String userRocId = getUserRocId();
			TransCtcLipmdaRequest req = new TransCtcLipmdaRequest();
			req.setUserId(userRocId);
			TransCtcLipmdaResponse resp = transCtcLipmdaClient.getTransCtcLipmdaDetail(req);
			//判斷是否可以選擇 "申請/取消" 按鈕
			for (TransCtcLipmdaVo vo : resp.getLipmdaList()) {
				if (StringUtils.isNotEmpty(vo.getPmdaEInfoN())) {
					if (vo.getPmdaEInfoN().equals("Y")) {
						electronicFormVo.setIsclean(true); // 有申請過電子帳單 需要可以取消
					} else if (vo.getPmdaEInfoN().equals("N")) {
						electronicFormVo.setElectronicForm(true); // 已取消過電子帳單需要重新申請
					}
				} else {
					electronicFormVo.setElectronicForm(true); // 已取消過電子帳單需要重新申請
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from  electronicForm: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		addAttribute("electronicFormVo", electronicFormVo);
		return "frontstage/onlineChange/electronicForm/electronic-form";
	}

	/**
	 * 取得申請電子表單畫面資料
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestLog
	@GetMapping("/electronicForm2")
	public String electronicForm2(RedirectAttributes redirectAttributes) {
		try {
			/**
			 * 1.查詢保單資料
			 */
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<ElectronicFormVo> electronicFormVoList =  transElectronicFormService.getPolicyDetail(userRocId ,userId , TransTypeUtil.ELECTRONIC_FORM_A_CODE);
			//過濾重複資料
			Map<String , ElectronicFormVo> map = electronicFormVoList.stream().collect(Collectors.toMap(ElectronicFormVo :: getPolicyNo, Function.identity() , 
					(o1 , o2)-> o1 , ConcurrentHashMap :: new ));
			List<ElectronicFormVo> checkPolicyList = map.values().stream().collect(Collectors.toList());
			/**
			 * 2.呼叫CTC查詢資料
			 */
			TransCtcLipmdaRequest req = new TransCtcLipmdaRequest();
			req.setUserId(userRocId);
			TransCtcLipmdaResponse resp = transCtcLipmdaClient.getTransCtcLipmdaDetail(req);
			Map clipmdaMap = mapList(resp.getLipmdaList());
			/**
			 * 3.整合保單與CTC資料 顯示於畫面
			 */
			List<ElectronicFormVo> policyList = new ArrayList<ElectronicFormVo>();
			for (ElectronicFormVo vo : checkPolicyList) {
				TransCtcLipmdaVo lipmadaVo = (TransCtcLipmdaVo) clipmdaMap.get(vo.getPolicyNo());
				if(!ObjectUtils.isEmpty(lipmadaVo)) {
					if(StringUtils.isEmpty(vo.getApplyLockedFlag())) {
						mappingElectronicFormA(vo , lipmadaVo);
					}else if(vo.getApplyLockedFlag().equals("N")){
						mappingElectronicFormA(vo , lipmadaVo);
					}else {
						mappingApplyLockedFlag(vo , lipmadaVo);
					}
					policyList.add(vo);
				}					
			}
			/**
			 * 4.畫面資料根據狀態排序
			 */
			policyList = policyList.stream()
					.sorted(Comparator.comparing(ElectronicFormVo::getPmdaEInfoNStatus)).collect(Collectors.toList());
			policyList = policyList.stream()
					.sorted(Comparator.comparing(ElectronicFormVo::getApplyLockedFlag)).collect(Collectors.toList());
			addAttribute("policyList", policyList);
			addAttribute("electronicFormStatus", "application");			
			addAttribute("electronicTitle", getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
					OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_APPLICATION));		
		} catch (Exception e) {
			logger.error("Unable to init from electronicForm2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/electronicForm/electronic-form1";
	}

	/**
	 * 取得取消電子表單畫面資料
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestLog
	@GetMapping("/electronicForm3")
	public String electronicForm3(RedirectAttributes redirectAttributes) {
		try {
			/**
			 * 1.查詢保單資料
			 */
			String userRocId = getUserRocId();
			String userId = getUserId();
			List<ElectronicFormVo> electronicFormVoList =  transElectronicFormService.getPolicyDetail(userRocId ,userId , TransTypeUtil.ELECTRONIC_FORM_C_CODE);
			//過濾重複資料
			Map<String , ElectronicFormVo> map = electronicFormVoList.stream().collect(Collectors.toMap(ElectronicFormVo :: getPolicyNo, Function.identity() , 
					(o1 , o2)-> o1 , ConcurrentHashMap :: new ));
			List<ElectronicFormVo> checkPolicyList = map.values().stream().collect(Collectors.toList());
			/**
			 * 2.呼叫CTC查詢資料
			 */
			TransCtcLipmdaRequest req = new TransCtcLipmdaRequest();
			req.setUserId(userRocId);
			TransCtcLipmdaResponse resp = transCtcLipmdaClient.getTransCtcLipmdaDetail(req);
			Map clipmdaMap = mapList(resp.getLipmdaList());
			/**
			 * 3.整合保單與CTC資料 顯示於畫面
			 */
			List<ElectronicFormVo> policyList = new ArrayList<ElectronicFormVo>();
			for (ElectronicFormVo vo : checkPolicyList) {
				TransCtcLipmdaVo lipmadaVo = (TransCtcLipmdaVo) clipmdaMap.get(vo.getPolicyNo());
				if(!ObjectUtils.isEmpty(lipmadaVo)) {
					if(StringUtils.isEmpty(vo.getApplyLockedFlag())) {
						mappingElectronicFormC(vo , lipmadaVo);
					}else if(vo.getApplyLockedFlag().equals("N")){
						mappingElectronicFormC(vo , lipmadaVo);
					}else {
						mappingApplyLockedFlag(vo , lipmadaVo);
					}
					policyList.add(vo);
				}					
			}
			/**
			 * 4.畫面資料根據狀態排序
			 */
			policyList = policyList.stream()
					.sorted(Comparator.comparing(ElectronicFormVo::getPmdaEInfoNStatus)).collect(Collectors.toList());
			policyList = policyList.stream()
					.sorted(Comparator.comparing(ElectronicFormVo::getApplyLockedFlag)).collect(Collectors.toList());
			addAttribute("policyList", policyList);
			addAttribute("electronicFormStatus", "cancel");			
			addAttribute("electronicTitle", getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
					OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_CANCEL));
		} catch (Exception e) {
			logger.error("Unable to init from electronicForm3: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/electronicForm/electronic-form1";
	}
	
	/**
	 * 取得驗證畫面資料、驗證碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/electronicForm4")
	public String electronicForm4(ElectronicFormVo electronicFormVo , BindingResult bindingResult) {
		try {
			// 根據最新保單帶出變更前的要保人聯絡資料
			List<String> policyNos = electronicFormVo.getPolicyNoList();
			if (!CollectionUtils.isEmpty(policyNos)) {
				String userRocId = getUserRocId();
				//查詢CTC取得Mail
				TransCtcLipmdaRequest req = new TransCtcLipmdaRequest();
				req.setUserId(userRocId);
				TransCtcLipmdaResponse resp = transCtcLipmdaClient.getTransCtcLipmdaDetail(req);
				Map clipmdaMap = mapList(resp.getLipmdaList());
				List<TransCtcLipmdaVo> clipmdas = new ArrayList<>();
				for (String policyNo : policyNos) {
					TransCtcLipmdaVo vo = new TransCtcLipmdaVo();
					vo = (TransCtcLipmdaVo) clipmdaMap.get(policyNo);
					clipmdas.add(vo);
				}
				electronicFormVo.setClopmdaVo(clipmdas);
			}
			addAttribute("electronicFormStatus", electronicFormVo.getElectronicFormStatus());
			addAttribute("electronicTitle", electronicFormVo.getTitleName());
			addAttribute("electronicFormVo", electronicFormVo);
			addAttribute("timeSet", 300);
			sendAuthCode("electronicForm");
			session.setAttribute("electronicFormVo", electronicFormVo);
		} catch (Exception e) {
			logger.error("Unable to init from electronicForm4: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/electronicForm/electronic-form2";
	}

	/**
	 * 取得各筆保單明細內容
	 * @return
	 */
	@RequestLog
	@GetMapping("/getUserElectronicFormNetWorkData")
	public String getUserElectronicFormNetWorkData(@RequestParam("policyNo") String policyNo) {
		addAttribute("userElectronic", transElectronicFormService.getUserCurrentNetworkData(policyNo));
		return "frontstage/onlineChange/electronicForm/electronic-form-detail";
	}

	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * @return
	 */
	@RequestLog
	@PostMapping("/electronicFormSuccess")
	public String electronicFormSuccess(ElectronicFormVo electronicFormVo , BindingResult bindingResult) {
//		BindingResult bindingResult
		try {
			boolean isTransApplyed = false;
			boolean applyed = false;
			/**
			 * 1.判斷是否有申請過的的單子
			 */
			List<String> policyNos = electronicFormVo.getPolicyNoList();
			for (String policyNo : policyNos) {
				if (electronicFormVo.getElectronicFormStatus().equals("application")) {
					applyed = transService.isTransApplyed(policyNo, TransTypeUtil.ELECTRONIC_FORM_A_CODE,
							OnlineChangeUtil.TRANS_STATUS_RECEIVED);
				} else {
					applyed = transService.isTransApplyed(policyNo, TransTypeUtil.ELECTRONIC_FORM_C_CODE,
							OnlineChangeUtil.TRANS_STATUS_RECEIVED);
				}
				if (applyed) {
					isTransApplyed = true;
					break;
				}
			}

			/**
			 * 2. 沒有申請過的單子才開始進行新增動作 
			 */
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = electronicFormVo.getAuthenticationNum();
				String msg = checkAuthCode("electronicForm", authNum);
				if (!StringUtils.isEmpty(msg)) {
					electronicFormVo.setAuthenticationNum(null);
					addAttribute("electronicFormVo", electronicFormVo);
					addSystemError(msg);
					return "forward:electronicForm4";
				}
				electronicFormVo = (ElectronicFormVo) session.getAttribute("electronicFormVo");
				for (String policyNo : policyNos) {
					String transNum = transService.getTransNum();
					int result = transElectronicFormService.insertElectronicForm(transNum, electronicFormVo,
							getUserId(), policyNo, getUserDetail());
					if (result <= 0) {
						addAttribute("electronicFormVo", electronicFormVo);
						addDefaultSystemError();
						return "forward:electronicForm4";
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to init from electronic-form: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:electronicForm4";
		}
		return "frontstage/onlineChange/electronicForm/electronic-form-success";
	}

	protected void sendAuthCode(String authenticationType) {
		try {
			setUserForSendAuth(authenticationType);

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

	private Map mapList(List<TransCtcLipmdaVo> clipmdaList) {
		Map map = new HashMap();
		for (TransCtcLipmdaVo vo : clipmdaList) {
			map.put(vo.getLipmInsuNo(), vo);
		}
		return map;
	}
	
	private void mappingElectronicFormA( ElectronicFormVo vo , TransCtcLipmdaVo lipmadaVo) {
		// 申請電子通知時，電子通知申請欄位須為null 且mail不得為空
		if (StringUtils.isEmpty(lipmadaVo.getPmdaEInfoN())) {
			vo.setPmdaEInfoNStatus("未開通");
			if (StringUtils.isNotEmpty(lipmadaVo.getPmdaApplicantEmail())) {
				vo.setApplyLockedFlag("N");
			} else {
				vo.setApplyLockedFlag("Y");
				vo.setApplyLockedMsg(getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
						OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_MAIL));
			}
		} else {
			if (lipmadaVo.getPmdaEInfoN().equals("N")) {
				vo.setPmdaEInfoNStatus("未開通");
				if (StringUtils.isNotEmpty(lipmadaVo.getPmdaApplicantEmail())) {
					vo.setApplyLockedFlag("N");
				} else {
					vo.setApplyLockedFlag("Y");
					vo.setApplyLockedMsg(getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
							OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_MAIL));
				}
			} else {
				vo.setApplyLockedFlag("Y");
				vo.setPmdaEInfoNStatus("開通");
				vo.setApplyLockedMsg(getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
						OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_ERROR));
			}
		}
	}
	
	private void mappingElectronicFormC( ElectronicFormVo vo , TransCtcLipmdaVo lipmadaVo) {
		// 申請電子通知時，電子通知申請欄位須為null 且mail不得為空
		if (StringUtils.isEmpty(lipmadaVo.getPmdaEInfoN())) {
			vo.setApplyLockedFlag("Y");
			vo.setPmdaEInfoNStatus("未開通");
			vo.setApplyLockedMsg(getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
					OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_MAIL));
		} else {
			if (lipmadaVo.getPmdaEInfoN().equals("N")) {
				vo.setApplyLockedFlag("Y");
				vo.setPmdaEInfoNStatus("未開通");
				vo.setApplyLockedMsg(getParameterValue(ApConstants.PAGE_WORDING_CATEGORY_CODE,
						OnlineChangeNoteUtil.ELECTRONIC_FORM_CODE_MAIL));
			} else {
				vo.setApplyLockedFlag("N");
				vo.setPmdaEInfoNStatus("開通");
			}
		}
	}
	
  private void mappingApplyLockedFlag(ElectronicFormVo vo , TransCtcLipmdaVo lipmadaVo) {
	  if (StringUtils.isEmpty(lipmadaVo.getPmdaEInfoN())) {
			vo.setPmdaEInfoNStatus("未開通");
		} else {
			if (lipmadaVo.getPmdaEInfoN().equals("N")) {
				vo.setPmdaEInfoNStatus("未開通");
			} else {
				vo.setPmdaEInfoNStatus("開通");
			}
		}
  }

}
