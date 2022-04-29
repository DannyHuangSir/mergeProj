package com.twfhclife.alliance.dao;

import com.twfhclife.alliance.model.MedicalTreatmentClaimApplyDataVo;
import com.twfhclife.alliance.model.MedicalTreatmentClaimFileDataVo;
import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimMedicalInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import org.apache.ibatis.annotations.Param;
import org.jgroups.protocols.EXAMPLE;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-20
 * 醫療
 */
public interface IMedicalDao {
    List<MedicalTreatmentClaimVo> getMedicalTreatmentByNoCaseId()throws Exception;
    
    /**
     * 以caseId查詢MEDICAL_TREATMENT_CLAIM
     * @param caseId
     * @return MedicalTreatmentClaimVo
     * @throws Exception
     */
    MedicalTreatmentClaimVo getMedicalTreatmentByCaseId(@Param("caseId")String caseId) throws Exception;

    int  updateMedicalTreatmentClaimToAllianceAndCaseId(@Param("vo") MedicalTreatmentClaimVo vo)throws  Exception;

    int  updateTransMedicalTreatmentClaimToAllianceAndCaseId(@Param("vo")MedicalTreatmentClaimVo vo)throws  Exception;

    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByCaseIdAndAllinaceStatus(@Param("itpsPthg")String itpsPthg,@Param("itpsEnd") String itpsEnd)throws  Exception;

    int updateTarnsMedicalTreatmentClaimToAllianceFileStatus(@Param("vo")MedicalTreatmentClaimVo vo)throws  Exception;

    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByAllinaceStatusIsItpsEnd(@Param("itpsEnd") String itpsEnd)throws Exception;
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentByAllinaceStatusIsPqhfEnd(@Param("pqhfEnd") String pqhfEnd)throws Exception;

    int updateTarnsMedicalTreatmentClaimToAllianceStatusAndMsg(@Param("vo") MedicalTreatmentClaimVo vo)throws Exception;
    //獲取需要進行下載的文件的File
    List<MedicalTreatmentClaimFileDataVo> getTransMedicalTreatmentFileDataByClaimSeqId(@Param("claimSeqId")Float claimSeqId, @Param("parameterByCategoryCode")List<String> parameterByCategoryCode)throws Exception;

    //修改下載的文件數據
    int updateTarnsMedicalTreatmentFileDataStatus(@Param("vo")  MedicalTreatmentClaimFileDataVo fileDataVo)throws Exception;

    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentAgainUploadFileid(@Param("pqhfEnd") String pqhfEnd)throws Exception;
    //獲取到聯盟的數據信息
    List<MedicalTreatmentClaimVo> getMedicalTreatmentClaimVoBySeqIdNoTransNum()throws Exception;
    //獲取對應的文件數據-聯盟
    List<MedicalTreatmentClaimFileDataVo> getMedicalTreatmentFileDataByClaimSeqId(@Param("claimSeqId")  Float claimSeqId)throws Exception;
   // 更新該筆Medical_Claim,表示已送到eservice.TRANS
    int updateMedicalTreatmentClaimToStatusByTransNum(@Param("transNum") String transNum,@Param("saveTrans") String saveTrans,@Param("caseId") String caseId)throws Exception;
    //進行查詢出transNum
    String getTransMedicalTreatmentByCaseId(@Param("caseId")String caseId,@Param("listStatus")List listStatus)throws Exception;
    //進行查詢案件的數據
    TransMedicalTreatmentClaimVo getTransMedicalTreatmentByTransNum(@Param("transNum")String transNum)throws Exception;
    //獲取到Medical的SequenceID
    Float getMedicalTreatmentSequence()throws Exception;
    //添加數據新案件資料
    int addMedicalTreatment(@Param("medicalVo")MedicalTreatmentClaimVo medicalVo)throws Exception;
    //添加文件
    int addMedicalTreatmentFileData(@Param("fileDataVo")MedicalTreatmentClaimFileDataVo fileDataVo)throws Exception;
    
    //更新當前已有保單的狀態
    int updateTransMedicalTreatmentByCaseId(@Param("caseId")String caseId, @Param("allianceStatus")String allianceStatus) throws Exception;
    
