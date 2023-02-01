package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdMsgNotifyDao;
import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.service.IJdzqMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdzqMsgService implements IJdzqMsgService {

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;

    @Override
    public int addJdzqMsg(JdzqNotifyMsg msg) {
        return jdMsgNotifyDao.addJdNotifyMsg(msg);
    }
}
