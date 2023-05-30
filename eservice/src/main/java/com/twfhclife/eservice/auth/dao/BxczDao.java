package com.twfhclife.eservice.auth.dao;

import org.apache.ibatis.annotations.Param;

public interface BxczDao {
    int insertBxczApiLog(@Param("actionId") String actionId, @Param("type") String type, @Param("apiCode") String apiCode, @Param("apiParam") String apiParam, @Param("apiResponse") String apiResponse);
}
