package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransBankInfoDao;
import com.twfhclife.eservice_batch.dao.TransChangeAccountDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransBankInfoVo;
import com.twfhclife.eservice_batch.model.TransChangeAccountVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 匯款帳號變更 (CS17):023
 * 
 * @author all
 */
public class TransChangeAccountUtil {

	private static final Logger logger = LogManager.getLogger(TransChangeAccountUtil.class);
	private static final String TRANS_TYPE = "CHANGE_PAY_ACCOUNT";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "023"; // 介接代碼
	
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
		TransChangeAccountDao changeAccountDao = new TransChangeAccountDao();
		TransBankInfoDao bankInfoDao = new TransBankInfoDao();
		
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
				
				// 取得新的變更匯款帳號資訊
				String bankId = "";
				String branchId = "";
				String accountNo = "";
				String accountName = "";
				
				TransBankInfoVo qryBankInfoVo = new TransBankInfoVo();
				qryBankInfoVo.setTransNum(transNum);
				List<TransBankInfoVo> bankInfoList = bankInfoDao.getTransBankInfoList(qryBankInfoVo);
				if (bankInfoList != null && bankInfoList.size() > 0) {
					TransBankInfoVo transBankInfoVo = bankInfoList.get(0);
					bankId = transBankInfoVo.getBankId();
					branchId = transBankInfoVo.getBranchId();
					accountNo = transBankInfoVo.getAccountNo();
					accountName = transBankInfoVo.getAccountName();
				}
				
				// 郵局代碼時，分行代碼清空
				if ("700".equals(bankId)) {
					branchId = "";
				}
				// 分行代碼大於3位
				if (!StringUtils.isEmpty(branchId) && branchId.length() > 3) {
					if (branchId.startsWith("0")) {
						branchId = branchId.substring(1, 4);
					}
				}
				logger.info("TransNum's bankId : {}", bankId);
				logger.info("TransNum's branchId : {}", branchId);
				logger.info("TransNum's accountName : {}", accountName);
				
				// 取得新的變更受益類別
				String changeType = ""; // 1:滿期金, 2:生存金
				TransChangeAccountVo qryChangeAccountVo = new TransChangeAccountVo();
				qryChangeAccountVo.setTransNum(transNum);
				List<TransChangeAccountVo> changeAccountList = changeAccountDao.getTransChangeAccountList(qryChangeAccountVo);
				if (changeAccountList != null && changeAccountList.size() > 0) {
					TransChangeAccountVo transChangeAccountVo = changeAccountList.get(0);
					changeType = transChangeAccountVo.getChangeType();
				}
				logger.info("TransNum's changeType : {}", changeType);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// TODO 受益人身分證號(10), 收文日(系統日yyyMMdd)生效日(系統日yyyMMdd)
						String beneficiaryRocid = changeAccountDao.findRocId(changeType, policyNo);
						String swiftCode = "";
						
						// 20211012 調整介接資料為正常, 如下:
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),受益類別(1),
						// 受益人身分證號(10),匯款戶名(10),銀行代碼(3),分行代碼(4)匯款帳號(16),國際號SwiftCode(16)
						/// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),受益類別(1),
						/// 受益人身分證號(10),匯款戶名(10),銀行代碼(3),分行代碼(3)匯款帳號(10),國際號SwiftCode(16)
						txtSb.append(String.format(StringUtils.repeat("%s", 12), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								changeType,
								StringUtil.rpadBlank(beneficiaryRocid, 10),
								StringUtil.rpadBlank(accountName, 10),
								StringUtil.rpadBlank(bankId, 3),
								StringUtil.rpadBlank(branchId, 4),
								StringUtil.rpadBlank(accountNo, 16),
								StringUtil.rpadBlank(swiftCode, 16)
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
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransChangeAccountVo transChangeAccountVo) {
		if (transChangeAccountVo != null) {
			String format = "受益種類： %s\r\n戶名： %s\r\n銀行： %s\r\n分行： %s\r\n局號： %s\r\n帳號： %s";
			String changeTypeCht = "";
			switch(transChangeAccountVo.getChangeType()) {
			case "2":
				changeTypeCht = "滿期金"; 
				break;
			case "3": 
				changeTypeCht = "生存金";
				break;
			case "F":
				changeTypeCht = "紅利給付";
				break;
			}
			if (transChangeAccountVo.getTransBankInfoVo() != null) {
				String strNew = String.format(format
						, changeTypeCht
						, MyStringUtil.nullToString(transChangeAccountVo.getTransBankInfoVo().getAccountName())
						, MyStringUtil.nullToString(transChangeAccountVo.getTransBankInfoVo().getBankName())
						, MyStringUtil.nullToString(transChangeAccountVo.getTransBankInfoVo().getBranchName())
						, MyStringUtil.nullToString(transChangeAccountVo.getTransBankInfoVo().getBranchId())
						, MyStringUtil.nullToString(transChangeAccountVo.getTransBankInfoVo().getAccountNo()));
				afterSb.append(strNew);
			}
		}
	}
}