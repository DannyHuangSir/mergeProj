package com.twfhclife.eservice.onlineChange.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.model.TravelPlanVo;
import com.twfhclife.eservice.onlineChange.model.TravelPolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITravelPolicyService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeNoteUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.FunctionUsageClient;
import com.twfhclife.generic.api_model.CommLogRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.HttpUtil;

/**
 * 旅平險
 * (無保單)
 */
@Controller
@EnableAutoConfiguration
public class TravelPolicyController extends BaseController {
	
	private static final Logger logger = LogManager.getLogger(TravelPolicyController.class);
	
	private static final String SYSTEM_ID = "eservice";
	
	@Autowired
	private ITravelPolicyService travlpolicyService;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private FunctionUsageClient functionUsageClient;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@RequestLog
	@GetMapping("/travelPolicy1")
	public String travelPolicy1() {
		try {
			if(getSession(ApConstants.SYSTEM_MSG_PARAMETER) == null || getSession(ApConstants.SYSTEM_PARAMETER) == null) {
				// 取得系統參數
				Map<String, Map<String, ParameterVo>> sysParamMap = parameterService.getSystemParameter(ApConstants.SYSTEM_ID);
				addSession(ApConstants.SYSTEM_MSG_PARAMETER, (Serializable)sysParamMap.get(ApConstants.SYSTEM_MSG_PARAMETER));
				Map<String, Map<String, ParameterVo>> sysParamMapMsg = new HashMap<String, Map<String,ParameterVo>>();
				sysParamMapMsg.put(ApConstants.SYSTEM_MSG_PARAMETER, sysParamMap.get(ApConstants.SYSTEM_MSG_PARAMETER));
				addSession(ApConstants.SYSTEM_PARAMETER, (Serializable) sysParamMapMsg);
			}
			String travelPolicyNote = parameterService.getParameterValueByCode(SYSTEM_ID, OnlineChangeNoteUtil.TRAVEL_POLICY_NOTE1_CODE);
			addAttribute("travelPolicyNote", travelPolicyNote);
			addAttribute("travelPolicyMsg", "");
			
			addAttribute("beginDate", StringUtils.trimToEmpty(getRequest().getParameter("beginDate")));
			addAttribute("beginTime", StringUtils.trimToEmpty(getRequest().getParameter("beginTime")));
			addAttribute("endDate", StringUtils.trimToEmpty(getRequest().getParameter("endDate")));
			addAttribute("endTime", StringUtils.trimToEmpty(getRequest().getParameter("endTime")));
			addAttribute("birthday", StringUtils.trimToEmpty(getRequest().getParameter("birthday")));
			addAttribute("days", StringUtils.trimToEmpty(getRequest().getParameter("days")));
			addAttribute("destType", StringUtils.trimToEmpty(getRequest().getParameter("destType")));
			addAttribute("destDesc", StringUtils.trimToEmpty(getRequest().getParameter("destDesc")));
			addAttribute("adid", StringUtils.trimToEmpty(getRequest().getParameter("adid")));
			addAttribute("mrid", StringUtils.trimToEmpty(getRequest().getParameter("mrid")));
			addAttribute("orid", StringUtils.trimToEmpty(getRequest().getParameter("orid")));
		} catch (Exception e) {
			logger.error("travelPolicy1 error! {}", e);
		}
		/* 計算功能使用次數 */
		try {
		    functionUsageClient.addFunctionUsage(ApConstants.SYSTEM_ID, "490");
		} catch(Exception e) {
		    logger.debug("Call FunctionUsageAdd error: ", e);
		}
		return "frontstage/onlineChange/travelPolicy/travelPolicy1";
	}

