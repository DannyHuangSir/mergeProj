package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransElectronicFormVo;

public interface TransElectronicFormMapper {
	
	public List<TransElectronicFormVo> getTransElectronicFormList(@Param("transElectronicFormVo") TransElectronicFormVo transElectronicFormVo);

}
