package com.twfhclife.eservice_batch.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ETLDao;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.ETLJobLogVo;
import com.twfhclife.eservice_batch.util.MailService;

public class BatchETLService {

	private static final Logger logger = LogManager.getLogger(BatchETLService.class);

	public void callETL() {
		ETLDao etlDao = new ETLDao();
		logger.info("============================================================================");
		
		logger.info("0. Start running call ETL process...");
		etlDao.callETLProcess();
		logger.info("0. End running call ETL process...");

		logger.info("1. Start check ETL result...");
		List<ETLJobLogVo> list = etlDao.getETLResult();
		sendMail(list);
		logger.info("1. End check ETL result...");
		
		logger.info("============================================================================");
	}
	
	/**
	 * 發出 email
	 */
	private void sendMail(List<ETLJobLogVo> list) {
		MailService mailService = new MailService();
		Map<String, String> mapDynimicContent = new HashMap<String, String>();
		
		// 取EMail
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String[] mails = rb.getString("mail.list").split(",");
		List<String> mailTo = new ArrayList<String>();
		for (String mail : mails) {
			mailTo.add(mail);
		}
		
		mapDynimicContent = this.getDynamicContent(list);
		String subject = "保戶專區轉檔結果通知-" + mapDynimicContent.get("subSubject");
		String content = mapDynimicContent.get("content");
		// 寄發EMail
		try {
			mailService.sendMail(content, subject, mailTo, null, null);
			// 儲存郵件簡訊發送紀錄
			BatchApiService apiService = new BatchApiService();
			for (String addr : mailTo) {
				apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, content));
			}
		} catch(Exception e) {
			logger.error("sendMail error: ", e);
		}
	}
	
	/**
	 * 
	 * @param list
	 */
	private Map<String, String> getDynamicContent(List<ETLJobLogVo> list) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer("<h2>今日保戶專區資料轉檔結果如下</h2>");
		String subSubject = "";
		if (list == null || list.size() == 0) {
			subSubject = "轉檔異常:未找到執行紀錄!";
		} else {
			if ("USP_SWAP_TABLE".equals(list.get(list.size() - 1).getJobName())) {
				subSubject = "轉檔完成!";
			} else {
				subSubject = "轉檔異常:未正常結束!";
			}
			sb.append("<h3>" + subSubject + "</h3>");
			map.put("subSubject", subSubject);
			
			// 表頭
			sb.append("<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" class=\"box\">");
			sb.append("<tr align=\"center\" class=\"table_th\">");
			sb.append("<th>執行序號</th>");
			sb.append("<th>程序名稱</th>");
			sb.append("<th>轉檔筆數</th>");
			sb.append("<th>開始時間</th>");
			sb.append("<th>結束時間</th>");
			sb.append("<th>執行時間(秒)</th>");
			sb.append("</tr>");
			
			for (ETLJobLogVo vo : list) {					
				sb.append("<tr align=\"center\">");
				sb.append("<td>" + vo.getJobSeq() + "</td>");
				sb.append("<td>" + vo.getJobName() + "</td>");
				sb.append("<td>" + vo.getJobCount() + "</td>");
				sb.append("<td>" + vo.getStartDate() + "</td>");
				sb.append("<td>" + vo.getEndDate() + "</td>");
				sb.append("<td>" + vo.getSpendTime() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
		}		

		map.put("content", sb.toString());
		return map;
	}
}
