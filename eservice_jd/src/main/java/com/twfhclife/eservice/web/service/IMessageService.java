package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.MessageVo;

import java.util.List;

public interface IMessageService {

	List<MessageVo> getMessages(String userId);

}