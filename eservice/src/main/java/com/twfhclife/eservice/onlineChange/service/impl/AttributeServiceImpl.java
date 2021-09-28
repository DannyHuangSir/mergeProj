package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.AnswerVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AttributeServiceImpl implements IAttributeService {

    private static final Logger logger = LogManager.getLogger(AttributeServiceImpl.class);

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    private TransDao transDao;

    @Autowired
    private ITransService transService;

    @Autowired
    private ITransRiskLevelService transRiskLevelService;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private TransRiskLevelDao transRiskLevelDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Override
    public List<QuestionVo> getQuestions() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return questionnaireDao.selectQuestionnaire(calendar.getTime());
    }

    @Override
    @Transactional
    public int addNewApply(TransAnswerVo vo, UsersVo user) {
        // 設定交易序號
        String transNum = transService.getTransNum();
        int result = 0;
        try {

            TransVo transVo = new TransVo();
            transVo.setTransNum(transNum);
            transVo.setTransDate(new Date());
            transVo.setTransType(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
            transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_APPLYING);
            transVo.setUserId(user.getUserId());
            transVo.setCreateUser(user.getUserId());
            transVo.setCreateDate(new Date());
            transDao.insertTrans(transVo);

            TransPolicyVo transPolicyVo = new TransPolicyVo();
            transPolicyVo.setTransNum(transNum);
            transPolicyVo.setPolicyNo("");
            transPolicyDao.insertTransPolicy(transPolicyVo);

            // 新增變更風險屬性
            TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
            transRiskLevelVo.setTransNum(transNum);
            transRiskLevelVo.setRiskLevelDesc(vo.getDesc());
            transRiskLevelVo.setRiskLevelOld(transRiskLevelService.getUserRiskAttr(user.getRocId()));
            transRiskLevelVo.setRiskLevelNew(vo.getLevel());
            transRiskLevelVo.setRiskScore(vo.getScore());
            transRiskLevelDao.insertTransRiskLevel(transRiskLevelVo);

            AnswerVo answerVo = new AnswerVo();
            answerVo.setAnswers(vo.getAnswers().toString());
            answerVo.setTransNum(transNum);
            answerVo.setQuestions(vo.getQuestions());
            Date systemDate = new Date();
            answerVo.setCreateTime(systemDate);
            answerVo.setUpdateTime(systemDate);
            answerDao.insert(answerVo);
            result = 1;
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from addNewApply: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return result;
    }

    @Override
    public TransRiskLevelVo getTransRiskLevelDetail(String transNum) {
        TransRiskLevelVo qryVo = new TransRiskLevelVo();
        qryVo.setTransNum(transNum);
        List<TransRiskLevelVo> transRiskLevelVoList = transRiskLevelDao.getTransRiskLevelList(qryVo);

        TransRiskLevelVo detailVo = new TransRiskLevelVo();
        if (transRiskLevelVoList != null && transRiskLevelVoList.size() > 0) {
            detailVo = transRiskLevelVoList.get(0);
        }
        return detailVo;
    }
}
