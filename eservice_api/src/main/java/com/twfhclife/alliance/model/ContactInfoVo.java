package com.twfhclife.alliance.model;

import java.util.ArrayList;
import java.util.List;

public class ContactInfoVo {
	
	
	/**************傳收件狀態******************/
	
	public static final String STATUS_RECEIVED = "RECEIVED";//已接收
	
	public static final String STATUS_COMPLETED = "COMPLETED";//已回報轉收件處理狀態
	
	
	/***************臺銀件狀態*****************/

	public static final String STATUS_WAITING_FOR_UPLOAD = "NOT_UPLOADED";//等待上傳
	
	public static final String STATUS_UPLOADED = "UPLOADED";//已上傳完成
	
	
	/**************206回報狀態******************/
	
    public static final String STATUS_3 = "3";//非保护或處理失敗
	
	public static final String STATUS_Y = "Y";//處理成功
	
	public static final String NON_INSURED = "非本公司保 戶";
	
	public static final String MSG_SUCCESS = "處理成功";
	
	public static final String MSG_FAIL ="處理失敗";
	
	
	private Float  claimSeqId;
	private String seqId;
	private String caseId;
	private String code;
	private String msg;
	private String cidNo;
	private String cbirdate;
	private String cname;
	private String crname;
	private String cename;
	private String cPhone;
	private String cMail;
	private String cAddress;
	private String name;
	private String rname;
	private String ename;
	private String idNo;
	private String rZipCode;
	private String rAddress;
	private String mZipCode;
	private String mAddress;
	private String phone;
	private String homeTel1;
	private String homeTelCode1;
	private String homeTelExt1;
	private String homeTel2;
	private String homeTelCode2;
	private String homeTelExt2;
	private String mail;
	private String from;
	private List<CompanyVo> to;
	private String agreement;
	private String logDateTime;
	private String logDesc1;
	private String source;
	private String status;
	private String sendAlliance;
	private Float  notifySeqId;
	private List<String> fileNames;
	private List<ContactInfoFileDataVo> fileDatas;
	private String transNum;
	public Float getClaimSeqId() {
		return claimSeqId;
	}
	public void setClaimSeqId(Float claimSeqId) {
		this.claimSeqId = claimSeqId;
	}
	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCidNo() {
		return cidNo;
	}
	public void setCidNo(String cidNo) {
		this.cidNo = cidNo;
	}
	public String getCbirdate() {
		return cbirdate;
	}
	public void setCbirdate(String cbirdate) {
		this.cbirdate = cbirdate;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCrname() {
		return crname;
	}
	public void setCrname(String crname) {
		this.crname = crname;
	}
	public String getCename() {
		return cename;
	}
	public void setCename(String cename) {
		this.cename = cename;
	}
	public String getcPhone() {
		return cPhone;
	}
	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}
	public String getcMail() {
		return cMail;
	}
	public void setcMail(String cMail) {
		this.cMail = cMail;
	}
	public String getcAddress() {
		return cAddress;
	}
	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getrZipCode() {
		return rZipCode;
	}
	public void setrZipCode(String rZipCode) {
		this.rZipCode = rZipCode;
	}
	public String getrAddress() {
		return rAddress;
	}
	public void setrAddress(String rAddress) {
		this.rAddress = rAddress;
	}
	public String getmZipCode() {
		return mZipCode;
	}
	public void setmZipCode(String mZipCode) {
		this.mZipCode = mZipCode;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHomeTel1() {
		return homeTel1;
	}
	public void setHomeTel1(String homeTel1) {
		this.homeTel1 = homeTel1;
	}
	public String getHomeTelCode1() {
		return homeTelCode1;
	}
	public void setHomeTelCode1(String homeTelCode1) {
		this.homeTelCode1 = homeTelCode1;
	}
	public String getHomeTelExt1() {
		return homeTelExt1;
	}
	public void setHomeTelExt1(String homeTelExt1) {
		this.homeTelExt1 = homeTelExt1;
	}
	public String getHomeTel2() {
		return homeTel2;
	}
	public void setHomeTel2(String homeTel2) {
		this.homeTel2 = homeTel2;
	}
	public String getHomeTelCode2() {
		return homeTelCode2;
	}
	public void setHomeTelCode2(String homeTelCode2) {
		this.homeTelCode2 = homeTelCode2;
	}
	public String getHomeTelExt2() {
		return homeTelExt2;
	}
	public void setHomeTelExt2(String homeTelExt2) {
		this.homeTelExt2 = homeTelExt2;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public List<CompanyVo> getTo() {
		return to;
	}
	public void setTo(List<CompanyVo> to) {
		this.to = to;
	}
	public void setTo(String strTo) {
		if(strTo!=null && strTo.trim()!="") {
			String[] strs = strTo.split(",");
			List<CompanyVo> tempto = new ArrayList<CompanyVo>();
			for(String str : strs) {
				if(str!=null && str.trim()!="") {
					CompanyVo vo = new CompanyVo();
					vo.setCompanyId(str);;
					tempto.add(vo);
				}
			}
			
			if(tempto!=null && !tempto.isEmpty()) {
				this.to = tempto;
			}
		}
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public String getLogDateTime() {
		return logDateTime;
	}
	public void setLogDateTime(String logDateTime) {
		this.logDateTime = logDateTime;
	}
	public String getLogDesc1() {
		return logDesc1;
	}
	public void setLogDesc1(String logDesc1) {
		this.logDesc1 = logDesc1;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSendAlliance() {
		return sendAlliance;
	}
	public void setSendAlliance(String sendAlliance) {
		this.sendAlliance = sendAlliance;
	}
	public Float getNotifySeqId() {
		return notifySeqId;
	}
	public void setNotifySeqId(Float notifySeqId) {
		this.notifySeqId = notifySeqId;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	public List<ContactInfoFileDataVo> getFileDatas() {
		return fileDatas;
	}
	public void setFileDatas(List<ContactInfoFileDataVo> fileDatas) {
		this.fileDatas = fileDatas;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	


	
	
}
