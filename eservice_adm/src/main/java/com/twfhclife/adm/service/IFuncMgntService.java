package com.twfhclife.adm.service;

import java.util.List;

import com.twfhclife.adm.model.FunctionItemVo;
import com.twfhclife.adm.model.SystemsVo;

/**
 * 權限管理-功能維護服務.
 * 
 * @author all
 */
public interface IFuncMgntService {
	
	/**
	 * 根據系統別取得所有功能.
	 * 
	 * @param sysId 系統id
	 * @return 回傳該系統的所有功能.
	 */	
	public List<FunctionItemVo> getAllFuncBySysId(String sysId);
	
	/**
	 * 判斷功能名稱是否存在.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳功能名稱是否存在
	 */
	public boolean isFunctionNameExist(FunctionItemVo functionVo);

	/**
	 * 新增系統
	 * @param systemsVo
	 * @return
	 */
	public int addSystem(SystemsVo systemsVo);
	
	/**
	 * 新增功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	public int addFunctionItem(FunctionItemVo functionVo);

	/**
	 * 更新功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	public int updateFunctionItem(FunctionItemVo functionVo);

	/**
	 * 刪除功能節點.
	 * 
	 * @param functionVo FunctionItemVo
	 * @return 回傳影響筆數
	 */
	public int deleteFunctionItem(FunctionItemVo functionVo);
	
}
