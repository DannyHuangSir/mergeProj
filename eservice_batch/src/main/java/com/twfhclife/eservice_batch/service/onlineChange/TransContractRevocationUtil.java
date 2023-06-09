package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransContractRevocationDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransContractRevocationVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

public class TransContractRevocationUtil {
	private static final Logger logger = LogManager.getLogger(TransElectronicFormUtil.class);
	private static final String TRANS_TYPE = "Contract_Revocation"; // 申請器樂撤銷通知服務
	private static final String TRANS_STATUS = "8"; // 1:已審核
	private static final String UPLOAD_CODE = "037"; // 介接代碼
	private static final String SYSTEM_ID = "eservice"; // 介接代碼

	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {

		TransDao transDao = new TransDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransContractRevocationDao dao = new TransContractRevocationDao();

		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE);

		// 取得申請資料
		List<TransVo> transList = transDao.getTransList(transVo);
		if (transList != null) {
			for (TransVo transVos : transList) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transVos.getTransNum());
				List<TransPolicyVo> transPolicyVos = transPolicyDao.getTransPolicyList(transPolicyVo);
				transPolicyVo = transPolicyVos.get(0);
				if (transPolicyVo != null) {
					TransContractRevocationVo vo = new TransContractRevocationVo();
					vo.setTransNum(transVos.getTransNum());
					List<TransContractRevocationVo> voList = dao.getTransContractRevocation(vo);

					//介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyyMMdd),生效日(系統日yyyyMMdd),是否為信用卡繳費(1),
					//契撤原因(10),匯款戶名(10),銀行代碼(3),分行代碼(4)匯款帳號(16),國際號SwiftCode(16),英文戶名(60)
					
					String rcpTypeCodeFlag ="";
					if(StringUtils.isNotEmpty(voList.get(0).getRcpTypeCodeFlag())) {
						rcpTypeCodeFlag = voList.get(0).getRcpTypeCodeFlag().equals("H") ? "1" : "0";//是否為信用卡繳費(1)
					}else {
						rcpTypeCodeFlag = "0";
					}
					
					StringBuffer sb = new StringBuffer();
					//契撤原因(10)
					/** 保單規劃不符需求 */
					String needsFlag =voList.get(0).getNeedsFlag().equals("1") ? "1" : "";
					/** 經濟因素 */
					String economyFlag =voList.get(0).getEconomyFlag().equals("1") ? "2" : "";
					/** 家人反對 */
					String familyFlag =voList.get(0).getFamilyFlag().equals("1") ? "3" : "";
					/** 對商品認知有誤 */
					String cognitionFlag =voList.get(0).getCognitionFlag().equals("1") ? "4" : "";
					/** 其他 */
					String otherFlag =voList.get(0).getOtherFlag().equals("1") ? "9" : "";
					 
					if(StringUtils.isNotEmpty(needsFlag)) {
						sb.append(needsFlag);
					}
					if(StringUtils.isNotEmpty(economyFlag)) {
						sb.append(economyFlag);
					}
					if(StringUtils.isNotEmpty(familyFlag)) {
						sb.append(familyFlag);
					}
					if(StringUtils.isNotEmpty(cognitionFlag)) {
						sb.append(cognitionFlag);
					}
					if(StringUtils.isNotEmpty(otherFlag)) {
						sb.append(otherFlag);
					}					
					
					// 契撤原因不足10碼，右側留空
					// 0:不是信用卡繳費時, 需用戶填寫退費帳戶資料:匯款戶名,匯款帳號,國際號swiftcode,英文戶名 等資料不足碼右側留空
					// 1:信用卡繳費,是退回原繳費卡號,不需用戶填寫退費帳戶資料,長度都會留空
					if(rcpTypeCodeFlag.equals("0")) {
						txtSb.append(String.format(StringUtils.repeat("%s", 15),
								UPLOAD_CODE, //介接代碼(3
								StringUtil.rpadBlank(transVos.getTransNum(), 12), //申請序號(12)
								StringUtil.rpadBlank(transPolicyVo.getPolicyNo(), 10), //保單號碼(10)
								systemTwDate, //收文日(系統日yyyyMMdd)
								systemTwDate, //生效日(系統日yyyyMMdd)
								rcpTypeCodeFlag,//是否為信用卡繳費(1),
								"1", //審核結果(1) 固定傳1
								StringUtil.rpad(sb.toString(), 10, " "),//契撤原因(10)
								voList.get(0).getLipmId(),
								StringUtil.rpad(voList.get(0).getNeacName(),10 ," ") ,//匯款戶名(10)
								voList.get(0).getBankCode(),//銀行代碼(3)
								voList.get(0).getBranchCode(),//分行代碼(4)
								StringUtil.rpad(voList.get(0).getAccount() , 16 , " "),//匯款帳號(16)
								StringUtil.rpad(voList.get(0).getSwiftCode() , 16 ," "),//國際號SwiftCode(16)
								StringUtil.rpad(voList.get(0).getEnglishName() , 60 ," ")//英文戶名(60)
								));
						txtSb.append("\r\n");
					}else {
						txtSb.append(String.format(StringUtils.repeat("%s", 10),
								UPLOAD_CODE, //介接代碼(3
								StringUtil.rpadBlank(transVos.getTransNum(), 12), //申請序號(12)
								StringUtil.rpadBlank(transPolicyVo.getPolicyNo(), 10), //保單號碼(10)
								systemTwDate, //收文日(系統日yyyyMMdd)
								systemTwDate, //生效日(系統日yyyyMMdd)
								rcpTypeCodeFlag,//是否為信用卡繳費(1),
								"1", //審核結果(1) 固定傳1
								StringUtil.rpad(sb.toString(), 10, " "), //契撤原因(10)
								voList.get(0).getLipmId(),
								StringUtil.rpad("",109," ")//契撤原因(10)
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
