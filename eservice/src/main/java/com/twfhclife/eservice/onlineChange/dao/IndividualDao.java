package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.IndividualVo;

public interface IndividualDao {
	
	int insertIndividual(@Param("individualVo") IndividualVo individualVo);
	
	IndividualVo getIndividualData(@Param("rocId") String rocId);
	
	int updateIndividual(@Param("individualVo") IndividualVo individualVo);	
}
