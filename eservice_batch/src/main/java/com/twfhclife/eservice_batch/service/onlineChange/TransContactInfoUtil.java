package com.twfhclife.eservice_batch.service.onlineChange;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.twfhclife.eservice_batch.util.CallApiMailCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoDtlDao;
import com.twfhclife.eservice_batch.dao.TransContactInfoOldDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransContactInfoDtlVo;
import com.twfhclife.eservice_batch.model.TransContactInfoOldVo;
import com.twfhclife.eservice_batch.model.TransContactInfoVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 聯絡資料變更 (DS01):010
 * 
 * @author all
 */
public class TransContactInfoUtil {

	private static final Logger logger = LogManager.getLogger(TransContactInfoUtil.class);
	private static final String TRANS_TYPE = "CONTACT_INFO";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "010"; // 介接代碼
	private static final String SYSTEM_ID = "eservice"; // 介接代碼
	private static final String CATEGORY_CODE = "ADDRESS_TRANSLATION_RULES"; // 介接代碼
	
	/**
	 * 合併申請項目.
	 * 
	 * @param txtSb StringBuilder
	 * @param systemTwDate 系統日
	 * @return 
	 */
	public List<TransVo> appendApplyItems(StringBuilder txtSb, String systemTwDate) {
		logger.info("Start running generate apply file: {}", TRANS_TYPE);
		
		TransDao transDao = new TransDao();
		ParameterDao paramDao = new ParameterDao();
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransContactInfoDao dao = new TransContactInfoDao();
		TransContactInfoDtlDao dtlDao = new TransContactInfoDtlDao();
		TransContactInfoOldDao oldDao = new TransContactInfoOldDao();
		
		List<ParameterVo> paramList = paramDao.getParameterByCategoryCode(SYSTEM_ID, CATEGORY_CODE);
		
		// 申請資料條件
		TransVo transVo = new TransVo();
		transVo.setStatus(TRANS_STATUS);
		transVo.setTransType(TRANS_TYPE);
		
		// 取得申請資料
		List<TransVo> transList = transDao.getTransList(transVo);
		if (transList != null) {
			for (TransVo trantsVo : transList) {
				String transNum = trantsVo.getTransNum();
				logger.info("TransNum: {}", transNum);
				
				// 取得新的變更資訊
				// TODO: 電子郵件(50),公司分機(4),要保人地址(50),收費地址(50)
				String email = "";
				String telHome = "";
				String telOffice = "";
				String ext = "";
				String mobile = "";
				String addressFull = "";
				String addressFullCharge = "";
				String fromCompantIdTag  = "";//收件來源-"0":為線上申請;"1":為聯盟轉收申請
				
				TransContactInfoVo qryVo = new TransContactInfoVo();
				qryVo.setTransNum(transNum);
				List<TransContactInfoVo> dataList = dao.getTransContactInfoList(qryVo);
				if (dataList != null && dataList.size() > 0) {
					logger.info("--------------------------------開始對比數據信息--------------------------------");
					BigDecimal transContactId = dataList.get(0).getId();
					
					TransContactInfoDtlVo qryDtlVo = new TransContactInfoDtlVo();
					qryDtlVo.setTransContactId(transContactId);
					List<TransContactInfoDtlVo> dataDtlList = dtlDao.getTransContactInfoDtlList(qryDtlVo);
					
					TransContactInfoOldVo qryOldVo = new TransContactInfoOldVo();
					qryOldVo.setTransContactId(transContactId);
					List<TransContactInfoOldVo> dataOldList = oldDao.getTransContactInfoOldList(qryOldVo);
					if (dataDtlList != null && dataDtlList.size() > 0 && dataOldList != null && dataOldList.size() > 0) {
						// BEGIN: update by 203990 at 20210910
						// 聯盟轉收申請 不做新舊資料比對, 直送收到的內容給核心
						TransContactInfoDtlVo dtlVo = new TransContactInfoDtlVo();
						
						if("L01".equals(dataDtlList.get(0).getFromCompanyId())) {
							fromCompantIdTag = "0";//線上申請
							dtlVo = checkChange(dataDtlList.get(0),dataOldList.get(0));
						}else {
							fromCompantIdTag = "1";//聯盟轉收申請
							dtlVo = dataDtlList.get(0);
						}
						// END: update by 203990 at 20210910

						telHome = StringUtils.trimToEmpty(dtlVo.getTelHome());
						telOffice = StringUtils.trimToEmpty(dtlVo.getTelOffice());
						if (telOffice.indexOf("#") != -1) {
							ext = telOffice.substring(telOffice.indexOf("#") + 1);
							telOffice = telOffice.substring(0, telOffice.indexOf("#"));
						}
						mobile = StringUtils.trimToEmpty(dtlVo.getMobile());
						email = StringUtils.trimToEmpty(dtlVo.getEmail()); 
						addressFull = StringUtils.trimToEmpty(dtlVo.getAddressFull());
						addressFullCharge = StringUtils.trimToEmpty(dtlVo.getAddressFullCharge());
						logger.info("--------------------------------start 替換   addressFull : {}--------------------------------", addressFull);
						logger.info("--------------------------------start 替換    addressFullCharge : {}--------------------------------", addressFullCharge);
						/**
						 *TODO  数据替换操作
						 * */
						for (ParameterVo param : paramList) {
							if(addressFull!=null){
								if(!"".equals(addressFull) && !"X".equals(addressFull)){
									addressFull = addressFull.replaceAll(param.getParameterCode(),param.getParameterValue());

								}
							}
							if(addressFullCharge!=null) {
								if (!"".equals(addressFullCharge) && !"X".equals(addressFullCharge)) {
									addressFullCharge = addressFullCharge.replaceAll(param.getParameterCode(), param.getParameterValue());
								}
							}
						}
						
						//將addressFull最後的樓或號後面以全型（）號框起來
						//remark 2021/09/07
//						if(addressFull.lastIndexOf("）",addressFull.length())==-1) {
//							addressFull = getReplaceUrl(addressFull);
//						}
//						if(addressFullCharge.lastIndexOf("）",addressFullCharge.length())==-1) {
//							addressFullCharge = getReplaceUrl(addressFullCharge);
//						}
						
					}
				}
				logger.info("TransNum's telHome : {}", telHome);
				logger.info("TransNum's telOffice : {}", telOffice);
				logger.info("TransNum's ext : {}", ext);
				logger.info("TransNum's mobile : {}", mobile);
				logger.info("TransNum's email : {}", email);
				logger.info("TransNum's addressFull : {}", addressFull);
				logger.info("TransNum's addressFullCharge : {}", addressFullCharge);
				logger.info("TransNum's fromCompantIdTag : {}", fromCompantIdTag);
				
				// 取得保單號碼
				TransPolicyVo tpQryVo = new TransPolicyVo();
				tpQryVo.setTransNum(transNum);
				List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
				if (transPolicyList != null) {
					for (TransPolicyVo tpVo : transPolicyList) {
						String policyNo = tpVo.getPolicyNo();
						logger.info("TransNum's policyNo : {}", policyNo);
						
						// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),住家電話(10),手機號碼(10),
						// 電子郵件(50),公司電話(10),公司分機(4),要保人地址(100),收費地址(100),收費來源(1)
						// 收文日(系統日yyyMMdd)
						// 生效日(系統日yyyMMdd)
						txtSb.append(String.format(StringUtils.repeat("%s", 13), 
								UPLOAD_CODE,
								StringUtil.rpadBlank(transNum, 12), 
								StringUtil.rpadBlank(policyNo, 10),
								systemTwDate,
								systemTwDate,
								StringUtil.rpadBlank(telHome, 10),
								StringUtil.rpadBlank(mobile, 10),
								StringUtil.rpadBlank(email, 50),
								StringUtil.rpadBlank(telOffice, 10),
								StringUtil.rpadBlank(ext, 4),
								StringUtil.rpadBlank(addressFull, 100),//2019/4/11 改100 by Evan
								StringUtil.rpadBlank(addressFullCharge, 100),//2019/4/11 改100 by Evan
								StringUtil.rpadBlank(fromCompantIdTag,1)//2021/07/19-保全聯盟鏈
						));
						txtSb.append("\r\n");
					}
				}
			}
			return transList;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 將addressFull最後的樓或號後面以全型（）號框起來
	 * @param addressFull
	 * @return String
	 */
	public static String getReplaceUrl(String addressFull) {
		logger.info("-------------------------------- start 替換   addressFull : {}--------------------------------", addressFull);
		if (addressFull!=null) {
			StringBuffer addressFullStringBuffer = new StringBuffer(addressFull);
			int addressFullLength = addressFullStringBuffer.length();
			int addressFullBuilding = addressFullStringBuffer.lastIndexOf("樓");
			int addressFullNumber   = addressFullStringBuffer.lastIndexOf("號");
			logger.info("------------"+addressFullLength);
			logger.info("---------------"+addressFullBuilding);
			logger.info("---------"+addressFullNumber);
			if (addressFullBuilding!=-1) {
				if (addressFullNumber!=-1) {
					//有楼有号
					//南路２段６號３樓（保全科王小華）
					if (addressFullBuilding > addressFullNumber) {
						if(addressFullBuilding<addressFullLength-1) {
							addressFullStringBuffer.insert(addressFullBuilding + 1, "（");
							addressFullStringBuffer.insert(addressFullLength + 1, "）");
							logger.info("----------有樓有號-------------" + addressFullLength);
						}
					}else{
						if(addressFullNumber<addressFullLength-1) {
							//没楼有号 如xxx樓xxx南路２段６號３
							addressFullStringBuffer.insert(addressFullNumber + 1, "（");
							addressFullStringBuffer.insert(addressFullLength + 1, "）");
							logger.info("----------没樓有號-------------" + addressFullLength);
						}
					}
				}else{
					if(addressFullBuilding<addressFullLength-1) {
						//有楼没号
						addressFullStringBuffer.insert(addressFullBuilding + 1, "（");
						addressFullStringBuffer.insert(addressFullLength + 1, "）");
						logger.info("----------有樓没號-------------" + addressFullLength);
					}
				}
			}else{
				if (addressFullNumber!=-1) {
					if(addressFullNumber<addressFullLength-1) {
						//没楼有号
						addressFullStringBuffer.insert(addressFullNumber + 1, "（");
						addressFullStringBuffer.insert(addressFullLength + 1, "）");
						logger.info("----------没樓有號-------------" + addressFullLength);
					}
				}
			}
			addressFull= addressFullStringBuffer.toString();
		}

		logger.info("--------------------------------end  替換  addressFull : {}--------------------------------", addressFull);
		return addressFull;
	}

	/**
	 * onlinechange_info 使用的文字內容
	 * @param beforeSb
	 * @param afterSb
	 * @param
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransContactInfoVo transContactInfoMaster) {
		if (transContactInfoMaster != null) {
			String contentFormat = "住家電話：%s \r\n公司電話：%s \r\n手機號碼：%s \r\n電子信箱：%s \r\n要保人地址：%s \r\n收費地址：%s";
			if (transContactInfoMaster.getTransContactInfoOldVo() != null) {
				beforeSb.append(String.format(contentFormat
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getTelHome())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getTelOffice())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getMobile())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getEmail())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getAddressFull())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoOldVo().getAddressFullCharge())
					));
			}
			if (transContactInfoMaster.getTransContactInfoDtlVo() != null) {
				afterSb.append(String.format(contentFormat
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getTelHome())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getTelOffice())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getMobile())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getEmail())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getAddressFull())
					, StringUtils.trimToEmpty(transContactInfoMaster.getTransContactInfoDtlVo().getAddressFullCharge())
					));
			}
		}
	}
	


	public TransContactInfoDtlVo checkChange(TransContactInfoDtlVo dtl, TransContactInfoOldVo old) {
		String fromCompanyId = dtl.getFromCompanyId();
		logger.info("------------------------獲取保單類型是否進行對此-----------{}-------首家案件走'X'邏輯---",fromCompanyId);
		if (fromCompanyId != null) {
			logger.info("------------------------判斷是否為首家案件---------------{}",CallApiMailCode.CONTACT_INFO_L01.equals(fromCompanyId));
			logger.info("------------------------當前為首家案件--------開始對比數據信息--------------------------------");
			dtl.setTelHome(checkChangeAttribute(fromCompanyId,dtl.getTelHome(),old.getTelHome()));
			dtl.setEmail(checkChangeAttribute(fromCompanyId,dtl.getEmail(),old.getEmail()));
			// 20220913 by 203990
			/// 手機為必填, 用做為必傳遞給核心以避免新舊比對後送全空值的問題
			//dtl.setMobile(checkChangeAttribute(fromCompanyId,dtl.getMobile(),old.getMobile()));
			dtl.setMobile(dtl.getMobile());
			dtl.setTelOffice(checkChangeAttribute(fromCompanyId,dtl.getTelOffice(),old.getTelOffice()));
			dtl.setAddressFull(checkChangeAttribute(fromCompanyId,dtl.getAddressFull(),old.getAddressFull()));
			dtl.setAddressFullCharge(checkChangeAttribute(fromCompanyId,dtl.getAddressFullCharge(),old.getAddressFullCharge()));

		}
		logger.info("============================對比數據數據之後的數據信息================={}=============",dtl);
		return dtl;
	}
	/**
	 * 送壽險核心的批次作業，從DB中取出狀態為「已接收」的申請記錄
	 * 依申請記錄中的保單號碼去取得現行保單中的聯絡資料內容，將取回的現行保單資料與線上變更申請的欲變更後資料進行比對，
	 * 如果新舊欄位值相同代表不需異動，給壽險核心檔案的對應欄位為空值，
	 * 如果新舊欄位值不相同代表需異動，給壽險核心檔案的對應欄位帶入欲變更的內容值，
	 * 如果新舊欄位值不相同，並且欲變更的內容值為空時，給壽險核心檔案的對應欄位為「X」值。
	 * @param fromCompanyid
	 * @param dtlAttributeValue
	 * @param oldAttributeValue
	 * @return String
	 */
	public String checkChangeAttribute(String fromCompanyId  ,String dtlAttributeValue, String oldAttributeValue) {
		
		String rtn = null;
		
		boolean isL01 = false;
		if("L01".equals(fromCompanyId)) {
			isL01 = true;
		}
		
		if (StringUtils.isNotBlank(dtlAttributeValue)) {//新值不為空
			if (!dtlAttributeValue.equals(oldAttributeValue)) {
				//新舊欄位值不相同
				rtn = dtlAttributeValue;
			} else {
				//新舊欄位值相同
				//給壽險核心檔案的對應欄位為空值
				rtn = "";
			}
		} else {//新值為空
			if (StringUtils.isNotBlank(oldAttributeValue)) {//舊值不為空
				if(isL01){
					//UI件時，表示清空，回傳X
					rtn = "X";
				}else {
					rtn = "";
				}
			}else {//舊值為空
				//回傳空值
				rtn = "";
			}
		}
			
		return rtn;
	}

	public static void main(String[] args) {
		TransContactInfoUtil util = new TransContactInfoUtil();
		
		String addressFull = "台北市中正區中正路5樓10號-1";
		System.out.println(util.getReplaceUrl(addressFull));
	}

}