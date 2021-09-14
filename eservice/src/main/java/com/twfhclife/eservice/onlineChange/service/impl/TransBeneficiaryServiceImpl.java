package com.twfhclife.eservice.onlineChange.service.impl;

import java.math.BigDecimal;
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

import com.twfhclife.eservice.onlineChange.dao.TransBeneficiaryDao;
import com.twfhclife.eservice.onlineChange.dao.TransBeneficiaryDtlDao;
import com.twfhclife.eservice.onlineChange.dao.TransBeneficiaryOldDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryVo;
import com.twfhclife.eservice.onlineChange.service.IBeneficiaryService;
import com.twfhclife.eservice.onlineChange.service.ITransBeneficiaryService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更受益人服務.
 * 
 * @author all
 */
@Service
public class TransBeneficiaryServiceImpl implements ITransBeneficiaryService {

	private static final Logger logger = LogManager.getLogger(TransBeneficiaryServiceImpl.class);
	
	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransBeneficiaryDao transBeneficiaryDao;

	@Autowired
	private TransBeneficiaryDtlDao transBeneficiaryDtlDao;

	@Autowired
	private TransBeneficiaryOldDao transBeneficiaryOldDao;
	
	@Autowired
	private IBeneficiaryService beneficiaryService;

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
					/* 20180904 依照客戶要求暫時隱藏 */
					if (vo.getSameIdCount() == 0) {
						// 要被保人不同人
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.INSURED_NOT_SAME_MSG);
						continue;
					}
					
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
					} else {
						/* 20180904 依照客戶要求暫時隱藏 */
						// 檢查是否有舊受益人
//						List<BeneficiaryVo> oldBeneficiary = beneficiaryService.getBeneficiaryByPolicyNo(vo.getPolicyNo());
//						if (CollectionUtils.isEmpty(oldBeneficiary)) {
//							vo.setApplyLockedFlag("Y");
//							vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_N_BENEFICIARY_MSG);
//						}
					}
				}
			}
		}
	}
	
	/**
	 * 變更受益人主檔-新增.
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransBeneficiary(TransBeneficiaryVo transBeneficiaryVo) {
		String transNum = transBeneficiaryVo.getTransNum();
		String userId = transBeneficiaryVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.BENEFICIARY_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transBeneficiaryVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增變更受益人主檔
			BigDecimal beneficiaryId = transBeneficiaryDao.getNextTransBeneficiaryId();
			transBeneficiaryVo.setId(beneficiaryId);
			transBeneficiaryDao.insertTransBeneficiary(transBeneficiaryVo);
			
			// 新增變更後受益人
			List<TransBeneficiaryDtlVo> transBeneficiaryDtlList = transBeneficiaryVo.getTransBeneficiaryDtlList();
			for (TransBeneficiaryDtlVo transBeneficiaryDtlVo : transBeneficiaryDtlList) {
				transBeneficiaryDtlVo.setTransBeneficiaryId(beneficiaryId);
				transBeneficiaryDtlDao.insertTransBeneficiaryDtl(transBeneficiaryDtlVo);
			}
			
			// 新增變更前受益人
			List<String> policyNos = transBeneficiaryVo.getPolicyNoList();
			if (policyNos != null && policyNos.size() == 1) {
				String policyNo = policyNos.get(0);
				
				List<BeneficiaryVo> oldBeneficiaryList = beneficiaryService.getBeneficiaryByPolicyNo(policyNo);
				for (BeneficiaryVo beneficiaryVo : oldBeneficiaryList) {
					
					TransBeneficiaryOldVo transBeneficiaryOldVo = new TransBeneficiaryOldVo();
					transBeneficiaryOldVo.setTransBeneficiaryId(beneficiaryId);
					transBeneficiaryOldVo.setBeneficiaryName(beneficiaryVo.getBeneficiaryName());
					transBeneficiaryOldVo.setBeneficiaryRelation(beneficiaryVo.getBeneficiaryRegion());
					
					Integer beneficiaryType = beneficiaryVo.getBeneficiaryType();
					if (beneficiaryType != null) {
						transBeneficiaryOldVo.setBeneficiaryType(BigDecimal.valueOf(beneficiaryType.intValue()));
					}
					Integer allocateType = beneficiaryVo.getAllocateType();
					if (allocateType != null) {
						transBeneficiaryOldVo.setAllocateType(BigDecimal.valueOf(allocateType.intValue()));
					}
					Integer allocateSeq = beneficiaryVo.getAllocateSeq();
					if (allocateSeq != null) {
						transBeneficiaryOldVo.setAllocateSeq(BigDecimal.valueOf(allocateSeq.intValue()));
					}
					transBeneficiaryOldDao.insertTransBeneficiaryOld(transBeneficiaryOldVo);
				}
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransBeneficiary: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 取得變更後受益人申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public BigDecimal getTransBeneficiaryId(String transNum) {
		TransBeneficiaryVo transBeneficiaryVo = new TransBeneficiaryVo();
		transBeneficiaryVo.setTransNum(transNum);
		List<TransBeneficiaryVo> dataList = transBeneficiaryDao.getTransBeneficiary(transBeneficiaryVo);
		
		BigDecimal transBeneficiaryId = null;
		if (!CollectionUtils.isEmpty(dataList)) {
			transBeneficiaryId = dataList.get(0).getId();
		}
		return transBeneficiaryId;
	}
	
	/**
	 * 變更後受益人-明細查詢.
	 * 
	 * @param beneficiaryId 變更受益人主檔id
	 * @return 回傳查詢結果
	 */
	@Override
	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlDetail(BigDecimal beneficiaryId) {
		TransBeneficiaryDtlVo qryVo = new TransBeneficiaryDtlVo();
		qryVo.setTransBeneficiaryId(beneficiaryId);
		return transBeneficiaryDtlDao.getTransBeneficiaryDtl(qryVo);
	}
	
	/**
	 * 變更前受益人-明細查詢.
	 * 
	 * @param beneficiaryId 變更受益人主檔id
	 * @return 回傳查詢結果
	 */
	@Override
	public List<TransBeneficiaryOldVo> getTransBeneficiaryOldDetail(BigDecimal beneficiaryId) {
		TransBeneficiaryOldVo qryVo = new TransBeneficiaryOldVo();
		qryVo.setTransBeneficiaryId(beneficiaryId);
		return transBeneficiaryOldDao.getTransBeneficiaryOld(qryVo);
	}
}