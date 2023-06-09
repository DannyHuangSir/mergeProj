package com.twfhclife.eservice_batch.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.twfhclife.eservice_batch.model.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tempuri.GetTokenDocument;
import org.tempuri.GetTokenResponseDocument;
import org.tempuri.UploadFileDocument;
import org.tempuri.UploadFileDocument.UploadFile;
import org.tempuri.UploadFileResponseDocument;
import org.tempuri.UploadFileResponseDocument.UploadFileResponse;

import tw.com.twfhclife.ezacquire.UploadServiceStub;

import com.google.gson.Gson;
import com.twfhclife.eservice_batch.dao.TransEndorsementDao;
import com.twfhclife.eservice_batch.util.TransTypeUtil;

public class BatchUploadEZService {

	private static final Logger logger = LogManager.getLogger(BatchUploadEZService.class);
	
	private static String ENVIORMENT;
	
	private static String EZ_ACQUIRE_ID;
	
	private static String EZ_ACQUIRE_ENDPOINT;
	
	private static String EZ_ACQUIRE_ACTION_GET_TOKEN;
	
	private static String EZ_ACQUIRE_ACTION_UPLOAD_FILE;
	
	private static String EZ_ACQUIRE_ACTION_QUERY_UPLOAD_FILE_STATUS;
	
	private static String EZ_INDEXDATA_SCAN_TYPE_ID;
	
	private static String EZ_INDEXDATA_BRANCH;
	
	private static String EZ_INDEXDATA_FORM_ID_CHANGEINFO;
	
	private static String EZ_INDEXDATA_FORM_ID_ENDORSEMENT;
	
	public BatchUploadEZService() {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		ENVIORMENT = rb.getString("running.enviorment");
		EZ_ACQUIRE_ID = rb.getString("ez_acquire_id");
		EZ_ACQUIRE_ENDPOINT = rb.getString("ez_acquire_endpoint_" + ENVIORMENT.toLowerCase());
		EZ_ACQUIRE_ACTION_GET_TOKEN = rb.getString("ez_acquire_action_get_token");
		EZ_ACQUIRE_ACTION_UPLOAD_FILE = rb.getString("ez_acquire_action_upload_file");
		EZ_ACQUIRE_ACTION_QUERY_UPLOAD_FILE_STATUS = rb.getString("ez_acquire_action_query_upload_file_status");
		EZ_INDEXDATA_SCAN_TYPE_ID = rb.getString("ez_indexdata_scan_type_id");
		EZ_INDEXDATA_BRANCH = rb.getString("ez_indexdata_branch");
		EZ_INDEXDATA_FORM_ID_CHANGEINFO = rb.getString("ez_indexdata_form_id_changeinfo");
		EZ_INDEXDATA_FORM_ID_ENDORSEMENT = rb.getString("ez_indexdata_form_id_endorsement");
		logger.debug(
				"ENVIORMENT:{}, EZ_ACQUIRE_ID:{}, EZ_ACQUIRE_ENDPOINT:{}, EZ_ACQUIRE_ACTION_GET_TOKEN:{}, EZ_ACQUIRE_ACTION_UPLOAD_FILE:{}, EZ_ACQUIRE_ACTION_QUERY_UPLOAD_FILE_STATUS:{}, EZ_INDEXDATA_SCAN_TYPE_ID:{}, EZ_INDEXDATA_BRANCH:{},EZ_INDEXDATA_FORM_ID_CHANGEINFO:{},EZ_INDEXDATA_FORM_ID_ENDORSEMENT:{}",
				ENVIORMENT, EZ_ACQUIRE_ID, EZ_ACQUIRE_ENDPOINT,
				EZ_ACQUIRE_ACTION_GET_TOKEN, EZ_ACQUIRE_ACTION_UPLOAD_FILE,
				EZ_ACQUIRE_ACTION_QUERY_UPLOAD_FILE_STATUS,
				EZ_INDEXDATA_SCAN_TYPE_ID, EZ_INDEXDATA_BRANCH,
				EZ_INDEXDATA_FORM_ID_CHANGEINFO,
				EZ_INDEXDATA_FORM_ID_ENDORSEMENT);
	}
	
