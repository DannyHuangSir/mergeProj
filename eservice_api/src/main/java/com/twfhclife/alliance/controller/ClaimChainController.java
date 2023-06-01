package com.twfhclife.alliance.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.alliance.model.*;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.generic.utils.AesUtil;
import com.twfhclife.generic.utils.ApConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.impl.MedicalTreatmentExternalServiceImpl;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.utils.MyJacksonUtil;

@RestController
public class ClaimChainController{

	private static final Logger logger = LogManager.getLogger(ClaimChainController.class);
	
	@Autowired
	IClaimChainService claimChainService;
	
	@Autowired
    MedicalTreatmentExternalServiceImpl medicalExternalServiceImpl;
	
	@Autowired
	IParameterService parameterServiceImpl;
	
	/**
	 * API-107 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/notifyOfNewCase")
	public ClaimResponseVo notifyOfNewCase(@RequestBody ClaimRequestVo reqVo) {
		logger.info("Start ClaimChainController.notifyOfNewCase().");
		
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		try {
			rtnVo = claimChainService.notifyOfNewCase(reqVo);
		}catch(Exception e) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}

		//rtnVo.setMsg("this is a test call");
		
		logger.info("End ClaimChainController.notifyOfNewCase().");
		return rtnVo;
	}
	
	/**
	 * for API-105查詢理賠案件後呼叫，及可用於本地新增
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/addInsuranceClaim")
	public ClaimResponseVo addInsuranceClaim(InsuranceClaimMapperVo icvo) {
		logger.info("Start ClaimChainController.addInsuranceClaim().");
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		
		try {
			//TODO
			if(icvo!=null) {
				int rtnValue = claimChainService.addInsuranceCliam(icvo);
				if(rtnValue>=1) {
					rtnVo.setCode(ClaimResponseVo.CODE_SUCCESS);
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
					rtnVo.setMsg("claimChainService.addInsuranceCliam error.");
				}
			}else {
				
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				
				String msg = "input params is null.";
				rtnVo.setMsg(msg);
				logger.error(msg);
			}

		}catch(Exception e) {

			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}
		
		logger.info("End ClaimChainController.addInsuranceClaim().");
		return rtnVo;
	}
	
	/**
	 * API-205 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/notifyOfNewCaseChange")
	public ClaimResponseVo newCaseNotified(@RequestBody ClaimRequestVo reqVo) {
		logger.info("Start ClaimChainController.newCaseNotified().");
		
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		try {
			rtnVo = claimChainService.newCaseNotified(reqVo);
		}catch(Exception e) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}

		//rtnVo.setMsg("this is a test call");
		
		logger.info("End ClaimChainController.newCaseNotified().");
		return rtnVo;
	}
	
	/**
	 * DNS-491 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/dns491")
	public DnsResponseVo notifyOfNewCaseDns(@RequestBody DnsRequestVo reqVo){
		logger.info("Start ClaimChainController.notifyOfNewCaseDns().");
		
		DnsResponseVo rtnVo = null;
		
		if(reqVo!=null && reqVo.getType()!=null && reqVo.getJobIds()!=null) {
			String strType = reqVo.getType();
			List<String> listStr = reqVo.getJobIds();
			
			try {
				//save to eservice NOTIFY_OF_NEW_CASE_DNS table
				rtnVo = this.claimChainService.notifyOfNewCaseDns(reqVo);
			}catch(Exception e) {
				logger.error(e);
			}
			
			if(rtnVo==null) {
				rtnVo = new DnsResponseVo();
				rtnVo.setCode(DnsResponseVo.CODE_ERROR);
				rtnVo.setMsg("eservice_api error.");
			}

		}else {
			if(rtnVo==null) {
				rtnVo = new DnsResponseVo();
			}
			
			rtnVo.setCode(DnsResponseVo.CODE_ERROR);
			
			StringBuffer sb = new StringBuffer("");
			sb.append("Input parameter error:");
			if(reqVo==null) {
				sb.append("DnsRequestVo is null.");
			}
			if(reqVo!=null && reqVo.getType()==null) {
				sb.append("type is null.");
			}
			if(reqVo!=null && reqVo.getJobIds()==null) {
				sb.append("jobId is null.");
			}
			rtnVo.setMsg(sb.toString());
			
			logger.info("input DnsRequestVo error:"+ (reqVo == null ? "null" : reqVo.toString()));
		}
		
		logger.info("End ClaimChainController.notifyOfNewCaseDns().");
		return rtnVo;
		
	}


	/**
	 * API-491 案件通知(CallBack 到保險公司)
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/api491")
	public MedicalRequestVo addNotifyOfNewCaseMedical(@RequestBody MedicalRequestVo vo){
		logger.info("Start ClaimChainController.addNotifyOfNewCaseMedical().");
		MedicalRequestVo mdVo = new MedicalRequestVo();
		try {
			if(vo!=null) {
				int rtnValue = claimChainService.addNotifyOfNewCaseMedical(vo);
				if(rtnValue>=1) {
					mdVo.setCode(ClaimResponseVo.CODE_SUCCESS);
					mdVo.setMsg(ClaimResponseVo.MSG_SUCCESS);
				}else {
					mdVo.setCode(ClaimResponseVo.CODE_ERROR);
					mdVo.setMsg("claimChainService.addNotifyOfNewCaseMedical error.");
				}
			}else {

				mdVo.setCode(ClaimResponseVo.CODE_ERROR);
				String msg = "input params is null.";
				mdVo.setMsg(msg);
				logger.error(msg);
			}
		}catch(Exception e) {
			mdVo.setCode(ClaimResponseVo.CODE_ERROR);
			mdVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}
		logger.info("End ClaimChainController.addNotifyOfNewCaseMedical().");
		return mdVo;
	}
	
	/**
	 * API-409 取得醫院之就診類型
	 * @param hospitalId
	 * @return OutpatientType[]
	 */
	@ApiRequest
	@RequestMapping("/api409")
	public OutpatientType[] callAPI409(
			@RequestParam(value="hpId",required=true) String hospitalId){
		logger.info("Start ClaimChainController.callAPI409().");
		
		OutpatientType[] outpatientTypes = null;
		
		try {
			if(hospitalId!=null) {
				//Request
        		Map<String, String> params = new HashMap<>();
        		params.put("hpId", hospitalId);//必填,String(20),醫院之醫事機構代碼
        		
//        		logger.info("API-409取得醫院之就診類型,request hpId=" + hospitalId);
//        		String strResponse = medicalExternalServiceImpl.postForEntity(
//        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api409.url"),
//        				params,
//        				null);
//        		logger.info("API-409取得醫院之就診類型,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API409_RESPONSE");
        		/**
        		 * fake code for dev-end
        		 */
                
        		if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
                	String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                	HospitalVo rtnVo = (HospitalVo)MyJacksonUtil.json2Object(dataString, HospitalVo.class);
                	if(rtnVo!=null && rtnVo.getOutpatientTypes()!=null) {
                		outpatientTypes = rtnVo.getOutpatientTypes();
                	}
                }
 
			}
		}catch(Exception e) {
			logger.error(e);
		}
		
