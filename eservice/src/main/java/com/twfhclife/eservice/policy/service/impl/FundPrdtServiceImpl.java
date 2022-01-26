package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.FundPrdtDao;
import com.twfhclife.eservice.policy.model.FundPrdtVo;
import com.twfhclife.eservice.policy.service.IFundPrdtService;

/**
 * 帳戶價值交易明細服務服務.
 * 
 * @author all
 */
@Service
public class FundPrdtServiceImpl implements IFundPrdtService {

	@Autowired
	private FundPrdtDao fundPrdtDao;
	
	/**
	 * 帳戶價值交易明細-查詢.
	 * 
	 * @param fundPrdtVo FundPrdtVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<FundPrdtVo> getFundPrdt(FundPrdtVo fundPrdtVo) {
		return fundPrdtDao.getFundPrdt(fundPrdtVo);
	}

	/**
	 * 取得保單的保費費用記錄(分頁).
	 * 
	 * @param policyNo 保單號碼
	 * @param startDate 開始時間
	 * @param endDate 結束時間
	 * @param pageNum 當前頁數
	 * @param pageSize 每頁幾筆
	 * @return 回傳保單的保費費用記錄
	 */
	@RequestLog
	@Override
	public List<FundPrdtVo> getFundPrdtPageList(String policyNo, String startDate, String endDate,
			int pageNum, int pageSize) {
		return fundPrdtDao.getFundPrdtPageList(policyNo, startDate, endDate, pageNum, pageSize);
	}
}