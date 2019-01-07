package com.fq.alibaba.easyexcel.encapsulation.excel.core;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fq.alibaba.easyexcel.encapsulation.excel.annotation.PropertyNotNull;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fq on 2019/01/07.
 */
public  class ExcelListener<T> extends AnalysisEventListener {

    private List<T> successList = new ArrayList();

    private List<T> failList = new ArrayList();

    /**
     * 数据校验
     * @param object
     * @param context
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        if(fieldIllegal(object)){
            failList.add((T)object);
            return;
        }

        //数据合法
        successList.add((T)object);
    }


    private boolean fieldIllegal(Object object){
        for(Field f : object.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                //字段非法为空
                if(f.isAnnotationPresent(PropertyNotNull.class)&&(f.get(object) == null)){
                    return true;
                }
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }



    /**
     * 解析完Excel后调用
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
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