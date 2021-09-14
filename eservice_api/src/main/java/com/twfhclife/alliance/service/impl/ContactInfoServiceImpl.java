package com.twfhclife.alliance.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.twfhclife.alliance.model.*;
import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.onlineChange.service.ITransContactInfoService;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.utils.CallApiDateFormatUtil;
import com.twfhclife.generic.utils.MyStringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.alliance.dao.ContactInfoDao;
import com.twfhclife.alliance.dao.NewCaseNotifiedDao;
import com.twfhclife.alliance.dao.NotifyOfNewCaseChangeDao;
import com.twfhclife.alliance.dao.NotifyOfNewCaseDnsDao;
import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.service.IContactInfoService;
import com.twfhclife.generic.dao.adm.ParameterDao;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.ApConstants;


@Service
public class ContactInfoServiceImpl implements IContactInfoService{

	@Autowired
	private ITransContactInfoService transContactInfoService;
	
	@Autowired
	private IMessagingTemplateService messagingTemplateService;

	private static final Logger logger = LogManager.getLogger(ContactInfoServiceImpl.class);
	
	private final PlatformTransactionManager transactionManager;
	
	public ContactInfoServiceImpl(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Autowired
	private NotifyOfNewCaseChangeDao notifyOfNewCaseChangeDao;
	
	@Autowired
	private NewCaseNotifiedDao newCaseNotifiedDao;
	
	@Autowired
	private NotifyOfNewCaseDnsDao notifyOfNewCaseDnsDao;
	
	@Autowired
	private ContactInfoDao contactInfoDao;
	
	@Autowired
	private ParameterDao parameterDao;

	@Override
	public ClaimResponseVo notifyOfNewCase(ClaimRequestVo reqVo) throws Exception {
		logger.info("Start ContactInfoServiceImpl.notifyOfNewCase().");
		//logger.info(ReflectionToStringBuilder.toString(reqVo));
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		int rtnValue = -1;
		if(reqVo==null) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
		}else {
			if(reqVo.getCaseId()==null || "".equals(reqVo.getCaseId().trim())) {
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				rtnVo.setMsg("error:caseId is empty.");
			}else if(reqVo.getType()==null || "".equals(reqVo.getType().trim())) {
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				rtnVo.setMsg("error:type is empty.");
			}else {
				logger.info("notifyOfNewCaseDao.addNotifyOfNewCase() return = "+rtnValue);
				
				rtnValue = notifyOfNewCaseChangeDao.addNotifyOfNewCaseChange(reqVo.getCaseId(), reqVo.getType());
				if(rtnValue==1) {
					rtnVo.setCode(ClaimResponseVo.CODE_SUCCESS);
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				}

			}
			
		}
		
		logger.info("End ContactInfoServiceImpl.notifyOfNewCase().");
		return rtnVo;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int addContactInfo(ContactInfoMapperVo icvo) throws Exception{
		int rtnValue = -1;
		logger.info("Start ContactInfoServiceImpl.addContactInfo().");
		logger.info("input vo="+ReflectionToStringBuilder.toString(icvo));

		ClaimResponseVo rtnVo = new ClaimResponseVo();
		
		if(icvo==null) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
		}else {
			
			//TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
			try {
				//get sequence
				Float seqId = contactInfoDao.getContactInfoSequence();
				logger.info("after contactInfoDao.getContactInfoSequence()seqId="+seqId);
				if(seqId!=null && seqId>0) {
					icvo.setSeqId(seqId);
					/**dataFormat */
					String cbirdate = icvo.getCbirdate();
					if(cbirdate!=null) {
						if(cbirdate.contains("-")) {
							cbirdate = CallApiDateFormatUtil.getStringParse(cbirdate);
						}
						icvo.setCbirdate(cbirdate);
					}

					/**
					 *追加一個資料檢查邏輯
					 * 要寫入CONTACT_INFO的資料cbidate欄位
					 * 		1.
					 * 		請先取用於LILIPM.LIPM_BIRTH
					 * 		若為空再取用LILIPI.LIPI_BIRTH
					 * 		若為空則使用DEFAULT VALUE ‘19801115’
					 * */
					if (StringUtils.isEmpty(icvo.getCbirdate())) {
						String lipiBirth = icvo.getLipiBirth();
						if(!StringUtils.isEmpty(lipiBirth)){
							if(lipiBirth.contains("-")) {
								lipiBirth = CallApiDateFormatUtil.getStringParse(lipiBirth);
							}
							icvo.setCbirdate(lipiBirth);
						}else{
							icvo.setCbirdate("19801115");
						}
					}
					logger.info("寫入CONTACT_INFO的資料cbidate欄位  icvo.getCbirdate()="+icvo.getCbirdate());
					rtnValue = contactInfoDao.addContactInfo(icvo);
					logger.info("after contactInfoDao.addContactInfo()="+rtnValue);
				}

				if(rtnValue>=1) {
					//諸存檔案描述清單
					List<ContactInfoFileDataVo> fileDatas = icvo.getFileDatas();
					logger.info("after contactInfoDao.addContactInfoFileData()seqId="+seqId);
					if(fileDatas!=null ){
						if(fileDatas.size()>0) {
							logger.info("fileDatas.size = " + fileDatas.size());
							for (ContactInfoFileDataVo fileVo : fileDatas) {
								fileVo.setContactSeqId(seqId);
								fileVo.setType("1");
								/***
								 *模仿API203 的  Base64 数据
								 *  便于后面测试API 204 的文件数据
								 * */
							/*	File file = new File("C:\\Users\\chenhui\\Desktop\\UCVVV.pdf");
								byte[] fileContent = FileUtils.readFileToByteArray(file);
								String base64= Base64.getEncoder().encodeToString(fileContent);
								fileVo.setFileBase64(base64);*/


								logger.info("諸存檔案描述清單  addContactInfoFileData"+fileVo);
								String fileBase64 = fileVo.getFileBase64();
								fileVo.setFileBase64("");
								Float fdId=	contactInfoDao.selectontactInfoFiledatasSeq();
								logger.info("-----------------獲取到文件的ID編號--{}",fdId);
								fileVo.setFdId(fdId);
								int rtnFile = contactInfoDao.addContactInfoFileData(fileVo);
								logger.info("-----------------諸存檔案  添加 文件數據  addContactInfoFileData---執行行數--{}",rtnFile);
								fileVo.setFileBase64(fileBase64);
								if (rtnFile>0) {
									int i = contactInfoDao.updateContactInfoFileData(fileVo);
									logger.info("-----------------諸存檔案  修改 文件數據  updateContactInfoFileData---執行行數--{}",i);
								}
								if (rtnFile == 1) {
									continue;
								} else {
									//insert fail.
									rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
									logger.info("-----------------諸存檔案描述清單  throw new Exception()-------------------");
									throw new Exception();
								}
							}
						}
					}
					/**用於類型判斷防止首家件與聯盟件，發送郵件*/
					/*logger.info("用於類型判斷防止首家件與聯盟件，發送郵件	---保單單號"+	icvo.getPolicyNo());
					logger.info("icvo.getPieceType()"+	icvo.getPieceType());
					if(icvo.getPieceType()!=null && !"".equals(icvo.getPieceType())){
						if (ApConstants.ALLIANCE_PIECE.equals(icvo.getPieceType())) {
							//轉收至本公司的案件資料成功存入本公司系統，並以電子郵件通知後台管理人員
							logger.info("用於類型判斷防止首家件與聯盟件，發送郵件"+	icvo.getPieceType());
							  //getPolicyNo
							logger.info("用於類型判斷防止首家件與聯盟件，發送郵件---保單單號"+	icvo.getPolicyNo());
							forwardingCaseImageSuccessMail(icvo.getPolicyNo());
						}
					}*/
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
					throw new Exception("contactInfoDao.addContactInfo() error.");
				}
			}catch (Exception e) {
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				if(icvo.getPieceType()!=null && !"".equals(icvo.getPieceType())){
					//失败发送邮件通知
					if (ApConstants.ALLIANCE_PIECE.equals(icvo.getPieceType())) {
							forwardingCaseImageFailMail(e);
					}
				}
				//transactionManager.rollback(txStatus);
			    throw e;
			}
			//transactionManager.commit(txStatus);
			
		}
		
		logger.info("End ContactInfoServiceImpl.addContactInfo().");
		return rtnValue;
	}

