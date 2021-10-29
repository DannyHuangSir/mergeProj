package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransRiskLevelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransRiskLevelMapper {

    List<TransRiskLevelVo> getTransLevel(@Param("transNum") String transNum);

    TransRiskLevelVo getEffectUserTransRiskLevel(String transNum);

    void updateIndividual(TransRiskLevelVo vo);

    String getTop1PolicyNo(@Param("rocId") String rocId);
}
