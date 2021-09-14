package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransCreditCardDateVo;

/**
 * 變更信用卡效期 Dao.
 * 
 * @author all
 */
public interface TransCreditCardDateDao {
	
	/**
	 * 變更信用卡效期-查詢.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳查詢結果
	 */
	List<TransCreditCardDateVo> getTransCreditCardDate(@Param("transCreditCardDateVo") TransCreditCardDateVo transCreditCardDateVo);
	
	/**
	 * 變更信用卡效期-新增.
	 * 
	 * @param transCreditCardDateVo TransCreditCardDateVo
	 * @return 回傳影響筆數
	 */
	int insertTransCreditCardDate(@Param("transCreditCardDateVo") TransCreditCardDateVo transCreditCardDateVo);
}