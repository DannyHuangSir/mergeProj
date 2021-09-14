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
import com.twfhclife.eservice.onlineChange.dao.TransCreditCardDateDao;
import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;
import com.twfhclife.eservice.onlineChange.service.ITransCreditCardDateService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 變更信用卡效期服務.
 * 
 * @author all
 */
@Service
public class TransCreditCardDateServiceImpl implements ITransCreditCardDateService {

	private static final Logger logger = LogManager.getLogger(TransCreditCardDateServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransCreditCardDateDao transCreditCardDateDao;

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
					String paymentMethod = vo.getPaymentMethod();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					} else {
						//非信用卡繳費方式
						if (!"9".equals(paymentMethod)) {
							vo.setApplyLockedFlag("Y");
							vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_N_CDCARD_PAY_MSG);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 變更信用卡效期-查詢.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransCreditCardDateVo> getTransCreditCardDate(TransCreditCardDateVo transCreditCardDateVo) {
		return transCreditCardDateDao.getTransCreditCardDate(transCreditCardDateVo);
	}

	/**
	 * 變更信用卡效期-新增.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransCreditCardDate(TransCreditCardDateVo transCreditCardDateVo) {
		String transNum = transCreditCardDateVo.getTransNum();
		String userId = transCreditCardDateVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CREDIT_CARD_DATE_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transCreditCardDateVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更信用卡效期
			transCreditCardDateDao.insertTransCreditCardDate(transCreditCardDateVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransCreditCardDate: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更信用卡效期-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransCreditCardDateVo getTransCreditCardDateDetail(String transNum) {
		TransCreditCardDateVo qryVo = new TransCreditCardDateVo();
		qryVo.setTransNum(transNum);
		List<TransCreditCardDateVo> detailList = transCreditCardDateDao.getTransCreditCardDate(qryVo);
		
		TransCreditCardDateVo detailVo = new TransCreditCardDateVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}