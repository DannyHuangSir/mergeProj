package com.twfhclife.eservice_batch.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;


public class MailService {

	/**
	 * Logger utility.
	 */
	private static final Logger logger = LogManager.getLogger(MailService.class);

	private Properties initProperties() {

		// 1. 初始化參數
		String smtpServer = ""; // 發送郵件的smtp地址
		String smtpServerPort = ""; // 發送郵件的端口
		String sender = ""; // 郵件的發送者
		String password = ""; // 登陸郵箱的密碼
		String ssl = ""; // 是否SSL協議

		// 2.給參數賦值
		// 測試用
//		smtpServer = "smtp.gmail.com"; 
//		smtpServerPort = "587"; 
//		sender = "eService.twfhclife@gmail.com"; 
//		password = "eService123"; 
//		ssl = "false";
		
		ParameterDao parameterDao = new ParameterDao();
		// 請設定於資料庫代碼內
		smtpServer = parameterDao.getParameterValueByCode(null, "MAIL_SMTP_SERVER");
		smtpServerPort = parameterDao.getParameterValueByCode(null, "SMTP_SERVER_PORT");
		sender = parameterDao.getParameterValueByCode(null, "MAIL_SENDER");
		password = parameterDao.getParameterValueByCode(null, "MAIL_PASSWORD");
		ssl = parameterDao.getParameterValueByCode(null, "MAIL_SSL");

		logger.info("send email,init properties,smtpServer=" + smtpServer + ",smtpServerPort=" + smtpServerPort
				+ ",sender=" + sender + ",password=" + password + ",ssl=" + ssl);

		// 3.創建Properties對象
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.port", smtpServerPort);
		properties.put("mail.smtp.auth", "true");

		properties.put("mail.smtp.sender", sender); // 只是為了暫存數據
		properties.put("mail.smtp.password", password);// 只是為了暫存數據

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
	public void sendMail(String content, String subject, List<String> mailTo, List<String> mailCc, List<File> listFile)
			throws Exception {
		// 1. 初始化properties
		Properties properties = initProperties();
		String sender = "" + properties.get("mail.smtp.sender"); // 郵件的發送者
		String password = "" + properties.get("mail.smtp.password"); // 登陸郵箱的密碼

		logger.info("send email,get username password,sender=" + sender + ", password=" + password);

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
			properties.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(properties, authenticator);
			session.setDebug(true); // 調試模式， 有必要時開啟。
		}

		// 3.創建Message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		
		// 4.設置郵件主題，
		message.setSubject(subject, "UTF-8"); // message設置subject

		InternetAddress from = new InternetAddress("eserviceBatch@twfhclife.com.tw");
		from.setPersonal("臺銀人壽保戶專區批次程序");
        message.setFrom(from);

		// 5.設置郵件接收人員，
//		String mailTos[] = mailTo.split(";");// message設置接收人
		logger.info("send email,mailTos={}", mailTo);
		for(String mail : mailTo) {
			logger.info("send email={}", mail);

			Address[] addresses_TO = InternetAddress.parse(mail);
			message.addRecipients(Message.RecipientType.TO, addresses_TO);
		}

		// 6.設置郵件CC人員，
		if (mailCc != null && mailCc.size() > 0) {// message設置抄送人
			for (String mail : mailCc) {
				Address[] addresses_CC = InternetAddress.parse(mail);
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
			logger.info("send email,connect email server error:", ex);
			throw ex;
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}

	public static void main(String[] agrs) throws AddressException, MessagingException {

		try {
			MailService mail = new MailService();
//			mail.sendMail("此為測試信件內容!!!!", "測試信件主旨!!!", "david.yu0903@gmail.com", "", null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
