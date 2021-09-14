package com.twfhclife.generic.util;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twfhclife.keycloak.model.KeyCloakRole;

public class MyJacksonUtil {
	
	private static final Logger logger = LogManager.getLogger(MyJacksonUtil.class);

	public static String object2Json(Object obj) {
		String jsonStr = "";
		try {
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			jsonStr = om.writeValueAsString(obj);
		} catch (Exception e1) {
			logger.info("MyJacksonUtil.object2Json error! {}", e1.getMessage());
		}
		return jsonStr;
	}
	
	public static Object json2Object(String jsonString, Class toClass) {
		Object obj = null;
		try {
			ObjectMapper om = new ObjectMapper();
			//将json字符串转换成对象
			Map map = om.readValue(jsonString, Map.class);
			obj = toClass.newInstance();
			obj = om.convertValue(map, toClass);
		} catch (Exception e1) {
			logger.info("MyJacksonUtil.json2Object error! {}", e1.getMessage());
		}
		return obj;
	}
	
	/**
	 * 從JSON String中取出指定json path的值
	 * @param jsonString ex:{"code": "0","msg": "success","data": {"fileReceived": "1"}}
	 * @param pathFieldName ex:"/data/fileRecived"
	 * @return String
	 * @throws Exception
	 */
	public static String readValue(String jsonString, String pathFieldName) throws Exception{
		String rtn = null;
		if(jsonString!=null && pathFieldName!=null) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonString);
			
			if(jsonNode!=null) {
				rtn = jsonNode.at(pathFieldName).asText();
			}
		}
		return rtn;
	}

	
	public static void main(String[] args) {
		KeyCloakRole k = new KeyCloakRole();
		String json = "{\"id\":\"123\",\"name\":\"abc\"}";
		k = (KeyCloakRole) MyJacksonUtil.json2Object(json, KeyCloakRole.class);
	}
}
