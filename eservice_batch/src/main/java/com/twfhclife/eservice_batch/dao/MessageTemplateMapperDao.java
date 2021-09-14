package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.MessageTemplateMapper;
import com.twfhclife.eservice_batch.model.MessagingTemplateVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MessageTemplateMapperDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(MessageTemplateMapperDao.class);




	public  MessagingTemplateVo getMessageTemplateByCode(String messagingTemplateCode){
		MessagingTemplateVo messageTemplateByCode = null;
		try {
			MessageTemplateMapper templateMapper  = this.getSqlSession().getMapper(MessageTemplateMapper.class);
			 messageTemplateByCode = templateMapper.getMessageTemplateByCode(messagingTemplateCode);
		} catch (Exception e) {
			logger.error("MessageTemplateMapperDao   getMessageTemplateByCode error:", e);
		} finally {
			this.release();
		}
		return messageTemplateByCode;
	};

	public  List<String> getReceiversByTemplateId(int messagingTemplateId,String receiverType){
		List<String> receiversByTemplateId  = null;
		try {
			MessageTemplateMapper templateMapper  = this.getSqlSession().getMapper(MessageTemplateMapper.class);
			receiversByTemplateId= templateMapper.getReceiversByTemplateId(messagingTemplateId, receiverType);
		} catch (Exception e) {
			logger.error("MessageTemplateMapperDao   getReceiversByTemplateId error:", e);
		} finally {
			this.release();
		}
		return receiversByTemplateId;
	};



}