    //查詢當前的數據是否有
    int getTransMedicalTreatmentFiledatasByFileId(@Param("fileId")String fileId)throws Exception;
    //進行修改文件的狀態
    int updateTarnsMedicalTreatmentFileStatus(@Param("fileId")String fileId, @Param("fileStatus")String fileStatus)throws Exception;
    //進行添加新的文件數據
    int addTarnsMedicalTreatmentFile(@Param("fileData")MedicalTreatmentClaimFileDataVo fileData)throws  Exception;
    //進行查詢出對應的ClaimsSeqId
    float getTransMedicalTreatmentClaimsSeqIdByCaseId(@Param("caseID")String caseID)throws Exception;
    //查詢狀態
    List<MedicalTreatmentClaimVo> getTransMedicalTreatmentAndTransByAllinaceStatus(@Param("lists")ArrayList<String> lists)throws Exception;

    /**
     *   //進行查詢出對於已開啓傳送公會聯盟鏈并且覆核人員審核的案件數據
     * @param code
     * @param code1
     * @return
     * @throws Exception
     */
    List<TransMedicalTreatmentClaimVo> getTransMedicalTreatmentBySendAlliance(@Param("code")String code)throws Exception;

    /**
     * 添加新的案件信息,推送給聯盟的數據
     * @param voTemp
     * @return
     * @throws Exception
     */
    int  addTransMedicalToMedicalTreatment(@Param("vo")TransMedicalTreatmentClaimVo voTemp)throws Exception;

    /**
     * 回寫是否推送到聯盟專場表中
     * @param voTemp
     * @return
     * @throws Exception
     */
    int updetaTransMedicalTreatmentClaimBySendAlliancePush(@Param("vo")TransMedicalTreatmentClaimVo voTemp,@Param("code")String  code)throws Exception;

    String getNotifyOfNewCaseMedicalIsCaseId(@Param("caseID")String caseId)throws Exception;

    int updateNotifyOfNewCaseMedicalIsCaseId(@Param("caseId")String caseID,@Param("ncStatus")String ncStatus,@Param("msg")String msg)throws Exception;
    //修改當前保單為重新獲取醫院端已接收到此次查調資
    //訊，待醫院端回覆資料
    int updateTarnsMedicalTreatmentClaimToAllianceStatus(@Param("transNum")String transNum, @Param("pths")String medicalInterfaceStatusPths)throws Exception;;
   // 查詢需要進行下載的檔案數據數據信息
    List<MedicalTreatmentClaimFileDataVo> getTransMedicalTreatmentClaimFileData(@Param("hasFile")String has_file);
    //儅舊案件的案件狀態為IPTS與HTPS_PTIS進行清除重新上傳描述
    int updateTransMedicalTreatmentClaimByReUpload(@Param("caseId")String caseId)throws Exception;

    List<TransMedicalTreatmentClaimMedicalInfoVo> getTransMedicalInfoByClaimId(@Param("claimSeqId") Float claimSeqId);

    int addMedicalTreatmentApplyData(@Param("vo")TransMedicalTreatmentClaimMedicalInfoVo infoVoTemp)throws Exception;

    List<MedicalTreatmentClaimApplyDataVo> getMedicalTreatmentClaimApplyData(@Param("claimSeqId") Float claimSeqId);

    List<MedicalTreatmentClaimApplyDataVo> getMedicalTreatmentClaimApplyDataByHasFile(@Param("hasFile")String has_file);

    int updateMedicalTreatmentClaimApplyDataForFileStatus(@Param("vo")MedicalTreatmentClaimApplyDataVo vo) throws Exception;

    int updateTransMedicalInfoForFileStatus(@Param("vo")MedicalTreatmentClaimApplyDataVo vo) throws Exception;

    int addMedicalTreatmentApplyDatas(@Param("vo")MedicalTreatmentClaimApplyDataVo applyData)throws Exception;

    int updateMedicalTreatmentApplyData(@Param("vo")MedicalTreatmentClaimApplyDataVo applyData)throws Exception;

    int updateTransMedicalTreatmentMedicalInfo(@Param("vo")MedicalTreatmentClaimApplyDataVo applyData)throws Exception;

    List<MedicalTreatmentClaimApplyDataVo> getMedicalTreatmentClaimApplyDataByInfo(@Param("seNo") String seNo, @Param("otype") String otype, @Param("depId") String depId, @Param("dtype") String dtype);
}
