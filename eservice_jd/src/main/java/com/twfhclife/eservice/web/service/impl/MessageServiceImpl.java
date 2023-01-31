package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.web.dao.MessageDao;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public List<MessageVo> getMessages(String userId) {
        return messageDao.getMessages(userId);
    }
}
