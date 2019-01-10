package com.ywkj.alibaba.easyexcel.encapsulation.excel.pojo;

import java.util.List;

/**
 * Created by Fq on 2019/1/7.
 */
public class ReadExcelResult<T> {
    private int allCount;
    private List<T> successList;
    private List<T> failList;


    public ReadExcelResult(List<T> successList, List<T> failList,int allCount) {
        this.successList = successList;
        this.failList = failList;
        this.allCount=allCount;
    }


    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
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
