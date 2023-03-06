package com.twfhclife.eservice.api_model;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.web.model.ExchangeRateVo;

import java.math.BigDecimal;
import java.util.Date;
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
