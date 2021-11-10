package com.twfhclife.eservice_batch.service.onlineChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.TransMergeDao;
import com.twfhclife.eservice_batch.model.TransMergeVo;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 合併變更(一):101
 * 
 * @author all
 */
public class MergeChangeUtil {

	private static final Logger logger = LogManager.getLogger(MergeChangeUtil.class);
	private static final String UPLOAD_CODE = "101"; // 介接代碼
	private static final String SYSTEM_ID = "eservice"; // 介接代碼
	private static final String CATEGORY_CODE = "ADDRESS_TRANSLATION_RULES"; // 介接代碼

	//	001:「繳別」變更: 申請值(1),
	//	002:「年金給付」變更: 申請值(1),
	//	003:「紅利選擇權」變更: 申請值(1),
	//	004:「增值回饋金領取方式」變更: 申請值(1),
	//	005:「自動墊繳選擇權」變更: 申請值(1),
	//	006:「受益人」變更: 申請註記(1) --當為Y時，以保單號碼、生效日查詢「受益人變更資料檔」
	//	007:「展期定期」: 申請註記(1)
	//	008:「減額繳清」: 申請註記(1)
	//	009:「減少保險金額（主約）」: 申請註記(1),欲變更金額(10)
	//	009:「減少保險金額（附約）」: 申請註記(1) --當為Y時，以保單號碼、生效日查詢「減少保險金額變更資料檔」※須以商品代號判斷是否為附約
	//	010:「聯絡資料」變更: 申請註記(1), 住家電話(10),手機號碼(10),電子郵件(50),公司電話(10),公司分機(4),要保人地址(50),收費地址(50)
	//	014:「補發保單」: 申請值(1), 寄送地址(50)
	//  027:「保戶基本資料更新」：國籍代碼(2),職業代碼大(2),職業代碼中(2),職業代碼小(4)
	//  002:「年金給付」變更: 保證期間申請值(2)
	private List<String> mergeChangeCodeList = Arrays.asList("001", "002", "003", "004", "005", "006", "009", "010", "014", "027", "029", "030", "031", "033", "034");

	public List<String> getMergePolicyNoList(String applyItemText) {
		String[] lines = applyItemText.split("\r\n");
		
		// 取得在合併項目的保單號碼清單
		Set<String> policyNoSet = new LinkedHashSet<>();
		for (String line : lines) {
			if (!StringUtils.isEmpty(line) && line.length() > 25) {
				if (mergeChangeCodeList.contains(line.substring(0,  3))) {
					policyNoSet.add(line.substring(15,  25));
				}
			}
		}
		
		// 判斷出現數量1次以上才需要合併
		List<String> mergePolicyNoList = new LinkedList<>();
		for (String policyNo : policyNoSet) {
			boolean needMerge = false;
			int cnt = 0;
			
			for (String line : lines) {
				if (!StringUtils.isEmpty(line) && line.length() > 25) {
					if (mergeChangeCodeList.contains(line.substring(0,  3))) {
						if (policyNo.equals(line.substring(15,  25))) {
							cnt++;
						}
					}
					/* 受益人一律做合併 */
					if("006".equals(line.substring(0, 3)) && policyNo.equals(line.substring(15,  25))) {
						needMerge = true;
					}
				}
			}
			// 若數量有1以上就需要合併申請
			if (cnt >= 2) {
				needMerge = true;
			}
			if (needMerge) {
				mergePolicyNoList.add(policyNo);
			}
		}
		logger.info("List of policyNos for the mergeChangeCodeList: {}", mergePolicyNoList);
		return mergePolicyNoList;
	}

