package com.twfhclife.generic.util;

import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.FunctionVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MenuUtil {

	private static final Logger logger = LogManager.getLogger(MenuUtil.class);

	public static List<FunctionVo> convertMenuTree(String userName, List<FunctionVo> dataList, String rootParentFunId) {
		List<FunctionVo> menuList = new ArrayList<>();
		for (FunctionVo func : dataList) {
			if (func.getParentFuncId().equals(rootParentFunId)) {
				menuList.add(func);
//				logger.info("{} function item: {}", userName, func);
				setSubMenuList(userName, dataList, func);
			}
		}
		
		setSubFuntionUrlList(menuList.get(0));
		return menuList;
	}

	private static void setSubFuntionUrlList(FunctionVo func) {
		List<String> subFuntionUrlList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(func.getSubMenuList())) {
			addFuntionUrl(subFuntionUrlList, func.getSubMenuList());
		}

		if (!CollectionUtils.isEmpty(subFuntionUrlList)) {
			func.getSubMenuList().forEach(subFunc -> setSubFuntionUrlList(subFunc));
		}
		func.setSubFuntionUrlList(subFuntionUrlList);
	}

	private static void addFuntionUrl(List<String> subFuntionUrlList, List<FunctionVo> dataList) {
		dataList.forEach(func -> {
			if (!StringUtils.isEmpty(func.getFunctionUrl())) {
				subFuntionUrlList.add(func.getFunctionUrl());
			}
			if (!CollectionUtils.isEmpty(func.getSubMenuList())) {
				addFuntionUrl(subFuntionUrlList, func.getSubMenuList());
			}
		});
	}

	private static void setSubMenuList(String userName, List<FunctionVo> dataList, FunctionVo parentFunc) {
		List<FunctionVo> subMenuList = new ArrayList<>();
		dataList.forEach(func -> {
			if (func.getParentFuncId().equals(parentFunc.getFunctionId().toString())) {
				logger.info("{} function item: {}", userName, func);
				subMenuList.add(func);
			}
		});

		if (!CollectionUtils.isEmpty(subMenuList)) {
			parentFunc.setSubMenuList(subMenuList);
			subMenuList.forEach(subFunc -> setSubMenuList(userName, dataList, subFunc));
		}
	}

	public List<LinkedHashMap<String, Object>> convertAceTree(List<FunctionItemVo> dataList, String rootParentFunId) {
		List<LinkedHashMap<String, Object>> aceTreeList = new ArrayList<>();
		for (FunctionItemVo func : dataList) {
			if (func.getParentFuncId().equals(rootParentFunId)) {
				Map<String, Object> rootMap = new HashMap<String, Object>();
				rootMap.put("icon-class", "orange");
				rootMap.put("id", func.getFunctionId());
				rootMap.put("par", "");
				rootMap.put("text", func.getFunctionName());
				rootMap.put("type", "folder");
				rootMap.put("divArr", "");
				rootMap.put("sort", "");
				rootMap.put("notify", "R");

				LinkedHashMap<String, Object> architectureMap = new LinkedHashMap<String, Object>();
				architectureMap.put("architecture", rootMap);
				aceTreeList.add(architectureMap);
				
				setSubAceTreeMap(dataList, rootMap);
			}
		}
		return aceTreeList;
	}

	private void setSubAceTreeMap(List<FunctionItemVo> dataList, Map<String, Object> parentFuncMap) {
		LinkedHashMap<String, Object> subAceTreeMap = new LinkedHashMap<>();
		int i = 0;
		for (FunctionItemVo func: dataList) {
			if (func.getParentFuncId().equals(parentFuncMap.get("id").toString())) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("icon-class", "green");
				dataMap.put("id", func.getFunctionId());
				dataMap.put("par", parentFuncMap.get("text").toString()); // 上層功能名稱
				dataMap.put("text", func.getFunctionName());
				dataMap.put("type", "F".equals(func.getFunctionType().trim()) ? "item" : "folder");
				dataMap.put("url", func.getFunctionUrl());
				dataMap.put("sort", func.getSort());
				dataMap.put("divArr", func.getDivArr());
				
				setSubAceTreeMap(dataList, dataMap);
				if ("FG".equals(func.getFunctionType())) {
					if (dataMap.get("additionalParameters") != null) {
						dataMap.put("notify", "Y");
					}
				}

				subAceTreeMap.put(String.valueOf(i), dataMap);
				i++;
			}			
		}

		if (!subAceTreeMap.isEmpty()) {
			LinkedHashMap<String, Object> childrenMap = new LinkedHashMap<>();
			childrenMap.put("children", subAceTreeMap);
			parentFuncMap.put("additionalParameters", childrenMap);
		}
	}
	
	public List<LinkedHashMap<String, Object>> convertDeptAceTree(List<DepartmentVo> dataList) {
		List<LinkedHashMap<String, Object>> aceTreeList = new ArrayList<>();
		for (DepartmentVo func : dataList) {
			if (func.getParentDep() == null) {
				Map<String, Object> rootMap = new HashMap<String, Object>();
				rootMap.put("icon-class", "orange");
				rootMap.put("id", func.getDepId());
				rootMap.put("par", "");
				rootMap.put("text", func.getDepName());
				rootMap.put("description", func.getDescription());
				rootMap.put("branchId",func.getBranchId());
				rootMap.put("type", "folder");
				rootMap.put("notify", "R");

				LinkedHashMap<String, Object> architectureMap = new LinkedHashMap<String, Object>();
				architectureMap.put("architecture", rootMap);
				aceTreeList.add(architectureMap);
				
				setDeptSubAceTreeMap(dataList, rootMap);
			}
		}
		return aceTreeList;
	}

	private void setDeptSubAceTreeMap(List<DepartmentVo> dataList, Map<String, Object> parentFuncMap) {
		LinkedHashMap<String, Object> subAceTreeMap = new LinkedHashMap<>();
		int i = 0;
		for (DepartmentVo func: dataList) {
			if (parentFuncMap.get("id").toString().equals(func.getParentDep())) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("icon-class", "green");
				dataMap.put("id", func.getDepId());
				dataMap.put("par", parentFuncMap.get("text").toString()); // 上層功能名稱
				dataMap.put("parentDep", func.getParentDep());
				dataMap.put("text", func.getDepName());
				dataMap.put("branchId",func.getBranchId());
				dataMap.put("description", func.getDescription());
				
				setDeptSubAceTreeMap(dataList, dataMap);
				if (dataMap.get("additionalParameters") != null) {
					dataMap.put("notify", "Y");
					dataMap.put("type", "folder");
				} else {
					dataMap.put("type", "item");
				}

				subAceTreeMap.put(String.valueOf(i), dataMap);
				i++;
			}			
		}

		if (!subAceTreeMap.isEmpty()) {
			LinkedHashMap<String, Object> childrenMap = new LinkedHashMap<>();
			childrenMap.put("children", subAceTreeMap);
			parentFuncMap.put("additionalParameters", childrenMap);
		}
	}
}
