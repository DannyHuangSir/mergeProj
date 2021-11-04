package com.twfhclife.adm.controller.risklevel;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.PartnerUserEntityVo;
import com.twfhclife.adm.model.QuestionVo;
import com.twfhclife.adm.model.QuestionaireVo;
import com.twfhclife.adm.service.IQuestionaireService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

@Controller
@EnableAutoConfiguration
public class RiskLevelController extends BaseController {

	private static final Logger logger = LogManager.getLogger(RiskLevelController.class);

	@Autowired
	private IQuestionaireService questionService;

	@RequestLog
	@LoginCheck(value=@FuncUsageParam(
			funcId = "989",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/attribute")
	public String attribute() {
		return "backstage/attribute/attributeSearch";
	}

	@RequestLog
	@PostMapping("/Attribute/getQuestionPageList")
	public ResponseEntity<PageResponseObj> getQuestionPageList(@RequestBody QuestionaireVo questionaireVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: QuestionaireVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
			// 設定jqGrid 資料查詢集合
			Calendar calendar = Calendar.getInstance();
			if (questionaireVo.getEffectiveEnd() != null) {
				calendar.setTime(questionaireVo.getEffectiveEnd());
				calendar.add(Calendar.HOUR, 23);
				calendar.add(Calendar.MINUTE, 59);
				calendar.add(Calendar.MILLISECOND, 59);
				questionaireVo.setEffectiveEnd(calendar.getTime());
			}

			if (questionaireVo.getStopEnd() != null) {
				calendar.setTime(questionaireVo.getStopEnd());
				calendar.add(Calendar.HOUR, 23);
				calendar.add(Calendar.MINUTE, 59);
				calendar.add(Calendar.MILLISECOND, 59);
				questionaireVo.setStopEnd(calendar.getTime());
			}
			pageResp.setRows(questionService.getQuestionPageList(questionaireVo));
			// 設定jqGrid 資料查詢總筆數
			pageResp.setRecords(questionService.getQuestionPageTotal(questionaireVo));
			// 設定jqGrid 目前頁數
			pageResp.setPage(questionaireVo.getPage());
			// 設定jqGrid 總頁數
			pageResp.setTotal((pageResp.getRecords() + questionaireVo.getRows() - 1) / questionaireVo.getRows());
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getQuestionPageList: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	@GetMapping("/attributeAdd")
	public String attributeAdd(Long questionId) {
		if (questionId != null && questionId > 0) {
			QuestionVo vo = questionService.getQuestionById(questionId);
			if (vo != null) {
				vo.setId(questionId);
				addAttribute("questionVo", vo);
				return "backstage/attribute/attributeUpdate";
			}
		}
		return "backstage/attribute/attributeAdd";
	}

	@PostMapping("/attributeUpdate")
	@ResponseBody
	public ResponseEntity<ResponseObj> attributeUpdate(@RequestBody QuestionVo questionVo) {
		int result = questionService.insertOrUpdateQuestion(questionVo);
		if (result > 0) {
			processSuccess(result);
		} else {
			processSystemError();
		}
		return processResponseEntity();
	}

	@PostMapping("/attributeDelete")
	@ResponseBody
	public ResponseEntity<ResponseObj> attributeDelete(@RequestBody QuestionVo vo) {
		int result = questionService.delelteQuestion(vo.getId());
		if (result > 0) {
			processSuccess(result);
		} else {
			processSystemError();
		}
		return processResponseEntity();
	}

}
