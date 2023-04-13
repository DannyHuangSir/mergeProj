package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransRolloverPeriodicallyVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;

public interface ITransRolloverPeriodicallyService {
	/**
	 * 主約/附約 規則檢核
	 * @param vo
	 * @return
	 */
	public List<TransCtcSelectDataAddCodeVo> getRuleResult(List<TransCtcSelectDataAddCodeVo> policyList , String userRocId);
	
	public int insertRolloverPeriodically( String transNum ,TransRolloverPeriodicallyVo transRolloverPeriodicallyVo ,UsersVo userVo);
	
	public TransRolloverPeriodicallyVo getLilipmDetail(TransCtcLilipmResponse policyNo);
	
	public TransRolloverPeriodicallyVo getTransRolloverPeriodicallyDetail(String transNum);

}
