package com.twfhclife.adm.controller.rpt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IOnlineChangeService;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.generic.annotation.*;
import com.twfhclife.generic.api_client.APIAllianceTemplateClient;
import com.twfhclife.generic.api_model.APIAllianceRequestVo;
import com.twfhclife.generic.api_model.ApiResponseObj;
import com.twfhclife.generic.api_model.ReturnHeader;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 報表查詢-線上申請查詢.
 * 
 * @author all
 */
@Controller
@EnableAutoConfiguration
public class OnlineChangeController extends BaseController {

	private static final Logger logger = LogManager.getLogger(OnlineChangeController.class);

	@Autowired
	private IOnlineChangeService onlineChangeService;
	@Autowired
	private APIAllianceTemplateClient apiAllianceTemplateClient;
	@Autowired
	private IParameterService parameterService;

	/**
	 * 線上申請查詢頁面-結果程式進入點.
	 * 
	 * @return
	 */	
	@RequestLog
	@LoginCheck(value = @FuncUsageParam(
			funcId = "190",
			systemId = ApConstants.SYSTEM_ID
			))
	@GetMapping("/onlineChange")
	public String onlineChange() {
		addAttribute("onlineChange", parameterService.getParameterByCategoryCode(null, "ONLINE_CHANGE"));
		addAttribute("onlineChangeStatus", parameterService.getParameterByCategoryCode(null, "ONLINE_CHANGE_STATUS"));
		return "backstage/rpt/onlineChange";
	}

