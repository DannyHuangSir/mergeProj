package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.*;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.util.ApConstants;
import com.twfhclife.eservice.web.dao.JdNotifyConfigDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PolicyServiceImpl implements IPolicyService {

    @Value("${eservice_api.policy.list.url}")
    private String policyListUrl;
    @Autowired
    private BaseRestClient baseRestClient;

    @Autowired
    private UsersDao usersDao;


    @Override
    public PolicyListDataResponse getPolicyList(KeycloakUser user, PolicyVo vo) {
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員
        int role = usersDao.checkUserRole(user.getId());
        List<PermQueryVo> caseQuery = Lists.newArrayList();
        switch (role) {
            case 2:
                caseQuery.addAll(usersDao.getCaseQueryBySupervisor(user.getId()));
                break;
            case 3:
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId()));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId()));
                break;
            case 5:
                break;
            default:
                caseQuery.addAll(usersDao.getCaseQueryByUser(user.getId()));
                break;
        }
        if (!CollectionUtils.isEmpty(caseQuery) || role == 5) {
            vo.setPermQuery(caseQuery);
            vo.setUserId(user.getUsername());
            vo.setSysId(ApConstants.SYSTEM_ID);
            return baseRestClient.postApi(new Gson().toJson(vo), policyListUrl, PolicyListDataResponse.class);
        }
        return new PolicyListDataResponse();
    }

    @Value("${eservice_api.policy.base.url}")
    private String policyBaseUrl;

    @Override
    public PolicyBaseVo getPolicyBase(String userId, String policyNo) {
        PolicyBaseDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyBaseUrl, PolicyBaseDataResponse.class);
        return responseObj.getPolicy();
    }

    @Value("${eservice_api.policy.safe.guard.url}")
    private String policySafeGuardUrl;

    @Override
    public PolicySafeGuardDataResponse getPolicyGuard(String userId, String policyNo) {
        PolicySafeGuardDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policySafeGuardUrl, PolicySafeGuardDataResponse.class);
        return responseObj;
    }

    @Value("${eservice_api.policy.payment.record.url}")
    private String policyPaymentRecordUrl;

    @Override
    public PolicyPaymentRecordDataResponse getPolicyPaymentRecord(String userId, String policyNo) {
        return baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyPaymentRecordUrl, PolicyPaymentRecordDataResponse.class);
    }

    @Value("${eservice_api.policy.premium.url}")
    private String policyPremiumUrl;

    @Override
    public PolicyPremiumDataResponse getPolicyPremium(String userId, String policyNo) {
        return baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyPremiumUrl, PolicyPremiumDataResponse.class);
    }

    @Value("${eservice_api.policy.expire.payment.url}")
    private String policyExpireOfPaymentUrl;

    @Override
    public PolicyExpireOfPaymentDataResponse getPolicyExpireOfPayment(String userId, String policyNo) {
        PolicyExpireOfPaymentDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyExpireOfPaymentUrl, PolicyExpireOfPaymentDataResponse.class);
        return responseObj;
    }

    @Value("${eservice_api.policy.change.info.url}")
    private String policyChangeInfoUrl;

    @Override
    public PolicyChangeInfoDataResponse getChangeInfo(String userId, String policyNo) {
        return baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyChangeInfoUrl, PolicyChangeInfoDataResponse.class);
    }

    @Value("${eservice_api.policy.income.distribution.url}")
    private String policyIncomeDistributionUrl;

    @Override
    public PolicyIncomeDistributionDataResponse getIncomeDistribution(String userId, String policyNo) {
        return baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyIncomeDistributionUrl, PolicyIncomeDistributionDataResponse.class);
    }

    @Value("${eservice_api.policy.transation.history.url}")
    private String transationHistroyUrl;

    @Override
    public JdPolicyFundTransactionResponse getPolicyFundTransaction(String userId, String policyNo, String transType, String startDate, String endDate, int pageNum, int pageSize) {
        PolicyFundTransactionRequest apiReq = new PolicyFundTransactionRequest();
        apiReq.setTransType(transType);
        apiReq.setPolicyNo(policyNo);
        apiReq.setTransType(transType);
        apiReq.setStartDate(startDate);
        apiReq.setEndDate(endDate);
        apiReq.setPageNum(pageNum);
        apiReq.setPageSize(pageSize);
        apiReq.setSysId(ApConstants.SYSTEM_ID);
        apiReq.setUserId(userId);
        return baseRestClient.postApi(new Gson().toJson(apiReq), transationHistroyUrl, JdPolicyFundTransactionResponse.class);
    }

    @Value("${eservice_api.policy.premium.cost.url}")
    private String premiumCostUrl;

    @Override
    public PolicyPremiumCostResponse getPolicyPremiumTransaction(String userId, String policyNo, String startDate, String endDate, int pageNum, int pageSize) {
        PolicyPremiumTransactionRequest apiReq = new PolicyPremiumTransactionRequest();
        apiReq.setSysId(ApConstants.SYSTEM_ID);
        apiReq.setUserId(userId);
        apiReq.setPolicyNo(policyNo);
        apiReq.setStartDate(startDate);
        apiReq.setEndDate(endDate);
        apiReq.setPageNum(pageNum);
        apiReq.setPageSize(pageSize);
        return baseRestClient.postApi(new Gson().toJson(apiReq), premiumCostUrl, PolicyPremiumCostResponse.class);
    }

    @Value("${eservice_api.policy.invt.fund.url}")
    private String policyInvtFundUrl;

    @Override
    public PolicyInvtFundVo getPolicyInvtFund(String userId, String policyNo) {
        PolicyInvtFundDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), policyInvtFundUrl, PolicyInvtFundDataResponse.class);
        return responseObj.getInvtFund();
    }

    @Value("${eservice_api.policy.notify.portfolio.url}")
    private String notifyPortfolioUrl;


    @Override
    public PortfolioResponse getPolicyNotifyPortfolio(String userId, String policyNo) {
        PortfolioRequest apiReq = new PortfolioRequest();
        apiReq.setSysId(ApConstants.SYSTEM_ID_JD);
        apiReq.setUserId(userId);
        apiReq.setPolicyNo(policyNo);
        return baseRestClient.postApi(new Gson().toJson(apiReq), notifyPortfolioUrl, PortfolioResponse.class);
    }

    @Value("${eservice_api.policy.portfolio.new.url}")
    private String portfolioNewUrl;


    @Override
    public PortfolioResponse getPolicyRateOfReturn(String userId, String policyNo) {
        PortfolioRequest apiReq = new PortfolioRequest();
        apiReq.setSysId(ApConstants.SYSTEM_ID_JD);
        apiReq.setUserId(userId);
        apiReq.setPolicyNo(policyNo);
        return baseRestClient.postApi(new Gson().toJson(apiReq), portfolioNewUrl, PortfolioResponse.class);
    }

    @Value("${eservice_api.policy.cancel.money.url}")
    private String cancelMoneyUrl;

    @Override
    public PolicyCancellationMoneyDataResponse getPolicyCancellationMoney(String userId, String policyNo) {
        PolicyCancellationMoneyDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), cancelMoneyUrl, PolicyCancellationMoneyDataResponse.class);
        return responseObj;
    }

    @Autowired
    private JdNotifyConfigDao jdNotifyConfigDao;

    @Override
    public int updateNotifyConfig(List<NotifyConfigVo> confs) {
        return jdNotifyConfigDao.updateNotifyConfig(confs);
    }

    @Override
    public NotifyConfigVo getNotifyConfig(String userId, String policyNo, String invtNo) {
        return jdNotifyConfigDao.getNotifyConfig(userId, policyNo, invtNo);
    }

    @Value("${eservice_api.policy.exchange.rate.url}")
    private String exchangeRateUrl;

    @Override
    public List<ExchangeRateVo> getExchangeRate(String userId, ExchangeRateVo exchangeRateVo) {
        ExchangeRateRequest apiReq = new ExchangeRateRequest();
        apiReq.setSysId(ApConstants.SYSTEM_ID_JD);
        apiReq.setUserId(userId);
        apiReq.setPolicyNo(exchangeRateVo.getPolicyNo());
        apiReq.setQueryMonth(exchangeRateVo.getQueryMonth());
        apiReq.setExchangeCode(exchangeRateVo.getExchangeCode());
        apiReq.setQueryType(exchangeRateVo.getQueryType());
        return baseRestClient.postApi(new Gson().toJson(apiReq), exchangeRateUrl, ExchangeRateDataResponse.class).getExchangeRates();
    }

    @Value("${eservice_api.policy.portfolio.schedule.url}")
    private String portfolioScheduleUrl;

    @Override
    public List<PortfolioVo> getPolicyRateSchedule(String policyNo) {
        return baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), portfolioScheduleUrl, PortfolioResponse.class).getPortfolioList();
    }
}
