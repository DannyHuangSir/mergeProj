package com.twfhclife.eservice_batch.service;

import com.twfhclife.eservice_batch.dao.TransMedicalTreatmentDao;
import com.twfhclife.eservice_batch.model.TransMedicalTreatmentClaimFileDatasVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ResourceBundle;

public class BatchUploadMedicalTreatmentFileDataService {

	private static final Logger logger = LogManager.getLogger(BatchUploadMedicalTreatmentFileDataService.class);
	
	private static final String SYSTEM_ID = "eservice";
	
	private static String MEDICAL_TREATMENT_FILEDATA_PATH;

	public BatchUploadMedicalTreatmentFileDataService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		MEDICAL_TREATMENT_FILEDATA_PATH = rb.getString("insurance.claim.filedata.path");
	}
	/**
	 * 主流程
	 * @param
	 */
	public void process() {
		logger.info("***BatchUploadMedicalTreatmentFileDataService start.***");
		
		try {
			String logMessage = "%s. TransNum: %s, %s";
			
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String enviorment = rb.getString("running.enviorment");
			
			//1.取得[TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS]未上傳影像系統的資料
			//TRANS.STATUS=0,TRANS_MEDICAL_TREATMENT_CLAIM.FROM_COMPANY_ID=L01,
			TransMedicalTreatmentDao dao = new TransMedicalTreatmentDao();
			List<TransMedicalTreatmentClaimFileDatasVo> listVo = dao.queryTransInsuranceClaimFileDataVo();
			
			if(listVo!=null && !listVo.isEmpty() && listVo.size()>0) {
				for(TransMedicalTreatmentClaimFileDatasVo vo : listVo) {
					//2.呼叫影像系統
					if (!"dev".equalsIgnoreCase(enviorment)) {
						logger.info("============================================================================");
						logger.info(String.format(logMessage, vo.getType(), vo.getTransNum(), "開始上傳醫療保單文件..."));
						
						//上傳申請單到影像系統
						BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
						
						String rtnTaskId = null;
						try {
							rtnTaskId = batchUploadEZService.uploadTransMedicalTreatmentClaimFiledatas(vo);
						}catch(Exception e) {
							logger.info(e.toString());
						}
						
						if (rtnTaskId!=null) {
							//3.get task_id from EZ_Acquire, update to  TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID
							vo.setEzAcquireTaskId(rtnTaskId);
							int updateTaskId = dao.updateEzAcquireTaskId(vo);
							logger.info("上傳聯盟醫療保單文件到影像系統完成");
							logger.info("ready to update,fdId={},ezAcquireTaskId={},return={}", vo.getFdId(),vo.getEzAcquireTaskId(),updateTaskId);
							if(updateTaskId>0) {
								logger.info("更新TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID=---成功.");
							}else {
								logger.error("更新TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID失敗,eztaskid="+rtnTaskId+",FD_ID="+vo.getFdId());
							}
							
						} else {
							logger.info("上傳聯盟醫療保單文件到影像系統失敗, transNum: " + vo.getTransNum());
						}
					}//end-if
				}//end-for
			}

		}catch(Exception e) {
			logger.error(e.toString());
			
		}
		logger.info("***BatchUploadMedicalTreatmentFileDataService end.***");
	}
	
}
