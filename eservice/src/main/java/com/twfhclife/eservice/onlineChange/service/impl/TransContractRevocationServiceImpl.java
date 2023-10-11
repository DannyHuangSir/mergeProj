package com.twfhclife.eservice.onlineChange.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.internal.org.apache.commons.lang3.exception.ExceptionUtils;
import com.itextpdf.text.DocumentException;
import com.twfhclife.eservice.onlineChange.dao.BankInfoDao;
import com.twfhclife.eservice.onlineChange.dao.TransContractRevocationDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.BankInfoVo;
import com.twfhclife.eservice.onlineChange.model.ContractRevocationPDFVo;
import com.twfhclife.eservice.onlineChange.model.ContractRevocationVo;
import com.twfhclife.eservice.onlineChange.model.TransContractRevocationFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransContractRevocationVo;
import com.twfhclife.eservice.onlineChange.service.ITransContractRevocationService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_client.TransCtcLibnagClient;
import com.twfhclife.generic.api_client.TransCtcLilipmClient;
import com.twfhclife.generic.api_client.TransCtcLineacClient;
import com.twfhclife.generic.api_client.TransCtcLiprpaClient;
import com.twfhclife.generic.api_model.TransCtcLibnagRequest;
import com.twfhclife.generic.api_model.TransCtcLibnagResponse;
import com.twfhclife.generic.api_model.TransCtcLibnagVo;
import com.twfhclife.generic.api_model.TransCtcLilipmRequest;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcLilipmVo;
import com.twfhclife.generic.api_model.TransCtcLineacRequest;
import com.twfhclife.generic.api_model.TransCtcLineacResponse;
import com.twfhclife.generic.api_model.TransCtcLineacVo;
import com.twfhclife.generic.api_model.TransCtcLiprpaRequest;
import com.twfhclife.generic.api_model.TransCtcLiprpaResponse;
import com.twfhclife.generic.api_model.TransCtcLiprpaVo;
import com.twfhclife.generic.api_model.TransCtcSelectDataResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataVo;
import com.twfhclife.generic.api_model.TransCtcSelectDetailResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDetailVo;
import com.twfhclife.generic.api_model.TransCtcSelectUtilRequest;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.PdfUtil;
import com.twfhclife.generic.util.RptUtils;

@Service
public class TransContractRevocationServiceImpl implements ITransContractRevocationService {

	private static final Logger logger = LogManager.getLogger(TransContractRevocationServiceImpl.class);

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransContractRevocationDao transContractRevocationDao;

	@Autowired
	private ParameterDao parameterDao;

	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;

	@Autowired
	private TransCtcLiprpaClient transLiprpaClient;

	@Autowired
	private TransCtcLineacClient transCtcLineacClient;

	@Autowired
	private TransCtcLilipmClient transCtcLilipmClient;

	@Autowired
	private IParameterService parameterService;

	@Autowired
	private TransCtcLibnagClient transCtcLibnagClient;

	@Autowired
	private MessageTemplateClient messageTemplateClient;

	@Autowired
	private BankInfoDao bankInfoDao;

	@Autowired
	private IMailService mailService;

	@Value("${eservice.contractrevocation.path}")
	private String contractrevocationPath;

	@Value("${upload.file.save.path}")
	private String FILE_SAVE_PATH;

	private final static String STRING_FORM_DATE = "yyyy/MM/dd hh:mm:ss";

	private final static String STRING_DATE = "yyyy-MM-dd hh:mm:ss";

	private static final String SYSTEM_ID = "eservice";

