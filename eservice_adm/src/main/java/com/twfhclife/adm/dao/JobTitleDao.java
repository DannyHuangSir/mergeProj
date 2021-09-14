package com.twfhclife.adm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.model.JobTitleVo;

/**
 * 職位管理 Dao.
 * 
 * @author all
 */
public interface JobTitleDao {
	
	/**
	 * 取得職位管理查詢結果.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳職位管理查詢結果
	 */
	List<Map<String, Object>> getJobTitlePageList(@Param("jobTitleVo") JobTitleVo jobTitleVo);

	/**
	 * 取得職位管理查詢結果總筆數.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳總筆數
	 */
	int getJobTitlePageTotal(@Param("jobTitleVo") JobTitleVo jobTitleVo);
	
	/**
	 * 取得職位名稱的數量.
	 * 
	 * @param jobTitleVo
	 * @return 職位名稱的數量
	 */
	int getCountByTitleName(@Param("jobTitleVo") JobTitleVo jobTitleVo);
	
	/**
	 * 職位管理-查詢.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳查詢結果
	 */
	List<JobTitleVo> getJobTitle(@Param("jobTitleVo") JobTitleVo jobTitleVo);
	
	/**
	 * 新增職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	int insertJobTitle(@Param("jobTitleVo") JobTitleVo jobTitleVo);
	
	/**
	 * 更新職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	int updateJobTitle(@Param("jobTitleVo") JobTitleVo jobTitleVo);

	/**
	 * 刪除職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	int deleteJobTitle(@Param("jobTitleVo") JobTitleVo jobTitleVo);
}

