package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransRewardDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransRewardVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 增值回饋金領取方式:004
 * 
 * @author all
 */
public class TransRewardUtil {

	private static final Logger logger = LogManager.getLogger(TransRewardUtil.class);
	private static final String TRANS_TYPE = "REWARD";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "004"; // 介接代碼
	
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
		TransRewardDao rewardDao = new TransRewardDao();
		
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
				
				// 取得新的變更資訊
				String reward = "";
				
				TransRewardVo qryVo = new TransRewardVo();
				qryVo.setTransNum(transNum);
				List<TransRewardVo> rewardList = rewardDao.getTransRewardList(qryVo);
				if (rewardList != null && rewardList.size() > 0) {
					TransRewardVo transRewardVo = rewardList.get(0);
					reward = StringUtils.trimToEmpty(transRewardVo.getRewardMode());
				}
				logger.info("TransNum's reward : {}", reward);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),申請值(1)
						txtSb.append(String.format(StringUtils.repeat("%s", 6), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								reward
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
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransRewardVo transRewardVo) {
		if (transRewardVo != null) {
			ParameterDao paramDao = new ParameterDao();
			List<ParameterVo> listParam = new ArrayList<>();
			listParam.addAll(paramDao.getParameterByCategoryCode("eservice", "REWARD_TYPE"));
			Map<String, String> mapParam = new HashMap<>();
			mapParam.putAll(listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			String rewardOld = MyStringUtil.nullToString(transRewardVo.getRewardModeOld());
			String reward    = MyStringUtil.nullToString(transRewardVo.getRewardMode());
			beforeSb.append(MyStringUtil.nullToString(mapParam.get(rewardOld))).append("\r\n");
			afterSb.append(MyStringUtil.nullToString(mapParam.get(reward))).append("\r\n");
		}
	}
}