		logger.info("End ClaimChainController.callAPI409().");
		return outpatientTypes;
	}
	
	/**
	 * API-409 取得醫院之就診類型
	 * @param hospitalId
	 * @param oType
	 * @return Division[]
	 */
	@ApiRequest
	@RequestMapping("/api410")
	public Division[] callAPI410(
			@RequestParam(value="hpId",required=true) String hospitalId,
			@RequestParam(value="otype",required=true) String oType){
		logger.info("Start ClaimChainController.callAPI410().");
		
		Division[] divisions = null;
		
		try {
			if(hospitalId!=null && oType!=null) {
				//Request
        		Map<String, String> params = new HashMap<>();
        		params.put("hpId", hospitalId);//必填,String(20),醫院之醫事機構代碼
        		params.put("otype", oType);//必填,String(50),就診類型代碼
        		
//        		logger.info("API-410取得醫院科別清單,request=" + params.toString());
//        		String strResponse = medicalExternalServiceImpl.postForEntity(
//        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api410.url"),
//        				params,
//        				null);
//        		logger.info("API-410取得醫院科別清單,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API410_RESPONSE");
        		/**
        		 * fake code for dev-end
        		 */
                
                if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
                	String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                	//dataString = dataString.substring(1, dataString.length() - 1);
            		//System.out.println(dataString);
            		
            		HospitalVo hospitalVo = (HospitalVo)MyJacksonUtil.json2Object(dataString, HospitalVo.class);
            		//System.out.println(hospitalVo);
                	
            		List<Division> list = hospitalVo.getDivisions();
            		divisions = (Division[])list.toArray(new Division[0]);
                }else {
                	logger.info("API-410 Response code is not 0.");
                }

			}
		}catch(Exception e) {
			logger.error(e);
		}
		
		logger.info("End ClaimChainController.callAPI410().");
		return divisions;
	}
	
	/**
	 * API-411 取得醫院資料群組
	 * @param hospitalId
	 * @param oType
	 * @return MedicalDataFileGroup[]
	 */
	@ApiRequest
	@RequestMapping("/api411")
	public MedicalDataFileGroup[] callAPI411(
			@RequestParam(value="hpId",required=true) String hospitalId,
			@RequestParam(value="otype",required=true) String oType){
		logger.info("Start ClaimChainController.callAPI411().");
		
		MedicalDataFileGroup[] fileGroups = null;
		
		try {
			if(hospitalId!=null && oType!=null) {
				//Request
        		Map<String, String> params = new HashMap<>();
        		params.put("hpId", hospitalId);//必填,String(20),醫院之醫事機構代碼
        		params.put("otype", oType);//必填,String(50),就診類型代碼
        		
//        		logger.info("API-411取得醫院資料群組,request=" + params.toString());
//        		String strResponse = medicalExternalServiceImpl.postForEntity(
//        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api411.url"),
//        				params,
//        				null);
//        		logger.info("API-411取得醫院資料群組,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API411_RESPONSE");
        		/**
        		 * fake code for dev-end
        		 */
                
        		if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
					String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
					
					OutpatientType outpatientTye = (OutpatientType)MyJacksonUtil.json2Object(dataString, OutpatientType.class);
					List<MedicalDataFileGroup> listGroup = outpatientTye.getFileGroup();
					fileGroups = listGroup.toArray(new MedicalDataFileGroup[0]);
                }
 
			}
		}catch(Exception e) {
			logger.error(e);
		}
		
		logger.info("End ClaimChainController.callAPI411().");
		return fileGroups;
	}


	@Value("${eservice.bxcz.414.callback.url}")
	private String callBack414;

	@Autowired
	private IInsuranceClaimService insuranceClaimService;
	@ApiRequest
	@RequestMapping("/api414")
	public Bxcz414ReturnVo callApi414(
			@RequestBody Bxcz414CallBackVo vo) {
		logger.info("Start ClaimChainController.callApi414().");
		Bxcz414ReturnVo ret = new Bxcz414ReturnVo();
		try {
			//TODO verify actionId
			if (StringUtils.isBlank(vo.getState())) {
				ret.setCode("1");
				ret.setMsg("此活動代碼不存在。");
				return ret;
			}
			BxczState state = new Gson().fromJson(new String(Base64.getDecoder().decode(vo.getState())), BxczState.class);
			if (StringUtils.equals(state.getType(), ApConstants.INSURANCE_CLAIM)) {
				TransInsuranceClaimVo claimVo = insuranceClaimService.getTransInsuranceClaimDetail(state.getTransNum());
				if (claimVo == null) {
					ret.setCode("2");
					ret.setMsg("非本公司保戶！");
					return ret;
				}
				logger.debug("api414 TransInsuranceClaimVo: ", new Gson().toJson(claimVo));
				ret.setCode("0");
				ret.setIdNo(claimVo.getIdNo());
				ret.setName(claimVo.getName());
				ret.setBirdate(claimVo.getBirdate());
				ret.setAcExpiredSec("300");
				ret.setTo(claimVo.getTo());
				ret.setRedirectUri(callBack414);
				ret.setCpoaContent(Lists.newArrayList(vo.getIdVerifyType()));
				ret.setId_token(StringUtils.isBlank(state.getId()) ? "" : AesUtil.decrypt(state.getId(), state.getActionId()));
				ret.setSeNo(claimVo.getTransNum());
				return ret;
			}
		} catch (Exception e) {
			logger.error("ClaimChainController.callApi414 error: " + e);
			ret.setCode("500");
			ret.setMsg("error");
		}
		logger.info("End ClaimChainController.callAPI411().");
		return ret;
	}

	/**
     * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
     * @param responseJsonString
     * @param pathFieldName ex:"/code"
     * @param checkValue ex:"0"
     * @return boolean
     */
    private boolean checkLiaAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
        boolean b = false;
        if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
            String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
            logger.info("-----------checkLiaAPIResponseValue-----------"+code);
            if(checkValue.equals(code)) {//success
                b = true;
            }
        }
        logger.info("-----------checkLiaAPIResponseValue-----return  ------"+b);
        return b;
    }
}
