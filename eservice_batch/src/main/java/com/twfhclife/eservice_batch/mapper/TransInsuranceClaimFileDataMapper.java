package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransInsuranceClaimFileDataVo;

public interface TransInsuranceClaimFileDataMapper {

	/**
	 * 取得需上傳影像系統的保單理賠上傳文件
	 * @param 
	 * @return List<TransInsuranceClaimFileDataVo>
	 */
	public List<TransInsuranceClaimFileDataVo> queryTransInsuranceClaimFileDataVo();
	
	/**
	 * 更新已上傳影像系 統後回傳的TASK_ID
	 * @param vo
	 * @return int
	 */
	public int updateEzAcquireTaskId(@Param("vo") TransInsuranceClaimFileDataVo vo);
}
