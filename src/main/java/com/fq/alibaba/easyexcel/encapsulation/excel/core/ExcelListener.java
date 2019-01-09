package com.fq.alibaba.easyexcel.encapsulation.excel.core;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fq.alibaba.easyexcel.encapsulation.excel.annotation.NotEmpty;
import com.fq.alibaba.easyexcel.encapsulation.excel.annotation.TypeConstraint;
import com.fq.alibaba.easyexcel.encapsulation.excel.enums.PropertyType;
import com.fq.alibaba.easyexcel.encapsulation.excel.exception.ExcelException;
import com.fq.alibaba.easyexcel.encapsulation.excel.util.RegularExpressionUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
                    throw new ExcelException("model中找不到reasonsForFailure字段");
                }else{
                    reasonsForFailureField.setAccessible(true);
                    this.reasonsForFailureField=reasonsForFailureField;
                }
            } catch (Exception e) {
                throw new ExcelException("model中找不到reasonsForFailure字段");
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
        try {
            for (Field field : fields) {
                if (!fieldLegalCheck(reasonsForFailureField, field, object)) {
                    return false;
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }


    /**
     * 字段合法性校验
     * @param reasonsForFailureField
     * @param field
     * @param object
     * @return
     */
    private boolean fieldLegalCheck(Field reasonsForFailureField,Field field,Object object) throws IllegalAccessException {
        if(!field.getType().equals(String.class)){
            return false;
        }
        String fieldValue=StringUtils.deleteWhitespace((String)field.get(object));
        field.set(object,fieldValue);
        if(!field.isAnnotationPresent(ExcelProperty.class)){
            return false;
        }
        ExcelProperty excelProperty=field.getAnnotation(ExcelProperty.class);
        String[] values=excelProperty.value();
        //字段是否为空
        if(field.isAnnotationPresent(NotEmpty.class)&&StringUtils.isEmpty(fieldValue)){
            reasonsForFailureField.set(object,(excelProperty.value().length==0?"": Arrays.toString(excelProperty.value())+"-")+"字段为空!");
            return false;
        }

        //字段是否约束
        if(field.isAnnotationPresent(TypeConstraint.class)){
            if(StringUtils.isEmpty(fieldValue)){
                reasonsForFailureField.set(object,(excelProperty.value().length==0?"":Arrays.toString(excelProperty.value())+"-")+"字段为空!");
                return false;
            }
            TypeConstraint typeConstraint=field.getAnnotation(TypeConstraint.class);
            String msg=typeConstraint.msg();
            //正则约束
            if(StringUtils.isNotEmpty(typeConstraint.regularExpression())&&!RegularExpressionUtil.isMatch(typeConstraint.regularExpression(),fieldValue)){
                constraintFail(object,values,msg);
                return false;
            }
            //直接约束
            if(typeConstraint.type().equals(PropertyType.DATE)){
                try {
                    DATE_FORMAT.parse(fieldValue);
                }catch (Exception e){
                    constraintFail(object,values,msg);
                    return false;
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
        reasonsForFailureField.set(object,(values.length==0?"":Arrays.toString(values)+"字段-")+msg);
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