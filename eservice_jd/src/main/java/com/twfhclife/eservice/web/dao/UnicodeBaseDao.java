package com.twfhclife.eservice.web.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.UnicodeBaseVo;

public interface UnicodeBaseDao {

	public UnicodeBaseVo getUnicodeBaseByName(@Param("internalName") String internalName);
}
