package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.MessagingTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-07-23
 */
public interface MessageTemplateMapper {

    /**
     * 通信管理-查詢通信主檔By Code
     *
     * @param messagingTemplateCode
     * @return MessagingTemplateVo
     */
    MessagingTemplateVo getMessageTemplateByCode(@Param("messagingTemplateCode") String messagingTemplateCode);

    List<String> getReceiversByTemplateId(@Param("messagingTemplateId") int messagingTemplateId, @Param("receiverType") String receiverType);

}
