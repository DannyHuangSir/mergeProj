package com.twfhclife.eservice_batch.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.BatchMain;

public class FTPUtil {
	
	private static final Logger logger = LogManager.getLogger(FTPUtil.class);
	private String username;
	private String password;
	private String host;
	private String port;
	private FTPClient ftpClient;
	
	public FTPUtil(String username, String password, String host, String port){
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		logger.info("connect info => username:{}, password:{}, host:{}, port:{}", username, password, host, port);
	}
	
	public boolean connect() {
		boolean isConnect = false;
		try {
			ftpClient = new FTPClient();
			logger.info("connect ftp... ");
			ftpClient.connect(host, Integer.parseInt(port));
			boolean success = ftpClient.login(username, password);
			if (!success) {
				logger.info("Could not login to the ftp server");
				isConnect = false;
			}
			logger.info("connect ftp... success");
			
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				logger.info("ftp connect failed");
				isConnect = false;
			}
			isConnect = true;
		} catch (Exception e) {
			logger.error("ftp connect failed:", e);
			isConnect = false;
		}
		return isConnect;
	}
	
	public boolean storeFile(String sorueFile, String destFilePath) {
		boolean isSuccess = false;
		try {
			logger.info("storeFile sorueFile:{}, destFile:{}", sorueFile, destFilePath);
			if (ftpClient.isConnected()) {
				ftpClient.enterLocalPassiveMode(); // important!
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			    FileInputStream in = new FileInputStream(new File(sorueFile));
			    boolean result = ftpClient.storeFile(destFilePath, in);
			    if (!result) {
			    	logger.info("ftp storeFile failed");
			        return false;
			    }
			    in.close();
			    isSuccess = true;
			} else {
				logger.info("ftp connect failed");
				isSuccess = false;
			}
		} catch (FileNotFoundException e) {
			logger.error("file not error:", e);
		} catch (Exception e) {
			logger.error("storeFile error:", e);
		}
		return isSuccess;
	}
	
	public boolean downloadFile(String remoteDir, String remoteFileName, File downloadFile) {
		boolean isSuccess = false;
		try {
			logger.info("downloadFile remoteDir:{}, remoteFileName:{}, downloadFile:{}", remoteDir, remoteFileName, downloadFile.getName());
	    	if (ftpClient.isConnected()) {
	    		ftpClient.changeWorkingDirectory(remoteDir);
	    		try (OutputStream os = new FileOutputStream(downloadFile)) {
	    			isSuccess = ftpClient.retrieveFile(remoteFileName, os);
		            if (!isSuccess) {
		            	logger.info("ftp downloadFile failed");
		                isSuccess = false;
		            }
		        }
	        } else {
				logger.info("ftp connect failed");
				isSuccess = false;
			}
	    } catch (IOException e) {
	        logger.error("downloadFile error:", e);
	        return false;
	    }
		return isSuccess;
	}
	
	public List<String> listFile(String remotePath) {
		List<String> list = null;
		try {
			logger.info("listFile remotePath:{}", remotePath);
			if (ftpClient.isConnected()) {
				ftpClient.enterLocalPassiveMode(); // important!
			    FTPFile[] files = ftpClient.listFiles(remotePath);
			    if (files != null && files.length > 0) {
			    	list = new ArrayList<String>();
			    	for (FTPFile f : files) {
			    		if (f.isFile()) {
			    			list.add(f.getName());
			    		}
			    	}
			    }
			} else {
				logger.info("ftp connect failed");
			}
		} catch (Exception e) {
			logger.error("listFile error:", e);
		}
		return list;
	}
	
	public boolean deleteFile(String remoteDir, String remoteFileName) {
		boolean isSuccess = false;
		try {
			logger.info("deleteFile remoteFileName:{}", remoteFileName);
			if (ftpClient.isConnected()) {
				ftpClient.changeWorkingDirectory(remoteDir);
				ftpClient.enterLocalPassiveMode(); // important!
			    boolean result = ftpClient.deleteFile(remoteFileName);
			    if (!result) {
			    	logger.info("ftp deleteFile failed");
			        return false;
			    }
			    isSuccess = true;
			} else {
				logger.info("ftp connect failed");
				isSuccess = false;
			}
		} catch (Exception e) {
			logger.error("deleteFile error:", e);
		}
		return isSuccess;
	}
	
	public void disConnect() {
		logger.info("ftp disConnect...");
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
				logger.info("ftp disConnect... success");
			} catch (IOException e) {
				logger.error("disConnect error:", e);
			}
		}
	}

	public static void main(String[] args) {
		FTPUtil ftpUtil = new FTPUtil("hpadmin", "hpe1234!@", "49.159.124.8", "21");
		ftpUtil.connect();
//		List<String> fileList = ftpUtil.listFile("/home/hpadmin");
//		for (String str : fileList) {
//			System.out.println(str);
//		}
//		ftpUtil.downloadFile("/home/ctc/eservice/ToELIFE", "ENDORSEMENT_RSP.2019012403275431", new File("C:/tmp/eservice/download/ENDORSEMENT_RSP.2019012403275431"));
//		ftpUtil.deleteFile("/home/ctc/eservice/ToELIFE", "ENDORSEMENT_RSP.2019012403275431");
		
		ftpUtil.disConnect();
		
		System.out.println(" 201810250004".trim());
	}
}
