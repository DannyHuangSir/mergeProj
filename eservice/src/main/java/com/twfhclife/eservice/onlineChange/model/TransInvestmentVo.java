package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;

import java.util.Date;
import java.util.List;

public class TransInvestmentVo extends AbstractOnlineChangeModelBean {

    private Long id;

    private String transNum;

    private String policyNo;

    private String invtNo;

    private List<InvestmentPortfolioVo> ownInvestments = Lists.newArrayList();

    private Short distributionRatio;

    private Short preDistributionRatio;

    private String investments;

    private String newInvestments;

    private String finalInvestments;

    private String finalInputInvestments;

    //申請日期
    private Date applyDate;
    //投資保單郵件發送頭部參數
    private String title;
    //投資保單郵件發送消息參數
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getFinalInputInvestments() {
        return finalInputInvestments;
    }

    public void setFinalInputInvestments(String finalInputInvestments) {
        this.finalInputInvestments = finalInputInvestments;
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

    public Short getDistributionRatio() {
        return distributionRatio;
    }

    public void setDistributionRatio(Short distributionRatio) {
        this.distributionRatio = distributionRatio;
    }

    public List<InvestmentPortfolioVo> getOwnInvestments() {
        return ownInvestments;
    }

    public void setOwnInvestments(List<InvestmentPortfolioVo> ownInvestments) {
        this.ownInvestments = ownInvestments;
    }

    public String getInvestments() {
        return investments;
    }

    public void setInvestments(String investments) {
        this.investments = investments;
    }

    public String getNewInvestments() {
        return newInvestments;
    }

    public void setNewInvestments(String newInvestments) {
        this.newInvestments = newInvestments;
    }

    public String getFinalInvestments() {
        return finalInvestments;
    }

    public void setFinalInvestments(String finalInvestments) {
        this.finalInvestments = finalInvestments;
    }

    public Short getPreDistributionRatio() {
        return preDistributionRatio;
    }

    public void setPreDistributionRatio(Short preDistributionRatio) {
        this.preDistributionRatio = preDistributionRatio;
    }
}