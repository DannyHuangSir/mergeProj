package com.twfhclife.eservice_batch.model;


public class ETLJobLogVo {

	private String jobSeq;
	private String jobName;
	private long jobCount;
	private String startDate;
	private String endDate;
	private int spendTime;

	public String getJobSeq() {
		return jobSeq;
	}

	public void setJobSeq(String jobSeq) {
		this.jobSeq = jobSeq;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getJobCount() {
		return jobCount;
	}

	public void setJobCount(long jobCount) {
		this.jobCount = jobCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(int spendTime) {
		this.spendTime = spendTime;
	}

}
