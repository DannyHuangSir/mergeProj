package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.dao.JdMsgNotifyDao;
import com.twfhclife.eservice.web.dao.MessageDao;
import com.twfhclife.eservice.web.model.JdzqNotifyMsg;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.service.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private MessageDao messageDao;

    @Override
    public List<MessageVo> getMessages(MessageVo vo, String userId) {
        return messageDao.getMessages(vo, userId, new Date());
    }

    @Override
    public int getNotRead(String id) {
        return messageDao.getNotRead(id, new Date());
    }

    @Override
    public int readMsg(Long id) {
        return messageDao.readMsg(id);
    }

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;

    @Scheduled( cron = "0/20 * * * * ?")
    public void notifyMsgSchedule() {
        List<JdzqNotifyMsg> msgs = jdMsgNotifyDao.getWillSendMsgs(new Date());
        List<Long> ids = Lists.newArrayList();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(msgs)) {
            for (JdzqNotifyMsg msg : msgs) {
                ids.add(msg.getId());
                jdMsgNotifyDao.addJdNotifyMsg(msg, new Date());
            }
            jdMsgNotifyDao.updateNotifyMsgComplete(ids);
        }
    }
}
