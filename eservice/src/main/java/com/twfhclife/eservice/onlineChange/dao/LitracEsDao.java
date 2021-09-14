package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.LitracEsVo;

/**
 * 滿期生存匯款資訊 Dao.
 * 
 * @author all
 */
public interface LitracEsDao {
	
	/**
	 * 滿期生存匯款資訊-查詢.
	 * 
	 * @param litracEsVo LitracEsVo
	 * @return 回傳查詢結果
	 */
	List<LitracEsVo> getLitracEs(@Param("litracEsVo") LitracEsVo litracEsVo);
	
	/**
	 * 滿期生存匯款資訊-依照保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<LitracEsVo> getLitracEsBypolicyNo(@Param("policyNo") String policyNo);
}