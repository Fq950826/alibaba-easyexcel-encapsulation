//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EasyExcelFactory {
    public EasyExcelFactory() {
    }

    public static List<Object> read(InputStream in, Sheet sheet) {
        final ArrayList rows = new ArrayList();
        (new ExcelReader(in, (Object)null, new AnalysisEventListener() {
            public void invoke(Object object, AnalysisContext context) {
                rows.add(object);
            }

            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }, false)).read(sheet);
        return rows;
    }

    public static void readBySax(InputStream in, Sheet sheet, AnalysisEventListener listener) {
        (new ExcelReader(in, (Object)null, listener)).read(sheet);
    }

    public static ExcelReader getReader(InputStream in, AnalysisEventListener listener) {
        return new ExcelReader(in, (Object)null, listener);
    }

    public static ExcelWriter getWriter(OutputStream outputStream) {
        return new ExcelWriter(outputStream, ExcelTypeEnum.XLSX, true);
    }

    public static ExcelWriter getWriter(OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        return new ExcelWriter(outputStream, typeEnum, needHead);
    }

    public static ExcelWriter getWriterWithTemp(InputStream temp, OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        return new ExcelWriter(temp, outputStream, typeEnum, Boolean.valueOf(needHead));
    }
}
