package com.twfhclife.generic.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.twfhclife.eservice.onlineChange.model.TravelPlanVo;
import com.twfhclife.eservice.onlineChange.model.TravelPolicyVo;
import com.twfhclife.eservice.onlineChange.service.impl.TravelPolicyServiceImpl;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.InsuredVo;
import com.twfhclife.eservice.web.model.ProposerVo;

/**
 * 報表工具類
 */
public class RptUtils {

	private static final Logger logger = LogManager.getLogger(RptUtils.class);

	private BaseFont kaiuBaseFont;

	private byte[] srcPdfBytes;

	private List<Map<String, Map<String, Float>>> appendTextList = new ArrayList<>();
	
	private List<Map<String, Object>> appendHardNameList = new ArrayList<>();

	/**
	 * 初始化.
	 * 
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public RptUtils(String templateSrcPdfName) throws IOException, DocumentException {
		Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
		Resource templateResource = new ClassPathResource("ftl/" + templateSrcPdfName);
		
		String kaiuFontPath = kaiuResource.getFile().getPath(); // 標楷體字型檔位置
		BaseFont kaiuBaseFont = BaseFont.createFont(kaiuFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		this.setKaiuBaseFont(kaiuBaseFont);

		if (templateResource.exists()) {
			File pdfFile = templateResource.getFile();
			logger.info("Find template PDF file on [{}]", pdfFile.getPath());
			
			byte[] pdfBytes = FileUtils.readFileToByteArray(templateResource.getFile());
			this.setSrcPdfBytes(pdfBytes);
		} else {
			logger.error("Unable to find template PDF file: {}", templateSrcPdfName);
			throw new IOException(String.format("Unable to find template PDF file: %s", templateSrcPdfName));
		}
	}

	/**
	 * 添加報表文字.
	 * 
	 * @param text 文字內容
	 * @param size 文字大小
	 * @param page 頁數
	 * @param left 文字左邊位置
	 * @param top 文字上方位置
	 */
	public void txt(String text, int size, int page, float left, float top) {
		if (!StringUtils.isEmpty(text)) {
			Map<String, Float> setCfg = new HashMap<>();
			setCfg.put("PAGE", new Float(page));
			setCfg.put("SIZE", new Float(size));
			setCfg.put("LEFT", left);
			setCfg.put("TOP", top);

			Map<String, Map<String, Float>> textCfg = new HashMap<>();
			textCfg.put(text, setCfg);

			appendTextList.add(textCfg);
		}
	}
	
	public void breakTxt(String text, int size, int page, float left, float top, int breakNum) {
		String breakLineSymbol = "@breakLine@";
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(text)) {
			for (int i = 0; i < text.length(); i++) {
				sb.append(text.charAt(i));
				// 達到斷行字數
				if ((i + 1) % breakNum == 0) {
					sb.append(breakLineSymbol);
				}
			}
		}
		
