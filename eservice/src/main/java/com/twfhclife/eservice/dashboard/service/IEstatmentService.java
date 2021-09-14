package com.twfhclife.eservice.dashboard.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.dashboard.model.EstatmentAttrVo;
import com.twfhclife.eservice.dashboard.model.EstatmentVo;

/**
 * 我的通知服務.
 * 
 * @author alan
 */
public interface IEstatmentService {

	/**
	 * 取得我的通知清單.
	 * 
	 * @param estatmentVo EstatmentVo
	 * @return 回傳我的通知清單
	 */
	public List<EstatmentVo> getNoticeBoardList(EstatmentVo estatmentVo);

	/**
	 * 取得我的通知屬性清單.
	 * 
	 * @param estatmentAttrVo EstatmentAttrVo
	 * @return 回傳我的通知屬性清單
	 */
	public List<EstatmentAttrVo> getEstatmentAttrList(EstatmentAttrVo estatmentAttrVo);

	/**
	 * 更新我的通知.
	 * 
	 * @param estatmentId BigDecimal
	 * @return 回傳影響筆數
	 */
	public int updateEstatment(BigDecimal estatmentId);
}
