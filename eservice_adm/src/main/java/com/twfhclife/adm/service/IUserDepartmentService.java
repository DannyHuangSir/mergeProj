package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.UserDepartmentVo;

/**
 * 人員部門管理服務.
 * 
 * @author all
 */
public interface IUserDepartmentService {
	
	/**
	 * 人員部門管理-分頁查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳人員部門管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getUserDepartmentPageList(UserDepartmentVo userDepartmentVo);

	/**
	 * 人員部門管理-查詢結果總筆數.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳總筆數
	 */
	public int getUserDepartmentPageTotal(UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員部門管理-查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳查詢結果
	 */
	public List<UserDepartmentVo> getUserDepartment(UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員部門管理-新增.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	public int insertUserDepartment(UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員部門管理-更新.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	public int updateUserDepartment(UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員部門管理-刪除.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	public int deleteUserDepartment(UserDepartmentVo userDepartmentVo);
}