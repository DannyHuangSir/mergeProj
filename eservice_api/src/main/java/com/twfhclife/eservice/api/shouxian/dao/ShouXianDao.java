package com.twfhclife.eservice.api.shouxian.dao;


import com.twfhclife.eservice.api.shouxian.model.PolicyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShouXianDao {

    List<PolicyVo> getPolicyList(@Param("vo") PolicyVo vo);
}
