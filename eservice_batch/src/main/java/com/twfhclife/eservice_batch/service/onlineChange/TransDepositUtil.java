package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransDepositDao;
import com.twfhclife.eservice_batch.dao.TransInvestmentDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransDepositVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class TransDepositUtil {

    private static final Logger logger = LogManager.getLogger(TransDepositUtil.class);
    private static final String TRANS_TYPE = "DEPOSIT";
    private static final String TRANS_STATUS = "1";  // 申請中
    private static final String UPLOAD_CODE = "028"; // 介接代碼

    public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
        logger.info("Start running generate apply file: {}", TRANS_TYPE);

        TransDao transDao = new TransDao();
        TransPolicyDao transPolicyDao = new TransPolicyDao();
        TransDepositDao transDepositDao = new TransDepositDao();

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
                TransDepositVo transDepositVo = new TransDepositVo();
                transDepositVo.setTransNum(transNum);
                List<TransDepositVo> list = transDepositDao.getTransDeposits(transDepositVo);
                if (list != null && list.size() > 0) {
                    // 取得保單號碼
                    TransPolicyVo tpQryVo = new TransPolicyVo();
                    tpQryVo.setTransNum(transNum);
                    List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
                    if (transPolicyList != null) {
                        for (TransPolicyVo tpVo : transPolicyList) {
                            logger.info("TransNum : {}, policyNo : {}", transNum, tpVo.getPolicyNo());
                            for (TransDepositVo vo : list) {
                                // 介接代碼(3),申請序號(12),保單號碼(10),投資標的(10),轉出單位（18）,匯款戶名(20),銀行名稱(10),分行名稱(10),銀行代碼(3),分行代碼(4),匯款帳號(16),國際號SwiftCode(16),英文戶名(60),
                                //收文日(系統日yyyMMdd),生效日(系統日yyyMMdd)
                                String line = String.format(StringUtils.repeat("%s", 14),
                                        UPLOAD_CODE,
                                        StringUtil.rpadBlank(transNum, 12),
                                        StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
                                        StringUtil.rpadBlank(vo.getInvtNo(), 10),
                                        StringUtil.lpad(String.valueOf(vo.getAmount()), 18, "0"),
                                        StringUtil.rpadBlank(String.valueOf(vo.getAccountName()), 18),
                                        StringUtil.rpadBlank(vo.getAccountName(), 20),
                                        StringUtil.rpadBlank(vo.getBankName(), 10),
                                        StringUtil.rpadBlank(vo.getBranchName(), 10),
                                        StringUtil.rpadBlank(vo.getBankCode(), 3),
                                        StringUtil.rpadBlank(vo.getBranchCode(), 4),
                                        StringUtil.rpadBlank(vo.getBankCode(), 16),
                                        StringUtil.rpadBlank(vo.getSwiftCode(), 16),
                                        StringUtil.rpadBlank(vo.getEnglishName(), 60),
                                        systemTwDate,
                                        systemTwDate
                                );
                                logger.info(line);
                                txtSb.append(line);
                                txtSb.append("\r\n");
                            }
                        }
                    }
                }
            }
            return transList;
        } else {
            return Collections.emptyList();
        }
    }
}
