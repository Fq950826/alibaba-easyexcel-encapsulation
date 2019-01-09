package com.fq.alibaba.easyexcel.encapsulation.excel.annotation;

import com.fq.alibaba.easyexcel.encapsulation.excel.enums.PropertyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Fq on 2019/1/7.
 * 字段类型约束
 * 正则约束 优先级 大于 类型直接约束
 */
@Retention(RetentionPolicy.RUNTIME)//记录在.class文件中，并且在运行时保留"注释"
@Target({ElementType.FIELD})
@NotEmpty
public @interface TypeConstraint {
    /**
     * 正则约束
     * @return
     */
    String regularExpression() default "";

    /**
     * 类型直接约束
     * @return
     */
    PropertyType type() default PropertyType.ALL;

    /**
     * 约束错误信息
     * @return
     */
    String msg();
}
