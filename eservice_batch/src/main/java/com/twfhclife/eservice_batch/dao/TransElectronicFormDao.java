package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransElectronicFormMapper;
import com.twfhclife.eservice_batch.model.TransElectronicFormVo;


public class TransElectronicFormDao extends BaseDao{
	
	private static final Logger logger = LogManager.getLogger(TransElectronicFormDao.class);
	
    public List<TransElectronicFormVo> getTransElectronicForm(TransElectronicFormVo transElectronicFormVo) {
        List<TransElectronicFormVo> transElectronicFormVos = null;
        try {
        	TransElectronicFormMapper transElectronicFormMapper = this.getSqlSession().getMapper(TransElectronicFormMapper.class);
        	transElectronicFormVos = transElectronicFormMapper.getTransElectronicFormList(transElectronicFormVo);
        } catch (Exception e) {
            logger.error("getTransElectronicFormList error:", e);
        } finally {
            this.release();
        }
        return transElectronicFormVos;
    }

	
}
