//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.util;

import java.io.File;

public class POITempFile {
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final String POIFILES = "poifiles";

    public POITempFile() {
    }

    public static void createPOIFilesDirectory() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if(tmpDir == null) {
            throw new RuntimeException("Systems temporary directory not defined - set the -Djava.io.tmpdir jvm property!");
        } else {
            File directory = new File(tmpDir, "poifiles");
            if(!directory.exists()) {
                syncCreatePOIFilesDirectory(directory);
            }

        }
    }

    private static synchronized void syncCreatePOIFilesDirectory(File directory) {
        if(!directory.exists()) {
            directory.mkdirs();
        }

    }
}
