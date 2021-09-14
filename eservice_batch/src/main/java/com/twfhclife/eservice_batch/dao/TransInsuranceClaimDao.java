package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransInsuranceClaimFileDataMapper;
import com.twfhclife.eservice_batch.model.TransInsuranceClaimFileDataVo;

public class TransInsuranceClaimDao extends BaseDao{

	private static final Logger logger = LogManager.getLogger(TransInsuranceClaimDao.class);
	
	public List<TransInsuranceClaimFileDataVo> queryTransInsuranceClaimFileDataVo() {
		List<TransInsuranceClaimFileDataVo> listVo = null;
		
		try {
			TransInsuranceClaimFileDataMapper mapper = this.getSqlSession().getMapper(TransInsuranceClaimFileDataMapper.class);
			listVo = mapper.queryTransInsuranceClaimFileDataVo();
		} catch (Exception e) {
			logger.error("queryTransInsuranceClaimFileDataVo error:", e);
		} finally {
			this.release();
		}
		
		return listVo;
	}
	
	public int updateEzAcquireTaskId(TransInsuranceClaimFileDataVo vo) {
		int rtn = -1;
		
		try {
			if(vo!=null && vo.getFdId()>0) {
				TransInsuranceClaimFileDataMapper mapper = this.getSqlSession().getMapper(TransInsuranceClaimFileDataMapper.class);
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