	/**
	 * 取得線上申請查詢結果.
	 * 
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChangeDetail")
	public String onlineChangeDetail() {
		return "backstage/rpt/onlineChangeDetail";
	}
	
	/**
	 * 取得線上申請查詢結果.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_001, 
			sqlId = EventCodeConstants.ONLINECHANGE_001_SQL_ID,
			daoVoParamKey = "transVo", 
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeDetail")
	public ResponseEntity<PageResponseObj> getOnlineChangeDetail(@RequestBody TransVo transVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			// Note: TransVo 需要繼承Pagination，接收 jquery datatable 的start 跟 length 屬性
			// 設定jquery datatable 資料查詢集合
			pageResp.setAaData(onlineChangeService.getOnlineChangeDetail(transVo,true));
			// 設定jquery datatable 需要的資料總筆數
			if(ApConstants.TRANS_TYPE_CONTACT_INFO.equals(transVo.getTransType()) ) {//變更保單聯絡資料
				pageResp.setiTotalDisplayRecords(onlineChangeService.getOnlineChangeCIODetailTotal(transVo));
			}else if(ApConstants.TRANS_TYPE_DNS_ALLIANCE.equals(transVo.getTransType())) {
				pageResp.setiTotalDisplayRecords(onlineChangeService.getOnlineChangeDnsDetailTotal(transVo));
			}else if(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE.equals(transVo.getTransType())) {
				pageResp.setiTotalDisplayRecords(onlineChangeService.getOnlineChangeMedicalTreatmentDetailTotal(transVo));
			}else {
				pageResp.setiTotalDisplayRecords(onlineChangeService.getOnlineChangeDetailTotal(transVo));
			}
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to getOnlineChangeDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}

	/**
	 * 取得線上申請單筆詳細資料(繳別).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_002, 
			sqlId = EventCodeConstants.ONLINECHANGE_002_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransPaymode")
	public String getTransPaymode(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			Map<String, Object> detail = onlineChangeService.getTransPaymode(transVo);
			addAttribute("detailData", detail);
			String INVESTMENT_TYPES = parameterService.getParameterValueByCode("eservice", "INVESTMENT_TYPE");
			/*if (StringUtils.isNotBlank(INVESTMENT_TYPES) &&
					INVESTMENT_TYPES.contains(detail.get("SHOW_POLICY_NO").toString().substring(0, 2))) {
				addAttribute("showAmount", true);
			} else {
				addAttribute("showAmount", false);
			}*/
			//目前暫時關閉顯示 2021/11/25 xianzhi
			addAttribute("showAmount", false);
			}else {
				logger.error("Unable to getTransPaymode: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransPaymode: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-paymode";
	}
	
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_029, 
			sqlId = EventCodeConstants.ONLINECHANGE_029_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransInsuranceClaim")
	public String getTransInsuranceClaim(@RequestBody TransVo transVo) {
		try {
			Map<String, Object> map = onlineChangeService.getTransInsuranceClaim(transVo);
			addAttribute("detailData", map);
			List<Map> list = (List) map.get("FileDatas");
			Map<String, List<Map<String, Object>>> fileData = Maps.newHashMap();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					if (fileData.get(m.get("TYPE")) != null) {
						fileData.get(m.get("TYPE")).add(m);
					} else {
						fileData.put(m.get("TYPE").toString(), Lists.newArrayList(m));
					}
				}
			}
			addAttribute("fileData", fileData);
			SignRecord signRecord = onlineChangeService.getNewSignStatus(String.valueOf(map.get("TRANS_NUM")));
			if (signRecord != null) {
				Map<String, Object> signRecordMap = Maps.newHashMap();
				signRecordMap.put("idVerifyTime", signRecord.getIdVerifyTime() != null ? DateUtil.formatDateTime(signRecord.getIdVerifyTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("idVerifyStatus", SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), null));
				signRecordMap.put("signTime", signRecord.getSignTime() != null ? DateUtil.formatDateTime(signRecord.getSignTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("signStatus", SignStatusUtil.signStatusToStr(null, signRecord.getSignStatus()));
				signRecordMap.put("signDownload", signRecord.getSignDownload());
				signRecordMap.put("signFileId", signRecord.getSignFileId());
				addAttribute("signRecord", signRecordMap);
			}
			addAttribute("hospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList("INSURANCE_CLAIM"));
		} catch (Exception e) {
			logger.error("Unable to getTransInsuranceClaim: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-policyClaims";
	}

	@GetMapping("/onlineChange/getPolicyClaimPrint")
	public String getPolicyClaimPrint(TransVo transVo) {
		try {
			Map<String, Object> map = onlineChangeService.getTransInsuranceClaim(transVo);
			addAttribute("detailData", map);
			List<Map> list = (List) map.get("FileDatas");
			Map<String, List<Map<String, Object>>> fileData = Maps.newHashMap();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					if (fileData.get(m.get("TYPE")) != null) {
						fileData.get(m.get("TYPE")).add(m);
					} else {
						fileData.put(m.get("TYPE").toString(), Lists.newArrayList(m));
					}
					if (StringUtils.contains(String.valueOf(m.get("FILE_NAME")), "pdf")) {
						Document document = new Document();
						byte[] fileBytes = Base64.getDecoder().decode(String.valueOf(m.get("FILE_BASE64")));
						document.setByteArray(fileBytes, 0, fileBytes.length, null);
						List<String> imagesBase64 = Lists.newArrayList();
						float scale = 2.5f;
						float rotation = 0f;
						for (int i = 0; i < document.getNumberOfPages(); i++) {
							try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
								BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation, scale);
								ImageIO.write(image, "png", bos);
								imagesBase64.add(Base64.getEncoder().encodeToString(bos.toByteArray()));
							}
						}
						m.put("pdfBase64", imagesBase64);
					}
				}
			}
			addAttribute("fileData", fileData);
			SignRecord signRecord = onlineChangeService.getNewSignStatus(String.valueOf(map.get("TRANS_NUM")));
			if (signRecord != null) {
				Map<String, Object> signRecordMap = Maps.newHashMap();
				signRecordMap.put("idVerifyTime", signRecord.getIdVerifyTime() != null ? DateUtil.formatDateTime(signRecord.getIdVerifyTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("idVerifyStatus", SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), null));
				signRecordMap.put("signTime", signRecord.getSignTime() != null ? DateUtil.formatDateTime(signRecord.getSignTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("signStatus", SignStatusUtil.signStatusToStr(null, signRecord.getSignStatus()));
				signRecordMap.put("signDownload", signRecord.getSignDownload());
				signRecordMap.put("signFileId", signRecord.getSignFileId());
				addAttribute("signRecord", signRecordMap);
			}
		} catch (Exception e) {
			logger.error("Unable to getPolicyClaimPrint: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-policyClaimPrint";
	}

	private List<byte[]> pdfBufferedImage(PDDocument doc ) {
		try {
			PDFRenderer renderer = new PDFRenderer(doc);
			int pageCount = doc.getNumberOfPages();
			List<byte[]> pdfImgArray = Lists.newArrayList();
			for (int i = 0; i < pageCount; i++) {
				try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
					BufferedImage image = renderer.renderImageWithDPI(i, 144);
					ImageIO.write(image, "png", bos);
					pdfImgArray.add(bos.toByteArray());
				}
			}
			return pdfImgArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保單醫療獲取明顯
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_029,
			sqlId = EventCodeConstants.ONLINECHANGE_029_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getMedicalTreatmentClaim")
	public String getMedicalTreatmentClaim(@RequestBody TransVo transVo) {
		try {
			//授權醫療單位名稱
			addAttribute("hospitalList", onlineChangeService.getHospitalList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));
			//授權醫療保險公司名稱
			addAttribute("hospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));
			Map<String, Object> detailDataMap = onlineChangeService.getMedicalTreatmentClaim(transVo);
			addAttribute("detailData", detailDataMap);
			if (detailDataMap.containsKey("CLAIMS_SEQ_ID")) {
				addAttribute("medicalInfo", onlineChangeService.getMedicalInfo((Double) detailDataMap.get("CLAIMS_SEQ_ID")));
			}
			if (detailDataMap.containsKey("ALLIANCE_STATUS")) {
				String code = (String) detailDataMap.get("ALLIANCE_STATUS");
				ParameterVo parameterVo = new ParameterVo();
				parameterVo.setParameterCode("MEDICAL_INTERFACE_STATUS_" + code);
				parameterVo.setSystemId(ApConstants.SYSTEM_API_ID);
				List<ParameterVo> parameterVos = parameterService.getParameter(parameterVo);
				if (CollectionUtils.isNotEmpty(parameterVos)) {
					addAttribute("statusStr", parameterVos.get(0).getParameterName());
					addAttribute("status", code);
				}
			}
			List<Map> list = (List) detailDataMap.get("FileDatas");
			Map<String, List<Map<String, Object>>> fileData = Maps.newHashMap();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					if (fileData.get(m.get("TYPE")) != null) {
						fileData.get(m.get("TYPE")).add(m);
					} else {
						fileData.put(m.get("TYPE").toString(), Lists.newArrayList(m));
					}
				}
			}
			addAttribute("fileData", fileData);
			SignRecord signRecord = onlineChangeService.getNewSignStatus(String.valueOf(detailDataMap.get("TRANS_NUM")));
			if (signRecord != null) {
				Map<String, Object> signRecordMap = Maps.newHashMap();
				signRecordMap.put("idVerifyTime", signRecord.getIdVerifyTime() != null ? DateUtil.formatDateTime(signRecord.getIdVerifyTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("idVerifyStatus", SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), null));
				signRecordMap.put("signTime", signRecord.getSignTime() != null ? DateUtil.formatDateTime(signRecord.getSignTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("signStatus", SignStatusUtil.signStatusToStr(null, signRecord.getSignStatus()));
				signRecordMap.put("signDownload", signRecord.getSignDownload());
				signRecordMap.put("signFileId", signRecord.getSignFileId());
				addAttribute("signRecord", signRecordMap);
			}
		} catch (Exception e) {
			logger.error("Unable to getTransInsuranceClaim: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-medicalTreatment";
	}

	@GetMapping("/onlineChange/medicalTreatmentPrint")
	public String medicalTreatmentPrint(TransVo transVo) {
		try {
			//授權醫療單位名稱
			addAttribute("hospitalList", onlineChangeService.getHospitalList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));
			//授權醫療保險公司名稱
			addAttribute("hospitalInsuranceCompanyList", onlineChangeService.getHospitalInsuranceCompanyList(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE));
			Map<String, Object> detailDataMap = onlineChangeService.getMedicalTreatmentClaim(transVo);
			addAttribute("detailData", detailDataMap);
			if (detailDataMap.containsKey("CLAIMS_SEQ_ID")) {
				addAttribute("medicalInfo", onlineChangeService.getMedicalInfo((Double) detailDataMap.get("CLAIMS_SEQ_ID")));
			}
			if (detailDataMap.containsKey("ALLIANCE_STATUS")) {
				String code = (String) detailDataMap.get("ALLIANCE_STATUS");
				ParameterVo parameterVo = new ParameterVo();
				parameterVo.setParameterCode("MEDICAL_INTERFACE_STATUS_" + code);
				parameterVo.setSystemId(ApConstants.SYSTEM_API_ID);
				List<ParameterVo> parameterVos = parameterService.getParameter(parameterVo);
				if (CollectionUtils.isNotEmpty(parameterVos)) {
					addAttribute("statusStr", parameterVos.get(0).getParameterName());
					addAttribute("status", code);
				}
			}
			List<Map> list = (List) detailDataMap.get("FileDatas");
			Map<String, List<Map<String, Object>>> fileData = Maps.newHashMap();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					if (fileData.get(m.get("TYPE")) != null) {
						fileData.get(m.get("TYPE")).add(m);
					} else {
						fileData.put(m.get("TYPE").toString(), Lists.newArrayList(m));
					}
					if (StringUtils.contains(String.valueOf(m.get("FILE_NAME")), "pdf")) {
						Document document = new Document();
						byte[] fileBytes = Base64.getDecoder().decode(String.valueOf(m.get("FILE_BASE64")));
						document.setByteArray(fileBytes, 0, fileBytes.length, null);
						List<String> imagesBase64 = Lists.newArrayList();
						float scale = 2.5f;
						float rotation = 0f;
						for (int i = 0; i < document.getNumberOfPages(); i++) {
							try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
								BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation, scale);
								ImageIO.write(image, "png", bos);
								imagesBase64.add(Base64.getEncoder().encodeToString(bos.toByteArray()));
							}
						}
						m.put("pdfBase64", imagesBase64);
					}
				}
			}
			addAttribute("fileData", fileData);
			SignRecord signRecord = onlineChangeService.getNewSignStatus(String.valueOf(detailDataMap.get("TRANS_NUM")));
			if (signRecord != null) {
				Map<String, Object> signRecordMap = Maps.newHashMap();
				signRecordMap.put("idVerifyTime", signRecord.getIdVerifyTime() != null ? DateUtil.formatDateTime(signRecord.getIdVerifyTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("idVerifyStatus", SignStatusUtil.signStatusToStr(signRecord.getIdVerifyStatus(), null));
				signRecordMap.put("signTime", signRecord.getSignTime() != null ? DateUtil.formatDateTime(signRecord.getSignTime(), "yyyy/MM/dd HH:mm") : "");
				signRecordMap.put("signStatus", SignStatusUtil.signStatusToStr(null, signRecord.getSignStatus()));
				signRecordMap.put("signDownload", signRecord.getSignDownload());
				signRecordMap.put("signFileId", signRecord.getSignFileId());
				addAttribute("signRecord", signRecordMap);
			}
		} catch (Exception e) {
			logger.error("Unable to getTransInsuranceClaim: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-medicalTreatmentPrint";
	}


	/**
	 * 醫療
	 * 開啟傳送聯盟/傳送聯盟覆核.
	 *
	 * @param TransInsuranceClaimVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.MEDICAL_TREATMENT_39,
			sqlId = EventCodeConstants.MEDICAL_TREATMENT_39_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/updateMedicalTreatmentSendAlliance")
	public ResponseEntity<ResponseObj> updateMedicalTreatmentSendAlliance(@RequestBody TransMedicalTreatmentClaimVo vo) {
		try {
			int result = -1;
			if(vo!=null && vo.getTransNum()!=null) {
				result = onlineChangeService.updateOrAddMedicalTreatment(vo);
			}
			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得線上申請單筆詳細資料(年金給付方式).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_003, 
			sqlId = EventCodeConstants.ONLINECHANGE_003_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransAnnuityMethod")
	public String getTransAnnuityMethod(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransAnnuityMethod(transVo));
			}else {
				logger.error("Unable to getTransAnnuityMethod: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}			
		} catch (Exception e) {
			logger.error("Unable to getTransAnnuityMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-annuityMethod";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更紅利選擇權).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_004, 
			sqlId = EventCodeConstants.ONLINECHANGE_004_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransBonus")
	public String getTransBonus(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransBonus(transVo));
			}else {
				logger.error("Unable to getTransBonus: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}	
		} catch (Exception e) {
			logger.error("Unable to getTransBonus: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-bonus";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更增值回饋分享金領取方式).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_005, 
			sqlId = EventCodeConstants.ONLINECHANGE_005_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransReward")
	public String getTransReward(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransReward(transVo));
			}else {
				logger.error("Unable to getTransReward: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}	
		} catch (Exception e) {
			logger.error("Unable to getTransReward: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-reward";
	}

	/**
	 * 取得線上申請單筆詳細資料(自動墊繳選擇權).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_006, 
			sqlId = EventCodeConstants.ONLINECHANGE_006_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransCushion")
	public String getTransCushion(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransCushion(transVo));
			} else {
				logger.error("Unable to getTransCushion: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransCushion: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-cushion";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更受益人).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_007, 
			sqlId = EventCodeConstants.ONLINECHANGE_007_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransBeneficiary")
	public String getTransBeneficiary(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransBeneficiary(transVo));
			} else {
				logger.error("Unable to getTransBeneficiary: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransBeneficiary: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-beneficiary";
	}

	/**
	 * 取得線上申請單筆詳細資料(展期).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_008, 
			sqlId = EventCodeConstants.ONLINECHANGE_008_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransRenew")
	public String getTransRenew(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransRenew(transVo));
			} else {
				logger.error("Unable to getTransRenew: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransRenew: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-renew";
	}

	/**
	 * 取得線上申請單筆詳細資料(減額).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_009, 
			sqlId = EventCodeConstants.ONLINECHANGE_009_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransReduce")
	public String getTransReduce(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransReduce(transVo));
			} else {
				logger.error("Unable to getTransReduce: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransReduce: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-reduce";
	}

	/**
	 * 取得線上申請單筆詳細資料(申請減少保險金額).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_010, 
			sqlId = EventCodeConstants.ONLINECHANGE_010_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransReducePolicy")
	public String getTransReducePolicy(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransReducePolicy(transVo));
			} else {
				logger.error("Unable to getTransReducePolicy: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransReducePolicy: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-reducePolicy";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更保單聯絡資料).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_011, 
			sqlId = EventCodeConstants.ONLINECHANGE_011_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransContactInfo")
	public String getTransContactInfo(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransContactInfo(transVo));
			} else {
				logger.error("Unable to getTransContactInfo: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransContactInfo: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-contactInfo";
	}

	/**
	 * 取得線上申請單筆詳細資料(設定停損停利通知).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_012, 
			sqlId = EventCodeConstants.ONLINECHANGE_012_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransFundNotification")
	public String getTransFundNotification(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransFundNotification(transVo));
			} else {
				logger.error("Unable to getTransFundNotification: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransFundNotification: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-fundNotification";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更收費管道).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_013, 
			sqlId = EventCodeConstants.ONLINECHANGE_013_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransPayMethod")
	public String getTransPayMethod(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransPayMethod(transVo));
			} else {
				logger.error("Unable to getTransPayMethod: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransPayMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-payMethod";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更信用卡效期).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_014, 
			sqlId = EventCodeConstants.ONLINECHANGE_014_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransCreditCardInfo")
	public String getTransCreditCardInfo(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransCreditCardInfo(transVo));
			} else {
				logger.error("Unable to getTransCreditCardInfo: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransCreditCardInfo: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-creditCardInfo";
	}

	/**
	 * 取得線上申請單筆詳細資料(解約).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_015, 
			sqlId = EventCodeConstants.ONLINECHANGE_015_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransCancelContract")
	public String getTransCancelContract(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransCancelContract(transVo));
			} else {
				logger.error("Unable to getTransCancelContract: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransCancelContract: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-cancelContract";
	}

	/**
	 * 取得線上申請單筆詳細資料(紅利提領列印).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_016, 
			sqlId = EventCodeConstants.ONLINECHANGE_016_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransSurrender")
	public String getTransSurrender(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransSurrender(transVo));
			} else {
				logger.error("Unable to getTransSurrender: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransSurrender: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-surrender";
	}

	/**
	 * 取得線上申請單筆詳細資料(申請保單貸款).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_017, 
			sqlId = EventCodeConstants.ONLINECHANGE_017_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransLoan")
	public String getTransLoan(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransLoan(transVo));
			} else {
				logger.error("Unable to getTransLoan: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransLoan: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-loan";
	}

	/**
	 * 取得線上申請單筆詳細資料(基本資料變更).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_018, 
			sqlId = EventCodeConstants.ONLINECHANGE_018_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransLoanCustInfo")
	public String getTransLoanCustInfo(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransLoanCustInfo(transVo));
			} else {
				logger.error("Unable to getTransLoanCustInfo: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransLoanCustInfo: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-custInfo";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(滿期).
	 * 
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_021,
			sqlId = EventCodeConstants.ONLINECHANGE_021_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransMaturity")
	public String getTransMaturity(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransMaturity(transVo));
			} else {
				logger.error("Unable to getMaturity: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getMaturity: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-maturity";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(補發表單).
	 * 
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_024,
			sqlId = EventCodeConstants.ONLINECHANGE_024_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransResendPolicy")
	public String getTransResendPolicy(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransResendPolicy(transVo));
			} else {
				logger.error("Unable to getTransResendPolicy: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch(Exception e) {
			logger.error("Unable to getTransResendPolicy: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-resendPolicy";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(終止授權).
	 * 
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_025,
			sqlId = EventCodeConstants.ONLINECHANGE_025_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransCancelAuth")
	public String getTransCancelAuth(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransCancelAuth(transVo));
			} else {
				logger.error("Unable to getTransCancelAuth: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch(Exception e) {
			logger.error("Unable to getTransCancelAuth: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-cancelAuth";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(保單價值列印).
	 * 
	 * @param TransVo transVo
	 * @return 
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_026,
			sqlId = EventCodeConstants.ONLINECHANGE_026_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransValuePrint")
	public String getTransValuePrint(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransValuePrint(transVo));
			} else {
				logger.error("Unable to getTransValuePrint: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch(Exception e) {
			logger.error("Unable to getTransValuePrint: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-valuePrint";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(旅平險).
	 * 
	 * @param TransVo transVo
	 * @return 
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_027,
			sqlId = EventCodeConstants.ONLINECHANGE_027_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransTravelPolicy")
	public String getTransTravelPolicy(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransTravelPolicy(transVo));
			} else {
				logger.error("Unable to getTransTravelPolicy: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransTravelPolicy: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-travelPolicy";
	}

	/**
	 * 審核.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_019, 
			sqlId = EventCodeConstants.ONLINECHANGE_019_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeReview")
	public ResponseEntity<ResponseObj> getOnlineChangeReview(@RequestBody TransVo transVo) {
		try {
			transVo.setStatus("1");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 補件.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_020, 
			sqlId = EventCodeConstants.ONLINECHANGE_020_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeResend")
	public ResponseEntity<ResponseObj> getOnlineChangeResend(@RequestBody TransVo transVo) {
		try {
			transVo.setStatus("4");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 完成.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_022, 
			sqlId = EventCodeConstants.ONLINECHANGE_022_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeComplete")
	public ResponseEntity<ResponseObj> getOnlineChangeComplete(@RequestBody TransVo transVo) {
		try {
			transVo.setStatus("2");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeComplete: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 下載檔案.
	 * <p>
	 * <b>TRAVEL_POLICY(旅平險), MATURITY(滿期), LOAN(保單借款)</b>
	 * </p>
	 * 
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_023, 
			sqlId = EventCodeConstants.ONLINECHANGE_023_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@RequestMapping(path = "/onlineChange/getTransUploadZipFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> getTransUploadZipFile(@RequestParam("transNum") String transNum){
		byte[] zipBytes = null;
		ByteArrayResource resource = null;
		HttpHeaders headers = new HttpHeaders();
		File zipFile = null;
		long zipFileLength = 0;
		FileOutputStream fos = null;
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			java.util.Optional<byte[]> result = onlineChangeService.getTransUploadZipFile(transVo);
			if(result.isPresent()) {
				zipBytes = result.get();
				String outFilename = String.format("%s.zip", transVo.getTransNum());
				headers.add(HttpHeaders.CONTENT_DISPOSITION
						, String.format("attachment; filename=%s", outFilename));
				zipFile = new File(outFilename);
				fos = new FileOutputStream(zipFile);
				fos.write(zipBytes);
				fos.flush();
				fos.close();
				zipFileLength = zipFile.length();
				java.nio.file.Path path = Paths.get(zipFile.getAbsolutePath());
				resource = new ByteArrayResource(Files.readAllBytes(path));
				boolean isDel = zipFile.delete();
				if (!isDel) {
					logger.error("Failed to delete zip file");
				}
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch(Exception e) {
			logger.error("Unable to getTransUploadZipFile: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.badRequest().build();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ResponseEntity.ok().headers(headers).contentLength(zipFileLength)
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}
	
	/**
	 * 取得線上申請單筆詳細資料(變更匯款帳戶).
	 * 
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_028,
			sqlId = EventCodeConstants.ONLINECHANGE_028_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransChangePayAccount")
	public String getTransChangePayAccount(@RequestBody TransVo transVo) {
		try {
			if (ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransChangePayAccount(transVo));
			} else {
				logger.error("Unable to getTransChangePayAccount: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
		} catch (Exception e) {
			logger.error("Unable to getTransChangePayAccount: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-changePayAccount";
	}
	
	/**
	 * 已收到所有紙本/紙本覆核.
	 * 
	 * @param TransInsuranceClaimVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_030, 
			sqlId = EventCodeConstants.ONLINECHANGE_030_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/setInsuranceClaimFileReceivedYes")
	public ResponseEntity<ResponseObj> updateInsuranceClaimFileReceived(@RequestBody TransInsuranceClaimVo vo) {
		try {
			int result = -1;
			if(vo!=null && vo.getTransNum()!=null) {
				if (ApConstants.FILE_RECEIVED.equals(vo.getFileReceived())) {
					vo.setOldFileReceived("C");
				}
				result = onlineChangeService.updateICFileReceived(vo);
				result = onlineChangeService.updateInsuranceClaimFileReceived(vo);
			}

			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 開啟傳送聯盟/傳送聯盟覆核.
	 * 
	 * @param TransInsuranceClaimVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_031, 
			sqlId = EventCodeConstants.ONLINECHANGE_031_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/updateInsuranceClaimSendAlliance")
	public ResponseEntity<ResponseObj> updateInsuranceClaimSendAlliance(@RequestBody TransInsuranceClaimVo vo) {
		try {
			int result = -1;
			if(vo!=null && vo.getTransNum()!=null) {
				if (ApConstants.SEND_ALLIANCE.equals(vo.getFileReceived())) {
					vo.setOldFileReceived("C");
				}
				result = onlineChangeService.updateInsuranceClaimSendAlliance(vo);
				
				if("Y".equals(vo.getSendAlliance())) {//審核通過才insert INSURANCE_CLAIM,INSURANCE_CLAIM_FILEDATAS
					result = onlineChangeService.addInsuranceClaim(vo);
				}
				
			}

			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReview: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得線上申請單筆詳細資料(變更風險屬性).
	 * 
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_041,
			sqlId = EventCodeConstants.ONLINECHANGE_041_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransRiskLevel")
	public String getTransRiskLevel(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransRiskLevel(transVo));
			} else {
				logger.error("Unable to getTransRiskLevel: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch (Exception e) {
			logger.error("Unable to getTransRiskLevel: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-riskLevel";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(保戶基本資料更新).
	 * 
	 * @return
	 */
	@PostMapping("/onlineChange/getTransPolicyHolderProfile")
	public String getTransPolicyHolderProfile(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			addAttribute("detailData", onlineChangeService.getTransPolicyHolderProfile(transVo));
			} else {
				logger.error("Unable to getTransPolicyHolderProfile: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch (Exception e) {
			logger.error("Unable to getTransPolicyHolderProfile: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-policyHolderProfile";
	}

	/**
	 * 取得線上申請單筆詳細資料(變更投資標的及配置比例).
	 * 
	 * @return
	 */
	@PostMapping("/onlineChange/getTransFundSwitch")
	public String getTransFundSwitch(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
			Map<String, Object> dataMap = onlineChangeService.getTransFundSwitch(transVo);
			addAttribute("detailData", dataMap);
			} else {
				logger.error("Unable to getTransFundSwitch: {}", new Exception("SQL Injection"));
				addDefaultSystemError();
			}
			} catch (Exception e) {
			logger.error("Unable to getTransFundSwitch: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-fund-switch";
	}
	
	/**
	 * 死亡除戶
	 * 
	 * @return
	 */
	@PostMapping("/onlineChange/getDnsAlliance")
	public String getDnsAlliance(@RequestBody TransVo transVo) {
		try {
			Map<String, Object> dataMap = onlineChangeService.getDnsAlliance(transVo);
			addAttribute("detailData", dataMap);
		} catch (Exception e) {
			logger.error("Unable to getTransFundSwitch: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-dns";
	}
	
	/**
	 * 查詢聯盟鏈歷程
	 *
	 * @return
	 */
	@PostMapping("/onlineChange/getUnionCourseList")
	public ResponseEntity<ResponseObj> getUnionCourseList(@RequestBody UnionCourseVo vo) {
		try {
			List<UnionCourseVo> results = onlineChangeService.getUnionCourseList(vo);
			processSuccess(results);
		} catch (Exception e) {
			logger.error("Unable to getUnionCourseList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/***
	 * 查詢數位簽署歷程
	 * @param vo
	 * @return
	 */
	@PostMapping("/onlineChange/signHistoryList")
	public ResponseEntity<ResponseObj> signHistory(@RequestBody TransVo vo) {
		try {
			List<BxczSignApiLog> results = onlineChangeService.getSignHistoryList(vo);
			processSuccess(results);
		} catch (Exception e) {
			logger.error("Unable to signHistoryList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 查詢聯盟案件歷程
	 *
	 * @return
	 */
	@PostMapping("/onlineChange/getUnionCourseListAllianceStatusMsg")
	public ResponseEntity<ResponseObj> getUnionCourseListAllianceStatusMsg(@RequestBody NotifyOfNewCaseMedicalVo vo) {
		try {
			List<NotifyOfNewCaseMedicalVo> results = onlineChangeService.getUnionCourseListAllianceStatusMsg(vo);
				processSuccess(results);
		} catch (Exception e) {
			logger.error("Unable to getUnionCourseList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}

		return processResponseEntity();
	}



	
	/**
	 * 開始處理
	 * 
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_022, 
			sqlId = EventCodeConstants.ONLINECHANGE_022_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeDispose")
	public ResponseEntity<ResponseObj> getOnlineChangeDispose(@RequestBody TransStatusHistoryVo vo) {
		try {			
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("0");
			transVo.setUpdateUser(getUserId());
	
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
				vo.setStatus("0");
				vo.setCustomerName(getUserId());
				vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				result = onlineChangeService.addTransStatusHistory(vo);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeDispose: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 異常件註記
	 * 
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_022, 
			sqlId = EventCodeConstants.ONLINECHANGE_022_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeReject")
	public ResponseEntity<ResponseObj> getOnlineChangeReject(@RequestBody TransStatusHistoryVo vo) {
		try {
			int num = onlineChangeService.getCaseIDNum(vo.getTransNum());
			if (num > 0) {
				TransVo transVo = new TransVo();
				transVo.setTransNum(vo.getTransNum());
				transVo.setStatus(vo.getStatus());
				transVo.setUpdateUser(getUserId());
				int result = onlineChangeService.updateTransStatus(transVo);
				if (result > 0) {
					processSuccess(result);
					//vo.setStatus("7");
					vo.setCustomerName(getUserId());
					vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
					result = onlineChangeService.addTransStatusHistory(vo);
					// 加入黑名單
					if (ApConstants.REJECT_REASON_01.equals(vo.getRejectReason()) && ApConstants.STATUS_7.equals(vo.getStatus())) {
						result = onlineChangeService.addBlackList(vo);
					}
					// 發送郵件
					onlineChangeService.sendMailTO(vo.getTransNum(), vo.getRejectReason(), vo.getStatus());
				} else {
					processError("更新失敗");
				}
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReject: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 退件
	 * 
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/getOnlineChangeRejection")
	public ResponseEntity<ResponseObj> getOnlineChangeRejection(@RequestBody TransStatusHistoryVo vo) {
		try {
			int num = onlineChangeService.getCaseIDNum(vo.getTransNum());
			if (num > 0) {
				TransStatusHistoryVo hisVo = onlineChangeService.getTransStatusHis(vo);
				processSuccess(hisVo);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReject: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}



	/**
	 * 醫療已完成
	 *
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_022,
			sqlId = EventCodeConstants.ONLINECHANGE_022_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeMedicalCompleted")
	public ResponseEntity<ResponseObj> getOnlineChangeMedicalCompleted(@RequestBody TransStatusHistoryVo vo) {
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("2");
			transVo.setUpdateUser(getUserId());
			/**
			 * 進行驗證當前案件的聯盟狀態是否為結束狀態
			 */
			//查詢聯盟結束狀態碼
			String itpsEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS_ITPS_END);
			String pqhfEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS_PQHF_END);
			String htpsPtis = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS_HTPS_PTIS);
			String itps = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICAL_INTERFACE_STATUS_ITPS);
			String notFinishedWindowMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_NOT_FINISHED_WINDOW_MSG);
			String notApiAllianceWindowMsg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_NOT_APIALLIANCE_WINDOW_MSG);
			//查詢當前保單狀態碼
			String AllianceStatus = onlineChangeService.getTransMedicalTreatmentByAllianceStatus(vo.getTransNum());
			if (!StringUtils.isEmpty(AllianceStatus)) {
				boolean boo = false;
				//1.該聯盟案件狀態需為HTPS_PTIS or ITPS or ITPS_END or PQHF_END
				if (AllianceStatus.equals(itpsEnd) ||AllianceStatus.equals(pqhfEnd)
						||AllianceStatus.equals(htpsPtis) ||AllianceStatus.equals(itps)){
					/**
					 * 當聯盟案件狀態=HTPS_PTIS or ITPS該案件有file_data，
					 * 	需全部已上傳影像系統成功(FILE_BASE64皆有值，且 EZ_ACQUIRE_TASK_ID皆有值)
					 * */
					 if(AllianceStatus.equals(htpsPtis)){
						 //進行查詢當前案件是否已上傳影像系統成功
						 //1.1  獲取當前案件需要上傳影響系統條數
						 int uploadCount= onlineChangeService.getTransMedicalTreatmentByCount(vo.getTransNum());
						 //1.2  獲取當前案件已上傳影像系統成功條數
						 int uploadSuccess= onlineChangeService.getTransMedicalTreatmentBySuccessCount(vo.getTransNum());
						 if(uploadCount>0){
							if(uploadCount==uploadSuccess){
								//呼叫API-406回報案件已完成,並於回傳成功(code=0)後，更新TRANS.STATUS=’2’
								//獲取當前的保單的caseId
								String caseId =onlineChangeService.getTransMedicalTreatmentByCaseId(vo.getTransNum());
								//call api-406 to
								Map<String, String> params = new HashMap<>();
								String transNum = vo.getTransNum();
								params.put("caseId",caseId);
								//聯盟鏈歷程參數
								Map<String, String> unParams = new HashMap<>();
								unParams.put("name", "API-406 已完成案件申請");
								unParams.put("caseId", caseId);
								unParams.put("transNum", transNum);
								//獲取API的請求地址
								String parameterValue = parameterService.getParameterValueByCode(ApConstants.SYSTEM_API_ID, ApConstants.MEDICALALLIANCE_API406_URL);
								APIAllianceRequestVo requestVo = new APIAllianceRequestVo();
								requestVo.setUrl(parameterValue);
								requestVo.setParams(params);
								requestVo.setUnParams(unParams);
								
								//調用API
								ReturnHeader returnHeader = apiAllianceTemplateClient.apiAlliance(requestVo);
								String returnCode = returnHeader.getReturnCode();
								if (ApiResponseObj.SUCCESS.equals(returnCode)) {
									String returnMesg = returnHeader.getReturnMesg();
									if(!StringUtils.isEmpty(returnMesg) && MyJacksonUtil.checkLiaAPIResponseValue(returnMesg,"/code","0")) {
										String msg = MyJacksonUtil.readValue(returnMesg, "/msg");
										TransMedicalTreatmentClaimVo mvo = new TransMedicalTreatmentClaimVo();
										mvo.setAllianceStatus(pqhfEnd);
										//進行回應狀態醫院資料信息描述
										mvo.setAllianceFileStatus(msg);
										onlineChangeService.updateTarnsMedicalTreatmentClaimToAllianceStatus(mvo);
										boo = true;
									}else{
										processError(notApiAllianceWindowMsg+"：呼叫聯盟API-406更新失敗");
									}
								}else{
									//API 調用失敗
									processError(notApiAllianceWindowMsg+"：呼叫聯盟API-406調用失敗");
								}
							}else{
								//有文件但未全部上傳至影像系統
								processError(notApiAllianceWindowMsg+"：有文件但未全部上傳至影像系統");
							}
						}else{
							//有文件但未成功塞入DB
							processError(notApiAllianceWindowMsg+"：有文件但未成功塞入DB");
						}
					 }

					/**
					 * 當聯盟案件狀態為以下，無需呼叫API406,直接更新TRANS.STATUS=2即可
					 * 		2.1.當聯盟案件狀態=ITPS_END，就可能不會有file_data
					 *      2.2.當聯盟案件狀態=PQHF_END，不會有file_data
					 *      2.3.當聯盟案件狀態=ITPS，不允許重覆呼叫API406
					 */
					if(AllianceStatus.equals(itpsEnd) 
							|| AllianceStatus.equals(pqhfEnd)
							|| AllianceStatus.equals(itps)){
						boo = true;
					}
					/***
					 * 進行更新status 狀態
					 */
					if(boo){
						int result = onlineChangeService.updateTransStatus(transVo);
						if (result > 0) {
							processSuccess(result);
							vo.setStatus("2");
							vo.setCustomerName(getUserId());
							vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
							result = onlineChangeService.addTransStatusHistory(vo);
							// 發送郵件
							onlineChangeService.sendMedicalTreatmentMailTO(vo.getTransNum(),ApConstants.INS_CLAIM_COMPLETED ,vo.getStatus());
						} else {
							processError("更新失敗");
						}
					}
				}else{
					processError(notFinishedWindowMsg);
				}
			}else{
				processError(notFinishedWindowMsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to getOnlineChangeComplete: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 已完成
	 * 
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_022, 
			sqlId = EventCodeConstants.ONLINECHANGE_022_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getOnlineChangeCompleted")
	public ResponseEntity<ResponseObj> getOnlineChangeCompleted(@RequestBody TransStatusHistoryVo vo) {
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("2");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
				vo.setStatus("2");
				vo.setCustomerName(getUserId());
				vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				result = onlineChangeService.addTransStatusHistory(vo);
				// 發送郵件
				onlineChangeService.sendMailTO(vo.getTransNum(),ApConstants.INS_CLAIM_COMPLETED ,vo.getStatus());
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeComplete: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 通知補件.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_033, 
			sqlId = EventCodeConstants.ONLINECHANGE_033_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/addTransRFE")
	public ResponseEntity<ResponseObj> addTransRFE(@RequestBody TransRFEVo vo) {
		// 檢查補件流程是否完成
		List<TransRFEVo> rlist = onlineChangeService.getTransRFEList(vo);
		boolean flag = false;
		for (TransRFEVo transRFEVo : rlist) {
			if("NON".equals(transRFEVo.getStatus()) || "WAIT".equals(transRFEVo.getStatus())) {
				flag = true;
				break;
			}
		}
		if(flag) {
			processError("補件流程還未完結，不能發起新的補件。");
			return processResponseEntity();
		}
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("4");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			
			vo.setStatus("NON");
			result = onlineChangeService.addTransRFE(vo);
			
			if (result > 0) {
				processSuccess(result);
				TransStatusHistoryVo transStatusHistoryVo = new TransStatusHistoryVo();
				transStatusHistoryVo.setTransNum(vo.getTransNum());
				transStatusHistoryVo.setStatus("4");
				transStatusHistoryVo.setCustomerName(getUserId());
				transStatusHistoryVo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				transStatusHistoryVo.setContent(vo.getRequestContent());
				result = onlineChangeService.addTransStatusHistory(transStatusHistoryVo);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeRFE: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 狀態歷程.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_034, 
			sqlId = EventCodeConstants.ONLINECHANGE_034_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransStatusHistoryList")
	public ResponseEntity<ResponseObj> getTransStatusHistoryList(@RequestBody TransStatusHistoryVo vo) {
		try {			
			List<TransStatusHistoryVo> result = onlineChangeService.getTransStatusHistoryList(vo);
			if (result != null && result.size() != 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getTransStatusHistoryList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 補件單歷程.
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_035, 
			sqlId = EventCodeConstants.ONLINECHANGE_035_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransRFEList")
	public ResponseEntity<ResponseObj> getTransRFEList(@RequestBody TransRFEVo vo) {
		try {			
			List<TransRFEVo> result = onlineChangeService.getTransRFEList(vo);
			if (result != null && result.size() != 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getTransRFEList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 更新補件單歷程
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/updateTransRFEStatus")
	public ResponseEntity<ResponseObj> updateTransRFEStatus(@RequestBody TransRFEVo vo) {
		try {
			int result = onlineChangeService.updateTransRFEStatus(vo);
			if (result > 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to updateTransRFEStatus: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 保單醫療-通知補件.
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_033,
			sqlId = EventCodeConstants.ONLINECHANGE_033_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/addMedicalTransRFE")
	public ResponseEntity<ResponseObj> addMedicalTransRFE(@RequestBody TransRFEVo vo) {
		// 檢查補件流程是否完成
		List<TransRFEVo> rlist = onlineChangeService.getMedicalTreatmentTransRFEList(vo);
		boolean flag = false;
		for (TransRFEVo transRFEVo : rlist) {
			if("NON".equals(transRFEVo.getStatus()) || "WAIT".equals(transRFEVo.getStatus())) {
				flag = true;
				break;
			}
		}
		if(flag) {
			processError("補件流程還未完結，不能發起新的補件。");
			return processResponseEntity();
		}
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("4");
			transVo.setUpdateUser(getUserId());

			int result = onlineChangeService.updateTransStatus(transVo);

			vo.setStatus("NON");
			result = onlineChangeService.addTransRFE(vo);

			if (result > 0) {
				processSuccess(result);
				TransStatusHistoryVo transStatusHistoryVo = new TransStatusHistoryVo();
				transStatusHistoryVo.setTransNum(vo.getTransNum());
				transStatusHistoryVo.setStatus("4");
				transStatusHistoryVo.setCustomerName(getUserId());
				transStatusHistoryVo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				transStatusHistoryVo.setContent(vo.getRequestContent());
				result = onlineChangeService.addTransStatusHistory(transStatusHistoryVo);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeRFE: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 保單醫療   補件單歷程.
	 *
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_035,
			sqlId = EventCodeConstants.ONLINECHANGE_035_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getMedicalTreatmentTransRFEList")
	public ResponseEntity<ResponseObj> getMedicalTreatmentTransRFEList(@RequestBody TransRFEVo vo) {
		try {
			List<TransRFEVo> result = onlineChangeService.getMedicalTreatmentTransRFEList(vo);
			if (result != null && result.size() != 0) {
				processSuccess(result);
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getMedicalTreatmentTransRFEList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 醫療保單異常件註記彈窗
	 *
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.MEDICAL_TREATMENT_37,
			sqlId = EventCodeConstants.MEDICAL_TREATMENT_37_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getMedicalTreatmentOnlineChangeReject")
	@ResponseBody
	public ResponseEntity<ResponseObj> getMedicalTreatmentOnlineChangeReject(@RequestBody TransStatusHistoryVo vo) {
		try {
			//查詢當前保單序號是否存在為異常案件
			int num = onlineChangeService.getMedicalTreatmentCaseIDNum(vo.getTransNum());
			TransStatusHistoryVo historyVo = new TransStatusHistoryVo();
			if (num > 0) {
				//查詢當前申請序號的追進的狀態并且是為異常狀態7
				 historyVo = onlineChangeService.getTransStatusHis(vo);
			}
			processSuccess(historyVo);
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReject: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 醫療保單標記異常件註記  提交數據信息
	 *
	 * @param TransStatusHistoryVo vo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.MEDICAL_TREATMENT_38,
			sqlId = EventCodeConstants.MEDICAL_TREATMENT_38_SQL_ID,
			daoVoParamKey = "vo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getMedicalTreatmentOnlineChangeRejectSubmit")
	public ResponseEntity<ResponseObj> getMedicalTreatmentOnlineChangeRejectSubmit(@RequestBody TransStatusHistoryVo vo) {
		try {
			int num = onlineChangeService.checkMedicalTreatmentIdNoExist(vo.getTransNum());
			logger.info("=======================================",num);
			if (num == 0) {
				TransVo transVo = new TransVo();
				transVo.setTransNum(vo.getTransNum());
				transVo.setStatus(vo.getStatus());
				transVo.setUpdateUser(getUserId());
				int result = onlineChangeService.updateTransStatus(transVo);
				if (result > 0) {
					processSuccess(result);
					//vo.setStatus("7");
					vo.setCustomerName(getUserId());
					vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
					result = onlineChangeService.addTransStatusHistory(vo);
					String rejectReason = vo.getRejectReason();
					boolean contains=false;
					if (!StringUtils.isEmpty(rejectReason)) {
						//獲取所有醫療異常的數據code
						String  parameterValueList=	parameterService.getParameterValueByCategoryCodeAndSystemId(ApConstants.SYSTEM_ID,ApConstants.MEDICAL_ABNORMAL_REASON_MSG,rejectReason);
						if (!StringUtils.isEmpty(parameterValueList)) {
							contains=true;
						}
					}
					// 加入黑名單
					if (contains && ApConstants.STATUS_7.equals(vo.getStatus())) {
						result = onlineChangeService.addMedicalTreatmentBlackList(vo);
						onlineChangeService.sendMedicalTreatmentMailTO(vo.getTransNum(), vo.getRejectReason(), vo.getStatus());
					}
					// 發送郵件
				} else {
					processError("更新失敗");
				}
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to getOnlineChangeReject: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/***
	 * 查詢申請明細-已持有投資標的轉換查詢
	 * @param transVo
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_036,
			sqlId = EventCodeConstants.ONLINECHANGE_036_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getConversionDetail")
	public String getConversionDetail(@RequestBody TransVo transVo) {
		try {
			addAttribute("detailData", onlineChangeService.getConversionDetail(transVo));
		} catch (Exception e) {
			logger.error("Unable to getConversionDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-conversions";
	}

	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_037,
			sqlId = EventCodeConstants.ONLINECHANGE_037_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getInvestmentDetail")
	public String getInvestmentDetail(@RequestBody TransVo transVo) {
		try {
			final List<String> showAccountInvts = Lists.newArrayList();
			parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID_ESERVICE, "SHOW_ACCOUNT_INVT_NOS")
					.forEach(e -> showAccountInvts.add(e.getParameterValue()));
			addAttribute("showAccountInvts", showAccountInvts);
			addAttribute("detailData", onlineChangeService.getInvestmentDetail(transVo));
		} catch (Exception e) {
			logger.error("Unable to getInvestmentDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-investment";
	}

	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_038,
			sqlId = EventCodeConstants.ONLINECHANGE_038_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getCashPaymentDetail")
	public String getCashPaymentDetail(@RequestBody TransVo transVo) {
		try {
			addAttribute("detailData", onlineChangeService.getCashPaymentDetail(transVo));
		} catch (Exception e) {
			logger.error("Unable to getCashPaymentDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-cashPayment";
	}

	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_039,
			sqlId = EventCodeConstants.ONLINECHANGE_039_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getDepositDetail")
	public String getDepositDetail(@RequestBody TransVo transVo) {
		try {
			Map<String, Object> vo = onlineChangeService.getDepositDetail(transVo);
			addAttribute("detailData", vo);
		} catch (Exception e) {
			logger.error("Unable to getDepositDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-deposit";
	}

	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_040,
			sqlId = EventCodeConstants.ONLINECHANGE_040_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransChangePremiumDetail")
	public String getTransChangePremium(@RequestBody TransVo transVo) {
		try {
			addAttribute("detailData", onlineChangeService.getTransChangePremiumDetail(transVo));
		} catch (Exception e) {
			logger.error("Unable to getTransChangePremiumDetail: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-changePremium";
	}
	
	/**
	 * 聯絡資料變更-失败
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/contactInfoFail")
	public ResponseEntity<ResponseObj> contactInfoFail(@RequestBody TransStatusHistoryVo vo) {
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("6");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
				vo.setStatus("6");
				vo.setCustomerName(getUserId());
				vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				result = onlineChangeService.addTransStatusHistory(vo);
				
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to contactInfoFail: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 聯絡資料變更-已完成
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/contactInfoCompleted")
	public ResponseEntity<ResponseObj> contactInfoComplete(@RequestBody TransStatusHistoryVo vo) {
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("2");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
				vo.setStatus("2");
				vo.setCustomerName(getUserId());
				vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				result = onlineChangeService.addTransStatusHistory(vo);
				
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to contactInfoComplete: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	/**
	 * 死亡除戶-已完成
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/dnsCompleted")
	public ResponseEntity<ResponseObj> dnsCompleted(@RequestBody TransStatusHistoryVo vo) {
		try {
			TransVo transVo = new TransVo();
			transVo.setTransNum(vo.getTransNum());
			transVo.setStatus("2");
			transVo.setUpdateUser(getUserId());
			
			int result = onlineChangeService.updateTransStatus(transVo);
			if (result > 0) {
				processSuccess(result);
				vo.setStatus("2");
				vo.setCustomerName(getUserId());
				vo.setIdentity((String) getSession(ApConstants.LOGIN_USER_ROLE_NAME));
				result = onlineChangeService.addTransStatusHistory(vo);
				
			} else {
				processError("更新失敗");
			}
		} catch (Exception e) {
			logger.error("Unable to contactInfoComplete: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	/**
	 * 导出死亡除戶CSV
	 * 
	 * @param transVo TransVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/onlineChange/exportDnsCSV")
	public ResponseEntity<PageResponseObj> exportDnsCSV(@RequestBody TransVo transVo) {
		PageResponseObj pageResp = new PageResponseObj();
		try {
			pageResp.setAaData(onlineChangeService.getDNS_CSV(transVo));
			pageResp.setResult(PageResponseObj.SUCCESS);
		} catch (Exception e) {
			pageResp.setResult(PageResponseObj.ERROR);
			logger.error("Unable to exportDnsCSV: {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.OK).body(pageResp);
	}
	
	/**
	 * 取得線上申請單筆詳細資料(申請電子通知服務).
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_042, 
			sqlId = EventCodeConstants.ONLINECHANGE_042_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransElectronicFormMethod")
	public String getTransElectronicFormMethod(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransElectronicFormMethod(transVo));
			}
		} catch (Exception e) {
			logger.error("Unable to getTransElectronicFormMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-electronicForm";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(減額繳清保險).
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_043, 
			sqlId = EventCodeConstants.ONLINECHANGE_043_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransDeratePaidOffMethod")
	public String getTransDeratePaidOffMethod(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransDeratePaidOffMethod(transVo));
			}
		} catch (Exception e) {
			logger.error("Unable to getTransDeratePaidOffMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-deratePaidOff";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(展期定期保險).
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_044, 
			sqlId = EventCodeConstants.ONLINECHANGE_044_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransRolloverPeriodicallyMethod")
	public String getTransRolloverPeriodicallyMethod(@RequestBody TransVo transVo) {
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				addAttribute("detailData", onlineChangeService.getTransRolloverPeriodicallyMethod(transVo));
			}				
		} catch (Exception e) {
			logger.error("Unable to getTransRolloverPeriodicallyMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-rolloverPeriodically";
	}
	
	/**
	 * 取得線上申請單筆詳細資料(展期定期保險).
	 * @return
	 */
	@RequestLog
	@EventRecordLog(value = @EventRecordParam(
			eventCode = EventCodeConstants.ONLINECHANGE_045, 
			sqlId = EventCodeConstants.ONLINECHANGE_045_SQL_ID,
			daoVoParamKey = "transVo",
			systemEventParams = {}
			))
	@PostMapping("/onlineChange/getTransContractRevocationMethod")
	public String getTransContractRevocationMethod(@RequestBody TransVo transVo) {
		String IMAGE_HEAD = "data:image/png;base64,";
		try {
			if(ValidateUtils.transIsValid(transVo)) {
				 Map<String , Object> transContractRevocation = onlineChangeService.getTransContractRevocationMethod(transVo);
				 String needsFlag =(String) transContractRevocation.get("NEEDS_FLAG");
				 String economyFlag =(String) transContractRevocation.get("ECONOMY_FLAG");
				 String cognitionFlag =(String) transContractRevocation.get("COGNITION_FLAG");
				 String familyFlag =(String) transContractRevocation.get("FAMILY_FLAG");
				 String otherFlag =(String) transContractRevocation.get("OTHER_FLAG");
				 
					StringBuffer  flagMsg = new StringBuffer();
					if(needsFlag.equals("1")) {
						flagMsg.append("保單規劃不符需求、");				
					}
					if(economyFlag.equals("1")) {
						flagMsg.append("經濟因素、");
					}
					if(familyFlag.equals("1")) {
						flagMsg.append("家人反對、");
					}
					if(cognitionFlag.equals("1")) {
						flagMsg.append("對商品認知有誤、");
					}
					if(otherFlag.equals("1")) {
						flagMsg.append("其他、");
					}
					String msg = flagMsg.substring(0,flagMsg.length()-1);
				 
				String image1 = (String) transContractRevocation.get("IMAGE1");
				if(StringUtils.isNotEmpty(image1)) {
					 transContractRevocation.put("IMAGE1", IMAGE_HEAD + image1);
				}
				
				String image2 = (String) transContractRevocation.get("IMAGE2");
				if(StringUtils.isNotEmpty(image2)) {
					transContractRevocation.put("IMAGE2", IMAGE_HEAD + image2);
				}

					
				 transContractRevocation.put("STATUSFLAG", msg);
				addAttribute("detailData", transContractRevocation	);
			}	
		} catch (Exception e) {
			logger.error("Unable to getTransContractRevocationMethod: {}", ExceptionUtils.getStackTrace(e));
			addDefaultSystemError();
		}
		return "backstage/rpt/onlineChangeDetail-contractRevocation";
	}

	@RequestLog
	@PostMapping(value = "/downloadSignPdf")
	public @ResponseBody HttpEntity<byte[]> downloadSignPdf(@RequestParam("signFileId") String signFileId) {
		byte[] document = null;
		HttpHeaders header = new HttpHeaders();
		try {
			document = onlineChangeService.getSignPdf(signFileId);
			if (document != null) {
				String fileName = String.format("inline; filename=數位簽署文件-%s.pdf", DateUtil.getRocDate(new Date()));
				header.setContentType(new MediaType("application", "pdf"));
				header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
				header.setContentLength(document.length);
			}
		} catch (Exception e) {
			logger.error("Unable to get data from downloadPolicyClaimPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return new HttpEntity<byte[]>(document, header);
	}

	@RequestLog
	@PostMapping(value = "/onlineChange/updatePolicyClaimTransApplyDate")
	@ResponseBody
	public void updatePolicyClaimTransApplyDate(@RequestBody TransVo vo) {
		try {
			onlineChangeService.updatePolicyClaimApplyDate(vo.getTransNum());
		} catch (Exception e) {
			logger.error("Unable to updatePolicyClaimTransApplyDate: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	@RequestLog
	@PostMapping(value = "/onlineChange/updateMedicalTransApplyDate")
	@ResponseBody
	public void updateMedicalTransApplyDate(@RequestBody TransVo vo) {
		try {
			onlineChangeService.updateMedicalTreatmentApplyDate(vo.getTransNum());
		} catch (Exception e) {
			logger.error("Unable to updateMedicalTransApplyDate: {}", ExceptionUtils.getStackTrace(e));
		}
	}

}
