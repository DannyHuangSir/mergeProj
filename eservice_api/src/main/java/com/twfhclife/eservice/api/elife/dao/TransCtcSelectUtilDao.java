package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailVo;

public interface TransCtcSelectUtilDao {
	
	List<TransCtcSelectDataVo> getTransCtcSelectDataByLipmId(@Param("lipmId") String lipmId);
	
	List<TransCtcSelectDetailVo> getTransCtcSelectDetailByLipmInsuSeqNo(@Param("lipmInsuSeqNo") String lipmInsuSeqNo);
	
	List<TransCtcSelectDataAddCodeVo> getTransCtcSelectDataAddCode(@Param("lipmId") String lipmId);
}
