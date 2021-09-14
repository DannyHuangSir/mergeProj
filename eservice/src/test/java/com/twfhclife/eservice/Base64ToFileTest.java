package com.twfhclife.eservice;


import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

/**
 * @author hui.chen
 * @create 2021-07-02
 */
public class Base64ToFileTest {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\chenhui\\Desktop\\Snipaste_2021-06-28_21-22-07.png");
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String base64= Base64.getEncoder().encodeToString(fileContent);
        String s=base64;
        String s1 = checkBase64(s);
        System.out.println(s1);
    }

    private static void Base64(String s) throws IOException {
        byte[] decode = Base64.getDecoder().decode(s);


        OutputStream out = null;
        try {
            out = new FileOutputStream("C:\\Users\\chenhui\\Desktop\\11111UCVVV.pdf");
            // Base64解码
            for (int i = 0; i < decode.length; ++i) {
                if (decode[i] < 0) {// 调整异常数据
                    decode[i] += 256;
                }
            }
            out.write(decode);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    public static String checkBase64(String base64ImgData) {
        byte[] b = Base64.getDecoder().decode(base64ImgData);
        String type = "";
        if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "png";
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "jpg";
        }else{
            type = "dpf";
        }
        return type;
    }

    public static void main011(String[] args) throws IOException {

        String s = GetImageStr("C:\\Users\\chenhui\\Desktop\\UCVVV.pdf");
        System.out.println(s);


        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream("C:\\Users\\chenhui\\Desktop\\11111UCVVV.pdf");
            // Base64解码
            byte[] b = decoder.decodeBuffer(s);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
}

    public static String GetImageStr(String imgPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = imgPath;// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        String encode = null; // 返回Base64编码过的字节数组字符串
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 读取图片字节数组
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return encode;
    }
}
