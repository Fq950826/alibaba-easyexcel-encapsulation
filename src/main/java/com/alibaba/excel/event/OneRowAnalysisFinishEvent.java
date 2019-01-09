//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.event;

import java.util.ArrayList;

public class OneRowAnalysisFinishEvent {
    private Object data;

    public OneRowAnalysisFinishEvent(Object content) {
        this.data = content;
    }

    public OneRowAnalysisFinishEvent(String[] content, int length) {
        if(content != null) {
            ArrayList ls = new ArrayList(length);

            for(int i = 0; i <= length; ++i) {
                ls.add(content[i]);
            }

            this.data = ls;
        }

    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
