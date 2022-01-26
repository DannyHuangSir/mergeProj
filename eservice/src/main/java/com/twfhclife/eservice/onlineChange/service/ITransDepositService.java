package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.TransDepositDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;

import java.util.List;

public interface ITransDepositService {

    List<ParameterVo> getDepositConfigs();

    PolicyListVo getDepositPolicy(String userRocId, String policyNo);

    int addNewDepositApply(TransDepositVo vo, UsersVo user);

    TransDepositDetailVo getAppliedTransDeposits(String transNum);

    void distributionDepositFund(String userRocId, TransDepositVo vo) throws Exception;
}
