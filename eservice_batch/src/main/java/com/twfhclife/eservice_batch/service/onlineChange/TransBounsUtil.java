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
import com.twfhclife.eservice_batch.dao.TransBounsDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransBounsVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 紅利選擇權:003
 * 
 * @author all
 */
public class TransBounsUtil {

	private static final Logger logger = LogManager.getLogger(TransBounsUtil.class);
	private static final String TRANS_TYPE = "BONUS";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "003"; // 介接代碼
	
	/**
	 * 合併申請項目.
	 * 
	 * @param txtSb StringBuilder
	 * @param systemTwDate 系統日
	 */
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
		logger.info("Start running generate apply file: {}", TRANS_TYPE);
		
		TransDao transDao = new TransDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransBounsDao bounsDao = new TransBounsDao();
		
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
				String bouns = "";
				
				TransBounsVo qryVo = new TransBounsVo();
				qryVo.setTransNum(transNum);
				List<TransBounsVo> bounsList = bounsDao.getTransBounsList(qryVo);
				if (bounsList != null && bounsList.size() > 0) {
					TransBounsVo transBounsVo = bounsList.get(0);
					bouns = StringUtils.trimToEmpty(transBounsVo.getBounsMode());
				}
				logger.info("TransNum's bouns : {}", bouns);
				
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
								bouns
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
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransBounsVo transBounsVo) {
		if (transBounsVo != null) {
			ParameterDao paramDao = new ParameterDao();
			List<ParameterVo> listParam = new ArrayList<>();
			listParam.addAll(paramDao.getParameterByCategoryCode("eservice", "BONUS_TYPE"));
			Map<String, String> mapParam = new HashMap<>();
			mapParam.putAll(listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			String bonusOld = MyStringUtil.nullToString(transBounsVo.getBounsModeOld());
			String bonus    = MyStringUtil.nullToString(transBounsVo.getBounsMode());
			beforeSb.append(MyStringUtil.nullToString(mapParam.get(bonusOld))).append("\r\n");
			afterSb.append(MyStringUtil.nullToString(mapParam.get(bonus))).append("\r\n");
		}
	}
}