package com.fun.minigallery.download;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
class FileUtil {
    static File getExternalStorageDirectory() throws Exception {
        //Sdcard 不存在
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new FileNotFoundException("SDCard Unmounted");
        }
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("%s %s", path, "does not exist!"));
        }
        return file;
    }

    /**
     * 获得文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀或者null
     */
    static String getFileSuffix(final String fileName) {
        String suffix = null;
        if (fileName != null && !"".equals(fileName)) {
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex + 1 <= fileName.length()) {
                suffix = fileName.substring(dotIndex + 1, fileName.length());
            }
        }
        return suffix;
    }

    static boolean renameFile(File orginFile, String newFileName) {
        if (orginFile == null || !orginFile.exists()) {
            return false;
        }
        return orginFile.renameTo(new File(newFileName));
    }

    static String getFilePathWithoutSuffix(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        String suffix = getFileSuffix(fileName);
        if (TextUtils.isEmpty(suffix)) {
            return fileName;
        }
        suffix = "." + suffix;
        return fileName.substring(0, fileName.length() - suffix.length());
    }
}
