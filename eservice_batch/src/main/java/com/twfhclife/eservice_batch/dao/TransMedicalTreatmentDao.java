package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransInsuranceClaimFileDataMapper;
import com.twfhclife.eservice_batch.mapper.TransMedicalTreatmentClaimFileDatasMapper;
import com.twfhclife.eservice_batch.model.TransMedicalTreatmentClaimFileDatasVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransMedicalTreatmentDao extends BaseDao{

	private static final Logger logger = LogManager.getLogger(TransMedicalTreatmentDao.class);
	
	public List<TransMedicalTreatmentClaimFileDatasVo> queryTransInsuranceClaimFileDataVo() {
		List<TransMedicalTreatmentClaimFileDatasVo> listVo = null;
		
		try {
			TransMedicalTreatmentClaimFileDatasMapper mapper = this.getSqlSession().getMapper(TransMedicalTreatmentClaimFileDatasMapper.class);
			listVo = mapper.queryTransMedicalTreatmentClaimFileDatasVo();
		} catch (Exception e) {
			logger.error("queryTransInsuranceClaimFileDataVo error:", e);
		} finally {
			this.release();
		}
		
		return listVo;
	}
	
	public int updateEzAcquireTaskId(TransMedicalTreatmentClaimFileDatasVo vo) {
		int rtn = -1;
		
		try {
			if(vo!=null && vo.getFdId()>0) {
				TransMedicalTreatmentClaimFileDatasMapper mapper = this.getSqlSession().getMapper(TransMedicalTreatmentClaimFileDatasMapper.class);
				rtn = mapper.updateEzAcquireTaskId(vo);
				this.commit();
			}
		} catch (Exception e) {
			logger.error("updateEzAcquireTaskId error:", e);
		} finally {
			this.release();
		}
		
		return rtn;
	}
}
