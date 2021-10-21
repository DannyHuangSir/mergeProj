package com.twfhclife.scheduling.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.alliance.model.*;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.alliance.service.IMedicalService;
import com.twfhclife.alliance.service.impl.MedicalServiceImpl;
import com.twfhclife.alliance.service.impl.MedicalTreatmentExternalServiceImpl;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import com.twfhclife.eservice.onlineChange.service.*;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import com.twfhclife.eservice.web.model.HospitalVo;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.service.MailService;
import com.twfhclife.generic.service.SmsService;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.CallApiCode;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.StatuCode;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
@Component
public class MedicalAllianceServiceTask {

    Log log = LogFactory.getLog(MedicalAllianceServiceTask.class);

    public static final String CODE_SUCCESS = "0";

    public static final String MSG_SUCCESS = "SUCCESS";


    @Autowired
    IClaimChainService claimChainService;

    @Autowired
    IExternalService allianceService;

    @Value("${upload.file.save.path}")
    private String FILE_SAVE_PATH;

    @Autowired
    ITransService transService;

    @Autowired
    ITransAddService transAddService;

    @Autowired
    ILilipmService iLilipmService;

    @Autowired
    IHospitalServcie iHospitalServcie;

    @Autowired
    IHospitalInsuranceCompanyServcie iHospitalInsuranceCompanyServcie;

	//@Value("${medicalAlliance.api401.url}")
    public String URL_API401;

    //@Value("${medicalAlliance.api402.url}")
    public String URL_API402;

    //@Value("${medicalAlliance.api403.url}")
    public String URL_API403;

    //@Value("${medicalAlliance.api404.url}")
    public String URL_API404;

    //@Value("${medicalAlliance.api405.url}")
    public String URL_API405;

    //@Value("${medicalAlliance.api406.url}")
    public String URL_API406;
    
    //@Value("${medicalAlliance.api407.url}")
    public String URL_API407;
    
    //@Value("${medicalAlliance.api408.url}")
    public String URL_API408;
    
    //@Value("${cron.api.disable}")
    public String API_DISABLE;

    @Autowired
    @Qualifier("apiParameterService")
    private IParameterService parameterService;

    @Autowired
    private IMedicalTreatmentService iMedicalTreatmentService;
    
    @Autowired
    private MedicalTreatmentExternalServiceImpl medicalExternalServiceImpl;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private IMedicalService iMedicalService;
    