	@Override
	public ArrayList<NotifyOfNewCaseChangeVo> getNofifyOfNewCaseByNcStatus(String ncStatus) throws Exception {
		ArrayList<NotifyOfNewCaseChangeVo> lists = null;
		if(ncStatus!=null && !ncStatus.contains("-")) {//To avoid SQL injection
			lists = notifyOfNewCaseChangeDao.getNofifyOfNewCaseChangeByNcStatus(ncStatus);
		}
		return lists;
	}

	@Override
	public List<ContactInfoMapperVo> getContactInfoByNoCaseId() throws Exception {
		List<ContactInfoMapperVo> list = contactInfoDao.getContactInfoByNoCaseId();
		ArrayList<ContactInfoMapperVo> mapperVoList = new ArrayList<>();
		if(list!=null && list.size()>0) {
			for(ContactInfoMapperVo vo : list) {
				ArrayList<String> arrayList = new ArrayList<>();
				if(vo!=null && vo.getSeqId()!=null) {
					List<ContactInfoFileDataVo> fileDatas = contactInfoDao.getFileDatasByContactSeqId(vo);
					//判断当前ContactInfo是否有文件
					if (fileDatas!=null && fileDatas.size()>0) {
					//filename要依聯盟規範-start
					String currentTimeStr = getCurrentTimeString();
					int fileCount = 0;
					for(ContactInfoFileDataVo fileVo : fileDatas) {
						fileCount ++;
						String newFileName = currentTimeStr+"L01"+"-"+"DOCUMENT"+getNumberString(fileCount);
						newFileName = newFileName + "-";
						newFileName = newFileName + fileVo.getType();
						
						//20210615-聯盟只收PDF，強制為PDF檔-start
						newFileName = newFileName + ".pdf";
//						String fileTyle = fileVo.getFileName().substring(fileVo.getFileName().lastIndexOf("."),fileVo.getFileName().length()); 
//						newFileName = newFileName + fileTyle;
						//20210615-聯盟只收PDF，強制為PDF檔-end
						arrayList.add(newFileName);
						fileVo.setFileName(newFileName);
					}
					//filename要依聯盟規範-end
					}
					vo.setFileDatas(fileDatas);
					//获取档案名称
					vo.setFileNames(arrayList);
				}
				mapperVoList.add(vo);
			}

		}
		return mapperVoList;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateCaseIdByContactSeqId(ContactInfoMapperVo vo) throws Exception {
		int rtn = -1;
		int iUpdateCaseId = -1;
		if(vo!=null) {
			iUpdateCaseId = contactInfoDao.updateCaseIdByContactSeqId(vo);
			logger.info("contactInfo 回压成功:影响行数："+iUpdateCaseId);
			if(iUpdateCaseId==1) {
				List<ContactInfoFileDataVo> fileDatas = vo.getFileDatas();
				int iUpdateFile = -1;
				if(fileDatas!=null && fileDatas.size()>0) {
					
					for(ContactInfoFileDataVo dataVo : fileDatas) {
						if(dataVo!=null) {
							if(dataVo.getFileId()==null) {
								//台銀件未上傳前,沒有fileId
								//台銀件上傳後,會從聯盟取得fileId
								dataVo.setFileId("empty");
							}
							iUpdateFile = updateFileIdByFdId(dataVo);
							if(iUpdateFile!=1) {
								logger.error("contactInfoDao.updateFileIdByFdId() error."); 
								throw new Exception();
							}
						}
					}
				}
				
				if(iUpdateFile==1) {
					rtn = iUpdateFile;
				}
				
			}else {
				logger.error("contactInfoDao.updateCaseIdByContactSeqId() error."); 
				throw new Exception();
			}
		}
		return rtn;
	}

	@Override
	public int updateFileIdByFdId(ContactInfoFileDataVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = contactInfoDao.updateFileIdByFdId(vo);
		}
		return rtn;
	}

