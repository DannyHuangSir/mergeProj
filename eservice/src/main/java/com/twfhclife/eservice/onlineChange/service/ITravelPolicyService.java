package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TableGetVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.model.TravelPlanVo;
import com.twfhclife.eservice.onlineChange.model.TravelPolicyVo;

public interface ITravelPolicyService {

	/**
	 * 保費計算
	 * @param userPlan (含 保費 傷害險金額 海外險金額)
	 * @param age 歲數
	 * @param days 旅行天數
	 * @return
	 */
	public TravelPlanVo getPremiumCount(TravelPlanVo userPlan, int age, int days);
	
	/**
	 * 旅平險申請
	 * @param vo
	 * @return
	 */
	public String addTravelPolicyTrans(TransVo vo);
	
	public TableGetVo getTransDetail(String transType, String transNum);

	/**
	 * 建立三種旅平險方案
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TravelPlanVo> createPlan(TravelPolicyVo vo) throws Exception;
	
	/**
	 * 取得旅平險要保書 byte[].
	 * 
	 * @param travelPolicyVo TravelPolicyVo
	 * @return 回傳旅平險要保書 byte[]
	 */
	public byte[] getPDF(TravelPolicyVo travelPolicyVo);
	
}
