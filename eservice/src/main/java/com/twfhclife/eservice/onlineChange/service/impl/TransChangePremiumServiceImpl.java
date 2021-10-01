package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.TransChangePremiumVo;
import com.twfhclife.eservice.onlineChange.service.ITransChangePremiumService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransChangePremiumServiceImpl implements ITransChangePremiumService {

    private static final Logger logger = LogManager.getLogger(TransChangePremiumServiceImpl.class);

    @Autowired
    private TransChangePremiumDao transChangePremiumDao;

    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    private TransDao transDao;

    @Autowired
    private OnlineChangeDao onlineChangeDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Override
    public Date getActiveDate(String policyNo) {
        return transChangePremiumDao.getActiveDate(policyNo);
    }

    @Override
    public int insertChangePremium(TransChangePremiumVo vo, String userId) {

        String transNum = vo.getTransNum();
        int result = 0;
        try {
            // 新增線上申請主檔
            TransVo transVo = new TransVo();
            transVo.setTransNum(transNum);
            transVo.setTransDate(new Date());
            transVo.setTransType(TransTypeUtil.CHANGE_PREMIUM_CODE);
            transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
            transVo.setUserId(userId);
            transVo.setCreateUser(userId);
            transVo.setCreateDate(new Date());
            transDao.insertTrans(transVo);

            // 新增保單號碼
            TransPolicyVo transPolicyVo = new TransPolicyVo();
            transPolicyVo.setTransNum(transNum);
            transPolicyVo.setPolicyNo(vo.getPolicyNoList().get(0));
            transPolicyDao.insertTransPolicy(transPolicyVo);

            transChangePremiumDao.insert(vo);
            result = 1;
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from insertChangePremium: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return result;
    }

    @Override
    public TransChangePremiumVo getTransChangePremiumDetail(String transNum) {
        TransChangePremiumVo qryVo = new TransChangePremiumVo();
        qryVo.setTransNum(transNum);
        List<TransChangePremiumVo> detailList = transChangePremiumDao.getTransChangePremium(qryVo);

        TransChangePremiumVo detailVo = new TransChangePremiumVo();
        if (detailList != null && detailList.size() > 0) {
            detailVo = detailList.get(0);
        }
        return detailVo;
    }
}
