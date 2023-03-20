package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.NoteNotifyDataResponse;
import com.twfhclife.eservice.api_model.PersonalCaseDataResponse;
import com.twfhclife.eservice.service.IMailService;
import com.twfhclife.eservice.web.dao.JdMsgNotifyDao;
import com.twfhclife.eservice.web.dao.MessageDao;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.JdzqNotifyMsg;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.model.NoteNotifyVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IMessageService;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
}
