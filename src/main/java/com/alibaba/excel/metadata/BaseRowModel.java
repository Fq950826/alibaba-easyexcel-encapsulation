//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;

public class BaseRowModel {
    private Map<Integer, CellStyle> cellStyleMap = new HashMap();

    public BaseRowModel() {
    }

    public void addStyle(Integer row, CellStyle cellStyle) {
        this.cellStyleMap.put(row, cellStyle);
    }

    public CellStyle getStyle(Integer row) {
        return (CellStyle)this.cellStyleMap.get(row);
    }

    public Map<Integer, CellStyle> getCellStyleMap() {
        return this.cellStyleMap;
    }

    public void setCellStyleMap(Map<Integer, CellStyle> cellStyleMap) {
        this.cellStyleMap = cellStyleMap;
    }
}
