package com.twfhclife.adm.service;

import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.model.NotifySearchVo;

import java.util.List;
import java.util.Map;

public interface IJdzqMsgService {

    int addJdzqMsgSchedule(JdzqNotifyMsg msg);

    List<Map<String, Object>> getMsgSchedule(NotifySearchVo vo);

    int getMsgScheduleTotal(NotifySearchVo vo);

    int deleteNotifyMsg(Long id);
}
