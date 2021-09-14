package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransCancelContractVo;

/**
 * 解約申請書套印 Dao.
 * 
 * @author all
 */
public interface TransCancelContractDao {
	
	/**
	 * 解約申請書套印-查詢.
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return 回傳查詢結果
	 */
	List<TransCancelContractVo> getTransCancelContract(@Param("transCancelContractVo") TransCancelContractVo transCancelContractVo);
	
	/**
	 * 解約申請書套印-新增.
	 * 
	 * @param transCancelContractVo TransCancelContractVo
	 * @return 回傳影響筆數
	 */
	int insertTransCancelContract(@Param("transCancelContractVo") TransCancelContractVo transCancelContractVo);
}