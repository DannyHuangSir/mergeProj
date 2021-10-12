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

    /**
     * 獲取當前的caseID
     * @param medicalTreatmentClaimFileDataVo
     * @return
     * @throws Exception
     */
    String getTreatmentClaimCaseId(MedicalTreatmentClaimFileDataVo medicalTreatmentClaimFileDataVo)throws Exception ;

    /**
     * 進行修改狀態信息
     * @param caseID
     * @return
     * @throws Exception
     */
    int updaetNotifyOfNewCaseMedicalStatus(String caseID)throws Exception ;
}
