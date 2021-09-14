package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.dao.FundValueDao;
import com.twfhclife.eservice.policy.model.FundValueVo;
import com.twfhclife.eservice.policy.service.IFundValueService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 淨值服務.
 * 
 * @author all
 */
@Service
public class FundValueServiceImpl implements IFundValueService {

	@Autowired
	private FundValueDao fundValueDao;
	
	/**
	 * 淨值-查詢.
	 * 
	 * @param fundValueVo FundValueVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<FundValueVo> getFundValue(FundValueVo fundValueVo) {
		return fundValueDao.getFundValue(fundValueVo);
	}
}