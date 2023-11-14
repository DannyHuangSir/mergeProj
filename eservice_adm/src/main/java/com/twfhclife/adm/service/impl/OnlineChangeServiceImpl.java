package com.twfhclife.adm.service.impl;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.twfhclife.adm.model.*;
import com.twfhclife.generic.util.DateFormatUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.twfhclife.adm.dao.OnlineChangeDao;
import com.twfhclife.adm.dao.ParameterDao;
import com.twfhclife.adm.dao.UnionCourseDao;
import com.twfhclife.adm.service.IOnlineChangeService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.api_model.MessageTriggerRequestVo;
import com.twfhclife.generic.service.IMailService;
import com.twfhclife.generic.util.ApConstants;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;


/**
 * 取得線上申請查詢結果.
 * 
 * @param vo TransVo
 * @return 回傳線上申請查詢結果
 */
@Service
public class OnlineChangeServiceImpl implements IOnlineChangeService {
	
	private static final Logger logger = LogManager.getLogger(OnlineChangeServiceImpl.class);
	
	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	private UnionCourseDao unionCourseDao;

	@Autowired
	private IMailService mailService;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private MessageTemplateClient messageTemplateClient;
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 取得線上申請查詢結果.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	@RequestLog
	public List<Map<String, Object>> getOnlineChangeDetail(TransVo transVo, boolean isPageable){
		List<Map<String, Object>> dataPageList = null;
		if(isPageable){
			if(ApConstants.TRANS_TYPE_CONTACT_INFO.equals(transVo.getTransType()) ) {//變更保單聯絡資料
				dataPageList = onlineChangeDao.getOnlineChangeCIDetail(transVo);
			}else if(ApConstants.TRANS_TYPE_DNS_ALLIANCE.equals(transVo.getTransType())) {
				dataPageList = onlineChangeDao.getOnlineChangeDnsDetail(transVo);
				return dataPageList;
			}else if(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE.equals(transVo.getTransType())) {
				dataPageList = onlineChangeDao.getOnlineChangeMedicalTreatmentDetail(transVo);
			}else {
				dataPageList = onlineChangeDao.getOnlineChangeDetailForPageable(transVo);
				if (!CollectionUtils.isEmpty(dataPageList)) {
					Map<String, ParameterVo> statusNameMap = parameterDao.getStatusMap();
					dataPageList.forEach(data -> {
						data.put("TRANS_TYPE_NAME", statusNameMap.get(data.get("TRANS_TYPE")).getParameterName());
						data.put("STATUS_NAME", statusNameMap.get(data.get("STATUS")).getParameterName());
					});
				}
			}
		}else{
			dataPageList = onlineChangeDao.getOnlineChangeDetail(transVo);
		}
		for (Map<String, Object> rowMap : dataPageList) {
			List<String> policyNoList = onlineChangeDao.getTransPolicyNoList((String) rowMap.get("TRANS_NUM"));
			if (policyNoList != null) {
				if (policyNoList.size() == 1) {
					rowMap.put("POLICY_NO", policyNoList.get(0));
				} else {
					rowMap.put("POLICY_NO", String.join(",", policyNoList));
				}
			}
			// 判斷非会员
			if(rowMap.get("ROC_ID") == null || "".equals(rowMap.get("ROC_ID"))) {
				rowMap.put("ROC_ID", "非會員");
			}else { //20221227 新增 身分證遮罩
				String rocId2 =(String) rowMap.get("ROC_ID");
				rocId2 = rocId2.substring(0,3) + "*****" + rocId2.substring(8,10);
				rowMap.put("ROC_ID2", rocId2 );
			}
			//死亡除戶
			if(null == transVo.getTransType() && ApConstants.TRANS_TYPE_DNS_ALLIANCE.equals((String)rowMap.get("TRANS_TYPE"))) {
				Map<String,Object> dnsInfo = (Map<String, Object>) onlineChangeDao.getDnsDetailInfo((String)rowMap.get("TRANS_NUM"));
				if(dnsInfo != null) {
					rowMap.put("POLICY_NO", dnsInfo.get("INSNOM"));
				}
			}
			// 判斷聯盟件
			if(ApConstants.TRANS_TYPE_CONTACT_INFO.equals((String)rowMap.get("TRANS_TYPE"))) {
				//	變更保單聯絡資料單獨處理
				String companyId = onlineChangeDao.getCIFromCompanyId((String)rowMap.get("TRANS_NUM"));
				if(companyId != null && !ApConstants.FROM_COMPANY_L01.equals(companyId)) {
					if(rowMap.get("TRANS_TYPE_NAME") != null) {
						rowMap.put("TRANS_TYPE_NAME", rowMap.get("TRANS_TYPE_NAME")+"(聯盟件)");
					}
				}
			}else if(ApConstants.MEDICAL_TREATMENT_PARAMETER_CODE.equals((String)rowMap.get("TRANS_TYPE"))) {
				//	醫療保單單獨處理
				String companyId = onlineChangeDao.getMedicalTreatmentFromCompanyId((String)rowMap.get("TRANS_NUM"));
				if(companyId != null && !ApConstants.FROM_COMPANY_L01.equals(companyId)) {
					if(rowMap.get("TRANS_TYPE_NAME") != null) {
						rowMap.put("TRANS_TYPE_NAME", rowMap.get("TRANS_TYPE_NAME")+"(聯盟件)");
					}
				}
			}else{
				if(rowMap.get("FROM_COMPANY_ID") != null && !ApConstants.FROM_COMPANY_L01.equals(rowMap.get("FROM_COMPANY_ID"))) {
					if(rowMap.get("TRANS_TYPE_NAME") != null) {
						rowMap.put("TRANS_TYPE_NAME", rowMap.get("TRANS_TYPE_NAME")+"(聯盟件)");
					}
				}
			}
			if (rowMap.get("TRANS_DATE") != null) {
				Date d = new Date(((Timestamp)rowMap.get("TRANS_DATE")).getTime());
				rowMap.put("TRANS_DATE", sdf.format(d));
			}
		}
		return dataPageList;
	}

