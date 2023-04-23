package com.twfhclife.eservice.web.service;

import java.util.List;
import java.util.Map;

public interface IOptionService {


	List<Map<String, Object>> getLevelStates();

    List<Map<String, Object>> getPayModeList();

    List<Map<String, Object>> getTransactionCodeList();


    List<Map<String, String>> getBankList();

    List<Map<String, String>> getPolicyTypeList();
}
