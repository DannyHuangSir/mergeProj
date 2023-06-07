package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.generic.annotation.EserviceEventParam;
import com.twfhclife.eservice.generic.annotation.EventRecordLog;
import com.twfhclife.eservice.generic.annotation.EventRecordParam;
import com.twfhclife.eservice.generic.annotation.SqlParam;
import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.AnswerVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.QuestionVo;
import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransChooseLevelVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.IndividualVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.PolicyDetailRequest;
import com.twfhclife.generic.api_model.PolicyDetailResponse;
import com.twfhclife.generic.api_model.PolicyDetailVo;

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
    
	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;
	
	@Autowired
	private IndividualChooseDao individualChooseDao;
	
	@Autowired
	private TransChooseLevelDao transChooseLevelDao;

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
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "ES-018",
            systemEventParams = {
                    @EserviceEventParam(
                            sqlId = "com.twfhclife.eservice.onlineChange.dao.TransDao.getTransNum",
                            execMethod = "送出線上申請"
                    )
            }))
    public TransAnswerVo addNewApply(IndividualChooseVo vo, UsersVo user) {
        // 設定交易序號
    	TransAnswerVo transAnswerVo = new TransAnswerVo();
        String transNum = transService.getTransNum();
        transAnswerVo.setTransNum(transNum);
        int result = 0;
        try {
            TransVo transVo = new TransVo();
            transVo.setTransNum(transNum);
            transVo.setTransDate(new Date());
            transVo.setTransType(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
            transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
            transVo.setUserId(user.getUserId());
            transVo.setCreateUser(user.getUserId());
            transVo.setCreateDate(new Date());
            transDao.insertTrans(transVo);

            TransPolicyVo transPolicyVo = new TransPolicyVo();
            transPolicyVo.setTransNum(transNum);
            transPolicyVo.setPolicyNo("");
            transPolicyDao.insertTransPolicy(transPolicyVo);
            insertTransChooseLevelVo(vo , user);
            // 新增變更風險屬性
            TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
            transRiskLevelVo.setTransNum(transNum);
            transRiskLevelVo.setRiskLevelDesc(vo.getDesc());
            transRiskLevelVo.setRiskLevelOld(transRiskLevelService.getUserRiskAttr(user.getRocId()));
            transRiskLevelVo.setRiskLevelNew(vo.getRiskAttr());
            transRiskLevelVo.setRiskScore(Integer.valueOf(vo.getScore()));
            transRiskLevelVo.setRocId(user.getRocId());
            transRiskLevelVo.setChoose(vo.getChoose());
            transRiskLevelVo.setRuleStatus(vo.getRuleStatus());
            transRiskLevelDao.insertTransRiskLevel(transRiskLevelVo);
            
			IndividualChooseVo selectIndividualChooseVo =individualChooseDao.getIndividualChoosData(vo.getRocId());
			if (selectIndividualChooseVo != null) {
				vo.setSource("1");
				vo.setEditDate(new Date());
				vo.setRatingDate(new Date());
				individualChooseDao.updateIndividualChoose(vo);
			} else {
				vo.setSource("1");
				vo.setEditDate(new Date());
				vo.setRatingDate(new Date());
				individualChooseDao.insertIndividualChoose(vo);
			}

            AnswerVo answerVo = new AnswerVo();
            answerVo.setAnswers(vo.getAnswers().toString());
            answerVo.setTransNum(transNum);
            answerVo.setQuestions(vo.getQuestions());
            Date systemDate = new Date();
            answerVo.setCreateTime(systemDate);
            answerVo.setUpdateTime(systemDate);
            answerDao.insert(answerVo);
            result = 1;
            return transAnswerVo;
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from addNewApply: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "ES-017",
            systemEventParams = {
                    @EserviceEventParam(
                            sqlId = "com.twfhclife.eservice.onlineChange.dao.TransDao.findByTransNum",
                            execMethod = "查詢線上申請明細",
                            sqlParams = {
                                    @SqlParam(requestParamkey = "transNums", sqlParamkey = "transNum")
                            }
                    )
            }))
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

    @Override
    public IndividualVo getIndividualVoByRocId(String userRocId) {
        return transRiskLevelDao.getIndividualVoByRocId(userRocId);
    }

	@Override
	public List<PolicyDetailVo> getPolicyDataByRocId(String rocId) {
		PolicyDetailRequest req = new PolicyDetailRequest();
		req.setRocId(rocId);
		PolicyDetailResponse response = transCtcSelectUtilClient.getPolicyDataByRocId(req);
		return response.getPolicyDetailList();
	}

	@Override
	public TransVo findByTransNum(String transNum) {
		return transDao.findByTransNum(transNum);
	}
	
	private void insertTransChooseLevelVo( IndividualChooseVo individualChooseVo , UsersVo usersVo) {
		String transNum = transService.getTransNum();
		TransVo transVo = new TransVo();
		transVo.setTransNum(transNum);
		transVo.setTransDate(new Date());
		transVo.setTransType(TransTypeUtil.CHOOSE_LEVEL_PARAMETER_CODE);
		transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
		if (usersVo != null) {
			transVo.setUserId(usersVo.getUserId());
			transVo.setCreateUser(usersVo.getUserId());
		} else {
			transVo.setCreateUser("twfhclife.com.tw");
		}
		transVo.setCreateDate(new Date());
		transDao.insertTrans(transVo);
		
		TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
		transChooseLevelVo.setTransNum(transNum);
		transChooseLevelVo.setRocId(individualChooseVo.getRocId());
		transChooseLevelVo.setChooseLevelOld(individualChooseVo.getOldRiskAttr());
		transChooseLevelVo.setChooseLevelNew(individualChooseVo.getRiskAttr());
		transChooseLevelVo.setChooseScore(Integer.valueOf(individualChooseVo.getScore()));
		transChooseLevelVo.setChooseLevelDesc(individualChooseVo.getDesc());
		transChooseLevelVo.setChoose(individualChooseVo.getChoose());
		transChooseLevelVo.setRuleStatus(individualChooseVo.getRuleStatus());
		transChooseLevelDao.insertTransChooseLevel(transChooseLevelVo);
	} 
}
