package com.twfhclife.adm.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.adm.dao.MessagingParameterDao;
import com.twfhclife.adm.dao.MessagingParameterTmpDao;
import com.twfhclife.adm.dao.MessagingReceiverDao;
import com.twfhclife.adm.dao.MessagingReceiverTmpDao;
import com.twfhclife.adm.dao.MessagingTemplateDao;
import com.twfhclife.adm.dao.MessagingTemplateTmpDao;
import com.twfhclife.adm.dao.ParameterDao;
import com.twfhclife.adm.model.MessagingParameterTmpVo;
import com.twfhclife.adm.model.MessagingParameterVo;
import com.twfhclife.adm.model.MessagingReceiverTmpVo;
import com.twfhclife.adm.model.MessagingReceiverVo;
import com.twfhclife.adm.model.MessagingTemplateTmpVo;
import com.twfhclife.adm.model.MessagingTemplateVo;
import com.twfhclife.adm.service.IMessagingTemplateService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.service.IMailService;

/**
 * 通信管理服務.
 * 
 * @author all
 */
@Service
public class MessagingTemplateServiceImpl implements IMessagingTemplateService {

	private static final Logger logger = LogManager.getLogger(MessagingTemplateServiceImpl.class);

	@Autowired
	private MessagingTemplateDao messagingTemplateDao;

	@Autowired
	private MessagingTemplateTmpDao messagingTemplateTmpDao;

	@Autowired
	private MessagingParameterDao messagingParameterDao;

	@Autowired
	private MessagingParameterTmpDao messagingParameterTmpDao;

	@Autowired
	private MessagingReceiverDao messagingReceiverDao;

	@Autowired
	private MessagingReceiverTmpDao messagingReceiverTmpDao;

	@Autowired
	private ParameterDao parameterDao;

	@Autowired
	private IMailService mailService;

	@Value("${communction.mail.domain}")
	public String webDomain;
	
