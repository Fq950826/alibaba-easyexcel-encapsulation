//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis;

import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.AnalysisEventRegisterCenter;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.util.TypeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class BaseSaxAnalyser implements AnalysisEventRegisterCenter, ExcelAnalyser {
    protected AnalysisContext analysisContext;
    private LinkedHashMap<String, AnalysisEventListener> listeners = new LinkedHashMap();

    public BaseSaxAnalyser() {
    }

    protected abstract void execute();

    public void appendLister(String name, AnalysisEventListener listener) {
        if(!this.listeners.containsKey(name)) {
            this.listeners.put(name, listener);
        }

    }

    public void analysis(Sheet sheetParam) {
        this.execute();
    }

    public void analysis() {
        this.execute();
    }

    public void cleanAllListeners() {
        this.listeners = new LinkedHashMap();
    }

    public void notifyListeners(OneRowAnalysisFinishEvent event) {
        this.analysisContext.setCurrentRowAnalysisResult(event.getData());
        if(this.analysisContext.getCurrentRowNum().intValue() < this.analysisContext.getCurrentSheet().getHeadLineMun()) {
            if(this.analysisContext.getCurrentRowNum().intValue() <= this.analysisContext.getCurrentSheet().getHeadLineMun() - 1) {
                this.analysisContext.buildExcelHeadProperty((Class)null, (List)this.analysisContext.getCurrentRowAnalysisResult());
            }
        } else {
            List content = this.converter((List)event.getData());
            this.analysisContext.setCurrentRowAnalysisResult(content);
            if(this.listeners.size() == 1) {
                this.analysisContext.setCurrentRowAnalysisResult(content);
            }

            Iterator var3 = this.listeners.entrySet().iterator();

            while(var3.hasNext()) {
                Entry entry = (Entry)var3.next();
                ((AnalysisEventListener)entry.getValue()).invoke(this.analysisContext.getCurrentRowAnalysisResult(), this.analysisContext);
            }
        }

    }

    private List<String> converter(List<String> data) {
        ArrayList list = new ArrayList();
        if(data != null) {
            Iterator var3 = data.iterator();

            while(var3.hasNext()) {
                String str = (String)var3.next();
                list.add(TypeUtil.formatFloat(str));
            }
        }

        return list;
    }
}
