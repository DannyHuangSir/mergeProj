package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransFundNotificationVo;

public interface TransFundNotificationMapper {

	public List<TransFundNotificationVo> queryTransFundNotification(@Param("transFundNotificationVo") TransFundNotificationVo transFundNotificationVo);
}
