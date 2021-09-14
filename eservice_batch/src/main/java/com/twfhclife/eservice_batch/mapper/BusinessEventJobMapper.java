package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.BusinessEventJobVo;
import com.twfhclife.eservice_batch.model.BusinessEventMsgTmpVo;

public interface BusinessEventJobMapper {
	
	public List<BusinessEventJobVo> queryBusinessEvenJobtList(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);
	
	public List<BusinessEventMsgTmpVo> queryUnTriggerData(@Param("businessEventJobVo") BusinessEventJobVo businessEventJobVo);

	public int updateNoticeStatus(@Param("status") Integer status, @Param("businessEventId") Integer businessEventId);
}