	@Override
	public List<ContactInfoMapperVo> getInsuranceClaimByFileReceived(String fileReceived) throws Exception {
		List<ContactInfoMapperVo> list = null;
		if(fileReceived!=null) {
			list = contactInfoDao.getInsuranceClaimByFileReceived(fileReceived);
		}
		
		return list;
	}

	@Override
	public int updateFileReceived(ContactInfoMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = contactInfoDao.updateFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public List<ContactInfoFileDataVo> getFileDataToDownload(ContactInfoFileDataVo vo) throws Exception {
		List<ContactInfoFileDataVo> files = null;
		if(vo!=null) {
			vo = new ContactInfoFileDataVo();
		}
		files = contactInfoDao.getFileDataToDownload(vo);
		
		return files;
	}

	@Override
	public List<ContactInfoFileDataVo> getFileDataToUpload(ContactInfoFileDataVo vo) throws Exception {
		List<ContactInfoFileDataVo> files = null;
		if(vo!=null) {
			vo = new ContactInfoFileDataVo();
		}
		files = contactInfoDao.getFileDataToUpload(vo);
		
		return files;
	}

	@Override
	public int updateAllianceCaseFileReceived(ContactInfoVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = contactInfoDao.updateAllianceCaseFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public List<ContactInfoMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception {
		List<ContactInfoMapperVo> vos = null;
		vos = contactInfoDao.getAllianceCaseByFileReceivedNotYet();
		return vos;
	}

	@Override
	public int updateFileStatusByFileId(ContactInfoFileDataVo vo) throws Exception {
		int rtn = -1;
		try {
			if(vo!=null) {
				rtn = contactInfoDao.updateFileStatusByFileId(vo);

				if(vo.getFileStatus()!=null && InsuranceClaimFileDataVo.FILE_STATUS_DOWNLOADED.equals(vo.getFileStatus())){
					/*if(rtn>0){
						//轉收至本公司的案件資料成功存入本公司系統，並以電子郵件通知後台管理人員
						java.lang.String policyNumber = java.lang.String.valueOf(vo.getContactSeqId());
						forwardingCaseImageSuccessMail(policyNumber);
					}else {
						throw  new  RuntimeException("案件資料存入系統-[失敗]");
					}*/
				}
			}
		}catch (RuntimeException re){
			forwardingCaseImageFailMail(re);
			throw  re;
		}
		return rtn;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public List<ContactInfoMapperVo> getContactInfoByHasNotifySeqIdNoBatchId() throws Exception {
		List<ContactInfoMapperVo> rtnList = contactInfoDao.getContactInfoByHasNotifySeqIdNoBatchId();
		//进行判断，防止API接口saveCioToEserviceTrans回写了BATCH_ID字段
		List<ContactInfoMapperVo> collect=null;
		if (rtnList!=null  && rtnList.size()>0) {
			//获取到CONTACT_INFO与CONTACT_INFO_FILEDATAS关联的SEQ_ID进行查询FileDatas
			 collect = rtnList.stream().map(x -> {
						Float seqId = x.getSeqId();
						seqId = seqId == null ? 0F : seqId;
						 /***存储到ESERVICE.dbo.TRANS_CONTACT_INFO_FILEDATAS表中
						  以便往後ＵＩ上有顯示聯盟變更保單資料縮圖
						  聯盟檔案要傳核心時亦要使用
						  */
						List<ContactInfoFileDataVo> fileDatasByContactSeqIdList =
								contactInfoDao.getContactInfoFileDataByClaimsSequId(seqId);
						x.setFileDatas(fileDatasByContactSeqIdList);
						return x;
					}).collect(Collectors.toList());
			logger.info("获取到CONTACT_INFO与CONTACT_INFO_FILEDATAS关联的SEQ_ID="+collect.toString());
		}
		return collect;
	}

	public int updateContactInfoTransNumByNotifySeqId(ContactInfoMapperVo vo) throws Exception{
		int rtn = -1;
		if(vo!=null && vo.getBatchId() !=null) {
			rtn = contactInfoDao.updateContactInfoTransNumByNotifySeqId(vo);
		}
		return rtn;
	}

	@Override
	public String getUserIdByIdNo(String idNo) throws Exception {
		String userId = null;
		if(idNo!=null) {
			userId = contactInfoDao.getUserIdByIdNo(idNo);
		}
		return userId;
	}

	@Override
	public int updateNcStatusBySeqId(NotifyOfNewCaseChangeVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = notifyOfNewCaseChangeDao.updateNccStatusBySeqId(vo);
		}
		return 0;
	}

	@Override
	public int countPIPMByIdNo(String idNo) throws Exception {
		int rtn = -1;
		if(idNo!=null && StringUtils.isNotEmpty(idNo.trim())) {
			rtn = contactInfoDao.countPIPMByIdNo(idNo);
		}
		return rtn;
	}

	@Override
	public int updateContactInfoMsg(ContactInfoMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null && vo.getSeqId()!=null) {
			rtn = contactInfoDao.updateContactInfoMsg(vo);
		}
		return rtn;
	}

	@Override
	public int updateContactAfterUploadToAlliance(ContactInfoMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = contactInfoDao.updateContactAfterUploadToAlliance(vo);
		}
		return rtn;
	}

