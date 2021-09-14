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
import com.twfhclife.eservice.onlineChange.dao.TransValuePrintDao;
import com.twfhclife.eservice.onlineChange.model.TransValuePrintVo;
import com.twfhclife.eservice.onlineChange.service.ITransValuePrintService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 保單價值列印服務.
 * 
 * @author all
 */
@Service
public class TransValuePrintServiceImpl implements ITransValuePrintService {

	private static final Logger logger = LogManager.getLogger(TransValuePrintServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransValuePrintDao transValuePrintDao;

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
					//modify 2019年1月22日 下午5:43 請新增控管契約狀況為繳清保險、展期定期保險、保單墊繳不能線上申請保單價值列印
					if (!StringUtils.isEmpty(status) && (Integer.parseInt(status) == 14 || Integer.parseInt(status) > 17)) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_NOT_APPLY_VALUE_PRINT_MSG);
					}
					if(StringUtils.equals("2", vo.getProdIsFatca())){
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.NO_POLICY_VUL_NOT_APPLY_MSG);
					}
					if(StringUtils.equals("3", vo.getProdType())){
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.VUL_REF_ACCOUNT_VALUE);
					}
				}
			}
		}
	}
	
	/**
	 * 保單價值列印-查詢.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransValuePrintVo> getTransValuePrint(TransValuePrintVo transValuePrintVo) {
		return transValuePrintDao.getTransValuePrint(transValuePrintVo);
	}

	/**
	 * 保單價值列印-新增.
	 * 
	 * @param transValuePrintVo TransValuePrintVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransValuePrint(TransValuePrintVo transValuePrintVo) {
		String transNum = transValuePrintVo.getTransNum();
		String userId = transValuePrintVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.VALUE_PRINT_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transValuePrintVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增保單價值列印
			transValuePrintDao.insertTransValuePrint(transValuePrintVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransValuePrint: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 保單價值列印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransValuePrintVo getTransValuePrintDetail(String transNum) {
		TransValuePrintVo qryVo = new TransValuePrintVo();
		qryVo.setTransNum(transNum);
		List<TransValuePrintVo> detailList = transValuePrintDao.getTransValuePrint(qryVo);
		
		TransValuePrintVo detailVo = new TransValuePrintVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}