package com.twfhclife.adm.controller.medical;

import com.google.common.collect.Maps;
import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IMedicalTreatmentClaimFileDataService;
import com.twfhclife.adm.service.IOnlineChangeService;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.generic.annotation.*;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;

import com.twfhclife.generic.util.SignStatusUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 報表查詢-保單醫療申請統計報表.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class MedicalTreatmentReportController extends BaseController {

	private static final Logger logger = LogManager.getLogger(MedicalTreatmentReportController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private IMedicalTreatmentClaimFileDataService iMedicalTreatmentClaimFileDataService;
	/**
	 * 線上申請查詢頁面-結果程式進入點.
	 * @return
	 */	
	@RequestLog
	@RequestMapping("/medicalTreatmentStatistics")
	public String statistics(MedicalTreatmentStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		//獲取醫療資料介接案件狀態信息列表
		List<ParameterVo> optionList = parameterService.getOptionList(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS);
		//獲取申請狀態數據信息
		List<ParameterVo> applyForOptionStatusList = parameterService.getOptionList(ApConstants.SYSTEM_ID_ESERVICE, ApConstants.ONLINE_CHANGE_STATUS);
		/*0	處理中 1	已審核  5	已上傳 6	失敗*/
		String [] str = {"0","1","5","6"};
		List<ParameterVo> collect = applyForOptionStatusList.stream().filter((x) -> {
			for (String s : str) {
				if (s.equals(x.getParameterCode())) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
		//int size = collect.size();
		addAttribute("medicalStatusOptionList", optionList);
		addAttribute("applyForOptionStatusList", collect);
		return   "backstage/medical/medicalTreatmentStatisticalReport1";
	}

	/**
	 * 獲取查詢條件
	 *   保單理賠申請統計報表
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentStatistics/filter")
	public String statisticsFilter(MedicalTreatmentStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return   "backstage/medical/medicalTreatmentStatisticalReport2";
	}
	
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentStatistics/csv")
	public String statisticsCsv(MedicalTreatmentStatisticsVo claimVo) {
		List reportList = onlineChangeService.getMedicalTreatmentStatisticsReport(claimVo);
		if (CollectionUtils.isNotEmpty(reportList)) {
			reportList.forEach(x -> {
				String signStatus = (String) ((Map)x).get("SIGN_STATUS");
				if (StringUtils.isNotBlank(signStatus)) {
					((Map) x).put("SIGN_STATUS", SignStatusUtil.signStatusToStr(signStatus));
				}
				String verifyStatus = (String) ((Map)x).get("ID_VERIFY_STATUS");
				if (StringUtils.isNotBlank(verifyStatus)) {
					((Map) x).put("ID_VERIFY_STATUS", SignStatusUtil.verifyStatusToStr(verifyStatus));
				}
			});
		}
			//授權醫療保險公司名稱
		addAttribute("csvHospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));

		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		return   "backstage/medical/medicalTreatmentStatisticalReport3";
	}
	
	
	/**
	 * 線上申請查詢頁面-結果程式進入點
	 * 
	 * @return
	 */	
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "190",
			systemId = ApConstants.SYSTEM_ID
			))
	@RequestMapping("/medicalTreatmentDetail")
	public String rptInsClaimDetail(MedicalTreatmentStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		//獲取醫療資料介接案件狀態信息列表
		List<ParameterVo> optionList = parameterService.getOptionList(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS);
		//獲取申請狀態數據信息
		List<ParameterVo> applyForOptionStatusList = parameterService.getOptionList(ApConstants.SYSTEM_ID_ESERVICE, ApConstants.ONLINE_CHANGE_STATUS);
		/*0	處理中 1	已審核  5	已上傳 6	失敗*/
		String [] str = {"0","1","5","6"};
		List<ParameterVo> collect = applyForOptionStatusList.stream().filter((x) -> {
				for (String s : str) {
					if (s.equals(x.getParameterCode())) {
						return false;
					}
				}
			return true;
		}).collect(Collectors.toList());
		//int size = collect.size();
		addAttribute("medicalStatusOptionList", optionList);
		addAttribute("applyForOptionStatusList", collect);
		return  "backstage/medical/medicalTreatmentDetailReport1";
	}
	
	/**
	 * 獲取查詢條件
	 * 	 保單理賠申請明細報表
	 * @return
	 */	
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/filter")
	public String rptInsClaimDetailFilter(MedicalTreatmentStatisticsVo claimVo) {
		addAttribute("claimVo", claimVo);
		return  "backstage/medical/medicalTreatmentDetailReport2";
	}
	/**
	 * 獲得CSV數據
	 * 
	 * @return
	 */	
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/csv")
	public String rptInsClaimDetailCSV(MedicalTreatmentStatisticsVo claimVo) {
		//System.out.println(claimVo);
		List reportList = onlineChangeService.getMedicalTreatmentDetailReport(claimVo);
		if (CollectionUtils.isNotEmpty(reportList)) {
			for (int i = 0; i < reportList.size(); i++) {
				Map x = (Map) reportList.get(i);
				if (x != null) {
					String signStatus = (String) x.get("SIGN_STATUS");
					if (StringUtils.isNotBlank(signStatus)) {
						x.put("SIGN_STATUS", SignStatusUtil.signStatusToStr(signStatus));
					}
					String verifyStatus = (String) x.get("ID_VERIFY_STATUS");
					if (StringUtils.isNotBlank(verifyStatus)) {
						x.put("ID_VERIFY_STATUS", SignStatusUtil.verifyStatusToStr(verifyStatus));
					}
				} else {
					x = Maps.newHashMap();
					reportList.set(i, x);
				}
			}
		}
		//獲取醫療保險公司的名稱
		//授權醫療單位名稱
		addAttribute("csvHospitalList", onlineChangeService.getHospitalList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));
		//授權醫療保險公司名稱
		addAttribute("csvHospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));

		addAttribute("claimVo", claimVo);
		addAttribute("reportList", reportList);
		return  "backstage/medical/medicalTreatmentDetailReport3";
	}

	/**
	 * 進行獲取Bsse64的文件數據信息大小
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/base64FileSize")
	public ResponseEntity<ResponseObj> getMedicalTreatmentDetailBase64FileSize(@RequestBody MedicalTreatmentClaimFileDataVo  medicalTreatmentClaimFileDataVo) {
		try {
			//進行查詢當前文件的大小
			System.out.println(medicalTreatmentClaimFileDataVo.getFdId());
			MedicalTreatmentClaimFileDataVo medicalFileVo=onlineChangeService.getMedicalTreatmentDetailBase64FileSize(medicalTreatmentClaimFileDataVo.getFdId());
			processSuccess(medicalFileVo);
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 進行獲取Bsse64的文件數據信息大小
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/medicalInfoBase64FileSize")
	public ResponseEntity<ResponseObj> getMedicalInfoFileSize(@RequestBody MedicalTreatmentClaimFileDataVo  medicalTreatmentClaimFileDataVo) {
		try {
			//進行查詢當前文件的大小
			System.out.println(medicalTreatmentClaimFileDataVo.getFdId());
			MedicalTreatmentClaimFileDataVo medicalFileVo=onlineChangeService.getMedicalInfoBase64FileSize(medicalTreatmentClaimFileDataVo.getFdId());
			processSuccess(medicalFileVo);
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}


	/**
	 * 執行醫療資料介接	重新上傳
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/getUploadMedicalCases")
	public ResponseEntity<ResponseObj> getUploadMedicalCases(@RequestBody MedicalTreatmentClaimFileDataVo  medicalTreatmentClaimFileDataVo) {
		try {
			/**
			 * 進行驗證當前保單的醫療資料是已經獲取
			 * */
			String transNum = medicalTreatmentClaimFileDataVo.getTransNum();
			if (StringUtils.isNotBlank(transNum)){
				int  uploadCount= onlineChangeService.getTransMedicalTreatmentByCount(transNum);
				//1.2  獲取當前案件已上傳影像系統成功條數
				int  uploadSuccess= onlineChangeService.getTransMedicalTreatmentBySuccessCount(transNum);
				if(uploadCount>0) {
					if (uploadCount == uploadSuccess) {
						//進行查詢修改文件的狀態
						int  i=iMedicalTreatmentClaimFileDataService.updaetMedicalTreatmentClaimFileDataFileStatusCases(medicalTreatmentClaimFileDataVo);
						if (i > 0) {
							String allianceStatus = medicalTreatmentClaimFileDataVo.getAllianceStatus();
							String itpr = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS_ITPR);
							if (allianceStatus != null && itpr.equals(allianceStatus)) {
								String parameterValueByCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_ITPR_WINDOW_MSG);
								processSuccess(parameterValueByCode);
							} else {
								processSuccess(i);
							}
						}
					}else{
						String notFileWindowMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_UPLOAD_NOT_FILE_WINDOW_MSG);
						logger.info("getUploadMedicalCases:  目前不可執行醫療資料介接重新上傳：有檔案未完全上傳影響系統");
						processError(notFileWindowMsg);
					}
				}else{
					String notFileDBWindowMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_UPLOAD_FILE_NOT_DB_WINDOW_MSG);
					logger.info("getUploadMedicalCases:  目前不可執行醫療資料介接重新上傳：有檔案未下載完成，請稍後再試");
					processError(notFileDBWindowMsg);
				}
			}else {
				processSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getMedicalFileCases: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}



	/**
	 * 執行醫療資料介接	-取/不取用醫療資料
	 * @param medicalTreatmentClaimFileDataVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/getMedicalFileCases")
	public ResponseEntity<ResponseObj> getMedicalFileCases(@RequestBody MedicalTreatmentClaimFileDataVo  medicalTreatmentClaimFileDataVo) {
		try {
			System.out.println(medicalTreatmentClaimFileDataVo);
			//進行查詢修改文件的狀態
			int  i=iMedicalTreatmentClaimFileDataService.updaetMedicalTreatmentClaimFileDataFileStatusCases(medicalTreatmentClaimFileDataVo);

			if(i>0 ){
				String allianceStatus = medicalTreatmentClaimFileDataVo.getAllianceStatus();
				String	itpr = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID,ApConstants.MEDICAL_INTERFACE_STATUS_ITPR);
				if (allianceStatus!=null && itpr.equals(allianceStatus)){
					String	parameterValueByCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID,ApConstants.MEDICAL_ITPR_WINDOW_MSG);
					processSuccess(parameterValueByCode);
				}else{
			processSuccess(i);
				}
			}else {
				processSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getMedicalFileCases: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	/**
	 * 執行醫療資料介接	-更新案件狀態
	 * @param medicalTreatmentClaimFileDataVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/medicalTreatmentDetail/getMedicalUpdateCaseStatus")
	public ResponseEntity<ResponseObj> getMedicalUpdateCaseStatus(@RequestBody MedicalTreatmentClaimFileDataVo  medicalTreatmentClaimFileDataVo) {
		try {
			System.out.println(medicalTreatmentClaimFileDataVo);
			//進行查詢獲取到當前保單的caseID
		  String caseID=		iMedicalTreatmentClaimFileDataService.getTreatmentClaimCaseId(medicalTreatmentClaimFileDataVo);
			//進行修改狀態信息[NOTIFY_OF_NEW_CASE_MEDICAL]
			int  i=iMedicalTreatmentClaimFileDataService.updaetNotifyOfNewCaseMedicalStatus(caseID);
			String parameterValueByCode=null;
			if(i>0){
				 parameterValueByCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID,ApConstants.MEDICAL_UPDATE_WINDOW_MSG);
			}

			if(i<1){
				parameterValueByCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID,ApConstants.MEDICAL_UPDATE_STATUS_WINDOW_MSG);
			}
			processSuccess(parameterValueByCode);
		} catch (Exception e) {
			logger.error("Unable to getMedicalUpdateCaseStatus: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

}
