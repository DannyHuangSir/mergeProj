package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;
import com.twfhclife.eservice.onlineChange.service.ITransCashPaymentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransCashPaymentServiceImpl implements ITransCashPaymentService {

    private static final Logger logger = LogManager.getLogger(TransCashPaymentServiceImpl.class);

    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    private TransDao transDao;

    @Autowired
    private OnlineChangeDao onlineChangeDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Autowired
    private TransCashPaymentDao transCashPaymentDao;

    @Override
    public int insertCashPayment(TransCashPaymentVo vo, String userId) {

        String transNum = vo.getTransNum();
        int result = 0;
        try {
            // 新增線上申請主檔
            TransVo transVo = new TransVo();
            transVo.setTransNum(transNum);
            transVo.setTransDate(new Date());
            transVo.setTransType(TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
            transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_APPLYING);
            transVo.setUserId(userId);
            transVo.setCreateUser(userId);
            transVo.setCreateDate(new Date());
            transDao.insertTrans(transVo);

            // 新增保單號碼
            TransPolicyVo transPolicyVo = new TransPolicyVo();
            transPolicyVo.setTransNum(transNum);
            transPolicyVo.setPolicyNo(vo.getPolicyNo());
            transPolicyDao.insertTransPolicy(transPolicyVo);

            transCashPaymentDao.insert(vo);
            result = 1;
        } catch (Exception e) {
            result = 0;
            logger.error("Unable to init from insertCashPayment: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return result;
    }

    @Override
    public String getPreAllocation(String policyNo) {
        TransCashPaymentVo vo = transCashPaymentDao.getCashPaymentByPolicyNo(policyNo);
        if (vo != null) {
            return vo.getAllocation();
        } else {
            return "";
        }
    }

    @Override
    public TransCashPaymentVo getCurrentTransCashPayment(String transNum) {
        return transCashPaymentDao.getTransPaymentByTransNum(transNum);
    }

    @Override
    public Boolean checkHasBankInfo(String userId) {
        return transCashPaymentDao.countTransBankInfo(userId) > 0;
    }
}
