package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.dao.JdMsgNotifyDao;
import com.twfhclife.eservice.web.dao.MessageDao;
import com.twfhclife.eservice.web.model.JdzqNotifyMsg;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public List<MessageVo> getMessages(MessageVo vo, String userId) {
        return messageDao.getMessages(vo, userId);
    }

    @Override
    public int getNotRead(String id) {
        return messageDao.getNotRead(id);
    }

    @Override
    public int readMsg(Long id) {
        return messageDao.readMsg(id);
    }

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;
    @Scheduled( cron = "0/20 * * * * ?")
    public void notifyMsgSchedule() {
        List<JdzqNotifyMsg> msgs = jdMsgNotifyDao.getWillSendMsgs();
        List<Long> ids = Lists.newArrayList();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(msgs)) {
            for (JdzqNotifyMsg msg : msgs) {
                ids.add(msg.getId());
                jdMsgNotifyDao.addJdNotifyMsg(msg);
            }
            jdMsgNotifyDao.updateNotifyMsgComplete(ids);
        }
    }
}
