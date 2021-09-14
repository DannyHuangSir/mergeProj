package com.twfhclife.eservice_batch.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportExportUtil {

	private static final Logger logger = LogManager.getLogger(ReportExportUtil.class);
	
	private Document pdfDoc = null;
	private BaseFont kaiuBaseFont = null;
	private Font kaiuFont = null;
	private PdfWriter writer = null;
	private ReportExportUtil instance = null;
	private ByteArrayOutputStream baos = null;
	private byte[] pdfBytes = null;
	private List<Map<String, Map<String, Float>>> appendTextList = new ArrayList<>();
	private List<Map<String, Map<String, Float>>> appendTextAlignCenterList = new ArrayList<>();
	private List<Map<String, Float>> drawBorderMapList = new ArrayList<>();
	
	/**
	 * 報表版面初始化設定.
	 * 
	 * @param rectangle 版面設定
	 * @param left 左邊邊界
	 * @param right 右邊邊界
	 * @param top 上邊邊界
	 * @param bottom 下邊邊界
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public void init(Rectangle rectangle, float left, float right, float top, float bottom) throws DocumentException, IOException {
		this.baos = new ByteArrayOutputStream();
		instance = this;
		String fontPath = ReportExportUtil.class.getClassLoader().getResource("font/kaiu.ttf").getPath();
		kaiuBaseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		kaiuFont = new Font(kaiuBaseFont, 12, Font.NORMAL);
		pdfDoc = new Document(rectangle, left, right, top, bottom);
		writer = PdfWriter.getInstance(pdfDoc, baos);
		pdfDoc.open();
	}

	/**
	 * 報表版面預設A4直式.
	 * 
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public ReportExportUtil() throws DocumentException, IOException {
		init(PageSize.A4, 20, 20, 80, 20);
	}
	
	/**
	 * 添加報表文字.
	 * 
	 * @param text 文字內容
	 * @throws DocumentException
	 */
	public ReportExportUtil txt(String text) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell(new Phrase(text, kaiuFont));
		cell.setPadding(0);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);

		table.addCell(cell);
		pdfDoc.add(table);
		return instance;
	}
	
	/**
	 * 添加報表文字.
	 * 使用置中對齊
	 * @param text
	 * @param size
	 * @param page
	 * @param left
	 * @param top
	 */
	public void txtAlignCenter(String text, int size, int page, float left, float top) {
		if (!StringUtils.isEmpty(text)) {
			Map<String, Float> setCfg = new HashMap<>();
			setCfg.put("PAGE", new Float(page));
			setCfg.put("SIZE", new Float(size));
			setCfg.put("LEFT", left);
			setCfg.put("TOP", top);

			Map<String, Map<String, Float>> textCfg = new HashMap<>();
			textCfg.put(text, setCfg);

			appendTextAlignCenterList.add(textCfg);
		}
	}
	
	/**
	 * 關閉Document.
	 */
	public ReportExportUtil closeDoc() {
		if (pdfDoc != null && pdfDoc.isOpen()) {
			try {
				txt(" "); // 確保PDF有內容，才能順利關閉
			} catch (DocumentException e) {
				logger.info("PdfUtil.closeDoc {}", e.getMessage());
			} finally{
				pdfDoc.close();
				pdfBytes = baos.toByteArray();
			} 
		}
		return instance;
	}
	
	/**
	 * 斷行.
	 * 
	 * @throws DocumentException
	 */
	public ReportExportUtil newLine() throws DocumentException {
		pdfDoc.add(new Paragraph(Chunk.NEWLINE));
		return instance;
	}
	
	public ReportExportUtil addReportData(Map<String, Object> mapHeader, List<Map<String, Object>> listData, float size) throws Exception{
		String[] title = (String[])mapHeader.get("TITLE");
		int columnSize = title.length;
		PdfPTable table = new PdfPTable(columnSize);
		table.setWidthPercentage(100f);
		
		int headerRows = 1; // 設定第一列為表頭
		table.setHeaderRows(headerRows);
		
		// 設定表頭寬度
		float[] widths = null;
		widths = new float[columnSize];
		if(mapHeader.get("TITLE_WIDTH") != null) {
			widths = (float[])mapHeader.get("TITLE_WIDTH");
		} else {
			for (int i = 0; i < widths.length; i++) {
				if (i == 0 || i == 1) {
					widths[i] = 1;
				} else {
					widths[i] = 2;
				}
			}			
		}
		table.setWidths(widths);
		
		// 繪製表頭
		for (String cellString : title) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_TOP);
