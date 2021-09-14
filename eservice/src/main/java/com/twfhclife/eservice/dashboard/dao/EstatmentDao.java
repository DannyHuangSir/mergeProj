package com.twfhclife.eservice.dashboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.dashboard.model.EstatmentVo;

/**
 * 我的通知主表 Dao.
 * 
 * @author all
 */
public interface EstatmentDao {
	
	/**
	 * 我的通知主表-查詢.
	 * 
	 * @param estatmentVo EstatmentVo
	 * @return 回傳查詢結果
	 */
	List<EstatmentVo> getEstatment(@Param("estatmentVo") EstatmentVo estatmentVo);
	
	/**
	 * 我的通知主表-更新.
	 * 
	 * @param estatmentVo EstatmentVo
	 * @return 回傳影響筆數
	 */
	int updateEstatment(@Param("estatmentVo") EstatmentVo estatmentVo);
}

