package com.fq.alibaba.easyexcel.encapsulation.excel.model;

import java.util.List;

/**
 * Created by Fq on 2019/1/7.
 */
public class ReadExcelResult<T> {
    private List<T> successList;
    private List<T> failList;


    public ReadExcelResult(List<T> successList, List<T> failList) {
        this.successList = successList;
        this.failList = failList;
    }

    public List<T> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<T> successList) {
        this.successList = successList;
    }

    public List<T> getFailList() {
        return failList;
    }

    public void setFailList(List<T> failList) {
        this.failList = failList;
    }
}
