package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransFundConversionMapper;
import com.twfhclife.eservice_batch.mapper.TransInvestmentMapper;
import com.twfhclife.eservice_batch.mapper.TransLoanMapper;
import com.twfhclife.eservice_batch.model.TransAccountVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import com.twfhclife.eservice_batch.model.TransLoanVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransInvestmentDao extends BaseDao {

    private static final Logger logger = LogManager.getLogger(TransInvestmentDao.class);

    public List<TransInvestmentVo> getTransInvestments(TransInvestmentVo transInvestmentVo) {
        List<TransInvestmentVo> transInvestments = null;
        try {
            TransInvestmentMapper transInvestmentMapper = this.getSqlSession().getMapper(TransInvestmentMapper.class);
            transInvestments = transInvestmentMapper.getTransInvestments(transInvestmentVo.getTransNum());
        } catch (Exception e) {
            logger.error("getTransInvestments error:", e);
        } finally {
            this.release();
        }
        return transInvestments;
    }

    public TransAccountVo findAccount(String transNum, String invtNo) {
        TransAccountVo transAccountVo = null;
        try {
            TransInvestmentMapper transInvestmentMapper = this.getSqlSession().getMapper(TransInvestmentMapper.class);
            transAccountVo = transInvestmentMapper.findAccount(transNum, invtNo);
        } catch (Exception e) {
            logger.error("findAccount error:", e);
        } finally {
            this.release();
        }
        return transAccountVo;
    }
}
