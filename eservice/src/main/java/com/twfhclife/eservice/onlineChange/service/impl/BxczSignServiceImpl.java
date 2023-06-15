package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.eservice.onlineChange.model.Bxcz415CallBackDataVo;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Service
public class BxczSignServiceImpl implements IBxczSignService {

	private static final Logger logger = LogManager.getLogger(BxczSignServiceImpl.class);

	@Autowired
	private BxczDao bxczDao;

    @Override
    public int addSignBxczRecord(SignRecord signRecord) {
        return bxczDao.insertBxczSignRecord(signRecord,null, null, null, null);
    }

	@Override
	public int updateSignRecordStatus(String code, String msg, Bxcz415CallBackDataVo vo) {
		Date signTime = null;
		Date verifyTime = null;
		if (StringUtils.isNotBlank(vo.getSignTime())) {
			try {
				signTime = new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getSignTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		if (StringUtils.isNotBlank(vo.getIdVerifyTime())) {
			try {
				verifyTime = new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getIdVerifyTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return bxczDao.updateBxczSignRecordByActionId(vo, code, msg, verifyTime, signTime);
	}

	@Override
	public SignRecord getSignRecord(String actionId) {
		return bxczDao.getSignRecordByActionId(actionId);
	}

	@Override
	public SignRecord getNewSignStatus(String transNum) {
		return bxczDao.getNewSignStatus(transNum);
	}

    @Override
    public byte[] getSignPdf(String signFileId) {
		String fileBase64 = bxczDao.getSignFileByFileId(signFileId);
        return StringUtils.isBlank(fileBase64) ? null : Base64.getDecoder().decode(fileBase64);
    }
}