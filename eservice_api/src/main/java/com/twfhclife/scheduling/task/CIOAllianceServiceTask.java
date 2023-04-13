package com.twfhclife.scheduling.task;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.alliance.model.*;
import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.model.TaiwanLocalPhoneVo;
import com.twfhclife.generic.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.twfhclife.alliance.service.ICioExternalService;
import com.twfhclife.alliance.service.IContactInfoService;
import com.twfhclife.alliance.service.impl.CioServiceImpl;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoOldVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.service.MailService;
import com.twfhclife.generic.service.SmsService;

/**
 * 聯絡資料變更暨保全聯盟鏈
 *
 */
@Component
public class CIOAllianceServiceTask {

	Log log = LogFactory.getLog(CIOAllianceServiceTask.class);

	public static final String CODE_SUCCESS = "0";

	public static final String MSG_SUCCESS = "SUCCESS";

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private IContactInfoService contactInfoService;

	@Autowired
	private CioServiceImpl cioServiceImpl;

	@Autowired
	private ICioExternalService cioService;

	@Autowired
	ILilipmService iLilipmService;

	@Autowired
	private IInsuranceClaimService insuranceClaimService;

	@Autowired
	private IPolicyListService policyListService;

	@Autowired
	private ITransContactInfoService transContactInfoService;

	@Autowired
	private ITransService transService;

	@Autowired
	ITransAddService transAddService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SmsService smsService;

	@Value("${upload.file.save.path}")
	private String FILE_SAVE_PATH;

	//@Value("${alliance.api201.url}")
	public String URL_API201;

	//@Value("${alliance.api202.url}")
	public String URL_API202;

	//@Value("${alliance.api203.url}")
	public String URL_API203;

	//@Value("${alliance.api204.url}")
	public String URL_API204;

	//@Value("${alliance.api106.url}")
	public String URL_API206;

	//@Value("${cron.api.disable}")
	public String API_DISABLE;
	//API调用  失败上限次数
	//默认值为5
	public String API_LIMIT_CODENUMBER="5";

	@Autowired
	IMessagingTemplateService messagingTemplateService;
	//存储各个API接口失败次数
	private  static Map<String,Integer>  hashMap=new ConcurrentHashMap();
	//是否进行发送 失败email
	private  Boolean boo=false;

