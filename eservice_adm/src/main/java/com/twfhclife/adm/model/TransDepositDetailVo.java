package com.twfhclife.adm.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class TransDepositDetailVo extends TransDepositVo {

    private String productName;

    private String applyItem;

    public String getApplyItem() {
        return applyItem;
    }

    public void setApplyItem(String applyItem) {
        this.applyItem = applyItem;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private List<Map<String, Object>> details = Lists.newArrayList();

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, Object>> details) {
        this.details = details;
    }
}