	@Override
	public ContactInfoMapperVo getCaseIdBySeqId(Float claimSeqId) {
		return contactInfoDao.getCaseIdBySeqId(claimSeqId);
	}
	
	private String getCurrentTimeString() {
		String rtn = "";
		
		Date date = new Date();
	    DateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	    rtn = dateformat.format(date);
	   // System.out.println("yyyyMMddHHmmss=" + rtn );
	    
		return rtn;
	}
	
	/**
	 * 格式化字串，整數，長度2，不足部分左邊補0
	 * @param num
	 * @return String
	 */
	private String getNumberString(int num) {
		String rtn = "";
		String pattern = "%02d"; // 格式化字串，整數，長度2，不足部分左邊補0
		rtn = String.format(pattern, num);
		return rtn;
	}

	@Override
	public List<ContactInfoFileDataVo> getContactInfoFileDataByClaimsSequId(Float seqId) {
		List<ContactInfoFileDataVo> listFileData = null;
		if(seqId!=null) {
			listFileData = contactInfoDao.getContactInfoFileDataByClaimsSequId(seqId);
		}
		return listFileData;
	}

//	@Override
//	public Map<String, String> getMailAndSMSInfo(String transNum) {
//		// TODO Auto-generated method stub
//		Map<String, String> rMap = new  HashMap<String, String>();
//		String detailInfo = contactInfoDao.getInfoTOMail(transNum);
//		if(detailInfo != null) {
//			String[] strs = detailInfo.split("\\|");
//			String mail = strs[3];
//			String mobile = null;
//			String subject = parameterDao.getParameterVoByCode(ApConstants. SYSTEM_ID_AMD,"NOT_UNION_CLAIMS",ApConstants.ABNORMAL_REASON_SUB).getParameterValue();
//			String content = parameterDao.getParameterVoByCode(ApConstants. SYSTEM_ID_AMD,"NOT_UNION_CLAIMS",ApConstants.ABNORMAL_REASON_MSG).getParameterValue();
//			content = content.replace("${NAME}",  strs[0]);
//			content = content.replace("${TRANS_CREATEDATE}", strs[1]);
//			content = content.replace("${TRANS_NUM}",  strs[2]);
//			
//			UserVo user = contactInfoDao.getMailByRocid(strs[4]);
//			if(user != null) {
//				if(mail == null) {
//					mail = user.getEmail();
//				}
//				mobile = user.getMobile();
//			}
//			
//			logger.info("-------------send mail begin----------");
//			
//			logger.info("mail:{}",mail);
//			logger.info("mobile:{}",mobile);
//			logger.info("subject:{}",subject);
//			logger.info("content:{}",content);
//			
//			logger.info("-------------send mail end------------");
//			rMap.put("mail",mail);
//			rMap.put("mobile",mobile);
//			rMap.put("subject",subject);
//			rMap.put("content",content);
//		}
//		return rMap;
//	}
	
