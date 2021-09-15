package com.twfhclife.eservice.onlineChange.service.impl;

import com.twfhclife.eservice.onlineChange.dao.HospitalInsuranceCompanyDao;
import com.twfhclife.eservice.onlineChange.service.IHospitalInsuranceCompanyServcie;
import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-15
 */
@Service
public class HospitalInsuranceCompanyServcieImpl implements
        IHospitalInsuranceCompanyServcie {
    private static final Logger logger = LogManager.getLogger(HospitalInsuranceCompanyServcieImpl.class);

    @Autowired
    public HospitalInsuranceCompanyDao hospitalInsuranceCompanyDao;

    @Override
    public int insertHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompany) throws Exception {
        Float hospitalInsuranceCompanyIdSequence = hospitalInsuranceCompanyDao.getHospitalInsuranceCompanyIdSequence();
        hospitalInsuranceCompany.setId(hospitalInsuranceCompanyIdSequence);
        return hospitalInsuranceCompanyDao.insertHospitalInsuranceCompanyVo(hospitalInsuranceCompany);
    }

    @Override
    public int updateHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompany) throws Exception {
        return hospitalInsuranceCompanyDao.updateHospitalInsuranceCompanyVo(hospitalInsuranceCompany);
    }

    @Override
    public int updateHospitalInsuranceCompanyVoList(List<HospitalInsuranceCompanyVo> hospitalInsuranceCompanies, String status) throws Exception {
        return hospitalInsuranceCompanyDao.updateHospitalInsuranceCompanyVoList(hospitalInsuranceCompanies,status);
    }

    @Override
    public int updateNotHospitalInsuranceCompanyVoIdList(List<HospitalInsuranceCompanyVo> hospitalInsuranceCompanies, String status) throws Exception {
        return hospitalInsuranceCompanyDao.updateNotHospitalInsuranceCompanyVoIdList(hospitalInsuranceCompanies,status);
    }

    @Override
    public List<HospitalInsuranceCompanyVo> getHospitalInsuranceCompanyVoList(HospitalInsuranceCompanyVo hospitalInsuranceCompanies) throws Exception {
        return hospitalInsuranceCompanyDao.getHospitalInsuranceCompanyVoList(hospitalInsuranceCompanies);
    }
}
