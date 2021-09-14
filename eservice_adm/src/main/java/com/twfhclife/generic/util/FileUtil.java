package com.twfhclife.generic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	private static final Logger logger = LogManager.getLogger(FileUtil.class);
	
	public static File convertMultipart(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = null;
		try {
			if (convFile.createNewFile()) {
				fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
			}
		} catch (IOException ioe) {
			logger.error("Unable to convertMultipart: {}", ExceptionUtils.getStackTrace(ioe));
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("Unable to close fileOutputStream: {}", ExceptionUtils.getStackTrace(e));
				}
			}
		}

		return convFile;
	}

	public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
		File convFile = new File(multipart.getOriginalFilename());
		try {
			multipart.transferTo(convFile);
		} catch (Exception e) {
			logger.error("Unable to multipartToFile: {}", ExceptionUtils.getStackTrace(e));
		}
		return convFile;
	}
}
