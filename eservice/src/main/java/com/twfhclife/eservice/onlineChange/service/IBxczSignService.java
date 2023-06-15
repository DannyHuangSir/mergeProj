package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IBxczSignService {
	
    int addSignBxczRecord(SignRecord signRecord);
	
    int updateSignRecordStatus(String code, String msg, Bxcz415CallBackDataVo vo);

	SignRecord getSignRecord(String actionId);

	SignRecord getNewSignStatus(String transNum);

	byte[] getSignPdf(String signFileId);
}