package com.twfhclife.eservice.auth.dao;

import com.twfhclife.eservice.onlineChange.model.Bxcz415CallBackDataVo;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BxczDao {
    int insertBxczApiLog(@Param("actionId") String actionId, @Param("type") String type, @Param("apiCode") String apiCode, @Param("apiParam") String apiParam, @Param("apiResponse") String apiResponse);

    int insertBxczSignRecord(@Param("actionId") String actionId, @Param("transNum") String transNum, @Param("date") Date date);

    int updateBxczSignRecordByActionId(@Param("vo") Bxcz415CallBackDataVo vo, @Param("code") String code, @Param("msg") String msg, @Param("idVerifyTime") Date idVerifyTime,  @Param("signTime") Date signTime);

    SignRecord getSignRecordByActionId(@Param("actionId") String actionId);
}
