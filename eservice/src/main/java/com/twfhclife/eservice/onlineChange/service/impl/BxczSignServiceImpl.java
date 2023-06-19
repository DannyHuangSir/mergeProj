package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.gson.Gson;
import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.eservice.onlineChange.model.Bxcz415CallBackDataVo;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import com.twfhclife.generic.util.MyJacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BxczSignServiceImpl implements IBxczSignService {

	private static final Logger logger = LogManager.getLogger(BxczSignServiceImpl.class);

	@Autowired
	private BxczDao bxczDao;

	public String ACCESS_TOKEN;

	private RestTemplate restTemplate;

	public BxczSignServiceImpl() {

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
    public int addSignBxczRecord(SignRecord signRecord) {

        return bxczDao.insertBxczSignRecord(signRecord,null, null, null, null);
    }

	@Override
	public int updateSignRecordStatus(String code, String msg, Bxcz415CallBackDataVo vo) {
		Date signTime = null;
		Date verifyTime = null;
		if (StringUtils.isNotBlank(vo.getSignTime())) {
			try {
				signTime = new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getSignTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		if (StringUtils.isNotBlank(vo.getIdVerifyTime())) {
			try {
				verifyTime = new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getIdVerifyTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return bxczDao.updateBxczSignRecordByActionId(vo, code, msg, verifyTime, signTime);
	}

	@Override
	public SignRecord getSignRecord(String actionId) {
		return bxczDao.getSignRecordByActionId(actionId);
	}

	@Override
	public SignRecord getNewSignStatus(String transNum) {
		return bxczDao.getNewSignStatus(transNum);
	}

    @Override
    public byte[] getSignPdf(String signFileId) {
		String fileBase64 = bxczDao.getSignFileByFileId(signFileId);
        return StringUtils.isBlank(fileBase64) ? null : Base64.getDecoder().decode(fileBase64);
    }

	@Override
	public List<SignRecord> getNotDownloadSignFile() {
		return bxczDao.getNotDownloadSignFile();
	}

	@Override
	public int updateSignDownloaded(String actionId) {
		return bxczDao.updateSignDownloaded(actionId);
	}

	@Override
	public int addSignFileData(String fileId, String clientId, String fileBase64) {
		return bxczDao.insertSignFileData(fileId, clientId, fileBase64);
	}

	@Override
	public String postForJson(String url, HttpHeaders headers, Map<String, String> params) throws Exception  {
		Gson gson = new Gson();
		String json = gson.toJson(params);
		logger.info("resquest json="+json);
		HttpEntity<String> entity = new HttpEntity<String>(json,headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
		boolean checkResp  = this.checkResponseStatus(responseEntity);//check http status
		boolean checkCode0 = false;
		if(checkResp) {
			if (responseEntity!=null && responseEntity.getBody()!=null) {
				checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
			}
		}
		if (checkCode0 && checkResp) {
			return MyJacksonUtil.readValue(responseEntity.getBody(), "/data/content");
		} else {
			return null;
		}
	}

	@Override
	public String postApi416(String api416Url, HttpHeaders headers, Map<String, String> api416Params) throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(api416Params);
		logger.info("resquest json="+json);
		HttpEntity<String> entity = new HttpEntity<String>(json,headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(api416Url, entity, String.class);
		boolean checkResp  = this.checkResponseStatus(responseEntity);//check http status
		boolean checkCode0 = false;
		if(checkResp) {
			if (responseEntity!=null && responseEntity.getBody()!=null) {
				checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
			}
		}
		if (checkCode0 && checkResp) {
			logger.info("call api416 resp: {}", responseEntity.getBody());
			return responseEntity.getBody();
		} else {
			return null;
		}
	}

	@Override
	public String postApi108(String urlApi108, Map<String, String> api108Params) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-token", ACCESS_TOKEN);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Gson gson = new Gson();
		String json = gson.toJson(api108Params);
		logger.info("resquest json=" + json);
		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlApi108, entity, String.class);
		boolean checkResp = this.checkResponseStatus(responseEntity);//check http status
		boolean checkCode0 = false;
		if (checkResp) {
			if (responseEntity != null && responseEntity.getBody() != null) {
				checkCode0 = checkLiaAPIResponseValue(responseEntity.getBody(), "/code", "0");
			}
		}
		if (checkCode0 && checkResp) {
			return responseEntity.getBody();
		} else {
			return null;
		}
	}

	@Override
	public int addSignBxczApiRecord(BxczSignApiLog bxczSignApiLog) {
		return bxczDao.addSignApiLog(bxczSignApiLog);
	}

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

	public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
		logger.info("http status=" + responseEntity.getStatusCodeValue());
		if(responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
			// 200 OK
			return true;
		} else {
			return false;
		}
	}

	public String getACCESS_TOKEN() {
		return ACCESS_TOKEN;
	}

	public void setACCESS_TOKEN(String ACCESS_TOKEN) {
		this.ACCESS_TOKEN = ACCESS_TOKEN;
	}
}