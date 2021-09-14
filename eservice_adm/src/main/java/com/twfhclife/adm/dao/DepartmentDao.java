package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.DepartmentVo;

/**
 * 部門維護 Dao.
 * 
 * @author all
 */
public interface DepartmentDao {
	
	/**
	 * 取得所有部門.
	 * 
	 * @return 回傳所有部門.
	 */	
	List<DepartmentVo> getDepts();
	
	/**
	 * 取得使用者部門清單.
	 * 
	 * @param userId 使用者ID
	 * @param adminUserFlag 是否為管理員
	 * @return 回傳所有部門.
	 */
	public List<DepartmentVo> getDeptList(@Param("userId") String userId, @Param("adminUserFlag") String adminUserFlag);
	
	/**
	 * 判斷部門名稱是否存在.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳部門名稱是否存在
	 */
	boolean isDeptNameExist(@Param("departmentVo") DepartmentVo departmentVo);

	/**
	 * 新增部門節點.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳影響筆數
	 */
	int addDepartment(@Param("departmentVo") DepartmentVo departmentVo);

	/**
	 * 更新部門節點.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳影響筆數
	 */
	int updateDepartment(@Param("departmentVo") DepartmentVo departmentVo);

	/**
	 * 刪除部門節點.
	 * 
	 * @param depId 部門id
	 * @return 回傳影響筆數
	 */
	int deleteDepartment(@Param("depId") String depId);

}
