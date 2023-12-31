package com.twfhclife.eservice.policy.controller;

import java.util.List;
import java.util.Map;

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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.model.FundValueVo;
import com.twfhclife.eservice.policy.model.PolicyVo;
import com.twfhclife.eservice.policy.model.ProductVo;
import com.twfhclife.eservice.policy.service.IFundValueService;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.ValidateUtil;

/**
 * 投資標的淨值.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class ProdNetValueController extends BasePolicyController {

	private static final Logger logger = LogManager.getLogger(ProdNetValueController.class);

	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;
	
	@Autowired
	private IPortfolioService portfolioService;

	@Autowired
	private IFundValueService fundValueService;
	
	/**
	 * 投資標的淨值.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/listing11")
	public String listing11(@RequestParam("policyNo") String policyNo) {
		try {
			if(!ValidateUtil.isValid(policyNo)) {
				addDefaultSystemError();
			}else {
				PolicyVo policyVo = policyListService.findById(policyNo);
				if (policyVo != null) {
					String twEffectiveDate = DateUtil.getRocDate(policyVo.getEffectiveDate());
					String twExpireDate = DateUtil.getRocDate(policyVo.getExpireDate());
					
					addAttribute("policyVo", policyVo);
					addAttribute("policyStartEndDate", String.format("%s ~ %s", twEffectiveDate, twExpireDate));
					
					// 取得商品
					ProductVo productVo = policyListService.findProductByPolicyNo(policyNo);
					
					// 取得指定年月的開賣日
					if (productVo != null) {
						String dateStr = DateUtil.formatDateTime(productVo.getDesiSaleDate(), DateUtil.FORMAT_WEST_DATE);
						List<String> rocYearMenu = DateUtil.getYearOpitonByEffectDate(dateStr);
						addAttribute("rocYearMenu", rocYearMenu);
						addAttribute("effectDate", dateStr);
					}
					
					addAttribute("portfolioList", portfolioService.getInvtOptionList(policyNo));

					Map<String, Map<String, ParameterVo>> sysParamMap = (Map<String, Map<String, ParameterVo>>) getSession(ApConstants.SYSTEM_PARAMETER);
					Map<String, ParameterVo> currency = sysParamMap.get("CURRENCY");
					if (policyVo.getCurrency() != null) {
						addAttribute("currName", currency.get(policyVo.getCurrency()).getParameterValue());
					}
				}

				// 要保人
				LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
				addAttribute("lilipmVo", lilipmVo);
				
				// 被保人
				LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
				addAttribute("lilipiVo", lilipiVo);
			}
		} catch (Exception e) {
			logger.error("Unable to get policy info from listing11: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontstage/listing11";
	}

	/**
	 * 取得投資標的商品的歷史記錄.
	 * 
	 * @param fundValueVo FundValueVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/getNetValueChartList")
	public ResponseEntity<ResponseObj> getNetValueChartList(@RequestBody FundValueVo fundValueVo) {
		try {
			processSuccess(fundValueService.getFundValue(fundValueVo));
		} catch (Exception e) {
			logger.error("Unable to getNetValueChartList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}