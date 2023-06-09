package com.twfhclife.eservice_batch.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.RegionDao;
import com.twfhclife.eservice_batch.dao.RoadDao;
import com.twfhclife.eservice_batch.model.ChunghwaApiRequest;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.RegionVo;
import com.twfhclife.eservice_batch.model.RoadVo;

public class BatchUpdateRoadService {

	private static final Logger logger = LogManager.getLogger(BatchUpdateRoadService.class);

	private String SYS_NAME = "eservice_batch";

	private String ESERVICE_API_CHUNGHWA = "ESERVICE_API_CHUNGHWA";
	private static final String SYSTEM_ID = "eservice"; // 路名RULES代碼
	private static final String CATEGORY_CODE = "ADDRESS_TRANSLATION_RULES"; // 路名RULES代碼

	public void updateRoad() {
		logger.info("=======================Check Road Update Start=========================");
		RegionDao regionDao = new RegionDao();
		ParameterDao paramDao = new ParameterDao();
		RoadDao roadDao = new RoadDao();
		List<ParameterVo> paramList = paramDao.getParameterByCategoryCode(SYSTEM_ID, CATEGORY_CODE);
		//塞選: 只需媒合段號
		paramList = paramList.stream().filter(x ->{
			String codeValues = x.getParameterValue();
			if(codeValues.contains("段")) {
				return true;
			}else {
				return false;
			}
		}).collect(Collectors.toList());
		logger.info("=======================Get CityRegion Start=========================");
		List<RegionVo> regionlist = regionDao.getCityRegion();
		logger.info("=======================Get CityRegion End=========================");
		if (regionlist.size() == 0) {
			logger.info("=======================Get CityRegion Error=========================" + " 查無資料");
			return;
		}
		// 透過縣市鄉鎮取得 路名
		logger.info("=======================Get getRoadByRegionId Start=========================");
		for (RegionVo vo : regionlist) {
			// 取得中華郵政查詢路名
			logger.info("=======================Get getStreetNameData Start : " + vo.getCity() + "-" + vo.getRegion()
					+ "=====================");

			String url = paramDao.getParameterValueByCode(SYS_NAME, ESERVICE_API_CHUNGHWA);
			String accessKey = paramDao.getParameterValueByCode("eservice_batch", "BATCH_ACCESSKEY");
			try {
				ChunghwaApiRequest request = new ChunghwaApiRequest();
				request.setCity(vo.getCity().replace("台", "臺"));
				request.setCityarea(vo.getRegion());
				SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
						SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
						NoopHostnameVerifier.INSTANCE);
				CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(scsf).build();

				HttpPost httpPost = new HttpPost(url);
				String json = new Gson().toJson(request);
				StringEntity entity = new StringEntity(json, "UTF-8");
				httpPost.setEntity(entity);
				httpPost.setHeader("Authorization", "Bearer " + accessKey);
				httpPost.setHeader("Content-type", "application/json");
				List<String> stList= new ArrayList<String>();
				CloseableHttpResponse response = client.execute(httpPost);
				if (response != null) {
					String body = EntityUtils.toString(response.getEntity(), "UTF-8");
					if(StringUtils.isNotEmpty(body)) {
						body = body.replace("[", "");
						body = body.replace("]", "");
						body = body.replace("\"", "");
						stList = Arrays.asList(body.split(","));
					}
				}
				response.close();
				client.close();
				// 比對路名 不存在就新增
				if (stList.size() > 0) {
					// 查詢取得路名
					List<RoadVo> roadVoList = roadDao.getRoadByRegionId(vo.getRegionId());
					if (roadVoList.size() == 0) {
						logger.info("=======================Get getRoadByRegionId Error =========================");
						logger.info("Error :" + vo.getCity() + "-" + vo.getRegion() + "查無資料");
						continue;
					}
					// 封裝現有的路名
					List<String> roadNameList = new ArrayList<>();
					roadNameList = roadVoList.stream().map(x -> {
						return x.getRoad();
					}).collect(Collectors.toList());

					for (String st : stList) {
						//段號轉換
						for (ParameterVo param : paramList) {
							st = st.replaceAll(param.getParameterCode(),param.getParameterValue());
						}
						//比對是否有新的路名
						if (!roadNameList.contains(st)) {
							roadDao.insertRoad(st, vo.getRegionId());
						}
					}
				}
			} catch (Exception e) {
				logger.info("=========Get CHUNGHWA_API_URL ERROR=========" + e);
			}
		}
		logger.info("=======================Get getRoadByRegionId End=========================");
		logger.info("=======================Check Road Update End==============================");
	}
}
