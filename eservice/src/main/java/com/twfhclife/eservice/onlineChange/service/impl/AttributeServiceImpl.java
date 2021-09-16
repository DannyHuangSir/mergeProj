package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.QuestionnaireDao;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl implements IAttributeService {

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Override
    public List<QuestionVo> getQuestions() {
        return questionnaireDao.selectQuestionnaire();
    }
}
