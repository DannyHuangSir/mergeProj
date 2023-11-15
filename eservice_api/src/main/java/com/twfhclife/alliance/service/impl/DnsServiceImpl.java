package com.twfhclife.alliance.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
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
	
	public DnsServiceImpl() throws Exception{
		//Fix the RestTemplate to be a singleton instance.
	    restTemplate = (this.restTemplate == null) ? new RestTemplate() : restTemplate;

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
	public String postForHttpURLConnection(String url, Map<String, String> params, Map<String, String> unParams) throws Exception {

		String strRes = null;

		//呼叫記錄object
		String apiName = unParams.get("name");
		
		UnionCourseVo uc = new UnionCourseVo();
		uc.setCaseId(unParams.get("caseId"));
		uc.setTransNum(unParams.get("transNum"));
		uc.setType(UnionCourseVo.TYPE);
		uc.setName(apiName);

		if(url!=null) {

			//org.json.JSONObject jsonObj = new org.json.JSONObject(params);
			Gson gson = new Gson();
			String json = gson.toJson(params);
			logger.info(apiName+",request json={}",json);

			uc.setCreateDate(new Date());
			
			//建立連線
			java.net.URL netUrl = new java.net.URL(url);
			java.net.HttpURLConnection httpConn=(java.net.HttpURLConnection)netUrl.openConnection();
			//設定引數
			httpConn.setDoOutput(true);//需要輸出
			httpConn.setDoInput(true);//需要輸入
			httpConn.setUseCaches(false);//不允許快取
			httpConn.setRequestMethod("POST");//設定POST方式連線
			//設定請求屬性
			httpConn.setRequestProperty("Content-Type", "application/json");
			//httpConn.setRequestProperty("Connection", "Keep-Alive");// 維持長連線
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setRequestProperty("Authorization", "Bearer "+this.ACCESS_TOKEN_DNS_AUTHORIZATION);
			httpConn.setRequestProperty("call_user",unParams.get("call_user") );
			//連線
			httpConn.connect();
			//建立輸入流，向指向的URL傳入引數
			java.io.DataOutputStream dos = new java.io.DataOutputStream(httpConn.getOutputStream());
			dos.write(json.toString().getBytes("UTF-8"));
			dos.flush();
			dos.close();
			//獲得響應狀態
			int resultCode = httpConn.getResponseCode();
			boolean checkResp = false;
			if(java.net.HttpURLConnection.HTTP_OK == resultCode) {
				checkResp = true;
			}
			if(checkResp){
				StringBuffer sb = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = 
						new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
				while((readLine = responseReader.readLine())!=null){
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				strRes = sb.toString();
				logger.info("response string="+strRes);
			}else {
				logger.info("HttpURLConnection is not OK.");
			}

			uc.setCompleteDate(new Date());

			if(checkResp) {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_S);
			}else {
				uc.setNcStatus(UnionCourseVo.NC_STATUS_F);
			}
			uc.setMsg(getDnsApiResponseDetail(strRes));

			try {
				//歷程錯誤不能影響聯盟response
				unionCourseDao.insertUnionCourseVo(uc);
			}catch(Exception e) {
				//do nothing.
			}

			if (!checkResp) {
				return null;
			}

			//strRes= responseEntity.getBody();
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
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 取得核心DNS API responst
	 * @param strRes
	 * @return
	 */
	private String getDnsApiResponseDetail(String strRes) {
		String str = "";
		try {
			if (strRes != null) {
				String detailStatus  = MyJacksonUtil.readValue(strRes, "/data/detail_status");
				String detailMessage = MyJacksonUtil.readValue(strRes, "/data/detail_message");
				str = "detailStatus:"+detailStatus + ",detail_message:"+detailMessage;
				//str = "{\"detail_status\":"+MyJacksonUtil.getNodeString(strRes, "data/detail_status")+",\"detail_message\":"+MyJacksonUtil.getNodeString(strRes, "data/detail_message")+"}";
			}
		} catch (Exception e) {
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
	
	public static void main(String[] args) throws Exception{
		String rtn = "{\"success\": true,\"data\": {\"detail_status\": \"0\",\"detail_message\": \"〔查詢完成〕\",\"values\": [{\"FSZ1-ID\": \"Z1234567\",\"FSZ1-PI-ST\": \"00\"}]}}\r\n" + 
				"";
		
		DnsServiceImpl impl = new DnsServiceImpl();
		String output = impl.getDnsApiResponseDetail(rtn);
		System.out.println("output="+output);
		
		//String id = MyJacksonUtil.readValue(rtn, "/data/values/FSZ1-ID");
	}

}
