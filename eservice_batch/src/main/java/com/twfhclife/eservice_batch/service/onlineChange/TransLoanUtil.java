package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.OnlineChangeInfoDao;
import com.twfhclife.eservice_batch.dao.TransBankInfoDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransLoanDao;
import com.twfhclife.eservice_batch.model.OnlineChangeInfoVo;
import com.twfhclife.eservice_batch.model.TransBankInfoVo;
import com.twfhclife.eservice_batch.model.TransLoanVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 保單借款(線上):026
 * 
 * @author all
 */
public class TransLoanUtil {

	private static final Logger logger = LogManager.getLogger(TransLoanUtil.class);
	private static final String TRANS_TYPE = "LOAN_NEW";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "026"; // 介接代碼
	
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
		TransLoanDao transLoanDao = new TransLoanDao();
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
				
				// 取得保單借款匯款帳號資訊
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
//				if ("700".equals(bankId)) {
//					branchId = "";
//				}
				logger.info("TransNum : {}'s bankId : {}, branchId : {}, accountNo : {}, accountName : {}",
						transNum, bankId, branchId, accountNo, accountName);

				// 取得保單借款資料
				TransLoanVo transLoanVo = new TransLoanVo();
				transLoanVo.setTransNum(transNum);
				List<TransLoanVo> transLoanList = transLoanDao.getTransLoanList(transLoanVo);
				if (transLoanList != null && transLoanList.size() > 0) {
					transLoanVo = transLoanList.get(0);
				}
				
				// 取得保單號碼,身分證號
				OnlineChangeInfoDao onlineChangeInfoDao = new OnlineChangeInfoDao();
				OnlineChangeInfoVo onlineChangeInfoVo = onlineChangeInfoDao.getOnlineChangeInfoByTransNum(transNum);
				if (onlineChangeInfoVo != null) {
					String policyNo = onlineChangeInfoVo.getPolicyNo();
					String rocId = onlineChangeInfoVo.getLipmId();
					logger.info("TransNum : {}'s policyNo : {}, rocId : {}", transNum, policyNo, rocId);
					
					// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),身分證號(10),匯款戶名(10),銀行代碼(3),分行代碼(4)匯款帳號(16),貸款金額(10)
					txtSb.append(String.format(StringUtils.repeat("%s", 11), 
							UPLOAD_CODE,
							StringUtil.rpadBlank(transNum, 12), 
							StringUtil.rpadBlank(policyNo, 10),
							systemTwDate,
							systemTwDate,
							StringUtil.rpadBlank(rocId, 10),
							StringUtil.rpadBlank(accountName, 10),
							StringUtil.rpadBlank(bankId, 3),
							StringUtil.rpadBlank(branchId, 4),
							StringUtil.rpadBlank(accountNo, 16),
							StringUtil.lpad(transLoanVo.getLoanAmount().toString(), 10, "0")
					));
					txtSb.append("\r\n");
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
	 * @param transLoanVo
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransLoanVo transLoanVo) {
		if (transLoanVo != null) {
			String format = "申請借款金額：%s\r\n匯款戶名：%s\r\n銀行名稱(代碼)：%s\r\n分行名稱(代碼)：%s\r\n匯款帳號：%s\r\n";
			afterSb.append(String.format(format
					, MyStringUtil.nullToString(transLoanVo.getLoanAmount())
					, MyStringUtil.nullToString(transLoanVo.getTransBankInfoVo().getAccountName())
					, MyStringUtil.nullToString(transLoanVo.getTransBankInfoVo().getBankName() + "(" + transLoanVo.getTransBankInfoVo().getBankId() + ")")
					, MyStringUtil.nullToString(transLoanVo.getTransBankInfoVo().getBranchName() + "(" + transLoanVo.getTransBankInfoVo().getBranchId() + ")")
					, MyStringUtil.nullToString(transLoanVo.getTransBankInfoVo().getAccountNo())
					));
		}
	}
}