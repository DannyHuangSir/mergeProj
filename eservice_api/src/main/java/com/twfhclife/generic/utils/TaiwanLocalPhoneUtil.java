package com.twfhclife.generic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twfhclife.generic.model.TaiwanLocalPhoneVo;

public class TaiwanLocalPhoneUtil {

	private static final Logger logger = LoggerFactory.getLogger(TaiwanLocalPhoneUtil.class);
	
	public static final String PATTERN_0826 = "08266[0-9]{4}";
	
	public static final String PATTERN_0836 = "0836[0-9]{5}";
	
	public static final String PATTERN_082 = "0823[0-9]{5}";
	
	public static final String PATTERN_089 = "089[0-9]{6}";
	
	public static final String PATTERN_037 = "037[0-9]{6}";
	
	public static final String PATTERN_049 = "049[0-9]{7}";
	
	public static final String PATTERN_02 = "02\\d{6,8}";
		
	public static final String PATTERN_03 = "03\\d{6,8}";
	
	public static final String PATTERN_04 = "04\\d{6,8}";
	
	public static final String PATTERN_05 = "05\\d{6,8}";
	
	public static final String PATTERN_06 = "06\\d{6,8}";
	
	public static final String PATTERN_07 = "07\\d{6,8}";
	
	public static final String PATTERN_08 = "08\\d{6,8}";
	
	
	
	public static TaiwanLocalPhoneVo parseLocalPhone(String strPhone){
		TaiwanLocalPhoneVo tLocalPhone = null;
		
		try{
			if(strPhone==null) {
				return null;
			}
			
			String tempStr = strPhone;

			if(strPhone.length()>=9) {//臺灣市內電話最短為九碼
				Pattern pattern = null;
				if(strPhone.startsWith("0826")) {
					pattern = Pattern.compile(PATTERN_0826);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("0826");
						tempStr = strPhone.replaceFirst("0826", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("0836")) {
					pattern = Pattern.compile(PATTERN_0836);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("0836");
						tempStr = strPhone.replaceFirst("0836", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("082")) {
					pattern = Pattern.compile(PATTERN_082);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("082");
						tempStr = strPhone.replaceFirst("082", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("089")) {
					pattern = Pattern.compile(PATTERN_089);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("089");
						tempStr = strPhone.replaceFirst("089", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("037")) {
					pattern = Pattern.compile(PATTERN_037);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("037");
						tempStr = strPhone.replaceFirst("037", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("049")) {
					pattern = Pattern.compile(PATTERN_049);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("049");
						tempStr = strPhone.replaceFirst("049", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("02")) {
					pattern = Pattern.compile(PATTERN_02);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("02");
						tempStr = strPhone.replaceFirst("02", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("03")) {
					pattern = Pattern.compile(PATTERN_03);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("03");
						tempStr = strPhone.replaceFirst("03", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("04")) {
					pattern = Pattern.compile(PATTERN_04);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("04");
						tempStr = strPhone.replaceFirst("04", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("05")) {
					pattern = Pattern.compile(PATTERN_05);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("05");
						tempStr = strPhone.replaceFirst("05", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("06")) {
					pattern = Pattern.compile(PATTERN_06);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("06");
						tempStr = strPhone.replaceFirst("06", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}
				else if(strPhone.startsWith("07")) {
					pattern = Pattern.compile(PATTERN_07);
					Matcher matcher = pattern.matcher(strPhone);
					if(matcher.matches()) {
						tLocalPhone = new TaiwanLocalPhoneVo();
						tLocalPhone.setLocalCode("07");
						tempStr = strPhone.replaceFirst("07", "");
						tLocalPhone.setPhoneNo(tempStr);
					}
				}else {
					//do nothing.
				}
				
				if(tLocalPhone==null) {
					if(strPhone.startsWith("08")) {
						pattern = Pattern.compile(PATTERN_08);
						Matcher matcher = pattern.matcher(strPhone);
						if(matcher.matches()) {
							tLocalPhone = new TaiwanLocalPhoneVo();
							tLocalPhone.setLocalCode("08");
							tempStr = strPhone.replaceFirst("08", "");
							tLocalPhone.setPhoneNo(tempStr);
						}
					}
				}

			}//end-if
			
			if(tLocalPhone==null) {//other phone format.
					tLocalPhone = new TaiwanLocalPhoneVo();
					tLocalPhone.setPhoneNo(tempStr);
			}
		}catch(Exception e) {
			logger.info(e.toString());
		}

		return tLocalPhone;
	}
	
	public static void main(String[] args) {
		TaiwanLocalPhoneUtil util = new TaiwanLocalPhoneUtil();
		
		TaiwanLocalPhoneVo vo = util.parseLocalPhone("082669876");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("083669876");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		//082 + 3
		vo = util.parseLocalPhone("082369876");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		//082 + not 3
		vo = util.parseLocalPhone("082412345");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("089312345");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("037123456");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0491234567");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0212345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0312345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0412345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0512345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0612345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("0712345678");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("087123456");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
		
		vo = util.parseLocalPhone("9999999999");
		System.out.println(vo.getLocalCode()+"-"+vo.getPhoneNo());
	}
	
}
