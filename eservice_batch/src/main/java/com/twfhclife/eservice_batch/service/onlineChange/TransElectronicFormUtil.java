package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransElectronicFormDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransElectronicFormVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

public class TransElectronicFormUtil {

	private static final Logger logger = LogManager.getLogger(TransElectronicFormUtil.class);
	private static final String TRANS_TYPE_A = "Electronic_Form_A"; // 申請電子表單通知服務
	private static final String TRANS_TYPE_C = "Electronic_Form_C"; // 取消電子表單通知服務
	private static final String TRANS_STATUS = "8";  // 1:已審核
	private static final String UPLOAD_CODE = "036"; // 介接代碼
	private static final String SYSTEM_ID = "eservice"; // 介接代碼
	private static final String CATEGORY_CODE = ""; // 介接代碼
	
	
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate){
		
		
		TransDao transDao = new TransDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransElectronicFormDao dao = new  TransElectronicFormDao();
		
		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE_A);
		
		// 取得申請資料
		List<TransVo> transByElectronicA = transDao.getTransList(transVo);
		
		// 申請資料條件
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE_C);
		
		// 取得申請資料
		List<TransVo> transByElectronicC = transDao.getTransList(transVo);
		
		List<TransVo> transList = new ArrayList<>();
		
		if(transByElectronicA != null) {
			for(TransVo transVos : transByElectronicA) {
				transList.add(transVos);
			}
		}
		
		if(transByElectronicC != null) {
			for(TransVo transVos : transByElectronicC) {
				transList.add(transVos);
			}
		}
						
		if(transList != null) {
			for(TransVo transVos : transList) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transVos.getTransNum());
				List<TransPolicyVo> transPolicyVos = transPolicyDao.getTransPolicyList(transPolicyVo);
				transPolicyVo = transPolicyVos.get(0);
				if(transPolicyVo != null) {
					TransElectronicFormVo vo = new TransElectronicFormVo();
					vo.setTransNum(transVos.getTransNum());
					List<TransElectronicFormVo> voList = dao.getTransElectronicForm(vo);
					
					String eInfoN = voList.get(0).geteInfoN().equals("Y") ? "1" : "0";
					String email = StringUtil.rpad( voList.get(0).getEmail(), 50 , " ");
										
					// 介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyyMMdd),生效日(系統日yyyyMMdd),開通或不開通(1)
					// 介接代碼 => 036 開通 = 1  不開通 = 0
					txtSb.append(String.format(StringUtils.repeat("%s", 7), 
							UPLOAD_CODE,
							StringUtil.rpadBlank(transVos.getTransNum(), 12), 
							StringUtil.rpadBlank(transPolicyVo.getPolicyNo(), 10),
							systemTwDate,
							systemTwDate,
							eInfoN,
							email));
				
					txtSb.append("\r\n");
				}				
			}		
			return transList;
		} else {
			return Collections.emptyList();
		}
	}
}
