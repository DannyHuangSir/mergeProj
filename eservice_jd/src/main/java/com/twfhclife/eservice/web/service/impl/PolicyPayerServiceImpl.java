package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.shouxian.dao.PolicyPayerDao;
import com.twfhclife.eservice.web.model.PolicyPayerVo;
import com.twfhclife.eservice.web.service.IPolicyPayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 付款人服務.
 * 
 * @author all
 */
@Service
public class PolicyPayerServiceImpl implements IPolicyPayerService {


	@Autowired
	public IUnicodeService unicodeService;

	@Autowired
	private PolicyPayerDao policyPayerDao;
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyPayerVo PolicyPayerVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<PolicyPayerVo> getPolicyPayerList(PolicyPayerVo policyPayerVo) {
		return policyPayerDao.getPolicyPayerList(policyPayerVo);
	}
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@Override
	public PolicyPayerVo getPolicyPayer(String policyNo) {
		PolicyPayerVo payer = policyPayerDao.getPolicyPayer(policyNo);
		if (payer != null) {
			payer.setPayerNameBase64(unicodeService.convertString2Unicode(payer.getPayerName()));
		}
		return payer;
	}
}