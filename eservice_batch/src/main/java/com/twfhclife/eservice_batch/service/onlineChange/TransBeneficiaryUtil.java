package com.twfhclife.eservice_batch.service.onlineChange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.ParameterDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDao;
import com.twfhclife.eservice_batch.dao.TransBeneficiaryDtlDao;
import com.twfhclife.eservice_batch.dao.TransDao;
import com.twfhclife.eservice_batch.dao.TransPolicyDao;
import com.twfhclife.eservice_batch.model.ParameterVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryDtlVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryOldVo;
import com.twfhclife.eservice_batch.model.TransBeneficiaryVo;
import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransVo;
import com.twfhclife.eservice_batch.util.MyStringUtil;
import com.twfhclife.eservice_batch.util.StringUtil;

/**
 * 受益人 (DS05):006
 * 
 * @author all
 */
public class TransBeneficiaryUtil {

	private static final Logger logger = LogManager.getLogger(TransBeneficiaryUtil.class);
	private static final String TRANS_TYPE = "BENEFICIARY";
	private static final String TRANS_STATUS = "1";  // 1:已審核
	private static final String UPLOAD_CODE = "006"; // 介接代碼
	
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
		TransPolicyDao transPolicyDao = new TransPolicyDao();
		TransBeneficiaryDao dao = new TransBeneficiaryDao();
		TransBeneficiaryDtlDao dtlDao = new TransBeneficiaryDtlDao();
		
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
				
