package com.twfhclife.alliance.dao;

import com.twfhclife.alliance.domain.Spa402RequestVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ServiceBillingDao {
    int addServiceBillingReplay(@Param("vo") Spa402RequestVo vo);

    Map<String, Object> getReplayStatusByIdNo(@Param("id") Long id);
}