	/**
	 * 取得線上申請結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getOnlineChangeDetailTotal(TransVo transVo) {
		return onlineChangeDao.getOnlineChangeDetailTotal(transVo);
	}

	/**
	 * 取得線上申請資料-繳別.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-繳別
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransPaymode(TransVo transVo) {
		return onlineChangeDao.getTransPaymode(transVo);
	}

	/**
	 * 取得線上申請資料-年金給付方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-年金給付方式
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransAnnuityMethod(TransVo transVo) {
		return onlineChangeDao.getTransAnnuityMethod(transVo);
	}
	
	/**
	 * 取得線上申請資料-變更紅利選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更紅利選擇權
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransBonus(TransVo transVo) {
		return onlineChangeDao.getTransBonus(transVo);
	}
	
	/**
	 * 取得線上申請資料-變更增值回饋分享金領取方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更增值回饋分享金領取方式
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransReward(TransVo transVo) {
		return onlineChangeDao.getTransReward(transVo);
	}

	/**
	 * 取得線上申請資料-自動墊繳選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-自動墊繳選擇權
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransCushion(TransVo transVo) {
		return onlineChangeDao.getTransCushion(transVo);
	}
	
	/**
	 * 取得線上申請資料-變更受益人.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更受益人
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransBeneficiary(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getTransBeneficiary(transVo);
		dataMap.put("dtl", onlineChangeDao.getTransBeneficiaryDetailList((BigDecimal) dataMap.get("ID")));
		dataMap.put("old", onlineChangeDao.getTransBeneficiaryDetailOldList((BigDecimal) dataMap.get("ID")));
		return dataMap;
	}
	
	/**
	 * 取得線上申請資料-展期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-展期
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransRenew(TransVo transVo) {
		return onlineChangeDao.getTransRenew(transVo);
	}
	
	/**
	 * 取得線上申請資料-減額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-減額
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransReduce(TransVo transVo) {
		return onlineChangeDao.getTransReduce(transVo);
	}
	
	/**
	 * 取得線上申請資料-申請減少保險金額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請減少保險金額
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransReducePolicy(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getTransReducePolicy(transVo);
		dataMap.put("dtl", onlineChangeDao.getTransReducePolicyDetailList((BigDecimal) dataMap.get("ID")));
		dataMap.put("old", onlineChangeDao.getTransReducePolicyDetailOldList((BigDecimal) dataMap.get("ID")));
		return dataMap;
	}
	
	/**
	 * 取得線上申請資料-變更保單聯絡資料.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更保單聯絡資料
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransContactInfo(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getTransContactInfo(transVo);
		dataMap.put("dtl", onlineChangeDao.getTransContactInfoDetailList((BigDecimal) dataMap.get("ID")));
		dataMap.put("old", onlineChangeDao.getTransContactInfoDetailOldList((BigDecimal) dataMap.get("ID")));
		logger.info("1============ : " + transVo.toString() + "============1");
		return dataMap;
	}
	
	/**
	 * 取得線上申請資料-設定停損停利通知.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-設定停損停利通知
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransFundNotification(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getTransFundNotification(transVo);
		dataMap.put("dtl", onlineChangeDao.getTransFundNotificationDetailList((BigDecimal) dataMap.get("ID")));
		return dataMap;
	}
	
	/**
	 * 取得線上申請資料-變更收費管道.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更收費管道
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransPayMethod(TransVo transVo) {
		return onlineChangeDao.getTransPayMethod(transVo);
	}
	
	/**
	 * 取得線上申請資料-變更信用卡效期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更信用卡效期
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransCreditCardInfo(TransVo transVo) {
		return onlineChangeDao.getTransCreditCardInfo(transVo);
	}
	
	/**
	 * 取得線上申請資料-解約.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-解約
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransCancelContract(TransVo transVo) {
		return onlineChangeDao.getTransCancelContract(transVo);
	}
	
	/**
	 * 取得線上申請資料-紅利提領列印.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-紅利提領列印
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransSurrender(TransVo transVo) {
		return onlineChangeDao.getTransSurrender(transVo);
	}
	
	/**
	 * 取得線上申請資料-申請保單貸款.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請保單貸款
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransLoan(TransVo transVo) {
		Map<String, Object> loanMap = onlineChangeDao.getTransLoan(transVo);
		List<TransExtendAttrVo> list = onlineChangeDao.getTransExtendsByTransNum(transVo);
		if (list != null) {
			for (TransExtendAttrVo vo : list) {
				loanMap.put(vo.getAttrKey(), vo.getAttrValue());
			}
		}
		return loanMap;
	}
	
	/**
	 * 取得線上申請資料-基本資料變更.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-基本資料變更
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransLoanCustInfo(TransVo transVo) {
		return onlineChangeDao.getTransLoanCustInfo(transVo);
	}
	
	/**
	 * 更新線上申請狀態.
	 * 
	 * @param transVo TransVo
	 * @return 回傳更新結果
	 */	
	@RequestLog
	public int updateTransStatus(TransVo transVo) {
		return onlineChangeDao.updateTransStatus(transVo);
	}

	/**
	 * 取得線上申請資料-滿期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-滿期
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransMaturity(TransVo transVo) {
		return onlineChangeDao.getTransMaturity(transVo);
	}

	/**
	 * 取得線上申請資料-補發保單.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-補發保單
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransResendPolicy(TransVo transVo) {
		return onlineChangeDao.getTransResendPolicy(transVo);
	}

	/**
	 * 取得線上申請資料-終止授權.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-終止授權
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransCancelAuth(TransVo transVo) {
		return onlineChangeDao.getTransCancelAuth(transVo);
	}

	/**
	 * 取得線上申請資料-保單價值列印.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-保單價值列印
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransValuePrint(TransVo transVo) {
		return onlineChangeDao.getTransValuePrint(transVo);
	}
	
	/**
	 * 取得線上申請資料-旅平險.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-旅平險
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransTravelPolicy(TransVo transVo) {
		return onlineChangeDao.getTransTravelPolicy(transVo);
	}

	@RequestLog
	@Override
	public Optional<byte[]> getTransUploadZipFile(TransVo transVo) {
		byte[] zipFile = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(bos);
			List<TransUploadFileVo> listFile = onlineChangeDao.getAllUploadFile(transVo);
			if(!listFile.isEmpty()) {
				int index = 0;
				for(TransUploadFileVo transUploadFileVo: listFile) {
					ZipEntry entry = new ZipEntry(String.format("%d.%s"
							, index++
							, transUploadFileVo.getFilename()));
					zos.putNextEntry(entry);
					zos.write(transUploadFileVo.getFileData());
				}
			}
			zos.finish();
			zos.close();
			bos.flush();
			bos.close();
			zipFile = bos.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return Optional.ofNullable(zipFile);
	}

	/**
	 * 取得線上申請資料-變更匯款帳號
	 * @param transVo
	 * @return 回傳線上申請資料-變更匯款帳號
	 */
	@RequestLog
	@Override
	public Map<String, Object> getTransChangePayAccount(TransVo transVo) {
		return onlineChangeDao.getTransChangePayAccount(transVo);
	}
	
	@RequestLog
	@Override
	public List<TransExtendAttrVo> getTransExtendsByTransNum(TransVo transVo) {
		return onlineChangeDao.getTransExtendsByTransNum(transVo);
	}

