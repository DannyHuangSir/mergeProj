package com.twfhclife.scheduling.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.alliance.model.*;
import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import com.twfhclife.eservice.onlineChange.service.IHospitalInsuranceCompanyServcie;
import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import com.twfhclife.generic.utils.StatuCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.alliance.service.impl.AllianceServiceImpl;
import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;

import com.twfhclife.eservice_api.service.IMessagingTemplateService;

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
import com.twfhclife.generic.utils.EMailTemplate;
import com.twfhclife.generic.utils.MyJacksonUtil;

@Component
public class AllianceServiceTask {

	Log log = LogFactory.getLog(AllianceServiceTask.class);
	Logger logger = LoggerFactory.getLogger(AllianceServiceTask.class);

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
	public String URL_API108;
	public String URL_API109;

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
	private IBxczSignService bxczSignService;

	@Autowired
	IMessagingTemplateService messagingTemplateService;

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
				if ("alliance.api108.url".equals(parameterItem.getParameterName())) {
					this.setURL_API108(parameterItem.getParameterValue());
				}
				if ("alliance.api109.url".equals(parameterItem.getParameterName())) {
					this.setURL_API109(parameterItem.getParameterValue());
				}
			});
		}
		if (resultSCHList != null) {
			resultSCHList.forEach(parameterItem -> {
				if ("cron.api.disable".equals(parameterItem.getParameterName())) {
					this.setAPI_DISABLE(parameterItem.getParameterValue());
				}else {
					if (System.getProperty(parameterItem.getParameterName()) == null) {
						logger.info("set parameter: {}, {}", parameterItem.getParameterName(), parameterItem.getParameterValue());
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

		if("N".equals(API_DISABLE)){
			int rtn = -1;
			try {
				//1.取得理賠申請上傳資料物件(Request)
				List<InsuranceClaimMapperVo> listIC = claimChainService.getInsuranceClaimByNoCaseId();
				//2.call api-101
				if(listIC!=null && !listIC.isEmpty() && listIC.size()>0) {
					for (InsuranceClaimMapperVo icvo : listIC) {
						if(icvo!=null) {
							SignInsuranceClaimMapperVo newVo = new SignInsuranceClaimMapperVo();
							BeanUtils.copyProperties(icvo, newVo);
							SignRecord signRecord = bxczSignService.getNewSignStatus(icvo.getTransNum());
							if (signRecord != null) {
								newVo.setActionId(signRecord.getActionId());
								newVo.setToaFileId(signRecord.getSignFileId());
							}
							if (CollectionUtils.isNotEmpty(icvo.getFileDatas())) {
								Map<String, List<InsuranceClaimFileDataVo>> map = Maps.newHashMap();
								icvo.getFileDatas().forEach(f -> {
									if (map.containsKey(f.getType())) {
										map.get(f.getType()).add(f);
									} else {
										map.put(f.getType(), Lists.newArrayList(f));
									}
								});
								List<SignInsuranceClaimFileDataVo> list = Lists.newArrayList();
								for (Map.Entry<String, List<InsuranceClaimFileDataVo>> entry : map.entrySet()) {
									SignInsuranceClaimFileDataVo vo = new SignInsuranceClaimFileDataVo();
									vo.setType(entry.getKey());
									vo.setFileRequired(entry.getValue().size());
									String fileNames = entry.getValue().stream().map(x -> x.getFileName()).collect(Collectors.toList()).toString();
									vo.setFileName(fileNames.substring(1, fileNames.length() - 1));
									list.add(vo);
								}
								if (CollectionUtils.isNotEmpty(list)) {
									newVo.setFileDatas(list);
								} else {
									newVo.setFileDatas(Lists.newArrayList());
								}
							}

							//3.call api-101 to upload.
							String strResponse = allianceService.postForEntity(URL_API101, newVo, "API-101理賠申請書上傳");
							log.info("call URL_API101,strResponse="+strResponse);
							//3-1.get api-101 response, update caseId, fileId to db.
							// 20220706 by 203990
							/// 非連線問題, 上傳失敗寄送信件給管理者
							//if(checkLiaAPIResponseValue(strResponse,"/code","0")) {
							if(checkLiaAPIResponseValueAndSendEmail(strResponse,"/code","0",icvo)) {
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

	@Autowired
	private BxczDao bxczDao;


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
									}

								}else {
									log.error("to/company is null or empty.");
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
									if(StringUtils.isNotBlank(str)) {
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
								boolean inEservice = false;
								if (policyNos != null && policyNos.size() > 0) {//保戶
									if (StringUtils.isNotBlank(icvo.getActionId()) && !StringUtils.equals("0", icvo.getActionId())) {
										mapperVo.setSignAgree("Y");
										Map<String, String> api416Params = Maps.newHashMap();
										HttpHeaders headers = new HttpHeaders();
										headers.add("Access-Token", clientSecret);
										headers.setContentType(MediaType.APPLICATION_JSON);
										try {
											Date startTime = new Date();
											String api416Resp = bxczSignService.postApi416(api416Url, headers, api416Params);
											String code = MyJacksonUtil.readValue(api416Resp, "/code");
											String msg = MyJacksonUtil.readValue(api416Resp, "/msg");
											Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmm").create();
											SignRecord record = gson.fromJson(api416Resp, SignRecord.class);
											record.setActionId(UUID.randomUUID().toString().replaceAll("-", ""));
											bxczDao.insertBxczSignRecord(record, code, msg, record.getIdVerifyTime(), record.getSignTime());
											BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL", "數位身分驗證/數位簽署狀態查詢", "0", "", "", record.getTransNum(), startTime, new Date());
											bxczDao.addSignApiLog(bxczSignApiLog);
										} catch (Exception e) {
											logger.error("call api416 error: {}, {}, {}", headers, params, e);
											throw new Exception("call api416 error: " + e);
										}
									} else {
										mapperVo.setSignAgree("N");
									}
									int iRtn = claimChainService.addInsuranceCliam(mapperVo);
									if (iRtn > 0) {//如果有查詢且儲存成功
										vo.setNcStatus(NotifyOfNewCaseVo.NC_STATUS_ONE);
										int ncupdate = claimChainService.updateNcStatusBySeqId(vo);
									}
									inEservice = true;
								} else {//非保戶
									vo.setNcStatus(NotifyOfNewCaseVo.NC_STATUS_ONE);
									vo.setMsg(NotifyOfNewCaseVo.MSG);
									claimChainService.updateNcStatusBySeqId(vo);
									// to do send mail /sms
								}
								try {
									Map<String, String> api108Params = Maps.newHashMap();
									api108Params.put("caseId", caseId);
									if (inEservice) {
										api108Params.put("status", "Y");
									} else {
										api108Params.put("status", "3");
										api108Params.put("msg", "非本公司保戶");
									}
									HttpHeaders headers = new HttpHeaders();
									headers.add("Access-Token", clientSecret);
									headers.setContentType(MediaType.APPLICATION_JSON);
									bxczSignService.postApi108(URL_API108, api108Params);
								} catch (Exception e) {
									logger.error("call api108 error: {}, {}", params, e);
									throw new Exception("call api108 error: " + e);
								}
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

	@Value("${cron.api109.expression.enable: true}")
	public boolean api109Enable;

	@Autowired
	private IHospitalInsuranceCompanyServcie iHospitalInsuranceCompanyServcie;

	@Scheduled(cron = "${cron.api109.expression}")
//	@Scheduled(cron = "5 * * * * *")
	public void callAPI109() {
		if (!api109Enable) {
			return;
		}
		log.info("-----------Start API-109 Task.-----------");
		log.info("API_DISABLE=" + API_DISABLE);
		if ("N".equals(API_DISABLE)) {
			try {
				Map<String, String> params = new HashMap<>();
				//聯盟鏈歷程參數
				Map<String, String> unParams = new HashMap<>();
				unParams.put("name", "API-109查詢保險公司清單");
				unParams.put("caseId", null);
				unParams.put("transNum", null);

				String strResponse = allianceService.postForEntity(URL_API109, params, unParams);
				//模仿返回的json數據
//				 String strResponse = "{\"code\":\"0\", \"msg\":\"success\", \"data\":[{\"insuranceId\":\"L02\", \"insuranceName\":\"台灣人壽\"}, {\"insuranceId\":\" L04\", \"insuranceName\":\"國泰人壽\"} ] }";
				log.info("API109-查詢保險公司清單參數  " + strResponse);
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
						log.info("API109-查詢保險公司清單參數-轉換數據  obj is null.");
					}

				}

				if (!org.springframework.util.CollectionUtils.isEmpty(hospitalVos)) {
					log.info("-----API109-查詢保險公司清單參數獲取到信息,開始進行處理------" + hospitalVos);
					//查詢本地保險公司清單
					HospitalInsuranceCompanyVo hospitalInsuranceCompanyVo = new HospitalInsuranceCompanyVo();
					List<HospitalInsuranceCompanyVo> hospitalVoList = iHospitalInsuranceCompanyServcie.getHospitalInsuranceCompanyVoList(hospitalInsuranceCompanyVo);
					System.out.println(hospitalVoList.size());
					System.out.println(hospitalVoList);
					if (!org.springframework.util.CollectionUtils.isEmpty(hospitalVoList)) {
						//進行修改包含舊名稱狀態為可以用
						iHospitalInsuranceCompanyServcie.updateHospitalInsuranceCompanyVoList(hospitalVos, StatuCode.AGREE_CODE.code);
						//進行修改不包含舊名稱狀態為不可以用
						iHospitalInsuranceCompanyServcie.updateNotHospitalInsuranceCompanyVoIdList(hospitalVos, StatuCode.DISAGREE_CODE.code);
						hospitalInsuranceCompanyVo.setStatus(StatuCode.AGREE_CODE.code);
						hospitalInsuranceCompanyVo.setFunctionName(ApConstants.INSURANCE_CLAIM);
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
						if (!org.springframework.util.CollectionUtils.isEmpty(collect)) {
							//進行添加新的數據信息
							for (HospitalInsuranceCompanyVo addHospitalVo : collect) {
								addHospitalVo.setFunctionName(ApConstants.INSURANCE_CLAIM);
								addHospitalVo.setStatus(StatuCode.AGREE_CODE.code);
								iHospitalInsuranceCompanyServcie.insertHospitalInsuranceCompanyVo(addHospitalVo);
							}
						}
					} else {
						//進行存儲數據
						hospitalVos.stream().forEach((x) -> {
							x.setFunctionName(ApConstants.INSURANCE_CLAIM);
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
			log.info("-----------End API-109 Task.-----------");
		}
	}

	@Value("${eservice.bxcz.login.client_id}")
	private String clientId;
	@Value("${eservice.bxcz.login.client_secret}")
	private String clientSecret;
	@Value("${eservice.bxcz.416.url}")
	private String api416Url;

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

	/** 20220629 by 203990  非連線問題, 上傳失敗寄送信件給管理者
	 * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
	 * @param responseJsonString
	 * @param pathFieldName ex:"/code"
	 * @param checkValue ex:"0"
	 * @return boolean
	 */
	private boolean checkLiaAPIResponseValueAndSendEmail(String responseJsonString,String pathFieldName,String checkValue, InsuranceClaimMapperVo icvo) throws Exception{
		boolean b = false;

		if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
			String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
			System.out.println("checkLiaAPIResponseValue="+code);
			String transNum = icvo.getTransNum();
			if(checkValue.equals(code)) {//success
				b = true;
			} else {
				// 20220629 by 203990
				// 非連線上傳失敗, 通知後台管理人員
				Map<String, Object> mailInfo = insuranceClaimService.getSendMailInfo("1");
				//發送系統管理員
				long timeMillis = System.currentTimeMillis();
				List<String> receivers = new ArrayList<String>();
				receivers = (List)mailInfo.get("receivers");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("EMAIL",EMailTemplate.INSURED_EMAIL_IS_NULL);
				paramMap.put("DATA",transNum);
				paramMap.put("EXCEPTION_LOG",responseJsonString);
				MessageTriggerRequestVo voCIO = new MessageTriggerRequestVo();
				voCIO.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_025);
				voCIO.setSendType("email");
				voCIO.setMessagingReceivers(receivers);
				voCIO.setParameters(paramMap);
				voCIO.setSystemId(ApConstants.SYSTEM_ID);
				//進行發送通信
				String resultSYSMailMsg = messagingTemplateService.triggerMessageTemplate(voCIO);
				log.info(ApConstants.TRANSFER_MAIL_025 + resultSYSMailMsg);

				String msg = MyJacksonUtil.readValue(responseJsonString, "/msg");
				icvo.setCaseId("fake_pass");
				icvo.setCode(code);
				icvo.setMsg(msg);
				//icvo.setStatus(ContactInfoVo.STATUS_WAITING_FOR_UPLOAD);

				log.info("===========UI件呼叫完 API101 非連線上傳失敗, 通知後台管理人員==========="+transNum+":fake_pass");
				claimChainService.updateCaseIdByClaimSeqId(icvo);
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

	public String getURL_API109() {
		return URL_API109;
	}

	public void setURL_API109(String URL_API109) {
		this.URL_API109 = URL_API109;
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

	public String getURL_API108() {
		return URL_API108;
	}

	public void setURL_API108(String URL_API108) {
		this.URL_API108 = URL_API108;
	}

	public String getAPI_DISABLE() {
		return API_DISABLE;
	}

	public void setAPI_DISABLE(String aPI_DISABLE) {
		API_DISABLE = aPI_DISABLE;
	}
}
