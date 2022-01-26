package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.TransBankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransSurrenderDao;
import com.twfhclife.eservice.onlineChange.model.TransBankInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransSurrenderVo;
import com.twfhclife.eservice.onlineChange.service.ITransSurrenderService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.util.PdfUtil;
import com.twfhclife.generic.util.RptUtils;

/**
 * 紅利提領申請書套印服務.
 * 
 * @author all
 */
@Service
public class TransSurrenderServiceImpl implements ITransSurrenderService {

	private static final Logger logger = LogManager.getLogger(TransSurrenderServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransSurrenderDao transSurrenderDao;

	@Autowired
	private TransBankInfoDao transBankInfoDao;
	
	@Autowired
	private ILilipmService lilipmService;
	
	@Autowired
	private ILilipiService lilipiService;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String policyType = vo.getPolicyType();
					if (!("UC".equals(policyType) || "UH".equals(policyType))) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_N_INVESTMENT_MSG);
					}
				}
			}
		}
	}
	
	/**
	 * 紅利提領申請書套印-查詢.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransSurrenderVo> getTransSurrender(TransSurrenderVo transSurrenderVo) {
		return transSurrenderDao.getTransSurrender(transSurrenderVo);
	}

	/**
	 * 紅利提領申請書套印-新增.
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransSurrender(TransSurrenderVo transSurrenderVo) {
		String transNum = transSurrenderVo.getTransNum();
		String userId = transSurrenderVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.SURRENDER_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_COMPLETE);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transSurrenderVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增紅利提領申請書套印
			transSurrenderDao.insertTransSurrender(transSurrenderVo);
			
			// 新增銀行帳戶資訊
			TransBankInfoVo transBankInfoVo = new TransBankInfoVo();
			transBankInfoVo.setTransNum(transNum);
			transBankInfoVo.setAccountName(transSurrenderVo.getAccountName());
			transBankInfoVo.setBankId(transSurrenderVo.getBankId());
			transBankInfoVo.setBranchId(transSurrenderVo.getBranchId());
			transBankInfoVo.setAccountNo(transSurrenderVo.getAccountNo());
			transBankInfoDao.insertTransBankInfo(transBankInfoVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransSurrender: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 取得紅利提領申請書報表 byte[].
	 * 
	 * @param transSurrenderVo TransSurrenderVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF(TransSurrenderVo transSurrenderVo) {
		byte[] appendPdfByte = null;
		try {
			String bankName = transSurrenderVo.getBankName();
			String branches = transSurrenderVo.getBranchName();
			String accountNo = transSurrenderVo.getAccountNo();
			String mobile = transSurrenderVo.getMobile();
			
			List<String> policyNoList = transSurrenderVo.getPolicyNoList();
			for (String policyNo : policyNoList) {
				RptUtils rptUtils = new RptUtils("bonus.pdf");
				
				String name = "";
				String rocId = "";
				String telHome = "";
				String insured = "";
				
				// 要保人
				LilipmVo lilipmVo = lilipmService.findByPolicyNo(policyNo);
				if (lilipmVo != null) {
					name = lilipmVo.getLipmName1();
					rocId = lilipmVo.getNoHiddenLipmId();
					telHome = lilipmVo.getNoHiddenLipmTelH();
				}
				
				// 被保人
				LilipiVo lilipiVo = lilipiService.findByPolicyNo(policyNo);
				if (lilipiVo != null) {
					insured = lilipiVo.getLipiName();
				}
				
				// 姓名
				rptUtils.txt(name, 12, 1, 143, 743); 
				
				// rocId
				if (!StringUtils.isEmpty(rocId)) {
					if (rocId.length() >= 1) {
						rptUtils.txt(rocId.substring(0, 1), 12, 1, 142, 716);
					}
					if (rocId.length() >= 2) {
						rptUtils.txt(rocId.substring(1, 2), 12, 1, 153.5f, 716);
					}
					if (rocId.length() >= 3) {
						rptUtils.txt(rocId.substring(2, 3), 12, 1, 165, 716);
					}
					if (rocId.length() >= 4) {
						rptUtils.txt(rocId.substring(3, 4), 12, 1, 176.5f, 716);
					}
					if (rocId.length() >= 5) {
						rptUtils.txt(rocId.substring(4, 5), 12, 1, 188, 716);
					}
					if (rocId.length() >= 6) {
						rptUtils.txt(rocId.substring(5, 6), 12, 1, 199.5f, 716);
					}
					if (rocId.length() >= 7) {
						rptUtils.txt(rocId.substring(6, 7), 12, 1, 211, 716);
					}
					if (rocId.length() >= 8) {
						rptUtils.txt(rocId.substring(7, 8), 12, 1, 222.5f, 716);
					}
					if (rocId.length() >= 9) {
						rptUtils.txt(rocId.substring(8, 9), 12, 1, 234, 716);
					}
					if (rocId.length() >= 10) {
						rptUtils.txt(rocId.substring(9, 10), 12, 1, 245.5f, 716);
					}
				}
				
				// policyNo
				if (!StringUtils.isEmpty(policyNo)) {
					if (policyNo.length() >= 1) {
						rptUtils.txt(policyNo.substring(0, 1), 12, 1, 142, 689);
					}
					if (policyNo.length() >= 2) {
						rptUtils.txt(policyNo.substring(1, 2), 12, 1, 153.5f, 689);
					}
					if (policyNo.length() >= 3) {
						rptUtils.txt(policyNo.substring(2, 3), 12, 1, 165, 689);
					}
					if (policyNo.length() >= 4) {
						rptUtils.txt(policyNo.substring(3, 4), 12, 1, 176.5f, 689);
					}
					if (policyNo.length() >= 5) {
						rptUtils.txt(policyNo.substring(4, 5), 12, 1, 188, 689);
					}
					if (policyNo.length() >= 6) {
						rptUtils.txt(policyNo.substring(5, 6), 12, 1, 199.5f, 689);
					}
					if (policyNo.length() >= 7) {
						rptUtils.txt(policyNo.substring(6, 7), 12, 1, 211, 689);
					}
					if (policyNo.length() >= 8) {
						rptUtils.txt(policyNo.substring(7, 8), 12, 1, 222.5f, 689);
					}
					if (policyNo.length() >= 9) {
						rptUtils.txt(policyNo.substring(8, 9), 12, 1, 234, 689);
					}
					if (policyNo.length() >= 10) {
						rptUtils.txt(policyNo.substring(9, 10), 12, 1, 245.5f, 689);
					}
				}
				
				// 電話
				rptUtils.txt(telHome, 12, 1, 318, 743);
				
				// 手機
				 rptUtils.txt(mobile, 12, 1, 318, 716);
				
				// 被保險人
				rptUtils.txt(insured, 12, 1, 318, 689);
				
				// 銀行名
				rptUtils.txt(bankName.replace("商業", "").replace("銀行", ""), 12, 1, 185, 562);
				
				// 分行名
				rptUtils.txt(branches.replace("分行", ""), 12, 1, 300, 562);
				
				// 帳號
				if (!StringUtils.isEmpty(accountNo)) {
					if (accountNo.length() >= 1) {
						rptUtils.txt(accountNo.substring(0, 1), 16, 1, 182, 537);
					}
					if (accountNo.length() >= 2) {
						rptUtils.txt(accountNo.substring(1, 2), 16, 1, 211, 537);
					}
					if (accountNo.length() >= 3) {
						rptUtils.txt(accountNo.substring(2, 3), 16, 1, 239, 537);
					}
					if (accountNo.length() >= 4) {
						rptUtils.txt(accountNo.substring(3, 4), 16, 1, 267, 537);
					}
					if (accountNo.length() >= 5) {
						rptUtils.txt(accountNo.substring(4, 5), 16, 1, 295, 537);
					}
					if (accountNo.length() >= 6) {
						rptUtils.txt(accountNo.substring(5, 6), 16, 1, 324, 537);
					}
					if (accountNo.length() >= 7) {
						rptUtils.txt(accountNo.substring(6, 7), 16, 1, 351, 537);
					}
					if (accountNo.length() >= 8) {
						rptUtils.txt(accountNo.substring(7, 8), 16, 1, 375, 537);
					}
					if (accountNo.length() >= 9) {
						rptUtils.txt(accountNo.substring(8, 9), 16, 1, 402, 537);
					}
					if (accountNo.length() >= 10) {
						rptUtils.txt(accountNo.substring(9, 10), 16, 1, 426, 537);
					}
					if (accountNo.length() >= 11) {
						rptUtils.txt(accountNo.substring(10, 11), 16, 1, 452, 537);
					}
					if (accountNo.length() >= 12) {
						rptUtils.txt(accountNo.substring(11, 12), 16, 1, 477, 537);
					}
					if (accountNo.length() >= 13) {
						rptUtils.txt(accountNo.substring(12, 13), 16, 1, 503, 537);
					}
					if (accountNo.length() >= 14) {
						rptUtils.txt(accountNo.substring(13, 14), 16, 1, 532, 537);
					}
				}
				
				byte[] cancelPdfByte = rptUtils.toPdf();
				if (appendPdfByte == null) {
					appendPdfByte = cancelPdfByte;
				} else {
					appendPdfByte = PdfUtil.appendPdf(appendPdfByte, cancelPdfByte);
				}
			}
			
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return appendPdfByte;
	}
	
	/**
	 * 紅利提領申請書套印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransSurrenderVo getTransSurrenderDetail(String transNum) {
		TransSurrenderVo qryVo = new TransSurrenderVo();
		qryVo.setTransNum(transNum);
		List<TransSurrenderVo> detailList = transSurrenderDao.getTransSurrender(qryVo);
		
		TransSurrenderVo detailVo = new TransSurrenderVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}