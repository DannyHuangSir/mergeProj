package com.twfhclife.eservice.onlineChange.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.onlineChange.model.ElectronicFormVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_model.TransCtcSelectDetailVo;

public interface ITransElectronicFormService {
	/**
	 * 新增紀錄
	 * @param transElectronicFormVo
	 * @return int
	 * @throws Exception
	 */
	public int insertElectronicForm(String transNum,ElectronicFormVo electronicFormVo , String userId,String policyNo ,UsersVo userVo) throws Exception;
	
	public Map<String,Object> getSendMailInfo();
	
	public List<ElectronicFormVo> getPolicyDetail(String lipmId,String userId , String type);
	
	public HashMap getUserCurrentNetworkData(String policyNo);
}
