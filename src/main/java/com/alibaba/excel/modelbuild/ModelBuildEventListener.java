//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.modelbuild;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.excel.util.TypeUtil;
import java.util.List;
import net.sf.cglib.beans.BeanMap;

public class ModelBuildEventListener extends AnalysisEventListener {
    public ModelBuildEventListener() {
    }

    public void invoke(Object object, AnalysisContext context) {
        if(context.getExcelHeadProperty() != null && context.getExcelHeadProperty().getHeadClazz() != null) {
            try {
                Object e = this.buildUserModel(context, (List)object);
                context.setCurrentRowAnalysisResult(e);
            } catch (Exception var4) {
                throw new ExcelGenerateException(var4);
            }
        }

    }

    private Object buildUserModel(AnalysisContext context, List<String> stringList) throws Exception {
        ExcelHeadProperty excelHeadProperty = context.getExcelHeadProperty();
        Object resultModel = excelHeadProperty.getHeadClazz().newInstance();
        if(excelHeadProperty == null) {
            return resultModel;
        } else {
            BeanMap.create(resultModel).putAll(TypeUtil.getFieldValues(stringList, excelHeadProperty, Boolean.valueOf(context.use1904WindowDate())));
            return resultModel;
        }
    }

    public void doAfterAllAnalysed(AnalysisContext context) {
    }
}
