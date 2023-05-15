package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PolicyTypeListResponse implements Serializable {
    private List<Map<String, String>> policyTypeList = Lists.newArrayList();

    public List<Map<String, String>> getPolicyTypeList() {
        return policyTypeList;
    }

    public void setPolicyTypeList(List<Map<String, String>> policyTypeList) {
        this.policyTypeList = policyTypeList;
    }
}
