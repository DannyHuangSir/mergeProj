package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLipmdaVo;

public interface TransCtcLipmdaDao {
	
	/**
	 *
	 * @return 回傳查詢結果
	 */
	List<TransCtcLipmdaVo> getLipmda(@Param("lipmId") String lipmId);

	/**
	 * 契約撤銷 查詢 保單狀態
	 * @param lipmId
	 * @return
	 */
	List<TransCtcLipmdaVo> getRevokeDetail(@Param("lipmId") String lipmId);

}

