package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.UnionCourseVo;


/**
 * 聯盟鏈歷程 Dao.
 * 
 * @author all
 */

public interface UnionCourseDao {
	/**
	 * 聯盟鏈歷程-查詢
	 * 
	 * @param UnionCourseDao unionCourseDao
	 * @return 回傳
	 */
	List<UnionCourseVo> getUnionCourseList(@Param("unionCourseVo") UnionCourseVo unionCourseVo);
	
	
}