	@Override
	public List<ContractRevocationVo> getPolicyDetail(String lipmId, String userId, String transType) {
		List<ContractRevocationVo> contractRevocationList = new ArrayList<ContractRevocationVo>();
		// 查詢CTC獲得保單資料
		TransCtcSelectUtilRequest transCtcSelectUtilRequest = new TransCtcSelectUtilRequest();
		transCtcSelectUtilRequest.setLipmId(lipmId);
		TransCtcSelectDataResponse resp = transCtcSelectUtilClient.getTransCtcSelectDataByLipmId(transCtcSelectUtilRequest);
		List<TransCtcSelectDataVo> selectDataList = resp.getSelectDataList();
		if(selectDataList.size() > 0) {
			Map<String , TransCtcSelectDataVo> map = selectDataList.stream()
					.collect(Collectors.toMap(TransCtcSelectDataVo :: getLipmInsuNo, Function.identity() 
					, (o1 , o2) -> o1 , ConcurrentHashMap :: new));
			List<TransCtcSelectDataVo> policyList = map.values().stream().collect(Collectors.toList());
			for (TransCtcSelectDataVo vo : policyList) {
				ContractRevocationVo contractRevocationVo = new ContractRevocationVo();
				// stop1 : 參數轉換
				// LIPM_INSU_NO保號
				contractRevocationVo.setLipmInsuNo(vo.getLipmInsuNo());
				// ASSN_ARRI_DATE 簽收日
				contractRevocationVo.setAssnArriDate(getStringDate(vo.getAssnArriDate()));
				// sett_ch_name 商品名稱
				contractRevocationVo.setSettChName(vo.getSettChName());
				// LIPM_NAME 要保人
				String lipmName = vo.getLipmName1();
				contractRevocationVo.setLipmName(lipmName.replace("　　", ""));
				// LIPI_NAME 主被保人姓名
				String lipiName = vo.getLipiName();
				contractRevocationVo.setLipiName(lipiName.replace("　　", ""));
				// LIPM_INSU_BEG_DATE 保單生效日
				contractRevocationVo.setLipmInsuBegDate(getStringDate(vo.getLipmInsuBegDate()));
				// LIPI_MAIN_AMT保額
				contractRevocationVo.setLipiMainAmt(vo.getLipiMainAmt());
				// LIPI_TABL_PREM 每期保費
				contractRevocationVo.setLipiTablPrem(vo.getLipiTablPrem());
				// PROD_CURRENCY幣別
				contractRevocationVo.setProdCurrency(vo.getProdCurrency());
				// LIPM_RCP_CODE繳別
				contractRevocationVo.setLipmRcpCode(vo.getLipmRcpCode());
				contractRevocationVo.setSettTerminate(vo.getSettTerminate());
				// stop2 : 保單契約內有可撤銷條款之商品
				if (StringUtils.isNotEmpty(vo.getSettTerminate())) {
					if ("N".equals(vo.getSettTerminate())) {
						contractRevocationVo.setApplyLockedFlag("Y");
						contractRevocationVo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_CONTRACT_REVOCATION_SETT_TERMINATE);
					}
				}
				// TODO 檢核需開啟
				// stop3 : 保戶需有申請電子保單 LIPMDA.PMDA_EPO_MK=’Y’
				if (StringUtils.isNotEmpty(vo.getPmdaEpoMk())) {
					if ("N".equals(vo.getPmdaEpoMk())) {
						contractRevocationVo.setApplyLockedFlag("Y");
						contractRevocationVo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_CONTRACT_REVOCATION_PMDA_EPO_MK);
					}
				} else {
					contractRevocationVo.setApplyLockedFlag("Y");
					contractRevocationVo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_CONTRACT_REVOCATION_PMDA_EPO_MK);
				}

