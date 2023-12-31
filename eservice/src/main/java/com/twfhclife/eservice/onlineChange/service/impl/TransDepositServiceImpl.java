package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.generic.annotation.EserviceEventParam;
import com.twfhclife.eservice.generic.annotation.EventRecordLog;
import com.twfhclife.eservice.generic.annotation.EventRecordParam;
import com.twfhclife.eservice.generic.annotation.SqlParam;
import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransDepositDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransDepositDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.DepositPolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransDepositServiceImpl implements ITransDepositService {

    private static final Logger log = LogManager.getLogger(TransDepositServiceImpl.class);

    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private TransDao transDao;

    @Autowired
    private OnlineChangeDao onlineChangeDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Autowired
    private TransDepositDao transDepositDao;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    private static final String DEPOSIT_PARAMETER_CATEGORY_CODE = "DEPOSIT_PARAMETER_CATEGORY";

    public List<ParameterVo> getDepositConfigs() {
        return parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, DEPOSIT_PARAMETER_CATEGORY_CODE);
    }

    @Override
    public PolicyListVo getDepositPolicy(String userRocId, String policyNo) {
        List<PolicyListVo> depositList = policyListService.getDepositList(userRocId, policyNo);
        if (!CollectionUtils.isEmpty(depositList)) {
            return depositList.get(0);
        }
        return null;
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
    public int addNewDepositApply(TransDepositVo vo, UsersVo user) {

        Map<String, Object> params = Maps.newHashMap();
        int result = 0;
        try {
        params.put("transNum", null);
        transDao.getTransNum(params);
        String transNum = params.get("transNum").toString();
        vo.setTransNum(transNum);

        TransVo transVo = new TransVo();
        transVo.setTransNum(transNum);
        transVo.setTransDate(new Date());
        transVo.setTransType(TransTypeUtil.DEPOSIT_PARAMETER_CODE);
        transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
        transVo.setUserId(user.getUserId());
        transVo.setCreateUser(user.getUserId());
        transVo.setCreateDate(new Date());
        transDao.insertTrans(transVo);

        TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
        hisVo.setCustomerName("系統日程");
        log.info("UsersVo", user);
        hisVo.setUsersVo(user);
        //寫入狀態歷程
        hisVo.setTransNum(transNum);
        hisVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
        onlineChangeDao.addTransStatusHistory(hisVo);

        TransPolicyVo transPolicyVo = new TransPolicyVo();
        transPolicyVo.setTransNum(transNum);
        transPolicyVo.setPolicyNo(vo.getPolicyNo());
        transPolicyDao.insertTransPolicy(transPolicyVo);

        List<TransDepositVo> newDeposits = new Gson().fromJson(vo.getInvtDeposits(), new TypeToken<List<TransDepositVo>>() {
        }.getType());

        if (!CollectionUtils.isEmpty(newDeposits)) {
            for (TransDepositVo e : newDeposits) {
                TransDepositVo transDepositVo = new TransDepositVo();
                BeanUtils.copyProperties(vo, transDepositVo);
                transDepositVo.setTransNum(transNum);
                transDepositVo.setInvtNo(e.getInvtNo());
                transDepositVo.setRatio(e.getRatio());
                transDepositVo.setAmount(e.getAmount());
                transDepositVo.setCurrency(e.getCurrency());
                transDepositDao.insert(transDepositVo);
            }
        } else {
            vo.setTransNum(transNum);
            transDepositDao.insert(vo);
        }
            result = 1;
        } catch (Exception e) {
            result = 0;
            log.error("Unable to init from addNewDepositApply: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return result;
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
    public TransDepositDetailVo getAppliedTransDeposits(String transNum) {
        return transDepositDao.getAppliedTransDeposits(transNum);
    }

    @Override
    public void distributionDepositFund(String rocId, TransDepositVo vo) throws Exception {

        DepositPolicyListVo depositPolicy = (DepositPolicyListVo) getDepositPolicy(rocId, vo.getPolicyNo());
        BigDecimal eachMinValue = BigDecimal.valueOf(0);
        BigDecimal surplusValue = BigDecimal.valueOf(0);
        BigDecimal amount = BigDecimal.valueOf(vo.getAmount());
        String stopAccount = "";
        String errMsg = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "DISTRIBUTION_DEPOSIT_FUND_ERR_MSG");
        List<ParameterVo> parameterVos = getDepositConfigs();
        if (!CollectionUtils.isEmpty(parameterVos)) {
            for (ParameterVo parameterVo : parameterVos) {
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_MIN_VALUE")) {
                    eachMinValue = new BigDecimal(parameterVo.getParameterValue());
                }

                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_STOP_ACCOUNT")) {
                    stopAccount = parameterVo.getParameterValue();
                }

                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_SURPLUS_VALUE")) {
                    surplusValue = new BigDecimal(parameterVo.getParameterValue());
                }
            }
        }

        List<TransDepositVo> newDeposits = Lists.newArrayList();
        if (depositPolicy != null && !CollectionUtils.isEmpty(depositPolicy.getPortfolioVoList())) {
            //1.分配含有保單中有分配比例的基金
            BigDecimal ratioAmount = BigDecimal.valueOf(0);
            BigDecimal notStopAccountAmount = BigDecimal.valueOf(0);
            Map<String, BigDecimal> ratioMap = Maps.newHashMap();
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null && portfolioVo.getAcctValue().intValue() > 0) {
                        BigDecimal ratio = transInvestmentService.getDistributeRationByInvtNo(depositPolicy.getPolicyNo(), portfolioVo.getInvtNo());
                        if (ratio != null) {
                        ratioMap.put(portfolioVo.getPolicyNo() + "_" + portfolioVo.getInvtNo(), ratio);
                        BigDecimal fundValue = amount.multiply(ratio).divide(BigDecimal.valueOf(100), 18, BigDecimal.ROUND_DOWN);
                            if (fundValue.compareTo(eachMinValue) < 0) {
                                throw new Exception(errMsg);
                            }
                            if (fundValue.compareTo(portfolioVo.getAcctValue()) > 0) {
                                throw new Exception(errMsg);
                            }
                            TransDepositVo transDepositVo = new TransDepositVo();
                            transDepositVo.setInvtNo(portfolioVo.getInvtNo());
                            transDepositVo.setRatio(fundValue.multiply(BigDecimal.valueOf(100)).divide(portfolioVo.getAcctValue(), 4, BigDecimal.ROUND_DOWN).doubleValue());
                            transDepositVo.setAmount(fundValue.doubleValue());
                            transDepositVo.setCurrency(portfolioVo.getInvtExchCurr());
                            newDeposits.add(transDepositVo);
                            ratioAmount = ratioAmount.add(fundValue);
                        }
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        notStopAccountAmount = notStopAccountAmount.add(portfolioVo.getAcctValue());
                    }
                }
            }

            //2.计算剩余基金总额
            BigDecimal remainingAllAmount = BigDecimal.valueOf(0);
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (portfolioVo.getAcctValue() != null && portfolioVo.getAcctValue().intValue() > 0
                        && !ratioMap.containsKey(portfolioVo.getPolicyNo() + "_" + portfolioVo.getInvtNo())) {
                    remainingAllAmount = remainingAllAmount.add(portfolioVo.getAcctValue());
                }
            }
            amount = amount.subtract(ratioAmount);

            //3.分配无分配比例的基金
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null && portfolioVo.getAcctValue().intValue() > 0) {
                    if (ratioMap.containsKey(portfolioVo.getPolicyNo() + "_" + portfolioVo.getInvtNo())) {
                                continue;
                            }
                    BigDecimal fundValue = BigDecimal.valueOf(0);
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        if (portfolioVo.getAcctValue().compareTo(eachMinValue) < 0) {
                            throw new Exception(errMsg);
                    }
                }

                    fundValue = amount.multiply(portfolioVo.getAcctValue().divide(remainingAllAmount, 18, BigDecimal.ROUND_DOWN));
                    if (fundValue.compareTo(eachMinValue) < 0) {
                        throw new Exception(errMsg);
            }
                    if (fundValue.compareTo(portfolioVo.getAcctValue()) > 0) {
                        throw new Exception(errMsg);
                        }

                        TransDepositVo transDepositVo = new TransDepositVo();
                        transDepositVo.setInvtNo(portfolioVo.getInvtNo());
                        transDepositVo.setRatio(fundValue.multiply(BigDecimal.valueOf(100)).divide(portfolioVo.getAcctValue(), 4, BigDecimal.ROUND_DOWN).doubleValue());
                        transDepositVo.setAmount(fundValue.doubleValue());
                        transDepositVo.setCurrency(portfolioVo.getInvtExchCurr());
                        newDeposits.add(transDepositVo);
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        ratioAmount = ratioAmount.add(fundValue);
                    }
                }
            }

            log.info("distribute fund is: {}", newDeposits);
            if (notStopAccountAmount.subtract(ratioAmount).compareTo(surplusValue) < 0) {
                throw new Exception(errMsg);
            }
            vo.setInvtDeposits(new Gson().toJson(newDeposits));
        }
    }
}
