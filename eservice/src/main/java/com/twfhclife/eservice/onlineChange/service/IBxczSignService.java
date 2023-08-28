package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IBxczSignService {
	
    int addSignBxczRecord(SignRecord signRecord);
	
    int updateSignRecordStatus(String code, String msg, Bxcz415CallBackDataVo vo);

	SignRecord getSignRecord(String actionId);

	SignRecord getNewSignStatus(String transNum);

	byte[] getSignPdf(String signFileId);

	List<SignRecord> getNotDownloadSignFile();

	int updateSignDownloaded(String actionId);

	int addSignFileData(String fileId, String clientId, String fileBase64);

	String postForJson(String api417Url, HttpHeaders headers, Map<String, String> params) throws Exception;

	String postApi416(String api416Url, HttpHeaders headers, Map<String, String> api416Params) throws Exception;

	String postApi108(String urlApi108, Map<String, String> api108Params) throws Exception;

	int addSignBxczApiRecord(BxczSignApiLog bxczSignApiLog);

    int updateSignStatus418(String actionId, String idVerifyStatus, String signStatus);
}