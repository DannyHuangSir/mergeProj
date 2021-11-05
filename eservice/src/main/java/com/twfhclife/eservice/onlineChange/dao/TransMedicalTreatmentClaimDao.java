package com.twfhclife.eservice.onlineChange.dao;


import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 線上申請-保單理賠醫起通申請 Dao.
 * 
 * @author all
 */
public interface TransMedicalTreatmentClaimDao {
	
	/**
	 * 取得TRANS_INSURANCE_CLAIM_SEQ的next value
	 * 
	 * @return Float
	 * @throws Exception
	 */
	Float getInsuranceClaimSequence() throws Exception;

	/**
	 * 保單理賠醫起通申請
	 * 
	 * @param params
	 */
	int addInsuranceClaim(@Param("vo") TransMedicalTreatmentClaimVo vo) throws Exception;
	
	/**
	 * 保單理賠醫起通申請
	 * 
	 * @param params
	 */
	int addInsuranceClaimFileData(@Param("vo") TransMedicalTreatmentClaimFileDataVo vo) throws Exception;

	/**
	 * 更新文件内容信息
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimFileDataFileBase64(@Param("vo") TransMedicalTreatmentClaimFileDataVo vo) throws Exception;
	/**
	 * 查询文件类型的id 编号
	 * @throws Exception
	 */
	Float selectInsuranceClaimFileDataId() throws Exception;

	/**
	 * 保單理賠醫起通申請明細
	 * 
	 * @param params
	 */
	List<TransMedicalTreatmentClaimVo> getTransInsuranceClaimByTransNum(@Param("transNum") String transNum) throws Exception;
	
	/**
	 * 保單理賠醫起通申請明細
	 * 
	 * @param params
	 */
	List<TransMedicalTreatmentClaimFileDataVo> getFileDatasByClaimSeqId(@Param("claimSeqId") Float claimSeqId) throws Exception;
	
	/**
	 * 保單類型
	 * 
	 * @param params
	 */
	String getTransTypeByTransNum(@Param("transNum") String transNum) throws Exception;
	
	/**
	 * 補件歷程ID
	 * 
	 * @param params
	 */
	Float getRefId(@Param("transNum") String transNum) throws Exception;
	
	/**
	 * 更新補件歷程狀態
	 * 
	 * @param params
	 */
	int updateSatusByRefId(@Param("rfeId") Float rfeId) throws Exception;
	
	/**
	 * 依保單號碼找出險種
	 * @param policyNo
	 * @return List<String>
	 */
	public List<String> getProductCodeByPolicyNo(String policyNo);

    List<Hospital> getHospitalList( @Param("functionName")  String functionName,@Param("status")  String status)throws Exception;

	List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(@Param("functionName") String functionName,@Param("status")  String status)throws  Exception;

    int getMedicalTreatmentWhetherFirst(@Param("policyNo")String policyNo, @Param("functionName")String medicalTreatmentParameterCode)throws Exception;
	//進行獲取當前保單已經選中的醫院資料
    List<String> gitChooseHospitalList(@Param("policyNo")String policyNo,@Param("userRocId") String userRocId)throws  Exception;
}