	public boolean uploadFile(String transNum, String docType, String token, File file, OnlineChangeInfoVo infoVo) throws Exception {
		logger.debug("Start uploadFile...");
		
		boolean result = false;
		UploadServiceStub stub = new UploadServiceStub(EZ_ACQUIRE_ENDPOINT);
		UploadFileDocument upploadFileDocument = UploadFileDocument.Factory.newInstance();
		UploadFile uploadFile = upploadFileDocument.addNewUploadFile();

		if ("".equals(StringUtils.trimToEmpty(token))) {
			logger.debug("token is null or empty, get a new token!!!");
			token = this.getEZToken();
		}
		
		EZIndexDataVo indexDataVo = new EZIndexDataVo();
		indexDataVo.setScanTypeId(EZ_INDEXDATA_SCAN_TYPE_ID);
		indexDataVo.setBranch(EZ_INDEXDATA_BRANCH);
		indexDataVo.setBusinessType(TransTypeUtil.getEZBusinessType(infoVo.getTransType()));
		
		//20221123 契撤申請結果使用 B1200301 保單網路服務變更相關文件
		String setFormId = EZ_INDEXDATA_FORM_ID_CHANGEINFO;
		if (file.getName().toLowerCase().indexOf("endorsement") != -1) {
			setFormId = EZ_INDEXDATA_FORM_ID_ENDORSEMENT;
		}
		else if (file.getName().indexOf("contractRevocationNotice") != -1) {
			setFormId = "B1200301";
		}
		indexDataVo.setFormId(setFormId);
																	
		indexDataVo.setInsurantId(infoVo.getLipiId());
		indexDataVo.setPolicyNumber(infoVo.getPolicyNo());
		indexDataVo.setApplicantId(infoVo.getLipmId());
		indexDataVo.setScanTypeId(EZ_INDEXDATA_SCAN_TYPE_ID);
		indexDataVo.setBranch(EZ_INDEXDATA_BRANCH);
		Gson gson = new Gson();
		String indexData = gson.toJson(indexDataVo);
		
		String sha1 = this.getFileSHA1(file);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		uploadFile.setAppId(EZ_ACQUIRE_ID);
		uploadFile.setTokenInfo(token);
		uploadFile.setFileType(docType);
		uploadFile.setFile(fileContent);
		uploadFile.setChecksum(sha1);
		uploadFile.setIndexData(indexData);
		logger.debug("=== uploadFile AppId:{}, TokenInfo:{}, FileType:{}, FileName:{}, Checksum:{}, IndexData:{} ===", EZ_ACQUIRE_ID, token, docType, file.getName(), sha1, indexData);
		
		UploadFileResponseDocument resp = stub.uploadFile(upploadFileDocument);
		UploadFileResponse uploadFileResponse = resp.getUploadFileResponse();
		String jsonResult = uploadFileResponse.getUploadFileResult();
		logger.debug("###uploadFile jsonResult:{}" + jsonResult);
		
		
		EZAcquireVo ezVo = gson.fromJson(jsonResult, EZAcquireVo.class);		
		logger.debug("uploadFile taskId:{}, documentId:{}, status:{}", ezVo.getTaskId(), ezVo.getDocumentId(), ezVo.getStatus());		
		if (ezVo.getStatus().toUpperCase().startsWith("S")) {
			ezVo.setTransNum(transNum);
			ezVo.setDocumentType(docType);
			int cut = new TransEndorsementDao().insertTransEZ(ezVo);
			if (cut == 0) {
				logger.debug("insertTransEZ failed!");
			}
			result = true;
		}

		logger.debug("End uploadFile...");
		return result;
	}
	
