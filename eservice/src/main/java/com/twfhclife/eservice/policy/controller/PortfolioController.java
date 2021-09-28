package com.twfhclife.eservice.policy.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.PortfolioClient;
import com.twfhclife.generic.api_model.PortfolioResponse;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 投資損益.
 * 
 * @author alan
 */
@Controller
public class PortfolioController extends BaseController {

	private static final Logger logger = LogManager.getLogger(PortfolioController.class);
	
	/**
	 * 投資組合服務
	 */
	@Autowired
	private IPortfolioService portfolioService;
	
	@Autowired
	private PortfolioClient portfolioClient;

	/**
	 * 投資損益及投報率.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing2")
	public String listing2(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			String riskLevelName = "";
			
			// Call api 取得資料
			PortfolioResponse portfolioResponse = portfolioClient.getPolicyRateOfReturn(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (portfolioResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyloanByPolicyNo]", userId);
				riskLevelName = portfolioResponse.getRiskLevelName();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyloanByPolicyNo data", userId);
				riskLevelName = portfolioService.getRiskLevelName(policyNo);
			}
			addAttribute("riskLevelName", riskLevelName);
			
			// 投資型保單畫面UP UQ UR 畫面顯示修改 
			//DOWN_LISTING_2 投資損益及投報率修改
			Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
			if (sysParamMap.containsKey("SYSTEM_CONSTANTS")) {
				Map<String, ParameterVo> paraMap = sysParamMap.get("SYSTEM_CONSTANTS");
				ParameterVo vo = (ParameterVo) paraMap.get("INVEST_CODE");
				ParameterVo vo2 = (ParameterVo) paraMap.get("DOWN_LISTING_2");
				String[] investCode = vo.getParameterValue().split(",");
				String[] downListing = vo2.getParameterValue().split(",");
				for (String insuType : investCode) {
					if (policyNo.startsWith(insuType)) {
						return "frontstage/listing2-1";
					}
				}
				for (String insuType2 : downListing) {
					if (policyNo.startsWith(insuType2)) {
						return "frontstage/listing2-2";
					}
				}
			}
		} catch (Exception e) {
			logger.error("Unable to getRiskLevelName from listing2: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		
		return "frontstage/listing2";
	}
	
	/**
	 * 取得取得投資損益及投報率清單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return
	 */
	@RequestLog
	@PostMapping("/getPortfolioList")
	public ResponseEntity<ResponseObj> getPortfolioList(@RequestParam("policyNo") String policyNo) {
		try {
			String userId = getUserId();
			List<PortfolioVo> portfolioList = null;
			
			// Call api 取得資料
			PortfolioResponse portfolioResponse = portfolioClient.getPolicyRateOfReturn(userId, policyNo);
			// 若無資料，嘗試由內部服務取得資料
			if (portfolioResponse != null) {
				logger.info("Get user[{}] data from eservice_api[getPolicyloanByPolicyNo]", userId);
				portfolioList = portfolioResponse.getPortfolioList();
			} else {
				logger.info("Call internal service to get user[{}] getPolicyloanByPolicyNo data", userId);
				portfolioList = portfolioService.getPortfolioList(policyNo, null);
			}
			processSuccess(portfolioList);
		} catch (Exception e) {
			logger.error("Unable to getPortfolioList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
