package com.twfhclife.alliance.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.twfhclife.alliance.dao.InsuranceClaimDao;
import com.twfhclife.alliance.dao.NotifyOfNewCaseDao;
import com.twfhclife.alliance.dao.NotifyOfNewCaseDnsDao;
import com.twfhclife.alliance.dao.NewCaseNotifiedDao;
import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.model.InsuranceClaimFileDataVo;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseDnsVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.generic.dao.adm.ParameterDao;
import com.twfhclife.generic.model.UserVo;
import com.twfhclife.generic.utils.ApConstants;

import io.netty.util.internal.StringUtil;


@Service
public class ClaimChainServiceImpl implements IClaimChainService{
	
	private static final Logger logger = LogManager.getLogger(ClaimChainServiceImpl.class);
	
	private final PlatformTransactionManager transactionManager;
	
	public ClaimChainServiceImpl(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Autowired
	private NotifyOfNewCaseDao notifyOfNewCaseDao;
	
	@Autowired
	private NewCaseNotifiedDao newCaseNotifiedDao;
	
	@Autowired
	private NotifyOfNewCaseDnsDao notifyOfNewCaseDnsDao;
	
	@Autowired
	private InsuranceClaimDao insuranceClaimDao;
	
	@Autowired
	private ParameterDao parameterDao;

	@Override
	public ClaimResponseVo notifyOfNewCase(ClaimRequestVo reqVo) throws Exception {
		logger.info("Start ClaimChainServiceImpl.notifyOfNewCase().");
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
				
				rtnValue = notifyOfNewCaseDao.addNotifyOfNewCase(reqVo.getCaseId(), reqVo.getType());
				if(rtnValue==1) {
					rtnVo.setCode(ClaimResponseVo.CODE_SUCCESS);
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				}

			}
			
		}
		
		logger.info("End ClaimChainServiceImpl.notifyOfNewCase().");
		return rtnVo;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int addInsuranceCliam(InsuranceClaimMapperVo icvo) throws Exception{
		int rtnValue = -1;
		logger.info("Start ClaimChainServiceImpl.addInsuranceCliam().");
		logger.info("input vo="+ReflectionToStringBuilder.toString(icvo));
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		
		if(icvo==null) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
		}else {
			
			//TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
			try {
				//get sequence
				Float seqId = insuranceClaimDao.getInsuranceClaimSequence();
				logger.info("after insuranceCliamDao.getInsuranceClaimSequence()seqId="+seqId);
				if(seqId!=null && seqId>0) {
					icvo.setClaimSeqId(seqId);
					rtnValue = insuranceClaimDao.addInsuranceClaim(icvo);
					logger.info("after insuranceCliamDao.addInsuranceClaim()="+rtnValue);
				}

				if(rtnValue>=1) {
					//諸存檔案描述清單
					List<InsuranceClaimFileDataVo> fileDatas = icvo.getFileDatas();
					if(fileDatas!=null && fileDatas.size()>0){
						for(InsuranceClaimFileDataVo fileVo : fileDatas) {
							fileVo.setClaimSeqId(seqId);
							//获取InsuranceClaimFileData FD_ID
							Float fdId = insuranceClaimDao.getItransInsuranceClaimFiledatasId();
							logger.info("---------------------獲取保單理賠的文件FD_ID-----{}",fdId);
							fileVo.setFdId(fdId);
							//获取保单的文件数据
							String fileBase64 = fileVo.getFileBase64();
							fileVo.setFileBase64("");
							int rtnFile = insuranceClaimDao.addInsuranceClaimFileData(fileVo);
							fileVo.setFileBase64(fileBase64);
							if (rtnFile>0) {
								int i = insuranceClaimDao.updateInsuranceClaimFileBase64(fileVo);
								logger.info("---------------------獲取保單理賠的文件  更新數據信息-----{}",i);
							}
							if(rtnFile==1) {
								continue;
							}else {
								//insert fail.
								rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
								throw new Exception();
							}
						}
					}
					
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
					throw new Exception("insuranceCliamDao.addInsuranceClaim() error.");
				}
			}catch (Exception e) {
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				
				//transactionManager.rollback(txStatus);
			    throw e;
			}
			//transactionManager.commit(txStatus);
			
		}
		
