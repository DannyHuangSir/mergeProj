package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 理賠申請書套印服務.
 * 
 * @author all
 */
public interface IInsuranceClaimService {
	
	/**
	 * 取得理賠申請書報表 byte[].
	 * 
	 * @param claimVo TransInsuranceClaimVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransInsuranceClaimVo claimVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 保單理賠申請-新增.
	 * 
	 * @param transInsuranceClaimVo TransInsuranceClaimVo
	 * @return 回傳影響筆數
	 */
	public Map<String,Object> insertTransInsuranceClaim(TransInsuranceClaimVo transInsuranceClaimVo, TransStatusHistoryVo hisVo);
	
	/**
	 * 保單理賠申請-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransInsuranceClaimVo getTransInsuranceClaimDetail(String transNum);
	
	/**
	 * 保單理賠申請-上傳申請應備文件
	 * 
	 * @param files 
	 * @return 上傳文件結果
	 */
	public List<TransInsuranceClaimFileDataVo> upLoadFiles(MultipartFile[] files);
	
	
	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 * @return
	 */
	public String checkBackList(BlackListVo blackListVo);
	
	/**
	 * 檢查保單理賠是否完成
	 */
	public int getPolicyClaimCompleted(String rocId);
	
	/**
	 * 理賠聯盟鏈險種
	 */
	public List<String> getInsClaimPlan();
	
	
	/**
	 * 獲取年齡
	 */
	public String getAgeByPolicyNo(String policyNo);
	
	public byte[] getPDF1(TransInsuranceClaimVo claimVo);
	
	
	public String getParameterValueByCode( String systemId, String parameterCode);

	/**
	 * 文件首次上传，将文件转换为缩图Base64格式，提供页面显示
	 * @param filePath  首次上传文件地址
	 * @return  转换后的Base64格式的数据
	 */
	public String converFiestFileToBase64Str(String filePath);

	/**
	 * 原始文件转换为Base64格式，存入DB
 	 * @param filePath
	 * @return
	 */
	public String converFileToBase64Str(String filePath);
	/**
	 * 原始文件转换为Base64格式，(缩略图)
	 * @param filePath
	 * @return
	 */
	public String converFileToBase64Miniature(String filePath);

	/**
	 * 获取Base64 文件数据，转换为缩图
	 *    如Base64无数据，获取Path+file_name 进行转换为缩图
	 * @param list   Base64 文件数据
	 * @return
	 */
	public List<TransInsuranceClaimFileDataVo>  transInsuranceClaimFileDataVoListBase64ToMiniature(List<TransInsuranceClaimFileDataVo> list);
	
	public Map<String, Object> getSendMailInfo(String string);
	
	/**
	 * 依保單號碼找出險種
	 * @param policyNo
	 * @return List<String>
	 */
	public List<String> getProductCodeByPolicyNo(String policyNo);

	/**
	 * 修改文件的數據Base64
	 * @param transInsuranceClaimFileDataVo
	 * @return
	 */
	int  updateInsuranceClaimFileDataFileBase64(TransInsuranceClaimFileDataVo  transInsuranceClaimFileDataVo) throws Exception;

    List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(String functionName, String status);
}