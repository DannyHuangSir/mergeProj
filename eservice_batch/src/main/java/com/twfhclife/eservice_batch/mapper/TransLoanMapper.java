package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransLoanVo;

public interface TransLoanMapper {
	
	public TransLoanVo findById(@Param("transLoanVo") TransLoanVo transLoanVo);
	
	public List<TransLoanVo> getTransLoanList(@Param("transLoanVo") TransLoanVo transLoanVo);
}