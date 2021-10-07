package com.twfhclife.eservice_batch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.model.CommLogRequestVo;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.service.BatchApiService;
import com.twfhclife.eservice_batch.service.BatchBusinessEventJobService;
import com.twfhclife.eservice_batch.service.BatchCheckPwdExpireService;
import com.twfhclife.eservice_batch.service.BatchDemoService;
import com.twfhclife.eservice_batch.service.BatchDownloadService;
import com.twfhclife.eservice_batch.service.BatchETLService;
import com.twfhclife.eservice_batch.service.BatchEventMessageService;
import com.twfhclife.eservice_batch.service.BatchNotificationService;
import com.twfhclife.eservice_batch.service.BatchReport01Service;
import com.twfhclife.eservice_batch.service.BatchReport02Service;
import com.twfhclife.eservice_batch.service.BatchReport03Service;
import com.twfhclife.eservice_batch.service.BatchUploadInsuranceClaimFileDataService;
import com.twfhclife.eservice_batch.service.BatchUploadMedicalTreatmentFileDataService;
import com.twfhclife.eservice_batch.service.BatchUploadOnlineChangeSheetService;
import com.twfhclife.eservice_batch.service.BatchUploadService;
import com.twfhclife.eservice_batch.util.MailService;
import com.twfhclife.eservice_batch.util.ZipperUtil;

/**
 * Batch主程式
 * 須使用Java 8+
 * @author YLW
 *
 */
public class BatchMain {

	private static final Logger logger = LogManager.getLogger(BatchMain.class);

	private String batchId;
	private String batchJobName;
	private Date startDate;
	private boolean isNotice = true;
	private final String SYSTEMA_ID = "eservice_batch";
	private final String SYS_BATCH_SCHEDULE = "SYS_BATCH_SCHEDULE";

	public void batchTasks(String[] args) {
		ParameterDao  dao = new ParameterDao();
		List<ParameterVo> params = dao.getParameterByCategoryCode(SYSTEMA_ID, SYS_BATCH_SCHEDULE);
		
		String uploadDownloadDisable = "N";//init
		if(params!=null && params.size()>0) {
			ParameterVo vo = params.get(0);
			if("N".equals(vo.getParameterValue())) {
				//do nothing.
			}else {
				uploadDownloadDisable = vo.getParameterValue();
			}
		}
		logger.info("cron.upload.download.disabled="+uploadDownloadDisable);
		
		// 執行BatchMain時傳入參數決定執行哪一支batch
		if (args == null || args.length == 0) {
			logger.error("Invalid input parameter! ");
			return;
		} else {
			batchId = args[0];
			isNotice = (args.length > 1 && args[1] != null) ? false : true;
		}

		switch (batchId) {
		case "1":
			logger.info("Start running batch #1: 事件記錄通知..");
			BatchEventMessageService bes = new BatchEventMessageService();
			bes.process();
			
			break;
		case "2":
			
			if("N".equals(uploadDownloadDisable)) {
				startDate = new Date();
				batchJobName = " 批次#2:上傳申請檔";
				logger.info("Start running batch #2:上傳申請檔..");
				BatchUploadService upload = new BatchUploadService();
				upload.uploadOnlineChangeFile();
				// 上傳結束後，補上 pdf 
				logger.info("upload completed! create pdf start");
				BatchUploadOnlineChangeSheetService ocSheet = new BatchUploadOnlineChangeSheetService();
				ocSheet.process(upload.getProcessList());
				logger.info("create pdf end");
				this.sendNoticeMail(batchJobName, startDate, new Date());
			}
			
			break;
		case "3":
			
			if("N".equals(uploadDownloadDisable)) {
				startDate = new Date();
				batchJobName = " 批次#3:下載申請檔";
				logger.info("Start running batch #3: 下載申請檔..");
				BatchDownloadService download = new BatchDownloadService();
				download.downloadOnlineChangeFile();
				download.downloadTransEndormentFile();
				this.sendNoticeMail(batchJobName, startDate, new Date());
			}
			
			break;
		case "4":
			logger.info("Start running batch #4: ETL..");
			BatchETLService etl = new BatchETLService();
			etl.callETL();
			break;
		case "5":
			logger.info("Start running batch#5: 停利停損通知..");
			BatchNotificationService roiNotice = new BatchNotificationService();
			roiNotice.excute();
			break;
		case "6":
			logger.info("Start running batch#6: 五年未登入鎖定帳號");
			//1.五年未登入鎖定帳號
			BatchCheckPwdExpireService service = new BatchCheckPwdExpireService();
			service.checkLastLoginOverYears();
			
			logger.info("Start running batch#6: CR17 保戶密碼到期前1個月發送電子郵件至要保人信箱");
			//1.批次於到期前1個月通知保戶
			service.checkNoticeUser();
			//2.到期後線上強制變更,且不可與前N次相同
			service.clearUsersLastChgPwdDate();

			break;
		case "R01":
			logger.info("Start running batch#R01: 報表01..");
			BatchReport01Service report01Service = new BatchReport01Service();
			report01Service.excute(args[1]);
			break;
		case "R02":
			logger.info("Start running batch#R02: 報表02..");
			BatchReport02Service report02Service = new BatchReport02Service();
			report02Service.excute(args[1]);
			break;
		case "R03":
			logger.info("Start running batch#R03: 報表03..");
			BatchReport03Service report03Service = new BatchReport03Service();
			report03Service.excute(args[1]);
			break;
		case "EVJOB":
			logger.info("Start running batch#EVJOB: 事件通知..");
			BatchBusinessEventJobService batchBusinessEventJobService = new BatchBusinessEventJobService();
			batchBusinessEventJobService.execute(args[1]);
			break;
		case "demo":
			logger.info("Start running batch #1: 填入batch1程式名稱..");
			BatchDemoService demo = new BatchDemoService();
			demo.DemoBatch();
			
			break;
		case "insuranceClaim":
			logger.info("Start running batch #insuranceClaim: Upload InsuranceClaimFileData to EZ_ACQUIRE..");
			BatchUploadInsuranceClaimFileDataService icService = new BatchUploadInsuranceClaimFileDataService();
			icService.process();
			break;
		case "medicalTreatment":
			logger.info("Start running batch #medicalTreatment: Upload MedicalTreatmentClaimFileData to EZ_ACQUIRE..");
			BatchUploadMedicalTreatmentFileDataService medicalTreatmentService = new BatchUploadMedicalTreatmentFileDataService();
			medicalTreatmentService.process();
			break;
		default:
			logger.info("Invalid BatchId: " + batchId + ", batch job stopped.");
			throw new IllegalArgumentException("Invalid BatchId: " + batchId);
		}

	}
	