		logger.info("End ClaimChainServiceImpl.addInsuranceCliam().");
		return rtnValue;
	}

	@Override
	public ArrayList<NotifyOfNewCaseVo> getNofifyOfNewCaseByNcStatus(String ncStatus) throws Exception {
		ArrayList<NotifyOfNewCaseVo> lists = null;
		if(ncStatus!=null && !ncStatus.contains("-")) {//To avoid SQL injection
			lists = notifyOfNewCaseDao.getNofifyOfNewCaseByNcStatus(ncStatus);
		}
		return lists;
	}

	@Override
	public List<InsuranceClaimMapperVo> getInsuranceClaimByNoCaseId() throws Exception {
		List<InsuranceClaimMapperVo> list = insuranceClaimDao.getInsuranceClaimByNoCaseId();
		if(list!=null && list.size()>0) {
			for(InsuranceClaimMapperVo vo : list) {
				if(vo!=null && vo.getClaimSeqId()!=null) {
					List<InsuranceClaimFileDataVo> fileDatas = insuranceClaimDao.getFileDatasByClaimSeqId(vo);
					
					//filename要依聯盟規範-start
					String currentTimeStr = getCurrentTimeString();
					int fileCount = 0;
					for(InsuranceClaimFileDataVo fileVo : fileDatas) {
						fileCount ++;
						
						String newFileName = currentTimeStr+"L01"+"-"+"DOCUMENT"+getNumberString(fileCount);
						newFileName = newFileName + "-";
						newFileName = newFileName + fileVo.getType();
						//20210615-聯盟只收PDF，強制為PDF檔-start
						newFileName = newFileName + ".pdf";
//						String fileTyle = fileVo.getFileName().substring(fileVo.getFileName().lastIndexOf("."),fileVo.getFileName().length()); 
//						newFileName = newFileName + fileTyle;
						//20210615-聯盟只收PDF，強制為PDF檔-end
						
						fileVo.setFileName(newFileName);
					}
					//filename要依聯盟規範-end
					
					vo.setFileDatas(fileDatas);
				}
			}
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateCaseIdByClaimSeqId(InsuranceClaimMapperVo vo) throws Exception {
		int rtn = -1;
		int iUpdateCaseId = -1;
		if(vo!=null) {
			iUpdateCaseId = insuranceClaimDao.updateCaseIdByClaimSeqId(vo);
			
			if(iUpdateCaseId==1) {
				List<InsuranceClaimFileDataVo> fileDatas = vo.getFileDatas();
				int iUpdateFile = -1;
				if(fileDatas!=null && fileDatas.size()>0) {
					
					for(InsuranceClaimFileDataVo dataVo : fileDatas) {
						if(dataVo!=null) {
							if(dataVo.getFileId()==null) {
								//台銀件未上傳前,沒有fileId
								//台銀件上傳後,會從聯盟取得fileId
								dataVo.setFileId("empty");
							}
							iUpdateFile = updateFileIdByFdId(dataVo);
							if(iUpdateFile!=1) {
								logger.error("insuranceCliamDao.updateFileIdByFdId() error."); 
								throw new Exception();
							}
						}
					}
				}
				
				if(iUpdateFile==1) {
					rtn = iUpdateFile;
				}
				
			}else {
				logger.error("insuranceCliamDao.updateCaseIdByClaimSeqId() error."); 
				throw new Exception();
			}
		}
		return rtn;
	}

	@Override
	public int updateFileIdByFdId(InsuranceClaimFileDataVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = insuranceClaimDao.updateFileIdByFdId(vo);
		}
		return rtn;
	}

	@Override
	public List<InsuranceClaimMapperVo> getInsuranceClaimByFileReceived(String fileReceived) throws Exception {
		List<InsuranceClaimMapperVo> list = null;
		if(fileReceived!=null) {
			list = insuranceClaimDao.getInsuranceClaimByFileReceived(fileReceived);
		}
		
		return list;
	}

	@Override
	public int updateFileReceived(InsuranceClaimMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = insuranceClaimDao.updateFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public int updateTransInsuranceClaimFileReceived(InsuranceClaimMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null && vo.getCaseId()!=null) {
			rtn = insuranceClaimDao.updateTransInsuranceClaimFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public List<InsuranceClaimFileDataVo> getFileDataToDownload(InsuranceClaimFileDataVo vo) throws Exception {
		List<InsuranceClaimFileDataVo> files = null;
		if(vo!=null) {
			vo = new InsuranceClaimFileDataVo();
		}
		files = insuranceClaimDao.getFileDataToDownload(vo);
		
		return files;
	}

	@Override
	public List<InsuranceClaimFileDataVo> getFileDataToUpload(InsuranceClaimFileDataVo vo) throws Exception {
		List<InsuranceClaimFileDataVo> files = null;
		if(vo!=null) {
			vo = new InsuranceClaimFileDataVo();
		}
		files = insuranceClaimDao.getFileDataToUpload(vo);
		
		return files;
	}

	@Override
	public int updateAllianceCaseFileReceived(InsuranceClaimVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = insuranceClaimDao.updateAllianceCaseFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public List<InsuranceClaimMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception {
		List<InsuranceClaimMapperVo> vos = null;
		vos = insuranceClaimDao.getAllianceCaseByFileReceivedNotYet();
		return vos;
	}

	@Override
	public int updateFileStatusByFileId(InsuranceClaimFileDataVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = insuranceClaimDao.updateFileStatusByFileId(vo);
		}
		
		return rtn;
	}

	@Override
	public List<InsuranceClaimMapperVo> getInsuranceClaimByHasNotifySeqIdNoTransNum() throws Exception {
		List<InsuranceClaimMapperVo> rtnList = insuranceClaimDao.getInsuranceClaimByHasNotifySeqIdNoTransNum();
		return rtnList;
	}

	public int updateInsuranceClaimTransNumByNotifySeqId(InsuranceClaimMapperVo vo) throws Exception{
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			rtn = insuranceClaimDao.updateInsuranceClaimTransNumByNotifySeqId(vo);
		}
		return rtn;
	}

	@Override
	public String getUserIdByIdNo(String idNo) throws Exception {
		String userId = null;
		if(idNo!=null) {
			userId = insuranceClaimDao.getUserIdByIdNo(idNo);
		}
		return userId;
	}

	@Override
	public int updateNcStatusBySeqId(NotifyOfNewCaseVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = notifyOfNewCaseDao.updateNcStatusBySeqId(vo);
		}
		return 0;
	}

	@Override
	public int countPIPMByIdNo(String idNo) throws Exception {
		int rtn = -1;
		if(idNo!=null && StringUtils.isNotEmpty(idNo.trim())) {
			rtn = insuranceClaimDao.countPIPMByIdNo(idNo);
		}
		return rtn;
	}

	@Override
	public int updateInsuranceClaimMsg(InsuranceClaimMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null && vo.getClaimSeqId()!=null) {
			rtn = insuranceClaimDao.updateInsuranceClaimMsg(vo);
		}
		return rtn;
	}

	@Override
	public int updateInsuranceAfterUploadToAlliance(InsuranceClaimMapperVo vo) throws Exception {
		int rtn = -1;
		if(vo!=null) {
			rtn = insuranceClaimDao.updateInsuranceAfterUploadToAlliance(vo);
		}
		return rtn;
	}

	@Override
	public InsuranceClaimMapperVo getCaseIdBySeqId(Float claimSeqId) {
		return insuranceClaimDao.getCaseIdBySeqId(claimSeqId);
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
	public List<InsuranceClaimFileDataVo> getInsuranceClaimFileDataByClaimsSequId(Float claimSeqId) {
		List<InsuranceClaimFileDataVo> listFileData = null;
		if(claimSeqId!=null) {
			listFileData = insuranceClaimDao.getInsuranceClaimFileDataByClaimsSequId(claimSeqId);
		}
		return listFileData;
	}

	@Override
	public Map<String, String> getMailAndSMSInfo(String transNum) {
		// TODO Auto-generated method stub
		Map<String, String> rMap = new  HashMap<String, String>();
		String detailInfo = insuranceClaimDao.getInfoTOMail(transNum);
		if(detailInfo != null) {
			String[] strs = detailInfo.split("\\|");
			String mail = strs[3];
			String mobile = null;
			String subject = parameterDao.getParameterVoByCode(ApConstants. SYSTEM_ID_AMD,"NOT_UNION_CLAIMS",ApConstants.ABNORMAL_REASON_SUB).getParameterValue();
			String content = parameterDao.getParameterVoByCode(ApConstants. SYSTEM_ID_AMD,"NOT_UNION_CLAIMS",ApConstants.ABNORMAL_REASON_MSG).getParameterValue();
			content = content.replace("${NAME}",  strs[0]);
			content = content.replace("${TRANS_CREATEDATE}", strs[1]);
			content = content.replace("${TRANS_NUM}",  strs[2]);
			
			UserVo user = insuranceClaimDao.getMailByRocid(strs[4]);
			if(user != null) {
				if(mail == null) {
					mail = user.getEmail();
				}
				mobile = user.getMobile();
			}
			
			logger.info("-------------send mail begin----------");
			
			logger.info("mail:{}",mail);
			logger.info("mobile:{}",mobile);
			logger.info("subject:{}",subject);
			logger.info("content:{}",content);
			
			logger.info("-------------send mail end------------");
			rMap.put("mail",mail);
			rMap.put("mobile",mobile);
			rMap.put("subject",subject);
			rMap.put("content",content);
		}
		return rMap;
	}
	
	@Override
	public ClaimResponseVo newCaseNotified(ClaimRequestVo reqVo) throws Exception {
		logger.info("Start ClaimChainServiceImpl.newCaseNotified().");
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
		
		logger.info("End ClaimChainServiceImpl.newCaseNotified().");
		return rtnVo;
	}

	@Override
	public DnsResponseVo notifyOfNewCaseDns(DnsRequestVo reqVo) throws Exception {
		DnsResponseVo responseVo = new DnsResponseVo();
		responseVo.setCode(responseVo.CODE_ERROR);//default
		
		UUID uid = UUID.randomUUID();
		try {
			if(reqVo != null && reqVo.getType()!=null && reqVo.getJobIds()!=null) {
				logger.info("input type={}", reqVo.getType());
				logger.info("input jobId={}",reqVo.getJobIds().toString());

				List<String> listJobId = reqVo.getJobIds();
				
				//需確認所有的jobId都被正常的insert
				int addResult = -1;
				if(listJobId != null) {
					
					logger.info("eservice_api callId={}", uid.toString());
					
					NotifyOfNewCaseDnsVo vo = null;
					for(String jobId : listJobId) {
						if(jobId!=null && !jobId.trim().isEmpty()) {//jobId can't be empty.
							jobId = jobId.trim();
							vo = new NotifyOfNewCaseDnsVo();
							vo.setJobId(jobId);
							vo.setType(reqVo.getType());
							vo.setCallId(uid.toString());
							
							addResult = notifyOfNewCaseDnsDao.addNotifyOfNewCaseDns(vo);
							
							if(addResult <= 0) {
								responseVo.setMsg("add error,jobId="+jobId);
								break;
							}
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
				responseVo.setMsg("Input oject format error..");
			}
		}catch(Exception e) {
			notifyOfNewCaseDnsDao.deleteNotifyOfNewCaseDnsByCallId(uid.toString());
			
			String msg = "delete record by callId="+uid.toString();
			responseVo.setMsg(msg);
			logger.info(msg);
			logger.info(e);
		}
		uid = null;//G.C
		
		return responseVo;
	}

	@Override
	public int addInsuranceClaimFileData(InsuranceClaimFileDataVo vo) throws Exception {
		int i = insuranceClaimDao.addInsuranceClaimFileData(vo);
		return i;
	}

	@Override
	public int updateInsuranceClaimFileBase64(InsuranceClaimFileDataVo vo) throws Exception {
		int i = insuranceClaimDao.updateInsuranceClaimFileBase64(vo);
		return i;
	}

	@Override
	public Float getItransInsuranceFiledatasId() throws Exception {
		Float itransInsuranceClaimFiledatasId = insuranceClaimDao.getItransInsuranceClaimFiledatasId();
		return itransInsuranceClaimFiledatasId;
	}

	public static void main(String[] args) {
		PlatformTransactionManager transactionManager = null;
		ClaimChainServiceImpl impl = new ClaimChainServiceImpl(transactionManager);
//		//impl.getCurrentTimeString();
//		System.out.println(impl.getNumberString(1));
//		
//		String fileName = "b.a.jpg";
//		String fileTyle = fileName.substring(fileName.lastIndexOf("."),fileName.length()); 
//		System.out.println(fileTyle);
		
		//test formate file name
		String filename = "202012120304檔名.jpg";
		String newFileName = "20200101121250"+"L01"+"-"+"DOCUMENT"+impl.getNumberString(1);
		newFileName = newFileName + "-";
		newFileName = newFileName + "A";
		String fileTyle = filename.substring(filename.lastIndexOf("."),filename.length()); 
		newFileName = newFileName + fileTyle;
		System.out.println("newFileName="+newFileName);
		
	}

}