	@Override
	public Map<String, Object> getTransRiskLevel(TransVo transVo) {
		Map<String, Object> map = onlineChangeDao.getTransRiskLevel(transVo);
		List<ParameterVo> list = parameterDao.getParameterByCategoryCode("eservice", Arrays.asList("RISK_LEVEL_TO_RR"));
		if (!CollectionUtils.isEmpty(list)) {
			for (ParameterVo vo : list) {
				Object val = map.get("RISK_LEVEL_OLD");
				Object newVal = map.get("RISK_LEVEL_NEW");
				if (val != null && org.apache.commons.lang3.StringUtils.equals("RISK_LEVEL_TO_RR_" + val, vo.getParameterCode())) {
					map.put("BEFORE", vo.getParameterName());
				}
				if (newVal != null && org.apache.commons.lang3.StringUtils.equals("RISK_LEVEL_TO_RR_" + newVal, vo.getParameterCode())) {
					map.put("AFTER", vo.getParameterName());
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getTransPolicyHolderProfile(TransVo transVo) {
		return onlineChangeDao.getTransPolicyHolderProfile(transVo);
	}	

	@Override
	public Map<String, Object> getTransFundSwitch(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getTransFundSwitch(transVo);
		dataMap.put("out", onlineChangeDao.getTransFundSwitchOut(transVo));
		dataMap.put("in",onlineChangeDao.getTransFundSwitchIn(transVo));
		return dataMap;
	}
	
	@Override
	public Map<String, Object> getDnsAlliance(TransVo transVo) {
		Map<String, Object> dataMap = onlineChangeDao.getDnsAlliance(transVo);
		return dataMap;
	}

	@Override
	public Map<String, Object> getTransInsuranceClaim(TransVo transVo) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		rMap = onlineChangeDao.getTransInsuranceClaim(transVo);
		List fileDatas = onlineChangeDao.getFileDatasDetailByClaimSeqId(Float.parseFloat(rMap.get("CLAIMS_SEQ_ID").toString()));
		List newfileDatas = new ArrayList();
		if (fileDatas != null && fileDatas.size() > 0) {
			for (int i = 0; i < fileDatas.size(); i++) {
				Map map = (Map) fileDatas.get(i);
				String PATH = (String) map.get("PATH");
				String FILE_NAME  = (String)map.get("FILE_NAME");
				String fileBase64 = (String)map.get("FILE_BASE64");
				 if ((PATH!=null && FILE_NAME!=null)||fileBase64!=null) {
					String filePath = PATH+File.separator+FILE_NAME;

					String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
					//pdf文档进行单独处理，抓取缩略图
					if(".pdf".equalsIgnoreCase(substring)) {
						String  letName=filePath.substring(0,filePath.lastIndexOf("."))+".png";
						map.put("fileOrPng",letName);
					}else{
						//获取图片的地址
						map.put("fileOrPng",filePath);
					}

					if(fileBase64!=null && !"".equals(fileBase64)){
						//直接将原文件base64 转为 缩图的 base64
						//进行考虑大文件的的处理
						byte[] decode = Base64.getDecoder().decode(fileBase64);
						//获取类型
						String base64Type = this.checkBase64ImgOrPdf(decode);
						logger.info("--------------------------------------------------PDF Base64  文件的类型="+base64Type);
						ByteArrayInputStream input = new ByteArrayInputStream(decode);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
						
						if (base64Type!=null &&  !"".equals(base64Type)) {
							PDDocument doc = null;
							try {
								if ("png".equals(base64Type) || "jpg".equals(base64Type)) {
									String miniatureBase64 = imgBase64(input, baos);
									map.put("FileBase64",miniatureBase64);
								}else{
									doc = PDDocument.load(input);
									String miniatureBase64 = this.imgBase64(doc, baos);
									map.put("FileBase64",miniatureBase64);
									doc.close();
									
									if(miniatureBase64!=null) {//modify log, do not print base64 string.
										logger.info("PDF Base64轉換為縮圖字串長度="+miniatureBase64.length());
									}else {
										logger.info("PDF Base64轉換為縮圖字串長度 is null.");
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}finally {
								try {
									baos.flush();
									baos.close();
									input.close();
									if(doc!=null) {
										doc.close();
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}else{
						File file = new File(filePath);
						if (file.exists()) {
							//通过路径,获取图片
							map.put("FileBase64", this.converFileToBase64Str(filePath));
							try {
								map.put("FILE_BASE64", this.imgBase64(file));
							}catch(Exception e) {
								logger.error("input file is null.");
								logger.error(e);
							}
						} else {
							map.put("FileBase64", "");
							map.put("FILE_BASE64", "");
						}
					}
					newfileDatas.add(map);
				}
			}
		}
		rMap.put("FileDatas", newfileDatas);
		//logger.info("1======获取的保单图片数据-----------====== : " + rMap.toString() + "============1");
		return rMap;
	}

	@Override
	public int updateInsuranceClaimFileReceived(TransInsuranceClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			if (org.apache.commons.lang3.StringUtils.equals(vo.getFileReceived(), "C")) {
				rtn = onlineChangeDao.updateInsuranceClaimFileReceived(vo, new Date());
			} else {
				rtn = onlineChangeDao.updateInsuranceClaimFileReceived(vo, null);
			}
		}
		return rtn;
	}
	
	@Override
	public int updateICFileReceived(TransInsuranceClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			rtn = onlineChangeDao.updateICFileReceived(vo);
		}
		return rtn;
	}

	@Override
	public int updateInsuranceClaimSendAlliance(TransInsuranceClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			if (org.apache.commons.lang3.StringUtils.equals(vo.getSendAlliance(), "C")) {
				rtn = onlineChangeDao.updateInsuranceClaimSendAlliance(vo, new Date());
			} else {
				rtn = onlineChangeDao.updateInsuranceClaimSendAlliance(vo, null);
			}
		}
		return rtn;
	}
	
	@Override
	public int addInsuranceClaim(TransInsuranceClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			List<TransInsuranceClaimVo> voList = onlineChangeDao.getTransInsuranceClaimByTransNum(vo.getTransNum());
			if (voList != null && voList.size() != 0) {
				for (TransInsuranceClaimVo voTemp : voList) {
					List<TransInsuranceClaimFileDataVo> fileDataList = onlineChangeDao.getFileDatas(voTemp.getClaimSeqId());

					Float seq = onlineChangeDao.getInsuranceClaimSequence();
					voTemp.setClaimSeqId(seq);
					rtn = onlineChangeDao.addInsuranceClaim(voTemp);
	
					if (fileDataList != null && fileDataList.size() != 0) {
						for (TransInsuranceClaimFileDataVo fileDataTemp : fileDataList) {
							fileDataTemp.setClaimSeqId(seq);
							rtn = onlineChangeDao.addInsuranceClaimFileData(fileDataTemp);
						}
					}
				}
			}
		}
		return rtn;
	}

	@Override
	public List<UnionCourseVo> getUnionCourseList(UnionCourseVo vo) {
		// TODO Auto-generated method stub
		return unionCourseDao.getUnionCourseList(vo);
	}
	
	/**
	 * 添加狀態歷程
	 * @param vo
	 * @return
	 */
	@Override
	public int addTransStatusHistory(TransStatusHistoryVo vo) {
		return onlineChangeDao.addTransStatusHistory(vo);
	}
	
	/**
	 * 添加補件
	 * @param vo
	 * @return
	 */
	@Override
	public int addTransRFE(TransRFEVo vo) {
		return onlineChangeDao.addTransRFE(vo);
	}
	
	/**
	 * 查詢狀態歷程
	 * @param vo
	 * @return
	 */
	@Override
	public List<TransStatusHistoryVo> getTransStatusHistoryList(TransStatusHistoryVo vo) {
		return onlineChangeDao.getTransStatusHistoryList(vo);
	}
	
	/**
	 * 查詢補件單歷程
	 * @param vo
	 * @return
	 */
	@Override
	public List<TransRFEVo> getTransRFEList(TransRFEVo vo) {
		 List<TransRFEVo> transRFEVos = onlineChangeDao.getTransRFEList(vo);
		 for (int i = 0; i < transRFEVos.size(); i++) {
			 TransRFEVo tVo = transRFEVos.get(i);
			 
			 List<TransInsuranceClaimFileDataVo> fileDataVoList = onlineChangeDao.getTransInsCliamFileData(tVo);
			for (TransInsuranceClaimFileDataVo fileDataVo : fileDataVoList) {
				//String filePath = fileDataVo.getFilePath()+"/"+fileDataVo.getFileName();
				String filePath = fileDataVo.getFilePath()+File.separator+fileDataVo.getFileName();
				fileDataVo.setFileBase64(this.converFileToBase64Str(filePath));
			}
			 tVo.setFileDatas(fileDataVoList);
		}
		return transRFEVos;
	}
	
	/**
	 * 更新補件單歷程
	 * @param vo
	 * @return
	 */
	@Override
	public int updateTransRFEStatus(TransRFEVo vo) {
		return onlineChangeDao.updateTransRFEStatus(vo);
	}

	
	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	@Override
	public String getRoleName(String userId) {
		String result = "";
		List<String> results = onlineChangeDao.getRoleName(userId);
		if (results != null && results.size() != 0) {
			for (String temp : results) {
				result = result + temp + ",";
			}
			return result.substring(0, result.length()-1);
		}
		return result;
	}
	
	@Override
	public int getCaseIDNum(String transNum) {
		return onlineChangeDao.getCaseIDNum(transNum);
	}
	
	/**
	 * 加入黑名單
	 * @param vo
	 * @return
	 */
	@Override
	public int addBlackList(TransStatusHistoryVo vo) {
		int k = 0;
		if(onlineChangeDao.checkIdNoExist(vo.getTransNum()) == 0) {
			k = onlineChangeDao.addBlackList(vo);
		}
		return k;
	}
	
	/**
	 * 查詢當前狀態歷程
	 * @param vo
	 * @return
	 */
	@Override
	public TransStatusHistoryVo getTransStatusHis(TransStatusHistoryVo vo) {
		return onlineChangeDao.getTransStatusHis(vo);
	}

	
	/**
	 * 發送郵件
	 * @param transNum
	 * @param code
	 * @param status
	 * @return
	 */
	@Override
	public void sendMailTO(String transNum,String code,String status) {
		// TODO Auto-generated method stub
		String detailInfo = onlineChangeDao.getInfoTOMail(transNum);
		String[] strs = detailInfo.split("\\|");
		String mail = strs[3];
		if(StringUtils.isEmpty(mail) && !StringUtils.isEmpty(strs[4])) {
			mail = onlineChangeDao.getMailByRocid(strs[4]);
		}
		if(!StringUtils.isEmpty(mail)) {
				String statusName = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, status);
				SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
				String loginTime = formater.format(new Date());
				List<String> receivers = new ArrayList<String>();
				receivers.add(mail);
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("TransNum", transNum);
				paramMap.put("TransStatus", statusName);
				paramMap.put("LoginTime", loginTime);
			if("2".equals(status)) {// 已完成
				String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_TRANS_REMARK);
				paramMap.put("TransRemark", transRemark);
				messageTemplateClient.msgApi(getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_005,receivers,paramMap,"email"));
				logger.info("發送保戶MAIL : " + mail);
		}else {// 異常件註記
//				List<ParameterVo> pList = parameterDao.getOptionList(ApConstants.SYSTEM_ID,ApConstants.ABNORMAL_REASON);
//				for (ParameterVo pVo : pList) {
//					if(code.equals(pVo.getParameterValue())) {
//						code = pVo.getParameterCode();
//					}
//				}
				String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_ABNORMAL_TRANS_REMARK);
				paramMap.put("TransRemark", transRemark);
				messageTemplateClient.msgApi(getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_006,receivers,paramMap,"email"));
				logger.info("發送保戶MAIL : " + mail);
				
		}
	}
	}

