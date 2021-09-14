package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.TransBounsDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransBounsVo;
import com.twfhclife.eservice.onlineChange.service.ITransBounsService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 變更紅利選擇權服務.
 * 
 * @author all
 */
@Service
public class TransBounsServiceImpl implements ITransBounsService {

	private static final Logger logger = LogManager.getLogger(TransBounsServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransBounsDao transBounsDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag()) && !"Y".equals(vo.getApplyLockedFlag())) {
					String status = vo.getStatus();
					//2019-01-08 v7版規則修改 投保終期<=系統日 or 契約狀況=12 or 契約狀況 >=14，不允許變更
					if (!StringUtils.isEmpty(status) && (Integer.parseInt(status) == 12 || Integer.parseInt(status) >= 14)) {
						vo.setApplyLockedFlag("Y");
//						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
						vo.setApplyLockedMsg("你選擇的保單契約狀態不提供申請變更紅利選擇權");
						continue;
					}
					
					String paymentMode = vo.getPaymentMode();
					if ("T".equals(paymentMode)) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.BONUS_PAYMODE_T_NOT_ALLOW_MSG);
						continue;
					}
					
					//紅利選擇權增加第4點條件
					try {
						String condition4 = transBounsDao.checkCondition4(vo.getPolicyNo());
						logger.debug("紅利選擇權第4點條件 policyNo={}, condition4={}", vo.getPolicyNo(), condition4);
						if ("N".equals(StringUtils.trimToEmpty(condition4))) {
							vo.setApplyLockedFlag("Y");
							vo.setApplyLockedMsg("此張保單不允許變更");
							continue;
						}
					} catch (Exception e) {
						logger.error("Unable to checkCondition4: {}", ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
	}
	
	/**
	 * 變更紅利選擇權-查詢.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransBounsVo> getTransBouns(TransBounsVo transBounsVo) {
		return transBounsDao.getTransBouns(transBounsVo);
	}

	/**
	 * 變更紅利選擇權-新增.
	 * 
	 * @param transBounsVo TransBounsVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransBouns(TransBounsVo transBounsVo) {
		String transNum = transBounsVo.getTransNum();
		String userId = transBounsVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.BONUS_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transBounsVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更紅利選擇權
			transBounsDao.insertTransBouns(transBounsVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransBouns: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更紅利選擇權-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransBounsVo getTransBounsDetail(String transNum) {
		TransBounsVo qryVo = new TransBounsVo();
		qryVo.setTransNum(transNum);
		List<TransBounsVo> detailList = transBounsDao.getTransBouns(qryVo);
		
		TransBounsVo detailVo = new TransBounsVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}