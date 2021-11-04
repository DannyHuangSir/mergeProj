package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.QuestionaireDao;
import com.twfhclife.adm.model.OptionVo;
import com.twfhclife.adm.model.QuestionVo;
import com.twfhclife.adm.model.QuestionaireVo;
import com.twfhclife.adm.service.IQuestionaireService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class QuestionaireServiceImpl implements IQuestionaireService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionaireServiceImpl.class);

    @Autowired
    private QuestionaireDao questionaireDao;

    @Override
    public List<Map<String, Object>> getQuestionPageList(QuestionaireVo questionaireVo) {
        return questionaireDao.selectPageQuestions(questionaireVo);
    }

    @Override
    public int getQuestionPageTotal(QuestionaireVo questionaireVo) {
        return questionaireDao.countQuestions(questionaireVo);
    }

    @Override
    public QuestionVo getQuestionById(Long questionId) {
        return questionaireDao.getQuestionById(questionId);
    }

    @Override
    @Transactional
    public int insertOrUpdateQuestion(QuestionVo questionVo) {
        int result = 1;
        Date systemTime = new Date();
        try {
            if (questionVo.getId() == null) {
                Long id = questionaireDao.selectNextQuestionId();
                questionVo.setId(id);
                questionVo.setCreateTime(systemTime);
                questionVo.setUpdateTime(systemTime);
                questionaireDao.insertQuestion(questionVo);
                if (!CollectionUtils.isEmpty(questionVo.getOptions())) {
                    for (OptionVo option : questionVo.getOptions()) {
                        option.setCreateTime(systemTime);
                        option.setUpdateTime(systemTime);
                        option.setQuestionId(questionVo.getId());
                        questionaireDao.insertOption(option);
                    }
                }
            } else {
                questionVo.setUpdateTime(systemTime);
                questionaireDao.updateQuestion(questionVo);
                if (!CollectionUtils.isEmpty(questionVo.getOptions())) {
                    for (OptionVo option : questionVo.getOptions()) {
                        if (option.getId() == null) {
                            option.setCreateTime(systemTime);
                            option.setUpdateTime(systemTime);
                            option.setQuestionId(questionVo.getId());
                            questionaireDao.insertOption(option);
                        } else {
                            option.setUpdateTime(systemTime);
                            option.setQuestionId(questionVo.getId());
                            questionaireDao.updateOption(option);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(questionVo.getRemoveIds())) {
                    questionaireDao.deleteOptionsByIds(questionVo.getRemoveIds());
                }
            }
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from insertOrUpdateQuestion: {}", ExceptionUtils.getStackTrace(e));
        }
        return result;
    }

    @Override
    @Transactional
    public int delelteQuestion(Long questionId) {
        int result = 1;
        try {
            questionaireDao.deleteQuestion(questionId);
            questionaireDao.deleteOptions(questionId);
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from delelteQuestion: {}", ExceptionUtils.getStackTrace(e));
        }
        return result;
    }
}