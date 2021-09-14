package com.twfhclife.eservice_batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MailTemplateUtil {
	
	private static final Logger logger = LogManager.getLogger(MailTemplateUtil.class);
	
	public static String getMailTempleteContent(String templateName) throws IOException {
		//sit & prod
		String templatePathProd = System.getProperty("user.dir") + File.separator + "resources/mailTemplate/" + templateName;
		//dev
		String templatePathDev = ReportExportUtil.class.getClassLoader().getResource("mailTemplate/" + templateName).getPath();
		File fileProd = new File(templatePathProd);
		File fileDev = new File(templatePathDev);
		File templateFile = null;
		if (fileProd.exists()) {
			templateFile = fileProd;
		}else if (fileDev.exists()) {
			templateFile = fileDev;
		} else {
			logger.error("Unable to find mailTemplate file: {}", templateName);
			throw new IOException(String.format("Unable to find mailTemplate file: %s", templateName));
		}
		return read(templateFile);
	}

	/**
	 * 讀取 Input File 回傳字串
	 * @param file
	 * @return
	 */
	public static String read(File file) throws IOException{
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = null;
		String readLine = null;
		try {
			fis = new FileInputStream(file);
			if(fis.available() > 0) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
				while((readLine = reader.readLine()) != null) {
					sb.append(readLine).append('\n');
				}
				if(reader != null) {
					reader.close();
				}
			}
		} catch(IOException e) {
			logger.error("read error:", e);
			throw e;
		} finally {
			if(fis != null) {
				fis.close();
			}
		}
		return sb.toString();
	}
	
}
