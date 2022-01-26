package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface QuestionnaireDao {

    List<QuestionVo> selectQuestionnaire(@Param("date") Date date);
}