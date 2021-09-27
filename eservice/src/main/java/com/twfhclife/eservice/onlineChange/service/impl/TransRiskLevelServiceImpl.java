package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransRiskLevelDao;
import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;
import com.twfhclife.eservice.onlineChange.service.ITransRiskLevelService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 變更風險屬性服務.
 * 
 * @author all
 */
@Service
public class TransRiskLevelServiceImpl implements ITransRiskLevelService {

	private static final Logger logger = LogManager.getLogger(TransRiskLevelServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransRiskLevelDao transRiskLevelDao;

	@Override
	public List<TransRiskLevelVo> getTransRiskLevelList(TransRiskLevelVo transRiskLevelVo) {
		return transRiskLevelDao.getTransRiskLevelList(transRiskLevelVo);
	}

	@Transactional
	@Override
	public int insertTransRiskLevel(TransRiskLevelVo transRiskLevelVo) {
		String transNum = transRiskLevelVo.getTransNum();
		String userId = transRiskLevelVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.RISK_LEVEL_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
//			// 查詢舊的風險屬性
//			String riskLevelOld = "RR1";//"select PMDA_INVE_ATTR from lipmda";
//			transRiskLevelVo.setRiskLevelOld(riskLevelOld);
			
			// 新增變更風險屬性
			transRiskLevelDao.insertTransRiskLevel(transRiskLevelVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransRiskLevel: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	@Override
	public TransRiskLevelVo getTransRiskLevelDetail(String transNum) {
		TransRiskLevelVo qryVo = new TransRiskLevelVo();
		qryVo.setTransNum(transNum);
		List<TransRiskLevelVo> transRiskLevelVoList = transRiskLevelDao.getTransRiskLevelList(qryVo);
		
		TransRiskLevelVo detailVo = new TransRiskLevelVo();
		if (transRiskLevelVoList != null && transRiskLevelVoList.size() > 0) {
			detailVo = transRiskLevelVoList.get(0);
		}
		return detailVo;
	}

	@Override
	public String getRiskLevelCode(String policyNo) {
		return transRiskLevelDao.getRiskLevelCode(policyNo);
	}

	@Override
	public String getUserRiskAttr(String rocId) {
		return transRiskLevelDao.getRiskAttr(rocId);
	}

	@Override
	public String computeRiskLevel(Integer score) {
		if (score <= 20) {
			return "A";
		} else if (score >= 50) {
			return "B";
		} else {
			return "C";
		}
	}
}