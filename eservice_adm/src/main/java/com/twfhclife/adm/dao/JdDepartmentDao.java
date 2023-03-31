package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.ParameterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Mapper
public interface JdDepartmentDao {
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
    public List<DepartmentVo> getDeptList(@Param("userId") String userId, @Param("adminUserFlag") String adminUserFlag,@Param("vo")DepartmentVo vo);

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

    List<Map<String, Object>> getNotifyDepts(@Param("passageWay") String passageWay);


    /**
     * 獲取通路數組
     * @param userId
     * @param adminUserFlag
     * @return
     */
    List<DepartmentVo> getDeptParentList(@Param("userId") String userId, @Param("adminUserFlag") String adminUserFlag);

    List<ParameterVo> getPassageWay();
    DepartmentVo getDepId(@Param("depId")String depId);

    List<DepartmentVo> getBranchList(@Param("userId") String userId, @Param("adminUserFlag") String adminUserFlag,@Param("vo")DepartmentVo vo);

    DepartmentVo getBranchId(@Param("depId") String depId,@Param("branchId")String branchId);

    int getDepLevel(@Param("departmentVo") DepartmentVo departmentVo);

    int getDepBranch(@Param("departmentVo")DepartmentVo departmentVo);

    DepartmentVo getDivDep(@Param("parentDep") String parentDep,@Param("branchId")String branchId);

    int insertUserDep(@Param("userId")String userId,@Param("depId")String depId,@Param("titleId")String titleId,@Param("branchId")String branchId);

    int countDepName(@Param("depName") String depName);

    int countBranchName(@Param("parentDep") String parentDep,@Param("depName") String depName);

    int countBranchId(@Param("parentDep") String parentDep,@Param("branchId") String branchId);

    int updateUserDep(@Param("userId") String userId,@Param("depId") String depId,@Param("branchId") String branchId);

    int countUserDep(@Param("userId") String userId,@Param("depId") String depId,@Param("branchId") String branchId);

    List<DepartmentVo> getBranchByList(@Param("userId") String userId, @Param("adminUserFlag") String adminUserFlag,@Param("vo")DepartmentVo vo);

}
