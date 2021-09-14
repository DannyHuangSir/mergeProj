package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.dao.LitracEsDao;
import com.twfhclife.eservice.onlineChange.model.LitracEsVo;
import com.twfhclife.eservice.onlineChange.service.ILitracEsService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 滿期生存匯款資訊服務.
 * 
 * @author all
 */
@Service
public class LitracEsServiceImpl implements ILitracEsService {

	@Autowired
	private LitracEsDao litracEsDao;
	
	/**
	 * 滿期生存匯款資訊-查詢.
	 * 
	 * @param litracEsVo LitracEsVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<LitracEsVo> getLitracEs(LitracEsVo litracEsVo) {
		return litracEsDao.getLitracEs(litracEsVo);
	}
	
	/**
	 * 滿期生存匯款資訊-依照保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<LitracEsVo> getLitracEsBypolicyNo(String policyNo) {
		return litracEsDao.getLitracEsBypolicyNo(policyNo);
	}
}