	/**
	 * 上傳聯盟鏈保險理賠上傳文件至影像系統
	 * @return
	 */
	public String uploadInsuranceClaimFileDatas(TransInsuranceClaimFileDataVo vo) throws Exception{
		logger.debug("***Start uploadFile***");
		String result = null;
		
		if(vo==null || vo.getPath()==null || vo.getFileName()==null) {
			return null;
		}
		
		File file = null;
		String fileExtension = FilenameUtils.getExtension(vo.getFileName());
		//modify:read file from TRANS_INSURANCE_CLAIM_FILEDATAS.FILE_BASE64 Column-start
		if(StringUtils.isBlank(vo.getFileBase64())) {//嘗試使用實體檔
			file = new File(vo.getPath()+"/"+vo.getFileName());
			logger.info("vo.getFileBase64() is null.");
		}else {
			file = File.createTempFile(vo.getFileName(), fileExtension);
			file = base64ToFile(file,vo.getFileBase64());
		}
		//modify:read file from TRANS_INSURANCE_CLAIM_FILEDATAS.FILE_BASE64 Column-end
		
		if(file!=null){			
			UploadServiceStub stub = new UploadServiceStub(EZ_ACQUIRE_ENDPOINT);
			UploadFileDocument upploadFileDocument = UploadFileDocument.Factory.newInstance();
			UploadFile uploadFile = upploadFileDocument.addNewUploadFile();

			String token = this.getEZToken();
			
			EZIndexDataVo indexDataVo = new EZIndexDataVo();
			
			//ScanType永遠設PDF
			indexDataVo.setScanTypeId("PDF");
			
			indexDataVo.setBranch(EZ_INDEXDATA_BRANCH);
			indexDataVo.setBusinessType("INSURANCE_CLAIM");
			
			/**
			 * E3000101 理賠聯盟鏈-數位同意書
			 * E3000201 理賠聯盟鏈-診斷證明書
			 * E3000301 理賠聯盟鏈-事故證明書
			 * E3000401 理賠聯盟鏈-收據
			 * E3000501 理賠聯盟鏈-補件
			 */
			String formId = "";
			if("1".equals(vo.getType()) || "DIGI_SERVICE_CONSENT_FORM".equals(vo.getType())) {
				formId = "E3000101";
			}else if("2".equals(vo.getType()) || "DIAGNOSIS_CERTIFICATE".equals(vo.getType())){
				formId = "E3000201";
			}else if("3".equals(vo.getType()) || "ACCIDENT_FORM".equals(vo.getType())){
				formId = "E3000301";
			}else if("4".equals(vo.getType()) || "MEDICAL_RECEIPT".equals(vo.getType())){
				formId = "E3000401";
			}else if("C".equals(vo.getType())) {
				formId = "E3000501";
			}
			indexDataVo.setFormId(formId);

			indexDataVo.setInsurantId(vo.getLipiId());
			indexDataVo.setPolicyNumber(vo.getPolicyNo());
			indexDataVo.setApplicantId(vo.getLipmId());

			Gson gson = new Gson();
			String indexData = gson.toJson(indexDataVo);
			
			String sha1 = this.getFileSHA1(file);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			uploadFile.setAppId(EZ_ACQUIRE_ID);
			uploadFile.setTokenInfo(token);
			
			uploadFile.setFileType(fileExtension.toUpperCase());
			
			uploadFile.setFile(fileContent);
			uploadFile.setChecksum(sha1);
			uploadFile.setIndexData(indexData);
			logger.debug("=== uploadFile AppId:{}, TokenInfo:{}, FileType:{}, FileName:{}, Checksum:{}, IndexData:{} ===", 
					EZ_ACQUIRE_ID, token, fileExtension.toUpperCase(), file.getName(), sha1, indexData);
			
			UploadFileResponseDocument resp = null;
			if(StringUtils.isEmpty(indexDataVo.getFormId())) {
				//在很極端的狀況下，會突然無法判斷formId給影像系統
				logger.info("indexDataVo.getFormId() is empty,don't call uploadFile() now.");
				return null;
			}else {
				resp = stub.uploadFile(upploadFileDocument);
			}
			
			UploadFileResponse uploadFileResponse = resp.getUploadFileResponse();
			String jsonResult = uploadFileResponse.getUploadFileResult();
			logger.debug("###uploadFile jsonResult:{}" + jsonResult);
			
			EZAcquireVo ezVo = gson.fromJson(jsonResult, EZAcquireVo.class);		
			logger.debug("uploadFile taskId:{}, documentId:{}, status:{}", 
					ezVo.getTaskId(), ezVo.getDocumentId(), ezVo.getStatus());
			
			if (ezVo.getStatus().toUpperCase().startsWith("S")) {
				ezVo.setTransNum(vo.getTransNum());
				ezVo.setDocumentType(fileExtension.toUpperCase());
				int cut = new TransEndorsementDao().insertTransEZ(ezVo);
				if (cut == 0) {
					logger.debug("insertTransEZ failed!");
				}
				result = ezVo.getTaskId();
			}

		}else {
			logger.error("File is null.path={},fileName={}", vo.getPath(),vo.getFileName());
		}
		
		logger.debug("***End uploadFile***");
		return result;
	}

