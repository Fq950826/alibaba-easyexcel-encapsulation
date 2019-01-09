//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelColumnProperty implements Comparable<ExcelColumnProperty> {
    private Field field;
    private int index = 99999;
    private List<String> head = new ArrayList();
    private String format;

    public ExcelColumnProperty() {
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getHead() {
        return this.head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public int compareTo(ExcelColumnProperty o) {
        int x = this.index;
        int y = o.getIndex();
        return x < y?-1:(x == y?0:1);
    }
}
