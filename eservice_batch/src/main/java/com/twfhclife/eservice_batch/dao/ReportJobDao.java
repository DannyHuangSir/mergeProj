package com.twfhclife.eservice_batch.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.mapper.ReportJobMapper;
import com.twfhclife.eservice_batch.model.ReportJobConditionVo;
import com.twfhclife.eservice_batch.model.ReportJobHistoryVo;
import com.twfhclife.eservice_batch.model.ReportJobVo;

/**
 * 建立 Report job 關聯
 * <ul>
 * <li>REPORT_JOB</li>
 * <li>REPORT_JOB_CONDITION</li>
 * <li>REPORT_JOB_HISTORY</li>
 * </ul>
 * @author Ken Wei
 *
 */
public class ReportJobDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(ReportJobDao.class);
	
	public ReportJobVo getReportJob(Integer reportJobId) {
		try {
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			return mapper.getReportJob(reportJobId);
		} catch(Exception e) {
			logger.error("getReportJob error: {}", e);
		} finally {
			this.release();
		}
		return null;
	}
	
	public List<ReportJobConditionVo> getReportCondition(ReportJobVo reportJobVo){
		try {
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			return mapper.getReportCondition(reportJobVo);
		} catch(Exception e) {
			logger.error("getReportCondition error: {}", e);
		} finally {
			this.release();
		}
		return null;
	}
	
	public int insertReportHitory(ReportJobHistoryVo reportJobHistoryVo) {
		try {
			int result = 0;
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			return mapper.insertReportHitory(reportJobHistoryVo);
		} catch(Exception e) {
			logger.error("insertReportHitory error: {}", e);
		} finally {
			this.commit();
			this.release();
		}
		return 0;
	}
	
	public List<Map<String, Object>> queryReport01(String realmId, Integer dateRange, String username, String roleId, String deptId, String titleId){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		try {
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			result = mapper.queryReport01(realmId, dateRange, username, roleId, deptId, titleId);
		} catch(Exception e) {
			logger.error("queryReport01 error: {}", e);
		} finally {
			this.release();
		}
		return result;
	}
	
	public List<Map<String, Object>> queryReport02(String realmId, String username, String roleId, String deptId, String titleId){
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			result = mapper.queryReport02(realmId, username, roleId, deptId, titleId);
		} catch(Exception e) {
			logger.error("queryReport02 error: {}", e);
		} finally {
			this.release();
		}
		return result;
	}
	
	public List<Map<String, Object>> queryReport03(String systemId, Integer dateRange){
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			ReportJobMapper mapper = this.getSqlSession().getMapper(ReportJobMapper.class);
			result = mapper.queryReport03(systemId, dateRange);
		} catch(Exception e) {
			logger.error("queryReport03 error: {}", e);
		} finally {
			this.release();
		}
		return result;
	}
}
