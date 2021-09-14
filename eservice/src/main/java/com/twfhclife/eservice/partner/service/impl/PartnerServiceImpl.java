package com.twfhclife.eservice.partner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.partner.dao.PartnerDao;
import com.twfhclife.eservice.partner.model.PartnerVo;
import com.twfhclife.eservice.partner.service.IPartnerService;
import com.twfhclife.eservice.policy.dao.AgentDao;
import com.twfhclife.eservice.policy.model.AgentVo;
import com.twfhclife.generic.service.IUnicodeService;

@Service
public class PartnerServiceImpl implements IPartnerService {

	@Autowired
	private PartnerDao partnerDao;

	@Autowired
	private AgentDao agentDao;

	@Autowired
	protected IUnicodeService unicodeService;

	/**
	 * 取得保代下及內部人員的所有保單清單.
	 * 
	 * @param rocId 要保人身份證字號
	 * @param policyNo 保單號碼
	 * @param agentCode 代理人代碼
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保代下的使用者的所有保單清單
	 */
	@Override
	public List<PartnerVo> getPartnerPolicyPageList(String rocId, String policyNo, String agentCode, int pageNum,
			int pageSize) {
		List<PartnerVo> list = partnerDao.getPartnerPolicyPageList(rocId, policyNo, agentCode, pageNum, pageSize);
		for (PartnerVo vo : list) {
			vo.setInsuredNameBase64(unicodeService.convertString2Unicode(vo.getInsuredName()));
		}
		return partnerDao.getPartnerPolicyPageList(rocId, policyNo, agentCode, pageNum, pageSize);
	}
	
	/**
	 * 取得合作對象.
	 * 
	 * @param agentCode 代理人代碼
	 * @return 回傳合作對象
	 */
	@Override
	public List<AgentVo> getAgentOptionList(String agentCode) {
		return agentDao.getAgentOptionList(agentCode);
	}
}