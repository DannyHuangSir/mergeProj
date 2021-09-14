package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransCertPrintDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransCertPrintVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 投保證明列印 (DS51):013
 * 
 * @author all
 */
public class TransCertPrintUtil {

	private static final Logger logger = LogManager.getLogger(TransCertPrintUtil.class);
	private static final String TRANS_TYPE = "CERTIFICATE_PRINT";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "013"; // 介接代碼
	
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
		TransCertPrintDao certPrintDao = new TransCertPrintDao();
		
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
				String passportName = "";
				String passportNo = "";
				String printCurr = "";
				String deliverType = "";
				String deliverAddr = "";
				String beliverMail = "";
				
				TransCertPrintVo qryVo = new TransCertPrintVo();
				qryVo.setTransNum(transNum);
				List<TransCertPrintVo> certPrintList = certPrintDao.getTransCertPrintList(qryVo);
				if (certPrintList != null && certPrintList.size() > 0) {
					TransCertPrintVo transCertPrintVo = certPrintList.get(0);
					lang = StringUtils.trimToEmpty(transCertPrintVo.getLang());
					passportName = StringUtils.trimToEmpty(transCertPrintVo.getPassportName());
					passportNo = StringUtils.trimToEmpty(transCertPrintVo.getPassportNo());
					printCurr = StringUtils.trimToEmpty(transCertPrintVo.getPrintCurr());
					deliverType = StringUtils.trimToEmpty(transCertPrintVo.getDeliverType());
					deliverAddr = StringUtils.trimToEmpty(transCertPrintVo.getDeliverAddr());
					beliverMail = StringUtils.trimToEmpty(transCertPrintVo.getBeliverMail());
				}
				logger.info("TransNum's lang : {}", lang);
				logger.info("TransNum's passportName : {}", passportName);
				logger.info("TransNum's passportNo : {}", passportNo);
				logger.info("TransNum's printCurr : {}", printCurr);
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
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),申請日(7),中英文CHT或ENG(3),紙本或EMAIL(1)),
						// 收件地址或EMAIL(50),護照英文姓名(50),護照號碼(20),幣別(3)
						// 保單號碼,申請日(<=系統日)
						// 收文日(系統日yyyMMdd)
						// 生效日(系統日yyyMMdd)
						txtSb.append(String.format(StringUtils.repeat("%s", 11), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								StringUtil.rpadBlank(lang, 3),
								StringUtil.rpadBlank(deliverType, 1),
								StringUtil.rpadBlank(deliverTypeValue, 50),
								StringUtil.rpadBlank(passportName, 50),
								StringUtil.rpadBlank(passportNo, 20),
								StringUtil.rpadBlank(printCurr, 3)
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
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransCertPrintVo transCertPrintVo) {
		if (transCertPrintVo != null) {
			String format = "列印格式： %s\r\n護照英文姓名： %s\r\n護照號碼： %s\r\n列印幣別： %s\r\n收件方式： %s\r\n收件地址： %s\r\n電子郵件信箱： %s";
			String printCurrCht = "";
			switch(transCertPrintVo.getPrintCurr()) {
			case "NTD": 
				printCurrCht = "台幣";
				break;
			case "USD":
				printCurrCht = "美元";
				break;
			case "EUR":
				printCurrCht = "歐元";
				break;
			}
			afterSb.append(String.format(format
				, ("C".equals(transCertPrintVo.getLang())? "中文": "E".equals(transCertPrintVo.getLang())? "英文": "")
				, MyStringUtil.nullToString(transCertPrintVo.getPassportName())
				, MyStringUtil.nullToString(transCertPrintVo.getPassportNo())
				, printCurrCht
				, ("P".equals(transCertPrintVo.getDeliverType())? "紙本": "E".equals(transCertPrintVo.getDeliverType())? "電子郵件信箱": "")
				, "P".equals(transCertPrintVo.getDeliverType())? MyStringUtil.nullToString(transCertPrintVo.getDeliverAddr()): ""
				, "E".equals(transCertPrintVo.getDeliverType())? MyStringUtil.nullToString(transCertPrintVo.getBeliverMail()): ""
				));
		}
	}
}