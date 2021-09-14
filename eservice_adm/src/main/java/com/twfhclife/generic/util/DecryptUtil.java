package com.twfhclife.generic.util;

import com.twfhclife.common.util.EncryptionUtil;

public class DecryptUtil {

	public static String decrypt(String s) {
		String destr = null;
		try {
			destr = EncryptionUtil.Decrypt(s);
			if(destr == null) {
				destr = s;
			}
		} catch (Exception e) {
			destr = s;
		}
		return destr;
	}

	public static String encrypt(String s) {
		String enstr = null;
		try {
			enstr = EncryptionUtil.Encrypt(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enstr;
	}
	
	public static void main(String[] args) {
		DecryptUtil d = new DecryptUtil();
		String str1 = d.encrypt("asd");
		System.out.println(str1);
		String str2 = d.decrypt(str1);
		System.out.println(str2);
	}
	
}
