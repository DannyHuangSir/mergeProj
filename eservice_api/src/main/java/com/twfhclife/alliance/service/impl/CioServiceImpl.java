package com.twfhclife.alliance.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
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
import com.twfhclife.alliance.model.ContactInfoParameterVo;
import com.twfhclife.alliance.model.UnionCourseVo;
import com.twfhclife.alliance.service.ICioExternalService;
import com.twfhclife.generic.utils.MyJacksonUtil;

@Service
public class CioServiceImpl implements ICioExternalService{
	
	private static final Logger logger = LogManager.getLogger(CioServiceImpl.class);
	
	// @Value("${alliance.api.accessToken}")
	public String ACCESS_TOKEN;

	private RestTemplate restTemplate;
	
	@Autowired
	private UnionCourseDao unionCourseDao;
	
	public CioServiceImpl() throws Exception{
		initRestTemplate();
	}
	
	private void initRestTemplate() throws Exception{
		// Fix the RestTemplate to be a singleton instance.
		//restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;
		restTemplate = new RestTemplate();//force init.

	    // Set the request factory. 
	    // IMPORTANT: This section I had to add for POST request. Not needed for GET
		int milliseconds = 20*1000;
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(milliseconds);
		httpRequestFactory.setConnectTimeout(milliseconds);
		httpRequestFactory.setReadTimeout(milliseconds);

		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
		httpRequestFactory.setHttpClient(httpClient);
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
	

	@Override
	public String postForEntity(String url, ContactInfoParameterVo contactInfoVo, String name) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(contactInfoVo.getCaseId());
		uc.setTransNum(contactInfoVo.getTransNum());
		uc.setType(uc.TYPE);
		uc.setName(name);
		
		if(url!=null && contactInfoVo!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			Gson gson = new Gson(); 
	        String json = gson.toJson(contactInfoVo);
	        logger.info("request json={}",json);
	        
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
	        
	        uc.setCreateDate(new Date());
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        uc.setCompleteDate(new Date());
	        
	        strRes = responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);
			boolean checkRes = this.checkResponseStatus(responseEntity);
			
			if(checkRes) {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_S);
			}else {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_F);
			}
			
			MyJacksonUtil.getNodeString(strRes, "msg");
			uc.setMsg(getResInfo(strRes));
			try {
				unionCourseDao.insertUnionCourseVo(uc);
			}catch(Exception e) {
				logger.info("call insertUnionCourseVo() error.e="+e.toString());
			}

	        if (!checkRes) {
				return null;
			}
	        
		}
		return strRes;
	}

	/**
	@Override
	public  int  insertUnionCourseVo(boolean boo,Map<String, String> unParams){
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(uc.TYPE);
		uc.setName(unParams.get("name"));
		uc.setCreateDate(new Date());
		if(boo) {
			uc.setNcStatus(uc.NC_STATUS_S);
		}else {
			uc.setNcStatus(uc.NC_STATUS_F);
		}
		uc.setCompleteDate(new Date());
		//uc.setMsg(getResInfo(strRes));
		int i = unionCourseDao.insertUnionCourseVo(uc);
		return  i;
	}
	*/

	@Override
	public String postForEntity(String url, Map<String, String> params,Map<String, String> unParams) throws Exception {
		this.initRestTemplate();//強制初始化restTemplate
		
		String strRes = null;
		
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(uc.TYPE);
		uc.setName(unParams.get("name"));
		
		if(url!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson(); 
	        String json = gson.toJson(params);
	        logger.info("request json={}",json);
			
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        
	        Calendar calObj = Calendar.getInstance();
	        Date ucCreateDdte = calObj.getTime();
	        uc.setCreateDate(ucCreateDdte);//UNION_COURSE.CRETE_DATE
	        //聯盟API千萬不要用restTemplate.exchange()方式呼叫,Bad request.

	        if( (url!=null && url.indexOf("lia206i")>=0) || params.containsKey("msg")){
	        	//API206回報,msg可能有中文字
	        	restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	        }
	        
	        this.logger.info("before call postForEntity:"+calObj.getTimeInMillis());
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        //logger.info("(restTemplate.postForEntity(url, entity, String.class))=" + MyJacksonUtil.object2Json(responseEntity));
	        calObj = Calendar.getInstance();
	        this.logger.info("after call postForEntity:"+calObj.getTimeInMillis());
	        
	        Date ucCompleteDate = calObj.getTime();
	        uc.setCompleteDate(ucCompleteDate);//UNION_COURSE.COMPLETE_DATE
	        
//	        responseEntity = restTemplate.postForEntity(url, entity, String.class, json);//useful client code.
	        
	       boolean checkResp = this.checkResponseStatus(responseEntity);
	       if(checkResp) {
	        	uc.setNcStatus(UnionCourseVo.NC_STATUS_S);
	        }else {
	        	uc.setNcStatus(UnionCourseVo.NC_STATUS_F);
	        }
			uc.setMsg(getResInfo(strRes));
			try{
				unionCourseDao.insertUnionCourseVo(uc);
			}catch(Exception e) {
				logger.info("call insertUnionCourseVo() error."+e.toString());
			}

	        if (!checkResp) {
				return null;
			}

			strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);
			//System.out.println("responseEntity.getBody()="+strRes);
			
//			Object obj = responseEntity.getBody();
//			if (obj == null) {
//				return null;
//			}
//			return ((ApiResponseObj<T>) obj).getResult();
			
		}
		
		return strRes;
	}

	@Override
	public String postForMapEntity(String url, Map<String, Object> params, Map<String, String> unParams) throws Exception {
		String strRes = null;
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(uc.TYPE);
		uc.setName(unParams.get("name"));
		
		if(url!=null) {
			ResponseEntity<String> responseEntity = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Access-token", ACCESS_TOKEN);
			headers.setContentType(MediaType.APPLICATION_JSON);

			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson();
			String json = gson.toJson(params);
			logger.info("request json={}",json);

			HttpEntity<String> entity = new HttpEntity<String>(json,headers);

			uc.setCreateDate(new Date());
			//聯盟API千萬不要用restTemplate.exchange()方式呼叫,Bad request.
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			responseEntity = restTemplate.postForEntity(url, entity, String.class);
			//logger.info("(restTemplate.postForEntity(url, entity, String.class))=" + MyJacksonUtil.object2Json(responseEntity));
			uc.setCompleteDate(new Date());

//	        responseEntity = restTemplate.postForEntity(url, entity, String.class, json);//useful client code.

			boolean checkResp = this.checkResponseStatus(responseEntity);
			if(checkResp) {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_S);
			}else {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_F);
			}

			if (!checkResp) {
				return null;
			}
			strRes = responseEntity.getBody();
			
			//API201  寫入caseId
			if (checkLiaAPIResponseValue(strRes, "/code", "0")) {
				String getCaseId = MyJacksonUtil.readValue(strRes, "/data/caseId");
				uc.setCaseId(getCaseId);
				uc.setMsg(getResInfo(strRes));
				try {
					unionCourseDao.insertUnionCourseVo(uc);
				} catch (Exception e) {
					logger.info("call insertUnionCourseVo() error,e=" + e.toString());
				}
			}

			logger.info("responseEntity.getBody()="+strRes);
			//System.out.println("responseEntity.getBody()="+strRes);

