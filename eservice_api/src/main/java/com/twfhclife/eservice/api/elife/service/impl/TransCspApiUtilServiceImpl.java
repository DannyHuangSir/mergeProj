package com.twfhclife.eservice.api.elife.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.twfhclife.eservice.api.elife.domain.Csp002Data;
import com.twfhclife.eservice.api.elife.domain.Csp002Detail;
import com.twfhclife.eservice.api.elife.domain.Csp002Vo;
import com.twfhclife.eservice.api.elife.domain.Csp003Data;
import com.twfhclife.eservice.api.elife.domain.Csp003Detail;
import com.twfhclife.eservice.api.elife.domain.Csp003Vo;
import com.twfhclife.eservice.api.elife.domain.CspData;
import com.twfhclife.eservice.api.elife.domain.CspResultVo;
import com.twfhclife.eservice.api.elife.domain.TransCsp002DataRequest;
import com.twfhclife.eservice.api.elife.domain.TransCspApiUtilRequest;
import com.twfhclife.eservice.api.elife.domain.TransOnlineTrialVo;
import com.twfhclife.eservice.api.elife.service.ITransCspApiUtilService;
import com.twfhclife.eservice.web.dao.ParameterDao;



@Service
public class TransCspApiUtilServiceImpl implements ITransCspApiUtilService{

	private RestTemplate restTemplate;
	
	@Autowired
	private ParameterDao parameterDao;
	
	private String SYSTEM_ID= "eservice_api";
	private String CSP001_URL= "CSP001_URL";
	private String CSP001_TOKEN = "CSP001_TOKEN";
	private String CSP002_URL= "CSP002_URL";
	private String CSP002_TOKEN = "CSP002_TOKEN";
	private String CSP003_URL= "CSP003_URL";
	private String CSP003_TOKEN = "CSP003_TOKEN";
	

	
	public TransCspApiUtilServiceImpl() throws Exception{
		initRestTemplate();
	}
	
	private void initRestTemplate()throws Exception {
		restTemplate = new RestTemplate();
		
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
	    
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}
	
	
	@Override
	public CspData getCsp001Detail(TransCspApiUtilRequest request) {

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, getAuthorization(CSP001_TOKEN));
		headers.setContentType(MediaType.APPLICATION_JSON);		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		StringBuilder sb = new StringBuilder();
		sb.append(getURL(CSP001_URL));//http://10.7.168.66:8280/csp/api/provide/v1.0.0/ESRV-DS16?
		sb.append("CB-SCN-NAME="); //交易代號
		sb.append("DS16 "); // 固定 DS16 
		sb.append("&");
		sb.append("CB-FUNC-CODE="); //交易類別
		sb.append("IA"); // 固定 IA 
		sb.append("&");
		sb.append("DS16-GP-NO="); //保單號碼
		sb.append(request.getInsuSeqNo());
		sb.append("&");
		sb.append("DS16-CHAN-PAID-UP="); // 狀態 繳清 : Y，;展期 : E
		sb.append(request.getPolicyType());

//		String url = "http://10.7.168.66:8280/csp/api/provide/v1.0.0/ESRV-DS16?CB-SCN-NAME=DS16 &CB-FUNC-CODE=IA&DS16-GP-NO=EE10000061&DS16-CHAN-PAID-UP=Y";
		ResponseEntity<CspData> responseEntity = restTemplate.exchange(sb.toString(), HttpMethod.GET, entity, CspData.class);