	private void sendNoticeMail(String batchJobName, Date startDate, Date endDate) {
		logger.info("@@@@ isNotice = {} @@@", isNotice);
		if (isNotice) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				StringBuilder sb = new StringBuilder();
				sb.append(batchJobName).append("<br />");
				sb.append("開始時間:").append(sdf.format(startDate)).append("<br />");
				sb.append("結束時間:").append(sdf.format(endDate)).append("<br />");
				
				ResourceBundle rb = ResourceBundle.getBundle("config");
				String enviorment = rb.getString("running.enviorment");
				String[] mailStr = rb.getString("mail.list").split(",");
				List<String> mails = new ArrayList<String>();
				for (String addr : mailStr) {
					mails.add(addr);
				}
			
				MailService mailService = new MailService();
				String pwd = "Aa123456";
				String filePath = "dev".equals(enviorment) ? "C:/eservice/batch_log/" : "/eservice/batch_log/";
				String zipFileSource = "batch_log.log"; 
				String zipNameDest = "batchLog";
				ZipperUtil zip = new ZipperUtil(pwd);
				zip.pack(filePath, zipFileSource, zipNameDest);
				List<File> listFile = new ArrayList<File>();
				File zipFile = new File(filePath + zipNameDest + "." + ZipperUtil.EXTENSION);
				listFile.add(zipFile);
				mailService.sendMail(sb.toString(), sdf.format(startDate).split(" ")[0] + batchJobName + "[env=" + enviorment + "]", mails, null, listFile);
				zipFile.delete();
				// 儲存郵件簡訊發送紀錄
				BatchApiService apiService = new BatchApiService();
				for (String addr : mails) {
					apiService.postCommLogAdd(new CommLogRequestVo("eservice_batch", "email", addr, sb.toString()));
				}
			} catch (Exception e) {
				logger.error("sendNoticeMail error:", e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		BatchMain batch = new BatchMain();
		batch.batchTasks(args);
	}
}