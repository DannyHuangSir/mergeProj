package com.twfhclife.generic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class MailTemplateUtil {
	
	public static String MAIL_TEMPLATE_CNT;
	
	private static final Logger logger = LogManager.getLogger(MailTemplateUtil.class);
	
	public static String getMailTempleteContent(String templateName) throws IOException {
		File templateFile = new ClassPathResource("templates/mailTemplate/" + templateName).getFile();
		return read(templateFile);
	}

	/**
	 * 讀取 Input File 回傳字串
	 * @param file
	 * @return
	 */
	public static String read(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = null;
		String readLine = null;
		try {
			fis = new FileInputStream(file);
			if (fis.available() > 0) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
					while ((readLine = reader.readLine()) != null) {
						sb.append(readLine).append('\n');
					}
				} catch (Exception e) {
					logger.error("read error:", e);
					throw e;
				} finally {
					if (reader != null) {
						reader.close();
					}
				}
			}
		} catch (IOException e) {
			logger.error("read error:", e);
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}

		}
		return sb.toString();
	}

	public static String getMailTemplateCnt() {
		if (MailTemplateUtil.MAIL_TEMPLATE_CNT == null) {
			try {
				MailTemplateUtil.MAIL_TEMPLATE_CNT = MailTemplateUtil.getMailTempleteContent("login-notice.html");
			} catch (IOException e) {
				logger.error("getMailTempleteContent error: ", e);
			}
		}
		return MailTemplateUtil.MAIL_TEMPLATE_CNT;
	}
}
