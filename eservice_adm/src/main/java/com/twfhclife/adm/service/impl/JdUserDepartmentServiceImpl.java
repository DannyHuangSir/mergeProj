package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdUserDepartmentDao;
import com.twfhclife.adm.model.UserDepartmentVo;
import com.twfhclife.adm.service.IJdUserDepartmentService;
import com.twfhclife.generic.annotation.RequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 人員角色管理服務.
 * 
 * @author all
 */
@Service
public class JdUserDepartmentServiceImpl implements IJdUserDepartmentService {

	@Autowired
	private JdUserDepartmentDao userDepartmentDao;
	
	/**
	 * 人員角色管理-分頁查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳人員角色管理-分頁查詢結果
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getUserDepartmentPageList(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.getUserDepartmentPageList(userDepartmentVo);
	}

	/**
	 * 人員角色管理-查詢結果總筆數.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getUserDepartmentPageTotal(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.getUserDepartmentPageTotal(userDepartmentVo);
	}
	
	/**
	 * 人員角色管理-查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<UserDepartmentVo> getUserDepartment(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.getUserDepartment(userDepartmentVo);
	}

	/**
	 * 人員角色管理-新增.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int insertUserDepartment(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.insertUserDepartment(userDepartmentVo);
	}
	
	/**
	 * 人員角色管理-更新.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int updateUserDepartment(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.updateUserDepartment(userDepartmentVo);
	}

	/**
	 * 人員角色管理-刪除.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteUserDepartment(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.deleteUserDepartment(userDepartmentVo);
	}

	@Override
	public List<UserDepartmentVo> getUserBranch(UserDepartmentVo userDepartmentVo) {
		return userDepartmentDao.getUserBranch(userDepartmentVo);
	}

	@Override
	public int countUserDep(String userId) {
		return userDepartmentDao.countUserDep(userId);
	}
}