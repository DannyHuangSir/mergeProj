package com.twfhclife.scheduling.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.alliance.model.CompanyVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.alliance.service.impl.AllianceServiceImpl;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.service.IHospitalInsuranceCompanyServcie;
import com.twfhclife.eservice.onlineChange.service.IHospitalServcie;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import com.twfhclife.eservice.web.model.HospitalVo;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.service.MailService;
import com.twfhclife.generic.service.SmsService;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyJacksonUtil;
import com.twfhclife.generic.utils.StatuCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private AllianceServiceImpl allianceServiceImpl;

    @Autowired
    private IInsuranceClaimService insuranceClaimService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MailService mailService;

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
                    allianceServiceImpl.setACCESS_TOKEN(parameterItem.getParameterValue());
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
                 String strResponse = allianceService.postForEntity(URL_API408, params, unParams);
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
                 String strResponse = allianceService.postForEntity(URL_API407, params, unParams);
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


    public static void main(String[] args) {
            try {
                Map<String, String> params = new HashMap<>();
                //聯盟鏈歷程參數
                Map<String, String> unParams = new HashMap<>();
                unParams.put("name", "API-407查詢醫療醫院清單");
                unParams.put("caseId", null);
                unParams.put("transNum", null);
                // String strResponse = allianceService.postForEntity(URL_API407, params, unParams);
                //模仿返回的json數據
                String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":[{\"hpId\":\"0101090517\",\"hpName\":\"臺北市立聯合醫院\"},{\"hpId\":\" 0401180014\",\"hpName\":\"國立台灣⼤學醫學院附設醫院\"}]}";
                System.out.println("API407-查詢醫療醫院清單參數  " + strResponse);
                List<HospitalVo> hospitalVos = new ArrayList<>();
                if (new MedicalAllianceServiceTask().checkLiaAPIResponseValue(strResponse, "/code", "0")) {
                    String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
                    //parser "to"-start
                    ObjectMapper mapper = new ObjectMapper();
                    java.util.List<JsonNode> listNode = mapper.readTree(dataString).findPath("to").findValues("companyId");
                    List<CompanyVo> listTo = null;
                    if(listNode!=null && listNode.size()>0) {
                        listTo = new java.util.ArrayList<CompanyVo>();
                        for(JsonNode jn : listNode) {
                            CompanyVo tempCvo = new CompanyVo();
                            tempCvo.setCompanyId(jn.asText());
                            listTo.add(tempCvo);
                        }

                    }else {
                        System.out.println("to/company is null or empty.");
                    }
                    //remove "TO:[]"
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode  = objectMapper.readTree(dataString);
                    dataString = rootNode.toString();
                   // List<HospitalVo> obj = (List<HospitalVo>) MyJacksonUtil.json2Object(dataString, HospitalVo.class);
                    //Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                    List<HospitalVo> obj = new Gson().fromJson(dataString, new TypeToken<List<HospitalVo>>(){}.getType());
                    if(obj!=null) {
                        //log.info("obj is not null.");
                        hospitalVos = (List)obj;
                    }else {
                        System.out.println("-----------obj!=null----------------");
                    }

                }

                if(!CollectionUtils.isEmpty(hospitalVos)) {
                    System.out.println("CollectionUtils.isEmpty=="+hospitalVos);
                }
            } catch (Exception e) {
            e.printStackTrace();
            }
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
}
