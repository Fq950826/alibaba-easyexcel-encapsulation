//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.util;

public class PositionUtils {
    public PositionUtils() {
    }

    public static int getRow(String currentCellIndex) {
        int row = 0;
        if(currentCellIndex != null) {
            String rowStr = currentCellIndex.replaceAll("[A-Z]", "").replaceAll("[a-z]", "");
            row = Integer.parseInt(rowStr) - 1;
        }

        return row;
    }

    public static int getCol(String currentCellIndex) {
        int col = 0;
        if(currentCellIndex != null) {
            char[] currentIndex = currentCellIndex.replaceAll("[0-9]", "").toCharArray();

            for(int i = 0; i < currentIndex.length; ++i) {
                col = (int)((double)col + (double)(currentIndex[i] - 64) * Math.pow(26.0D, (double)(currentIndex.length - i - 1)));
            }
        }

        return col - 1;
    }
}
