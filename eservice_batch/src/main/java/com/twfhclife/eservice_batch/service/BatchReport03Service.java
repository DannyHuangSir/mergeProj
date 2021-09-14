package com.twfhclife.eservice_batch.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.ReportJobDao;
import com.twfhclife.eservice_batch.model.ReportJobHistoryVo;
import com.twfhclife.eservice_batch.model.ReportJobVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.ReportExportUtil;

public class BatchReport03Service {

	private static final Logger logger = LogManager.getLogger(BatchReport03Service.class);
	
	public final static String SYSTEM_ID = "eservice_batch";
	
	public final static String REALM_ID = "twfhclife";
	
	private ReportJobDao dao;
	
	private ParameterDao paramDao;
	
	
	/**
	 * 處理主要程式流程 for Report03 - 各項作業使用統計報表
	 * @param reportJob
	 */
	public void excute(String reportJobId) {
		/**
		 * 1. 取出報表要求之參數
		 * 2. 取出報表設定
		 * 3. 取出帳號資料
		 * 4. 產生報表到指定路徑
		 * 5. 寫入歷史查詢
		 */
		
		Integer intReportJobId = null;
		String msg = "";
		ReportJobHistoryVo hist = new ReportJobHistoryVo();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
		LocalDateTime startDate = LocalDateTime.now();
		ReportExportUtil reportUtil = null;
		try {
			try {
				intReportJobId = new Integer(reportJobId);
			} catch(NumberFormatException nume) {
				logger.error("Conver input reportJobId to Integer error: {}", nume);
			}
			if(intReportJobId == null) {
				// 轉換失敗，不做任何事
				return ;
			}
			dao = new ReportJobDao();
			paramDao = new ParameterDao();
			
			// 1. 
			ReportJobVo runtimeJob = dao.getReportJob(intReportJobId);
			runtimeJob.setListCondition(dao.getReportCondition(runtimeJob));
			runtimeJob.setMapCondition(runtimeJob.getListCondition()
					.stream()
					.collect(Collectors.toMap(vo -> vo.getCondition(), vo -> vo.getConditionValue())));
			logger.info(String.format("執行中: %s, 批次代碼: %s", runtimeJob.getReportName(), runtimeJob.getReportJobId()));
			hist.setReportJobId(runtimeJob.getReportJobId());
			hist.setExecuteTime(format.format(startDate));
			hist.setExecuteStatus(ReportJobHistoryVo.SUCCESS);
			
			Map<String, String> mapCondtionCht = new HashMap<>();
			mapCondtionCht.putAll(runtimeJob.getListCondition()
				.stream()
				.collect(Collectors.toMap(vo -> vo.getCondition() + "_CHT", vo -> vo.getConditionValueCht())));
			
			// 2. 
			// 報表使用的參數 ex: condition_cht
			String conditionAllCht = paramDao.getParameterValueByCode(SYSTEM_ID, "REPORT_03_CONDITION_CHT_FORMAT");
			String targetPath = paramDao.getParameterValueByCode(SYSTEM_ID, "REPORT_FILE_PATH");
			String filename = paramDao.getParameterValueByCode(SYSTEM_ID, "REPORT_03_FILE_NAME") + "_" 
					+ DateTimeFormatter.ofPattern("yyyyMMddhhmmss").format(startDate) + ".pdf";
			String tableTitle = paramDao.getParameterValueByCode(SYSTEM_ID, "REPORT_03_HEADER_CHT");
			String tableTitleKey = paramDao.getParameterValueByCode(SYSTEM_ID, "REPORT_03_HEADER");
			Map<String, Object> mapHeader = new HashMap<>();
			mapHeader.put("TITLE", tableTitle.split(","));
			mapHeader.put("TITLE_KEY", tableTitleKey.split(","));
			float[] widthSet = {100f, 100f, 50f};
			mapHeader.put("TITLE_WIDTH", widthSet);
			
			// 3. 
			String systemId = runtimeJob.getMapCondition().get("REPORT_PARAM_SYSNAME");
			List<Map<String, Object>> listData = dao.queryReport03(systemId, runtimeJob.getDateRange());
			
			// 4. 
			reportUtil = new ReportExportUtil();
			reportUtil.txt(String.format("%s", runtimeJob.getReportName())).newLine();
			reportUtil.txt("報表條件︰");
			reportUtil.txt(
					String.format(
						conditionAllCht,
						MyStringUtil.objToStr(mapCondtionCht.get("REPORT_PARAM_SYSNAME_CHT")),
						runtimeJob.getDateRange()
						)
					).newLine();
			reportUtil.addReportData(mapHeader, listData, 10f);
			reportUtil.closeDoc();
			reportUtil.saveTo(targetPath.concat(filename));
			
			hist.setDownloadUrl(targetPath.concat(filename));
			hist.setFileName(filename);
			
		} catch(Exception e) {
			logger.error("BatchReport01Service error: {}", e);
			msg = e.getMessage();
		} finally {
			if(MyStringUtil.isNotNullOrEmpty(msg)) {
				hist.setErrorMsg(msg);
				hist.setExecuteStatus(ReportJobHistoryVo.ERROR);
			}
			if(reportUtil != null) {
				reportUtil.closeDoc();
			}
			// 5. 
			dao = new ReportJobDao();
			dao.insertReportHitory(hist);
		}
	}
}
