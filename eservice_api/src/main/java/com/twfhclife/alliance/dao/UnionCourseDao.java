package com.twfhclife.alliance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.UnionCourseVo;


/**
 * 聯盟鏈歷程 Dao.
 * 
 * @author all
 */

public interface UnionCourseDao {
	/**
	 * 聯盟鏈歷程-新增.
	 * 
	 * @param UnionCourseDao unionCourseDao
	 * @return 回傳影響筆數
	 */
	int insertUnionCourseVo(@Param("unionCourseVo") UnionCourseVo unionCourseVo);
	
	/**
	 * 找出有效被保人保單
	 * @param id
	 * @return List
	 */
	List<String> getPolicyNoByID(@Param("id") String id);
	
	/**
	 * 依保單號碼找出商品險種
	 * @param policyNo
	 * @return List<String>
	 */
	List<String> getProductCodeByPolicyNo(@Param("policyNo") String policyNo);
}
