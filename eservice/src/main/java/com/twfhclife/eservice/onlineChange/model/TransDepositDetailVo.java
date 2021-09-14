package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class TransDepositDetailVo extends TransDepositVo {

    private List<Map<String, Object>> details = Lists.newArrayList();

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, Object>> details) {
        this.details = details;
    }
}
