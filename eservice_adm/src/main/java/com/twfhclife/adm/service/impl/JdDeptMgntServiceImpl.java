package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JdDepartmentDao;
import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.ParameterVo;
import com.twfhclife.adm.service.IJdDeptMgntService;
import com.twfhclife.generic.annotation.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Service
public class JdDeptMgntServiceImpl implements IJdDeptMgntService {

    @Autowired
    private JdDepartmentDao departmentDao;

    /**
     * 後台預設最高權限管理員帳號
     */
    @Value("${eservice_admin.administrator}")
    protected String systemAdminUser;

    /**
     * 取得所有部門.
     *
     * @return 回傳所有部門.
     */
    @RequestLog
    public List<DepartmentVo> getDepts() {
        return departmentDao.getDepts();
    }

    /**
     * 取得使用者部門清單.
     *
     * @param userId 使用者ID
     * @return 回傳所有部門.
     */
    @RequestLog
    public List<DepartmentVo> getDeptList(String userId, String username,DepartmentVo vo) {
        // 判斷目前登入者是否有最高權限管理員
        String adminUserFlag = (StringUtils.equals(username, systemAdminUser) ? "Y" : "N");
        return departmentDao.getDeptList(userId, adminUserFlag,vo);
    }

    /**
     * 判斷部門名稱是否存在.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳部門名稱是否存在
     */
    @RequestLog
    public boolean isDeptNameExist(DepartmentVo departmentVo) {
        boolean depNameExist = false;
        String parentDep = departmentVo.getParentDep();
        String depName = departmentVo.getDepName();
        String depId = departmentVo.getDepId();
        List<DepartmentVo> dataList = departmentDao.getDepts();
        for (DepartmentVo depVo: dataList) {
            if (parentDep != null && depName != null && !depVo.getDepId().equals(depId)) {
                if (parentDep.equals(depVo.getParentDep()) && depName.equals(depVo.getDepName())) {
                    depNameExist = true;
                    break;
                }
            }
        }
        return depNameExist;
    }

    @Override
    public boolean isDeptIdExist(DepartmentVo departmentVo) {
        boolean depNameExist = false;
        String depId = departmentVo.getDepId();
        List<DepartmentVo> dataList = departmentDao.getDepts();
        for (DepartmentVo depVo: dataList) {
            if (depVo.getDepId().equals(depId)) {
                    depNameExist = true;
                    break;
            }
        }
        return depNameExist;
    }

    /**
     * 新增部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    @RequestLog
    public int addDepartment(DepartmentVo departmentVo) {
        return departmentDao.addDepartment(departmentVo);
    }

    /**
     * 更新部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    @RequestLog
    public int updateDepartment(DepartmentVo departmentVo) {
        return departmentDao.updateDepartment(departmentVo);

    }

    /**
     * 刪除部門節點.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳影響筆數
     */
    @RequestLog
    public int deleteDepartment(DepartmentVo departmentVo) {
        return departmentDao.deleteDepartment(departmentVo.getDepId());
    }

    /**
     * 取得上一層部門清單.
     *
     * @param departmentVo DepartmentVo
     * @return 回傳上一層部門清單
     */
    @RequestLog
    public List<DepartmentVo> getParentDepList(DepartmentVo departmentVo) {
        List<DepartmentVo> deptList = departmentDao.getDepts();
        String parantDep = "";
        for (DepartmentVo deptVo : deptList) {
            if (departmentVo.getParentDep() != null && departmentVo.getParentDep().equals(deptVo.getDepId())) {
                parantDep = deptVo.getParentDep();
            }
        }

        List<DepartmentVo> parentDepList = new ArrayList<>();
        if (!StringUtils.isEmpty(parantDep)) {
            for (DepartmentVo deptVo : deptList) {
                if (parantDep.equals(deptVo.getParentDep())) {
                    parentDepList.add(deptVo);
                }
            }
        }
        return parentDepList;
    }

    @Override
    public List<DepartmentVo> getDeptParentList(String userId, String username) {
        // 判斷目前登入者是否有最高權限管理員
        String adminUserFlag = (StringUtils.equals(username, systemAdminUser) ? "Y" : "N");
        return departmentDao.getDeptParentList(userId, adminUserFlag);
    }

    @Override
    public List<Map<String, Object>> getNotifyDepts(String passageWay) {
        return departmentDao.getNotifyDepts(passageWay);
    }

    @Override
    public List<ParameterVo> getPassageWay() {
        return departmentDao.getPassageWay();
    }

    @RequestLog
    public boolean isBranchIdExist(DepartmentVo departmentVo) {
        boolean BranchIdExist = false;
        String parentDep = departmentVo.getParentDep();
        String branchId = departmentVo.getBranchId();
        List<DepartmentVo> dataList = departmentDao.getDepts();
        for (DepartmentVo depVo: dataList) {
            if (StringUtils.isNotBlank(parentDep) && StringUtils.isNotBlank(branchId)) {
                if (parentDep.equals(depVo.getParentDep()) && branchId.equals(depVo.getBranchId())) {
                    BranchIdExist = true;
                    break;
                }
            }
        }
        return BranchIdExist;
    }

    @Override
    public DepartmentVo getDepId(String depId) {
        return departmentDao.getDepId(depId);
    }

    @Override
    public List<DepartmentVo> getBranchList(String userId, String username, DepartmentVo vo) {
        String adminUserFlag = (StringUtils.equals(username, systemAdminUser) ? "Y" : "N");
        return departmentDao.getBranchList(userId, adminUserFlag,vo);
    }

    @Override
    public DepartmentVo getBranchId(String depId, String branchId) {
        return departmentDao.getBranchId(depId,branchId);
    }

    @Override
    public int getDepLevel(DepartmentVo departmentVo) {
        return departmentDao.getDepLevel(departmentVo);
    }

    @Override
    public int getDepBranch(DepartmentVo departmentVo) {
        return 0;
    }

    @Override
    public DepartmentVo getDivDep(String parentDep, String branchId) {
        return departmentDao.getDivDep(parentDep,branchId);
    }

    @Override
    public int insertUserDep(String userId,String depId,String titleId,String branchId) {
        return departmentDao.insertUserDep(userId,depId,titleId,branchId);
    }

    @Override
    public int countDeptName(String depName) {
        return departmentDao.countDepName(depName);
    }

    @Override
    public int countBranchName(String parentDep,String depName) {
        return departmentDao.countBranchName(parentDep,depName);
    }

    @Override
    public int countBranchId(String parentDep, String branchId) {
        return departmentDao.countBranchId(parentDep,branchId);
    }

    @Override
    public int updateUserDep(String userId, String depId, String branchId) {
        return departmentDao.updateUserDep(userId,depId,branchId);
    }

    @Override
    public int countUserDep(String userId, String depId, String branchId) {
        return departmentDao.countUserDep(userId,depId,branchId);
    }

    @Override
    public List<DepartmentVo> getBranchByList(String userId, String username, DepartmentVo vo) {
        String adminUserFlag = (StringUtils.equals(username, systemAdminUser) ? "Y" : "N");
        return departmentDao.getBranchByList(userId, adminUserFlag,vo);
    }


}
