package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransDepositDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;

public interface TransDepositDao {

    int insert(TransDepositVo record);

    TransDepositDetailVo  getAppliedTransDeposits(String transNum);
}