package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.MedicalTreatmentClaimFileDataDao;
import com.twfhclife.adm.model.MedicalTreatmentClaimFileDataVo;
import com.twfhclife.adm.service.IMedicalTreatmentClaimFileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author hui.chen
 * @create 2021-09-17
 * 醫療
 */
@Service
public class MedicalTreatmentClaimFileDataServiceImpl  implements IMedicalTreatmentClaimFileDataService {

    @Autowired
    private MedicalTreatmentClaimFileDataDao medicalTreatmentClaimFileDataDao;

    @Override
    public int updaetMedicalTreatmentClaimFileDataFileStatusCases(MedicalTreatmentClaimFileDataVo medicalTreatmentClaimFileDataVo)throws Exception {
        int i=0;
        if (medicalTreatmentClaimFileDataVo!=null){
            if (!StringUtils.isEmpty(medicalTreatmentClaimFileDataVo.getTransNum())
                && !StringUtils.isEmpty(medicalTreatmentClaimFileDataVo.getAllianceStatus())
            ) {
            i= medicalTreatmentClaimFileDataDao.updaetMedicalTreatmentClaimFileDataFileStatusCases(medicalTreatmentClaimFileDataVo);
            }
        }
        return  i;
    }
}
