package com.twfhclife.eservice.api.elife.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.domain.TransDetailVo;
import com.twfhclife.eservice.api.elife.service.ITransHistoryDetailService;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice.onlineChange.model.TransBounsVo;
import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransChangeAccountVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoOldVo;
import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;
import com.twfhclife.eservice.onlineChange.model.TransCushionVo;
import com.twfhclife.eservice.onlineChange.model.TransCustInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransFundSwitchVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymethodVo;
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransRenewReduceVo;
import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;
import com.twfhclife.eservice.onlineChange.model.TransRewardVo;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;
import com.twfhclife.eservice.onlineChange.service.IPolicyHolderProfileService;
import com.twfhclife.eservice.onlineChange.service.ITransAnnuityMethodService;
import com.twfhclife.eservice.onlineChange.service.ITransBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransBounsService;
import com.twfhclife.eservice.onlineChange.service.ITransCertPrintService;
import com.twfhclife.eservice.onlineChange.service.ITransChangeAccountService;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransCreditCardDateService;
import com.twfhclife.eservice.onlineChange.service.ITransCushionService;
import com.twfhclife.eservice.onlineChange.service.ITransCustInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransFundNotificationService;
import com.twfhclife.eservice.onlineChange.service.ITransFundSwitchService;
import com.twfhclife.eservice.onlineChange.service.ITransPaymethodService;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
import com.twfhclife.eservice.onlineChange.service.ITransReducePolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransRenewReduceService;
import com.twfhclife.eservice.onlineChange.service.ITransResendPolicyService;
import com.twfhclife.eservice.onlineChange.service.ITransRewardService;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.service.ITransValuePrintService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 線上申請明細服務.
 * 
 * @author all
 */
@Service
public class TransHistoryDetailServiceImpl implements ITransHistoryDetailService {

	private static final Logger logger = LogManager.getLogger(TransHistoryDetailServiceImpl.class);
	
	@Autowired
	public TransDao transDao;
	@Autowired
	public TransPolicyDao transPolicyDao;
	@Autowired
	private ITransAnnuityMethodService transAnnuityMethodService;
	@Autowired
	private ITransBeneficiaryService transBeneficiaryService;
	@Autowired
	private ITransBounsService transBounsService;
	@Autowired
	private ITransCreditCardDateService transCreditCardDateService;
	@Autowired
	private ITransCushionService transCushionService;
	@Autowired
	private ITransFundNotificationService transFundNotificationService;
	@Autowired
	private ITransPaymethodService transPaymethodService;
	@Autowired
	private ITransPaymodeService transPaymodeService;
	@Autowired
	private ITransReducePolicyService transReducePolicyService;
	@Autowired
	private ITransRenewReduceService transRenewReduceService;
	@Autowired
	private ITransRewardService transRewardService;
	@Autowired
	private ITransCertPrintService transCertPrintService;
	@Autowired
	private ITransChangeAccountService transChangeAccountService;
	@Autowired
	private ITransContactInfoService transContactInfoService;
	@Autowired
	private ITransCustInfoService transCustInfoService;
	@Autowired
	private ITransValuePrintService transValuePrintService;
	@Autowired
	private ITransResendPolicyService transResendPolicyService;
	@Autowired
	private ITransRiskLevelService transRiskLevelService;
	@Autowired
	private IPolicyHolderProfileService policyHolderProfileService;
	@Autowired
	private ITransFundSwitchService transFundSwitchService;

