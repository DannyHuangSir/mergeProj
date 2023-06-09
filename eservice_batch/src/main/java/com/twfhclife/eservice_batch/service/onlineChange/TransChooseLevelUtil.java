package com.twfhclife.eservice_batch.service.onlineChange;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransChooseLevelDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.model.TransChooseLevelVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;


public class TransChooseLevelUtil {
	private static final Logger logger = LogManager.getLogger(TransChooseLevelUtil.class);
	private static final String TRANS_TYPE = "CHOOSE_LEVEL"; // 申請器樂撤銷通知服務
	private static final String TRANS_STATUS = "1"; // 1:已審核
	private static final String UPLOAD_CODE = "038"; // 介接代碼
	
	public static void updateIndividual(String transNum) {
		try {
			TransChooseLevelDao  transChooseLevelDao = new TransChooseLevelDao();
			TransChooseLevelVo vo = transChooseLevelDao.getEffectTransChooseLevel(transNum);
			if(vo != null) {
				transChooseLevelDao.updateIndividual(vo);
			}
		}catch (Exception e) {
			 logger.error("updateIndividual error: " + e);
		}
	}
	
	
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {

		TransDao transDao = new TransDao();
		TransChooseLevelDao transChooseLevelDao= new TransChooseLevelDao();

		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE);

		// 取得申請資料
		List<TransVo> transList = transDao.getTransList(transVo);
		if (transList != null) {
			for (TransVo transVos : transList) {
				String transNum = transVos.getTransNum();
				logger.info("TransNum{}" , transNum);
				TransChooseLevelVo transChooseLevelVo = new TransChooseLevelVo();
				transChooseLevelVo.setTransNum(transNum);
				List<TransChooseLevelVo> transChooseLevelList = transChooseLevelDao.getTransChooseLevel(transChooseLevelVo);
				if(transChooseLevelList != null) {
					for(TransChooseLevelVo vo : transChooseLevelList) {
						//介接代碼(3),申請序號(12),保戶身份證(10),收文日(系統日yyyMMdd),評估日(系統日yyyMMdd),風險屬性(1),風險評分(3),保戶身份證(10)
						String score = String.valueOf(vo.getChooseScore());
						score = "   ".substring(0,score.length()-1) + score;
						String line = String.format(StringUtils.repeat("%s", 8),
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12),
								StringUtil.rpadBlank(vo.getRocId(), 10),
								systemTwDate,
								getTwDate(vo.getTransNum().substring(0,8)),
								StringUtil.rpadBlank(vo.getChooseLevelNew(), 1),
								score,
								StringUtil.rpadBlank(vo.getRocId(), 10)
						);
						logger.info(line);
						txtSb.append(line);
						txtSb.append("\r\n");
					}
				}
			}
			return transList;
		} else {
			return Collections.emptyList();
		}
	}
	
	private String getTwDate(String date) {
		String twYear = Integer.parseInt(date.substring(0, 4)) - 1911 + "";
		return StringUtils.rightPad(twYear, 3, "0") + date.substring(4, 8);
	}
}
