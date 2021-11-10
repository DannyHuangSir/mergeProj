package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransInvestmentDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.dao.TransPolicyHolderProfileDao;
import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransInvestmentUtil {

    private static final Logger logger = LogManager.getLogger(TransInvestmentUtil.class);
    private static final String TRANS_TYPE = "INVESTMENT";
    private static final String TRANS_STATUS = "1";   // 申請中
    private static final String UPLOAD_CODE = "029"; // 介接代碼

    public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
        logger.info("Start running generate apply file: {}", TRANS_TYPE);

        TransDao transDao = new TransDao();
        TransPolicyDao transPolicyDao = new TransPolicyDao();
        TransInvestmentDao transInvestmentDao = new TransInvestmentDao();
        ParameterDao parameterDao = new ParameterDao();

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
                TransInvestmentVo transInvestmentVo = new TransInvestmentVo();
                transInvestmentVo.setTransNum(transNum);
                List<TransInvestmentVo> list = transInvestmentDao.getTransInvestments(transInvestmentVo);
                if (list != null && list.size() > 0) {
                    // 取得保單號碼
                    TransPolicyVo tpQryVo = new TransPolicyVo();
                    tpQryVo.setTransNum(transNum);
                    List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
                    final List<String> showAccountInvts = new ArrayList<>();
                    parameterDao.getParameterByCategoryCode("eservice", "SHOW_ACCOUNT_INVT_NOS")
                            .forEach(e -> showAccountInvts.add(e.getParameterValue()));
                    boolean addedAccount = false;
                    if (transPolicyList != null) {
                        for (TransPolicyVo tpVo : transPolicyList) {
                            logger.info("TransNum : {}, policyNo : {}", transNum, tpVo.getPolicyNo());
                            for (TransInvestmentVo vo : list) {
                                if (!addedAccount && showAccountInvts.contains(vo.getInvtNo())) {
                                    addedAccount = true;
                                    TransAccountVo accountVo = transInvestmentDao.findAccount(transNum, vo.getInvtNo());
                                    //介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyMMdd),生效日(系統日yyyMMdd),受益類別(1),受益人身分證號(10),匯款戶名(10),銀行代碼(3),分行代碼(4)匯款帳號(16),國際號SwiftCode(16),英文戶名(60)
                                    if (accountVo != null) {
                                        String line = String.format(StringUtils.repeat("%s", 13),
                                                "035",
                                                StringUtil.rpadBlank(transNum, 12),
                                                StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
                                                systemTwDate,
                                                systemTwDate,
                                                "5",
                                                StringUtil.rpadBlank(accountVo.getRocId(), 10),
                                                StringUtil.rpadBlank(accountVo.getAccountName(), 10),
                                                StringUtil.rpadBlank(accountVo.getBankCode(), 3),
                                                StringUtil.rpadBlank(accountVo.getBranchCode(), 4),
                                                StringUtil.rpadBlank(accountVo.getBankAccount(), 16),
                                                StringUtil.rpadBlank(accountVo.getSwiftCode(), 16),
                                                StringUtil.rpadBlank(accountVo.getEnglishName(), 60)
                                        );
                                        logger.info(line);
                                        txtSb.append(line);
                                        txtSb.append("\r\n");
                                    }
                                }
                                // 介接代碼(3),申請序號(12),保單號碼(10)投資標的(10),變更後單位（18),1(1)，收文日(系統日yyyMMdd),生效日(下個周月日yyyMMdd
                                String line = String.format(StringUtils.repeat("%s", 8),
                                        UPLOAD_CODE,
                                        StringUtil.rpadBlank(transNum, 12),
                                        StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
                                        systemTwDate,
                                        systemTwDate,
                                        StringUtil.rpadBlank(vo.getInvtNo(), 10),
                                        StringUtil.lpad(String.valueOf(vo.getDistributionRatio()), 3, " "),
                                        "1"
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
