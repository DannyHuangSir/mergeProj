package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.*;
import com.twfhclife.adm.domain.DownloadUserAuthCSVResponse;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IAuthorizationService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.util.MyStringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 權限查詢與報表服務.
 * 
 * @author all
 */
@Service
public class AuthorizationServiceImpl implements IAuthorizationService {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private UserDepartmentDao userDepartmentDao;
	
	@Autowired
	private RoleSysAuthDao roleSysAuthDao;
	
	@Autowired
	private RoleDivAuthDao roleDivAuthDao;
	
	@Autowired
	private RoleFuncAuthDao roleFuncAuthDao;
	
	@Autowired
	private FunctionItemDao functionItemDao;
	
	@Autowired
	private FunctionDivDao functionDivDao;
	
	@Autowired
	private AuthorizationDao authorizationDao;
	
	@Autowired
	private SystemsDao systemsDao;

	@Autowired
	private JdUserDepartmentDao JdUserDepartmentDao;
	
	/**
	 * 設定使用者的系統跟角色權限資料.
	 * 
	 * @param userAuthList 使用者清單
	 */
	@RequestLog
	@Override
	public void setUserSystemRoleAuth(List<Map<String, Object>> userAuthList) {
		for (Map<String, Object> userMap : userAuthList) {
			String userId = (String) userMap.get("ID"); // USER_ENTITY.ID
			List<String> sysIdList = new ArrayList<>();
			List<String> sysNameList = new ArrayList<>();
			List<String> roleIdList = new ArrayList<>();
			List<String> roleNameList = new ArrayList<>();
			List<String> depNameList = new ArrayList<>();
			List<String> titleNameList = new ArrayList<>();
			
			// 取得使用者的角色清單
			UserRoleVo qryVo = new UserRoleVo();
			qryVo.setUserId(userId);
			List<UserRoleVo> userRoleList = userRoleDao.getUserRole(qryVo);
			for (UserRoleVo userRoleVo : userRoleList) {
				String roleId = userRoleVo.getRoleId();
				String roleName = MyStringUtil.nullToString(userRoleVo.getRoleName());
				if (!roleIdList.contains(roleId)) {
					roleIdList.add(roleId);
				}
				if (!roleNameList.contains(roleName)) {
					roleNameList.add(roleName);
				}
				
				// 取得使用者的系統清單
				RoleSysAuthVo sysQryVo = new RoleSysAuthVo();
				sysQryVo.setRoleId(roleId);
				List<RoleSysAuthVo> roleSysAuthList = roleSysAuthDao.getRoleSysAuth(sysQryVo);
				for (RoleSysAuthVo roleSysAuthVo : roleSysAuthList) {
					String sysId = roleSysAuthVo.getSysId();
					String sysName = roleSysAuthVo.getSysName();
					if (!sysIdList.contains(sysId)) {
						sysIdList.add(sysId);
					}
					if (!sysNameList.contains(sysName)) {
						sysNameList.add(sysName);
					}
				}
			}
			
			// 取得使用者的部門職位清單
			UserDepartmentVo deptQryVo = new UserDepartmentVo();
			deptQryVo.setUserId(userId);
			List<UserDepartmentVo> userDeptList = userDepartmentDao.getUserDepartment(deptQryVo);
			for (UserDepartmentVo userDeptVo : userDeptList) {
				String depName = userDeptVo.getDepName();
				String titleName = userDeptVo.getTitleName();
				if (!StringUtils.isEmpty(depName) && !depNameList.contains(depName)) {
					depNameList.add(depName);
				}
				if (!StringUtils.isEmpty(titleName) && !titleNameList.contains(titleName)) {
					titleNameList.add(titleName);
				}
			}
			
			// 設定系統跟角色資料
			userMap.put("ROLE_ID_LIST", String.join(",", roleIdList));
			userMap.put("ROLE_NAME_LIST", String.join(",", roleNameList));
			userMap.put("SYS_ID_LIST", String.join(",", sysIdList));
			userMap.put("SYS_NAME_LIST", String.join(",", sysNameList));
			userMap.put("DEP_NAME_LIST", String.join(",", depNameList));
			userMap.put("TITLE_NAME_LIST", String.join(",", titleNameList));
		}
	}
	
