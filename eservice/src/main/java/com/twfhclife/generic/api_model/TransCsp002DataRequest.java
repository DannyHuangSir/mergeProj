package com.twfhclife.generic.api_model;

public class TransCsp002DataRequest {
	//保單號碼
		private String lipmInsuNo;
		//查詢日期
		private String date ;
		//滿期
		private boolean maturityType;
		//解約
		private boolean cancellationType;
		//可借款額度
		private boolean loanType;
		//已借款需還款金額
		private boolean repaymentType;
		public String getLipmInsuNo() {
			return lipmInsuNo;
		}
		public void setLipmInsuNo(String lipmInsuNo) {
			this.lipmInsuNo = lipmInsuNo;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public boolean isMaturityType() {
			return maturityType;
		}
		public void setMaturityType(boolean maturityType) {
			this.maturityType = maturityType;
		}
		public boolean isCancellationType() {
			return cancellationType;
		}
		public void setCancellationType(boolean cancellationType) {
			this.cancellationType = cancellationType;
		}
		public boolean isLoanType() {
			return loanType;
		}
		public void setLoanType(boolean loanType) {
			this.loanType = loanType;
		}
		public boolean isRepaymentType() {
			return repaymentType;
		}
		public void setRepaymentType(boolean repaymentType) {
			this.repaymentType = repaymentType;
		}
		
}
