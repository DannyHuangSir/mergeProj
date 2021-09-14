package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.JobTitleDao;
import com.twfhclife.adm.model.JobTitleVo;
import com.twfhclife.adm.service.IJobTitleService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 職位管理服務.
 * 
 * @author all
 */
@Service
public class JobTitleServiceImpl implements IJobTitleService {

	@Autowired
	private JobTitleDao jobTitleDao;
	
	/**
	 * 取得職位管理查詢結果
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳登入記錄查詢
	 */
	@RequestLog
	@Override
	public List<Map<String, Object>> getJobTitlePageList(JobTitleVo jobTitleVo) {
		return jobTitleDao.getJobTitlePageList(jobTitleVo);
	}

	/**
	 * 取得職位管理查詢結果總筆數
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getJobTitlePageTotal(JobTitleVo jobTitleVo) {
		return jobTitleDao.getJobTitlePageTotal(jobTitleVo);
	}

	/**
	 * 判斷職位名稱是否存在.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳職位名稱是否存在
	 */
	@RequestLog
	@Override
	public boolean isJobTitleNameExist(JobTitleVo jobTitleVo) {
		return (jobTitleDao.getCountByTitleName(jobTitleVo) > 0);
	}
	
	/**
	 * 職位管理-查詢.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳查詢結果
	 */
	public List<JobTitleVo> getJobTitle(JobTitleVo jobTitleVo) {
		return jobTitleDao.getJobTitle(jobTitleVo);
	}

	/**
	 * 新增職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int insertJobTitle(JobTitleVo jobTitleVo) {
		return jobTitleDao.insertJobTitle(jobTitleVo);
	}
	
	/**
	 * 更新職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int updateJobTitle(JobTitleVo jobTitleVo) {
		return jobTitleDao.updateJobTitle(jobTitleVo);
	}

	/**
	 * 刪除職位.
	 * 
	 * @param jobTitleVo JobTitleVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteJobTitle(JobTitleVo jobTitleVo) {
		return jobTitleDao.deleteJobTitle(jobTitleVo);
	}
}