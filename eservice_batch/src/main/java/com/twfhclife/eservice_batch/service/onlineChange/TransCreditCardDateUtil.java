package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransCreditCardDateDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransCreditCardDateVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 信用卡有效年月 (CS17):017
 * 
 * @author all
 */
public class TransCreditCardDateUtil {

	private static final Logger logger = LogManager.getLogger(TransCreditCardDateUtil.class);
	private static final String TRANS_TYPE = "CREDIT_CARD_DATE";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "017"; // 介接代碼
	
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
		TransCreditCardDateDao paymodeDao = new TransCreditCardDateDao();
		
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
				String validMm = "";
				String validYy = "";
				String validMmYy = "";
				
				TransCreditCardDateVo qryVo = new TransCreditCardDateVo();
				qryVo.setTransNum(transNum);
				List<TransCreditCardDateVo> creditCardList = paymodeDao.getTransCreditCardDateList(qryVo);
				for (TransCreditCardDateVo transCreditCardDateVo : creditCardList) {
					validMm = StringUtils.trimToEmpty(transCreditCardDateVo.getValidMm());
					validYy = StringUtils.trimToEmpty(transCreditCardDateVo.getValidYy());
					
					validMmYy = StringUtil.rpadBlank(validMm, 2) + StringUtil.rpadBlank(validYy, 2);
					logger.info("TransNum's validMm : {}", validMm);
					logger.info("TransNum's validYy : {}", validYy);
					logger.info("TransNum's validMmYy : {}", validMmYy);
					
					// 取得保單號碼
					TransPolicyVo tpQryVo = new TransPolicyVo();
					tpQryVo.setTransNum(transNum);
					List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
					if (transPolicyList != null) {
						for (TransPolicyVo tpVo : transPolicyList) {
							String policyNo = tpVo.getPolicyNo();
							logger.info("TransNum's policyNo : {}", policyNo);
							
							// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),有效月年(4)
							txtSb.append(String.format(StringUtils.repeat("%s", 6), 
									UPLOAD_CODE,
									StringUtil.rpadBlank(transNum, 12), 
									StringUtil.rpadBlank(policyNo, 10),
									systemTwDate,
									systemTwDate,
									validMmYy
							));
							txtSb.append("\r\n");
						}
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
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransCreditCardDateVo transCreditCardDateVo) {
		if (transCreditCardDateVo != null) {
			String format = "信用卡有效期限： %s/%s(月/年)";
			String strOld = String.format(format
					, MyStringUtil.nullToString(transCreditCardDateVo.getValidMmOld())
					, MyStringUtil.nullToString(transCreditCardDateVo.getValidYyOld()));
			String strNew = String.format(format
					, MyStringUtil.nullToString(transCreditCardDateVo.getValidMm())
					, MyStringUtil.nullToString(transCreditCardDateVo.getValidYy()));
			beforeSb.append(strOld).append("\r\n");
			afterSb.append(strNew).append("\r\n");
		}
	}
}