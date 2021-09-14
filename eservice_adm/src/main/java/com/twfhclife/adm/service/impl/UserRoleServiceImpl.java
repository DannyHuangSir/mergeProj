package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.UserRoleDao;
import com.twfhclife.adm.model.UserRoleVo;
import com.twfhclife.adm.service.IUserRoleService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 人員角色管理服務.
 * 
 * @author all
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	/**
	 * 人員角色管理-分頁查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳人員角色管理-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getUserRolePageList(UserRoleVo userRoleVo) {
		return userRoleDao.getUserRolePageList(userRoleVo);
	}

	/**
	 * 人員角色管理-查詢結果總筆數.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getUserRolePageTotal(UserRoleVo userRoleVo) {
		return userRoleDao.getUserRolePageTotal(userRoleVo);
	}
	
	/**
	 * 人員角色管理-查詢.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<UserRoleVo> getUserRole(UserRoleVo userRoleVo) {
		return userRoleDao.getUserRole(userRoleVo);
	}

	/**
	 * 人員角色管理-新增.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int insertUserRole(UserRoleVo userRoleVo) {
		return userRoleDao.insertUserRole(userRoleVo);
	}
	
	/**
	 * 人員角色管理-更新.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int updateUserRole(UserRoleVo userRoleVo) {
		return userRoleDao.updateUserRole(userRoleVo);
	}

	/**
	 * 人員角色管理-刪除.
	 * 
	 * @param userRoleVo UserRoleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteUserRole(UserRoleVo userRoleVo) {
		return userRoleDao.deleteUserRole(userRoleVo);
	}
}