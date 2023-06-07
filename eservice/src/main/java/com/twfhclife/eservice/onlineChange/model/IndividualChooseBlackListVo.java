package com.twfhclife.eservice.onlineChange.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class IndividualChooseBlackListVo {
	
	/**
	 * IP
	 */
	private String ip ;
	/**
	 * 黑名單種類
	 * 1.系統自創 2.管理人員
	 */
	private String blackCategory ;
	/**
	 * 黑名單開始時間
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime blackStart ;
	/**
	 * 黑名單結束時間
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime blackEnd ;
	
	/**
	 * 狀態
	 * 0 = 失效 ， 1 = 有效
	 */
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getBlackCategory() {
		return blackCategory;
	}
	public void setBlackCategory(String blackCategory) {
		this.blackCategory = blackCategory;
	}
	public LocalDateTime getBlackStart() {
		return blackStart;
	}
	public void setBlackStart(LocalDateTime blackStart) {
		this.blackStart = blackStart;
	}
	public LocalDateTime getBlackEnd() {
		return blackEnd;
	}
	public void setBlackEnd(LocalDateTime blackEnd) {
		this.blackEnd = blackEnd;
	}
	
}
