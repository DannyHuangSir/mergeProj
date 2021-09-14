package com.twfhclife.eservice_batch.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class FileUtil {

	public static boolean moveFile(String sourcePath, String targetPath) {
		boolean fileMoved = true;
		try {
			int a = targetPath.lastIndexOf("/");
			int b = targetPath.lastIndexOf("\\");
			File f = new File(targetPath.substring(0, a != -1 ? a : b));
			if (!f.exists()) {
				f.mkdir();
			}
			
			Files.move(Paths.get(sourcePath), Paths.get(targetPath),StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			fileMoved = false;
		}
		return fileMoved;
	}
	
	public static boolean forceDeleteDir(String dir) {
		boolean result = false;
		Path directory = Paths.get(dir);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				   @Override
				   public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
				       Files.delete(file); // this will work because it's always a File
				       return FileVisitResult.CONTINUE;
				   }

				   @Override
				   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				       Files.delete(dir); //this will work because Files in the directory are already deleted
				       return FileVisitResult.CONTINUE;
				   }
				});
			result = true;
		} catch (IOException e) {
			result = false;
		}
		return result;
	}
	
	public static String sha1(File file) throws NoSuchAlgorithmException,
			IOException {
		final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			byte[] buffer = new byte[1024];
			for (int read = 0; (read = is.read(buffer)) != -1;) {
				messageDigest.update(buffer, 0, read);
			}
		}

		// Convert the byte to hex format
		try (Formatter formatter = new Formatter()) {
			for (final byte b : messageDigest.digest()) {
				formatter.format("%02x", b);
			}
			return formatter.toString();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
