package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.JdUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Mapper
public interface JdUserBatchDao {
    int addUsers(@Param("jdUserVo") JdUserVo jdUserVo);

    int updateUsers(@Param("jdUserVo") JdUserVo jdUserVo);

    int deleteIC(@Param("jdUserVo") JdUserVo jdUserVo);

    int deleteUserIC(@Param("jdUserVo") JdUserVo jdUserVo);

    List<Map<String, Object>> getJdUserQuery(@Param("vo") JdUserVo vo);

    int countJdUser(@Param("vo") JdUserVo vo);

    List<Map<String, Object>> getJdUserIcQuery(@Param("vo") JdUserVo vo);

    int countJdUserIc(@Param("vo") JdUserVo vo);
}
