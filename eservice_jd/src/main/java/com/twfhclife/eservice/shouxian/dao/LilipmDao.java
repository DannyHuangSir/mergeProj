package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.LilipmVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 要保人 Dao.
 * 
 * @author all
 */
public interface LilipmDao {
	
	/**
	 * 要保人-查詢.
	 * 
	 * @param lilipmVo LilipmVo
	 * @return 回傳查詢結果
	 */
	List<LilipmVo> getLilipm(@Param("lilipmVo") LilipmVo lilipmVo);
	
	/**
	 * 要保人-根據證號查詢.
	 * 
	 * @param rocId 證號
	 * @return 回傳要保人
	 */
	LilipmVo findByRocId(@Param("rocId") String rocId);
	
	/**
	 * 要保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳要保人
	 */
	LilipmVo findByPolicyNo(@Param("policyNo") String policyNo);
	
	/**
	 * 查詢要保人聯絡資訊-根據保單號碼清單查詢最近的.
	 * 
	 * @param policyNos 保單號碼清單
	 * @return 回傳要保人
	 */
	LilipmVo findContactInfoByPolicyNoList(@Param("policyNos") List<String> policyNos);
	
	/**
	 * 取得使用者的所有保單號碼清單.
	 * 
	 * @param rocId 使用者證號
	 * @return 回傳使用者的所有保單號碼
	 */
	List<String> getUserPolicyNos(@Param("rocId") String rocId);
	
	/**
	 * 取得保單的 LIPM_TRED_TMS 資訊
	 * 用作繳別由短變長判斷依據
	 * 
	 * @param policyNo
	 * @return
	 */
	Integer getLipmTredTmsByPolicyNo(@Param("policyNo") String policyNo);
	
	/**
	 * 要保人-查詢有效保單.
	 * 
	 * @param lilipmVo LilipmVo
	 * @return 回傳查詢結果
	 */
	List<LilipmVo> getAliveLilipm(@Param("lilipmVo") LilipmVo lilipmVo);
	
	/**
	 * 判斷是否保戶
	 * @param rocId
	 * @return 回傳數據條數
	 */
	public int getInsuredUsersByRocId(@Param("rocId") String rocId);

	/**
	 * 取得保單的 LIPM_TARGET_PREM 資訊
	 * 用作繳別由短變長判斷依據
	 * @param policyNo
	 * @return
	 */
	BigDecimal getLipmTargetPremByPolicyNo(@Param("policyNo") String policyNo);
	
}