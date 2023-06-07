package com.twfhclife.eservice.onlineChange.model;

public class OptionVo {

    private Long id;

    private Long score;

    private String isMulti;

    private String item;

    private String extraValue;
   
    private String riskFlag;
    
    private String cancelFlag;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(String isMulti) {
        this.isMulti = isMulti;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

	public String getRiskFlag() {
		return riskFlag;
	}

	public void setRiskFlag(String riskFlag) {
		this.riskFlag = riskFlag;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
    
    
}