	@Override
	public ClaimResponseVo newCaseNotified(ClaimRequestVo reqVo) throws Exception {
		logger.info("Start ContactInfoServiceImpl.newCaseNotified().");
		//logger.info(ReflectionToStringBuilder.toString(reqVo));
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		int rtnValue = -1;
		if(reqVo==null) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
		}else {
			if(reqVo.getCaseId()==null || "".equals(reqVo.getCaseId().trim())) {
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				rtnVo.setMsg("error:caseId is empty.");
				logger.info("check caseId empty error:" + rtnVo.toString());
			}else if(reqVo.getCaseId().length() > 50){
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				rtnVo.setMsg("error:caseId maxlength 50.");
				logger.info("check caseId maxlength error:" + rtnVo.toString());
			}else {
				logger.info("newCaseNotifiedDao.addNotifyOfNewCase() return = "+rtnValue);
				rtnValue = newCaseNotifiedDao.addNotifyOfNewCase(reqVo.getCaseId(),"{\"code\":\""+ClaimResponseVo.CODE_SUCCESS+"\",\"msg\":\""+ClaimResponseVo.MSG_SUCCESS+"\"}");
				if(rtnValue==1) {
					rtnVo.setCode(ClaimResponseVo.CODE_SUCCESS);
					rtnVo.setMsg(ClaimResponseVo.MSG_SUCCESS);
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
					rtnVo.setMsg(ClaimResponseVo.MSG_ERROR);
				}

			}
			
		}
		
