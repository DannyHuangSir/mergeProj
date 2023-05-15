package com.twfhclife.jd.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.jd.web.model.ExchangeRateVo;

import java.util.List;

public class ExchangeRateDataResponse {

    public List<ExchangeRateVo> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRateVo> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    private List<ExchangeRateVo> exchangeRates = Lists.newArrayList();
}
