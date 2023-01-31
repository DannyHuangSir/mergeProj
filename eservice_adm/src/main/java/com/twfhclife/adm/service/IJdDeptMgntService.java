package com.twfhclife.adm.service;

import com.twfhclife.adm.model.DepartmentVo;

import java.util.List;

/**
 * @auther lihao
 */
public interface IJdDeptMgntService {
    /**
     * 經代專區取得所有部門.
     *
     * @return 回傳所有部門.
     */
    public List<DepartmentVo> getDepts();

    /**
     * 經代專區取得使用者部門清單.
     *
     * @param userId 使用者ID
     * @return 回傳所有部門.
     */
    public List<DepartmentVo> getDeptList(String userId, String username);

    /**
     * 經代專區判斷部門名稱是否存在.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳部門名稱是否存在
     */
    public boolean isDeptNameExist(DepartmentVo departmentVo);

    /**
     * 經代專區新增部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    public int addDepartment(DepartmentVo departmentVo);

    /**
     * 經代專區更新部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    public int updateDepartment(DepartmentVo departmentVo);

    /**
     * 經代專區刪除部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    public int deleteDepartment(DepartmentVo departmentVo);

    /**
     * 經代專區取得上一層部門清單.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳上一層部門清單
     */
    public List<DepartmentVo> getParentDepList(DepartmentVo departmentVo);
}