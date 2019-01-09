//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.context;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.CellRange;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.util.WorkBookUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class WriteContext {
    private Sheet currentSheet;
    private com.alibaba.excel.metadata.Sheet currentSheetParam;
    private String currentSheetName;
    private Table currentTable;
    private ExcelTypeEnum excelType;
    private Workbook workbook;
    private OutputStream outputStream;
    private Map<Integer, Table> tableMap = new ConcurrentHashMap();
    private CellStyle defaultCellStyle;
    private CellStyle currentHeadCellStyle;
    private CellStyle currentContentCellStyle;
    private ExcelHeadProperty excelHeadProperty;
    private boolean needHead;

    public WriteContext(InputStream templateInputStream, OutputStream out, ExcelTypeEnum excelType, boolean needHead) throws IOException {
        this.needHead = Boolean.TRUE.booleanValue();
        this.needHead = needHead;
        this.outputStream = out;
        this.workbook = WorkBookUtil.createWorkBook(templateInputStream, excelType);
        this.defaultCellStyle = StyleUtil.buildDefaultCellStyle(this.workbook);
    }

    public void currentSheet(com.alibaba.excel.metadata.Sheet sheet) {
        if(null == this.currentSheetParam || this.currentSheetParam.getSheetNo() != sheet.getSheetNo()) {
            this.cleanCurrentSheet();
            this.currentSheetParam = sheet;

            try {
                this.currentSheet = this.workbook.getSheetAt(sheet.getSheetNo() - 1);
            } catch (Exception var3) {
                this.currentSheet = WorkBookUtil.createSheet(this.workbook, sheet);
            }

            StyleUtil.buildSheetStyle(this.currentSheet, sheet.getColumnWidthMap());
            this.initCurrentSheet(sheet);
        }

    }

    private void initCurrentSheet(com.alibaba.excel.metadata.Sheet sheet) {
        this.initExcelHeadProperty(sheet.getHead(), sheet.getClazz());
        this.initTableStyle(sheet.getTableStyle());
        this.initTableHead();
    }

    private void cleanCurrentSheet() {
        this.currentSheet = null;
        this.currentSheetParam = null;
        this.excelHeadProperty = null;
        this.currentHeadCellStyle = null;
        this.currentContentCellStyle = null;
        this.currentTable = null;
    }

    private void initExcelHeadProperty(List<List<String>> head, Class<? extends BaseRowModel> clazz) {
        if(head != null || clazz != null) {
            this.excelHeadProperty = new ExcelHeadProperty(clazz, head);
        }

    }

    public void initTableHead() {
        if(this.needHead && null != this.excelHeadProperty && !CollectionUtils.isEmpty(this.excelHeadProperty.getHead())) {
            int startRow = this.currentSheet.getLastRowNum();
            if(startRow > 0) {
                startRow += 4;
            } else {
                startRow = this.currentSheetParam.getStartRow();
            }

            this.addMergedRegionToCurrentSheet(startRow);

            for(int i = startRow; i < this.excelHeadProperty.getRowNum() + startRow; ++i) {
                Row row = WorkBookUtil.createRow(this.currentSheet, i);
                this.addOneRowOfHeadDataToExcel(row, this.excelHeadProperty.getHeadByRowNum(i - startRow));
            }
        }

    }

    private void addMergedRegionToCurrentSheet(int startRow) {
        Iterator var2 = this.excelHeadProperty.getCellRangeModels().iterator();

        while(var2.hasNext()) {
            CellRange cellRangeModel = (CellRange)var2.next();
            this.currentSheet.addMergedRegion(new CellRangeAddress(cellRangeModel.getFirstRow() + startRow, cellRangeModel.getLastRow() + startRow, cellRangeModel.getFirstCol(), cellRangeModel.getLastCol()));
        }

    }

    private void addOneRowOfHeadDataToExcel(Row row, List<String> headByRowNum) {
        if(headByRowNum != null && headByRowNum.size() > 0) {
            for(int i = 0; i < headByRowNum.size(); ++i) {
                WorkBookUtil.createCell(row, i, this.getCurrentHeadCellStyle(), (String)headByRowNum.get(i));
            }
        }

    }

    private void initTableStyle(TableStyle tableStyle) {
        if(tableStyle != null) {
            this.currentHeadCellStyle = StyleUtil.buildCellStyle(this.workbook, tableStyle.getTableHeadFont(), tableStyle.getTableHeadBackGroundColor());
            this.currentContentCellStyle = StyleUtil.buildCellStyle(this.workbook, tableStyle.getTableContentFont(), tableStyle.getTableContentBackGroundColor());
        }

    }

    private void cleanCurrentTable() {
        this.excelHeadProperty = null;
        this.currentHeadCellStyle = null;
        this.currentContentCellStyle = null;
        this.currentTable = null;
    }

    public void currentTable(Table table) {
        if(null == this.currentTable || this.currentTable.getTableNo() != table.getTableNo()) {
            this.cleanCurrentTable();
            this.currentTable = table;
            this.initExcelHeadProperty(table.getHead(), table.getClazz());
            this.initTableStyle(table.getTableStyle());
            this.initTableHead();
        }

    }

    public ExcelHeadProperty getExcelHeadProperty() {
        return this.excelHeadProperty;
    }

    public boolean needHead() {
        return this.needHead;
    }

    public Sheet getCurrentSheet() {
        return this.currentSheet;
    }

    public void setCurrentSheet(Sheet currentSheet) {
        this.currentSheet = currentSheet;
    }

    public String getCurrentSheetName() {
        return this.currentSheetName;
    }

    public void setCurrentSheetName(String currentSheetName) {
        this.currentSheetName = currentSheetName;
    }

    public ExcelTypeEnum getExcelType() {
        return this.excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public CellStyle getCurrentHeadCellStyle() {
        return this.currentHeadCellStyle == null?this.defaultCellStyle:this.currentHeadCellStyle;
    }

    public CellStyle getCurrentContentStyle() {
        return this.currentContentCellStyle;
    }

    public Workbook getWorkbook() {
        return this.workbook;
    }

    public com.alibaba.excel.metadata.Sheet getCurrentSheetParam() {
        return this.currentSheetParam;
    }

    public void setCurrentSheetParam(com.alibaba.excel.metadata.Sheet currentSheetParam) {
        this.currentSheetParam = currentSheetParam;
    }

    public Table getCurrentTable() {
        return this.currentTable;
    }

    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }
}
