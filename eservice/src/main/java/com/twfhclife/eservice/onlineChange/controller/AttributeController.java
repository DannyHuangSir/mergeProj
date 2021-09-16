package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.model.TransAnswerVo;
import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;
import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.PortfolioClient;
import com.twfhclife.generic.controller.BaseUserDataController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AttributeController extends BaseUserDataController  {

    private static final Logger logger = LogManager.getLogger(AttributeController.class);

    @Autowired
    private IAttributeService attributeService;

    @Autowired
    private ITransInvestmentService transInvestmentService;

    @Autowired
    private ITransRiskLevelService riskLevelService;

    @RequestLog
    @GetMapping("/attribute1")
    public String attribute1(RedirectAttributes redirectAttributes) {

        if(!checkCanUseOnlineChange()) {
            String message = getParameterValue(ApConstants.SYSTEM_MSG_PARAMETER, "E0088");
            addSystemError(message);
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
        addAttribute("questions", attributeService.getQuestions());
        return "frontstage/onlineChange/attribute/attribute1";
    }

    @RequestLog
    @PostMapping("/attribute2")
    public String attribute2(TransAnswerVo vo) {
        String riskLevel = riskLevelService.computeRiskLevel(vo.getScore());
        vo.setLevel(riskLevel);
        vo.setDesc(transInvestmentService.transRiskLevelToName(riskLevel));
        addAttribute("transAnswerVo", vo);
        return "frontstage/onlineChange/attribute/attribute2";
    }

    @RequestLog
    @PostMapping("/attributeSuccess")
    public String attributeSuccess(TransAnswerVo vo) {
        UsersVo user = getUserDetail();
        try {
            int result = attributeService.addNewApply(vo, user);
            if (result <= 0) {
                addDefaultSystemError();
                return "forward:attribute2";
            }
        } catch (Exception e) {
            logger.error(e);
            return "forward:attribute2";
        }
        return "frontstage/onlineChange/attribute/attribute-success";
    }
}