	@RequestLog
	@PostMapping("/travelPolicy2")
	public String travelPolicy2(@RequestParam("travelPolicy") String travelPolicy) {
		ObjectMapper objectMapper = new ObjectMapper();
		//旅平險內容
		TravelPolicyVo vo = null;
		//各方案內容
		List<TravelPlanVo> travelPlans = new ArrayList<TravelPlanVo>();
		try {
	    	vo = objectMapper.readValue(travelPolicy, TravelPolicyVo.class);	    	
		    travelPlans.addAll(travlpolicyService.createPlan(vo));
	    	//傳回頁面
			addAttribute(ApConstants.TRAVEL_POLICY, vo);
			addAttribute("travelPlans", travelPlans);
		} catch (Exception e) {
			/*addAttribute("travelPolicyMsg", "保費計算錯誤!");*/
			addAttribute("travelPolicyMsg", getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0097"));
			return "frontstage/onlineChange/travelPolicy/travelPolicy1";
		}
		return "frontstage/onlineChange/travelPolicy/travelPolicy2";
	}

	@RequestLog
	@PostMapping("/travelPolicy3")
	public String travelPolicy3(@RequestParam("travelPolicy") String travelPolicy) {
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	//使用者選擇的旅平險內容
	    	TravelPolicyVo vo = objectMapper.readValue(travelPolicy, TravelPolicyVo.class);
	    	//被保人與要保人關係下拉
	    	//List<ParameterVo> insuredRelationList = parameterService.getParameterByCategoryCode(SYSTEM_ID, OnlineChangeUtil.INSURED_RELATION_PARAMETER_CATEGORY_CODE);
	    	//受益人與被保人關係下拉
	    	//List<ParameterVo> beneficiaryRelationList = parameterService.getParameterByCategoryCode(SYSTEM_ID, OnlineChangeUtil.BENEFICIARY_RELATION_PARAMETER_CATEGORY_CODE);
			//預計領取地點下拉關係
	    	List<ParameterVo> receivePolicyLocationList = parameterService.getParameterByCategoryCode(SYSTEM_ID, OnlineChangeUtil.RECEIVE_POLICY_LOCATION_PARAMETER_CATEGORY_CODE);
			
			//傳回頁面
		    //addAttribute("insuredRelationList", insuredRelationList);
//		    addAttribute("beneficiaryRelationList", beneficiaryRelationList);
		    addAttribute("receivePolicyLocationList", receivePolicyLocationList);
		    addAttribute(ApConstants.TRAVEL_POLICY, vo);
		    addSession(ApConstants.USER_TERM_TRAVEL_POLICY, new Date());
	    } catch (Exception e) {
	    	logger.error("travelPolicy3 error! {}", e);
		}
		return "frontstage/onlineChange/travelPolicy/travelPolicy3";
	}
	
	@RequestLog
	@PostMapping("/travelPolicyTransAdd")
	public ResponseEntity<ResponseObj> travelPolicyTransAdd(@RequestBody String travelPolicy) {
		try {
			//使用者選擇的旅平險內容
			ObjectMapper objectMapper = new ObjectMapper();
			TravelPolicyVo vo = objectMapper.readValue(travelPolicy, TravelPolicyVo.class);
	    	TransVo travelPolicyTrans = new TransVo();
			String userId = getUserId();
			travelPolicyTrans.setUserId(userId);
			travelPolicyTrans.setTransType(TransTypeUtil.TRAVEL_POLICY_PARAMETER_CODE);
			travelPolicyTrans.setTravelPolicy(vo);
			String message = travlpolicyService.addTravelPolicyTrans(travelPolicyTrans);
			if(ApConstants.SUCCESS.equals(message)){
				Map<String, String> data = new HashMap<>();
				data.put("transNum", travelPolicyTrans.getTransNum());
				this.setResponseObj(ResponseObj.SUCCESS, message, data);
			}else{
				this.setResponseObj(ResponseObj.ERROR, message, null);
			}
	    }catch(Exception e){
	    	logger.error("addTravelPolicyTrans error! {}", e);
	    	this.setResponseObj(ResponseObj.ERROR, ApConstants.SYSTEM_ERROR, null);
	    }
		return ResponseEntity.status(HttpStatus.OK).body(this.getResponseObj());
	}

	@RequestLog
	@PostMapping("/travelPolicy4")
	public String travelPolicy4(@RequestParam("travelPolicy") String travelPolicy, @RequestParam("transNum") String transNum) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			//使用者選擇的旅平險內容
			TravelPolicyVo vo = objectMapper.readValue(travelPolicy, TravelPolicyVo.class);
	    	
	    	//計算附加內容
	    	TravelPlanVo plan = vo.getTravelPlan();
	    	String additional = "";
	    	String additionalTit = "";
	    	String additionalCon = "";
	    	String additionalCon2 = "";
	    	if(plan.getAge() >= 15 && plan.getAge() <=80){
	    		additionalCon = "15足歲~80歲";
	    		additionalCon2 = "40%";
	    	}else{
	    		additionalCon = "未滿15足歲";
	    		additionalCon2 = "70%";
	    	}
	    	if(!plan.getMrid().toString().equals("0")){
	    		additionalTit = "意外傷害醫療";
	    	}
	    	if(!plan.getOrid().toString().equals("0")){
	    		if(!additionalTit.equals("")){
	    			additionalTit = additionalTit + "＋";
	    		}
	    		additionalTit = additionalTit + "海外突發疾病醫療";
	    	}
	    	
