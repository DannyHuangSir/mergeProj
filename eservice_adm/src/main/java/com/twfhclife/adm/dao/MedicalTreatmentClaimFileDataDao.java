package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.MedicalTreatmentClaimFileDataVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author hui.chen
 * @create 2021-09-17
 */
public interface MedicalTreatmentClaimFileDataDao {
    /**
     * 進行修改醫療的文件狀態信息
     * @param medicalTreatmentClaimFileDataVo
     * @return
     * @throws Exception
     */
  int updaetMedicalTreatmentClaimFileDataFileStatusCases(@Param("vo") MedicalTreatmentClaimFileDataVo medicalTreatmentClaimFileDataVo)throws Exception;
}
