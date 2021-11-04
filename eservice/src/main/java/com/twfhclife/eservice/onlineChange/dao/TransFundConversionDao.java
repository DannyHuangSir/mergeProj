package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransFundConversionVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransFundConversionDao {

    int delete(Long id);

    int insert(TransFundConversionVo record);
    int insertInvestments(@Param("record") TransFundConversionVo record,
                          @Param("transInvestment") TransInvestmentVo transInvestment);

    TransFundConversionVo selectByPrimaryKey(Long id);

    int update(TransFundConversionVo record);
    //查詢投資轉換詳情
    List<TransFundConversionVo> transInvestmentConversionDetail(@Param("transNum") String transNum);
}