package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.CaseVo;
import com.twfhclife.eservice.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CaseController extends BaseController {

	@Autowired
	private ICaseService caseService;

	@GetMapping("/caseQuery")
	public String caseQuery() {
		addAttribute("queryCase", new CaseQueryVo());
		addAttribute("autoQuery", false);
		return "frontstage/jdzq/caseQuery/case-query";
	}


	@GetMapping("/returnCase")
	public String returnCase() {
		addAttribute("queryCase", getSession("queryCase"));
		addAttribute("autoQuery", true);
		return "frontstage/jdzq/caseQuery/case-query";
	}

	@PostMapping(value = { "/personalCaseList" })
	@ResponseBody
	public ResponseObj personalCaseList() {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getPersonalCaseList(getLoginUser()));
		return responseObj;
	}

	@PostMapping(value = { "/caseList" })
	@ResponseBody
	public ResponseObj caseList(@RequestBody CaseQueryVo vo) {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getCaseList(getLoginUser(), vo));
		addSession("queryCase", vo);
		return responseObj;
	}

	@RequestMapping(value = { "/caselisting1" })
	public String caselisting1(@RequestParam("policyNo") String policyNo) {
		addAttribute("policyNo", policyNo);
		CaseVo vo = caseService.getCaseProcess(getUserId(), policyNo);
		addAttribute("policyName", vo.getPolicyType());
		addAttribute("vo",  vo);
		return "frontstage/jdzq/caseQuery/caselisting1";
	}

	@RequestMapping(value = { "/caselisting2" })
	public String caselisting2(@RequestParam("policyNo") String policyNo, @RequestParam("policyName") String policyName) {
		addAttribute("policyNo", policyNo);
		addAttribute("policyName", policyName);
		addAttribute("vo",  caseService.getCasePolicyInfo(getUserId(), policyNo));
		return "frontstage/jdzq/caseQuery/caselisting2";
	}

	@RequestMapping(value = { "/caselisting3" })
	public String caselisting3(@RequestParam("policyNo") String policyNo, @RequestParam("policyName") String policyName) {
		addAttribute("policyNo", policyNo);
		addAttribute("policyName", policyName);
		addAttribute("list",  caseService.getNoteContent(getUserId(), policyNo));
		return "frontstage/jdzq/caseQuery/caselisting3";
	}

	@RequestMapping(value = { "/notePdf" })
	public @ResponseBody HttpEntity<byte[]> downloadPolicyClaimPDF(@RequestParam("policyNo") String policyNo,
																   @RequestParam("noteKey") String noteKey) throws Exception {
		HttpHeaders header = new HttpHeaders();
		String fileName = String.format("inline; filename=照會單-%s.pdf", policyNo);
		byte[] document = caseService.getNotePdf(getUserId(), policyNo, noteKey);
		header.setContentType(new MediaType("application", "pdf"));
		header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		header.setContentLength(document.length);
		return new HttpEntity<byte[]>(document, header);
	}
}