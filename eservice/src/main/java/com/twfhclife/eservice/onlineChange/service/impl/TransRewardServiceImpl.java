package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.dao.TransRewardDao;
import com.twfhclife.eservice.onlineChange.model.TransRewardVo;
import com.twfhclife.eservice.onlineChange.service.ITransRewardService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更增值回饋分享金領取方式服務.
 * 
 * @author all
 */
@Service
public class TransRewardServiceImpl implements ITransRewardService {

	private static final Logger logger = LogManager.getLogger(TransRewardServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransRewardDao transRewardDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					//2019-01-08 v7版規則修改 投保終期<=系統日 or 契約狀況=12、14 or 契約狀況>=31 or 契約狀況=15(限ED險)
					if (!StringUtils.isEmpty(status) && (Integer.parseInt(status) == 12 || Integer.parseInt(status) == 14 || Integer.parseInt(status) == 31)) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
						continue;
					}
					
					if (vo.getPolicyNo().startsWith("ED") && !StringUtils.isEmpty(status) && Integer.parseInt(status) == 15) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
						continue;
					}
					
//					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 14) {
//						vo.setApplyLockedFlag("Y");
//						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
//						continue;
//					}
					
					//2019-01-08 v7版移除此規則
//					String paymentMode = vo.getPaymentMode();
//					if ("T".equals(paymentMode)) {
//						vo.setApplyLockedFlag("Y");
//						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_PAYMODE_T_NOT_ALLOW_MSG);
//						continue;
//					}
				}
			}
		}
	}
	
	/**
	 * 變更增值回饋分享金領取方式-查詢.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransRewardVo> getTransReward(TransRewardVo transRewardVo) {
		return transRewardDao.getTransReward(transRewardVo);
	}

	/**
	 * 變更增值回饋分享金領取方式-新增.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransReward(TransRewardVo transRewardVo) {
		String transNum = transRewardVo.getTransNum();
		String userId = transRewardVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.REWARD_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transRewardVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更增值回饋分享金領取方式
			transRewardDao.insertTransReward(transRewardVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransReward: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更增值回饋分享金領取方式-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransRewardVo getTransRewardDetail(String transNum) {
		TransRewardVo qryVo = new TransRewardVo();
		qryVo.setTransNum(transNum);
		List<TransRewardVo> detailList = transRewardDao.getTransReward(qryVo);
		
		TransRewardVo detailVo = new TransRewardVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}