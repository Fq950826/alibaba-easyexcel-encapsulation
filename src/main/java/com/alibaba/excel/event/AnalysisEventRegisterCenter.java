//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.event;

import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;

public interface AnalysisEventRegisterCenter {
    void appendLister(String var1, AnalysisEventListener var2);

    void notifyListeners(OneRowAnalysisFinishEvent var1);

    void cleanAllListeners();
}
