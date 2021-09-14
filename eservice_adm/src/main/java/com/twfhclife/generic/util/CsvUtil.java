package com.twfhclife.generic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CsvUtil {
	private static final Logger logger = LogManager.getLogger(CsvUtil.class);

	public static List<Map<String, String>> commonCsvReader(File csvfile, String[] headers) throws IOException {
		List<Map<String, String>> datalist = null;
		try (Reader reader = new FileReader(csvfile);
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			datalist = new ArrayList<Map<String, String>>();
			for (CSVRecord csvRecord : csvRecords) {
				// Accessing values by Header names
				Map<String, String> rowMap = new HashMap<String, String>();
				for (String header : headers) {
					rowMap.put(header, csvRecord.get(header));
				}
				datalist.add(rowMap);
				logger.debug("row data = ", rowMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return datalist;
	}

	public static void testCsvWriter() {
		String filename = "download_test.csv";
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("ID", "Name", "Designation", "Company"));) {

			csvPrinter.printRecord("1", "Sundar Pichai", "CEO", "Google");
			csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
			csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

			csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

			csvPrinter.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
