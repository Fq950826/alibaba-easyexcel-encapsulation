//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.support;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.filesystem.FileMagic;

public enum ExcelTypeEnum {
    XLS(".xls"),
    XLSX(".xlsx");

    private String value;

    private ExcelTypeEnum(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ExcelTypeEnum valueOf(InputStream inputStream) {
        try {
            if(!inputStream.markSupported()) {
                return null;
            } else {
                FileMagic e = FileMagic.valueOf(inputStream);
                return FileMagic.OLE2.equals(e)?XLS:(FileMagic.OOXML.equals(e)?XLSX:null);
            }
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }
}
