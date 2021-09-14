package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.SystemsVo;

/**
 * 系統設定 Dao.
 * 
 * @author all
 */
public interface SystemsDao {
	
	/**
	 * 查詢所有系統別.
	 * 
	 * @return 回傳所有系統別.
	 */
	List<SystemsVo> findAll();
	
	/**
	 * 查詢系統別.
	 * 
	 * @param systemsVo SystemsVo
	 * @return 回傳查詢結果.
	 */
	List<SystemsVo> getSystems(@Param("systemsVo") SystemsVo systemsVo);
	
	List<String> getAllSysId();

}