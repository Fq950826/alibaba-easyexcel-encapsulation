//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.write;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.POITempFile;
import com.alibaba.excel.util.TypeUtil;
import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.write.ExcelBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import net.sf.cglib.beans.BeanMap;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelBuilderImpl implements ExcelBuilder {
    private WriteContext context;

    public ExcelBuilderImpl(InputStream templateInputStream, OutputStream out, ExcelTypeEnum excelType, boolean needHead) {
        try {
            POITempFile.createPOIFilesDirectory();
            this.context = new WriteContext(templateInputStream, out, excelType, needHead);
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public void addContent(List data, int startRow) {
        if(!CollectionUtils.isEmpty(data)) {
            int rowNum = this.context.getCurrentSheet().getLastRowNum();
            if(rowNum == 0) {
                Row i = this.context.getCurrentSheet().getRow(0);
                if(i == null && (this.context.getExcelHeadProperty() == null || !this.context.needHead())) {
                    rowNum = -1;
                }
            }

            if(rowNum < startRow) {
                rowNum = startRow;
            }

            for(int var6 = 0; var6 < data.size(); ++var6) {
                int n = var6 + rowNum + 1;
                this.addOneRowOfDataToExcel(data.get(var6), n);
            }

        }
    }

    public void addContent(List data, Sheet sheetParam) {
        this.context.currentSheet(sheetParam);
        this.addContent(data, sheetParam.getStartRow());
    }

    public void addContent(List data, Sheet sheetParam, Table table) {
        this.context.currentSheet(sheetParam);
        this.context.currentTable(table);
        this.addContent(data, sheetParam.getStartRow());
    }

    public void merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress cra = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        this.context.getCurrentSheet().addMergedRegion(cra);
    }

    public void finish() {
        try {
            this.context.getWorkbook().write(this.context.getOutputStream());
            this.context.getWorkbook().close();
        } catch (IOException var2) {
            throw new ExcelGenerateException("IO error", var2);
        }
    }

    private void addBasicTypeToExcel(List<Object> oneRowData, Row row) {
        if(!CollectionUtils.isEmpty(oneRowData)) {
            for(int i = 0; i < oneRowData.size(); ++i) {
                Object cellValue = oneRowData.get(i);
                WorkBookUtil.createCell(row, i, this.context.getCurrentContentStyle(), cellValue, TypeUtil.isNum(cellValue));
            }

        }
    }

    private void addJavaObjectToExcel(Object oneRowData, Row row) {
        int i = 0;
        BeanMap beanMap = BeanMap.create(oneRowData);

        for(Iterator var5 = this.context.getExcelHeadProperty().getColumnPropertyList().iterator(); var5.hasNext(); ++i) {
            ExcelColumnProperty excelHeadProperty = (ExcelColumnProperty)var5.next();
            BaseRowModel baseRowModel = (BaseRowModel)oneRowData;
            String cellValue = TypeUtil.getFieldStringValue(beanMap, excelHeadProperty.getField().getName(), excelHeadProperty.getFormat());
            CellStyle cellStyle = baseRowModel.getStyle(Integer.valueOf(i)) != null?baseRowModel.getStyle(Integer.valueOf(i)):this.context.getCurrentContentStyle();
            WorkBookUtil.createCell(row, i, cellStyle, cellValue, TypeUtil.isNum(excelHeadProperty.getField()));
        }

    }

    private void addOneRowOfDataToExcel(Object oneRowData, int n) {
        Row row = WorkBookUtil.createRow(this.context.getCurrentSheet(), n);
        if(oneRowData instanceof List) {
            this.addBasicTypeToExcel((List)oneRowData, row);
        } else {
            this.addJavaObjectToExcel(oneRowData, row);
        }

    }
}
