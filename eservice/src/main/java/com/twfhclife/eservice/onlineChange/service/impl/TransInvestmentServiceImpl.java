package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.TransFundConversionVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.CompareInvestmentVo;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.util.FormulaUtil;
import com.twfhclife.eservice.web.dao.OptionDao;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.twfhclife.eservice.onlineChange.util.TransTypeUtil.*;

@Service
public class TransInvestmentServiceImpl implements ITransInvestmentService {

    private static final Logger log = LogManager.getLogger(TransInvestmentServiceImpl.class);

    @Autowired
    private ITransRiskLevelService riskLevelService;

    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    private TransDao transDao;

    @Autowired
    private OnlineChangeDao onlineChangeDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Autowired
    private TransInvestmentDao transInvestmentDao;

    @Autowired
    private   TransFundConversionDao  transFundConversionDao;

    @Autowired
    private OptionDao optionDao;
    @Autowired
    private IParameterService parameterService;


    private static final Map<String, String> MSG_MAP = ImmutableMap.<String, String>builder()
            .put(INVESTMENT_PARAMETER_CODE, "未來保費投資標的與分配比例正在進行中")
            .put(INVESTMENT_CONVERSION_CODE, "已持有投資標的轉換正在進行中")
            .put(DEPOSIT_PARAMETER_CODE, "保單提領(贖回)正在進行中")
            .put(FUND_NOTIFICATION_PARAMETER_CODE, "設定停利停損通知正在進行中")
            .put(CASH_PAYMENT_PARAMETER_CODE, "收益分配或撥回資產分配方式正在進行中")
            .put(RISK_LEVEL_PARAMETER_CODE, "變更風險屬性正在進行中")
            .put(CHANGE_PREMIUM_CODE, "定期超額保費正在進行中")
            .build();

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
                                vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_N_INVESTMENT_MSG);
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
                    e.setApplyLockedFlag("Y");
                    // 此張保單已過投保終期
                    e.setApplyLockedFlag("Y");
                    e.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_EXPIREDATE_MSG);
                    continue;
                }
            }
        }
    }

    @Override
    public List<InvestmentPortfolioVo> getOwnInvestment(String policyNo) {
        List<InvestmentPortfolioVo> investmentList = transInvestmentDao.getOwnInvestment(policyNo, new Date());
        for (InvestmentPortfolioVo vo : investmentList) {
            BigDecimal netAmt = vo.getSafpNetAmt(); // 目前金額
            BigDecimal netUnits = vo.getSafpNetUnits(); // 目前單位數
            BigDecimal ntdVal = vo.getSafpAvgPntdval(); // 平均台幣買價
            BigDecimal netValue = vo.getNetValueSell(); // 淨值
            BigDecimal exchRate = vo.getExchRateBuy(); // 匯率
            BigDecimal expeNtd = vo.getClupExpeNtdSum(); // 累計投資收益(不一定是台幣)

            BigDecimal[] values = new BigDecimal[3];
            BigDecimal roiRate; // 參考報酬率(%)
            BigDecimal acctValue; // 帳戶價值
            BigDecimal avgPval; // 平均成本

            // 台幣時匯率為1
            if ("NTD".equals(vo.getInvtExchCurr())) {
                exchRate = BigDecimal.valueOf(1);
            }

            try {
                if (vo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
                    values = FormulaUtil.formula2(netAmt, exchRate, ntdVal);
                    vo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
                } else {
                    // {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
                    //System.out.println("invtNo=" + vo.getInvtNo());
                    values = FormulaUtil.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
                }
                // 從Array指定變數
                roiRate = values[0];
                acctValue = values[1];
                avgPval = values[2];

            } catch (Exception ex) {
                log.error("policyNo:" + vo.getPolicyNo() + "invtNo :" + vo.getInvtNo());
                roiRate = new BigDecimal(0);
                acctValue = new BigDecimal(0);
                avgPval = new BigDecimal(0);
            }

            // 放入vo
            vo.setRoiRate(roiRate);
            vo.setAcctValue(acctValue);
            vo.setAvgPval(avgPval);
        }
        return investmentList;
    }

    @Override
    public List<InvestmentPortfolioVo> getNewInvestments(String policyNo, List<InvestmentPortfolioVo> portfolioVos, String rocId) {
        List<String> ownInvtNos = null;
        if (!CollectionUtils.isEmpty(portfolioVos)) {
            ownInvtNos = Lists.newArrayList();
            for (InvestmentPortfolioVo e : portfolioVos) {
                ownInvtNos.add(String.valueOf(e.getInvtNo()));
            }
        }
        String riskLevel = riskLevelService.getUserRiskAttr(rocId);
        String listRR = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, "RISK_LEVEL_TO_RR_" + riskLevel);
        List<String> rrs = null;
        if (StringUtils.isNotBlank(listRR)) {
            rrs = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(listRR);
        }
        return transInvestmentDao.getNewInvestments(policyNo, ownInvtNos, rrs);
    }

    @Override
    public  List<List<TransFundConversionVo>>  transInvestmentConversionDetail(String transNum) throws Exception {
        List<TransFundConversionVo> transFundConversionVos =
                transFundConversionDao.transInvestmentConversionDetail(transNum);
        log.info(transFundConversionDao);
        log.info(transFundConversionDao.toString());
        List<List<TransFundConversionVo>> lists = new ArrayList<>();
        ArrayList<TransFundConversionVo> out = new ArrayList<>();
        ArrayList<TransFundConversionVo> in = new ArrayList<>();
        transFundConversionVos.stream().forEach((x) -> {
            if (TransTypeUtil.INVESTMENT_STATUS_OUT.equals(x.getInvestmentType())) {
                TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
                transFundConversionVo.setInvtNo(x.getInvtNo());
                transFundConversionVo.setInvtName(x.getInvtName());
                transFundConversionVo.setRatio(x.getRatio());
                transFundConversionVo.setValue(x.getValue());
                out.add(transFundConversionVo);
            }
            if (TransTypeUtil.INVESTMENT_STATUS_IN.equals(x.getInvestmentType())) {
                TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
                transFundConversionVo.setInvtNo(x.getInvtNo());
                transFundConversionVo.setInvtName(x.getFundName());
                transFundConversionVo.setRatio(x.getRatio());
                //組裝數據
                transFundConversionVo.setBankAccount(x.getBankAccount());
                transFundConversionVo.setBankName(x.getBankName());
                transFundConversionVo.setBranchName(x.getBranchName());
                transFundConversionVo.setSwiftCode(x.getSwiftCode());
                transFundConversionVo.setEnglishName(x.getEnglishName());
                transFundConversionVo.setAccountName(x.getAccountName());
                in.add(transFundConversionVo);
            }
        });
        lists.add(out);
        lists.add(in);
        lists.add(in);
        return lists;
    }

    @Override
    public InvestmentPortfolioVo getInvestmentLimitSize(String policyNo, String policyType) throws Exception {
        String strBuffer = "";
        if(!StringUtils.isEmpty(policyType)){
            strBuffer=policyType;
        }else {
            if(!StringUtils.isEmpty(policyNo)){
                if(policyNo.length()>=2){
                    String substring = policyNo.substring(0,2);
                    strBuffer=substring;
                }
            }
        }
        //獲取保單的比例參數值
        String sizeMax = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, strBuffer + "_MAX");
        if(StringUtils.isEmpty(sizeMax)){
            sizeMax="90";
        }
        String sizeMin = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, strBuffer + "_MIN");
        if(StringUtils.isEmpty(sizeMin)){
            sizeMin="20";
        }
        InvestmentPortfolioVo icnvestmentPortfolioVo = new InvestmentPortfolioVo();
        icnvestmentPortfolioVo.setPolicyNo(policyNo);
        icnvestmentPortfolioVo.setPolicyType(strBuffer);
        icnvestmentPortfolioVo.setRatioMaxSize(Integer.parseInt(sizeMax));
        icnvestmentPortfolioVo.setRatioMark("保單比例值");
        icnvestmentPortfolioVo.setRatioMinSize(Integer.parseInt(sizeMin));
        return icnvestmentPortfolioVo;
    }

    @Override
    public String checkHasApplying(String userId) {
        String value = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.PROCESS_INVESTMENT_LIST);
        if (StringUtils.isNotBlank(value)) {
            List<String> transTypes = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(value);
            return MSG_MAP.get(transInvestmentDao.selectProcessInvestment(userId, transTypes));
        }
        return null;
    }

    @Override
    public void handleNotExistingInvestmentLock(String userRocId, List<PolicyListVo> policyList) throws Exception {
        if (!CollectionUtils.isEmpty(policyList)) {
            for (PolicyListVo e: policyList) {
                String policyNo = e.getPolicyNo();
                if (!StringUtils.isEmpty(policyNo)) {
                    //判斷是否是被保人
                    if (StringUtils.equals(userRocId, e.getRocId())) {
                        //進行判斷保單的類型
                        if ("UC,UH".contains(StringUtils.substring(policyNo, 0, 2))) {
                            List<InvestmentPortfolioVo> ownInvestment = this.getHeldInvestmentTarget(policyNo);
                            if (CollectionUtils.isEmpty(ownInvestment)) {
                                e.setApplyLockedFlag("Y");
                                e.setApplyLockedMsg("該保單未有已投資標則無法申請");
                                continue;
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public List<InvestmentPortfolioVo> getHeldInvestmentTarget(String policyNo) throws Exception {
        List<InvestmentPortfolioVo> investmentList = transInvestmentDao.getHeldInvestmentTarget(policyNo);
        for (InvestmentPortfolioVo vo : investmentList) {
            BigDecimal netAmt = vo.getSafpNetAmt(); // 目前金額
            BigDecimal netUnits = vo.getSafpNetUnits(); // 目前單位數
            BigDecimal ntdVal = vo.getSafpAvgPntdval(); // 平均台幣買價
            BigDecimal netValue = vo.getNetValueSell(); // 淨值
            BigDecimal exchRate = vo.getExchRateBuy(); // 匯率
            BigDecimal expeNtd = vo.getClupExpeNtdSum(); // 累計投資收益(不一定是台幣)

            BigDecimal[] values = new BigDecimal[3];
            BigDecimal roiRate; // 參考報酬率(%)
            BigDecimal acctValue; // 帳戶價值
            BigDecimal avgPval; // 平均成本

            // 台幣時匯率為1
            if ("NTD".equals(vo.getInvtExchCurr())) {
                exchRate = BigDecimal.valueOf(1);
            }

            try {
                if (vo.getInvtNo().startsWith("RT")) { // RT： {[(帳戶金額*匯率)/平均台幣買價]-1}%
                    values = FormulaUtil.formula2(netAmt, exchRate, ntdVal);
                    vo.setSafpNetUnits(netAmt); // RT 不會有單位數 請顯示SAFP_NET_AMT & RT不會有淨值
                } else {
                    // {[(單位數*單位淨值*匯率)/(平均台幣買價*總單位數)]–1}%
                    //System.out.println("invtNo=" + vo.getInvtNo());
                    values = FormulaUtil.formula1(netUnits, netValue, exchRate, ntdVal, expeNtd);
                }
                // 從Array指定變數
                roiRate = values[0];
                acctValue = values[1];
                avgPval = values[2];

            } catch (Exception ex) {
                log.error("policyNo:" + vo.getPolicyNo() + "invtNo :" + vo.getInvtNo());
                roiRate = new BigDecimal(0);
                acctValue = new BigDecimal(0);
                avgPval = new BigDecimal(0);
            }

            // 放入vo
            vo.setRoiRate(roiRate);
            vo.setAcctValue(acctValue);
            vo.setAvgPval(avgPval);
        }
        return investmentList;

    }


    @Override
    public List<TransFundConversionVo> inransFundConversionDate(List<InvestmentPortfolioVo> inInvestmentPortfolioVo) throws Exception{
        ArrayList<TransFundConversionVo> inTransFundConversionVosList = new ArrayList<>();
        //組裝轉入基金
        inInvestmentPortfolioVo.stream().forEach((in) -> {
            TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
            //獲取申請號
            /*String policyNo = in.getPolicyNo();
            if (!StringUtils.isEmpty(policyNo)) {
                String transNum = transDao.isTransNum(policyNo);
                transFundConversionVo.setTransNum(transNum);
            }*/
            transFundConversionVo.setInvtNo(in.getInvtNo());
            transFundConversionVo.setPolicyNo(in.getPolicyNo());
            Float number = in.getNumber();
            if (number==null) {
                number = 0F;
            }
            Integer ratio = in.getRatio();
            if (ratio==null) {
                ratio = 0;
            }
            transFundConversionVo.setValue(new BigDecimal(number));
            transFundConversionVo.setRatio(new BigDecimal(ratio));
            transFundConversionVo.setInvestmentType(TransTypeUtil.INVESTMENT_STATUS_IN);
            transFundConversionVo.setInvtName(in.getInvtName());
            inTransFundConversionVosList.add(transFundConversionVo);
        });
        return inTransFundConversionVosList;
    }

    @Override
    @Transactional
    public TransInvestmentVo insertTransFundConversion(TransInvestmentVo transInvestment,UsersVo user) throws Exception {
        String outInvestments = transInvestment.getInvestments();
        String inInvestments = transInvestment.getNewInvestments();
        List<TransFundConversionVo> oInvestments =
                    new Gson().fromJson(outInvestments, new TypeToken<List<TransFundConversionVo>>(){}.getType());
        if (CollectionUtils.isEmpty(oInvestments)) {
            throw new RuntimeException("保單已持有投資標的轉換-轉出申請為空，請重試！");
        }

        List<TransFundConversionVo> iInvestments =
                new Gson().fromJson(inInvestments, new TypeToken<List<TransFundConversionVo>>(){}.getType());
        if (CollectionUtils.isEmpty(iInvestments)) {
            throw new RuntimeException("保單已持有投資標的轉換-轉入申請為空，請重試！");
        }

        Date date = new Date();
        Map<String, Object> params = Maps.newHashMap();
        params.put("transNum", null);
        transDao.getTransNum(params);
        String transNum = params.get("transNum").toString();

        TransVo transVo = new TransVo();
        transVo.setTransNum(transNum);
        transVo.setTransDate(date);
        //注意:未來保費投資標的與分配比例  是方法addNewInvestmentApply
        transVo.setTransType(INVESTMENT_CONVERSION_CODE);
        transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
        transVo.setUserId(user.getUserId());
        transVo.setCreateUser(user.getUserId());
        transVo.setCreateDate(date);
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
        transPolicyVo.setPolicyNo(oInvestments.get(0).getPolicyNo());
        transPolicyDao.insertTransPolicy(transPolicyVo);


        oInvestments.stream().forEach((x)->{
             x.setTransNum(transNum);
            int insert = transFundConversionDao.insert(x);
        });

        iInvestments.forEach((x)->{
            x.setTransNum(transNum);
                int insert = transFundConversionDao.insertInvestments(x,transInvestment);
        });


        TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
        transInvestmentVo.setTransNum(oInvestments.get(0).getTransNum());
        transInvestmentVo.setPolicyNo(oInvestments.get(0).getPolicyNo());
        transInvestmentVo.setApplyDate(date);
        return transInvestmentVo;
    }


    @Override
    public Map<String, List<Map<String, String>>> getCompanyAndCurrencyList(String policyNo) {
        Map<String, List<Map<String, String>>> map = Maps.newHashMap();
        map.put("companys",  optionDao.getCompanysList(policyNo));
        map.put("currencys",  optionDao.getCurrencysList(policyNo));
        return map;
    }


    @Override
    public String transRiskLevelToName(String riskLevel) {
       List<ParameterVo> list = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "RISK_LEVEL_TO_RR");
       if (!CollectionUtils.isEmpty(list)) {
           for (ParameterVo vo : list) {
               if (StringUtils.equals(vo.getParameterCode(), "RISK_LEVEL_TO_RR_" + riskLevel)) {
                   return vo.getParameterName();
        }
           }
       }
       return "";
    }

    @Override
    public List<TransInvestmentDetailVo> getAppliedInvestments(String transNum) {
        return transInvestmentDao.selectCompareInvestments(transNum);
    }

    @Override
    public List<TransFundConversionVo> outransFundConversionDate (
            List<InvestmentPortfolioVo> outInvestmentPortfolioVo) throws Exception {
        ArrayList<TransFundConversionVo> outTransFundConversionVosList = new ArrayList<>();
        //組裝轉出基金
        outInvestmentPortfolioVo.stream().forEach((out) -> {
            TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
            //獲取申請號
            transFundConversionVo.setInvtNo(out.getInvtNo());
            transFundConversionVo.setPolicyNo(out.getPolicyNo());
            Float number = out.getNumber();
            if (number==null) {
                number = 0F;
            }
            transFundConversionVo.setValue(new BigDecimal(number));
            transFundConversionVo.setInvestmentType(TransTypeUtil.INVESTMENT_STATUS_OUT);
            if (out.getRatio()==null) {
                out.setRatio(0);
            }
            transFundConversionVo.setRatio(new BigDecimal(out.getRatio()));
           /* if (out.getRatio() != null && out.getRatio() != 0) {
                transFundConversionVo.setRatio(new BigDecimal(out.getRatio()));
            } else if (out.getNumber() != null) {
                //((參考淨值 *  输出單位數) / 參考帳戶價值*10000)  /100.00
                try {
                    BigDecimal big01 = out.getNetValueSell().multiply(new BigDecimal(out.getNumber()));
                    BigDecimal big = big01.divide(out.getAcctValue(),4, RoundingMode.HALF_UP);
                    BigDecimal divide = big.multiply(new BigDecimal(10000)).divide(new BigDecimal(100.00),4, RoundingMode.HALF_UP);
                    transFundConversionVo.setRatio(divide);
                } catch (Exception ex) {
                    transFundConversionVo.setRatio(new BigDecimal(0));
                    log.info("((參考淨值 *  输出單位數)/參考帳戶價值 *10000)  /100.00 計算失敗:{}", ExceptionUtils.getStackTrace(ex));
                    ex.printStackTrace();
                }
            } else {
                transFundConversionVo.setRatio(new BigDecimal(0));
            }*/
            transFundConversionVo.setInvtName(out.getInvtName());
                if(out.getNumber()>0){
            outTransFundConversionVosList.add(transFundConversionVo);
                }
        });
        return outTransFundConversionVosList;


    }

    @Override
    public List<CompareInvestmentVo> compareNew(List<InvestmentPortfolioVo> ownInvts, List<InvestmentPortfolioVo> newInvts) {

        Map<String, Integer> ownRationMap = ownInvts.stream().collect(Collectors.toMap(e -> e.getPolicyNo() + "-" + e.getInvtNo(), x -> x.getRatio()));
        List<CompareInvestmentVo> compareds = Lists.newArrayList();
        mergeNewownInvts(newInvts).forEach(e -> {
            CompareInvestmentVo vo = new CompareInvestmentVo();
            vo.setAfterRatio(e.getRatio());
            Integer ratio = ownRationMap.get(e.getPolicyNo() + "-" + e.getInvtNo());
            if (ratio != null) {
                vo.setPreRatio(ratio);
            }
            vo.setInvtName(e.getInvtName());
            vo.setInvtNo(e.getInvtNo());
            compareds.add(vo);
        });
        return compareds;
    }

    /***
     * 合並相同基金投資
     * @param newInvts
     * @return
     */
    private List<InvestmentPortfolioVo> mergeNewownInvts(List<InvestmentPortfolioVo> newInvts) {
        Map<String, InvestmentPortfolioVo> map = Maps.newHashMap();
        for (InvestmentPortfolioVo newInvt : newInvts) {
            InvestmentPortfolioVo vo = map.get(newInvt.getPolicyNo() + "-" + newInvt.getInvtNo());
            if (vo != null) {
                vo.setRatio(newInvt.getRatio() + vo.getRatio());
            } else {
                map.put(newInvt.getPolicyNo() + "-" + newInvt.getInvtNo(), newInvt);
            }
        }
        return map.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addNewInvestmentApply(TransInvestmentVo vo, UsersVo user) throws Exception {

        List<CompareInvestmentVo> newInvestments = new Gson().fromJson(vo.getFinalInvestments(), new TypeToken<List<CompareInvestmentVo>>(){}.getType());
        if (CollectionUtils.isEmpty(newInvestments)) {
            throw new RuntimeException("申請為空，請重試！");
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("transNum", null);
        transDao.getTransNum(params);
        String transNum = params.get("transNum").toString();

        TransVo transVo = new TransVo();
        transVo.setTransNum(transNum);
        transVo.setTransDate(new Date());
        transVo.setTransType(INVESTMENT_PARAMETER_CODE);
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

        for (CompareInvestmentVo e : newInvestments) {
            TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
            transInvestmentVo.setDistributionRatio(e.getAfterRatio().shortValue());
            transInvestmentVo.setPreDistributionRatio(e.getPreRatio().shortValue());
            transInvestmentVo.setTransNum(transNum);
            transInvestmentVo.setInvtNo(e.getInvtNo());
            transInvestmentVo.setPolicyNo(vo.getPolicyNo());
            transInvestmentVo.setInvtName(e.getInvtName());
            transInvestmentVo.setEnglishName(vo.getEnglishName());
            transInvestmentVo.setSwiftCode(vo.getSwiftCode());
            transInvestmentVo.setBankCode(vo.getBankCode());
            transInvestmentVo.setBankName(vo.getBankName());
            transInvestmentVo.setBranchCode(vo.getBranchCode());
            transInvestmentVo.setBranchName(vo.getBranchName());
            transInvestmentVo.setAccountName(vo.getAccountName());
            transInvestmentVo.setBankAccount(vo.getBankAccount());
            transInvestmentDao.insert(transInvestmentVo);
        }
        vo.setTransNum(transNum);
    }

    public Map<String,Object> getSendMailInfo() {
        String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INVESTMENT_TRANS_REMARK);
        String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM, OnlineChangeUtil.TWFHCLIFE_ADM);
        String[] mails = mailTo.split(";");
        Map<String,Object> rMap = new HashMap<String,Object>();
        List<String> receivers = new ArrayList<String>();
        if(mails.length > 0) {
            for (String mail : mails) {
                receivers.add(mail);
                log.info("Mail Address : " + mail);
            }
        }
        rMap.put("transRemark", transRemark);
        rMap.put("receivers", receivers);
        return rMap;
    }

    @Override
    public Map<String, String> getNtdAndUsdUnitMin() {
            String ntd_unit_min = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.NTD_UNIT_MIN);
            String usd_unit_min = parameterService.getParameterValueByCode(
                    ApConstants.SYSTEM_ID, OnlineChangeUtil.USD_UNIT_MIN);
        HashMap stringHashMap = new HashMap<String,String>();
        stringHashMap.put(OnlineChangeUtil.NTD_UNIT_MIN,ntd_unit_min);
            stringHashMap.put( OnlineChangeUtil.USD_UNIT_MIN,usd_unit_min);
        return stringHashMap;
    }

    @Override
    public Map<String, String> getNtdAndUsdProportionMin() {
        String ntdProportionMin = parameterService.getParameterValueByCode(
                ApConstants.SYSTEM_ID, OnlineChangeUtil.NTD_PROPORTION_MIN);
        String usdProportionMin = parameterService.getParameterValueByCode(
                ApConstants.SYSTEM_ID, OnlineChangeUtil.USD_PROPORTION_MIN);
        HashMap stringHashMap = new HashMap<String,String>();
        stringHashMap.put(OnlineChangeUtil.NTD_PROPORTION_MIN,ntdProportionMin);
        stringHashMap.put(OnlineChangeUtil.USD_PROPORTION_MIN,usdProportionMin);
        return stringHashMap;
    }

    @Override
    public BigDecimal getDistributeRationByInvtNo(String policyNo, String invtNo) {
        return transInvestmentDao.getDistributeRationByInvtNo(policyNo, invtNo, new Date());
    }

    @Override
    public List<String> getChckSwiftCode() throws Exception {
        List<ParameterVo> parameterByCategoryCode = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, OnlineChangeUtil.SHOW_ACCOUNT_INVT_NOS);

        List<String> collect = parameterByCategoryCode.stream().map(x -> {
            return x.getParameterValue();
        }).collect(Collectors.toList());
        return  collect;


    }
}
