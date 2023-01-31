package com.twfhclife.adm.service;

import com.twfhclife.adm.model.DepartmentVo;

import java.util.List;

/**
 * 基本資料管理-部門管理服務.
 * 
 * @author all
 */
public interface IDeptMgntService {
	
	/**
	 * 取得所有部門.
	 * 
	 * @return 回傳所有部門.
	 */	
	public List<DepartmentVo> getDepts();
	
	/**
	 * 取得使用者部門清單.
	 * 
	 * @param userId 使用者ID
	 * @return 回傳所有部門.
	 */	
	public List<DepartmentVo> getDeptList(String userId, String username);
	
	/**
	 * 判斷部門名稱是否存在.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳部門名稱是否存在
	 */
	public boolean isDeptNameExist(DepartmentVo departmentVo);

	/**
	 * 新增部門節點.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳影響筆數
	 */
	public int addDepartment(DepartmentVo departmentVo);

	/**
	 * 更新部門節點.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳影響筆數
	 */
	public int updateDepartment(DepartmentVo departmentVo);

	/**
	 * 刪除部門節點.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳影響筆數
	 */
	public int deleteDepartment(DepartmentVo departmentVo);

	/**
	 * 取得上一層部門清單.
	 * 
	 * @param departmentVo DepartmentVo
	 * @return 回傳上一層部門清單
	 */
	public List<DepartmentVo> getParentDepList(DepartmentVo departmentVo);

}
