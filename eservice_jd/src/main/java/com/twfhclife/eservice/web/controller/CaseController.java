package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.model.CaseVo;
import com.twfhclife.eservice.web.model.PolicySafeGuardVo;
import com.twfhclife.eservice.web.service.ICaseService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CaseController extends BaseController {

	@Autowired
	private ICaseService caseService;

	@GetMapping("/caseQuery")
	public String caseQuery() {
		return "frontstage/jdzq/caseQuery/case-query";
	}

	@PostMapping(value = { "/personalCaseList" })
	@ResponseBody
	public ResponseObj personalCaseList() {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getCaseList(getLoginUser(), null));
		return responseObj;
	}

	@PostMapping(value = { "/caseList" })
	@ResponseBody
	public ResponseObj caseList(@RequestBody CaseQueryVo vo) {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getCaseList(getLoginUser(), vo));
		return responseObj;
	}

	@RequestMapping(value = { "/caselisting1" })
	public String caselisting1(@RequestParam("policyNo") String policyNo) {
		addAttribute("policyNo", policyNo);
		addAttribute("vo",  caseService.getCaseProcess(policyNo));
		return "frontstage/jdzq/caseQuery/caselisting1";
	}

	@RequestMapping(value = { "/caselisting2" })
	public String caselisting2(@RequestParam("policyNo") String policyNo) {
		addAttribute("policyNo", policyNo);
		addAttribute("vo",  caseService.getCasePolicyInfo(policyNo));
		return "frontstage/jdzq/caseQuery/caselisting2";
	}

	@RequestMapping(value = { "/caselisting3" })
	public String caselisting3(@RequestParam("policyNo") String policyNo) {
		addAttribute("policyNo", policyNo);
		return "frontstage/jdzq/caseQuery/caselisting3";
	}
}