package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.NotifyConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JdNotifyConfigDao {

    int updateNotifyConfig(@Param("list") List<NotifyConfigVo> list);

    NotifyConfigVo getNotifyConfig(@Param("userId") String userId, @Param("policyNo") String policyNo, @Param("invtNo") String invtNo);
}