	/**
	 * 通信管理-分頁查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳通信管理-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getMessagingTemplatePageList(MessagingTemplateVo messagingTemplateVo) {
		return messagingTemplateDao.getMessagingTemplatePageList(messagingTemplateVo);
	}

	/**
	 * 通信管理-查詢結果總筆數.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getMessagingTemplatePageTotal(MessagingTemplateVo messagingTemplateVo) {
		return messagingTemplateDao.getMessagingTemplatePageTotal(messagingTemplateVo);
	}
	
	/**
	 * 通信管理-查詢.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<MessagingTemplateVo> getMessagingTemplate(MessagingTemplateVo messagingTemplateVo) {
		List<MessagingTemplateVo> dataList = messagingTemplateDao.getMessagingTemplate(messagingTemplateVo);
		for (MessagingTemplateVo mtVo : dataList) {
			// 設定參數
			MessagingParameterVo mpQryVo = new MessagingParameterVo();
			mpQryVo.setMessagingTemplateId(mtVo.getMessagingTemplateId());
			mtVo.setMessagingParameterList(messagingParameterDao.getMessagingParameter(mpQryVo));
			// 設定收件人
			MessagingReceiverVo mrQryVo = new MessagingReceiverVo();
			mrQryVo.setMessagingTemplateId(mtVo.getMessagingTemplateId());
			mtVo.setMessagingReceiverList(messagingReceiverDao.getMessagingReceiver(mrQryVo));
		}
		return dataList;
	}

	/**
	 * 通信管理-新增.
	 * 
	 * @param mtVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Transactional
	@Override
	public int insertMessagingTemplate(MessagingTemplateVo mtVo) {
		BigDecimal messagingTemplateId = messagingTemplateDao.getMessagingTemplateId();
		mtVo.setMessagingTemplateId(messagingTemplateId);
		mtVo.setCreateDate(new Date());
		mtVo.setUpdateDate(new Date());
		mtVo.setStatus("2");
		if ("sms".equals(mtVo.getSendType())) {
			mtVo.setMessagingSubject("");
		}
		
		MessagingTemplateTmpVo mttVo = new MessagingTemplateTmpVo();
		BeanUtils.copyProperties(mtVo, mttVo);
		
		// 新增通信
		int result = messagingTemplateDao.insertMessagingTemplate(mtVo);
		messagingTemplateTmpDao.insertMessagingTemplateTmp(mttVo);
		
		// 新增收件人
		for (String receiverValue : mtVo.getMessagingReceivers()) {
			MessagingReceiverVo mrVo = new MessagingReceiverVo();
			mrVo.setMessagingReceiverId(messagingTemplateDao.getMessagingReceiverId());
			mrVo.setMessagingReceiverType(mtVo.getSendType());
			mrVo.setMessagingReceiverValue(receiverValue);
			mrVo.setMessagingTemplateId(messagingTemplateId);
			
			MessagingReceiverTmpVo mrtVo = new MessagingReceiverTmpVo();
			BeanUtils.copyProperties(mrVo, mrtVo);
			
			messagingReceiverDao.insertMessagingReceiver(mrVo);
			messagingReceiverTmpDao.insertMessagingReceiverTmp(mrtVo);
		}
		
		if(mttVo.getParameters() != null) {
			// 新增參數
			for (String parameterOption : mttVo.getParameters()) {
				String paramId = parameterOption.split("\\^")[0];
				//String paramValue = parameterOption.split("^")[1];
				MessagingParameterVo mpVo = new MessagingParameterVo();
				mpVo.setParameterId(new BigDecimal(paramId));
				mpVo.setMessagingTemplateId(messagingTemplateId);
				
				MessagingParameterTmpVo mptVo = new MessagingParameterTmpVo();
				BeanUtils.copyProperties(mpVo, mptVo);
				
				messagingParameterDao.insertMessagingParameter(mpVo);
				messagingParameterTmpDao.insertMessagingParameterTmp(mptVo);
			}
		}
		
		sendMail(mtVo.getMessagingTemplateId(), mtVo.getMessagingTemplateCode(), mtVo.getMessagingTemplateName(), "add");
		return result;
	}
	
	/**
	 * 通信管理-更新.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Transactional
	@Override
	public int updateMessagingTemplate(MessagingTemplateVo mtVo) {
		MessagingTemplateTmpVo mttVo = new MessagingTemplateTmpVo();
		BeanUtils.copyProperties(mtVo, mttVo);
		
		mttVo.setStatus("2");
		mttVo.setUpdateDate(new Date());
		if ("sms".equals(mttVo.getSendType())) {
			mttVo.setMessagingSubject("");
		}
		
		// 更新通信TMP
		int result = messagingTemplateTmpDao.updateMessagingTemplateTmp(mttVo);
		
		// 刪除收件人TMP
		MessagingReceiverTmpVo mrtVo = new MessagingReceiverTmpVo();
		mrtVo.setMessagingTemplateId(mttVo.getMessagingTemplateId());
		messagingReceiverTmpDao.deleteMessagingReceiverTmp(mrtVo);
		
		// 新增收件人TMP
		for (String receiverValue : mttVo.getMessagingReceivers()) {
			MessagingReceiverTmpVo vo = new MessagingReceiverTmpVo();
			vo.setMessagingReceiverId(messagingTemplateDao.getMessagingReceiverId());
			vo.setMessagingReceiverType(mtVo.getSendType());
			vo.setMessagingReceiverValue(receiverValue);
			vo.setMessagingTemplateId(mtVo.getMessagingTemplateId());
			messagingReceiverTmpDao.insertMessagingReceiverTmp(vo);
		}
		
		// 刪除參數TMP
		MessagingParameterTmpVo mptVo = new MessagingParameterTmpVo();
		mptVo.setMessagingTemplateId(mttVo.getMessagingTemplateId());
		messagingParameterTmpDao.deleteMessagingParameterTmp(mptVo);
		
		if(mttVo.getParameters() != null) {
			// 新增參數TMP
			for (String parameterId : mttVo.getParameters()) {
				String paramId = parameterId.split("\\^")[0];
				MessagingParameterTmpVo vo = new MessagingParameterTmpVo();
				vo.setParameterId(new BigDecimal(paramId));
				vo.setMessagingTemplateId(mtVo.getMessagingTemplateId());
				messagingParameterTmpDao.insertMessagingParameterTmp(vo);
			}
		}
		
		sendMail(mttVo.getMessagingTemplateId(), mttVo.getMessagingTemplateCode(), mttVo.getMessagingTemplateName(), "update");
		return result;
	}
	
	/**
	 * 通信管理-停用.
	 * 
	 * @param messagingTemplateVo MessagingTemplateVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int stopMessagingTemplate(MessagingTemplateVo messagingTemplateVo) {
		MessagingTemplateTmpVo qryVo = new MessagingTemplateTmpVo();
		qryVo.setMessagingTemplateId(messagingTemplateVo.getMessagingTemplateId());
		
		List<MessagingTemplateTmpVo> mttList = messagingTemplateTmpDao.getMessagingTemplateTmp(qryVo);
		int result = 0;
		if (mttList != null) {
			MessagingTemplateTmpVo mttVo = mttList.get(0);
			mttVo.setStatus("2");
			mttVo.setUpdateUser(messagingTemplateVo.getUpdateUser());
			mttVo.setUpdateDate(new Date());
			
			result = messagingTemplateTmpDao.updateMessagingTemplateTmp(mttVo);
			sendMail(mttVo.getMessagingTemplateId(), mttVo.getMessagingTemplateCode(), mttVo.getMessagingTemplateName(), "stop");
		}
		return result;
	}

	/**
	 * 修改模板狀態.
	 * 
	 * @param vo MessagingTemplateVo
	 * @return
	 */
	@RequestLog
	@Transactional
	@Override
	public String updateMessagingTemplateStatus(MessagingTemplateVo vo){
		// 檢查是否已經審核過
		MessagingTemplateVo map = messagingTemplateDao.checkReviewStatus(vo.getMessagingTemplateId());
		if (map == null || !map.getStartDate().equals(vo.getStartDate())) {
			return "此連結已失效，請重新寄送覆核通知。";
		} else {
			if ("0".equals(vo.getStatus()) || "3".equals(vo.getStatus())) {
				// 同意
				// 將tmp狀態更新為通過 不變更修改日期
				MessagingTemplateTmpVo mttVo = new MessagingTemplateTmpVo();
				mttVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				mttVo.setStatus(vo.getStatus());
				messagingTemplateTmpDao.updateMessagingTemplateTmp(mttVo);
				
				// 將tmp複製至主表
				mttVo = new MessagingTemplateTmpVo();
				mttVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				MessagingTemplateTmpVo srcMttVo = messagingTemplateTmpDao.getMessagingTemplateTmp(mttVo).get(0);
				
				MessagingTemplateVo targetMtVo = new MessagingTemplateVo();
				BeanUtils.copyProperties(srcMttVo, targetMtVo);
				
				messagingTemplateDao.updateMessagingTemplate(targetMtVo);
				
				// 將tmp收件人更新至主表收件人 先刪後加
				MessagingReceiverVo mrVo = new MessagingReceiverVo();
				mrVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				messagingReceiverDao.deleteMessagingReceiver(mrVo);

				MessagingReceiverTmpVo mrtVo = new MessagingReceiverTmpVo();
				mrtVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				List<MessagingReceiverTmpVo> mrtList = messagingReceiverTmpDao.getMessagingReceiverTmp(mrtVo);
				for (MessagingReceiverTmpVo srcMrtVo : mrtList) {
					MessagingReceiverVo targetMrVo = new MessagingReceiverVo();
					BeanUtils.copyProperties(srcMrtVo, targetMrVo);
					
					messagingReceiverDao.insertMessagingReceiver(targetMrVo);
				}
				
				// 將tmp變數更新至主表變數 先刪後加
				MessagingParameterVo mpVo = new MessagingParameterVo();
				mpVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				messagingParameterDao.deleteMessagingParameter(mpVo);

				MessagingParameterTmpVo mptVo = new MessagingParameterTmpVo();
				mptVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				List<MessagingParameterTmpVo> mptList = messagingParameterTmpDao.getMessagingParameterTmp(mptVo);
				for (MessagingParameterTmpVo srcMptVo : mptList) {
					MessagingParameterVo targetMpVo = new MessagingParameterVo();
					BeanUtils.copyProperties(srcMptVo, targetMpVo);
					
					messagingParameterDao.insertMessagingParameter(targetMpVo);
				}
			} else {
				// 不同意
				// 將tmp資料status 設為 不同意
				MessagingTemplateTmpVo mttVo = new MessagingTemplateTmpVo();
				mttVo.setMessagingTemplateId(vo.getMessagingTemplateId());
				mttVo.setStatus(vo.getStatus());
				messagingTemplateTmpDao.updateMessagingTemplateTmp(mttVo);
				
				// 如果是新增的模板 需要將主表也設為不同意 反之主表不動
				if ("Y".equals(map.getType())) {
					MessagingTemplateVo mtVo = new MessagingTemplateVo();
					mtVo.setMessagingTemplateId(vo.getMessagingTemplateId());
					mtVo.setStatus(vo.getStatus());
					messagingTemplateDao.updateMessagingTemplate(mtVo);
				}
			}
			return "審核完成!";
		}
	}

