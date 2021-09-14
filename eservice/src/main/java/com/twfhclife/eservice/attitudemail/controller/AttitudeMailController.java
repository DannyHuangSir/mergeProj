package com.twfhclife.eservice.attitudemail.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.twfhclife.eservice.attitudemail.service.IAttitudeMailService;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.AttitudeMailVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;

/**
 * 保戶信箱使用 Controller 
 * @author Ken
 *
 */
@Controller
@EnableAutoConfiguration
public class AttitudeMailController extends BaseController {

	private static final Logger logger = LogManager.getLogger(AttitudeMailController.class);
	
	@Autowired
	private IAttitudeMailService attitudeMailService;
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private IParameterService parameterService;
	
	/**
	 * Initialize 保戶信箱
	 * @return
	 */
	@RequestLog
	@GetMapping("/attitudeMail")
	public String initAttributeMail() {
		try {
			this.addAttribute("attitudeMailVo", new AttitudeMailVo());
			try {
				if(getSession(ApConstants.SYSTEM_MSG_PARAMETER) == null) {
					List<ParameterVo> listParam = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYSTEM_MSG_PARAMETER);
					Map<String, ParameterVo> mapParam = listParam.stream()
							.collect(Collectors.toMap(ParameterVo::getParameterCode, Function.identity()));
					addAttribute(ApConstants.SYSTEM_MSG_PARAMETER, mapParam);
				}
			} catch(Exception de) {
				/*從外部進來可能沒有 session*/
				List<ParameterVo> listParam = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYSTEM_MSG_PARAMETER);
				Map<String, ParameterVo> mapParam = listParam.stream()
						.collect(Collectors.toMap(ParameterVo::getParameterCode, Function.identity()));
				addAttribute(ApConstants.SYSTEM_MSG_PARAMETER, mapParam);
			}
		} catch(Exception e) {
			logger.error("Unable to init from attitudeMail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "frontStage/attitudeMail";
	}
	
	@RequestLog
	@PostMapping("/attitudeMailSave")
	public ResponseEntity<ResponseObj> saveAttributeMail(@RequestBody AttitudeMailVo attitudeMailVo) {
		try {
			if(attitudeMailService.insertAttitudeMail(attitudeMailVo)) {
				processSuccess(null);
			} else {
				String errorMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0001");
				//processError("發送失敗");
				processError(errorMsg);
			}
		} catch(Exception e) {
			logger.error("saveAttributeMail: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@PostMapping("/attitudeMailSearch")
	public ResponseEntity<ResponseObj> loadLilipiData(@RequestBody AttitudeMailVo attitudeMailVo) {
		try {
			LilipmVo vo = lilipmService.findByPolicyNo(attitudeMailVo.getAttitudeInsuNo());
			if(vo == null) {
				String errorMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "E0002");
				//processError("無效的保單號碼");
				processError(errorMsg);
			} else {
				attitudeMailVo.setAttitudeName(vo.getLipmName1());
				attitudeMailVo.setAttitudeId(vo.getNoHiddenLipmId());
					// 計算 birth day to get age 
				if(vo.getLipmBirth() != null) {
					LocalDate sysdate = LocalDate.now();
					LocalDate birthDate = vo.getLipmBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					attitudeMailVo.setAttitudeOld(Period.between(birthDate, sysdate).getYears());
				}
				attitudeMailVo.setAttitudeAddr(vo.getLipmAddr());
				attitudeMailVo.setAttitudeTelDay(vo.getNoHiddenLipmTelH());
				processSuccess(attitudeMailVo);
			}
		} catch(Exception e) {
			logger.error("loadLilipiData: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
