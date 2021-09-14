package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.UserDeputyVo;

/**
 * 人員代理人管理服務
 * @author Ken Wei
 *
 */
public interface IUserDeputyService {

	/**
	 * 人員代理人管理-查詢
	 * @param userDeputyVo
	 * @return 回傳查詢結果
	 */
	public List<UserDeputyVo> getUserDeputy(UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-新增
	 * @param userDeputyVo
	 * @return 回傳影響筆數
	 */
	public int insertUserDeputy(UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-查詢結果總筆數
	 * @param userDeputyVo
	 * @return 回傳總筆數
	 */
	public int getUserDeputyPageTotal(UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-分頁查詢
	 * @param userDeputyVo
	 * @return 回傳分頁查詢結果
	 */
	public List<Map<String, Object>> getUserDeputyPageList(UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-可使用代理人查詢
	 * @param userDeputyVo
	 * @param admRealm
	 * @return 回傳選擇角色可以設定的代理人
	 */
	public List<Map<String, Object>> getCanDeputyUser(UserDeputyVo userDeputyVo);
	
	/**
	 * 人員代理人管理-代理人刪除
	 * @param id
	 * @return 回傳刪除筆數
	 */
	public int deleteDeputyById(Integer id);
}
