package com.twfhclife.generic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * Properties文件讀入用Util.
 *
 * @author HYH
 * @version 1.0
 */
public class PropertiesUtil {

    /** Logger對象. */
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 從Property文件中取得內容并保存至Map.
     * 
     * @param propertyFileName property文件名
     * @return property文件內容
     */
    public static Map<String, String> getProperties(String propertyFileName) {
        Properties prop = new Properties();
        Map<String, String> propMap = new HashMap<String, String>();
        if (Strings.isNullOrEmpty(propertyFileName)) {
            logger.error(String.format("The specified property file is not existed.[%s]", propertyFileName));
            return propMap;
        }
        InputStream in = null;
        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertyFileName);
            prop.load(in);

            Set<Object> keyset = prop.keySet();
            for (Object object : keyset) {
                propMap.put(object.toString(), prop.getProperty(object.toString()).toString());
            }
        } catch (IOException e) {
            logger.error("getProperties", e.getMessage());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                // DO NOTHING
            }
        }
        return propMap;
    }
    
    public static String getPropertiesValue(String propertyFileName, String key) {
        Properties prop = new Properties();
        String propValue = "";
        if (Strings.isNullOrEmpty(propertyFileName)) {
            logger.error(String.format("The specified property file is not existed.[%s]", propertyFileName));
            return "";
        }
        InputStream in = null;
        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertyFileName);
            prop.load(in);

            Set<Object> keyset = prop.keySet();
            for (Object object : keyset) {
            	if(key.equals(object.toString())) {
            		propValue = prop.getProperty(object.toString()).toString();
            		break;
            	}
            }
        } catch (IOException e) {
            logger.error("getProperties", e.getMessage());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                // DO NOTHING
            }
        }
        return propValue;
    }
}
