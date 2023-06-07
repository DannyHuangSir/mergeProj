package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.IndividualChooseIpVo;

public interface IndividualChooseIpDao {

	public List<IndividualChooseIpVo> getIndividualChooseIp(@Param("ip") String ip  , @Param("time") int time);
	
	public int insertIndividualChooseIp(@Param("individualChooseIpVo") IndividualChooseIpVo individualChooseIpVo);
	
}
