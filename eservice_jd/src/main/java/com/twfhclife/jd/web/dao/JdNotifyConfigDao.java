package com.twfhclife.jd.web.dao;

import com.twfhclife.jd.web.model.NotifyConfigVo;
import com.twfhclife.jd.web.model.NotifyScheduleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JdNotifyConfigDao {

    int updateNotifyConfig(@Param("list") List<NotifyConfigVo> list);

    NotifyConfigVo getNotifyConfig(@Param("userId") String userId, @Param("policyNo") String policyNo, @Param("invtNo") String invtNo);
    List<NotifyScheduleVo> getNotifyConfigSchedule();
}
