package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.DepartmentDao;
import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.service.IDeptMgntService;
import com.twfhclife.generic.annotation.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本資料管理-部門管理服務.
 * 
 * @author all
 */
@Service
public class DeptMgntServiceImpl implements IDeptMgntService {
	
	@Autowired
	private DepartmentDao departmentDao;

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
	public List<DepartmentVo> getDeptList(String userId, String username) {
		// 判斷目前登入者是否有最高權限管理員
		String adminUserFlag = (StringUtils.equals(username, systemAdminUser) ? "Y" : "N");
		return departmentDao.getDeptList(userId, adminUserFlag);
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

}