				if ("N".equals(contractRevocationVo.getApplyLockedFlag())) {
					// stop4 : 保單簽收日(含)起算10日內, 若保單簽收日無資料, 使用保單生效日做為條件
					String date = StringUtils.isNotEmpty(vo.getAssnArriDate()) ? vo.getAssnArriDate()
							: vo.getLipmInsuBegDate();
	 				int days = DateUtil.getDateInterval(date.substring(0, 10));
					if (days - 10 > 0) {
						contractRevocationVo.setApplyLockedFlag("Y");
						contractRevocationVo.setApplyLockedMsg(OnlineChangMsgUtil.TRANS_CONTRACT_REVOCATION_ASSN_ARRI_DATE);
					}
				}
				contractRevocationList.add(contractRevocationVo);
			}
		}
	
		// 取得目前申請中的保單號碼
		List<String> applyPolicyNos = transPolicyDao.getProgressPolicyNoList(userId, transType);
		if (!CollectionUtils.isEmpty(applyPolicyNos)) {
			for (ContractRevocationVo vo : contractRevocationList) {
				// 保單未到期但已經有在申請中
				if (applyPolicyNos.contains(vo.getLipmInsuNo())) {
						vo.setApplyLockedFlag("Y");
						// 前次申請尚在處理中，暫無法進行此項申請
						vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_HIST_TRANS_MSG);
				}
			}
		}
		return contractRevocationList;
	}

	@Override
	public ContractRevocationVo getPolicy(String lipmInsuSeqNo) {
		TransCtcSelectUtilRequest transCtcSelectUtilRequest = new TransCtcSelectUtilRequest();
		transCtcSelectUtilRequest.setLipmInsuSeqNo(lipmInsuSeqNo);
		TransCtcSelectDetailResponse resp = transCtcSelectUtilClient.getTransCtcSelectDetailByLipmInsuSeqNo(transCtcSelectUtilRequest);
		TransCtcSelectDetailVo vo = new TransCtcSelectDetailVo();
		ContractRevocationVo contractRevocationVo = new ContractRevocationVo();

		vo = resp.getSelectDetailList().get(0);
		// LIPM_INSU_NO保號
		contractRevocationVo.setLipmInsuNo(vo.getLipmInsuNo());
		// ASSN_ARRI_DATE 簽收日
		contractRevocationVo.setAssnArriDate(vo.getAssnArriDate());
		// sett_ch_name 商品名稱
		contractRevocationVo.setSettChName(vo.getSettChName());
		// LIPM_NAME 要保人
		String lipmName = vo.getLipmName1();
		contractRevocationVo.setLipmName(lipmName.replace("　　", ""));
		// LIPI_NAME 主被保人姓名
		String lipiName = vo.getLipiName();
		contractRevocationVo.setLipiName(lipiName.replace("　　", ""));
		// LIPM_INSU_BEG_DATE 保單生效日
		contractRevocationVo.setLipmInsuBegDate(getStringDate(vo.getLipmInsuBegDate()));
		// LIPI_MAIN_AMT保額
		contractRevocationVo.setLipiMainAmt(vo.getLipiMainAmt());
		// LIPI_TABL_PREM 每期保費
		contractRevocationVo.setLipiTablPrem(vo.getLipiTablPrem());
		// PROD_CURRENCY幣別
		contractRevocationVo.setProdCurrency(vo.getProdCurrency());
		// LIPM_RCP_CODE繳別
		contractRevocationVo.setLipmRcpCode(vo.getLipmRcpCode());

		return contractRevocationVo;
	}

	@Override
	public ContractRevocationVo getRevokeByLiprpaForInsuSeqNo(ContractRevocationVo contractRevocationVo) {

		TransCtcLiprpaRequest req = new TransCtcLiprpaRequest();
		req.setProdNo(contractRevocationVo.getLipmInsuNo().substring(0, 2));
		req.setPrpaInsuSeqNo(contractRevocationVo.getLipmInsuNo());
		TransCtcLiprpaResponse reqp = transLiprpaClient.getRevokeByLiprpaForInsuSeqNo(req);

		if (reqp.getLiprpaList().size() != 0) {
			TransCtcLiprpaVo vos = new TransCtcLiprpaVo();
			vos = reqp.getLiprpaList().get(0);
			contractRevocationVo.setPrpaRcpBatch(vos.getPrpaRcpBatch());
			contractRevocationVo.setPrpaActAmt(vos.getPrpaActAmt());
			contractRevocationVo.setPrpkRcpTypeCode(vos.getPrpkRcpTypeCode());
		}
		return contractRevocationVo;
	}

	@Override
	public ContractRevocationVo getRevokeByLineacForNeacInsuNo(ContractRevocationVo contractRevocationVo) {
		TransCtcLineacRequest apiReq = new TransCtcLineacRequest();
		// TODO 檢核需開啟
		apiReq.setInsuNo(contractRevocationVo.getLipmInsuNo());
		TransCtcLineacResponse transCtcLineacResponse = transCtcLineacClient.getRevokeByLineacForNeacInsuNo(apiReq);

		if (transCtcLineacResponse.getLineacList().size() != 0) {
			TransCtcLineacVo lineacVo = new TransCtcLineacVo();
			lineacVo = transCtcLineacResponse.getLineacList().get(0);

			BankInfoVo qryBankInfoVo = new BankInfoVo();
			qryBankInfoVo.setBankId(lineacVo.getNeacBankCode());
			qryBankInfoVo.setBranchId(lineacVo.getNeacBranchCode());
			List<BankInfoVo> bankInfoList = bankInfoDao.getBankInfo(qryBankInfoVo);
			if(bankInfoList.size() != 0) {
				BankInfoVo bankInfoVo = bankInfoList.get(0);
				contractRevocationVo.setOldNeacName(lineacVo.getNeacName());
				contractRevocationVo.setOldBankCode(lineacVo.getNeacBankCode());
				contractRevocationVo.setOldBankName(bankInfoVo.getBankName());
				contractRevocationVo.setOldBranchCode(lineacVo.getNeacBranchCode());
				contractRevocationVo.setOldBranchName(bankInfoVo.getBranchName());
				if (!"NTD".equals(contractRevocationVo.getProdCurrency())) {
					contractRevocationVo.setOldSwiftCode(lineacVo.getNeacAchBic());
					contractRevocationVo.setOldEnglishName(lineacVo.getNeacEnName());
				}
				contractRevocationVo.setOldAccount(lineacVo.getNeacCharAt());
			}
		}
		return contractRevocationVo;
	}

	@Override
	public int insertContractRevocation(String transNum, ContractRevocationVo contractRevocationVo, UsersVo userVo,
			Map<String, ParameterVo> parameterList) {
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CONTRACT_REVOCATION);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_RECEIVED);
			transVo.setUserId(userVo.getUserId());
			transVo.setCreateUser(userVo.getUserId());
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);

			// 新增申請的保險單號
			TransPolicyVo transPolicyVo = new TransPolicyVo();
			transPolicyVo.setTransNum(transNum);
			transPolicyVo.setPolicyNo(contractRevocationVo.getLipmInsuNo());
			transPolicyDao.insertTransPolicy(transPolicyVo);
			// 新增要申請的電子表單狀態
			TransContractRevocationVo transContractRevocationVo = new TransContractRevocationVo();
			StringBuilder oldDetails = new StringBuilder();
			transContractRevocationVo.setLipmInsuNo(contractRevocationVo.getLipmInsuNo());
			transContractRevocationVo.setSettChName(contractRevocationVo.getSettChName());
			transContractRevocationVo.setLipmName(contractRevocationVo.getLipmName());
			transContractRevocationVo.setLipiName(contractRevocationVo.getLipiName());
			transContractRevocationVo.setProdCurrency(contractRevocationVo.getProdCurrency());
			transContractRevocationVo.setLipmInsuBegDate(contractRevocationVo.getLipmInsuBegDate());
			transContractRevocationVo.setPrpaActAmt(contractRevocationVo.getPrpaActAmt());
			transContractRevocationVo.setSwiftCode(contractRevocationVo.getSwiftCode());
			transContractRevocationVo.setEnglishName(contractRevocationVo.getEnglishName());
			transContractRevocationVo.setNeacName(contractRevocationVo.getNeacName());
			transContractRevocationVo.setBankCode(contractRevocationVo.getBankCode());
			transContractRevocationVo.setBankName(contractRevocationVo.getBankName());
			transContractRevocationVo.setBranchCode(contractRevocationVo.getBranchCode());
			transContractRevocationVo.setBranchName(contractRevocationVo.getBranchName());
			transContractRevocationVo.setAccount(contractRevocationVo.getAccount());
			transContractRevocationVo.setRcpTypeCodeFlag(contractRevocationVo.getRcpTypeCodeFlag());
			transContractRevocationVo.setNeedsFlag(contractRevocationVo.getNeedsFlag());
			transContractRevocationVo.setEconomyFlag(contractRevocationVo.getEconomyFlag());
			transContractRevocationVo.setFamilyFlag(contractRevocationVo.getFamilyFlag());
			transContractRevocationVo.setCognitionFlag(contractRevocationVo.getCognitionFlag());
			transContractRevocationVo.setOtherFlag(contractRevocationVo.getOtherFlag());
			transContractRevocationVo.setTransNum(transNum);
			transContractRevocationVo.setImage1(contractRevocationVo.getImage1());
			transContractRevocationVo.setImage2(contractRevocationVo.getImage2());

			if ("A".equals(contractRevocationVo.getRcpTypeCodeFlag())
					&& StringUtils.isNotEmpty(contractRevocationVo.getOldBankCode())) {
				String oldSwiftCode = StringUtils.isNotEmpty(contractRevocationVo.getOldSwiftCode())
						? contractRevocationVo.getOldSwiftCode()
						: "null";
				String oldEnglishName = StringUtils.isNotEmpty(contractRevocationVo.getOldEnglishName())
						? contractRevocationVo.getOldEnglishName()
						: "null";
				String oldNeacName = StringUtils.isNotEmpty(contractRevocationVo.getOldNeacName())
						? contractRevocationVo.getOldNeacName()
						: "null";
				String oldBankName = StringUtils.isNotEmpty(contractRevocationVo.getOldBankName())
						? contractRevocationVo.getOldBankName()
						: "null";
				String oldBranchName = StringUtils.isNotEmpty(contractRevocationVo.getOldBranchName())
						? contractRevocationVo.getOldBranchName()
						: "null";
				String oldAccount = StringUtils.isNotEmpty(contractRevocationVo.getOldAccount())
						? contractRevocationVo.getOldAccount()
						: "null";

				oldDetails.append(oldSwiftCode + ";");
				oldDetails.append(oldEnglishName + ";");
				oldDetails.append(oldNeacName + ";");
				oldDetails.append(oldBankName + ";");
				oldDetails.append(oldBranchName + ";");
				oldDetails.append(oldAccount);

				transContractRevocationVo.setOldDetails(oldDetails.toString());
			}

			transContractRevocationVo.setOtherReason(contractRevocationVo.getOtherReason());
			transContractRevocationVo.setEmail(userVo.getEmail());
			transContractRevocationVo.setLipmId(contractRevocationVo.getLipmId());
			transContractRevocationDao.insertTransContractRevocation(transContractRevocationVo);
			// 製作PDF相關資料
			contractRevocationVo.setTransNum(transNum);
			ContractRevocationPDFVo pdfVo = getPDFDetail(contractRevocationVo, parameterList);
			contractRevocationVo.setTransNum(transNum);
			sendNotification(contractRevocationVo, userVo, transContractRevocationVo.getLipmInsuNo());
			pdfVo.setUserMail(userVo.getEmail());
			pdfVo.setTransNum(transNum);
			sendMailToSalesman(pdfVo);
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransContractRevocation:: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}

	@Override
	public ContractRevocationVo getTransContractRevocationDetail(String transNum) {
		ContractRevocationVo contractRevocationVo = new ContractRevocationVo();
		TransContractRevocationVo qryVo = new TransContractRevocationVo();
		qryVo.setTransNum(transNum);
		List<TransContractRevocationVo> detailList = transContractRevocationDao.getTransContractRevocation(qryVo);

		TransContractRevocationVo detailVo = new TransContractRevocationVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}

		List<String> lipmInsuNo = transPolicyDao.getTransPolicyNoList(detailVo.getTransNum());
		contractRevocationVo.setLipmInsuNo(lipmInsuNo.get(0));
		contractRevocationVo.setSettChName(detailVo.getSettChName());
		contractRevocationVo.setLipmName(detailVo.getLipmName());
		contractRevocationVo.setLipiName(detailVo.getLipiName());
		contractRevocationVo.setProdCurrency(detailVo.getProdCurrency());
		contractRevocationVo.setLipmInsuBegDate(detailVo.getLipmInsuBegDate());
		contractRevocationVo.setPrpaActAmt(detailVo.getPrpaActAmt());
		contractRevocationVo.setSwiftCode(detailVo.getSwiftCode());
		contractRevocationVo.setEnglishName(detailVo.getEnglishName());
		contractRevocationVo.setNeacName(detailVo.getNeacName());
		contractRevocationVo.setAccount(detailVo.getAccount());
		contractRevocationVo.setBankName(detailVo.getBankName());
		contractRevocationVo.setBranchName(detailVo.getBranchName());
		contractRevocationVo.setRcpTypeCodeFlag(detailVo.getRcpTypeCodeFlag());
		StringBuffer flagMsg = new StringBuffer();
		if (detailVo.getNeedsFlag().equals("1")) {
			flagMsg.append("保單規劃不符需求、");
		}
		if (detailVo.getEconomyFlag().equals("1")) {
			flagMsg.append("經濟因素、");
		}
		if (detailVo.getFamilyFlag().equals("1")) {
			flagMsg.append("家人反對、");
		}
		if (detailVo.getCognitionFlag().equals("1")) {
			flagMsg.append("對商品認知有誤、");
		}
		if (detailVo.getOtherFlag().equals("1")) {
			flagMsg.append("其他、");
		}

		String msg = flagMsg.substring(0, flagMsg.length() - 1);
		contractRevocationVo.setStatusFlag(msg);
		contractRevocationVo.setImage1(detailVo.getImage1());
		contractRevocationVo.setImage2(detailVo.getImage2());
		contractRevocationVo.setOtherReason(detailVo.getOtherReason());
		return contractRevocationVo;
	}

	/**
	 * 保單理賠申請-上傳申請應備文件
	 * 
	 * @param files
	 * @return 上傳文件結果
	 */
	public TransContractRevocationFileDataVo upLoadFiles(MultipartFile[] files) {
		TransContractRevocationFileDataVo dataVo = new TransContractRevocationFileDataVo();
		if (files != null) {
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				String filepath = contractrevocationPath;
				File localFile = new File(filepath);
				if (!localFile.exists()) {
					localFile.mkdirs();
				}
				try {
					File server_file = new File(filepath + File.separator + fileName);
					// File server_file = new File(filepath+fileName);
					logger.error("------------------------------------------------------------server_fileis: {}",
							server_file);

					if (server_file.exists()) {
						SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						fileName = fileName.split("\\.")[0] + fmdate.format(new Date()) + "."
								+ fileName.split("\\.")[1];
						server_file = new File(filepath + File.separator + fileName);
					}
					file.transferTo(server_file);

					dataVo.setFilePath(filepath);
//					File fileLength = new File(filepath +File.separator+ fileName);
					logger.error(
							"--------------------------------------------------------------------MultipartFile is: {}",
							localFile.length());
					dataVo.setFileName(fileName);
					String substring = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					// 对PDF文档进行处理
					// 获取图片的地址
					dataVo.setFileOrPng(fileName);
//					}
					dataVo.setFileBase64(this.converFileToBase64Miniature(filepath + File.separator + fileName));
					logger.error("MultipartFile is: {}", server_file);
				} catch (Exception e) {
					logger.info("文件上传异常");
				}
			}
		}
		return dataVo;
	}

	public byte[] getPDF(ContractRevocationPDFVo vo) {
		try {
			byte[] appendPdfByte = null;
			RptUtils rptUtils = new RptUtils("contractRevocation.pdf");
			rptUtils.txt(vo.getLipmName(), 12, 1, 138, 695); // 要保人
			rptUtils.txt(vo.getLipiName(), 12, 1, 347, 695); // 被保人
			rptUtils.txt(vo.getLipmInsuNo(), 12, 1, 138, 673); // 保單號碼
			rptUtils.txt(vo.getNoteDate(), 12, 1, 347, 673); //  照會日期
			rptUtils.txt(vo.getSettChName(), 12, 1, 138, 651); // 主約險種
			rptUtils.txt(vo.getName(), 12, 1, 138, 628); // 業務員
			rptUtils.txt(vo.getAginInveArea(), 12, 1, 347, 628); // 經攬單位
			LocalDate localdate = LocalDate.now();
			LocalDate newDate = localdate.plusDays(10);
			rptUtils.txt(String.valueOf((newDate.getYear() - 1911)), 12, 1, 235, 605); // 年
			rptUtils.txt(String.valueOf(newDate.getMonthValue()), 12, 1, 285, 605); // 月
			rptUtils.txt(String.valueOf(newDate.getDayOfMonth()), 12, 1, 328, 605); // 日
			byte[] pdfByte = rptUtils.toPdf();
			if (appendPdfByte == null) {
				appendPdfByte = pdfByte;
			} else {
				appendPdfByte = PdfUtil.appendPdf(appendPdfByte, pdfByte);
			}
//			FileOutputStream fos = new FileOutputStream(new File("D:\\ELIFE\\123.pdf"));
//			IOUtils.write(pdfByte, fos);
//			IOUtils.closeQuietly(fos);
			return pdfByte;
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStringDate(String date) {
		try {
			if (StringUtils.isNotEmpty(date)) {
				SimpleDateFormat formater = new SimpleDateFormat(STRING_FORM_DATE);
				return DateUtil.getRocDate(formater.parse(date));
			}
		} catch (ParseException e) {
			SimpleDateFormat formater = new SimpleDateFormat(STRING_DATE);
			try {
				return DateUtil.getRocDate(formater.parse(date));
			} catch (ParseException e1) {
				return null;
			}
		}
		return null;
	}

	public void sendNotification(ContractRevocationVo contractRevocationVo, UsersVo user, String policyNo) {
		try {
			Map<String, Object> mailInfo = getSendMailInfo();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransNum", contractRevocationVo.getTransNum());
			paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
			paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
			logger.info("Trans Num : {}", contractRevocationVo.getTransNum());
			logger.info("Status Name : {}", mailInfo.get("statusName"));
			logger.info("Trans Remark : {}", mailInfo.get("transRemark"));
			logger.info("receivers={}", mailInfo.get("receivers"));
			logger.info("user phone : {}", user.getMobile());
			logger.info("user mail : {}", user.getEmail());
			// 获取保单编号
			paramMap.put("POLICY_NO", policyNo);
			logger.info("POLICY_NO : {}", policyNo);

			List<String> receivers = new ArrayList<String>();

			String applyDate = DateUtil.formatDateTime(new Date(), "yyyy年MM月dd日 HH時mm分ss秒");
			paramMap.put("DATA", applyDate);
			// 申請狀態-申請中
			paramMap.put("TransStatus", "已審核");
			// 申請功能
			ParameterVo parameterValueByCode = new ParameterVo();
			parameterValueByCode = parameterService.getParameterByParameterValue(ApConstants.SYSTEM_ID,
					OnlineChangeUtil.ONLINE_CHANGE_PARAMETER_CATEGORY_CODE, TransTypeUtil.CONTRACT_REVOCATION);
			paramMap.put("APPLICATION_FUNCTION", parameterValueByCode.getParameterName());

			// 發送系統管理員
			
			receivers = (List) mailInfo.get("receivers");
			messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_038, receivers, paramMap,
					"email");

			// 發送保戶SMS
			receivers.clear();// 清空
			paramMap.clear();// 清空模板參數
			// 設置模板參數
			receivers.add(user.getMobile());
			logger.info("user phone : {}", user.getMobile());
			messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_037, receivers, paramMap, "sms");
			// 發送保戶MAIL
			// TODO 檢核需開啟
			if (user.getEmail() != null) {
				receivers.clear();// 清空
				receivers.add(user.getEmail());
				logger.info("user mail : {}", user.getEmail());

				messageTemplateClient.sendNoticeViaMsgTemplate(OnlineChangeUtil.ELIFE_MAIL_037, receivers, paramMap,
						"email");
			}
		} catch (Exception e) {
			logger.info("insertTransContractRevocation() success, but send notify mail/sms error.");
		}
		logger.info("End send mail");
	}

	public Map<String, Object> getSendMailInfo() {
		String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER,
				ApConstants.INVESTMENT_TRANS_REMARK);
		String mailTo = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_ADM,
				OnlineChangeUtil.TWFHCLIFE_ELECTRONIC_ADM);
		String[] mails = mailTo.split(";");
		Map<String, Object> rMap = new HashMap<String, Object>();
		List<String> receivers = new ArrayList<String>();
		if (mails.length > 0) {
			for (String mail : mails) {
				receivers.add(mail);
				logger.info("Mail Address : " + mail);
			}
		}
		rMap.put("transRemark", transRemark);
		rMap.put("receivers", receivers);
		return rMap;
	}

	public ContractRevocationPDFVo getPDFDetail(ContractRevocationVo contractRevocationVo,
			Map<String, ParameterVo> parameterList) {
		ContractRevocationPDFVo pdfVo = new ContractRevocationPDFVo();
		// 取得受理單位代碼
		TransCtcLilipmRequest pmReq = new TransCtcLilipmRequest();
		pmReq.setLipmInsuSeqNo(contractRevocationVo.getLipmInsuNo());
		TransCtcLilipmResponse pmResp = transCtcLilipmClient.getRevokeByLilipmForLipmInsuSeqNo(pmReq);
		List<TransCtcLilipmVo> transCtcLilipmVoList = pmResp.getCtcLilipmListVo();
		contractRevocationVo.setAccUnit(transCtcLilipmVoList.get(0).getLipmAccUnit());
		// 取得受理單位資訊
		ParameterVo parameterVo = new ParameterVo();
		if (StringUtils.isNotEmpty(contractRevocationVo.getAccUnit())) {
			parameterVo = parameterList.get(contractRevocationVo.getAccUnit());
			if (parameterVo == null) {
				parameterVo = parameterList.get("01");
			}
		} else {
			parameterVo = parameterList.get("01");
		}
		TransCtcLibnagRequest bnagReq = new TransCtcLibnagRequest();
		bnagReq.setBnagInsuSeqNo(contractRevocationVo.getLipmInsuNo());
		TransCtcLibnagResponse bangResp = transCtcLibnagClient.getRevokeByLibnagForBnagInsuSeqNo(bnagReq);
		TransCtcLibnagVo bnagVo = new TransCtcLibnagVo();
		if (bangResp.getCtcLibnagListVo().size() != 0) {
			bnagVo = bangResp.getCtcLibnagListVo().get(0);
		}

		TransCtcLilipmResponse pmResps = transCtcLilipmClient.getRevokeByLilipmForSeqNoAndAginRecCode(pmReq);
		TransCtcLilipmVo vo = new TransCtcLilipmVo();
		if (pmResps.getCtcLilipmListVo().size() != 0) {
			vo = pmResps.getCtcLilipmListVo().get(0);
		}

		// 服務員Mail
		pdfVo.setEmail(parameterVo.getParameterValue());
		pdfVo.setLipmName(contractRevocationVo.getLipmName()); // 要保人
		pdfVo.setLipiName(contractRevocationVo.getLipiName()); // 被保人
		pdfVo.setLipmInsuNo(contractRevocationVo.getLipmInsuNo()); // 保單號碼
		String date = DateUtil.formatDateTime(new Date(), "yyyy/MM/dd");
		date = DateUtil.westToTwDate(date);
		pdfVo.setNoteDate(date); // 照會時間
		if (bnagVo != null) {
			pdfVo.setName(StringUtils.isNotEmpty(bnagVo.getBnagRecClerk()) ? bnagVo.getBnagRecClerk() : ""); // 業務員姓名
		}
		if (vo != null) {
			pdfVo.setAginInveArea(StringUtils.isNotEmpty(vo.getAginInveArea()) ? vo.getAginInveArea() : ""); // 經辦單位
		}
		pdfVo.setSettChName(contractRevocationVo.getSettChName());

		TransContractRevocationVo transContractRevocationVo = new TransContractRevocationVo();
		transContractRevocationVo.setTransNum(contractRevocationVo.getTransNum());
		List<TransContractRevocationVo> contractRevocationList = transContractRevocationDao
				.getTransContractRevocation(transContractRevocationVo);
		transContractRevocationVo = contractRevocationList.get(0);
		if (vo != null) {
			transContractRevocationVo.setAginInveArea(vo.getAginInveArea());
		}
		try {
			transContractRevocationDao.updateAginInveAreaforId(transContractRevocationVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfVo;
	}

	public void sendMailToSalesman(ContractRevocationPDFVo contractRevocationPdfVo) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(
					"<!DOCTYPE html><html lang=\"zh-Hant\"><head><meta charset=\"utf-8\"/></head><body>CONTENT</body></html>");
			String content = sb.toString();
			content = content.replace("CONTENT", "契約撤銷申請照會單");
			String subject = parameterService.getParameterValueByCode(SYSTEM_ID, "CONTRACT_REVOCATION_SUBJECT");
			String transNum = contractRevocationPdfVo.getTransNum();
			subject = subject.replace("TRANS_NUM", contractRevocationPdfVo.getLipmInsuNo());
			String filePath = contractrevocationPath +  contractRevocationPdfVo.getTransNum() +  File.separator + transNum + ".pdf";
			File files = new  File(filePath);
			if (!files.exists()) {
				files.mkdir();
			}		
			java.io.File sendFile = new java.io.File(filePath);
			FileUtils.writeByteArrayToFile(sendFile, getPDF(contractRevocationPdfVo));
			List<java.io.File> listFile = new ArrayList<>();
			listFile.add(sendFile);
			StringBuilder ccMailUser = new StringBuilder();
			String ccUser =  parameterService.getParameterValueByCode(SYSTEM_ID, "CONTRACT_REVOCATION_ATTACHMENT_USER");
			String [] ccMail = ccUser.split(",");
			for(String str : ccMail) {
				if(!str.equals(contractRevocationPdfVo.getEmail())) {
					ccMailUser.append(str).append(";");
				}
			}
			String mailCc = ccMailUser.toString();
			mailCc = mailCc.substring(0 , mailCc.length()-1);
			mailService.sendMail(content, subject, contractRevocationPdfVo.getEmail(), mailCc, listFile);
		} catch (Exception e) {
			logger.error("Send Mail To Salesman Error : {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Convert File(ex:jpg,pdf) to Base64
	 * 
	 * @param filePath
	 * @return String
	 */
	public String converFileToBase64Miniature(String filePath) {
		String encodedString = null;

		PDDocument pdDoc = null;
		try {
			if (filePath != null) {
				logger.info("--------------------------------------------------input filePath=" + filePath);
				File file = new File(filePath);

				long length = file.length();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();// io流
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				// pdf获取缩略图
				encodedString = imgBase64(file);
				logger.error("--------------------------------------------------Thumbnails  Base64 {}", encodedString);
			}
		} catch (Exception e) {
			logger.error("input filePath is null.");
			logger.error(e);
		} finally {
			try {
				if (pdDoc != null) {
					pdDoc.close();
				}
			} catch (Exception e) {
				// make sure close PDDocument
			}
		}
		return encodedString;
	}

	/**
	 * 获取小于51200的图片的base64数据
	 * 
	 * @param file 小于51200的图片地址
	 * @return base64数据
	 * @throws IOException
	 */
	private String imgBase64(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String base64 = Base64.getEncoder().encodeToString(fileContent);
		return base64;
	}

	/**
	 * 生产图片
	 * 
	 * @param piclist
	 * @return
	 */
	private BufferedImage listBufferedImage(List<BufferedImage> picList) {
		// 纵向处理图片
		if (picList == null || picList.size() <= 0) {
			logger.error("input filePath is null.图片数组为空!");
			return null;
		}
		try {
			int height = 0, width = 0, newHeight = 0, newLastHeight = 0, picNumber = picList.size();
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
				if (i != 0)
					newHeight += newLastHeight;
				// 写入流中
				imageResult.setRGB(0, newHeight, width, newLastHeight, imgRGB.get(i), 0, width);
			}
			return imageResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
