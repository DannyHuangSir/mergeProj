package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.util.List;

public class TransDetailVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String transNum;
	
	private String transType;
	
	private List<String> policyNoList;
	
	private TransAnnuityMethodVo transAnnuityMethodVo;
	
	private List<TransBeneficiaryDtlVo> transBeneficiaryDtlList;
	
	private List<TransBeneficiaryOldVo> transBeneficiaryOldList;
	
	private TransBounsVo transBounsVo;
	
	private TransCreditCardDateVo transCreditCardDateVo;
	
	private TransCushionVo transCushionVo;
	
	private List<TransFundNotificationDtlVo> transFundNotificationDtlList;
	
	private TransPaymethodVo transPaymethodVo;
	
	private TransPaymodeVo transPaymodeVo;
	
	private List<TransReducePolicyDtlVo> transReducePolicyDtlList;
	
	private TransRenewReduceVo transRenewReduceVo;
	
	private TransRewardVo transRewardVo;
	
	private TransCertPrintVo transCertPrintVo;
	
	private TransChangeAccountVo transChangeAccountVo;
	
	private TransContactInfoDtlVo transContactInfoDtlVo;
	
	private TransContactInfoOldVo transContactInfoOldVo;
	
	private TransCustInfoVo transCustInfoVo;
	
	private TransValuePrintVo transValuePrintVo;
	
	private TransResendPolicyVo transResendPolicyVo;
	
	private TransRiskLevelVo transRiskLevelVo;
	
	private TransPolicyHolderProfileVo transPolicyHolderProfileVo;
	
	private TransFundSwitchVo transFundSwitchVo;
	
	private TransInsuranceClaimVo transInsuranceClaimVo;

	public String getTransNum() {
		return transNum;
	}

	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public List<String> getPolicyNoList() {
		return policyNoList;
	}

	public void setPolicyNoList(List<String> policyNoList) {
		this.policyNoList = policyNoList;
	}

	public TransPaymodeVo getTransPaymodeVo() {
		return transPaymodeVo;
	}

	public void setTransPaymodeVo(TransPaymodeVo transPaymodeVo) {
		this.transPaymodeVo = transPaymodeVo;
	}

	public TransAnnuityMethodVo getTransAnnuityMethodVo() {
		return transAnnuityMethodVo;
	}

	public void setTransAnnuityMethodVo(TransAnnuityMethodVo transAnnuityMethodVo) {
		this.transAnnuityMethodVo = transAnnuityMethodVo;
	}

	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlList() {
		return transBeneficiaryDtlList;
	}

	public void setTransBeneficiaryDtlList(List<TransBeneficiaryDtlVo> transBeneficiaryDtlList) {
		this.transBeneficiaryDtlList = transBeneficiaryDtlList;
	}

	public List<TransBeneficiaryOldVo> getTransBeneficiaryOldList() {
		return transBeneficiaryOldList;
	}

	public void setTransBeneficiaryOldList(List<TransBeneficiaryOldVo> transBeneficiaryOldList) {
		this.transBeneficiaryOldList = transBeneficiaryOldList;
	}

	public TransBounsVo getTransBounsVo() {
		return transBounsVo;
	}

	public void setTransBounsVo(TransBounsVo transBounsVo) {
		this.transBounsVo = transBounsVo;
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

	public List<TransFundNotificationDtlVo> getTransFundNotificationDtlList() {
		return transFundNotificationDtlList;
	}

	public void setTransFundNotificationDtlList(List<TransFundNotificationDtlVo> transFundNotificationDtlList) {
		this.transFundNotificationDtlList = transFundNotificationDtlList;
	}

	public TransPaymethodVo getTransPaymethodVo() {
		return transPaymethodVo;
	}

	public void setTransPaymethodVo(TransPaymethodVo transPaymethodVo) {
		this.transPaymethodVo = transPaymethodVo;
	}

	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlList() {
		return transReducePolicyDtlList;
	}

	public void setTransReducePolicyDtlList(List<TransReducePolicyDtlVo> transReducePolicyDtlList) {
		this.transReducePolicyDtlList = transReducePolicyDtlList;
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

	public TransContactInfoDtlVo getTransContactInfoDtlVo() {
		return transContactInfoDtlVo;
	}

	public void setTransContactInfoDtlVo(TransContactInfoDtlVo transContactInfoDtlVo) {
		this.transContactInfoDtlVo = transContactInfoDtlVo;
	}

	public TransContactInfoOldVo getTransContactInfoOldVo() {
		return transContactInfoOldVo;
	}

	public void setTransContactInfoOldVo(TransContactInfoOldVo transContactInfoOldVo) {
		this.transContactInfoOldVo = transContactInfoOldVo;
	}

	public TransCustInfoVo getTransCustInfoVo() {
		return transCustInfoVo;
	}

	public void setTransCustInfoVo(TransCustInfoVo transCustInfoVo) {
		this.transCustInfoVo = transCustInfoVo;
	}

	public TransValuePrintVo getTransValuePrintVo() {
		return transValuePrintVo;
	}

	public void setTransValuePrintVo(TransValuePrintVo transValuePrintVo) {
		this.transValuePrintVo = transValuePrintVo;
	}

	public TransResendPolicyVo getTransResendPolicyVo() {
		return transResendPolicyVo;
	}

	public void setTransResendPolicyVo(TransResendPolicyVo transResendPolicyVo) {
		this.transResendPolicyVo = transResendPolicyVo;
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