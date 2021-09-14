package com.twfhclife.adm.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.JobTitleVo;

/**
 * 職位管理服務.
 * 
 * @author all
 */
public interface IJobTitleService {
	
	/**
	 * 取得職位管理查詢結果.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳職位管理查詢
	 */
	public List<Map<String, Object>> getJobTitlePageList(JobTitleVo jobTitleVo);

	/**
	 * 取得職位管理查詢結果總筆數.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳總筆數
	 */
	public int getJobTitlePageTotal(JobTitleVo jobTitleVo);
	
	/**
	 * 判斷職位名稱是否存在.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳職位名稱是否存在
	 */
	public boolean isJobTitleNameExist(JobTitleVo jobTitleVo);
	
	/**
	 * 職位管理-查詢.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳查詢結果
	 */
	public List<JobTitleVo> getJobTitle(JobTitleVo jobTitleVo);
	
	/**
	 * 新增職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	public int insertJobTitle(JobTitleVo jobTitleVo);
	
	/**
	 * 更新職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	public int updateJobTitle(JobTitleVo jobTitleVo);
	
	/**
	 * 刪除職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	public int deleteJobTitle(JobTitleVo jobTitleVo);
}