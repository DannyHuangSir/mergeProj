package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyHolderProfileDao;
import com.twfhclife.eservice.onlineChange.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice.onlineChange.service.IPolicyHolderProfileService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 保戶基本資料更新服務.
 * 
 * @author all
 */
@Service
public class PolicyHolderProfileServiceImpl implements IPolicyHolderProfileService {

	private static final Logger logger = LogManager.getLogger(PolicyHolderProfileServiceImpl.class);

	@Autowired
	private ITransService transService;
	
	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransPolicyHolderProfileDao transPolicyHolderProfileDao;

	@Override
	public List<TransPolicyHolderProfileVo> getPolicyHolderProfileList(TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		return transPolicyHolderProfileDao.getPolicyHolderProfileList(transPolicyHolderProfileVo);
	}

	@Transactional
	@Override
	public int insertPolicyHolderProfile(TransPolicyHolderProfileVo transPolicyHolderProfileVo) {
		String userId = transPolicyHolderProfileVo.getUserId();
		
		int result = 0;
		
		try {
			TransVo transVo = new TransVo();
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.POLICY_HOLDER_PROFILE_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			
			for (String policyNo : transPolicyHolderProfileVo.getPolicyNoList()) {
				// 新增線上申請主檔
				String transNum = transService.getTransNum();
				transVo.setTransNum(transNum);				
				transDao.insertTrans(transVo);
				
				// 新增保單號碼
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
				
				// 新增保戶基本資料更新檔
				transPolicyHolderProfileVo.setTransNum(transNum);
				transPolicyHolderProfileDao.insertPolicyHolderProfile(transPolicyHolderProfileVo);
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertPolicyHolderProfile: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	@Override
	public TransPolicyHolderProfileVo getPolicyHolderProfileDetail(String transNum) {
		TransPolicyHolderProfileVo qryVo = new TransPolicyHolderProfileVo();
		qryVo.setTransNum(transNum);
		List<TransPolicyHolderProfileVo> transPolicyHolderProfileList = transPolicyHolderProfileDao.getPolicyHolderProfileList(qryVo);
		
		TransPolicyHolderProfileVo detailVo = new TransPolicyHolderProfileVo();
		if (transPolicyHolderProfileList != null && transPolicyHolderProfileList.size() > 0) {
			detailVo = transPolicyHolderProfileList.get(0);
		}
		return detailVo;
	}

	@Override
	public List<String> getAddCodeByLipmId(String lipmId) {
		return transPolicyHolderProfileDao.getAddCodeByLipmId(lipmId);
	}
}