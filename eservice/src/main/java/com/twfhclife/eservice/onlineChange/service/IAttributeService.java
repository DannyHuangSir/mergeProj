package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.web.model.UsersVo;

import java.util.List;

public interface IAttributeService {

    List<QuestionVo> getQuestions();

    int addNewApply(TransAnswerVo vo, UsersVo user);
}
