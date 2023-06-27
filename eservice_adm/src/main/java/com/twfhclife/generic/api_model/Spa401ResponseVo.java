package com.twfhclife.generic.api_model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Spa401ResponseVo implements Serializable {

	private List<Spa401DataResponse> details = Lists.newArrayList();
	private Integer totalPage;
	private Integer totalNum;
	private Integer currentPage;
	private Integer currentPageSize;

	public List<Spa401DataResponse> getDetails() {
		return details;
	}

	public void setDetails(List<Spa401DataResponse> details) {
		this.details = details;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getCurrentPageSize() {
		return currentPageSize;
	}

	public void setCurrentPageSize(Integer currentPageSize) {
		this.currentPageSize = currentPageSize;
	}

	public static class Spa401DataResponse implements Serializable {

		private Long id;
		private String orgId;
		private String hospital;
		private String serviceType;
		private String caseNo;
		private String eventTime;
		private Double fileSize;
		private Double fee;
		private String linkId;
		private String reportState;
		private String reportMessage;
		private String inEservice;
		private String replayStatus;
		private String replayTime;

		public String getReplayTime() {
			return replayTime;
		}

		public void setReplayTime(String replayTime) {
			this.replayTime = replayTime;
		}

		public String getReplayStatus() {
			return replayStatus;
		}

		public void setReplayStatus(String replayStatus) {
			this.replayStatus = replayStatus;
		}

		public String getInEservice() {
			return inEservice;
		}

		public void setInEservice(String inEservice) {
			this.inEservice = inEservice;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getOrgId() {
			return orgId;
		}

		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}

		public String getHospital() {
			return hospital;
		}

		public void setHospital(String hospital) {
			this.hospital = hospital;
		}

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		public String getCaseNo() {
			return caseNo;
		}

		public void setCaseNo(String caseNo) {
			this.caseNo = caseNo;
		}

		public String getEventTime() {
			return eventTime;
		}

		public void setEventTime(String eventTime) {
			this.eventTime = eventTime;
		}

		public Double getFileSize() {
			return fileSize;
		}

		public void setFileSize(Double fileSize) {
			this.fileSize = fileSize;
		}

		public Double getFee() {
			return fee;
		}

		public void setFee(Double fee) {
			this.fee = fee;
		}

		public String getLinkId() {
			return linkId;
		}

		public void setLinkId(String linkId) {
			this.linkId = linkId;
		}

		public String getReportState() {
			return reportState;
		}

		public void setReportState(String reportState) {
			this.reportState = reportState;
		}

		public String getReportMessage() {
			return reportMessage;
		}

		public void setReportMessage(String reportMessage) {
			this.reportMessage = reportMessage;
		}
	}
}
