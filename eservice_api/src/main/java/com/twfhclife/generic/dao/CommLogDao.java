package com.twfhclife.generic.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.generic.domain.CommLogRequest;

public interface CommLogDao {

	int addCommLog(@Param("vo") CommLogRequest req) throws Exception;

}
