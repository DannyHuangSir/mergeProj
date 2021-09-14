package com.twfhclife.adm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.MessagingParameterTmpDao;
import com.twfhclife.adm.dao.MessagingReceiverTmpDao;
import com.twfhclife.adm.dao.MessagingTemplateTmpDao;
import com.twfhclife.adm.model.MessagingParameterTmpVo;
import com.twfhclife.adm.model.MessagingReceiverTmpVo;
import com.twfhclife.adm.model.MessagingTemplateTmpVo;
import com.twfhclife.adm.service.IMessagingTemplateTmpService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 通信管理TMP服務.
 * 
 * @author all
 */
@Service
public class MessagingTemplateTmpServiceImpl implements IMessagingTemplateTmpService {

	@Autowired
	private MessagingTemplateTmpDao messagingTemplateTmpDao;

	@Autowired
	private MessagingParameterTmpDao messagingParameterTmpDao;

	@Autowired
	private MessagingReceiverTmpDao messagingReceiverTmpDao;
	
	/**
	 * 通信管理TMP-查詢.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<MessagingTemplateTmpVo> getMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo) {
		List<MessagingTemplateTmpVo> dataList = messagingTemplateTmpDao.getMessagingTemplateTmp(messagingTemplateTmpVo);
		for (MessagingTemplateTmpVo mttVo : dataList) {
			// 設定參數
			MessagingParameterTmpVo mptQryVo = new MessagingParameterTmpVo();
			mptQryVo.setMessagingTemplateId(mttVo.getMessagingTemplateId());
			mttVo.setMessagingParameterTmpList(messagingParameterTmpDao.getMessagingParameterTmp(mptQryVo));
			// 設定收件人
			MessagingReceiverTmpVo mrtQryVo = new MessagingReceiverTmpVo();
			mrtQryVo.setMessagingTemplateId(mttVo.getMessagingTemplateId());
			mttVo.setMessagingReceiverTmpList(messagingReceiverTmpDao.getMessagingReceiverTmp(mrtQryVo));
		}
		return dataList;
	}

	/**
	 * 通信管理TMP-新增.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int insertMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo) {
		return messagingTemplateTmpDao.insertMessagingTemplateTmp(messagingTemplateTmpVo);
	}
	
	/**
	 * 通信管理TMP-更新.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int updateMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo) {
		return messagingTemplateTmpDao.updateMessagingTemplateTmp(messagingTemplateTmpVo);
	}

	/**
	 * 通信管理TMP-刪除.
	 * 
	 * @param messagingTemplateTmpVo MessagingTemplateTmpVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteMessagingTemplateTmp(MessagingTemplateTmpVo messagingTemplateTmpVo) {
		return messagingTemplateTmpDao.deleteMessagingTemplateTmp(messagingTemplateTmpVo);
	}
}