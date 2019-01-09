//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.util;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.StyleUtil;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkBookUtil {
    public WorkBookUtil() {
    }

    public static Workbook createWorkBook(InputStream templateInputStream, ExcelTypeEnum excelType) throws IOException {
        Object workbook;
        if(ExcelTypeEnum.XLS.equals(excelType)) {
            workbook = templateInputStream == null?new HSSFWorkbook():new HSSFWorkbook(new POIFSFileSystem(templateInputStream));
        } else {
            workbook = templateInputStream == null?new SXSSFWorkbook(500):new SXSSFWorkbook(new XSSFWorkbook(templateInputStream));
        }

        return (Workbook)workbook;
    }

    public static Sheet createOrGetSheet(Workbook workbook, com.alibaba.excel.metadata.Sheet sheet) {
        Sheet sheet1 = null;

        try {
            try {
                sheet1 = workbook.getSheetAt(sheet.getSheetNo() - 1);
            } catch (Exception var4) {
                ;
            }

            if(null == sheet1) {
                sheet1 = createSheet(workbook, sheet);
                StyleUtil.buildSheetStyle(sheet1, sheet.getColumnWidthMap());
            }

            return sheet1;
        } catch (Exception var5) {
            throw new RuntimeException("constructCurrentSheet error", var5);
        }
    }

    public static Sheet createSheet(Workbook workbook, com.alibaba.excel.metadata.Sheet sheet) {
        return workbook.createSheet(sheet.getSheetName() != null?sheet.getSheetName():sheet.getSheetNo() + "");
    }

    public static Row createRow(Sheet sheet, int rowNum) {
        return sheet.createRow(rowNum);
    }

    public static Cell createCell(Row row, int colNum, CellStyle cellStyle, String cellValue) {
        return createCell(row, colNum, cellStyle, cellValue, Boolean.valueOf(false));
    }

    public static Cell createCell(Row row, int colNum, CellStyle cellStyle, Object cellValue, Boolean isNum) {
        Cell cell = row.createCell(colNum);
        cell.setCellStyle(cellStyle);
        if(null != cellValue) {
            if(isNum.booleanValue()) {
                cell.setCellValue(Double.parseDouble(cellValue.toString()));
            } else {
                cell.setCellValue(cellValue.toString());
            }
        }

        return cell;
    }
}
