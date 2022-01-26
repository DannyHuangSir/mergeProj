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

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.TransCushionDao;
import com.twfhclife.eservice.onlineChange.model.TransCushionVo;
import com.twfhclife.eservice.onlineChange.service.ITransCushionService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 自動墊繳選擇權服務.
 * 
 * @author all
 */
@Service
public class TransCushionServiceImpl implements ITransCushionService {

	private static final Logger logger = LogManager.getLogger(TransCushionServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransCushionDao transCushionDao;

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
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 12) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					}
				}
				
				//線上申請-自動墊繳選擇權繳別T不允許變更
				String paymentMode = vo.getPaymentMode();
				if ("T".equals(paymentMode)) {
					vo.setApplyLockedFlag("Y");
					vo.setApplyLockedMsg(OnlineChangMsgUtil.CUSION_PAYMODE_T_NOT_ALLOW_MSG);
					continue;
				}
			}
		}
	}
	
	/**
	 * 自動墊繳選擇權-查詢.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransCushionVo> getTransCushion(TransCushionVo transCushionVo) {
		return transCushionDao.getTransCushion(transCushionVo);
	}

	/**
	 * 自動墊繳選擇權-新增.
	 * 
	 * @param transCushionVo TransCushionVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransCushion(TransCushionVo transCushionVo) {
		String transNum = transCushionVo.getTransNum();
		String userId = transCushionVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CUSHION_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transCushionVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增自動墊繳選擇權
			transCushionDao.insertTransCushion(transCushionVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransCushion: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 自動墊繳選擇權-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransCushionVo getTransCushionDetail(String transNum) {
		TransCushionVo qryVo = new TransCushionVo();
		qryVo.setTransNum(transNum);
		List<TransCushionVo> detailList = transCushionDao.getTransCushion(qryVo);
		
		TransCushionVo detailVo = new TransCushionVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}