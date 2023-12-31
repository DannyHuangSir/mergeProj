package com.twfhclife.scheduling.task;

import com.twfhclife.alliance.dao.NotifyOfNewCaseDnsDao;
import com.twfhclife.alliance.model.DnsContentVo;
import com.twfhclife.alliance.model.DnsReturnVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseDnsVo;
import com.twfhclife.alliance.service.impl.DnsServiceImpl;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.api.elife.domain.TransAddRequest;
import com.twfhclife.eservice.api.elife.domain.TransAddResponse;
import com.twfhclife.eservice.api.elife.service.ITransAddService;
import com.twfhclife.eservice.onlineChange.model.TransDnsVo;
import com.twfhclife.eservice.onlineChange.service.ITransDnsService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.domain.ReturnHeader;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.CallApiCode;
import com.twfhclife.generic.utils.MyJacksonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 死亡除戶
 *
 */
@Component
public class DnsAllianceServiceTask {

	Log log = LogFactory.getLog(DnsAllianceServiceTask.class);

	Logger logger = LoggerFactory.getLogger(DnsAllianceServiceTask.class);
	
	public static final String CODE_SUCCESS = "0";
	
	public static final String MSG_SUCCESS  = "SUCCESS";
	
	/**
	 * DNS101 request type
	 * 保單號碼
	 */
	public static final String DNS101_REQUEST_TYPE_INSURANCE_ID = "InsuranceId";
	
	/**
	 * DNS101 request type
	 * 身份證字號
	 */
	public static final String DNS101_REQUEST_TYPE_ID_NO = "IdNo";
	
	/**
	 * 呼叫核心CSP PROVIDE的傳入參數call_user,長度不可超過7位元
	 */
	public static final String CSP_PROVIDE_CALL_USER = "ES-API";
	
	//@Value("${cron.api.dns.disable}")
	public String API_DNS_DISABLE;
	
	//@Value("${alliance.dns101.url}")
	public String URL_DNS101;
	
	//@Value("${alliance.dns201.url}")
	public String URL_DNS201;
	
	public String URL_DNSFSZ1;
	
	public String URL_DNSFS62;

	@Autowired
	@Qualifier("apiParameterService")
	private IParameterService parameterService;
	
	@Autowired
	private DnsServiceImpl dnsServiceImpl;
	
	@Autowired
	private ITransDnsService transDnsServiceImpl;
	
	@Autowired
	private NotifyOfNewCaseDnsDao dnsDao;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	ITransAddService transAddService;
	
