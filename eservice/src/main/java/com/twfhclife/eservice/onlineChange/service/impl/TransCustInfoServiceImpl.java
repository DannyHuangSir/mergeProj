package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.onlineChange.dao.TransCustInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.model.TransCustInfoVo;
import com.twfhclife.eservice.onlineChange.service.ITransCustInfoService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 變更基本資料服務.
 * 
 * @author all
 */
@Service
public class TransCustInfoServiceImpl implements ITransCustInfoService {

	private static final Logger logger = LogManager.getLogger(TransCustInfoServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private TransCustInfoDao transCustInfoDao;
	
	/**
	 * 變更基本資料-查詢.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransCustInfoVo> getTransCustInfo(TransCustInfoVo transCustInfoVo) {
		return transCustInfoDao.getTransCustInfo(transCustInfoVo);
	}

	/**
	 * 變更基本資料-新增.
	 * 
	 * @param transCustInfoVo TransCustInfoVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransCustInfo(TransCustInfoVo transCustInfoVo) {
		String transNum = transCustInfoVo.getTransNum();
		String userId = transCustInfoVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CUST_INFO_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_COMPLETE);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增變更基本資料
			transCustInfoDao.insertTransCustInfo(transCustInfoVo);
			
			// 更新users table
			UsersVo usersVo = new UsersVo();
			usersVo.setUserId(userId);
			usersVo.setEmail(transCustInfoVo.getEmail());
			usersVo.setMobile(transCustInfoVo.getMobile());
			usersVo.setSmsFlag(transCustInfoVo.getSmsFlag());
			usersVo.setMailFlag(transCustInfoVo.getMailFlag());
			usersDao.updateCustInfo(usersVo);
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransCustInfo: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 變更基本資料-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransCustInfoVo getTransCustInfoDetail(String transNum) {
		TransCustInfoVo qryVo = new TransCustInfoVo();
		qryVo.setTransNum(transNum);
		List<TransCustInfoVo> detailList = transCustInfoDao.getTransCustInfo(qryVo);
		
		TransCustInfoVo detailVo = new TransCustInfoVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		return detailVo;
	}
}