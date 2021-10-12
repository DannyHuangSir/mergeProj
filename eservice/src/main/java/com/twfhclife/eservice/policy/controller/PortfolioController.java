package com.twfhclife.eservice.policy.controller;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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

	@Autowired
	private ITransRiskLevelService riskLevelService;

	@Autowired
	private ITransInvestmentService transInvestmentService;

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

			String riskLevel = riskLevelService.getUserRiskAttr(getUserRocId());
			addAttribute("riskLevelName", transInvestmentService.transRiskLevelToName(riskLevel));
			// 投資型保單畫面UP UQ UR 畫面顯示修改
			//DOWN_LISTING_2 投資損益及投報率修改
			List<ParameterVo> parameterVos = getParameterList("LISTING_MAP_TEMPLATE");
			if (!CollectionUtils.isEmpty(parameterVos)) {
				for (ParameterVo parameterVo : parameterVos) {
					if (parameterVo.getParameterCode().endsWith(policyNo.substring(0, 2))) {
						return parameterVo.getParameterValue();
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
