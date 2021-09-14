package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransValuePrintDao;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransValuePrintVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 保單價值列印 (DS48):012
 * 
 * @author all
 */
public class TransValuePrintUtil {

	private static final Logger logger = LogManager.getLogger(TransValuePrintUtil.class);
	private static final String TRANS_TYPE = "VALUE_PRINT";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "012"; // 介接代碼
	
	/**
	 * 合併申請項目.
	 * 
	 * @param txtSb StringBuilder
	 * @param systemTwDate 系統日
	 * @return 
	 */
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
		logger.info("Start running generate apply file: {}", TRANS_TYPE);
		
		TransDao transDao = new TransDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransValuePrintDao valuePrintDao = new TransValuePrintDao();
		
		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE);
		
		// 取得申請資料
		List<TransVo> transList = transDao.getTransList(transVo);
		if (transList != null) {
			for (TransVo trantsVo : transList) {
				String transNum = trantsVo.getTransNum();
				logger.info("TransNum: {}", transNum);
				
				// 取得新的變更保單資訊
				String lang = "";
				String deliverType = "";
				String deliverAddr = "";
				String beliverMail = "";
				
				TransValuePrintVo qryVo = new TransValuePrintVo();
				qryVo.setTransNum(transNum);
				List<TransValuePrintVo> valuePrintList = valuePrintDao.getTransValuePrintList(qryVo);
				if (valuePrintList != null && valuePrintList.size() > 0) {
					TransValuePrintVo transValuePrintVo = valuePrintList.get(0);
					lang = StringUtils.trimToEmpty(transValuePrintVo.getLang());
					deliverType = StringUtils.trimToEmpty(transValuePrintVo.getDeliverType());
					deliverAddr = StringUtils.trimToEmpty(transValuePrintVo.getDeliverAddr());
					beliverMail = StringUtils.trimToEmpty(transValuePrintVo.getBeliverMail());
				}
				logger.info("TransNum's lang : {}", lang);
				logger.info("TransNum's deliverType : {}", deliverType);
				logger.info("TransNum's deliverAddr : {}", deliverAddr);
				logger.info("TransNum's beliverMail : {}", beliverMail);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// 收件方式：紙本  電子郵件
						String deliverTypeValue = "";
						if ("E".equals(deliverType)) {
							deliverTypeValue = beliverMail;
						} else if ("P".equals(deliverType)) {
							deliverTypeValue = deliverAddr;
						}
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),計算基準日(7),中英文CHT或ENG(3),紙本或EMAIL(1),收件地址或EMAIL(50)
						// 保單號碼,申請日(<=系統日)
						// 收文日(系統日yyyMMdd)
						// 生效日(系統日yyyMMdd)
						txtSb.append(String.format(StringUtils.repeat("%s", 8), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								StringUtil.rpadBlank(lang, 3),
								StringUtil.rpadBlank(deliverType, 1),
								StringUtil.rpadBlank(deliverTypeValue, 50)
						));
						txtSb.append("\r\n");
					}
				}
			}
			return transList;
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * onlinechange_info 使用的文字內容
	 * @param beforeSb
	 * @param afterSb
	 * @param transDetailList
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransValuePrintVo transValuePrintVo) {
		if (transValuePrintVo != null) {
			String format = "列印格式： %s\r\n收件方式： %s\r\n收件地址： %s\r\n電子郵件信箱： %s";
			afterSb.append(String.format(format
				, ("C".equals(transValuePrintVo.getLang())? "中文": ("E".equals(transValuePrintVo.getLang())? "英文": ""))
				, ("P".equals(transValuePrintVo.getDeliverType())? "紙本": ("E".equals(transValuePrintVo.getDeliverType())? "電子郵件信箱": ""))
				, ("P".equals(transValuePrintVo.getDeliverType())? MyStringUtil.nullToString(transValuePrintVo.getDeliverAddr()): "")
				, ("E".equals(transValuePrintVo.getDeliverType())? MyStringUtil.nullToString(transValuePrintVo.getBeliverMail()): "")
				));
		}
	}
}