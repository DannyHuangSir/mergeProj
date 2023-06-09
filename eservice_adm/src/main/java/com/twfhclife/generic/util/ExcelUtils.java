package com.twfhclife.generic.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @auther lihao
 */
public class ExcelUtils {
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 读取Excel文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private static Workbook read(String filePath) {
        if (filePath == null) {
            return null;
        }
        String ext = filePath.substring(filePath.lastIndexOf("."));
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            return readFromInputStream(inputStream, ext);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        }
        return null;
    }

    /**
     * 从流中读取，上传文件可以直接获取文件流，无需暂存到服务器上
     *
     * @param inputStream
     * @param ext
     * @return
     */
    private static Workbook readFromInputStream(InputStream inputStream, String ext) {
        try {
            return (!".xls".equals(ext)) ?
                    (".xlsx".equals(ext) ? new XSSFWorkbook(inputStream) : null) : new HSSFWorkbook(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return null;
    }

    /**
     * 读取Excel内容，返回list，每一行存放一个list
     *
     * @param wb
     * @return
     */
    private static List<List<String>> readExcelContentList(Workbook wb) {
        if (wb != null) {
            List<List<String>> content = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            int rowNum = sheet.getPhysicalNumberOfRows();
            int colNum = row.getPhysicalNumberOfCells();
            // 正文内容应该从第二行开始, 第一行为表头的标题
            for (int ri = 1; ri <= rowNum; ri++) {
                row = sheet.getRow(ri);
                if (row == null){
                    continue;
                }
                int ci = 0;
                List<String> col = new ArrayList<>();
                while (ci < colNum) {
                    Object obj = getCellFormatValue(row.getCell(ci++));
                    obj = (obj instanceof Date) ? simpleDateFormat.format((Date) obj) : obj;
                    col.add((String) obj);
                }
                // if the row is pure blank string, will be filtered,
                // but have the null columns, not all blank, will not destroy the row information, and not be filtered.
                long count = col.stream().filter(StringUtils::isNoneBlank).count();
                Optional.of(col).filter(x -> count > 0).ifPresent(content::add);
            }
            return content;
        }
        return null;
    }


    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     */
    private static Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA:
                    // 判断当前的cell为Date, 取时间类型；数字则转字符串
                    cellvalue = DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:
                    break;
            }
        }
        return cellvalue;
    }

    /**
     * 读取Excel
     *
     * @param filePath Excel文件路径
     * @return
     */
    public static List<List<String>> readExcel(String filePath) {
        Workbook wb = read(filePath);
        return readExcelContentList(wb);
    }

    /**
     * 读取Excel
     *
     * @param inputStream Excel文件流
     * @return
     */
    public static List<List<String>> readExcelFromInputStream(InputStream inputStream, String ext) {
        Workbook workbook = readFromInputStream(inputStream, ext);
        return readExcelContentList(workbook);
    }
}
