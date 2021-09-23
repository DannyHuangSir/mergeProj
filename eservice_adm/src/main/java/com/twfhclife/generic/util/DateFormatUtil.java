package com.twfhclife.generic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hui.chen
 * @create 2021-09-07
 */
public class DateFormatUtil {


    /**
     * 字符串轉換為時間類型的字符串
     * @param dateFormat  時間類型的字符串  yyyyMMdd
     * @param returnDateFormat  返回的時間類型的字符串  yyyy-MM-dd
     * @param args  需要格式化的字符串 20201205
     * @return  返回格式化之後的字符串 2020-12-05
     */
    public  static String  getStringToDateString(String dateFormat,String  args,String returnDateFormat){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            Date parse =simpleDateFormat.parse(args);
            SimpleDateFormat format = new SimpleDateFormat(returnDateFormat);
            String formatStr = format.format(parse);
            return   formatStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return args;
    }
}
