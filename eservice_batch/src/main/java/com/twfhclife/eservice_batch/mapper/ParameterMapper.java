package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.ParameterVo;

public interface ParameterMapper {

	List<ParameterVo> getParameterByCategoryCode(@Param("systemId") String systemId,
			@Param("categoryCode") String categoryCode);
	
	ParameterVo getParameterValueByCode(@Param("systemId") String systemId,
			@Param("parameterCode") String parameterCode);

	String getStatusName(@Param("parentCategoryCode") String var1, @Param("status") String var2);

}
