package com.ywkj.alibaba.easyexcel.encapsulation.excel.core;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.annotation.NotEmpty;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.annotation.TypeConstraint;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.enums.PropertyType;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.exception.ExcelException;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.util.ExcelDateUtils;
import com.ywkj.alibaba.easyexcel.encapsulation.excel.util.RegularExpressionUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Fq on 2019/01/07.
 */
public  class ExcelListener<T> extends AnalysisEventListener {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private List<T> successList = new ArrayList();

    private List<T> failList = new ArrayList();

    private Field reasonsForFailureField=null;

    private Field[] fields=null;


    @Override
    public void invoke(Object object, AnalysisContext context) {
        if(fields==null){
            Class c = object.getClass();
            try {
                Field reasonsForFailureField = c.getSuperclass().getDeclaredField("reasonsForFailure");
                if(reasonsForFailureField==null){
                    throw new ExcelException("model中需集成 DefaultRowModel 或者添加 reasonsForFailure 字段");
                }else{
                    reasonsForFailureField.setAccessible(true);
                    this.reasonsForFailureField=reasonsForFailureField;
                }
            } catch (Exception e) {
                throw new ExcelException("model中需集成 DefaultRowModel 或者添加 reasonsForFailure 字段");
            }
            Field[] fields=c.getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
            }
            this.fields=fields;
        }

        if(legalCheck(object)){
            //数据合法
            successList.add((T)object);
            return;
        }
        //数据不合法
        failList.add((T)object);
    }


    /**
     * 对象数据合法性校验
     * @param object
     * @return
     */
    private boolean legalCheck(Object object) {
        boolean success=true;
        try {
            for (Field field : fields) {
                if (!fieldLegalCheck(field, object)) {
                    success= false;
                }
            }
        }catch (Exception e){
            return false;
        }
        return success;
    }


    /**
     * 字段合法性校验
     * @param field
     * @param object
     * @return
     */
    private boolean fieldLegalCheck(Field field,Object object) throws IllegalAccessException {
        if(!field.getType().equals(String.class)){
            throw new ExcelException("model字段必须是 String 类型");
        }
        String fieldValue=StringUtils.deleteWhitespace((String)field.get(object));
        field.set(object,fieldValue==null?"":fieldValue);
        if(!field.isAnnotationPresent(ExcelProperty.class)){
            throw new ExcelException("model字段必须使用 @ExcelProperty 注解");
        }
        ExcelProperty excelProperty=field.getAnnotation(ExcelProperty.class);
        String[] values=excelProperty.value();
        //字段是否为空
        if(field.isAnnotationPresent(NotEmpty.class)&&StringUtils.isEmpty(fieldValue)){
            constraintFail(object,values,"必填为空");
            return false;
        }

        //字段是否约束
        if(field.isAnnotationPresent(TypeConstraint.class)&&StringUtils.isNotEmpty(fieldValue)){
            TypeConstraint typeConstraint=field.getAnnotation(TypeConstraint.class);
            String msg=typeConstraint.msg();
            //正则约束
            if(StringUtils.isNotEmpty(typeConstraint.regularExpression())&&!RegularExpressionUtil.isMatch(typeConstraint.regularExpression(),fieldValue)){
                constraintFail(object,values,msg);
                return false;
            }
            //直接约束
            if(typeConstraint.type().equals(PropertyType.DATE)){
                //该字段为时间类型
                try {
                    DATE_FORMAT.parse(fieldValue);
                }catch (Exception e){
                    try{
                        Date date = ExcelDateUtils.getDate(Integer.parseInt(fieldValue));
                        field.set(object,DATE_FORMAT.format(date));
                    }catch (Exception ex){
                        constraintFail(object,values,msg);
                        return false;
                    }
                }
            }else{
                if(!typeConstraint.type().equals(PropertyType.ALL)&&!RegularExpressionUtil.isMatch(typeConstraint.type().value(),fieldValue)){
                    constraintFail(object,values,msg);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 约束失败
     * @param object
     * @param values
     * @param msg
     * @throws IllegalAccessException
     */
    private void constraintFail(Object object,String[] values,String msg) throws IllegalAccessException {
        String reason =(String)reasonsForFailureField.get(object);
        if(StringUtils.isEmpty(reason)) {
            reasonsForFailureField.set(object, (values.length == 0 ? "" : Arrays.toString(values) + "字段-") + msg);
        }else{
            reasonsForFailureField.set(object, reason+" ; "+(values.length == 0 ? "" : Arrays.toString(values) + "字段-") + msg);
        }
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