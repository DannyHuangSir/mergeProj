package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.OptionVo;
import com.twfhclife.adm.model.QuestionVo;
import com.twfhclife.adm.model.QuestionaireVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QuestionaireDao {

    List<Map<String, Object>> selectPageQuestions(@Param("vo") QuestionaireVo vo);

    int countQuestions(@Param("vo") QuestionaireVo questionaireVo);

    QuestionVo getQuestionById(@Param("questionId") Long questionId);

    int insertQuestion(QuestionVo questionVo);

    int insertOption(OptionVo option);

    int deleteQuestion(@Param("questionId") Long questionId);

    int deleteOptions(@Param("questionId") Long questionId);

    int updateQuestion(QuestionVo questionVo);

    int updateOption(OptionVo option);

    int deleteOptionsByIds(@Param("ids") List<Long> removeIds);

    Long selectNextQuestionId();
}
