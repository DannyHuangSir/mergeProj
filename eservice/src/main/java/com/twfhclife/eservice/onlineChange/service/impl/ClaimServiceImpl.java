package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.model.ClaimVo;
import com.twfhclife.eservice.onlineChange.service.IClaimService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.generic.util.RptUtils;

/**
 * 理賠申請書套印服務.
 * 
 * @author all
 */
@Service
public class ClaimServiceImpl implements IClaimService {

	private static final Logger logger = LogManager.getLogger(ClaimServiceImpl.class);

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
	 * 取得理賠申請書報表 byte[].
	 * 
	 * @param claimVo ClaimVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF(ClaimVo claimVo) {
		byte[] pdfByte = null;
		try {
			RptUtils rptUtils = new RptUtils("claim.pdf");

			// 要保人
			rptUtils.txt(claimVo.getCustomerName(), 11, 1, 103, 754);

			// 保單號碼
			String policyNo = claimVo.getPolicyNo();
			rptUtils.txt(policyNo, 11, 1, 275, 754); 
			
			// 申請項目
			float f = (float)635.3;
			for (String itemCode : claimVo.getApplyItemList()) {
				if ("01".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.0f, f);
				}
				if ("02".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.0f + (46 * 1), f);
				}
				if ("03".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.5f + (46 * 2), f);
				}
				if ("04".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 103.0f + (46 * 3), f);
				}
				if ("05".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 103.5f + (46 * 4), f);
				}
				if ("06".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 104.5f + (46 * 5), f);
				}
				if ("07".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 105.0f + (46 * 6), f);
				}
				if ("08".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 133.5f + (46 * 7), f);
					rptUtils.txt(claimVo.getItemOther(), 10, 1, 486f, 635.3f);
				}
			}
			
			// 事故者資料-被保險人姓名
			rptUtils.txt(claimVo.getInsuredName(), 11, 1, 103, 724);
			
			// 事故者資料-與主被保險人(員工)之關係
			if ("01".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279, 731);
			}
			if ("02".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279 + 47.5f, 731);
			}
			if ("03".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279, 716);
			}
			if ("04".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279 + 47.5f, 716);
			}
			
			// 事故者資料-身分證號碼
			String rocId = claimVo.getRocId();
			if (!StringUtils.isEmpty(rocId)) {
				if (rocId.length() >= 1) {
					rptUtils.txt(rocId.substring(0, 1), 11, 1, 102.3f, 696);
				}
				if (rocId.length() >= 2) {
					rptUtils.txt(rocId.substring(1, 2), 11, 1, 102.3f + (10.7f * 1), 696);
				}
				if (rocId.length() >= 3) {
					rptUtils.txt(rocId.substring(2, 3), 11, 1, 102.3f + (10.7f * 2), 696);
				}
				if (rocId.length() >= 4) {
					rptUtils.txt(rocId.substring(3, 4), 11, 1, 102.3f + (10.7f * 3), 696);
				}
				if (rocId.length() >= 5) {
					rptUtils.txt(rocId.substring(4, 5), 11, 1, 102.3f + (10.7f * 4), 696);
				}
				if (rocId.length() >= 6) {
					rptUtils.txt(rocId.substring(5, 6), 11, 1, 102.3f + (10.7f * 5), 696);
				}
				if (rocId.length() >= 7) {
					rptUtils.txt(rocId.substring(6, 7), 11, 1, 102.3f + (10.7f * 6), 696);
				}
				if (rocId.length() >= 8) {
					rptUtils.txt(rocId.substring(7, 8), 11, 1, 102.3f + (10.7f * 7), 696);
				}
				if (rocId.length() >= 9) {
					rptUtils.txt(rocId.substring(8, 9), 11, 1, 102.3f + (10.7f * 8), 696);
				}
				if (rocId.length() >= 10) {
					rptUtils.txt(rocId.substring(9, 10), 11, 1, 102.3f + (10.7f * 9), 696);
				}
			}
			
			// 事故者資料-聯絡電話
			rptUtils.txt(claimVo.getTel1(), 11, 1, 275, 696);
			// 事故者資料-出生日期
			rptUtils.txt(claimVo.getBirthDate(), 11, 1, 103, 674.5f);
			// 事故者資料-手機號碼
			rptUtils.txt(claimVo.getMobile(), 11, 1, 275, 674.5f);
			// 事故者資料-職業
			rptUtils.txt(claimVo.getJob(), 11, 1, 460, 674.5f);
			// 事故者資料-聯絡地址
			rptUtils.txt(claimVo.getAddr(), 11, 1, 103, 653);
			
			// 疾病-診斷病名
			rptUtils.breakTxt(claimVo.getDiseaseName(), 10, 1, 103, 603, 10);
			// 疾病-該疾病初診日
			rptUtils.txt(claimVo.getDiagnosisDate(), 10, 1, 103, 562);
			// 疾病-曾就診之醫院
			rptUtils.txt(claimVo.getHospital(), 10, 1, 103, 535);
			
			// 意外-發生時間-年
			rptUtils.txt(claimVo.getAccidentYear(), 10, 1, 275, 603);
			// 意外-發生時間-月
			rptUtils.txt(claimVo.getAccidentMonth(), 10, 1, 274.0f + (35 * 1), 603);
			// 意外-發生時間-日
			rptUtils.txt(claimVo.getAccidentDay(), 10, 1, 267.0f + (35 * 2), 603);
			// 意外-發生時間-時
			rptUtils.txt(claimVo.getAccidentHour(), 10, 1, 257.0f + (35 * 3), 603);
			// 意外-事故地點
			rptUtils.txt(claimVo.getAccidentLocation(), 10, 1, 275, 583);
			// 意外-事故處理單位
			rptUtils.txt(claimVo.getAccidentHandleUnit(), 10, 1, 275, 562);
			// 意外-承辦員警
			rptUtils.txt(claimVo.getPoliceName(), 10, 1, 275, 544);
			// 意外-員警電話
			rptUtils.txt(claimVo.getPoliceTel(), 10, 1, 275, 526);
			// 意外-事故原因及送醫經過詳情
			rptUtils.breakTxt(claimVo.getAccidentReason(), 9, 1, 390, 595, 20);
			
			// 被保險人是否投保別家保險公司之保險
			if ("Y".equals(claimVo.getOtherCompanyInsured())) {
				rptUtils.txt("■", 11, 1, 47, 483.5f);
			}
			if ("N".equals(claimVo.getOtherCompanyInsured())) {
				rptUtils.txt("■", 11, 1, (float) 47 + 32, 483.5f);
			}
			
			// 公司名稱
			rptUtils.txt(claimVo.getOtherInsuCompany(), 10, 1, 138, 490);
			// 保險種類
			rptUtils.txt(claimVo.getOtherInsuType(), 10, 1, 273, 490);
			// 保險金額
			rptUtils.txt(claimVo.getOtherInsuAmout(), 10, 1, 390, 490);
			// 同時申請理賠
			if ("Y".equals(claimVo.getOtherInsuClaim())) {
				rptUtils.txt("■", 11, 1, 468, 490);
			}
			if ("N".equals(claimVo.getOtherInsuClaim())) {
				rptUtils.txt("■", 11, 1, 468 + 32.5f, 490);
			}
			
			// 給付方式
			if ("01".equals(claimVo.getPaymentMethod())) {
				rptUtils.txt("■", 12, 1, 28, 429);
			}
			if ("02".equals(claimVo.getPaymentMethod())) {
				rptUtils.txt("■", 12, 1, 28, 385);
			}
			
			// 匯款帳戶
			rptUtils.txt(claimVo.getAccountName(), 11, 1, 165, 449);
			// 銀行名稱
			rptUtils.txt(claimVo.getBankName(), 11, 1, 165, 429.2f);
			// 分行名稱
			rptUtils.txt(claimVo.getBranchName(), 11, 1, 295, 429.2f);
			
			// 帳號
			String accountNumber = claimVo.getAccountNumber();
			if (!StringUtils.isEmpty(accountNumber)) {
				if (accountNumber.length() >= 1) {
					rptUtils.txt(accountNumber.substring(0, 1), 11, 1, 166.5f, 409);
				}
				if (accountNumber.length() >= 2) {
					rptUtils.txt(accountNumber.substring(1, 2), 11, 1, 166.5f + (13.8f * 1), 409);
				}
				if (accountNumber.length() >= 3) {
					rptUtils.txt(accountNumber.substring(2, 3), 11, 1, 166.5f + (13.8f * 2), 409);
				}
				if (accountNumber.length() >= 4) {
					rptUtils.txt(accountNumber.substring(3, 4), 11, 1, 166.5f + (13.8f * 3), 409);
				}
				if (accountNumber.length() >= 5) {
					rptUtils.txt(accountNumber.substring(4, 5), 11, 1, 166.5f + (13.8f * 4), 409);
				}
				if (accountNumber.length() >= 6) {
					rptUtils.txt(accountNumber.substring(5, 6), 11, 1, 166.5f + (13.8f * 5), 409);
				}
				if (accountNumber.length() >= 7) {
					rptUtils.txt(accountNumber.substring(6, 7), 11, 1, 166.5f + (13.8f * 6), 409);
				}
				if (accountNumber.length() >= 8) {
					rptUtils.txt(accountNumber.substring(7, 8), 11, 1, 166.5f + (13.8f * 7), 409);
				}
				if (accountNumber.length() >= 9) {
					rptUtils.txt(accountNumber.substring(8, 9), 11, 1, 166.5f + (13.8f * 8), 409);
				}
				if (accountNumber.length() >= 10) {
					rptUtils.txt(accountNumber.substring(9, 10), 11, 1, 166.5f + (13.8f * 9), 409);
				}
				if (accountNumber.length() >= 11) {
					rptUtils.txt(accountNumber.substring(10, 11), 11, 1, 166.5f + (13.8f * 10), 409);
				}
				if (accountNumber.length() >= 12) {
					rptUtils.txt(accountNumber.substring(11, 12), 11, 1, 166.5f + (13.8f * 11), 409);
				}
				if (accountNumber.length() >= 13) {
					rptUtils.txt(accountNumber.substring(12, 13), 11, 1, 166.5f + (13.8f * 12), 409);
				}
				if (accountNumber.length() >= 14) {
					rptUtils.txt(accountNumber.substring(13, 14), 11, 1, 166.5f + (13.8f * 13), 409);
				}
			}
			
			// 郵遞區號
			String postalCode = claimVo.getPostalCode();
			if (!StringUtils.isEmpty(postalCode)) {
				if (postalCode.length() >= 1) {
					rptUtils.txt(postalCode.substring(0, 1), 10, 1, 77.5f, 378.6f);
				}
				if (postalCode.length() >= 2) {
					rptUtils.txt(postalCode.substring(1, 2), 10, 1, 77.5f + (12.5f * 1), 378.6f);
				}
				if (postalCode.length() >= 3) {
					rptUtils.txt(postalCode.substring(2, 3), 10, 1, 77f + (12.5f * 2), 378.6f);
				}
				if (postalCode.length() >= 4) {
					rptUtils.txt(postalCode.substring(3, 4), 10, 1, 76f + (12.5f * 3), 378.6f);
				}
				if (postalCode.length() >= 5) {
					rptUtils.txt(postalCode.substring(4, 5), 10, 1, 76f + (12.5f * 4), 378.6f);
				}
			}
			
			// 郵寄地址
			rptUtils.txt(claimVo.getPostalAddr(), 11, 1, 135, 378.6f);

			pdfByte= rptUtils.toPdf();
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}
}