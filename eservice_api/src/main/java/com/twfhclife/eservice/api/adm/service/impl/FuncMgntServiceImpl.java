package com.twfhclife.eservice.api.adm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.eservice.api.adm.model.FunctionDivVo;
import com.twfhclife.eservice.api.adm.model.FunctionItemVo;
import com.twfhclife.eservice.api.adm.service.IFuncMgntService;
import com.twfhclife.generic.dao.adm.FunctionDivDao;
import com.twfhclife.generic.dao.adm.FunctionItemDao;
import com.twfhclife.generic.utils.MyStringUtil;

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
	
	/**
	 * 根據系統別取得所有功能.
	 * 
	 * @return 回傳該系統的所有功能.
	 */
	public List<FunctionItemVo> getAllFuncBySysId(String sysId) {
		List<FunctionItemVo> funcItemList = functionItemDao.getAllFuncBySysId(sysId);
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
		
		return funcItemList;
	}

	/**
	 * 判斷功能名稱是否存在.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳功能名稱是否存在
	 */
	public boolean isFunctionNameExist(FunctionItemVo functionVo) {
		boolean funNameExist = false;
		String sysId = functionVo.getSysId();
		String parentFuncId = functionVo.getParentFuncId();
		String functionName = functionVo.getFunctionName();
		List<FunctionItemVo> dataList = functionItemDao.getAllFuncBySysId(sysId);
		for (FunctionItemVo func: dataList) {
			if (parentFuncId != null && functionName != null) {
				if (parentFuncId.equals(func.getParentFuncId()) && functionName.equals(func.getFunctionName())) {
					if(functionVo.getFunctionId() != null && functionVo.getFunctionId().equals(func.getFunctionId())) {
						// 傳入了相同的functionId，代表為更新節點行為
						funNameExist = false;
					} else {
						// 在同一個父節點底下有相同的功能名稱
						funNameExist = true;
					}
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
			if (functionVo.getFunctionId() != null) {
				functionDivDao.deleteByFunId(functionVo.getFunctionId());
			}
			//無法在新增function時同時新增div(因為沒有functionId)
//			if(functionVo.getDivArr() != null) {
//				String[] divArr = functionVo.getDivArr().split(",");
//				for (String divName : divArr) {
//					if (MyStringUtil.isNotNullOrEmpty(divName)) {
//						FunctionDivVo functionDivVo = new FunctionDivVo();
//						functionDivVo.setFunctionId(new BigDecimal(functionVo.getFunctionId()));
//						functionDivVo.setDivName(divName);
//						functionDivVo.setCreateUser(functionVo.getCreateUser());
//						functionDivDao.insertFunctionDiv(functionDivVo);
//					}
//				}
//			}
		}
		return result;
	}

	/**
	 * 更新功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
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
			functionDivDao.deleteByFunId(functionVo.getFunctionId());
			if(functionVo.getDivArr() != null) {
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
