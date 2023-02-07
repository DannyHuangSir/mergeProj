package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.MessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageDao {

    List<MessageVo> getMessages(@Param("vo") MessageVo vo, @Param("userId") String userId);

    int getMessagesTotal(@Param("vo") MessageVo vo, @Param("userId") String userId);
    int getNotRead(@Param("userId") String userId);

    int readMsg(@Param("id") Long id);
}
