package com.fq.alibaba.easyexcel.encapsulation.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Fq on 2019/1/7.
 */
@Retention(RetentionPolicy.RUNTIME)//记录在.class文件中，并且在运行时保留"注释"
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
public @interface NotEmpty {
}
