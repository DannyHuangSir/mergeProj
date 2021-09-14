package com.twfhclife.generic.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twfhclife.keycloak.model.KeyCloakRole;

public class MyJacksonUtil {

	public static String object2Json(Object obj) {
		String jsonStr = "";
		try {
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			jsonStr = om.writeValueAsString(obj);
		} catch (Exception e1) {
			//e1.printStackTrace();
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
			//e1.printStackTrace();
		}
		return obj;
	}
	
	public static Map<String, Object> object2MapList(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper oMapper = new ObjectMapper();
			// object -> Map
			map = oMapper.convertValue(obj, map.getClass());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return map;
	}
	
//	private Map<String, String> toMap(Div div) {
//	    Map<String, String> result = new HashMap<>();
//	    result.put("id", div.getId().toString());
//	    result.put("name", div.getName());
//	    return result;
//	}
	
	public static void main(String[] args) {
		KeyCloakRole k = new KeyCloakRole();
		String json = "{\"id\":\"123\",\"name\":\"abc\"}";
		k = (KeyCloakRole) MyJacksonUtil.json2Object(json, KeyCloakRole.class);
	}
}
