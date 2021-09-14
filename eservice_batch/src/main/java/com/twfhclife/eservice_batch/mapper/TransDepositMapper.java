package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransDepositVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransDepositMapper {

    List<TransDepositVo> getTransDeposits(@Param("transNum") String transNum);

}
