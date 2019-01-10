package com.ywkj.alibaba.easyexcel.encapsulation.excel.enums;

public enum PropertyType {
    //所有
    ALL(""),

    //整数
    INTEGER("^-?\\d+$"),

    //正整数
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$"),

    //0+正整数
    ZERO_POSITIVE_INTEGER("^\\d+$"),

    //浮点数
    FLOAT("^(-?\\d+)(\\.\\d+)?$"),

    //正浮点数
    POSITIVE_FLOAT("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$"),

    //0+正浮点数
    ZERO_POSITIVE_FLOAT("^\\d+(\\.\\d+)?$"),

    //日期（年月日）
    DATE("");



    private String value;

    private PropertyType(String value) {
        this.value = value;
    }


    public String value() {
        return this.value;
    }
}
