package com.twfhclife.eservice.onlineChange.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;
import com.twfhclife.eservice.onlineChange.dao.TransCertPrintDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransExtendAttrDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;
import com.twfhclife.eservice.onlineChange.model.TransExtendAttrVo;
import com.twfhclife.eservice.onlineChange.service.ITransCertPrintService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.service.IUnicodeService;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.generic.util.RptUtils;

/**
 * 投保證明列印服務.
 * 
 * @author all
 */
@Service
public class TransCertPrintServiceImpl implements ITransCertPrintService {

	private static final Logger logger = LogManager.getLogger(TransCertPrintServiceImpl.class);
	
	private static final String ALLOW_ST = "00,01,02,03,04,05,06,07,08,09,10,11,12,13,15,16,17,18,29,30";

	@Autowired
	private TransDao transDao;

	@Autowired
	private TransPolicyDao transPolicyDao;

	@Autowired
	private TransCertPrintDao transCertPrintDao;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private IUnicodeService unicodeService;

	@Autowired
	private TransExtendAttrDao transExtendAttrDao;

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	@Override
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList) {
		if (!CollectionUtils.isEmpty(policyList)) {
			for (PolicyListVo vo : policyList) {
				if ("N".equals(vo.getExpiredFlag())) {
					String status = vo.getStatus();
					if (!StringUtils.isEmpty(status)) {
						// 2019-12-23 allow status:00~13,15,16,17,18,29,30
						if (ALLOW_ST.indexOf(status) == -1) {
							vo.setApplyLockedFlag("Y");
							if ("14".equals(status)) {
								vo.setApplyLockedMsg("此張保單不允許線上申請");
							} else {
								vo.setApplyLockedMsg(OnlineChangMsgUtil.POLICY_STATUS_NOT_ALLOW_MSG);
							}
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * 投保證明列印-查詢.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<TransCertPrintVo> getTransCertPrint(TransCertPrintVo transCertPrintVo) {
		return transCertPrintDao.getTransCertPrint(transCertPrintVo);
	}

	/**
	 * 投保證明列印-新增.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return 回傳影響筆數
	 */
	@Transactional
	@Override
	public int insertTransCertPrint(TransCertPrintVo transCertPrintVo, List<TransExtendAttrVo> transExtendAttrList) {
		String transNum = transCertPrintVo.getTransNum();
		String userId = transCertPrintVo.getUserId();
		
		int result = 0;
		try {
			// 新增線上申請主檔
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(new Date());
			transVo.setTransType(TransTypeUtil.CERT_PRINT_PARAMETER_CODE);
			transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_COMPLETE);
			transVo.setUserId(userId);
			transVo.setCreateUser(userId);
			transVo.setCreateDate(new Date());
			transDao.insertTrans(transVo);
			
			// 新增保單號碼
			for (String policyNo : transCertPrintVo.getPolicyNoList()) {
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo(policyNo);
				transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			// 新增投保證明列印
			transCertPrintDao.insertTransCertPrint(transCertPrintVo);
			
			// 新增額外欄位資料
			if (transExtendAttrList != null) {
				for (TransExtendAttrVo transExtendAttrVo : transExtendAttrList) {
					transExtendAttrVo.setTransNum(transCertPrintVo.getTransNum());
					transExtendAttrDao.insertTransExtendAttr(transExtendAttrVo);
				}
			}
			
			result = 1;
		} catch (Exception e) {
			result = 0;
			logger.error("Unable to init from insertTransCertPrint: {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return result;
	}
	
	/**
	 * 投保證明列印-明細查詢.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	@Override
	public TransCertPrintVo getTransCertPrintDetail(String transNum) {
		TransCertPrintVo qryVo = new TransCertPrintVo();
		qryVo.setTransNum(transNum);
		List<TransCertPrintVo> detailList = transCertPrintDao.getTransCertPrint(qryVo);
		
		TransCertPrintVo detailVo = new TransCertPrintVo();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		
		List<TransExtendAttrVo> transExtendAttrList = transExtendAttrDao.getTransExtendsByTransNum(transNum);
		if (transExtendAttrList != null) {
			for (TransExtendAttrVo vo : transExtendAttrList) {
				try {
					for (Field f : detailVo.getClass().getDeclaredFields()) {
						if (f.getName().equals(vo.getAttrKey())) {
							f.setAccessible(true);
							f.set(detailVo, vo.getAttrValue());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return detailVo;
	}

	@Override
	public Optional<byte[]> prepareCertPrint(TransCertPrintVo transCertPrintVo) {
		byte[] pdf = null;
		RptUtils rptUtils = null;
		try {
			final String langE = "E";
			final String langC = "C";
			if(langE.equals(transCertPrintVo.getLang())) {
				rptUtils = new RptUtils("certPrintEN.pdf");
			} else if(langC.equals(transCertPrintVo.getLang())) {
				rptUtils = new RptUtils("certPrintTW.pdf");
			}
			
			List<ParameterVo> listParm = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, "PAYMODE_ENG_MAPPING");
			Map<String, String> mapPaymodeParam = new HashMap<>();
			mapPaymodeParam.putAll(listParm.stream().collect(Collectors.toMap(ParameterVo::getParameterName, ParameterVo::getParameterValue)));
			
			// 投保證明 pdf 內容設定 
			if(transCertPrintVo.getPolicyNoList() != null) {
				String insuNo = transCertPrintVo.getPolicyNoList().get(0);
				List<Map<String, Object>> listInsuDetail = transCertPrintDao.queryInsuDetail(insuNo);
				List<Map<String, Object>> listBenefit = transCertPrintDao.queryBenefitDetail(insuNo);
				//附約
				List<Map<String, Object>> listSubPromotion = 
						listBenefit.stream()
						.filter((item) -> { return "2".equals(MyStringUtil.nullToString(item.get("PRODUCT_TYPE")));})
						.collect(Collectors.toList());
				listBenefit = listBenefit.stream()
						.filter((item) -> { return !"2".equals(MyStringUtil.nullToString(item.get("PRODUCT_TYPE")));})
						.collect(Collectors.toList());
				
				logger.debug("=========listInsuDetail=========");
				if (listInsuDetail != null) {
					for (Map map : listInsuDetail) {
						java.util.Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							logger.debug(key+"="+map.get(key));
						}
					}
				}
				
				logger.debug("=========listBenefit=========");
				if (listBenefit != null) {
					for (Map map : listBenefit) {
						java.util.Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							logger.debug(key+"="+map.get(key));
						}
					}
				}
				
				logger.debug("=========listSubPromotion=========");
				if (listSubPromotion != null) {
					for (Map map : listSubPromotion) {
						java.util.Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							logger.debug(key+"="+map.get(key));
						}
					}
				}
				
				int fontSize = 12;
				// Date 
				LocalDate localDate = LocalDate.now();
				rptUtils.txt(localDate.toString(), fontSize, 1, 492, 757);
				
				// identify 
				Map<String, Object> mapInsu = new HashMap<>();
				mapInsu.putAll(listInsuDetail.get(0));
				DecimalFormat numberFormat = new DecimalFormat("#,##0");
				
				String currency = "";
				if (langE.equals(transCertPrintVo.getLang())) {
					currency = MyStringUtil.objToStr(mapInsu.get("CURRENCY"));
				} else {
					currency = parameterService.getParameterValueByCode(ApConstants.SYSTEM_ID, MyStringUtil.objToStr(mapInsu.get("CURRENCY")));
				}
				String lipiName = MyStringUtil.objToStr(mapInsu.get("LIPI_NAME"));
				String base64LipiName = unicodeService.convertString2Unicode(lipiName);
				String lipiId = MyStringUtil.objToStr(mapInsu.get("LIPI_ID"));
				String lipiBirth = MyStringUtil.objToStr(mapInsu.get("LIPI_BIRTH"));
				String lipmInsuNo = MyStringUtil.objToStr(mapInsu.get("LIPM_INSU_NO"));
				String lipiInsuBegDate = MyStringUtil.objToStr(mapInsu.get("LIPI_INSU_BEG_DATE"));
				String strLipiTablPrem = String.format("%s %s %s", 
						MyStringUtil.objToStr(mapInsu.get("PAYMODE_CHT")),
						"： " + currency,
						MyStringUtil.objToStr(numberFormat.format(mapInsu.get("LIPI_TABL_PREM")))
						);
				String lipmName = MyStringUtil.objToStr(mapInsu.get("LIPM_NAME_1"));
				String base64LipmName = unicodeService.convertString2Unicode(lipmName);
				String productName = MyStringUtil.objToStr(mapInsu.get("PRODUCT_NAME"));
				
				String prodBaseAmt = MyStringUtil.nullToString(mapInsu.get("PROD_BASE_AMT"));
				String prodBaseType = MyStringUtil.nullToString(mapInsu.get("PROD_BASE_TYPE"));

				// 2021/3/8 追加顯示 元 或 單位
				int lipiMainAmt = Integer.parseInt(MyStringUtil.objToStr(mapInsu.get("LIPI_MAIN_AMT")));
				int lipiCancerQuan = Integer.parseInt(MyStringUtil.objToStr(mapInsu.get("LIPI_CANCER_QUAN")));
				String mainAmtShow = "";

				String mainAmt = this.amtViewProcess(MyStringUtil.objToStr(numberFormat.format(mapInsu.get("MAIN_AMT"))), prodBaseAmt, "");
//				String productId = MyStringUtil.nullToString(mapInsu.get("PRODUCT_ID"));
				String lipmSt = MyStringUtil.nullToString(mapInsu.get("LIPM_ST"));
				if(langE.equals(transCertPrintVo.getLang())) {
					// 處理需要英文的部份
//					rptUtils.txt("---------------------------------------------------------------------------------------------"
//							, 11, 1, 53, 610);
					lipiName = transCertPrintVo.getPassportName();
					lipiId = transCertPrintVo.getPassportNo();
					lipmName = transCertPrintVo.getLipmPassportName();
					strLipiTablPrem = String.format("%s %s %s", 
							MyStringUtil.nullToString(mapPaymodeParam.get(mapInsu.get("PAYMODE_CHT"))),
							"： " + currency,
							MyStringUtil.objToStr(numberFormat.format(mapInsu.get("LIPI_TABL_PREM")))
							);
					productName = MyStringUtil.objToStr(mapInsu.get("PRODUCT_NAME_ENG"));
					if(listBenefit != null && !listBenefit.isEmpty()) {
						rptUtils.txt("Name of Insurance：", fontSize, 1, 36, 580);
						rptUtils.txt(productName, fontSize, 1, 43, 562);
						
						// 2021/3/8 追加顯示 元 或 單位
						// 2021/3/24 英文版面 單位型 調整顯示 Unit(s)
						if (lipiMainAmt > 0) { 
							mainAmtShow = currency;
						} 
						else if (lipiCancerQuan > 0) {
							mainAmtShow = "Unit(s)";
						}
						
						if (!"N/A".equals(mainAmt) && !"4".equals(prodBaseType)) {//加上LIPROD.PROD_BASE_TYPE = '4' 條件,也就是遇到年金險商品就不顯示
							if (lipiMainAmt > 0) { 
								rptUtils.txt(String.format("Amount/Unit： %s %s", mainAmtShow, mainAmt), fontSize, 1, 43, 544);
							}
							else {
								rptUtils.txt(String.format("Amount/Unit： %s %s", mainAmt, mainAmtShow), fontSize, 1, 43, 544);
							}
						}
						rptUtils.txt("Coverage：", fontSize, 1, 36, 524);
						float benefitTop = 504f;
						float range = 15f;
						for(Map<String, Object> map: listBenefit) {
							String strBenefitName = MyStringUtil.objToStr(map.get("BENEFIT_DETAIL"));
//							String strBenefitId = MyStringUtil.nullToString(map.get("PRODUCT_ID"));
							if(!strBenefitName.isEmpty() && !strBenefitName.equals(productName)) {
								rptUtils.txt(strBenefitName, fontSize, 1, 43, benefitTop);
								benefitTop -= range;
							}
						}
						if(listSubPromotion != null && !listSubPromotion.isEmpty()) {
							benefitTop -= 2f;
							rptUtils.txt("Rider：", fontSize, 1, 43, benefitTop);
							rptUtils.txt("Amount / Unit", fontSize, 1, 440, benefitTop);
							benefitTop -= range;
							for(Map<String, Object> mapSubPromo: listSubPromotion) {
								String subPromoName = MyStringUtil.nullToString(mapSubPromo.get("BENEFIT_DETAIL"));
								if(!subPromoName.isEmpty()) {
									rptUtils.txt(subPromoName, fontSize, 1, 50, benefitTop);
//									benefitTop -= range;
									String subPromoAmt = mapSubPromo.get("MAIN_AMOUNT") == null ? "N/A" : numberFormat.format(mapSubPromo.get("MAIN_AMOUNT"));
									String subProdBaseAmt = MyStringUtil.nullToString(mapSubPromo.get("PROD_BASE_AMT"));
									if (!"N/A".equals(this.amtViewProcess(subPromoAmt, subProdBaseAmt, ""))) {
										rptUtils.txt(this.amtViewProcess(subPromoAmt, subProdBaseAmt, ""), fontSize, 1, 443, benefitTop);
									}
									benefitTop -= range;
								}
							}
						}
					}
				} else {
//					rptUtils.txt("---------------------------------------------------------------------------------------------"
//							, 11, 1, 53, 610);
					if(listBenefit != null && !listBenefit.isEmpty()) {
						rptUtils.txt("險種名稱：", fontSize, 1, 54, 580);
						rptUtils.txt(productName, fontSize, 1, 63, 562);
						
						// 2021/3/8 追加顯示 元 或 單位
						// 2021/3/24 中文版面 金額型 調整顯示 台幣
						if (lipiMainAmt > 0) {
							mainAmtShow = currency;
							//mainAmtShow = "元";
						}
						else if (lipiCancerQuan > 0) {
							mainAmtShow = "單位";
						}
						
						if (!"N/A".equals(mainAmt) && !"4".equals(prodBaseType)) {//加上LIPROD.PROD_BASE_TYPE = '4' 條件,也就是遇到年金險商品就不顯示
							if (lipiMainAmt > 0) { 
								rptUtils.txt(String.format("保險金額/單位數： %s %s 元", mainAmtShow, mainAmt), fontSize, 1, 63, 544);
							}
							else {
								rptUtils.txt(String.format("保險金額/單位數： %s %s", mainAmt, mainAmtShow), fontSize, 1, 63, 544);
							}
						}
						rptUtils.txt("保障內容：", fontSize, 1, 54, 524);
						float benefitTop = 504f;
						float range = 15f;
						for(Map<String, Object> map: listBenefit) {
							String strBenefitName = MyStringUtil.objToStr(map.get("BENEFIT_NAME"));
//							String strBenefitId = MyStringUtil.nullToString(map.get("PRODUCT_ID"));
							if(!strBenefitName.isEmpty() && !strBenefitName.equals(productName)) {
								rptUtils.txt(strBenefitName, fontSize, 1, 63, benefitTop);
								benefitTop -= range;
							}
						}
						if(listSubPromotion != null && !listSubPromotion.isEmpty()) {
							rptUtils.txt("附約：", fontSize, 1, 63, benefitTop);
							rptUtils.txt("保險金額/單位數", fontSize, 1, 420, benefitTop);
							benefitTop -= range;
							for(Map<String, Object> mapSubPromo: listSubPromotion) {
								String subPromo = MyStringUtil.nullToString(mapSubPromo.get("BENEFIT_NAME"));
								if(!subPromo.isEmpty()) {
									rptUtils.txt(subPromo, fontSize, 1, 75, benefitTop);
//									benefitTop -= range;
									String subPromoAmt = mapSubPromo.get("MAIN_AMOUNT") == null ? "N/A" : numberFormat.format(mapSubPromo.get("MAIN_AMOUNT"));
									String subProdBaseAmt = MyStringUtil.nullToString(mapSubPromo.get("PROD_BASE_AMT"));
									if (!"N/A".equals(this.amtViewProcess(subPromoAmt, subProdBaseAmt, ""))) {
										rptUtils.txt(this.amtViewProcess(subPromoAmt, subProdBaseAmt, ""), fontSize, 1, 423, benefitTop);
									}
									benefitTop -= range;
								}
							}
						}
					}
				}
				
				// 被保人
				rptUtils.txt(lipiName, fontSize, 1, 162, 740);
				if(base64LipiName != null && !base64LipiName.isEmpty()) {
					String unicode = unicodeService.convertString2UnicodeNonImage(lipiName);
					if(!unicode.isEmpty()) {
						rptUtils.hardName(unicode, fontSize, 1, 162, 740);
					}
				}
				// ROC id
				rptUtils.txt(lipiId, fontSize, 1, 162, 722);
				// birthday
				rptUtils.txt(lipiBirth, fontSize, 1, 162, 702);
				// 保單號碼
				rptUtils.txt(lipmInsuNo, fontSize, 1, 162, 684);
				// 保單生效日
				rptUtils.txt(lipiInsuBegDate, fontSize, 1, 162, 666);
				// 保費 = 繳別: MONEY
				rptUtils.txt(strLipiTablPrem, fontSize, 1, 162, 650);
				// 要保人
				rptUtils.txt(lipmName, fontSize, 1, 162, 632);
				if(base64LipmName != null && !base64LipmName.isEmpty()) {
					String unicode = unicodeService.convertString2UnicodeNonImage(lipmName);
					if(!unicode.isEmpty()) {
						rptUtils.hardName(unicode, fontSize, 1, 162, 632);
					}
				}

				if ("15".equals(lipmSt)) {
					if(langE.equals(transCertPrintVo.getLang())) {
						// 保單狀態       ：繳清保險
						rptUtils.txt("Condition        ：", fontSize, 1, 36, 612);
						rptUtils.txt("Paid-up Insurance", fontSize, 1, 162, 612);
					} else if(langC.equals(transCertPrintVo.getLang())) {
						// 保單狀態       ：繳清保險
						rptUtils.txt("保單狀態       ：", fontSize, 1, 54, 614);
						rptUtils.txt("繳清保險", fontSize, 1, 162, 614);
					}
				}
				
				pdf = rptUtils.toPdf();
			} else {
				
			}
		} catch(IOException | DocumentException e) {
			logger.error("Unable to load templete PDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return Optional.ofNullable(pdf);
	}
	
	/**
	 * 進行 "金額/單位" 顯示格式化，依照 product_code 決定
	 * <ul>
	 * <li>case: 1, 5, 9 -> 單位</li>
	 * <li>case: 2, 3, 4 -> 金額</li>
	 * <li>case: null    -> N/A</li>
	 * <li>default:      -> N/A</li>
	 * </ul>
	 * @param amt
	 * @param amtType
	 * @return
	 */
	private String amtViewProcess(String amt, String amtType, String currency) {
		List<String> amtInPer = Arrays.asList(new String[]{"1", "5", "9"});
		List<String> amtInMoney = Arrays.asList(new String[]{"2", "3", "4"});
//		String amtFormat = currency + " %s";
		String amtFormat = currency + "%s";
		String rtnAmt = "N/A";
		if(amtInPer.contains(amtType)) {
			// 單位顯示保單
			rtnAmt = "        " + amt;
		} else if(amtInMoney.contains(amtType)) {
			// 金額顯示保單
			rtnAmt = String.format(amtFormat, amt);
		} else {
			// 沒有保額或者不知道如何顯示
		}
		return rtnAmt;
	}
}