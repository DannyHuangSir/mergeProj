package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.onlineChange.dao.*;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.api_client.OnlineChangeClient;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.service.IOptionService;
import com.twfhclife.generic.service.ISendSmsService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.RptUtils;
import com.twfhclife.generic.util.RptUtils2;
import com.twfhclife.generic.util.StatuCode;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 医通申請書套印服務.
 * 
 * @author all
 */
@Service
public class MedicalTreatmentServicelmpl implements IMedicalTreatmentService {

	private static final Logger logger = LogManager.getLogger(MedicalTreatmentServicelmpl.class);
	
	@Autowired
	ITransService transService;

	@Autowired
	private BankInfoDao bankInfoDao;
	
	@Autowired
	private TransDao transDao;

	@Autowired
	private TransMedicalTreatmentClaimDao transMedicalTreatmentClaimDao;

	@Autowired
	private TransPolicyDao transPolicyDao;
	
	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private IOptionService optionService;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private ISendSmsService smsService;
	
	@Value("${upload.file.save.path}")
	private String FILE_SAVE_PATH;


	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
//				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status) && Integer.parseInt(status) >= 31) {
						vo.setApplyLockedFlag("Y");
//						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
						appendMsg(vo,OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
//					}
				}
			}
		}
	}
	
	private void appendMsg(PolicyListVo policyListVo, String msg) {
		if(policyListVo.getApplyLockedMsg() != null) {
			String eStr = policyListVo.getApplyLockedMsg();
			if(!eStr.contains(msg)) {
				policyListVo.setApplyLockedMsg(eStr + "|" + msg);
			}
		}else {
			policyListVo.setApplyLockedMsg(msg);
		}
		
	}

	/**
	 * 取得醫起通申請書報表 byte[].
	 * 
	 * @param claimVo ClaimVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF(TransMedicalTreatmentClaimVo claimVo) {
		byte[] pdfByte = null;
		try {
			RptUtils rptUtils = new RptUtils("claim.pdf");
			String regDate = "(\\d{4})-(\\d{2})-(\\d{2})";
//			claimVo.getAccidentDate().replaceAll(regDate, "$1$2$3");

			// 要保人
			rptUtils.txt(claimVo.getCustomerName(), 11, 1, 103, 754);

			// 保單號碼
			String policyNo = claimVo.getPolicyNo();
			rptUtils.txt(policyNo, 11, 1, 275, 754);

			// 申請項目
			float f = (float)635.3;
			for (String itemCode : claimVo.getApplyItemList()) {
				if ("死亡給付".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.0f, f);
				}
				if ("殘廢給付".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.0f + (46 * 1), f);
				}
				if ("住院醫療".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 102.5f + (46 * 2), f);
				}
				if ("傷害醫療".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 103.0f + (46 * 3), f);
				}
				if ("癌症醫療".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 103.5f + (46 * 4), f);
				}
				if ("重大疾病".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 104.5f + (46 * 5), f);
				}
				if ("生命末期提前給付".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 105.0f + (46 * 6), f);
				}
				if ("其他".equals(itemCode)) {
					rptUtils.txt("■", 10, 1, 133.5f + (46 * 7), f);
					rptUtils.txt(claimVo.getItemOther(), 10, 1, 486f, 635.3f);
				}
			}

			// 事故者資料-被保險人姓名
			rptUtils.txt(claimVo.getName(), 11, 1, 103, 724);

			// 事故者資料-與主被保險人(員工)之關係
			if ("01".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279, 731);
			}
			if ("02".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279 + 47.5f, 731);
			}
			if ("03".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279, 716);
			}
			if ("04".equals(claimVo.getRelation())) {
				rptUtils.txt("■", 12, 1, 279 + 47.5f, 716);
			}

			// 事故者資料-身分證號碼
			String rocId = claimVo.getIdNo();
			if (!StringUtils.isEmpty(rocId)) {
				if (rocId.length() >= 1) {
					rptUtils.txt(rocId.substring(0, 1), 11, 1, 102.3f, 696);
				}
				if (rocId.length() >= 2) {
					rptUtils.txt(rocId.substring(1, 2), 11, 1, 102.3f + (10.7f * 1), 696);
				}
				if (rocId.length() >= 3) {
					rptUtils.txt(rocId.substring(2, 3), 11, 1, 102.3f + (10.7f * 2), 696);
				}
				if (rocId.length() >= 4) {
					rptUtils.txt(rocId.substring(3, 4), 11, 1, 102.3f + (10.7f * 3), 696);
				}
				if (rocId.length() >= 5) {
					rptUtils.txt(rocId.substring(4, 5), 11, 1, 102.3f + (10.7f * 4), 696);
				}
				if (rocId.length() >= 6) {
					rptUtils.txt(rocId.substring(5, 6), 11, 1, 102.3f + (10.7f * 5), 696);
				}
				if (rocId.length() >= 7) {
					rptUtils.txt(rocId.substring(6, 7), 11, 1, 102.3f + (10.7f * 6), 696);
				}
				if (rocId.length() >= 8) {
					rptUtils.txt(rocId.substring(7, 8), 11, 1, 102.3f + (10.7f * 7), 696);
				}
				if (rocId.length() >= 9) {
					rptUtils.txt(rocId.substring(8, 9), 11, 1, 102.3f + (10.7f * 8), 696);
				}
				if (rocId.length() >= 10) {
					rptUtils.txt(rocId.substring(9, 10), 11, 1, 102.3f + (10.7f * 9), 696);
				}
			}

			// 事故者資料-聯絡電話
			rptUtils.txt(claimVo.getTel(), 11, 1, 275, 696);
			// 事故者資料-出生日期
			if (claimVo.getBirdate() != null) {
				rptUtils.txt(claimVo.getBirdate().replaceAll(regDate, "$1$2$3"), 11, 1, 103, 674.5f);
			}
			// 事故者資料-手機號碼
			rptUtils.txt(claimVo.getPhone(), 11, 1, 275, 674.5f);
			// 事故者資料-職業
			rptUtils.txt(claimVo.getJob(), 11, 1, 460, 674.5f);
			// 事故者資料-聯絡地址
			rptUtils.txt(claimVo.getAddress(), 11, 1, 103, 653);

			// 疾病-診斷病名
			rptUtils.breakTxt(claimVo.getDiseaseName(), 10, 1, 103, 603, 10);
			// 疾病-該疾病初診日
			rptUtils.txt(claimVo.getAccidentDate(), 10, 1, 103, 562);
			// 疾病-曾就診之醫院
			rptUtils.txt(claimVo.getHospital(), 10, 1, 103, 535);

			if (claimVo.getAccidentDate() != null) {
				String accidentDate = claimVo.getBirdate().replaceAll(regDate, "$1$2$3");
				// 意外-發生時間-年
				rptUtils.txt(accidentDate.substring(0, 4), 10, 1, 275, 603);
				// 意外-發生時間-月
				rptUtils.txt(accidentDate.substring(4, 6), 10, 1, 274.0f + (35 * 1), 603);
				// 意外-發生時間-日
				rptUtils.txt(accidentDate.substring(6, 8), 10, 1, 267.0f + (35 * 2), 603);

			}
			// 意外-發生時間-時
			rptUtils.txt(claimVo.getAccidentTime(), 10, 1, 257.0f + (35 * 3), 603);
			// 意外-事故地點
			rptUtils.txt(claimVo.getAccidentLocation(), 10, 1, 275, 583);
			// 意外-事故處理單位
			rptUtils.txt(claimVo.getPoliceStation(), 10, 1, 275, 562);
			// 意外-承辦員警
			rptUtils.txt(claimVo.getPoliceName(), 10, 1, 275, 544);
			// 意外-員警電話
			rptUtils.txt(claimVo.getPolicePhone(), 10, 1, 275, 526);
			// 意外-事故原因及送醫經過詳情
			rptUtils.breakTxt(claimVo.getAccidentDescr(), 9, 1, 390, 595, 20);

			// 被保險人是否投保別家保險公司之保險
			if ("Y".equals(claimVo.getOtherCompanyInsured())) {
				rptUtils.txt("■", 11, 1, 47, 483.5f);
			}
			if ("N".equals(claimVo.getOtherCompanyInsured())) {
				rptUtils.txt("■", 11, 1, (float) 47 + 32, 483.5f);
			}

			// 公司名稱
			rptUtils.txt(claimVo.getOtherInsuCompany(), 10, 1, 138, 490);
			// 保險種類
			rptUtils.txt(claimVo.getOtherInsuType(), 10, 1, 273, 490);
			// 保險金額
			rptUtils.txt(claimVo.getOtherInsuAmout(), 10, 1, 390, 490);
			// 同時申請醫起通
			if ("Y".equals(claimVo.getOtherInsuClaim())) {
				rptUtils.txt("■", 11, 1, 468, 490);
			}
			if ("N".equals(claimVo.getOtherInsuClaim())) {
				rptUtils.txt("■", 11, 1, 468 + 32.5f, 490);
			}

			// 給付方式
			if ("1".equals(claimVo.getPaymentMethod())) {
				rptUtils.txt("■", 12, 1, 28, 429);
			}
			if ("2".equals(claimVo.getPaymentMethod())) {
				rptUtils.txt("■", 12, 1, 28, 385);
			}

			// 匯款帳戶
			rptUtils.txt(claimVo.getAccountName(), 11, 1, 165, 449);

			BankInfoVo qryBankInfoVo = new BankInfoVo();
			qryBankInfoVo.setBankId(claimVo.getBankCode());
			qryBankInfoVo.setBranchId(claimVo.getBranchCode());
			List<BankInfoVo> bankInfoList = bankInfoDao.getBankInfo(qryBankInfoVo);
			if (bankInfoList != null && bankInfoList.size() == 1) {
				BankInfoVo bankInfoVo = bankInfoList.get(0);
				// 銀行名稱
				rptUtils.txt(bankInfoVo.getBankName(), 11, 1, 165, 429.2f);
				// 分行名稱
				rptUtils.txt(bankInfoVo.getBranchName(), 11, 1, 295, 429.2f);
			}

			// 帳號
			String accountNumber = claimVo.getBankAccount();
			if (!StringUtils.isEmpty(accountNumber)) {
				if (accountNumber.length() >= 1) {
					rptUtils.txt(accountNumber.substring(0, 1), 11, 1, 166.5f, 409);
				}
				if (accountNumber.length() >= 2) {
					rptUtils.txt(accountNumber.substring(1, 2), 11, 1, 166.5f + (13.8f * 1), 409);
				}
				if (accountNumber.length() >= 3) {
					rptUtils.txt(accountNumber.substring(2, 3), 11, 1, 166.5f + (13.8f * 2), 409);
				}
				if (accountNumber.length() >= 4) {
					rptUtils.txt(accountNumber.substring(3, 4), 11, 1, 166.5f + (13.8f * 3), 409);
				}
				if (accountNumber.length() >= 5) {
					rptUtils.txt(accountNumber.substring(4, 5), 11, 1, 166.5f + (13.8f * 4), 409);
				}
				if (accountNumber.length() >= 6) {
					rptUtils.txt(accountNumber.substring(5, 6), 11, 1, 166.5f + (13.8f * 5), 409);
				}
				if (accountNumber.length() >= 7) {
					rptUtils.txt(accountNumber.substring(6, 7), 11, 1, 166.5f + (13.8f * 6), 409);
				}
				if (accountNumber.length() >= 8) {
					rptUtils.txt(accountNumber.substring(7, 8), 11, 1, 166.5f + (13.8f * 7), 409);
				}
				if (accountNumber.length() >= 9) {
					rptUtils.txt(accountNumber.substring(8, 9), 11, 1, 166.5f + (13.8f * 8), 409);
				}
				if (accountNumber.length() >= 10) {
					rptUtils.txt(accountNumber.substring(9, 10), 11, 1, 166.5f + (13.8f * 9), 409);
				}
				if (accountNumber.length() >= 11) {
					rptUtils.txt(accountNumber.substring(10, 11), 11, 1, 166.5f + (13.8f * 10), 409);
				}
				if (accountNumber.length() >= 12) {
					rptUtils.txt(accountNumber.substring(11, 12), 11, 1, 166.5f + (13.8f * 11), 409);
				}
				if (accountNumber.length() >= 13) {
					rptUtils.txt(accountNumber.substring(12, 13), 11, 1, 166.5f + (13.8f * 12), 409);
				}
				if (accountNumber.length() >= 14) {
					rptUtils.txt(accountNumber.substring(13, 14), 11, 1, 166.5f + (13.8f * 13), 409);
				}
			}

			// 郵遞區號
			String postalCode = claimVo.getZipCode();
			if (!StringUtils.isEmpty(postalCode)) {
				if (postalCode.length() >= 1) {
					rptUtils.txt(postalCode.substring(0, 1), 10, 1, 77.5f, 378.6f);
				}
				if (postalCode.length() >= 2) {
					rptUtils.txt(postalCode.substring(1, 2), 10, 1, 77.5f + (12.5f * 1), 378.6f);
				}
				if (postalCode.length() >= 3) {
					rptUtils.txt(postalCode.substring(2, 3), 10, 1, 77f + (12.5f * 2), 378.6f);
				}
				if (postalCode.length() >= 4) {
					rptUtils.txt(postalCode.substring(3, 4), 10, 1, 76f + (12.5f * 3), 378.6f);
				}
				if (postalCode.length() >= 5) {
					rptUtils.txt(postalCode.substring(4, 5), 10, 1, 76f + (12.5f * 4), 378.6f);
				}
			}

			// 郵寄地址
			rptUtils.txt(claimVo.getPostalAddr(), 11, 1, 135, 378.6f);

			pdfByte= rptUtils.toPdf();
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}

	/**
	 * 保單醫起通申請-新增.
	 *
	 * @param transMedicalTreatmentClaimVo TransInsuranceClaimVo
	 * @return 回傳影響筆數
	 */
	@Override
	@Transactional
	public Map<String,Object> inserttransMedicalTreatmentClaimVo(TransMedicalTreatmentClaimVo transMedicalTreatmentClaimVo, TransStatusHistoryVo hisVo) {
		
		String transNum = transMedicalTreatmentClaimVo.getTransNum();
		if(StringUtils.isBlank(transNum)) {
			transNum = transService.getTransNum();
		}
		
		String userId = transMedicalTreatmentClaimVo.getUserId();

		String status = OnlineChangeUtil.TRANS_STATUS_APPLYING;
		// 判斷符合聯盟鏈醫起通的商品清單
		String policyNo = transMedicalTreatmentClaimVo.getPolicyNo();
		boolean flag = false;
		if(policyNo == null || "".equals(policyNo)) {
			flag = true;
			//transNum = "000000000";
			status = OnlineChangeUtil.TRANS_STATUS_ABNORMAL;
		} else {
			// 判斷聯盟件
			String fromCompanyId = transMedicalTreatmentClaimVo.getFrom();
			if(fromCompanyId != null && !OnlineChangeUtil.FROM_COMPANY_L01.equals(fromCompanyId)) {
				if(transMedicalTreatmentClaimVo.getStauts() !=null  && OnlineChangeUtil.TRANS_STATUS_ABNORMAL.equals(transMedicalTreatmentClaimVo.getStauts())){
					status = OnlineChangeUtil.TRANS_STATUS_ABNORMAL;
				}else {
					status = OnlineChangeUtil.TRANS_STATUS_APPLYING;
				}
			}
		}

		if (StringUtils.equals(transMedicalTreatmentClaimVo.getSignAgree(), "Y")) {
			status = OnlineChangeUtil.TRANS_STATUS_WAIT_SIGN;
		}

		if (StringUtils.equals(status, OnlineChangeUtil.TRANS_STATUS_APPLYING)) {
			transMedicalTreatmentClaimVo.setApplyDate(new Date());
		}
		
		int result = 0;
		try {
			// 新增線上申請主檔
			Date toDay = new Date();
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(toDay);
			transVo.setTransType(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE);
			transVo.setStatus(status);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(toDay);
			result = transDao.insertTrans(transVo);

			if(result>0) {
				// 新增保單號碼
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				result = transPolicyDao.insertTransPolicy(transPolicyVo);
			}


			//處理時間
			String regDate = "(\\d{4})-(\\d{2})-(\\d{2})";
			String regTime = "(\\d{2}):(\\d{2})";
			String temp = null;
			if (transMedicalTreatmentClaimVo.getBirdate() != null) {
				temp = transMedicalTreatmentClaimVo.getBirdate().replaceAll(regDate, "$1$2$3");
				transMedicalTreatmentClaimVo.setBirdate(temp);
			}
			if (transMedicalTreatmentClaimVo.getAccidentDate() != null) {
				temp = transMedicalTreatmentClaimVo.getAccidentDate().replaceAll(regDate, "$1$2$3");
				transMedicalTreatmentClaimVo.setAccidentDate(temp);
			}
			if (transMedicalTreatmentClaimVo.getAccidentTime() != null) {
				temp = transMedicalTreatmentClaimVo.getAccidentTime().replaceAll(regTime, "$1$2");
				transMedicalTreatmentClaimVo.setAccidentTime(temp);
			}
			if (transMedicalTreatmentClaimVo.getApplicationDate() != null) {
				temp = transMedicalTreatmentClaimVo.getApplicationDate().replaceAll(regDate, "$1$2$3");
				transMedicalTreatmentClaimVo.setApplicationDate(temp);
			}
			if (transMedicalTreatmentClaimVo.getApplicationTime() != null) {
				temp = transMedicalTreatmentClaimVo.getApplicationTime().replaceAll(regTime, "$1$2");
				transMedicalTreatmentClaimVo.setApplicationTime(temp);
			}

			if (transMedicalTreatmentClaimVo.getPoliceDate() != null) {
				temp = transMedicalTreatmentClaimVo.getPoliceDate().replaceAll(regDate, "$1$2$3");
				transMedicalTreatmentClaimVo.setPoliceDate(temp);
			}
			if (transMedicalTreatmentClaimVo.getPoliceTime() != null) {
				temp = transMedicalTreatmentClaimVo.getPoliceTime().replaceAll(regTime, "$1$2");
				transMedicalTreatmentClaimVo.setPoliceTime(temp);
			}

//			logger.error("TransInsuranceClaimVo is: {}", transInsuranceClaimVo.toString());
//			TransInsuranceClaimVo vo = new TransInsuranceClaimVo();
//			vo.setApplyItem("死亡給付;殘廢給付");
//			vo.setCustomerName("鑫守一");
//			vo.setPolicyNo("3110075257");
//			vo.setName("鑫守一");
//			vo.setIdNo("A122021826");
//			vo.setBirdate("19670212");
//			vo.setPhone("0946345534");
//			vo.setPaymentMethod("1");
//			vo.setBankCode("004");
//			vo.setBankAccount("4321434");
//			vo.setAccountName("test");
//			vo.setAccidentDate("20210118");
//			vo.setAccidentTime("1111");
//			vo.setAccidentCause("2");
//			vo.setPoliceDate("20210120");
//			vo.setPoliceTime("1122");
			transMedicalTreatmentClaimVo.setApplicationItem("1");

			transMedicalTreatmentClaimVo.setClaimSeqId(transMedicalTreatmentClaimDao.getInsuranceClaimSequence());
//			vo.setClaimSeqId(transInsuranceClaimVo.getClaimSeqId());
			transMedicalTreatmentClaimVo.setTransNum(transNum);
//			vo.setTransNum(transNum);

			if(result>0) {
				//對時間進行處理
				String authorizationEndDate = transMedicalTreatmentClaimVo.getAuthorizationEndDate();
				String authorizationStartDate = transMedicalTreatmentClaimVo.getAuthorizationStartDate();
				if(!StringUtils.isEmpty(authorizationEndDate)){
					transMedicalTreatmentClaimVo.setAuthorizationEndDate(authorizationEndDate.replace("-",""));
				}
				if(!StringUtils.isEmpty(authorizationStartDate)){
					transMedicalTreatmentClaimVo.setAuthorizationStartDate(authorizationStartDate.replace("-",""));
				}
				// 新增保單醫起通申請
				logger.error("transMedicalTreatmentClaimVo", transMedicalTreatmentClaimVo);
				result = transMedicalTreatmentClaimDao.addInsuranceClaim(transMedicalTreatmentClaimVo);
			}

			if (result > 0) {
				if ((CollectionUtils.isNotEmpty(transMedicalTreatmentClaimVo.getMedicalInfo()))) {
					for (TransMedicalTreatmentClaimMedicalInfoVo vo : transMedicalTreatmentClaimVo.getMedicalInfo()) {
						vo.setClaimId(transMedicalTreatmentClaimVo.getClaimSeqId());
						vo.setCreateDate(new Date());
						vo.setHeTime(vo.getHeTime() == null ? "" :vo.getHeTime().replaceAll("/", ""));
						vo.setHsTime(vo.getHsTime() == null ? "" :vo.getHsTime().replaceAll("/", ""));
						result = transMedicalTreatmentClaimDao.addInsuranceClaimMedicalInfo(vo);
						if (result < 1) {
							break;
						}
					}
				}
			}

//			TransInsuranceClaimFileDataVo data = new TransInsuranceClaimFileDataVo();
//			data.setType("A");
//			data.setFilePath("C:\\root\\");
//			data.setFileName("20210129124640.jpg");
//			List<TransInsuranceClaimFileDataVo> dataList = new ArrayList<TransInsuranceClaimFileDataVo>();
//			dataList.add(data);
//			transInsuranceClaimVo.setFileDatas(dataList);

			if(result>0) {
				// 新增保單醫起通申請應備文件
				if (transMedicalTreatmentClaimVo.getFileDatas() != null && transMedicalTreatmentClaimVo.getFileDatas().size() > 0) {
					for (TransMedicalTreatmentClaimFileDataVo transMedicalTreatmentClaimFileDataVo : transMedicalTreatmentClaimVo.getFileDatas()) {
						transMedicalTreatmentClaimFileDataVo.setClaimSeqId(transMedicalTreatmentClaimVo.getClaimSeqId());
						/**
						 * 通过文件上传的路径地址,获取文件转换为 Base64为的数据，
						 *   因为Base64，在页面只有一页的数据(PDF)
						 * */
						//if(StringUtils.isEmpty(transInsuranceClaimFileDataVo.getFileBase64())) {
						String filePath = transMedicalTreatmentClaimFileDataVo.getFilePath()+"/"+transMedicalTreatmentClaimFileDataVo.getFileName();
						String base64 = this.converFiestFileToBase64Str(filePath);
						// 2023.7.11 文件無法轉換為base64則使用參數中的base64
						if (StringUtils.isNotBlank(base64)) {
							transMedicalTreatmentClaimFileDataVo.setFileBase64(this.converFileToBase64Str(filePath));
						}
						logger.error("通過文件上傳的路徑地址，獲取檔案轉換為Base64為的數據:{}", transMedicalTreatmentClaimFileDataVo.getFileBase64());

						//}
						//获取文件的id编号
						Float aFloat = transMedicalTreatmentClaimDao.selectInsuranceClaimFileDataId();
						transMedicalTreatmentClaimFileDataVo.setFdId(aFloat);
						String fileBase64 = transMedicalTreatmentClaimFileDataVo.getFileBase64();
						logger.info("====保單醫起通申請-上傳文件==============獲取檔案的ID編號=================={}", aFloat);
						transMedicalTreatmentClaimFileDataVo.setFileBase64("");
						int i = transMedicalTreatmentClaimDao.addInsuranceClaimFileData(transMedicalTreatmentClaimFileDataVo);
						logger.info("=====保單醫起通申請-上傳文件=============INSERT  InsuranceClaimFileData==================   響應行數----{}", i);
						transMedicalTreatmentClaimFileDataVo.setFileBase64(fileBase64);
						if (i>0) {
							int J = transMedicalTreatmentClaimDao.updateInsuranceClaimFileDataFileBase64(transMedicalTreatmentClaimFileDataVo);
							logger.info("========保單醫起通申請-上傳文件==========UPDATE   InsuranceClaimFileData   FileBase64==================   響應行數----{}", J);
						}
					}
				}
			}
			//寫入狀態歷程
			if(result>0) {
				hisVo.setTransNum(transNum);
       			hisVo.setStatus(status);
       			if (flag) {
       				hisVo.setRejectReason(OnlineChangeUtil.NOT_UNION_CLAIMS);
       			}
				onlineChangeDao.addTransStatusHistory(hisVo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
			logger.error("Unable to init from insertTransInsuranceClaim: {}", ExceptionUtils.getStackTrace(e));
		}
		Map<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("result", result);
		rMap.put("status", status);
		return rMap;
	}

	/**
	 *
	 * @param transNum
	 * @return TransInsuranceClaimVo
	 */
	@Override
	public TransMedicalTreatmentClaimVo getTransInsuranceClaimDetail(String transNum) {
		TransMedicalTreatmentClaimVo transInsuranceClaimVo = null;
		try {
			List<TransMedicalTreatmentClaimVo> transInsuranceClaimVoList = transMedicalTreatmentClaimDao.getTransInsuranceClaimByTransNum(transNum);
			if (transInsuranceClaimVoList != null && transInsuranceClaimVoList.size() > 0) {
				transInsuranceClaimVo = transInsuranceClaimVoList.get(0);
				// 銀行名稱
				List<Map<String, Object>> bankMap = optionService.getBankList();
				for (Map<String, Object> m : bankMap) {
					if (m.containsKey("KEY") && m.get("KEY").toString().equals(transInsuranceClaimVo.getBankCode())) {
						transInsuranceClaimVo.setBankName(m.get("VALUE").toString());
						break;
					}
				}
				// 分行名稱
				List<Map<String, Object>> branchMap = optionService.getBranchesList(transInsuranceClaimVo.getBankCode());
				for (Map<String, Object> m : branchMap) {
					if (m.containsKey("KEY") && m.get("KEY").toString().equals(transInsuranceClaimVo.getBranchCode())) {
						transInsuranceClaimVo.setBranchName(m.get("VALUE").toString());
						break;
					}
				}
			}
			//格式化時間報案日期：2020/12/22 12:01
			String policeDate = transInsuranceClaimVo.getPoliceDate();
			String policeTime = transInsuranceClaimVo.getPoliceTime();
			if (!StringUtils.isEmpty(policeDate)) {
				String yyyyMMdd = DateUtil.getStringToDateString("yyyyMMdd", policeDate, "yyyy/MM/dd");
				transInsuranceClaimVo.setPoliceDate(yyyyMMdd);
			}
			if (!StringUtils.isEmpty(policeTime)) {
				String yyyyMMdd = DateUtil.getStringToDateString("HHmm", policeTime, "HH:mm");
				transInsuranceClaimVo.setPoliceTime(yyyyMMdd);
			}
			//格式化時間出生年月日： 1992/02/02
			String birdate = transInsuranceClaimVo.getBirdate();
			if (!StringUtils.isEmpty(birdate)) {
				String yyyyMMdd = DateUtil.getStringToDateString("yyyyMMdd", birdate, "yyyy/MM/dd");
				transInsuranceClaimVo.setBirdate(yyyyMMdd);
			}
			//格式化時間發生時間
			String accidentDate = transInsuranceClaimVo.getAccidentDate();
			String accidentTime = transInsuranceClaimVo.getAccidentTime();
			if (!StringUtils.isEmpty(accidentDate)) {
				String yyyyMMdd = DateUtil.getStringToDateString("yyyyMMdd", accidentDate, "yyyy/MM/dd");
				transInsuranceClaimVo.setAccidentDate(yyyyMMdd);
			}
			if (!StringUtils.isEmpty(accidentTime)) {
				String yyyyMMdd = DateUtil.getStringToDateString("HHmm", accidentTime, "HH:mm");
				transInsuranceClaimVo.setAccidentTime(yyyyMMdd);
			}
			//授權時間區間

			String authorizationStartDate = transInsuranceClaimVo.getAuthorizationStartDate();
			String authorizationEndDate = transInsuranceClaimVo.getAuthorizationEndDate();
			if (!StringUtils.isEmpty(authorizationStartDate)) {
				String yyyyMMdd = DateUtil.getStringToDateString("yyyyMMdd", authorizationStartDate, "yyyy/MM/dd");
				transInsuranceClaimVo.setAuthorizationStartDate(yyyyMMdd);
			}
			if (!StringUtils.isEmpty(authorizationEndDate)) {
				String yyyyMMdd = DateUtil.getStringToDateString("yyyyMMdd", authorizationEndDate, "yyyy/MM/dd");
				transInsuranceClaimVo.setAuthorizationEndDate(yyyyMMdd);
			}
			List<TransMedicalTreatmentClaimFileDataVo> transInsuranceClaimFileDataVoList = transMedicalTreatmentClaimDao.getFileDatasByClaimSeqId(transInsuranceClaimVo.getClaimSeqId());
		/*	for (TransInsuranceClaimFileDataVo fileDataVo : transInsuranceClaimFileDataVoList) {
				String filePath = fileDataVo.getFilePath()+File.separator+fileDataVo.getFileName();
				fileDataVo.setFileBase64(this.converFileToBase64Str(filePath));
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				//对PDF文件进行处理
				if(".pdf".equals(substring) || ".PDF".equals(substring)){
					String  letName=filePath.substring(0,filePath.lastIndexOf("."))+".png";
					fileDataVo.setFileOrPng(letName);
				}else{
					fileDataVo.setFileOrPng(filePath);
				}

			}*/
			if (transInsuranceClaimFileDataVoList != null && transInsuranceClaimFileDataVoList.size() > 0) {
				List<TransMedicalTreatmentClaimFileDataVo> transInsuranceClaimFileDataVos =
						 this.transInsuranceClaimFileDataVoListBase64ToMiniature(transInsuranceClaimFileDataVoList);
				transInsuranceClaimVo.setFileDatas(transInsuranceClaimFileDataVos);
			}
		} catch (Exception e) {
			logger.error("Unable to init from getTransInsuranceClaimDetail: {}", ExceptionUtils.getStackTrace(e));
		}
		return transInsuranceClaimVo;
	}

	/**
	 * 保單醫起通申請-上傳申請應備文件
	 *
	 * @param files
	 * @return 上傳文件結果
	 */
	public List<TransMedicalTreatmentClaimFileDataVo> upLoadFiles(MultipartFile[] files) {
		List<TransMedicalTreatmentClaimFileDataVo> fileList = new ArrayList<TransMedicalTreatmentClaimFileDataVo>();
		if (files != null) {
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
//				String filepath = System.getProperty("user.dir");
//				logger.error("filePath is: {}", filepath);
				String filepath = FILE_SAVE_PATH;
	        	File localFile = new File(filepath);
	            if (!localFile.exists()) {
	                localFile.mkdirs();
	            }
	            try {
					File server_file = new File(filepath+File.separator+fileName);
					//            	File server_file = new File(filepath+fileName);
					logger.error("------------------------------------------------------------server_fileis: {}",server_file);

					if (server_file.exists()) {
	                    SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	            		fileName = fileName.split("\\.")[0]+fmdate.format(new Date())+"."+fileName.split("\\.")[1];
	            		server_file = new File(filepath +File.separator+ fileName);
	            	}
					file.transferTo(server_file);
					TransMedicalTreatmentClaimFileDataVo fileData = new TransMedicalTreatmentClaimFileDataVo();
					fileData.setFilePath(filepath);
					File fileLength = new File(filepath +File.separator+ fileName);
					logger.error("--------------------------------------------------------------------MultipartFile is: {}", localFile.length());
					fileData.setFileName(fileName);
					String substring = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					//对PDF文档进行处理
					if(".pdf".equals(substring) || ".PDF".equals(substring)  ){
						String  letName=fileName.substring(0,fileName.lastIndexOf("."))+".png";
						fileData.setFileOrPng(letName);
					}else{
						//获取图片的地址
						fileData.setFileOrPng(fileName);
					}

					fileData.setFileBase64(this.converFileToBase64Miniature(filepath+File.separator+fileName));
					fileList.add(fileData);
					logger.error("MultipartFile is: {}", server_file);
				} catch (Exception e) {
					logger.info("文件上传异常");
				}
			}
		}
		return fileList;
	}

	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 * @return
	 */
	@Override
	public String checkBackList(BlackListVo blackListVo) {
		String detailInfo = onlineChangeDao.getMedicalBlackList(blackListVo);
		return detailInfo;
	}

	/**
	 * 檢查保單醫起通是否完成
	 */
	@Override
	public int getPolicyClaimCompleted(String rocId) {
		// TODO Auto-generated method stub
		return onlineChangeDao.getPolicyClaimCompleted(rocId);
	}

	/**
	 * 醫起通聯盟鏈險種
	 */
	@Override
	public List<String> getInsClaimPlan() {
		// TODO Auto-generated method stub
		 List<ParameterVo> params = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.MEDICAL_TREATMENT_ITEMS);
		 List<String> rlist = new ArrayList<String>();
		 for (ParameterVo vo : params) {
			 rlist.add(vo.getParameterValue());
		}
		return rlist;
	}

	/**
	 * 獲取年齡
	 */
	@Override
	public String getAgeByPolicyNo(String policyNo) {
		// TODO Auto-generated method stub
		return onlineChangeDao.getAgeByPolicyNo(policyNo);
	}


	/**
	 * 取得醫起通申請書報表 byte[].
	 *
	 * @param claimVo ClaimVo
	 * @return 回傳報表 byte[]
	 */
	@Override
	public byte[] getPDF1(TransMedicalTreatmentClaimVo claimVo) {
		RptUtils2 rptUtils = new RptUtils2();
		byte[] pdf = null;
		try {
			 claimVo.setApplicationDate(onlineChangeDao.getCreateDateByTransNum(claimVo.getTransNum()));
			 pdf = rptUtils.generatePDF(claimVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pdf;
	}
	
	/**
	 * 由Code取Value
	 * @param parameterCode
	 * @return
	 */
	@Override
	public String getParameterValueByCode( String systemId, String parameterCode) {
		
		return parameterDao.getParameterValueByCode(systemId, parameterCode);
	}

	@Override
	public String converFiestFileToBase64Str(String filePath) {
		String encodedString = null;
		try {
			if (filePath != null) {
				logger.info("--------------------------------------------------input filePath=" + filePath);
				File file = new File(filePath);
				long length = file.length();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				if (".pdf".equals(substring) || ".PDF".equals(substring)) {
					logger.info("--------------------------------------------------input filePath pdf=>Image=" + filePath);
					PDDocument doc = PDDocument.load(file);
					encodedString = this.imgBase64(doc, baos);
					logger.error("--------------------------------------------------Thumbnails  PDF=>img Base64 {}", encodedString);
					doc.close();
				} else if (length <= 51200L) {
					logger.info("--------------------------------------------------input filePath length<=51200{}" + filePath);
					encodedString = this.imgBase64(file);
					logger.error("--------------------------------------------------Thumbnails  Base64 length<=51200{}", encodedString);
				} else {
					logger.info("--------------------------------------------------input filePath length>51200{}" + filePath);
					encodedString = this.imgBase64(file, baos);
					logger.error("--------------------------------------------------Thumbnails  Base64 length>51200{}", encodedString);
				}
				logger.error("--------------------------------------------------Thumbnails  Base64 {}", encodedString);
			}
		} catch (Exception e) {
			logger.error("input filePath is null.");
			logger.error(e);
		}
		return encodedString;
	}

	public Map<String,Object> getSendMailInfo(String status) {
		String statusName = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, status);
		// TODO  需要調參數
		String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_TRANS_REMARK);
		String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM, OnlineChangeUtil.TWFHCLIFE_YQT_ADM);
		String[] mails = mailTo.split(";");
		Map<String,Object> rMap = new HashMap<String,Object>();
		List<String> receivers = new ArrayList<String>();
		if(mails.length > 0) {
			for (String mail : mails) {
				receivers.add(mail);
				logger.info("Mail Address : " + mail);
			}
		}
		
		rMap.put("statusName", statusName);
		rMap.put("transRemark", transRemark);
		rMap.put("receivers", receivers);
		return rMap;
	}
	/**
	 * Convert File(ex:jpg,pdf) to Base64
	 * @param filePath
	 * @return String
	 */
	@Override
	public String converFileToBase64Str(String filePath) {
		String encodedString = null;
		try {
			if(filePath!=null) {
				logger.info("input filePath="+filePath);

				byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			}else {
				logger.error("input filePath is null.");
			}
		}catch(Exception e) {
			logger.error(e);
		}
		return encodedString;
	}

	/**
	 * Convert File(ex:jpg,pdf) to Base64
	 * @param filePath
	 * @return String
	 */
	@Override
	public String converFileToBase64Miniature(String filePath) {
		String encodedString = null;
	/*	try {
			if(filePath!=null) {
				logger.info("input filePath="+filePath);
				
				byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			}else {
				logger.error("input filePath is null.");
			}
		}catch(Exception e) {
			logger.error(e);
		}
		*/
		
		PDDocument pdDoc = null;
		try {
			if(filePath!=null) {
				logger.info("--------------------------------------------------input filePath="+filePath);
				File file = new File(filePath);

				long length = file.length();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				//pdf获取缩略图
				if(".pdf".equals(substring) || ".PDF".equals(substring)){
					logger.info("--------------------------------------------------input filePath pdf=>Image="+filePath);
					pdDoc = PDDocument.load(file);
					encodedString = imgBase64(pdDoc,baos);
					//System.out.println(trim);
					logger.error("--------------------------------------------------Thumbnails  PDF=>img Base64 {}", encodedString);
					pdDoc.close();
				}else {
					//<=50KB 正常显示
					if (length<=51200) {
						logger.info("--------------------------------------------------input filePath length<=51200{}"+filePath);
						encodedString =imgBase64(file);
						logger.error("--------------------------------------------------Thumbnails  Base64 length<=51200{}", encodedString);
					}else{
						logger.info("--------------------------------------------------input filePath length>51200{}"+filePath);
						//进行抓取缩略图
						encodedString =imgBase64(file,baos);
						logger.error("--------------------------------------------------Thumbnails  Base64 length>51200{}", encodedString);
					}
				}
				logger.error("--------------------------------------------------Thumbnails  Base64 {}", encodedString);
			}
		}catch(Exception e) {
			logger.error("input filePath is null.");
			logger.error(e);
		}finally {
			try{
				if(pdDoc!=null) {
					pdDoc.close();
				}
			}catch(Exception e) {
				//make sure close PDDocument
			}
		}

		return encodedString;
	}

	@Override
	public List<TransMedicalTreatmentClaimFileDataVo> transInsuranceClaimFileDataVoListBase64ToMiniature(List<TransMedicalTreatmentClaimFileDataVo> list) {

		List<TransMedicalTreatmentClaimFileDataVo> collect = (List<TransMedicalTreatmentClaimFileDataVo>)list.stream().map(x -> {
			String fileBase64 = x.getFileBase64();
			String filePath = x.getFilePath() + File.separator + x.getFileName();
			String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
			if (".pdf".equals(substring) || ".PDF".equals(substring)) {
				String letName = filePath.substring(0, filePath.lastIndexOf(".")) + ".png";
				x.setFileOrPng(letName);
			} else {
				x.setFileOrPng(filePath);
			}
			if (StringUtils.isNotBlank(fileBase64)) {
				x.setOriginFileBase64(fileBase64);
				byte[] decode = Base64.getDecoder().decode(fileBase64);
				//获取文件类型
				String base64Type = this.checkBase64ImgOrPdf(decode);
				logger.info("--------------------------------------------------PDF Base64 " + base64Type);
						ByteArrayInputStream input = new ByteArrayInputStream(decode);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if (base64Type != null && !"".equals(base64Type))
					try {
						if ("png".equals(base64Type) || "jpg".equals(base64Type)) {
							String miniatureBase64 = this.imgBase64(input, baos);
							x.setFileBase64(miniatureBase64);
						} else {
							PDDocument doc = PDDocument.load(input);
							String miniatureBase64 = this.imgBase64(doc, baos);
							logger.info("--------------------------------------------------PDF Base64"+ miniatureBase64);
									x.setFileBase64(miniatureBase64);
							doc.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							baos.flush();
							baos.close();
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			} else if (StringUtils.isNotBlank(filePath)) {
				File file = new File(filePath);
				if (file.exists()) {
					String fileToBase64 = this.converFiestFileToBase64Str(filePath);
					x.setOriginFileBase64(fileToBase64);
					x.setFileBase64(fileToBase64);
				}
			}
			return x;
		}).collect(Collectors.toList());
		return collect;
	}

	/**
	 * 判断Base64数据的是什么文件类型的
	 * @param b  Base64二维数组
	 * @return
	 */
	private String checkBase64ImgOrPdf(byte[] b) {
		String type = "";
		if (35152 == ((b[0] & 0xFF) << 8 | b[1] & 0xFF)) {
			type = "png";
		} else if (65496 == ((b[0] & 0xFF) << 8 | b[1] & 0xFF)) {
			type = "jpg";
		} else {
			type = "dpf";
		}
		return type;
	}

	/**
	 * 获取小于51200的图片的base64数据
	 * @param file  小于51200的图片地址
	 * @return  base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String base64= Base64.getEncoder().encodeToString(fileContent);
		return  base64;
	}

	/**
	 * PPT转换图片50*50的base64数据
	 * @param filePath  PPT转换图片50*50地址
	 * @param baos
	 * @return  base64数据
	 * @throws IOException
	 */
	private String imgBase64(PDDocument doc, ByteArrayOutputStream baos) throws IOException {
		BufferedImage  bufferedImage = pdfBufferedImage(doc);
		Thumbnails.of(bufferedImage). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64 = Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}
	/**
	 * 获取图片50*50的的base64数据
	 * @param file  获取图片50*50地址
	 * @param baos
	 * @return   base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(File file,ByteArrayOutputStream baos) throws IOException {
		Thumbnails.of(file). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64= Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}

	/**
	 * 获取图片50*50的的base64数据
	 * @param inputStream  获取图片数据流
	 * @param baos
	 * @return
	 * @throws IOException
	 */
	private String imgBase64(InputStream inputStream, ByteArrayOutputStream baos) throws IOException {
		Thumbnails.of(new InputStream[] { inputStream }).size(50, 50).outputQuality(0.25F).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();
		String base64 = Base64.getEncoder().encodeToString(bytes);
		return base64;
	}

	/**
	 * 抓取PDF第一页，进行转换为图片
	 * @param path  pdf地址
	 * @return 转换为图片
	 */
	private BufferedImage pdfBufferedImage(PDDocument doc ) {
	//	File file = new File(path);
		try {
			//String imgPDFPath = file.getParent();
			//int dot = file.getName().lastIndexOf('.');
			//String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
			//PDDocument doc = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(doc);
			//int pageCount = doc.getNumberOfPages();
			List<BufferedImage> piclist = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				/* dpi越大转换后144越清晰，相对转换速度越慢 */
				BufferedImage image = renderer.renderImageWithDPI(i, 144);
				//ImageIO.write(image, "png", new File("));
				piclist.add(image);
			}
			BufferedImage bufferedImage = listBufferedImage(piclist);
			return  bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生产图片
	 * @param picList
	 * @return
	 */
	private  BufferedImage listBufferedImage(List<BufferedImage> picList) {
		// 纵向处理图片
		if (picList == null || picList.size() <= 0) {
			logger.error("input filePath is null.图片数组为空!");
			return null;
		}
		try {
			int height = 0,
					width = 0,
					newHeight = 0,
					newLastHeight = 0,
					picNumber = picList.size();
			int[] heightArray = new int[picNumber];
			BufferedImage newImage = null;
			List<int[]> imgRGB = new ArrayList<int[]>();
			// 保存一张图片中的RGB数据
			int[] newImgRGB;
			for (int i = 0; i < picNumber; i++) {
				newImage = picList.get(i);
				heightArray[i] = newHeight = newImage.getHeight();
				if (i == 0) {
					width = newImage.getWidth();
				}
				height += newHeight;
				newImgRGB = new int[width * newHeight];
				newImgRGB = newImage.getRGB(0, 0, width, newHeight, newImgRGB, 0, width);
				imgRGB.add(newImgRGB);
			}
			newHeight = 0;
			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < picNumber; i++) {
				newLastHeight = heightArray[i];
				// 计算偏移高度
				if (i != 0) newHeight += newLastHeight;
				// 写入流中
				imageResult.setRGB(0, newHeight, width, newLastHeight, imgRGB.get(i), 0, width);
			}
			return imageResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getProductCodeByPolicyNo(String policyNo) {
		List<String> rtn = null;
		if(policyNo!=null && !"".equals(policyNo.trim())) {
			rtn = transMedicalTreatmentClaimDao.getProductCodeByPolicyNo(policyNo);
		}
		return rtn;
	}

	@Override
	public int updateInsuranceClaimFileDataFileBase64(TransMedicalTreatmentClaimFileDataVo transMedicalTreatmentClaimFileDataVo) throws Exception {
		int i = transMedicalTreatmentClaimDao.updateInsuranceClaimFileDataFileBase64(transMedicalTreatmentClaimFileDataVo);
		return i;
	}

	@Override
	public List<Hospital> getHospitalList(String  functionName,String status) throws Exception {

		return transMedicalTreatmentClaimDao.getHospitalList(functionName,status);
	}

	@Override
	public List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(String  functionName,String status) throws Exception {
		return transMedicalTreatmentClaimDao.getHospitalInsuranceCompanyList(functionName,status);
	}

	@Override
	public Integer getMedicalTreatmentWhetherFirst(String policyNo, String medicalTreatmentParameterCode) throws Exception {
		int  i= transMedicalTreatmentClaimDao.getMedicalTreatmentWhetherFirst(policyNo,medicalTreatmentParameterCode);
		if(i>0){
		return  1;
		}
		return  0;
	}

	@Override
	public String getBirdateByPolicyNo(String policyNo) {
		return onlineChangeDao.getBirdateByPolicyNo(policyNo);
	}

	@Override
	public String getPolicyClaimCompletedPolicyno(String userRocId) throws Exception {
		return onlineChangeDao.getPolicyClaimCompletedPolicyno(userRocId);
	}

	@Override
	public List<PolicyListVo> handlPolicyClaimCompletedPolicynoNotLocked(List<PolicyListVo> handledPolicyList, String userRocId) throws Exception {
		String policyNo =  this.getPolicyClaimCompletedPolicyno(userRocId);
		//進行排除當前賬戶正在申請的保單,便於第二次申請
		if(!org.springframework.util.StringUtils.isEmpty(policyNo)){
		handledPolicyList.stream().forEach(x -> {
				String policy= x.getPolicyNo();
				if (!org.springframework.util.StringUtils.isEmpty(policy) && policyNo.equals(policy)) {
					x.setApplyLockedFlag("");
					x.setApplyLockedMsg("");
				}else{
					if(!org.springframework.util.StringUtils.isEmpty(x.getApplyLockedFlag())
							&&  !x.getApplyLockedFlag().equals("Y")
					  ){
						x.setApplyLockedFlag("Y");
						x.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_MANY_TIMES_MSG);
					}
				}
			});
		}else{
			return  handledPolicyList;
		}
		return handledPolicyList;
	}

	@Override
	public List<Hospital> gitChooseHospitalList(String policyNo, String userRocId) throws Exception {
		//當前可用的醫院
		List<Hospital> hospitalList = transMedicalTreatmentClaimDao.getHospitalList(TransTypeUtil.MEDICAL_TREATMENT_PARAMETER_CODE, StatuCode.AGREE_CODE.code);
		 //當前保單已經選擇的醫院
		List<String> strings = transMedicalTreatmentClaimDao.gitChooseHospitalList(policyNo, userRocId);
		List<Hospital> collect = hospitalList.stream().map(X -> {
				 if(!org.springframework.util.CollectionUtils.isEmpty(strings)){
					 strings.stream().forEach(j->{
						 if (!StringUtils.isEmpty(j) && j.equals(X.getHpid())) {
							X.setHpid("");
						 }
					 });
				 }
			return X;
		}).collect(Collectors.toList());

		return  collect;
	}

	@Override
	public List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo(Float claimSeqId) {
		return transMedicalTreatmentClaimDao.getMedicalInfoByClaimId(claimSeqId);
	}

    @Override
    public List<Map<String, Object>> autoCheckedCompany(String url, Map<String, String> params) throws Exception {
		OnlineChangeClient onlineChangeClient = new OnlineChangeClient();
		String resp = onlineChangeClient.postForEntity(url, params);
		logger.info("autoCheckedCompany -> resp: {}", resp);
		return new Gson().fromJson(resp, new TypeToken<List<HashMap>>() {}.getType());
    }

	@Override
	public int updateTransApplyDate(Float claimSeqId, Date date) {
		return transMedicalTreatmentClaimDao.updateTransApplyDate(claimSeqId, date);
	}

	@Override
	public List<TransMedicalTreatmentClaimVo> getUnProcessedTrans(Float claimSeqId) {
		return transMedicalTreatmentClaimDao.getUnProcessedTrans(claimSeqId, new Date());
	}

    @Override
    public int updateTransUploadDate(String transNum, Date date) {
		return transMedicalTreatmentClaimDao.updateTransUploadDate(transNum, new Date());
    }

}