				TransBeneficiaryVo qryVo = new TransBeneficiaryVo();
				qryVo.setTransNum(transNum);
				List<TransBeneficiaryVo> dataList = dao.getTransBeneficiaryList(qryVo);
				if (dataList != null && dataList.size() > 0) {
					BigDecimal transBeneficiaryId = dataList.get(0).getId();
					
					// 取得新的變更資訊
					String beneficiaryType = "";
					String beneficiaryName = "";
					String beneficiaryRocid = "";
					String beneficiaryRelation = "";
					String reason = "";
					String allocateType = "";
					String allocateSeq = "";
					String beneficiaryTel = "";
					String beneficiaryMobile = "";
					String beneficiaryAddressFull = "";
					
					TransBeneficiaryDtlVo qryDtlVo = new TransBeneficiaryDtlVo();
					qryDtlVo.setTransBeneficiaryId(transBeneficiaryId);
					List<TransBeneficiaryDtlVo> dataDtlList = dtlDao.getTransBeneficiaryDtlList(qryDtlVo);
					for (TransBeneficiaryDtlVo dtlVo : dataDtlList) {
						beneficiaryType = (dtlVo.getBeneficiaryType() == null ? "" : dtlVo.getBeneficiaryType().toString());
						beneficiaryName = StringUtils.trimToEmpty(dtlVo.getBeneficiaryName());
						beneficiaryRocid = StringUtils.trimToEmpty(dtlVo.getBeneficiaryRocid());
						beneficiaryRelation = (dtlVo.getBeneficiaryRelation() == null ? "" : dtlVo.getBeneficiaryRelation().toString());
						reason = StringUtils.trimToEmpty(dtlVo.getReason());
						allocateType = (dtlVo.getAllocateType() == null ? "" : dtlVo.getAllocateType().toString());
						allocateSeq = (dtlVo.getAllocateSeq() == null ? "" : dtlVo.getAllocateSeq().toString());
						beneficiaryTel = StringUtils.trimToEmpty(dtlVo.getBeneficiaryTel());
						beneficiaryAddressFull = StringUtils.trimToEmpty(dtlVo.getBeneficiaryAddressFull());
						
						logger.info("TransNum's beneficiaryType : {}", beneficiaryType);
						logger.info("TransNum's beneficiaryName : {}", beneficiaryName);
						logger.info("TransNum's beneficiaryRocid : {}", beneficiaryRocid);
						logger.info("TransNum's beneficiaryRelation : {}", beneficiaryRelation);
						logger.info("TransNum's reason : {}", reason);
						logger.info("TransNum's allocateType : {}", allocateType);
						logger.info("TransNum's allocateSeq : {}", allocateSeq);
						logger.info("TransNum's beneficiaryTel : {}", beneficiaryTel);
						logger.info("TransNum's beneficiaryAddressFull : {}", beneficiaryAddressFull);
						
						// 取得保單號碼
						TransPolicyVo tpQryVo = new TransPolicyVo();
						tpQryVo.setTransNum(transNum);
						List<TransPolicyVo> transPolicyList = transPolicyDao.getTransPolicyList(tpQryVo);
						if (transPolicyList != null) {
							for (TransPolicyVo tpVo : transPolicyList) {
								String policyNo = tpVo.getPolicyNo();
								logger.info("TransNum's policyNo : {}", policyNo);
								
								// 介接代碼(3),申請序號(12),保單號碼(10),收文日(7),生效日(7),受益人類型(1),姓名(10),
								// 身分證字號(10),與被保人關係(1),原因(50),分配方式(1),指定順位或比例(3),
								// 受益人電話(10),受益人手機(10),受益人地址(50)
								// 收文日(系統日)
								// 生效日(系統日)
								txtSb.append(String.format(StringUtils.repeat("%s", 15), 
										UPLOAD_CODE,
										StringUtil.rpadBlank(transNum, 12), 
										StringUtil.rpadBlank(policyNo, 10),
										systemTwDate,
										systemTwDate,
										StringUtil.rpadBlank(this.convertBeneType(beneficiaryType), 1),
										StringUtil.rpadBlank(beneficiaryName, 10),
										StringUtil.rpadBlank(beneficiaryRocid, 10),
										StringUtil.rpadBlank(beneficiaryRelation, 1),
										StringUtil.rpadBlank(reason, 50),
										StringUtil.rpadBlank(allocateType, 1),
										StringUtil.rpadBlank(allocateSeq, 3),
										StringUtil.rpadBlank(beneficiaryTel, 10),
										StringUtil.rpadBlank(beneficiaryMobile, 10),
										StringUtil.rpadBlank(beneficiaryAddressFull, 50)
								));
								txtSb.append("\r\n");
							}
						}
					}
				}
			}
			return transList;
		} else {
			return Collections.emptyList();
		}
	}
	
	/**
	 * 轉換為壽險核心認的出來的代碼:身故0、滿期1、祝壽2、生存3
	 * 
	 * @param oriType
	 * @return
	 */
	private String convertBeneType(String oriType) {
		String updType = "";
		switch(oriType) {
		case "1":
			updType = "1";
			break;
		case "2":
			updType = "0";
			break;
		case "5":
			updType = "3";
			break;
		case "7":
			updType = "2";
			break;
			
		}
		logger.info("convertBeneType oriType : {}, updType : {}", oriType, updType);
		return updType;
	}
	
	/**
	 * onlinechange_info 使用的文字內容
	 * @param beforeSb
	 * @param afterSb
	 * @param transDetailList
	 */
	public void getChangeContent(StringBuilder beforeSb, StringBuilder afterSb, TransBeneficiaryVo transBeneficiaryVo) {
		if (transBeneficiaryVo != null) {
			String format = "受益人類型：%s 姓名：%s 關係：%s\r\n分配方式：%s 順位或比例：%s 電話：%s\r\n";
			ParameterDao paramDao = new ParameterDao();
			List<ParameterVo> listParam = new ArrayList<>();
			listParam.addAll(paramDao.getParameterByCategoryCode("eservice", "BENEFICIARY_TYPE"));
			Map<String, String> mapParam = new HashMap<>();
			mapParam.putAll(listParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			Map<String, String> mapAllocateParam = new HashMap<>();
			List<ParameterVo> listAllocateParam = new ArrayList<>();
			listAllocateParam.addAll(paramDao.getParameterByCategoryCode(null, "BENEFICIARY_ALLOCATE_TYPE"));
			mapAllocateParam.putAll(listAllocateParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			Map<String, String> mapRelationParam = new HashMap<>();
			List<ParameterVo> listRelationParam = new ArrayList<>();
			listRelationParam.addAll(paramDao.getParameterByCategoryCode("eservice", "BENEFICIARY_RELATION"));
			mapRelationParam.putAll(listRelationParam.stream().collect(Collectors.toMap(ParameterVo::getParameterCode, ParameterVo::getParameterValue)));
			List<TransBeneficiaryOldVo> oldList = transBeneficiaryVo.getOldList();
			if (oldList != null && oldList.size() > 0) {
				for(TransBeneficiaryOldVo oldVo : oldList) {
					beforeSb.append(String.format(format
						, MyStringUtil.nullToString(mapParam.get(oldVo.getBeneficiaryType().toString()))
						, MyStringUtil.nullToString(oldVo.getBeneficiaryName())
						, MyStringUtil.nullToString(mapRelationParam.get(oldVo.getBeneficiaryRelation()))
						, MyStringUtil.nullToString(mapAllocateParam.get(oldVo.getAllocateType() == null ? "" : oldVo.getAllocateType().toString()))
						, MyStringUtil.nullToString(oldVo.getAllocateSeq())
						, ""
						));
				}
			}
		
			List<TransBeneficiaryDtlVo> dtlList = transBeneficiaryVo.getDtlList();
			if (dtlList != null && dtlList.size() > 0) {
				for(TransBeneficiaryDtlVo vo : dtlList) {
					afterSb.append(String.format(format
						, MyStringUtil.nullToString(mapParam.get(vo.getBeneficiaryType().toString()))
						, MyStringUtil.nullToString(vo.getBeneficiaryName())
						, MyStringUtil.nullToString(mapRelationParam.get(vo.getBeneficiaryRelation()))
						, MyStringUtil.nullToString(mapAllocateParam.get(vo.getAllocateType().toString()))
						, MyStringUtil.nullToString(vo.getAllocateSeq())
						, MyStringUtil.nullToString(vo.getBeneficiaryTel())
						));
				}
			}
		}
	}
}