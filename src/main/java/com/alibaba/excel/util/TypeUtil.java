//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.util;

import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.excel.util.StringUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.cglib.beans.BeanMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class TypeUtil {
    private static List<String> DATE_FORMAT_LIST = new ArrayList(4);
    public static final Pattern pattern;

    public TypeUtil() {
    }

    private static int getCountOfChar(String value, char c) {
        int n = 0;
        if(value == null) {
            return 0;
        } else {
            char[] chars = value.toCharArray();
            char[] var4 = chars;
            int var5 = chars.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                char cc = var4[var6];
                if(cc == c) {
                    ++n;
                }
            }

            return n;
        }
    }

    public static Object convert(String value, Field field, String format, boolean us) {
        if(!StringUtils.isEmpty(value)) {
            if(Float.class.equals(field.getType())) {
                return Float.valueOf(Float.parseFloat(value));
            } else if(!Integer.class.equals(field.getType()) && !Integer.TYPE.equals(field.getType())) {
                if(Double.class.equals(field.getType()) || Double.TYPE.equals(field.getType())) {
                    if(null != format && !"".equals(format)) {
                        int d2 = getCountOfChar(value, '0');
                        return Double.valueOf(Double.parseDouble(formatFloat0(value, d2)));
                    } else {
                        return Double.valueOf(Double.parseDouble(formatFloat(value)));
                    }
                } else if(!Boolean.class.equals(field.getType()) && !Boolean.TYPE.equals(field.getType())) {
                    if(!Long.class.equals(field.getType()) && !Long.TYPE.equals(field.getType())) {
                        if(Date.class.equals(field.getType())) {
                            if(!value.contains("-") && !value.contains("/") && !value.contains(":")) {
                                Double d1 = Double.valueOf(Double.parseDouble(value));
                                return HSSFDateUtil.getJavaDate(d1.doubleValue(), us);
                            } else {
                                return getSimpleDateFormatDate(value, format);
                            }
                        } else if(BigDecimal.class.equals(field.getType())) {
                            return new BigDecimal(value);
                        } else if(String.class.equals(field.getType())) {
                            return formatFloat(value);
                        } else {
                            return null;
                        }
                    } else {
                        return Long.valueOf(Long.parseLong(value));
                    }
                } else {
                    String d = value.toLowerCase();
                    if(!d.equals("true") && !d.equals("false")) {
                        Integer integer = Integer.valueOf(Integer.parseInt(value));
                        if(integer.intValue() == 0) {
                            return Boolean.valueOf(false);
                        } else {
                            return Boolean.valueOf(true);
                        }
                    } else {
                        return Boolean.valueOf(Boolean.parseBoolean(value.toLowerCase()));
                    }
                }
            } else {
                return Integer.valueOf(Integer.parseInt(value));
            }
        } else {
            return null;
        }
    }

    public static Boolean isNum(Field field) {
        return field == null?Boolean.valueOf(false):(!Integer.class.equals(field.getType()) && !Integer.TYPE.equals(field.getType())?(!Double.class.equals(field.getType()) && !Double.TYPE.equals(field.getType())?(!Long.class.equals(field.getType()) && !Long.TYPE.equals(field.getType())?(BigDecimal.class.equals(field.getType())?Boolean.valueOf(true):Boolean.valueOf(false)):Boolean.valueOf(true)):Boolean.valueOf(true)):Boolean.valueOf(true));
    }

    public static Boolean isNum(Object cellValue) {
        return !(cellValue instanceof Integer) && !(cellValue instanceof Double) && !(cellValue instanceof Short) && !(cellValue instanceof Long) && !(cellValue instanceof Float) && !(cellValue instanceof BigDecimal)?Boolean.valueOf(false):Boolean.valueOf(true);
    }

    public static String getDefaultDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static Date getSimpleDateFormatDate(String value, String format) {
        if(StringUtils.isEmpty(value)) {
            return null;
        } else {
            Date date = null;
            if(!StringUtils.isEmpty(format)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

                try {
                    date = simpleDateFormat.parse(value);
                    return date;
                } catch (ParseException var7) {
                    ;
                }
            }

            Iterator simpleDateFormat2 = DATE_FORMAT_LIST.iterator();

            while(simpleDateFormat2.hasNext()) {
                String dateFormat = (String)simpleDateFormat2.next();

                try {
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(dateFormat);
                    date = simpleDateFormat1.parse(value);
                } catch (ParseException var6) {
                    ;
                }

                if(date != null) {
                    break;
                }
            }

            return date;
        }
    }

    public static String formatFloat(String value) {
        if(null != value && value.contains(".") && isNumeric(value)) {
            try {
                BigDecimal decimal = new BigDecimal(value);
                BigDecimal setScale = decimal.setScale(10, 5).stripTrailingZeros();
                return setScale.toPlainString();
            } catch (Exception var3) {
                ;
            }
        }

        return value;
    }

    public static String formatFloat0(String value, int n) {
        if(null != value && value.contains(".") && isNumeric(value)) {
            try {
                BigDecimal decimal = new BigDecimal(value);
                BigDecimal setScale = decimal.setScale(n, 5);
                return setScale.toPlainString();
            } catch (Exception var4) {
                ;
            }
        }

        return value;
    }

    private static boolean isNumeric(String str) {
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String formatDate(Date cellValue, String format) {
        SimpleDateFormat simpleDateFormat;
        if(!StringUtils.isEmpty(format)) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        return simpleDateFormat.format(cellValue);
    }

    public static String getFieldStringValue(BeanMap beanMap, String fieldName, String format) {
        String cellValue = null;
        Object value = beanMap.get(fieldName);
        if(value != null) {
            if(value instanceof Date) {
                cellValue = formatDate((Date)value, format);
            } else {
                cellValue = value.toString();
            }
        }

        return cellValue;
    }

    public static Map getFieldValues(List<String> stringList, ExcelHeadProperty excelHeadProperty, Boolean use1904WindowDate) {
        HashMap map = new HashMap();

        for(int i = 0; i < stringList.size(); ++i) {
            ExcelColumnProperty columnProperty = excelHeadProperty.getExcelColumnProperty(i);
            if(columnProperty != null) {
                Object value = convert((String)stringList.get(i), columnProperty.getField(), columnProperty.getFormat(), use1904WindowDate.booleanValue());
                if(value != null) {
                    map.put(columnProperty.getField().getName(), value);
                }
            }
        }

        return map;
    }

    static {
        DATE_FORMAT_LIST.add("yyyy/MM/dd HH:mm:ss");
        DATE_FORMAT_LIST.add("yyyy-MM-dd HH:mm:ss");
        DATE_FORMAT_LIST.add("yyyyMMdd HH:mm:ss");
        pattern = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?$");
    }
}
