package com.twfhclife.adm.service.impl;

import com.twfhclife.adm.dao.JobMgmtDao;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IJobMgmtService;
import com.twfhclife.adm.service.IParameterService;
import com.twfhclife.generic.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobMgmtServiceImpl implements IJobMgmtService {

	@Autowired
	private KeycloakUtil keycloakUtil;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private JobMgmtDao jobMgmtDao;
	
	@Value("${keycloak.adm-realm:twfhclife}")
	protected String ADM_REALM;
	@Value("${keycloak.elife-realm:elife}")
	protected String ELIFE_REALM;

	@Value("${keycloak.elife-realm:elife}_jdzq")
	protected String JD_REALM;

	@Autowired
	private SshUtil sshUtil;

	@Override
	public Map<String, String> getRealtimeLoginStat() throws Exception {
		Map<String, String> statMap = new HashMap<>();
		statMap.putAll(keycloakUtil.getSessionStats(ELIFE_REALM));
		statMap.putAll(keycloakUtil.getSessionStats(ADM_REALM));
		statMap.putAll(keycloakUtil.getSessionStats(JD_REALM));
		return statMap;
	}

	@Override
	public List<Map<String, Object>> getReportJobList(ReportJobScheduleVo reportJobScheduleVo) {
		String reportRuleFormat = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_RULE_FORMAT");
		String jobTimeFormat = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_JOB_TIME_FORMAT");
		List<Map<String, Object>> listReportJob = jobMgmtDao.getReportJobList(reportJobScheduleVo);
		for(Map<String, Object> mapReportJob: listReportJob) {
			// 給予參數(condition->REPORT_RULE)/時間(CRON_EXP->JOB_TIME)中文化
			Integer reportJobId = Integer.parseInt(mapReportJob.get("REPORT_KEY").toString());
			List<ReportJobConditionVo> listCondition = jobMgmtDao.getReportJobConditionByReportJobId(reportJobId);
			String reportRule = "";
			for(ReportJobConditionVo condition: listCondition) {
				reportRule += 
					(reportRuleFormat.replaceAll("RULE_NAME", condition.getConditionCht())
							.replaceAll("RULE_VALUE", condition.getConditionValueCht()));
			}
			if(reportRule.endsWith(",")) {
				reportRule = reportRule.substring(0, reportRule.length()-1);
			}
			mapReportJob.put("REPORT_RULE", reportRule);
			
			String cronExp = mapReportJob.get("CRON_EXP").toString();
			String[] cronExpFormat = jobTimeFormat.split(",");
			List<String> listCronExp = java.util.Arrays.asList(cronExp.split(" "));
			java.util.Collections.reverse(listCronExp);
			String jobTime = "";
			for(int index = 0; index < listCronExp.size(); index++) {
				if(!"*".equals(listCronExp.get(index))) {
					jobTime += cronExpFormat[index].replaceAll("\\?", listCronExp.get(index));
				}
			}
			if(MyStringUtil.isNullOrEmpty(jobTime)) {
				// crontab default run per minutes  
				jobTime = cronExpFormat[cronExpFormat.length-1].replaceAll("\\?", "1");
			}
			mapReportJob.put("JOB_TIME", jobTime);
		}
		return listReportJob;
	}

	@Override
	public int getReportJobPageTotal(ReportJobScheduleVo reportJobScheduleVo) {
		return jobMgmtDao.getReportJobPageTotal(reportJobScheduleVo);
	}

	@Override
	public int insertReportJobSchedule(ReportJobScheduleVo reportJobScheduleVo) {
		// 設定 SEQ 
		reportJobScheduleVo.setReportJobId(jobMgmtDao.getReportJobSeq());
		// 設定 crontab 時間表達式
		if(!ValidateUtils.isCorrectCronTabTime(reportJobScheduleVo.getCronExp())) {
			// 時間表達式不完整
			return -1;
		}
		// 組成完整 crontab 命令式
		String executeCmd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_EXECUTE_CMD_FORMAT");
		String reportCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BATCH_CODE_" + reportJobScheduleVo.getReportId().toUpperCase());
		executeCmd = executeCmd
				.replaceAll("REPORT_CODE", reportCode)
				.replaceAll("REPORT_JOB_ID", reportJobScheduleVo.getReportJobId().toString());
		reportJobScheduleVo.setExecuteCmd(executeCmd);
		
		for(ReportJobConditionVo vo: reportJobScheduleVo.getListCondition()) {
			if(vo != null) {
				int result = 0;
				if(MyStringUtil.isNullOrEmpty(vo.getConditionCht())) {
					String conditionCht = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, vo.getCondition());
					vo.setConditionCht(MyStringUtil.isNullOrEmpty(conditionCht)? vo.getCondition(): conditionCht);
				}
				if(MyStringUtil.isNullOrEmpty(vo.getConditionValueCht())) {
					vo.setConditionValueCht(vo.getConditionValue());
				}
				result = jobMgmtDao.insertReportJobCondition(reportJobScheduleVo, vo);
				if(result <= 0) {
					return -1;
				}
			}
		}
		
		// 插入 Report_Job 
		return jobMgmtDao.insertReportJob(reportJobScheduleVo);
	}

	@Override
	public int updateReportJobSchedule(ReportJobScheduleVo reportJobScheduleVo) {
		// crontab 時間表達式
		if(!ValidateUtils.isCorrectCronTabTime(reportJobScheduleVo.getCronExp())) {
			// 時間表達式不完整
			return -1;
		}
		// 組成完整 crontab 命令式
		String executeCmd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_EXECUTE_CMD_FORMAT");
		String reportCode = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "BATCH_CODE_" + reportJobScheduleVo.getReportId().toUpperCase());
		executeCmd = executeCmd
				.replace("REPORT_CODE", reportCode)
				.replace("REPORT_JOB_ID", reportJobScheduleVo.getReportJobId().toString());
		reportJobScheduleVo.setExecuteCmd(executeCmd);
		
		// 更新 Report_Job_Condition
		List<String> nowCondition = jobMgmtDao.querySettingConditionKeyByReportJobId(reportJobScheduleVo.getReportJobId());
		for(ReportJobConditionVo condition: reportJobScheduleVo.getListCondition()) {
			int result = 0;
			if(condition != null) {
				condition.setModifyUser(reportJobScheduleVo.getModifyUser());
				condition.setReportJobId(reportJobScheduleVo.getReportJobId());
				if(MyStringUtil.isNullOrEmpty(condition.getConditionCht())) {
					String conditionCht = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, condition.getCondition());
					condition.setConditionCht(MyStringUtil.isNullOrEmpty(conditionCht)? condition.getCondition(): conditionCht);
				}
				if(MyStringUtil.isNullOrEmpty(condition.getConditionValueCht())) {
					condition.setConditionValueCht(condition.getConditionValue());
				}
			}
			if(condition != null && MyStringUtil.isNullOrEmpty(condition.getConditionValue())) {
				result = jobMgmtDao.deleteCondition(condition);
				continue;
			}
			if(nowCondition != null && condition != null && nowCondition.contains(condition.getCondition())) {
				result = jobMgmtDao.updateReportJobCondition(condition);
			} else {
				result = jobMgmtDao.insertReportJobCondition(reportJobScheduleVo, condition);
			}
			if(result <= 0) {
				return -1;
			}
		}
		
		// 更新 Report_Job 
		return jobMgmtDao.updateReportJob(reportJobScheduleVo);
	}

	@Override
	public int processCrontab() {
		// 設定 cron job 排程
		List<String> listCmd = new ArrayList<>();
		listCmd.addAll(jobMgmtDao.queryAllSettingReportJobCmd());
		listCmd.addAll(jobMgmtDao.queryAllSettingBusinessEventJobCmd());
		if(listCmd != null) {
			try {
				sshUtil.writeIntoCrontab(listCmd);
				return 1;
			} catch(Exception e) {
				return -1;
			}
		}
		return 0;
	}

	@Override
	public int delReportJob(ReportJobScheduleVo reportJobScheduleVo) {
		jobMgmtDao.deleteReportJobConditionByReportJobId(reportJobScheduleVo.getReportJobId());
		return jobMgmtDao.deleteReportJobByReportJobId(reportJobScheduleVo.getReportJobId());
	}

	@Override
	public ReportJobScheduleVo queryReportJobDetailByReportJobId(ReportJobScheduleVo reportJobScheduleVo) {
		List<ReportJobScheduleVo> list = jobMgmtDao.getReportJobScheduleList(reportJobScheduleVo);
		if(list != null && list.get(0) != null) {
			ReportJobScheduleVo vo = list.get(0); 
			vo.setListCondition(jobMgmtDao.getReportJobConditionByReportJobId(vo.getReportJobId()));
			return vo;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> queryReportJobHistory(ReportJobHistoryVo reportJobHistoryVo) {
		return jobMgmtDao.getReportJobHistory(reportJobHistoryVo);
	}

	@Override
	public int getReportJobHistoryPageTotal(ReportJobHistoryVo reportJobHistoryVo) {
		return jobMgmtDao.getReportJobHistoryPageTotal(reportJobHistoryVo);
	}

	@Override
	public String queryDownloadUrlById(Integer reportId) {
		return jobMgmtDao.queryDownloadUrlById(reportId);
	}

	@Override
	public List<ParameterVo> getEventJobSelect(String systemId) {
		return jobMgmtDao.getEvenJobSelect(systemId);
	}

	@Override
	public List<Map<String, Object>> getBusinessEventJobList(BusinessEventJobVo businessEventJobVo) {
		String jobTimeFormat = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_JOB_TIME_FORMAT");
		List<Map<String, Object>> listResult = jobMgmtDao.getBusinessEventJobList(businessEventJobVo);
		for(Map<String, Object> entry: listResult) {
			
			
			String cronExp = entry.get("CRON_EXP").toString();
			String[] cronExpFormat = jobTimeFormat.split(",");
			List<String> listCronExp = java.util.Arrays.asList(cronExp.split(" "));
			java.util.Collections.reverse(listCronExp);
			String jobTime = "";
			for(int index = 0; index < listCronExp.size(); index++) {
				if(!"*".equals(listCronExp.get(index))) {
					jobTime += cronExpFormat[index].replaceAll("\\?", listCronExp.get(index));
				}
			}
			if(MyStringUtil.isNullOrEmpty(jobTime)) {
				// crontab default run per minutes  
				jobTime = cronExpFormat[cronExpFormat.length-1].replaceAll("\\?", "1");
			}
			entry.put("JOB_TIME", jobTime);
		}
		return listResult;
	}

	@Override
	public int getBusinessEventJobPageTotal(BusinessEventJobVo businessEventJobVo) {
		return jobMgmtDao.getBusinessEventJobPageTotal(businessEventJobVo);
	}

	@Override
	public boolean addBusinessEventJob(BusinessEventJobVo businessEventJobVo) {
		
		if(!ValidateUtils.isCorrectCronTabTime(businessEventJobVo.getCronExp())) {
			// 時間表達式不完整
			return false;
		}
		businessEventJobVo.setEventJobId(jobMgmtDao.getEventJobSeq());
		// 組成完整 crontab 命令式
		String executeCmd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_EXECUTE_CMD_FORMAT");
		executeCmd = executeCmd
				.replaceAll("REPORT_CODE", BusinessEventJobVo.EVENT_JOB_CODE)
				.replaceAll("REPORT_JOB_ID", businessEventJobVo.getEventJobId().toString());
		businessEventJobVo.setExecuteCmd(executeCmd);
		return jobMgmtDao.insertBusinessEventJob(businessEventJobVo) == 1;
	}

	@Override
	public boolean delBusinessEventJob(BusinessEventJobVo businessEventJobVo) {
		return jobMgmtDao.delBusinessEventJob(businessEventJobVo) == 1;
	}

	@Override
	public boolean updBusinessEventJob(BusinessEventJobVo businessEventJobVo) {
		if(!ValidateUtils.isCorrectCronTabTime(businessEventJobVo.getCronExp())) {
			// 時間表達式不完整
			return false;
		}
		
		// 組成完整 crontab 命令式
		String executeCmd = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, "REPORT_EXECUTE_CMD_FORMAT");
		executeCmd = executeCmd
				.replaceAll("REPORT_CODE", BusinessEventJobVo.EVENT_JOB_CODE)
				.replaceAll("REPORT_JOB_ID", businessEventJobVo.getEventJobId().toString());
		businessEventJobVo.setExecuteCmd(executeCmd);
		return jobMgmtDao.updBusinessEventJob(businessEventJobVo) == 1;
	}

	@Override
	public BusinessEventJobVo getBusinessEventJobData(BusinessEventJobVo businessEventJobVo) {
		return jobMgmtDao.getBusinessEventJobData(businessEventJobVo);
	}

}