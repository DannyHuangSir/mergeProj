package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.generic.annotation.EserviceEventParam;
import com.twfhclife.eservice.generic.annotation.EventRecordLog;
import com.twfhclife.eservice.generic.annotation.EventRecordParam;
import com.twfhclife.eservice.generic.annotation.SqlParam;
import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransCashPaymentDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.onlineChange.service.ITransCashPaymentService;
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
    @EventRecordLog(value = @EventRecordParam(
            eventCode = "ES-018",
            systemEventParams = {
                    @EserviceEventParam(
                            sqlId = "com.twfhclife.eservice.onlineChange.dao.TransDao.getTransNum",
                            execMethod = "送出線上申請"
                    )
            }))
    public int insertCashPayment(TransCashPaymentVo vo, String userId) {

        String transNum = vo.getTransNum();
        int result = 0;
        try {
            // 新增線上申請主檔
            TransVo transVo = new TransVo();
            transVo.setTransNum(transNum);
            transVo.setTransDate(new Date());
            transVo.setTransType(TransTypeUtil.CASH_PAYMENT_PARAMETER_CODE);
            transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
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
    public TransCashPaymentVo getCurrentTransCashPayment(String transNum) {
        return transCashPaymentDao.getTransPaymentByTransNum(transNum);
    }

    @Override
    public Boolean checkHasBankInfo(String policyNo) {
        return transCashPaymentDao.countTransBankInfo(policyNo) > 0;
    }
}
