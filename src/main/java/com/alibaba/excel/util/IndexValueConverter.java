//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.util;

import com.alibaba.excel.metadata.IndexValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IndexValueConverter {
    public IndexValueConverter() {
    }

    public static List<String> converter(List<IndexValue> i_list) {
        ArrayList tem = new ArrayList();
        char[] start = new char[]{'@'};

        for(int j = 0; j < i_list.size(); ++j) {
            IndexValue currentIndexValue = (IndexValue)i_list.get(j);
            char[] currentIndex = currentIndexValue.getV_index().replaceAll("[0-9]", "").toCharArray();
            if(j > 0) {
                start = ((IndexValue)i_list.get(j - 1)).getV_index().replaceAll("[0-9]", "").toCharArray();
            }

            int deep = subtraction26(currentIndex, start);

            for(int k = 0; k < deep - 1; ++k) {
                tem.add((Object)null);
            }

            tem.add(currentIndexValue.getV_value());
        }

        return tem;
    }

    private static int subtraction26(char[] currentIndex, char[] beforeIndex) {
        int result = 0;
        Stack currentStack = new Stack();
        Stack berforStack = new Stack();

        int i;
        for(i = 0; i < currentIndex.length; ++i) {
            currentStack.push(Character.valueOf(currentIndex[i]));
        }

        for(i = 0; i < beforeIndex.length; ++i) {
            berforStack.push(Character.valueOf(beforeIndex[i]));
        }

        i = 0;

        for(char beforechar = 64; !currentStack.isEmpty(); beforechar = 64) {
            char currentChar = ((Character)currentStack.pop()).charValue();
            if(!berforStack.isEmpty()) {
                beforechar = ((Character)berforStack.pop()).charValue();
            }

            int n = currentChar - beforechar;
            if(n < 0) {
                n += 26;
                if(!currentStack.isEmpty()) {
                    char borrow = ((Character)currentStack.pop()).charValue();
                    char newBorrow = (char)(borrow - 1);
                    currentStack.push(Character.valueOf(newBorrow));
                }
            }

            result = (int)((double)result + (double)n * Math.pow(26.0D, (double)i));
            ++i;
        }

        return result;
    }
}
