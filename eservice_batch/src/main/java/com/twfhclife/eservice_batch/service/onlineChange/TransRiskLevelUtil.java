package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransRiskLevelDao;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransRiskLevelVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class TransRiskLevelUtil {

    private static final Logger logger = LogManager.getLogger(TransRiskLevelUtil.class);
    private static final String TRANS_TYPE = "RISK_LEVEL";
    private static final String TRANS_STATUS = "1";   // 申請中
    private static final String UPLOAD_CODE = "031"; // 介接代碼

    public static void updateIndividual(String transNum) {
        try {
            TransRiskLevelDao transRiskLevelDao = new TransRiskLevelDao();
            TransRiskLevelVo vo = transRiskLevelDao.getEffectUserTransRiskLevel(transNum);
            if (vo != null) {
                transRiskLevelDao.updateIndividual(vo);
            }
        } catch (Exception e) {
            logger.error("updateIndividual error: " + e);
        }
    }

    public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
        logger.info("Start running generate apply file: {}", TRANS_TYPE);

        TransDao transDao = new TransDao();
        TransPolicyDao transPolicyDao = new TransPolicyDao();
        TransRiskLevelDao transRiskLevelDao = new TransRiskLevelDao();

        // 申請資料條件
        TransVo transVo = new TransVo();
        transVo.setStatus(TRANS_STATUS);
        transVo.setTransType(TRANS_TYPE);

        // 取得申請資料
        List<TransVo> transList = transDao.getTransList(transVo);
        if (transList != null) {
            for (TransVo trantsVo : transList) {
                String transNum = trantsVo.getTransNum();
                logger.info("TransNum: {}", transNum);
                // 取得資料
                TransRiskLevelVo transRiskLevelVo = new TransRiskLevelVo();
                transRiskLevelVo.setTransNum(transNum);
                List<TransRiskLevelVo> list = transRiskLevelDao.getTransLevel(transRiskLevelVo);
                if (list != null && list.size() > 0) {
                    String policyNo = transRiskLevelDao.getTop1PolicyNo(list.get(0).getRocId());
                    // 取得保單號碼
                    TransPolicyVo tpQryVo = new TransPolicyVo();
                    tpQryVo.setTransNum(transNum);
                            logger.info("TransNum : {}, policyNo : {}", transNum, policyNo);
                            for (TransRiskLevelVo vo : list) {
                        //  介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyMMdd),生效日(系統日yyyMMdd),風險屬性(1),風險評分(3),保戶身份證(10)
                                String score = String.valueOf(vo.getRiskScore());
                        score = "   ".substring(0, score.length() - 1) + score;
                                String line = String.format(StringUtils.repeat("%s", 8),
                                        UPLOAD_CODE,
                                        StringUtil.rpadBlank(transNum, 12),
                                        StringUtil.rpadBlank(policyNo, 10),
                                        systemTwDate,
                                        getTwDate(vo.getTransNum().substring(0,8)) ,
                                        StringUtil.rpadBlank(vo.getRiskLevelNew(), 1),
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
