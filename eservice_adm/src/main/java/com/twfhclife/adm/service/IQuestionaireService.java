package com.twfhclife.adm.service;

import com.twfhclife.adm.model.QuestionVo;
import com.twfhclife.adm.model.QuestionaireVo;

import java.util.List;
import java.util.Map;

public interface IQuestionaireService {

    List<Map<String, Object>> getQuestionPageList(QuestionaireVo questionaireVo);

    int getQuestionPageTotal(QuestionaireVo questionaireVo);

    QuestionVo getQuestionById(Long questionId);

    int insertOrUpdateQuestion(QuestionVo questionVo);

    int delelteQuestion(Long questionId);
}
