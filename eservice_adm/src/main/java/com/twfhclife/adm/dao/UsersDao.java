package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.UsersVo;

/**
 * 人員資訊 Dao.
 * 
 * @author all
 */
public interface UsersDao {
	
	/**
	 * 人員資訊-查詢.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳查詢結果
	 */
	List<UsersVo> getUsers(@Param("usersVo") UsersVo usersVo);
	
	/**
	 * 人員資訊-檢查帳號是否存在.
	 * 
	 * @param userId 使用者帳號
	 * @return 回傳筆數
	 */
	int checkUserIdExist(@Param("userId") String userId);
	
	/**
	 * 人員資訊-新增.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int insertUsers(@Param("usersVo") UsersVo usersVo);

	/**
	 * 人員資訊-刪除.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int deleteUsers(@Param("usersVo") UsersVo usersVo);
	
	/**
	 * 人員資訊-更新線上申請使用
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int updateOnlineChangeUse(@Param("usersVo") UsersVo usersVo);

	/**
	 * 人員資訊-更新.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int updateUsers(@Param("usersVo") UsersVo usersVo);

}