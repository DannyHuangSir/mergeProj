package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransDeratePaidOffMapper;
import com.twfhclife.eservice_batch.model.TransDeratePaidOffVo;

public class TransDeratePaidOffDao extends BaseDao{
private static final Logger logger = LogManager.getLogger(TransDeratePaidOffDao.class);
	
    public TransDeratePaidOffVo getTransDeratePaidOff(TransDeratePaidOffVo transDeratePaidOffVo) {
        try {
        	TransDeratePaidOffMapper transDeratePaidOffMapper = this.getSqlSession().getMapper(TransDeratePaidOffMapper.class);
        	transDeratePaidOffVo = transDeratePaidOffMapper.findByTransNum(transDeratePaidOffVo);
        } catch (Exception e) {
            logger.error("getTransDeratePaidOffList error:", e);
        } finally {
            this.release();
        }
        return transDeratePaidOffVo;
    }

}
