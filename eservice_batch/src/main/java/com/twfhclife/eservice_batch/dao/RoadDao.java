package com.twfhclife.eservice_batch.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.RoadMapper;
import com.twfhclife.eservice_batch.model.RoadVo;

public class RoadDao extends BaseDao{

private static final Logger logger = LogManager.getLogger(RoadDao.class);
	
    public List<RoadVo> getRoadByRegionId(String regionId) {
    	List<RoadVo> listVo = new ArrayList<RoadVo>();
        try {
        	RoadMapper roadMapper = this.getSqlSession().getMapper(RoadMapper.class);
        	listVo = roadMapper.getRoadByRegionId(regionId);
        } catch (Exception e) {
            logger.error("getRoadByRegionId error:", e);
        } finally {
            this.release();
        }
        return listVo;
    }
	
    public int insertRoad(String road , String regionId) {
    	int count = 0;
        try {
        	RoadMapper roadMapper = this.getSqlSession().getMapper(RoadMapper.class);
        	 count = roadMapper.insertRoad(road , regionId );
        	 this.commit();
        } catch (Exception e) {
            logger.error("getRoadByRegionId error:", e);
        } finally {
            this.release();
        }
        return count;
    }	
    
}
