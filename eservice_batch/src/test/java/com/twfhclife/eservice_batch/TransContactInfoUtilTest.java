package com.twfhclife.eservice_batch;

import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.service.onlineChange.TransContactInfoUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hui.chen
 * @create 2021-07-27
 */
public class TransContactInfoUtilTest {
    public static void main01(String[] args) {
        String string="[(]";
        String s = "台北:之&市-信f義 區F林;口,街2段9號1樓保(全科王小華)".replaceAll(string, "（");
        System.out.println(s);
        System.out.println("ASD".length());
        System.out.println("ASD".lastIndexOf("S"));
        StringBuffer asdf = new StringBuffer("ASDF");
        System.out.println(asdf.insert(asdf.length(),")"));
        System.out.println("台北市信義區大道路台北：－＆市－信樓義，（區樓林）；口，街２段１樓保全科王小華".length());
        System.out.println("台北市信義區大道路台北:之&市-信f義 (區F林);口,街２段１樓保全科王小華".length());



    }
    public static void main(String[] args) {
        StringBuilder txtSb = new StringBuilder();
        String systemTwDate = getDate();
        List<TransVo> transVos = new TransContactInfoUtil().appendApplyItems(txtSb, systemTwDate);
        System.out.println(transVos);
    }

    private static String getDate() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String twYear = Integer.parseInt(today.substring(0, 4)) - 1911 + "";
      return   StringUtils.rightPad(twYear, 3, "0") + today.substring(4, 8);
    }
}
