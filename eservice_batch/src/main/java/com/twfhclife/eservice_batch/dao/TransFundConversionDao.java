package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransFundConversionMapper;
import com.twfhclife.eservice_batch.mapper.TransInvestmentMapper;
import com.twfhclife.eservice_batch.model.TransFundConversionVo;
import com.twfhclife.eservice_batch.model.TransInvestmentVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransFundConversionDao extends BaseDao {

    private static final Logger logger = LogManager.getLogger(TransFundConversionDao.class);

    public List<TransFundConversionVo> getTransFundConversions(TransFundConversionVo transFundConversionVo) {
        List<TransFundConversionVo> transFundConversionVos = null;
        try {
            TransFundConversionMapper transFundConversionMapper = this.getSqlSession().getMapper(TransFundConversionMapper.class);
            transFundConversionVos = transFundConversionMapper.getTransFundConversions(transFundConversionVo.getTransNum());
        } catch (Exception e) {
            logger.error("getTransFundConversions error:", e);
        } finally {
            this.release();
        }
        return transFundConversionVos;
    }
}
