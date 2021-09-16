package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.QuestionVo;

import java.util.List;

public interface QuestionnaireDao {

    List<QuestionVo> selectQuestionnaire();
}