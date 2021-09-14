package com.twfhclife.eservice_batch.util;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipperUtil {
	private String password;
	public static final String EXTENSION = "zip";

	public ZipperUtil(String password) {
		this.password = password;
	}

	public void pack(String filePath, String zipFileSource, String zipNameDest) throws ZipException {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
		zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
		zipParameters.setPassword(password);
		String baseFileName = filePath + zipFileSource;
		String destinationZipFilePath = filePath + zipNameDest + "." + EXTENSION;
		ZipFile zipFile = new ZipFile(destinationZipFilePath);
		zipFile.addFile(new File(baseFileName), zipParameters);
	}

	public void unpack(String sourceZipFilePath, String extractedZipFilePath)
			throws ZipException {
		ZipFile zipFile = new ZipFile(sourceZipFilePath + "." + EXTENSION);

		if (zipFile.isEncrypted()) {
			zipFile.setPassword(password);
		}

		zipFile.extractAll(extractedZipFilePath);
	}

	public static void main(String[] args) throws ZipException {
		ZipperUtil zip = new ZipperUtil("123456");
		zip.pack("C:\\home\\eservice_batch\\logs\\", "batch_log.log", "batchLog");
	}
}
