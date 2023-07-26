package com.twfhclife.eservice.api.shouxian.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.twfhclife.eservice.api.elife.domain.PortfolioResponse;
import com.twfhclife.eservice.api.shouxian.dao.ShouXianDao;
import com.twfhclife.eservice.api.shouxian.domain.ExchangeRateRequest;
import com.twfhclife.eservice.api.shouxian.domain.PolicyTypeListResponse;
import com.twfhclife.eservice.api.shouxian.model.JdFundTransactionVo;
import com.twfhclife.eservice.api.shouxian.model.*;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.generic.util.RoiRateUtil;
import com.twfhclife.generic.utils.DateUtil;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ShouxianService {

    private static Logger logger = LoggerFactory.getLogger(ShouxianService.class);

    @Autowired
    private ShouXianDao shouXianDao;

    public List<PolicyVo> getPolicyList(PolicyVo vo) {
        return shouXianDao.getPolicyList(vo);
    }

    public PolicyCountVo getPolicyListTotal(PolicyVo vo) {
        return shouXianDao.getPolicyListTotal(vo);
    }

    public PolicyBaseVo getPolicyBase(String policyNo) {
        if (StringUtils.isNotBlank(policyNo) && policyNo.length() > 3) {
            return shouXianDao.getBasePolicy(policyNo, policyNo.substring(0, 2), policyNo.substring(2, 3), policyNo.substring(3, policyNo.length()));
        } else {
            return new PolicyBaseVo();
        }
    }

    public List<SafeGuardVo> getSafeGuard(String policyNo) {
        return shouXianDao.getSafeGuard(policyNo);
    }

    public List<PaymentRecordVo> getPaymentRecord(String policyNo) {
        return shouXianDao.getPaymentRecord(policyNo);
    }

    public List<PremiumVo> getPolicyPremium(String policyNo) {
        return shouXianDao.getPolicyPremium(policyNo);
    }

    public List<ExpireOfPaymentVo> getExpireOfPayment(String policyNo) {
        return shouXianDao.getExpireOfPayment(policyNo);
    }

    public List<ChangeInfoVo> getPolicyChangeInfo(String policyNo) {
        return shouXianDao.getPolicyChangeInfo(policyNo);
    }

    public List<IncomeDistributionVo> getPolicyIncomeDistribution(String policyNo) {
       return shouXianDao.getIncomeDistribution(policyNo);
    }

    public List<JdFundTransactionVo> getFundTransactionPageList(String policyNo, String transType, String startDate, String endDate, Integer pageNum, Integer pageSize) {
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
        vo.setPolicy(getPolicyBase(policyNo));
        return vo;
    }

    private BigDecimal[] getZero() {
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal[] values = {zero, zero, zero};
        return values;
    }

    /** 公式1: FD 基金: {[((單位數*單位淨值*匯率) + 累積投資收益)/(平均台幣買價*總單位數)] – 1}% */
    private BigDecimal[] formula1(BigDecimal netUnits, BigDecimal netValue, BigDecimal exchRate, BigDecimal ntdVal, BigDecimal accumAmt) {
        BigDecimal[] values = new BigDecimal[3];
        if (netUnits != null && netValue != null && exchRate != null && ntdVal != null && accumAmt != null) {
            values = RoiRateUtil.formula1(netUnits, netValue, exchRate, ntdVal, accumAmt);
        } else {
            values = getZero();
        }
        return values;
    }

    /** 公式2: RT 貨幣帳戶: {[(帳戶金額*匯率)/平均台幣買價]-1}% */
    private BigDecimal[] formula2(BigDecimal netAmt, BigDecimal exchRate, BigDecimal ntdVal) {
        BigDecimal[] values = new BigDecimal[3];
        if (netAmt != null && exchRate != null && ntdVal != null) {
            values = RoiRateUtil.formula2(netAmt, exchRate, ntdVal);
        } else {
            values = this.getZero();
        }
        return values;
    }

    @Value("${csp.api.provide.esrv-fsz2.url}")
    private String url;

    @Value("${csp.api.provide.esjd-usz3.url}")
    private String usz3Url;

    public List<CancellationMoneyVo> getPolicyCancellationMoney(String policyNo) throws Exception {
        initRestTemplate();
        String strResponse = postForEntity(policyNo);
        return new Gson().fromJson(strResponse, new TypeReference<List<CancellationMoneyVo>>(){}.getType());
    }

    public List<CancellationMoneyVo> getInvestPolicyCancellationMoney(String policyNo) throws Exception {
        initRestTemplate();
        String strResponse = postInvestForEntity(policyNo);
        return new Gson().fromJson(strResponse, new TypeReference<List<InvetCancellationMoneyVo>>(){}.getType());
    }

    private String postInvestForEntity(String policyNo) throws Exception {
        String strRes = null;
        Map<String, Object> map = Maps.newHashMap();
        map.put("USZ3-SCN-NAME", "MSZ1");
        map.put("USZ3-FUNC-CODE", "IN");
        map.put("USZ3-INSU-NO-Q", policyNo);
        map.put("USZ3-CALC-DATE", DateUtil.getFourYearRocDate());
        map.put("USZ3-CALC-TYPE", 1);
        logger.info("投資型保單帳戶價值及解約金API request: {}", map);
        ResponseEntity<String> responseEntity = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        responseEntity = restTemplate.postForEntity(usz3Url, entity, String.class);

        strRes = responseEntity.getBody();
        boolean checkRes = this.checkResponseStatus(responseEntity);
        if (checkRes) {
            logger.info("投資型保單帳戶價值及解約金API-RESP:{}", strRes);
            return MyJacksonUtil.readValueString(strRes, "/data/values");
        }
        return "";
    }

    @Value("${csp.api.provide.esrv-fsz2.token}")
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
        map.put("FSZ2-CALC-DATE", DateUtil.getFourYearRocDate());
        map.put("FSZ2-INSU-NO", policyNo);
        map.put("FSZ2-CALC-TYPE", 3);
        logger.info("解約金API request: {}", map);
        ResponseEntity<String> responseEntity = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        responseEntity = restTemplate.postForEntity(url, entity, String.class);

        strRes = responseEntity.getBody();
        boolean checkRes = this.checkResponseStatus(responseEntity);
        if (checkRes) {
            logger.info("解約金API-RESP:{}", strRes);
            return MyJacksonUtil.readValueString(strRes, "/data/values");
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

    public PortfolioResponse getPortfolioResp(String policyNo) {
        PortfolioResponse resp = new PortfolioResponse();
        List<PortfolioVo> portfolioList = shouXianDao.getPortfolioList(policyNo);
        Date maxDate = null;
        Iterator var4 = portfolioList.iterator();
        while (var4.hasNext()) {
            PortfolioVo portfolioVo = (PortfolioVo) var4.next();
            BigDecimal netAmt = portfolioVo.getSafpNetAmt(); // 目前金額
            BigDecimal netUnits = portfolioVo.getSafpNetUnits(); // 目前單位數
            BigDecimal ntdVal = portfolioVo.getSafpAvgPntdval(); // 平均台幣買價
            BigDecimal netValue = portfolioVo.getNetValueSell(); // 淨值
            BigDecimal exchRate = portfolioVo.getExchRateBuy(); // 匯率
            BigDecimal expeNtd = portfolioVo.getClupExpeNtdSum(); // 累計投資收益(不一定是台幣)

            BigDecimal[] values = new BigDecimal[3];
            BigDecimal roiRate; // 參考報酬率(%)
            BigDecimal acctValue; // 帳戶價值
            BigDecimal avgPval; // 平均成本

            // 台幣時匯率為1
            if ("NTD".equals(portfolioVo.getInvtExchCurr())) {
                exchRate = BigDecimal.valueOf(1);
            }

            try {
                if (portfolioVo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
                    values = this.formula2(netAmt, exchRate, ntdVal);
                    portfolioVo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
                } else {
                    // {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
                    values = this.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
                    if (values[2] != null && values[1] != null) {
                        values[0] = values[1].subtract(values[2]).divide(values[2], 4, BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100));
                    }
                }

                // 從Array指定變數
                roiRate = values[0];
                acctValue = values[1];
                avgPval = values[2];

            } catch (Exception ex) {
                logger.error("policyNo:" + portfolioVo.getPolicyNo() + "invtNo :" + portfolioVo.getInvtNo());
                roiRate = new BigDecimal(0);
                acctValue = new BigDecimal(0);
                avgPval = new BigDecimal(0);
            }

            // 放入vo
            portfolioVo.setRoiRate(roiRate);
            portfolioVo.setAcctValue(acctValue);
            portfolioVo.setAvgPval(avgPval);

            if (maxDate == null && portfolioVo.getNetValueDate() != null) {
                maxDate = portfolioVo.getNetValueDate();
            } else if (maxDate != null && portfolioVo.getNetValueDate() != null
                && portfolioVo.getNetValueDate().getTime() > maxDate.getTime()) {
                maxDate = portfolioVo.getNetValueDate();
            }
        }
        if (maxDate != null && maxDate.getTime() > new Date().getTime()) {
            maxDate = new Date();
        }
        resp.setPortfolioList(portfolioList);
        if (maxDate != null) {
            resp.setEndDate(DateUtil.westToTwDate(new SimpleDateFormat("yyyy-MM-dd").format(maxDate)));
        }
        return resp;
    }

    public PolicyAmountVo getPolicyAmount(String policyNo) {
        PolicyAmountVo policyAmountVo = shouXianDao.selectPolicyAmount(policyNo);
        if (policyAmountVo == null) {
            policyAmountVo = new PolicyAmountVo();
        }
        policyAmountVo.setPolicyDate(DateUtil.getRocDate(new Date()));
        return policyAmountVo;

    }


    public List<Map<String, String>> getPolicyTypeList() {
        return shouXianDao.selectPolicyTypeList();
    }

}
