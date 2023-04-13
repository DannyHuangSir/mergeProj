package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransElectronicFormVo;

public interface TransElectronicFormDao {
	int insertTransElectronicForm(@Param("transElectronicFormVo") TransElectronicFormVo transElectronicFormVo);
}
