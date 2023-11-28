package com.twfhclife.jd.web.controller;

import com.twfhclife.jd.controller.BaseController;
import com.twfhclife.jd.keycloak.model.KeycloakUser;
import com.twfhclife.jd.web.dao.UsersDao;
import com.twfhclife.jd.web.domain.CaseQueryVo;
import com.twfhclife.jd.web.domain.ResponseObj;
import com.twfhclife.jd.web.domain.PersonSortVo;
import com.twfhclife.jd.web.model.CaseVo;
import com.twfhclife.jd.web.model.DepartmentVo;
import com.twfhclife.jd.web.service.ICaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CaseController extends BaseController {

	@Autowired
	private ICaseService caseService;

	@Autowired
	private UsersDao usersDao;

	@GetMapping("/caseQuery")
	public String caseQuery() {
		addAttribute("queryCase", new CaseQueryVo());
		addAttribute("autoQuery", false);

		KeycloakUser user = getLoginUser();
		// role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員 5 平台管理人員
		int role = usersDao.checkUserRole(user.getId());
		addAttribute("role", role);

		if(role == 3 || role == 4 || role == 5 ) {
			List<DepartmentVo> deptParentList = usersDao.getDeptParentList(user.getId(), role);
			addAttribute("deptParentList", deptParentList);
		}
		return "frontstage/jdzq/caseQuery/case-query";
	}


	@GetMapping("/returnCase")
	public String returnCase() {
		CaseQueryVo vo = (CaseQueryVo) getSession("queryCase");
		addAttribute("queryCase", vo == null ? new CaseQueryVo() : vo);
		addAttribute("autoQuery", vo == null ? false : true);

		KeycloakUser user = getLoginUser();
		// role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員 5 平台管理人員
		int role = usersDao.checkUserRole(user.getId());
		addAttribute("role", role);

		if(role == 3 || role == 4 || role == 5 ) {
			List<DepartmentVo> deptParentList = usersDao.getDeptParentList(user.getId(), role);
			addAttribute("deptParentList", deptParentList);
		}

		return "frontstage/jdzq/caseQuery/case-query";
	}

	@GetMapping("/returnDashboard")
	public String returnDashboard() {
		return "frontstage/dashboard";
	}

	@PostMapping(value = { "/personalCaseList" })
	@ResponseBody
	public ResponseObj personalCaseList(@RequestBody PersonSortVo sort) {
		ResponseObj responseObj = new ResponseObj();
		responseObj.setResult(ResponseObj.SUCCESS);
		responseObj.setResultData(caseService.getPersonalCaseList(getLoginUser(), sort));
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

//	@PostMapping(value = { "/clearCaseSearch" })
//	@ResponseBody
//	public void clearCaseSearch() {
//		removeFromSession("queryCase");
//	}

	@RequestMapping(value = { "/personalCaseListing1" })
	public String personalCaseListing1(@RequestParam("policyNo") String policyNo) {
		removeFromSession("queryCase");
		return caselisting1(policyNo,"personalCaseListing1");
	}


	@RequestMapping(value = { "/caselisting1" })
	public String caselisting1(@RequestParam("policyNo") String policyNo, @RequestParam("parentUrl") String parentUrl) {
		addAttribute("policyNo", policyNo);
		CaseVo vo = caseService.getCaseProcess(getUserId(), policyNo);
		addAttribute("parentUrl", StringUtils.isBlank(parentUrl) ? "caselisting1" : parentUrl);
		addAttribute("policyName", vo.getPolicyType());
		addAttribute("vo",  vo);
		return "frontstage/jdzq/caseQuery/caselisting1";
	}

	@RequestMapping(value = { "/caselisting2" })
	public String caselisting2(@RequestParam("policyNo") String policyNo, @RequestParam("policyName") String policyName, @RequestParam("parentUrl") String parentUrl) {
		addAttribute("policyNo", policyNo);
		addAttribute("policyName", policyName);
		addAttribute("parentUrl", StringUtils.isBlank(parentUrl) ? "caselisting1" : parentUrl);
		addAttribute("vo",  caseService.getCasePolicyInfo(getUserId(), policyNo));
		return "frontstage/jdzq/caseQuery/caselisting2";
	}

	@RequestMapping(value = { "/caselisting3" })
	public String caselisting3(@RequestParam("policyNo") String policyNo, @RequestParam("policyName") String policyName, @RequestParam("parentUrl") String parentUrl) {
		addAttribute("policyNo", policyNo);
		addAttribute("policyName", policyName);
		addAttribute("parentUrl", StringUtils.isBlank(parentUrl) ? "caselisting1" : parentUrl);
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
