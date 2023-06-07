package com.twfhclife.eservice.onlineChange.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class IndividualChooseIpVo {
	/**
	 * 流水號
	 */
	private int id;
	/**
	 * 身分證
	 */
	private String rocId;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * 驗聖馬發送時間
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime otpTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRocId() {
		return rocId;
	}
	public void setRocId(String rocId) {
		this.rocId = rocId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public LocalDateTime getOtpTime() {
		return otpTime;
	}
	public void setOtpTime(LocalDateTime otpTime) {
		this.otpTime = otpTime;
	}
	
	
}