//			Object obj = responseEntity.getBody();
//			if (obj == null) {
//				return null;
//			}
//			return ((ApiResponseObj<T>) obj).getResult();

		}

		return strRes;
	}

	private boolean checkLiaAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
		boolean b = false;

		if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
			String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
			System.out.println("checkLiaAPIResponseValue="+code);
			if(checkValue.equals(code)) {//success
				b = true;
			}
		}
		System.out.println("checkLiaAPIResponseValue,return="+b);
		return b;
	}

	public static void main(String[] args){
		/*
		String url = "http://210.61.11.88:80/lia-api/api/ext/lia103i";
		Map<String,String> params = new HashMap<>();
		params.put("caseId", "20201231161500-9xCTpWe");
		Map<String, String> unionParams = new HashMap<String, String>();
		Gson gson = new Gson(); 
        String json = gson.toJson(params);
		System.out.println(json);
		
		try {
			CioServiceImpl impl = new CioServiceImpl();
			String str = impl.postForEntity(url, params, unionParams);
			
			System.out.println(str);
		}catch(Exception e) {
			System.out.println(e);
		}
		*/

	}

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String aCCESS_TOKEN) {
		ACCESS_TOKEN = aCCESS_TOKEN;
	}

	private String getResInfo(String strRes) {
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