    /***
     * 獲取API對於的數據信息信息
     */
    @PostConstruct
    public void doMedicalAllianceServiceTask() {
        List<ParameterVo> resultSCHList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_MEDICAL_SCH);
        List<ParameterVo> resultURLList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_MEDICAL_API_URL);
        List<ParameterVo> resultBASELList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_PRO_BASE_URL);
        if (resultBASELList != null) {
            resultBASELList.forEach(parameterItem ->{
                if ("alliance.api.accessToken".equals(parameterItem.getParameterName())) {
                	medicalExternalServiceImpl.setACCESS_TOKEN(parameterItem.getParameterValue());
                }
            });
        }
        if (resultURLList != null) {
            resultURLList.forEach(parameterItem -> {
                if ("medicalAlliance.api401.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API401(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api402.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API402(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api403.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API403(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api404.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API404(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api405.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API405(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api406.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API406(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api407.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API407(parameterItem.getParameterValue());
                }
                if ("medicalAlliance.api408.url".equals(parameterItem.getParameterName())) {
                    this.setURL_API408(parameterItem.getParameterValue());
                }
            });
        }
        if (resultSCHList != null) {
            resultSCHList.forEach(parameterItem -> {
                if ("cron.api.medical.disable".equals(parameterItem.getParameterName())) {
                    this.setAPI_DISABLE(parameterItem.getParameterValue());
                }else {
                    if (System.getProperty(parameterItem.getParameterName()) == null) {
                        System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
                    }
                }
            });
        }
    }

    /**
     * cron.saveToMedicalTrans.expression
     * 數據轉存到MEDICAL_TREATMENT_CLAIM表中
     */
    @Scheduled(cron = "${cron.saveTransToMedical.expression}")
    public  void  saveTransToMedical(){
        log.info("Start saveTransToMedical.");
        log.info("API_DISABLE="+API_DISABLE);

        if("N".equals(API_DISABLE)){
            try {
                int listVo = iMedicalService.getTransMedicalTreatmentBySendAlliance();
            }catch(Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }

        log.info("End saveTransToMedical.");

    }



    /**
     * cron.saveToMedicalTrans.expression
     * 數據轉存到TRANS/TRANS_POLICY/TRANS_MEDICAL_CLAIM_XXX表中
     */
    @Scheduled(cron = "${cron.saveToMedicalTrans.expression}")
    public void saveToMedicalTrans(){
        log.info("Start saveToMedicalTrans.");
        log.info("API_DISABLE="+API_DISABLE);

        if("N".equals(API_DISABLE)){
            try {
                List<MedicalTreatmentClaimVo> listVo = iMedicalService.getMedicalTreatmentClaimVoBySeqIdNoTransNum();
                if(listVo!=null && !listVo.isEmpty()) {
                    TransMedicalTreatmentClaimVo transMedicalTreatmentClaimVo=null;
                    for(MedicalTreatmentClaimVo vo : listVo) {
                    	try {//避免一有錯誤就整批都不做
                    		String idNo = vo.getIdNo();
                            // 判斷是否為保戶
                            int countPIPM = iLilipmService.getInsuredUsersByRocId(idNo);
                            log.info("判斷是否為保戶,count="+countPIPM);
                            if(countPIPM>0) {//保戶
                                //取userId
                                String userId = null;
                                userId = claimChainService.getUserIdByIdNo(idNo);
                                if(userId!=null) {
                                    //有註冊eservice用戶
                                    //do nothing.
                                }else {
                                    //未註冊eservice用戶
                                    userId = "EMPTY";
                                }

                                //2.新增保單號碼
                                List<String> insClaimsPlans = iMedicalTreatmentService.getInsClaimPlan();

                                StringBuffer policyBuff = new StringBuffer("");

                                List<String> policyNos = allianceService.getPolicyNoByID(idNo);
                                if(policyNos!=null && policyNos.size()>0) {
                                    for(String policyNo : policyNos) {
                                        log.info("MedicalAllianceServiceTask get policyNo="+policyNo);
                                        if(policyNo!=null && StringUtils.isNotEmpty(policyNo.trim())) {
                                            List<String> prodCodes = allianceService.getProductCodeByPolicyNo(policyNo);
                                            for(String prodCode : prodCodes) {
                                                log.info("MedicalAllianceServiceTask get prodCode="+prodCode);
                                                if(insClaimsPlans.contains(prodCode)) {
                                                    policyBuff.append(policyNo).append(",");
                                                    break;
                                                }
                                            }
                                        }//end-if
                                    }//end-for
                                }//end-if

                                if(policyBuff!=null && policyBuff.length()>0) {//移除最後一個逗號
                                    policyBuff.deleteCharAt(policyBuff.length() - 1);
                                }
                                log.info("MedicalAllianceServiceTask get policyBuff="+policyBuff.toString());
                                //此時若沒有保單號碼，故不做
                                //3.新增線上申請保單醫療
                                transMedicalTreatmentClaimVo = new TransMedicalTreatmentClaimVo();
                                BeanUtils.copyProperties(vo,transMedicalTreatmentClaimVo);
                                
                                //設定交易序號
    							String transNum = transService.getTransNum();
    							transMedicalTreatmentClaimVo.setTransNum(transNum);

                                transMedicalTreatmentClaimVo.setUserId(userId);
                                transMedicalTreatmentClaimVo.setPolicyNo(policyBuff.toString());
                                String fromCompanyId = vo.getFromCompanyId();
                                String toCompanyId   = vo.getToCompanyId();
                                transMedicalTreatmentClaimVo.setFrom(fromCompanyId);
                                transMedicalTreatmentClaimVo.setTo(toCompanyId);
                                
                                //3.1.塞好FILEDATAS
                                List<MedicalTreatmentClaimFileDataVo> fileDatas = vo.getFileData();
                                if(fileDatas!=null && fileDatas.size()>0) {
                                    List<TransMedicalTreatmentClaimFileDataVo> transFileDatas = new ArrayList<TransMedicalTreatmentClaimFileDataVo>();
                                    for(MedicalTreatmentClaimFileDataVo ivo : fileDatas) {
                                        TransMedicalTreatmentClaimFileDataVo tvo = new TransMedicalTreatmentClaimFileDataVo();
                                        tvo.setFileName(ivo.getFileName());
                                        tvo.setFilePath(this.FILE_SAVE_PATH);
                                        tvo.setType(ivo.getType());
                                        tvo.setFileBase64(ivo.getFileBase64());
                                        tvo.setFileId(ivo.getFileId());
                                        tvo.setFileStatus(ivo.getFileStatus());
                                        transFileDatas.add(tvo);
                                    }
                                    transMedicalTreatmentClaimVo.setFileDatas(transFileDatas);
                                }//end-if

                                //3.2.call addTransRequest()
                                int i = iMedicalService.addTransRequest(transMedicalTreatmentClaimVo);//這裡會主動先塞好TRANS Table.
                                if(i>0) {
                                    //4.更新該筆Medical_Claim,表示已送到eservice.TRANS
                                    if(transNum!=null) {
                                        vo.setTransNum(transNum);
                                        vo.setStauts(CallApiCode.SAVE_TRANS);
                                        int rtn = iMedicalService.updateMedicalTreatmentClaimToStatusTransNumByCaseId(vo);
                                    }

                                }

                            }else {//非保戶
                                vo.setStauts("非保戶");
                                int rtn = iMedicalService.updateMedicalTreatmentClaimToStatusTransNumByCaseId(vo);
                            }
                    	}catch(Exception e) {
                    		log.info(e.toString());
                    	}
                    }//end-for
                }//end-if

            }catch(Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }

        log.info("End saveToMedicalTrans.");

    }

    /**
     * API-401理賠申請上傳
     */
     @Scheduled(cron = "${cron.medical401.expression}")
    public void callAPI401() {
        log.info("-----------Start API-401 Task.-----------");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            try {
                //1.取得醫療申請上傳資料
                List<MedicalTreatmentClaimVo> listMedical = iMedicalService.getMedicalTreatmentByNoCaseId();
                //2.call api-401
                if(listMedical!=null && !listMedical.isEmpty() && listMedical.size()>0) {
                    log.info("取得醫療申請上傳資料條數="+listMedical.size());
                    for (MedicalTreatmentClaimVo vo : listMedical) {
                    	try {
                            if(vo!=null) {
                                //3.call api-401 to upload.
                                String strResponse = medicalExternalServiceImpl.postForEntity(URL_API401, vo, "API-401理賠申請上傳");
                                //String  strResponse="{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"caseId\":\"20210125153001-45c17f68e615-L01\"}}";
                                log.info("call URL_API401,strResponse="+strResponse);
                                
                                //3-1.get api-401 response, update caseId, fileId to db.
                                if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
                                    String caseId = MyJacksonUtil.readValue(strResponse, "/data/caseId");
                                    log.info("caseId="+caseId);
                                    String msg = MyJacksonUtil.readValue(strResponse, "/msg");
                                    vo.setCaseId(caseId);
                                    //更新TRANS_MEDICAL_TREATMENT_CLAIM  與  MEDICAL_TREATMENT_CLAIM
                                    //進行回應CaseId  與  allianceStatus
                                    //進行獲取聯盟的狀態信息
                                    String parameterValueByCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_FTPS_PQHG);
                                    vo.setAllianceStatus(parameterValueByCode);
                                    log.info("updateMedicalTreatmentClaimToAlliance 的參數-----="+vo);
                                    int  i=iMedicalService.updateMedicalTreatmentClaimToAlliance(vo);
                                    //進行將CASE_ID 進行回壓,便於首家案件更新狀態,執行跑403
                                    if(i>1){
                                        claimChainService.addNotifyOfNewCaseMedicalIsPrice(vo);
                                    }
                                }

                            }
                    	}catch(Exception e) {
                    		log.info(e.toString());
                    	}
                    	
                    }//end-for

                }//end-if
            } catch (Exception e) {
                log.error(e);
            }
            log.info("-----------End API-401 Task.-----------");
        }
    }

    /**
     * API-402 回覆是否向醫院取資料
     */
    @Scheduled(cron = "${cron.medical402.expression}")
	public void callAPI402() {
    	log.info("-----------Start API-402 Task.-----------");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            try {
                //1.取得醫療申請否向醫院取資料數據信息
                        //1.1獲取是否取用/不取醫院資料code  eservice_api
                String itpsPthg = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_ITPS_PTHG);
                String itpsEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_ITPS_END);
                List<MedicalTreatmentClaimVo> listMedical = iMedicalService.getTransMedicalTreatmentByCaseIdAndAllinaceStatus(itpsPthg,itpsEnd);
                //2.call api-402
                if(listMedical!=null && !listMedical.isEmpty() && listMedical.size()>0) {
                    for (MedicalTreatmentClaimVo vo : listMedical) {
                        if(vo!=null) {
							// 3.call api-402 to
							/*
							 * Y=取資料 N=不取資料
							 */
							String action;
							if (itpsPthg.equals(vo.getAllianceStatus())) {
								action = StatuCode.ACTION_CODE_Y.code;
							} else {
								action = StatuCode.ACTION_CODE_N.code;
							}
                            Map<String, String> params = new HashMap<>();
                            String caseId = vo.getCaseId();
                            String transNum = vo.getTransNum();
                            params.put("caseId",caseId);
                            params.put("action",action);
                            //聯盟鏈歷程參數
                            Map<String, String> unParams = new HashMap<>();
                            unParams.put("name", "API-402回覆是否向醫院取資料");
                            unParams.put("caseId", caseId);
                            unParams.put("transNum", transNum);

                            String strResponse = medicalExternalServiceImpl.postForEntity(URL_API402, params, unParams);
                            //String strResponse = "{\"code\":\"0\",\"msg\":\"success\"}";
                            log.info("call URL_API402,strResponse="+strResponse);
                            
                            //3-1.get api-402 response
                            if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
                                String msg = MyJacksonUtil.readValue(strResponse, "/msg");
                                //進行回應狀態醫院資料信息描述
                                vo.setAllianceFileStatus(msg);
                                iMedicalService.updateTarnsMedicalTreatmentClaimToAllianceFileStatus(vo);
                            }

                        }
                    }

                }

            } catch (Exception e) {
                log.error(e);
            }
            log.info("-----------End API-402 Task.-----------");
        }
    }
   
   /**
    * API-403 查詢案件資訊
    * 1.全新案件：儲存至MEDICAL_TREAMENT_XXX
    * 2.非全新案件：更新TRANS_MEDICAL_TREATMENT.ALLIANCE_STATUS
    */
	@Scheduled(cron = "${cron.medical403.expression}")
	public void callAPI403() {
        log.info("-----------Start API-403 Task.-----------");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            try {
                //1.查詢案件資訊資料
                //1.1 查詢案件的CaseId未取得查詢理賠資料
            	List<NotifyOfNewCaseMedicalVo> notifyList = iMedicalService.getNotifyOfNewCaseMedicalByNcStatus(NotifyOfNewCaseMedicalVo.STATUS_DEFAULT);
                //2.call api-403
                if(notifyList!=null && !notifyList.isEmpty() && notifyList.size()>0) {
                    for (NotifyOfNewCaseMedicalVo vo : notifyList) {
                        if(vo!=null) {
                            //3.call api-403 to
                            Map<String, String> params = new HashMap<>();
                            String caseId = vo.getCaseId();
                            params.put("caseId",caseId);
                            //聯盟鏈歷程參數
                            Map<String, String> unParams = new HashMap<>();
                            unParams.put("name", "API-403 查詢案件資訊");
                            unParams.put("caseId", caseId);

                            /**
                             * 獲取到transNum數據
                             * 1.新案件進行創建
                             * 2.已有的案件進行查詢出
                             */
                            boolean isAllNewCase = false;//用於判斷是否為全新案件
                            MedicalTreatmentClaimVo medicalTCVo = iMedicalService.getMedicalTreatmentByCaseId(caseId);
                            if(medicalTCVo==null) {
                            	isAllNewCase = true;
                            } else {
                            	//非全新案件，則以查得的STATUS更新TRANS_MEDICAL_TREATMENT_CLAIM.ALLIANCE_STATUS
                            	//更新成功,則此NotifyOfNewCaseMedicalVo.NC_STATUS更新為1
                            	//更新不成功則忽略，下次重作
                            }
                            
                            /**
                             * 此時不一定有TRANS_NUM,因為可能尚未轉到TRANS
                             */
                            String transNum = iMedicalService.getTransMedicalTreatmentByCaseId(caseId);
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(transNum)) {
                                unParams.put("transNum", transNum);
                            }else{
                                //do not create new TRANS_NUM here.
                            }

                            String strResponse = medicalExternalServiceImpl.postForEntity(URL_API403, params, unParams);
                            // strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"hpUid\":\"202101030900001-JQ\",\"idNo\":\"A123456789\",\"dtype\":\"Receipt,CertificateDiagnosis\",\"name\":\"王⼤明\",\"birdate\":\"19910415\",\"hpId\":\"0101090517\",\"cpoa\":\"本⼈王⼤明/A123456789…\",\"hsTime\":\"20210120\",\"heTime\":\"20210125\",\"phone\":\"0912345678\",\"zipCode\":\"70157\",\"address\":\"台北市中正區信義路⼀段 21-3 號\",\"mail\":\" abc@test.com.tw \",\"paymentMethod\":\"1\",\"bankCode\":\"004\",\"branchCode\":\"0107\",\"bankAccount\":\"12345678901234\",\"applicationDate\":\"20190105\",\"applicationTime\":\"1520\",\"applicationItem\":\"1\",\"job\":\"老師\",\"jobDescr\":\"⼯作的描述內容\",\"accidentDate\":\"20190101\",\"accidentTime\":\"1520\",\"accidentCause\":\"disease\",\"accidentLocation\":\"台北市中正區信義路⼀段\",\"accidentDescr\":\"遭計程⾞追撞\",\"policeStation\":\"臺北市政府警察局⼤安分局安和路派出所\",\"policeName\":\"王⼩明\",\"policePhone\":\"0987654321\",\"policeDate\":\"20190101\",\"policeTime\":\"1530\",\"stauts\":\"ITPS\",\"to\":\"L01,L02,L03\",\"from\":\"L01\",\"fromData\":{\"from\":\"L01\",\"status\":\"PTIG\"},\"toData\":[{\"to\":\"L02\",\"status\":\"PTHS\"},{\"to\":\"L03\",\"status\":\"PTHS\"}],\"fileData\":[{\"dtype\":\"CertificateDiagnosis\",\"fileId\":\"45c17f68e615-L02-c-123\",\"fileStatus\":\"HAS_FILE\"},{\"dtype\":\"CertificateDiagnosis\",\"fileId\":\"45c17f68e615-L02-c-133\",\"fileStatus\":\"NO_FILE\"},{\"dtype\":\"Receipt\",\"fileId\":\"45c17f68e615-L02-c-789\",\"fileStatus\":\"RE_FILE\"}]}}";
                            log.info("call URL_API403,strResponse="+strResponse);

                            if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {
                                String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                                ObjectMapper objectMapper = new ObjectMapper();
                                JsonNode rootNode = objectMapper.readTree(dataString);
                                dataString = rootNode.toString();
                                //如有時間進行格式化時間
                                //Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                                MedicalTreatmentClaimVo medicalVo = new Gson().fromJson(dataString, new TypeToken<MedicalTreatmentClaimVo>() {
                                }.getType());
                                log.info("after new Gson().fromJson................."+medicalVo);

                                if (medicalVo != null) {
                                	medicalVo.setCaseId(caseId);
                                    /**
                                     * 判斷是否台銀該接收的資料-start
                                     * 1.fromData.from=L01時,表示為台銀首家件,只取用status做為更新聯盟案件狀態
                                     * 2.toData.to不含L01時,表示並非傳送給台銀,此案件不落地
                                     */
                                    //fromData
                                    String fromValue = this.getFromDataToValue(strResponse);
                                    String fromDataStatus = this.getFromDataStatus(strResponse);
                                    
                                    //toData
                                    String toValue = this.getToDataToValue(strResponse);
                                    String allianceStatus = this.getToDataStatus(strResponse);
                                    
                                    if("L01".equals(fromValue)) {//台銀首家件資料
                                    	isAllNewCase = false;
                                    	medicalVo.setAllianceStatus(fromDataStatus);
                                    }else {//非台銀首家件資料
                                    	if(StringUtils.isNotBlank(toValue) && toValue.indexOf("L01")>=0) {
                                    		//do nothing.
                                    	}else {
                                    		//非傳送給台銀的案件,此案件不落地
                                    		vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
                                            vo.setMsg(NotifyOfNewCaseChangeVo.MSG_NOT_FOR_TWFHCLIFE);
                                            int updateCnt = iMedicalService.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
                                            log.info("NotifyOfNewCaseChangeVo.MSG_NOT_FOR_TWFHCLIFE,caseId="+vo.getCaseId());
                                            continue;
                                    	}
                                    }
                                    /**
                                     * 判斷是否台銀該接收的資料-end
                                     */
                                    
                                    //ID在本公司僅為要保人視為非本公司保戶方式，不接收資料)
                                    List<String> policyNos = allianceService.getPolicyNoByID(medicalVo.getIdNo());
                                    boolean isInsured = false;//是否為保戶
                                    if(policyNos!=null && policyNos.size()>0) {//保戶
                                        isInsured = true;
                                    }
                                    log.info("=================API403-查詢查詢案件資訊-當前是否為保戶:"+isInsured);
                                    
                                    //修正filename,path可能的錯誤-start
                                    if(medicalVo.getFileData()!=null && medicalVo.getFileData().size()>0) {
                                    	for(MedicalTreatmentClaimFileDataVo mtcfdVo : medicalVo.getFileData()) {
                                    		//path
                                    		if(StringUtils.isBlank(mtcfdVo.getPath())) {
                                    			mtcfdVo.setPath(this.FILE_SAVE_PATH);
                                    		}
                                    		
                                    		//filename
                                    		String fileId = mtcfdVo.getFileId();
                                    		if(StringUtils.isBlank(mtcfdVo.getFileName())) {
                                    			mtcfdVo.setFileName(fileId);//default set fileId
                                    		}
                                    		
                                    		String fileExtension = FilenameUtils.getExtension(mtcfdVo.getFileName());
                                            if(StringUtils.isBlank(fileExtension)) {
                                            	mtcfdVo.setFileName(mtcfdVo.getFileName()+".pdf");
                                            }
                                    	}
                                    }
                                    //修正filename可能的錯誤-end

                                    if(isAllNewCase) {
                                    	if(isInsured){
                                            /**
                                             * 存儲數據的組合
                                             */
                                            medicalVo.setFromCompanyId(medicalVo.getFrom());
                                            medicalVo.setToCompanyId(medicalVo.getTo());
                                            medicalVo.setAuthorizationEndDate(medicalVo.getHeTime());
                                            medicalVo.setAuthorizationStartDate(medicalVo.getHsTime());
                                            medicalVo.setToHospitalId(medicalVo.getHpId());
                                            
                                            medicalVo.setFileData(medicalVo.getFileData());

                                            //toData
                                            //String toValue = this.getToDataToValue(strResponse);
                                            medicalVo.setToCompanyId(toValue);
                                            //String allianceStatus = getToDataStatus(strResponse);
                                            medicalVo.setAllianceStatus(allianceStatus);
                                            
                                            //fromData
                                            //String fromValue = this.getFromDataToValue(strResponse);
                                            medicalVo.setFromCompanyId(fromValue);

                                            /**
                                             * disease=疾病,1
                                             * accident=意外,2
                                             */
                                            String accidentCause = medicalVo.getAccidentCause();
                                            if (!org.springframework.util.StringUtils.isEmpty(accidentCause)) {
                                                if ("disease".equals(accidentCause)) {
                                                    medicalVo.setAccidentCause("1");
                                                }else if("accident".equals(accidentCause)) {
                                                    medicalVo.setAccidentCause("2");
                                                }else {
                                                	log.info("Can't judge accidentCause="+accidentCause);
                                                }
                                            }

                                            // "1":診斷證明書，"2":收據
                                            String dtype = medicalVo.getDtype();
                                            if (!org.springframework.util.StringUtils.isEmpty(dtype)) {
                                                String[] split = dtype.split(",");
                                                StringBuffer stringBuffer = new StringBuffer();
                                                for (int i = 0; i < split.length; i++) {
                                                    String s = split[i];
                                                    if ("CertificateDiagnosis".equals(s)) {
                                                        stringBuffer.append("1,");
                                                    }
                                                    if ("Receipt".equals(s)) {
                                                        stringBuffer.append("2,");
                                                    }
                                                }
                                                medicalVo.setAuthorizationFileType(stringBuffer.toString());
                                            }
                                            
                                            String pqhsPtis = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_PQHS_PTIS);
                                            //案件資訊的回傳狀態為PQHS_PTIS時,新的轉收件存入後申請狀態為申請中
                                            if(medicalVo.getAllianceStatus() !=null && pqhsPtis.equals(medicalVo.getAllianceStatus())){
                                                medicalVo.setStauts("-1");
                                            }
                                            
                                            /**
                                             * 進行存儲數據
                                             */
                                            int iRtn = iMedicalService.addMedicalRequest(medicalVo);
                                            //更新是否已經取得資料
                                            if(iRtn>0) {//如果有查詢且儲存成功
                                                vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
                                                vo.setMsg(NotifyOfNewCaseChangeVo.SUCCESS_MSG);
                                                iMedicalService.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
                                                log.info("狀態已修改為保戶");
                                            }

                                        } else {//非保戶
                                        	vo.setNcStatus(NotifyOfNewCaseVo.NC_STATUS_ONE);
                                            vo.setMsg(NotifyOfNewCaseVo.MSG);
                                            iMedicalService.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
                                            //匯報402接口
                                            params.put("action", StatuCode.ACTION_CODE_N.code);
                                            //聯盟鏈歷程參數
                                            unParams.put("name", "API-402不取醫療資料");
                                             medicalExternalServiceImpl.postForEntity(URL_API402, params, unParams);
                                        }//end-if(isInsured)
                                    }else {
                                    	//非全新案件
                                    	if(StringUtils.isNotBlank(transNum)) {//台銀首家件且已有CASEID故transNum不會為空
                                    		//進行更新最新的狀態信息數據
                                            //設置默認的地址
                                    		int iRtn = iMedicalService.updateTransMedicalTreatmentByCaseId(medicalVo,this.FILE_SAVE_PATH);

                                            //更新是否已經取得資料
                                            if(iRtn>0) {//如果有查詢且儲存成功
                                                vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
                                                vo.setMsg(NotifyOfNewCaseChangeVo.SUCCESS_MSG+",update ALLIANE_STATUS="+medicalVo.getAllianceStatus());
                                                iMedicalService.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
                                                log.info("案件／檔案狀態更新結束");
                                            }else {
                                            	//狀態未更新成功，則忽略此案件下次重作
                                            	log.info("案件／檔案狀態更新結束未更新成功，則忽略此案件下次重作,seqId="+vo.getSeqId());
                                            }
                                    	}else {
                                    		vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
                                            vo.setMsg(NotifyOfNewCaseChangeVo.MSG_TWFHCLIFE_NO_THIS_CASE);
                                            iMedicalService.updateNotifyOfNewCaseMedicalNcStatusBySeqId(vo);
                                            log.info("台銀首家件查無此案件,caseId="+vo.getCaseId());
                                    	}
                                    	
                                    }
 
                                } else {
                                    log.info("API403-查詢查詢案件資訊-轉換數據  medicalVo is null.");
                                }
                            }else{
                                log.info("API403-查詢查詢案件資訊-失敗 ");
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
            log.info("-----------End API-403 Task.-----------");
        }
    }


    /**
     * API-404 檔案下載
     * */
    @Scheduled(cron = "${cron.medical404.expression}")
    public void callAPI404() {
        log.info("Start API-404 Task.");
        log.info("API_DISABLE="+API_DISABLE);
        if("N".equals(API_DISABLE)){
            try {
                List<MedicalTreatmentClaimFileDataVo> listMedical = iMedicalService.getTransMedicalTreatmentClaimFileData( CallApiCode.MEDICAL_INTERFACE_HAS_FILE);
                //2.call api-404
                if(listMedical!=null && !listMedical.isEmpty() && listMedical.size()>0) {
                    for (MedicalTreatmentClaimFileDataVo vo : listMedical) {
	                                //3.call api-404 to
	                                Map<String, String> params = new HashMap<>();
	                                String caseId = vo.getCaseId();
	                                String transNum = vo.getTransNum();
	                                params.put("fileId",vo.getFileId());
	                                //聯盟鏈歷程參數
	                                Map<String, String> unParams = new HashMap<>();
	                                unParams.put("name", "API-404 檔案下載");
	                                unParams.put("caseId", caseId);
	                                unParams.put("transNum", transNum);

	                                String strResponse = medicalExternalServiceImpl.postForEntity(URL_API404, params, unParams);
	                                //String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"content\":\"kbiefw3i8n3oi493nf…\"}}";
	                                if(strResponse!=null) {
	                                	//do not print base64 string
	                                	log.info("call URL_API404,strResponse is not null.");
	                                }else {
	                                	log.info("call URL_API404,strResponse is null.");
	                                }
	                                
		                            //3-1.get api-404 response
		                            if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
		                                String content = MyJacksonUtil.readValue(strResponse, "/data/content");
                                        vo.setFileStatus(CallApiCode.MEDICAL_INTERFACE_HAS_FILE);
                                        vo.setFileBase64(content);
		                                //進行回應狀態
		                                iMedicalService.updateTarnsMedicalTreatmentFileDataStatus(vo);
                        }
                    }
                }
            }catch(Exception e) {
                //e.printStackTrace();
                log.info(e);
            }
        }
        log.info("End API-404 Task.");

    }

    /**
     * API-405 申請醫療資料重新上傳
     * */
    @Scheduled(cron = "${cron.medical405.expression}")
    public void callAPI405() {
        log.info("Start API-405 Task.");
        log.info("API_DISABLE="+API_DISABLE);
        if("N".equals(API_DISABLE)){
            try {
                String pqhfEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_ITPR);
                List<MedicalTreatmentClaimVo> listMedical = iMedicalService.getTransMedicalTreatmentAgainUploadFileid(pqhfEnd);
                //2.call api-405
                if(listMedical!=null && !listMedical.isEmpty() && listMedical.size()>0) {
                    for (MedicalTreatmentClaimVo vo : listMedical) {
                        List<MedicalTreatmentClaimFileDataVo> fileDatas = vo.getFileData();
                        if(fileDatas!=null && !fileDatas.isEmpty() && fileDatas.size()>0) {
                            for (MedicalTreatmentClaimFileDataVo fileDataVo : fileDatas) {
                                if(vo!=null && fileDataVo !=null) {
                                    //3.call api-405 to
                                    Map<String, String> params = new HashMap<>();
                                    String caseId = vo.getCaseId();
                                    String transNum = vo.getTransNum();
                                    params.put("fileId",fileDataVo.getFileId());
                                    params.put("reason",vo.getReUpload());
                                    //聯盟鏈歷程參數
                                    Map<String, String> unParams = new HashMap<>();
                                    unParams.put("name", "API-405 申請醫療資料重新上傳");
                                    unParams.put("caseId", caseId);
                                    unParams.put("transNum", transNum);

                                    String strResponse = medicalExternalServiceImpl.postForEntity(URL_API405, params, unParams);
                                    //String strResponse = "{\"code\":\"0\",\"msg\":\"success\"}";
                                    log.info("call URL_API405,strResponse="+strResponse);
                                    //3-1.get api-404 response
                                    if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
                                        fileDataVo.setFileStatus(CallApiCode.MEDICAL_INTERFACE_RE_FILE);
                                        fileDataVo.setFileBase64("");
                                        //進行回應狀態
                                        int i = iMedicalService.updateTarnsMedicalTreatmentFileDataStatus(fileDataVo);
                                    }

                                }
                            }
                            int i = iMedicalService.updaetNotifyOfNewCaseMedicalStatus(vo.getCaseId(),vo.getTransNum());
                        }
                    }
                }
            }catch(Exception e) {
                //e.printStackTrace();
                log.info(e);
            }
        }
        log.info("End API-405 Task.");

    }

    /**
     *  API-406 已完成案件申請
     * */
    @Scheduled(cron = "${cron.medical406.expression}")
    public void callAPI406() {
        log.info("Start API-406 Task.");
        log.info("API_DISABLE="+API_DISABLE);
        if("N".equals(API_DISABLE)){
            try {
                String pqhfEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_PQHF_END);
                String itpsEnd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, CallApiCode.MEDICAL_INTERFACE_STATUS_ITPS_END);
                List<MedicalTreatmentClaimVo> listMedical = iMedicalService.getTransMedicalTreatmentByAllinaceStatus(pqhfEnd,itpsEnd);

                //2.call api-406
                if(listMedical!=null && !listMedical.isEmpty() && listMedical.size()>0) {
                    for (MedicalTreatmentClaimVo vo : listMedical) {
                        if(vo!=null) {
                            //3.call api-406 to
                            Map<String, String> params = new HashMap<>();
                            String caseId = vo.getCaseId();
                            String transNum = vo.getTransNum();
                            params.put("caseId",caseId);
                            //聯盟鏈歷程參數
                            Map<String, String> unParams = new HashMap<>();
                            unParams.put("name", "API-406 已完成案件申請");
                            unParams.put("caseId", caseId);
                            unParams.put("transNum", transNum);

                            String strResponse = medicalExternalServiceImpl.postForEntity(URL_API406, params, unParams);
                            //String strResponse = "{\"code\":\"0\",\"msg\":\"success\"}";
                            log.info("call URL_API406,strResponse="+strResponse);
                            //3-1.get api-402 response
                            if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
                                String msg = MyJacksonUtil.readValue(strResponse, "/msg");
                                vo.setAllianceStatus(pqhfEnd);
                                //進行回應狀態醫院資料信息描述
                                vo.setAllianceFileStatus(msg);
                                iMedicalService.updateTarnsMedicalTreatmentClaimToAllianceStatus(vo);
                            }

                        }
                    }

                }
            }catch(Exception e) {
                log.info(e);
            }
        }
        log.info("End API-406 Task.");

    }


    /**
     *  API-408 查詢保險公司清單
     * */
    @Scheduled(cron = "${cron.medical408.expression}")
    public void callAPI408() {
        log.info("-----------Start API-408 Task.-----------");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            try {
                Map<String, String> params = new HashMap<>();
                //聯盟鏈歷程參數
                Map<String, String> unParams = new HashMap<>();
                unParams.put("name", "API-408查詢保險公司清單");
                unParams.put("caseId", null);
                unParams.put("transNum", null);
                
                String strResponse = medicalExternalServiceImpl.postForEntity(URL_API408, params, unParams);
                //模仿返回的json數據
                // String strResponse ="{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"insuranceId\":\"L02\",\"insuranceName\":\"台灣壽險\"},{\"insuranceId\":\"L03\",\"insuranceName\":\"國泰壽險\"},{\"insuranceId\":\"L04\",\"insuranceName\":\"中國壽險\"},{\"insuranceId\":\"L05\",\"insuranceName\":\"平安壽險\"},{\"insuranceId\":\"L06\",\"insuranceName\":\"郵政保險\"}]} ";
                // String strResponse ="{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"insuranceId\":\"L02\",\"insuranceName\":\"台灣壽險\"},{\"insuranceId\":\"L03\",\"insuranceName\":\"國泰壽險\"},{\"insuranceId\":\"L04\",\"insuranceName\":\"中國壽險\"}]} ";
                log.info("API408查詢保險公司清單參數  " + strResponse);
                List<HospitalInsuranceCompanyVo> hospitalVos = new ArrayList<>();
                if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {
                    String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(dataString);
                    dataString = rootNode.toString();
                    //如有時間進行格式化時間
                    //Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                    List<HospitalInsuranceCompanyVo> obj = new Gson().fromJson(dataString, new TypeToken<List<HospitalInsuranceCompanyVo>>() {
                    }.getType());
                    log.info("after new Gson().fromJson.................");
                    if (obj != null) {
                        hospitalVos = (List) obj;
                    } else {
                        log.info("API408-查詢保險公司清單參數-轉換數據  obj is null.");
                    }

                }

                if (!CollectionUtils.isEmpty(hospitalVos)) {
                    log.info("-----API408-查詢保險公司清單參數獲取到信息,開始進行處理------" + hospitalVos);
                    //查詢本地保險公司清單
                    HospitalInsuranceCompanyVo hospitalInsuranceCompanyVo = new HospitalInsuranceCompanyVo();
                    List<HospitalInsuranceCompanyVo> hospitalVoList = iHospitalInsuranceCompanyServcie.getHospitalInsuranceCompanyVoList(hospitalInsuranceCompanyVo);
                    System.out.println(hospitalVoList.size());
                    System.out.println(hospitalVoList);
                    if (!CollectionUtils.isEmpty(hospitalVoList)) {
                        //進行修改包含舊名稱狀態為可以用
                        iHospitalInsuranceCompanyServcie.updateHospitalInsuranceCompanyVoList(hospitalVos, StatuCode.AGREE_CODE.code);
                        //進行修改不包含舊名稱狀態為不可以用
                        iHospitalInsuranceCompanyServcie.updateNotHospitalInsuranceCompanyVoIdList(hospitalVos, StatuCode.DISAGREE_CODE.code);
                        hospitalInsuranceCompanyVo.setStatus(StatuCode.AGREE_CODE.code);
                        hospitalInsuranceCompanyVo.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                        //刪除已經存在的數據信息
                        List<HospitalInsuranceCompanyVo> vo = iHospitalInsuranceCompanyServcie.getHospitalInsuranceCompanyVoList(hospitalInsuranceCompanyVo);
                        List<HospitalInsuranceCompanyVo> collect = hospitalVos.stream().filter(x -> {
                            for (HospitalInsuranceCompanyVo hospitalVo1 : vo) {
                                if (hospitalVo1.getInsuranceId().equals(x.getInsuranceId())) {
                                    return false;
                                }
                            }
                            return true;
                        }).collect(Collectors.toList());
                        int size = collect.size();
                        if (!CollectionUtils.isEmpty(collect)) {
                            //進行添加新的數據信息
                            for (HospitalInsuranceCompanyVo addHospitalVo : collect) {
                                addHospitalVo.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                                addHospitalVo.setStatus(StatuCode.AGREE_CODE.code);
                                iHospitalInsuranceCompanyServcie.insertHospitalInsuranceCompanyVo(addHospitalVo);
                            }
                        }
                    } else {
                        //進行存儲數據
                        hospitalVos.stream().forEach((x) -> {
                            x.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                            x.setStatus(StatuCode.AGREE_CODE.code);
                            try {
                                iHospitalInsuranceCompanyServcie.insertHospitalInsuranceCompanyVo(x);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
            log.info("-----------End API-408 Task.-----------");
        }
    }
    /**
     *  API-407 查詢醫院清單
     * */
    @Scheduled(cron = "${cron.medical407.expression}")
    public void callAPI407() {
        log.info("-----------Start API-407 Task.-----------");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            try {
                Map<String, String> params = new HashMap<>();
                //聯盟鏈歷程參數
                Map<String, String> unParams = new HashMap<>();
                unParams.put("name", "API-407查詢醫療醫院清單");
                unParams.put("caseId", null);
                unParams.put("transNum", null);
                
                String strResponse = medicalExternalServiceImpl.postForEntity(URL_API407, params, unParams);
                //模仿返回的json數據
                //String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"hpId\":\"0101090519\",\"hpName\":\"重慶市立聯合醫院\"},{\"hpId\":\"0401180020\",\"hpName\":\"成都護士學醫學院\"},{\"hpId\":\"0401180018\",\"hpName\":\"林口長醫院\"},{\"hpId\":\"0401180013\",\"hpName\":\"輔大醫院\"},{\"hpId\":\"0401180016\",\"hpName\":\"臺北市立聯合醫院\"}]}";
                //String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"hpId\":\"0401180013\",\"hpName\":\"輔大醫院\"},{\"hpId\":\"0401180015\",\"hpName\":\"淡水馬階醫院\"},{\"hpId\":\"101090517\",\"hpName\":\"聯合醫院\"},{\"hpId\":\"0401180013\",\"hpName\":\"輔大醫院\"},{\"hpId\":\"0401180014\",\"hpName\":\"國立台灣學醫學院附設醫院\"}]}";
                log.info("API407-查詢醫療醫院清單參數  " + strResponse);
                List<HospitalVo> hospitalVos = new ArrayList<>();
                if (checkLiaAPIResponseValue(strResponse, "/code", "0")) {
                    String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(dataString);
                    dataString = rootNode.toString();
                    //如有時間進行格式化時間
                    //Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                    List<HospitalVo> obj = new Gson().fromJson(dataString, new TypeToken<List<HospitalVo>>() {
                    }.getType());
                    log.info("after new Gson().fromJson.................");
                    if (obj != null) {
                        hospitalVos = (List) obj;
                    } else {
                        log.info("API407-查詢醫療醫院清單參數-轉換數據  obj is null.");
                    }

                }

                if (!CollectionUtils.isEmpty(hospitalVos)) {
                    log.info("-----API407-查詢醫療醫院清單參數獲取到信息,開始進行處理------" + hospitalVos);
                    //查詢本地醫院清單
                    HospitalVo hospitalVo = new HospitalVo();
                    List<HospitalVo> hospitalVoList = iHospitalServcie.getHospitalVoList(hospitalVo);
                    System.out.println(hospitalVoList.size());
                    System.out.println(hospitalVoList);
                    if (!CollectionUtils.isEmpty(hospitalVoList)) {
                        //進行修改包含舊名稱狀態為可以用
                        iHospitalServcie.updateHospitalVoList(hospitalVos, StatuCode.AGREE_CODE.code);
                        //進行修改不包含舊名稱狀態為不可以用
                        iHospitalServcie.updateNotHospitalVoIdList(hospitalVos, StatuCode.DISAGREE_CODE.code);
                        hospitalVo.setStatus(StatuCode.AGREE_CODE.code);
                        hospitalVo.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                        //刪除已經存在的數據信息
                        List<HospitalVo> vo = iHospitalServcie.getHospitalVoList(hospitalVo);
                        List<HospitalVo> collect = hospitalVos.stream().filter(x -> {
                            for (HospitalVo hospitalVo1 : vo) {
                                if (hospitalVo1.getHpId().equals(x.getHpId())) {
                                    return false;
                                }
                            }
                            return true;
                        }).collect(Collectors.toList());
                        int size = collect.size();
                        if (!CollectionUtils.isEmpty(collect)) {
                            //進行添加新的數據信息
                            for (HospitalVo addHospitalVo : collect) {
                                addHospitalVo.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                                addHospitalVo.setStatus(StatuCode.AGREE_CODE.code);
                                iHospitalServcie.insertHospitalVo(addHospitalVo);
                            }
                        }
                    } else {
                        //進行存儲數據
                        hospitalVos.stream().forEach((x) -> {
                            x.setFunctionName(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE);
                            x.setStatus(StatuCode.AGREE_CODE.code);
                            try {
                                iHospitalServcie.insertHospitalVo(x);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
            log.info("-----------End API-407 Task.-----------");
        }
    }
    /**
     * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
     * @param responseJsonString
     * @param pathFieldName ex:"/code"
     * @param checkValue ex:"0"
     * @return boolean
     */
    private boolean checkLiaAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
        boolean b = false;
        if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
            String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
            log.info("-----------checkLiaAPIResponseValue-----------"+code);
            if(checkValue.equals(code)) {//success
                b = true;
            }
        }
        log.info("-----------checkLiaAPIResponseValue-----return  ------"+b);
        return b;
    }


    public String getURL_API401() {
        return URL_API401;
    }

    public void setURL_API401(String URL_API401) {
        this.URL_API401 = URL_API401;
    }

    public String getURL_API402() {
        return URL_API402;
    }

    public void setURL_API402(String URL_API402) {
        this.URL_API402 = URL_API402;
    }

    public String getURL_API403() {
        return URL_API403;
    }

    public void setURL_API403(String URL_API403) {
        this.URL_API403 = URL_API403;
    }

    public String getURL_API404() {
        return URL_API404;
    }

    public void setURL_API404(String URL_API404) {
        this.URL_API404 = URL_API404;
    }

    public String getURL_API405() {
        return URL_API405;
    }

    public void setURL_API405(String URL_API405) {
        this.URL_API405 = URL_API405;
    }

    public String getURL_API406() {
        return URL_API406;
    }

    public void setURL_API406(String URL_API406) {
        this.URL_API406 = URL_API406;
    }

    public String getURL_API407() {
        return URL_API407;
    }

    public void setURL_API407(String URL_API407) {
        this.URL_API407 = URL_API407;
    }

    public String getURL_API408() {
        return URL_API408;
    }

    public void setURL_API408(String URL_API408) {
        this.URL_API408 = URL_API408;
    }

    public String getAPI_DISABLE() {
        return API_DISABLE;
    }

    public void setAPI_DISABLE(String API_DISABLE) {
        this.API_DISABLE = API_DISABLE;
    }
    
    /**
     * 取得toData nodeString.
     * @param strResponse
     * @return
     */
    private String getToDataNodeString(String strResponse) {
    	String nodeString = null;
    	try {
    		if(StringUtils.isNotBlank(strResponse)) {
        		String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
        		//System.out.println("dataString="+dataString);
    	       	dataString = MyJacksonUtil.getNodeString(dataString, "toData");
    	       	
    	       	if(StringUtils.isNotBlank(dataString)) {
    	       		nodeString = dataString;
    	       	}
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return nodeString;
    }
    
    /**
     * 取得toData裡的to value.
     * @param strResponse
     * @return
     */
    private String getToDataToValue(String strResponse) {
    	String toValue = null;
    	try {
    		String dataString = getToDataNodeString(strResponse);
    		toValue = MyJacksonUtil.readValue(dataString.replace("[", "").replace("]", ""), "/to");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return toValue;
    }
    
    /**
     * 取得data下的toData裡的status
     * @param strResponse
     * @return String
     */
    private String getToDataStatus(String strResponse) {
    	String status = null; 
    	try{
    		String dataString = getToDataNodeString(strResponse);
	       	
	       	status = MyJacksonUtil.readValue(dataString.replace("[", "").replace("]", ""), "/status");
	       	//System.out.println("status="+status);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return status;
    }
    
    /**
     * 取得fromData nodeString
     * @param strResponse
     * @return
     */
    private String getFromDataNodeString(String strResponse) {
    	String nodeString = null;
    	try {
    		if(StringUtils.isNotBlank(strResponse)) {
        		String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
        		//System.out.println("dataString="+dataString);
    	       	dataString = MyJacksonUtil.getNodeString(dataString, "fromData");
    	       	
    	       	if(StringUtils.isNotBlank(dataString)) {
    	       		nodeString = dataString;
    	       	}
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return nodeString;
    }
    
    /**
     * 取得fromData裡的from value
     * @param strResponse
     * @return
     */
    private String getFromDataToValue(String strResponse) {
    	String status = null; 
    	try{
    		String dataString = getFromDataNodeString(strResponse);
	       	
	       	status = MyJacksonUtil.readValue(dataString.replace("[", "").replace("]", ""), "/from");
	       	//System.out.println("status="+status);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return status;
    }
    
    /**
     * 取得fromData裡的status value
     * @param strResponse
     * @return
     */
    private String getFromDataStatus(String strResponse) {
    	String status = null; 
    	try{
    		String dataString = getFromDataNodeString(strResponse);
	       	
	       	status = MyJacksonUtil.readValue(dataString.replace("[", "").replace("]", ""), "/status");
	       	//System.out.println("status="+status);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return status;
    }
    
    
    
    public static void main(String[] args) {
    	System.out.println("MAIN()");
    	try {
    		String strResponse = "{\"code\":\"0\",\"data\":{\"toData\":[{\"to\":\"L01\",\"status\":\"PQHS_PTIS\"}]}}";
        	String allianceStatus = MyJacksonUtil.readValue(strResponse, "/data/toData/status");
        	System.out.println("allianceStatus="+allianceStatus);
        	
        	 String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
        	 System.out.println("dataString="+dataString);
        	 dataString = MyJacksonUtil.getNodeString(dataString, "toData");
        	 System.out.println("dataString="+dataString);
        	 dataString = MyJacksonUtil.readValue(dataString.replace("[", "").replace("]", ""), "/status");
        	 System.out.println("dataString="+dataString);
        	 
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }

}
