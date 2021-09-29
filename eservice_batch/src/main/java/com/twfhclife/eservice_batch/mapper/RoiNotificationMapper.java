package com.twfhclife.eservice_batch.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice_batch.model.MyPortfolioVo;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.InvestmentVo;
import com.twfhclife.eservice_batch.model.RoiNotificationVo;

public interface RoiNotificationMapper {

	public List<RoiNotificationVo> findEnableAll();
	
	public InvestmentVo findFundInfoByInsuNo(@Param("investmentVo")InvestmentVo investmentVo);
	
	public String findEmailByPolicyNo(@Param("policyNo") String policyNo);
	
	public Map<String, Object> findNetValueLatest(@Param("invtNo") String invtNo);
	
	public Map<String, Object> findExchRateLatest(@Param("exchCode") String exchCode);
	
	public Map<String, Object> findAccumulation(@Param("insuNo") String insuNo, @Param("invtNo") String invtNo);
	
	public int insertRoiNotificationJob(@Param("roiNotificationVo") RoiNotificationVo roiNotificationVo);
	
	public int updateAllDisable();

	InvestmentVo findFundByInvestNo(@Param("vo") InvestmentVo investmentVo);

	MyPortfolioVo findPortfolioByInvestNo(@Param("vo") InvestmentVo investmentVo);
}
