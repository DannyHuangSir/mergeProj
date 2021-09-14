package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransResendPolicyDao;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransResendPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 補發保單 (DS01):014
 * 
 * @author all
 */
public class TransResendPolicyUtil {

	private static final Logger logger = LogManager.getLogger(TransResendPolicyUtil.class);
	private static final String TRANS_TYPE = "POLICY_RESEND";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "014"; // 介接代碼
	
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
		TransResendPolicyDao resendPolicyDao = new TransResendPolicyDao();
		
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
				String address = "";
				String name = "";
				String tel = "";
				
				TransResendPolicyVo qryResendPolicyVo = new TransResendPolicyVo();
				qryResendPolicyVo.setTransNum(transNum);
				List<TransResendPolicyVo> resendPolicyList = resendPolicyDao.getTransResendPolicyList(qryResendPolicyVo);
				if (resendPolicyList != null && resendPolicyList.size() > 0) {
					TransResendPolicyVo transResendPolicyVo = resendPolicyList.get(0);
					address = StringUtils.trimToEmpty(transResendPolicyVo.getAddress());
					name = StringUtils.trimToEmpty(transResendPolicyVo.getName());
					tel = StringUtils.trimToEmpty(transResendPolicyVo.getTel());
				}
				
				logger.info("TransNum's address : {}", address);
				logger.info("TransNum's name : {}", name);
				logger.info("TransNum's tel : {}", tel);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),申請值(1),寄送地址(50)
						txtSb.append(String.format(StringUtils.repeat("%s", 7), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12),
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								"Y",
								StringUtil.rpadBlank(address, 50)
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
	 * @param before
	 * @param after
	 * @param transDetailList
	 */
	public void getChangeContent(StringBuilder before, StringBuilder after, TransResendPolicyVo transResendPolicyVo) {
		if (transResendPolicyVo != null) {
			String format = "收件人： %s\r\n聯絡電話： %s\r\n收件地址： %s";
			after.append(String.format(format
				, MyStringUtil.nullToString(transResendPolicyVo.getName())
				, MyStringUtil.nullToString(transResendPolicyVo.getTel())
				, MyStringUtil.nullToString(transResendPolicyVo.getAddress())
				));
		}
	}
}