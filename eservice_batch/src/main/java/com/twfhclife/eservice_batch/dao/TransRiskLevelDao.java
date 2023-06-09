package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransRiskLevelMapper;
import com.twfhclife.eservice_batch.model.IndividualChooseVo;
import com.twfhclife.eservice_batch.model.TransRiskLevelVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class TransRiskLevelDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransRiskLevelDao.class);

	public List<TransRiskLevelVo> getTransLevel(TransRiskLevelVo vo) {
		List<TransRiskLevelVo> transRiskLevelVos = null;
		try {
			TransRiskLevelMapper transRiskLevelMapper = this.getSqlSession().getMapper(TransRiskLevelMapper.class);
			transRiskLevelVos = transRiskLevelMapper.getTransLevel(vo.getTransNum());
		} catch (Exception e) {
			logger.error("getTransLevel error:", e);
		} finally {
			this.release();
		}
		return transRiskLevelVos;
	}

	public TransRiskLevelVo getEffectUserTransRiskLevel(String transNum) throws Exception {
		try {
			TransRiskLevelMapper transRiskLevelMapper = this.getSqlSession().getMapper(TransRiskLevelMapper.class);
			return transRiskLevelMapper.getEffectUserTransRiskLevel(transNum);
		} catch (Exception e) {
			logger.error("getEffectUserTransRiskLevel error:", e);
			throw new Exception(e);
		} finally {
			this.release();
		}
	}

	public void updateIndividual(TransRiskLevelVo vo) throws Exception {
		try {
			TransRiskLevelMapper transRiskLevelMapper = this.getSqlSession().getMapper(TransRiskLevelMapper.class);
			transRiskLevelMapper.updateIndividual(vo);
			this.commit();
		} catch (Exception e) {
			logger.error("updateIndividual error:", e);
			throw new Exception(e);
		} finally {
			this.release();
		}
	}

    public String getTop1PolicyNo(String rocId) {
		try {
			TransRiskLevelMapper transRiskLevelMapper = this.getSqlSession().getMapper(TransRiskLevelMapper.class);
			return transRiskLevelMapper.getTop1PolicyNo(rocId);
		} catch (Exception e) {
			logger.error("getTop1PolicyNo error:", e);
		} finally {
			this.release();
		}
		return null;
    }
    
    public IndividualChooseVo getIndividualChooseByRocId(String rocId){
    	try {
			TransRiskLevelMapper transRiskLevelMapper = this.getSqlSession().getMapper(TransRiskLevelMapper.class);
			return transRiskLevelMapper.getIndividualChooseByRocId(rocId);
		} catch (Exception e) {
			logger.error("getIndividualChooseByRocId error:", e);
		} finally {
			this.release();
		}
		return null;    	
    }
}