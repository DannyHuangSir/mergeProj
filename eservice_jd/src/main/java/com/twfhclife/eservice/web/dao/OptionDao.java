package com.twfhclife.eservice.web.dao;

import java.util.List;
import java.util.Map;

public interface OptionDao {


	List<Map<String, Object>> getPolicyTyps();

	List<Map<String, Object>> getLevelStates();
}
