package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 醫起通申請書套印服務.
 * 
 * @author all
 */
public interface IMedicalTreatmentService {
	
	/**
	 * 取得醫起通申請書報表 byte[].
	 * 
	 * @param claimVo TransInsuranceClaimVo
	 * @return 回傳報表 byte[]
	 */
	public byte[] getPDF(TransMedicalTreatmentClaimVo claimVo);

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 保單醫起通申請-新增.
	 *
	 * @param TransMedicalTreatmentClaimVo transMedicalTreatmentClaimVo
	 * @return 回傳影響筆數
	 */
	public Map<String,Object> inserttransMedicalTreatmentClaimVo(TransMedicalTreatmentClaimVo transMedicalTreatmentClaimVo, TransStatusHistoryVo hisVo);
	
	/**
	 * 保單醫起通申請-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public TransMedicalTreatmentClaimVo getTransInsuranceClaimDetail(String transNum);
	
	/**
	 * 保單醫起通申請-上傳申請應備文件
	 * 
	 * @param files 
	 * @return 上傳文件結果
	 */
	public List<TransMedicalTreatmentClaimFileDataVo> upLoadFiles(MultipartFile[] files);
	
	
	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 * @return
	 */
	public String checkBackList(BlackListVo blackListVo);
	
	/**
	 * 檢查保單是否完成
	 */
	public int getPolicyClaimCompleted(String rocId);
	
	/**
	 * 醫起通聯盟鏈險種
	 */
	public List<String> getInsClaimPlan();
	
	
	/**
	 * 獲取年齡
	 */
	public String getAgeByPolicyNo(String policyNo);
	
	public byte[] getPDF1(TransMedicalTreatmentClaimVo claimVo);
	
	
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
	public List<TransMedicalTreatmentClaimFileDataVo>  transInsuranceClaimFileDataVoListBase64ToMiniature(List<TransMedicalTreatmentClaimFileDataVo> list);
	
	public Map<String, Object> getSendMailInfo(String string);
	
	/**
	 * 依保單號碼找出險種
	 * @param policyNo
	 * @return List<String>
	 */
	public List<String> getProductCodeByPolicyNo(String policyNo);

	/**
	 * 修改文件的數據Base64
	 * @param transMedicalTreatmentClaimFileDataVo
	 * @return
	 */
	int  updateInsuranceClaimFileDataFileBase64(TransMedicalTreatmentClaimFileDataVo  transMedicalTreatmentClaimFileDataVo) throws Exception;


	/**
	 * //獲取保險公司明顯
	 * @param functionName  對於的按個功能下的數據,名稱代號
	 * @return
	 * @throws Exception
	 */
    List<Hospital> getHospitalList(String  functionName,String status)throws  Exception;

	/**
	 * //獲取醫院明顯
	 * @param functionName 對於的按個功能下的數據,名稱代號
	 * @return
	 * @throws Exception
	 */
	List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(String  functionName,String status)throws Exception;
	/**
	 *
	 * 當前保單是否為地一次提交,是否有對於的文件數據信息
	 * */
    Integer getMedicalTreatmentWhetherFirst(String policyNo, String medicalTreatmentParameterCode)throws Exception;
    
    /**
     * 獲取生日(yyyy/MM/dd)
     * @param policyNo
     * @return String
     */
	public String  getBirdateByPolicyNo(String policyNo);

	//獲取當前正在申請中的保單
    String getPolicyClaimCompletedPolicyno(String userRocId)throws Exception;
	//第二次申請非當前申請保單進行鎖定
	List<PolicyListVo> handlPolicyClaimCompletedPolicynoNotLocked(List<PolicyListVo> handledPolicyList, String userRocId)throws Exception ;
	//進行獲取當前保單已經選中的醫院資料
    List<Hospital> gitChooseHospitalList(String policyNo, String userRocId)throws Exception;

    List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo(Float claimSeqId);

	List<Map<String, Object>> autoCheckedCompany(String url, Map<String, String> params) throws Exception;

	int updateTransApplyDate(Float claimSeqId, Date date);

    List<TransMedicalTreatmentClaimVo> getUnProcessedTrans(Float claimSeqId);

	int updateTransUploadDate(String transNum, Date date);

	List<TransMedicalTreatmentClaimVo> getUnProcessedSendAlliance(Float claimSeqId);
}