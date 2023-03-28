package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.UserDepartmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人員職位管理 Dao.
 * 
 * @author all
 */
public interface JdUserDepartmentDao {
	
	/**
	 * 人員職位管理-分頁查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳人員職位管理-分頁查詢結果
	 */
	List<Map<String, Object>> getUserDepartmentPageList(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);

	/**
	 * 人員職位管理-查詢結果總筆數.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳總筆數
	 */
	int getUserDepartmentPageTotal(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員職位管理-查詢.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳查詢結果
	 */
	List<UserDepartmentVo> getUserDepartment(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員職位管理-新增.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	int insertUserDepartment(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);
	
	/**
	 * 人員職位管理-更新.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	int updateUserDepartment(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);

	/**
	 * 人員職位管理-刪除.
	 * 
	 * @param userDepartmentVo UserDepartmentVo
	 * @return 回傳影響筆數
	 */
	int deleteUserDepartment(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);

	List<UserDepartmentVo> getUserBranch(@Param("userDepartmentVo") UserDepartmentVo userDepartmentVo);

	int countUserDep(@Param("userId")String userId);
}

