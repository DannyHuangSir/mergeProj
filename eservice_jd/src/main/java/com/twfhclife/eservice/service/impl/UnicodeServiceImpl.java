package com.twfhclife.eservice.service.impl;

import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.web.dao.UnicodeBaseDao;
import com.twfhclife.eservice.web.model.UnicodeBaseVo;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Base64;

@Service
public class UnicodeServiceImpl implements IUnicodeService {
	
	private static final Logger logger = LogManager.getLogger(UnicodeServiceImpl.class);
	
	@Autowired
	private UnicodeBaseDao unicodeBaseDao;

	@Override
	public String convertString2Unicode(String name) {

		try {
			if (name == null) {
				return "";
			}
			
			boolean isConvert = false;
			String uniCode = "";
			for (int i = 0; i < name.length(); i++) {
				// 取出每一個字符
                char c = name.charAt(i);
				if ("?".equals(new String(String.valueOf(c).getBytes("big5")))) {
					isConvert = true;
					UnicodeBaseVo vo = unicodeBaseDao.getUnicodeBaseByName(String.valueOf(c));
					if (vo == null) {
						continue;
					}
					uniCode += "\\u" + vo.getUtf16Code();
				} else {
					uniCode += "\\u" + Integer.toHexString(c);
				}
			}

			// 沒有難字,直接回傳
			if (!isConvert || "".equals(uniCode) || "null".equalsIgnoreCase(uniCode)) {
				return "";
			}

			return getSvgBase64(uniCode);
		} catch (Exception e) {
			logger.error("convertString2Unicode: {}", ExceptionUtils.getStackTrace(e));
			return name;
		}
	}

	@Override
	public String convertString2UnicodeNonImage(String name) {
		try {
			if (name == null) {
				return "";
			}
			
			boolean isConvert = false;
			String uniCode = "";
			for (int i = 0; i < name.length(); i++) {
				// 取出每一個字符
                char c = name.charAt(i);
				if ("?".equals(new String(String.valueOf(c).getBytes("big5")))) {
					isConvert = true;
					UnicodeBaseVo vo = unicodeBaseDao.getUnicodeBaseByName(String.valueOf(c));
					if (vo == null) {
						continue;
					}
					uniCode += "\\u" + vo.getUtf16Code();
				} else {
					uniCode += "\\u" + Integer.toHexString(c);
				}
			}

			// 沒有難字,直接回傳
			if (!isConvert || "".equals(uniCode)) {
				return "";
			}

			return uniCode;
		} catch (Exception e) {
			logger.error("convertString2Unicode: {}", ExceptionUtils.getStackTrace(e));
			return name;
		}
	}

	private String getSvgBase64(String name) throws Exception {
		name = name.replaceAll("\\\\unull", "");
		String base64Image = "";
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = name.indexOf("\\u", pos)) != -1) {
			sb.append(name.substring(pos, i));
			if (i + 5 < name.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(name.substring(i + 2, i + 6), 16));
			}
		}

		// createGraphics
		Graphics2D g = svgGenerator;

		// 設定字型顏色 =&gt; BLACK
		g.setColor(Color.BLACK);
		
		Font font = new Font(Font.DIALOG, Font.PLAIN, 15);
		g.setFont(font);
		// 把文字draw到圖片上
		g.drawString(sb.toString(), 10, 15);

		g.dispose();
		
		boolean useCSS = true;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(bos,"UTF-8");
			svgGenerator.stream(out, useCSS);
			base64Image = Base64.getEncoder().encodeToString(bos.toByteArray());
		} catch (Exception e) {
			logger.error("Unable to getBase64Image: {}", ExceptionUtils.getStackTrace(e));
		}
		return base64Image;
	}
	
	public static void main(String[] args) throws Exception{
		String name = "鑫珮瀞媖姍堃";
		System.out.println(name.toCharArray());
		
		System.out.println(new String(name.getBytes(), java.nio.charset.StandardCharsets.UTF_8));
		
//		String strValueOf = null;
//		for(char c : name.toCharArray()) {
//			strValueOf = new String(String.valueOf(c).getBytes("big5"));
//			System.out.println(strValueOf);
//		}
		
	}
	
}
