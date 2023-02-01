package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.JdzqNotifyMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JdMsgNotifyDao {

    int addJdNotifyMsg(@Param("msg") JdzqNotifyMsg msg);
}
