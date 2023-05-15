package com.twfhclife.jd.web.dao;

import com.twfhclife.jd.web.model.JdzqNotifyMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface JdMsgNotifyDao {

    int addJdNotifyMsg(@Param("msg") JdzqNotifyMsg msg, @Param("date") Date date);

    List<JdzqNotifyMsg> getWillSendMsgs(@Param("date") Date date);

    int updateNotifyMsgComplete(@Param("ids") List<Long> ids);
}
