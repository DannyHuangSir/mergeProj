package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransContractRevocationVo;
import com.twfhclife.eservice.web.model.HospitalVo;

public interface TransContractRevocationDao {
	int insertTransContractRevocation(@Param("transContractRevocationVo") TransContractRevocationVo transContractRevocationVo);
	
    List<TransContractRevocationVo> getTransContractRevocation(TransContractRevocationVo qryVo);
    
    int updateAginInveAreaforId (TransContractRevocationVo transContractRevocationVo) throws Exception;

}
