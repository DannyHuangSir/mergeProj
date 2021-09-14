package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.PartnerUserEntityVo;

/**
 * 外部人員管理 Dao.
 * 
 * @author all
 */
public interface PartnerUserEntityDao {
	
	/**
	 * 外部人員管理-分頁查詢.
	 * 
	 * @param partnerUserEntityVo PartnerUserEntityVo
	 * @return 回傳人員管理-分頁查詢結果
	 */
	List<Map<String, Object>> getPartnerUserEntityPageList(@Param("partnerUserEntityVo") PartnerUserEntityVo partnerUserEntityVo);

	/**
	 * 外部人員管理-查詢結果總筆數.
	 * 
	 * @param partnerUserEntityVo PartnerUserEntityVo
	 * @return 回傳總筆數
	 */
	int getPartnerUserEntityPageTotal(@Param("partnerUserEntityVo") PartnerUserEntityVo partnerUserEntityVo);
	
	/**
	 * 外部人員-取得名稱
	 * 
	 * @param partnerUserEntityVo PartnerUserEntityVo
	 * @return 回傳名稱
	 */
	String getNextPartnerUserName(@Param("partnerUserEntityVo") PartnerUserEntityVo partnerUserEntityVo);
	
	/**
	 * 外部人員-檢查名稱是否存在.
	 * 
	 * @param partnerUserEntityVo PartnerUserEntityVo
	 * @return 回傳筆數
	 */
	int checkUserNameExist(@Param("partnerUserEntityVo") PartnerUserEntityVo partnerUserEntityVo);
}