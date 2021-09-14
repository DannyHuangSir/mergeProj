package com.twfhclife.eservice.onlineChange.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransFundSwitchDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransFundSwitchVo;
import com.twfhclife.eservice.onlineChange.service.ITransFundSwitchService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.FundSwitchVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.user.dao.LilipmDao;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更繳別服務.
 * 
 * @author all
 */
@Service
public class TransFundSwitchServiceImpl implements ITransFundSwitchService {

	private static final Logger logger = LogManager
			.getLogger(TransFundSwitchServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransFundSwitchDao transFundSwitchDao;

	@Autowired
	private LilipmDao lilipmDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList
	 *            保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
//				if ("2".equals(vo.getPolicyType())) {
//					vo.setApplyLockedFlag("Y");
//					vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_N_INVESTMENT_MSG);
//					continue;
//				}
			}
		}
	}

	/**
	 * 變更投資標的與比例-新增.
	 * 
	 * @param transFundSwitchVo
	 *            TransFundSwitchVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransFundSwitch(TransFundSwitchVo transFundSwitchVo) {
		String transNum = transFundSwitchVo.getTransNum();
		String userId = transFundSwitchVo.getUserId();

		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.FUND_SWITCH_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);

			// 新增保單號碼
			for (String policyNo : transFundSwitchVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}

			// 新增變更投資標的
			for (FundSwitchVo fundSwitchVo : transFundSwitchVo.getFundSwitchOutList()) {
				fundSwitchVo.setTransNum(transNum);
				transFundSwitchDao.insertTransFundSwitchOut(fundSwitchVo);
			}
			for (FundSwitchVo fundSwitchVo : transFundSwitchVo.getFundSwitchInList()) {
				fundSwitchVo.setTransNum(transNum);
				transFundSwitchDao.insertTransFundSwitchIn(fundSwitchVo);
			}
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransFundSwitch: {}",
					ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	/**
	 * 變更投資標的與比例-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransFundSwitchVo getTransFundSwitchDetail(String transNum) {
		TransFundSwitchVo detailVo = null;
		try {
			List<FundSwitchVo> outList = transFundSwitchDao.getTransFundSwitchOut(transNum);
			List<FundSwitchVo> inList = transFundSwitchDao.getTransFundSwitchIn(transNum);
			detailVo = new TransFundSwitchVo();
			detailVo.setFundSwitchInList(inList);
			detailVo.setFundSwitchOutList(outList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return detailVo;
	}

	@Override
	public List<FundSwitchVo> getFundValueOptionList(String policyType) {
		return transFundSwitchDao.getOptionFundList(policyType);
	}

	@Override
	public void convertFundSwitch(TransFundSwitchVo transFundSwitchVo) throws Exception {
		if (transFundSwitchVo.getSwitchOutJsonData() != null) {
			List<FundSwitchVo> fundSwitchOutList = new ObjectMapper().readValue(transFundSwitchVo.getSwitchOutJsonData(), new TypeReference<List<FundSwitchVo>>() {}); 
			transFundSwitchVo.setFundSwitchOutList(fundSwitchOutList);
			
			if (transFundSwitchVo.getTotalOutAmt() == null && fundSwitchOutList != null) {
				BigDecimal total = new BigDecimal(0);
				for (FundSwitchVo vo : fundSwitchOutList) {
					total = total.add(vo.getSwitchAmount());
				}
				transFundSwitchVo.setTotalOutAmt(total);
			}
		}
		
		if (transFundSwitchVo.getSwitchInJsonData() != null) {
			List<FundSwitchVo> fundSwitchInList = new ObjectMapper().readValue(transFundSwitchVo.getSwitchInJsonData(), new TypeReference<List<FundSwitchVo>>() {}); 
			for (FundSwitchVo vo : fundSwitchInList) {
				vo.setSwitchAmount(vo.getSwitchPercentage().divide(new BigDecimal(100)).multiply(transFundSwitchVo.getTotalOutAmt()));
			}
			transFundSwitchVo.setFundSwitchInList(fundSwitchInList);
		}
	}

}