		logger.info("End ContactInfoServiceImpl.newCaseNotified().");
		return rtnVo;
	}

	@Override
	public DnsResponseVo notifyOfNewCaseDns(DnsRequestVo reqVo) throws Exception {
		DnsResponseVo responseVo = new DnsResponseVo();
		responseVo.setCode(responseVo.CODE_ERROR);//default
		
		UUID uid = UUID.randomUUID();
		try {
			if(reqVo != null) {
				logger.info("input type={}", reqVo.getType());
				logger.info("input jobId={}",reqVo.getJobIds().toString());

				List<String> listJobId = reqVo.getJobIds();
				
				//需確認所有的jobId都被正常的insert
				int addResult = -1;
				if(listJobId != null) {
					
					logger.info("eservice_api callId={}", uid.toString());
					
					NotifyOfNewCaseDnsVo vo = null;
					for(String jobId : listJobId) {
						vo = new NotifyOfNewCaseDnsVo();
						vo.setJobId(jobId);
						vo.setType(reqVo.getType());
						vo.setCallId(uid.toString());
						
						addResult = notifyOfNewCaseDnsDao.addNotifyOfNewCaseDns(vo);
						
						if(addResult <= 0) {
							responseVo.setMsg("add error,jobId="+jobId);
							break;
						}
					}//end-for
					
					if(addResult<=0) {//其中一筆新增失敗，則整批刪除
						notifyOfNewCaseDnsDao.deleteNotifyOfNewCaseDnsByCallId(uid.toString());
						
						String msg = "delete record by callId="+uid.toString();
						responseVo.setMsg(msg);
						logger.info(msg);
					}else {
						responseVo.setCode(responseVo.CODE_SUCCESS);
						responseVo.setMsg("");
					}
				}//end-if
				
			}else {
				responseVo.setMsg("Input oject is null.");
			}
		}catch(Exception e) {
			notifyOfNewCaseDnsDao.deleteNotifyOfNewCaseDnsByCallId(uid.toString());
			
			String msg = "delete record by callId="+uid.toString();
			responseVo.setMsg(msg);
			logger.info(msg);
			logger.info(e);
		}
		
		return responseVo;
	}
	
	@Override
	public List<ContactInfoMapperVo> getContactInfoByHasNotifySeqIdAndBatchId() throws Exception {
		List<ContactInfoMapperVo> rtnList = contactInfoDao.getContactInfoByHasNotifySeqIdAndBatchId();
		return rtnList;
	}

	@Override
	public int getCompletedContactInfoByBatchId(Float batchId) {
		return contactInfoDao.getCompletedContactInfoByBatchId(batchId);
	}

	@Override
	public int updateStatusByBatchId(ContactInfoMapperVo vo) throws Exception {
		return contactInfoDao.updateStatusByBatchId(vo);
	}
	
	@Override
	public int updateStatusByCaseId(ContactInfoMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null && vo.getCaseId()!=null) {
			contactInfoDao.updateStatusByCaseId(vo);
		}
		return rtn;
	}

	@Override
	public List<ContactInfoMapperVo> getTransContactInfoByForTask() throws Exception {
		List<ContactInfoMapperVo> transContactInfoByForTask = contactInfoDao.getTransContactInfoByForTask();
		logger.info("----査詢當前的getTransContactInfoByForTask 數據--------------{}",transContactInfoByForTask.toString());
		/**
		 *査詢當前的檔案數據
		 * */
		List<ContactInfoMapperVo> collect = transContactInfoByForTask.stream().map(x -> {
			Float seqId = x.getSeqId();
			List<ContactInfoFileDataVo> contactInfoFileDataByClaimsSequId =
					contactInfoDao.getTransContactInfoFileDataByClaimsSequId(seqId);
			x.setFileDatas(contactInfoFileDataByClaimsSequId);
			logger.info("----査詢當前的檔案數據-------獲取到當前的FileData數據------------------{}",x.toString());
			return x;
		}).collect(Collectors.toList());
		return collect;
	}
	