	/**
	 * 檢查模版代號是否已存在.
	 * 
	 * @param vo
	 * @return
	 */
	@RequestLog
	@Override
	public boolean checkMessagingCode(MessagingTemplateVo vo) {
		String messagingCode = messagingTemplateDao.checkMessagingCode(vo);
		return StringUtils.isNotEmpty(messagingCode);
	}
	
	/**
	 * 寄送郵件通知.
	 * 
	 * @param messagingTemplateId
	 * @param messagingTemplateCode
	 * @param messagingTemplateName
	 * @param operation
	 */
	private void sendMail(BigDecimal messagingTemplateId, String messagingTemplateCode, String messagingTemplateName,
			String operation) {
		try {
			Map<String, String> countOpMap = new HashMap<>();
			countOpMap.put("add", "新增");
			countOpMap.put("update", "修改");
			countOpMap.put("stop", "停用");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = sdf.format(new Date());
			String mailTo = parameterDao.getParameterValueByCode(null, "COMM_TEMP_MANAGER");
			String content = parameterDao.getParameterValueByCode(null, "COMM_TEMP_CONTENT");
			String countOp = countOpMap.get(operation);
			String subject = (countOp + "通信模版審核通知");
			
			content = content.replace("{{countOp}}", countOp);
			content = content.replace("{{messagingTemplateCode}}", messagingTemplateCode);
			content = content.replace("{{messagingTemplateName}}", messagingTemplateName);
			content = content.replace("{{webDomain}}", webDomain);
			content = content.replace("{{messagingTemplateId}}", messagingTemplateId.toString());
			content = content.replace("{{operation}}", operation);
			content = content.replace("{{dateTime}}", dateString);
			
			if (operation.equals("add") || operation.equals("update")) {
				content = content.replace("{{status}}", "3");
			} else {
				content = content.replace("{{status}}", "0");
			}

			mailService.sendMail(content, subject, mailTo, "", null);
		} catch (Exception e) {
			logger.error("Unable to sendMail: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