//			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(new BaseColor(195, 205, 195));
			
			Font headerFont= new Font(kaiuBaseFont, size);
			cellString = cellString.replaceAll("\\n", "\n");
			
			Paragraph p = new Paragraph(cellString, headerFont);
			p.setAlignment(Element.ALIGN_CENTER);
			cell.setPhrase(new Phrase(cellString, headerFont));
			
			Float fontSize = p.getFont().getSize();
			Float capHeight = p.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);

			cell.setPaddingBottom(4 * (fontSize - capHeight));
			cell.addElement(p);
			
			table.addCell(cell);
		}

		// 添加資料
		if (listData != null) {
			String[] titleKey = (String[])mapHeader.get("TITLE_KEY");
			for (Map<String, Object> rowMap : listData) {
				//logger.debug("report data row = " + rowMap);
				
				for (String cellKey : titleKey) {
					PdfPCell cell = new PdfPCell();

					int align = Element.ALIGN_CENTER;
					String cellValue = (rowMap.get(cellKey) == null ? "" : rowMap.get(cellKey).toString());

					Font kaiuFontData = new Font(kaiuBaseFont, size);

					Paragraph p = new Paragraph(cellValue, kaiuFontData);
					p.setAlignment(align);

					Float fontSize = p.getFont().getSize();
					Float capHeight = p.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);

					cell.setPaddingBottom(3 * (fontSize - capHeight));
					cell.addElement(p);

					table.addCell(cell);
				}
			}
		}
		
		pdfDoc.add(table);
		
		return instance;
	}
	
	/**
	 * 因應保單價值列印新增
	 * 需要傳入已經帶入格式的字串 & 字型大小
	 * @param listData
	 * @param size
	 * @param left
	 * @param top
	 * @param height
	 * @return
	 * @throws DocumentException
	 */
	public ReportExportUtil addToOneColumnTable(List<String> listData, int size, float left, float top, float height) throws DocumentException {
		Map<String, Float> drawBorderMap = new HashMap<>();
		drawBorderMap.put("TOP", top + height);
		Float lineHeight = top;
		int textLength = 0;
		for(String line: listData) {
			txt(line, size, 1, left, lineHeight);
			lineHeight -= height;
			if(line.length() > textLength) {
				textLength = line.length();
			}
		}
		drawBorderMap.put("TAIL", lineHeight);
		drawBorderMap.put("LEFT", left - 10);
		drawBorderMap.put("RIGHT", left + 410f);
		drawBorderMapList.add(drawBorderMap);
		return instance;
	}
	
	public void saveTo(String targetPath) throws Exception{
		FileOutputStream fs = new FileOutputStream(targetPath);
		fs.write(pdfBytes);
		fs.flush();
		fs.close();
	}
	
	public ReportExportUtil(String templatePdfName) throws IOException, DocumentException {
//		String fontPath = ReportExportUtil.class.getClassLoader().getResource("font/kaiu.ttf").getPath();
		this.kaiuBaseFont = BaseFont.createFont("font/kaiu.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		//sit & prod
		String templatePathProd = System.getProperty("user.dir") + File.separator + "resources/pdfTemplate/" + templatePdfName;
		
		//dev
		String templatePathDev = ReportExportUtil.class.getClassLoader().getResource("pdfTemplate/" + templatePdfName).getPath();
		File fileProd = new File(templatePathProd);
		File fileDev = new File(templatePathDev);
		if (fileProd.exists()) {
			this.pdfBytes = FileUtils.readFileToByteArray(fileProd);
			logger.info("Find template PDF file on [{}]", fileProd.getPath());
		}else if (fileDev.exists()) {
			this.pdfBytes = FileUtils.readFileToByteArray(fileDev);
			logger.info("Find template PDF file on [{}]", fileDev.getPath());
		} else {
			logger.error("Unable to find template PDF file: {}", templatePdfName);
			throw new IOException(String.format("Unable to find template PDF file: %s", templatePdfName));
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

	/**
	 * 匯出.
	 * 
	 * @throws IOException 
	 * @throws DocumentException
	 */
	public void toPdf() throws DocumentException, IOException {
		ByteArrayOutputStream baosStamper = null;
		PdfStamper stamper = null;
		PdfReader pr = null;
		try {
			pr = new PdfReader(this.pdfBytes);
			baosStamper = new ByteArrayOutputStream();
			stamper = new PdfStamper(pr, baosStamper);

			for (Map<String, Map<String, Float>> textCfg : appendTextList) {
				List<String> textList =  new ArrayList<>(textCfg.keySet());
				String text = textList.get(0);
				Map<String, Float> setCfg = textCfg.get(text);
				float left = setCfg.get("LEFT");
				float top = setCfg.get("TOP");
				float fontSize = setCfg.get("SIZE");
				
				PdfContentByte upCb = stamper.getOverContent(setCfg.get("PAGE").intValue());
				upCb.beginText();
				upCb.setFontAndSize(kaiuBaseFont, fontSize);
				upCb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, left, top, 0);
				upCb.endText();
			}
			for (Map<String, Map<String, Float>> textCfg: appendTextAlignCenterList) {
				List<String> textList =  new ArrayList<>(textCfg.keySet());
				String text =  textList.get(0);
				Map<String, Float> setCfg = textCfg.get(text);
				float left = setCfg.get("LEFT");
				float top = setCfg.get("TOP");
				float fontSize = setCfg.get("SIZE");
				
				PdfContentByte upCb = stamper.getOverContent(setCfg.get("PAGE").intValue());
				upCb.beginText();
				upCb.setFontAndSize(kaiuBaseFont, fontSize);
				upCb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, left, top, 0);
				upCb.endText();
			}
			
			// 畫出框線
			if(!drawBorderMapList.isEmpty()) {
				for(Map<String, Float> drawBorderMap: drawBorderMapList) {
					PdfContentByte canvas = stamper.getOverContent(1);
					BaseColor baseColor = new BaseColor(0, 0, 0);
					Float top = drawBorderMap.get("TOP");
					Float tail = drawBorderMap.get("TAIL");
					Float left = drawBorderMap.get("LEFT");
					Float right = drawBorderMap.get("RIGHT");
					canvas.setColorStroke(baseColor);
					canvas.moveTo(left, top);
					canvas.lineTo(left, tail);
					canvas.lineTo(right, tail);
					canvas.lineTo(right, top);
					canvas.lineTo(left, top);
					canvas.closePathStroke();
				}
			}
			
			// 解難字問題
//			for(Map<String, Object> imageCfg: appendHardNameList) {
//				String sourceKey = "SOURCE";
//				String paramKey = "PARAM";
//				Map<String, Float> setCfg = (Map<String, Float>)imageCfg.get(paramKey);
//				float left = setCfg.get("LEFT");
//				float top = setCfg.get("TOP");
//				float fontSize = setCfg.get("SIZE");
//				String strUnicode = (String) imageCfg.get(sourceKey);
//				// 轉換難字
//				PdfContentByte upCb = stamper.getOverContent(setCfg.get("PAGE").intValue());
//				upCb.beginText();
//				try {
//					StringBuilder sb = new StringBuilder();
//					int i = -1;
//					int pos = 0;
//
//					while ((i = strUnicode.indexOf("\\u", pos)) != -1) {
//						sb.append(strUnicode.substring(pos, i));
//						if (i + 5 < strUnicode.length()) {
//							pos = i + 6;
//							sb.append((char) Integer.parseInt(strUnicode.substring(i + 2, i + 6), 16));
//						}
//					}
//					upCb.setFontAndSize(getKaiuBaseFont(), fontSize);
//					upCb.showTextAligned(PdfContentByte.ALIGN_LEFT, sb.toString(), left, top, 0);
//					
//				} finally {
//					upCb.endText();
//				}
//			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (stamper != null) stamper.close();
			if (pr != null) {
				pr.close();
			}
			if (baosStamper != null) {
				this.pdfBytes = baosStamper.toByteArray();
			}
			IOUtils.closeQuietly(baosStamper);
		}
	}

	public byte[] getPdfBytes() {
		return pdfBytes;
	}

	public void setPdfBytes(byte[] pdfBytes) {
		this.pdfBytes = pdfBytes;
	}

	public static void main(String[] args) {
//		try {
//			List<TransEndorsementVo> list = new ArrayList<TransEndorsementVo>();
//			TransEndorsementVo vo = new TransEndorsementVo();
//			vo.setTextContent("保單號碼 EE10065162");
//			vo.setTextOrder("1");
//			vo.setTransNum("201811230006");
//			list.add(vo);
//			vo = new TransEndorsementVo();
//			vo.setTextContent("茲經通知並經雙方同意本保單自108年08月15日起變更如下");
//			vo.setTextOrder("2");
//			vo.setTransNum("201811230006");
//			list.add(vo);
//			vo = new TransEndorsementVo();
//			vo.setTextContent("繳別變更為 季繳");
//			vo.setTextOrder("3");
//			vo.setTransNum("201811230006");
//			list.add(vo);
//			vo = new TransEndorsementVo();
//			vo.setTextContent("主約每期保費變更為	$43,885");
//			vo.setTextOrder("4");
//			vo.setTransNum("201811230006");
//			list.add(vo);
//			vo = new TransEndorsementVo();
//			vo.setTextContent("	其餘並無變動特此批註	107/11/26");
//			vo.setTextOrder("5");
//			vo.setTransNum("201811230006");
//			list.add(vo);
//			
//			Collections.sort(list, new Comparator<TransEndorsementVo>() {
//			    @Override
//			    public int compare(TransEndorsementVo v1, TransEndorsementVo v2) {
//			        return v2.getTextOrder().compareTo(v1.getTextOrder());
//			    }
//			});
//			
//			ReportExportUtil pdf = new ReportExportUtil("endorsement.pdf");
//			int offset = 15;
//			for (int i=0; i<list.size(); i++) {
//				TransEndorsementVo vo1 = list.get(i);
//				pdf.txt(vo1.getTextContent(), 12, 1, 150, 420 + (offset * (i + 1)));
//			}
//			pdf.toPdf();
//			
//			FileOutputStream fos = new FileOutputStream(new File("C:/tmp/endorsement.pdf"));
//			IOUtils.write(pdf.pdfBytes, fos);
//			IOUtils.closeQuietly(fos);
//						
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
}
