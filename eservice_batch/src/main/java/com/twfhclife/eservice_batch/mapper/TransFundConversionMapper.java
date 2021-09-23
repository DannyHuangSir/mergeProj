package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransFundConversionVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransFundConversionMapper {
	
	public List<TransFundConversionVo> getTransFundConversions(@Param("transNum") String transNum);
}