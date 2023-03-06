package com.twfhclife.eservice.api.shouxian.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.twfhclife.eservice.api.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.api.shouxian.domain.ExchangeRateRequest;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.FundTransactionVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.generic.util.RoiRateUtil;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ShouxianService {

    @Autowired
    private ShouXianDao shouXianDao;

    public List<PolicyVo> getPolicyList(PolicyVo vo) {
        return shouXianDao.getPolicyList(vo);
    }

    public PolicyBaseVo getPolicyBase(String policyNo) {
        return shouXianDao.getBasePolicy(policyNo);
    }

    public PolicySafeGuardVo getSafeGuard(String policyNo) {
        return shouXianDao.getSafeGuard(policyNo);
    }

    public PolicyPaymentRecordVo getPaymentRecord(String policyNo) {
        return shouXianDao.getPaymentRecord(policyNo);
    }

    public PolicyPremiumVo getPolicyPremium(String policyNo) {
        return shouXianDao.getPolicyPremium(policyNo);
    }

    public PolicyExpireOfPaymentVo getExpireOfPayment(String policyNo) {
        return shouXianDao.getExpireOfPayment(policyNo);
    }

    public PolicyChangeInfoVo getPolicyChangeInfo(String policyNo) {
        return shouXianDao.getPolicyChangeInfo(policyNo);
    }

    public PolicyIncomeDistributionVo getPolicyIncomeDistribution(String policyNo) {
        PolicyIncomeDistributionVo vo = new PolicyIncomeDistributionVo();
        vo.setPolicy(shouXianDao.getPolicyInfo(policyNo));
        vo.setIncomeDistributions(shouXianDao.getIncomeDistribution(policyNo));
        return vo;
    }

    public List<FundTransactionVo> getFundTransactionPageList(String policyNo, String transType, String startDate, String endDate, Integer pageNum, Integer pageSize) {
        return shouXianDao.getFundTransactionPageList(policyNo, transType, startDate, endDate, pageNum, pageSize);
    }

    public int getFundTransactionTotal(String policyNo, String transType, String startDate, String endDate) {
        return shouXianDao.getFundTransactionTotal(policyNo, transType, startDate, endDate);
    }

    public List<FundPrdtVo> getFundPrdtPageList(String policyNo, String startDate, String endDate, Integer pageNum, Integer pageSize) {
        return shouXianDao.getFundPrdtPageList(policyNo, startDate, endDate, pageNum, pageSize);
    }

    public int getFundPrdtTotal(String policyNo, String startDate, String endDate) {
        return shouXianDao.getFundPrdtTotal(policyNo, startDate, endDate);
    }

    public PolicyInvtFundVo getPolicyInvtFund(String policyNo) {
        PolicyInvtFundVo vo = new PolicyInvtFundVo();
        vo.setPolicy(shouXianDao.getPolicyInfo(policyNo));
        return vo;
    }

    public List<PortfolioVo> getPortfolioList(String policyNo) {
        List<PortfolioVo> portfolioList = shouXianDao.getPortfolioList(policyNo);
        Iterator var4 = portfolioList.iterator();

        while (var4.hasNext()) {
            PortfolioVo portfolioVo = (PortfolioVo) var4.next();
            BigDecimal netAmt = portfolioVo.getSafpNetAmt();
            BigDecimal netUnits = portfolioVo.getSafpNetUnits();
            BigDecimal ntdVal = portfolioVo.getSafpAvgPntdval();
            BigDecimal netValue = portfolioVo.getNetValueSell();
            BigDecimal exchRate = portfolioVo.getExchRateBuy();
            BigDecimal expeNtd = portfolioVo.getClupExpeNtdSum();
            BigDecimal[] values = new BigDecimal[3];
            if ("NTD".equals(portfolioVo.getInvtExchCurr())) {
                exchRate = BigDecimal.valueOf(1L);
            }

            BigDecimal roiRate;
            BigDecimal acctValue;
            BigDecimal avgPval;
            try {
                if (portfolioVo.getInvtNo().startsWith("RT")) {
                    values = this.formula2(netAmt, exchRate, ntdVal);
                    portfolioVo.setSafpNetUnits(netAmt);
                } else {
                    values = this.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
                }

                roiRate = values[0];
                acctValue = values[1];
                avgPval = values[2];
            } catch (Exception var17) {
                roiRate = new BigDecimal(0);
                acctValue = new BigDecimal(0);
                avgPval = new BigDecimal(0);
            }

            portfolioVo.setRoiRate(roiRate);
            portfolioVo.setAcctValue(acctValue);
            portfolioVo.setAvgPval(avgPval);
        }
        return portfolioList;
    }

    private BigDecimal[] getZero() {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal[] values = {zero, zero, zero};
        return values;
    }

    /**
     * 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}%
     */
    private BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
        BigDecimal[] values = new BigDecimal[3];
        if (netUnits != null && netValue != null && exchRate != null && ntdVal != null && accumAmt != null) {
            values = RoiRateUtil.formula1(netUnits, netValue, exchRate, ntdVal, accumAmt);
        } else {
            values = this.getZero();
        }
        return values;
    }

    /**
     * 公式2: RT 貨幣帳戶: {[(帳戶金額*匯率)/平均台幣買價]-1}%
     */
    private BigDecimal[] formula2(BigDecimal netAmt, BigDecimal exchRate, BigDecimal ntdVal) {
        BigDecimal[] values = new BigDecimal[3];
        if (netAmt != null && exchRate != null && ntdVal != null) {
            values = RoiRateUtil.formula2(netAmt, exchRate, ntdVal);
        } else {
            values = this.getZero();
        }
        return values;
    }

    @Value("${csp.api.provide.esrv-fsz2.token}")
    private String url;
    public List<CancellationMoneyVo> getPolicyCancellationMoney(String policyNo) throws Exception {
//        initRestTemplate();
//        postForEntity(policyNo);
        String strResponse = "[{\"FSZ2-SCN-NAME\": \"FSZ2\", \"FSZ2-FUNC-CODE\": \"IN\", \"FSZ2-INSU-NO\": \"BR10000075\", \"FSZ2-CALC-DATE\": \"01111104\", \"FSZ2-CALC-TYPE\": \"2\", \"FSZ2-AMT\": \"148500\", \"FSZ2-EFFECTIVE-DATE\": \"00000000\"} ]";
        return new Gson().fromJson(strResponse, new TypeReference<List<CancellationMoneyVo>>(){}.getType());
    }

    public PolicyVo getPolicyInfo(String policyNo) {
        return shouXianDao.getPolicyInfo(policyNo);
    }

    @Value("${csp.api.provide.esrv-fsz2.url}")
    public String ACCESS_TOKEN;

    private RestTemplate restTemplate;

    private void initRestTemplate() {
        restTemplate = new RestTemplate();//force init.

        int milliseconds = 20 * 1000;
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(milliseconds);
        httpRequestFactory.setConnectTimeout(milliseconds);
        httpRequestFactory.setReadTimeout(milliseconds);
        restTemplate.setRequestFactory(httpRequestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    private String postForEntity(String policyNo) throws Exception {
        String strRes = null;
        Map<String, Object> map = Maps.newHashMap();
        map.put("FSZ2-SCN-NAME", "FSZ2");
        map.put("FSZ2-FUNC-CODE", "IN");
        map.put("FSZ2-CALC-DATE", new Date());
        map.put("FSZ2-INSU-NO", policyNo);
        map.put("FSZ2-CALC-TYPE", 3);

        ResponseEntity<String> responseEntity = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-token", ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        responseEntity = restTemplate.postForEntity(url, entity, String.class);

        strRes = responseEntity.getBody();
        boolean checkRes = this.checkResponseStatus(responseEntity);
        if (checkRes) {
            return MyJacksonUtil.getNodeString(strRes, "data.valuse");
        }
        return "";
    }

    public boolean checkResponseStatus(ResponseEntity<?> responseEntity) {
        if (responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
            return true;
        } else {
            return false;
        }
    }

    public List<ExchangeRateVo> getExchangeRate(ExchangeRateRequest vo) {
        return shouXianDao.getExchangeRate(vo);
    }
}
