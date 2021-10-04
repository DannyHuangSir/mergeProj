package com.twfhclife.alliance.service;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.twfhclife.alliance.model.MedicalTreatmentClaimVo;

/**
 * 呼叫外部API用的interface
 *
 */
public interface IMedicalTreatmentExternalService {
	
	/**
	 * 401 推送給聯盟資料
	 * @param url_api401
	 * @param vo
	 * @param s apiName String
	 * @return String
	 */
    String postForEntity(String url_api401, MedicalTreatmentClaimVo vo, String s) throws Exception;
    
    
    /**
	 * 檢查http response status
	 * @param responseEntity
	 * @return boolean
	 */
	boolean checkResponseStatus(ResponseEntity<?> responseEntity);
    
}
