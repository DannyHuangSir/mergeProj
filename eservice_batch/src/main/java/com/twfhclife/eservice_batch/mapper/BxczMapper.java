package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.SignFileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BxczMapper {
    List<SignFileVo> getInsuranceClaimSignFile();
    List<SignFileVo> getMedicalTreatmentSignFile();

    int updateEzAcquireTaskId(@Param("vo") SignFileVo vo);
}
