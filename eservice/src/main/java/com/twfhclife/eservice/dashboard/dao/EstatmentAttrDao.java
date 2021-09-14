package com.twfhclife.eservice.dashboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.dashboard.model.EstatmentAttrVo;

/**
 * 我的通知主表屬性 Dao.
 * 
 * @author all
 */
public interface EstatmentAttrDao {
	
	/**
	 * 我的通知主表屬性-查詢.
	 * 
	 * @param estatmentAttrVo EstatmentAttrVo
	 * @return 回傳查詢結果
	 */
	List<EstatmentAttrVo> getEstatmentAttr(@Param("estatmentAttrVo") EstatmentAttrVo estatmentAttrVo);
}

