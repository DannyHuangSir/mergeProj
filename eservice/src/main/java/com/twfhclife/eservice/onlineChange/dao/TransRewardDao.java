package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransRewardVo;

/**
 * 變更增值回饋分享金領取方式 Dao.
 * 
 * @author all
 */
public interface TransRewardDao {
	
	/**
	 * 變更增值回饋分享金領取方式-查詢.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳查詢結果
	 */
	List<TransRewardVo> getTransReward(@Param("transRewardVo") TransRewardVo transRewardVo);
	
	/**
	 * 變更增值回饋分享金領取方式-新增.
	 * 
	 * @param transRewardVo TransRewardVo
	 * @return 回傳影響筆數
	 */
	int insertTransReward(@Param("transRewardVo") TransRewardVo transRewardVo);
}