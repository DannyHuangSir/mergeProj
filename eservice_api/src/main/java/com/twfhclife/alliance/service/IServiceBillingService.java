package com.twfhclife.alliance.service;

import com.twfhclife.alliance.domain.Spa402RequestVo;

import java.util.Map;

public interface IServiceBillingService {


    int addServiceBillingReplay(Spa402RequestVo vo);

    Map<String, Object> getReplayStatusByIdNo(Long id);
}
