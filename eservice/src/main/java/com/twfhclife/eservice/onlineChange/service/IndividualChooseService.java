package com.twfhclife.eservice.onlineChange.service;

import java.util.Date;
import java.util.List;

import com.twfhclife.eservice.onlineChange.model.IndividualChooseBlackListVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseIpVo;
import com.twfhclife.eservice.onlineChange.model.IndividualChooseVo;
import com.twfhclife.eservice.onlineChange.model.TransChooseLevelVo;
import com.twfhclife.generic.api_model.LicohilVo;
import com.twfhclife.generic.api_model.PolicyDetailVo;

public interface IndividualChooseService {
	/**
	 * 取得用戶保單資料
	 */
	public List<PolicyDetailVo> getPolicyDataByRocId(String rocId);
	/**
	 * 驗證畫面填寫資料是否正確(本行客戶)
	 * @param invtPolicyList
	 * @param individualChooseVo
	 * @return
	 */
	public String checkDetail(LicohilVo licohilVo , IndividualChooseVo individualChooseVo) ;
	
	/**
	 * 驗證畫面填寫資料是否正確(非本行客戶)
	 * @param invtPolicyList
	 * @param individualChooseVo
	 * @return
	 */
	public String checkDetail2(IndividualChooseVo oldIndividualChooseVo , IndividualChooseVo individualChooseVo) ;
	
	/**
	 * 透過身分證查詢是否有風險評估的資料
	 * @param rocId
	 * @return
	 */
	public IndividualChooseVo  getIndividualChoosData(String rocId);

	/**
	 * 比對風險評估日期是否可以變更(本行)
	 * @param birthday
	 * @return
	 */
	public boolean checkRatingDate(Date ratingDate);
	
	/**
	 * 比對風險評估日期是否可以變更(非本行)
	 * @param birthday
	 * @return
	 */
	public boolean checkRatingDate2(Date ratingDate);
	/**
	 * 比對系統最新的評估日期
	 * @param individalRatingDate eserivce 
	 * @param licohilRatingDate CTC
	 * @return
	 */
	public boolean compareDate(Date individalRatingDate , Date licohilRatingDate);
	
	/**
	 * 新增風險評估紀錄
	 * @param individualChooseVo
	 * @return
	 */
	public int insertIndividualChoose(IndividualChooseVo individualChooseVo);
	/**
	 * 查詢 紀錄是否存在
	 * @param rocId
	 * @return
	 */
	public TransChooseLevelVo getTransChooseLevel(String rocId);
	/**
	 * 取得投資型保單種類
	 * @return
	 */
	public List<String> getpolicyInvestmentType();
	/**
	 * 取得最新一筆投資型保單
	 * @param licohilVoList
	 * @return
	 */
	public LicohilVo getpolicyHaveInvestmentType(List<LicohilVo> licohilVoList);
	/**
	 * 取得最新一筆非投資型保單
	 * @param licohilVoList
	 * @return
	 */
	public LicohilVo getpolicyNotHaveInvestmentType(List<LicohilVo> licohilVoList);
	/**
	 * 驗證日期
	 * @param individualChooseVo 畫面所需參數設置
	 * @param licohilVo CTC核心資料
	 * @param oldIndividualChooseVo eservice資料
	 * @return
	 */
	public IndividualChooseVo verificationDate(IndividualChooseVo individualChooseVo , LicohilVo licohilVo , IndividualChooseVo oldIndividualChooseVo );
	
	public IndividualChooseBlackListVo getIndividualChooseBlackList(String ip);
	
	public int insertIndividualChooseIp(IndividualChooseIpVo individualChooseIpVo);
	
	public List<IndividualChooseIpVo> getIndividualChooseIp(String ip ,int time);
	
	public int insertOrUpdateIndividualChooseBlackList(String ip);
	
	public boolean sendPdfMail(IndividualChooseVo individualChooseVo , String email1 , String email2);
	
}
