package com.twfhclife.eservice.util;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RptUtils2 {
 

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

/**------------------------創建表格單元格的方法start----------------------------*/
    /**
     * 創建單元格(指定字體)
     * @param value
     * @param font
     * @return
     */
    public static PdfPCell createCell(String value, Font font) {
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
	public static PdfPCell createCell(String value, Font font, int align) {
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
    public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        if (!boderFlag) {
            cell.setBorder(0);
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
