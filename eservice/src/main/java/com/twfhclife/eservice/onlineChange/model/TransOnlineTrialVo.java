package com.twfhclife.eservice.onlineChange.model;

public class TransOnlineTrialVo {
	/** 解約金額-金額 */
	private String cancellationAmt;
	/** 解約金額-無法取得原因 */
	private String cancellationMsg;
	/** 滿期金額-金額 */
	private String maturityAmt;
	/** 滿期金額-無法取得原因 */
	private String maturityMsg;
	/** 可借款金額-金額 */
	private String loanAmt;
	/** 可借款金額-無法取得原因 */
	private String loanMsg;
	/** 需還款金額 -金額*/
	private String repaymentAmt;
	/** 需還款金額 -無法取得原因*/
	private String repaymentMsg;
	/** 還款帳號-金額 */
	private String refundAccount;
	/** 還款帳號-無法取得原因 */
	private String refundMsg;
	
	
	public String getCancellationAmt() {
		return cancellationAmt;
	}
	public void setCancellationAmt(String cancellationAmt) {
		this.cancellationAmt = cancellationAmt;
	}
	public String getCancellationMsg() {
		return cancellationMsg;
	}
	public void setCancellationMsg(String cancellationMsg) {
		this.cancellationMsg = cancellationMsg;
	}
	public String getMaturityAmt() {
		return maturityAmt;
	}
	public void setMaturityAmt(String maturityAmt) {
		this.maturityAmt = maturityAmt;
	}
	public String getMaturityMsg() {
		return maturityMsg;
	}
	public void setMaturityMsg(String maturityMsg) {
		this.maturityMsg = maturityMsg;
	}
	public String getLoanAmt() {
		return loanAmt;
	}
	public void setLoanAmt(String loanAmt) {
		this.loanAmt = loanAmt;
	}
	public String getLoanMsg() {
		return loanMsg;
	}
	public void setLoanMsg(String loanMsg) {
		this.loanMsg = loanMsg;
	}
	public String getRepaymentAmt() {
		return repaymentAmt;
	}
	public void setRepaymentAmt(String repaymentAmt) {
		this.repaymentAmt = repaymentAmt;
	}
	public String getRepaymentMsg() {
		return repaymentMsg;
	}
	public void setRepaymentMsg(String repaymentMsg) {
		this.repaymentMsg = repaymentMsg;
	}
	public String getRefundAccount() {
		return refundAccount;
	}
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	public String getRefundMsg() {
		return refundMsg;
	}
	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}
}
