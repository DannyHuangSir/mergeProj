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
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.util.ApConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

	@Override
	public void handlePolicyStatusLocked(String userRocId, List<PolicyListVo> policyList, String parameterCode) {
		 String INVESTMENT_TYPES = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, parameterCode + "_INVESTMENT_TYPE");
	        if (!CollectionUtils.isEmpty(policyList)) {
	            for (PolicyListVo e: policyList) {
	                if (StringUtils.equals(e.getApplyLockedFlag(), "Y")) {
	                    continue;
	                }
	                if (!CollectionUtils.isEmpty(policyList)) {
	                    for (PolicyListVo vo : policyList) {
	                        if ("N".equals(vo.getExpiredFlag())) {
	                            String policyType = vo.getPolicyType();
	                            if (StringUtils.isNotBlank(INVESTMENT_TYPES) && !INVESTMENT_TYPES.contains(policyType)) {
	                                vo.setApplyLockedFlag("Y");
	                                vo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_CASH_PAYMENT);
	                                continue;
	                            }
	                        }
	                    }
	                }
	                if (StringUtils.isNotBlank(userRocId) && !StringUtils.equals(userRocId, e.getRocId())) {
	                    e.setApplyLockedFlag("Y");
	                    e.setApplyLockedMsg("被保人無法申請保單");
	                    continue;
	                }
	                if ("Y".equals(e.getExpiredFlag())) {
//	                    e.setApplyLockedFlag("Y");
	                    // 此張保單已過投保終期
	                    e.setApplyLockedFlag("Y");
	                    e.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
	                    continue;
	                }
	            }
	        }
	}
}
