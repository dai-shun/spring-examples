package com.daishun.springmybatissimpleexample.common.utils;

import java.io.File;

/**
 * @author daishun
 * @since 2019/8/2
 */
public final class FileUtils extends org.apache.commons.io.FileUtils {

    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }

    public static String getUserDir(){
        return System.getProperty("user.dir");
    }

    private FileUtils(){

    }
}
