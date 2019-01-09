//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel;

import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.ExcelAnalyserImpl;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.parameter.AnalysisParam;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import java.util.List;

public class ExcelReader {
    private ExcelAnalyser analyser;

    /** @deprecated */
    @Deprecated
    public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent, AnalysisEventListener eventListener) {
        this(in, excelTypeEnum, customContent, eventListener, true);
    }

    public ExcelReader(InputStream in, Object customContent, AnalysisEventListener eventListener) {
        this(in, customContent, eventListener, true);
    }

    /** @deprecated */
    @Deprecated
    public ExcelReader(AnalysisParam param, AnalysisEventListener eventListener) {
        this(param.getIn(), param.getExcelTypeEnum(), param.getCustomContent(), eventListener, true);
    }

    /** @deprecated */
    @Deprecated
    public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent, AnalysisEventListener eventListener, boolean trim) {
        this.validateParam(in, eventListener);
        this.analyser = new ExcelAnalyserImpl(in, excelTypeEnum, customContent, eventListener, trim);
    }

    public ExcelReader(InputStream in, Object customContent, AnalysisEventListener eventListener, boolean trim) {
        ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.valueOf(in);
        this.validateParam(in, eventListener);
        this.analyser = new ExcelAnalyserImpl(in, excelTypeEnum, customContent, eventListener, trim);
    }

    public void read() {
        this.analyser.analysis();
    }

    public void read(Sheet sheet) {
        this.analyser.analysis(sheet);
    }

    /** @deprecated */
    @Deprecated
    public void read(Sheet sheet, Class<? extends BaseRowModel> clazz) {
        sheet.setClazz(clazz);
        this.analyser.analysis(sheet);
    }

    public List<Sheet> getSheets() {
        return this.analyser.getSheets();
    }

    private void validateParam(InputStream in, AnalysisEventListener eventListener) {
        if(eventListener == null) {
            throw new IllegalArgumentException("AnalysisEventListener can not null");
        } else if(in == null) {
            throw new IllegalArgumentException("InputStream can not null");
        }
    }
}
