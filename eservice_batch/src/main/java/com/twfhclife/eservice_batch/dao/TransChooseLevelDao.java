package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransChooseLevelMapper;
import com.twfhclife.eservice_batch.mapper.TransRiskLevelMapper;
import com.twfhclife.eservice_batch.model.IndividualChooseVo;
import com.twfhclife.eservice_batch.model.TransChooseLevelVo;

public class TransChooseLevelDao extends BaseDao{

	private static final Logger logger = LogManager.getLogger(TransChooseLevelDao.class);

	
    public List<TransChooseLevelVo> getTransChooseLevel(TransChooseLevelVo transChooseLevelVo) {
        List<TransChooseLevelVo> transChooseLevelVos = null;
        try {
        	TransChooseLevelMapper transChooseLevelMapper = this.getSqlSession().getMapper(TransChooseLevelMapper.class);
        	transChooseLevelVos = transChooseLevelMapper.getTransChooseLevel(transChooseLevelVo.getTransNum());
        } catch (Exception e) {
            logger.error("getTransChooseLevel error:", e);
        } finally {
            this.release();
        }
        return transChooseLevelVos;
    }
    
    public TransChooseLevelVo getEffectTransChooseLevel(String transNum) throws Exception{
    	TransChooseLevelVo transChooseLevelVo = null;
    	try{
           	TransChooseLevelMapper transChooseLevelMapper = this.getSqlSession().getMapper(TransChooseLevelMapper.class);
           	transChooseLevelVo = transChooseLevelMapper.getEffectTransChooseLevel(transNum);
    	}catch (Exception e) {
    		 logger.error("getEffectTransChooseLevel error:", e);
    		 throw new Exception(e);
		}
    	return transChooseLevelVo;
    }
    
    public void updateIndividual(TransChooseLevelVo vo)throws Exception{
    	try {
        	TransChooseLevelMapper transChooseLevelMapper = this.getSqlSession().getMapper(TransChooseLevelMapper.class);
        	transChooseLevelMapper.updateIndividual(vo);
        	this.commit();
    	}catch (Exception e) {
    		logger.error("updateIndividual error:", e);
			throw new Exception(e);
		}finally {
			this.release();
		}    	
    }
    
    public IndividualChooseVo getIndividualChooseByRocId(String rocId){
    	try {
    		TransChooseLevelMapper transChooseLevelMapper = this.getSqlSession().getMapper(TransChooseLevelMapper.class);
			return transChooseLevelMapper.getIndividualChooseByRocId(rocId);
		} catch (Exception e) {
			logger.error("getIndividualChooseByRocId error:", e);
		} finally {
			this.release();
		}
		return null;    	
    }
}
