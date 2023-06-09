package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.RoadVo;

public interface RoadMapper {

	public List<RoadVo> getRoadByRegionId(@Param("regionId") String regionId);
	
	public int insertRoad(@Param("road") String road ,@Param("regionId") String regionId);
	
}
