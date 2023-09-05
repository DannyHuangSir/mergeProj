package com.twfhclife.adm.controller.rpt;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.twfhclife.generic.util.SignStatusUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.twfhclife.adm.model.InsClaimStatisticsVo;
import com.twfhclife.adm.service.IOnlineChangeService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 報表查詢-保單理賠申請統計報表.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class InsClaimReportController extends BaseController {

	private static final Logger logger = LogManager.getLogger(InsClaimReportController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;

	/**
	 * 線上申請查詢頁面-結果程式進入點.
	 * 
	 * @return
	 */	
	@RequestLog
	@RequestMapping("/rptInsClaimStatistics")
	public String onlineChange(InsClaimStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return "backstage/rpt/policyClaimsStatisticalReport1";
	}

	/**
	 * 獲取查詢條件
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/rptInsClaimStatistics/filter")
	public String onlineChangeDetail(InsClaimStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return  "backstage/rpt/policyClaimsStatisticalReport2";
	}
	
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/rptInsClaimStatistics/csv")
	public String onlineChangeCSV(InsClaimStatisticsVo claimVo) {
		List reportList = onlineChangeService.getInsClaimStatisticsReport(claimVo);
		if (CollectionUtils.isNotEmpty(reportList)) {
			reportList.forEach(x -> {
				String signStatus = (String) ((Map)x).get("SIGN_STATUS");
				if (StringUtils.isNotBlank(signStatus)) {
					((Map) x).put("SIGN_STATUS", SignStatusUtil.signStatusToStr(signStatus));
				}
				String verifyStatus = (String) ((Map)x).get("ID_VERIFY_STATUS");
				if (StringUtils.isNotBlank(verifyStatus)) {
					((Map) x).put("ID_VERIFY_STATUS", SignStatusUtil.verifyStatusToStr(verifyStatus));
				}
			});
		}
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		addAttribute("hospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList("INSURANCE_CLAIM"));
		return   "backstage/rpt/policyClaimsStatisticalReport3";
	}
	
	
	/**
	 * 線上申請查詢頁面-結果程式進入點
	 * 
	 * @return
	 */	
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "190",
			systemId = ApConstants.SYSTEM_ID
			))
	@RequestMapping("/rptInsClaimDetail")
	public String rptInsClaimDetail(InsClaimStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return  "backstage/rpt/policyClaimsDetailReport1";
	}
	
	/**
	 * 獲取查詢條件
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/rptInsClaimDetail/filter")
	public String rptInsClaimDetailFilter(InsClaimStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return  "backstage/rpt/policyClaimsDetailReport2";
	}
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/rptInsClaimDetail/csv")
	public String rptInsClaimDetailCSV(InsClaimStatisticsVo claimVo) {
		List reportList = onlineChangeService.getInsClaimDetailReport(claimVo);
		if (CollectionUtils.isNotEmpty(reportList)) {
			for (int i = 0; i < reportList.size(); i++) {
				Map x = (Map) reportList.get(i);
				if (x != null) {
					String signStatus = (String) x.get("SIGN_STATUS");
					if (StringUtils.isNotBlank(signStatus)) {
						x.put("SIGN_STATUS", SignStatusUtil.signStatusToStr(signStatus));
					}
					String verifyStatus = (String) x.get("ID_VERIFY_STATUS");
					if (StringUtils.isNotBlank(verifyStatus)) {
						x.put("ID_VERIFY_STATUS", SignStatusUtil.verifyStatusToStr(verifyStatus));
					}
				} else {
					x = Maps.newHashMap();
					reportList.set(i, x);
				}
			}
		}
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		addAttribute("hospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList("INSURANCE_CLAIM"));
		return  "backstage/rpt/policyClaimsDetailReport3";
	}
}
