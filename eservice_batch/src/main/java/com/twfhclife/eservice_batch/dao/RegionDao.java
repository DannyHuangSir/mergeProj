package com.twfhclife.eservice_batch.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.RegionMapper;
import com.twfhclife.eservice_batch.model.RegionVo;


public class RegionDao extends BaseDao{
	
private static final Logger logger = LogManager.getLogger(RegionDao.class);
	
    public List<RegionVo> getCityRegion() {
    	List<RegionVo> listVo = new ArrayList<RegionVo>();
        try {
        	RegionMapper regionVoMapper = this.getSqlSession().getMapper(RegionMapper.class);
        	listVo = regionVoMapper.getCityRegion();
        } catch (Exception e) {
            logger.error("getCityRegion error:", e);
        } finally {
            this.release();
        }
        return listVo;
    }
}
