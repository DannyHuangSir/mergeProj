package com.twfhclife.jd.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.twfhclife.jd.api_client.BaseRestClient;
import com.twfhclife.jd.api_client.MessageTemplateClient;
import com.twfhclife.jd.api_model.NoteNotifyDataResponse;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.web.dao.JdMsgNotifyDao;
import com.twfhclife.jd.web.dao.JdNotifyConfigDao;
import com.twfhclife.jd.web.dao.UsersDao;
import com.twfhclife.jd.web.model.*;
import com.twfhclife.jd.web.service.IParameterService;
import com.twfhclife.jd.web.service.IPolicyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ServiceServiceTask {

    Logger logger = LoggerFactory.getLogger(ServiceServiceTask.class);

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private BaseRestClient baseRestClient;

    @Value("${eservice_api.note.schedule.url}")
    private String noteScheduleUrl;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @PostConstruct
    public void task() {
        List<ParameterVo> list = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.CRON_JOB);
        if (list != null) {
            list.forEach(parameterItem -> {
                if (System.getProperty(parameterItem.getParameterName()) == null) {
                    logger.info("set parameter: {}, {}", parameterItem.getParameterName(), parameterItem.getParameterValue());
                    System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
                }
            });
        }
    }
    @Scheduled(cron = "${cron.notify.note.expression:0 0 2 ? * *}")
    public void notifyNoteSchedule() {
        logger.info("---- notifyNoteSchedule start ----");
        NoteNotifyDataResponse responseObj = baseRestClient.postApi("", noteScheduleUrl, NoteNotifyDataResponse.class);
        if (responseObj != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(responseObj.getNoteNotifies())) {
            logger.info("照會截止日通知 api response: {}", new Gson().toJson(responseObj));
            for (NoteNotifyVo noteNotify : responseObj.getNoteNotifies()) {
                try {
                    UsersVo vo = usersDao.getUserBySaleId(noteNotify.getpSalesCode(), noteNotify.getAgentCode(), StringUtils.isBlank(noteNotify.getAgentBranchM()) ? "" : noteNotify.getAgentBranchM());
                    if (vo != null) {
                        JdzqNotifyMsg msg = new JdzqNotifyMsg();
                        msg.setUsers(Lists.newArrayList(vo.getUserId()));
                        msg.setTitle("照會截止日通知");
                        msg.setMsg(String.format("要保人%s/被保險人%s-保單號碼%s案件的照會回覆截止日期為%s，再請確認是否已回覆。", noteNotify.getAppName(), noteNotify.getInsName(), noteNotify.getPolicyNo(), noteNotify.getDueDate()));
                        msg.setNotifyTime(new Date());
                        jdMsgNotifyDao.addJdNotifyMsg(msg, new Date());
                        Map<String, String> paramMap = new HashMap();
                        List<String> receivers = new ArrayList<>();
                        receivers.add(vo.getEmail());
                        paramMap.put("AppName", noteNotify.getAppName());
                        paramMap.put("InsName", noteNotify.getInsName());
                        paramMap.put("DueDate", noteNotify.getDueDate());
                        paramMap.put("PolicyNo", noteNotify.getPolicyNo());
                        logger.info("send 照會截止日通知: {}", paramMap);
                        messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_JD_MAIL-001", receivers, paramMap, "email");
                    }

                    if (StringUtils.isNotBlank(noteNotify.getBranchCode())) {
                        List<UsersVo> branchLeaders = usersDao.getBranchLeaders(noteNotify.getAgentCode(), StringUtils.isBlank(noteNotify.getAgentBranchM()) ? "" : noteNotify.getAgentBranchM(), noteNotify.getBranchCode());
                        if (CollectionUtils.isNotEmpty(branchLeaders)) {
                            branchLeaders.stream().filter(x -> !StringUtils.equals(x.getUserId(), vo != null ? vo.getUserId() : null)).forEach(i -> {
                                JdzqNotifyMsg branchMsg = new JdzqNotifyMsg();
                                branchMsg.setUsers(Lists.newArrayList(i.getUserId()));
                                branchMsg.setTitle("照會截止日通知");
                                branchMsg.setMsg(String.format("要保人%s/被保險人%s-保單號碼%s案件的照會回覆截止日期為%s，再請確認是否已回覆。", noteNotify.getAppName(), noteNotify.getInsName(), noteNotify.getPolicyNo(), noteNotify.getDueDate()));
                                branchMsg.setNotifyTime(new Date());
                                jdMsgNotifyDao.addJdNotifyMsg(branchMsg, new Date());
                                Map<String, String> branchMsgParamMap = new HashMap();
                                List<String> branchMsgReceivers = new ArrayList<>();
                                branchMsgReceivers.add(i.getEmail());
                                branchMsgParamMap.put("AppName", noteNotify.getAppName());
                                branchMsgParamMap.put("InsName", noteNotify.getInsName());
                                branchMsgParamMap.put("DueDate", noteNotify.getDueDate());
                                branchMsgParamMap.put("PolicyNo", noteNotify.getPolicyNo());
                                logger.info("send 照會截止日通知: {}", branchMsgParamMap);
                                messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_JD_MAIL-001", branchMsgReceivers, branchMsgParamMap, "email");
                            });
                        }
                    }
                } catch (Exception e) {
                    logger.error("notifyNoteSchedule error: {}", e);
                }
            }
        }
    }

    @Autowired
    private JdNotifyConfigDao notifyConfigDao;

    @Autowired
    private IPolicyService policyService;

    @Scheduled(cron = "${cron.notify.config.expression:0 0 2 ? * *}")
    public void notifyStopProfitAndStopLoss() {
        List<NotifyScheduleVo> notifyScheduleVos = notifyConfigDao.getNotifyConfigSchedule();
        if(CollectionUtils.isNotEmpty(notifyScheduleVos)) {
            for (NotifyScheduleVo notifyScheduleVo : notifyScheduleVos) {
                try {
                    Set<String> notifyPolicyNos = Sets.newHashSet();
                    if (CollectionUtils.isNotEmpty(notifyScheduleVo.getInvts())) {
                        for (SubNotifyScheduleVo invt : notifyScheduleVo.getInvts()) {
                            List<PortfolioVo> portfolioVos = policyService.getPolicyRateSchedule(invt.getPolicyNo());
                            if (CollectionUtils.isNotEmpty(portfolioVos)) {
                                for (PortfolioVo portfolioVo : portfolioVos) {
                                    if (StringUtils.equals(portfolioVo.getInvtNo(), invt.getInvtNo()) && checkROI(portfolioVo, invt)) {
                                        notifyPolicyNos.add(portfolioVo.getPolicyNo());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(notifyPolicyNos)) {
                        JdzqNotifyMsg msg = new JdzqNotifyMsg();
                        msg.setUsers(Lists.newArrayList(notifyScheduleVo.getUserId()));
                        msg.setTitle("停利停損通知");
                        msg.setMsg("保單：" + notifyPolicyNos.toString() + " 達到停利停損設定，請知悉！");
                        msg.setNotifyTime(new Date());
                        jdMsgNotifyDao.addJdNotifyMsg(msg, new Date());
                    }
                } catch (Exception e) {
                    logger.error("notifyStopProfitAndStopLoss error: {}", e);
                }
            }
        }
    }
    private boolean checkROI(PortfolioVo vo, SubNotifyScheduleVo vo1) {
        // 計算該保戶+該投資標的之報酬率
        BigDecimal roiRate = vo.getRoiRate();
        BigDecimal percentageUp = new BigDecimal(vo1.getUpRate() == null ? "99999" : vo1.getUpRate().toString());
        BigDecimal percentageDown = new BigDecimal(vo1.getDownRate() == null ? "99999" : vo1.getDownRate().toString());
        return this.doCompare(percentageUp, percentageDown.multiply(new BigDecimal(-1)), roiRate);
    }

    /** 進行比較: FD/RT */
    private boolean doCompare(BigDecimal percentageUp, BigDecimal percentageDown, BigDecimal value) {
        boolean flag = false;
        // 當滿足點發生, 即回傳true
        if (value.compareTo(percentageUp) >= 0 || value.compareTo(percentageDown) <= 0) {
            flag = true;
        }
        return flag;
    }
}
