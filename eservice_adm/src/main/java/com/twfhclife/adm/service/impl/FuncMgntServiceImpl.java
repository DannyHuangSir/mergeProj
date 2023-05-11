package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.FunctionDivDao;
import com.twfhclife.adm.dao.FunctionItemDao;
import com.twfhclife.adm.model.FunctionDivVo;
import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.SystemsVo;
import com.twfhclife.adm.service.IFuncMgntService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.FuncMgmtClient;
import com.twfhclife.generic.util.MyStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 權限管理-功能維護服務.
 * 
 * @author all
 */
@Service
public class FuncMgntServiceImpl implements IFuncMgntService {

	@Autowired
	private FunctionItemDao functionItemDao;

	@Autowired
	private FunctionDivDao functionDivDao;

	@Autowired
	FuncMgmtClient funcMgmtClient;

	/**
	 * 根據系統別取得所有功能.
	 *
	 * @return 回傳該系統的所有功能.
	 */
	@RequestLog
	public List<FunctionItemVo> getAllFuncBySysId(String sysId) {
		List<FunctionItemVo> funcItemList = null;
		try {
			funcItemList = functionItemDao.getAllFuncBySysId(sysId);
		} catch (Exception e) {
			funcItemList = functionItemDao.getAllFuncBySysId(sysId);
			List<FunctionDivVo> funcDivList = functionDivDao.findAll();
			for (FunctionItemVo funcItemVo: funcItemList) {
				// 若位功能類型，找該功能下的div權限設定
				if ("F".equals(funcItemVo.getFunctionType())) {
					// 從funDivList 資料設定 div 區塊
					List<String> divNames = new ArrayList<>();
					for(FunctionDivVo funcDivVo : funcDivList) {
						if (funcDivVo.getFunctionId().toString().equals(funcItemVo.getFunctionId())) {
							divNames.add(funcDivVo.getDivName());
						}
					}
					funcItemVo.setDivArr(StringUtils.join(divNames, ','));
				}
			}
		}

		return funcItemList;
	}

	/**
	 * 判斷功能名稱是否存在.
	 *
	 * @param functionVo FunctionItemVo
	 * @return 回傳功能名稱是否存在
	 */
	@RequestLog
	public boolean isFunctionNameExist(FunctionItemVo functionVo) {
		boolean funNameExist = false;
		String sysId = functionVo.getSysId();
		String parentFuncId = functionVo.getParentFuncId();
		String functionName = functionVo.getFunctionName();
		List<FunctionItemVo> dataList = functionItemDao.getAllFuncBySysId(sysId);
		for (FunctionItemVo func: dataList) {
			if (parentFuncId != null && functionName != null) {
				if (parentFuncId.equals(func.getParentFuncId()) && functionName.equals(func.getFunctionName())) {
					funNameExist = true;
					break;
				}
			}
		}
		return funNameExist;
	}

	/**
	 * 新增功能節點.
	 *
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Transactional
	public int addSystem(SystemsVo systemsVo) {
		int result = functionItemDao.addSystem(systemsVo);
		if (result > 0) {
			// 接著自動新增functionItem的系統跟目錄
			FunctionItemVo functionVo = new FunctionItemVo();
			functionVo.setSysId(systemsVo.getSysId());
			functionVo.setFunctionName(systemsVo.getSysName());
			functionVo.setFunctionType("FG");
			functionVo.setCreateUser(systemsVo.getCreateUser());
			functionVo.setParentFuncId(systemsVo.getSysId());
			functionVo.setActive("Y");
			result += functionItemDao.addFunctionItem(functionVo);
		}
		return result;
	}

	/**
	 * 新增功能節點.
	 *
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Transactional
	public int addFunctionItem(FunctionItemVo functionVo) {

		if(functionVo!=null) {
			if(functionVo.getSort()==null || "".equals(functionVo.getSort().trim())) {
				/**
				 * fixed sqlserver.jdbc.SQLServerException
				 * 將資料類型從nvarchar轉換到numberic時發生錯誤
				 */
				functionVo.setSort("0");//fixed sqlserver.jdbc.SQLServerException
			}
		}

		int result = functionItemDao.addFunctionItem(functionVo);
		if (result > 0) {
			if (functionVo != null && functionVo.getFunctionId() != null) {
				functionDivDao.deleteByFunId(functionVo.getFunctionId());
			}

			if (functionVo != null) {
				String[] divArr = functionVo.getDivArr().split(",");
				for (String divName : divArr) {
					if (MyStringUtil.isNotNullOrEmpty(divName)) {
						FunctionDivVo functionDivVo = new FunctionDivVo();
						functionDivVo.setFunctionId(new BigDecimal(functionVo.getFunctionId()));
						functionDivVo.setDivName(divName);
						functionDivVo.setCreateUser(functionVo.getCreateUser());
						functionDivDao.insertFunctionDiv(functionDivVo);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 更新功能節點.
	 *
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	@Transactional
	public int updateFunctionItem(FunctionItemVo functionVo) {

		if(functionVo!=null) {
			if(functionVo.getSort()==null || "".equals(functionVo.getSort().trim())) {
				/**
				 * fixed sqlserver.jdbc.SQLServerException
				 * 將資料類型從nvarchar轉換到numberic時發生錯誤
				 */
				functionVo.setSort("0");//fixed sqlserver.jdbc.SQLServerException
			}
		}

		int result = functionItemDao.updateFunctionItem(functionVo);
		if (result > 0) {
			if (functionVo != null && functionVo.getFunctionId() != null) {
				functionDivDao.deleteByFunId(functionVo.getFunctionId());
			}
			if (functionVo != null) {
				String[] divArr = functionVo.getDivArr().split(",");
				for (String divName : divArr) {
					if (MyStringUtil.isNotNullOrEmpty(divName)) {
						FunctionDivVo functionDivVo = new FunctionDivVo();
						functionDivVo.setFunctionId(new BigDecimal(functionVo.getFunctionId()));
						functionDivVo.setDivName(divName);
						functionDivVo.setCreateUser(functionVo.getCreateUser());
						functionDivDao.insertFunctionDiv(functionDivVo);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 刪除功能節點.
	 *
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	@RequestLog
	public int deleteFunctionItem(FunctionItemVo functionVo) {
		int result = 0;
		if ("F".equals(functionVo.getFunctionType())) {
			// 刪除功能
			result = functionItemDao.deleteFunctionItem(functionVo.getFunctionId());
			functionDivDao.deleteByFunId(functionVo.getFunctionId());
		} else {
			// 刪除分類
			result = functionItemDao.deleteFunctionItem(functionVo.getFunctionId());
		}
		return result;
	}
}
