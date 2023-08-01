package com.twfhclife.jd.service.impl;

import com.twfhclife.jd.service.IMailService;
import com.twfhclife.jd.web.dao.ParameterDao;
import com.twfhclife.jd.web.model.MyAuthenticatorBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

@Service
public class MailServiceImpl implements IMailService {

	/**
	 * Logger utility.
	 */
	private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

	@Autowired
	private ParameterDao parameterDao;

	private Properties initProperties() {

		// 1. 初始化參數
		String smtpServer = ""; // 發送郵件的smtp地址
		String smtpServerPort = ""; // 發送郵件的端口
		String sender = ""; // 郵件的發送者
		String code = ""; // 登陸郵箱的密碼
		String ssl = ""; // 是否SSL協議

		// 2.給參數賦值
		// 測試用v
//		smtpServer = "smtp.gmail.com"; 
//		smtpServerPort = "587"; 
//		sender = "eService.twfhclife@gmail.com"; 
//		password = "eService123"; 
//		ssl = "false";
		

		// 請設定於資料庫代碼內
		smtpServer = parameterDao.getParameterValueByCode(null, "MAIL_SMTP_SERVER");
		smtpServerPort = parameterDao.getParameterValueByCode(null, "SMTP_SERVER_PORT");
		sender = parameterDao.getParameterValueByCode(null, "MAIL_SENDER");
		code = parameterDao.getParameterValueByCode(null, "MAIL_PASSWORD");
		ssl = parameterDao.getParameterValueByCode(null, "MAIL_SSL");

		logger.info("send email,init properties,smtpServer=" + smtpServer + ",smtpServerPort=" + smtpServerPort
				+ ",sender=" + sender + ",password=" + code + ",ssl=" + ssl);

		// 3.創建Properties對象
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.port", smtpServerPort);
		properties.put("mail.smtp.auth", "true");

		properties.put("mail.smtp.sender", sender); // 只是為了暫存數據
		properties.put("mail.smtp.password", code);// 只是為了暫存數據

		if ("true".equalsIgnoreCase(ssl)) {
			logger.info("send email, entry ssl");
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.setProperty("mail.smtp.port", smtpServerPort);// smtpServerPort=465
			properties.setProperty("mail.smtp.socketFactory.port", smtpServerPort); // smtpServerPort=465
		} else {
			properties.put("mail.smtp.starttls.enable", "true");
		}

		return properties;
	}

	private void attachmentFile(MimeMessage message, String mailContent, List<File> listFile) throws Exception {

		// 1.定義mainPart
		Multipart mainPart = new MimeMultipart();

		// 2.組織發送內容
		BodyPart htmlContent = new MimeBodyPart();
		htmlContent.setContent(mailContent, "text/html; charset=utf-8");
		mainPart.addBodyPart(htmlContent);

		// 3.組織附件
		MimeBodyPart filePart;
		FileDataSource filedatasource;

		for (int j = 0; j < listFile.size(); j++) { // 逐个加入附件
			filePart = new MimeBodyPart();
			filedatasource = new FileDataSource(listFile.get(j));
			filePart.setDataHandler(new DataHandler(filedatasource));
			try {
				filePart.setFileName(MimeUtility.encodeText(filedatasource.getName()));
			} catch (Exception e) {
				logger.error("sendAuthentication: {}", ExceptionUtils.getStackTrace(e));
			}
			mainPart.addBodyPart(filePart);
		}

		// 4. 将MiniMultipart对象设置为邮件内容
		message.setContent(mainPart);
	}

	/**
	 * 郵件寄送
	 * 伺服器: https://www.minwt.com/website/server/14737.html
	 * 
	 * @param content
	 *            內容
	 * @param subject
	 *            主題
	 * @param mailTo
	 *            收件者
	 * @param mailCc
	 *            副件者
	 * @param listFile
	 *            附加檔案
	 * 
	 */
	@Override
	public void sendMail(String content, String subject, String mailTo, String mailCc, List<File> listFile)
			throws Exception {
		// 1. 初始化properties
		Properties properties = initProperties();
		String sender = "" + properties.get("mail.smtp.sender"); // 郵件的發送者
		String password = "" + properties.get("mail.smtp.password"); // 登陸郵箱的密碼

//		logger.info("send email,get username password,sender=" + sender + ", password=" + password);

		// 2. 創建session
		Session session = null; // 和郵箱服務器連接。
		Authenticator authenticator = new MyAuthenticatorBean(sender, password);
		if (properties.get("mail.smtp.host").toString().indexOf("10.7") != -1) {
			logger.debug("********臺銀人壽環境********");
			properties.put("mail.smtp.auth", "false");
			//session = Session.getInstance(properties, authenticator);
			session = Session.getInstance(properties);
			session.setDebug(false); // 調試模式， 有必要時開啟。
		} else {
			logger.debug("********外部環境********");
			session = Session.getDefaultInstance(properties, authenticator);
			session.setDebug(true); // 調試模式， 有必要時開啟。
		}

		// 3.創建Message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));

		// 4.設置郵件主題，
		message.setSubject(subject, "UTF-8"); // message設置subject

		// 5.設置郵件接收人員，
		String mailTos[] = mailTo.split(";");// message設置接收人
		logger.info("send email,mailTo={}", mailTo);
		for (int i = 0; i < mailTos.length; i++) {
			
			logger.info("send email,mailTos={}", mailTos[i]);
			if(mailTos[i]!=null && !"".equals(mailTos[i].trim())) {
				Address[] addresses_TO = InternetAddress.parse(mailTos[i]);
				message.addRecipients(Message.RecipientType.TO, addresses_TO);
			}
		}

		// 6.設置郵件CC人員，
		if (mailCc != null && !"".equals(mailCc)) {// message設置抄送人
			String mailCcs[] = mailCc.split(";");
			for (int i = 0; i < mailCcs.length; i++) {
				Address[] addresses_CC = InternetAddress.parse(mailCcs[i]);
				message.addRecipients(Message.RecipientType.CC, addresses_CC);
			}
		}

		// 7.設置附件
		if (listFile == null || listFile.size() == 0) {// 沒有附件
			message.setContent(content, "text/html;charset=UTF-8");// message設置內容
		} else {// 有附件
			attachmentFile(message, content, listFile);
		}

		// 8.發送EMAIL
		Transport transport = null;
		try {
			transport = session.getTransport("smtp");
			transport.send(message);
		} catch (Exception ex) {
			logger.info("send email,connect email server error:", ex.fillInStackTrace());
			throw ex;
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
}