		CspData obj = responseEntity.getBody();
		if (obj == null) {
			return null;
		}
		return obj;
	}
	
	private String getURL(String urlLocation) {
		String url = parameterDao.getParameterValueByCode(SYSTEM_ID,
				urlLocation);
		return url;
	}
	
	private String getAuthorization(String token) {
		String authorization = parameterDao.getParameterValueByCode(SYSTEM_ID,
				token);
		return authorization;
	}

	public TransOnlineTrialVo getCsp0021Detail(TransCsp002DataRequest reuqest) {
		TransOnlineTrialVo vo = new TransOnlineTrialVo();		
		if(reuqest.isMaturityType()) {
			CspResultVo resultVo = getCsp002Api(reuqest , "2");
			if(resultVo != null) {
				if(StringUtils.isNotEmpty(resultVo.getResult())) {
					vo.setMaturityAmt(resultVo.getResult());
				}else {
					if(StringUtils.isNotEmpty(resultVo.getErrorMsg())) {
						vo.setMaturityMsg(resultVo.getErrorMsg());
					}else {
						vo.setMaturityMsg("查無滿期金額資料");
					}
				}
			}else {
				vo.setMaturityMsg("查無滿期金額資料");
			}
		}
		
		if(reuqest.isCancellationType()) {
			CspResultVo resultVo = getCsp002Api(reuqest , "3");
			if(resultVo != null) {
				if(StringUtils.isNotEmpty(resultVo.getResult())) {
					vo.setCancellationAmt(resultVo.getResult());
				}else {
					if(StringUtils.isNotEmpty(resultVo.getErrorMsg())) {
						vo.setCancellationMsg(resultVo.getErrorMsg());
					}else {
						vo.setCancellationMsg("查無解約金額資料");
					}
				}
			}else {
				vo.setCancellationMsg("查無解約金額資料");
			}
		}
		
		if(reuqest.isLoanType()) {
			CspResultVo resultVo = getCsp002Api(reuqest , "4");
			if(resultVo != null) {
				if(StringUtils.isNotEmpty(resultVo.getResult())) {
					vo.setLoanAmt(resultVo.getResult());
				}else {
					if(StringUtils.isNotEmpty(resultVo.getErrorMsg())) {
						vo.setLoanMsg(resultVo.getErrorMsg());
					}else {
						vo.setLoanMsg("查無可貸金額資料");
					}
				}
			}else {
				vo.setLoanMsg("查無可貸金額資料");
			}
		}
		
		if (reuqest.isRepaymentType()) {
			CspResultVo resultVo = getCsp002Api(reuqest, "5");
			if(resultVo != null) {
				if(StringUtils.isNotEmpty(resultVo.getResult())) {
					vo.setRepaymentAmt(resultVo.getResult());
					CspResultVo resultVo2 =	getCsp003Api(reuqest.getLipmInsuNo());
					if(resultVo2 != null) {
						if(StringUtils.isNotEmpty(resultVo2.getResult())) {
							vo.setRefundAccount(resultVo2.getResult());
						}else {
							if(StringUtils.isNotEmpty(resultVo2.getErrorMsg())) {
								vo.setRefundMsg(resultVo2.getErrorMsg());
							}else {
								vo.setRefundMsg("查無虛擬帳號資料");
							}
						}
					}else {
						vo.setRefundMsg("查無虛擬帳號資料");
					}
				}else {
					if(StringUtils.isNotEmpty(resultVo.getErrorMsg())) {
						vo.setRepaymentMsg(resultVo.getErrorMsg());
					}else {
						vo.setRepaymentMsg("查無還款金額資料");
					}
				}
			}else {
				vo.setRepaymentMsg("查無還款金額資料");
			}		
		}	
		return vo;
	}
	
	private CspResultVo getCsp002Api(TransCsp002DataRequest reuqest , String type) {
		CspResultVo vo = new CspResultVo();
		try {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, getAuthorization(CSP002_TOKEN));
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String , String>body = new HashMap<String , String >();
		body.put("FSZ2-SCN-NAME" ,	"FSZ2");
		body.put("FSZ2-FUNC-CODE" ,	"IN");
		body.put("FSZ2-INSU-NO" ,	reuqest.getLipmInsuNo());
		body.put("FSZ2-CALC-DATE" ,	reuqest.getDate());
		body.put("FSZ2-CALC-TYPE" ,	type);
		HttpEntity<?> entity = new HttpEntity<>(body , headers);
		String url = getURL(CSP002_URL); //"http://10.7.168.66:8280/csp/api/provide/v1.0.0/ESRV-FSZ2";
		ResponseEntity<Csp002Vo> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Csp002Vo.class);
		if(responseEntity != null) {
			Csp002Vo csp002Vo = responseEntity.getBody();
			if(csp002Vo.getSuccess().equals("true")) {
				Csp002Data csp002Data =csp002Vo.getData();
				if(csp002Data.getDetailStatus().equals("0")) {
					List<Csp002Detail>  list = csp002Data.getValues();
					if(list.size() > 0) {
						Csp002Detail detail = list.get(0);
						vo.setResult(detail.getFsz2Amt());
					}
				}else {
					vo.setErrorMsg(csp002Data.getDetailMessage());
				}
			}
		}
		
		}catch (Exception e) {
			return vo;
		}
		return vo;
	}
	
	private CspResultVo getCsp003Api(String lipmInsuNo) {
		CspResultVo vo = new CspResultVo();
		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.AUTHORIZATION, getAuthorization(CSP003_TOKEN));
		header.setContentType(MediaType.APPLICATION_JSON);
		Map<String , String>body = new HashMap<String , String >();
		body.put("FS82-SCN-NAME", "FS82");
		body.put("FS82-FUNC-CODE", "IN");
		body.put("FS82-BRANCH-CODE", "");
		body.put("FS82-DP", "");
		body.put("FS82-INSU-NO", lipmInsuNo);
		HttpEntity<?> entity = new HttpEntity<>(body , header);
		String url = getURL(CSP003_URL); //"http://10.7.168.66:8280/csp/api/provide/v1.0.0/ESRV-FSZ2";
		ResponseEntity<Csp003Vo> responseEntitys = restTemplate.exchange(url, HttpMethod.POST, entity, Csp003Vo.class);
		if (responseEntitys != null) {
			Csp003Vo csp003Vo = responseEntitys.getBody();
			if (csp003Vo.getSuccess().equals("true")) {
				Csp003Data csp003Data = csp003Vo.getData();
				if (csp003Data.getDetailStatus().equals("0")) {
					List<Csp003Detail> lists = csp003Data.getValues();
					if (lists.size() > 0) {
						Csp003Detail csp003Detail = lists.get(0);
						vo.setResult(csp003Detail.getFs82VirtualNo());
					}
				}
				else {
					vo.setErrorMsg(csp003Data.getDetailMessage());
				}
			}
		} 
		return vo;
	}

}
