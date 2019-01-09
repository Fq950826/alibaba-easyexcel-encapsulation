//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

import com.alibaba.excel.metadata.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class TableStyle {
    private IndexedColors tableHeadBackGroundColor;
    private Font tableHeadFont;
    private Font tableContentFont;
    private IndexedColors tableContentBackGroundColor;

    public TableStyle() {
    }

    public IndexedColors getTableHeadBackGroundColor() {
        return this.tableHeadBackGroundColor;
    }

    public void setTableHeadBackGroundColor(IndexedColors tableHeadBackGroundColor) {
        this.tableHeadBackGroundColor = tableHeadBackGroundColor;
    }

    public Font getTableHeadFont() {
        return this.tableHeadFont;
    }

    public void setTableHeadFont(Font tableHeadFont) {
        this.tableHeadFont = tableHeadFont;
    }

    public Font getTableContentFont() {
        return this.tableContentFont;
    }

    public void setTableContentFont(Font tableContentFont) {
        this.tableContentFont = tableContentFont;
    }

    public IndexedColors getTableContentBackGroundColor() {
        return this.tableContentBackGroundColor;
    }

    public void setTableContentBackGroundColor(IndexedColors tableContentBackGroundColor) {
        this.tableContentBackGroundColor = tableContentBackGroundColor;
    }
}