	/**
	 * 初始化失败次数
	 */
	static {
		hashMap.put(CallApiCode.CALL_API201_NO_NUMBER,0);
		hashMap.put(CallApiCode.CALL_API202_NO_NUMBER,0);
		hashMap.put(CallApiCode.CALL_API203_NO_NUMBER,0);
		hashMap.put(CallApiCode.CALL_API204_NO_NUMBER,0);
		hashMap.put(CallApiCode.CALL_API206_NO_NUMBER,0);
		hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_TRANS,0);
		hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS,0);
	}

	@PostConstruct
	public void doAllianceServiceTask() {
		List<ParameterVo> resultSCHList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_CIO_SCHEDULE");
    	List<ParameterVo> resultURLList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_CIO_API_URL");
    	List<ParameterVo> resultBASELList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_PRO_BASE_URL");
    	//获取API调用失败上线次数
		String codeNumber= parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, ApConstants.API_NO_RESPONSE_NUMBER);

		if (resultBASELList != null) {
    		resultBASELList.forEach(parameterItem ->{
        		if ("alliance.api.accessToken".equals(parameterItem.getParameterName())) {
        			cioServiceImpl.setACCESS_TOKEN(parameterItem.getParameterValue());
        		}
        	});
    	}
		if (codeNumber!=null) {
			this.setAPI_LIMIT_CODENUMBER(codeNumber);
		}
    	if (resultURLList != null) {
	    	resultURLList.forEach(parameterItem -> {
	    		if ("alliance.api201.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API201(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api202.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API202(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api203.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API203(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api204.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API204(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api206.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API206(parameterItem.getParameterValue());
	    		}
	    	});
    	}
    	if (resultSCHList != null) {
    		resultSCHList.forEach(parameterItem -> {
    			if ("cron.api.cio.disable".equals(parameterItem.getParameterName())) {
        			this.setAPI_DISABLE(parameterItem.getParameterValue());
        		}else {
        			if (System.getProperty(parameterItem.getParameterName()) == null) {
        				System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
        			}
        		}
            });
    	}

	}
	public String getURL_API201() {
		return URL_API201;
	}
	public void setURL_API201(String uRL_API201) {
		URL_API201 = uRL_API201;
	}
	public String getURL_API202() {
		return URL_API202;
	}
	public void setURL_API202(String uRL_API202) {
		URL_API202 = uRL_API202;
	}
	public String getURL_API203() {
		return URL_API203;
	}
	public void setURL_API203(String uRL_API203) {
		URL_API203 = uRL_API203;
	}
	public String getURL_API204() {
		return URL_API204;
	}
	public void setURL_API204(String uRL_API204) {
		URL_API204 = uRL_API204;
	}
	public String getURL_API206() {
		return URL_API206;
	}
	public void setURL_API206(String uRL_API206) {
		URL_API206 = uRL_API206;
	}

	public String getAPI_LIMIT_CODENUMBER() {
		return API_LIMIT_CODENUMBER;
	}

	public void setAPI_LIMIT_CODENUMBER(String API_LIMIT_CODENUMBER) {
		this.API_LIMIT_CODENUMBER = API_LIMIT_CODENUMBER;
	}

	public String getAPI_DISABLE() {
		return API_DISABLE;
	}
	public void setAPI_DISABLE(String aPI_DISABLE) {
		API_DISABLE = aPI_DISABLE;
	}

	@Value("${cron.cio201.expression.enable: true}")
	public boolean api201Enable;
	/**
	 * 保全契約變更申 請書上傳 
	 */
	@Scheduled(cron = "${cron.cio201.expression}")
	public void callAPI201() {
		if (!api201Enable) {
			return;
		}
		log.info("Start API-201 Task.");
		log.info("API_DISABLE="+API_DISABLE);
		//testcode
//		try {
//			ContactInfoParameterVo paramVO = new ContactInfoParameterVo();
//			ContactInfoCondition conVO = new ContactInfoCondition();
//			conVO.setCidNo("A987654321");
//			conVO.setCbirdate("19801115");  
//			conVO.setCname("王大明");   
//			
//			ContactInfoContact contVO = new ContactInfoContact();
//			contVO.setcPhone("0912345678");
//			contVO.setcMail("abc@test.com.tw");
//			contVO.setcAddress("台北市中正區信義路一段21-3號");
//			
//			paramVO.setCondition(conVO);
//			paramVO.setContact(contVO);
//			
//			paramVO.setName("王大明");
//			paramVO.setIdNo("A123456789");
//			paramVO.setrZipCode("10048");
//			paramVO.setrAddress("台北市中正區信義路一段21-3號");
//			paramVO.setmZipCode("10048");
//			paramVO.setmAddress("台北市中正區信義路一段21-3號");
//			paramVO.setPhone("0912345678");
//			paramVO.setHomeTel1("12345678");
//			paramVO.setHomeTelCode1("0836");
//			paramVO.setHomeTelExt1("12345");
//			paramVO.setHomeTel2("12345678");
//			paramVO.setHomeTelCode2("0836");
//			paramVO.setHomeTelExt2("12345");
//			paramVO.setMail("abc@test.com.tw\"");
//			paramVO.setFrom("L01");
//			
//			paramVO.setAgreement("同意書內容之字串");
//			paramVO.setLogDateTime("20200201170105");
//			paramVO.setLogDesc1("PDCA(001)");
//			paramVO.setSource("W");
//			
//			List<CompanyVo> tovo = new ArrayList<CompanyVo>();
//			CompanyVo vo1 = new CompanyVo();
//			vo1.setCompanyId("L02");
//			tovo.add(vo1);
//			CompanyVo vo2 = new CompanyVo();
//			vo2.setCompanyId("L04");
//			tovo.add(vo2);
//			paramVO.setTo(tovo);
//			
//			List<ContactInfoFileDataVo> fileDatas = new ArrayList<ContactInfoFileDataVo>();
//			ContactInfoFileDataVo file1 = new ContactInfoFileDataVo();
//			file1.setType("A");
//			file1.setFileName("20200122121250L01-A00001-A.pdf");
//			fileDatas.add(file1);
//			ContactInfoFileDataVo file2 = new ContactInfoFileDataVo();
//			file2.setType("B");
//			file2.setFileName("20200122121250L01-A00001-B.pdf");
//			fileDatas.add(file2);
//			paramVO.setFileDatas(fileDatas);
//			
//			Gson gson = new Gson(); 
//	        String json = gson.toJson(paramVO);
//	        log.info("json="+json);
//
//			String strResponse = cioService.postForEntity(URL_API201, paramVO, "API201");
//	    String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"caseId\":\"78688fc3-f408-4f39-bd2c-45c17f68e615-L01\",\"fileDatas\":[{\"fileId\":\"48c37063-f09a-4934-be64....\",\"fileName\":\"20200122121250L01-A00001-001.pdf\"},{\"fileId\":\"aaaa5445-f09a-4934-be64-....\",\"fileName\":\"20200122121250L01-A00001-002.pdf\"}]}}";
//			log.info("strResponse="+strResponse);
//		}catch(Exception e) {
//			log.error(e);
//		}
		//testcode


		if("N".equals(API_DISABLE)){
			int rtn = -1;
			//获取失败上线次数
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			try {
				//1.取得申請上傳資料物件(Request)
				List<ContactInfoMapperVo> listIC = contactInfoService.getContactInfoByNoCaseId();
				//2.call api-201
				if(listIC!=null && listIC.size()>0) {
					for (ContactInfoMapperVo icvo : listIC) {
						if(icvo!=null) {
							//"to": [{"companyId": "L02","status": "Y","msg": "回報之訊息"}, {"companyId": "L03","status": "Y","msg": "回報之訊息"}],
							//ContactInfoParameterVo paramVO = new ContactInfoParameterVo();
							ContactInfoCondition conVO = new ContactInfoCondition();
							conVO.setCidNo(icvo.getCidNo());
							conVO.setCbirdate(icvo.getCbirdate());
							conVO.setCname(icvo.getCname());

							ContactInfoContact contVO = new ContactInfoContact();
							contVO.setcPhone(icvo.getPhone());
							contVO.setcMail(icvo.getcMail());
							contVO.setcAddress(icvo.getcAddress());

						/*	paramVO.setCondition(conVO);
							paramVO.setContact(contVO);
							
							paramVO.setName(icvo.getName());
							paramVO.setIdNo(icvo.getIdNo());
							paramVO.setrZipCode(icvo.getrZipCode());
							paramVO.setrAddress(icvo.getrAddress());
							paramVO.setmZipCode(icvo.getmZipCode());
							paramVO.setmAddress(icvo.getmAddress());
							paramVO.setPhone(icvo.getPhone());
							paramVO.setHomeTel1(icvo.getHomeTel1());
							paramVO.setHomeTelCode1(icvo.getHomeTelCode1());
							paramVO.setHomeTelExt1(icvo.getHomeTelExt1());
							paramVO.setHomeTel2(icvo.getHomeTel2());
							paramVO.setHomeTelCode2(icvo.getHomeTelCode2());
							paramVO.setHomeTelExt2(icvo.getHomeTelExt2());
							paramVO.setMail(icvo.getMail());
							paramVO.setFrom(icvo.getFrom());
							if(icvo != null && icvo.getTo() != null) {
								String cStr = icvo.getTo();
	                            List<CompanyVo> cList = new ArrayList<CompanyVo>();
	                            if(cStr != null) {
	                                String[] cIds = cStr.split(",");
	                                for (int i = 0; i < cIds.length; i++) {
	                                    CompanyVo vo = new CompanyVo();
	                                    vo.setCompanyId(cIds[i]);
	                                    cList.add(vo);
	                                }
	                            }
	                            paramVO.setTo(cList);
							}


							paramVO.setAgreement(icvo.getAgreement());
							paramVO.setLogDateTime(icvo.getLogDateTime());
							paramVO.setLogDesc1(icvo.getLogDesc1());
							paramVO.setSource(icvo.getSource());*/
							/**
							 *"to":"L02,L03",
							 * */
							Gson gson = new Gson();
							HashMap<String, Object> hashMapContactInfo = new HashMap<>();
							hashMapContactInfo.put("condition",conVO);
							hashMapContactInfo.put("contact",contVO);
							hashMapContactInfo.put("name",icvo.getName());
							hashMapContactInfo.put("idNo",icvo.getIdNo());
							hashMapContactInfo.put("rZipCode",icvo.getrZipCode());
							hashMapContactInfo.put("rAddress",icvo.getrAddress());
							hashMapContactInfo.put("mZipCode",icvo.getmZipCode());
							hashMapContactInfo.put("mAddress",icvo.getmAddress());
							hashMapContactInfo.put("phone",icvo.getPhone());
							//拆解上傳 住家电话
							/**
							 * 201只是單純的將資料上傳，不做其它判斷邏輯
							 * 改成在saveCioToEserviceUnionTempTable()拆解
							 * */
						/*	if (!StringUtils.isEmpty(icvo.getHomeTel1())) {
								TaiwanLocalPhoneVo taiwanLocalPhoneVo = TaiwanLocalPhoneUtil.parseLocalPhone(icvo.getHomeTel1());
								hashMapContactInfo.put("homeTel1",taiwanLocalPhoneVo.getPhoneNo());
								hashMapContactInfo.put("homeTelCode1",taiwanLocalPhoneVo.getLocalCode());
							}else {

							}*/
							hashMapContactInfo.put("homeTel1",icvo.getHomeTel1());
							hashMapContactInfo.put("homeTelCode1",icvo.getHomeTelCode1());

							hashMapContactInfo.put("homeTelExt1",icvo.getHomeTelExt1());
							hashMapContactInfo.put("homeTel2",icvo.getHomeTel2());
							hashMapContactInfo.put("homeTelCode2",icvo.getHomeTelCode2());
							hashMapContactInfo.put("homeTelExt2",icvo.getHomeTelExt2());
							hashMapContactInfo.put("mail",icvo.getMail());
							hashMapContactInfo.put("from",icvo.getFrom());
							hashMapContactInfo.put("to",icvo.getTo());
							hashMapContactInfo.put("agreement",icvo.getAgreement());
							hashMapContactInfo.put("logDateTime",icvo.getLogDateTime());
							hashMapContactInfo.put("logDesc1",icvo.getLogDesc1());
							hashMapContactInfo.put("source",icvo.getSource());
							//fileNames
							hashMapContactInfo.put("fileNames",icvo.getFileNames());

							HashMap<String, String> hashMapAPI = new HashMap<>();
							hashMapAPI.put("name","API-201保全契約變更申請書上傳");
							hashMapAPI.put("caseId",icvo.getCaseId());
							hashMapAPI.put("transNum", icvo.getTransNum());
							log.info("-=======================call URL_API201,  is transNum ================"+ icvo.getTransNum());

							//獲取transNum
							/*Float batchId = icvo.getBatchId();
							if (batchId!= null) {
								List<String>  transNum	=transContactInfoService.getTransContactInfoTransNumByBatchId(batchId);
								if (transNum != null) {
									log.info("-=======================call URL_API201,  is transNum ================"+ transNum.toString());
									hashMapAPI.put("transNum", transNum.toString());
								}
							}*/

					        String json = gson.toJson(hashMapContactInfo);
							log.info("call URL_API201,json="+json);
							//3.call api-201 to upload.
							String strResponse = cioService.postForMapEntity(URL_API201, hashMapContactInfo, hashMapAPI);
							log.info("call URL_API201,strResponse="+strResponse);
							//进行判断联盟获取是否有资料
							if(strResponse==null || "".equals(strResponse)){
								hashMap.put(CallApiCode.CALL_API201_NO_NUMBER
										,hashMap.get(CallApiCode.CALL_API201_NO_NUMBER)+1);
								if (hashMap.get(CallApiCode.CALL_API201_NO_NUMBER)>=maxGoOnline) {
									boo=true;
									hashMap.put(CallApiCode.CALL_API201_NO_NUMBER,0);
									throw  new  RuntimeException("URL_API201  Response  is  null ");
								}
							}
							//3-1.get api-201 response, update caseId, fileId to db.
							// 20220629 by 203990
							/// 非連線問題, 上傳失敗寄送信件給管理者
							//if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
							if(checkLiaAPIResponseValueAndSendEmail(strResponse,"/code","0",icvo)) {
								String caseId = MyJacksonUtil.readValue(strResponse, "/data/caseId");
								log.info("caseId="+caseId);
								String msg = MyJacksonUtil.readValue(strResponse, "/msg");
								icvo.setCaseId(caseId);
								icvo.setCode(CODE_SUCCESS);
								icvo.setMsg(msg);
								icvo.setStatus(ContactInfoVo.STATUS_UPLOADED);
								log.info("推送联盟成功-进行回压caseID,code, msg"+icvo.toString());
								//依fileName,塞好回傳的fileId-start
								//boolean fileIdSaveOK = false;
								List<ContactInfoFileDataVo> targetFileDatas = icvo.getFileDatas();
								if(targetFileDatas!=null && !targetFileDatas.isEmpty()) {
									String jsonFileDatas = MyJacksonUtil.getNodeString(strResponse, "data");
									log.info("jsonFileDatas = "+jsonFileDatas);
									Object obj = MyJacksonUtil.json2Object(jsonFileDatas, ContactInfoVo.class);
									if(obj!=null) {
										ContactInfoVo tempICVo = (ContactInfoVo)obj;
										List<ContactInfoFileDataVo> tempFileDatas = tempICVo.getFileDatas();
										Map<String,String> mapFileId = new HashMap<String,String>();
										for(ContactInfoFileDataVo tempVo : tempFileDatas) {
											mapFileId.put(tempVo.getFileName(), tempVo.getFileId());
										}
										for(ContactInfoFileDataVo targetVo : targetFileDatas) {
											targetVo.setFileId(mapFileId.get(targetVo.getFileName()));
										//	fileIdSaveOK = true;
										}
									}
								}
								icvo.setFileDatas(targetFileDatas);
								//依fileName,塞好回傳的fileId-end
								/***
								 * 无论是否有文件，都进行回压caseID,code, msg
								 * */
								//if(fileIdSaveOK) {
									rtn = contactInfoService.updateCaseIdByContactSeqId(icvo);
								//}

								/*if(rtn>0) {
									//更新CONTACT_INFO
									rtn = contactInfoService.updateContactAfterUploadToAlliance(icvo);
								}*/
								//UI件呼叫完API201上傳成功取得CASE_ID後，使用TRANS_NUM and BATCH_ID回壓TRANS_CONTACT_INFO.CASE_ID欄位
								log.info("===========UI件呼叫完API201上傳成功取得CASE_ID後---回壓TRANS_CONTACT_INFO.CASE_ID欄位==========="+icvo.getTransNum()+":"+icvo.getCaseId());
								contactInfoService.updateTransContactInfoCaseId(icvo.getCaseId(),icvo.getTransNum());

							}

						}
					}

				}

			}catch(Exception e) {
				hashMap.put(CallApiCode.CALL_API201_NO_NUMBER
						,hashMap.get(CallApiCode.CALL_API201_NO_NUMBER)+1);
				Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API201_NO_NUMBER);
				log.info("API201调用失败次数::{}"+integerNoNumber);
				if (boo||(integerNoNumber>=maxGoOnline)) {
					log.info("Call threshold exceeded start  mail");
					callFailedMail(e,"201");
					hashMap.put(CallApiCode.CALL_API201_NO_NUMBER,0);
					boo=false;
					log.info("Call threshold exceeded  send mail");
				}
				log.error(e.toString());
			}
		}//end-if


    	log.info("End API-201 Task.");
	}

	@Value("${cron.cio202.expression.enable: true}")
	public boolean api202Enable;
	/**
	 * 檔案上傳
	 */
	@Scheduled(cron = "${cron.cio202.expression}")
	public void callAPI202() {
		if (!api202Enable) {
			return;
		}
		log.info("Start API-202 Task.");
		log.info("API_DISABLE="+API_DISABLE);
//		String strResponse=null;
		//testcode
/*		try {
			Map<String,String> testparams = new HashMap<>();
			testparams.put("fileId", "20201216163030-3cuzhNS");
			String teststrBase64 = this.converFileToBase64Str("./202012163030L01-2810002021-A.pdf");
			testparams.put("base64", teststrBase64);
			Map<String,String> unParams = new HashMap<>();
			unParams.put("name", "API204");
			unParams.put("caseId", "case-id-2342-22k4-44b");
			unParams.put("transNum", "2016070001");
			strResponse = cioService.postForEntity(URL_API201, testparams,unParams);
			//去保險科技共享平台查看檔案
		}catch(Exception e){
			log.error(e);
		}*/
		//testcode




		if("N".equals(API_DISABLE)){
			//获取失败上线次数
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			try {
				//1.上傳件且上傳且未成功:NOTIFY_SEQ_ID=null and UPPER(MSG)!='SUCCESS'
				ContactInfoFileDataVo vo = new ContactInfoFileDataVo();
				List<ContactInfoFileDataVo> files = contactInfoService.getFileDataToUpload(vo);

				//2.call api-202
				if(files!=null &&files.size()>0) {
					for(ContactInfoFileDataVo fileVo : files) {
						if(fileVo!=null) {
							//2.1.call api-202
							Map<String,String> params = new HashMap<>();
							params.put("fileId", fileVo.getFileId());
							//直接存储strBase64屏蔽
//							String strBase64 = this.converFileToBase64Str(fileVo.getPath()+"/"+fileVo.getFileName());
							//获取Base64数据
							String fileBase64 = this.getFileBase64(fileVo);
							if(fileBase64!=null) {
								//do not print base64 string
								log.info("call URL_API202,fileBase64 is not null.");
							}else {
								log.info("call URL_API202,fileBase64 is null.");
							}
							params.put("base64", fileBase64);

							//參數
						    ContactInfoMapperVo insVo = contactInfoService.getCaseIdBySeqId(fileVo.getContactSeqId());
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-202檔案上傳");
							unParams.put("caseId", insVo.getCaseId());
							unParams.put("transNum", "");

							String strResponse = cioService.postForEntity(URL_API202, params,unParams);
							//String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"caseId\":\"78688fc3-f408-4f39-bd2c-45c17f68e615-L01\",\"fileDatas\":[{\"fileId\":\"48c37063-f09a-4934-be64....\",\"fileName\":\"20200122121250L01-A00001-001.pdf\"},{\"fileId\":\"aaaa5445-f09a-4934-be64-....\",\"fileName\":\"20200122121250L01-A00001-002.pdf\"}]}}";

							log.info("URL_API202聯盟鏈歷程參數  "+strResponse);
							//进行判断联盟获取是否有资料
							if(strResponse==null || "".equals(strResponse)){
								hashMap.put(CallApiCode.CALL_API202_NO_NUMBER
										,hashMap.get(CallApiCode.CALL_API202_NO_NUMBER)+1);
								if (hashMap.get(CallApiCode.CALL_API202_NO_NUMBER)>=maxGoOnline) {
									boo=true;
									hashMap.put(CallApiCode.CALL_API202_NO_NUMBER,0);
									throw  new  RuntimeException("URL_API202  Response  is  null ");
								}
							}

							//2.2.若回傳msg='SUCCESS',
							//update FILE_STATUS='1',CONTATCT_INFO_FILEDATAS.MSG='SUCCESS',UPDATE_MSG_DATE=getdate()
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {//已有檔案上傳時聯盟會擋重覆上傳
								String msg = MyJacksonUtil.readValue(strResponse, "/msg");
								fileVo.setMsg(msg);
								fileVo.setFileStatus(ContactInfoFileDataVo.FILE_STATUS_UPLOADED);

								int rtn = contactInfoService.updateFileStatusByFileId(fileVo);
								log.info("URL_API202文件上传会写状态，响应行数  "+rtn);

							}

						}
					}
				}


			}catch(Exception e) {
				hashMap.put(CallApiCode.CALL_API202_NO_NUMBER
						,hashMap.get(CallApiCode.CALL_API202_NO_NUMBER)+1);
				Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API202_NO_NUMBER);
				log.info("API202调用失败次数::{}"+integerNoNumber);
				if (boo||(integerNoNumber>=maxGoOnline)) {
					log.info("Call threshold exceeded start  mail");
					callFailedMail(e,"202");
					hashMap.put(CallApiCode.CALL_API202_NO_NUMBER,0);
					boo=false;
					log.info("Call threshold exceeded  send mail");
				}
				log.error(e.toString());
			}
		}//end-if



    	log.info("End API-202 Task.");

	}

	@Value("${cron.cio203.expression.enable: true}")
	public boolean api203Enable;

	/**
	 * 查詢案件
	 */
	@Scheduled(cron = "${cron.cio203.expression}")
	public void callAPI203() {
		if (!api203Enable) {
			return;
		}
		log.info("Start API-203 Task.");
		log.info("API_DISABLE="+API_DISABLE);

		//testcode
//		try {
//		String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"condition\":{\"cidNo\":\"A122021826\",\"cbirdate\":\"19801115\",\"cname\":\"王西明\",\"crname\":\"wang ta ming\",\"cename\":\"Daming WANG\"},\"contact\":{\"cPhone\":\"0912345678\",\"cMail\":\" abc@test.com.tw \",\"cAddress\":\"台北市中正區信義路一段21-3號\"},\"name\":\"王大明\",\"idNo\":\"A122021826\",\"rZipCode\":\"10048\",\"rAddress\":\"台北市中正區信義路一段21-3號\",\"mZipCode\":\"10048\",\"mAddress\":\"台北市中正區信義路一段21-3號\",\"phone\":\"0912345678\",\"homeTel1\":\"12345678\",\"homeTelCode1\":\"0836\",\"homeTelExt1\":\"12345\",\"homeTel2\":\"12345678\",\"homeTelCode2\":\"02\",\"homeTelExt2\":\"12345\",\"mail\":\"abc@test.com.tw\",\"from\":\"L02\",\"to\":[{\"companyId\":\"L02\",\"status\":\"Y\",\"msg\":\"回報之訊息\"},{\"companyId\":\"L03\",\"status\":\"Y\",\"msg\":\"回報之訊息\"}],\"agreement\":\"同意書內容之字串\",\"logDateTime\":\"20200201170105\",\"logDesc1\":\" PDCA(001)\",\"source\":\"W\",\"fileDatas\":[{\"fileId\":\"48c37063-f09a-4934-be64-....\",\"size\":\"300\",\"fileName\":\"20200122121250L01-A00001-001.pdf\",\"fileStatus\":\"1\",\"path\":\"/L01/202003/wKODkASHSAiMmM5P77JdYg/\",\"sftpPath\":\"/D:/apps/user/receive/done/L01/202003/wKOD kASHSAiMmM5P77JdYg/\"},{\"fileId\":\"aaaaaaaa-f09a-4934-be64-....\",\"size\":\"300\",\"fileName\":\"20200122121250L01-A00001-002.pdf\",\"fileStatus\":\"1\",\"path\":\"/L01/202003/wKODkASHSAiMmM5P77JdYg/\",\"sftpPath\":\"/D:/apps/user/receive/done/L01/202003/wKOD kASHSAiMmM5P77JdYg/\"}]}}";
//			String dataString = MyJacksonUtil.getNodeString(teststrResponse, "data");
//			System.out.println("dataString="+dataString);
//			Object obj = MyJacksonUtil.json2Object(dataString, ContactInfoParameterVo.class);
//			if(obj!=null) {
//				ContactInfoParameterVo testicvo = (ContactInfoParameterVo)obj;
//				Gson gson = new Gson(); 
//				String jsonString = gson.toJson(testicvo);
//				System.out.println("gson.toJson(testicvo)="+jsonString);
//			}
//		}catch(Exception e) {
//			log.error(e);
//		}
		//testcode


		if("N".equals(API_DISABLE)){
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			ContactInfoParameterVo icvo = null;
			
			//1.取得聯盟通知新案件資料物件
			List<NotifyOfNewCaseChangeVo> newCases = null;
			try {
				newCases = contactInfoService.getNofifyOfNewCaseByNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ZERO);
			}catch(Exception e) {
				log.info(e);
			}
			
			//获取失败上线次数
			log.error("获取失败次数  "+hashMap.get(CallApiCode.CALL_API203_NO_NUMBER));
			//2.call api-203
			if(newCases!=null && newCases.size()>0) {
				for(NotifyOfNewCaseChangeVo vo : newCases) {
					try {
						icvo = null;//init
						if(vo!=null && vo.getCaseId()!=null) {
							String caseId      = vo.getCaseId();
							Float  nofifySeqId = vo.getSeqId();
							//call api-203
							Map<String,String> params = new HashMap<>();
							params.put("caseId", vo.getCaseId());

							//聯盟鏈歷程參數
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-203查詢保全變更案件");
							unParams.put("caseId", vo.getCaseId());
							unParams.put("transNum", null);

							String strResponse = cioService.postForEntity(URL_API203, params, unParams);
							log.info("API203-聯盟鏈歷程參數  "+strResponse);

							//进行判断联盟获取是否有资料
							if(strResponse==null || "".equals(strResponse)){
								hashMap.put(CallApiCode.CALL_API203_NO_NUMBER
										,hashMap.get(CallApiCode.CALL_API203_NO_NUMBER)+1);
								if (hashMap.get(CallApiCode.CALL_API203_NO_NUMBER)>=maxGoOnline) {
									boo=true;
									hashMap.put(CallApiCode.CALL_API203_NO_NUMBER,0);
									throw  new  RuntimeException("URL_API203  Response  is  null ");
								}
							}

							//icvo , get data form Api203;
							String code = "";
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
								String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
								code = MyJacksonUtil.readValue(strResponse, "/code");
								//log.info("dataString="+dataString);

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
										//System.out.println(jn.asText());
									}

								}else {
									log.error("to/company is null or empty.");
									//System.out.println(listNode);
								}
								//remove "TO:[]"
								ObjectMapper objectMapper = new ObjectMapper();
								JsonNode rootNode  = objectMapper.readTree(dataString);
								ObjectNode nodeObj = (ObjectNode) rootNode;
								nodeObj.remove("to");
								dataString = rootNode.toString();
								//log.info("after remove to dataString="+dataString);
								//parser "to"-end

								Object obj = MyJacksonUtil.json2Object(dataString, ContactInfoParameterVo.class);
								log.info("after MyJacksonUtil.json2Object");
								if(obj!=null) {
									//log.info("obj is not null.");
									icvo = (ContactInfoParameterVo)obj;
									//icvo.setCode("0");

									if(listTo!=null) {
										icvo.setTo(listTo);
									}
//										Gson gson = new Gson();
//										String jsonString = gson.toJson(icvo);
//										log.info("gson.toJson(testicvo)="+jsonString);
								}else {
									log.info("obj is null.");
								}
							}
							
							String idNo = null;
							if(icvo.getCondition()!=null) {
								idNo = icvo.getCondition().getCidNo();//取用舊ID
							}
							log.info("----cio203  查詢是否有案件未完結---------"+idNo);
							
							//查询是否有案件未完結
							int contactInfoCompleted = transContactInfoService.getContactInfoCompleted(idNo);
							if (contactInfoCompleted>0) {
								log.info("案件未完結-回報的status=Y ,msg=已有案件進行中");
								vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_Y);
								vo.setMsg(NotifyOfNewCaseChangeVo.GO_AHEAD_MSG);
								contactInfoService.updateNcStatusBySeqId(vo);


								// 20211110 by 203990
								// 【保全聯盟鏈 - 轉收件 - 擋件】通知後台管理人員
								Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
								//發送系統管理員
								long timeMillis = System.currentTimeMillis();
								List<String> receivers = new ArrayList<String>();
								receivers = (List)mailInfo.get("receivers");
								Map<String, String> paramMap = new HashMap<String, String>();
								paramMap.put("EMAIL",EMailTemplate.INSURED_EMAIL_IS_NULL);
								//paramMap.put("DATA",CallApiDateFormatUtil.getTimeMillisToString(timeMillis,"yyyyMMddHHmmss"));
								paramMap.put("DATA",caseId);
								paramMap.put("CACE_NUMBER",caseId);
								MessageTriggerRequestVo voCIO = new MessageTriggerRequestVo();
								voCIO.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_023);
								voCIO.setSendType("email");
								voCIO.setMessagingReceivers(receivers);
								voCIO.setParameters(paramMap);
								voCIO.setSystemId(ApConstants.SYSTEM_ID);
								//进行发送通信
								String resultSYSMailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
								log.info("發送【保全聯盟鏈 - 轉收件 - 擋件】系統管理員RESULT : " + resultSYSMailMsg);
								
								
								/**
								 * 如果保戶已有申請單在處理中，回報案件狀態為“Y” msg=已有案件處理中
								 * */
								params.put("status", NotifyOfNewCaseChangeVo.NC_STATUS_Y);
								params.put("msg", NotifyOfNewCaseChangeVo.GO_AHEAD_MSG);
								log.info("----------保戶已有申請單在處理中 调用206接口,参数---" + params);

								//聯盟鏈歷程參數
								unParams.put("name", "API206");
								cioService.postForEntity(URL_API206, params, unParams);
								break;
							}

							if(icvo!=null) {
								ContactInfoParameterVo paramVo = new ContactInfoParameterVo();
								BeanUtils.copyProperties(icvo,paramVo);
								ContactInfoMapperVo mapperVo = new ContactInfoMapperVo();
								ContactInfoCondition condiction = paramVo.getCondition();
								ContactInfoContact contact = paramVo.getContact();
								mapperVo.setCode(code) ;
								mapperVo.setMsg(null);
								mapperVo.setCidNo(condiction.getCidNo());
								mapperVo.setCbirdate(condiction.getCbirdate());
								mapperVo.setCname(condiction.getCname());
								mapperVo.setCrname(condiction.getCrname());
								mapperVo.setCename(condiction.getCename());
								mapperVo.setcPhone(contact.getcPhone()) ;
								mapperVo.setcMail(contact.getcMail());
								mapperVo.setcAddress(contact.getcAddress());
								mapperVo.setName(paramVo.getName());
								mapperVo.setRname(condiction.getCrname());
								mapperVo.setEname(condiction.getCename());
								mapperVo.setIdNo(paramVo.getIdNo());
								mapperVo.setrZipCode(paramVo.getrZipCode()) ;
								mapperVo.setrAddress(paramVo.getrAddress());
								mapperVo.setmZipCode(paramVo.getmZipCode());
								mapperVo.setmAddress(paramVo.getmAddress()) ;
								mapperVo.setPhone(paramVo.getPhone());
								mapperVo.setHomeTel1(paramVo.getHomeTel1()) ;
								mapperVo.setHomeTelCode1(paramVo.getHomeTelCode1()) ;
								mapperVo.setHomeTelExt1(paramVo.getHomeTelExt1()) ;
								mapperVo.setHomeTel2(paramVo.getHomeTel2());
								mapperVo.setHomeTelCode2(paramVo.getHomeTelCode2());
								mapperVo.setHomeTelExt2(paramVo.getHomeTelExt2());
								mapperVo.setMail(paramVo.getMail()) ;
								mapperVo.setFrom(paramVo.getFrom());
								mapperVo.setAgreement(paramVo.getAgreement()) ;
								mapperVo.setLogDateTime(paramVo.getLogDateTime());
								mapperVo.setLogDesc1(paramVo.getLogDesc1()) ;
								mapperVo.setSource(paramVo.getSource()) ;
								mapperVo.setSendAlliance("");
								mapperVo.setStatus(ContactInfoVo.STATUS_RECEIVED);
								/**用於類型判斷防止首家件與聯盟件，發送郵件*/
								mapperVo.setPieceType(ApConstants.ALLIANCE_PIECE);

								log.info("ApConstants.ALLIANCE_PIECE"+	ApConstants.ALLIANCE_PIECE);
								log.info("mapperVo.setPieceType(ApConstants.ALLIANCE_PIECE)"+	mapperVo.getPieceType());
								if(icvo.getTo()!=null) {//補to物件
									String str = "";
									List<CompanyVo> comps= icvo.getTo();
									for(CompanyVo compvo : comps) {
										str += compvo.getCompanyId()+",";
									}
									if (str.endsWith(",")) {
										str = str.substring(0, str.length() - 1);
									}
									if(str!=null && str.trim()!="") {
										mapperVo.setTo(str);
									}
								}

								mapperVo.setCaseId(caseId);

								//將聯盟通知件seqId塞入
								mapperVo.setNotifySeqId(nofifySeqId);
								List<ContactInfoFileDataVo> listFileData = icvo.getFileDatas();
								log.info("mapperVo.getFileDatas()="+mapperVo.getFileDatas());
								if(listFileData!=null && listFileData.size()>0){
									for(ContactInfoFileDataVo fileVo : listFileData) {
										if(fileVo!=null) {
											fileVo.setNotifySeqId(nofifySeqId);
										}
									}
									mapperVo.setFileDatas(listFileData);
								}else {
									log.info("listFileData is null.");
								}
								
								//modify by 2021/10/26-only get from LILIPM-start
								int k = -1;
								LilipmVo lilipmVo = iLilipmService.findByRocId(mapperVo.getCidNo());//只找LILIPM
								//int k = iLilipmService.getInsuredUsersByRocId(mapperVo.getCidNo());//找LILIPM+LILIPI
								if(lilipmVo!=null) {
									k = 1;
									log.info("判斷是否為保戶:LilipmVo.LIPM_INSU_NO="+lilipmVo.getLipmInsuNo()+",LilipmVo.LIPM_ID="+lilipmVo.getLipmId());
								}else {
									log.info("判斷是否為保戶:LilipmVo is null."+"ROC_ID="+mapperVo.getCidNo());
								}
								//modify by 2021/10/26-only get from LILIPM-end
								
								if (k > 0) {
									
									// BEGIN: update by 203990 at 20210910
									// 追加判別該用戶所屬的全部保單是否為有效保單, 若全部為無效保單, 就不落地直接回覆聯盟為失敗, 留訊:'不存在有效保單'
									List<PolicyListVo> policyList = policyListService.getUserPolicyList(mapperVo.getCidNo());
									log.info("判斷是否不存在有效保單,policyList.size()="+policyList.size());
									if (policyList!=null && policyList.size() > 0) {
										
										int non_policys = policyList.size();
										for(PolicyListVo pVo : policyList) {
											if("Y".equals(pVo.getApplyLockedFlag())) {
												non_policys--;
											}
										}
	
										if (non_policys <= 0) {
											//不存在有效保單，資料不落地
											vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
											vo.setMsg(NotifyOfNewCaseChangeVo.MSG);
											contactInfoService.updateNcStatusBySeqId(vo);
								
											log.info("-----------------不存在有效保單--调用206接口---");
											//不存在有效保單，
											unParams.put("name", "API206");
											params.put("status", ContactInfoVo.STATUS_3);
											params.put("msg", ContactInfoVo.NON_POLICYS);
											log.info("----------调用206接口,参数---"+params);
											String api206Response = cioService.postForEntity(URL_API206, params, unParams);
											log.info("call URL_API206,strResponse="+api206Response);

										}else {	
											int iRtn = contactInfoService.addContactInfo(mapperVo);
											if(iRtn>0) {//如果有查詢且儲存成功
												vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
												int ncupdate = contactInfoService.updateNcStatusBySeqId(vo);
												log.info("状态已经修改为保戶："+ncupdate);
						
											}
					
											/**
											 * 修改姓名或ID視為「異常件」，回報案件狀態為“Y” msg=已轉為人工處理
											 * 当新名稱和ID有數據,為需要修改
											 * */
											if(StringUtils.isNotBlank(mapperVo.getName()) || StringUtils.isNotBlank(mapperVo.getIdNo())){
												//匯報206接口
												params.put("status", NotifyOfNewCaseChangeVo.NC_STATUS_Y);
												params.put("msg", NotifyOfNewCaseChangeVo.GO_WORK_AHEAD_MSG);
												log.info("----------修改姓名或ID視為「異常件」，回報案件狀態為“Y” msg=已轉為人工處理 调用206接口,参数---"+params);
				
												//聯盟鏈歷程參數
												unParams.put("name", "API206");
												cioService.postForEntity(URL_API206, params, unParams);
											}
										}

									}else {
										//不存在有效保單，資料不落地
										vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
										vo.setMsg(NotifyOfNewCaseChangeVo.MSG);
										contactInfoService.updateNcStatusBySeqId(vo);
										
										log.info("-----------------不存在有效保單--调用206接口---");
										//不存在有效保單，
										unParams.put("name", "API206");
										params.put("status", ContactInfoVo.STATUS_3);
										params.put("msg", ContactInfoVo.NON_POLICYS);
										log.info("----------调用206接口,参数---"+params);
										String api206Response = cioService.postForEntity(URL_API206, params, unParams);
										log.info("call URL_API206,strResponse="+api206Response);
									}
									// END: update by 203990 at 20210910
									
								}else {
									//非保戶，資料不落地
									vo.setNcStatus(NotifyOfNewCaseChangeVo.NC_STATUS_ONE);
									vo.setMsg(NotifyOfNewCaseChangeVo.MSG);
									contactInfoService.updateNcStatusBySeqId(vo);
									
									log.info("-----------------不是保户--调用206接口---");
									//非本公司保 戶，
									unParams.put("name", "API206");
									params.put("status", ContactInfoVo.STATUS_3);
									params.put("msg", ContactInfoVo.NON_INSURED);
									log.info("----------调用206接口,参数---"+params);
									String api206Response = cioService.postForEntity(URL_API206, params, unParams);
									log.info("call URL_API206,strResponse="+api206Response);

								}
							}
						}
					
					}catch(Exception e) {
						log.info(e);
						hashMap.put(CallApiCode.CALL_API203_NO_NUMBER
								,hashMap.get(CallApiCode.CALL_API203_NO_NUMBER)+1);
						Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API203_NO_NUMBER);
						log.info("API203调用失败次数::{}"+integerNoNumber);
						if (boo||(integerNoNumber>=maxGoOnline)) {
							log.info("Call threshold exceeded start  mail");
							callFailedMail(e,"203");
							hashMap.put(CallApiCode.CALL_API203_NO_NUMBER,0);
							boo=false;
							log.info("Call threshold exceeded  send mail");
						}
						
					}
				}//end-for(NotifyOfNewCaseChangeVo vo : newCases)
				
			}//end-if(newCases!=null && newCases.size()>0)

		}//end-if("N".equals(API_DISABLE)){

    	log.info("End API-203 Task.");
	}

	@Value("${cron.cio204.expression.enable: true}")
	public boolean api204Enable;
	/**
	 * 檔案下載
	 */
	@Scheduled(cron = "${cron.cio204.expression}")
	public void callAPI204() {
		if (!api204Enable) {
			return;
		}
		log.info("Start API-204 Task.");
		log.info("API_DISABLE="+API_DISABLE);

		//testcode
//		try {
//			Map<String,String> testparams = new HashMap<>();
//			testparams.put("fileId", "20201230162649-PR1IojO");
//			String teststrResponse = allianceService.postForEntity(URL_API106, testparams);
//		String strResponse = "{   \"code\": \"0\",   \"msg\": \"success\",   \"data\": {     \"name\": \"20200122121250L01-A00001.pdf \",     \"base64\": \"Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=\"}}";
//			String strPath   = "./";
//			String strBase64 = null;
//			String fileName  = null;
//			String msg = null;
//			if(checkLiaAPIResponseValue(teststrResponse,"/code","0")) {
//				strBase64 = MyJacksonUtil.readValue(teststrResponse, "/data/base64");
//				fileName  = MyJacksonUtil.readValue(teststrResponse, "/data/name");
//				msg = MyJacksonUtil.readValue(teststrResponse, "/msg");
//			}
//			if(strBase64!=null) {
//				boolean isSaveOK = converBase64StrToFile(strBase64, strPath+"//"+fileName);
//				
//			}
//		}catch(Exception e) {
//			log.error(e);
//		}

		//testcode


		if("N".equals(API_DISABLE)){
			//获取失败上线次数
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			try {
				//1.聯盟件且沒有下載記錄:INSURANCE_CLAIM_FILEDATAS.NOTIFY_SEQ_ID!=null and and FILE_STATUS='2'
				//FILE_STATUS=2,可下載
				ContactInfoFileDataVo vo = new ContactInfoFileDataVo();
				List<ContactInfoFileDataVo> files = contactInfoService.getFileDataToDownload(vo);

				//2.call api-106下載檔案並於本地儲存
				if(files!=null &&files.size()>0) {
					for(ContactInfoFileDataVo fileVo : files) {
						if(fileVo!=null) {
							//2.1.call api-204
							Map<String,String> params = new HashMap<>();
							params.put("fileId", fileVo.getFileId());

							//聯盟鏈歷程參數
							ContactInfoMapperVo insVo = contactInfoService.getCaseIdBySeqId(fileVo.getContactSeqId());
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-204檔案下載");
							if(insVo!=null && insVo.getCaseId()!=null) {
								unParams.put("caseId", insVo.getCaseId().trim());
							}else {
								unParams.put("caseId", "EMPTY");
							}
							unParams.put("transNum", "");

							String strResponse = cioService.postForEntity(URL_API204, params, unParams);

							//进行判断联盟获取是否有资料
							if(strResponse==null || "".equals(strResponse)){
								hashMap.put(CallApiCode.CALL_API204_NO_NUMBER
										,hashMap.get(CallApiCode.CALL_API204_NO_NUMBER)+1);
								if (hashMap.get(CallApiCode.CALL_API204_NO_NUMBER)>=maxGoOnline) {
									boo=true;
									hashMap.put(CallApiCode.CALL_API204_NO_NUMBER,0);
									throw  new  RuntimeException("URL_API204  Response  is  null ");
								}
							}
							//2.2.回傳的msg='SUCCESS',將base64存成本地檔案
							//String strPath   = fileVo.getPath();//假如strPath="./",即"jboss-eap7/";否則會存在當台LINUX所在的根目錄為起始
							String strPath = FILE_SAVE_PATH;//使用本地設定路徑
							String strBase64 = null;
							String fileName  = null;
							String msg = null;
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
								strBase64 = MyJacksonUtil.readValue(strResponse, "/data/base64");
								fileName  = MyJacksonUtil.readValue(strResponse, "/data/name");
								msg = MyJacksonUtil.readValue(strResponse, "/msg");

								boolean isSaveOK = false;
								if(strBase64!=null) {
									log.info("strPath="+strPath);
									//  直接进行存编码格式Base64  ，此处屏蔽屏Base64
								//	isSaveOK = converBase64StrToFile(strBase64, strPath+fileName);
									fileVo.setFileStatus(InsuranceClaimFileDataVo.FILE_STATUS_DOWNLOADED);
									fileVo.setFileBase64(strBase64);
									fileVo.setMsg(msg);
								}
								if(fileVo.getFileBase64()!=null) {
									//do not print base64 string
									log.info("call URL_API204,fileBase64 is not null.");
								}else {
									log.info("call URL_API204,fileBase64 is null.");
								}
								/***
								 * 模仿将DB数据写出文档
								 * */
								/*Base64(fileVo.getFileBase64(), "111111111111111.pdf");
								log.info("----------------------------------模仿寫入到本地盤符----------------------");
								log.info("isSaveOK=" + isSaveOK);
								log.info("----------------------------------開始寫入完成----------------------");
								*/

								int rtn = contactInfoService.updateFileStatusByFileId(fileVo);

								//TODO  保单理赔一样的逻辑
								if(rtn>0) {//update base64 to TRANS_CONTACT_INFO_FILEDATAS by FILE_ID
									//若TRANS_CONTACT_INFO_FILEDATAS有同FILE_ID一併更新,若更新失敗不能影響檔案內容下載
									try {
										com.twfhclife.eservice.onlineChange.model.TransContactInfoFileDataVo tciFileDataVo =
												new com.twfhclife.eservice.onlineChange.model.TransContactInfoFileDataVo();
										tciFileDataVo.setFileId(fileVo.getFileId());
										tciFileDataVo.setFileBase64(fileVo.getFileBase64());
										int updateTransFileDataRtn = transContactInfoService.updateFileBase64ByFileId(tciFileDataVo);
										log.info("updateFileBase64ByFileId "+updateTransFileDataRtn +" records.");
									}catch(Exception e) {
										log.info("updateFileBase64ByFileId fail.exception="+e.toString());
									}

								}

							}//end code=0

							if(checkLiaAPIResponseValue(strResponse,"/code","10606")) {//error control-檔案查詢查無資料
								msg = MyJacksonUtil.readValue(strResponse, "/msg");

								fileVo.setMsg(msg);
								int rtn = contactInfoService.updateFileStatusByFileId(fileVo);

							}//end code=10606

						}
					}
				}

				/***
				 * API204回写了之后再次模仿将DB数据写出文档
				 * */
			/*	log.info("-------------------------再次或API204 修改的updateFileStatusByFileId  ----------------------");
				log.info("-------------------------再次List<ContactInfoFileDataVo> fileDataVo = contactInfoService.getFileDataToDownload(vo)----------------------");
				List<ContactInfoFileDataVo> fileDataVo = contactInfoService.getFileDataToDownloadSuccess();
				ContactInfoFileDataVo contactInfoFileDataVo = fileDataVo.get(0);
				//模仿从数据写出文档
				Base64(contactInfoFileDataVo.getFileBase64(), "API204  Update FileStatusByFile.pdf");
				log.info("-------------------------再次  獲取寫入完成----------------------");
				*/

			}catch(Exception e) {
				hashMap.put(CallApiCode.CALL_API204_NO_NUMBER
						,hashMap.get(CallApiCode.CALL_API204_NO_NUMBER)+1);
				Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API204_NO_NUMBER);
				log.info("API204调用失败次数::{}"+integerNoNumber);
				if (boo||(integerNoNumber>=maxGoOnline)) {
					log.info("Call threshold exceeded start  mail");
					callFailedMail(e,"204");
					hashMap.put(CallApiCode.CALL_API204_NO_NUMBER,0);
					boo=false;
					log.info("Call threshold exceeded  send mail");
				}
				log.error(e.toString());
			}

		}//end-if


    	log.info("End API-204 Task.");

	}

	/***
	 *模仿将DB数据写出文档
	 * 当前用于测试API 204 的文件数据
	 * */
