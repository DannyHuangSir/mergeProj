package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.RcdcChangeDao;
import com.twfhclife.eservice.policy.model.RcdcChangeVo;
import com.twfhclife.eservice.policy.service.IRcdcChangeService;

/**
 * 保單紅利服務.
 * 
 * @author all
 */
@Service
public class RcdcChangeServiceImpl implements IRcdcChangeService {

	@Autowired
	private RcdcChangeDao rcdcChangeDao;
	
	
	/**
	 * 契約變更-查詢(根據保單號碼).
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<RcdcChangeVo> findByPolicyNo(String policyNo) {
		return rcdcChangeDao.findByPolicyNo(policyNo);
	}
	
}