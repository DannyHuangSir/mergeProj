package com.twfhclife.generic.utils;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.twfhclife.alliance.model.InsuranceClaimFileDataVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.eservice.web.model.Division;
import com.twfhclife.eservice.web.model.HospitalVo;

public class MyJacksonUtil {

	public static String object2Json(Object obj) {
		String jsonStr = "";
		try {
			ObjectMapper om = new ObjectMapper();
//			om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			om.setSerializationInclusion(Include.NON_NULL);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			jsonStr = om.writeValueAsString(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		return jsonStr;
	}
	
	public static Object json2Object(String jsonString, Class toClass) {
		Object obj = null;
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			//将json字符串转换成对象
			Map map = om.readValue(jsonString, Map.class);
			obj = toClass.newInstance();
			obj = om.convertValue(map, toClass);
		} catch (Exception e1) {
			e1.printStackTrace();
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
	
	public static String getNodeString(String jsonString, String nodeName) throws Exception{
		String rtn = null;
		if(jsonString!=null && nodeName!=null) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonString);
			JsonNode pointNode = jsonNode.get(nodeName);
			rtn = pointNode.toString();
		}
		return rtn;
	}
	
	private void testJsonStringToObject() throws Exception{
		String jsonString = "{\"from\":\"L01\",\"to\":[{\"companyId\":\"L02\"},{\"companyId\":\"L03\"},{\"companyId\":\"L03\"},{\"companyId\":\"L05\"}]}";
		jsonString = "{\"from\":\"L01\",\"to\":[{\"companyId\":\"L02\"}]}";
		
		ObjectMapper mapper = new ObjectMapper();  
		java.util.List<JsonNode> listNode = mapper.readTree(jsonString).findPath("to").findValues("companyId");
		
		if(listNode!=null &&listNode.size()>0) {
			for(JsonNode jn : listNode) {
				System.out.println(jn.asText());
			}
			
		}else {
			System.out.println(listNode);
		}
		
		
	}
	
	public static void main(String[] args) throws Exception{
		String jsonStr = "{\"code\": \"0\",\"msg\": \"success\",\"data\": {\"fileReceived\": \"1\"}}";
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode jsonNode = objectMapper.readTree(jsonStr);
//		String value = jsonNode.at("/data/fileRecived").asText();
		
//		MyJacksonUtil util = new MyJacksonUtil();
//		String value = util.getNodeString(jsonStr, "data");
//		System.out.println("value="+value);
		
//		MyJacksonUtil util = new MyJacksonUtil();
//		util.testJsonStringToObject();
		
//		String jsonFileData = "{\"data\":[{\"fileDatas\": [{\"fileId\": \"01\",\"fileName\": \"filename01\"}, {\"fileId\": \"02\",\"fileName\": \"filename02\"}]}]}\r\n" + 
//				"";
//		String str = MyJacksonUtil.getNodeString(jsonFileData, "data");
//		System.out.println(str);
		
//		String jsonString = "{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"divisions\":[{\"mainDepid\":\"100\",\"mainDepname\":\"內科\",\"subDivisions\":[{\"depid\":\"10001\",\"depname\":\"心臟內科\"},{\"depid\":\"10002\",\"depname\":\"腸胃內科\"}]},{\"mainDepid\":\"200\",\"mainDepname\":\"外科\",\"subDivisions\":[{\"depid\":\"20001\",\"depname\":\"一般外科\"},{\"depid\":\"20002\",\"depname\":\"整型外科\"}]}]}]}";
//		jsonString = MyJacksonUtil.getNodeString(jsonString, "data");
//		System.out.println(jsonString);
//		
//		jsonString = jsonString.substring(1, jsonString.length() - 1);
//		System.out.println(jsonString);
//		
//		com.twfhclife.eservice.web.model.HospitalVo hospitalVo = (com.twfhclife.eservice.web.model.HospitalVo)MyJacksonUtil.json2Object(jsonString, com.twfhclife.eservice.web.model.HospitalVo.class);
//		System.out.println(hospitalVo);

		

	}


}