	    	if(plan.getMrid() == new BigDecimal(0) && plan.getOrid() == new BigDecimal(0)){
	    		additional = "不附加";
	    	}else{
	    		additionalTit = additionalTit + "給付";
		    	additional = additionalTit + " : "+additionalCon + "，最高為保險金額之"+additionalCon2+" (不超過200萬)";
	    	}
	    	
	    	if ("1".equals(vo.getProposer().getSameAddr())) {
	    		vo.getProposer().setAddress("同被保人住所");
	    	}
	    	
	    	plan.setAdditional(additional);
	    	addAttribute(ApConstants.TRAVEL_POLICY, vo);
	    	addSession("travelPolicyPrint", vo);
	    	addAttribute("transNum", transNum);
	    	
	    }catch(Exception e){
	    	logger.error("travelPolicy4 error! {}", e);
	    }
		return "frontstage/onlineChange/travelPolicy/travelPolicy4";
	}
	
	@RequestLog
	@RequestMapping(value = "/travelPolicyPDF")
	public @ResponseBody HttpEntity<byte[]> travelPolicyPDF(TravelPolicyVo travelPolicyVo) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			TravelPolicyVo travelPolicyPrintVo = (TravelPolicyVo) getSession("travelPolicyPrint");
			document = travlpolicyService.getPDF(travelPolicyPrintVo);
			
			String fileName = String.format("inline; filename=旅平險要保書-%s.pdf", "123");
			header.setContentType(new MediaType("application", "pdf"));
			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			header.setContentLength(document.length);
		} catch (Exception e) {
			logger.error("Unable to get data from downloadTravelPolicyPDF: {}", ExceptionUtils.getStackTrace(e));
		}

		return new HttpEntity<byte[]>(document, header);
	}
	
	@RequestLog
	@PostMapping("/travelPolicyAutoSend")
	public ResponseEntity<ResponseObj> travelPolicyAutoSend(@RequestBody String requesInput){
		/** 寄送內部窗口，非直接對客戶寄送 */
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// 使用者選擇的旅平險內容
			Map<String, String> map = objectMapper.readValue(requesInput, Map.class);
			TravelPolicyVo vo = objectMapper.readValue(map.get("travelPolicy"), TravelPolicyVo.class);
			// 追加發送 email 給指定窗口
			StringBuffer sb = new StringBuffer();
			sb.append("<!DOCTYPE html><html lang=\"zh-Hant\"><head><meta charset=\"utf-8\"/></head><body>CONTENT</body></html>");
			String content = sb.toString();
			content = content.replace("CONTENT", objectMapper.readValue(map.get("travelPolicyInfo"), String.class));
	    	String subject = parameterService.getParameterValueByCode(SYSTEM_ID, "TRAVEL_POL_SUBJECT");
	    	String mailTo = parameterService.getParameterValueByCode(SYSTEM_ID, "TRAVEL_POL_MAIL_ADDR");
	    	String transNum = objectMapper.readValue(map.get("transNum"), String.class);
	    	subject = subject.replace("TRANS_NUM", transNum);
	    	String filename = "/temp/TravelPolicy" + transNum + ".pdf";
	    	logger.debug("filename:" + filename);
	    	logger.debug("subject:" + subject);
	    	logger.debug("content:" + content);
	    	
	    	java.io.File sendFile = new java.io.File(filename);
	    	FileUtils.writeByteArrayToFile(sendFile, travlpolicyService.getPDF(vo));
	    	List<java.io.File> listFile = new ArrayList<>();
	    	listFile.add(sendFile);
	    	mailService.sendMail(content, subject, mailTo, null, listFile);
	    	try {
	    		String url = parameterDao.getParameterValueByCode("eservice_batch", "COMM_LOG_ADD_URL");
				String accessKey = parameterDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
				new HttpUtil().postCommLogAdd(url, accessKey, new CommLogRequest(ApConstants.SYSTEM_ID, "email", mailTo, content));
			} catch (Exception e) {
				logger.error("Unable to postCommLogAdd(email) in TravelPolicyController: {}", ExceptionUtils.getStackTrace(e));
			}
	    	if (sendFile.delete()) {
	    		logger.info("delete file success");
	    	}
	    	processSuccess(ResponseObj.SUCCESS);
		} catch(Exception e) {
			logger.error("autoSendTravelPolicy error: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

}
