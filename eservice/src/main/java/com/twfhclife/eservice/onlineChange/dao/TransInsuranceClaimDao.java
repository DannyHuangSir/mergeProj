package com.twfhclife.eservice.onlineChange.dao;

import java.util.Date;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.HospitalInsuranceCompany;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;

/**
 * 線上申請-保單理賠申請 Dao.
 * 
 * @author all
 */
public interface TransInsuranceClaimDao {
	
	/**
	 * 取得TRANS_INSURANCE_CLAIM_SEQ的next value
	 * 
	 * @return Float
	 * @throws Exception
	 */
	Float getInsuranceClaimSequence() throws Exception;

	/**
	 * 保單理賠申請
	 * 
	 * @param params
	 */
	int addInsuranceClaim(@Param("vo") TransInsuranceClaimVo vo) throws Exception;
	
	/**
	 * 保單理賠申請
	 * 
	 * @param params
	 */
	int addInsuranceClaimFileData(@Param("vo") TransInsuranceClaimFileDataVo vo) throws Exception;

	/**
	 * 更新文件内容信息
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimFileDataFileBase64(@Param("vo") TransInsuranceClaimFileDataVo vo) throws Exception;
	/**
	 * 查询文件类型的id 编号
	 * @throws Exception
	 */
	Float selectInsuranceClaimFileDataId() throws Exception;

	/**
	 * 保單理賠申請明細
	 * 
	 * @param params
	 */
	List<TransInsuranceClaimVo> getTransInsuranceClaimByTransNum(@Param("transNum") String transNum) throws Exception;
	
	/**
	 * 保單理賠申請明細
	 * 
	 * @param params
	 */
	List<TransInsuranceClaimFileDataVo> getFileDatasByClaimSeqId(@Param("claimSeqId") Float claimSeqId) throws Exception;
	
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

    List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(@Param("functionName") String functionName,@Param("status")  String status);

    int getInsuranceClaimWhetherFirst(@Param("policyNo")String policyNo, @Param("functionName")String insuranceClaimParameterCode);

	int updateTransApplyDate(@Param("claimSeqId") Float claimSeqId, @Param("date") Date date);
}