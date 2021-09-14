package com.twfhclife.adm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.FunctionDailyUsageDao;
import com.twfhclife.adm.service.IFunctionDailyUsageService;

@Service
public class FunctionDailyUsageServiceImpl implements IFunctionDailyUsageService {

	@Autowired
	private FunctionDailyUsageDao functionDailyUsageDao;
	
	@Override
	public int addFunctionUsageCount(String funcId, String systemId) {
		/* 更新使用次數 +1 */
		Integer intFuncId = Integer.valueOf(funcId);
		int updateResult = functionDailyUsageDao.updateFunctionDailyUsage(intFuncId, systemId);
		
		/* updateResult = 0, 表示還沒有建立當天該功能統計欄位 */
		if(updateResult == 0) {
			updateResult = functionDailyUsageDao.insertFunctionDailyUsage(intFuncId, systemId);
		}
		
		return updateResult;
	}

}
