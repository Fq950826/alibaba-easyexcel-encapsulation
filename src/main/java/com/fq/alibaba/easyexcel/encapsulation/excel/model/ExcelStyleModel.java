package com.fq.alibaba.easyexcel.encapsulation.excel.model;


/**
 * @工程名:common-utils
 * @author: sevenDay
 * @时间: 2019/1/7 11:06
 * @描述:
 */
public class ExcelStyleModel {

    private boolean bold;

    private Integer backColor;

    private short fontHeight;

    private String fontName;

    public ExcelStyleModel(boolean bold, Integer backColor, short fontHeight, String fontName) {
        this.bold = bold;
        this.backColor = backColor;
        this.fontHeight = fontHeight;
        this.fontName = fontName;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public Integer getBackColor() {
        return backColor;
    }

    public void setBackColor(Integer backColor) {
        this.backColor = backColor;
    }

    public short getFontHeight() {
        return fontHeight;
    }

    public void setFontHeight(short fontHeight) {
        this.fontHeight = fontHeight;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }
}
