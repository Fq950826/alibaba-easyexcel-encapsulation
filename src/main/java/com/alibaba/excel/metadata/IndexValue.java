//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.metadata;

public class IndexValue {
    private String v_index;
    private String v_value;

    public IndexValue(String v_index, String v_value) {
        this.v_index = v_index;
        this.v_value = v_value;
    }

    public String getV_index() {
        return this.v_index;
    }

    public void setV_index(String v_index) {
        this.v_index = v_index;
    }

    public String getV_value() {
        return this.v_value;
    }

    public void setV_value(String v_value) {
        this.v_value = v_value;
    }

    public String toString() {
        return "IndexValue [v_index=" + this.v_index + ", v_value=" + this.v_value + "]";
    }
}