		String[] newTxtList = sb.toString().split(breakLineSymbol);
		for (int i = 0; i < newTxtList.length; i++) {
			Map<String, Float> setCfg = new HashMap<>();
			setCfg.put("PAGE", new Float(page));
			setCfg.put("SIZE", new Float(size));
			setCfg.put("LEFT", left);
			
			// 高度為 top 減去字型大小的高度
			setCfg.put("TOP", top - (i * size));
			
			Map<String, Map<String, Float>> textCfg = new HashMap<>();
			textCfg.put(newTxtList[i], setCfg);

			appendTextList.add(textCfg);
		}
	}
	
	/**
	 *  難字處理在指定頁數&位置
	 * @param image
	 * @param size
	 * @param page
	 * @param left
	 * @param top
	 * @return
	 * @throws Exception
	 */
	public void hardName(String base64Str, int size, int page, float left, float top) {
		try {
			if (base64Str != null && base64Str.length() > 0) {
				Map<String, Float> setCfg = new HashMap<>();
				setCfg.put("PAGE", new Float(page));
				setCfg.put("SIZE", new Float(size));
				setCfg.put("LEFT", left);
				setCfg.put("TOP", top);
				
				Map<String, Object> hardNameCfg = new HashMap<>();
				hardNameCfg.put("PARAM", setCfg);
				hardNameCfg.put("SOURCE", base64Str);
				
				appendHardNameList.add(hardNameCfg);
			}
		} catch(Exception e) {
			logger.error("Unable to image: {}", e);
		}
	}
	
	/**
	 * 匯出.
	 * 
	 * @throws IOException 
	 * @throws DocumentException
	 */
	public byte[] toPdf() throws DocumentException, IOException {
		ByteArrayOutputStream baosStamper = null;
		PdfStamper stamper = null;
		byte[] pdfByte = null;
		PdfReader pr = null;
		try {
			pr = new PdfReader(getSrcPdfBytes());
			baosStamper = new ByteArrayOutputStream();
			stamper = new PdfStamper(pr, baosStamper);

			for (Map<String, Map<String, Float>> textCfg : appendTextList) {
				List<String> textList =  new ArrayList<>(textCfg.keySet());
				String text =  textList.get(0);
				Map<String, Float> setCfg = textCfg.get(text);
				float left = setCfg.get("LEFT");
				float top = setCfg.get("TOP");
				float fontSize = setCfg.get("SIZE");
				
				PdfContentByte upCb = stamper.getOverContent(setCfg.get("PAGE").intValue());
				upCb.beginText();
				upCb.setFontAndSize(getKaiuBaseFont(), fontSize);
				upCb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, left, top, 0);
				upCb.endText();
			}
			// 解難字問題
			for(Map<String, Object> imageCfg: appendHardNameList) {
				String sourceKey = "SOURCE";
				String paramKey = "PARAM";
				Map<String, Float> setCfg = (Map<String, Float>)imageCfg.get(paramKey);
				float left = setCfg.get("LEFT");
				float top = setCfg.get("TOP");
				float fontSize = setCfg.get("SIZE");
				String strUnicode = (String) imageCfg.get(sourceKey);
				// 轉換難字
				PdfContentByte upCb = stamper.getOverContent(setCfg.get("PAGE").intValue());
				upCb.beginText();
				try {
					StringBuilder sb = new StringBuilder();
					int i = -1;
					int pos = 0;

					while ((i = strUnicode.indexOf("\\u", pos)) != -1) {
						sb.append(strUnicode.substring(pos, i));
						if (i + 5 < strUnicode.length()) {
							pos = i + 6;
							sb.append((char) Integer.parseInt(strUnicode.substring(i + 2, i + 6), 16));
						}
					}
					upCb.setFontAndSize(getKaiuBaseFont(), fontSize);
					upCb.showTextAligned(PdfContentByte.ALIGN_LEFT, sb.toString(), left, top, 0);
					
				} finally {
					upCb.endText();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (stamper != null) stamper.close();
			if (pr != null) {
				pr.close();
			}
			if (baosStamper != null) {
				pdfByte = baosStamper.toByteArray();
			}
			IOUtils.closeQuietly(baosStamper);
		}
		return pdfByte;
	}

	public BaseFont getKaiuBaseFont() {
		return kaiuBaseFont;
	}

	public void setKaiuBaseFont(BaseFont kaiuBaseFont) {
		this.kaiuBaseFont = kaiuBaseFont;
	}

	public byte[] getSrcPdfBytes() {
		return srcPdfBytes;
	}

	public void setSrcPdfBytes(byte[] srcPdfBytes) {
		this.srcPdfBytes = srcPdfBytes;
	}

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			ClaimVo claimVo = new ClaimVo();
//			
//			List<String> applyItemList = new ArrayList<>();
//			applyItemList.add("01");
//			applyItemList.add("02");
//			applyItemList.add("03");
//			applyItemList.add("04");
//			applyItemList.add("05");
//			applyItemList.add("06");
//			applyItemList.add("07");
//			applyItemList.add("08");
//			
//			// test data
//			claimVo.setApplyItemList(applyItemList);
//			claimVo.setPolicyNo("TEST000001");
//			claimVo.setCustomerName("陳文倫");
//			claimVo.setItemOther("測試項目");
//			claimVo.setInsuredName("陳文倫之子");
//			claimVo.setRelation("04");
//			claimVo.setRocId("A123456789");
//			claimVo.setTel1("02-12345678");
//			claimVo.setBirthDate("1900/01/01");
//			claimVo.setMobile("0912345678");
//			claimVo.setJob("測試職業");
//			claimVo.setAddr("台北市................");
//			claimVo.setDiseaseName("測試病名測試病名測試病名測試病名測試病名");
//			claimVo.setDiagnosisDate("2018/01/01");
//			claimVo.setHospital("台大醫院");
//			claimVo.setAccidentYear("101");
//			claimVo.setAccidentMonth("11");
//			claimVo.setAccidentDay("21");
//			claimVo.setAccidentHour("12");
//			claimVo.setAccidentLocation("測試地點");
//			claimVo.setAccidentHandleUnit("測試單位");
//			claimVo.setPoliceName("警察1");
//			claimVo.setPoliceTel("0933123456");
//			claimVo.setAccidentReason("爬樓梯下到B1時跌倒導致左手小指指甲斷掉,有叫救護車送醫,醫生說這個很嚴重如果有保險可以申請理賠,有開立診斷證明");
//			claimVo.setOtherCompanyInsured("N");
//			claimVo.setOtherInsuCompany("保險公司");
//			claimVo.setOtherInsuType("保險種類");
//			claimVo.setOtherInsuAmout("12345");
//			claimVo.setOtherInsuClaim("N");
//			claimVo.setPaymentMethod("02");
//			claimVo.setAccountName("陳文倫");
//			claimVo.setBankName("測試銀行");
//			claimVo.setBranchName("測試分行");
//			claimVo.setAccountNumber("12345678901234567890");
//			claimVo.setPostalCode("012345");
//			claimVo.setPostalAddr("台北市松山區................");
//			
//			ClaimServiceImpl claimService = new ClaimServiceImpl();
//			byte[] pdfByte = claimService.getPDF(claimVo);

			TravelPolicyVo travelPolicyVo = new TravelPolicyVo();
			// 保險始期
			travelPolicyVo.setBeginDate("2018/07/10");
			travelPolicyVo.setBeginTime("00");
			// 保險終止
			travelPolicyVo.setEndDate("2018/07/30");
			travelPolicyVo.setEndTime("00");
			// 旅行天數
			travelPolicyVo.setTravelDay(10);
			// 其他保險公司名稱
			travelPolicyVo.setOtherTravelCompany("南山人壽");
			// 其他保險金額
			travelPolicyVo.setOtherAmtWrap(new BigDecimal(120));
			
			
			// 要保人
			ProposerVo policyHolder = new ProposerVo();
			// 姓名
			policyHolder.setProposer("要保人");
			// 地址
			policyHolder.setAddress("台北市大安區敦化南路二段69號B1");
			policyHolder.setCountry("中華民國");
			policyHolder.setBirthday(new Date());
			policyHolder.setIdentityId("A123456789");
			policyHolder.setCellphoneNum("0987654321");
			policyHolder.setEmail("test@gmail.com");
			

			// 被保人
			InsuredVo insured = new InsuredVo();
			// 姓名
			insured.setInsuredName("被保人"); 
			// 生日
			insured.setBirthday(new Date());
			//歲數
			TravelPlanVo travelPlanVo = new TravelPlanVo();
			travelPlanVo.setAge(20);
			// 電話
			insured.setHomeTel("0987654321"); 
			// 身分證字號
			insured.setIdentityId("A123456789"); 
			// 職業
			insured.setJob("student"); 
			// 住址
			insured.setAddress("台北市大安區敦化南路二段69號B1"); 
			insured.setRelationStr("本人");
			insured.setWnpiAnnounce("0");
			insured.setCellphoneNum("0987654321");
			
			// 受益人
			BeneficiaryVo beneficiary = new BeneficiaryVo();
			// 姓名
			beneficiary.setBeneficiaryName("受益人"); 
			// 身分證字號
			beneficiary.setBeneficiaryRocid("B123456789"); 
			// 與被保人關係
			beneficiary.setBeneficiaryRelationStr("小三"); 
			// 住址
			beneficiary.setBeneficiaryAddress("台北市大安區111敦化南路二段69號B1"); 
			// 電話
			beneficiary.setBeneficiaryTel("0988765432"); 
			beneficiary.setCountry("中華民國");
			beneficiary.setBeniBirthday(new Date());
			beneficiary.setAllocateType(0);
			
			// 旅行地點
			travelPolicyVo.setTravelDestType("國外"); 
			travelPolicyVo.setTravelDest("日本"); 

			// 旅平險保單方案內容
			TravelPlanVo travelPlan = travelPolicyVo.getTravelPlan();
			// 保險金額
			travelPlanVo.setAddid(new BigDecimal(333));
			travelPlanVo.setAddidPremium(new BigDecimal(22));
			// 保險費合計
			travelPlanVo.setPremium(new BigDecimal(999));
			travelPlanVo.setMrid(new BigDecimal(11));
			travelPlanVo.setOrid(new BigDecimal(55));
			travelPlanVo.setMridPremium(new BigDecimal(1));
			travelPlanVo.setOridPremium(new BigDecimal(5));

			travelPolicyVo.setTravelPlan(travelPlanVo);
			travelPolicyVo.setInsured(insured);
			travelPolicyVo.setBeneficiary(beneficiary);
			travelPolicyVo.setProposer(policyHolder);
			
			TravelPolicyServiceImpl travelPolicyService = new TravelPolicyServiceImpl();
			byte[] pdfByte = travelPolicyService.getPDF(travelPolicyVo);
			FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\evanl\\Desktop\\新增資料夾\\旅平險要保書.pdf"));
			IOUtils.write(pdfByte, fos);
			IOUtils.closeQuietly(fos);
			
			
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
}