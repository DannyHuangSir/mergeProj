package com.twfhclife.adm.service;

import com.twfhclife.adm.model.JdUserVo;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.model.UserEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 人員管理服務.
 * 
 * @author all
 */
public interface IJdUserMgntService {
	
	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getUserEntityPageList(String userId, UserEntityVo userEntityVo);

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳總筆數
	 */
	public int getUserEntityPageTotal(String userId, UserEntityVo userEntityVo);
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	List<UserEntityVo> getUserEntity(UserEntityVo userEntityVo);

	List<Map<String, Object>> getNotifyUsers(NotifySearchVo userEntityVo);

	List<Map<String, Object>> getJdUserQuery(JdUserVo vo);

	int countJdUser(JdUserVo vo);

	List<Map<String, Object>> getJdUserIcQuery(JdUserVo vo);

	int countJdUserIc(JdUserVo vo);

	UserEntityVo getUser(String userName,String realId);
}