package com.twfhclife.eservice_batch.service;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransInsuranceClaimDao;
import com.twfhclife.eservice_batch.model.TransInsuranceClaimFileDataVo;

public class BatchUploadInsuranceClaimFileDataService {

	private static final Logger logger = LogManager.getLogger(BatchUploadInsuranceClaimFileDataService.class);
	
	private static final String SYSTEM_ID = "eservice";
	
	private static String INSURANCE_CLAIM_FILEDATA_PATH;

	public BatchUploadInsuranceClaimFileDataService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		INSURANCE_CLAIM_FILEDATA_PATH = rb.getString("insurance.claim.filedata.path");
	}
	/**
	 * 主流程
	 * @param inputList
	 */
	public void process() {
		logger.info("***BatchUploadInsuranceClaimFileDataService start.***");
		
		try {
			String logMessage = "%s. TransNum: %s, %s";
			
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String enviorment = rb.getString("running.enviorment");
			
			//1.取得TRANS_INSURANCE_CLAIM_FILEDATAS未上傳影像系統的資料
			//TRANS.STATUS=0,TRANS_INSURANCE_CLAIM.FROM_COMPANY_ID=L01,
			TransInsuranceClaimDao dao = new TransInsuranceClaimDao();
			List<TransInsuranceClaimFileDataVo> listVo = dao.queryTransInsuranceClaimFileDataVo();
			
			if(listVo!=null && !listVo.isEmpty() && listVo.size()>0) {
				for(TransInsuranceClaimFileDataVo vo : listVo) {
					//2.呼叫影像系統
					if (!"dev".equalsIgnoreCase(enviorment)) {
						logger.info("============================================================================");
						logger.info(String.format(logMessage, vo.getType(), vo.getTransNum(), "開始上傳聯盟保單理賠文件..."));
						
						//上傳申請單到影像系統
						BatchUploadEZService batchUploadEZService = new BatchUploadEZService();
						
						String rtnTaskId = null;
						rtnTaskId = batchUploadEZService.uploadInsuranceClaimFileDatas(vo);
						if (rtnTaskId!=null) {
							logger.info("上傳聯盟保單理賠文件到影像系統完成");
							
							//3.get task_id from EZ_Acquire, update to TRANS_INSURANCE_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID
							vo.setEzAcquireTaskId(rtnTaskId);
							int updateTaskId = dao.updateEzAcquireTaskId(vo);
							logger.info("ready to update,fdId={},ezAcquireTaskId={},return={}", vo.getFdId(),vo.getEzAcquireTaskId(),updateTaskId);
							if(updateTaskId>0) {
								logger.info("更新TRANS_INSURANCE_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID成功.");
							}else {
								logger.error("更新TRANS_INSURANCE_CLAIM_FILEDATAS.EZ_ACQUIRE_TASK_ID失敗,eztaskid="+rtnTaskId+",FD_ID="+vo.getFdId());
							}
							
						} else {
							logger.info("上傳聯盟保單理賠文件到影像系統失敗, transNum: " + vo.getTransNum());
						}
				}//end-if
				}//end-for
			}

		}catch(Exception e) {
			logger.error(e.toString());
			
		}
		logger.info("***BatchUploadInsuranceClaimFileDataService end.***");
	}
	
}
