package com.twfhclife.eservice.onlineChange.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransFundConversionVo implements Serializable {

    private Long id;
    //申請序號
    private String transNum;
    //保單號
    private String policyNo;
    //投資標代號
    private String invtNo;
    //投資方式  in 轉入  out  轉出
    private String investmentType;
    //分配比例
    private BigDecimal ratio;
    //分配單位
    private BigDecimal value;
    //	 投資標的名稱
    private String  invtName;
    //	 轉入的投資標的名稱
    private String  fundName;
    private String bankAccount;
    private String bankName;
    private String bankCode;
    private String branchName;
    private String branchCode;
    private String englishName;
    private String swiftCode;
    private String accountName;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getInvtName() {
        return invtName;
    }

    public void setInvtName(String invtName) {
        this.invtName = invtName;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getInvtNo() {
        return invtNo;
    }

    public void setInvtNo(String invtNo) {
        this.invtNo = invtNo;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", transNum=").append(transNum);
        sb.append(", policyNo=").append(policyNo);
        sb.append(", invtNo=").append(invtNo);
        sb.append(", investmentType=").append(investmentType);
        sb.append(", ratio=").append(ratio);
        sb.append(", value=").append(value);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TransFundConversionVo other = (TransFundConversionVo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTransNum() == null ? other.getTransNum() == null : this.getTransNum().equals(other.getTransNum()))
            && (this.getPolicyNo() == null ? other.getPolicyNo() == null : this.getPolicyNo().equals(other.getPolicyNo()))
            && (this.getInvtNo() == null ? other.getInvtNo() == null : this.getInvtNo().equals(other.getInvtNo()))
            && (this.getInvestmentType() == null ? other.getInvestmentType() == null : this.getInvestmentType().equals(other.getInvestmentType()))
            && (this.getRatio() == null ? other.getRatio() == null : this.getRatio().equals(other.getRatio()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTransNum() == null) ? 0 : getTransNum().hashCode());
        result = prime * result + ((getPolicyNo() == null) ? 0 : getPolicyNo().hashCode());
        result = prime * result + ((getInvtNo() == null) ? 0 : getInvtNo().hashCode());
        result = prime * result + ((getInvestmentType() == null) ? 0 : getInvestmentType().hashCode());
        result = prime * result + ((getRatio() == null) ? 0 : getRatio().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }
}