package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.TransAnnuityMethodDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransAnnuityMethodVo;
import com.twfhclife.eservice.onlineChange.service.ITransAnnuityMethodService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更年金給付方式服務.
 * 
 * @author all
 */
@Service
public class TransAnnuityMethodServiceImpl implements ITransAnnuityMethodService {

	private static final Logger logger = LogManager.getLogger(TransAnnuityMethodServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransAnnuityMethodDao transAnnuityMethodDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
//		List<String> policyAnnuAllowTypeList = Arrays.asList("32", "AG", "BC", "UL");
		
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
						continue;
					}
					//改由procedure USP_VERIFY_POLICY_RULE判斷
//					String policyType = vo.getPolicyType();
//					if (!policyAnnuAllowTypeList.contains(policyType)) {
//						vo.setApplyLockedFlag("Y");
//						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_ANNU_NOT_ALLOW_MSG);
//						continue;
//					}
				}
			}
		}
	}
	
	/**
	 * 變更年金給付方式-查詢.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransAnnuityMethodVo> getTransAnnuityMethod(TransAnnuityMethodVo transAnnuityMethodVo) {
		return transAnnuityMethodDao.getTransAnnuityMethod(transAnnuityMethodVo);
	}

	/**
	 * 變更年金給付方式-新增.
	 * 
	 * @param transAnnuityMethodVo TransAnnuityMethodVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransAnnuityMethod(TransAnnuityMethodVo transAnnuityMethodVo) {
		String transNum = transAnnuityMethodVo.getTransNum();
		String userId = transAnnuityMethodVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.ANNUITY_METHOD_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transAnnuityMethodVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更年金給付方式
			transAnnuityMethodDao.insertTransAnnuityMethod(transAnnuityMethodVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransAnnuityMethod: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更年金給付方式-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransAnnuityMethodVo getTransAnnuityMethodDetail(String transNum) {
		TransAnnuityMethodVo qryVo = new TransAnnuityMethodVo();
		qryVo.setTransNum(transNum);
		List<TransAnnuityMethodVo> detailList = transAnnuityMethodDao.getTransAnnuityMethod(qryVo);
		
		TransAnnuityMethodVo detailVo = new TransAnnuityMethodVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
	
	@Override
	public String getOldGuarTerm(@Param("policyNo") String policyNo) {
		String oldGuarTerm = "";
		oldGuarTerm = transAnnuityMethodDao.getOldGuarTerm(policyNo);
		return oldGuarTerm;
	}
}