package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransContractRevocationMapper;
import com.twfhclife.eservice_batch.model.TransContractRevocationVo;


public class TransContractRevocationDao extends BaseDao{
	
	private static final Logger logger = LogManager.getLogger(TransContractRevocationDao.class);
	
    public List<TransContractRevocationVo> getTransContractRevocation(TransContractRevocationVo transContractRevocationVo) {
        List<TransContractRevocationVo> transContractRevocationVos = null;
        try {
        	TransContractRevocationMapper transContractRevocationMapper = this.getSqlSession().getMapper(TransContractRevocationMapper.class);
        	transContractRevocationVos = transContractRevocationMapper.getTransContractRevocationList(transContractRevocationVo);
        } catch (Exception e) {
            logger.error("getTransContractRevocation error:", e);
        } finally {
            this.release();
        }
        return transContractRevocationVos;
    }
}