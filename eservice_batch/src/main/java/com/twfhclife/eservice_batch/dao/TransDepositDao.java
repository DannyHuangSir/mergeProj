package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransDepositMapper;
import com.twfhclife.eservice_batch.model.TransDepositVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransDepositDao extends BaseDao {

    private static final Logger logger = LogManager.getLogger(TransDepositDao.class);

    public List<TransDepositVo> getTransDeposits(TransDepositVo transDepositVo) {
        List<TransDepositVo> transInvestments = null;
        try {
            TransDepositMapper transDepositMapper = this.getSqlSession().getMapper(TransDepositMapper.class);
            transInvestments = transDepositMapper.getTransDeposits(transDepositVo.getTransNum());
        } catch (Exception e) {
            logger.error("getTransDeposits error:", e);
        } finally {
            this.release();
        }
        return transInvestments;
    }
}
