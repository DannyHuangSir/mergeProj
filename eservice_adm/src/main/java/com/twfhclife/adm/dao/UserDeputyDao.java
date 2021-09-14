package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.UserDeputyVo;

/**
 * 人員代理人管理 Dao
 * @author Ken Wei
 *
 */
public interface UserDeputyDao {

	/**
	 * 人員代理人管理-查詢
	 * @param userDeputyVo
	 * @return
	 */
	public List<UserDeputyVo> getUserDeputy(@Param("userDeputyVo") UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-新增
	 * @param userDeputyVo
	 * @return
	 */
	public int insertUserDeputy(@Param("userDeputyVo") UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-查詢結果總筆數
	 * @param userDeputyVo
	 * @return
	 */
	public int getUserDeputyPageTotal(@Param("userDeputyVo") UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-分頁查詢
	 * @param userDeputyVo
	 * @return
	 */
	public List<Map<String, Object>> getUserDeputyPageList(@Param("userDeputyVo") UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-可用代理人查詢
	 * @param userDeputyVo
	 * @param admRealm
	 * @return
	 */
	public List<Map<String, Object>> getCanDeputyUser(@Param("userDeputyVo") UserDeputyVo userDeputyVo, @Param("admRealm") String admRealm);
	
	/**
	 * 人員代理人管理-代理人刪除
	 * @param id
	 * @return
	 */
	public int deleteDeputyById(@Param("id") Integer id);
}
