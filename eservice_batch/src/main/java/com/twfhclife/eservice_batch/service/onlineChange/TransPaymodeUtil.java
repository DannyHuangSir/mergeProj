package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPaymodeDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransPaymodeVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 繳別:001
 * 
 * @author all
 */
public class TransPaymodeUtil {

	private static final Logger logger = LogManager.getLogger(TransPaymodeUtil.class);
	private static final String TRANS_TYPE = "PAYMODE";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "001"; // 介接代碼
	
	/**
	 * 合併申請項目.
	 * @param txtSb StringBuilder
	 * @param systemTwDate 系統日
	 * @param activeTwDate
	 */
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate, String activeTwDate) {
		logger.info("Start running generate apply file: {}", TRANS_TYPE);
		
		TransDao transDao = new TransDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransPaymodeDao paymodeDao = new TransPaymodeDao();
		ParameterDao parameterDao = new ParameterDao();
		
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
				String paymode = "";
				String mk = "";
				String activeDate = "";
				BigDecimal amount = BigDecimal.valueOf(0);

				TransPaymodeVo qryVo = new TransPaymodeVo();
				qryVo.setTransNum(transNum);
				List<TransPaymodeVo> paymodeList = paymodeDao.getTransPaymodeList(qryVo);
				if (paymodeList != null && paymodeList.size() > 0) {
					TransPaymodeVo transPaymodeVo = paymodeList.get(0);
					paymode = StringUtils.trimToEmpty(transPaymodeVo.getPaymode());
					mk =  StringUtils.trimToEmpty(transPaymodeVo.getMk());
					amount = transPaymodeVo.getAmount();
				}
				logger.info("TransNum's paymode : {}", paymode);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						activeDate = paymodeDao.getActiveDate(policyNo);//下一應繳日
						logger.info("TransNum's policyNo : {}", policyNo);
						logger.info("TransNum's activeDate : {}", activeDate);
						String INVESTMENT_TYPES = parameterDao.getParameterValueByCode("eservice", "PAYMODE_INVESTMENT_TYPE");
						if (StringUtils.isNotBlank(INVESTMENT_TYPES) && INVESTMENT_TYPES.contains(policyNo.substring(0,2))) {
							// 介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyMMdd),生效日(下個周月日yyyMMdd),新繳別(1),彈性繳註記(1),新定期繳費(10)
							if (StringUtils.equals("E", paymode)) {
								paymode = "A";
								mk = "Y";
							}
							txtSb.append(String.format(StringUtils.repeat("%s", 8),
									"034",
									StringUtil.rpadBlank(transNum, 12),
									StringUtil.rpadBlank(policyNo, 10),
									systemTwDate,
									activeDate,
									StringUtil.rpadBlank(paymode, 1),
									StringUtil.rpadBlank(mk, 1),
									StringUtil.lpad(String.valueOf(amount.intValue()), 10, " ")
							));
						} else {
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),申請值(1)
							txtSb.append(String.format(StringUtils.repeat("%s", 6),
								UPLOAD_CODE,
									StringUtil.rpadBlank(transNum, 12),
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								activeDate,
								paymode
						));
						}
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
	 * @param transPaymodeVo
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransPaymodeVo transPaymodeVo) {
		if (transPaymodeVo != null) {
			ParameterDao paramDao = new ParameterDao();
			List<ParameterVo> listParam = new ArrayList<>();
			listParam.addAll(paramDao.getParameterByCategoryCode("eservice", "PAYMODE_TYPE"));
			Map<String, String> mapParam = new HashMap<>();
			mapParam.putAll(listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			String strPaymodeOld = MyStringUtil.nullToString(transPaymodeVo.getPaymodeOld());
			String strPaymode	 = MyStringUtil.nullToString(transPaymodeVo.getPaymode());
			beforeSb.append(strPaymodeOld).append("\r\n");
			afterSb.append(strPaymode).append("\r\n");
		}
	}
	
}