	@Override
	public List getInsClaimStatisticsReport(InsClaimStatisticsVo claimVo) {
		// TODO Auto-generated method stub
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();
		for (int i = 0; i < k; i++) {
			str += "C." + columns.get(i);
			if(i != k-1) {
				str += ",";
			}
		}
		return onlineChangeDao.getInsClaimStatisticsReport(claimVo,str,claimVo.getStatus(),claimVo.getFileReceivedList(),claimVo.getSendAllianceList());
	}
	
	@Override
	public List getInsClaimDetailReport(InsClaimStatisticsVo claimVo) {
		// TODO Auto-generated method stub
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();
		for (int i = 0; i < k; i++) {
				str += "C." + columns.get(i);
			if(i != k-1) {
				str += ",";
			}
		}
		List detailList = onlineChangeDao.getInsClaimDetailReport(claimVo,str,claimVo.getStatus(),claimVo.getFileReceivedList(),claimVo.getSendAllianceList());
//		for (int i = 0; i < detailList.size(); i++) {
//			Map map = (Map)detailList.get(i);
//			String name = (String)map.get("LIPM_NAME_1");
//			detailList.get(i).put("LIPM_NAME_1",unicodeService.convertString2Unicode(name));
//			
//		}
		return detailList;
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
				logger.info("--------------------------------------------------input filePath="+filePath);
				File file = new File(filePath);
				long length = file.length();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
				String substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				//pdf获取缩略图
				if(".pdf".equals(substring) || ".PDF".equals(substring)){
					logger.info("--------------------------------------------------input filePath pdf=>Image="+filePath);
					PDDocument doc = PDDocument.load(file);
					encodedString =this.imgBase64(doc,baos);
					logger.error("--------------------------------------------------Thumbnails  PDF=>img Base64 ");
					doc.close();
			}else {
					//<=50KB
					if (length<=51200) {
						logger.info("--------------------------------------------------input filePath length<=51200{}"+filePath);
						encodedString =this.imgBase64(file);
						logger.error("--------------------------------------------------Thumbnails  Base64 length<=51200");
					}else{
						logger.info("--------------------------------------------------input filePath length>51200{}"+filePath);
						//进行抓取缩略图
						encodedString =this.imgBase64(file,baos);
						logger.error("--------------------------------------------------Thumbnails  Base64 length>51200");
					}
				}
				logger.error("--------------------------------------------------Thumbnails  Base64 ");
			}
		}catch(Exception e) {
			logger.error("input filePath is null.");
			logger.error(e);
		}
		
		return encodedString;
	}
	/**
	 * 通过Base64 Byte数组进行判断文件是什么格式的
	 * @param b
	 * @return
	 */
	private  String checkBase64ImgOrPdf(byte[] b) {
		String type = "";
		if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
			type = "png";
		} else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
			type = "jpg";
		}else{
			type = "pdf";
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
	 * 原始大小Base64數據
	 * @param
	 * @param baos
	 * @return  base64数据
	 * @throws IOException
	 */
	private  String   imgOriginalBase64(byte[] bytes ) throws IOException {
		String base64= Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}
	/**
	 * PPT转换图片50*50的base64数据
	 * @param filePath  PPT转换图片50*50地址
	 * @param baos
	 * @return  base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(PDDocument doc,ByteArrayOutputStream baos) throws IOException {
		BufferedImage  bufferedImage = pdfBufferedImage(doc);
		Thumbnails.of(bufferedImage). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64= Base64.getEncoder().encodeToString(bytes);
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
	 * @param file  获取图片50*50地址
	 * @param baos
	 * @return   base64数据
	 * @throws IOException
	 */
	private  String   imgBase64(InputStream inputStream, ByteArrayOutputStream baos) throws IOException {
		Thumbnails.of(inputStream). size(50, 50).outputQuality(0.25f).outputFormat("png").toOutputStream(baos);
		byte[] bytes = baos.toByteArray();//转换成字节
		String base64= Base64.getEncoder().encodeToString(bytes);
		return  base64;
	}
	private MessageTriggerRequestVo getMessageTriggerRequestVo(String msgCode, List<String> receivers, Map<String, String> paramMap,String type) {
		MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
		vo.setMessagingTemplateCode(msgCode);
		vo.setSendType(type);
		vo.setMessagingReceivers(receivers);
		vo.setParameters(paramMap);
		vo.setSystemId(ApConstants.SYSTEM_ID);
		return vo;
	}

	/**
	 * 抓取PDF第一张转换为缩略图
	 * @param path   pdf 地址
	 * @return  缩略图
	 */
	private BufferedImage pdfBufferedImage(PDDocument doc ) {
		//File file = null;
		//PDDocument doc = null;
		PDFRenderer renderer = null;
		BufferedImage bufferedImage = null;
		try {
			//file = new File(path);
			//String imgPDFPath = file.getParent();
			//int dot = file.getName().lastIndexOf('.');
			//String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
			//doc = PDDocument.load(file);
			renderer = new PDFRenderer(doc);
			//int pageCount = doc.getNumberOfPages();
			List<BufferedImage> piclist = new ArrayList<>();
			for (int i = 0; i < 1; i++) {
				/* dpi越大转换后144越清晰，相对转换速度越慢 */
				BufferedImage image = renderer.renderImageWithDPI(i, 144);
				//ImageIO.write(image, "png", new File("));
				piclist.add(image);
			}
			bufferedImage = listBufferedImage(piclist);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}

	/**
	 * 生产图片
	 * @param piclist
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
	public List getContactInfoStatisticsReport(ContactInfoReportVo claimVo) {
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();
		for (int i = 0; i < k; i++) {
			str += "C." + columns.get(i);
			if(i != k-1) {
				str += ",";
			}
		}
		return onlineChangeDao.getContactInfoStatisticsReport(claimVo,str,claimVo.getStatus(),claimVo.getCompanyList());
	}
	
	@Override
	public List getContactInfoDetailReport(ContactInfoReportVo claimVo) {
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();
		for (int i = 0; i < k; i++) {
				str += "C." + columns.get(i);
			if(i != k-1) {
				str += ",";
			}
		}
		List detailList = onlineChangeDao.getContactInfoDetailReport(claimVo,str,claimVo.getStatus(),claimVo.getCompanyList());
		return detailList;
	}
	
	@Override
	public int contactInfoFail(TransVo vo) {
		return onlineChangeDao.updateTransStatus(vo);
	}

	@Override
	public int contactInfoComplete(TransVo vo) {
		return onlineChangeDao.updateTransStatus(vo);
	}

	@Override

	public List<Map<String, Object>> getDNS_CSV(TransVo transVo) {
		return onlineChangeDao.getDNS_CSV(transVo);
	}

	public List getMedicalTreatmentStatisticsReport(MedicalTreatmentStatisticsVo claimVo) {
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();

		for(int i = 0; i < k; ++i) {
			str = str + "C." + (String)columns.get(i);
			if (i != k - 1) {
				str = str + ",";
			}
		}

		return this.onlineChangeDao.getMedicalTreatmentStatisticsReport(claimVo, str, claimVo.getStatus(), claimVo.getFileReceivedList(), claimVo.getSendAllianceList());
	}
	public List getMedicalTreatmentDetailReport(MedicalTreatmentStatisticsVo claimVo) {
		List<String> columns = claimVo.getColumn();
		String str = "";
		int k = columns.size();

		for(int i = 0; i < k; ++i) {
			str = str + "C." + (String)columns.get(i);
			if (i != k - 1) {
				str = str + ",";
			}
		}

		List detailList = this.onlineChangeDao.getMedicalTreatmentDetailReport(claimVo, str, claimVo.getStatus(), claimVo.getFileReceivedList(), claimVo.getSendAllianceList());
		return detailList;
	}

	@Override
	public Map<String, Object> getMedicalTreatmentClaim(TransVo transVo) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		rMap = onlineChangeDao.getMedicalTreatmentClaim(transVo);
		List fileDatas = onlineChangeDao.getMedicalTreatmentFileDatasDetailByClaimSeqId(Float.parseFloat(rMap.get("CLAIMS_SEQ_ID").toString()));
		List newfileDatas = new ArrayList();
		if (fileDatas != null && fileDatas.size() > 0) {
			for (int i = 0; i < fileDatas.size(); i++) {
				Map map = (Map) fileDatas.get(i);
				String PATH = (String) map.get("PATH");
				String FILE_NAME  = (String)map.get("FILE_NAME");
				String fileBase64 = (String)map.get("FILE_BASE64");
				if ((PATH!=null && FILE_NAME!=null)||fileBase64!=null) {
					String filePath="";
					String substring="";
					if(!org.springframework.util.StringUtils.isEmpty(PATH)
							&&  !org.springframework.util.StringUtils.isEmpty(FILE_NAME)) {
						filePath= PATH + File.separator + FILE_NAME;
						if (filePath.lastIndexOf(".")!=-1) {
							substring = filePath.substring(filePath.lastIndexOf("."), filePath.length());
						}
					}
					//pdf文档进行单独处理，抓取缩略图
					if (".pdf".equalsIgnoreCase(substring)) {
						String letName = filePath.substring(0, filePath.lastIndexOf(".")) + ".png";
						map.put("fileOrPng", letName);
					} else {
						if(filePath.lastIndexOf(".png")!=-1 || filePath.lastIndexOf(".jpg")!=-1){
						//获取图片的地址
						map.put("fileOrPng", filePath);
						}else{
							map.put("fileOrPng", filePath+".png");
						}
					}
					if(fileBase64!=null && !"".equals(fileBase64)){
						//直接将原文件base64 转为 缩图的 base64
						byte[] decode = Base64.getDecoder().decode(fileBase64);
						//获取类型
						String base64Type = this.checkBase64ImgOrPdf(decode);
						logger.info("--------------------------------------------------PDF Base64  文件的类型="+base64Type);
						ByteArrayInputStream input = new ByteArrayInputStream(decode);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流

						if (base64Type!=null &&  !"".equals(base64Type)) {
							PDDocument doc = null;
							PDDocument newDoc = null;
							try {
								filePath= PATH + File.separator + FILE_NAME;
								if ("png".equals(base64Type) || "jpg".equals(base64Type)) {
									String miniatureBase64 = imgBase64(input, baos);
									map.put("FileBase64",miniatureBase64);
								}else{
									doc = PDDocument.load(input);
									PDPage page = doc.getPage(0);
									newDoc = new PDDocument();
									newDoc.addPage(page);
									String miniatureBase64 = this.imgBase64(newDoc, baos);
									logger.info("--------------------------------------------------PDF Base64转换为缩图-----Base64  is  not  null");
									map.put("FileBase64",miniatureBase64);
									doc.close();
								}
							//	map.put("fileOrPng", filePath+".png");
							} catch (IOException e) {
								e.printStackTrace();
							}finally {
								if (newDoc != null) {
									try {
										newDoc.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								try {
									baos.flush();
									baos.close();
									input.close();
									if(doc!=null) {
										doc.close();
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}else{
						if(!org.springframework.util.StringUtils.isEmpty(PATH)) {
						File file = new File(filePath);
						if (file.exists()) {
							//通过路径,获取图片
							map.put("FileBase64", this.converFileToBase64Str(filePath));
							}else{
								map.put("FileBase64", "");
						}
					}
					}
					newfileDatas.add(map);
				}
			}
		}
		//進行獲取聯盟的數據信息類型
		List<ParameterVo> parameters = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_API_ID, Arrays.asList(ApConstants.MEDICAL_TREATMENT_FEDERATION_FILE_TYPE));
		List<ParameterVo> medicalUploadfile = parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID_ESERVICE, Arrays.asList(ApConstants.MEDICAL_TREATMENT_UPLOADFILE));

		rMap.put("Parameters", parameters);
		rMap.put("medicalUploadfile", medicalUploadfile);
		rMap.put("FileDatas", newfileDatas);
		//logger.info("1======获取的保单图片数据-----------====== : " + rMap.toString() + "============1");
		/**
		 * 對時間的進行處理
		 * */
		String birdate = (String) rMap.get("BIRDATE");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(birdate)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("yyyyMMdd", birdate, "yyyy/MM/dd");
			rMap.put("BIRDATE", yyyyMMdd);
		}
		String accident_date = (String) rMap.get("ACCIDENT_DATE");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(accident_date)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("yyyyMMdd", accident_date, "yyyy/MM/dd");
			rMap.put("ACCIDENT_DATE", yyyyMMdd);
		}
		String accident_time = (String) rMap.get("ACCIDENT_TIME");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(accident_time)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("HHmm", accident_time, "HH:mm");
			rMap.put("ACCIDENT_TIME", yyyyMMdd);
		}
		String police_date = (String) rMap.get("POLICE_DATE");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(police_date)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("yyyyMMdd", police_date, "yyyy/MM/dd");
			rMap.put("POLICE_DATE", yyyyMMdd);
		}

		String police_time = (String) rMap.get("POLICE_TIME");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(police_time)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("HHmm", police_time, "HH:mm");
			rMap.put("POLICE_TIME", yyyyMMdd);
		}
		String authorization_start_date = (String) rMap.get("AUTHORIZATION_START_DATE");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(authorization_start_date)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("yyyyMMdd", authorization_start_date, "yyyy/MM/dd");
			rMap.put("AUTHORIZATION_START_DATE", yyyyMMdd);
		}
		String authorization_end_date = (String) rMap.get("AUTHORIZATION_END_DATE");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(authorization_end_date)) {
			String yyyyMMdd = DateFormatUtil.getStringToDateString("yyyyMMdd", authorization_end_date, "yyyy/MM/dd");
			rMap.put("AUTHORIZATION_END_DATE", yyyyMMdd);
		}

		return rMap;
	}

	@Override
	public List<Hospital> getHospitalList(String medicalTreatmentParameterCode) {
		return onlineChangeDao.getHospitalList(medicalTreatmentParameterCode);
	}

	@Override
	public List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(String medicalTreatmentParameterCode) {
		return onlineChangeDao.getHospitalInsuranceCompanyList(medicalTreatmentParameterCode);
	}

	@Override
	public List<TransRFEVo> getMedicalTreatmentTransRFEList(TransRFEVo vo) {
		List<TransRFEVo> transRFEVos = onlineChangeDao.getTransRFEList(vo);
		for (int i = 0; i < transRFEVos.size(); i++) {
			TransRFEVo tVo = transRFEVos.get(i);

			List<MedicalTreatmentClaimFileDataVo> fileDataVoList = onlineChangeDao.getTransMedicalTreatmentClaimFiledatas(tVo);
			for (MedicalTreatmentClaimFileDataVo fileDataVo : fileDataVoList) {
				//String filePath = fileDataVo.getFilePath()+"/"+fileDataVo.getFileName();
				String filePath = fileDataVo.getFilePath()+File.separator+fileDataVo.getFileName();
				fileDataVo.setFileBase64(this.converFileToBase64Str(filePath));
			}
			tVo.setMedicalTreatmentFileDatas(fileDataVoList);
		}
		return transRFEVos;
	}

	@Override
	public int getMedicalTreatmentCaseIDNum(String transNum) {
		return onlineChangeDao.getMedicalTreatmentCaseIDNum(transNum);
	}

	@Override
	public int checkMedicalTreatmentIdNoExist(String transNum) {
		int i = onlineChangeDao.checkMedicalTreatmentIdNoExist(transNum);
		return i;
	}

	@Override
	public int addMedicalTreatmentBlackList(TransStatusHistoryVo vo) {
		return onlineChangeDao.addMedicalTreatmentBlackList(vo);
	}

	@Override
	public void sendMedicalTreatmentMailTO(String transNum, String rejectReason, String status) {
		// TODO Auto-generated method stub
		String detailInfo = onlineChangeDao.getInfoMedicalTreatmentTOMail(transNum);
		String[] strs = detailInfo.split("\\|");
		String mail = strs[3];
		if(StringUtils.isEmpty(mail) && !StringUtils.isEmpty(strs[4])) {
			mail = onlineChangeDao.getMailByRocid(strs[4]);
		}
		if(!StringUtils.isEmpty(mail)) {
			String statusName = parameterDao.getStatusName(ApConstants.ONLINE_CHANGE_STATUS, status);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
			String loginTime = formater.format(new Date());
			List<String> receivers = new ArrayList<String>();
			receivers.add(mail);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("TransNum", transNum);
			paramMap.put("TransStatus", statusName);
			paramMap.put("LoginTime", loginTime);
			if("2".equals(status)) {// 已完成
				String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_TRANS_REMARK);
				paramMap.put("TransRemark", transRemark);
				messageTemplateClient.msgApi(getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_005,receivers,paramMap,"email"));
				logger.info("發送保戶MAIL : " + mail);
			}else {// 異常件註記
//				List<ParameterVo> pList = parameterDao.getOptionList(ApConstants.SYSTEM_ID,ApConstants.ABNORMAL_REASON);
//				for (ParameterVo pVo : pList) {
//					if(code.equals(pVo.getParameterValue())) {
//						code = pVo.getParameterCode();
//					}
//				}
				String transRemark = parameterDao.getStatusName(ApConstants.MESSAGING_PARAMETER, ApConstants.INSURANCE_CLAIM_ABNORMAL_TRANS_REMARK);
				paramMap.put("TransRemark", transRemark);
				messageTemplateClient.msgApi(getMessageTriggerRequestVo(ApConstants.ELIFE_MAIL_006,receivers,paramMap,"email"));
				logger.info("發送保戶MAIL : " + mail);

			}
		}
	}

	@Override
	public int getOnlineChangeMedicalTreatmentDetailTotal(TransVo transVo) {
		return onlineChangeDao.getOnlineChangeMedicalTreatmentDetailTotal(transVo);
	}

	@Override
	public MedicalTreatmentClaimFileDataVo getMedicalTreatmentDetailBase64FileSize(Float fdId) throws Exception {
		MedicalTreatmentClaimFileDataVo medicalVo=null;
		if (fdId!=null && fdId!=0) {
			medicalVo=	onlineChangeDao.getMedicalTreatmentDetailBase64(fdId);
			String fileBase64 = medicalVo.getFileBase64();
			if (fileBase64 != null && !"".equals(fileBase64)) {
				byte[] decode = Base64.getDecoder().decode(fileBase64);
				//获取文件类型
				String base64Type = this.checkBase64ImgOrPdf(decode);
				if (base64Type != null && !"".equals(base64Type)){
					try {
						if ("png".equals(base64Type) || "jpg".equals(base64Type)) {
							int length = decode.length;//转换成字节
							if(length<2097152) {
								String miniatureBase64 = this.imgOriginalBase64(decode);
								medicalVo.setFileBase64(miniatureBase64);
								medicalVo.setType("png");
							}else{
								medicalVo.setFileBase64("");
							}
						} else {
							int length = decode.length;//转换成字节
							if(length<2097152) {
								String miniatureBase64 = this.imgOriginalBase64(decode);
								medicalVo.setFileBase64(miniatureBase64);
								medicalVo.setType("pdf");
							}else{
								medicalVo.setFileBase64("");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			medicalVo=new MedicalTreatmentClaimFileDataVo();
			medicalVo.setFileSize(0F);
			medicalVo.setFileBase64("");
		}
		return medicalVo;
	}

	@Override
	public int updateMedicalTreatmentSendAlliance(TransMedicalTreatmentClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			if (org.apache.commons.lang3.StringUtils.equals(vo.getSendAlliance(), "C")) {
				rtn = onlineChangeDao.updateMedicalTreatmentSendAlliance(vo, new Date());
			} else {
				rtn = onlineChangeDao.updateMedicalTreatmentSendAlliance(vo, null);
			}
		}
		return rtn;
	}

	@Override
	public int addMedicalTreatmentClaim(TransMedicalTreatmentClaimVo vo) {
		int rtn = -1;
		if(vo!=null && vo.getTransNum()!=null) {
		 List<TransMedicalTreatmentClaimVo> voList = onlineChangeDao.getTransMedicalTreatmentByTransNum(vo.getTransNum());
			if (voList != null && voList.size() != 0) {
				for (TransMedicalTreatmentClaimVo voTemp : voList) {
					Float seq = onlineChangeDao.getMedicalTreatmentClaimSequence();
					voTemp.setClaimSeqId(seq);
					rtn = onlineChangeDao.addMedicalTreatmentClaim(voTemp);
				}
			}
		}
		return rtn;
	}

	@Override
	@Transactional
	public int updateOrAddMedicalTreatment(TransMedicalTreatmentClaimVo vo) {
		int result = -1;
		if(vo!=null && vo.getTransNum()!=null) {
			if (ApConstants.SEND_ALLIANCE.equals(vo.getFileReceived())) {
				vo.setOldFileReceived("C");
			}
			result = updateMedicalTreatmentSendAlliance(vo);

			/*
			 不進行手動推送,使用api  cron.saveTransToMedical.expression 定時推送
			if("Y".equals(vo.getSendAlliance())) {//審核通過才insert MedicalTreatment
				result = addMedicalTreatmentClaim(vo);
			}*/

		}
		return result;
	}

	@Override
	public String getTransMedicalTreatmentByAllianceStatus(String transNum) throws Exception {
		return onlineChangeDao.getTransMedicalTreatmentByAllianceStatus(transNum);
	}

	@Override
	public int getTransMedicalTreatmentByCount(String transNum) throws Exception {
		return onlineChangeDao.getTransMedicalTreatmentByCount(transNum);
	}

	@Override
	public int getTransMedicalTreatmentBySuccessCount(String transNum) throws Exception {
		return onlineChangeDao.getTransMedicalTreatmentBySuccessCount(transNum);
	}

	@Override
	public String getTransMedicalTreatmentByCaseId(String transNum) throws Exception {
		return onlineChangeDao.getTransMedicalTreatmentByCaseId(transNum);
	}

	@Override
	public void updateTarnsMedicalTreatmentClaimToAllianceStatus(TransMedicalTreatmentClaimVo mvo)throws Exception {
		 onlineChangeDao.updateTarnsMedicalTreatmentClaimToAllianceStatus(mvo);
	}

	@Override
	public List<NotifyOfNewCaseMedicalVo> getUnionCourseListAllianceStatusMsg(NotifyOfNewCaseMedicalVo vo) throws Exception {
		if (!org.springframework.util.StringUtils.isEmpty(vo.getCaseId())) {
			return onlineChangeDao.getUnionCourseListAllianceStatusMsg(vo);
		}
		return null;
	}

	@Override
	public List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo(Double claimId) {
		List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfoList = onlineChangeDao.getMedicalInfoByClaimId(claimId);
		if (medicalInfoList != null && medicalInfoList.size() >= 1) {
			for (TransMedicalTreatmentClaimMedicalInfoVo medicalInfo : medicalInfoList) {
				if (medicalInfo.getDtypeList() != null && medicalInfo.getDtypeList().size() >= 1) {
					for (Map<String, String> map : medicalInfo.getDtypeList()) {
						String fileBase64 = map.get("fileBase64");
						if (fileBase64 != null && !"".equals(fileBase64)) {
							//直接将原文件base64 转为 缩图的 base64
							byte[] decode = Base64.getDecoder().decode(fileBase64);
							//获取类型
							String base64Type = this.checkBase64ImgOrPdf(decode);
							map.put("fileType", base64Type);
							logger.info("--------------------------------------------------PDF Base64  文件的类型="+base64Type);
							if (base64Type!=null && ("pdf".equalsIgnoreCase(base64Type))) {
								File f = new File("print/tmp/file/" + UUID.randomUUID() + ".pdf");
								if (!f.getParentFile().exists()) {
									f.getParentFile().mkdirs();
								}
								try (FileOutputStream out = new FileOutputStream(f)) {
									IOUtils.write(decode, out);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
								try (PDDocument doc = PDDocument.load(f); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
									String miniatureBase64 = this.imgBase64(doc, baos);
									logger.info("--------------------------------------------------PDF Base64转换为缩图-----Base64  is  not  null");
									map.put("fileBase64", miniatureBase64);
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						}
					}
				}
			}
		}
		return medicalInfoList;
	}

	@Override
	public MedicalTreatmentClaimFileDataVo getMedicalInfoBase64FileSize(Float fdId) throws Exception {
		MedicalTreatmentClaimFileDataVo medicalVo=null;
		if (fdId!=null && fdId!=0) {
			medicalVo=	onlineChangeDao.getMedicalInfoDetailBase64(fdId);
			String fileBase64 = medicalVo.getFileBase64();
			if (fileBase64 != null && !"".equals(fileBase64)) {
				byte[] decode = Base64.getDecoder().decode(fileBase64);
				//获取文件类型
				String base64Type = this.checkBase64ImgOrPdf(decode);
				if (base64Type != null && !"".equals(base64Type)){
					try {
						if ("png".equals(base64Type) || "jpg".equals(base64Type)) {
							int length = decode.length;//转换成字节
							if(length<2097152) {
								String miniatureBase64 = this.imgOriginalBase64(decode);
								medicalVo.setFileBase64(miniatureBase64);
								medicalVo.setType("png");
							}else{
								medicalVo.setFileBase64("");
							}
						} else {
							int length = decode.length;//转换成字节
							if(length<2097152) {
								String miniatureBase64 = this.imgOriginalBase64(decode);
								medicalVo.setFileBase64(miniatureBase64);
								medicalVo.setType("pdf");
							}else{
								medicalVo.setFileBase64("");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			medicalVo=new MedicalTreatmentClaimFileDataVo();
			medicalVo.setFileSize(0F);
			medicalVo.setFileBase64("");
		}
		return medicalVo;
	}


	@Override
	public Map<String, Object> getConversionDetail(TransVo transVo) {
		List<TransFundConversionVo> conversionDetail = onlineChangeDao.getConversionDetail(transVo);
		Map<String, Object> map = new HashMap<>();
		ArrayList<TransFundConversionVo> out = new ArrayList<>();
		ArrayList<TransFundConversionVo> in = new ArrayList<>();
		conversionDetail.stream().forEach((x) -> {
			if (ApConstants.INVESTMENT_STATUS_OUT.equals(x.getInvestmentType())) {
				TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
				transFundConversionVo.setInvtNo(x.getInvtNo());
				transFundConversionVo.setInvtName(x.getInvtName());
				transFundConversionVo.setRatio(x.getRatio());
				transFundConversionVo.setValue(x.getValue());
				out.add(transFundConversionVo);
			}
			if (ApConstants.INVESTMENT_STATUS_IN.equals(x.getInvestmentType())) {
				TransFundConversionVo transFundConversionVo = new TransFundConversionVo();
				transFundConversionVo.setInvtNo(x.getInvtNo());
				transFundConversionVo.setInvtName(x.getFundName());
				transFundConversionVo.setRatio(x.getRatio());

				//組裝數據
				transFundConversionVo.setBankAccount(x.getBankAccount());
				transFundConversionVo.setBankName(x.getBankName());
				transFundConversionVo.setBranchName(x.getBranchName());
				transFundConversionVo.setSwiftCode(x.getSwiftCode());
				transFundConversionVo.setEnglishName(x.getEnglishName());
				transFundConversionVo.setAccountName(x.getAccountName());

				in.add(transFundConversionVo);
			}
		});
 		//查詢當前保單的日期\狀態\單號\名稱
		String transNum = transVo.getTransNum();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(transNum)) {
			map=onlineChangeDao.getOnlineChangeDetailByTransNum(transNum);

		}
		//進行查詢當前保單推送給核心的數據信息
	 String  applyItem=	onlineChangeDao.getTransApplyItemByTransNum(transNum);

		map.put(ApConstants.INVESTMENT_STATUS_OUT,out);
		map.put("applyItem",applyItem);
		map.put(ApConstants.INVESTMENT_STATUS_IN,in);

		return map;
	}

    @Override
    public Map<String, Object> getInvestmentDetail(TransVo transVo) {
		Map<String, Object> map = Maps.newHashMap();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(transVo.getTransNum())) {
			map = onlineChangeDao.getOnlineChangeDetailByTransNum(transVo.getTransNum());
		}

		map.put("investments", onlineChangeDao.selectCompareInvestments(transVo));
        return map;
    }

    @Override
    public Map<String, Object> getDepositDetail(TransVo transVo) {
		Map<String, Object> map = Maps.newHashMap();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(transVo.getTransNum())) {
			map = onlineChangeDao.getOnlineChangeDetailByTransNum(transVo.getTransNum());

		}
		TransDepositDetailVo vo = onlineChangeDao.getAppliedTransDeposits(transVo.getTransNum());
		if (!CollectionUtils.isEmpty(vo.getDetails())) {
			for (Map<String, Object> detail : vo.getDetails()) {
				if (vo.getAmount() == null) {
					vo.setAmount(0D);
				}
				vo.setAmount(BigDecimal.valueOf(vo.getAmount()).add((BigDecimal)detail.get("amount")).doubleValue());
			}
		}
		map.put("deposits", vo);
		return map;
    }

	@Override
	public Map<String, Object> getCashPaymentDetail(TransVo transVo) {
		Map<String, Object> map = Maps.newHashMap();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(transVo.getTransNum())) {
			map = onlineChangeDao.getOnlineChangeDetailByTransNum(transVo.getTransNum());

		}
		map.put("cashPayment", onlineChangeDao.getTransPaymentByTransNum(transVo.getTransNum()));
		return map;
	}

	@Override
    public Map<String, Object> getTransChangePremiumDetail(TransVo transVo) {
		Map<String, Object> map = Maps.newHashMap();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(transVo.getTransNum())) {
			map = onlineChangeDao.getOnlineChangeDetailByTransNum(transVo.getTransNum());

		}
		List<Map<String, Object>> detailList = onlineChangeDao.getTransChangePremium(transVo.getTransNum());
		Map<String, Object> detailVo = Maps.newHashMap();
		if (detailList != null && detailList.size() > 0) {
			detailVo = detailList.get(0);
		}
		map.put("changePremium", detailVo);
		return map;
    }

    @Override
	public int getOnlineChangeCIODetailTotal(TransVo transVo) {
		return onlineChangeDao.getOnlineChangeCIODetailTotal(transVo);
	}

	@Override
	public int getOnlineChangeDnsDetailTotal(TransVo transVo) {
		return onlineChangeDao.getOnlineChangeDnsDetailTotal(transVo);
	}
	
	@Override
	public Map<String , Object> getTransElectronicFormMethod(TransVo transVo){
		return onlineChangeDao.getTransElectronicFormMethod(transVo);
	}
	
	@Override
	public Map<String , Object> getTransDeratePaidOffMethod(TransVo transVo){
		return onlineChangeDao.getTransDeratePaidOffMethod(transVo);
	}
	
	@Override
	public Map<String , Object> getTransRolloverPeriodicallyMethod(TransVo transVo){
		return onlineChangeDao.getTransRolloverPeriodicallyMethod(transVo);
	}

	@Override
	public Map<String, Object> getTransContractRevocationMethod(TransVo transVo) throws Exception {
		return onlineChangeDao.getTransContractRevocationMethod(transVo);
	}

    @Override
    public SignRecord getNewSignStatus(String transNum) {
        return onlineChangeDao.getNewSignStatus(transNum);
    }

    @Override
    public byte[] getSignPdf(String signFileId) {
		String fileBase64 = onlineChangeDao.getSignFileByFileId(signFileId);
		return org.apache.commons.lang3.StringUtils.isBlank(fileBase64) ? null : Base64.getDecoder().decode(fileBase64);
    }

    @Override
    public List<BxczSignApiLog> getSignHistoryList(TransVo vo) {
        return onlineChangeDao.getSignApiRecordByTransNum(vo.getTransNum());
    }

    @Override
    public int updatePolicyClaimApplyDate(String transNum) {
        return onlineChangeDao.updatePolicyClaimApplyDate(transNum, new Date());
    }

	@Override
	public int updateMedicalTreatmentApplyDate(String transNum) {
		return onlineChangeDao.updateMedicalTreatmentApplyDate(transNum, new Date());
	}

    @Override
    public TransInsuranceClaimFileDataVo getInsuranceClaimFile(Float id) {
		return onlineChangeDao.getInsuranceClaimFile(id);
    }

    @Override
    public MedicalTreatmentClaimFileDataVo getMedicalInfoClaimFile(Float id) {
		return onlineChangeDao.getMedicalInfoClaimFile(id);
    }

	@Override
	public MedicalTreatmentClaimFileDataVo getMedicalInfoClaimFileData(Float id) {
		return onlineChangeDao.getMedicalInfoClaimFileData(id);
	}
}
