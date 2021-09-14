package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.PartnerUserEntityVo;
import com.twfhclife.adm.model.RoleDivAuthVo;
import com.twfhclife.adm.model.UsersVo;

/**
 * 外部人員管理服務.
 * 
 * @author all
 */
public interface IPartnerUserMgntService {
	
	/**
	 * 外部人員管理-分頁查詢.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	public List<Map<String, Object>> getPartnerUserEntityPageList(PartnerUserEntityVo userEntityVo);

	/**
	 * 外部人員管理-查詢結果總筆數.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳總筆數
	 */
	public int getPartnerUserEntityPageTotal(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-取得名稱
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳名稱
	 */
	public String getNextPartnerUserName(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-檢查名稱是否存在.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳筆數
	 */
	public int checkUserNameExist(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-新增.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	public int insertPartnerUser(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-刪除.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	public int deletePartnerUser(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-解鎖.
	 * 
	 * @param userEntityVo PartnerUserEntityVo
	 * @return 回傳影響筆數
	 */
	public int unlockUser(PartnerUserEntityVo userEntityVo);
	
	/**
	 * 外部人員-更新 Online Change 使用
	 * @param userVo UsersVo
	 * @return 回傳影響筆數
	 */
	public int updateOnlineChangeUse(UsersVo usersVo);
	
	/**
	 * 依UserId取得不可使用的DivId清單
	 * @param userId
	 * @return List<RoleDivAuthVo>
	 */
	public List<RoleDivAuthVo> getRoleDivAuthByUserId(String userId);
}