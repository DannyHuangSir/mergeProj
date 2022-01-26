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

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.dao.TransReducePolicyDao;
import com.twfhclife.eservice.onlineChange.dao.TransReducePolicyDtlDao;
import com.twfhclife.eservice.onlineChange.dao.TransReducePolicyOldDao;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyOldVo;
import com.twfhclife.eservice.onlineChange.model.TransReducePolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITransReducePolicyService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 減少保險金額服務.
 * 
 * @author all
 */
@Service
public class TransReducePolicyServiceImpl implements ITransReducePolicyService {

	private static final Logger logger = LogManager.getLogger(TransReducePolicyServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransReducePolicyDao transReducePolicyDao;

	@Autowired
	private TransReducePolicyDtlDao transReducePolicyDtlDao;

	@Autowired
	private TransReducePolicyOldDao transReducePolicyOldDao;

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
			}
		}
	}
	
	/**
	 * 減少保險金額-查詢.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransReducePolicyVo> getTransReducePolicy(TransReducePolicyVo transReducePolicyVo) {
		return transReducePolicyDao.getTransReducePolicy(transReducePolicyVo);
	}

	/**
	 * 減少保險金額-新增.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransReducePolicy(TransReducePolicyVo transReducePolicyVo) {
		String transNum = transReducePolicyVo.getTransNum();
		String userId = transReducePolicyVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.REDUCE_POLICY_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transReducePolicyVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增減少保險金額
			BigDecimal transReducePolicyId = transReducePolicyDao.getNextTransReducePolicyId();
			transReducePolicyVo.setId(transReducePolicyId);
			transReducePolicyDao.insertTransReducePolicy(transReducePolicyVo);
			
			// 新增減少保險金額-明細
			List<TransReducePolicyDtlVo> transReducePolicyDtlList = transReducePolicyVo.getTransReducePolicyDtlList();
			for (TransReducePolicyDtlVo transReducePolicyDtlVo : transReducePolicyDtlList) {
				// 有輸入變更減少保險金額才新增
				if (transReducePolicyDtlVo.getContractAmount() != null) {
					transReducePolicyDtlVo.setTransReducePolicyId(transReducePolicyId);
					transReducePolicyDtlDao.insertTransReducePolicyDtl(transReducePolicyDtlVo);
				}
			}
			
			// 新增減少保險金額-修改前
			for (TransReducePolicyDtlVo transReducePolicyDtlVo : transReducePolicyDtlList) {
				// 有輸入變更減少保險金額才新增
				if (transReducePolicyDtlVo.getContractAmount() != null) {
					TransReducePolicyOldVo transReducePolicyOldVo = new TransReducePolicyOldVo();
					transReducePolicyOldVo.setTransReducePolicyId(transReducePolicyId);
					transReducePolicyOldVo.setContractType(transReducePolicyDtlVo.getContractType());
					transReducePolicyOldVo.setProductName(transReducePolicyDtlVo.getProductName());
					transReducePolicyOldVo.setContractAmount(transReducePolicyDtlVo.getContractAmountOld());
					transReducePolicyOldDao.insertTransReducePolicyOld(transReducePolicyOldVo);
				}
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransReducePolicy: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 取得減少保險金額申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public BigDecimal getTransReducePolicyId(String transNum) {
		TransReducePolicyVo qryVo = new TransReducePolicyVo();
		qryVo.setTransNum(transNum);
		List<TransReducePolicyVo> dataList = transReducePolicyDao.getTransReducePolicy(qryVo);
		
		BigDecimal transReducePolicyId = null;
		if (!CollectionUtils.isEmpty(dataList)) {
			transReducePolicyId = dataList.get(0).getId();
		}
		return transReducePolicyId;
	}
	
	/**
	 * 減少保險金額-明細查詢.
	 * 
	 * @param transReducePolicyId 減少保險金額申請id
	 * @return 回傳查詢結果
	 */
	@Override
	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlDetail(BigDecimal transReducePolicyId) {
		TransReducePolicyDtlVo qryVo = new TransReducePolicyDtlVo();
		qryVo.setTransReducePolicyId(transReducePolicyId);
		List<TransReducePolicyDtlVo> dtlList = transReducePolicyDtlDao.getTransReducePolicyDtl(qryVo);
		
		TransReducePolicyOldVo qryOldVo = new TransReducePolicyOldVo();
		qryOldVo.setTransReducePolicyId(transReducePolicyId);
		List<TransReducePolicyOldVo> oldList = transReducePolicyOldDao.getTransReducePolicyOld(qryOldVo);
		
		for (TransReducePolicyDtlVo dtlVo : dtlList) {
			for (TransReducePolicyOldVo oldVo : oldList) {
				String contractType = StringUtils.trimToEmpty(dtlVo.getContractType());
				String productName = StringUtils.trimToEmpty(dtlVo.getProductName());
				String contractTypeOld = StringUtils.trimToEmpty(oldVo.getContractType());
				String productNameOld = StringUtils.trimToEmpty(oldVo.getProductName());
				if (contractType.equals(contractTypeOld) && productName.equals(productNameOld)) {
					dtlVo.setContractAmountOld(oldVo.getContractAmount());
				}
			}
		}
		
		return dtlList;
	}
}