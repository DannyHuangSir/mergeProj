package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.TransCashPaymentDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransInvestmentDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransCashPaymentVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class TransCashPaymentUtil {

    private static final Logger logger = LogManager.getLogger(TransCashPaymentUtil.class);
    private static final String TRANS_TYPE = "CASH_PAYMENT";
    private static final String TRANS_STATUS = "1";   // 申請中
    private static final String UPLOAD_CODE = "032"; // 介接代碼

    public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
        logger.info("Start running generate apply file: {}", TRANS_TYPE);

        TransDao transDao = new TransDao();
        TransPolicyDao transPolicyDao = new TransPolicyDao();
        TransCashPaymentDao transCashPaymentDao = new TransCashPaymentDao();

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
                TransCashPaymentVo transCashPaymentVo = new TransCashPaymentVo();
                transCashPaymentVo.setTransNum(transNum);
                List<TransCashPaymentVo> list = transCashPaymentDao.getTransDeposits(transCashPaymentVo);
                if (list != null && list.size() > 0) {
                    // 取得保單號碼
                    TransPolicyVo tpQryVo = new TransPolicyVo();
                    tpQryVo.setTransNum(transNum);
                    List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
                    if (transPolicyList != null) {
                        for (TransPolicyVo tpVo : transPolicyList) {
                            logger.info("TransNum : {}, policyNo : {}", transNum, tpVo.getPolicyNo());
                            for (TransCashPaymentVo vo : list) {
                                //介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyMMdd),生效日(系統日yyyMMdd),新收益分配或撥回資產分配方式(1),受益類別(1),匯款戶名(20),銀行名稱(10),分行名稱(10),銀行代碼(3),分行代碼(4),匯款帳號(16),國際號SwiftCode(16),英文戶名(60)
                                String line = String.format(StringUtils.repeat("%s", 15),
                                        UPLOAD_CODE,
                                        StringUtil.rpadBlank(transNum, 12),
                                        StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
                                        systemTwDate,
                                        systemTwDate,
                                        StringUtil.rpadBlank(vo.getAllocation(), 1),
                                        "5",
                                        StringUtil.rpadBlank(vo.getAccountName(), 20),
                                        StringUtil.rpadBlank(vo.getBankName(), 10),
                                        StringUtil.rpadBlank(vo.getBranchName(), 10),
                                        StringUtil.rpadBlank(vo.getBankCode(), 3),
                                        StringUtil.rpadBlank(vo.getBranchCode(), 4),
                                        StringUtil.rpadBlank(vo.getBankAccount(), 16),
                                        StringUtil.rpadBlank(vo.getSwiftCode(), 16),
                                        StringUtil.rpadBlank(vo.getEnglishName(), 60)
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
