package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;

public interface IndividualChooseDao {

	public IndividualChooseVo getIndividualChoosData(@Param("rocId") String rocId); 
	
	public int insertIndividualChoose(@Param("individualChooseVo") IndividualChooseVo individualChooseVo);
	
	public int updateIndividualChoose(@Param("individualChooseVo") IndividualChooseVo individualChooseVo);
	
	public List<String> getpolicyInvestmentType();
	
}
