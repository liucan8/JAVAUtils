package com.lc.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static int MAX_ROW = 63336;

    /**
     * @param fieldName excel数据的抬头栏，即名称栏
     * @param fieldData excel导出的实际数据
     * @param splitCount Excel每个工作表的行数
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook createWorkbook(List<String> fieldName, List<?> fieldData, int splitCount) {
        HSSFWorkbook workBook = new HSSFWorkbook();//创建一个工作簿
        int rows = fieldData.size();//清点出输入数据的行数
        int sheetNum = 0;//将工作表个数清零
        //根据数据的行数与每个工作表所能容纳的行数，求出需要创建工作表的个数
        if (rows % splitCount == 0) {
            sheetNum = rows / splitCount;
        } else {
            sheetNum = rows / splitCount + 1;
        }

        for (int i = 1; i <= sheetNum; i++) {
            HSSFSheet sheet = workBook.createSheet("Page " + i);//创建工作表
            HSSFRow headRow = sheet.createRow(0); //创建第一栏，抬头栏
            for (int j = 0; j < fieldName.size(); j++) {
                HSSFCell cell = headRow.createCell(j);//创建抬头栏单元格
                //设置单元格格式
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                sheet.setColumnWidth(j, 6000);
                HSSFCellStyle style = workBook.createCellStyle();
                HSSFFont font = workBook.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                short color = HSSFColor.RED.index;
                font.setColor(color);
                style.setFont(font);
                //将数据填入单元格
                if (fieldName.get(j) != null) {
                    cell.setCellStyle(style);
                    cell.setCellValue((String) fieldName.get(j));
                } else {
                    cell.setCellStyle(style);
                    cell.setCellValue("-");
                }
            }
            //创建数据栏单元格并填入数据
            for (int k = 0; k < (rows < splitCount ? rows : splitCount); k++) {
                if (((i - 1) * splitCount + k) >= rows) {
                    break;
                }
                HSSFRow row = sheet.createRow(k + 1);
                List<?> rowList = (List<?>) fieldData.get((i - 1)
                        * splitCount + k);
                for (int n = 0; n < rowList.size(); n++) {
                    HSSFCell cell = row.createCell(n);
                    if (rowList.get(n) != null) {
                        cell.setCellValue((String) rowList.get(n).toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        return workBook;
    }

    /**
     * 读取excel
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> readExcel(String filePath) throws Exception {

        String extensionName = getExtensionName(filePath);

        if (extensionName.equalsIgnoreCase("xlsx")) {
            return readExcel2007(filePath);
        } else {
            return readExcel2003(filePath);
        }

    }

    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    private static List<Map<String, Object>> readExcel2007(String filePath) throws IOException {
        List<Map<String, Object>> valueList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            XSSFWorkbook xwb = new XSSFWorkbook(fis);   // 构造 XSSFWorkbook 对象，strPath 传入文件路径
            XSSFSheet sheet = xwb.getSheetAt(0);            // 读取第一章表格内容
            // 定义 row、cell
            XSSFRow row;
            // 循环输出表格中的第一行内容   表头
            Map<Integer, String> keys = new HashMap<>();
            row = sheet.getRow(0);
            if (row != null) {
                for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                    // 通过 row.getCell(j).toString() 获取单元格内容，
                    if (row.getCell(j) != null) {
                        if (!row.getCell(j).toString().isEmpty()) {
                            keys.put(j, row.getCell(j).toString());
                        }
                    } else {
                        keys.put(j, "K-R1C" + j + "E");
                    }
                }
            }
            // 循环输出表格中的从第二行开始内容
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    boolean isValidRow = false;
                    Map<String, Object> val = new HashMap<>();
                    for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = null;
                            if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = new DataFormatter().formatRawCellContents(cell.getNumericCellValue(), 0, "yyyy-MM-dd");
                                } else {
                                    DecimalFormat df = new DecimalFormat("#");
                                    cellValue = df.format(cell.getNumericCellValue());
                                }
                            } else {
                                cellValue = cell.toString();
                            }
                            if (cellValue != null && cellValue.trim().length() <= 0) {
                                cellValue = null;
                            }
                            val.put(keys.get(j), cellValue);
                            if (!isValidRow && cellValue != null && cellValue.trim().length() > 0) {
                                isValidRow = true;
                            }
                        }
                    }

                    // 第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf(e.getMessage(), e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return valueList;
    }

    private static List<Map<String, Object>> readExcel2003(String filePath) throws IOException {
        //返回结果集
        List<Map<String, Object>> valueList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            HSSFWorkbook wookbook = new HSSFWorkbook(fis);  // 创建对Excel工作簿文件的引用
            HSSFSheet sheet = wookbook.getSheetAt(0);   // 在Excel文档中，第一张工作表的缺省索引是0
            int rows = sheet.getPhysicalNumberOfRows(); // 获取到Excel文件中的所有行数­
            Map<Integer, String> keys = new HashMap<>();
            int cells = 0;
            // 遍历行­（第1行  表头） 准备Map里的key
            HSSFRow firstRow = sheet.getRow(0);
            if (firstRow != null) {
                // 获取到Excel文件中的所有的列
                cells = firstRow.getPhysicalNumberOfCells();
                // 遍历列
                for (int j = 0; j < cells; j++) {
                    // 获取到列的值­
                    try {
                        HSSFCell cell = firstRow.getCell(j);
                        String cellValue = getCellValue(cell);
                        keys.put(j, cellValue);
                    } catch (Exception e) {
                        System.out.printf(e.getMessage(), e);
                    }
                }
            }
            // 遍历行­（从第二行开始）
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格(从第二行开始)
                HSSFRow row = sheet.getRow(i);
                // 行不为空
                if (row != null) {
                    //准备当前行 所储存值的map
                    Map<String, Object> val = new HashMap<>();

                    boolean isValidRow = false;

                    // 遍历列
                    for (int j = 0; j < cells; j++) {
                        // 获取到列的值­
                        try {
                            HSSFCell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            val.put(keys.get(j), cellValue);
                            if (!isValidRow && cellValue != null && cellValue.trim().length() > 0) {
                                isValidRow = true;
                            }
                        } catch (Exception e) {
                            System.out.printf(e.getMessage(), e);
                        }
                    }
                    //第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf(e.getMessage(), e);
        } finally {
            if (fis != null) {
                fis.close();
            }

        }
        return valueList;
    }

    private static String getCellValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        String cellValue = null;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    break;
                }
                cellValue = df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = null;
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
        }
        if (cellValue != null && cellValue.trim().length() <= 0) {
            cellValue = null;
        }
        return cellValue;
    }

}
