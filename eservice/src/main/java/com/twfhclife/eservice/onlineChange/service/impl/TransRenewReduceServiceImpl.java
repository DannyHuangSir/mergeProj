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
import com.twfhclife.eservice.onlineChange.model.TransRenewReduceVo;
import com.twfhclife.eservice.onlineChange.service.ITransRenewReduceService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.dao.PolicyExtraDao;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更展期定期保險/減額繳清保險服務.
 * 
 * @author all
 */
@Service
public class TransRenewReduceServiceImpl implements ITransRenewReduceService {

	private static final Logger logger = LogManager.getLogger(TransRenewReduceServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;
	
	@Autowired
	public PolicyExtraDao policyExtraDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 * @param userId 使用者ID
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList, String userId) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 12) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					} else {
						// 是否有貸款金額
						PolicyExtraVo policyExtraVo = policyExtraDao.findByPolicyNo(vo.getPolicyNo());
						if (policyExtraVo != null && policyExtraVo.getLoanAmount() != null) {
							if (policyExtraVo.getLoanAmount().doubleValue() > 0) {
								vo.setApplyLockedFlag("Y");
								vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_LOAN_NOT_ALLOW_MSG);
							}
						}
					}
				}
			}
			
			// 判斷是否有前一次申請(展期/減額)
			if (!StringUtils.isEmpty(userId)) {
				// 取得目前申請中(展期/減額)的保單號碼
				List<String> applyRenewPolicyNos = transPolicyDao.getProgressPolicyNoList(userId,
						TransTypeUtil.RENEW_PARAMETER_CODE);
				List<String> applyReducePolicyNos = transPolicyDao.getProgressPolicyNoList(userId,
						TransTypeUtil.REDUCE_PARAMETER_CODE);
				
				for (PolicyListVo vo : policyList) {
					// 保單未到期且尚未鎖定中
					if ("N".equals(vo.getExpiredFlag()) && !"Y".equals(vo.getApplyLockedFlag())) {
						int renewReducecnt = 0;
						if (applyRenewPolicyNos != null && applyRenewPolicyNos.contains(vo.getPolicyNo())) {
							renewReducecnt++;
						}
						if (applyReducePolicyNos != null && applyReducePolicyNos.contains(vo.getPolicyNo())) {
							renewReducecnt++;
						}
						logger.debug("{} renewReducecnt: {}", vo.getPolicyNo(), renewReducecnt);
						if (renewReducecnt > 0) {
							vo.setApplyLockedFlag("Y");
							// 前次申請尚在處理中，暫無法進行此項申請
							vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 變更展期定期保險/減額繳清保險-新增.
	 * 
	 * @param transRenewReduceVo TransRenewReduceVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransRenewReduce(TransRenewReduceVo transRenewReduceVo) {
		String transNum = transRenewReduceVo.getTransNum();
		String userId = transRenewReduceVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(transRenewReduceVo.getTransType());
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transRenewReduceVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransRenewReduce: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更展期定期保險/減額繳清保險-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransRenewReduceVo getTransRenewReduceDetail(String transNum) {
		TransRenewReduceVo detailVo = new TransRenewReduceVo();
		
		List<String> policyNoList = transPolicyDao.getTransPolicyNoList(transNum);
		detailVo.setPolicyNoList(policyNoList);
		
		TransVo transVo = transDao.findByTransNum(transNum);
		if (transVo != null) {
			detailVo.setTransType(transVo.getTransType());
		}
		return detailVo;
	}
}