package com.twfhclife.eservice.onlineChange.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.onlineChange.model.TransCashPaymentVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.DepositPolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.model.PortfolioVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.model.LoginRequestVo;
import com.twfhclife.eservice.web.model.LoginResultVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.ILoginService;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.StatuCode;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import java.math.BigDecimal;
import java.util.*;

/***
 * 線上申請 - 申請保單提領(贖回)
 */
@Controller
public class TransDepositController extends BaseUserDataController {

    private static final Logger logger = LogManager.getLogger(TransDepositController.class);

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Autowired
    private ITransDepositService transDepositService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private ITransService transService;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private IParameterService parameterService;

    @RequestLog
    @GetMapping("/deposit1")
    public String deposit1(RedirectAttributes redirectAttributes) {
        if (!checkCanUseOnlineChange()) {
            String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:apply1";
        }

        /**
         * 有申請中的保單理賠,則不可再申請
         * TRANS  status=-1,0,4
         */
        String msg = transInvestmentService.checkHasApplying(getUserId());
        if (StringUtils.isNotBlank(msg)) {
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:apply1";
        }

        String userRocId = getUserRocId();
        List<PolicyListVo> policyList = policyListService.getDepositList(userRocId, null);

        if (policyList != null) {

            for (PolicyListVo policyListVo : policyList) {
                Pair<BigDecimal, BigDecimal> pair = computeMinAndMax((DepositPolicyListVo) policyListVo);
                policyListVo.setPolicyAcctValue(pair.getValue().compareTo(pair.getKey()) > 0 ? pair.getValue() : BigDecimal.valueOf(0));
            }

            List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,
                    getUserId(), TransTypeUtil.DEPOSIT_PARAMETER_CODE);
            transInvestmentService.handlePolicyStatusLocked(userRocId, handledPolicyList);
            transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,
                    TransTypeUtil.DEPOSIT_PARAMETER_CODE);
            addAttribute("policyList", handledPolicyList);
        }
        return "frontstage/onlineChange/deposit/deposit1";
    }

    @RequestLog
    @PostMapping("/deposit2")
    public String deposit2(TransDepositVo vo) {
        String userRocId = getUserRocId();
        DepositPolicyListVo depositPolicy = (DepositPolicyListVo) transDepositService.getDepositPolicy(userRocId, vo.getPolicyNo());
        Pair<BigDecimal, BigDecimal> pair = computeMinAndMax(depositPolicy);

        addAttribute("minValue", pair.getValue().compareTo(pair.getKey()) > 0 ? pair.getKey() : 0);
        addAttribute("maxValue", pair.getValue().compareTo(pair.getKey()) > 0 ? pair.getValue() : 0);
        addAttribute("depositVo", depositPolicy);
        addAttribute("depositPercent", transDepositService.getDepositConfigs(vo.getPolicyNo()));
        return "frontstage/onlineChange/deposit/deposit2";
    }

    @RequestLog
    @PostMapping("/deposit3")
    public String deposit3(TransDepositVo vo) {
        addAttribute("userName", getUserDetail().getUserName());
        addAttribute("showPost", checkPostShow(vo.getPolicyNo()));
        if (StringUtils.equals(vo.getDepositMethod(), "1")) {
            transDepositService.distributionDepositFund(getUserRocId(), vo);
        }
        addAttribute("depositVo", vo);
        return "frontstage/onlineChange/deposit/deposit3";
    }

    @RequestLog
    @PostMapping("/deposit4")
    public String deposit4(TransDepositVo vo) {
        addAttribute("depositVo", vo);
        if (StringUtils.isNotBlank(vo.getInvtDeposits())) {
            addAttribute("invtDeposits", new Gson().fromJson(vo.getInvtDeposits(), new TypeToken<List<Map<String, Object>>>() {
            }.getType()));
        }
        sendAuthCode("deposit");
        addAttribute("depositTimeSet", 300);
        return "frontstage/onlineChange/deposit/deposit4";
    }

    @RequestLog
    @PostMapping("/depositSuccess")
    public String depositSuccess(TransDepositVo vo) {
        String msg;
        if (StringUtils.equals(vo.getAuthType(), "password")) {
            msg = checkPassword(vo.getUserPassword());
        } else {
            msg = checkAuthCode("deposit", vo.getAuthenticationNum());
        }
        if (!StringUtils.isEmpty(msg)) {
            addSystemError(msg);
            return "forward:deposit4";
        } else {
            UsersVo user = getUserDetail();
            try {
                transDepositService.addNewDepositApply(vo, user);
            } catch (Exception e) {
                logger.error(e);
                return "forward:deposit4";
            }
            return "frontstage/onlineChange/deposit/deposit-success";
        }
    }

    @RequestLog
    @PostMapping("/getTransDepositDetail")
    public String getTransDepositDetail(@RequestParam("transNum") String transNum) {
        try {
            TransDepositDetailVo vo = transDepositService.getAppliedTransDeposits(transNum);
            if (!CollectionUtils.isEmpty(vo.getDetails())) {
                for (Map<String, Object> detail : vo.getDetails()) {
                    if (vo.getAmount() == null) {
                        vo.setAmount(0D);
                    }
                    vo.setAmount(BigDecimal.valueOf(vo.getAmount()).add((BigDecimal)detail.get("amount")).doubleValue());
                }
            }
            addAttribute("depositDetail", vo);
        } catch (Exception e) {
            logger.error("Unable to getTransDepositDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return "frontstage/onlineChange/deposit/deposit-detail";
    }

    private Boolean checkPostShow(String policyNo) {
        String types = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "DEPOSIT_POST_SHOW");
        if (!StringUtils.isEmpty(policyNo)) {
            if (policyNo.length() >= 2) {
                String substring = policyNo.substring(0, 2);
                return StringUtils.isBlank(types) || types.contains(substring);
            }
        }
        return false;
    }

    //keycloak驗證密碼
    private String checkPassword(String authenticationNum) {
        try {
            UsersVo loginUser = getUserDetail();
            if (loginUser == null) {
                return "密碼驗證失敗！";
            }
            LoginRequestVo loginRequestVo = new LoginRequestVo();
            String decodePasswd = decodeBase64(authenticationNum);
            loginRequestVo.setUserId(loginUser.getUserId());
            loginRequestVo.setPassword(decodePasswd);
            LoginResultVo res = loginService.doLogin(loginRequestVo);
            return res != null && StringUtils.equals("SUCCESS", res.getReturnCode()) ? null : "密碼驗證失敗！";
        } catch (Exception e) {
            logger.error(e);
            return "密碼驗證失敗！";
        }
    }

    private String decodeBase64(String mi) {
        String mingwen = "";
        if (StringUtils.isNotBlank(mi)) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] by = decoder.decodeBuffer(mi);
                mingwen = new String(by);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mingwen;
    }

    private Pair<BigDecimal, BigDecimal> computeMinAndMax(DepositPolicyListVo depositPolicy) {
        BigDecimal max = depositPolicy.getPolicyAcctValue();
        BigDecimal min = BigDecimal.valueOf(0);
        BigDecimal minValue = BigDecimal.valueOf(0);
        BigDecimal surplusValue = BigDecimal.valueOf(0);
        String stopAccount = "";
        List<ParameterVo> parameterVos = transDepositService.getDepositConfigs(depositPolicy.getPolicyNo());
                if (!CollectionUtils.isEmpty(parameterVos)) {
                    for (ParameterVo parameterVo : parameterVos) {
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_MIN_VALUE")) {
                    minValue = new BigDecimal(parameterVo.getParameterValue());
                            }
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_" + depositPolicy.getCurrency() + "_SURPLUS_VALUE")) {
                    surplusValue = new BigDecimal(parameterVo.getParameterValue());
                        }
                if (StringUtils.equals(parameterVo.getParameterCode(), "DEPOSIT_" + depositPolicy.getPolicyType() + "_STOP_ACCOUNT")) {
                    stopAccount = parameterVo.getParameterValue();
                    }
                }
            }

        if (depositPolicy != null && !CollectionUtils.isEmpty(depositPolicy.getPortfolioVoList())) {
            for (PortfolioVo portfolioVo : depositPolicy.getPortfolioVoList()) {
                if (!stopAccount.contains(portfolioVo.getInvtNo())) {
                    BigDecimal ratio = transInvestmentService.getDistributeRationByInvtNo(depositPolicy.getPolicyNo(), portfolioVo.getInvtNo());
                    BigDecimal tmpValue = BigDecimal.valueOf(0);
                    if (ratio != null) {
                        //分配比例轉換提領最小金額
                        tmpValue = minValue.multiply(BigDecimal.valueOf(100)).divide(ratio, 4, BigDecimal.ROUND_DOWN);
                    } else {
                        tmpValue = minValue;
                    }

                    if (tmpValue.compareTo(minValue) < 0 || portfolioVo.getAcctValue() == null || portfolioVo.getAcctValue().subtract(tmpValue).compareTo(surplusValue) < 0) {
                        continue;
        }
                    min = min.add(tmpValue);
                    max = max.subtract(surplusValue);
    }
            }
        }
        return new Pair<>(min, max);
    }

}
