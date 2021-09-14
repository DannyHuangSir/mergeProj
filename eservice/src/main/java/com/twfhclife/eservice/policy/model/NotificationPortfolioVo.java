package com.twfhclife.eservice.policy.model;

public class NotificationPortfolioVo extends PortfolioVo {

    private Integer percentageUp;

    private Integer percentageDown;

    public Integer getPercentageUp() {
        return percentageUp;
    }

    public void setPercentageUp(Integer percentageUp) {
        this.percentageUp = percentageUp;
    }

    public Integer getPercentageDown() {
        return percentageDown;
    }

    public void setPercentageDown(Integer percentageDown) {
        this.percentageDown = percentageDown;
    }
}
