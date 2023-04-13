package com.twfhclife.eservice.onlineChange.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.twfhclife.eservice.onlineChange.model.ContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.TransCancelContractVo;
import com.twfhclife.eservice.onlineChange.model.TransContractRevocationFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.TransDeratePaidOffVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.UsersVo;

public interface ITransContractRevocationService {

	public List<ContractRevocationVo> getPolicyDetail(String lipmId,String userId , String transType);
	
	public ContractRevocationVo getPolicy(String lipmInsuSeqNo);
	
	public ContractRevocationVo getRevokeByLiprpaForInsuSeqNo(ContractRevocationVo contractRevocationVo);
	
	public ContractRevocationVo getRevokeByLineacForNeacInsuNo(ContractRevocationVo contractRevocationVo);
	
	public int insertContractRevocation( String transNum ,ContractRevocationVo contractRevocationVo ,UsersVo userVo , Map<String, ParameterVo> parameterList);
	
	public TransContractRevocationFileDataVo upLoadFiles(MultipartFile[] files);
	
	public ContractRevocationVo getTransContractRevocationDetail(String transNum);

}
