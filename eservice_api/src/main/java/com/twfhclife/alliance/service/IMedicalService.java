package com.twfhclife.alliance.service;

import com.twfhclife.alliance.model.MedicalTreatmentClaimFileDataVo;
import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseMedicalVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import org.jgroups.protocols.EXAMPLE;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-20
 * 醫療
 */
public interface IMedicalService {

    /**
     * 取得醫療申請上傳資料
     * @return
     */
    List<MedicalTreatmentClaimVo> getMedicalTreatmentByNoCaseId() throws Exception;
    
    /**
     * 以caseId查詢MEDICAL_TREATMENT_CLAIM
     * @return MedicalTreatmentClaimVo
     * @throws Exception
     */
    MedicalTreatmentClaimVo getMedicalTreatmentByCaseId(String caseId) throws Exception;
    
    //進行會寫CaseID與聯盟狀態
    int updateMedicalTreatmentClaimToAlliance(MedicalTreatmentClaimVo vo) throws Exception;

    //獲取需要進行取/不取醫院資料數據
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByCaseIdAndAllinaceStatus(String itpsPthg,String  itpsEnd) throws Exception;
    
    //進行回應狀態醫院資料信息描述
    int updateTarnsMedicalTreatmentClaimToAllianceFileStatus(MedicalTreatmentClaimVo vo) throws  Exception;
    
    //進行獲取流程結束和不進行取用資料的數據
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByAllinaceStatus(String pqhfEnd, String itpsEnd) throws Exception;
    
    //進行回壓聯盟結束流程狀態
    int updateTarnsMedicalTreatmentClaimToAllianceStatus(MedicalTreatmentClaimVo vo) throws Exception;
    
    //查詢可以獲取下載的文件數據
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentUploadFileid(String pqhfEnd) throws Exception;
    
    //進行回應狀態
    int updateTarnsMedicalTreatmentFileDataStatus(MedicalTreatmentClaimFileDataVo fileDataVo) throws Exception;
    
    //查詢重新上傳的數據信息
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentAgainUploadFileid(String pqhfEnd) throws Exception;
    
    //獲取到聯盟的數據信息
    List<MedicalTreatmentClaimVo> getMedicalTreatmentClaimVoBySeqIdNoTransNum() throws Exception;
    
    //進行添加到的TRANS
    int addTransRequest(TransMedicalTreatmentClaimVo vo) throws Exception;
    
    /**
     * 以caseId更新TRANS_NUM,STATUS
     * 進行修改狀態,表示已經推送過了聯盟-台銀
     * @param vo
     * @return
     * @throws Exception
     */
    int updateMedicalTreatmentClaimToStatusTransNumByCaseId(MedicalTreatmentClaimVo vo) throws Exception;
    
    //查詢案件的CaseId  未取得查詢理賠資料
    List<NotifyOfNewCaseMedicalVo> getNotifyOfNewCaseMedicalByNcStatus(String statusDefault) throws  Exception;
    
    //進行查詢出transNum
    String getTransMedicalTreatmentByCaseId(String caseId)throws Exception;
    
    //回應對於的狀態信息
    int  updateNotifyOfNewCaseMedicalNcStatusBySeqId(NotifyOfNewCaseMedicalVo vo) throws Exception;
   
    //通過transNum進行查詢出對於的數據
    TransMedicalTreatmentClaimVo getTransMedicalTreatmentByTransNum(String transNum) throws Exception;
    
    //添加新的數據
    int addMedicalRequest(MedicalTreatmentClaimVo medicalVo)throws  Exception;
    
    //進行更新數據信息
    int updateTransMedicalTreatmentByCaseId(MedicalTreatmentClaimVo claimVo) throws Exception;
    
    //查詢是否需要進行推送給聯盟的數據
    int getTransMedicalTreatmentBySendAlliance() throws Exception;

}
