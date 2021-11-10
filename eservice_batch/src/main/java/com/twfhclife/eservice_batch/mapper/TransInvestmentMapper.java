package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransLoanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransInvestmentMapper {
	
	public List<TransInvestmentVo> getTransInvestments(@Param("transNum") String transNum);

    TransAccountVo findAccount(@Param("transNum") String transNum, @Param("invtNo") String invtNo);
}