/*
	public void Base64(String s, String path) throws IOException {
		log.info("----------------------------------開始寫入到本地盤符----------------------");
		byte[] decode = Base64.getDecoder().decode(s);
		OutputStream out = null;
		try {
			out = new FileOutputStream("C:\\Users\\chenhui\\Desktop\\" + path);
			// Base64解码
			for (int i = 0; i < decode.length; ++i) {
				if (decode[i] < 0) {// 调整异常数据
					decode[i] += 256;
				}
			}
			out.write(decode);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
*/

	@Value("${cron.cio206.expression.enable: true}")
	public boolean api206Enable;
	/**
	 * 回報案件(只有聯盟件要回報)
	 * 轉收公司告知首家公司此案件已處理完畢  (即此案件已啟動轉收公司的保全流程)，或是無法處理及其原因。
	 */
	@Scheduled(cron = "${cron.cio206.expression}")
	public void callAPI206() {
		if (!api206Enable) {
			return;
		}
		log.info("Start API-206 Task.");
		log.info("API_DISABLE="+API_DISABLE);

		/**
		 *test code
		 *String strResponse = "{\"code\":\"0\",\"msg\":\"success\",\"data\":null}";
		 * 
		 */

		if("N".equals(API_DISABLE)){
			//获取失败上线次数
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			
			List<ContactInfoMapperVo> listVo = null;
			try {
				listVo = contactInfoService.getContactInfoByHasNotifySeqIdAndBatchId();
			}catch(Exception e) {
				log.info(e);
			}

			
			if(listVo != null && listVo.size() > 0) {
				for (ContactInfoMapperVo vo : listVo) {
					Map<String,String> params = new HashMap<>();
					try {
						Float batchId = vo.getBatchId();
						String caseId = vo.getCaseId();
						log.info("do batchId="+batchId+",caseId="+caseId);
						
						params.put("caseId", caseId);
						
						if(vo.getMsg() == null) {
							
							//1.以BATCH_ID取出TRANS.STATUS尚未完結的筆數(已完結為2,6,7)
							int k = contactInfoService.getCompletedContactInfoByBatchId(batchId);
							if(k > 0) {//有未完結案件，暫不進行回報
								log.info("call URL_API206,不進行回報,有未完結案件,當前BATCH_D="+vo.getBatchId()+",有未滿足status= 2,6,7 ");
								continue;
							}
							
							//2.無未完結案件時
							if(k == 0) {
								//TRANS.STATUS=7,聯盟件於 API-203查詢保全變更案件時,若為異常件已直接回報;故只要有一個STATUS=7即不再回報
								Integer abnormalCount = contactInfoService.getAbnormlContactInfoCountByBatchIdStatus(batchId);
								if(abnormalCount!=null && abnormalCount>0) {
									log.info("call URL_API206,不進行回報,當前BATCH_D="+vo.getBatchId()+",有TRANS.STATUS=7 ");
									
									vo.setStatus(ContactInfoVo.STATUS_Y);
									vo.setMsg(ContactInfoVo.MSG_SUCCESS);
									int rtn = contactInfoService.updateStatusByBatchId(vo);
									log.info("call URL_API206,當前BATCH_D="+vo.getBatchId()+",有status=7,不進行回報聯盟,直接壓CONTACT_INFO.STATUS=Y.");
									
									continue;
								}
								
								//  若有一個不為2,則回報失敗,否則回報為成功
								int statusNum = contactInfoService.getCompletedContactInfoByBatchIdStatus(batchId);
								if(statusNum == 0) {
									params.put("status", ContactInfoVo.STATUS_Y);
									params.put("msg", ContactInfoVo.MSG_SUCCESS );
								}else {
									params.put("status", ContactInfoVo.STATUS_3);
									params.put("msg",  ContactInfoVo.MSG_FAIL);
									log.info("call URL_API206,執行回寫狀態--MSG_FAIL ="+k);
								}
							}
						}

						//非本公司保戶
						if(ContactInfoVo.NON_INSURED.equals(vo.getMsg())) {
							params.put("status", ContactInfoVo.STATUS_3);
							params.put("msg", ContactInfoVo.NON_INSURED);
						}

						Map<String, String> unParams = new HashMap<>();
						unParams.put("caseId", vo.getCaseId());
						unParams.put("transNum", "");
						unParams.put("name", "API-206回報案件");
						String strResponse = cioService.postForEntity(URL_API206, params, unParams);
						log.info("call URL_API206,strResponse="+strResponse);

						//进行判断联盟获取是否有资料
						if(strResponse==null || "".equals(strResponse)){
							hashMap.put(CallApiCode.CALL_API206_NO_NUMBER
									,hashMap.get(CallApiCode.CALL_API206_NO_NUMBER)+1);
							if (hashMap.get(CallApiCode.CALL_API206_NO_NUMBER)>=maxGoOnline) {
								boo=true;
								hashMap.put(CallApiCode.CALL_API206_NO_NUMBER,0);
								throw  new  RuntimeException("URL_API206  Response  is  null ");
							}
						}

						if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
							//vo.setStatus(ContactInfoVo.STATUS_COMPLETED);
							/*異常件不會被變更為成功或失敗了，改為當狀態為「異常件」時即調用API-206回報案件處理”成功”*/
							vo.setStatus(ContactInfoVo.STATUS_Y);
							vo.setMsg(ContactInfoVo.MSG_SUCCESS);
							int rtn = contactInfoService.updateStatusByBatchId(vo);
							log.info("call URL_API206,updateStatusByBatchId="+rtn);

						}
					}catch(Exception e) {
						log.info(e);
						hashMap.put(CallApiCode.CALL_API206_NO_NUMBER
								,hashMap.get(CallApiCode.CALL_API206_NO_NUMBER)+1);
						Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API206_NO_NUMBER);
						log.info("API206调用失败次数::{}"+integerNoNumber);
						if (boo||(integerNoNumber>=maxGoOnline)) {
							log.info("Call threshold exceeded start  mail");
							callFailedMail(e,"206");
							hashMap.put(CallApiCode.CALL_API206_NO_NUMBER,0);
							boo=false;
							log.info("Call threshold exceeded  send mail");
						}
						log.error(e.toString());
					}
				}//end-for (ContactInfoMapperVo vo : listVo)
			}//end-if(listVo != null && listVo.size() > 0)

		}//end-if

    	log.info("End API-206 Task.");

	}

	@Value("${cron.saveCioToEserviceTrans.expression.enable: true}")
	public boolean saveCioToEserviceTransEnable;

	@Scheduled(cron = "${cron.saveCioToEserviceTrans.expression}")
	public void saveCioToEserviceTrans() {
		if (!saveCioToEserviceTransEnable) {
			return;
		}
		//取得未加到eservice.TRANS的case
		//檢核是否為保戶，是否為用戶
		//1.聯盟通知件(NOTIFY_SEQ_ID!=null, CASE_ID!=null, CODE='0' ,MSG is null)
		//2.未送到eservice.trans(TRANS_NUM is null)
		//3.首家保險公司已取得紙本註記(FILE_RECEIVED='1')
		//4.所有附加檔案已下載完成

		log.info("Start saveCioToEserviceTrans.");
		log.info("API_DISABLE="+API_DISABLE);

		//获取失败上线次数
		Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());

		if("N".equals(API_DISABLE)){
			try {
				List<ContactInfoMapperVo> listVo = contactInfoService.getContactInfoByHasNotifySeqIdNoBatchId();
				if(listVo!=null && !listVo.isEmpty()) {

					TransContactInfoDtlVo tic = null;
					TransAddRequest addReq = new TransAddRequest();
					addReq.setSysId("eservice");
					addReq.setTransType(TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
					List<String>  regions = parameterService.getRegionOption();
					for(ContactInfoMapperVo vo : listVo) {
						//使用旧资料来进行判断
						String cidNo = vo.getCidNo();

						TransContactInfoOldVo transContactInfoOldVo = new TransContactInfoOldVo();
						transContactInfoOldVo.setIdNo(cidNo);
						transContactInfoOldVo.setMobile(vo.getcPhone());
						transContactInfoOldVo.setEmail(vo.getcMail());
						addReq.setTransContactInfoOldVo(transContactInfoOldVo);

						//檢核是否為保戶
						int countPIPM = contactInfoService.countPIPMByIdNo(cidNo);
						if(countPIPM>0) {//保戶
							//取userId
							String userId = null;
							userId = contactInfoService.getUserIdByIdNo(cidNo);
							if(userId!=null) {
								//有註冊eservice用戶
								//do nothing.
							}else {
								//未註冊eservice用戶
								userId = "EMPTY";
							}
							addReq.setUserId(userId);

							//設定交易序號
							Float batchId = transService.getBatchIdSequence();

							//2.新增保單號碼
//						    List<String> insClaimsPlans = insuranceClaimService.getInsClaimPlan();						    
						    List<String> policyNos = new ArrayList<String>();
						    
						    policyNos = getUsableUserPolicyListByRocId(cidNo);
						    log.info("CIOAllianceServcieTask get policyNos="+policyNos.toString());

					    	tic = new TransContactInfoDtlVo();
							BeanUtils.copyProperties(vo,tic);
							
							tic.setUserId(userId);

							if(vo != null) {
								//手機
								if(StringUtils.isNotBlank(vo.getPhone())) {
									tic.setMobile(vo.getPhone().trim());
								}

								//要保人地址
								String contactInfoRAddress = vo.getrAddress();
								if(contactInfoRAddress!=null) {
									contactInfoRAddress = contactInfoRAddress.trim();

									if(StringUtils.isNotBlank(contactInfoRAddress)
											&& contactInfoRAddress.length()>=4) {
										tic.setAddressFull(contactInfoRAddress);

										String regionAddress = contactInfoRAddress.substring(3, contactInfoRAddress.length());
										String region = checkContainRegion(regions,regionAddress);
										tic.setRegion(region);
										tic.setCity(contactInfoRAddress.substring(0,3));

										String address = regionAddress.replace(region, "");
										tic.setAddress(address);
									}
								}

								//收費地址
								String contactInfoMAddress = vo.getmAddress();
								if(contactInfoMAddress!=null) {
									contactInfoMAddress = contactInfoMAddress.trim();

									if(StringUtils.isNotBlank(contactInfoMAddress)
											&& contactInfoMAddress.length()>=4) {
										tic.setAddressFullCharge(contactInfoMAddress);

										String regionAddress = contactInfoMAddress.substring(3, contactInfoMAddress.length());
										String region = checkContainRegion(regions,regionAddress);
										tic.setRegionCharge(region);
										tic.setCityCharge(contactInfoMAddress.substring(0,3));

										String address = regionAddress.replace(region, "");
										tic.setAddressCharge(address);

									}
								}

								//email
								if(StringUtils.isNotBlank(vo.getMail())) {
									tic.setEmail(vo.getMail().trim());
								}

								//住家電話
								if(StringUtils.isNotBlank(vo.getHomeTel1())) {
									String telHome = null;
									//hometelcode1
									String hometelcode1 = vo.getHomeTelCode1();
									if(StringUtils.isNotBlank(hometelcode1)) {
										telHome = StringUtils.join(telHome,hometelcode1.trim());
									}
									//hometel1
									telHome = StringUtils.join(telHome,vo.getHomeTel1().trim());

									//hometelext1
									if(vo.getHomeTelExt1()!=null) {
										telHome = StringUtils.join(telHome,"#",vo.getHomeTelExt1().trim());
									}

									tic.setTelHome(telHome);
								}

								tic.setFormCompany(vo.getFrom());
								tic.setToCompany(vo.getTo());
								tic.setLipmName1(vo.getCname());
								tic.setPolicyNoList(policyNos);

								if(StringUtils.isNotBlank(vo.getName())) {
									tic.setName(vo.getName().trim());
								}

								if(StringUtils.isNotBlank(vo.getIdNo())) {
									tic.setIdno(vo.getIdNo().trim());
								}

								//判斷異常件
								//檢核身份證變更／姓名變更
								if(StringUtils.isBlank(vo.getName())) {
									tic.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
								}else {
									tic.setStatus(OnlineChangeUtil.TRANS_STATUS_ABNORMAL);
								}

								if(OnlineChangeUtil.TRANS_STATUS_AUDITED.equals(tic.getStatus())) {
									if(StringUtils.isBlank(vo.getIdNo())) {
										tic.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
									}else {
										tic.setStatus(OnlineChangeUtil.TRANS_STATUS_ABNORMAL);
									}
								}
								log.info("判斷異常件(status=7為異常件):tic.getStatus()=" + tic.getStatus());
							}

							//目前沒有文件暫時屏蔽
							//3.1.塞好FILEDATAS 
//							List<ContactInfoFileDataVo> fileDatas = contactInfoService.getContactInfoFileDataByClaimsSequId(vo.getSeqId());
//							if(fileDatas!=null && fileDatas.size()>0) {
//								List<TransContactInfoFileDataVo> transFileDatas = new ArrayList<TransContactInfoFileDataVo>();
//								for(ContactInfoFileDataVo ivo : fileDatas) {
//									TransContactInfoFileDataVo tvo = new TransContactInfoFileDataVo();
//									tvo.setSeqId(vo.getSeqId());
//									tvo.setFileName(ivo.getFileName());
//									tvo.setFilePath(this.FILE_SAVE_PATH);
//									if("A".equals(ivo.getType())) {
//										tvo.setType("DIAGNOSIS_CERTIFICATE");
//									}else if("B".equals(ivo.getType())){
//										tvo.setType("MEDICAL_RECEIPT");
//									}else {
//										tvo.setType(ivo.getType());
//									}
//									transFileDatas.add(tvo);
//								}
//								tic.setFileDatas(transFileDatas);
//							}
							//end-if

							addReq.setTransContactInfoDtlVo(tic);
							TransContactInfoVo transContactInfoVo = new TransContactInfoVo();
							transContactInfoVo.setBatchId(batchId);
							//transContactInfoVo.setCaseId();
							// transContactInfoVo.setCaseId();
							transContactInfoVo.setCaseId(vo.getCaseId());
							if (vo.getFileDatas()!=null && vo.getFileDatas().size()>0) {
								List<ContactInfoFileDataVo> fileDatas = vo.getFileDatas();
								List<com.twfhclife.eservice.onlineChange.model.ContactInfoFileDataVo> collect
											= fileDatas.stream().map(x -> {
									com.twfhclife.eservice.onlineChange.model.ContactInfoFileDataVo contactInfoFileDataVo
											= new com.twfhclife.eservice.onlineChange.model.ContactInfoFileDataVo();
									contactInfoFileDataVo.setNotifySeqId(x.getNotifySeqId());
									contactInfoFileDataVo.setFileId(x.getFileId());
									contactInfoFileDataVo.setSize(x.getSize());
									contactInfoFileDataVo.setType(x.getType());
									contactInfoFileDataVo.setFileName(x.getFileName());
									contactInfoFileDataVo.setFileStatus(x.getFileStatus());
									contactInfoFileDataVo.setPath(x.getPath());
									contactInfoFileDataVo.setMsg(x.getMsg());
									contactInfoFileDataVo.setSftpPath(x.getSftpPath());
									contactInfoFileDataVo.setFileBase64(x.getFileBase64());
									contactInfoFileDataVo.setRfeId(x.getRfeId());
									contactInfoFileDataVo.setUpdateMsgDate(x.getUpdateMsgDate());
									contactInfoFileDataVo.setCreateDate(x.getCreateDate());
									return contactInfoFileDataVo;
								}).collect(Collectors.toList());
								log.info("----------BeanUtils.copyProperties(fileDatas,objects):{}--------"+collect);
								transContactInfoVo.setFileDatas(collect);
							}
							addReq.setTransContactInfoVo(transContactInfoVo);
							//3.2.call TransAddServiceImpl.addTransRequest()
							TransAddResponse transAddResp = transAddService.addTransRequest(addReq);//這裡會主動先塞好TRANS Table.
							if(transAddResp!=null) {
								//4.更新該筆CONTACT_INFO,表示已送到eservice.TRANS
								if(batchId!=null) {
									vo.setBatchId(batchId);
									int rtn = contactInfoService.updateContactInfoTransNumByNotifySeqId(vo);
								}
							}


						}else {//非保戶
							vo.setMsg(ContactInfoVo.NON_INSURED);
							int rtn = contactInfoService.updateContactInfoMsg(vo);
						}

					}
				}

			}catch(Exception e) {
				log.error(e);

				hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_TRANS
						,hashMap.get(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_TRANS)+1);
				Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_TRANS);
				log.info("API-saveCioToEserviceTrans调用失败次数::{}"+integerNoNumber);
				if (boo||(integerNoNumber>=maxGoOnline)) {
					log.info("Call threshold exceeded start  mail");
					callFailedMail(e,"saveCioToEserviceTrans");
					hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_TRANS,0);
					boo = false;
					log.info("Call threshold exceeded  send mail");
				}
			}
		}

		log.info("End saveCioToEserviceTrans.");
	}

	@Value("${cron.saveCioToEserviceUnionTempTable.expression.enable: true}")
	public boolean saveCioToEserviceUnionTempTableEnable;

	@Scheduled(cron = "${cron.saveCioToEserviceUnionTempTable.expression}")
	public void saveCioToEserviceUnionTempTable() {
		if (!saveCioToEserviceUnionTempTableEnable) {
			return;
		}
		log.info("Start saveCioToEserviceUnionTempTable.");
		log.info("API_DISABLE="+API_DISABLE);
		if("N".equals(API_DISABLE)){
			//获取失败上线次数
			Integer maxGoOnline = Integer.valueOf(this.getAPI_LIMIT_CODENUMBER());
			try {
				List<ContactInfoMapperVo> listIC = contactInfoService.getTransContactInfoByForTask();
				for (ContactInfoMapperVo mapperVo : listIC) {
					mapperVo.setStatus(ContactInfoVo.STATUS_WAITING_FOR_UPLOAD);
					String idNo = mapperVo.getIdNo();
					String name = mapperVo.getName();
					if(!StringUtils.isEmpty(idNo)){
						String cidNo = mapperVo.getCidNo();
						if (idNo.equals(cidNo)){
							mapperVo.setIdNo(null);
						}
					}
					if(!StringUtils.isEmpty(name)){
						String cName = mapperVo.getCname();
						if (name.equals(cName)){
							mapperVo.setName(null);
						}
					}
					log.info("========================saveCioToEserviceUnionTempTable--數據對比之前========================"+mapperVo);
					mapperVo.setHomeTel1(checkChangeAttribute(mapperVo.getHomeTel1(),mapperVo.getHomeTel2()));
					//拆解上傳 住家电话
					if (!StringUtils.isEmpty(mapperVo.getHomeTel1())) {
					log.info("mapperVo.getHomeTel1()={}"+mapperVo.getHomeTel1());
						TaiwanLocalPhoneVo taiwanLocalPhoneVo = TaiwanLocalPhoneUtil.parseLocalPhone(mapperVo.getHomeTel1());
						mapperVo.setHomeTel1(taiwanLocalPhoneVo.getPhoneNo());
						mapperVo.setHomeTelCode1(taiwanLocalPhoneVo.getLocalCode());
					}

					/**
					 * 台银 ——> 联盟  数据的对比
					 */
					// 20220908 by 203990
					/// 保全變更資料Step3的Mobile用原資料,寫入ContantInfo的Phone為空值,導致送給聯盟的cPhone為空值的BUG調整
					//mapperVo.setPhone(checkChangeAttribute(mapperVo.getPhone(),mapperVo.getcPhone()));
					mapperVo.setMail(checkChangeAttribute(mapperVo.getMail(),mapperVo.getcMail()));
					mapperVo.setrAddress(checkChangeAttribute(mapperVo.getrAddress(),mapperVo.getcAddress()));
					mapperVo.setmAddress(checkChangeAttribute(mapperVo.getmAddress(),mapperVo.getCmAddress()));
					//ContactInfo  表中缺少ContactInfo  欄目,待確認是否添加此欄目
					mapperVo.setTelOffice(checkChangeAttribute(mapperVo.getTelOffice(),mapperVo.getcTelOffice()));
					
					//hometel2-UI件無第二家用電話
					mapperVo.setHomeTel2(null);
					mapperVo.setHomeTelCode2(null);
					mapperVo.setHomeTelExt2(null);
					
					//mapperVo.
					log.info("========================saveCioToEserviceUnionTempTable--數據對比之后========================"+mapperVo);
					int iRtn = contactInfoService.addContactInfo(mapperVo);
				}
		    }catch(Exception e) {
				hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS
						,hashMap.get(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS)+1);
				Integer integerNoNumber = hashMap.get(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS);
				log.info("API-saveCioToEserviceUnionTempTable调用失败次数::{}"+integerNoNumber);
				if (boo||(integerNoNumber>=maxGoOnline)) {
					log.info("Call threshold exceeded start  mail");
					callFailedMail(e,"saveCioToEserviceUnionTempTable");
					hashMap.put(CallApiCode.CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS,0);
					boo=false;
					log.info("Call threshold exceeded  send mail");
				}
		 		log.error(e);
			   }
			}

		log.info("End saveCioToEserviceUnionTempTable.");
	}

	/**
	 * 台银 ——> 联盟  数据的对比
	 * @param dtlAttributeValue 新数据
	 * @param oldAttributeValue 旧数据
	 * @return 返回存入DB数据
	 */
	private String checkChangeAttribute(String dtlAttributeValue, String oldAttributeValue){
		String rtn = null;

		if (StringUtils.isNotBlank(dtlAttributeValue)) {//新值不為空
			if (!dtlAttributeValue.equals(oldAttributeValue)) {
				//新舊欄位值不相同
				rtn = dtlAttributeValue;
			} else {
				//新舊欄位值相同
				//給壽險核心檔案的對應欄位為空值
				rtn = "";
			}
		} else {//新值為空
			rtn = "";
		}

		return rtn;
	}


	private String checkContainRegion(List<String> regions, String address) {
		for (String str : regions) {
			address.contains(str);
			return str;
		}
		return "";
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
			System.out.println("checkLiaAPIResponseValue="+code);
			if(checkValue.equals(code)) {//success
				b = true;
			}
		}
		System.out.println("checkLiaAPIResponseValue,return="+b);
		return b;
	}
	/** 20220629 by 203990  非連線問題, 上傳失敗寄送信件給管理者
	 * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
	 * @param responseJsonString
	 * @param pathFieldName ex:"/code"
	 * @param checkValue ex:"0"
	 * @return boolean
	 */
	private boolean checkLiaAPIResponseValueAndSendEmail(String responseJsonString,String pathFieldName,String checkValue,ContactInfoMapperVo icvo) throws Exception{
		boolean b = false;

		if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
			String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
			System.out.println("checkLiaAPIResponseValue="+code);
			String transNum = icvo.getTransNum();
			if(checkValue.equals(code)) {//success
				b = true;
			}
			else {
				// 20220629 by 203990
				// 非連線上傳失敗, 通知後台管理人員
				Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
				//發送系統管理員
				long timeMillis = System.currentTimeMillis();
				List<String> receivers = new ArrayList<String>();
				receivers = (List)mailInfo.get("receivers");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("EMAIL",EMailTemplate.INSURED_EMAIL_IS_NULL);
				paramMap.put("DATA",transNum);
				paramMap.put("EXCEPTION_LOG",responseJsonString);
				MessageTriggerRequestVo voCIO = new MessageTriggerRequestVo();
				voCIO.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_024);
				voCIO.setSendType("email");
				voCIO.setMessagingReceivers(receivers);
				voCIO.setParameters(paramMap);
				voCIO.setSystemId(ApConstants.SYSTEM_ID);
				//進行發送通信
				String resultSYSMailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
				log.info(ApConstants.TRANSFER_MAIL_024 + resultSYSMailMsg);

				String msg = MyJacksonUtil.readValue(responseJsonString, "/msg");
				icvo.setCaseId("fake_pass");
				icvo.setCode(code);
				icvo.setMsg(msg);
				icvo.setStatus(ContactInfoVo.STATUS_WAITING_FOR_UPLOAD);

				log.info("===========UI件呼叫完 API201 非連線上傳失敗, 通知後台管理人員==========="+transNum+":fake_pass");
				contactInfoService.updateCaseIdByContactSeqId(icvo);
			}
		}
		System.out.println("checkLiaAPIResponseValue,return="+b);
		return b;
	}

	/**
	 * Convert File(ex:jpg,pdf) to Base64
	 * @param filePath
	 * @return String
	 */
	private String converFileToBase64Str(String filePath) {
		String encodedString = null;
		try {
			if(filePath!=null) {
				log.info("input filePath="+filePath);

				byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			}else {
				log.error("input filePath is null.");
			}
		}catch(Exception e) {
			log.error(e);
		}

		return encodedString;
	}

	/**
	 * Convert Base64 To File(ex:jpg,pdf)
	 * @param base64EencodedString
	 * @param outputFileName
	 * @return boolean
	 */
	private boolean converBase64StrToFile(String base64EencodedString,String outputFileName) {
		boolean rtn = false;
		try{
			if(base64EencodedString!=null && outputFileName!=null) {
				log.info("input base64EencodedString is not null.");
				log.info("input outputFileName="+outputFileName);
				//log.info("input base64EencodedString="+base64EencodedString);
				String str=new String(Base64.getDecoder().decode(base64EencodedString));
				log.info("Base64.getDecoder()  地址:"+str);
				byte[] decodedBytes =str.getBytes();
				//log.info("decodedBytes:"+decodedBytes);
				FileUtils.writeByteArrayToFile(new File(outputFileName), decodedBytes);
				rtn = true;
			}else {
				log.error("input base64EencodedString or outputFileName is null.");
			}

		}catch(Exception e) {
			log.error(e);
		}

		return rtn;
	}


	private  String getFileBase64(ContactInfoFileDataVo fileVo ) throws Exception {
		if(fileVo==null) {
			return null;
		}
		String fileBase64   = fileVo.getFileBase64();
		String pathFileName = fileVo.getPath() + "/" + fileVo.getFileName();
		if (fileBase64!=null && !"".equals(fileBase64)) {
			if(fileVo.getFileName()!=null && !fileVo.getFileName().toLowerCase().endsWith("pdf")) {//不是PDF檔就轉成PDF
				log.info("img-base64 is not  null ");
				byte[] decode = Base64.getDecoder().decode(fileBase64);
				File file = FileToBase64Str.convertToPdfFile(decode);
				byte[] fileContent = FileUtils.readFileToByteArray(file);
				fileBase64 = Base64.getEncoder().encodeToString(fileContent);
				log.info("imgToPdf-base64:"+fileBase64);
			}else {
				log.info("PDF 直接返回数据:"+fileBase64);
				return fileBase64;
			}
		}else {
		 	//base64,没有走地址,获取Base64
			fileBase64 = FileToBase64Str.converFileToBase64Str(pathFileName);
		}
		return  fileBase64;
	}


	public static void main(String[] args) throws Exception{
		CIOAllianceServiceTask task = new CIOAllianceServiceTask();

		//task.callAPI201();

		//test conver to object
//		String teststrResponse = "{\"code\": \"0\",\"msg\": \"SUCCESS\",\"data\": {\"name\": \"王大明\",\"idNo\": \"A123456789\",\"birdate\": \"19910415\",\"phone\": \"0912345678\",\"zipCode\": \"70157\",\"address\": \"台北市中正區信義路一段 21-3號\",\"mail\": \" abc@test.com.tw \",\"paymentMethod\": \"1\",\"bankCode\": \"004\",\"branchCode\": \"0107\",\"bankAccount\": \"12345678901234\",\"applicationDate\": \"20190105\",\"applicationTime\": \"1520\",\"applicationItem\": \"1\",\"job\": \"老師\",\"jobDescr\": \"工作描述\",\"accidentDate\": \"20190101\",\"accidentTime\": \"1520\",\"accidentCause\": \"1\",\"accidentLocation\": \"台北市中正區信義路一段\",\"accidentDescr\": \"遭計程車追撞\",\"policeStation\": \"臺北市政府警察局大安分局安和路派出所\",\"policeName\": \"王小明\",\"policePhone\": \"0987654321\",\"policeDate\": \"20190101\",\"policeTime\": \"1530\",\"from\": \"L01\",\"to\": [{\"companyId\": \"L02\"}, {\"companyId\": \"L03\"}],\"fileDatas\": [{\"fileId\": \"48c37063-f09a-4934-be64-127574b640c5\",\"size\": \"300\",\"type\": \"A\",\"fileName\": \"20200122121250L01-A00001-A.pdf\",\"fileStatus\": \"1\",\"path\": \"/L01/202003/wKODkASHSAiMmM5P77JdYg/\"}, {\"fileId\": \"fd2406de-3c26-45b4-8518-497f856cee52\",\"size\": \"300\",\"type\": \"B\",\"fileName\": \"20200122121250L01-A00001-B.pdf\",\"fileStatus\": \"1\",\"path\": \"/L01/202003/wKODkASHSAiMmM5P77JdYg/\"}]}}";
//		String dataString = MyJacksonUtil.getNodeString(teststrResponse, "data");
//		System.out.println("dataString="+dataString);
//		Object obj = MyJacksonUtil.json2Object(dataString, InsuranceClaimVo.class);
//		if(obj!=null) {
//			InsuranceClaimVo testicvo = (InsuranceClaimVo)obj;
//			Gson gson = new Gson(); 
//			String jsonString = gson.toJson(testicvo);
//			System.out.println("gson.toJson(testicvo)="+jsonString);
//		}

		//test for file
//		String str = task.converFileToBase64Str("C:\\Users\\blflo\\Downloads\\Baeldung.pdf");
//		System.out.println("base64="+str);
//		task.converBase64StrToFile(str, "C:\\\\Users\\\\blflo\\\\Downloads\\\\Baeldung_new.pdf");

		//test bean copy
		//task.testBeanCopy();

		//test join
		String str = null;
		str = StringUtils.join("aa","#",str);
		System.out.println("***"+str+"***");

	}

	/**
	 * 取得未鎖定保單清單
	 * @param rocId
	 * @return List<String>
	 */
	private List<String> getUsableUserPolicyListByRocId(String rocId){
		List<String> policyNos = new ArrayList<String>();
		if(StringUtils.isEmpty(rocId)) {
			return policyNos;
		}
		
		List<PolicyListVo> policyList = policyListService.getUserPolicyList(rocId);
		// 處理保單狀態是否鎖定
		if (policyList != null) {
//			List<PolicyListVo> handledPolicyList = transService.handleGlobalPolicyStatusLocked(policyList,userId, TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
//			log.info("--------handledPolicyList--------"+ handledPolicyList.size());
//			transContactInfoService.handlePolicyStatusLocked(handledPolicyList);
//			transService.handleVerifyPolicyRuleStatusLocked(handledPolicyList,TransTypeUtil.CONTACT_INFO_PARAMETER_CODE);
			for(PolicyListVo pVo : policyList) {
				if(!"Y".equals(pVo.getApplyLockedFlag())) {
					String policyNo = pVo.getPolicyNo();
					policyNos.add(policyNo);
				}
			}
		}
		
		return policyNos;
	}

	/**
	 * API调用失败，超过上线阈值，进行发送邮件
	 * @param e  API 端口
	 * @param code  异常信息
	 */
	private  void  callFailedMail(Exception e,String  code){
		try {
			Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			paramMap.put("PROGRAM_NAME", stackTraceElement.getClassName()+":"+stackTraceElement.getMethodName());
			paramMap.put("NUMBER", new String().valueOf(stackTraceElement.getLineNumber()));
			paramMap.put("CODE", code);
			paramMap.put("DATA", CallApiDateFormatUtil.getCurrentTimeString());
			
			//發送系統管理員
			List<String> receivers = new ArrayList<String>();
			//receivers = (List)mailInfo.get("receivers");
			//聯盟相關API調不通【無回應】管理人員
			String mailTo = parameterService.getParameterValueByCode("eservice_adm", "API_NO_MAIL_012");
			if(StringUtils.isNotEmpty(mailTo)) {
				String[] mails = mailTo.split(";");
				for(String mail : mails) {
					receivers.add(mail);
				}
			}

			MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
			vo.setMessagingTemplateCode(ApConstants.API_NO_MAIL_012);
			vo.setSendType("email");
			vo.setMessagingReceivers(receivers);
			vo.setParameters(paramMap);
			vo.setSystemId(ApConstants.SYSTEM_ID);
			log.info("MessageTriggerRequestVo="+vo);
			//进行发送通信
			String resultMsg = messagingTemplateService.triggerMessageTemplate(vo);
			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
				log.info("Send message successfully complete.");
			} else {
				log.info("Failed to send message successfully");
			}
		}catch (Exception ex){
			log.info("Failed to send message successfully:"+ex.getMessage());
		}
	}


}
