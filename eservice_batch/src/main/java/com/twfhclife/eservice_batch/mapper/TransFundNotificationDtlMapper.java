package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransFundNotificationDtlVo;

public interface TransFundNotificationDtlMapper {

	public List<TransFundNotificationDtlVo> queryTransFundNotificationDtl(@Param("transFundNotificationDtlVo") TransFundNotificationDtlVo transFundNotificationDtlVo);
}
