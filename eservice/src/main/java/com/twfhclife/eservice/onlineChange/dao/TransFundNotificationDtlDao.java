package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;

/**
 * 停損停利通知明細 Dao.
 * 
 * @author all
 */
public interface TransFundNotificationDtlDao {
	
	/**
	 * 停損停利通知明細-查詢.
	 * 
	 * @param transFundNotificationDtlVo TransFundNotificationDtlVo
	 * @return 回傳查詢結果
	 */
	List<TransFundNotificationDtlVo> getTransFundNotificationDtl(@Param("transFundNotificationDtlVo") TransFundNotificationDtlVo transFundNotificationDtlVo);
	
	/**
	 * 停損停利通知明細-新增.
	 * 
	 * @param transFundNotificationDtlVo TransFundNotificationDtlVo
	 * @return 回傳影響筆數
	 */
	int insertTransFundNotificationDtl(@Param("transFundNotificationDtlVo") TransFundNotificationDtlVo transFundNotificationDtlVo);
}