	/**
	 * 上傳聯盟鏈醫療保單上傳文件至影像系統
	 * @return
	 */
	public String uploadTransMedicalTreatmentClaimFiledatas(TransMedicalTreatmentClaimFileDatasVo vo) throws Exception{
		logger.debug("***Start uploadFile***");
		String result = null;

		if(vo==null || vo.getPath()==null || vo.getFileName()==null) {
			return null;
		}

		File file = null;
		String fileExtension = FilenameUtils.getExtension(vo.getFileName());
		//modify:read file from TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.FILE_BASE64 Column-start
		if(StringUtils.isBlank(vo.getFileBase64())) {//嘗試使用實體檔
			file = new File(vo.getPath() + File.pathSeparator + vo.getFileName());
			logger.info("vo.getFileBase64() is null.");
		}else {
			file = File.createTempFile(vo.getFileName(), fileExtension);
			file = base64ToFile(file,vo.getFileBase64());
		}
		//modify:read file from TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.FILE_BASE64 Column-end

		if(file!=null){

			UploadServiceStub stub = new UploadServiceStub(EZ_ACQUIRE_ENDPOINT);
			UploadFileDocument upploadFileDocument = UploadFileDocument.Factory.newInstance();
			UploadFile uploadFile = upploadFileDocument.addNewUploadFile();

			String token = this.getEZToken();

			EZIndexDataVo indexDataVo = new EZIndexDataVo();

			//ScanType永遠設PDF
			indexDataVo.setScanTypeId("PDF");

			indexDataVo.setBranch(EZ_INDEXDATA_BRANCH);
			indexDataVo.setBusinessType("MEDICAL_DOCUMENT_CONTENT");

			/**
			 * E3000101 醫療聯盟鏈-數位同意書
			 * E3000201 醫療聯盟鏈-診斷證明書
			 * E3000301 醫療聯盟鏈-事故證明書
			 * E3000401 醫療聯盟鏈-收據
			 * E3000501 醫療聯盟鏈-補件
			 */
			String formId = "";
			if("1".equals(vo.getType()) || "MEDICAL_DOCUMENT_CONTENT".equals(vo.getType())) {
				formId = "E3000101";
			}else if("2".equals(vo.getType()) || "CertificateDiagnosis".equals(vo.getType())){
				formId = "E3000201";
			}else if("3".equals(vo.getType()) || "ACCIDENT_FORM".equals(vo.getType())){
				formId = "E3000301";
			}else if("4".equals(vo.getType()) || "Receipt".equals(vo.getType())){
				formId = "E3000401";
			}else if("C".equals(vo.getType())) {
				formId = "E3000501";
			}
			indexDataVo.setFormId(formId);

			indexDataVo.setInsurantId(vo.getLipiId());
			indexDataVo.setPolicyNumber(vo.getPolicyNo());
			indexDataVo.setApplicantId(vo.getLipmId());

			Gson gson = new Gson();
			String indexData = gson.toJson(indexDataVo);
			logger.info("=====獲取到的醫療數據信息===={}",indexData);
			String sha1 = this.getFileSHA1(file);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			uploadFile.setAppId(EZ_ACQUIRE_ID);
			uploadFile.setTokenInfo(token);

			uploadFile.setFileType(fileExtension.toUpperCase());

			uploadFile.setFile(fileContent);
			uploadFile.setChecksum(sha1);
			uploadFile.setIndexData(indexData);
			logger.debug("=== uploadFile AppId:{}, TokenInfo:{}, FileType:{}, FileName:{}, Checksum:{}, IndexData:{} ===",
					EZ_ACQUIRE_ID, token, fileExtension.toUpperCase(), file.getName(), sha1, indexData);

			UploadFileResponseDocument resp = null;
			if(StringUtils.isEmpty(indexDataVo.getFormId())) {
				//在很極端的狀況下，會突然無法判斷formId給影像系統
				logger.info("indexDataVo.getFormId() is empty,don't call uploadFile() now.");
				return null;
			}else {
				resp = stub.uploadFile(upploadFileDocument);
			}

			UploadFileResponse uploadFileResponse = resp.getUploadFileResponse();
			String jsonResult = uploadFileResponse.getUploadFileResult();
			logger.debug("###uploadFile jsonResult:{}" + jsonResult);

			EZAcquireVo ezVo = gson.fromJson(jsonResult, EZAcquireVo.class);
			logger.debug("uploadFile taskId:{}, documentId:{}, status:{}",
					ezVo.getTaskId(), ezVo.getDocumentId(), ezVo.getStatus());

			if (ezVo.getStatus().toUpperCase().startsWith("S")) {
				ezVo.setTransNum(vo.getTransNum());
				ezVo.setDocumentType(fileExtension.toUpperCase());
				int cut = new TransEndorsementDao().insertTransEZ(ezVo);
				if (cut == 0) {
					logger.debug("insertTransEZ failed!");
				}
				result = ezVo.getTaskId();
			}

		}else {
			logger.error("File is null.path={},fileName={}", vo.getPath(),vo.getFileName());
		}

		logger.debug("***End uploadFile***");
		return result;
	}

