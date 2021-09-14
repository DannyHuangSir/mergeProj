package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransCreditCardDateVo;

public interface TransCreditCardDateMapper {
	
	public TransCreditCardDateVo findById(@Param("transCreditCardDateVo") TransCreditCardDateVo transCreditCardDateVo);
	
	public List<TransCreditCardDateVo> getTransCreditCardDateList(@Param("transCreditCardDateVo") TransCreditCardDateVo transCreditCardDateVo);
}