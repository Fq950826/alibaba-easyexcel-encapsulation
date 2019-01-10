package com.ywkj.alibaba.easyexcel.encapsulation.excel.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class RegularExpressionUtil {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");


    public static void main(String[] args){
        String a="2018/8/9";
        try {
            DATE_FORMAT.parse(a);
        }catch (Exception e){
            System.out.println("错误");
        }



        //a=StringUtils.deleteWhitespace(a);
        //System.out.println(isMatch("/^((((19|20)\\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$/",a));
    }
    public static boolean isMatch(String regularExpression,String content){
        return Pattern.matches(regularExpression, content);
    }
}
