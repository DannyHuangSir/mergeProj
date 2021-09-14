package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.model.MessageTriggerRequestVo;

import java.util.Map;

/**
 * @author hui.chen
 * @create 2021-07-23
 */
public interface IMessagingTemplateService {

    public String triggerMessageTemplate(MessageTriggerRequestVo requestVo);

    public Map<String, Object> getSendMailInfo();
}
