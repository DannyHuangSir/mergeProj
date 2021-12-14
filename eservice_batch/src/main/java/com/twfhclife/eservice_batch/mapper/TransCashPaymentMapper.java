package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransCashPaymentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransCashPaymentMapper {

    List<TransCashPaymentVo> getTransCashPayments(@Param("transNum") String transNum);

    TransAccountVo findAccount(String transNum);
}
