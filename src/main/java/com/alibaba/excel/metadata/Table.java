//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.TableStyle;
import java.util.List;

public class Table {
    private Class<? extends BaseRowModel> clazz;
    private List<List<String>> head;
    private int tableNo;
    private TableStyle tableStyle;

    public TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public void setTableStyle(TableStyle tableStyle) {
        this.tableStyle = tableStyle;
    }

    public Table(Integer tableNo) {
        this.tableNo = tableNo.intValue();
    }

    public Class<? extends BaseRowModel> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<? extends BaseRowModel> clazz) {
        this.clazz = clazz;
    }

    public List<List<String>> getHead() {
        return this.head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public int getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }
}
