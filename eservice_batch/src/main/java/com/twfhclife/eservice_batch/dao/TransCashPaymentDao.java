package com.twfhclife.eservice_batch.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.TransCashPaymentMapper;
import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransCashPaymentVo;

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

    public TransAccountVo findAccount(String transNum) {
        TransAccountVo transAccountVo = null;
        try {
            TransCashPaymentMapper transCashPaymentMapper = this.getSqlSession().getMapper(TransCashPaymentMapper.class);
            transAccountVo = transCashPaymentMapper.findAccount(transNum);
        } catch (Exception e) {
            logger.error("findAccount error:", e);
        } finally {
            this.release();
        }
        return transAccountVo;
    }
}
