//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.annotation.FieldType;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventRegisterCenter;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;
import com.alibaba.excel.util.PositionUtils;
import java.util.Arrays;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XlsxRowHandler extends DefaultHandler {
    private String currentCellIndex;
    private FieldType currentCellType;
    private int curRow;
    private int curCol;
    private String[] curRowContent = new String[20];
    private String currentCellValue;
    private SharedStringsTable sst;
    private AnalysisContext analysisContext;
    private AnalysisEventRegisterCenter registerCenter;

    public XlsxRowHandler(AnalysisEventRegisterCenter registerCenter, SharedStringsTable sst, AnalysisContext analysisContext) {
        this.registerCenter = registerCenter;
        this.analysisContext = analysisContext;
        this.sst = sst;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        this.setTotalRowCount(name, attributes);
        this.startCell(name, attributes);
        this.startCellValue(name);
    }

    private void startCellValue(String name) {
        if(name.equals("v") || name.equals("t")) {
            this.currentCellValue = "";
        }

    }

    private void startCell(String name, Attributes attributes) {
        if("c".equals(name)) {
            this.currentCellIndex = attributes.getValue("r");
            int nextRow = PositionUtils.getRow(this.currentCellIndex);
            if(nextRow > this.curRow) {
                this.curRow = nextRow;
            }

            this.analysisContext.setCurrentRowNum(Integer.valueOf(this.curRow));
            this.curCol = PositionUtils.getCol(this.currentCellIndex);
            String cellType = attributes.getValue("t");
            this.currentCellType = FieldType.EMPTY;
            if(cellType != null && cellType.equals("s")) {
                this.currentCellType = FieldType.STRING;
            }
        }

    }

    private void endCellValue(String name) throws SAXException {
        if(this.curCol >= this.curRowContent.length) {
            this.curRowContent = (String[])Arrays.copyOf(this.curRowContent, (int)((double)this.curCol * 1.5D));
        }

        if("v".equals(name)) {
            switch(this.currentCellType.ordinal()) {
                case 1:
                    int idx = Integer.parseInt(this.currentCellValue);
                    this.currentCellValue = (new XSSFRichTextString(this.sst.getEntryAt(idx))).toString();
                    this.currentCellType = FieldType.EMPTY;
                default:
                    this.curRowContent[this.curCol] = this.currentCellValue;
            }
        } else if("t".equals(name)) {
            this.curRowContent[this.curCol] = this.currentCellValue;
        }

    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        this.endRow(name);
        this.endCellValue(name);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.currentCellValue = this.currentCellValue + new String(ch, start, length);
    }

    private void setTotalRowCount(String name, Attributes attributes) {
        if("dimension".equals(name)) {
            String d = attributes.getValue("ref");
            String totalStr = d.substring(d.indexOf(":") + 1, d.length());
            String c = totalStr.toUpperCase().replaceAll("[A-Z]", "");
            this.analysisContext.setTotalCount(Integer.valueOf(Integer.parseInt(c)));
        }

    }

    private void endRow(String name) {
        if(name.equals("row")) {
            this.registerCenter.notifyListeners(new OneRowAnalysisFinishEvent(this.curRowContent, this.curCol));
            this.curRowContent = new String[20];
        }

    }
}
