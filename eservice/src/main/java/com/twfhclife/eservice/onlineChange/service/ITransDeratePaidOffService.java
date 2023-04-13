package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.TransDeratePaidOffVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataAddCodeVo;

public interface ITransDeratePaidOffService {

	/**
	 * 主約/附約 規則檢核
	 * @param vo
	 * @return
	 */
	public TransDeratePaidOffVo getLilipmDetail(TransCtcLilipmResponse response);
	
	public int insertDeratePaidOff( String transNum ,TransDeratePaidOffVo transDeratePaidOffVo ,UsersVo userVo);
	
	public List<TransCtcSelectDataAddCodeVo> getRuleResult(List<TransCtcSelectDataAddCodeVo> policyList , String userRocId);
	
	public TransDeratePaidOffVo getTransDeratePaidOffDetail(String transNum);
}
