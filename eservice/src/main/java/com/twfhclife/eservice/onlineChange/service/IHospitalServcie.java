package com.twfhclife.eservice.onlineChange.service;

import com.twfhclife.eservice.web.model.HospitalVo;
import com.twfhclife.eservice.web.model.TransVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-14
 * 醫療-醫院
 */
public interface IHospitalServcie {


    /**
     * 新增.
     *
     * @param
     * @return 回傳影響筆數
     */
    int insertHospitalVo(HospitalVo hospitalVo)throws Exception;

    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalVo(HospitalVo hospitalVo)throws Exception;
    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalVoList(List<HospitalVo>  hospitalVos,String  status)throws Exception;
    /**
     * 修改
     * @param
     * @return 回傳影響筆數
     */
    int updateNotHospitalVoIdList(List<HospitalVo>  hospitalVos,String  status)throws Exception;
    /**
     * 查詢所以
     * @param hospitalVo
     * @return
     */
    List<HospitalVo> getHospitalVoList(HospitalVo hospitalVo)throws Exception;


    /**
     * 進行查詢異常描述信息id
     * @param transNum
     * @param transStatusAbnormal
     * @return
     */
    String getTransStatusHistoryByRejectReason(String transNum, String transStatusAbnormal);
}
