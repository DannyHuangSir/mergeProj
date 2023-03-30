package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.JdUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther lihao
 */
@Mapper
public interface JdUserDao {
    JdUserVo getUser(@Param("rocId") String rocId);

    JdUserVo getUserIC(@Param("depId") String depId,@Param("icId")String icId);

    int insertUserIC(@Param("jdUserVo") JdUserVo jdUserVo);

    int deleteUserIC(@Param("jdUserVo") JdUserVo jdUserVo);

    int updateUserIC(@Param("jdUserVo") JdUserVo jdUserVo);

    JdUserVo getIcId(@Param("icId")String icId,@Param("depId")String depId);

    int countUserIc(@Param("icId")String icId);

    int countUser(@Param("rocId") String rocId);


}
