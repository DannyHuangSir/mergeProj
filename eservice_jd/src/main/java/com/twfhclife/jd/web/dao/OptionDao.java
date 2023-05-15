package com.twfhclife.jd.web.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OptionDao {


	List<Map<String, Object>> getPolicyTyps();

	List<Map<String, Object>> getLevelStates();

	List<Map<String, Object>> getPayModeList();

    List<Map<String, Object>> getTransactionCodeList(@Param("categoryCode") String categoryCode);

	List<Map<String, String>> getBankList();
}
