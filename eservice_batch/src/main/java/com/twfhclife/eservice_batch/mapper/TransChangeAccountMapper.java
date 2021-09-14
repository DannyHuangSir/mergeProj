package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransChangeAccountVo;

public interface TransChangeAccountMapper {
	
	public TransChangeAccountVo findById(@Param("transChangeAccountVo") TransChangeAccountVo transChangeAccountVo);
	
	public List<TransChangeAccountVo> getTransChangeAccountList(@Param("transChangeAccountVo") TransChangeAccountVo transChangeAccountVo);
	
	public String findRocId(@Param("changeType") String changeType , @Param("policyNo") String policyNo );
}