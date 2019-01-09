//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.event;

import com.alibaba.excel.context.AnalysisContext;

public abstract class AnalysisEventListener<T> {
    public AnalysisEventListener() {
    }

    public abstract void invoke(T var1, AnalysisContext var2);

    public abstract void doAfterAllAnalysed(AnalysisContext var1);
}
