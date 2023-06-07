package com.twfhclife.eservice.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.AuditLogVo;

/**
 * 前台登入 Dao.
 * 
 * @author all
 */
public interface LoginDao {
	
	/**
	 * 新增登入記錄.
	 * 
	 * @param auditLogVo AuditLogVo
	 */
	public void addAuditLog(@Param("auditLogVo") AuditLogVo auditLogVo);
	
	/**
	 * 更新登出記錄.
	 * 
	 * @param userId 用戶名稱
	 */
	public void updateLogoutDate(@Param("userId") String userId);
	
	/**
	 * 取得最近登入日期.
	 * 
	 * @param userId 用戶名稱
	 */
	public Date getLastLoginTime(@Param("userId") String userId);
	
	/**
	 * 查詢最近的n筆登入記錄.
	 * 
	 * @param userId
	 * @param row
	 */
	public List<AuditLogVo> getLastAuditLog(@Param("userId") String userId, @Param("row") String row);
	
	public List<String> getpolicyInvestmentType();
}
