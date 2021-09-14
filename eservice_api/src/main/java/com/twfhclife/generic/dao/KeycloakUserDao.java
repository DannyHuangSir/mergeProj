package com.twfhclife.generic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.keycloak.representations.idm.UserRepresentation;

import com.twfhclife.eservice.api.adm.model.UserEntityVo;


public interface KeycloakUserDao {
	
	UserRepresentation findByUserName(@Param("realm") String realm, @Param("username") String username);
	
	List<Map<String, String>> findByUserAttributes(@Param("userId") String userId);
	
	/**
	 * 人員管理-分頁查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳人員管理-分頁查詢結果
	 */
	List<Map<String, Object>> getUserEntityPageList(@Param("userEntityVo") UserEntityVo userEntityVo,
			@Param("adminUserFlag") String adminUserFlag);

	/**
	 * 人員管理-查詢結果總筆數.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @param adminUserFlag 最高管理員權限註記
	 * @return 回傳總筆數
	 */
	int getUserEntityPageTotal(@Param("userEntityVo") UserEntityVo userEntityVo,
			@Param("adminUserFlag") String adminUserFlag);
	
	/**
	 * 人員管理-查詢.
	 * 
	 * @param userEntityVo UserEntityVo
	 * @return 回傳查詢結果
	 */
	List<UserEntityVo> getUserEntity(@Param("userEntityVo") UserEntityVo userEntityVo);
	
	String getUserIdByUsername(@Param("username") String username, @Param("realm") String realm);
}