	/**
	 * 查詢線上申請紀錄明細.
	 * 
	 * @param transNumList 交易序號清單
	 * @return 回傳申請明細
	 */
	@Override
	public List<TransDetailVo> getTransHistoryDetailList(List<String> transNumList) {
		List<TransDetailVo> transHistoryDetailList = new ArrayList<>();
		for (String transNum : transNumList) {
			TransDetailVo detailVo = new TransDetailVo();
			detailVo.setTransNum(transNum);
			
			// 取得交易類型
			TransVo transVo = transDao.findByTransNum(transNum);
			if (transVo != null) {
				detailVo.setTransType(transVo.getTransType());
			}
			logger.debug("Find transNum[{}] transType: {}", transNum, detailVo.getTransType());
			
			// 取得交易序號對應的保單號碼
			detailVo.setPolicyNoList(transPolicyDao.getTransPolicyNoList(transNum));
			
			String transType = detailVo.getTransType();
			switch (transType) {
			case TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE:
				// 變更年金給付方式
				TransAnnuityMethodVo transAnnuityMethodVo = transAnnuityMethodService.getTransAnnuityMethodDetail(transNum);
				detailVo.setTransAnnuityMethodVo(transAnnuityMethodVo);
				break;
			case TransTypeUtil.BENEFICIARY_PARAMETER_CODE:
				// 變更受益人
				BigDecimal beneficiaryId = transBeneficiaryService.getTransBeneficiaryId(transNum);
				List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = transBeneficiaryService.getTransBeneficiaryDtlDetail(beneficiaryId);
				List<TransBeneficiaryOldVo> transBeneficiaryOldList = transBeneficiaryService.getTransBeneficiaryOldDetail(beneficiaryId);
				
				detailVo.setTransBeneficiaryDtlList(transBeneficiaryDtlList);
				detailVo.setTransBeneficiaryOldList(transBeneficiaryOldList);
				break;
			case TransTypeUtil.BONUS_PARAMETER_CODE:
				// 變更紅利選擇權
				TransBounsVo transBounsVo = transBounsService.getTransBounsDetail(transNum);
				detailVo.setTransBounsVo(transBounsVo);
				break;
			case TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE:
				// 變更信用卡效期
				TransCreditCardDateVo transCreditCardDateVo = transCreditCardDateService.getTransCreditCardDateDetail(transNum);
				detailVo.setTransCreditCardDateVo(transCreditCardDateVo);
				break;
			case TransTypeUtil.CUSHION_PARAMETER_CODE:
				// 自動墊繳選擇權
				TransCushionVo transCushionVo = transCushionService.getTransCushionDetail(transNum);
				detailVo.setTransCushionVo(transCushionVo);
				break;
			case TransTypeUtil.FUND_NOTIFICATION_PARAMETER_CODE:
				// 設定停損停利通知
				BigDecimal transFundNotificationId = transFundNotificationService.getTransFundNotificationId(transNum);
				List<TransFundNotificationDtlVo> transFundNotificationDtlList = transFundNotificationService
						.getTransFundNotificationDtlDetail(transNum, transFundNotificationId);
				detailVo.setTransFundNotificationDtlList(transFundNotificationDtlList);
				break;
			case TransTypeUtil.PAYMETHOD_PARAMETER_CODE:
				// 變更收費管道
				TransPaymethodVo transPaymethodVo = transPaymethodService.getTransPaymethodDetail(transNum);
				detailVo.setTransPaymethodVo(transPaymethodVo);
				break;
			case TransTypeUtil.PAYMODE_PARAMETER_CODE:
				// 變更繳別
				TransPaymodeVo transPaymodeVo = transPaymodeService.getTransPaymodeDetail(transNum);
				detailVo.setTransPaymodeVo(transPaymodeVo);
				break;
			case TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE:
				// 減少保險金額
				BigDecimal transReducePolicyId = transReducePolicyService.getTransReducePolicyId(transNum);
				List<TransReducePolicyDtlVo> transReducePolicyDtlList = transReducePolicyService
						.getTransReducePolicyDtlDetail(transReducePolicyId);
				detailVo.setTransReducePolicyDtlList(transReducePolicyDtlList);
				break;
			case TransTypeUtil.RENEW_PARAMETER_CODE: case TransTypeUtil.REDUCE_PARAMETER_CODE:
				// 展期,減額
				TransRenewReduceVo transRenewReduceVo = transRenewReduceService.getTransRenewReduceDetail(transNum);
				detailVo.setTransRenewReduceVo(transRenewReduceVo);
				break;
			case TransTypeUtil.REWARD_PARAMETER_CODE:
				// 變更增值回饋分享金領取方式
				TransRewardVo transRewardVo = transRewardService.getTransRewardDetail(transNum);
				detailVo.setTransRewardVo(transRewardVo);
				break;
			case TransTypeUtil.CERT_PRINT_PARAMETER_CODE:
				// 投保證明列印
				TransCertPrintVo transCertPrintVo = transCertPrintService.getTransCertPrintDetail(transNum);
				detailVo.setTransCertPrintVo(transCertPrintVo);
				break;
			case TransTypeUtil.CHANGE_PAY_ACCOUNT_PARAMETER_CODE:
				// 匯款帳號變更
				TransChangeAccountVo transChangeAccountVo = transChangeAccountService.getTransChangeAccountDetail(transNum);
				detailVo.setTransChangeAccountVo(transChangeAccountVo);
				break;
			case TransTypeUtil.CONTACT_INFO_PARAMETER_CODE:
				// 變更保單聯絡資料
				BigDecimal contactInfoId = transContactInfoService.getTransContactInfoId(transNum);
				TransContactInfoDtlVo transContactInfoDtlVo = transContactInfoService.getTransContactInfoDtlDetail(contactInfoId);
				TransContactInfoOldVo transContactInfoOldVo = transContactInfoService.getTransContactInfoOldDetail(contactInfoId);
				
				detailVo.setTransContactInfoDtlVo(transContactInfoDtlVo);
				detailVo.setTransContactInfoOldVo(transContactInfoOldVo);
				break;
			case TransTypeUtil.CUST_INFO_PARAMETER_CODE:
				// 變更基本資料
				TransCustInfoVo transCustInfoVo = transCustInfoService.getTransCustInfoDetail(transNum);
				detailVo.setTransCustInfoVo(transCustInfoVo);
				break;
			case TransTypeUtil.VALUE_PRINT_PARAMETER_CODE:
				// 保單價值列印
				TransValuePrintVo transValuePrintVo = transValuePrintService.getTransValuePrintDetail(transNum);
				detailVo.setTransValuePrintVo(transValuePrintVo);
				break;
			case TransTypeUtil.POLICY_RESEND_PARAMETER_CODE:
				// 補發保單
				TransResendPolicyVo transResendPolicyVo = transResendPolicyService.getTransResendPolicyDetail(transNum);
				detailVo.setTransResendPolicyVo(transResendPolicyVo);
				break;
			case TransTypeUtil.RISK_LEVEL_PARAMETER_CODE:
				// 變更風險屬性
				TransRiskLevelVo transRiskLevelVo = transRiskLevelService.getTransRiskLevelDetail(transNum);
				detailVo.setTransRiskLevelVo(transRiskLevelVo);
				break;
			case TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE:
				// 保戶基本資料更新
				TransPolicyHolderProfileVo transPolicyHolderProfileVo = policyHolderProfileService.getPolicyHolderProfileDetail(transNum);
				detailVo.setTransPolicyHolderProfileVo(transPolicyHolderProfileVo);
				break;
			case TransTypeUtil.FUND_SWITCH_PARAMETER_CODE:
				// 變更投資標的及配置比例
				TransFundSwitchVo transFundSwitchVo = transFundSwitchService.getTransFundSwitchDetail(transNum);
				detailVo.setTransFundSwitchVo(transFundSwitchVo);
				break;
			}
			transHistoryDetailList.add(detailVo);
		}
		
		return transHistoryDetailList;
	}
}
