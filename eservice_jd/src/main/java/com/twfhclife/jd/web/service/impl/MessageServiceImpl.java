package com.twfhclife.jd.web.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.dao.JdMsgNotifyDao;
import com.twfhclife.jd.web.dao.MessageDao;
import com.twfhclife.jd.web.model.JdzqNotifyMsg;
import com.twfhclife.jd.web.model.MessageVo;
import com.twfhclife.jd.web.service.IMessageService;
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
