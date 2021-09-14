package com.twfhclife.eservice.dashboard.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.dashboard.service.IApplyProgressService;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 線上申請進度服務.
 * 
 * @author alan
 */
@Service
public class ApplyProgressServiceImpl implements IApplyProgressService {

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	/**
	 * 取得使用者的線上申請記錄-申請中
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳使用者的線上申請記錄
	 */
	public List<TransVo> getApplyProgressList(String rocId) {
		TransVo qryVo = new TransVo();
		qryVo.setRocId(rocId);
		qryVo.setTransStatusList(Arrays.asList(
				OnlineChangeUtil.TRANS_STATUS_PROCESSING, 
				OnlineChangeUtil.TRANS_STATUS_AUDITED, 
				OnlineChangeUtil.TRANS_STATUS_UPLOADED));
		
		List<TransVo> dataList = transDao.getChangeHistoryList(qryVo);
		for (TransVo transVo : dataList) {
			List<String> policyNoList = transPolicyDao.getTransPolicyNoList(transVo.getTransNum());
			if (policyNoList != null) {
				if (policyNoList.size() == 1) {
					transVo.setPolicyNo(policyNoList.get(0));
				} else {
					transVo.setPolicyNo(String.join(",", policyNoList));
				}
			}
		}
		
		return dataList;
	}
	
	/**
	 * 取得使用者的線上申請記錄.
	 * 
	 * @param rocId 用戶證號
	 * @param transStatus 申請狀態
	 * @return 回傳使用者的線上申請記錄
	 */
	@RequestLog
	@Override
	public List<TransVo> getChangeHistoryList(String rocId, String transStatus) {
		TransVo qryVo = new TransVo();
		qryVo.setRocId(rocId);
		qryVo.setStatus(transStatus);
		
		List<TransVo> dataList = transDao.getChangeHistoryList(qryVo);
		for (TransVo transVo : dataList) {
			List<String> policyNoList = transPolicyDao.getTransPolicyNoList(transVo.getTransNum());
			if (policyNoList != null) {
				if (policyNoList.size() == 1) {
					transVo.setPolicyNo(policyNoList.get(0));
				} else {
					transVo.setPolicyNo(String.join(",", policyNoList));
				}
			}
		}
		
		return dataList;
	}
}