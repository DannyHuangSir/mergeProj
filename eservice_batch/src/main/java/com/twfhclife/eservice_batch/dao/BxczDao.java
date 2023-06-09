package com.twfhclife.eservice_batch.dao;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.twfhclife.eservice_batch.mapper.BxczMapper;
import com.twfhclife.eservice_batch.mapper.TransInsuranceClaimFileDataMapper;
import com.twfhclife.eservice_batch.model.SignFileVo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BxczDao extends BaseDao {

    private static Logger logger = LoggerFactory.getLogger(BxczDao.class);

    public List<SignFileVo> getSignFiles() {

        List<SignFileVo> signFiles = null;
        try {
            BxczMapper templateMapper  = this.getSqlSession().getMapper(BxczMapper.class);
            signFiles = templateMapper.getSignFiles();
        } catch (Exception e) {
            logger.error("BxczDao getNewSignStatus error:", e);
        } finally {
            this.release();
        }
        return signFiles;
    }

    public int updateEzAcquireTaskId(SignFileVo vo) {
        int rtn = -1;

        try {
            if (vo != null && StringUtils.isNotBlank(vo.getSignFileId())) {
                BxczMapper mapper = this.getSqlSession().getMapper(BxczMapper.class);
                rtn = mapper.updateEzAcquireTaskId(vo);
                this.commit();
            }
        } catch (Exception e) {
            logger.error("updateEzAcquireTaskId error:", e);
        } finally {
            this.release();
        }
        return rtn;
    }
}
