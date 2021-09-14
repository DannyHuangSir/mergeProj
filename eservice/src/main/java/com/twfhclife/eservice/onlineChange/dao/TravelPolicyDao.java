package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.model.TravelPlanVo;
import com.twfhclife.eservice.onlineChange.model.TravelPolicyVo;

public interface TravelPolicyDao {
	
	public List<TravelPlanVo> getPremiumCount(@Param("vo") TravelPlanVo userPlan, @Param("age") int age, @Param("days") int days);
	
	public int addTransTravelPolicy(@Param("vo") TransVo vo);
	
	public int addTransTravelPolicyHolder(@Param("vo") TransVo vo);
	
	public int addTransTravelPolicyInsured(@Param("vo") TransVo vo);
	
	public int addTransTravelPolicyBene(@Param("vo") TransVo vo);
	
	public int addTransTravelPolicyExt(@Param("vo") TransVo vo);
	
	public TravelPolicyVo getTravelPolicyDetial(@Param("transNum") String transNum);
	
}
