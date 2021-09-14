package com.twfhclife.generic.util;

import com.itextpdf.text.pdf.Barcode39;

public class BarcodeUtil {
	
	/**
	 * create code39 barcode
	 * @param content
	 * @return Barcode39
	 */
	public Barcode39 create(String content) {
		Barcode39 code39 = null;
		if(content==null || "".trim().equals(content)) {
			return null;
		}
		
		code39 = new Barcode39();
		code39.setCode(content);

		//是否需要前後*字號
		//code39.setStartStopText(false);
		//code39.setExtended(true);
		
		return code39;
	}
	


	public static void main(String[] args) throws Exception{
		BarcodeUtil butil = new BarcodeUtil();
		
		String fileName = "D:\\temp\\samplePDF_barcode2.pdf";
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(new com.itextpdf.text.Rectangle(340, 842));
		com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileName));
		document.open();
		com.itextpdf.text.pdf.PdfContentByte cb = writer.getDirectContent();
		
		Barcode39 code39 = butil.create("E1100101");
		document.add(code39.createImageWithBarcode(cb, null, null));
		
		document.close();
		
		
	}
}
