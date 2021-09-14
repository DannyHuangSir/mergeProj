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
import com.twfhclife.eservice.onlineChange.dao.TransResendPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITransResendPolicyService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 補發保單服務.
 * 
 * @author all
 */
@Service
public class TransResendPolicyServiceImpl implements ITransResendPolicyService {

	private static final Logger logger = LogManager.getLogger(TransResendPolicyServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransResendPolicyDao transResendPolicyDao;

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
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					}
				}
			}
		}
	}
	
	/**
	 * 補發保單-查詢.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransResendPolicyVo> getTransResendPolicy(TransResendPolicyVo transResendPolicyVo) {
		return transResendPolicyDao.getTransResendPolicy(transResendPolicyVo);
	}

	/**
	 * 補發保單-新增.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransResendPolicy(TransResendPolicyVo transResendPolicyVo) {
		String transNum = transResendPolicyVo.getTransNum();
		String userId = transResendPolicyVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.POLICY_RESEND_PARAMETER_CODE);
//			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_BEADDED);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transResendPolicyVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增補發保單
			transResendPolicyDao.insertTransResendPolicy(transResendPolicyVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransResendPolicy: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	@Override
	public TransResendPolicyVo getTransResendPolicyDetail(String transNum) {
		TransResendPolicyVo qryVo =  new TransResendPolicyVo();
		qryVo.setTransNum(transNum);
		List<TransResendPolicyVo> list = transResendPolicyDao.getTransResendPolicy(qryVo);
		
		TransResendPolicyVo detailVo = null;
		if (list != null && list.size() > 0) {
			detailVo = list.get(0);
			detailVo.setPolicyNoList(transPolicyDao.getTransPolicyNoList(transNum));
		}
		return detailVo;
	}
}