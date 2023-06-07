package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.policy.model.IndividualVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.PolicyDetailVo;

import java.util.List;

public interface IAttributeService {

    List<QuestionVo> getQuestions();

    TransAnswerVo addNewApply(IndividualChooseVo vo, UsersVo user);

    TransRiskLevelVo getTransRiskLevelDetail(String transNum);

    IndividualVo getIndividualVoByRocId(String userRocId);
    
    List<PolicyDetailVo> getPolicyDataByRocId(String rocId);
    
    TransVo findByTransNum(String transNum);
}
