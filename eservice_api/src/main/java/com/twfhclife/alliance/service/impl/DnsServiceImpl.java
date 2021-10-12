package com.twfhclife.alliance.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
	
	//死亡除戶推送給核心TOKENZ值
	public String ACCESS_TOKEN_DNS_AUTHORIZATION;

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
	public String postCoreEntity(String url, Map<String, String> params, Map<String, String> unParams) throws Exception {

		String strRes = null;

		//呼叫記錄object
		String apiName = unParams.get("name");
		
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(UnionCourseVo.TYPE);
		uc.setName(apiName);

		if(url!=null) {
			ResponseEntity<String> responseEntity = null;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer "+this.ACCESS_TOKEN_DNS_AUTHORIZATION);
			headers.set("call_user",unParams.get("call_user") );
			headers.setAccept(java.util.Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			
//			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//	        requestBody.add("FSZ1-SCN-NAME","FSZ1");
//	        requestBody.add("FSZ1-FUNC-CODE","IN");
//	        requestBody.add("FSZ1-INSU-NO",params.get("FSZ1-INSU-NO"));
//	        requestBody.add("FSZ1-ID",params.get("FSZ1-ID"));
	        //HttpEntity<MultiValueMap> entity = new HttpEntity<MultiValueMap>(requestBody, headers);

			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson();
			String json = gson.toJson(params);
			logger.info(apiName+",request json={}",json);

			HttpEntity<String> entity = new HttpEntity<String>(json,headers);
			uc.setCreateDate(new Date());
			
			//responseEntity = restTemplate.postForEntity(url, entity, String.class);//HttpClientErrorException 400
			//Fsz1 fsz1 = restTemplate.postForObject(url, entity, Fsz1.class);//HttpClientErrorException 400
			//logger.info("apiName={}"+apiName+",response Fsz1 Object={}"+fsz1);
			//responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);//HttpClientErrorException 400
			//String rtnAfterPost = restTemplate.postForObject(url, entity, String.class);//HttpClientErrorException 400
			//logger.info("rtnAfterPost={}",rtnAfterPost);
			
			String postRequest = "POST format";
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Authorization", "Bearer "+this.ACCESS_TOKEN_DNS_AUTHORIZATION);
			request.setHeader("call_user",unParams.get("call_user") );
			//Request body
		    StringEntity reqEntity = new StringEntity(postRequest,"UTF-8");
		    request.setEntity(reqEntity);
		    logger.info("call url="+url+",request.toString()="+request.toString());
		    //Response
		    HttpResponse response = httpClient.execute(request);
		    org.apache.http.HttpEntity apachHttpEntity = response.getEntity();
		    logger.info("apacheHttpEntity="+apachHttpEntity);

		    BufferedReader rd = new BufferedReader(new InputStreamReader(apachHttpEntity.getContent()));
		    StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			String resultString = result.toString();
			logger.info("resultString="+resultString);
			
		    
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
		if(responseEntity==null) {
			return false;
		}
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

	public String getACCESS_TOKEN_DNS_AUTHORIZATION() {
		return ACCESS_TOKEN_DNS_AUTHORIZATION;
	}

	public void setACCESS_TOKEN_DNS_AUTHORIZATION(String ACCESS_TOKEN_DNS_AUTHORIZATION) {
		this.ACCESS_TOKEN_DNS_AUTHORIZATION = ACCESS_TOKEN_DNS_AUTHORIZATION;
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

/**
 * 核心系統API回傳物件
 *
 */
class Fsz1{
	private String success;
	private Data data;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
}

class Data{
	private String token;
	private String detailSuccess;
	private String detailMessage;
	private Values values;
	
	public Values getValues() {
		return values;
	}
	public void setValues(Values values) {
		this.values = values;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDetailSuccess() {
		return detailSuccess;
	}
	public void setDetailSuccess(String detailSuccess) {
		this.detailSuccess = detailSuccess;
	}
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
	
}

class Values{
	private String fsz1ScnName;
	private String fsz1FuncCode;
	private String fsz1InsuNo;
	private String fsz1Id;
	private String fsz1PiSt;
	
	public String getFsz1ScnName() {
		return fsz1ScnName;
	}
	public void setFsz1ScnName(String fsz1ScnName) {
		this.fsz1ScnName = fsz1ScnName;
	}
	public String getFsz1FuncCode() {
		return fsz1FuncCode;
	}
	public void setFsz1FuncCode(String fsz1FuncCode) {
		this.fsz1FuncCode = fsz1FuncCode;
	}
	public String getFsz1InsuNo() {
		return fsz1InsuNo;
	}
	public void setFsz1InsuNo(String fsz1InsuNo) {
		this.fsz1InsuNo = fsz1InsuNo;
	}
	public String getFsz1Id() {
		return fsz1Id;
	}
	public void setFsz1Id(String fsz1Id) {
		this.fsz1Id = fsz1Id;
	}
	public String getFsz1PiSt() {
		return fsz1PiSt;
	}
	public void setFsz1PiSt(String fsz1PiSt) {
		this.fsz1PiSt = fsz1PiSt;
	}
	
}
