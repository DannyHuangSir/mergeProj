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
import com.twfhclife.eservice.onlineChange.dao.TransPaymodeDao;
import com.twfhclife.eservice.onlineChange.model.TransPaymodeVo;
import com.twfhclife.eservice.onlineChange.service.ITransPaymodeService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.dao.LilipmDao;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 變更繳別服務.
 * 
 * @author all
 */
@Service
public class TransPaymodeServiceImpl implements ITransPaymodeService {

	private static final Logger logger = LogManager.getLogger(TransPaymodeServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransPaymodeDao transPaymodeDao;

	@Autowired
	private LilipmDao lilipmDao;

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
						continue;
					}
					
					String paymentMode = vo.getPaymentMode();
					LilipmVo pmVo = lilipmDao.findByPolicyNo(vo.getPolicyNo());
					if ("T".equals(paymentMode) || ("A".equals(paymentMode) && "Y".equals(pmVo.getLipmFlexRcpMk()))) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_PAYMODE_T_NOT_ALLOW_MSG);
						continue;
					}
				}
			}
		}
	}
	
	/**
	 * 變更繳別-查詢.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransPaymodeVo> getTransPaymode(TransPaymodeVo transPaymodeVo) {
		return transPaymodeDao.getTransPaymode(transPaymodeVo);
	}

	/**
	 * 變更繳別-新增.
	 * 
	 * @param transPaymodeVo TransPaymodeVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransPaymode(TransPaymodeVo transPaymodeVo) {
		String transNum = transPaymodeVo.getTransNum();
		String userId = transPaymodeVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.PAYMODE_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transPaymodeVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更繳別
			transPaymodeDao.insertTransPaymode(transPaymodeVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransPaymode: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更繳別-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransPaymodeVo getTransPaymodeDetail(String transNum) {
		TransPaymodeVo qryVo = new TransPaymodeVo();
		qryVo.setTransNum(transNum);
		List<TransPaymodeVo> detailList = transPaymodeDao.getTransPaymode(qryVo);
		
		TransPaymodeVo detailVo = new TransPaymodeVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}

	/**
	 * 
	 */
	@Override
	public Integer getPolicyPayMethodChange(String policyNo) {
		return lilipmDao.getLipmTredTmsByPolicyNo(policyNo);
	}
}