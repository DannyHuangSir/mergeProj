package com.twfhclife.generic.service;

import java.io.File;
import java.util.List;

public interface IMailService {
	
	public void sendMail(String content, String subject, String mailTo, String mailCc, List<File> listFile) throws Exception;

}
