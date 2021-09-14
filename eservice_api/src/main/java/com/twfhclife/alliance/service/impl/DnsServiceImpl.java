package com.twfhclife.alliance.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.twfhclife.alliance.dao.UnionCourseDao;
import com.twfhclife.alliance.model.UnionCourseVo;
import com.twfhclife.alliance.service.IDnsExternalService;
import com.twfhclife.generic.utils.MyJacksonUtil;

/**
 *  死亡除戶
 *  呼叫聯盟Service
 *
 */
@Service
public class DnsServiceImpl implements IDnsExternalService {
	
	private static final Logger logger = LogManager.getLogger(DnsServiceImpl.class);
	
	//@Value("${alliance.api.accessToken}")
	public String ACCESS_TOKEN;
	
	/**
	 * 請透過公會申請，或延用現有新通報平台的 Access-Token
	 */
	//@Value("${alliance.api.dns101.accessToken}")
	public String ACCESS_TOKEN_DNS101;
	

	private RestTemplate restTemplate;
	
	@Autowired
	private UnionCourseDao unionCourseDao;
	
	public DnsServiceImpl() {
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

	/**
	 * 呼叫外部 api
	 * @param url
	 * @param params
	 * @param unParams
	 * @return String json
	 * @throws Exception
	 */
	@Override
	public String postForEntity(String url, Map<String, String> params, Map<String, String> unParams) throws Exception {
		
		String strRes = null;
		
		//呼叫記錄object
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(UnionCourseVo.TYPE);
		uc.setName(unParams.get("name"));
		
		if(url!=null) {
			ResponseEntity<String> responseEntity = null;
			
			HttpHeaders headers = new HttpHeaders();
			//if(url.endsWith("dns101i")){//外部的URL有可能會異動,故唯一可百分百確認的只有自行設定的unParams
			if("DNS101".equals(unParams.get("name"))) {
				headers.set("Access-token", this.ACCESS_TOKEN_DNS101);
			}else {
			headers.set("Access-token", ACCESS_TOKEN);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson(); 
	        String json = gson.toJson(params);
	        logger.info("request json={}",json);
	        
	        HttpEntity<String> entity = new HttpEntity<String>(json,headers);
	        uc.setCreateDate(new Date());
	        responseEntity = restTemplate.postForEntity(url, entity, String.class);
	        uc.setCompleteDate(new Date());
	        
	        boolean checkResp = this.checkResponseStatus(responseEntity);

	        if(checkResp) {
	        	uc.setNcStatus(UnionCourseVo.NC_STATUS_S);
	        }else {
	        	uc.setNcStatus(UnionCourseVo.NC_STATUS_F);
	        }
			uc.setMsg(getResInfo(strRes));
			
			try {
				//歷程錯誤不能影響聯盟response
				unionCourseDao.insertUnionCourseVo(uc);
			}catch(Exception e) {
				//do nothing.
			}
	        
			if (!checkResp) {
				return null;
			}
			
			strRes= responseEntity.getBody();
			logger.info("responseEntity.getBody()="+strRes);
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
	
	/**
	 * 取得聯盟回傳的code,msg
	 * @param strRes
	 * @return
	 */
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

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String aCCESS_TOKEN) {
		ACCESS_TOKEN = aCCESS_TOKEN;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getACCESS_TOKEN_DNS101() {
		return ACCESS_TOKEN_DNS101;
	}

	public void setACCESS_TOKEN_DNS101(String aCCESS_TOKEN_DNS101) {
		ACCESS_TOKEN_DNS101 = aCCESS_TOKEN_DNS101;
	}

}
