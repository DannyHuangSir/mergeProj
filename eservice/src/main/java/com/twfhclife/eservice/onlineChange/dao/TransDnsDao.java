package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransDnsVo;

public interface TransDnsDao {

	int insertTransDns(@Param("vo") TransDnsVo vo);

	TransDnsVo getTransDaoDnsDetail(@Param("transNum") String transNum);
}
