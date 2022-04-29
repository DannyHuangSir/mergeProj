package com.twfhclife.generic.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import com.twfhclife.eservice.onlineChange.model.TransMedicalTreatmentClaimVo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimVo;
 
public class RptUtils2 {
 
    // main測試
    public static void main(String[] args) throws Exception {
    	 FileOutputStream fout = null;
		try {
			  
			TransInsuranceClaimVo claimVo = new TransInsuranceClaimVo();
			claimVo.setTransNum("1034332344");
			claimVo.setApplicationItem("醫療保險");
			claimVo.setApplicationDate("2012-11-21");
			claimVo.setPolicyNo("1023822");
            String filename = "C:\\D\\PDFDemo.pdf";
	        fout = new FileOutputStream(filename);
            // 將字節寫入文件
            fout.write(new RptUtils2().generatePDF(claimVo));
            fout.close();
		 } catch (Exception e) {
            e.printStackTrace();
         } finally {
        	 if(fout != null) {
        		 fout.close();
        	 }
		}
    }
 
    // 定義全局的字體靜態變量
	private static Font titlefont;
	private static Font keyfont;
    // 最大寬度
	private static int maxWidth = 700;
	// 靜態代碼塊
    static {
        try {
            // 不同字體（這裏定義為同一種字體：包含不同字號、不同style）
        	Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
        	String kaiuFontPath = kaiuResource.getFile().getPath();
            BaseFont bfChinese = BaseFont.createFont(kaiuFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            titlefont = new Font(bfChinese, 25, Font.BOLD);
            keyfont = new Font(bfChinese, 14, Font.NORMAL);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 生成PDF文件
	public byte[] generatePDF(TransInsuranceClaimVo claimVo) throws Exception {
		 // 1.新建document對象
        Document document = new Document(PageSize.A4);
        
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
        PdfWriter  writer = PdfWriter.getInstance(document, baos);
        document.open();
        
    	// 段落
		Paragraph paragraph = new Paragraph("紙本文件寄回檢核表 ", titlefont);
		paragraph.setAlignment(1); // 設置文字居中 0靠左   1，居中     2，靠右
		paragraph.setIndentationLeft(12); // 設置左縮進
		paragraph.setIndentationRight(12); // 設置右縮進
		paragraph.setFirstLineIndent(24); // 設置首行縮進
		paragraph.setLeading(20f); // 行间距
		paragraph.setSpacingBefore(5f); // 設置段落上空白
		paragraph.setSpacingAfter(10f); // 設置段落下空白
 
		
 
		BarcodeUtil butil = new BarcodeUtil();
		PdfContentByte cb = writer.getDirectContent();
		Barcode39 code39 = butil.create("E1100101");
	     
		// 表格
		PdfPTable table = createTable(new float[] {90, 80,40});
		
		table.addCell(createCell("檢核表條碼 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(code39.createImageWithBarcode(cb, null, null),keyfont,Element.ALIGN_CENTER,1,false));
		table.addCell(createCell("", keyfont, Element.ALIGN_CENTER,1,false));
		
		code39 = butil.create(claimVo.getTransNum());
		table.addCell(createCell("理賠申請書代號條碼 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(code39.createImageWithBarcode(cb, null, null),keyfont,Element.ALIGN_CENTER,1,false));
		table.addCell(createCell("", keyfont, Element.ALIGN_CENTER,1,false));

		
		table.addCell(createCell("申請項目 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getApplicationItem(),keyfont,Element.ALIGN_LEFT,2,false));
		
		
		table.addCell(createCell("申請日期 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getApplicationDate(),keyfont,Element.ALIGN_LEFT,2,false));
		
	
		table.addCell(createCell("申請序號 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getTransNum(),keyfont,Element.ALIGN_LEFT,2,false));
		
		table.addCell(createCell("保單編號 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getPolicyNo(),keyfont,Element.ALIGN_LEFT,2,false));

		table.addCell(createCell("保單需寄回的附件清單 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("",keyfont,Element.ALIGN_LEFT,2,false));
		
		if(claimVo.getFileDataList() != null) {
			String[] fileDatas = claimVo.getFileDataList().split(",");
			for (String name : fileDatas) {
				if (name.length()>20) {
					String[] split = name.split("");
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 1; i <= split.length; i++) {
						stringBuffer.append(split[i-1]);
						if (i%20==0) {
				table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
							table.addCell(createCell(stringBuffer.toString(), keyfont, Element.ALIGN_LEFT, 2, false));
							stringBuffer.delete(0,stringBuffer.length());
						}
						if(i >= split.length && stringBuffer.length()>0 && stringBuffer.length()<20){
							table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
							table.addCell(createCell(stringBuffer.toString(), keyfont, Element.ALIGN_LEFT, 2, false));
							stringBuffer.delete(0,stringBuffer.length());
						}
					}
				}else {
					table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
					table.addCell(createCell(name, keyfont, Element.ALIGN_LEFT, 2, false));
				}
			}
		}

		table.addCell(createCell("寄回資訊-收件人 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("臺銀人壽 理賠科",keyfont,Element.ALIGN_LEFT,2,false));

		table.addCell(createCell("寄回資訊-收件地址 : ", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("106 台北市大安區敦化南路二段 69 號 2 樓",keyfont,Element.ALIGN_LEFT,2,false));

		document.add(paragraph);
		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	// 生成PDF文件
	public byte[] generatePDF(TransMedicalTreatmentClaimVo claimVo) throws Exception {
		// 1.新建document對象
		Document document = new Document(PageSize.A4);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter  writer = PdfWriter.getInstance(document, baos);
		document.open();

		// 段落
		Paragraph paragraph = new Paragraph("紙本文件寄回檢核表 ", titlefont);
		paragraph.setAlignment(1); // 設置文字居中 0靠左   1，居中     2，靠右
		paragraph.setIndentationLeft(12); // 設置左縮進
		paragraph.setIndentationRight(12); // 設置右縮進
		paragraph.setFirstLineIndent(24); // 設置首行縮進
		paragraph.setLeading(20f); // 行间距
		paragraph.setSpacingBefore(5f); // 設置段落上空白
		paragraph.setSpacingAfter(10f); // 設置段落下空白



		BarcodeUtil butil = new BarcodeUtil();
		PdfContentByte cb = writer.getDirectContent();
		Barcode39 code39 = butil.create("E1100101");

		// 表格
		PdfPTable table = createTable(new float[] {90, 80,40});

		table.addCell(createCell("檢核表條碼 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(code39.createImageWithBarcode(cb, null, null),keyfont,Element.ALIGN_CENTER,1,false));
		table.addCell(createCell("", keyfont, Element.ALIGN_CENTER,1,false));

		code39 = butil.create(claimVo.getTransNum());
		table.addCell(createCell("理賠申請書代號條碼 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(code39.createImageWithBarcode(cb, null, null),keyfont,Element.ALIGN_CENTER,1,false));
		table.addCell(createCell("", keyfont, Element.ALIGN_CENTER,1,false));


		table.addCell(createCell("申請項目 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getApplicationItem(),keyfont,Element.ALIGN_LEFT,2,false));


		table.addCell(createCell("申請日期 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getApplicationDate(),keyfont,Element.ALIGN_LEFT,2,false));


		table.addCell(createCell("申請序號 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getTransNum(),keyfont,Element.ALIGN_LEFT,2,false));

		table.addCell(createCell("保單編號 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell(claimVo.getPolicyNo(),keyfont,Element.ALIGN_LEFT,2,false));

		table.addCell(createCell("保單需寄回的附件清單 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("",keyfont,Element.ALIGN_LEFT,2,false));

		if(claimVo.getFileDataList() != null) {
			String[] fileDatas = claimVo.getFileDataList().split(",");
			for (String name : fileDatas) {
				if (name.length()>20) {
					String[] split = name.split("");
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 1; i <= split.length; i++) {
						stringBuffer.append(split[i-1]);
						if (i%20==0) {
				table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
							table.addCell(createCell(stringBuffer.toString(), keyfont, Element.ALIGN_LEFT, 2, false));
							stringBuffer.delete(0,stringBuffer.length());
						}
						if(i >= split.length && stringBuffer.length()>0 && stringBuffer.length()<20){
							table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
							table.addCell(createCell(stringBuffer.toString(), keyfont, Element.ALIGN_LEFT, 2, false));
							stringBuffer.delete(0,stringBuffer.length());
						}
					}
				}else {
					table.addCell(createCell("", keyfont, Element.ALIGN_RIGHT,1,false));
					table.addCell(createCell(name, keyfont, Element.ALIGN_LEFT, 2, false));
				}
			}
		}

		table.addCell(createCell("寄回資訊-收件人 :", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("臺銀人壽 理賠科",keyfont,Element.ALIGN_LEFT,2,false));

		table.addCell(createCell("寄回資訊-收件地址 : ", keyfont, Element.ALIGN_RIGHT,1,false));
		table.addCell(createCell("106 台北市大安區敦化南路二段 69 號 2 樓",keyfont,Element.ALIGN_LEFT,2,false));

		document.add(paragraph);
		document.add(table);
		document.close();
		return baos.toByteArray();
	}


/**------------------------創建表格單元格的方法start----------------------------*/
    /**
     * 創建單元格(指定字體)
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 創建單元格（指定字體、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
	public PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
    /**
     * 創建單元格（指定字體、水平居..、單元格跨x列合並）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    
    public PdfPCell createCell(Image image, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setImage(image);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 創建單元格（指定字體、水平居..、單元格跨x列合並、設置單元格內邊距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 創建單元格（指定字體、水平..、邊框寬度：0表示無邊框、內邊距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
	public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		cell.setBorderWidthLeft(borderWidth[0]);
		cell.setBorderWidthRight(borderWidth[1]);
		cell.setBorderWidthTop(borderWidth[2]);
		cell.setBorderWidthBottom(borderWidth[3]);
		cell.setPaddingTop(paddingSize[0]);
		cell.setPaddingBottom(paddingSize[1]);
		if (flag) {
			cell.setColspan(2);
		}
		return cell;
	}
/**------------------------創建表格單元格的方法end----------------------------*/
 
 
/**--------------------------創建表格的方法start------------------- ---------*/
    /**
     * 創建默認列寬，指定列數、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
	public PdfPTable createTable(int colNumber, int align) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(align);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 創建指定列寬、列數的表格
     * @param widths
     * @return
     */
	public PdfPTable createTable(float[] widths) {
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 創建空白的表格
     * @return
     */
	public PdfPTable createBlankTable() {
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.addCell(createCell("", keyfont));
		table.setSpacingAfter(20.0f);
		table.setSpacingBefore(20.0f);
		return table;
	}
/**--------------------------創建表格的方法end------------------- ---------*/
 
 
}
