package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.JdzqNotifyMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JdMsgNotifyDao {

    int addJdNotifyMsg(@Param("msg") JdzqNotifyMsg msg);

    List<JdzqNotifyMsg> getWillSendMsgs();

    int updateNotifyMsgComplete(@Param("ids") List<Long> ids);
}