	/**
	 * 取得使用者角色功能權限樹.
	 * 
	 * @param roleIds 使用者角色
	 * @param disable 是否要disable選取
	 * @return 回傳使用者角色功能權限資料
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getRoleTree(List<String> roleIds, boolean disable) {
		// 取得使用者帳號/角色下的能使用的 functionItem 功能
		List<FunctionItemVo> allSysFuncList = new LinkedList<FunctionItemVo>();
//		List<String> sysIds = functionItemDao.getUnionSysId();
		List<String> sysIds = systemsDao.getAllSysId();
		for (String sysId : sysIds) {
			List<FunctionItemVo> sysFuncList = functionItemDao.getAllFuncBySysId(sysId);
			for (FunctionItemVo vo : sysFuncList) {
				allSysFuncList.add(vo);
			}
		}
		
		// 取得使用者帳號/角色下的不能使用的 functiondDiv 功能
		List<FunctionDivVo> funDivList = functionDivDao.findAll();

		// 取得角色設定的功能權限
		List<String> funcIdList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roleIds)) {
			funcIdList = roleFuncAuthDao.getFunctionIdByRoleIdList(roleIds);
		}
		
		// 取得角色設定的DIV權限
		List<String> divIdList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roleIds)) {
			divIdList = roleDivAuthDao.getDivIdByRoleIdList(roleIds);
		}

		// 建立 jsTree 的資料清單
		List<Map<String, Object>> roleFunTreeList = new ArrayList<Map<String, Object>>();
		for(FunctionItemVo fVo : allSysFuncList){
			Map<String, Object> rootMap = new HashMap<String, Object>();
			Map<String, Object> stateMap = new HashMap<String, Object>();
			rootMap.put("id", fVo.getFunctionId());
			String parent = fVo.getParentFuncId();
			if (parent.equals(fVo.getSysId())) {
				rootMap.put("icon", "fa fa-th-list green");
				parent = "#";
			} else {
				if (fVo.getFunctionType().equals("F")) {
					rootMap.put("icon", "fa fa-file grey");
				} else {
					rootMap.put("icon", "fa fa-folder blue");
				}
			}
			rootMap.put("parent", parent);
			rootMap.put("text", fVo.getFunctionName());
			rootMap.put("sysId", fVo.getSysId());
			
			if ("F".equals(fVo.getFunctionType())) {
				if (funcIdList.contains(fVo.getFunctionId())) {
					stateMap.put("selected", true);
				}
			}

			stateMap.put("disabled", disable);
			rootMap.put("state", stateMap);
			roleFunTreeList.add(rootMap);
		}
		
		// 組DIV區塊
		for(FunctionItemVo funcItemVo : allSysFuncList) {
			if ("F".equals(funcItemVo.getFunctionType())) {
				// 從funDivList 資料設定 div 區塊
				for(FunctionDivVo funcDivVo : funDivList) {
					if (funcDivVo.getFunctionId().toString().equals(funcItemVo.getFunctionId())) {
						Map<String, Object> divMap = new HashMap<String, Object>();
						Map<String, Object> divStateMap = new HashMap<String, Object>();
						divMap.put("id", "div_" + funcDivVo.getDivId());
						divMap.put("parent", funcItemVo.getFunctionId());
						divMap.put("text", funcDivVo.getDivName());
						divMap.put("icon", "fa fa-bookmark orange2");
						
						if (divIdList.contains(funcDivVo.getDivId().toString())) {
							divStateMap.put("selected", true);
						}
						
						divStateMap.put("disabled", disable);
						divMap.put("state", divStateMap);
						roleFunTreeList.add(divMap);
					}
				}
			}
		}
		
		return roleFunTreeList;
	}

	@RequestLog
	@Override
	public List<DownloadUserAuthCSVResponse> downloadUserAuthCSV(String sysId) {
		return authorizationDao.downloadUserAuthCSV(sysId);
	}

	/**
	 * 经代专区
	 * @param userAuthList 使用者清單
	 */
	@RequestLog
	@Override
	public void setJdUserSystemRoleAuth(List<Map<String, Object>> userAuthList) {
		for (Map<String, Object> userMap : userAuthList) {
			String userId = (String) userMap.get("ID"); // USER_ENTITY.ID
			List<String> sysIdList = new ArrayList<>();
			List<String> sysNameList = new ArrayList<>();
			List<String> roleIdList = new ArrayList<>();
			List<String> roleNameList = new ArrayList<>();
			List<String> depNameList = new ArrayList<>();
			List<String> titleNameList = new ArrayList<>();
			List<String> branchNameList = new ArrayList<>();
			List<String> divRoleIdList = new ArrayList<>();

			// 取得使用者的角色清單
			UserRoleVo qryVo = new UserRoleVo();
			qryVo.setUserId(userId);
			List<UserRoleVo> userRoleList = userRoleDao.getUserRole(qryVo);
			for (UserRoleVo userRoleVo : userRoleList) {
				String roleId = userRoleVo.getRoleId();
				String roleName = MyStringUtil.nullToString(userRoleVo.getRoleName());
				String divRoleId = userRoleVo.getDivRoleId();
				if (!roleIdList.contains(roleId)) {
					roleIdList.add(roleId);
				}
				if (!roleNameList.contains(roleName)) {
					roleNameList.add(roleName);
				}
				if (!divRoleIdList.contains(divRoleId)){
					divRoleIdList.add(divRoleId);
				}
				// 取得使用者的系統清單
				RoleSysAuthVo sysQryVo = new RoleSysAuthVo();
				sysQryVo.setRoleId(roleId);
				List<RoleSysAuthVo> roleSysAuthList = roleSysAuthDao.getRoleSysAuth(sysQryVo);
				for (RoleSysAuthVo roleSysAuthVo : roleSysAuthList) {
					String sysId = roleSysAuthVo.getSysId();
					String sysName = roleSysAuthVo.getSysName();
					if (!sysIdList.contains(sysId)) {
						sysIdList.add(sysId);
					}
					if (!sysNameList.contains(sysName)) {
						sysNameList.add(sysName);
					}
				}
			}

			// 取得使用者的通路分支機構清單
			UserDepartmentVo deptQryVo = new UserDepartmentVo();
			deptQryVo.setUserId(userId);
			List<UserDepartmentVo> userDeptList = JdUserDepartmentDao.getUserDepartment(deptQryVo);
			for (UserDepartmentVo userDeptVo : userDeptList) {
				String depName = userDeptVo.getDepName();
				String titleName = userDeptVo.getTitleName();

				if (!StringUtils.isEmpty(depName) && !depNameList.contains(depName)) {
					depNameList.add(depName);
				}
				if (!StringUtils.isEmpty(titleName) && !titleNameList.contains(titleName)) {
					titleNameList.add(titleName);
				}

			}
			List<UserDepartmentVo> userBranchList = JdUserDepartmentDao.getUserBranch(deptQryVo);
			for (UserDepartmentVo vo:userBranchList) {
				String branchName = vo.getBranchName();
				if (!StringUtils.isEmpty(branchName) && !branchNameList.contains(branchName)) {
					branchNameList.add(branchName);
				}

			}

			// 設定系統跟角色資料
			userMap.put("ROLE_ID_LIST", String.join(",", roleIdList));
			userMap.put("ROLE_NAME_LIST", String.join(",", roleNameList));
			userMap.put("SYS_ID_LIST", String.join(",", sysIdList));
			userMap.put("SYS_NAME_LIST", String.join(",", sysNameList));
			userMap.put("DEP_NAME_LIST", String.join(",", depNameList));
			userMap.put("TITLE_NAME_LIST", String.join(",", titleNameList));
			userMap.put("BRACH_NAME_LIST",String.join(",", branchNameList));
			userMap.put("DIV_ROLE_ID_LIST",String.join(",", divRoleIdList));
			userMap.put("DEP_ID", userDeptList.size() > 0 ? userDeptList.get(0).getDepId() : "");
		}
	}
}