//	public static void main(String[] args) {
//		PlatformTransactionManager transactionManager = null;
//		ContactInfoServiceImpl impl = new ContactInfoServiceImpl(transactionManager);
//		//impl.getCurrentTimeString();
//		System.out.println(impl.getNumberString(1));
//		
//		String fileName = "b.a.jpg";
//		String fileTyle = fileName.substring(fileName.lastIndexOf("."),fileName.length()); 
//		System.out.println(fileTyle);
//	}

		/**
		 * 成功发送邮件
		 * @param policyNumber  保單編號
		 */
		public   void  forwardingCaseImageSuccessMail(String policyNumber){
			try {
				Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
				paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
				paramMap.put("POLICY_NUMBER", policyNumber);
				//發送系統管理員
				List<String> receivers = new ArrayList<String>();
				receivers = (List)mailInfo.get("receivers");

				MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
				vo.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_016);
				vo.setSendType("email");
				vo.setMessagingReceivers(receivers);
				vo.setParameters(paramMap);
				vo.setSystemId(ApConstants.SYSTEM_ID);
				logger.info("MessageTriggerRequestVo="+vo);
				//进行发送通信
				String resultMsg = messagingTemplateService.triggerMessageTemplate(vo);
				if(MyStringUtil.isNullOrEmpty(resultMsg)) {
					logger.info("Send message successfully complete.");
				} else {
					logger.info("Failed to send message successfully");
				}
			}catch (Exception e){
				logger.error("Failed to send message successfully:"+e.getMessage());
			}
		}

	@Override
	public int updateTransContactInfoCaseId(String caseId, String transNum) {
		return transContactInfoService.updateTransContactInfoCaseId(caseId,transNum);
	}

	@Override
	public int getCompletedContactInfoByBatchIdStatus(Float batchId) {
		return contactInfoDao.getCompletedContactInfoByBatchIdStatus(batchId);
	}

	/***
	 *模仿将DB数据写出文档
	 * 当前用于测试API 204 的文件数据
	 * */
	/*@Override
	public List<ContactInfoFileDataVo> getFileDataToDownloadSuccess() {
		return contactInfoDao.getFileDataToDownloadSuccess();
	}*/

	/**
	 * 失败发送邮件
	 * @param e
	 */
	private  void  forwardingCaseImageFailMail(Exception e){
		try {
			Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			paramMap.put("PROGRAM_NAME", stackTraceElement.getClassName()+":"+stackTraceElement.getMethodName());
			paramMap.put("NUMBER", new String().valueOf(stackTraceElement.getLineNumber()));
			paramMap.put("EXCEPTION_LOG", e.getMessage());
			paramMap.put("DATA", CallApiDateFormatUtil.getCurrentTimeString());
			//發送系統管理員
			List<String> receivers = new ArrayList<String>();
			receivers = (List)mailInfo.get("receivers");

			MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
			vo.setMessagingTemplateCode(ApConstants.TRANSFER_MAIL_017);
			vo.setSendType("email");
			vo.setMessagingReceivers(receivers);
			vo.setParameters(paramMap);
			vo.setSystemId(ApConstants.SYSTEM_ID);
			logger.info("MessageTriggerRequestVo="+vo);
			//进行发送通信
			String resultMsg = messagingTemplateService.triggerMessageTemplate(vo);
			if(MyStringUtil.isNullOrEmpty(resultMsg)) {
				logger.info("Send message successfully complete.");
			} else {
				logger.info("Failed to send message successfully");
			}
		}catch (Exception ex){
			logger.error("Failed to send message successfully:"+ex.getMessage());
		}
	}

	@Override
	public Integer getAbnormlContactInfoCountByBatchIdStatus(Float batchId) {
		Integer rtn = null;
		if(batchId!=null && batchId>0) {
			rtn = contactInfoDao.getAbnormlContactInfoCountByBatchIdStatus(batchId);
		}
		return rtn;
	}

}
