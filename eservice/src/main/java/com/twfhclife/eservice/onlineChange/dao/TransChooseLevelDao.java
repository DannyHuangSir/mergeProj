package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransChooseLevelVo;

public interface TransChooseLevelDao {

	int insertTransChooseLevel(@Param("transChooseLevelVo") TransChooseLevelVo transChooseLevelVo );
	
	int updateTransChooseLevel(@Param("transChooseLevelVo") TransChooseLevelVo transChooseLevelVo );
	
	TransChooseLevelVo getTransChooseLevel(@Param("rocId") String rocId); 
	
}
