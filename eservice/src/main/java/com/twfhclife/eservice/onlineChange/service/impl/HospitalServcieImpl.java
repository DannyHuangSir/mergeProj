package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.HospitalDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.service.IHospitalServcie;
import com.twfhclife.eservice.web.model.HospitalVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
@Service
public class HospitalServcieImpl implements IHospitalServcie {
    private static final Logger logger = LogManager.getLogger(HospitalServcieImpl.class);

    @Autowired
    public HospitalDao hospitalDao;

    @Override
    public int insertHospitalVo(HospitalVo hospitalVo)throws Exception {
        Float hospitalIdSequence = hospitalDao.getHospitalIdSequence();
                   hospitalVo.setId(hospitalIdSequence);
        return hospitalDao.insertHospitalVo(hospitalVo);
    }

    @Override
    public int updateHospitalVo(HospitalVo hospitalVo)throws Exception {

        return hospitalDao.updateHospitalVo(hospitalVo);
    }

    @Override
    public int updateHospitalVoList(List<HospitalVo> hospitalVos,String  status) throws Exception {
        return hospitalDao.updateHospitalVoList(hospitalVos,status);
    }

    @Override
    public int updateNotHospitalVoIdList(List<HospitalVo> hospitalVos, String status) throws Exception {
        return hospitalDao.updateNotHospitalVoIdList(hospitalVos,status);
    }

    @Override
    public List<HospitalVo> getHospitalVoList(HospitalVo hospitalVo)throws Exception {
        return hospitalDao.getHospitalVoList(hospitalVo);
    }

    @Override
    public String getTransStatusHistoryByRejectReason(String transNum, String transStatusAbnormal) {

        return hospitalDao.getTransStatusHistoryByRejectReason(transNum,transStatusAbnormal);
    }
}
