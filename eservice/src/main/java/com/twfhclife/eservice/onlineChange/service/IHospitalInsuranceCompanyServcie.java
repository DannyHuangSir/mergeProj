package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import com.twfhclife.eservice.web.model.HospitalVo;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-14
 * 醫起通-醫院保險公司
 */
public interface IHospitalInsuranceCompanyServcie {


    /**
     * 新增.
     *
     * @param
     * @return 回傳影響筆數
     */
    int insertHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompany)throws Exception;

    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompany)throws Exception;
    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalInsuranceCompanyVoList(List<HospitalInsuranceCompanyVo>  hospitalInsuranceCompanies
                    ,String  status)throws Exception;
    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateNotHospitalInsuranceCompanyVoIdList(List<HospitalInsuranceCompanyVo>  hospitalInsuranceCompanies, String  status)throws Exception;
    /**
     * 查詢所以
     * @param hospitalVo
     * @return
     */
    List<HospitalInsuranceCompanyVo> getHospitalInsuranceCompanyVoList(HospitalInsuranceCompanyVo hospitalInsuranceCompanies)throws Exception;
}
