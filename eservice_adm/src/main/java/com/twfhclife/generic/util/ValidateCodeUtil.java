package com.twfhclife.generic.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class ValidateCodeUtil {
	private static final Logger logger = LogManager.getLogger(ValidateCodeUtil.class);
	// 圖片的寬度。
	private int width = 160;
	// 圖片的高度。
	private int height = 40;
	// 驗證碼字符個數
	private int codeCount = 5;
	// 驗證碼干擾線數
	private int lineCount = 150;
	// 驗證碼
	private String code = null;
	// 驗證碼圖片Buffer
	private BufferedImage buffImg = null;

	/*
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	*/		

	private char[] codeSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public ValidateCodeUtil() {
		this.createCode();
	}

	/**
	 * @param width 圖片寬
	 * @param height 圖片高
	 */
	public ValidateCodeUtil(int width, int height) {
		this.width = width;
		this.height = height;
		this.createCode();
	}

	/**
	 * @param width 圖片寬
	 * @param height 圖片高
	 * @param codeCount 字符個數
	 * @param lineCount 干擾線條數
	 */
	public ValidateCodeUtil(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		this.createCode();
	}

	public void createCode() {
		int x = 0, fontHeight = 0, codeY = 0;
		int red = 0, green = 0, blue = 0;

		x = width / (codeCount + 2); // 每個字符的寬度
		fontHeight = height - 4;     // 字體的高度
		codeY = height - 4;

		// 圖像buffer
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 生成隨機數
		Random random;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		// 將圖像填充為白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 創建字體
		ImgFontByte imgFont = new ImgFontByte();
		Font font = imgFont.getFont(fontHeight);
		g.setFont(font);

		for (int i = 0; i < lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawLine(xs, ys, xe, ye);
		}

		// randomCode記錄隨機產生的驗證碼
		StringBuffer randomCode = new StringBuffer();
		// 隨機產生codeCount個字符的驗證碼。
		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			// 產生隨機的顏色值，讓輸出的每個字符的顏色值都將不同
			/*
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			*/
			g.setColor(new Color(0, 0, 0));
			g.drawString(strRand, (i + 1) * x, codeY);
			// 將產生的四個隨機數組合在一起。
			randomCode.append(strRand);
		}
		// 將四位數字的驗證碼保存到Session中。
		code = randomCode.toString();
	}

	public void write(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		this.write(sos);
	}

	public void write(OutputStream sos) throws IOException {
		ImageIO.write(buffImg, "png", sos);
		sos.close();
	}
	
	public String imgToBase64String() {
		String imgBase64 = "";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(buffImg, "png", Base64.getEncoder().wrap(os));
			imgBase64 = os.toString("UTF-8");
		} catch (final IOException ioe) {
			logger.error("ValidateCodeUtil.imgToBase64String: {}", ExceptionUtils.getStackTrace(ioe));
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				logger.warn("Unable to close ByteArrayOutputStream: {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return imgBase64;
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}

	public String getCode() {
		return code;
	}
}
