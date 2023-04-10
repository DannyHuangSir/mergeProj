package com.twfhclife.scheduling.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.alliance.model.CompanyVo;
import com.twfhclife.alliance.model.InsuranceClaimFileDataVo;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.alliance.service.impl.AllianceServiceImpl;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.service.MailService;
import com.twfhclife.generic.service.SmsService;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.MyJacksonUtil;

@Component
public class AllianceServiceTask {
	
	Log log = LogFactory.getLog(AllianceServiceTask.class);
	
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
	
	//@Value("${alliance.api101.url}")
	public String URL_API101;
	
	//@Value("${alliance.api102.url}")
	public String URL_API102;
	
	//@Value("${alliance.api103.url}")
	public String URL_API103;
	
	//@Value("${alliance.api104.url}")
	public String URL_API104;
	
	//@Value("${alliance.api105.url}")
	public String URL_API105;
	
	//@Value("${alliance.api106.url}")
	public String URL_API106;
	
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
	
	@PostConstruct
	public void doAllianceServiceTask() {
		List<ParameterVo> resultSCHList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_PRO_SCH);
    	List<ParameterVo> resultURLList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_PRO_API_URL);
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
	    		if ("alliance.api101.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API101(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api102.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API102(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api103.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API103(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api104.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API104(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api105.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API105(parameterItem.getParameterValue());
	    		}
	    		if ("alliance.api106.url".equals(parameterItem.getParameterName())) {
	    			this.setURL_API106(parameterItem.getParameterValue());
	    		}
	    	});
    	}
    	if (resultSCHList != null) {
    		resultSCHList.forEach(parameterItem -> {
    			if ("cron.api.disable".equals(parameterItem.getParameterName())) {
        			this.setAPI_DISABLE(parameterItem.getParameterValue());
        		}else {
        			if (System.getProperty(parameterItem.getParameterName()) == null) {
        				System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
        			}
        		}
            });
    	}
	}

	@Value("${cron.api101.expression.enable: true}")
	public boolean api101Enable;

	/**
	 * 理賠申請書上傳
	 */
	@Scheduled(cron = "${cron.api101.expression}")
	public void callAPI101() {
		if (!api101Enable) {
			return;
		}
		log.info("Start API-101 Task.");
		log.info("API_DISABLE="+API_DISABLE);

		//testcode
//		try {
//			InsuranceClaimVo testicvo = new InsuranceClaimVo();
//			testicvo.setName("鑫守二");
//			testicvo.setIdNo("A1234567890");
//			testicvo.setBirdate("19801115");
//			testicvo.setPhone("0910000000");
//			testicvo.setPaymentMethod("1");//匯款
//			testicvo.setBankCode("012");
//			testicvo.setBankAccount("00123215456487");
//			testicvo.setApplicationDate("20210119");
//			testicvo.setApplicationTime("1700");
//			testicvo.setApplicationItem("1");
//			testicvo.setAccidentDate("20201101");
//			testicvo.setAccidentTime("1330");
//			testicvo.setAccidentCause("1");//疾病
//			testicvo.setFrom("L01");//臺銀
//			
//			List<CompanyVo> tovo = new ArrayList<CompanyVo>();
//			CompanyVo vo1 = new CompanyVo();
//			vo1.setCompanyId("L02");
//			tovo.add(vo1);
//			CompanyVo vo2 = new CompanyVo();
//			vo2.setCompanyId("L04");
//			tovo.add(vo2);
//			testicvo.setTo(tovo);
//			
//			List<InsuranceClaimFileDataVo> fileDatas = new ArrayList<InsuranceClaimFileDataVo>();
//			InsuranceClaimFileDataVo file1 = new InsuranceClaimFileDataVo();
//			file1.setType("A");
//			file1.setFileName("20200122121250L01-A00001-A.pdf");
//			fileDatas.add(file1);
//			InsuranceClaimFileDataVo file2 = new InsuranceClaimFileDataVo();
//			file2.setType("B");
//			file2.setFileName("20200122121250L01-A00001-B.pdf");
//			fileDatas.add(file2);
//			testicvo.setFileDatas(fileDatas);
//			
//			Gson gson = new Gson(); 
//	        String json = gson.toJson(testicvo);
//	        log.info("json="+json);
//
//			String strResponse = allianceService.postForEntity(URL_API101, testicvo);
//			log.info("strResponse="+strResponse);
//		}catch(Exception e) {
//			log.error(e);
//		}
		//testcode
		
		if("N".equals(API_DISABLE)){
			int rtn = -1;
			try {
				//1.取得理賠申請上傳資料物件(Request)
				List<InsuranceClaimMapperVo> listIC = claimChainService.getInsuranceClaimByNoCaseId();
				//2.call api-101
				if(listIC!=null && !listIC.isEmpty() && listIC.size()>0) {
					for (InsuranceClaimMapperVo icvo : listIC) {
						if(icvo!=null) {
							//3.call api-101 to upload.
							String strResponse = allianceService.postForEntity(URL_API101, icvo, "API-101理賠申請書上傳");
							log.info("call URL_API101,strResponse="+strResponse);
							//3-1.get api-101 response, update caseId, fileId to db.
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
								String caseId = MyJacksonUtil.readValue(strResponse, "/data/caseId");
								log.info("caseId="+caseId);
								String msg = MyJacksonUtil.readValue(strResponse, "/msg");
								icvo.setCaseId(caseId);
								icvo.setCode(CODE_SUCCESS);
								icvo.setMsg(msg);
								icvo.setStatus(InsuranceClaimVo.STATUS_UPLOADED);
								
								//依fileName,塞好回傳的fileId-start
								boolean fileIdSaveOK = false;
								List<InsuranceClaimFileDataVo> targetFileDatas = icvo.getFileDatas();
								if(targetFileDatas!=null && !targetFileDatas.isEmpty()) {
									String jsonFileDatas = MyJacksonUtil.getNodeString(strResponse, "data");
									log.info("jsonFileDatas = "+jsonFileDatas);
									Object obj = MyJacksonUtil.json2Object(jsonFileDatas, InsuranceClaimVo.class);
									if(obj!=null) {
										InsuranceClaimVo tempICVo = (InsuranceClaimVo)obj;
										List<InsuranceClaimFileDataVo> tempFileDatas = tempICVo.getFileDatas();
										Map<String,String> mapFileId = new HashMap<String,String>();
										for(InsuranceClaimFileDataVo tempVo : tempFileDatas) {
											mapFileId.put(tempVo.getFileName(), tempVo.getFileId());
										}
										for(InsuranceClaimFileDataVo targetVo : targetFileDatas) {
											targetVo.setFileId(mapFileId.get(targetVo.getFileName()));
											fileIdSaveOK = true;
										}
									}
								}
								icvo.setFileDatas(targetFileDatas);
								//依fileName,塞好回傳的fileId-end
								
								if(fileIdSaveOK) {
									rtn = claimChainService.updateCaseIdByClaimSeqId(icvo);
								}
								
								if(rtn>0) {
									//更新INSURANCE_CLAIM
									rtn = claimChainService.updateInsuranceAfterUploadToAlliance(icvo);
								}
								
							}
							
						}
					}
				
				}
				
			}catch(Exception e) {
				log.error(e.toString());
			}
		}//end-if
    	
		
    	log.info("End API-101 Task.");
	}

	@Value("${cron.api102.expression.enable: true}")
	public boolean api102Eanbel;

	/**
	 * 上傳收到案件之紙本註記</br>
	 * 首家收到所有紙本文件後，進行這個案件的註記
	 */
	@Scheduled(cron = "${cron.api102.expression}")
	public void callAPI102() {
		if (!api102Eanbel) {
			return;
		}
		log.info("Start API-102 Task.");
		log.info("API_DISABLE="+API_DISABLE);
		
		//testcode
//		try {
//			Map<String,String> testparams = new HashMap<>();
//			testparams.put("caseId", "20201216163030-DjYgkAC");
//			String teststrResponse = allianceService.postForEntity(URL_API102, testparams);
//			log.info("teststrResponse="+teststrResponse);
//			String testapiCode = MyJacksonUtil.readValue(teststrResponse, "/code");
//			log.info("testapiCode="+testapiCode);
//		}catch(Exception e) {
//			log.error(e);
//		}
		//testcode
		
		if("N".equals(API_DISABLE)){
			
			try {
				//NOTIFY_SEQ_ID is null,STATUS!=已撤消，且傳送聯盟=Y,收到紙本FILE_RECEIVED=1
				List<InsuranceClaimMapperVo> listIC = 
						claimChainService.getInsuranceClaimByFileReceived(InsuranceClaimMapperVo.FILE_RECEIVED_YES);
				if(listIC!=null && listIC.size()>0) {
					for(InsuranceClaimMapperVo vo : listIC) {
						//all API-102
						String apiCode = null;
						Map<String,String> params = new HashMap<>();
						params.put("caseId", vo.getCaseId());
						//聯盟鏈歷程參數
						Map<String,String> unParams = new HashMap<>();
						unParams.put("name", "API-102上傳收到案件之紙本註記");
						unParams.put("caseId", vo.getCaseId());
						unParams.put("transNum", vo.getTransNum());
						
						String strResponse = allianceService.postForEntity(URL_API102, params, unParams);
						
						if(strResponse!=null) {
							apiCode = MyJacksonUtil.readValue(strResponse, "/code");
						}
						
						//code=0,update FILE_RECEIVED=OK
						if("0".equals(apiCode)) {
							vo.setFileReceived(InsuranceClaimVo.FILE_RECEIVED_OK);
							int updateOK = claimChainService.updateFileReceived(vo);
						}
					}
				}
			}catch(Exception e) {
				log.error(e.toString());
			}
		}//end-if
		
    	log.info("End API-102 Task.");

	}

	@Value("${cron.api103.expression.enable: true}")
	public boolean api103Enable;
	/**
	 * 查詢是否收到案件之紙本
	 */
	@Scheduled(cron = "${cron.api103.expression}")
	public void callAPI103() {
		if (!api103Enable) {
			return;
		}
		log.info("Start API-103 Task.");
		log.info("API_DISABLE="+API_DISABLE);

		if("N".equals(API_DISABLE)){
			try {
				//testcode
//				Map<String,String> testparams = new HashMap<>();
//				testparams.put("caseId", "20201230162649-RZSqGj0");
//				String teststrResponse = allianceService.postForEntity(URL_API103, testparams);
//				String testfilereceived = null;
//				if(checkLiaAPIResponseValue(teststrResponse,"/code","0")) {
//					testfilereceived = MyJacksonUtil.readValue(teststrResponse, "/data/fileReceived");
//				}
//				log.info("testfilereceived="+testfilereceived);
				//testcode
	
				//1.以INSURANCE_CLAIM的NOTIFY_SEQ_ID is not null,caseId!=null,code=0資料call api-103，
				List<InsuranceClaimMapperVo> vos = claimChainService.getAllianceCaseByFileReceivedNotYet();
				
				//2.update to INSURANCE_CLAIM.FILE_RECEIVED
				if(vos!=null && vos.size()>0) {
					for(InsuranceClaimMapperVo vo : vos) {
						if(vo!=null) {
							String fileReceived = null;
							//2.1.CALL API103,fileReceived=?
							Map<String,String> params = new HashMap<>();
							params.put("caseId", vo.getCaseId());
							
							//聯盟鏈歷程參數
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-103查詢是否收到案件之紙本");
							unParams.put("caseId", vo.getCaseId());
							unParams.put("transNum", vo.getTransNum());
							String strResponse = allianceService.postForEntity(URL_API103, params, unParams);
							
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
								fileReceived = MyJacksonUtil.readValue(strResponse, "/data/fileReceived");
							}
	
							//2.2.UPDATE-"1":收到,"2":沒收到
							if(InsuranceClaimMapperVo.FILE_RECEIVED_YES.equals(fileReceived) 
									|| InsuranceClaimMapperVo.FILE_RECEIVED_NO.equals(fileReceived)){//只有1 or 2是合法值
								vo.setFileReceived(fileReceived);
								int rtn = claimChainService.updateFileReceived(vo);
								log.info("***Try to update INSURANCE_CLAIM.FILE_RECEIVED="+fileReceived+",CASE_ID="+vo.getCaseId()+"***");
								log.info("***update rtn="+rtn+"***");
								
								if(InsuranceClaimMapperVo.FILE_RECEIVED_YES.equals(fileReceived)) {
									//1.try to update TRANS_INSURANCE_CLAIM,set FILE_RECEIVED='1' by CASE_ID
									vo.setFileReceived(InsuranceClaimMapperVo.FILE_RECEIVED_YES);
									int updateTICcount = claimChainService.updateTransInsuranceClaimFileReceived(vo);
									log.info("***Try to update TRANS_INSURANCE_CLAIM.FILE_RECEIVED=1,CASE_ID="+vo.getCaseId()+"***");
									log.info("***update updateTICcount="+updateTICcount+"***");
									//2.update successed, send mail to systemAdmin.
								}
							}
							
						}
					}
				}
	
			}catch(Exception e) {
				log.error(e.toString());
			}
		}//end-if
		
    	log.info("End API-103 Task.");

	}

	@Value("${cron.api104.expression.enable: true}")
	public boolean api104Enable;

	/**
	 * 檔案上傳
	 */
	@Scheduled(cron = "${cron.api104.expression}")
	public void callAPI104() {
		if (!api104Enable) {
			return;
		}
		log.info("Start API-104 Task.");
		log.info("API_DISABLE="+API_DISABLE);
		
		//testcode
//		try {
//			Map<String,String> testparams = new HashMap<>();
//			testparams.put("fileId", "20201216163030-3cuzhNS");
//			String teststrBase64 = this.converFileToBase64Str("./202012163030L01-2810002021-A.pdf");
//			testparams.put("base64", teststrBase64);
//			String teststrResponse = allianceService.postForEntity(URL_API104, testparams);
//			//去保險科技共享平台查看檔案
//		}catch(Exception e){
//			log.error(e);
//		}
		//testcode
		
		
		if("N".equals(API_DISABLE)){
			
			try {
				//1.上傳件且上傳且未成功:NOTIFY_SEQ_ID=null and UPPER(MSG)!='SUCCESS'
				InsuranceClaimFileDataVo vo = new InsuranceClaimFileDataVo();
				List<InsuranceClaimFileDataVo> files = claimChainService.getFileDataToUpload(vo);
				
				//2.call api-104
				if(files!=null &&files.size()>0) {
					for(InsuranceClaimFileDataVo fileVo : files) {
						if(fileVo!=null) {
							//2.1.call api-104
							Map<String,String> params = new HashMap<>();
							params.put("fileId", fileVo.getFileId());
							
							String strBase64 = null;
							if(fileVo.getFileBase64()!=null ) {
								log.info("取用自file_base64 string.");
								strBase64 = converNonPDFBase64ToPDFBase64(fileVo.getFileBase64());
							}else {
								//嘗試取用實體位置文件檔
								log.info("取用自實體檔路徑.");
								strBase64 = this.converFileToBase64Str(fileVo.getPath()+"/"+fileVo.getFileName());
							}
							params.put("base64", strBase64);
							
							if(strBase64!=null) {
								log.info("strBase64.length()="+strBase64.length());
							}else {
								log.info("strBase64 is null.");
							}
							
							//聯盟鏈歷程參數
							InsuranceClaimMapperVo insVo = claimChainService.getCaseIdBySeqId(fileVo.getClaimSeqId());
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-104檔案上傳");
							unParams.put("caseId", insVo.getCaseId());
							unParams.put("transNum", insVo.getTransNum());
							String strResponse = allianceService.postForEntity(URL_API104, params,unParams);
							
							//2.2.若回傳msg='SUCCESS',
							//update FILE_STATUS='1',INSURANCE_CLAIM_FILEDATAS.MSG='SUCCESS',UPDATE_MSG_DATE=getdate()
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {//已有檔案上傳時聯盟會擋重覆上傳
								String msg = MyJacksonUtil.readValue(strResponse, "/msg");
								fileVo.setMsg(msg);
								fileVo.setFileStatus(InsuranceClaimFileDataVo.FILE_STATUS_UPLOADED);
								
								int rtn = claimChainService.updateFileStatusByFileId(fileVo);
							}

						}
					}
				}
				
			}catch(Exception e) {
				log.error(e.toString());
			}
		}//end-if
		
    	log.info("End API-104 Task.");

	}

	@Value("${cron.api105.expression.enable: true}")
	public boolean api105Enable;
	/**
	 * 查詢理賠案件
	 */
	@Scheduled(cron = "${cron.api105.expression}")
	public void callAPI105() {
		if (!api105Enable) {
			return;
		}
		log.info("Start API-105 Task.");
		log.info("API_DISABLE="+API_DISABLE);
		
		//testcode
//		try {
//			String teststrResponse = "{\"code\": \"0\",\"msg\": \"SUCCESS\",\"data\": {\"name\": \"王大明\",\"idNo\": \"A123456789\",\"birdate\": \"19910415\",\"phone\": \"0912345678\",\"zipCode\": \"70157\",\"address\": \"台北市中正區信義路一段 21-3號\",\"mail\": \" abc@test.com.tw \",\"paymentMethod\": \"1\",\"bankCode\": \"004\",\"branchCode\": \"0107\",\"bankAccount\": \"12345678901234\",\"applicationDate\": \"20190105\",\"applicationTime\": \"1520\",\"applicationItem\": \"1\",\"job\": \"老師\",\"jobDescr\": \"工作描述\",\"accidentDate\": \"20190101\",\"accidentTime\": \"1520\",\"accidentCause\": \"1\",\"accidentLocation\": \"台北市中正區信義路一段\",\"accidentDescr\": \"遭計程車追撞\",\"policeStation\": \"臺北市政府警察局大安分局安和路派出所\",\"policeName\": \"王小明\",\"policePhone\": \"0987654321\",\"policeDate\": \"20190101\",\"policeTime\": \"1530\",\"from\": \"L01\",\"to\": [{\"companyId\": \"L02\"}, {\"companyId\": \"L03\"}],\"fileDatas\": [{\"fileId\": \"48c37063-f09a-4934-be64-127574b640c5\",\"size\": \"300\",\"type\": \"A\",\"fileName\": \"20200122121250L01-A00001-A.pdf\",\"fileStatus\": \"1\",\"path\": \"/L01/202003/wKODkASHSAiMmM5P77JdYg/\"}, {\"fileId\": \"fd2406de-3c26-45b4-8518-497f856cee52\",\"size\": \"300\",\"type\": \"B\",\"fileName\": \"20200122121250L01-A00001-B.pdf\",\"fileStatus\": \"1\",\"path\": \"/L01/202003/wKODkASHSAiMmM5P77JdYg/\"}]}}";
//			String dataString = MyJacksonUtil.getNodeString(teststrResponse, "data");
//			System.out.println("dataString="+dataString);
//			Object obj = MyJacksonUtil.json2Object(dataString, InsuranceClaimVo.class);
//			if(obj!=null) {
//				InsuranceClaimVo testicvo = (InsuranceClaimVo)obj;
//				Gson gson = new Gson(); 
//				String jsonString = gson.toJson(testicvo);
//				System.out.println("gson.toJson(testicvo)="+jsonString);
//			}
//		}catch(Exception e) {
//			log.error(e);
//		}
		//testcode
		
		
		if("N".equals(API_DISABLE)){
			
			try {
				InsuranceClaimVo icvo = null;
				//1.取得聯盟通知新案件資料物件
				ArrayList<NotifyOfNewCaseVo> newCases = 
						claimChainService.getNofifyOfNewCaseByNcStatus(NotifyOfNewCaseVo.NC_STATUS_ZERO);
				
				//2.call api-105
				if(newCases!=null && newCases.size()>0) {
					for(NotifyOfNewCaseVo vo : newCases) {
						icvo = null;//init
						if(vo!=null && vo.getCaseId()!=null) {
							String caseId      = vo.getCaseId();
							Float  nofifySeqId = vo.getSeqId();
							//call api-105
							Map<String,String> params = new HashMap<>();
							params.put("caseId", vo.getCaseId());
							
							//聯盟鏈歷程參數
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-105查詢理賠案件");
							unParams.put("caseId", vo.getCaseId());
							unParams.put("transNum", null);
							String strResponse = allianceService.postForEntity(URL_API105, params, unParams);
							
							//icvo , get data form Api105;
							if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
								String dataString = MyJacksonUtil.getNodeString(strResponse, "data");
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
								
								Object obj = MyJacksonUtil.json2Object(dataString, InsuranceClaimVo.class);
								log.info("after MyJacksonUtil.json2Object");
								if(obj!=null) {
									//log.info("obj is not null.");
									icvo = (InsuranceClaimVo)obj;
									icvo.setCode("0");

									if(listTo!=null) {
										icvo.setTo(listTo);
									}
//									Gson gson = new Gson(); 
//									String jsonString = gson.toJson(icvo);
//									log.info("gson.toJson(testicvo)="+jsonString);
								}else {
									log.info("obj is null.");
								}
							}
							
							if(icvo!=null) {
								InsuranceClaimMapperVo mapperVo = new InsuranceClaimMapperVo();
								BeanUtils.copyProperties(icvo,mapperVo);
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
								List<InsuranceClaimFileDataVo> listFileData = mapperVo.getFileDatas();
								if(listFileData!=null && listFileData.size()>0){
									for(InsuranceClaimFileDataVo fileVo : listFileData) {
										if(fileVo!=null) {
											fileVo.setNotifySeqId(nofifySeqId);
										}
									}
									mapperVo.setFileDatas(listFileData);
								}
								
								//以ROCID查被保人保單,判斷是否為保戶(20210528若該ID在本公司僅為要保人視為非本公司保戶方式，不接收資料)
								//int k = iLilipmService.getInsuredUsersByRocId(mapperVo.getIdNo());//查(要保人+被保人)保單數
								List<String> policyNos = allianceService.getPolicyNoByID(mapperVo.getIdNo());
							    if(policyNos!=null && policyNos.size()>0) {//保戶
									int iRtn = claimChainService.addInsuranceCliam(mapperVo);
									if(iRtn>0) {//如果有查詢且儲存成功
										vo.setNcStatus(NotifyOfNewCaseVo.NC_STATUS_ONE);
										int ncupdate = claimChainService.updateNcStatusBySeqId(vo);
									}
							    }else {//非保戶
									vo.setNcStatus(NotifyOfNewCaseVo.NC_STATUS_ONE);
									vo.setMsg(NotifyOfNewCaseVo.MSG);
									claimChainService.updateNcStatusBySeqId(vo);
									// to do send mail /sms
							    }//end-if
							
							}//end-if
					
						}//end-if
						
					}//end-for
					
				}//end-if
			}catch(Exception e) {
				log.error(e.toString());
			}
		}//end-if
		
    	log.info("End API-105 Task.");

	}

	@Value("${cron.api106.expression.enable: true}")
	public boolean api106Enable;

	/**
	 * 檔案下載
	 */
	@Scheduled(cron = "${cron.api106.expression}")
	public void callAPI106() {
		if (!api106Enable) {
			return;
		}
		log.info("Start API-106 Task.");
		log.info("API_DISABLE="+API_DISABLE);
		
		//testcode
//		try {
//			Map<String,String> testparams = new HashMap<>();
//			testparams.put("fileId", "20201230162649-PR1IojO");
//			String teststrResponse = allianceService.postForEntity(URL_API106, testparams);
//			
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
			try {
				//1.聯盟件且沒有下載記錄:INSURANCE_CLAIM_FILEDATAS.NOTIFY_SEQ_ID!=null and UPPER(INSURANCE_CLAIM_FILEDATAS.MSG)!='SUCCESS'
				InsuranceClaimFileDataVo vo = new InsuranceClaimFileDataVo();
				List<InsuranceClaimFileDataVo> files = claimChainService.getFileDataToDownload(vo);
				
				//2.call api-106下載檔案並於本地儲存
				if(files!=null &&files.size()>0) {
					for(InsuranceClaimFileDataVo fileVo : files) {
						if(fileVo!=null) {
							//2.1.call api-106
							Map<String,String> params = new HashMap<>();
							params.put("fileId", fileVo.getFileId());
							
							//聯盟鏈歷程參數
							InsuranceClaimMapperVo insVo = claimChainService.getCaseIdBySeqId(fileVo.getClaimSeqId());
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "API-106檔案下載");
							unParams.put("caseId", insVo.getCaseId());
							unParams.put("transNum", insVo.getTransNum());
							String strResponse = allianceService.postForEntity(URL_API106, params, unParams);
						//	String strResponse = "{   \"code\": \"0\",   \"msg\": \"success\",   \"data\": {     \"name\": \"20200122121250L01-A00001.pdf \",     \"base64\": \"Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=\"}}";
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
									/*File file = new File("C:\\Users\\chenhui\\Desktop\\UCVVV.pdf");
									byte[] fileContent = FileUtils.readFileToByteArray(file);
									String	 base64= Base64.getEncoder().encodeToString(fileContent);
									*/
									isSaveOK = converBase64StrToFile(strBase64, strPath+"/"+fileName);
									fileVo.setFileStatus(InsuranceClaimFileDataVo.FILE_STATUS_DOWNLOADED);
									fileVo.setMsg(msg);
								}
								fileVo.setFileBase64(strBase64);
								//2.3.儲存完,update INSURANCE_CLAIM_FILEDATAS.MSG='SUCCESS'
								if(isSaveOK) {
									//进行获取TRANS_INSURANCE_CLAIM_FILEDATAS  的ID编号
									  claimChainService.updateFileStatusByFileId(fileVo);
									int i = claimChainService.updateInsuranceClaimFileBase64(fileVo);
									log.info("updateInsuranceClaimFileBase64 "+i +"第一次更新");
									if (i>0) {
										//若FILEDATAS有同FILE_ID一併更新,若更新失敗不能影響檔案內容下載
										//  TODO 便於後續使用fileId  對 TRANS_INSURANCE_CLAIM_FILEDATAS  更新 Base64

									  //目前	ESERVICE.DBO.TRANS_INSURANCE_CLAIM_FILEDATAS  中
									  //是沒有FileId欄目檔案編號,待明日確認后,是否可以添加該欄目,
									  //在進行修改SQL添加即可
										try {
											String fileId = fileVo.getFileId();
											if (fileId != null) {
												log.info("----------------fileVo.getFileId();-----=" + fileId);
												TransInsuranceClaimFileDataVo tciFileDataVo =
														new TransInsuranceClaimFileDataVo();
												tciFileDataVo.setFileId(fileId);
												tciFileDataVo.setFileBase64(fileVo.getFileBase64());
												int claimChain = insuranceClaimService.updateInsuranceClaimFileDataFileBase64(tciFileDataVo);
												log.info("updateInsuranceClaimFileBase64  第二次更新數據 Base64 " + claimChain + " records.");
											}
										} catch (Exception e) {
											log.info("updateFileBase64ByFileId fail.exception=" + e.toString());
										}
									}
								}
							}//end code=0

							if(checkLiaAPIResponseValue(strResponse,"/code","10606")) {//error control-檔案查詢查無資料
								msg = MyJacksonUtil.readValue(strResponse, "/msg");
								
								fileVo.setMsg(msg);
								int rtn = claimChainService.updateFileStatusByFileId(fileVo);
							}//end code=10606
							
						}
					}
				}

			}catch(Exception e) {
				log.error(e.toString());
			}
			
		}//end-if
		
    	log.info("End API-106 Task.");

	}

	@Value("${cron.saveToEserviceTrans.expression.enable: true}")
	public boolean saveToEserviceTransEnable;
	
	@Scheduled(cron = "${cron.saveToEserviceTrans.expression}")
	public void saveToEserviceTrans() {
		if (!saveToEserviceTransEnable) {
			return;
		}
		//取得未加到eservice.TRANS的case
		//檢核是否為保戶，是否為用戶
		//1.聯盟通知件(NOTIFY_SEQ_ID!=null, CASE_ID!=null, CODE='0' ,MSG is null)
		//2.未送到eservice.trans(TRANS_NUM is null)
		//3.首家保險公司已取得紙本註記(FILE_RECEIVED='1')
		//4.所有附加檔案已下載完成
		
		log.info("Start saveToEserviceTrans.");
		log.info("API_DISABLE="+API_DISABLE);
		
		if("N".equals(API_DISABLE)){
			try {
				List<InsuranceClaimMapperVo> listVo = claimChainService.getInsuranceClaimByHasNotifySeqIdNoTransNum();
				if(listVo!=null && !listVo.isEmpty()) {
					
					TransInsuranceClaimVo tic = null;
					TransAddRequest addReq = new TransAddRequest();
					addReq.setSysId("eservice");
					addReq.setTransType(TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE);
					
					for(InsuranceClaimMapperVo vo : listVo) {
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
							addReq.setUserId(userId);
							
							//設定交易序號
							String transNum = transService.getTransNum();
							
							//1.新增線上申請主檔
//							java.util.Date toDay = new java.util.Date();
//							TransVo transVo = new TransVo();
//							transVo.setTransNum(transNum);
//							transVo.setTransDate(toDay);
//							transVo.setTransType(TransTypeUtil.INSURANCE_CLAIM_PARAMETER_CODE);
//							transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_APPLYING);
//							transVo.setUserId(userId);
//							transVo.setCreateUser("system");
//							transVo.setCreateDate(toDay);
//							int rtnInsertTrans = transService.insertTrans(transVo);
							
							//if(rtnInsertTrans>0) {
								//2.新增保單號碼
							    List<String> insClaimsPlans = insuranceClaimService.getInsClaimPlan();
							    
							    StringBuffer policyBuff = new StringBuffer("");
							    
							    List<String> policyNos = allianceService.getPolicyNoByID(idNo);
							    if(policyNos!=null && policyNos.size()>0) {
							    	for(String policyNo : policyNos) {
								    	log.info("AllianceServcieTask get policyNo="+policyNo);
								    	if(policyNo!=null && StringUtils.isNotEmpty(policyNo.trim())) {
									    		List<String> prodCodes = allianceService.getProductCodeByPolicyNo(policyNo);
									    	for(String prodCode : prodCodes) {
									    		log.info("AllianceServcieTask get prodCode="+prodCode);
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
							    log.info("AllianceServcieTask get policyBuff="+policyBuff.toString());
								//此時若沒有保單號碼，故不做
								
								//3.新增線上申請保單理賠
								tic = new TransInsuranceClaimVo();
								BeanUtils.copyProperties(vo,tic);
								tic.setTransNum(transNum);
								tic.setUserId(userId);
								tic.setPolicyNo(policyBuff.toString());
								tic.setCreateDate(vo.getCreateDate());
								
								//3.1.塞好FILEDATAS
								List<InsuranceClaimFileDataVo> fileDatas = claimChainService.getInsuranceClaimFileDataByClaimsSequId(tic.getClaimSeqId());
								if(fileDatas!=null && fileDatas.size()>0) {
									List<TransInsuranceClaimFileDataVo> transFileDatas = new ArrayList<TransInsuranceClaimFileDataVo>();
									for(InsuranceClaimFileDataVo ivo : fileDatas) {
										TransInsuranceClaimFileDataVo tvo = new TransInsuranceClaimFileDataVo();
										tvo.setClaimSeqId(tic.getClaimSeqId());
										tvo.setFileName(ivo.getFileName());
										tvo.setFilePath(this.FILE_SAVE_PATH);
										if("A".equals(ivo.getType())) {
											tvo.setType("DIAGNOSIS_CERTIFICATE");
										}else if("B".equals(ivo.getType())){
											tvo.setType("MEDICAL_RECEIPT");
										}else {
											tvo.setType(ivo.getType());
										}
										tvo.setFileBase64(ivo.getFileBase64());
										tvo.setFileId(ivo.getFileId());
										transFileDatas.add(tvo);
									}
									tic.setFileDatas(transFileDatas);
								}//end-if
								
								addReq.setTransInsuranceClaimVo(tic);

								//3.2.call TransAddServiceImpl.addTransRequest()
								TransAddResponse transAddResp = transAddService.addTransRequest(addReq);//這裡會主動先塞好TRANS Table.
								if(transAddResp!=null) {
									//4.更新該筆Insurance_Claim,表示已送到eservice.TRANS
									if(transNum!=null) {
										vo.setTransNum(transNum);
										int rtn = claimChainService.updateInsuranceClaimTransNumByNotifySeqId(vo);
									}
								
								}
								
								/**
								// to do send mail/sms
								String fromCompanyId = vo.getFrom();
								if ("".equals(policyBuff.toString()) && fromCompanyId != null && !OnlineChangeUtil.FROM_COMPANY_L01.equals(fromCompanyId)) {
									Map<String, String> msgInfo = claimChainService.getMailAndSMSInfo(transNum);
									if(null != msgInfo.get("mail")) {
										List<String> mailTo = new ArrayList<String>();
										mailTo.add(msgInfo.get("mail"));
										mailService.sendMail(msgInfo.get("content"), msgInfo.get("subject"),mailTo , null, null);
									}
									if(null != msgInfo.get("mobile")) {
										smsService.sendSms(msgInfo.get("mobile"), msgInfo.get("content"));
									}
									
								}
								**/
							//}//if(rtnInsertTrans>0)
							
						}else {//非保戶
							vo.setMsg("非保戶");
							int rtn = claimChainService.updateInsuranceClaimMsg(vo);
						}
					}
				}

			}catch(Exception e) {
				log.error(e);
			}
		}
		
		log.info("End saveToEserviceTrans.");
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
	
	/**
	 * Image base64 String to PDF
	 * @param imgBase64Str
	 * @return temFile
	 */
	private File image64ToPdf(String imgBase64Str) {
		
		File temFile = null;
		Document document = null;
		try{
			log.info("start to conver imgBase64Str to temp PDF file.");
			if(imgBase64Str!=null) {
				document = new Document();
				temFile = File.createTempFile("tmpFile", ".pdf");
				PdfWriter.getInstance(document, new FileOutputStream(temFile));
				document.open();
	            byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(imgBase64Str.getBytes());
	            
	            Image image = Image.getInstance(decoded);
	            image.scaleToFit(500, 800);
	            
	            document.add(image);
	            document.close();
			}
			
			log.info("end to conver imgBase64Str to temp PDF file.");
		}catch (Exception e) {
            e.printStackTrace();
        }
		
		return temFile;
	}
	
	/**
	 * 將non-PDF base64 String 轉為 PDF base64 String
	 * @param imgBase64Str
	 * @return String
	 */
	private String converNonPDFBase64ToPDFBase64(String imgBase64Str) {
		String strBase64 = null;
		
		try {
			if(imgBase64Str==null) {
				return null;
			}
			if(imgBase64Str.startsWith("JVBER")) {//PDF base64 format.
				strBase64 = imgBase64Str;
			}else {
				File tempFile = image64ToPdf(imgBase64Str);
				byte[] fileContent = FileUtils.readFileToByteArray(tempFile);
				strBase64 = Base64.getEncoder().encodeToString(fileContent);
				tempFile = null;//G.C
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return strBase64;
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
				
				File contentFile = null;
				if(!filePath.endsWith("pdf")) {//不是PDF檔就轉成PDF
					contentFile = convertToPdfFile(filePath);
				}else {
					contentFile = new File(filePath);
				}
				
				byte[] fileContent = FileUtils.readFileToByteArray(contentFile);
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			}else {
				log.error("input filePath is null.");
			}
		}catch(Exception e) {
			log.error(e);
		}
		
		return encodedString;
	}
	
	private File convertToPdfFile(String imgFilePath){
		log.info("***start convertToPdfFile() filePath="+imgFilePath);
		File filePDF = null;
		Document document = null;
		PdfWriter writer  = null;
		try {
			if(imgFilePath!=null) {
				filePDF = File.createTempFile("ImgToPDF", ".pdf");
				document = new Document();
				java.io.FileOutputStream fos = new java.io.FileOutputStream(filePDF);
				writer = PdfWriter.getInstance(document, fos);
				writer.open();
			    document.open();
			    
			    Image img = Image.getInstance(imgFilePath);
			    //A4 pixel 595x842
			    img.scaleToFit(500, 800);
			    
			    document.add(img);
			}
		}catch(Exception e) {
			log.error("convertToPdfFile() error:", e);
		}finally {
			try {
				if(document!=null) {
					document.close();
				}
				if(writer!=null) {
					writer.close();
				}
			}catch(Exception e) {
				//do nothing.
			}
		}
		
		if(filePDF==null) {
			log.info("***end convertToPdfFile() output file is null.");
		}else {
			log.info("***end convertToPdfFile() output file is not null.fileSize="+filePDF.length());
		}
		
		return filePDF;
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
				
				byte[] decodedBytes = Base64.getDecoder().decode(base64EencodedString);
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

	public String getURL_API101() {
		return URL_API101;
	}

	public void setURL_API101(String uRL_API101) {
		URL_API101 = uRL_API101;
	}

	public String getURL_API102() {
		return URL_API102;
	}

	public void setURL_API102(String uRL_API102) {
		URL_API102 = uRL_API102;
	}

	public String getURL_API103() {
		return URL_API103;
	}

	public void setURL_API103(String uRL_API103) {
		URL_API103 = uRL_API103;
	}

	public String getURL_API104() {
		return URL_API104;
	}

	public void setURL_API104(String uRL_API104) {
		URL_API104 = uRL_API104;
	}

	public String getURL_API105() {
		return URL_API105;
	}

	public void setURL_API105(String uRL_API105) {
		URL_API105 = uRL_API105;
	}

	public String getURL_API106() {
		return URL_API106;
	}

	public void setURL_API106(String uRL_API106) {
		URL_API106 = uRL_API106;
	}

	public String getAPI_DISABLE() {
		return API_DISABLE;
	}

	public void setAPI_DISABLE(String aPI_DISABLE) {
		API_DISABLE = aPI_DISABLE;
	}

	
	/**
	 * test after bean copy for ArrayList
	 * @return String
	 * @throws Exception
	 */
	private String testBeanCopy() throws Exception{
		String rtn = null;
		InsuranceClaimMapperVo mapperVo = new InsuranceClaimMapperVo();
		
		InsuranceClaimVo icvo = new InsuranceClaimVo();
		CompanyVo cvo1 = new CompanyVo();
		cvo1.setCompanyId("L01");
		CompanyVo cvo2 = new CompanyVo();
		cvo2.setCompanyId("L02");
		icvo.setTo(Arrays.asList(cvo1,cvo2));
		
		BeanUtils.copyProperties(icvo, mapperVo);
		
		System.out.println("mapperVo.getTo()="+mapperVo.getTo());
		
		Gson gson = new Gson();
		String strGson = gson.toJson(mapperVo);
		System.out.println("strGson="+strGson);
		
		String icvostrGson = gson.toJson(icvo);
		System.out.println("icvostrGson="+icvostrGson);
		return rtn;
	}

	public static void main(String[] args) throws Exception{
		AllianceServiceTask task = new AllianceServiceTask();
		
		//task.callAPI101();
		
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
		
		
	}

}
