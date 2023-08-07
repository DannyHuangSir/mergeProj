package com.twfhclife.alliance.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.alliance.domain.*;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.MedicalRequestVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.alliance.service.IServiceBillingService;
import com.twfhclife.alliance.service.impl.MedicalTreatmentExternalServiceImpl;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.Division;
import com.twfhclife.eservice.web.model.HospitalVo;
import com.twfhclife.eservice.web.model.MedicalDataFileGroup;
import com.twfhclife.eservice.web.model.OutpatientType;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClaimChainController {

	private static final Logger logger = LogManager.getLogger(ClaimChainController.class);
	
	@Autowired
	IClaimChainService claimChainService;
	
	@Autowired
	IParameterService parameterServiceImpl;

	@Autowired
	IExternalService externalService;

	@Autowired
	private MedicalTreatmentExternalServiceImpl medicalExternalServiceImpl;

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
        		
        		logger.info("API-409取得醫院之就診類型,request hpId=" + hospitalId);
        		String strResponse = medicalExternalServiceImpl.postForEntity(
        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api409.url"),
        				params,
        				null);
        		logger.info("API-409取得醫院之就診類型,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
//        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API409_RESPONSE");
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
        		
        		logger.info("API-410取得醫院科別清單,request=" + params.toString());
        		String strResponse = medicalExternalServiceImpl.postForEntity(
        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api410.url"),
        				params,
        				null);
        		logger.info("API-410取得醫院科別清單,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
//        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API410_RESPONSE");
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
        		
        		logger.info("API-411取得醫院資料群組,request=" + params.toString());
        		String strResponse = medicalExternalServiceImpl.postForEntity(
        				this.parameterServiceImpl.getParameterValueByCode("eservice_api","medicalAlliance.api411.url"),
        				params,
        				null);
        		logger.info("API-411取得醫院資料群組,回傳=" + strResponse);
        		
        		/**
        		 * fake code for dev-start
        		 */
//        		String strResponse = parameterServiceImpl.getParameterValueByCode("eservice_api", "FAKE_API411_RESPONSE");
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

	@Autowired
	ILilipmService iLilipmService;

	/**
	 * SPA-401 明細對帳問題回報分頁查詢
	 *
	 * @param vo
	 */
	@ApiRequest
	@RequestMapping("/spa401")
	public ApiResponseObj<Spa401ResponseVo> callSpa401(
			@RequestBody Spa401RequestVo vo) {

		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		response.setReturnHeader(returnHeader);
		logger.info("Start ClaimChainController.callSpa401().");
		try {
			//Request
			Map<String, Object> params = new HashMap<>();
			params.put("orgId", vo.getOrgId());
			params.put("serviceType", vo.getServiceType());
			params.put("verifyDate", vo.getVerifyDate());
			params.put("pageNumber",vo.getPageNumber());
			params.put("pageSize", vo.getPageSize());

			logger.info("SPA-401取得明細對帳問題回報,request=" + params.toString());
			String strResponse = externalService.postForEntity(
					this.parameterServiceImpl.getParameterValueByCode("eservice_api", "alliance.spa401.url"),
					params);
			logger.info("SPA-401取得明細對帳問題回報,回傳=" + strResponse);
			if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
				String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				Spa401ResponseVo spa401ResponseVo = (Spa401ResponseVo) MyJacksonUtil.json2Object(dataString, Spa401ResponseVo.class);
				// 判斷是否為保戶
				if (spa401ResponseVo != null && CollectionUtils.isNotEmpty(spa401ResponseVo.getDetails())) {
					spa401ResponseVo.getDetails().forEach(d -> {
						String rocId = null;
						if (isMedicalServiceType(vo.getServiceType())) {
							Map<String, String> params403 = new HashMap<>();
							params403.put("caseId", d.getCaseNo());
							try {
								String resp403 = medicalExternalServiceImpl.postForEntity(this.parameterServiceImpl.getParameterValueByCode("eservice_api", "medicalAlliance.api403.url"), params403, null);
								if (checkLiaAPIResponseValue(resp403,"/code","0")) {
									rocId = MyJacksonUtil.readValue(resp403, "/data/idNo");
								}
							} catch (Exception e) {
								logger.error("call api 403 error: {}", e);
							}
						} else if (isClaimServiceType(vo.getServiceType())) {
							Map<String, Object> params105 = new HashMap<>();
							params105.put("caseId", d.getCaseNo());
							try {
								String resp105 = externalService.postForEntity(this.parameterServiceImpl.getParameterValueByCode("eservice_api", "alliance.api105.url"), params105);
								if (checkLiaAPIResponseValue(resp105,"/code","0")) {
									rocId = MyJacksonUtil.readValue(resp105, "/data/idNo");
								}
							} catch (Exception e) {
								logger.error("call api 105 error: {}", e);
							}
						}
						if (StringUtils.isNotBlank(rocId)) {
							int countPIPM = iLilipmService.getInsuredUsersByRocId(rocId);
							d.setInEservice(countPIPM > 0 ? "Y" : "N");
						}
						Map<String, Object> statusMap = serviceBillingService.getReplayStatusByIdNo(d.getId());
						if (statusMap != null) {
							d.setReplayStatus((String) statusMap.get("REPLAY_STATUS"));
							d.setReplayTime((String) statusMap.get("REPLAY_TIME"));
						}
					});
				}
				response.setResult(spa401ResponseVo);
			}
		} catch (Exception e) {
			logger.error(e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		}
		logger.info("End ClaimChainController.callSpa401().");
		return response;
	}

	private static boolean isClaimServiceType(String serviceType) {
		return Lists.newArrayList("claim", "claimFrom", "claimTo", "claimFromFileScan", "claimToFileScan").contains(serviceType);
	}

	private static boolean isMedicalServiceType(String serviceType) {
		return Lists.newArrayList("ihs", "ihsFileScan").contains(serviceType);
	}

	@Autowired
	private IServiceBillingService serviceBillingService;
	/**
	 * SPA-402 明細對帳問題回報狀態更新
	 *
	 * @param vo
	 */
	@ApiRequest
	@RequestMapping("/spa402")
	public ApiResponseObj<String> callSpa402i(@RequestBody Spa402RequestVo vo) {
		logger.info("Start ClaimChainController.callSpa402i().");
		ApiResponseObj response = new ApiResponseObj();
		ReturnHeader returnHeader = new ReturnHeader();
		response.setReturnHeader(returnHeader);
		try {
			List<Map<String, Object>> data = Lists.newArrayList();
			Map<String, Object> dataMap = Maps.newHashMap();
			dataMap.put("id", vo.getId());
			dataMap.put("code", vo.getStatus());
			dataMap.put("msg", vo.getMsg());
			data.add(dataMap);
			Map<String, Object> params = new HashMap<>();
			params.put("orgId", vo.getOrgId());
			params.put("data", data);

			logger.info("SPA-402明細對帳問題回報狀態更新,request=" + params);
			String strResponse = externalService.postForEntity(
					this.parameterServiceImpl.getParameterValueByCode("eservice_api", "alliance.spa402.url"),
					params);
			logger.info("SPA-402明細對帳問題回報狀態更新,回傳=" + strResponse);
			if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, MyJacksonUtil.getNodeString(strResponse, "msg"), "", "");
			}
			serviceBillingService.addServiceBillingReplay(vo);
		} catch (Exception e) {
			logger.error(e);
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
		}

		logger.info("End ClaimChainController.callSpa402i().");
		return response;
	}

	/**
	 * claimSelectAll 理賠及理賠醫起通全選規則查詢
	 */
	@ApiRequest
	@PostMapping("/claimSelectAll")
	public List<Map<String, Object>> claimSelectAll(
			@RequestBody Map<String, Object> vo) {
		logger.info("Start ClaimChainController.claimSelectAll().");

		try {
			logger.info("claimSelectAll理賠及理賠醫起通全選規則查詢,request=" + vo);
			String strResponse = externalService.postForEntity(
					this.parameterServiceImpl.getParameterValueByCode("eservice_api", "alliance.claimSelectAll.url"),
					vo);
			logger.info("claimSelectAll理賠及理賠醫起通全選規則查詢,回傳=" + strResponse);

			if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {//String(10),0代表成功,錯誤代碼則自行定義
				String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
				return new Gson().fromJson(MyJacksonUtil.getNodeString(dataString, "orgDatas"), new TypeToken<List<HashMap>>() {
				}.getType());
			}

		} catch (Exception e) {
			logger.error(e);
		}

		logger.info("End ClaimChainController.claimSelectAll().");
		return Lists.newArrayList();
	}
}
