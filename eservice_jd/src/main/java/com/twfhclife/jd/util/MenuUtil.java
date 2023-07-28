package com.twfhclife.jd.util;

import com.twfhclife.jd.web.model.FunctionVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
}
