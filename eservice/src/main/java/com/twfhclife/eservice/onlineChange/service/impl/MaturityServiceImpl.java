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
import com.twfhclife.eservice.onlineChange.model.MaturityVo;
import com.twfhclife.eservice.onlineChange.service.IMaturityService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.util.RptUtils;

/**
 * 滿期套印服務.
 * 
 * @author all
 */
@Service
public class MaturityServiceImpl implements IMaturityService {

	private static final Logger logger = LogManager.getLogger(MaturityServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

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
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					}
				}
			}
		}
	}

	/**
	 * 取得滿期報表 byte[].
	 * 
	 * @param maturityVo MaturityVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF(MaturityVo maturityVo) {
		byte[] pdfByte = null;
		try {
			RptUtils rptUtils = new RptUtils("maturity.pdf");

			// 保單號碼
			String policyNo = maturityVo.getPolicyNo();
			rptUtils.txt(policyNo, 11, 1, 118, 557);
			
			// 投保始期
			rptUtils.txt(maturityVo.getEffectiveDate(), 11, 1, 358, 557);

			// 要保人
			rptUtils.txt(maturityVo.getCustomerName(), 11, 1, 118, 535);
			
			// 保險金額
			rptUtils.txt(maturityVo.getMainAmount() + "", 11, 1, 358, 535);
			
			// 被保險人
			rptUtils.txt(maturityVo.getInsuredName(), 11, 1, 118, 512);
			
			// 滿期日期
			rptUtils.txt(maturityVo.getEffectiveDate(), 11, 1, 358, 512);
			
			// 滿期金受益人
			rptUtils.txt(maturityVo.getBeneficiaryName(), 11, 1, 140, 489);
			
			// 滿期金額
			rptUtils.txt("依保單條款約定之滿期金給付", 11, 1, 358, 489);
			
			// 領款方式
			if ("01".equals(maturityVo.getPaymentMethod())) {
				rptUtils.txt("■", 11, 1, 58, 433);
				// 匯款帳戶
				rptUtils.txt(maturityVo.getAccountName(), 11, 1, 175, 451);
				// 銀行名稱
				rptUtils.txt(maturityVo.getBankName(), 11, 1, 105, 410);
				// 分行名稱
				rptUtils.txt(maturityVo.getBranchName(), 11, 1, 195, 410);
				// 帳號
				rptUtils.txt(maturityVo.getAccountNumber(), 11, 1, 105, 385);
			}
			if ("02".equals(maturityVo.getPaymentMethod())) {
				rptUtils.txt("■", 11, 1, 58, 351);
				
				// 郵遞區號
				String postalCode = maturityVo.getPostalCode();
				if (!StringUtils.isEmpty(postalCode)) {
					if (postalCode.length() >= 1) {
						rptUtils.txt(postalCode.substring(0, 1), 10, 1, 189, 351);
					}
					if (postalCode.length() >= 2) {
						rptUtils.txt(postalCode.substring(1, 2), 10, 1, 199, 351);
					}
					if (postalCode.length() >= 3) {
						rptUtils.txt(postalCode.substring(2, 3), 10, 1, 209, 351);
					}
				}
				
				// 郵寄地址
				rptUtils.txt(maturityVo.getPostalAddr(), 11, 1, 230, 351);
			}

			pdfByte = rptUtils.toPdf();
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}
	
	/**
	 * 新增申請主檔.
	 * 
	 * @param maturityVo MaturityVo
	 */
	@Transactional
	@Override
	public int insertTransMaturity(MaturityVo maturityVo) {
		String transNum = maturityVo.getTransNum();
		String userId = maturityVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.MATURITY_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_BEADDED);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			TransPolicyVo transPolicyVo = new TransPolicyVo();
			transPolicyVo.setTransNum(transNum);
			transPolicyVo.setPolicyNo(maturityVo.getPolicyNo());
			transPolicyDao.insertTransPolicy(transPolicyVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransMaturity: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
}