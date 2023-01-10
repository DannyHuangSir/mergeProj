package com.twfhclife.eservice.service;

import java.io.File;
import java.util.List;

public interface IMailService {
	
	public void sendMail(String content, String subject, String mailTo, String mailCc, List<File> listFile) throws Exception;

}
