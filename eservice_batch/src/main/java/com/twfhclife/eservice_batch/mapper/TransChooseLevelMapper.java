package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.IndividualChooseVo;
import com.twfhclife.eservice_batch.model.TransChooseLevelVo;

public interface TransChooseLevelMapper {
	
	List<TransChooseLevelVo> getTransChooseLevel(@Param("transNum") String transNum);

	TransChooseLevelVo getEffectTransChooseLevel(@Param("transNum") String transNum); 
	
	void updateIndividual(TransChooseLevelVo vo);
	
	IndividualChooseVo getIndividualChooseByRocId(@Param("rocId") String  rocId);
}
