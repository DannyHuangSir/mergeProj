package com.twfhclife.jd.web.service;

import com.twfhclife.jd.web.model.MessageVo;

import java.util.List;

public interface IMessageService {

	List<MessageVo> getMessages(MessageVo vo, String userId);

	int getNotRead(String id);

    int readMsg(Long id);
}