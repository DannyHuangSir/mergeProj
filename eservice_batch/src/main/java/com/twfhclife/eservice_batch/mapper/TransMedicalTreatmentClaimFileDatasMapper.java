package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransMedicalTreatmentClaimFileDatasVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransMedicalTreatmentClaimFileDatasMapper {

	/**
	 * 取得需上傳影像系統的醫療保單上傳文件
	 * @param 
	 * @return List<TransMedicalTreatmentClaimFileDatasVo>
	 */
	public List<TransMedicalTreatmentClaimFileDatasVo> queryTransMedicalTreatmentClaimFileDatasVo();
	
	/**
	 * 更新已上傳影像系 統後回傳的TASK_ID
	 * @param vo
	 * @return int
	 */
	public int updateEzAcquireTaskId(@Param("vo") TransMedicalTreatmentClaimFileDatasVo vo);
}
