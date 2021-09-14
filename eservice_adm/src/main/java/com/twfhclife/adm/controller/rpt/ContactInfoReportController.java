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

import com.twfhclife.adm.model.ContactInfoReportVo;
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
public class ContactInfoReportController extends BaseController {

	private static final Logger logger = LogManager.getLogger(ContactInfoReportController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;

	/**
	 * 線上申請查詢頁面-結果程式進入點.
	 * 
	 * @return
	 */	
	@RequestLog
	@GetMapping("/rptContactInfoStatistics")
	public String onlineChange() {
		return   "backstage/rpt/policyContactInfoStatisticalReport1";
	}

	/**
	 * 獲取查詢條件
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/rptContactInfoStatistics/filter")
	public String onlineChangeDetail(ContactInfoReportVo claimVo) {
		addAttribute("claimVo", claimVo);
		
		return   "backstage/rpt/policyContactInfoStatisticalReport2";
	}
	
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/rptContactInfoStatistics/csv")
	public String onlineChangeCSV(ContactInfoReportVo claimVo) {
		List reportList = onlineChangeService.getContactInfoStatisticsReport(claimVo);
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		return   "backstage/rpt/policyContactInfoStatisticalReport3";
	}
	
	

	/**
	 * 查詢明細頁面
	 * 
	 * @return
	 */	
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "190",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/rptContactInfoDetail")
	public String rptContactInfoDetail() {
		
		return  "backstage/rpt/policyContactInfoDetailReport1";
	}
	
	/**
	 * 獲取查詢條件
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/rptContactInfoDetail/filter")
	public String rptContactInfoDetailFilter(ContactInfoReportVo claimVo) {
		addAttribute("claimVo", claimVo);
		return  "backstage/rpt/policyContactInfoDetailReport2";
	}
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/rptContactInfoDetail/csv")
	public String rptContactInfoDetailCSV(ContactInfoReportVo claimVo) {
		List reportList = onlineChangeService.getContactInfoDetailReport(claimVo);
		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		return  "backstage/rpt/policyContactInfoDetailReport3";
	}
}
