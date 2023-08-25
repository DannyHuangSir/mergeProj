package com.twfhclife.generic.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AesUtil {

    private static final String AES = "eservice_lipei2";
    /**
     * 初始向量IV, 初始向量IV的长度规定为128位16个字节, 初始向量的来源为随机生成.
     */
    private static GCMParameterSpec gcMParameterSpec;

    /**
     * 加密解密算法/加密模式/填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    static {
        SecureRandom random = new SecureRandom();
        byte[] bytesIV = new byte[16];
        random.nextBytes(bytesIV);
        gcMParameterSpec = new GCMParameterSpec(128, bytesIV);
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }

    /**
     * AES加密
     */
    public static String decrypt(String key, String content) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcMParameterSpec);
            // 获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码1
            byte[] byteEncode = content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            // 根据密码器的初始化方式加密
            byte[] byteAes = cipher.doFinal(byteEncode);
            // 将加密后的数据转换为字符串
            return DatatypeConverter.printHexBinary(byteAes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     */
    public static String encrypt(String key, String content) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, gcMParameterSpec);
            // 将加密并编码后的内容解码成字节数组
            byte[] byteContent = DatatypeConverter.parseHexBinary(content);
            // 解密
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}