package com.twfhclife.eservice.policy.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PolicyReversalVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 保單號碼 */
    private String policyNo;

    /** 交易日期 */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
    private Date trDate;

    /** 撥回方式  */
    private String trCode;

    /** 現金金額 */
    private BigDecimal expeNTD;

    /** 單位 */
    private BigDecimal newUnits;

    /** 給付日期 */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
    private Date payDate;

    /**判斷是否為現金**/
    private boolean isExpeNTD = false;
    
    /**判斷是否為單位**/
    private boolean isNewUnits = false;
    
    //---非對應資料庫
    /** 撥回方式畫面文字  撥回方式:00=單位,01=現金 */
    private String trCodeName;

    /** 畫面數字 */
    private BigDecimal trAmount;
    
    /**電匯日**/
    private Date pattDate;


    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Date getTrDate() {
        return trDate;
    }

    public void setTrDate(Date trDate) {
        this.trDate = trDate;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public BigDecimal getExpeNTD() {
        return expeNTD;
    }

    public void setExpeNTD(BigDecimal expeNTD) {
        this.expeNTD = expeNTD;
    }

    public BigDecimal getNewUnits() {
        return newUnits;
    }

    public void setNewUnits(BigDecimal newUnits) {
        this.newUnits = newUnits;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getTrCodeName() {
        return trCodeName;
    }

    public void setTrCodeName(String trCodeName) {
        this.trCodeName = trCodeName;
    }


    public BigDecimal getTrAmount() {
        return trAmount;
    }

    public void setTrAmount(BigDecimal trAmount) {
        this.trAmount = trAmount;
    }

    public boolean getIsExpeNTD() {
		return isExpeNTD;
	}

	public void setIsExpeNTD(boolean isExpeNTD) {
		this.isExpeNTD = isExpeNTD;
	}

	public boolean getIsNewUnits() {
		return isNewUnits;
	}

	public void setIsNewUnits(boolean isNewUnits) {
		this.isNewUnits = isNewUnits;
	}

	public Date getPattDate() {
		return pattDate;
	}

	public void setPattDate(Date pattDate) {
		this.pattDate = pattDate;
	}

	//調整欄位資料
    public void fixData(){
        if("00".equals(trCode)){
            trCodeName = "單位";
            isNewUnits = true;
//            trAmount = newUnits;
        }else if("01".equals(trCode)){
            trCodeName = "現金";
//            trAmount = expeNTD;
            isExpeNTD = true;
            trDate = payDate;
        }else{
            trCodeName = "";
            trAmount = new BigDecimal(0);
        }
    }

}
