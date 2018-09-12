package com.fun.minigallery.download;

import java.io.File;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class DownloaderUtil {
    private static final String KEY_BREAK_POINT = "break_point";

    static final String SUFFIX_TMP = ".tmp";

    static String generateTaskId(String url) {
        return MD5Tool.getMD5(url);
    }

    static String generateDownloadPath(String taskId) {
        try {
            return FileUtil.getExternalStorageDirectory() + File.separator + "download" + File.separator + taskId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
