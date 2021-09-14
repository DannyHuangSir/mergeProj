package com.twfhclife.eservice_batch.service.onlineChange;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransReducePolicyDao;
import com.twfhclife.eservice_batch.dao.TransReducePolicyDtlDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransReducePolicyDtlVo;
import com.twfhclife.eservice_batch.model.TransReducePolicyVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 減少保險金額(主約：DS01, 附約：DS04):009
 * 
 * @author all
 */
public class TransReducePolicyUtil {

	private static final Logger logger = LogManager.getLogger(TransReducePolicyUtil.class);
	private static final String TRANS_TYPE = "REDUCE_POLICY";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "009"; // 介接代碼
	
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
		TransReducePolicyDao dao = new TransReducePolicyDao();
		TransReducePolicyDtlDao dtlDao = new TransReducePolicyDtlDao();
		
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
				
				TransReducePolicyVo qryVo = new TransReducePolicyVo();
				qryVo.setTransNum(transNum);
				List<TransReducePolicyVo> dataList = dao.getTransReducePolicyList(qryVo);
				if (dataList != null && dataList.size() > 0) {
					BigDecimal transReducePolicyId = dataList.get(0).getId();
					
					// 取得新的變更資訊
					String contractAmount = "";
					
					TransReducePolicyDtlVo qryDtlVo = new TransReducePolicyDtlVo();
					qryDtlVo.setTransReducePolicyId(transReducePolicyId);
					List<TransReducePolicyDtlVo> dataDtlList = dtlDao.getTransReducePolicyDtlList(qryDtlVo);
					for (TransReducePolicyDtlVo dtlVo : dataDtlList) {
						contractAmount = (dtlVo.getContractAmount() == null ? "" : dtlVo.getContractAmount().toString());
						logger.info("TransNum's contractAmount : {}", contractAmount);
						
						// 取得保單號碼
						TransPolicyVo tpQryVo = new TransPolicyVo();
						tpQryVo.setTransNum(transNum);
						List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
						if (transPolicyList != null) {
							for (TransPolicyVo tpVo : transPolicyList) {
								String policyNo = tpVo.getPolicyNo();
								logger.info("TransNum's policyNo : {}", policyNo);
								
								// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),商品代號(2),欲變更金額(10)
								// 收文日(系統日)
								// 生效日(繳別,已轉應繳日,計算下一期應繳日)
								txtSb.append(String.format(StringUtils.repeat("%s", 7), 
										UPLOAD_CODE,
										StringUtil.rpadBlank(transNum, 12), 
										StringUtil.rpadBlank(policyNo, 10),
										systemTwDate,
										systemTwDate,
										policyNo.substring(0, 2),
										StringUtil.rpadBlank(contractAmount, 10)
								));
								txtSb.append("\r\n");
							}
						}
					}
				}
			}
			return transList;
		} else {
			return Collections.emptyList();
		}
	}
}