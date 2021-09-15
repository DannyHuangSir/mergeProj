package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.web.model.HospitalInsuranceCompanyVo;
import com.twfhclife.eservice.web.model.HospitalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
public interface HospitalInsuranceCompanyDao {

    /**
     * 新增.
     *
     * @param
     * @return 回傳影響筆數
     */
    int insertHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompanyVo)throws Exception;

    /**
     * 查詢所以
     * @param hospitalVo
     * @return
     */
    List<HospitalInsuranceCompanyVo> getHospitalInsuranceCompanyVoList(HospitalInsuranceCompanyVo hospitalInsuranceCompanyVo)throws Exception;
    /**
     * 修改
     *
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalInsuranceCompanyVo(HospitalInsuranceCompanyVo hospitalInsuranceCompanyVo)throws Exception;

    /**
     * 獲取id編號
     * @return
     */
    Float getHospitalInsuranceCompanyIdSequence()throws Exception;
    /**
     * 修改
     *
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalInsuranceCompanyVoList(@Param("hospitalInsuranceCompanyVo") List<HospitalInsuranceCompanyVo> hospitalInsuranceCompanyVo,@Param("status") String  status)throws Exception;

    int updateNotHospitalInsuranceCompanyVoIdList(@Param("hospitalInsuranceCompanyVo") List<HospitalInsuranceCompanyVo> hospitalInsuranceCompanyVo,@Param("status") String  status)throws Exception;
}
