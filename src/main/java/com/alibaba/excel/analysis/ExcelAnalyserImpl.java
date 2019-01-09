//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis;

import com.alibaba.excel.analysis.BaseSaxAnalyser;
import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.v03.XlsSaxAnalyser;
import com.alibaba.excel.analysis.v07.XlsxSaxAnalyser;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.context.AnalysisContextImpl;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.modelbuild.ModelBuildEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import java.util.List;

public class ExcelAnalyserImpl implements ExcelAnalyser {
    private AnalysisContext analysisContext;
    private BaseSaxAnalyser saxAnalyser;

    public ExcelAnalyserImpl(InputStream inputStream, ExcelTypeEnum excelTypeEnum, Object custom, AnalysisEventListener eventListener, boolean trim) {
        this.analysisContext = new AnalysisContextImpl(inputStream, excelTypeEnum, custom, eventListener, trim);
    }

    private BaseSaxAnalyser getSaxAnalyser() {
        if(this.saxAnalyser != null) {
            return this.saxAnalyser;
        } else {
            try {
                if(this.analysisContext.getExcelType() != null) {
                    switch(this.analysisContext.getExcelType().ordinal()) {
                        case 1:
                            this.saxAnalyser = new XlsSaxAnalyser(this.analysisContext);
                            break;
                        case 2:
                            this.saxAnalyser = new XlsxSaxAnalyser(this.analysisContext);
                    }
                } else {
                    try {
                        this.saxAnalyser = new XlsxSaxAnalyser(this.analysisContext);
                    } catch (Exception var2) {
                        if(!this.analysisContext.getInputStream().markSupported()) {
                            throw new ExcelAnalysisException("Xls must be available markSupported,you can do like this <code> new BufferedInputStream(new FileInputStream(\"/xxxx\"))</code> ");
                        }

                        this.saxAnalyser = new XlsSaxAnalyser(this.analysisContext);
                    }
                }
            } catch (Exception var3) {
                throw new ExcelAnalysisException("File type error，io must be available markSupported,you can do like this <code> new BufferedInputStream(new FileInputStream(\\\"/xxxx\\\"))</code> \"", var3);
            }

            return this.saxAnalyser;
        }
    }

    public void analysis(Sheet sheetParam) {
        this.analysisContext.setCurrentSheet(sheetParam);
        this.analysis();
    }

    public void analysis() {
        BaseSaxAnalyser saxAnalyser = this.getSaxAnalyser();
        this.appendListeners(saxAnalyser);
        saxAnalyser.execute();
        this.analysisContext.getEventListener().doAfterAllAnalysed(this.analysisContext);
    }

    public List<Sheet> getSheets() {
        BaseSaxAnalyser saxAnalyser = this.getSaxAnalyser();
        saxAnalyser.cleanAllListeners();
        return saxAnalyser.getSheets();
    }

    private void appendListeners(BaseSaxAnalyser saxAnalyser) {
        saxAnalyser.cleanAllListeners();
        if(this.analysisContext.getCurrentSheet() != null && this.analysisContext.getCurrentSheet().getClazz() != null) {
            saxAnalyser.appendLister("model_build_listener", new ModelBuildEventListener());
        }

        if(this.analysisContext.getEventListener() != null) {
            saxAnalyser.appendLister("user_define_listener", this.analysisContext.getEventListener());
        }

    }
}
