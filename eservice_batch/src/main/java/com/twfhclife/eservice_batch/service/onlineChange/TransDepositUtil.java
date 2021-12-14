package com.twfhclife.eservice_batch.service.onlineChange;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransDepositDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.TransDepositVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
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
                            boolean isFirst = true;
                            String policyType = tpVo.getPolicyNo().substring(0,2);
                            StringBuilder line = new StringBuilder();
                            for (TransDepositVo vo : list) {
                                //獲取最新單位凈值
                                String currencyAccounts = parameterDao.getParameterValueByCode("eservice", "DEPOSIT_" + policyType + "_CURRENCY_ACCOUNT");
                                String stopAccounts = parameterDao.getParameterValueByCode("eservice", "DEPOSIT_" + policyType + "_STOP_ACCOUNT");
                                BigDecimal fundValue;
                                if ((StringUtils.isNotBlank(currencyAccounts) && currencyAccounts.contains(vo.getInvtNo()))
                                    || (StringUtils.isNotBlank(stopAccounts) && stopAccounts.contains(vo.getInvtNo()))) {
                                    logger.info("保單提領(贖回)投資標：{}，貨幣賬戶：{}，停泊账户：{} ，使用金額作爲提領單位。", vo.getInvtNo(), currencyAccounts, stopAccounts);
                                    fundValue = BigDecimal.valueOf(vo.getAmount());
                                } else {
                                MutablePair<BigDecimal, Date> fund = transDepositDao.getNearFundValue(vo.getInvtNo());
                                if (fund == null) {
                                    logger.warn("保單提領(贖回)投資標：{}，無法獲取最新凈值，跳過介接。", vo.getInvtNo());
                                    continue;
                                }
                                logger.info("保單提領(贖回) 投資標：{}的最新净值：{}， 交易日期：{}", vo.getInvtNo(), fund.getLeft(), fund.getRight());
                                    fundValue = BigDecimal.valueOf(vo.getAmount()).divide(fund.getLeft(), 4, BigDecimal.ROUND_DOWN);
                                logger.info("保單提領(贖回) 投資標：{}，計算後的單位凈值為：{}", vo.getInvtNo(), fundValue);
                                }

                                // 介接代碼(3),申請序號(12),保單號碼(10),收文日(系統日yyyMMdd),生效日(系統日yyyMMdd),匯款戶名(20),銀行名稱(10),分行名稱(10),銀行代碼(3),分行代碼(4),匯款帳號(16),國際號SwiftCode(16),英文戶名(60),投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）,投資標的(10),轉出單位（18）
                                if (isFirst) {
                                    line.append(String.format(StringUtils.repeat("%s", 13),
                                        UPLOAD_CODE,
                                        StringUtil.rpadBlank(transNum, 12),
                                        StringUtil.rpadBlank(tpVo.getPolicyNo(), 10),
                                            systemTwDate,
                                            systemTwDate,
                                        StringUtil.rpadBlank(vo.getAccountName(), 20),
                                            StringUtil.newRpadBlank(vo.getBankName(), 10),
                                            StringUtil.newRpadBlank(vo.getBranchName(), 10),
                                        StringUtil.rpadBlank(vo.getBankCode(), 3),
                                        StringUtil.rpadBlank(vo.getBranchCode(), 4),
                                        StringUtil.rpadBlank(vo.getBankAccount(), 16),
                                        StringUtil.rpadBlank(vo.getSwiftCode(), 16),
                                            StringUtil.rpadBlank(vo.getEnglishName(), 60)
                                    ));
                                    isFirst = false;
                            }
                                line.append(String.format(StringUtils.repeat("%s", 2),
                                        StringUtil.rpadBlank(vo.getInvtNo(), 10),
                                        StringUtil.lpad(String.valueOf(fundValue.intValue()), 14, " ")) + "0000");
                            }
                            txtSb.append(String.format(StringUtils.repeat("%s", 1),
                                    StringUtil.rpadBlank(line.toString(), 3 + 12 + 10 + 20 + 10 + 10 + 3 + 4 + 16 + 16 + 60 + 7 + 7 + 28 * 10)));
                            txtSb.append("\r\n");
                            logger.info(line);
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
