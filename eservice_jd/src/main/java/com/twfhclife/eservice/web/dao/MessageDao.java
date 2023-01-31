package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.MessageVo;

import java.util.List;

public interface MessageDao {

    List<MessageVo> getMessages(String userId);
}
