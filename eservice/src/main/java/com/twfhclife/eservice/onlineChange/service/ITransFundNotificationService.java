package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransFundNotificationDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransFundNotificationVo;
import com.twfhclife.eservice.policy.model.*;

/**
 * 停損停利通知服務.
 * 
 * @author all
 */
public interface ITransFundNotificationService {
	
	/**
	 * 停損停利通知-查詢.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳查詢結果
	 */
	public List<TransFundNotificationVo> getTransFundNotification(TransFundNotificationVo transFundNotificationVo);
	
	/**
	 * 停損停利通知-新增.
	 * 
	 * @param transFundNotificationVo TransFundNotificationVo
	 * @return 回傳影響筆數
	 */
	public int insertTransFundNotification(TransFundNotificationVo transFundNotificationVo);
	
	/**
	 * 取得停損停利通知申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public BigDecimal getTransFundNotificationId(String transNum);

	/**
	 * 停損停利通知-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @param transFundNotificationId 停損停利通知申請id
	 * @return 回傳查詢結果
	 */
	public List<TransFundNotificationDtlVo> getTransFundNotificationDtlDetail(String transNum,
			BigDecimal transFundNotificationId);


	List<NotificationFundVo> getSearchPortfolio(String policyNo, List<String> invtNos, String userRocId);

	List<NotificationPortfolioVo> getOwnNotifications(String policyNo);
}