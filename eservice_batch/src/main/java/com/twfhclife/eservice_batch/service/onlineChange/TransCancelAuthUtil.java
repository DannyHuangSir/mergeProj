package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 終止授權 (CS17):018
 * 
 * @author all
 */
public class TransCancelAuthUtil {

	private static final Logger logger = LogManager.getLogger(TransCancelAuthUtil.class);
	private static final String TRANS_TYPE = "CANCEL_AUTH";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "018"; // 介接代碼
	
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
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7)
						txtSb.append(String.format(StringUtils.repeat("%s", 5), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate
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
}