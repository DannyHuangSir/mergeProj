package com.twfhclife.eservice.onlineChange.dao;

import com.twfhclife.eservice.web.model.HospitalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hui.chen
 * @create 2021-09-14
 */
public interface HospitalDao {

    /**
     * 新增.
     *
     * @param
     * @return 回傳影響筆數
     */
    int insertHospitalVo(HospitalVo hospitalVo)throws Exception;

    /**
     * 查詢所以
     * @param hospitalVo
     * @return
     */
    List<HospitalVo> getHospitalVoList(HospitalVo hospitalVo)throws Exception;
    /**
     * 修改
     *
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalVo(HospitalVo hospitalVo)throws Exception;

    /**
     * 獲取id編號
     * @return
     */
    Float getHospitalIdSequence()throws Exception;
    /**
     * 修改
     *
     * @param
     * @return 回傳影響筆數
     */
    int updateHospitalVoList(@Param("hospitalVos") List<HospitalVo> hospitalVos,@Param("status") String  status)throws Exception;

    int updateNotHospitalVoIdList(@Param("hospitalVos") List<HospitalVo> hospitalVos,@Param("status") String  status)throws Exception;
    /**
     * 進行查詢異常描述信息id
     * @param transNum
     * @param transStatusAbnormal
     * @return
     */
    String getTransStatusHistoryByRejectReason(@Param("transNum")String transNum,@Param("transStatus") String transStatusAbnormal);

}