	public String uploadTransMedicalInfoFiledatas(TransMedicalInfoVo vo) throws Exception {
		logger.debug("***Start uploadFile***");
		String result = null;

		if (vo == null || vo.getFileName() == null) {
			return null;
		}

		File file = null;
		String fileExtension = FilenameUtils.getExtension(vo.getFileName());
		if(fileExtension==null || "".equals(fileExtension.trim())) {
			fileExtension = "pdf";//default pdf.
		}
		
		//modify:read file from TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.FILE_BASE64 Column-start
		if(StringUtils.isBlank(vo.getFileBase64())) {//嘗試使用實體檔
			file = new File(vo.getPath() + File.pathSeparator + vo.getFileName());
			logger.info("vo.getFileBase64() is null.");
		}else {
			file = File.createTempFile(vo.getFileName(), fileExtension);
			file = base64ToFile(file,vo.getFileBase64());
		}
		//modify:read file from TRANS_MEDICAL_TREATMENT_CLAIM_FILEDATAS.FILE_BASE64 Column-end

		if (file != null) {

			UploadServiceStub stub = new UploadServiceStub(EZ_ACQUIRE_ENDPOINT);
			UploadFileDocument upploadFileDocument = UploadFileDocument.Factory.newInstance();
			UploadFile uploadFile = upploadFileDocument.addNewUploadFile();

			String token = this.getEZToken();

			EZIndexDataVo indexDataVo = new EZIndexDataVo();

			//ScanType永遠設PDF
			indexDataVo.setScanTypeId("PDF");

			indexDataVo.setBranch(EZ_INDEXDATA_BRANCH);
			indexDataVo.setBusinessType("MEDICAL_DOCUMENT_CONTENT");

			/**
			 * E3000201 診斷證明書
			 * E3000401 費用明細
			 * E3000601 出院病摘
			 * E3000701 醫學影像
			 * E3000801 病理檢查
			 * E3000901 手術資料
			 * E3001001 住院
			 * E3001101 急診
			 */

			String formId = "";
			if ("CertificateDiagnosis".equals(vo.getType())) {
				formId = "E3000201";
			} else if ("Receipt".equals(vo.getType())) {
				formId = "E3000401";
			} else if ("DischargeSummary".equals(vo.getType())) {
				formId = "E3000601";
			} else if ("MedicalImage".equals(vo.getType())) {
				formId = "E3000701";
			} else if ("Pathlogy".equals(vo.getType())) {
				formId = "E3000801";
			} else if ("Surgery".equals(vo.getType())) {
				formId = "E3000901";
			} else if ("Hospitalization".equals(vo.getType())) {
				formId = "E3001001";
			} else if ("Emergency".equals(vo.getType())) {
				formId = "E3001101";
			}
			indexDataVo.setFormId(formId);

			indexDataVo.setInsurantId(vo.getLipiId());
			indexDataVo.setPolicyNumber(vo.getPolicyNo());
			indexDataVo.setApplicantId(vo.getLipmId());

			Gson gson = new Gson();
			String indexData = gson.toJson(indexDataVo);
			logger.info("=====獲取到的醫療數據信息===={}", indexData);
			String sha1 = this.getFileSHA1(file);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			uploadFile.setAppId(EZ_ACQUIRE_ID);
			uploadFile.setTokenInfo(token);

			uploadFile.setFileType(fileExtension.toUpperCase());

			uploadFile.setFile(fileContent);
			uploadFile.setChecksum(sha1);
			uploadFile.setIndexData(indexData);
			logger.debug("=== uploadFile AppId:{}, TokenInfo:{}, FileType:{}, FileName:{}, Checksum:{}, IndexData:{} ===",
					EZ_ACQUIRE_ID, token, fileExtension.toUpperCase(), file.getName(), sha1, indexData);

			UploadFileResponseDocument resp = null;
			if (StringUtils.isEmpty(indexDataVo.getFormId())) {
				//在很極端的狀況下，會突然無法判斷formId給影像系統
				logger.info("indexDataVo.getFormId() is empty,don't call uploadFile() now.");
				return null;
			} else {
				resp = stub.uploadFile(upploadFileDocument);
			}

			UploadFileResponse uploadFileResponse = resp.getUploadFileResponse();
			String jsonResult = uploadFileResponse.getUploadFileResult();
			logger.debug("###uploadFile jsonResult:{}" + jsonResult);

			EZAcquireVo ezVo = gson.fromJson(jsonResult, EZAcquireVo.class);
			logger.debug("uploadFile taskId:{}, documentId:{}, status:{}",
					ezVo.getTaskId(), ezVo.getDocumentId(), ezVo.getStatus());

			if (ezVo.getStatus().toUpperCase().startsWith("S")) {
				ezVo.setTransNum(vo.getTransNum());
				ezVo.setDocumentType(fileExtension.toUpperCase());
				int cut = new TransEndorsementDao().insertTransEZ(ezVo);
				if (cut == 0) {
					logger.debug("insertTransEZ failed!");
				}
				result = ezVo.getTaskId();
			}

		} else {
			logger.error("File is null.fileName={}", vo.getFileName());
		}

		logger.debug("***End uploadFile***");
		return result;
	}


