package com.twfhclife.adm.service;

import com.twfhclife.adm.model.MedicalTreatmentClaimFileDataVo;
import org.springframework.stereotype.Service;

/**
 * @author hui.chen
 * @create 2021-09-17
 * 醫療文件
 */

public interface IMedicalTreatmentClaimFileDataService {
    /**
     *修改文件的狀態信息
     * @param medicalTreatmentClaimFileDataVo
     * @return
     */
    int updaetMedicalTreatmentClaimFileDataFileStatusCases(MedicalTreatmentClaimFileDataVo medicalTreatmentClaimFileDataVo)throws Exception ;
}
