package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransMedicalInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransMedicalInfoFileDataMapper {

	/**
	 * 取得需上傳影像系統的醫療保單上傳文件
	 * @param 
	 * @return List<TransMedicalInfoFileDataVo>
	 */
	List<TransMedicalInfoVo> queryTransMedicalInfoVo();
	
	/**
	 * 更新已上傳影像系 統後回傳的TASK_ID
	 * @param vo
	 * @return int
	 */
	int updateEzAcquireTaskId(@Param("vo") TransMedicalInfoVo vo);
}
