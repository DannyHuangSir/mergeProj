package com.twfhclife.eservice.api.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.api.adm.model.UserEntityVo;

/**
 * 人員管理服務.
 * 
 * @author all
 */
public interface IUserMgntService {
	
	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getUserEntityPageList(UserEntityVo userEntityVo, boolean isAdmin);

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userId 使用者代號
	 * @param userEntityVo UserEntityVo
	 * @return 回傳總筆數
	 */
	public int getUserEntityPageTotal(UserEntityVo userEntityVo, boolean isAdmin);
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	List<UserEntityVo> getUserEntity(UserEntityVo userEntityVo);
}