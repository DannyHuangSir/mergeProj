package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransChangePremiumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransChangePremiumMapper {

    List<TransChangePremiumVo> getTransChangePremiums(@Param("transNum") String transNum);

}
