package com.twfhclife.generic.api_model;

import java.io.Serializable;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.*;
import org.hibernate.validator.constraints.NotEmpty;

public class TransAddRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查詢的系統代號
	 */
	@NotEmpty(message = "sysId cannot empty!")
	private String sysId;

	/**
	 * 查詢的使用者代號
	 */
	@NotEmpty(message = "userId cannot empty!")
	private String userId;
	
	private String transType;
	
	private TransAnnuityMethodVo transAnnuityMethodVo;
	
	private TransBeneficiaryVo transBeneficiaryVo;
	
	private TransBounsVo transBounsVo;
	
	private CancelAuthVo cancelAuthVo;
	
	private TransCancelContractVo transCancelContractVo;
	
	private TransCreditCardDateVo transCreditCardDateVo;
	
	private TransCushionVo transCushionVo;
	
	private TransFundNotificationVo transFundNotificationVo;
	
	private TransLoanVo transLoanVo;
	
	private MaturityVo maturityVo;
	
	private TransPaymethodVo transPaymethodVo;
	
	private TransPaymodeVo transPaymodeVo;
	
	private TransReducePolicyVo transReducePolicyVo;
	
	private TransRenewReduceVo transRenewReduceVo;
	
	private TransRewardVo transRewardVo;
	
	private TransSurrenderVo transSurrenderVo;
	
	private TransCertPrintVo transCertPrintVo;
	
	private TransChangeAccountVo transChangeAccountVo;
	
	private TransContactInfoVo transContactInfoVo;
	
	private TransContactInfoDtlVo transContactInfoDtlVo;
	
	private TransResendPolicyVo transResendPolicyVo;
	
	private TransValuePrintVo transValuePrintVo;
	
	private List<TransExtendAttrVo> transExtendAttrList;
	
	private TransRiskLevelVo transRiskLevelVo;
	
	private TransPolicyHolderProfileVo transPolicyHolderProfileVo;
	
	private TransFundSwitchVo transFundSwitchVo;
	
	private TransInsuranceClaimVo transInsuranceClaimVo;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public TransAnnuityMethodVo getTransAnnuityMethodVo() {
		return transAnnuityMethodVo;
	}

	public void setTransAnnuityMethodVo(TransAnnuityMethodVo transAnnuityMethodVo) {
		this.transAnnuityMethodVo = transAnnuityMethodVo;
	}

	public TransBeneficiaryVo getTransBeneficiaryVo() {
		return transBeneficiaryVo;
	}

	public void setTransBeneficiaryVo(TransBeneficiaryVo transBeneficiaryVo) {
		this.transBeneficiaryVo = transBeneficiaryVo;
	}

	public TransBounsVo getTransBounsVo() {
		return transBounsVo;
	}

	public void setTransBounsVo(TransBounsVo transBounsVo) {
		this.transBounsVo = transBounsVo;
	}

	public CancelAuthVo getCancelAuthVo() {
		return cancelAuthVo;
	}

	public void setCancelAuthVo(CancelAuthVo cancelAuthVo) {
		this.cancelAuthVo = cancelAuthVo;
	}

	public TransCancelContractVo getTransCancelContractVo() {
		return transCancelContractVo;
	}

	public void setTransCancelContractVo(TransCancelContractVo transCancelContractVo) {
		this.transCancelContractVo = transCancelContractVo;
	}

	public TransCreditCardDateVo getTransCreditCardDateVo() {
		return transCreditCardDateVo;
	}

	public void setTransCreditCardDateVo(TransCreditCardDateVo transCreditCardDateVo) {
		this.transCreditCardDateVo = transCreditCardDateVo;
	}

	public TransCushionVo getTransCushionVo() {
		return transCushionVo;
	}

	public void setTransCushionVo(TransCushionVo transCushionVo) {
		this.transCushionVo = transCushionVo;
	}

	public TransFundNotificationVo getTransFundNotificationVo() {
		return transFundNotificationVo;
	}

	public void setTransFundNotificationVo(TransFundNotificationVo transFundNotificationVo) {
		this.transFundNotificationVo = transFundNotificationVo;
	}

	public TransLoanVo getTransLoanVo() {
		return transLoanVo;
	}

	public void setTransLoanVo(TransLoanVo transLoanVo) {
		this.transLoanVo = transLoanVo;
	}

	public MaturityVo getMaturityVo() {
		return maturityVo;
	}

	public void setMaturityVo(MaturityVo maturityVo) {
		this.maturityVo = maturityVo;
	}

	public TransPaymethodVo getTransPaymethodVo() {
		return transPaymethodVo;
	}

	public void setTransPaymethodVo(TransPaymethodVo transPaymethodVo) {
		this.transPaymethodVo = transPaymethodVo;
	}

	public TransPaymodeVo getTransPaymodeVo() {
		return transPaymodeVo;
	}

	public void setTransPaymodeVo(TransPaymodeVo transPaymodeVo) {
		this.transPaymodeVo = transPaymodeVo;
	}

	public TransReducePolicyVo getTransReducePolicyVo() {
		return transReducePolicyVo;
	}

	public void setTransReducePolicyVo(TransReducePolicyVo transReducePolicyVo) {
		this.transReducePolicyVo = transReducePolicyVo;
	}

	public TransRenewReduceVo getTransRenewReduceVo() {
		return transRenewReduceVo;
	}

	public void setTransRenewReduceVo(TransRenewReduceVo transRenewReduceVo) {
		this.transRenewReduceVo = transRenewReduceVo;
	}

	public TransRewardVo getTransRewardVo() {
		return transRewardVo;
	}

	public void setTransRewardVo(TransRewardVo transRewardVo) {
		this.transRewardVo = transRewardVo;
	}

	public TransSurrenderVo getTransSurrenderVo() {
		return transSurrenderVo;
	}

	public void setTransSurrenderVo(TransSurrenderVo transSurrenderVo) {
		this.transSurrenderVo = transSurrenderVo;
	}

	public TransCertPrintVo getTransCertPrintVo() {
		return transCertPrintVo;
	}

	public void setTransCertPrintVo(TransCertPrintVo transCertPrintVo) {
		this.transCertPrintVo = transCertPrintVo;
	}

	public TransChangeAccountVo getTransChangeAccountVo() {
		return transChangeAccountVo;
	}

	public void setTransChangeAccountVo(TransChangeAccountVo transChangeAccountVo) {
		this.transChangeAccountVo = transChangeAccountVo;
	}
	
	public TransContactInfoVo getTransContactInfoVo() {
		return transContactInfoVo;
	}

	public void setTransContactInfoVo(TransContactInfoVo transContactInfoVo) {
		this.transContactInfoVo = transContactInfoVo;
	}

	public TransContactInfoDtlVo getTransContactInfoDtlVo() {
		return transContactInfoDtlVo;
	}

	public void setTransContactInfoDtlVo(TransContactInfoDtlVo transContactInfoDtlVo) {
		this.transContactInfoDtlVo = transContactInfoDtlVo;
	}

	public TransResendPolicyVo getTransResendPolicyVo() {
		return transResendPolicyVo;
	}

	public void setTransResendPolicyVo(TransResendPolicyVo transResendPolicyVo) {
		this.transResendPolicyVo = transResendPolicyVo;
	}

	public TransValuePrintVo getTransValuePrintVo() {
		return transValuePrintVo;
	}

	public void setTransValuePrintVo(TransValuePrintVo transValuePrintVo) {
		this.transValuePrintVo = transValuePrintVo;
	}

	public List<TransExtendAttrVo> getTransExtendAttrList() {
		return transExtendAttrList;
	}

	public void setTransExtendAttrList(List<TransExtendAttrVo> transExtendAttrList) {
		this.transExtendAttrList = transExtendAttrList;
	}

	public TransRiskLevelVo getTransRiskLevelVo() {
		return transRiskLevelVo;
	}

	public void setTransRiskLevelVo(TransRiskLevelVo transRiskLevelVo) {
		this.transRiskLevelVo = transRiskLevelVo;
	}

	public TransPolicyHolderProfileVo getTransPolicyHolderProfileVo() {
		return transPolicyHolderProfileVo;
	}

	public void setTransPolicyHolderProfileVo(
			TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		this.transPolicyHolderProfileVo = transPolicyHolderProfileVo;
	}

	public TransFundSwitchVo getTransFundSwitchVo() {
		return transFundSwitchVo;
	}

	public void setTransFundSwitchVo(TransFundSwitchVo transFundSwitchVo) {
		this.transFundSwitchVo = transFundSwitchVo;
	}

	public TransInsuranceClaimVo getTransInsuranceClaimVo() {
		return transInsuranceClaimVo;
	}

	public void setTransInsuranceClaimVo(TransInsuranceClaimVo transInsuranceClaimVo) {
		this.transInsuranceClaimVo = transInsuranceClaimVo;
	}
}