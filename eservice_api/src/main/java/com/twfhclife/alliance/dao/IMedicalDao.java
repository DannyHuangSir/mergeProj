package com.twfhclife.alliance.dao;

import com.twfhclife.alliance.model.MedicalTreatmentClaimFileDataVo;
import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import org.apache.ibatis.annotations.Param;
import org.jgroups.protocols.EXAMPLE;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-20
 * 醫療
 */
public interface IMedicalDao {
    List<MedicalTreatmentClaimVo> getMedicalTreatmentByNoCaseId()throws Exception;;

    int  updateMedicalTreatmentClaimToAllianceAndCaseId(@Param("vo") MedicalTreatmentClaimVo vo)throws  Exception;

    int  updateTransMedicalTreatmentClaimToAllianceAndCaseId(@Param("vo")MedicalTreatmentClaimVo vo);

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
    int updateMedicalTreatmentClaimToStatusByTransNum(@Param("transNum") String transNum,@Param("saveTrans")  String saveTrans)throws Exception;
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
    int updateTransMedicalTreatmentByTransNum(@Param("transNum")String transNum, @Param("allianceStatus")String allianceStatus)throws  Exception;
    //查詢當前的數據是否有
    int getTransMedicalTreatmentFiledatasByFileId(@Param("fileId")String fileId)throws Exception;
    //進行修改文件的狀態
    int updateTarnsMedicalTreatmentFileStatus(@Param("fileId")String fileId, @Param("fileStatus")String fileStatus)throws Exception;
    //進行添加新的文件數據
    int addTarnsMedicalTreatmentFile(@Param("fileData")MedicalTreatmentClaimFileDataVo fileData)throws  Exception;
    //進行查詢出對應的ClaimsSeqId
    float getTransMedicalTreatmentByClaimsSeqId(@Param("transNum")String transNum)throws Exception;
}
