package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.LitracEsVo;

/**
 * 滿期生存匯款資訊服務.
 * 
 * @author all
 */
public interface ILitracEsService {
	
	/**
	 * 滿期生存匯款資訊-查詢.
	 * 
	 * @param litracEsVo LitracEsVo
	 * @return 回傳查詢結果
	 */
	List<LitracEsVo> getLitracEs(LitracEsVo litracEsVo);
	
	/**
	 * 滿期生存匯款資訊-依照保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	List<LitracEsVo> getLitracEsBypolicyNo(String policyNo);
}