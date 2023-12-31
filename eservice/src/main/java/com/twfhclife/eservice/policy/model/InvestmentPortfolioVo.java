package com.twfhclife.eservice.policy.model;

public class InvestmentPortfolioVo extends PortfolioVo {

    //投资百分比
    private Integer ratio;
    //编号
    private String investmentId;

    //投资轉出單位數
    private Float number;

    //基金公司
    private String company;
    //比例標識描述
    private String ratioMark;
    //比例大小限制
    private Integer ratioMinSize;
    private Integer ratioMaxSize;
    //報單類型
    private String policyType;
    //停泊賬號  N  不是   Y是
    private String depositAccount;
    private String investmentsList;
    private String numInvestment;
    private String bankAccount;
    private String bankName;
    private String bankCode;
    private String branchName;
    private String branchCode;
    private String englishName;
    private String swiftCode;
    private String accountName;

    public String getInvestmentsList() {
        return investmentsList;
    }

    public void setInvestmentsList(String investmentsList) {
        this.investmentsList = investmentsList;
    }

    public String getNumInvestment() {
        return numInvestment;
    }

    public void setNumInvestment(String numInvestment) {
        this.numInvestment = numInvestment;
    }

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

    public String getDepositAccount() {
        return depositAccount;
    }

    public void setDepositAccount(String depositAccount) {
        this.depositAccount = depositAccount;
    }

    public Integer getRatioMinSize() {
        return ratioMinSize;
    }

    public void setRatioMinSize(Integer ratioMinSize) {
        this.ratioMinSize = ratioMinSize;
    }

    public Integer getRatioMaxSize() {
        return ratioMaxSize;
    }

    public void setRatioMaxSize(Integer ratioMaxSize) {
        this.ratioMaxSize = ratioMaxSize;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getRatioMark() {
        return ratioMark;
    }

    public void setRatioMark(String ratioMark) {
        this.ratioMark = ratioMark;
    }



    public Float getNumber() {
        return number;
    }

    public void setNumber(Float number) {
        this.number = number;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
