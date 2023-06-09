package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransRolloverPeriodicallyDao;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransRolloverPeriodicallyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

public class TransRolloverPeriodicallyUtil {
	private static final Logger logger = LogManager.getLogger(TransRolloverPeriodicallyUtil.class);
	private static final String TRANS_TYPE = "ROLLOVER_PERIODICALLY";
	private static final String TRANS_STATUS = "8";  // 1:已審核
	private static final String UPLOAD_CODE = "007"; // 介接代碼
	
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
		TransRolloverPeriodicallyDao dao = new   TransRolloverPeriodicallyDao();
		
		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE);
		
		// 取得申請資料
		List<TransVo> transList = transDao.getTransList(transVo);
								
		if(transList != null) {
			for(TransVo transVos : transList) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transVos.getTransNum());
				List<TransPolicyVo> transPolicyVos = transPolicyDao.getTransPolicyList(transPolicyVo);
				transPolicyVo = transPolicyVos.get(0);
				if(transPolicyVo != null) {
					TransRolloverPeriodicallyVo vo = new TransRolloverPeriodicallyVo();
					vo.setTransNum(transVos.getTransNum());
					vo = dao.getTransRolloverPeriodically(vo);
					
					String nextTredPaabDate = vo.getNextTredPaabDate();
					if(StringUtils.isNotEmpty(nextTredPaabDate)) {
						// "介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7)收文日(系統日)生效日(用繳別及已轉應繳日,計算下一期應繳日)"
						// 介接代碼 => 007 生效日(用繳別及已轉應繳日,計算下一期應繳日 為 TransDeratePaidOff 的nextTredPaabDate 欄位
						txtSb.append(String.format(StringUtils.repeat("%s", 5), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transVos.getTransNum(), 12), 
								StringUtil.rpadBlank(transPolicyVo.getPolicyNo(), 10),
								systemTwDate,
								nextTredPaabDate));				
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