	@PostConstruct
	public void doAllianceServiceTask() {
		List<ParameterVo> resultSchList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_DNS_SCHEDULE");
    	List<ParameterVo> resultUrlList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_DNS_API_URL");
    	List<ParameterVo> resultBaseLList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "SYS_PRO_BASE_URL");
    	List<ParameterVo> resultBaseLListAuthorization = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, CallApiCode.SYS_DNS_HEADER);
    	if (resultBaseLList != null) {
    		resultBaseLList.forEach(parameterItem ->{
        		if ("alliance.api.accessToken".equals(parameterItem.getParameterName())) {
        			dnsServiceImpl.setACCESS_TOKEN(parameterItem.getParameterValue());
        		}else if("alliance.api.dns101.accessToken".equals(parameterItem.getParameterName())) {
        			dnsServiceImpl.setACCESS_TOKEN_DNS101(parameterItem.getParameterValue());
        		}else {
        			log.info("set accessToken error,parameterItem.getParameterName()="+parameterItem.getParameterName()
    				+",parameterItem.getParameterValue(){}="+parameterItem.getParameterValue());
        		}
        	});
    	}
		if (resultBaseLListAuthorization!=null) {
			resultBaseLListAuthorization.forEach(parameterItem->{
				if (CallApiCode.ALLIANCE_API_AUTHORIZATION.equals(parameterItem.getParameterName())) {
					dnsServiceImpl.setACCESS_TOKEN_DNS_AUTHORIZATION(parameterItem.getParameterValue());
				}
			});
		}
    	if (resultUrlList != null) {
    		resultUrlList.forEach(parameterItem -> {
    			String parameterCode = parameterItem.getParameterCode();
    			
    			if("alliance.dns101.url".equals(parameterCode)) {
    				this.setURL_DNS101(parameterItem.getParameterValue());
    			}else if("alliance.dns201.url".equals(parameterCode)) {
    				this.setURL_DNS201(parameterItem.getParameterValue());
    			}else if("alliance.apiFS62.url".equals(parameterCode)) {
    				this.setURL_DNSFS62(parameterItem.getParameterValue());
    			}else if("alliance.apiFSZ1.url".equals(parameterCode)) {
    				this.setURL_DNSFSZ1(parameterItem.getParameterValue());
    			}else {
    				log.info("set dns url error,parameterItem.getParameterCode()="+parameterCode
    				+",parameterItem.getParameterValue(){}="+parameterItem.getParameterValue());
    			} 
	    	});
    	}
    	if (resultSchList != null) {
    		resultSchList.forEach(parameterItem -> {
    			if ("cron.api.dns.disable".equals(parameterItem.getParameterName())) {
        			this.setAPI_DNS_DISABLE(parameterItem.getParameterValue());
        		}else {
        			//api's cron schedule set here.
        			if (System.getProperty(parameterItem.getParameterName()) == null) {
						logger.info("set parameter: {}, {}", parameterItem.getParameterName(), parameterItem.getParameterValue());
        				System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
        			}
        		}
            });
    	}
	}


	@Value("${cron.dnsFS62.expression.enable: true}")
	public boolean dnsFS62Enable;

	/**
	 * DNS-FS62供死亡除戶通報通知寫入FS62相關通報訊息
	 */
	@Scheduled(cron = "${cron.dnsFS62.expression}")
	public void callDNSFS62() {
		if (!dnsFS62Enable) {
			return;
		}
		log.info("Start DNS-FS62 Task.");
		log.info("API_DNS_DISABLE="+API_DNS_DISABLE);

		if("N".equals(API_DNS_DISABLE)){
			try {
				//A.20211210-modify:取得未上傳,除戶案件的STATUS未完成(0,1,5),直接call FS62寫入核心
				List<DnsContentVo> listContent = dnsDao.getTransDnsByStatus("");
				if(listContent!=null && listContent.size()>0) {
					for(DnsContentVo contentVo : listContent) {
						if(contentVo!=null) {
							String apiCode = null;
							Map<String,String> params = new HashMap<>();
							params.put("FS62-SCN-NAME","FS62");//必填：固定用”FS62”
							params.put("FS62-FUNC-CODE","AA");//必填：固定用”AA”
							params.put("FS62-INSU-NO",contentVo.getPolicyNo());//必填：保單號碼
							params.put("FS62-PAGE","0");//放0即可
							
							String addDate = contentVo.getAdddate();
							if(StringUtils.isNotBlank(addDate) && addDate.length()>=7) {
								addDate = addDate.substring(0,7);
								addDate = "0"+addDate;
							}
							params.put("FS62-ACC-DATE",addDate);//必填：辦理日(yyyyMMdd)年份為民國年;聯盟傳來的有時分秒(民國年 YYYMMDDhhmm)
							
							params.put("FS62-PAY-CODE","30");//必填：種類/固定”30”
							params.put("FS62-ACCI-DATE","0"+contentVo.getCondate());//必填：身故日(事故日),民國年 yyyyMMdd;(聯盟傳來民國年 YYYMMDD)
							params.put("FS62-DIAB-CLASS", "");//殘障分類,放空白即可

							//進行查詢事故的原因
							StringBuffer reason = new StringBuffer();
							reason.append("批註除戶回報系統通報");
							reason.append(contentVo.getName());
							reason.append("0"+contentVo.getCondate());//身故日
							reason.append("身故");
							params.put("FS62-ACCI-REASON1",reason.toString());//必填：事故原因，長度44
							params.put("FS62-ACCI-REASON2","");//必填：事故原因2，長度78
							params.put("FS62-CLERK","");//放空白即可
							params.put("FS62-BRANCH-CODE","01");//放'01'即可

							//聯盟鏈歷程參數
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "DNS-FS62報通知核心");
							unParams.put("caseId", contentVo.getCaseId());
							unParams.put("transNum", contentVo.getTransNum());
							unParams.put("call_user", CSP_PROVIDE_CALL_USER);

							String strResponse = this.dnsServiceImpl.postForHttpURLConnection(URL_DNSFS62, params, unParams);
							//String strResponse = "{\"success\":true,\"data\":{\"token\":\"20210830_00000001\",\"detail_status\":\"0\",\"detail_message\":\"〔寫入完成〕\"}}";
							log.info("call URL_dnsFS62,strResponse="+strResponse);

							String callRtncode = MyJacksonUtil.readValue(strResponse, "/success");//CSP平台執行狀況，true=成功，其他=失敗
							Boolean aBoolean = Boolean.valueOf(callRtncode);
							if(aBoolean) {//核心回傳成功
								String dataDetailStatus = MyJacksonUtil.readValue(strResponse, "/data/detail_status");
								String msg = MyJacksonUtil.readValue(strResponse, "/data/detail_message");
								String token = MyJacksonUtil.readValue(strResponse, "/data/token");
								if("0".equals(dataDetailStatus)) {
									contentVo.setDetailMessage(msg!=null?msg:"新增完成");
									contentVo.setToken(token);
									int rtnCnt = dnsDao.updateTransDnsSDetailMessageByTransNum(contentVo);
									log.info("dnsDao.updateTransDnsSDetailMessageByTransNum rtnCnt="+rtnCnt);
								}else{
									log.info("Call URL_DNSFS62 return detail_status is not '0'.");
								}//end-if
							} else {
								log.info("Call URL_DNSFS62 return is false.");
								//do nothing.
							}
						}//end-if(contentVo!=null)
					}//end-for
				}//end-if

			}catch(Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}

		log.info("End DNS-FS62 Task.");
	}

	@Value("${cron.dnsFSZ1.expression.enable: true}")
	public boolean dnsFSZ1Enable;

	/**
	 * DNS-FSZ1供死亡除戶通報取得最新契況
	 */
	@Scheduled(cron = "${cron.dnsFSZ1.expression}")
	public void callDNSFSZ1() {
		if (!dnsFSZ1Enable) {
			return;
		}
		log.info("Start DNS-FSZ1 Task.");
		log.info("API_DNS_DISABLE=" + API_DNS_DISABLE);

		if ("N".equals(API_DNS_DISABLE)) {
			try {
				//A.
				//取得TRANS_DNS.TOKEN is null - 表示有成功寫入核心
				//取得TRANS中除戶案件的STATUS標示為0 or 1 or 5 - 表示合法的TRANS
				//取得TRANS中除戶案件的(FSZ1_PI_ST is null) - 表示未取過契況
				List<DnsContentVo> listContent = dnsDao.getTransDnsByStatusAndFsz1PiSt();
				if (listContent != null && listContent.size() > 0) {
					for (DnsContentVo contentVo : listContent) {
						if (contentVo != null) {
							//B.LOOP案件回報聯盟
							//all DNS-FSZ1
							String apiCode = null;
							Map<String, String> params = new HashMap<>();
							params.put("FSZ1-SCN-NAME", "FSZ1");//必填：固定用”FSZ1
							params.put("FSZ1-FUNC-CODE", "IN");//必填：固定用”IN”
							params.put("FSZ1-INSU-NO", contentVo.getPolicyNo());//必填：保單號碼
							params.put("FSZ1-ID", contentVo.getIdno());//必填：主被保人ID

							//聯盟鏈歷程參數
							Map<String, String> unParams = new HashMap<>();
							unParams.put("name", "DNS-FSZ1取得最新契況");
							unParams.put("caseId", contentVo.getCaseId());
							unParams.put("transNum", contentVo.getTransNum());
							unParams.put("call_user", CSP_PROVIDE_CALL_USER);
							
							String strResponse = this.dnsServiceImpl.postForHttpURLConnection(URL_DNSFSZ1, params, unParams);
							//String strResponse = "{\"success\":true,\"data\":{\"token\":\"20210902_00000004\",\"detail_status\":\"0\",\"detail_message\":\"〔查詢完成〕\",\"values\":[{\"FSZ1-SCN-NAME\":\"FSZ1\",\"FSZ1-FUNC-CODE\":\"IN\",\"FSZ1-INSU-NO\":\"US10000014\",\"FSZ1-ID\":\"M299999897\",\"FSZ1-PI-ST\":\"00\"}]}}";
							log.info("call URL_DNSFSZ1,strResponse=" + strResponse);

							String callRtncode = MyJacksonUtil.readValue(strResponse, "/success");//true代表成功,其他代表查無資料
							Boolean aBoolean = Boolean.valueOf(callRtncode);
							if (aBoolean) {//核心回傳成功
								String dataDetailStatus = MyJacksonUtil.readValue(strResponse, "/data/detail_status");
								if("0".equals(dataDetailStatus)) {//表示核心系統執行成功
									String token    = MyJacksonUtil.readValue(strResponse, "/data/token");
									//String fsz1Name = MyJacksonUtil.readValue(strResponse, "/data/values/FSZ1-SCN-NAME");
									//String fsz1Code = MyJacksonUtil.readValue(strResponse, "/data/values/FSZ1-FUNC-CODE");
									//String fsz1No   = MyJacksonUtil.readValue(strResponse, "/data/values/FSZ1-INSU-NO");
									String fsz1Id   = MyJacksonUtil.readValue(strResponse, "/data/values/0/FSZ1-ID");
									String fsz1PiSt = MyJacksonUtil.readValue(strResponse, "/data/values/0/FSZ1-PI-ST");
									
									contentVo.setToken(token);
									contentVo.setFsz1PiSt(fsz1PiSt);
									contentVo.setFsz1Id(fsz1Id);
									
									/**
									 * 55-身故
									 * 62-身故,部份家屬尚未來辦理相關事項
									 */
									if("55".equals(fsz1PiSt) || "62".equals(fsz1PiSt)) {//表示取得身故值
										int rtn = -1;
										try {
											rtn = dnsDao.updateTransDnssfsz1PiStByPolicyNo(contentVo);
											log.info("updateTransDnssfsz1PiStByPolicyNo() rtn=" + rtn);
											if(rtn>0) {
												rtn = dnsDao.updateTransStatusByTransNum(contentVo.getTransNum(), "2");
												log.info("updateTransStatusByTransNum(),transNum="+contentVo.getTransNum()+ ",rtn=" + rtn);
											}else {
												log.info("updateTransDnssfsz1PiStByPolicyNo() fail");
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}else {
										//do nothing.
									}

								}//end-if("0".equals(dataDetailStatus))
							}//end-if
						} else {
							//do nothing.
						}
					}//end-if(contentVo!=null)
				}//end-for
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.toString());
			}
		}
		log.info("End DNS-FSZ1 Task.");
	}

	@Value("${cron.dns101.expression.enable: true}")
	public boolean dns101Enable;

	/**
	 * DNS-101案件回報
	 */
	@Scheduled(cron = "${cron.dns101.expression}")
	public void callDNS101() {
		if (!dns101Enable) {
			return;
		}
		log.info("Start DNS-101 Task.");
		log.info("API_DNS_DISABLE="+API_DNS_DISABLE);
		
		if("N".equals(API_DNS_DISABLE)){
			try {
				//A.取得TRANS中除戶案件的STATUS標示為已完成(STATUS=2)
				List<DnsContentVo> listContent = dnsDao.getTransDnsByTransStatus("2");
				if(listContent!=null && listContent.size()>0) {
					for(DnsContentVo contentVo : listContent) {
						if(contentVo!=null) {
							//B.LOOP案件回報聯盟
							//all DNS-101
							String apiCode = null;
							Map<String,String> params = new HashMap<>();
							params.put("type", contentVo.getType());//必填：InsuranceId:保單號碼;IdNo:身份證字號
							params.put("caseId", contentVo.getCaseId());//必填：
							
							//聯盟鏈歷程參數
							Map<String,String> unParams = new HashMap<>();
							unParams.put("name", "DNS-101案件回報");
							unParams.put("caseId", contentVo.getCaseId());
							unParams.put("transNum", contentVo.getTransNum());

							String strResponse = this.dnsServiceImpl.postForEntity(URL_DNS101, params, unParams);
							log.info("call URL_DNS101,strResponse="+strResponse);

							String callRtncode = MyJacksonUtil.readValue(strResponse, "/code");//0代表成功,1代表查無資料
							if("0".equals(callRtncode) || "1".equals(callRtncode)) {//聯盟回傳成功
								String msg = MyJacksonUtil.readValue(strResponse, "/msg");
								String strContent = MyJacksonUtil.getNodeString(strResponse, "data");
								if(strContent!=null) {
									Object obj = MyJacksonUtil.json2Object(strContent, DnsReturnVo.class);
									if(obj!=null) {
										DnsReturnVo dnsVo = (DnsReturnVo)obj;
										if(dnsVo!=null) {
											String updateNumber = dnsVo.getUpdateNumber();
											//由於無法檢查聯盟回傳的updateNumber是否為正確，故不檢查
											//C.回壓TRANS,已完成聯盟回報
											contentVo.setSendAlliance(DnsContentVo.SEND_ALLIANCE_REPORT_TO_ALLIANCE);
											contentVo.setCode(callRtncode);
											contentVo.setMsg(msg);
											dnsDao.updateTransDnsSendAllianceBySeqId(contentVo);
										}
									}
								}//end-if
							} else {
								//do nothing.
							}
						}//end-if(contentVo!=null)
					}//end-for
				}//end-if

			}catch(Exception e) {
				log.error(e.toString());
			}
		}
		
		log.info("End DNS-101 Task.");
	}

	@Value("${cron.dns201.expression.enable: true}")
	public boolean dns201Enable;

	/**
	 * DNS-201 查詢案件資訊
	 */
	@Scheduled(cron = "${cron.dns201.expression}")
	public void callDNS201() {
		if (!dns201Enable) {
			return;
		}
		log.info("Start DNS-201 Task.");
		log.info("API_DNS_DISABLE="+API_DNS_DISABLE);
		
		if("N".equals(API_DNS_DISABLE)){
			try {
				
				//取得聯盟通知案件通知
//				ArrayList<NotifyOfNewCaseDnsVo> ayDns = this.getFakeNodifyNewCaseArray();//fake-code
				ArrayList<NotifyOfNewCaseDnsVo> ayDns = 
						dnsDao.getNofifyOfNewCaseDnsByNcStatus(NotifyOfNewCaseDnsVo.NC_STATUS_ZERO);
				
				for(NotifyOfNewCaseDnsVo nncdVo : ayDns) {
					if(nncdVo!=null && nncdVo.getSeqId()!=null) {
						
						Float notifyOfNewCaseSeqId = nncdVo.getSeqId();
						String type  = nncdVo.getType();
						String jobId = nncdVo.getJobId();
						
						//2.call DNS-102
						String apiCode = null;
						Map<String,String> params = new HashMap<>();
						params.put("jobId", jobId);//必填：保單號碼 or 身份證字號
						
						//聯盟鏈歷程參數
						Map<String,String> unParams = new HashMap<>();
						unParams.put("name", "DNS-201 查詢案件資訊");
						unParams.put("caseId", null);
						unParams.put("transNum", null);
						
//						String strResponse = this.getFakeDNS201Str();//fake-code
						String strResponse = this.dnsServiceImpl.postForEntity(URL_DNS201, params, unParams);
						log.info("call URL_DNS201,strResponse="+strResponse);
						
						if(checkAPIResponseValue(strResponse,"/code","0")) {
							
							String msg = MyJacksonUtil.readValue(strResponse, "/msg");
							
							String strContent = MyJacksonUtil.getNodeString(strResponse, "data");
							if(strContent!=null) {
								Object obj = MyJacksonUtil.json2Object(strContent, DnsReturnVo.class);
								if(obj!=null) {
									DnsReturnVo dnsVo = (DnsReturnVo)obj;
									
									int addResult = -1;
									if(dnsVo!=null) {
										for(DnsContentVo vo : dnsVo.getContent()) {
											//insert into new table DNS
											vo.setNotifySeqId(notifyOfNewCaseSeqId);
											vo.setType(type);
											vo.setJobId(jobId);
											vo.setStatus(DnsContentVo.STATUS_DEFAULT);//for DB not null default '0'
											
											//modify 2021/07/14
											//不重覆轉件-start:如果TYPE='InsuranceId'且 CASE_ID已存在,則STATUS='1'
											if(vo.getCaseId()!=null) {
												Integer numCaseId = dnsDao.getDnsAllianceByCaseId(vo.getCaseId());
												if(numCaseId!=null && numCaseId.intValue()>0) {
													vo.setStatus(DnsContentVo.STATUS_SAVED_TO_ESERVICE);//資料落地但不轉進eservice
													log.info("caseId不重覆轉件："+vo.getCaseId());
												}
											}
											//不重覆轉件-end
											
											addResult = dnsDao.addDnsAlliance(vo);
											if(addResult<=0) {
												break;
											}
										}
										
										if(addResult<=0) {
											//delete all insert DNS_ALLIANCE
											dnsDao.deleteDnsAllianceByNotifySeqId(notifyOfNewCaseSeqId);
											log.info("Data Insert Error, delete DNS_ALLIANCE Table by notifySeqId="+notifyOfNewCaseSeqId);
										}else {
											//取得資料且儲存成功
											//update NOTIFY_OF_NEW_CAST_DNS.STATUS=1
											nncdVo.setNcStatus(NotifyOfNewCaseDnsVo.NC_STATUS_ONE);
											nncdVo.setMsg("");
											dnsDao.updateNcStatusBySeqId(nncdVo);
										}
									}//end-if
									
								}//end-if
							}//end-if
							
						}//end-if

					}
				}//end-for

			}catch(Exception e) {
				log.error(e.toString());
			}
		}
		
		log.info("End DNS-201 Task.");
	}

	@Value("${cron.dns.saveToEserviceTrans.expression.enable: true}")
	public boolean saveToEserviceTransEnable;

	@Scheduled(cron = "${cron.dns.saveToEserviceTrans.expression}")
	public void saveToEserviceTransDns() {
		if (!saveToEserviceTransEnable) {
			return;
		}
		log.info("Start saveToEserviceTransDns Task.");
		log.info("API_DNS_DISABLE="+API_DNS_DISABLE);
		
		if("N".equals(API_DNS_DISABLE)){
			try {
				//A.取得未轉存至eservice TRANS/TRANS_DNS的聯盟資料
				DnsContentVo vo = new DnsContentVo();
				vo.setStatus(DnsContentVo.STATUS_DEFAULT);
				List<DnsContentVo> listContent = dnsDao.getDnsAllianceByStatus(vo);
				
				if(listContent!=null && listContent.size()>0) {
					for(DnsContentVo dnsVo : listContent) {
						//B.轉存eservice
						String transNum = transService.getTransNum();// 設定交易序號
						
//						TransStatusHistoryVo transHistVo = new TransStatusHistoryVo();
//						transHistVo.setStatus("0");//已接收
//						transHistVo.setCustomerName(dnsVo.getName());
//						transHistVo.setTransNum(transNum);
						
						TransDnsVo targetDnsVo = new TransDnsVo();
						BeanUtils.copyProperties(dnsVo,targetDnsVo);
						targetDnsVo.setTransNum(transNum);
						
						TransAddRequest addReq = new TransAddRequest();
						addReq.setSysId("eservice");
						addReq.setTransType(TransTypeUtil.DNS_PARAMETER_CODE);
						addReq.setTransDnsVo(targetDnsVo);
						
						//3.2.call TransAddServiceImpl.addTransRequest()
						int insertResult = -1;
						TransAddResponse transAddResp = transAddService.addTransRequest(addReq);//這裡會主動先塞好TRANS Table.
						if(transAddResp!=null && ReturnHeader.SUCCESS_CODE.equals(transAddResp.getTransResult())) {
							//4.更新該筆DNS_ALLIANCE,表示已送到eservice.TRANS
							dnsVo.setStatus("1");
							insertResult = dnsDao.updateDnsAllianceStatusBySeqId(dnsVo);
							log.info("update DNS_ALLIANCE, set STATUS='1'");
						}
							
						if(insertResult>0) {
							log.info("update DNS_ALLIANCE,seqId="+dnsVo.getSeqId()+" OK.");
						}else {
							log.info("update DNS_ALLIANCE,seqId="+dnsVo.getSeqId()+" Fail.");
						}
							
					}
						
				}
					
			}catch(Exception e) {
				log.error(e.toString());
			}
		}
		
		log.info("End saveToEserviceTransDns Task.");
	}

	@Value("${cron.auto.expression.enable: true}")
	public boolean autoEnable;
	/**
	 * 系統註記案件為「AUTO超時自動結案」
	 */
	@Scheduled(cron = "${cron.auto.expression}")
	public void dnsAuto() {
		if (!autoEnable) {
			return;
		}
		log.info("Start DNS-AUTO Task.");
		try {
			//1.取得「收檔日+N天契況尚未變更」的案件
			String autoDay = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "DNS_AUTO_DAY");
			if (StringUtils.isNotBlank(autoDay) && StringUtils.isNumeric(autoDay)) {
				List<DnsContentVo> listContent = dnsDao.getTransDnsByStatusByAutoDay(Integer.parseInt(autoDay));
				if (CollectionUtils.isNotEmpty(listContent)) {
					listContent.forEach(c -> {
						//系統註記案件為「AUTO超時自動結案」
						dnsDao.updateTransDnsAuto(c);
						//直接結束案件並且將強制回報聯盟已完成
						dnsDao.updateTransStatusAuto(c.getTransNum(), "2");
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		log.info("End DNS-AUTO Task.");
	}


	/**
	 * 檢核回傳的聯盟API jsonString中,指定欄位的指定值
	 * @param responseJsonString
	 * @param pathFieldName ex:"/code"
	 * @param checkValue ex:"0"
	 * @return boolean
	 */
	private boolean checkAPIResponseValue(String responseJsonString,String pathFieldName,String checkValue) throws Exception{
		boolean b = false;

		if(responseJsonString!=null && pathFieldName!=null && checkValue!=null) {
			String code = MyJacksonUtil.readValue(responseJsonString, pathFieldName);
			System.out.println("checkAPIResponseValue="+code);
			if(checkValue.equals(code)) {//success
				b = true;
			}
		}
		System.out.println("checkAPIResponseValue,return="+b);
		return b;
	}

	public String getAPI_DNS_DISABLE() {
		return API_DNS_DISABLE;
	}

	public void setAPI_DNS_DISABLE(String aPI_DNS_DISABLE) {
		API_DNS_DISABLE = aPI_DNS_DISABLE;
	}

	public String getURL_DNS101() {
		return URL_DNS101;
	}

	public void setURL_DNS101(String uRL_DNS101) {
		URL_DNS101 = uRL_DNS101;
	}

	public String getURL_DNS201() {
		return URL_DNS201;
	}

	public void setURL_DNS201(String uRL_DNS201) {
		URL_DNS201 = uRL_DNS201;
	}

	public static void main(String[] args) throws Exception{
		
		DnsAllianceServiceTask task = new DnsAllianceServiceTask();
		//task.API_DNS_DISABLE = "N";
		//task.testCallDNS201(task);
		String addDate = "0711012123456";
		System.out.println(addDate.substring(0,7));
		
	}
	
	private ArrayList<NotifyOfNewCaseDnsVo> getFakeNodifyNewCaseArray(){
		ArrayList<NotifyOfNewCaseDnsVo> ar = new ArrayList<NotifyOfNewCaseDnsVo>();
		
		NotifyOfNewCaseDnsVo vo = new NotifyOfNewCaseDnsVo();
		vo.setSeqId(new Float(8));
		vo.setCallId("f09ddd24-88a8-4cba-95c3-5494aceb5d17");
		vo.setType("IdNo");
		vo.setJobId("20210125153001-I-45c17f68e699");
		
		ar.add(vo);
		
		return ar;
	}
	
	private DnsReturnVo getFakeDnsReturnVo() {
		DnsReturnVo vo = new DnsReturnVo();
		
		List<DnsContentVo> content = new ArrayList<DnsContentVo>();
		DnsContentVo c1 = new DnsContentVo();
		c1.setIdno("AXXXXXXXXX");
		c1.setName("王XX");
		c1.setBirdate("0710115");
		c1.setSex("1");
		c1.setCon("30");
		c1.setCondate("0711115");
		c1.setAdddate("11011152301");
		content.add(c1);
		
		
		DnsContentVo c2 = new DnsContentVo();
		c1.setIdno("JXXXXXXXXX");
		c1.setName("王小明");
		c1.setBirdate("0880115");
		c1.setSex("1");
		c1.setCon("30");
		c1.setCondate("0711115");
		c1.setAdddate("11011152301");
		content.add(c2);
		
		vo.setContent(content);
		return vo;
	}
	
	private String getFakeDNS201Str() {
		String str = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"dataNumber\":2,\"content\":[{\"caseId\":\"I_20210415222401-A…6789-aaaaaa\",\"idno\":\"AXXXXXXXXX\",\"name\":\"王XX\",\"birdate\":\"0710115\",\"sex\":\"1\",\"con\":\"30\",\"condate\":\"0711115\",\"adddate\":\"11011152301\"},{\"caseId\":\"I_20210415222402-J…6789-aaaaaa\",\"idno\":\"JXXXXXXXXX\",\"name\":\"王XX\",\"birdate\":\"0880115\",\"sex\":\"2\",\"con\":\"30\",\"condate\":\"0711215\",\"adddate\":\"11011152301\"}]}}";
		return str;
	}
	
	private void testCallDNS201(DnsAllianceServiceTask task) {
		task.callDNS201();;
	}

	public String getURL_DNSFSZ1() {
		return URL_DNSFSZ1;
	}

	public void setURL_DNSFSZ1(String URL_DNSFSZ1) {
		this.URL_DNSFSZ1 = URL_DNSFSZ1;
	}

	public String getURL_DNSFS62() {
		return URL_DNSFS62;
	}

	public void setURL_DNSFS62(String URL_DNSFS62) {
		this.URL_DNSFS62 = URL_DNSFS62;
	}
}
