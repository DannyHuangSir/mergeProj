package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdMsgNotifyDao;
import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.service.IJdzqMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JdzqMsgService implements IJdzqMsgService {

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;

    @Override
    public int addJdzqMsg(JdzqNotifyMsg msg) {
        return jdMsgNotifyDao.addJdNotifyMsg(msg);
    }

    @Override
    public int addJdzqMsgSchedule(JdzqNotifyMsg msg) {
        return jdMsgNotifyDao.addJdzqMsgSchedule(msg);
    }

    @Override
    public List<Map<String, Object>> getMsgSchedule(NotifySearchVo vo) {
        return jdMsgNotifyDao.getJdzqMsgSchedule(vo);
    }

    @Override
    public int getMsgScheduleTotal(NotifySearchVo vo) {
        return jdMsgNotifyDao.getJdzqMsgScheduleTotal(vo);
    }

    @Scheduled( cron = "0/10 * * * * ? ")
    public void notifyMsgSchedule() {

    }
}
