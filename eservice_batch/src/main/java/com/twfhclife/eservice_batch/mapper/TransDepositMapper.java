package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransDepositVo;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransDepositMapper {

    List<TransDepositVo> getTransDeposits(@Param("transNum") String transNum);

    MutablePair<BigDecimal, Date> getNearFundValue(@Param("invtNo")  String invtNo);
}
