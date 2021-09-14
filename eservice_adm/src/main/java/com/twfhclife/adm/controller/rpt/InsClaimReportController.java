package com.twfhclife.adm.controller.rpt;

import java.util.List;
import java.util.Map;

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
	@GetMapping("/rptInsClaimStatistics")
	public String onlineChange() {
		return   "backstage/rpt/policyClaimsStatisticalReport1";
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
		
		return   "backstage/rpt/policyClaimsStatisticalReport2";
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
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
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
	@GetMapping("/rptInsClaimDetail")
	public String rptInsClaimDetail() {
		
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
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		return  "backstage/rpt/policyClaimsDetailReport3";
	}
}
