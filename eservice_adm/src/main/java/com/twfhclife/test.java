package com.twfhclife;

import com.twfhclife.adm.model.JdBatchSchedulVO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther lihao
 */
public class test {
    public static void main(String[] args) throws Exception {
//        test.InjectPojo();
//        test.exportFile();
//        IJdUserBatchService jdUserBatchService = new JdUserBatchServiceImpl();
//        jdUserBatchService.upLoadFile();
//        List<String> list = new ArrayList<>();
//        list.add("40000@1");
//        list.add("40000@2");
//        list.add("40000@3");
//        for (int i = 0;i<list.size();i++){
//            String[] strings = list.get(i).toString().split("@");
//            String s1 = strings[0];
//            String s2 = strings[1];
//            System.out.println(s1);
//            System.out.println(s2);
//        }
//        String strings1 = new String("3000.0");
//        strings1.substring(0,strings1.lastIndexOf("."));
//        System.out.println(strings1.substring(0,strings1.lastIndexOf(".")));

//        Date date = new Date(System.currentTimeMillis());
//        System.out.println((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)));
//        Date date = new Date();
//        System.out.println((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)));
//        file2byte();
//        byte[] b = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
//        String s = "圖書館,飯店";
//        BufferedOutputStream bufferedOutputStream = null;
//        try {
//            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("C:/root/workfile.csv"));
//            for (int j = 0;j<b.length;j++){
//                bufferedOutputStream.write(b[j]);
//            }
//            byte[] bytes = s.getBytes();
//            for (int i = 0;i<bytes.length;i++){
//                bufferedOutputStream.write(bytes[i]);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            bufferedOutputStream.close();
//        }
//        IJdUserBatchService jdUserBatchService = new JdUserBatchServiceImpl();
////        HttpServletResponse httpServletResponse = null;
////        jdUserBatchService.exportFailFile("8F56479A-3E6D-47BA-B11E-07F5C51B0FDD",true,httpServletResponse);
//        jdUserBatchService.workFile();
        JdBatchSchedulVO batch = new JdBatchSchedulVO();
        Date date = new Date();
        batch.setBatchStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        System.out.println(batch.getBatchStartTime());
    }


}