	public String getEZToken() throws Exception {
//		String token = "";
		logger.debug("Start getEZToken...");
		
		UploadServiceStub stub = new UploadServiceStub(EZ_ACQUIRE_ENDPOINT);
		GetTokenDocument getTokenDocument = GetTokenDocument.Factory.newInstance();
		GetTokenDocument.GetToken getToken = getTokenDocument.addNewGetToken();
		
		getToken.setAction(EZ_ACQUIRE_ACTION_UPLOAD_FILE);
		getToken.setId(EZ_ACQUIRE_ID);
		GetTokenResponseDocument resp = stub.getToken(getTokenDocument);
		GetTokenResponseDocument.GetTokenResponse getTokenResp = resp.getGetTokenResponse();
		String token = getTokenResp.getGetTokenResult();
		logger.debug("getEZToken token:{}", token);
		
//		Gson gson = new Gson();
//		EZAcquireVo ezVo = gson.fromJson(jsonResult, EZAcquireVo.class);
//		logger.debug("getEZToken id:{}, issueDate:{}", ezVo.getId(), ezVo.getIssueDate());
//		token = ezVo.getId();
		logger.debug("End getEZToken...");
		return token;
	}
	
	private String getFileSHA1(File file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    InputStream fis = new FileInputStream(file);
	    int n = 0;
	    byte[] buffer = new byte[8192];
	    while (n != -1) {
	        n = fis.read(buffer);
	        if (n > 0) {
	            digest.update(buffer, 0, n);
	        }
	    }
	    return new HexBinaryAdapter().marshal(digest.digest());
	}
	
	private File base64ToFile(File file,String strBase64) {
		if(file==null) {
			return file;
		}
		
		BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
		try {
			byte[] bytes = Base64.getDecoder().decode(strBase64);
			
			fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		
		return file;
	}
	
	/**
	 * 判斷base64 String的圖片格式
	 * 
	 * @param base64ImgData
	 * @return String
	 */
	private static String checkImageBase64Format(String base64ImgData) {
		byte[] b = Base64.getDecoder().decode(base64ImgData);
		String type = "";
		if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
			type = "bmp";
		} else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
			type = "png";
		} else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
			type = "jpg";
		}
		return type;
	}
	
}
