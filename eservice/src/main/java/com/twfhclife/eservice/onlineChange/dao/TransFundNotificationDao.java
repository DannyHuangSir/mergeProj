package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.onlineChange.model.TransFundNotificationVo;
import com.twfhclife.eservice.policy.model.NotificationFundVo;
import com.twfhclife.eservice.policy.model.NotificationPortfolioVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 停損停利通知主檔 Dao.
 * 
 * @author all
 */
public interface TransFundNotificationDao {
	
	/**
	 * 停損停利通知主檔-查詢.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳查詢結果
	 */
	List<TransFundNotificationVo> getTransFundNotification(@Param("transFundNotificationVo") TransFundNotificationVo transFundNotificationVo);
	
	/**
	 * 停損停利通知主檔-新增.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳影響筆數
	 */
	int insertTransFundNotification(@Param("transFundNotificationVo") TransFundNotificationVo transFundNotificationVo);
	
	/**
	 * 取得下一筆序號.
	 * 
	 * @return 回傳序號
	 */
	BigDecimal getNextTransFundNotificationId();


	List<NotificationFundVo> getSearchFunds(@Param("riskLevel") String riskLevel, @Param("invtNos") List<String> invtNos);

	List<NotificationPortfolioVo> getNotificationPortfolioList(String policyNo);
}