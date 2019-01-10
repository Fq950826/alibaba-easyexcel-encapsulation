package com.ywkj.alibaba.easyexcel.encapsulation.excel.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @工程名:common-utils
 * @author: sevenDay
 * @时间: 2019/1/4 13:05
 * @描述:
 */
public class ExcelDateUtils {

    /**
     * @descrp:功能描述
     * @auther: sevenDay
     * @date: 2019/1/4 13:16
     * @param days :
     * @return : java.util.Date
     */
    public static Date getDate(int days) {
        Calendar c = Calendar.getInstance();
        c.set(1900, 0, 1);
        c.add(Calendar.DATE, days - 2);
        return c.getTime();
    }

    public static Date getTime(Date date, double ditNumber) {
        Calendar c = Calendar.getInstance();
        int mills = (int) (Math.round(ditNumber * 24 * 3600));
        int hour = mills / 3600;
        int minute = (mills - hour * 3600) / 60;
        int second = mills - hour * 3600 - minute * 60;
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }
    /**
     * @descrp:Excel时间类型转变为字符串的事件类型
     * @auther: sevenDay
     * @date: 2019/1/4 13:10
     * @param time :
     * @return : java.util.Date
     */
    public static String getExcelNumberConverToDate(Double time){

        int date = (int)time.intValue();
        double doubled = time % 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(getTime(getDate(date),doubled));

    }

    public static void main(String[] args) {
        Date date = ExcelDateUtils.getDate(43459);


        //43459.517349537
        //2018/12/25 12:24:59
        String result = ExcelDateUtils.getExcelNumberConverToDate(43459.517349537);
        System.out.println(result);
    }




}
