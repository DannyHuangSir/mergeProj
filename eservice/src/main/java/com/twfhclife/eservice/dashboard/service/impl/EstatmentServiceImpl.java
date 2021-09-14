package com.twfhclife.eservice.dashboard.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.dashboard.dao.EstatmentAttrDao;
import com.twfhclife.eservice.dashboard.dao.EstatmentDao;
import com.twfhclife.eservice.dashboard.model.EstatmentAttrVo;
import com.twfhclife.eservice.dashboard.model.EstatmentVo;
import com.twfhclife.eservice.dashboard.service.IEstatmentService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 我的通知服務.
 * 
 * @author alan
 */
@Service
public class EstatmentServiceImpl implements IEstatmentService {

	@Autowired
	private EstatmentDao estatmentDao;

	@Autowired
	private EstatmentAttrDao estatmentAttrDao;

	/**
	 * 取得我的通知清單.
	 * 
	 * @param estatmentVo EstatmentVo
	 * @return 回傳我的通知清單
	 */
	@RequestLog
	@Override
	public List<EstatmentVo> getNoticeBoardList(EstatmentVo estatmentVo) {
		return estatmentDao.getEstatment(estatmentVo);
	}

	/**
	 * 取得我的通知屬性清單.
	 * 
	 * @param estatmentAttrVo EstatmentAttrVo
	 * @return 回傳我的通知屬性清單
	 */
	@RequestLog
	@Override
	public List<EstatmentAttrVo> getEstatmentAttrList(EstatmentAttrVo estatmentAttrVo) {
		return estatmentAttrDao.getEstatmentAttr(estatmentAttrVo);
	}

	/**
	 * 更新我的通知.
	 * 
	 * @param estatmentId BigDecimal
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Override
	public int updateEstatment(BigDecimal estatmentId) {
		EstatmentVo qryVo = new EstatmentVo();
		qryVo.setId(estatmentId);
		List<EstatmentVo> dataList = estatmentDao.getEstatment(qryVo);
		
		int result = 0 ;
		if (dataList != null && dataList.size() == 1) {
			EstatmentVo estatmentVo = dataList.get(0);
			if (BigDecimal.ZERO.equals(estatmentVo.getStatus())) {
				estatmentVo.setStatus(BigDecimal.ONE); // 1:讀取
				result = estatmentDao.updateEstatment(estatmentVo);
			}
		}
		
		return result;
	}
}