package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.FunctionVo;

public interface LoginDao {

	List<FunctionVo> getMenuList(@Param("systemId") String systemId,
			@Param("keyCloakUserId") String keyCloakUserId,
			@Param("adminUserFlag") String adminUserFlag);

	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	List<String> getRoleName(@Param("userId") String userId);
}
