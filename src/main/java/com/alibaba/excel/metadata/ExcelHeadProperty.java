//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

import com.alibaba.excel.annotation.ExcelColumnNum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.CellRange;
import com.alibaba.excel.metadata.ExcelColumnProperty;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelHeadProperty {
    private Class<? extends BaseRowModel> headClazz;
    private List<List<String>> head = new ArrayList();
    private List<ExcelColumnProperty> columnPropertyList = new ArrayList();
    private Map<Integer, ExcelColumnProperty> excelColumnPropertyMap1 = new HashMap();

    public ExcelHeadProperty(Class<? extends BaseRowModel> headClazz, List<List<String>> head) {
        this.headClazz = headClazz;
        this.head = head;
        this.initColumnProperties();
    }

    private void initColumnProperties() {
        if(this.headClazz != null) {
            ArrayList fieldList = new ArrayList();

            for(Class tempClass = this.headClazz; tempClass != null; tempClass = tempClass.getSuperclass()) {
                fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            }

            ArrayList headList = new ArrayList();
            Iterator var4 = fieldList.iterator();

            while(var4.hasNext()) {
                Field excelColumnProperty = (Field)var4.next();
                this.initOneColumnProperty(excelColumnProperty);
            }

            Collections.sort(this.columnPropertyList);
            if(this.head == null || this.head.size() == 0) {
                var4 = this.columnPropertyList.iterator();

                while(var4.hasNext()) {
                    ExcelColumnProperty excelColumnProperty1 = (ExcelColumnProperty)var4.next();
                    headList.add(excelColumnProperty1.getHead());
                }

                this.head = headList;
            }
        }

    }

    private void initOneColumnProperty(Field f) {
        ExcelProperty p = (ExcelProperty)f.getAnnotation(ExcelProperty.class);
        ExcelColumnProperty excelHeadProperty = null;
        if(p != null) {
            excelHeadProperty = new ExcelColumnProperty();
            excelHeadProperty.setField(f);
            excelHeadProperty.setHead(Arrays.asList(p.value()));
            excelHeadProperty.setIndex(p.index());
            excelHeadProperty.setFormat(p.format());
            this.excelColumnPropertyMap1.put(Integer.valueOf(p.index()), excelHeadProperty);
        } else {
            ExcelColumnNum columnNum = (ExcelColumnNum)f.getAnnotation(ExcelColumnNum.class);
            if(columnNum != null) {
                excelHeadProperty = new ExcelColumnProperty();
                excelHeadProperty.setField(f);
                excelHeadProperty.setIndex(columnNum.value());
                excelHeadProperty.setFormat(columnNum.format());
                this.excelColumnPropertyMap1.put(Integer.valueOf(columnNum.value()), excelHeadProperty);
            }
        }

        if(excelHeadProperty != null) {
            this.columnPropertyList.add(excelHeadProperty);
        }

    }

    public void appendOneRow(List<String> row) {
        for(int i = 0; i < row.size(); ++i) {
            Object list;
            if(this.head.size() <= i) {
                list = new ArrayList();
                this.head.add((List<String>) list);
            } else {
                list = (List)this.head.get(0);
            }

            ((List)list).add(row.get(i));
        }

    }

    public ExcelColumnProperty getExcelColumnProperty(int columnNum) {
        return (ExcelColumnProperty)this.excelColumnPropertyMap1.get(Integer.valueOf(columnNum));
    }

    public Class getHeadClazz() {
        return this.headClazz;
    }

    public void setHeadClazz(Class headClazz) {
        this.headClazz = headClazz;
    }

    public List<List<String>> getHead() {
        return this.head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public List<ExcelColumnProperty> getColumnPropertyList() {
        return this.columnPropertyList;
    }

    public void setColumnPropertyList(List<ExcelColumnProperty> columnPropertyList) {
        this.columnPropertyList = columnPropertyList;
    }

    public List<CellRange> getCellRangeModels() {
        ArrayList cellRanges = new ArrayList();

        for(int i = 0; i < this.head.size(); ++i) {
            List columnValues = (List)this.head.get(i);

            for(int j = 0; j < columnValues.size(); ++j) {
                int lastRow = this.getLastRangNum(j, (String)columnValues.get(j), columnValues);
                int lastColumn = this.getLastRangNum(i, (String)columnValues.get(j), this.getHeadByRowNum(j));
                if((lastRow > j || lastColumn > i) && lastRow >= 0 && lastColumn >= 0) {
                    cellRanges.add(new CellRange(j, lastRow, i, lastColumn));
                }
            }
        }

        return cellRanges;
    }

    public List<String> getHeadByRowNum(int rowNum) {
        ArrayList l = new ArrayList(this.head.size());
        Iterator var3 = this.head.iterator();

        while(var3.hasNext()) {
            List list = (List)var3.next();
            if(list.size() > rowNum) {
                l.add(list.get(rowNum));
            } else {
                l.add(list.get(list.size() - 1));
            }
        }

        return l;
    }

    private int getLastRangNum(int j, String value, List<String> values) {
        if(value == null) {
            return -1;
        } else {
            if(j > 0) {
                String last = (String)values.get(j - 1);
                if(value.equals(last)) {
                    return -1;
                }
            }

            int var7 = j;

            for(int i = j + 1; i < values.size(); ++i) {
                String current = (String)values.get(i);
                if(value.equals(current)) {
                    var7 = i;
                } else if(i > j) {
                    break;
                }
            }

            return var7;
        }
    }

    public int getRowNum() {
        int headRowNum = 0;
        Iterator var2 = this.head.iterator();

        while(var2.hasNext()) {
            List list = (List)var2.next();
            if(list != null && list.size() > 0 && list.size() > headRowNum) {
                headRowNum = list.size();
            }
        }

        return headRowNum;
    }
}
