package com.twfhclife.adm.service;

import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.ParameterVo;

import java.util.List;
import java.util.Map;

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
    public List<DepartmentVo> getDeptList(String userId, String username,DepartmentVo vo);

    /**
     * 經代專區判斷部門名稱是否存在.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳部門名稱是否存在
     */
    public boolean isDeptNameExist(DepartmentVo departmentVo);

    public boolean isDeptIdExist(DepartmentVo departmentVo);

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

    List<DepartmentVo> getDeptParentList(String userId, String username);

    List<Map<String, Object>> getNotifyDepts(String passageWay);

    List<ParameterVo> getPassageWay();

    boolean isBranchIdExist(DepartmentVo departmentVo);

    DepartmentVo getDepId(String depId);

    public List<DepartmentVo> getBranchList(String userId, String username,DepartmentVo vo);

    DepartmentVo getBranchId(String depId,String branchId);

    int getDepLevel(DepartmentVo departmentVo);

    int getDepBranch(DepartmentVo departmentVo);

    DepartmentVo getDivDep(String parentDep,String branchId);

    int insertUserDep(String userId,String depId,String titleId,String branchId);

    int countDeptName(String depName);

    int countBranchName(String parentDep,String depName);

    int countBranchId(String parentDep,String branchId);

    int updateUserDep(String userId,String depId,String branchId);

    int countUserDep(String userId,String depId,String branchId);

    List<DepartmentVo> getBranchByList(String userId, String username,DepartmentVo vo);
}
