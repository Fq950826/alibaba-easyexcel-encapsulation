//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

public class CellRange {
    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;

    public CellRange(int firstRow, int lastRow, int firstCol, int lastCol) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
    }

    public int getFirstRow() {
        return this.firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getFirstCol() {
        return this.firstCol;
    }

    public void setFirstCol(int firstCol) {
        this.firstCol = firstCol;
    }

    public int getLastCol() {
        return this.lastCol;
    }

    public void setLastCol(int lastCol) {
        this.lastCol = lastCol;
    }
}
