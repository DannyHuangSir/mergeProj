package com.twfhclife.eservice.api.elife.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.api.elife.domain.StreetNameDataVo;
import com.twfhclife.eservice.api.elife.service.IChunghwaApiService;
import com.twfhclife.generic.dao.adm.ParameterDao;

@Service
public class ChunghwaApiServiceimpl implements IChunghwaApiService{

	private static final Logger logger = LogManager.getLogger(ChunghwaApiServiceimpl.class);
	
	@Autowired
	private ParameterDao parameterDao;
	
	private String SYS_NAME = "eservice_batch";

	private String ROAD_NAME = "CHUNGHWA_API_URL";
	
	@Override
	public List<StreetNameDataVo> getStreetNameData(String city, String region) {
		logger.info("=======================Get CHUNGHWA_API_URL Start=========================");
				
    	String url = parameterDao.getParameterValueByCode(SYS_NAME, ROAD_NAME);
		
		List<StreetNameDataVo> list = new ArrayList<StreetNameDataVo>();

		try {
	        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
	            // 信任所有
	            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	                return true;
	            }
	        }).build();
			
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
	                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	 				
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			sb.append("city=" + city);
			sb.append("&");
			sb.append("cityarea=" + region);
			HttpPost startStubMethod = new HttpPost(sb.toString());
			startStubMethod.setHeader("Content-type" , "application/json");
			
			CloseableHttpResponse response = client.execute(startStubMethod);
			if(response != null) {
				String body = EntityUtils.toString(response.getEntity() , "UTF-8");
				body = body.replace("\r\n", "");
				body = body.replace("\t", "");
				body = body.trim();
				Type listType = new TypeToken<List<StreetNameDataVo>>() {}.getType();
				list = new Gson().fromJson(body, listType);
				logger.info("=======================Get CHUNGHWA_API_URL END=========================");
				response.close();
				client.close();
				return list;
			}else {
				logger.info("=======================Get CHUNGHWA_API_URL END=========================");
				client.close();
				return null;
			}
		} catch (KeyManagementException e) {
			logger.info("=======================Get CHUNGHWA_API_URL Error=========================" + e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			logger.info("=======================Get CHUNGHWA_API_URL Error=========================" + e);
			return null;
		} catch (KeyStoreException e) {
			logger.info("=======================Get CHUNGHWA_API_URL Error=========================" + e);
			return null;
		}catch (ClientProtocolException e) {
			logger.info("=======================Get CHUNGHWA_API_URL Error=========================" + e);
			return null;
		} catch (IOException e) {
			logger.info("=======================Get CHUNGHWA_API_URL Error=========================" + e);
			return null;
		}
	}	
	
}
