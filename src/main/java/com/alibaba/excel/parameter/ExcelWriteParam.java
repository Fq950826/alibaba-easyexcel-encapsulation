//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.parameter;

import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.OutputStream;

/** @deprecated */
@Deprecated
public class ExcelWriteParam {
    private OutputStream outputStream;
    private ExcelTypeEnum type;

    public ExcelWriteParam(OutputStream outputStream, ExcelTypeEnum type) {
        this.outputStream = outputStream;
        this.type = type;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public ExcelTypeEnum getType() {
        return this.type;
    }

    public void setType(ExcelTypeEnum type) {
        this.type = type;
    }
}
