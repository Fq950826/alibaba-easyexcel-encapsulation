//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.context;

import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import java.util.List;

public interface AnalysisContext {
    Object getCustom();

    Sheet getCurrentSheet();

    void setCurrentSheet(Sheet var1);

    ExcelTypeEnum getExcelType();

    InputStream getInputStream();

    AnalysisEventListener getEventListener();

    Integer getCurrentRowNum();

    void setCurrentRowNum(Integer var1);

    /** @deprecated */
    @Deprecated
    Integer getTotalCount();

    void setTotalCount(Integer var1);

    ExcelHeadProperty getExcelHeadProperty();

    void buildExcelHeadProperty(Class<? extends BaseRowModel> var1, List<String> var2);

    boolean trim();

    void setCurrentRowAnalysisResult(Object var1);

    Object getCurrentRowAnalysisResult();

    void interrupt();

    boolean use1904WindowDate();

    void setUse1904WindowDate(boolean var1);
}
