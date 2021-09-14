package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransCashPaymentMapper;
import com.twfhclife.eservice_batch.mapper.TransDepositMapper;
import com.twfhclife.eservice_batch.model.TransCashPaymentVo;
import com.twfhclife.eservice_batch.model.TransDepositVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransCashPaymentDao extends BaseDao {

    private static final Logger logger = LogManager.getLogger(TransCashPaymentDao.class);

    public List<TransCashPaymentVo> getTransDeposits(TransCashPaymentVo transCashPaymentVo) {
        List<TransCashPaymentVo> transCashPayments = null;
        try {
            TransCashPaymentMapper transCashPaymentMapper = this.getSqlSession().getMapper(TransCashPaymentMapper.class);
            transCashPayments = transCashPaymentMapper.getTransCashPayments(transCashPaymentVo.getTransNum());
        } catch (Exception e) {
            logger.error("getTransDeposits error:", e);
        } finally {
            this.release();
        }
        return transCashPayments;
    }
}