	/**
	 * 合併項目.
	 * 
	 * @param applyItemText
	 * @param mergePolicyNoList
	 * @param systemTwDate
	 * @return
	 */
	public String mergeApplyItem(String applyItemText, List<String> mergePolicyNoList, String systemTwDate) {
		ParameterDao paramDao = new ParameterDao();
		List<ParameterVo> paramList = paramDao.getParameterByCategoryCode(SYSTEM_ID, CATEGORY_CODE);

		// 合併後需要移除的line key
		List<String> removeLineKeyList = new ArrayList<>();
		String[] lines = applyItemText.split("\r\n");
		TransMergeDao transMergeDao = new TransMergeDao();
		
		String paymode = "";
		String annuityMethod = "";
		String guaranteePeriod = "";//年金保證期間
		String bouns = "";
		String reward = "";
		String cushion = "";
		String beneficiaryFlag = "";
		String renewFlag = "";
		String reduceFlag = "";
		String reducePolicyFlag1 = "";
		String contractAmount = "";
		String reducePolicyFlag2 = "";
		String contactInfoFlag = "";
		String contactInfo = "";
		String resendPolicyFlag = "";
		String resendPolicyAdress = "";
		String policyHolderProfile = "";
		StringBuilder conversion = null;
		StringBuilder investment = null;
		String riskLevel = "";
		String changePremium = "";
		String investPaymode = "";

		StringBuilder mergeSb = new StringBuilder();
		for (String policyNo : mergePolicyNoList) {
			String transNumMerge = transMergeDao.getTodayNextTransMergeNum();

			// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7)
			mergeSb.append(String.format(StringUtils.repeat("%s", 5), 
					UPLOAD_CODE,
					StringUtil.rpadBlank(transNumMerge, 12),
					StringUtil.rpadBlank(policyNo, 10),
					systemTwDate,
					systemTwDate
			));
			
			/**
			 * init start.
			 * 在lines for-loop前,每行重設定為初始值
			 */
			paymode = "";
			annuityMethod   = "";
			guaranteePeriod = "";
			bouns   = "";
			reward  = "";
			cushion = "";
			beneficiaryFlag = "";
			renewFlag  = "";
			reduceFlag = "";
			reducePolicyFlag1 = "";
			contractAmount    = "";
			reducePolicyFlag2 = "";
			contactInfoFlag   = "";
			contactInfo       = "";
			resendPolicyFlag    = "";
			resendPolicyAdress  = "";
			policyHolderProfile = "";
			conversion = new StringBuilder();
			investment = new StringBuilder();
			riskLevel = "";
			changePremium = "";
			investPaymode = "";

			/**
			 * init end.
			 */
			
			for (String line : lines) {

				if (!StringUtils.isEmpty(line) && line.length() > 25) {
					String transNum = line.substring(3,  15);
					String linePolicyNo = line.substring(15,  25);
					String lineKey = line.substring(0,  25);
					
					TransMergeVo transMergeVo = new TransMergeVo();
					transMergeVo.setTransNum(transNum);
					transMergeVo.setTransNumMerge(transNumMerge);
					
					//2018-10-02 生效日不為當日不可合併
//					//「繳別」變更: 申請值(1)
//					if (line.startsWith("001") && policyNo.equals(linePolicyNo) && line.length() >= 40) {
//						paymode = line.substring(39,  40);
//						insertTransMerge(transMergeVo);
//						
//						if (!removeLineKeyList.contains(lineKey)) {
//							removeLineKeyList.add(lineKey);
//						}
//					}
					//「年金給付」變更: 申請值(1)
					if (line.startsWith("002") && policyNo.equals(linePolicyNo) && line.length() >= 40) {
						annuityMethod = line.substring(39,  40);
						
						if(line.length()>=42) {//年金保證期間為二碼
							guaranteePeriod = line.substring(40,  42);
						}
						if(guaranteePeriod==null || "".equals(guaranteePeriod.trim())) {
							guaranteePeriod = "00";
						}
								
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「紅利選擇權」變更: 申請值(1)
					if (line.startsWith("003") && policyNo.equals(linePolicyNo) && line.length() >= 40) {
						bouns = line.substring(39,  40);
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「增值回饋金領取方式」變更: 申請值(1)
					if (line.startsWith("004") && policyNo.equals(linePolicyNo) && line.length() >= 40) {
						reward = line.substring(39,  40);
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「自動墊繳選擇權」變更: 申請值(1)
					if (line.startsWith("005") && policyNo.equals(linePolicyNo) && line.length() >= 40) {
						cushion = line.substring(39,  40);
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「受益人」變更: 申請註記(1) --當為Y時，以保單號碼、生效日查詢「受益人變更資料檔」
					if (line.startsWith("006") && policyNo.equals(linePolicyNo)) {
						beneficiaryFlag = "Y";
						insertTransMerge(transMergeVo);
					}
					//2018-10-02 生效日不為當日不可合併
//					//「展期定期」: 申請註記(1)
//					if (line.startsWith("007") && policyNo.equals(linePolicyNo)) {
//						renewFlag = "Y";
//						insertTransMerge(transMergeVo);
//						
//						if (!removeLineKeyList.contains(lineKey)) {
//							removeLineKeyList.add(lineKey);
//						}
//					}
					//2018-10-02 生效日不為當日不可合併
//					//「減額繳清」: 申請註記(1)
//					if (line.startsWith("008") && policyNo.equals(linePolicyNo)) {
//						reduceFlag = "Y";
//						insertTransMerge(transMergeVo);
//						
//						if (!removeLineKeyList.contains(lineKey)) {
//							removeLineKeyList.add(lineKey);
//						}
//					}
					//「減少保險金額（主約）」: 申請註記(1),欲變更金額(10)
					if (line.startsWith("009") && policyNo.equals(linePolicyNo) && line.length() >= 51) {
						reducePolicyFlag1 = "Y";
						contractAmount = line.substring(41,  51);
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「聯絡資料」變更: 申請註記(1), 住家電話(10),手機號碼(10),電子郵件(50),公司電話(10),公司分機(4),要保人地址(100),收費地址(100),收費來源(1)
					if (line.startsWith("010") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						contactInfoFlag = "Y";
						
						contactInfo = line.substring(39);
						/**
						//contactInfo = line.substring(39).replaceAll("之", "－");
						for (ParameterVo param : paramList) {
							String substring = line.substring(39);
							if (substring!=null) {
								contactInfo  = substring.replaceAll(param.getParameterCode(), param.getParameterValue());
							}
						}
						if(contactInfo.lastIndexOf("）",contactInfo.length())==-1) {
							contactInfo= TransContactInfoUtil.getReplaceUrl(contactInfo);
						}
						**/

						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「補發保單」: 申請值(1), 寄送地址(50)
					if (line.startsWith("014") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						resendPolicyFlag = "Y";
						resendPolicyAdress = line.substring(40);
						insertTransMerge(transMergeVo);
						
						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
					//「保戶基本資料更新」：國籍代碼(2),職業代碼大(2),職業代碼中(2),職業代碼小(4)
					if (line.startsWith("027") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						policyHolderProfile = line.substring(39);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}

					if (line.startsWith("029") && policyNo.equals(linePolicyNo) && line.length() > 51) {
						investment.append(line, 39, 52);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}

					if (line.startsWith("030") && policyNo.equals(linePolicyNo) && line.length() > 76) {
						conversion.append(line, 39, 77);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}

					if (line.startsWith("031") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						riskLevel = line.substring(39);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}

					if (line.startsWith("033") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						changePremium = line.substring(39);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}

					if (line.startsWith("034") && policyNo.equals(linePolicyNo) && line.length() > 39) {
						investPaymode = line.substring(39);
						insertTransMerge(transMergeVo);

						if (!removeLineKeyList.contains(lineKey)) {
							removeLineKeyList.add(lineKey);
						}
					}
				}
			}

			mergeSb.append(String.format(StringUtils.repeat("%s", 22),
					StringUtil.rpadBlank(paymode, 1),
					StringUtil.rpadBlank(annuityMethod, 1),
					StringUtil.rpadBlank(bouns, 1),
					StringUtil.rpadBlank(reward, 1),
					StringUtil.rpadBlank(cushion, 1),
					StringUtil.rpadBlank(beneficiaryFlag, 1),
					StringUtil.rpadBlank(renewFlag, 1),
					StringUtil.rpadBlank(reduceFlag, 1),
					StringUtil.rpadBlank(reducePolicyFlag1, 1),
					StringUtil.rpadBlank(contractAmount, 10),
					StringUtil.rpadBlank(reducePolicyFlag2, 1),
					StringUtil.rpadBlank(contactInfoFlag, 1),
					StringUtil.rpadBlank(contactInfo, 10 + 10 + 50 + 10 + 4 + 100 + 100 +1),
					StringUtil.rpadBlank(resendPolicyFlag, 1),
					StringUtil.rpadBlank(resendPolicyAdress, 50),
					StringUtil.rpadBlank(policyHolderProfile, 10),
					StringUtil.rpadBlank(guaranteePeriod, 2),
					StringUtil.rpadBlank(investment.toString(), 10 * 13),
					StringUtil.rpadBlank(conversion.toString(), 10 * 38),
					StringUtil.rpadBlank(riskLevel, 14),
					StringUtil.rpadBlank(changePremium, 10),
					StringUtil.rpadBlank(investPaymode, 12)
			));
			mergeSb.append("\r\n");
		}
		
		for (String removeLineKey : removeLineKeyList) {
			logger.info("List of removeLineKey for the removeLineKeyList: {}", removeLineKey);
		}
		
		StringBuilder newApplyItemDetailSb = new StringBuilder();
		for (String line : lines) {
			if (!StringUtils.isEmpty(line) && line.length() > 25) {
				String lineKey = line.substring(0,  25);
				// 若沒有在需要移除的line 就append
				if (!removeLineKeyList.contains(lineKey)) {
					newApplyItemDetailSb.append(line);
					newApplyItemDetailSb.append("\r\n");
				}
			}
		}
		
		return newApplyItemDetailSb.toString() +  mergeSb.toString();
	}
	
	private void insertTransMerge(TransMergeVo transMergeVo) {
		TransMergeDao transMergeDao = new TransMergeDao();
		TransMergeVo qryVo = transMergeDao.findById(transMergeVo);
		if (qryVo == null) {
			transMergeDao.insertTransMerge(transMergeVo);
		}
	}
}