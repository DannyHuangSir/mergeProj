package com.twfhclife.alliance.service.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.twfhclife.alliance.dao.UnionCourseDao;
import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;
import com.twfhclife.alliance.model.UnionCourseVo;
import com.twfhclife.alliance.service.IMedicalTreatmentExternalService;
import com.twfhclife.generic.dao.adm.ParameterDao;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyJacksonUtil;

@Service
public class MedicalTreatmentExternalServiceImpl implements IMedicalTreatmentExternalService{
	
	private static final Logger logger = LogManager.getLogger(MedicalTreatmentExternalServiceImpl.class);
	
	//@Value("${alliance.api.accessToken}")
	public String ACCESS_TOKEN;
	
	private RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("apiParameterDao")
    private ParameterDao parameterDao;
	
	@Autowired
	private UnionCourseDao unionCourseDao;

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String aCCESS_TOKEN) {
		ACCESS_TOKEN = aCCESS_TOKEN;
	}
	
	public MedicalTreatmentExternalServiceImpl() {
		
		//Fix the RestTemplate to be a singleton instance.
	    restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;

	    // Set the request factory. 
	    // IMPORTANT: This section I had to add for POST request. Not needed for GET
	    int milliseconds = 20*1000;
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(milliseconds);
		httpRequestFactory.setConnectTimeout(milliseconds);
		httpRequestFactory.setReadTimeout(milliseconds);
	    restTemplate.setRequestFactory(httpRequestFactory);

	    // Add converters
	    // Note I use the Jackson Converter, I removed the http form converter 
	    // because it is not needed when posting String, used for multipart forms.
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}

	@Override
    public String postForEntity(String url_api401, MedicalTreatmentClaimVo vo, String s){
    	logger.info("**start into postForEntity()**");
        String strRes = null;
        
        try {
        	UnionCourseVo uc = new UnionCourseVo();
            uc.setCaseId(vo.getCaseId());
            uc.setTransNum(vo.getTransNum());
            uc.setType(uc.TYPE);
            uc.setName(s);
            uc.setCreateDate(new Date());
            
            logger.info("debug00");
            logger.info("url_api401="+url_api401);
            if(url_api401!=null && vo!=null) {
                ResponseEntity<String> responseEntity = null;

                HttpHeaders headers = new HttpHeaders();
                headers.set("Access-token", ACCESS_TOKEN);
                headers.setContentType(MediaType.APPLICATION_JSON);
                logger.info("debug01");
                
                //進行封裝數據,進行傳輸給聯盟
                //hpid
                vo.setHpId(vo.getToHospitalId());
				vo.setSubHpId(vo.getToSubHospitalId());
                
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("本人");
                stringBuffer.append(vo.getName());
                stringBuffer.append("/");
                stringBuffer.append(vo.getIdNo());
                
                logger.info("debug02");
                //查詢同意條款
                String parameterValueByCode = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ESERVICE, ApConstants.MEDICAL_TREATMENT_CONTENT);
                StringBuffer str = new StringBuffer();
                str.append(parameterValueByCode);
                String replace = str.toString().replaceAll("<([^>]*)>", "").replace("\r", "").replace("\t", "").replace("\n", "");
                stringBuffer.append(replace);

                logger.info("debug03");
                vo.setCpoa(stringBuffer.toString());
//                vo.setHsTime(vo.getAuthorizationStartDate());
//                vo.setHeTime(vo.getAuthorizationEndDate());

                logger.info("debug04");
                vo.setTo(vo.getToCompanyId());
                
                String authorizationFileType = vo.getAuthorizationFileType();
                String[] split = authorizationFileType.split(",");
                List<String> objects = new ArrayList<>();
                //1":診斷證明書，"2":收據
                for (int i = 0; i < split.length; i++) {
                	switch (split[i]){
                		case "1":
                			objects.add("CertificateDiagnosis");
                			break;
                		case "2":
                			objects.add("Receipt");
                			break;
                		default:
                			break;
                	}
                }
//                vo.setDtypes(objects);
                
                //accidentCause
                //disease=疾病,accident=意外
                String oriAccidentCause = vo.getAccidentCause();
                if("1".equals(oriAccidentCause)) {
                	vo.setAccidentCause("disease");
                }else if("2".equals(oriAccidentCause)){
                	vo.setAccidentCause("accident");
                }else {
                	logger.info("***Can't judge accidentCause,oriAccidentCause="+oriAccidentCause);
                }

                logger.info("debug05");
                Gson gson = new Gson();
                String json = gson.toJson(vo);
                logger.info("api401 resquest json="+json);

                HttpEntity<String> entity = new HttpEntity<String>(json,headers);
                restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
                responseEntity = restTemplate.postForEntity(url_api401, entity, String.class);

                if(responseEntity!=null) {
                    strRes = responseEntity.getBody();
                    logger.info("api401 responseEntity.getBody()="+strRes);
                }else {
                	logger.info("api401 responseEntity obj is null.");
                }
                // strRes= "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"caseId\":\"20210125153001-45c17f68e615-L01\"}}";
                
                logger.info("debug06");
                boolean checkRes = this.checkResponseStatus(responseEntity);
                if(checkRes) {
                    uc.setNcStatus(uc.NC_STATUS_S);
                    String caseId = MyJacksonUtil.readValue(strRes, "/data/caseId");
                    uc.setCaseId(caseId);
                }else {
                    uc.setNcStatus(uc.NC_STATUS_F);
                }
                uc.setCompleteDate(new Date());
                MyJacksonUtil.getNodeString(strRes, "msg");
                uc.setMsg(getResInfo(strRes));
                unionCourseDao.insertUnionCourseVo(uc);

                if (!checkRes) {
                    return null;
                }

            }
        }catch(Exception e) {
        	logger.info(e.toString());
        }
        
        return strRes;
    }
	
	@Override
	public String postForEntity(String url, Map<String, String> params,Map<String, String> unParams) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setType(uc.TYPE);
		if(unParams!=null) {
			uc.setCaseId(unParams.get("caseId"));
			uc.setTransNum(unParams.get("transNum"));
			uc.setName(unParams.get("name"));
		}
		uc.setCreateDate(new Date());
		if(url!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson(); 
	        String json = gson.toJson(params);
	        logger.info("CALL API:"+url+",resquest json="+json);
			
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        //聯盟API千萬不要用restTemplate.exchange()方式呼叫,Bad request.
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        //logger.info("(restTemplate.postForEntity(url, entity, String.class))=" + MyJacksonUtil.object2Json(responseEntity));
	        
//	        responseEntity = restTemplate.postForEntity(url, entity, String.class, json);//useful client code.
	        
	        boolean checkResp = this.checkResponseStatus(responseEntity);
	        if(checkResp) {
	        	uc.setNcStatus(uc.NC_STATUS_S);
	        }else {
	        	uc.setNcStatus(uc.NC_STATUS_F);
	        }
	        uc.setCompleteDate(new Date());
			uc.setMsg(getResInfo(strRes));
			if(unParams!=null) {//當unParams傳入為null,表示不寫入聯盟歷程
				unionCourseDao.insertUnionCourseVo(uc);
			}
	        
	        if (!checkResp) {
				return null;
			}
	        
			strRes= responseEntity.getBody();
			
			if(url.contains("ihs404i")) {
				//do not print base64 string
				if(strRes!=null) {
					logger.info("responseEntity.getBody() is not null.");
				}else {
					logger.info("responseEntity.getBody() is null.");
				}
			}else {
				logger.info("responseEntity.getBody():"+strRes);
			}
			
			
//			Object obj = responseEntity.getBody();
//			if (obj == null) {
//				return null;
//			}
//			return ((ApiResponseObj<T>) obj).getResult();
			
		}
		
		return strRes;
	}

	@Override
	public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}
	
	private String getResInfo(String strRes) {
        String str = "";
        try {
            if (strRes != null) {
                str = "{\"code\":"+MyJacksonUtil.getNodeString(strRes, "code")+",\"msg\":"+MyJacksonUtil.getNodeString(strRes, "msg")+"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
