package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private static final String DEPOST_PARAMETER_CATEGORY_CODE = "DEPOST_PARAMETER_CATEGORY";

    public List<ParameterVo> getDepositConfigs(String policyNo) {
        return parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, DEPOST_PARAMETER_CATEGORY_CODE);
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
    public void addNewDepositApply(TransDepositVo vo, UsersVo user) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("transNum", null);
        transDao.getTransNum(params);
        String transNum = params.get("transNum").toString();

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
    }

    @Override
    public TransDepositDetailVo getAppliedTransDeposits(String transNum) {
        return transDepositDao.getAppliedTransDeposits(transNum);
    }

    @Override
    public void distributionDepositFund(String rocId, TransDepositVo vo) {

        DepositPolicyListVo depositPolicy = (DepositPolicyListVo) getDepositPolicy(rocId, vo.getPolicyNo());
        BigDecimal eachMinValue = BigDecimal.valueOf(0);
        BigDecimal eachSurplusValue = BigDecimal.valueOf(0);
        BigDecimal amount = BigDecimal.valueOf(vo.getAmount());
        String stopAccount = "";
        List<ParameterVo> parameterVos = getDepositConfigs(depositPolicy.getPolicyNo());
        if (!CollectionUtils.isEmpty(parameterVos)) {
            for (ParameterVo parameterVo : parameterVos) {
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_MIN_VALUE")) {
                    eachMinValue = new BigDecimal(parameterVo.getParameterValue());
                }
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_SURPLUS_VALUE")) {
                    eachSurplusValue = new BigDecimal(parameterVo.getParameterValue());
                }
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_STOP_ACCOUNT")) {
                    stopAccount = parameterVo.getParameterValue();
                }
            }
        }

        List<TransDepositVo> newDeposits = Lists.newArrayList();
        if (depositPolicy != null && !CollectionUtils.isEmpty(depositPolicy.getPortfolioVoList())) {
            //1.每個基金先減去固定最小金額
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null) {
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        BigDecimal ratio = transInvestmentService.getDistributeRationByInvtNo(depositPolicy.getPolicyNo(), portfolioVo.getInvtNo());
                        if (ratio == null && portfolioVo.getAcctValue().subtract(eachMinValue).compareTo(eachSurplusValue) > 0) {
                            amount = amount.subtract(eachMinValue);
                        }
                    }
                }
            }
            //2.分配含有保單中有分配比例的基金
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null) {
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        BigDecimal ratio = transInvestmentService.getDistributeRationByInvtNo(depositPolicy.getPolicyNo(), portfolioVo.getInvtNo());
                        if (ratio != null) {
                            BigDecimal fundValue = amount.multiply(ratio).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_DOWN);
                            BigDecimal surplusValue = portfolioVo.getAcctValue().subtract(eachSurplusValue);
                            if (fundValue.compareTo(eachMinValue) < 0) {
                                continue;
                            }
                            if (fundValue.compareTo(surplusValue) > 0) {
                                fundValue = surplusValue;
                            }
                            TransDepositVo transDepositVo = new TransDepositVo();
                            transDepositVo.setInvtNo(portfolioVo.getInvtNo());
                            transDepositVo.setRatio(fundValue.multiply(BigDecimal.valueOf(100)).divide(portfolioVo.getAcctValue(), 4, BigDecimal.ROUND_DOWN).doubleValue());
                            transDepositVo.setAmount(fundValue.doubleValue());
                            transDepositVo.setCurrency(portfolioVo.getInvtExchCurr());
                            newDeposits.add(transDepositVo);
                            amount = amount.subtract(fundValue);
                        }
                    }
                }
            }
            //3.分配含无保單中有分配比例的基金
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null) {
                    if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                        BigDecimal ratio = transInvestmentService.getDistributeRationByInvtNo(depositPolicy.getPolicyNo(), portfolioVo.getInvtNo());
                        if (ratio == null) {
                            BigDecimal tmpMinValue = portfolioVo.getAcctValue().subtract(eachSurplusValue);
                            if (tmpMinValue.compareTo(eachMinValue) < 0) {
                                continue;
                            }
                            BigDecimal tmpAmount = amount.add(eachMinValue);
                            BigDecimal fundValue = tmpAmount.compareTo(tmpMinValue) > 0 ? tmpMinValue : tmpAmount;
                            TransDepositVo transDepositVo = new TransDepositVo();
                            transDepositVo.setInvtNo(portfolioVo.getInvtNo());
                            transDepositVo.setRatio(fundValue.multiply(BigDecimal.valueOf(100)).divide(portfolioVo.getAcctValue(), 4, BigDecimal.ROUND_DOWN).doubleValue());
                            transDepositVo.setAmount(fundValue.doubleValue());
                            transDepositVo.setCurrency(portfolioVo.getInvtExchCurr());
                            newDeposits.add(transDepositVo);
                            amount = amount.subtract(fundValue);
                        }
                    }
                }
            }
            //4.最後分配停泊賬戶提領基金
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (amount.doubleValue() >= 0 && portfolioVo.getAcctValue() != null) {
                    if (stopAccount.contains(portfolioVo.getInvtNo())) {
                        BigDecimal fundValue = amount;
                        if (fundValue.compareTo(amount) > 0) {
                            fundValue = amount;
                        }
                        TransDepositVo transDepositVo = new TransDepositVo();
                        transDepositVo.setInvtNo(portfolioVo.getInvtNo());
                        transDepositVo.setRatio(fundValue.multiply(BigDecimal.valueOf(100)).divide(portfolioVo.getAcctValue(), 4, BigDecimal.ROUND_DOWN).doubleValue());
                        transDepositVo.setAmount(fundValue.doubleValue());
                        transDepositVo.setCurrency(portfolioVo.getInvtExchCurr());
                        newDeposits.add(transDepositVo);
                        amount = amount.subtract(fundValue);
                    }
                }
            }
            vo.setInvtDeposits(new Gson().toJson(newDeposits));
        }
    }
}
