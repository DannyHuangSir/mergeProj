package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransContractRevocationVo;

public interface TransContractRevocationMapper {

	public List<TransContractRevocationVo> getTransContractRevocationList(@Param("transContractRevocationVo") TransContractRevocationVo transContractRevocationVo);
	
}
