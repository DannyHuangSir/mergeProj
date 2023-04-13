package com.twfhclife.eservice.onlineChange.controller;

import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.ContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.TransRolloverPeriodicallyVo;
import com.twfhclife.eservice.onlineChange.service.ITransRolloverPeriodicallyService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.generic.api_client.TransCspApiUtilClient;
import com.twfhclife.generic.api_client.TransCtcLilipmClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.Csp001Data;
import com.twfhclife.generic.api_model.Csp001Detail;
import com.twfhclife.generic.api_model.CspData;
import com.twfhclife.generic.api_model.TransCspApiUtilRequest;
import com.twfhclife.generic.api_model.TransCspApiUtilResponse;
import com.twfhclife.generic.api_model.TransCtcLilipmRequest;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;
import com.twfhclife.generic.api_model.TransCtcSelectUtilRequest;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;

@Controller
public class TransRolloverPeriodicallyController extends BaseUserDataController {
	
	private static final Logger logger = LogManager.getLogger(TransRolloverPeriodicallyController.class);
	
	@Autowired
	private ITransService transService;

	@Autowired
	private ITransRolloverPeriodicallyService transRolloverPeriodicallyService ;
	
	@Autowired
	private TransCtcLilipmClient transCtcLilipmClient;
	
	@Autowired
	private TransCspApiUtilClient transCspApiUtilClient;
	
	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;
	/**
	 * 取得展期定期保險畫面資料
	 * @return
	 */
	@RequestLog
	@GetMapping("/rolloverPeriodically1")
	public String getRolloverPeriodically(RedirectAttributes redirectAttributes) {
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
			 * 2.查詢保單資料
			 */
			String userRocId = getUserRocId();
			String userId = getUserId();
			TransCtcSelectUtilRequest utilRequest = new TransCtcSelectUtilRequest();
			utilRequest.setLipmId(userRocId);
			TransCtcSelectDataAddCodeResponse codeResponse = new TransCtcSelectDataAddCodeResponse();
			codeResponse = transCtcSelectUtilClient.getTransCtcSelectDataAddCode(utilRequest);
			List<TransCtcSelectDataAddCodeVo> dataAddCodeList = new ArrayList<TransCtcSelectDataAddCodeVo>();
			dataAddCodeList = codeResponse.getDataAddCodeList();
			if(dataAddCodeList.size() > 0) {
				Map<String , TransCtcSelectDataAddCodeVo> map = dataAddCodeList.stream().collect(Collectors.toMap(TransCtcSelectDataAddCodeVo :: getLipmInsuNo , Function.identity() , 
						(o1 , o2) -> o1	, ConcurrentHashMap :: new));
				List<TransCtcSelectDataAddCodeVo> policyList = map.values().stream().collect(Collectors.toList());
				policyList = transRolloverPeriodicallyService.getRuleResult(policyList, userId );
				/**
				 * 3.畫面資料根據狀態排序
				 */
				policyList = policyList.stream().sorted(Comparator.comparing(TransCtcSelectDataAddCodeVo::getLipmInsuNo))
						.collect(Collectors.toList());
				policyList = policyList.stream().sorted(Comparator.comparing(TransCtcSelectDataAddCodeVo::getApplyLockedFlag))
						.collect(Collectors.toList());
				addAttribute("policyList", policyList);
			}else {
				redirectAttributes.addFlashAttribute("errorMessage", "查無展期定期保險資料，請洽服務人員");
				return "redirect:apply1";
			}
			
		} catch (Exception e) {
			logger.error("Unable to init from rolloverPeriodically1: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/rolloverPeriodically/rolloverPeriodically1";
	}
	
	/**
	 * 取得驗證畫面資料、驗證碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/rolloverPeriodically2")
	public String rolloverPeriodically2(TransRolloverPeriodicallyVo transRolloverPeriodicallyVo,
			BindingResult bindingResult) {
		try {
			// 根據最新保單帶出變更前的要保人聯絡資料
			String policyNo = "";
			if (StringUtils.isNotEmpty(transRolloverPeriodicallyVo.getPolicyNo())) {
				policyNo = transRolloverPeriodicallyVo.getPolicyNo();
			} else {
				policyNo = transRolloverPeriodicallyVo.getPolicyNoList().get(0);
			}
			// 查詢CTC 取得畫面資料
			TransCtcLilipmRequest request = new TransCtcLilipmRequest();
			request.setPolicyNo(policyNo);
			TransCtcLilipmResponse resp = transCtcLilipmClient.getTransCtcLilipmDetail(request);
			transRolloverPeriodicallyVo = transRolloverPeriodicallyService.getLilipmDetail(resp);
			// 查詢 減額繳清資料
			TransCspApiUtilRequest utilRequest = new TransCspApiUtilRequest();
			utilRequest.setInsuSeqNo(policyNo);
			utilRequest.setPolicyType("E"); // Y : 繳清 ; E : 展延
			TransCspApiUtilResponse response =transCspApiUtilClient.getTransCsp001Api(utilRequest);
			if(response != null) {
				CspData cspData = response.getCspData();
				if(cspData.getSuccess().equals("true")) {
					Csp001Data csp001Data = cspData.getData();
					if(csp001Data.getDetailStatus().equals("0")) {
						if(csp001Data.getValues().size() > 0) {
							Csp001Detail csp001Detail = csp001Data.getValues().get(0);
							String ds16Dsec =csp001Detail.getDs16Dsec();
							String ds16ExtendTermDate =csp001Detail.getDs16ExtendTermDate();
							String ds16NewAmt =csp001Detail.getDs16NewAmt();
							if(StringUtils.isEmpty(ds16NewAmt) || StringUtils.isEmpty(ds16ExtendTermDate) || StringUtils.isNotEmpty(ds16Dsec)) {
								addSystemError(ds16Dsec);
								return getRolloverPeriodically(null);
							}else {
								transRolloverPeriodicallyVo.setRolloverAmt(Integer.valueOf(ds16NewAmt).toString());
								StringBuilder sb = new StringBuilder();
								sb.append(Integer.valueOf(ds16ExtendTermDate.substring(0,4)));
								sb.append("年");
								sb.append(Integer.valueOf(ds16ExtendTermDate.substring(4,6)));
								sb.append("月");
								sb.append(Integer.valueOf(ds16ExtendTermDate.substring(6,8)));
								sb.append("日");
								transRolloverPeriodicallyVo.setRolloverDate(ds16ExtendTermDate);
								transRolloverPeriodicallyVo.setRolloverDate2(sb.toString());
							}
						}else {
							logger.error("select CSP Detail Values Error :" + cspData);
							addSystemError("查無要展期定期保險的資料，請您至臨櫃辦理，謝謝。");
							return getRolloverPeriodically(null);
						}					
					}else {
						logger.error("select CSP Detail Status Error :" + cspData);
						addSystemError("查無要展期定期保險的資料，請您至臨櫃辦理，謝謝。");
						return getRolloverPeriodically(null);
					}				
				}else {
					logger.error("select CSP Detail Success Error :" + cspData);
					addSystemError("查無要展期定期保險的資料，請您至臨櫃辦理，謝謝。");
					return getRolloverPeriodically(null);
				}
			}else {
				logger.error("select CSP Detail Success Error : response null" );
				addSystemError("查無要展期定期保險的資料，請您至臨櫃辦理，謝謝。");
				return getRolloverPeriodically(null);
			}
			
			transRolloverPeriodicallyVo.setPolicyNo(policyNo);
			addAttribute("rolloverPeriodicallyVo", transRolloverPeriodicallyVo);
			sendAuthCode("transRolloverPeriodically");
		} catch (Exception e) {
			logger.error("Unable to init from rolloverPeriodically2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/onlineChange/rolloverPeriodically/rolloverPeriodically2";
	}
	
	/**
	 * 申請完成頁面(驗證申請驗證碼).
	 * @return
	 */
	@RequestLog
	@PostMapping("/rolloverPeriodicallySuccess")
	public String rolloverPeriodicallySuccess(TransRolloverPeriodicallyVo transRolloverPeriodicallyVo, BindingResult bindingResult) {

		try {
			boolean isTransApplyed = false;
			boolean applyed = false;
			/**
			 * 1.判斷是否有申請過的的單子
			 */
			String policyNo = transRolloverPeriodicallyVo.getPolicyNo();
			applyed = transService.isTransApplyed(policyNo, TransTypeUtil.ROLLOVER_PERIODICALLY,
					OnlineChangeUtil.TRANS_STATUS_RECEIVED);
			if (applyed) {
				isTransApplyed = true;
			}
			
			/**
			 * 2. 沒有申請過的單子才開始進行新增動作
			 */
			if (!isTransApplyed) {
				// 驗證驗證碼
				String authNum = transRolloverPeriodicallyVo.getAuthenticationNum();
				String msg = checkAuthCode("transRolloverPeriodically", authNum);
				if (!StringUtils.isEmpty(msg)) {
					transRolloverPeriodicallyVo.setAuthenticationNum(null);
					addAttribute("transRolloverPeriodicallyVo", transRolloverPeriodicallyVo);
					addSystemError(msg);
					return "forward:rolloverPeriodically2";
				}

				String transNum = transService.getTransNum();
				int result = 	transRolloverPeriodicallyService.insertRolloverPeriodically(transNum, transRolloverPeriodicallyVo, getUserDetail());
					if (result <= 0) {
						addAttribute("transRolloverPeriodicallyVo", transRolloverPeriodicallyVo);
						addDefaultSystemError();
						return "forward:rolloverPeriodically2";
					}
			}
		} catch (Exception e) {
			logger.error("Unable to init from transRolloverPeriodicallyVo: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
			return "forward:rolloverPeriodically2";
		}
		return "frontstage/onlineChange/rolloverPeriodically/rolloverPeriodically-success";
	}
	
	/**
	 * 取得申請紀錄明細
	 * @return
	 */
	@RequestLog
	@PostMapping("/getTransRolloverPeriodicallyDetail")
	public String getTransRolloverPeriodicallyDetail(@RequestParam("transNum") String transNum) {
		 try {
			 TransRolloverPeriodicallyVo contractRevocationVo = transRolloverPeriodicallyService.getTransRolloverPeriodicallyDetail(transNum);
			 String rolloverDate = contractRevocationVo.getRolloverDate();
			 rolloverDate = rolloverDate.substring(0, 3) + "年" + rolloverDate.substring(4, 6) + "月" + rolloverDate.substring(7,9) + "日";
			 contractRevocationVo.setRolloverDate(rolloverDate);
	            addAttribute("transRolloverPeriodicallyDetail", contractRevocationVo);
	        } catch (Exception e) {
	            logger.error("Unable to getTransRolloverPeriodicallyDetail: {}", ExceptionUtils.getStackTrace(e));
	            addDefaultSystemError();
	        }
			return "frontstage/onlineChange/rolloverPeriodically/rolloverPeriodically-detail";
	}
	
}
