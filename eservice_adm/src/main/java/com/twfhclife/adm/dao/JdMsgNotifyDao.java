package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.model.NotifySearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JdMsgNotifyDao {

    int addJdzqMsgSchedule(@Param("msg") JdzqNotifyMsg msg);

    List<Map<String, Object>> getJdzqMsgSchedule(@Param("vo") NotifySearchVo vo);

    int getJdzqMsgScheduleTotal(@Param("vo") NotifySearchVo vo);

    int deleteNotifyMsg(@Param("id") Long id);
}
