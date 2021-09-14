package com.twfhclife.generic.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hui.chen
 * @create 2021-07-11
 */
public class CallApiDateFormatUtil {

    private static String  pattern="yyyy-MM-dd HH:mm:ss";

    public CallApiDateFormatUtil(String pattern) {
        this.pattern = pattern;
    }
    public CallApiDateFormatUtil() {
    }
    /**
     * 格式化时间
     * @return
     */
    public static  String getCurrentTimeString() {
        String rtn = "";
        Date date = new Date();
        DateFormat dateformat = new SimpleDateFormat(pattern);
        rtn = dateformat.format(date);
        // System.out.println("yyyyMMddHHmmss=" + rtn );

        return rtn;
    }

    public static  String getCurrenTimeMillisString(Long  timeMillis) {
        String rtn = "";
        DateFormat dateformat = new SimpleDateFormat(pattern);
        rtn = dateformat.format(timeMillis);
        // System.out.println("yyyyMMddHHmmss=" + rtn );
        return rtn;
     }

    public static  String getStringParse(String parse ) throws ParseException {
        String rtn = "";
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse1 = dateformat.parse(parse);
        rtn = getDateToString(parse1, "yyyyMMdd");
        return rtn;
    }
    public static  String getTimeMillisToString(Long  timeMillis,String patterns ) {
        String rtn = "";
        DateFormat dateformat = new SimpleDateFormat(patterns);
        rtn = dateformat.format(timeMillis);
        // System.out.println("yyyyMMddHHmmss=" + rtn );

        return rtn;
    }
    public static  String getDateToString(Date date,String patterns ) {
        String rtn = "";
        DateFormat dateformat = new SimpleDateFormat(patterns);
        rtn = dateformat.format(date);
        // System.out.println("yyyyMMddHHmmss=" + rtn );

        return rtn;
    }

    public static void main(String[] args) throws Exception{
        long timeMillis = System.currentTimeMillis();
        String yyyyMMddHHmmss = getTimeMillisToString(timeMillis, "yyyyMMddHHmmss");
        System.out.println(yyyyMMddHHmmss);


        CallApiDateFormatUtil util = new CallApiDateFormatUtil();
    	String afterParse = null;
    	afterParse = util.getStringParse("1965-02-20");
    	System.out.println(afterParse);
    	
    	System.out.println("19650220".contains("-"));
    }
}
