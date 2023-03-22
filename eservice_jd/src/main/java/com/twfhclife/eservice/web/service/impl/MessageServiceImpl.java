package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.NoteNotifyDataResponse;
import com.twfhclife.eservice.service.IMailService;
import com.twfhclife.eservice.web.dao.JdMsgNotifyDao;
import com.twfhclife.eservice.web.dao.JdNotifyConfigDao;
import com.twfhclife.eservice.web.dao.MessageDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.*;
import com.twfhclife.eservice.web.service.IMessageService;
import com.twfhclife.eservice.web.service.IPolicyService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MessageServiceImpl implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private MessageDao messageDao;

    @Override
    public List<MessageVo> getMessages(MessageVo vo, String userId) {
        return messageDao.getMessages(vo, userId);
    }

    @Override
    public int getNotRead(String id) {
        return messageDao.getNotRead(id);
    }

    @Override
    public int readMsg(Long id) {
        return messageDao.readMsg(id);
    }

    @Autowired
    private JdMsgNotifyDao jdMsgNotifyDao;
    @Scheduled( cron = "0/20 * * * * ?")
    public void notifyMsgSchedule() {
        List<JdzqNotifyMsg> msgs = jdMsgNotifyDao.getWillSendMsgs();
        List<Long> ids = Lists.newArrayList();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(msgs)) {
            for (JdzqNotifyMsg msg : msgs) {
                ids.add(msg.getId());
                jdMsgNotifyDao.addJdNotifyMsg(msg);
            }
            jdMsgNotifyDao.updateNotifyMsgComplete(ids);
        }
    }

    @Autowired
    private BaseRestClient baseRestClient;

    @Autowired
    private UsersDao usersDao;

    @Value("${eservice_api.note.schedule.url}")
    private String noteScheduleUrl;

    @Autowired
    private IMailService mailService;

    @Autowired
    private MessageTemplateClient messageTemplateClient;

    @Scheduled( cron = "0 0 2 ? * *")
//    @Scheduled( cron = "0/20 * * * * ?")
    public void notifyNoteSchedule() {
        NoteNotifyDataResponse responseObj = baseRestClient.postApi("", noteScheduleUrl, NoteNotifyDataResponse.class);
        if (responseObj != null && CollectionUtils.isNotEmpty(responseObj.getNoteNotifies())) {
            for (NoteNotifyVo noteNotify : responseObj.getNoteNotifies()) {
                try {
                    UsersVo vo = usersDao.getUserBySaleId(noteNotify.getpSalesCode());
                    if (vo != null) {
                        String title = String.format("要保人%s/被保險人%s 照會截止日通知", noteNotify.getAppName(), noteNotify.getInsName());
                        JdzqNotifyMsg msg = new JdzqNotifyMsg();
                        msg.setUsers(Lists.newArrayList(vo.getUserId()));
                        msg.setTitle(title);
                        msg.setMsg(String.format("要保人%s/被保險人%s-保單號碼%s案件的照會回覆截止日期為%s，再請確認是否已回覆。", noteNotify.getAppName(), noteNotify.getInsName(), noteNotify.getPolicyNo(), noteNotify.getDueDate()));
                        msg.setNotifyTime(new Date());
                        jdMsgNotifyDao.addJdNotifyMsg(msg);
                        Map<String, String> paramMap = new HashMap();
                        List<String> receivers = new ArrayList<>();
                        receivers.add(vo.getEmail());
                        paramMap.put("AppName", noteNotify.getAppName());
                        paramMap.put("InsName", noteNotify.getInsName());
                        paramMap.put("DueDate", noteNotify.getDueDate());
                        paramMap.put("POLICY_NO", noteNotify.getPolicyNo());
                        messageTemplateClient.sendNoticeViaMsgTemplate("ELIFE_JD_MAIL-001", receivers, paramMap, "email");
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
    @Scheduled( cron = "0 0 2 ? * *")
//    @Scheduled( cron = "0/20 * * * * ?")
    public void notifyStopProfitAndStopLoss() {
        List<NotifyScheduleVo> notifyScheduleVos = notifyConfigDao.getNotifyConfigSchedule();
        if(CollectionUtils.isNotEmpty(notifyScheduleVos)) {
            for (NotifyScheduleVo notifyScheduleVo : notifyScheduleVos) {
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
                    msg.setMsg("保單：" + notifyPolicyNos + " 達到停利停損設定，請知悉！");
                    msg.setNotifyTime(new Date());
                    jdMsgNotifyDao.addJdNotifyMsg(msg);
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
