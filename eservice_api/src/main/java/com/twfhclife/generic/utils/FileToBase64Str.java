package com.twfhclife.generic.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.scheduling.task.AllianceServiceTask;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Base64;

/**
 * @author hui.chen
 * @create 2021-07-15
 */
public class FileToBase64Str {
  static   Log log = LogFactory.getLog(FileToBase64Str.class);
    /**
     * Convert File(ex:jpg,pdf) to Base64
     * @param filePath
     * @return String
     */
    public  static   String converFileToBase64Str(String filePath) throws  Exception{
        String encodedString = null;
        try {
            log.info("input filePath="+filePath);
            if(filePath!=null) {
                File file = new File(filePath);
                if (file.exists()) {
                    File contentFile = null;
                    if(!filePath.endsWith("pdf")) {//不是PDF檔就轉成PDF
                        contentFile = convertToPdfFile(filePath);
                    }else {
                        contentFile = new File(filePath);
                    }

                    byte[] fileContent = FileUtils.readFileToByteArray(contentFile);
                    encodedString = Base64.getEncoder().encodeToString(fileContent);
                }else {
                	log.info("input filePath="+filePath+" is not exists.");
                }
            }else {
                log.error("input filePath is null.");
            }
        }catch(Exception e) {
            log.error(e);
        }

        return encodedString;
    }

    public  static File convertToPdfFile(String imgFilePath) throws  Exception{
        log.info("***start convertToPdfFile() filePath="+imgFilePath);
        File filePDF = null;
        Document document = null;
        PdfWriter writer  = null;
        try {
            if(imgFilePath!=null) {
                filePDF = File.createTempFile("ImgToPDF", ".pdf");
                document = new Document();
                java.io.FileOutputStream fos = new java.io.FileOutputStream(filePDF);
                writer = PdfWriter.getInstance(document, fos);
                writer.open();
                document.open();

                Image img = Image.getInstance(imgFilePath);
                //A4 pixel 595x842
                img.scaleToFit(500, 800);

                document.add(img);
            }
        }catch(Exception e) {
            log.error("convertToPdfFile() error:", e);
        }finally {
            try {
                if(document!=null) {
                    document.close();
                }
                if(writer!=null) {
                    writer.close();
                }
            }catch(Exception e) {
                //do nothing.
            }
        }

        if(filePDF==null) {
            log.info("***end convertToPdfFile() output file is null.");
        }else {
            log.info("***end convertToPdfFile() output file is not null.fileSize="+filePDF.length());
        }

        return filePDF;
    }

    public  static File convertToPdfFile(byte[] decode)throws  Exception{
        log.info("***start convertToPdfFile()byte[] decode ="+decode);
        File filePDF = null;
        Document document = null;
        PdfWriter writer  = null;
        try {
            if(decode!=null) {
                filePDF = File.createTempFile("ImgToPDF", ".pdf");
                document = new Document();
                java.io.FileOutputStream fos = new java.io.FileOutputStream(filePDF);
                writer = PdfWriter.getInstance(document, fos);
                writer.open();
                document.open();

                Image img = Image.getInstance(decode);
                //A4 pixel 595x842
                img.scaleToFit(500, 800);

                document.add(img);
            }
        }catch(Exception e) {
            log.error("convertToPdfFile() error:", e);
        }finally {
            try {
                if(document!=null) {
                    document.close();
                }
                if(writer!=null) {
                    writer.close();
                }
            }catch(Exception e) {
                //do nothing.
            }
        }

        if(filePDF==null) {
            log.info("***end convertToPdfFile() output file is null.");
        }else {
            log.info("***end convertToPdfFile() output file is not null.fileSize="+filePDF.length());
        }

        return filePDF;
    }

}
