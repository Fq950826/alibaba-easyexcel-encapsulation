package com.fq.alibaba.easyexcel.encapsulation.excel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class DefaultRowModel extends BaseRowModel {

    @ExcelProperty(value = "失败原因")
    protected String reasonsForFailure;

    public String getReasonsForFailure() {
        return reasonsForFailure;
    }

    public void setReasonsForFailure(String reasonsForFailure) {
        this.reasonsForFailure = reasonsForFailure;
    }
}
