package com.twfhclife.generic.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

@Component
public class SshUtil {

	@Value("${jsch.ssh.host}")
	private String host = "220.133.126.209";
	@Value("${jsch.ssh.port}")
	private int port = 8822;
	@Value("${jsch.ssh.user}")
	private String user = "hpeadmin";
	@Value("${jsch.ssh.pwd}")
	private String pwd = "hpe1234!@";
	@Value("${jsch.ssh.tempcron.path}")
	private String tempPath = "/home/hpeadmin/crontab.d/";
	
	
	public List<String> getCrontab() {
		List<String> crontabl = new ArrayList<>();
		try {
			JSch jsch = new JSch();
			
			Session session = jsch.getSession(user, host, port);
			session.setPassword(pwd);
			
			java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			session.setTimeout(30000);// making a connection with timeout.
			session.connect(); 

			Channel channel = session.openChannel("exec");
			String cmd = "crontab -l";
			((ChannelExec) channel).setCommand(cmd);
			channel.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				//System.out.printf("\t[Resp] %s\n", line);
				System.out.println(line);
				crontabl.add(line);
			}
			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return crontabl;
	}
	
	public List<String> writeToCrontab(List<String> cronList, String fileName) {
		List<String> crontabl = new ArrayList<>();
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, port);
			session.setPassword(pwd);
			
			java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			session.setTimeout(30000);// making a connection with timeout.
			session.connect(); 

			Channel channel = session.openChannel("exec");
			String cmd = "crontab -l > "+ tempPath + "temp.cron;";
			for (String cron : cronList) {
				cmd += "echo \"" + cron + "\" >> " + tempPath + "temp.cron;";
			}
			cmd += "crontab " + tempPath + "temp.cron;";
			//cmd += "rm " + tempPath + "temp.cron;";
			((ChannelExec) channel).setCommand(cmd);
			channel.connect();

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return crontabl;
	}
	
	/**
	 *  將排程列表寫入crontab，並將新舊crontab備份
	 * @param cronList
	 * @throws Exception
	 */
	public void writeIntoCrontab(List<String> cronList) throws Exception {
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, port);
			session.setPassword(pwd);
			
			java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			session.setTimeout(30000);// making a connection with timeout.
			session.connect(); 

			Channel channel = session.openChannel("exec");
			String tempFile = tempPath + "temp.cron";
			String now = MyStringUtil.getFormateStringFromDate(new Date(), "yyyyMMdd-hhmm");
			String backupOldFile = tempPath + "backup/crontab." + now + ".old";
			String backupNewFile = tempPath + "backup/crontab." + now + ".new";
			String cmd = "";
			cmd += "rm " + tempFile + ";";
			cmd += "crontab -l > " + backupOldFile + ";";// 備份當前的crontab
			if (cronList.size() > 0) {
			for (String cron : cronList) {
				cmd += "echo \"" + cron + "\" >> " + tempFile + ";";// write to temp file
			}
			} else {
				cmd += "echo \"\" >> " + tempFile + ";";// write to temp file
			}
			cmd += "crontab " + tempFile + ";";// commit to crontab(寫入即生效)
			cmd += "mv " + tempFile + " " + backupNewFile + ";";// 備份本次變更的crontab
			((ChannelExec) channel).setCommand(cmd);
			channel.connect();

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
//	public List<String> enableAllTempCrontab() {
//		List<String> crontabl = new ArrayList<>();
//		try {
//			JSch jsch = new JSch();
//			
//			Session session = jsch.getSession(user, host, port);
//			session.setPassword(pwd);
	
//			java.util.Properties config = new java.util.Properties();
//		    config.put("StrictHostKeyChecking", "no");
//			session.setConfig(config);
//			
//			session.setTimeout(30000);// making a connection with timeout.
//			session.connect(); 
//
//			Channel channel = session.openChannel("exec");
//			String cmd = "cat " + tempPath + "*.cron | crontab -";
//			((ChannelExec) channel).setCommand(cmd);
//			channel.connect();
//			BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				//System.out.printf("\t[Resp] %s\n", line);
//				System.out.println(line);
//				crontabl.add(line);
//			}
//			channel.disconnect();
//			session.disconnect();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return crontabl;
//	}
	
	public static void main(String[] args) {
		SshUtil ssh = new SshUtil();
		List<String> cronlist = new ArrayList<>();
		cronlist.add("00 01 * * 3 echo hellod2");
		cronlist.add("00 08 * * 5 echo hellod3");
		try {
			ssh.writeIntoCrontab(cronlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
