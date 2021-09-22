package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.TransChangePremiumVo;

import java.util.Date;

public interface ITransChangePremiumService {

    Date getActiveDate(String policyNo);

    int insertChangePremium(TransChangePremiumVo vo, String userId);

    TransChangePremiumVo getTransChangePremiumDetail(String transNum);
}
