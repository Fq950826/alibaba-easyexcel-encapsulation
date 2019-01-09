//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis;

import com.alibaba.excel.metadata.Sheet;
import java.util.List;

public interface ExcelAnalyser {
    void analysis(Sheet var1);

    void analysis();

    List<Sheet> getSheets();
}
