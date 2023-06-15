package com.twfhclife.alliance.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.alliance.model.SignInsuranceClaimMapperVo;
import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.UnionCourseVo;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.generic.utils.MyJacksonUtil;

@Service
public class AllianceServiceImpl implements IExternalService{
	
	private static final Logger logger = LogManager.getLogger(AllianceServiceImpl.class);
	
	// @Value("${alliance.api.accessToken}")
	public String ACCESS_TOKEN;

	private RestTemplate restTemplate;
	
	@Autowired
	private UnionCourseDao unionCourseDao;
	
	public AllianceServiceImpl() {
		
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

	public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
     * @param responseJsonString
     * @param pathFieldName ex:"/code"
     * @param checkValue ex:"0"
     * @return boolean
     */
    public boolean checkLiaAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
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
	

	@Override
	public String postForEntity(String url, InsuranceClaimMapperVo cliaimVo, String name) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(cliaimVo.getCaseId());
		uc.setTransNum(cliaimVo.getTransNum());
		uc.setType(uc.TYPE);
		uc.setName(name);
		uc.setCreateDate(new Date());
		
		if(url!=null && cliaimVo!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);

			Gson gson = new Gson(); 
	        String json = gson.toJson(cliaimVo);
	        logger.info("resquest json="+json);
	        /**
			//替換-start
			//to:[{"companyId","L02"},{"companyId","L04"}] >> to:{L02,L04}
			String toStr = "";
			if(cliaimVo.getTo()==null && cliaimVo.getTo().size()<=0) {
				return null;
			}else {
				
				List<CompanyVo> listVo = cliaimVo.getTo();
				for(CompanyVo vo : listVo) {
					toStr += vo.getCompanyId()+",";
				}

				if (toStr.endsWith(",")) {
					toStr = toStr.substring(0, toStr.length() - 1);
				}
			}
			
			Gson gson = new Gson(); 
	        String json = gson.toJson(cliaimVo);
	        
	        ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode  = objectMapper.readTree(json);
			JsonNode newToNode = new TextNode(toStr);
			ObjectNode nodeObj = (ObjectNode) rootNode;
			nodeObj.remove("to");
			nodeObj.set("to", newToNode);
			json = rootNode.toString();
			logger.info("repleae json="+json);
			//替換-end
	        **/
	        
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        
	        strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);

			// 20220907 by 203990
			/// add checkCode0
	        boolean checkRes  = this.checkResponseStatus(responseEntity);//check http status
	        boolean checkCode0 = false;//check response code value.
	        if(checkRes) {
	        	if (responseEntity!=null && responseEntity.getBody()!=null) {
		        	//String(10),0代表成功,錯誤代碼則自行定義
		        	checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
		        }
	        }
	        
	        if(checkCode0 && checkRes) {
	        	uc.setNcStatus(uc.NC_STATUS_S);
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
		return strRes;
	}

    @Override
    public String postForEntity(String url, SignInsuranceClaimMapperVo cliaimVo, String name) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(cliaimVo.getCaseId());
		uc.setTransNum(cliaimVo.getTransNum());
		uc.setType(uc.TYPE);
		uc.setName(name);
		uc.setCreateDate(new Date());

		if(url!=null && cliaimVo!=null) {
			ResponseEntity<String> responseEntity = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);

			Gson gson = new Gson();
			String json = gson.toJson(cliaimVo);
			logger.info("resquest json="+json);

			HttpEntity<String> entity = new HttpEntity<String>(json,headers);

			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			responseEntity = restTemplate.postForEntity(url, entity, String.class);

			strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);

			// 20220907 by 203990
			/// add checkCode0
			boolean checkRes  = this.checkResponseStatus(responseEntity);//check http status
			boolean checkCode0 = false;//check response code value.
			if(checkRes) {
				if (responseEntity!=null && responseEntity.getBody()!=null) {
					//String(10),0代表成功,錯誤代碼則自行定義
					checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
				}
			}

			if(checkCode0 && checkRes) {
				uc.setNcStatus(uc.NC_STATUS_S);
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
		return strRes;
    }


    @Override
	public String postForEntity(String url, Map<String, String> params,Map<String, String> unParams) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(uc.TYPE);
		uc.setName(unParams.get("name"));
		uc.setCreateDate(new Date());
		if(url!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson(); 
	        String json = gson.toJson(params);
	        logger.info("resquest json="+json);
			
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        //聯盟API千萬不要用restTemplate.exchange()方式呼叫,Bad request.
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        //logger.info("(restTemplate.postForEntity(url, entity, String.class))=" + MyJacksonUtil.object2Json(responseEntity));
	        
//	        responseEntity = restTemplate.postForEntity(url, entity, String.class, json);//useful client code.
	        
	        boolean checkResp  = this.checkResponseStatus(responseEntity);//check http status
	        boolean checkCode0 = false;//check response code value.
	        if(checkResp) {
	        	if (responseEntity!=null && responseEntity.getBody()!=null) {
		        	//String(10),0代表成功,錯誤代碼則自行定義
		        	checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
		        }
	        }
	        
	        if(checkCode0 && checkResp) {
	        	uc.setNcStatus(uc.NC_STATUS_S);
	        }else {
	        	uc.setNcStatus(uc.NC_STATUS_F);
	        }
	        uc.setCompleteDate(new Date());
			uc.setMsg(getResInfo(strRes));
			unionCourseDao.insertUnionCourseVo(uc);
	        
	        if (!checkResp) {
				return null;
			}
	        

			strRes = responseEntity != null ? responseEntity.getBody() : null;
			//System.out.println("responseEntity.getBody()="+strRes);
			
//			Object obj = responseEntity.getBody();
//			if (obj == null) {
//				return null;
//			}
//			return ((ApiResponseObj<T>) obj).getResult();
			
		}
		
		return strRes;
	}
	
	public static void main(String[] args){
		
		String url = "http://210.61.11.88:80/lia-api/api/ext/lia103i";
		Map<String,String> params = new HashMap<>();
		params.put("caseId", "20201231161500-9xCTpWe");
		Map<String, String> unionParams = new HashMap<String, String>();
		Gson gson = new Gson(); 
        String json = gson.toJson(params);
		System.out.println(json);
		
		try {
			AllianceServiceImpl impl = new AllianceServiceImpl();
			String str = impl.postForEntity(url, params, unionParams);
			
			System.out.println(str);
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String aCCESS_TOKEN) {
		ACCESS_TOKEN = aCCESS_TOKEN;
	}

	private String getResInfo(String strRes) {
		// TODO Auto-generated method stub
		String str = "";
		try {
			if (strRes != null) {
				str = "{\"code\":"+MyJacksonUtil.getNodeString(strRes, "code")+",\"msg\":"+MyJacksonUtil.getNodeString(strRes, "msg")+"}";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public List<String> getPolicyNoByID(String id) {
		return unionCourseDao.getPolicyNoByID(id);
	}

	@Override
	public List<String> getProductCodeByPolicyNo(String policyNo) {
		List<String> rtn = null;
		if(policyNo!=null && !"".equals(policyNo.trim())) {
			rtn = unionCourseDao.getProductCodeByPolicyNo(policyNo);
		}
		return rtn;
	}

}
