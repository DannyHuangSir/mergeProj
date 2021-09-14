package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransPolicyHolderProfileDao;
import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 保戶基本資料更新(線上):027
 * 
 * @author all
 */
public class TransPolicyHolderProfileUtil {

	private static final Logger logger = LogManager.getLogger(TransPolicyHolderProfileUtil.class);
	private static final String TRANS_TYPE = "POLICY_HOLDER_PROFILE";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "027"; // 介接代碼
	
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
		TransPolicyHolderProfileDao transPolicyHolderProfileDao = new TransPolicyHolderProfileDao();
		
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
				
				// 取得資料
				TransPolicyHolderProfileVo transPolicyHolderProfileVo = new TransPolicyHolderProfileVo();
				transPolicyHolderProfileVo.setTransNum(transNum);
				List<TransPolicyHolderProfileVo> list = transPolicyHolderProfileDao.getTransPolicyHolderProfileList(transPolicyHolderProfileVo);
				if (list != null && list.size() > 0) {
					transPolicyHolderProfileVo = list.get(0);
				}
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						logger.info("TransNum : {}, policyNo : {}", transNum, tpVo.getPolicyNo());
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),國籍代碼(2),職業代碼大(2),職業代碼中(2),職業代碼小(4)
						String line = String.format(StringUtils.repeat("%s", 9), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
								systemTwDate,
								systemTwDate,
								StringUtil.rpadBlank(transPolicyHolderProfileVo.getCountry(), 2),
								StringUtil.rpadBlank(transPolicyHolderProfileVo.getJobB(), 2),
								StringUtil.rpadBlank(transPolicyHolderProfileVo.getJobM(), 2),
								StringUtil.rpadBlank(transPolicyHolderProfileVo.getJobS(), 4)
						);
						logger.info(line);
						txtSb.append(line);
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
	 * @param transPolicyHolderProfileVo
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransPolicyHolderProfileVo newVo, TransPolicyHolderProfileVo oldVo) {
		if (oldVo != null) {
			String[] oldCountry = null;
			if (oldVo.getCountryDesc() != null && oldVo.getCountryDesc().indexOf(",") != -1) {
				oldCountry = oldVo.getCountryDesc().split(",");
			} else {
				oldCountry = new String[]{"",""};
			}
			
			beforeSb.append(String.format("要保人國籍：%s \r\n[區域]%s\r\n[國家]%s\r\n要保人職業：\r\n[大分類]%s\r\n[中分類]%s\r\n[小分類]%s\r\n"
					, MyStringUtil.nullToString(oldVo.getCountry())
					, MyStringUtil.nullToString(oldCountry[0])
					, MyStringUtil.nullToString(oldCountry[1])
					, MyStringUtil.nullToString(oldVo.getJobB())
					, MyStringUtil.nullToString(oldVo.getJobM())
					, MyStringUtil.nullToString(oldVo.getJobS())
					));
		}
		if (newVo != null) {
			String[] newCountry = null;
			if (newVo.getCountryDesc() != null && newVo.getCountryDesc().indexOf(",") != -1) {
				newCountry = newVo.getCountryDesc().split(",");
			} else {
				newCountry = new String[]{"",""};
			}
			afterSb.append(String.format("要保人國籍：%s \r\n[區域]%s\r\n[國家]%s\r\n要保人職業：\r\n[大分類]%s\r\n[中分類]%s\r\n[小分類]%s\r\n服務單位：%s\r\n"
					, MyStringUtil.nullToString(newVo.getCountry())
					, MyStringUtil.nullToString(newCountry[0])
					, MyStringUtil.nullToString(newCountry[1])
					, MyStringUtil.nullToString(newVo.getJobBDesc()) + "(" + MyStringUtil.nullToString(newVo.getJobB()) + ")"
					, MyStringUtil.nullToString(newVo.getJobMDesc()) + "(" + MyStringUtil.nullToString(newVo.getJobM()) + ")"
					, MyStringUtil.nullToString(newVo.getJobSDesc()) + "(" + MyStringUtil.nullToString(newVo.getJobS()) + ")"
					, MyStringUtil.nullToString(newVo.getCompanyName())
					));
		}
	}
}